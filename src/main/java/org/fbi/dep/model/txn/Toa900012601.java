package org.fbi.dep.model.txn;

import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.base.TOABody;
import org.fbi.dep.model.base.TOAHeader;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hanjianlong on 2015-8-10.
 * �˻����ս�����ϸ��ѯ
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
        /*01	��Χϵͳ��ˮ��
          02	���׽��
        */
        public String TOTCNT;       // �ܼ�¼��
        public String CURCNT;       // �����ڼ�¼��
        public String DBTCNT;       // �跽�ܱ���
        public String DBTAMT;       // �跽�ܽ��
        public String CRTCNT;       // �����ܱ���
        public String CRTAMT;       // �����ܽ��
        public List<BodyDetail> DETAILS;
        public String RTN_REQ_SN;
        public String RTN_TX_AMT;
    }

    public static class BodyDetail implements Serializable {
        public String CUSIDT;       // �ͻ���
        public String APCODE;       // ������
        public String CURCDE;       // ���Һ�
        public String STMDAT;       // ��������
        public String TLRNUM;       // ���׹�Ա
        public String VCHSET;       // ��Ʊ�׺�
        public String SETSEQ;       // ��Ʊ�������
        public String TXNAMT;       // ���׽��
        public String LASBAL;       // ���׺����
        public String FURINF;       // ժҪ

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
