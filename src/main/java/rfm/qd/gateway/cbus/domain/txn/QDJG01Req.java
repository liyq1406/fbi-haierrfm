package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractReqMsg;
import org.apache.commons.lang.StringUtils;

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
