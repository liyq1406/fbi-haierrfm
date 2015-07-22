package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TOA;
import rfm.ta.gateway.dep.model.base.TOABody;
import rfm.ta.gateway.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：撤销监管
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class TOA2001002 extends TOA implements Serializable {
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
        /*01	结果	                4   0000表示成功
          02	预售资金监管平台流水	16
        */
    }
}
