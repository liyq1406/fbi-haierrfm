package rfm.ta.gateway.hfnb.server;

import com.thoughtworks.xstream.converters.ConversionException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.utils.MD5Helper;
import rfm.ta.gateway.hfnb.model.base.TaHfnbToaXmlErr;
import rfm.ta.gateway.hfnb.enums.TxnRtnCode;
import rfm.ta.view.deposit.AbstractTxnProcessor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by XIANGYANG on 2015-7-1.
 * ��������ݴ�������
 */

public class ServerChannelHandler extends SimpleChannelHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerChannelHandler.class);


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent me) {
        String rtnmac = null;
        String txnCode = null;
        String userId = null;
        String reqsn = null;
        String version = null;
        String txnDate = null;
        String txnTime = null;
        String userKey = null;
        String rtnXml = null;
        try {

            byte[] dataBytes = (byte[]) me.getMessage();
            logger.info("��̩�������ʽ���ƽ̨-���յ����ġ�" + new String(dataBytes));

            //�ͻ��������ĸ�ʽΪ(Encrypt-MD5:32�ֽ�MD5ֵ+�س�����+XML������)
            //1.����mac
            int macLength = 32;
            byte[] macBytes = new byte[macLength];
            System.arraycopy(dataBytes, 12, macBytes, 0, macBytes.length);
            String mac = new String(macBytes, "GBK");

            //2.����XML������info����
            byte[] bodyBytes = new byte[dataBytes.length - macLength - 13];
            System.arraycopy(dataBytes, macLength + 13, bodyBytes, 0, bodyBytes.length);
            String xmlMsgData = new String(bodyBytes, "GBK");

            SAXReader saxReader = new SAXReader();
            InputStream inputStream = new ByteArrayInputStream(bodyBytes);
            Document document = saxReader.read(inputStream);
            Element root = document.getRootElement();
            for (Iterator i = root.elementIterator(); i.hasNext(); ) {
                Element info = (Element) i.next();
                if ("info".equals(info.getName())) {
                    for (Iterator j = info.elementIterator(); j.hasNext(); ) {
                        Element node = (Element) j.next();
                        if ("txncode".equals(node.getName())) {
                            txnCode = node.getText();
                        }
                        if ("userid".equals(node.getName())) {
                            userId = node.getText();
                        }
                        if ("reqsn".equals(node.getName())) {
                            reqsn = node.getText();
                        }
                        if ("version".equals(node.getName())) {
                            version = node.getText();
                        }
                        if ("txndate".equals(node.getName())) {
                            txnDate = node.getText();
                        }
                        if ("txntime".equals(node.getName())) {
                            txnTime = node.getText();
                        }
                    }
                    break;
                }
            }

            //3.MD5У��
            userKey = PropertyManager.getProperty("ta.hfnb.userkey." + userId);
            String md5 = MD5Helper.getMD5String(userId + userKey + xmlMsgData);
            // ��֤ʧ�� ������֤ʧ����Ϣ
            if (!md5.equalsIgnoreCase(mac)) {
                logger.info("�û���ʶ��" + userId + " " + userKey + " ����ʱ��:" + txnDate + " " + txnTime);
                logger.info("MACУ��ʧ��[�����]MD5:" + md5 + "[�ͻ���]MAC:" + mac);
                throw new RuntimeException(TxnRtnCode.MSG_VERIFY_MAC_ILLEGAL.toRtnMsg());
            } else {
                logger.info("MACУ��ɹ�,�û���ʶ��" + userId + " " + userKey + " ����ʱ��:" + txnDate + " " + txnTime);
            }

            Class txnClass = null;
            try {
                txnClass = Class.forName("rfm.ta.view.deposit.TaTxn" + txnCode + "Action");
            } catch (ClassNotFoundException e) {
                txnClass = null;
                throw new RuntimeException(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.getCode()+"|"+TxnRtnCode.MSG_ANALYSIS_ILLEGAL.getTitle());
            }
            if (txnClass != null) {
                AbstractTxnProcessor processor = (AbstractTxnProcessor) txnClass.newInstance();
                rtnXml = processor.process(userId, xmlMsgData);
            }
            rtnmac = MD5Helper.getMD5String(userId + userKey + rtnXml);
            logger.info("��SbsHttpRouteBuilder ���ͱ��ġ�" + new String((rtnmac + "\n" + rtnXml).getBytes("GBK")));
            me.getChannel().write((rtnmac + "\n" + rtnXml).getBytes("GBK"));
        } catch (Exception e) {
            //  �����쳣��Ϣ
            TaHfnbToaXmlErr errXmlHttp = new TaHfnbToaXmlErr();
            errXmlHttp.getInfo().setTxncode(txnCode);
            errXmlHttp.getInfo().setUserid(userId);
            errXmlHttp.getInfo().setReqsn(reqsn);
            errXmlHttp.getInfo().setVersion(version);
            errXmlHttp.getInfo().setTxndate(txnDate);
            errXmlHttp.getInfo().setTxntime(txnTime);

            if (txnCode == null || ConversionException.class.equals(e.getClass())) {
                logger.error("���Ľ���ʧ��", e);
                errXmlHttp.getInfo().setRtncode(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.getCode());
                errXmlHttp.getInfo().setRtnmsg(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.getTitle());
                errXmlHttp.getBody().setRtncode(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.getCode());
                errXmlHttp.getBody().setRtnmsg(TxnRtnCode.MSG_ANALYSIS_ILLEGAL.getTitle());
            } else {
                String exmsg = e.getMessage();
                logger.error("�����쳣", e);
                if (exmsg == null) {
                    exmsg = TxnRtnCode.SERVER_EXCEPTION.getCode() + "|" + TxnRtnCode.SERVER_EXCEPTION.getTitle();
                } else if (!exmsg.contains("|")) {
                    exmsg = TxnRtnCode.SERVER_EXCEPTION.getCode() + "|" + exmsg;
                }
                String errmsg[] = exmsg.split("\\|");
                errXmlHttp.getInfo().setRtncode(errmsg[0]);
                errXmlHttp.getInfo().setRtnmsg(errmsg[1]);
                errXmlHttp.getBody().setRtncode(errmsg[0]);
                errXmlHttp.getBody().setRtnmsg(errmsg[1]);
            }
            rtnXml = errXmlHttp.toString();
            rtnmac = MD5Helper.getMD5String(rtnXml + userId + userKey);
            byte[] rtnBytes=(rtnmac + "\n" + rtnXml).getBytes();
            logger.info("��̩�������ʽ���ƽ̨-���ͱ��ġ�" + new String(rtnBytes));
            me.getChannel().write(rtnBytes);
        } finally {
            me.getChannel().disconnect();
            me.getChannel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
