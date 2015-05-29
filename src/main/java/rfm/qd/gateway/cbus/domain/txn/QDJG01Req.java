package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractReqMsg;
import org.apache.commons.lang.StringUtils;

public class QDJG01Req extends AbstractReqMsg {

    /*
    �˺�	28	������Ҳ��ո�
     */
    public String accountNo;

    @Override
    public String bodyToString() {

        return StringUtils.rightPad(accountNo, 28, ' ');
    }

}
