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
 * ��������ݴ�����
 *
 * @author zxb
 */
@Component
public class ServerHandler implements IServerHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);
    @Autowired
    private ServerMessageService serverMessageService;

    /**
     * ���ӵĳɹ�ʱ�Ĳ���
     */
    @Override
    public boolean onConnect(INonBlockingConnection nbc) throws IOException,
            BufferUnderflowException {
        String remoteName = nbc.getRemoteAddress().getHostName();
        logger.info("�����ط���ˡ�Զ������: " + remoteName + "�뱾�������������ӣ�");
        return true;
    }

    /**
     * ���ӶϿ�ʱ�Ĳ���
     */
    @Override
    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
        logger.info("�����ط���ˡ�Զ�������뱾�������Ͽ����ӣ�");
        return true;
    }

    public boolean onData(INonBlockingConnection connection) throws IOException {

        int dataLength = 0;

        dataLength = Integer.parseInt(connection.readStringByDelimiter("\r\n"));
        logger.info("�����ط���ˡ����������ĳ��ȣ�" + dataLength);

        connection.setHandler(new ContentHandler(this, serverMessageService, dataLength));

        return true;
    }


    /**
     * ������ʱ�Ĵ����¼�
     */
    @Override
    public boolean onIdleTimeout(INonBlockingConnection connection) throws IOException {
        logger.error("�����ط���ˡ�Idle ��ʱ��");
        return true;
    }

    /**
     * ���ӳ�ʱ�����¼�
     */
    @Override
    public boolean onConnectionTimeout(INonBlockingConnection connection) throws IOException {
        logger.error("�����ؿͻ��ˡ���Զ���������ӳ�ʱ��");
        return true;
    }

    @Override
    public boolean onConnectException(INonBlockingConnection iNonBlockingConnection, IOException e) throws IOException {
        logger.error("�����ؿͻ��ˡ���Զ���������ӷ����쳣��");
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
            logger.info("�����ط���ˡ�������������:" + encrypedStr);

            String datagram = null;
            try {
                datagram = DesCrypter.getInstance().decrypt(encrypedStr);
                logger.info("�����ط���ˡ����ձ�������:" + datagram);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("�����ط���ˡ����յ����Ľ����쳣��", e.getMessage());
                throw new RuntimeException("�����ط���ˡ����յ����Ľ����쳣��");
            }
            // ������յ��ı��ģ���������Ӧ����
            String responseMsg = serverMessageService.handleMessage(datagram);
            logger.info("�����ط���ˡ����ͱ�������:" + responseMsg);
            String miStr = null;
            try {
                miStr = DesCrypter.getInstance().encrypt(responseMsg);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("�����ط���ˡ��������ļ����쳣��", e.getMessage());
                throw new RuntimeException("�����ط���ˡ��������ļ����쳣��");
            }
            responseMsg = miStr.getBytes().length + "\r\n" + miStr;
            logger.info("�����ط���ˡ�������������:" + responseMsg);
            nbc.write(responseMsg, "GBK");
            nbc.flush();
        }
        return true;
    }
}
