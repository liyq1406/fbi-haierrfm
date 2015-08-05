package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TIABody;
import org.fbi.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：交存记账
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class Tia9902002 extends TIA implements Serializable {
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
        /*01	交易代码	    4	2002
          02	监管银行代码	2
          03	城市代码	    6
          04	交存申请编号	14
          05	交存金额	    20	2001交易交存验证返回的实缴金额；2003交易无返此项，由柜员录入。
          06	监管账号	    30	划拨验证的输出项
          07	结算方式	    2	01_ 现金 02_ 转账 03_ 支票
          08	支票号码	    30
          09	银行记账流水	30
          10	记账日期	    10	送系统日期即可
          11	记账网点	    30
          12	记账人员	    30
          13	发起方	        1	1_监管银行*/
        public String BANK_ID;            // 监管银行代码	  2
        public String CITY_ID;            // 城市代码	      6
        public String BRANCH_ID;          // 网点号         30
        public String INITIATOR;          // 发起方         1   1_监管银行
        public String TX_AMT;             // 05	交存金额	    20	2001交易交存验证返回的实缴金额；2003交易无返此项，由柜员录入。
        public String ACC_ID;             // 06	监管账号	    30	划拨验证的输出项
        public String STL_TYPE;           // 07   结算方式	    2	01_ 现金 02_ 转账 03_ 支票
        public String CHECK_ID;           // 08   支票号码	    30
        public String TX_DATE;            // 日期           10  送系统日期即可
    }
}
