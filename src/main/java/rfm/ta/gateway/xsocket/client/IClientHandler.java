package rfm.ta.gateway.xsocket.client;

import org.xsocket.connection.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: обнГ9:06
 * To change this template use File | Settings | File Templates.
 */
@Deprecated
public interface IClientHandler extends IDataHandler, IConnectHandler,
        IDisconnectHandler, IIdleTimeoutHandler, IConnectionTimeoutHandler,
        IConnectExceptionHandler {
}
