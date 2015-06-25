package rfm.ta.gateway.xsocket.client;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: обнГ9:02
 * To change this template use File | Settings | File Templates.
 */
public interface IConnect {
    boolean sendData(String dataContent) throws IOException;

    boolean close() throws IOException;
}
