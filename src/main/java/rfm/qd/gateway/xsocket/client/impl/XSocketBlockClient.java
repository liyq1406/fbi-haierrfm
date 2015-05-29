package rfm.qd.gateway.xsocket.client.impl;

import rfm.qd.gateway.xsocket.client.ConnectClient;
import rfm.qd.gateway.xsocket.crypt.des.DesCrypter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.*;
import pub.platform.advance.utils.PropertyManager;

import java.io.IOException;
import java.nio.BufferUnderflowException;

/**
 * 客户端接收服务端信息
 *
 * @author zxb
 */
public class XSocketBlockClient extends ConnectClient implements IConnectHandler {

    private static final Logger logger = LoggerFactory.getLogger(XSocketBlockClient.class);
    private IBlockingConnection bc;   //  非阻塞连接
    private INonBlockingConnection nbc;
    private boolean isCrypt = false;
    private DesCrypter crypter;

    public XSocketBlockClient(String serverIP, int serverPort, int timeoutMills) throws IOException {
        super(serverIP, serverPort);
        nbc = new NonBlockingConnection(serverIP, serverPort, this);
        bc = new BlockingConnection(nbc);
        bc.setConnectionTimeoutMillis(timeoutMills);
        bc.setEncoding("GBK");
        bc.setAutoflush(true);  //  设置自动清空缓存
        if ("DES".equalsIgnoreCase(PropertyManager.getProperty("crypt.type"))) {
            try {
                crypter = DesCrypter.getInstance();
            } catch (Exception e) {
                logger.error("无法实例化加密类！", e.getMessage());
                throw new RuntimeException("无法实例化加密类！" + e.getMessage());
            }
            isCrypt = true;
        }
    }

    @Override
    public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
        String remoteName = nbc.getRemoteAddress().getHostName();
        logger.info("【本地客户端】与远程主机:" + remoteName + "建立连接。");
        return true;
    }

    /**
     * 发送并接收数据
     *
     * @param dataContent
     * @return
     * @throws java.io.IOException
     */
    public String sendDataUntilRcv(String dataContent) throws Exception {

        String toSendData = null;
        logger.info("【本地客户端】发送报文：" + dataContent);

        if (isCrypt) {
            // 加密
            String miContent = crypter.encrypt(dataContent);
            toSendData = miContent.getBytes().length + "\r\n" + miContent;
        } else {
            toSendData = dataContent.getBytes().length + "\r\n" + dataContent;
        }
        logger.info("【本地客户端】发送密文：" + toSendData);
        if (sendData(toSendData)) {
            int gramLength = Integer.parseInt(bc.readStringByDelimiter("\r\n"));
            logger.info("【本地客户端】接收报文内容长度：" + gramLength);


            String dataGram = null;
            if (isCrypt) {
                String strDataGram = "";
                while (StringUtils.isEmpty(strDataGram) ||
                        strDataGram.getBytes().length < gramLength) {
                    strDataGram += bc.readStringByLength(gramLength - strDataGram.getBytes().length);
                }
                dataGram = DesCrypter.getInstance().decrypt(strDataGram);
            } else {
                dataGram = bc.readStringByLength(gramLength);
            }
            logger.info("【本地客户端】接收报文内容：" + dataGram);
            return dataGram;
        }
        return null;
    }

    @Override
    public boolean sendData(String dataContent) throws IOException {
        if (bc == null || !bc.isOpen()) {
            throw new RuntimeException("未建立连接！");
        } else {
            bc.write(dataContent);
            bc.flush();
        }
        return true;
    }

    /**
     * 关闭客户端链接
     *
     * @return
     * @throws java.io.IOException
     */
    public boolean close() throws IOException {
        if (bc != null && bc.isOpen()) {
            bc.close();
            bc = null;
        }
        if (nbc != null && nbc.isOpen()) {
            nbc.close();
            nbc = null;
        }
        return true;
    }
}
