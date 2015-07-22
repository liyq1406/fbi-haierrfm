package rfm.ta.common.gateway.dep.model.txn;

import rfm.ta.common.gateway.dep.model.base.TIA;
import rfm.ta.common.gateway.dep.model.base.TIABody;
import rfm.ta.common.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：交存验证
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2002001 extends TIA implements Serializable {
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
        /*01	交易代码	    4	2001
          02	监管银行代码	2
          03	城市代码	    6
          04	交存申请编号    14
          05	验证流水    	30
          06	验证日期	    10	送系统日期即可
          07	验证网点	    30
          08	验证人员	    30
          09	发起方	        1	1_监管银行*/
        public String TX_DATE;            // 日期           10  送系统日期即可
    }
}
