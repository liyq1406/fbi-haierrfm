package rfm.ta.gateway.xsocket.server;

import org.springframework.stereotype.Component;
import org.xsocket.connection.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: обнГ9:11
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface TaIServerHandler extends IDataHandler, IConnectHandler, IIdleTimeoutHandler,
        IConnectionTimeoutHandler, IDisconnectHandler, IConnectExceptionHandler {
}
