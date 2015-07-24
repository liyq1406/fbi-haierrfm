package org.fbi.dep.model.base;

import java.io.Serializable;

/**
 * DEP往RFM发送的Java对象的报文头
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class TOAHeader implements Serializable {
    public String CHANNEL_ID;                // 渠道
    public String APP_ID;                    // 应用ID
    public String BIZ_ID;                    // 业务ID
    public String REQ_SN;                    // 流水号 客户端应用系统内唯一   流水 16
    public String TX_CODE;                   // 交易请求码
    public String RETURN_CODE;               // 交易响应码                   结果 4 0000表示成功
    public String RETURN_MSG;                // 交易响应信息                 描述 60
    public String SIGNED_MSG;                // 交易报文签名信息
}
