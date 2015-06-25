package rfm.ta.gateway.sbs.domain.txn;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.sbs.domain.base.AbstractReqMsg;
import rfm.ta.gateway.utils.StringUtil;

public class QDJG04Req extends AbstractReqMsg {

    /*
    收报行	SND-TO-BK-NO	C(12)
    汇款人名称	RMTR-NAME-FL	C(64)
    汇款人帐号	RMTR-ACCT-NO	C(32)
    收款人名称	PAYEE-NAME-FL	C(64)
    收款人帐号	PAYEE-FL-ACCT-NO	C(32)
    汇款金额	RMT-AMT	N(13，2)
    汇款用途	RMT-PURP	C(64)
//    凭证种类  4
//    凭证号码  16
//    备注   40
     */

    public String sndToBkNo;         //  收报行  12
    public String rmtrNameFl;        //  汇款人名称  64
    public String rmtrAcctNo;        //  汇款人帐号 32
    public String payeeNameFl;       //  收款人名称  64
    public String payeeFlAcctNo;     //  收款人帐号  32
    public String rmtAmt;            //  汇款金额  15
    public String rmtPurp;           //  汇款用途  64

//    public String voucherType;      // 凭证种类  4
//    public String voucherNo;        // 凭证号码  16
    public String remark;           // 备注   40

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
