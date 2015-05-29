package rfm.qd.gateway.xsocket.client;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: ÏÂÎç2:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConnectClient implements IBlockConnect {
    protected String serverIP;
    protected int serverPort;

    public ConnectClient() {
    }

    protected ConnectClient(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
