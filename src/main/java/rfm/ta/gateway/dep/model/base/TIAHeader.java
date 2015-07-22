package rfm.ta.gateway.dep.model.base;

import java.io.Serializable;

/**
 * RFM往DEP发送的Java对象的报文头
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class TIAHeader implements Serializable {
    public String CHANNEL_ID;                // 渠道
    public String APP_ID;                    // 应用ID
    public String BIZ_ID;                    // 业务ID                       监管申请编号  14
    public String REQ_SN;                    // 流水号 客户端应用系统内唯一  流水号        30
    public String USER_ID;                   // 操作员ID                     柜员号        30
    public String PASSWORD;                  // 操作员密码                   划拨密码      32
    public String TX_CODE;                   // 交易请求码                   交易代码       4
    public String SIGNED_MSG;                // 交易报文签名信息
}
