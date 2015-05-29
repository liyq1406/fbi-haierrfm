package rfm.qd.gateway.xsocket.server;

import rfm.qd.gateway.xsocket.server.impl.ServerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xsocket.connection.IConnection.FlushMode;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;
import pub.platform.advance.utils.PropertyManager;

import java.io.IOException;

/**
 * 服务端
 *
 * @author zxb
 */
@Component
public class XSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(XSocketServer.class);
    private static final int PORT = PropertyManager.getIntProperty("socket_server_monitor_port");

    private IServer server;
    @Autowired
    private ServerHandler serverHandler;

    public XSocketServer() {
    }
    private void init() throws IOException {
        this.server = new Server(PORT, serverHandler);
        this.server.setFlushmode(FlushMode.ASYNC);   // 异步
    }

    public void start() throws IOException {
        init();
        logger.info("【SocketServer】 " + server.getLocalAddress() + ":" + PORT + "  开始启动...");
        server.start();
        logger.info("【SocketServer】  " + server.getLocalAddress() + ":" + PORT + "  启动成功...");
    }

    public boolean stop() throws IOException {
        logger.info("【SocketServer】  " + server.getLocalAddress() + ":" + PORT + "  开始关闭...");
        if (server != null) {
            server.close();
            server = null;
        }
        logger.info("【SocketServer】关闭结束...");

        return true;
    }

}
