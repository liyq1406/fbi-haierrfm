package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanjianlong on 2015-8-10.
 * 账户当日交易明细查询
 */
public class Toa900012602 extends TOA {
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
        public String FLOFLG;   //后续标识
        public String TOTCNT;   //总记录数
        public String CURCNT;   //本包内记录数
        public List<BodyDetail> DETAILS;

        public static class BodyDetail implements Serializable {
            public String FBTIDX;          // 外围系统流水号
            public String ACTNUM;          // 付款账号
            public String TXNAMT;          // 交易金额（贷款交易时，本金）
            public String INTAMT;          // 利息金额
            public String FEEAMT;          // 手续费金额
            public String BENACT;          // 对方账号
            public String ERYTIM;          // 交易时间

            public String getFBTIDX() {
                return FBTIDX;
            }

            public void setFBTIDX(String FBTIDX) {
                this.FBTIDX = FBTIDX;
            }

            public String getACTNUM() {
                return ACTNUM;
            }

            public void setACTNUM(String ACTNUM) {
                this.ACTNUM = ACTNUM;
            }

            public String getTXNAMT() {
                return TXNAMT;
            }

            public void setTXNAMT(String TXNAMT) {
                this.TXNAMT = TXNAMT;
            }

            public String getINTAMT() {
                return INTAMT;
            }

            public void setINTAMT(String INTAMT) {
                this.INTAMT = INTAMT;
            }

            public String getFEEAMT() {
                return FEEAMT;
            }

            public void setFEEAMT(String FEEAMT) {
                this.FEEAMT = FEEAMT;
            }

            public String getBENACT() {
                return BENACT;
            }

            public void setBENACT(String BENACT) {
                this.BENACT = BENACT;
            }

            public String getERYTIM() {
                return ERYTIM;
            }

            public void setERYTIM(String ERYTIM) {
                this.ERYTIM = ERYTIM;
            }
        }
    }
}
