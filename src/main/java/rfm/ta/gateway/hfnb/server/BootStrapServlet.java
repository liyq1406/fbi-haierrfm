package rfm.ta.gateway.hfnb.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.advance.utils.PropertyManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Created by XIANGYANG on 2015-7-2.
 */

public class BootStrapServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(BootStrapServlet.class);

    @Override
    public void init() throws ServletException {
        try {
            // 启动本地对网银服务端
            logger.info("HFNB ServerSocket启动......");
            int portForStaring = PropertyManager.getIntProperty("ta.hfnb.server.socket.port");
            LocalServerBootstrap localServer = new LocalServerBootstrap(portForStaring);
            localServer.start();
            logger.info("HFNB ServerSocket启动完成......");
        } catch (Exception e) {
            logger.error("HFNB ServerSocket初始化错误。", e);
            System.exit(-1);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        System.exit(-1);
    }
}
