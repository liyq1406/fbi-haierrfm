package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：划拨验证
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class Toa9902101 extends TOA implements Serializable {
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
          04	划拨金额	            20  以分为单位
          05	收款银行	            90
          06	收款单位账号	        30
          07	收款单位户名	        150
          08	项目名称	            128
          09	开发企业名称	        255
          10    预售资金监管平台流水    16
        */
        public String SPVSN_ACC_ID;             // 02	监管账号        30
        public String SPVSN_ACC_NAME;           // 03	监管账户户名    150
        public String TX_AMT;             // 04   划拨金额	    20  以分为单位
        public String GERL_BANK;          // 05   收款银行       90
        public String GERL_ACC_ID;       // 06	收款单位账号	30
        public String GERL_ACC_NAME;     // 07   收款单位户名	150
        public String PROG_NAME;         // 08   项目名称        128
        public String COMP_NAME;         // 09   开发企业名称    255
    }
}
