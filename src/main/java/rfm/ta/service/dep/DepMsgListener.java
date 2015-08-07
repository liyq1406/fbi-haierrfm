package rfm.ta.service.dep;

import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import rfm.ta.common.enums.EnuTaTxCode;
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

    @ManagedProperty(value = "#{taPaymentService}")
    private TaPaymentService taPaymentService;

    @Override
    public void onMessage(Message message) {
        String txnCode = null;
        String correlationID = null;
        try {
            correlationID = message.getJMSCorrelationID();
            txnCode = message.getStringProperty("JMSX_BIZID");
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put("JMSX_CHANNELID", message.getStringProperty("JMSX_CHANNELID"));
            propertyMap.put("JMSX_APPID", message.getStringProperty("JMSX_APPID"));
            propertyMap.put("JMSX_BIZID", txnCode);
            propertyMap.put("JMSX_SRCMSGFLAG", message.getStringProperty("JMSX_SRCMSGFLAG"));
            ObjectMessage objMsg = (ObjectMessage) message;
            TIA tiaTmp = (TIA)objMsg.getObject();
            TOA toaSbs;
            TOA toaFdc;
            if(EnuTaTxCode.TRADE_2001.getCode().equals(txnCode)){
                toaFdc=taPaymentService.sendAndRecvRealTimeTxn9902001(tiaTmp);
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
            }else if(EnuTaTxCode.TRADE_2002.getCode().equals(txnCode)){
                toaSbs=taPaymentService.sendAndRecvRealTimeTxn900012002(tiaTmp);
                if(toaSbs!=null) {
                    toaFdc = taPaymentService.sendAndRecvRealTimeTxn9902002(tiaTmp);
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                }
            }else if(EnuTaTxCode.TRADE_2011.getCode().equals(txnCode)){
                toaSbs=taPaymentService.sendAndRecvRealTimeTxn900012011(tiaTmp);
                if(toaSbs!=null) {
                    toaFdc = taPaymentService.sendAndRecvRealTimeTxn9902011(tiaTmp);
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                }
            }
        } catch (Exception e) {
            logger.error("[fip]消息处理异常!", e);
        }
    }

    public TaPaymentService getTaPaymentService() {
        return taPaymentService;
    }

    public void setTaPaymentService(TaPaymentService taPaymentService) {
        this.taPaymentService = taPaymentService;
    }
}
