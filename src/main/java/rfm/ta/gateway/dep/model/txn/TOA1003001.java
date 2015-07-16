package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TOA;
import rfm.ta.gateway.dep.model.base.TOABody;
import rfm.ta.gateway.dep.model.base.TOAHeader;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银联：实时查询交易结果
 * User: zhanrui
 * Date: 2012-01-31
 */
public class TOA1003001 extends TOA implements Serializable {
    public Header header = new Header();
    public Body body = new Body();

    @Override
    public TOAHeader getHeader() {
        return header;
    }

    @Override
    public TOABody getBody() {
        return body;
    }

    //====================================================================
    public static class Header extends TOAHeader {
    }

    public static class Body extends TOABody {
        public String QUERY_SN;
        public String ACCOUNT_NO;
        public String ACCOUNT_NAME;
        public BigDecimal AMOUNT;
        public String REMARK;
    }
}
