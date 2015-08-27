package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TIABody;
import org.fbi.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：解除监管
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class Tia9901002 extends TIA implements Serializable {
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
          04	终止证明编号    14
          05    监管专户账号    30
          06    监管专户户名    150
          07	流水号    	    30
          08	日期	        10	送系统日期即可
          09	网点号	        30
          10	柜员号	        30
          11	发起方	        1	1_监管银行*/
        public String SPVSN_BANK_ID;     // 监管银行代码	  2
        public String CITY_ID;            // 城市代码	      6
        public String BRANCH_ID;          // 网点号         30
        public String INITIATOR;          // 发起方         1   1_监管银行
        public String SPVSN_ACC_ID;      // 监管专户账号   30
        public String SPVSN_ACC_NAME;    // 监管专户户名   150
        public String TX_DATE;            // 日期           10  送系统日期即可
    }
}
