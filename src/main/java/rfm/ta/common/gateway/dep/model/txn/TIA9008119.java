package rfm.ta.common.gateway.dep.model.txn;

import rfm.ta.common.gateway.dep.model.base.TIA;
import rfm.ta.common.gateway.dep.model.base.TIABody;
import rfm.ta.common.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;
import java.util.List;

/**
 * SBS: 批量多账户余额查询
 * User: zhanrui
 * Date: 2012-01-31
 */

public class TIA9008119 extends TIA implements Serializable {
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
        public List<String> ACCTNUMS;
    }

}
