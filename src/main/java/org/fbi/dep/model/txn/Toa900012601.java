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

public class Toa900012601 extends TOA {
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
        public String TOTCNT;       // 总记录数
        public String CURCNT;       // 本包内记录数
        public String DBTCNT;       // 借方总笔数
        public String DBTAMT;       // 借方总金额
        public String CRTCNT;       // 贷方总笔数
        public String CRTAMT;       // 贷方总金额
        public List<BodyDetail> DETAILS;
        public String RTN_REQ_SN;
        public String RTN_TX_AMT;
    }

    public static class BodyDetail implements Serializable {
        public String CUSIDT;       // 客户号
        public String APCODE;       // 核算码
        public String CURCDE;       // 货币号
        public String STMDAT;       // 交易日期
        public String TLRNUM;       // 交易柜员
        public String VCHSET;       // 传票套号
        public String SETSEQ;       // 传票套内序号
        public String TXNAMT;       // 交易金额
        public String LASBAL;       // 交易后余额
        public String FURINF;       // 摘要

        public String getCUSIDT() {
            return CUSIDT;
        }

        public void setCUSIDT(String CUSIDT) {
            this.CUSIDT = CUSIDT;
        }

        public String getAPCODE() {
            return APCODE;
        }

        public void setAPCODE(String APCODE) {
            this.APCODE = APCODE;
        }

        public String getCURCDE() {
            return CURCDE;
        }

        public void setCURCDE(String CURCDE) {
            this.CURCDE = CURCDE;
        }

        public String getSTMDAT() {
            return STMDAT;
        }

        public void setSTMDAT(String STMDAT) {
            this.STMDAT = STMDAT;
        }

        public String getTLRNUM() {
            return TLRNUM;
        }

        public void setTLRNUM(String TLRNUM) {
            this.TLRNUM = TLRNUM;
        }

        public String getVCHSET() {
            return VCHSET;
        }

        public void setVCHSET(String VCHSET) {
            this.VCHSET = VCHSET;
        }

        public String getSETSEQ() {
            return SETSEQ;
        }

        public void setSETSEQ(String SETSEQ) {
            this.SETSEQ = SETSEQ;
        }

        public String getTXNAMT() {
            return TXNAMT;
        }

        public void setTXNAMT(String TXNAMT) {
            this.TXNAMT = TXNAMT;
        }

        public String getLASBAL() {
            return LASBAL;
        }

        public void setLASBAL(String LASBAL) {
            this.LASBAL = LASBAL;
        }

        public String getFURINF() {
            return FURINF;
        }

        public void setFURINF(String FURINF) {
            this.FURINF = FURINF;
        }
    }
}
