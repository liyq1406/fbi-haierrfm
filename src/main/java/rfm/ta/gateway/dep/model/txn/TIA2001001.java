package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：建立监管
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2001001 extends TIA implements Serializable {
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
          04	监管申请编号    14
          05    帐户类别        1  0：预售监管户
          06    监管专户账号    30
          07    监管专户户名    150
          08	流水号    	    30
          09	日期	        10	送系统日期即可
          10	网点号	        30
          11	柜员号	        30
          12	发起方	        1	1_监管银行*/
        public String REQ_CONTEXT;             // 报文主题内容

        public String REMARK;                   // 备注
    }
}
