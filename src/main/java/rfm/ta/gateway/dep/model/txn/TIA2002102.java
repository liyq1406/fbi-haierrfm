package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;

/**
 * 泰安房产资金监管：划拨记账
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2002102 extends TIA implements Serializable {
    public  Header header = new Header();
    public  Body body = new Body();

    @Override
    public TIAHeader getHeader() {
        return  header;
    }

    @Override
    public TIABody getBody() {
        return  body;
    }

    //====================================================================
    public  static class Header extends TIAHeader {
    }

    public static class Body extends TIABody {
        /*01	交易代码	    4	2102
          02	监管银行代码	2
          03	城市代码	    6
          04	划拨业务编号	14
          05	划拨密码	    32	MD5
          06	监管账号	    30	划拨验证的输出项
          07	收款单位账号	30	划拨验证的输出项
          08	划拨资金	    20	划拨验证的输出项
          09	结算方式	    2	01_ 现金 02_ 转账 03_ 支票
          10	支票号码	    30
          11	银行记账流水	30
          12	记账日期	    10	送系统日期即可
          13	记账网点	    30
          14	记账人员	    30
          15	发起方	        1	1_监管银行*/

        public String ACC_ID;             // 06	监管账号	    30	划拨验证的输出项
        public String RECV_ACC_ID;       // 07	收款单位账号	30	划拨验证的输出项
        public String TX_AMT;             // 08	划拨资金	    20	划拨验证的输出项
        public String STL_TYPE;           // 09   结算方式	    2	01_ 现金 02_ 转账 03_ 支票
        public String CHECK_ID;           // 10   支票号码	    30
        public String TX_DATE;            // 日期           10  送系统日期即可
    }
}
