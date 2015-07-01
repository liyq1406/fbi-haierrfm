package rfm.ta.gateway.xsocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsocket.connection.IConnection.FlushMode;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.gateway.xsocket.server.impl.TaServerHandlerTa;

import java.io.IOException;

/**
 * �����
 *
 * @author zxb
 */
@Component
public class TaXSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(TaXSocketServer.class);
    private static final int PORT = PropertyManager.getIntProperty("socket_server_monitor_port");

    private IServer server;
    @Autowired
    private TaServerHandlerTa taServerHandler;

    public TaXSocketServer() {
    }
    private void init() throws IOException {
        this.server = new Server(PORT, taServerHandler);
        this.server.setFlushmode(FlushMode.ASYNC);   // �첽
    }

    public void start() throws IOException {
        init();
        logger.info("��SocketServer�� " + server.getLocalAddress() + ":" + PORT + "  ��ʼ����...");
        server.start();
        logger.info("��SocketServer��  " + server.getLocalAddress() + ":" + PORT + "  �����ɹ�...");
    }

    public boolean stop() throws IOException {
        logger.info("��SocketServer��  " + server.getLocalAddress() + ":" + PORT + "  ��ʼ�ر�...");
        if (server != null) {
            server.close();
            server = null;
        }
        logger.info("��SocketServer���رս���...");

        return true;
    }

}