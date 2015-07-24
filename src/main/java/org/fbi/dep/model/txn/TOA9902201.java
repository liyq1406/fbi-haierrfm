package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：返还验证
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class TOA9902201 extends TOA implements Serializable {
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
          02	监管账号                30
          03    监管账户户名            150
          04	返还金额	            20  以分为单位
          05	业主姓名	            80
          06	证件类型    	        30
          07	证件号码                255
          08    预售资金监管平台流水    16
        */
        public String RES_CONTEXT;             // 报文主题内容

        public String REMARK;                   // 备注
    }
}
