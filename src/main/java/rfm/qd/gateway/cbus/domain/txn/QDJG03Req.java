package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractReqMsg;
import rfm.qd.gateway.utils.StringUtil;
import org.apache.commons.lang.StringUtils;

public class QDJG03Req extends AbstractReqMsg {

    /*
    客户帐号	28	转出账号？
    委托单位帐号	28	转入账号？
    金额	15
    备注 40
    */

    public String payOutAccount;
    public String payInAccount;
    public String payAmt;

    public String remark;

    public String bodyToString() {

        return StringUtils.rightPad(payOutAccount, 28, ' ') + StringUtils.rightPad(payInAccount, 28, ' ')
                + StringUtils.leftPad(payAmt, 15, ' ')
                + StringUtil.rightPad4ChineseToByteLength(remark, 40, " ");
    }
}
