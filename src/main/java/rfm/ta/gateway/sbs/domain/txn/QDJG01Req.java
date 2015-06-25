package rfm.ta.gateway.sbs.domain.txn;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.sbs.domain.base.AbstractReqMsg;

public class QDJG01Req extends AbstractReqMsg {

    /*
    ’À∫≈	28	◊Û∂‘∆Î”“≤πø’∏Ò
     */
    public String accountNo;

    @Override
    public String bodyToString() {

        return StringUtils.rightPad(accountNo, 28, ' ');
    }

}
