package rfm.ta.service.dep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.stereotype.Service;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.gateway.dep.model.base.TIA;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * DEP���ݽ���ƽ̨�ӿ�
 * ��������ͨ�ý��׽ӿ�
 * User: zhanrui
 * Date: 11-8-24
 * Time: ����7:17
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DepService {
    private static final Logger logger = LoggerFactory.getLogger(DepService.class);

    private static String DEP_BIZID = PropertyManager.getProperty("app_id");
    private static String DEP_USERNAME = PropertyManager.getProperty("jms.username");
    private static String DEP_PWD = PropertyManager.getProperty("jms.password");

    @Resource
    private JmsTemplate jmsSendTemplate;

    @Resource
    private JmsTemplate jmsRecvTemplate;

    /**
     * ͨ��MQ �� DEP������Ϣ (ͨ�ý��׽ӿ�)
     * @param channelId
     * @param msgtxt
     * @return
     * @throws javax.jms.JMSException
     */
    public String sendDepMessage(final String channelId, final TIA msgtxt) throws JMSException {
        TextMessage msg = (TextMessage) jmsSendTemplate.execute(new ProducerCallback<Object>() {
            public Object doInJms(Session session, MessageProducer producer) throws JMSException {
                ObjectMessage msg = session.createObjectMessage(msgtxt);
                msg.setStringProperty("JMSX_CHANNELID", channelId);
                msg.setStringProperty("JMSX_APPID", "HAIERRFM");
                msg.setStringProperty("JMSX_BIZID", DEP_BIZID.toUpperCase());
                msg.setStringProperty("JMSX_USERID", DEP_USERNAME);
                msg.setStringProperty("JMSX_PASSWORD", DEP_PWD);
                producer.send(msg);
                return msg;
            }
        });
        String msgid = msg.getJMSMessageID();
        logger.info("MQ��Ϣ����, MSGID=" + msgid);
        logger.debug("MQ��Ϣ����, MSGID=" + msgid + "\n  ��������: \n" + msg.getText());
        return msgid;
    }

    public String recvDepMessage(String msgid) {
        Message msg = null;
        try {
            String filter = "JMSCorrelationID = '" + msgid + "'";
            msg = jmsRecvTemplate.receiveSelected(filter);

            //TODO ��ʱ����
            if (msg != null) {
                if (msg instanceof TextMessage) {
                    logger.info("������Ϣ:" + ((TextMessage) msg).getText());
                    return ((TextMessage) msg).getText();
                } else {
                    throw new RuntimeException("��Ϣ���ʹ���");
                }
            } else {
                logger.info("��ʱ");
                throw new RuntimeException("���ձ��ĳ�ʱ.");
            }
        } catch (Exception e) {
            logger.error("������Ϣʧ�ܡ�", e);
            throw new RuntimeException("������Ϣʧ�ܡ�" + e.getMessage());
        }
    }

}
