package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TIABody;
import org.fbi.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：划拨冲正
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class Tia9902111 extends TIA implements Serializable {
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
          05	银行冲正流水	30
          06	记账日期	    10	送系统日期即可
          07	记账网点	    30
          08	记账人员	    30
          09	发起方	        1	1_监管银行*/
        public String BANK_ID;            // 监管银行代码	  2
        public String CITY_ID;            // 城市代码	      6
        public String BRANCH_ID;          // 网点号         30
        public String INITIATOR;          // 发起方         1   1_监管银行
        public String TX_DATE;            // 日期           10  送系统日期即可
    }
}
