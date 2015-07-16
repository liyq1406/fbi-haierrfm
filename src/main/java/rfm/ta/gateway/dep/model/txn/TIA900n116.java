package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * SBS 对外支付落地审核明细查询
 * User: zhanrui
 * Date: 2012-02-14
 */

public class TIA900n116 extends TIA implements Serializable {
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
        public String ORDDAT;   //交易日期 YYYYMMDD
    }

}
