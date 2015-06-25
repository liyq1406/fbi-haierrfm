package rfm.ta.gateway.xsocket.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.connection.INonBlockingConnection;
import rfm.ta.gateway.service.IMessageService;
import rfm.ta.gateway.service.impl.ClientMessageService;
import rfm.ta.gateway.xsocket.client.IClientHandler;

import java.io.IOException;
import java.nio.BufferUnderflowException;

/**
 * �ͻ��˶����ݴ�����
 *
 * @author zxb
 */
@Deprecated
public class ClientHandler implements IClientHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private IMessageService messageService = new ClientMessageService();

    /**
     * ���ӵĳɹ�ʱ�Ĳ���
     */
    @Override
    public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException {
        String remoteName = nbc.getRemoteAddress().getHostName();
        logger.info("�����ؿͻ��ˡ���Զ������:" + remoteName + "�������ӡ�");
        return true;
    }

    /**
     * ���յ�����ʱ��Ĵ���
     */
    @Override
    public boolean onData(INonBlockingConnection nbc) throws IOException, BufferUnderflowException {
        String dataContent = null;
        dataContent = nbc.readStringByDelimiter("\r\n");
        logger.info("�����ؿͻ��ˡ�ClientHandler���յ�����:" + dataContent);
        messageService.handleMessage(dataContent);
        return true;
    }

    /**
     * ���ӶϿ�ʱ�Ĳ���
     */
    @Override
    public boolean onDisconnect(INonBlockingConnection nbc) throws IOException {
        // TODO ----------------------------------

        logger.info("�����ؿͻ��ˡ���Զ�������Ͽ������ӡ�");

        return true;
    }

    @Override
    public boolean onIdleTimeout(INonBlockingConnection iNonBlockingConnection) throws IOException {
        logger.error("�����ؿͻ��ˡ���Զ�������������ӳ�ʱ��");
        return true;
    }

    @Override
    public boolean onConnectionTimeout(INonBlockingConnection iNonBlockingConnection) throws IOException {
        logger.error("�����ؿͻ��ˡ���Զ���������ӳ�ʱ��");
        return false;
    }


    @Override
    public boolean onConnectException(INonBlockingConnection iNonBlockingConnection, IOException e) throws IOException {
        logger.error("�����ؿͻ��ˡ���Զ���������ӷ����쳣��");
        return false;
    }
}
