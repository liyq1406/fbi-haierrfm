package rfm.ta.gateway.xsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import platform.service.SystemService;
import rfm.ta.gateway.xsocket.server.TaXSocketServer;

@Component("taXSocketManager")
public class TaXSocketManager {

    private static final Logger logger = LoggerFactory.getLogger(TaXSocketManager.class);
    private static final long serialVersionUID = -5534543207744847501L;
    @Autowired
    private TaXSocketServer taXSocketServer;

    public TaXSocketManager() {
    }

    // 初始化
    public void init() {
        printLine();
        try {
            taXSocketServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        printLine();
    }


    // 销毁
    public void destroy() {
        printLine();
        try {
            taXSocketServer.stop();
            taXSocketServer = null;
        } catch (Exception e) {
            throw new RuntimeException("Socket Server 销毁时发生异常 !");
        }
        printLine();
    }

    public TaXSocketServer getTaXSocketServer() {
        return taXSocketServer;
    }

    public void setTaXSocketServer(TaXSocketServer taXSocketServer) {
        this.taXSocketServer = taXSocketServer;
    }

    private static void printLine() {
        logger.info("//////////////////////////" + SystemService.getDatetime18() + "//////////////////////////");
    }

}


