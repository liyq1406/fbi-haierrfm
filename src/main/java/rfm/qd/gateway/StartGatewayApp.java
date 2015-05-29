package rfm.qd.gateway;

import rfm.qd.gateway.xsocket.ContainerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-26
 * Time: ����11:54
 * To change this template use File | Settings | File Templates.
 */
public class StartGatewayApp {
     private static final Logger logger = LoggerFactory.getLogger(StartGatewayApp.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        ContainerManager.init();
        logger.info("====== Socket Server ��ʼ������==========");
    }
}
