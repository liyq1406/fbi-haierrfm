package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.Tia900010002;
import org.fbi.dep.model.txn.Tia9902001;
import org.fbi.dep.model.txn.Toa900010002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.RfmMessage;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.acc.TaAccDetlService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.List;

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
                tia9902001.body.SPVSN_BANK_ID= EnuTaBankId.BANK_HAIER.getCode(); // 02   监管银行代码   2
                tia9902001.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();        // 03   城市代码       6
                tia9902001.body.TX_DATE=ToolUtil.getStrLastUpdDate();               // 06   验证日期       10  送系统日期即可
                tia9902001.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode();   // 09   发起方         1   1_监管银行
                toaFdc=taFdcService.sendAndRecvRealTimeTxn9902001(tia9902001);
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
            }else if(EnuTaFdcTxCode.TRADE_2002.getCode().equals(txnCode)){// 记账
                TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
                Tia900010002 tia900010002Temp =(Tia900010002)tiaTmp;

                // 验证重复记账
                TaRsAccDtl taRsAccDtlTempQry = new TaRsAccDtl();
                taRsAccDtlTempQry.setBizId(tia900010002Temp.header.BIZ_ID);            // 业务编号
                taRsAccDtlTempQry.setTxCode(tia900010002Temp.header.TX_CODE);          // 交易号
                List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTempQry);
                if(taRsAccDtlList.size() == 1){
                    String actFlag = taRsAccDtlList.get(0).getActFlag();
                    if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())) {
                        Toa900010002 toa900010002 = new Toa900010002();
                        toa900010002.header.RETURN_CODE = "E001";
                        toa900010002.header.RETURN_MSG = RfmMessage.getProperty("Payment.E001");
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                        return;
                    }else{
                        taRsAccDtlTemp=taRsAccDtlList.get(0);
                    }
                } else {
                    taRsAccDtlTemp.setTxCode(tia900010002Temp.header.TX_CODE);                // 交易代码
                    taRsAccDtlTemp.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());          // 监管银行
                    taRsAccDtlTemp.setCityId(EnuTaCityId.CITY_TAIAN.getCode());               // 城市代码
                    taRsAccDtlTemp.setBizId(tia900010002Temp.header.BIZ_ID);                  // 交存申请编号
                    taRsAccDtlTemp.setTxAmt(tia900010002Temp.body.TX_AMT);                    // 交易金额
                    taRsAccDtlTemp.setSpvsnAccId(tia900010002Temp.body.SPVSN_ACC_ID);        // 监管账号
                    taRsAccDtlTemp.setGerlAccId(tia900010002Temp.body.GERL_ACC_ID);          // 一般账号
                    taRsAccDtlTemp.setStlType(EnuTaStlType.STL_TYPE02.getCode());             // 结算方式
                    taRsAccDtlTemp.setCheckId("");                                             // 支票号码
                    String reqsn = tiaTmp.getHeader().REQ_SN;
                    if(reqsn==null){
                        taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());                // 银行记账流水
                    }else
                    if(reqsn.length() > 30) {
                        reqsn = reqsn.substring(0, 30);
                    }
                    taRsAccDtlTemp.setReqSn(reqsn);                                             // 银行记账流水
                    taRsAccDtlTemp.setTxDate(ToolUtil.getStrLastUpdDate());                     // 交易日期
                    taRsAccDtlTemp.setBranchId(tia900010002Temp.body.BANK_BRANCH_ID);        // 记账网点
                    taRsAccDtlTemp.setUserId(tia900010002Temp.header.USER_ID);                // 记账人员
                    taRsAccDtlTemp.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());         // 发起方

                    taRsAccDtlTemp.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG0.getCode());// 删除标志
                    taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());              // 交易状态
                    taRsAccDtlTemp.setReturnCode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());
                    taAccDetlService.insertRecord(taRsAccDtlTemp);
                }
                sendAndRecvSBSAndFDC(taRsAccDtlTemp,correlationID,propertyMap);
            }else if(EnuTaFdcTxCode.TRADE_2011.getCode().equals(txnCode)){// 冲正
                TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
                Tia900010002 tia900010002Temp =(Tia900010002)tiaTmp;

                // 验证重复记账
                TaRsAccDtl taRsAccDtlTempQry = new TaRsAccDtl();
                taRsAccDtlTempQry.setBizId(tia900010002Temp.header.BIZ_ID);            // 业务编号
                taRsAccDtlTempQry.setTxCode(tia900010002Temp.header.TX_CODE);          // 交易号
                List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTempQry);
                if(taRsAccDtlList.size() == 1){
                    String actFlag = taRsAccDtlList.get(0).getActFlag();
                    if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())) { // 已经冲正成功的处理
                        Toa900010002 toa900010002 = new Toa900010002();
                        toa900010002.header.RETURN_CODE = "E001";
                        toa900010002.header.RETURN_MSG = RfmMessage.getProperty("Payment.E001");
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                        return;
                    }else{ // 已经冲正但是存在不明原因的失败的处理
                        taRsAccDtlTemp=taRsAccDtlList.get(0);
                    }
                } else { // 未进行过冲正的处理
                    taRsAccDtlTemp = new TaRsAccDtl();
                    taRsAccDtlTemp.setBizId(tia900010002Temp.header.BIZ_ID);
                    taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2002.getCode());
                    taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTemp);
                    if(taRsAccDtlList.size() == 1){ // 存在交存的处理
                        taRsAccDtlTemp = taRsAccDtlList.get(0);
                        // 与交存记账：收款账号和付款账号关系正好颠倒
                        taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2011.getCode());
                        String accId = taRsAccDtlTemp.getSpvsnAccId();
                        taRsAccDtlTemp.setSpvsnAccId(taRsAccDtlTemp.getGerlAccId());
                        taRsAccDtlTemp.setGerlAccId(accId);
                        taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                        taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());
                        taRsAccDtlTemp.setReturnCode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());

                        taAccDetlService.insertRecord(taRsAccDtlTemp);
                    } else { // 不存在交存的处理
                        Toa900010002 toa900010002 = new Toa900010002();
                        toa900010002.header.RETURN_CODE = "E002";
                        toa900010002.header.RETURN_MSG = RfmMessage.getProperty("Payment.E002");
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                        return;
                    }
                }
                sendAndRecvSBSAndFDC(taRsAccDtlTemp,correlationID,propertyMap);
            }
        } catch (Exception e) {
            logger.error("[DEP]消息处理异常!", e);
        }
    }

    /*验证后立即交存记账用*/
    public Boolean sendAndRecvSBSAndFDC(TaRsAccDtl taRsAccDtlPara,String correlationID,HashMap<String, String> propertyMap) {
        try {
            // 往SBS发送记账信息            ;
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlPara);
            if(toaSbs!=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                    taRsAccDtlPara.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlPara);
                    TOA toaFdc;
                    // 往泰安房地产中心发送记账信息
                    TaTxnFdc taTxnFdcTemp=new TaTxnFdc();
                    BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlPara);
                    if(taTxnFdcTemp.getTxCode().contains(EnuTaFdcTxCode.TRADE_2002.getCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902002(taTxnFdcTemp);
                        Toa900010002 toa900010002=new Toa900010002();
                        toa900010002.header.RETURN_CODE=toaFdc.getHeader().RETURN_CODE;
                        if(!EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900010002.header.RETURN_CODE)) {
                            toa900010002.header.RETURN_MSG = toaFdc.getHeader().RETURN_MSG;
                        }
                        toa900010002.header.REQ_SN=toaFdc.getHeader().REQ_SN;
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                    }else if(taTxnFdcTemp.getTxCode().contains(EnuTaFdcTxCode.TRADE_2011.getCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902011(taTxnFdcTemp);
                        Toa900010002 toa900010002=new Toa900010002();
                        toa900010002.header.RETURN_CODE=toaFdc.getHeader().RETURN_CODE;
                        if(!EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900010002.header.RETURN_CODE)) {
                            toa900010002.header.RETURN_MSG = toaFdc.getHeader().RETURN_MSG;
                        }
                        toa900010002.header.REQ_SN=toaFdc.getHeader().REQ_SN;
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                    }
                } else { // SBS记账失败的处理
                    logger.error("返还异常:返回码（" + toaSbs.getHeader().RETURN_CODE + ");返回信息（" + toaSbs.getHeader().RETURN_MSG + ")");
                    taAccDetlService.deleteRecord(taRsAccDtlPara.getPkId());
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaSbs, correlationID, propertyMap));
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
