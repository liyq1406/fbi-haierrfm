package rfm.qd.gateway.xsocket.client.impl;

import rfm.qd.gateway.xsocket.client.IBlockConnect;
import pub.platform.advance.utils.PropertyManager;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: ÏÂÎç1:57
 * To change this template use File | Settings | File Templates.
 */
public enum ClientFactory {
    XSocket(PropertyManager.getProperty("socket_server_monitor_ip"),
            PropertyManager.getIntProperty("socket_server_monitor_port"),
            PropertyManager.getIntProperty("socket_timeout_millis"));
    private String serverIp;
    private int serverPort;
    private int timeoutMills;
    private IBlockConnect client;

    private ClientFactory(String serverIp, int serverPort, int timeoutMills) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.timeoutMills = timeoutMills;
    }

    public IBlockConnect getBlockClient() throws IOException {
        client = null;
        client = new XSocketBlockClient(serverIp, serverPort,timeoutMills);
        return client;
    }
}
