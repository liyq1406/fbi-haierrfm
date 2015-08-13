package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * 泰安房产资金监管：日终对账信息取得
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class Toa900012601 extends TOA implements Serializable {
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
        public String DRCNT;   //借方总笔数
        public String DRAMT;   //借方总金额
        public String CRCNT;   //保留字段
        public String CRAMT;   //保留字段
    }
}
