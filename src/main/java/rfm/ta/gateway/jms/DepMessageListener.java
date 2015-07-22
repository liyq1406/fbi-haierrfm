package rfm.ta.gateway.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import rfm.ta.gateway.dep.model.base.TiaXml;
import rfm.ta.gateway.dep.model.base.ToaXml;
import rfm.ta.view.deposit.DepAbstractTxnProcessor;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.HashMap;

/**
 * Created by XIANGYANG on 2015-7-20.
 */

@Component
public class DepMessageListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DepMessageListener.class);
    @Autowired
    @Qualifier(value = "jmsFipOutTemplate")
    private JmsTemplate jmsFipOutTemplate;

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
            TiaXml tiaXml = (TiaXml)objMsg.getObject();
            logger.info("��rfm����dep���ס�correlationID��" + correlationID + ",������:" + txnCode);
            WebApplicationContext springContext = ContextLoader.getCurrentWebApplicationContext();
            DepAbstractTxnProcessor cbsTxnProcessor = (DepAbstractTxnProcessor) springContext.getBean("depTxn" + txnCode + "Processor");
            ToaXml toa = cbsTxnProcessor.run(tiaXml);
            jmsFipOutTemplate.send(new ObjectMessageCreator(toa, correlationID, propertyMap));
        } catch (Exception e) {
            logger.error("[fip]��Ϣ�����쳣!", e);
        }
    }
}
