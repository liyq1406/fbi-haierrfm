package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：交存记账
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2002002 extends TIA implements Serializable {
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
          05	交存金额	    32	MD5
          06	监管账号	    30	划拨验证的输出项
          07	结算方式	    2	01_ 现金 02_ 转账 03_ 支票
          08	支票号码	    30
          09	银行记账流水	30
          10	记账日期	    10	送系统日期即可
          11	记账网点	    30
          12	记账人员	    30
          13	发起方	        1	1_监管银行*/
        public String REQ_CONTEXT;             // 报文主题内容

        public String REMARK;                   // 备注
    }
}
