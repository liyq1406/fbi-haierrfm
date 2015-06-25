package rfm.ta.gateway.xsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContainerManager {

    private static final Logger logger = LoggerFactory.getLogger(ContainerManager.class);
    private static ApplicationContext context;

    private ContainerManager() {
    }

    public static void init() {
        logger.info("... Container Manager 初始化开始.......");
        context = new ClassPathXmlApplicationContext(new String[]{"applicationContext-socket.xml"});
        XSocketManager manager = (XSocketManager)getBean("xSocketManager");
        manager.init();
    }

    public static Object getBean(String key) {
        if (context == null) {
            init();
        }
        return context.getBean(key);
    }

    public static void stop(int code) {
        if (code == 0) {
            logger.info(" Container Manager 正常关闭...");
        } else {
            logger.info(" Container Manager 发生异常，即将关闭...");
        }
        System.exit(code);
    }
}
