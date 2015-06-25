package rfm.ta.gateway.sbs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.connection.*;
import rfm.ta.gateway.xsocket.client.ConnectClient;

import java.io.IOException;
import java.nio.BufferUnderflowException;

/**
 * 客户端接收服务端信息
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
        blockingConnection.setAutoflush(true);  //  设置自动清空缓存
    }

    @Override
    public boolean onConnect(INonBlockingConnection nbc) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
        logger.info("【本地客户端】与远程主机:" + serverIP + "建立连接。");
        return true;
    }

    public byte[] sendDataUntilRcv(byte[] datagram) throws Exception {
        byte[] rtnBytes = null;
        if (sendData(datagram)) {
            int garamLength = Integer.parseInt(blockingConnection.readStringByLength(6).trim());
            logger.info("【本地客户端】接收报文长度：" + garamLength);
            rtnBytes = blockingConnection.readBytesByLength(garamLength);
        }
        return rtnBytes;
    }

    public boolean sendData(byte[] dataContent) throws IOException {
        if (blockingConnection == null || !blockingConnection.isOpen()) {
            throw new RuntimeException("未建立连接！");
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
     * 关闭客户端链接
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
