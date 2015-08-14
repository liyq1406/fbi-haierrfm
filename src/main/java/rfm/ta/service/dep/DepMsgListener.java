package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.Tia9902001;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import rfm.ta.common.enums.*;
import rfm.ta.service.account.TaPaymentService;

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

    @Autowired
    @ManagedProperty(value = "#{taPaymentService}")
    private TaPaymentService taPaymentService;

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
                /*01	���״���	    4	2001
                  02	������д���	2
                  03	���д���	    6
                  04	����������    14
                  05	��֤��ˮ    	30
                  06	��֤����	    10	��ϵͳ���ڼ���
                  07	��֤����	    30
                  08	��֤��Ա	    30
                  09	����	        1	1_�������*/
                Tia9902001 tia9902001=(Tia9902001)tiaTmp;
                tia9902001.header.CHANNEL_ID= ToolUtil.DEP_CHANNEL_ID_RFM;
                tia9902001.body.BANK_ID= EnuTaBankId.BANK_HAIER.getCode();        // 02   ������д���   2
                tia9902001.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();        // 03   ���д���       6
                tia9902001.body.TX_DATE=ToolUtil.getStrLastUpdDate();              // 06   ��֤����       10  ��ϵͳ���ڼ���
                tia9902001.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode();  // 09   ����         1   1_�������
                toaFdc=taPaymentService.sendAndRecvRealTimeTxn9902001(tia9902001);
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
            }else if(EnuTaFdcTxCode.TRADE_2002.getCode().equals(txnCode)){
                tiaTmp.getHeader().TX_CODE= EnuTaSbsTxCode.TRADE_0002.getCode();
                toaSbs=taPaymentService.sendAndRecvRealTimeTxn900012002(tiaTmp);
                if(toaSbs!=null) {
                    tiaTmp.getHeader().TX_CODE= txnCode;
                    toaFdc = taPaymentService.sendAndRecvRealTimeTxn9902002(tiaTmp);
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                }
            }else if(EnuTaFdcTxCode.TRADE_2011.getCode().equals(txnCode)){
                tiaTmp.getHeader().TX_CODE= EnuTaSbsTxCode.TRADE_0002.getCode();
                toaSbs=taPaymentService.sendAndRecvRealTimeTxn900012011(tiaTmp);
                if(toaSbs!=null) {
                    tiaTmp.getHeader().TX_CODE= txnCode;
                    toaFdc = taPaymentService.sendAndRecvRealTimeTxn9902011(tiaTmp);
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                }
            }
        } catch (Exception e) {
            logger.error("[fip]��Ϣ�����쳣!", e);
        }
    }

    public TaPaymentService getTaPaymentService() {
        return taPaymentService;
    }

    public void setTaPaymentService(TaPaymentService taPaymentService) {
        this.taPaymentService = taPaymentService;
    }
}
