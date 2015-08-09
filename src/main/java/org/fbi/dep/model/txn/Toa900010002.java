package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：交存记账到SBS
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class Toa900010002 extends TOA implements Serializable {
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
        /*01	外围系统流水号
          02	交易金额
        */
        public String RTN_REQ_SN;
        public String RTN_TX_AMT;
    }
}
