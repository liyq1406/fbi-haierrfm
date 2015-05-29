package rfm.qd.gateway.xsocket.server.impl;

import rfm.qd.gateway.service.impl.ServerMessageService;
import rfm.qd.gateway.xsocket.crypt.des.DesCrypter;
import rfm.qd.gateway.xsocket.server.IServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;

import java.io.IOException;
import java.nio.BufferUnderflowException;

/**
 * 服务端数据处理类
 *
 * @author zxb
 */
@Component
public class ServerHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    @Autowired
    private ServerMessageService serverMessageService;

    /**
     * 连接的成功时的操作
     */
    @Override
    public boolean onConnect(INonBlockingConnection nbc) throws IOException,
            BufferUnderflowException {
        String remoteName = nbc.getRemoteAddress().getHostName();
        logger.info("【本地服务端】远程主机: " + remoteName + "与本地主机建立连接！");
        return true;
    }

    /**
     * 连接断开时的操作
     */
    @Override
    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
        logger.info("【本地服务端】远程主机与本地主机断开连接！");
        return true;
    }

    public boolean onData(INonBlockingConnection connection) throws IOException {

        int dataLength = 0;

        dataLength = Integer.parseInt(connection.readStringByDelimiter("\r\n"));
        logger.info("【本地服务端】待接收密文长度：" + dataLength);

        connection.setHandler(new ContentHandler(this, serverMessageService, dataLength));

        return true;
    }


    /**
     * 请求处理超时的处理事件
     */
    @Override
    public boolean onIdleTimeout(INonBlockingConnection connection) throws IOException {
        logger.error("【本地服务端】Idle 超时。");
        return true;
    }

    /**
     * 连接超时处理事件
     */
    @Override
    public boolean onConnectionTimeout(INonBlockingConnection connection) throws IOException {
        logger.error("【本地客户端】与远程主机连接超时。");
        return true;
    }

    @Override
    public boolean onConnectException(INonBlockingConnection iNonBlockingConnection, IOException e) throws IOException {
        logger.error("【本地客户端】与远程主机连接发生异常。");
        return true;
    }

    public ServerMessageService getServerMessageService() {
        return serverMessageService;
    }

    public void setServerMessageService(ServerMessageService serverMessageService) {
        this.serverMessageService = serverMessageService;
    }
}

class ContentHandler implements IDataHandler {
    private static Logger logger = LoggerFactory.getLogger(ContentHandler.class);
    private StringBuilder encrypedStrBuilder = new StringBuilder();
    private ServerMessageService serverMessageService;
    private int remaining = 0;
    private ServerHandler hdl = null;

    public ContentHandler(ServerHandler hdl, ServerMessageService serverMessageService, int dataLength) {
        this.hdl = hdl;
        remaining = dataLength;
        this.serverMessageService = serverMessageService;
    }

    public boolean onData(INonBlockingConnection nbc) throws IOException {
        int available = nbc.available();

        int lengthToRead = remaining;
        if (available < remaining) {
            lengthToRead = available;
        }

        String buffers = nbc.readStringByLength(lengthToRead);
        encrypedStrBuilder.append(buffers);
        remaining -= lengthToRead;

        if (remaining == 0) {
            String encrypedStr = encrypedStrBuilder.toString();
            nbc.setAttachment(hdl);
            logger.info("【本地服务端】接收密文内容:" + encrypedStr);

            String datagram = null;
            try {
                datagram = DesCrypter.getInstance().decrypt(encrypedStr);
                logger.info("【本地服务端】接收报文内容:" + datagram);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("【本地服务端】接收到密文解码异常！", e.getMessage());
                throw new RuntimeException("【本地服务端】接收到密文解码异常！");
            }
            // 处理接收到的报文，并生成响应报文
            String responseMsg = serverMessageService.handleMessage(datagram);
            logger.info("【本地服务端】发送报文内容:" + responseMsg);
            String miStr = null;
            try {
                miStr = DesCrypter.getInstance().encrypt(responseMsg);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("【本地服务端】发送密文加密异常！", e.getMessage());
                throw new RuntimeException("【本地服务端】发送密文加密异常！");
            }
            responseMsg = miStr.getBytes().length + "\r\n" + miStr;
            logger.info("【本地服务端】发送密文内容:" + responseMsg);
            nbc.write(responseMsg, "GBK");
            nbc.flush();
        }
        return true;
    }
}
