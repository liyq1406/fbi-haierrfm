package rfm.ta.gateway.sbs.domain.txn;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.sbs.domain.base.AbstractReqMsg;
import rfm.ta.gateway.utils.StringUtil;

public class QDJG04Req extends AbstractReqMsg {

    /*
    �ձ���	SND-TO-BK-NO	C(12)
    ���������	RMTR-NAME-FL	C(64)
    ������ʺ�	RMTR-ACCT-NO	C(32)
    �տ�������	PAYEE-NAME-FL	C(64)
    �տ����ʺ�	PAYEE-FL-ACCT-NO	C(32)
    �����	RMT-AMT	N(13��2)
    �����;	RMT-PURP	C(64)
//    ƾ֤����  4
//    ƾ֤����  16
//    ��ע   40
     */

    public String sndToBkNo;         //  �ձ���  12
    public String rmtrNameFl;        //  ���������  64
    public String rmtrAcctNo;        //  ������ʺ� 32
    public String payeeNameFl;       //  �տ�������  64
    public String payeeFlAcctNo;     //  �տ����ʺ�  32
    public String rmtAmt;            //  �����  15
    public String rmtPurp;           //  �����;  64

//    public String voucherType;      // ƾ֤����  4
//    public String voucherNo;        // ƾ֤����  16
    public String remark;           // ��ע   40

    public String bodyToString() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(StringUtils.rightPad(sndToBkNo, 12, ' '));
        strBuilder.append(StringUtil.rightPad4ChineseToByteLength(rmtrNameFl, 64, " "));
        strBuilder.append(StringUtils.rightPad(rmtrAcctNo, 32, ' '));
        strBuilder.append(StringUtil.rightPad4ChineseToByteLength(payeeNameFl, 64, " "));
        strBuilder.append(StringUtils.rightPad(payeeFlAcctNo, 32, ' '));
        strBuilder.append(StringUtils.rightPad(rmtAmt, 15, ' '));
        strBuilder.append(StringUtil.rightPad4ChineseToByteLength(rmtPurp, 64, " "));
//        strBuilder.append(StringUtils.rightPad(voucherType, 4, ' '));
//        strBuilder.append(StringUtils.leftPad(voucherNo, 16, "0"));
        strBuilder.append(StringUtil.rightPad4ChineseToByteLength(remark, 40, " "));

        return strBuilder.toString();
    }

}
