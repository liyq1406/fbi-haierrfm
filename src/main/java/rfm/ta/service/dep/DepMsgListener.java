package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.Tia900010002;
import org.fbi.dep.model.txn.Tia9902001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.service.biz.acc.TaAccDetlService;

import javax.faces.bean.ManagedProperty;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.HashMap;

@Component
public class DepMsgListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DepMsgListener.class);
    @Autowired
    private JmsTemplate jmsRfmOutTemplate;

    @Autowired
    private TaSbsService taSbsService;

    @Autowired
    private TaFdcService taFdcService;

    @Autowired
    private TaAccDetlService taAccDetlService;

    @Override
    public void onMessage(Message message) {
        try {
            String correlationID = message.getJMSCorrelationID();
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put("JMSX_CHANNELID", message.getStringProperty("JMSX_CHANNELID"));
            propertyMap.put("JMSX_APPID", message.getStringProperty("JMSX_APPID"));
            propertyMap.put("JMSX_SRCMSGFLAG", message.getStringProperty("JMSX_SRCMSGFLAG"));
            ObjectMessage objMsg = (ObjectMessage) message;
            TIA tiaTmp = (TIA)objMsg.getObject();
            TOA toaFdc;
            String txnCode = tiaTmp.getHeader().TX_CODE;
            tiaTmp.getHeader().REQ_SN=ToolUtil.getStrAppReqSn_Back();// 网银传过来的流水号是32位UUID
            if(EnuTaFdcTxCode.TRADE_2001.getCode().equals(txnCode)){
                /*01	交易代码	    4	2001
                  02	监管银行代码	2
                  03	城市代码	    6
                  04	交存申请编号    14
                  05	验证流水    	30
                  06	验证日期	    10	送系统日期即可
                  07	验证网点	    30
                  08	验证人员	    30
                  09	发起方	        1	1_监管银行*/
                Tia9902001 tia9902001=(Tia9902001)tiaTmp;
                tia9902001.header.CHANNEL_ID= ToolUtil.DEP_CHANNEL_ID_RFM;
                tia9902001.body.SPVSN_BANK_ID= EnuTaBankId.BANK_HAIER.getCode();        // 02   监管银行代码   2
                tia9902001.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();        // 03   城市代码       6
                tia9902001.body.TX_DATE=ToolUtil.getStrLastUpdDate();              // 06   验证日期       10  送系统日期即可
                tia9902001.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode();  // 09   发起方         1   1_监管银行
                toaFdc=taFdcService.sendAndRecvRealTimeTxn9902001(tia9902001);
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
            }else{// 记账
                sendAndRecvSBSAndFDC(tiaTmp,correlationID,propertyMap);
            }
        } catch (Exception e) {
            logger.error("[fip]消息处理异常!", e);
        }
    }

    /*验证后立即交存记账用*/
    public Boolean sendAndRecvSBSAndFDC(TIA tiaPara,String correlationID,HashMap<String, String> propertyMap) {
        try {
            // 往SBS发送记账信息
            Tia900010002 tia900010002Temp =(Tia900010002)tiaPara ;
            TaRsAccDtl taTxnFdcTemp=new TaRsAccDtl();
            taTxnFdcTemp.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnFdcTemp.setSpvsnAccId(tia900010002Temp.body.SPVSN_ACC_ID);   // 付款账号
            taTxnFdcTemp.setGerlAccId(tia900010002Temp.body.GERL_ACC_ID);   // 收款账号
            taTxnFdcTemp.setTxAmt(tia900010002Temp.body.TX_AMT);   // 交易金额
            taTxnFdcTemp.setReqSn(tia900010002Temp.header.REQ_SN); // 外围系统流水
            taTxnFdcTemp.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // 交易日期
            taTxnFdcTemp.setUserId(tia900010002Temp.header.USER_ID);// 柜员号
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taTxnFdcTemp);
            if(toaSbs!=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                    taTxnFdcTemp.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taTxnFdcTemp.setLastUpdBy(taTxnFdcTemp.getUserId());
                    taAccDetlService.updateRecord(taTxnFdcTemp);
                    TOA toaFdc;
                    // 往泰安房地产中心发送记账信息
                    if(EnuTaFdcTxCode.TRADE_2202.getCode().equals(taTxnFdcTemp.getTxCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902002(tia900010002Temp);
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                    }else if(EnuTaFdcTxCode.TRADE_2211.getCode().equals(taTxnFdcTemp.getTxCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902011(tia900010002Temp);
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                    }
                } else { // SBS记账失败的处理
                    logger.error("返还异常:返回码（"+toaSbs.getHeader().RETURN_CODE+");返回信息（"+toaSbs.getHeader().RETURN_MSG+")");
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaSbs, correlationID, propertyMap));
                    taAccDetlService.deleteRecord(taTxnFdcTemp);
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            logger.error("验证后立即交存记账用，", e);
            MessageUtil.addError(e.getMessage());
            return false;
        }
    }

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
    }

    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
    }

    public JmsTemplate getJmsRfmOutTemplate() {
        return jmsRfmOutTemplate;
    }

    public void setJmsRfmOutTemplate(JmsTemplate jmsRfmOutTemplate) {
        this.jmsRfmOutTemplate = jmsRfmOutTemplate;
    }

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }
}
