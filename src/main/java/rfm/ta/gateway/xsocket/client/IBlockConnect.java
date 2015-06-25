package rfm.ta.gateway.xsocket.client;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: обнГ9:02
 * To change this template use File | Settings | File Templates.
 */
public interface IBlockConnect extends IConnect {
    String sendDataUntilRcv(String datagram) throws Exception;
}
