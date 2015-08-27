package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：记账结果查询
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class Toa9902501 extends TOA implements Serializable {
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
          02	记账结果	            1   0_成功 1_失败
          03	预售资金监管平台记账流水16  业务记账流水
          04	监管银行记账流水	    30  业务记账流水
          05	预售资金监管平台流水	16
        */
        public String SPVSN_ACT_RSTL;          // 02  记账结果	                 1   0_成功 1_失败
        public String FDC_ACT_SN;        // 03  预售资金监管平台记账流水  16  业务记账流水
        public String FDC_BANK_ACT_SN;  // 04  监管银行记账流水	         30  业务记账流水
    }
}
