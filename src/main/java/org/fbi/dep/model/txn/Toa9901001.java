package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：建立监管
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class Toa9901001 extends TOA implements Serializable {
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
          03	预售人名称	            255
          04	预售项目地址            128
          05    预售项目名称            128
        */
        public String PRE_SALE_PER_NAME;             // 预售人名称	            255
        public String PRE_SALE_PRO_ADDR;             // 预售项目地址            128
        public String PRE_SALE_PRO_NAME;             // 预售项目名称            128
    }
}
