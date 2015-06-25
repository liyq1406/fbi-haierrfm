package rfm.ta.gateway.sbs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.*;
import rfm.ta.gateway.xsocket.client.ConnectClient;

import java.io.IOException;
import java.nio.BufferUnderflowException;

/**
 * �ͻ��˽��շ������Ϣ
 *
 * @author zxb
 */
public class CbusSocketClient extends ConnectClient implements IConnectHandler {

    private static final Logger logger = LoggerFactory.getLogger(CbusSocketClient.class);

    private IBlockingConnection blockingConnection;

    public CbusSocketClient(String serverIP, int serverPort, long timeoutMills) throws IOException {
        super(serverIP, serverPort);
        INonBlockingConnection nonBlockingConnection = new NonBlockingConnection(serverIP, serverPort, this);
        blockingConnection = new BlockingConnection(nonBlockingConnection);
        blockingConnection.setConnectionTimeoutMillis(timeoutMills);
        blockingConnection.setEncoding("GBK");
        blockingConnection.setAutoflush(true);  //  �����Զ���ջ���
    }

    @Override
    public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
        logger.info("�����ؿͻ��ˡ���Զ������:" + serverIP + "�������ӡ�");
        return true;
    }

    public byte[] sendDataUntilRcv(byte[] datagram) throws Exception {
        byte[] rtnBytes = null;
        if (sendData(datagram)) {
            int garamLength = Integer.parseInt(blockingConnection.readStringByLength(6).trim());
            logger.info("�����ؿͻ��ˡ����ձ��ĳ��ȣ�" + garamLength);
            rtnBytes = blockingConnection.readBytesByLength(garamLength);
        }
        return rtnBytes;
    }

    public boolean sendData(byte[] dataContent) throws IOException {
        if (blockingConnection == null || !blockingConnection.isOpen()) {
            throw new RuntimeException("δ�������ӣ�");
        } else {
            blockingConnection.write(dataContent);
            blockingConnection.flush();
        }
        return true;
    }

    @Override
    public boolean sendData(String dataContent) throws IOException {
        return false;
    }

    @Override
    public String sendDataUntilRcv(String datagram) throws Exception {
        return null;
    }

    /**
     * �رտͻ�������
     *
     * @return
     * @throws java.io.IOException
     */
    public boolean close() throws IOException {
        if (blockingConnection != null && blockingConnection.isOpen()) {
            blockingConnection.close();
            blockingConnection = null;
        }
        return true;
    }

    public IBlockingConnection getBlockingConnection() {
        return blockingConnection;
    }

    public void setBlockingConnection(IBlockingConnection blockingConnection) {
        this.blockingConnection = blockingConnection;
    }

}
