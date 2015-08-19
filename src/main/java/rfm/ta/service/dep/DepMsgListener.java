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
    @Qualifier(value = "jmsRfmOutTemplate")
    private JmsTemplate jmsRfmOutTemplate;

    @ManagedProperty(value = "#{taSbsService}")
    private TaSbsService taSbsService;

    @Autowired
    @ManagedProperty(value = "#{taFdcService}")
    private TaFdcService taFdcService;

    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @Override
    public void onMessage(Message message) {
        String txnCode = null;
        String correlationID = null;
        try {
            correlationID = message.getJMSCorrelationID();
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put("JMSX_CHANNELID", message.getStringProperty("JMSX_CHANNELID"));
            propertyMap.put("JMSX_APPID", message.getStringProperty("JMSX_APPID"));
            propertyMap.put("JMSX_SRCMSGFLAG", message.getStringProperty("JMSX_SRCMSGFLAG"));
            ObjectMessage objMsg = (ObjectMessage) message;
            TIA tiaTmp = (TIA)objMsg.getObject();
            TOA toaSbs;
            TOA toaFdc;
            txnCode = tiaTmp.getHeader().TX_CODE;
            if(EnuTaFdcTxCode.TRADE_2001.getCode().equals(txnCode)){
                /*01	????	    4	2001
                  02	??????	2
                  03	????	    6
                  04	??????    14
                  05	????    	30
                  06	????	    10	???????
                  07	????	    30
                  08	????	    30
                  09	???	        1	1_????*/
                Tia9902001 tia9902001=(Tia9902001)tiaTmp;
                tia9902001.header.CHANNEL_ID= ToolUtil.DEP_CHANNEL_ID_RFM;
                tia9902001.body.BANK_ID= EnuTaBankId.BANK_HAIER.getCode();        // 02   ??????   2
                tia9902001.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();        // 03   ????       6
                tia9902001.body.TX_DATE=ToolUtil.getStrLastUpdDate();              // 06   ????       10  ???????
                tia9902001.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode();  // 09   ???         1   1_????
                toaFdc=taFdcService.sendAndRecvRealTimeTxn9902001(tia9902001);
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
            }else{// ??
                sendAndRecvSBSAndFDC(tiaTmp,correlationID,propertyMap);
            }
        } catch (Exception e) {
            logger.error("[fip]??????!", e);
        }
    }

    /*??????????*/
    public Boolean sendAndRecvSBSAndFDC(TIA tiaPara,String correlationID,HashMap<String, String> propertyMap) {
        try {
            // ?SBS??????
            Tia900010002 tia900010002Temp =(Tia900010002)tiaPara ;
            TaRsAccDtl taTxnFdcTemp=new TaRsAccDtl();
            taTxnFdcTemp.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnFdcTemp.setAccId(tia900010002Temp.body.ACC_ID);   // ????
            taTxnFdcTemp.setRecvAccId(tia900010002Temp.body.RECV_ACC_ID);   // ????
            taTxnFdcTemp.setTxAmt(tia900010002Temp.body.TX_AMT);   // ????
            taTxnFdcTemp.setReqSn(tia900010002Temp.header.REQ_SN); // ??????
            taTxnFdcTemp.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // ????
            taTxnFdcTemp.setUserId(tia900010002Temp.header.USER_ID);// ???
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taTxnFdcTemp);
            if(toaSbs!=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS???????
                    taTxnFdcTemp.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taTxnFdcTemp);
                    TOA toaFdc;
                    // ??????????????
                    if(EnuTaFdcTxCode.TRADE_2202.getCode().equals(taTxnFdcTemp.getTxCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902002(tia900010002Temp);
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                    }else if(EnuTaFdcTxCode.TRADE_2211.getCode().equals(taTxnFdcTemp.getTxCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902011(tia900010002Temp);
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                    }
                } else { // SBS???????
                    taAccDetlService.deleteRecord(taTxnFdcTemp.getPkId());
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            logger.error("??????????,", e);
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
