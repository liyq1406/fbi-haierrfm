package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：交存验证
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class TOA9902001 extends TOA implements Serializable {
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
          02	帐户类别	            1   0：监管户；
          03	交存金额	            20  以分为单位
          04	监管专户账号            30
          05    监管专户户名            150
          06    预售资金监管平台流水    16
        */
        public String ACC_TYPE;           // 帐户类别       1  0：预售监管户
        public String TX_AMT;             // 交存金额	      20  以分为单位
        public String ACC_ID;             // 监管专户账号   30
        public String ACC_NAME;           // 监管专户户名   150
    }
}
