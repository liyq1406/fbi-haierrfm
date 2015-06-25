package rfm.ta.gateway.sbs.domain.txn;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.sbs.domain.base.AbstractReqMsg;
import rfm.ta.gateway.utils.StringUtil;

public class QDJG03Req extends AbstractReqMsg {

    /*
    �ͻ��ʺ�	28	ת���˺ţ�
    ί�е�λ�ʺ�	28	ת���˺ţ�
    ���	15
    ��ע 40
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
