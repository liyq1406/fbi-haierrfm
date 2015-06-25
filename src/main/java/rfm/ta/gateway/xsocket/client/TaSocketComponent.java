package rfm.ta.gateway.xsocket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rfm.ta.gateway.xsocket.client.impl.ClientFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-23
 * Time: 上午11:19
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TaSocketComponent {

    /**
     * 发送报文，返回接收到的响应报文
     * @param datagram
     * @return
     * @throws java.io.IOException
     */
    private Logger logger = LoggerFactory.getLogger(TaSocketComponent.class);

    public String sendAndRecvDataByBlockConn(String datagram) throws Exception {

        IBlockConnect client = ClientFactory.XSocket.getBlockClient();
        String recvDatagram = client.sendDataUntilRcv(datagram);
        client.close();
        return recvDatagram;
    }
}
