package rfm.ta.common.gateway.dep.model.base;

import java.io.Serializable;

/**
 * DEP往RFM发送的Java对象的报文头解析
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class ToaXmlHeader implements Serializable {
    public String TRX_CODE;                // 交易请求码
    public String REQ_SN;                  // 流水号 客户端应用系统内唯一
    public String WSYS_ID;                 // 外联系统ID
    public String RET_CODE;                // 交易响应码
    public String ERR_MSG;                 // 交易响应信息
}
