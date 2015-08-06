package rfm.ta.service.dep;

import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;
import org.springframework.stereotype.Service;
import pub.platform.advance.utils.PropertyManager;
import org.fbi.dep.model.base.TIA;

import javax.annotation.Resource;
import javax.jms.*;

/**
 * DEP数据交换平台接口
 * 银联：走通用交易接口
 * User: zhanrui
 * Date: 11-8-24
 * Time: 上午7:17
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DepMsgSendAndRecv {
    private static final Logger logger = LoggerFactory.getLogger(DepMsgSendAndRecv.class);
    private static String DEP_APPID = PropertyManager.getProperty("app_id");
    private static String DEP_USERNAME = PropertyManager.getProperty("jms.username");
    private static String DEP_PWD = PropertyManager.getProperty("jms.password");

    @Resource
    private JmsTemplate jmsSendTemplate;

    @Resource
    private JmsTemplate jmsRecvTemplate;

    /**
     * 通过MQ 向 DEP发送消息 (通用交易接口)
     * @param tiaPara
     * @return
     * @throws javax.jms.JMSException
     */
    public String sendDepMessage(final TIA tiaPara) throws JMSException {
        ObjectMessage msg = (ObjectMessage) jmsSendTemplate.execute(new ProducerCallback<Object>() {
            public Object doInJms(Session session, MessageProducer producer) throws JMSException {
                ObjectMessage msg = session.createObjectMessage(tiaPara);
                msg.setStringProperty("JMSX_CHANNELID", tiaPara.getHeader().CHANNEL_ID);
                msg.setStringProperty("JMSX_APPID", DEP_APPID);
                msg.setStringProperty("JMSX_BIZID", tiaPara.getHeader().BIZ_ID);
                msg.setStringProperty("JMSX_USERID", DEP_USERNAME);
                msg.setStringProperty("JMSX_PASSWORD", DEP_PWD);
                producer.send(msg);
                return  msg;
            }
        });
        String msgid = msg.getJMSMessageID();
        logger.info("MQ消息发送, MSGID=" + msgid);
        logger.debug("MQ消息发送, MSGID=" + msgid + "\n  报文内容: \n" + msg.toString());
        return msgid;
    }

    public TOA recvDepMessage(String msgid) {
        Object msg = null;
        try {
            String filter = "JMSCorrelationID = '" + msgid + "'";
            msg = jmsRecvTemplate.receiveSelectedAndConvert(filter);

            //TODO 超时处理
            if (msg != null) {
                //logger.info("接收消息:" + ((ObjectMessage) msg));
                return ((TOA) msg);
            } else {
                logger.info("超时");
                throw new RuntimeException("接收报文超时.");
            }
        } catch (Exception e) {
            logger.error("接收消息失败。", e);
            throw new RuntimeException("接收消息失败。" + e.getMessage());
        }
    }

}
