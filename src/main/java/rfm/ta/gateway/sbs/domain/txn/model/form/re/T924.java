package rfm.ta.gateway.sbs.domain.txn.model.form.re;

import rfm.ta.gateway.sbs.domain.core.domain.AssembleModel;
import rfm.ta.gateway.sbs.domain.core.domain.SOFFormBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichao.W At 2015/7/9 10:29
 * wanglichao@163.com
 * ̩����������
 */
public class T924 extends SOFFormBody {
    private String FLOFLG = "0"; // ������ʶ
    private String totcnt = "0"; // �ܼ�¼��
    private String curcnt = "0"; // �����ڼ�¼��
    private List<Bean> beanList = new ArrayList<Bean>();

    @Override
    public void assembleFields(int offset, byte[] buffer) {
        byte[] floflgBytes = new byte[1];
        byte[] totcntBytes = new byte[6];
        byte[] curcntBytes = new byte[6];
        System.arraycopy(buffer, offset, floflgBytes, 0, 1);
        totcnt = new String(floflgBytes);
        System.arraycopy(buffer, offset + 1, totcntBytes, 0, 6);
        totcnt = new String(totcntBytes);
        System.arraycopy(buffer, offset + 7, curcntBytes, 0, 6);
        curcnt = new String(curcntBytes);

        int index = offset + 13;
        int beanLength = 66;
        do {
            Bean bean = new Bean();
            bean.assembleFields(index, buffer);
            beanList.add(bean);
            index += beanLength;
        } while (index < buffer.length);
    }

    public String getFLOFLG() {
        return FLOFLG;
    }

    public void setFLOFLG(String FLOFLG) {
        this.FLOFLG = FLOFLG;
    }

    public String getTotcnt() {
        return totcnt;
    }

    public void setTotcnt(String totcnt) {
        this.totcnt = totcnt;
    }

    public String getCurcnt() {
        return curcnt;
    }

    public void setCurcnt(String curcnt) {
        this.curcnt = curcnt;
    }

    public List<Bean> getBeanList() {
        return beanList;
    }

    public class Bean extends AssembleModel {
        {
            fieldTypes = new int[]{1, 1, 1, 1, 1, 1, 4, 1, 1};//9
            fieldLengths = new int[]{14, 8, 8, 4, 4, 2, 21, 35, 18};
        }

        private String ACTNUM;   //�����˺�
        private String STMDAT;   //��������
        private String ERYTIM;   //����ʱ��
        private String TLRNUM;   //���׹�Ա
        private String VCHSET;   //��Ʊ�׺�
        private String SETSEQ;   //��Ʊ����˳���
        private BigDecimal TXNAMT;   //���׽��
        private String BENACT;   //�տ��˺�
        private String MPCSEQ;   //��Χϵͳ��ˮ��

        public String getACTNUM() {
            return ACTNUM;
        }

        public void setACTNUM(String ACTNUM) {
            this.ACTNUM = ACTNUM;
        }

        public String getSTMDAT() {
            return STMDAT;
        }

        public void setSTMDAT(String STMDAT) {
            this.STMDAT = STMDAT;
        }

        public String getERYTIM() {
            return ERYTIM;
        }

        public void setERYTIM(String ERYTIM) {
            this.ERYTIM = ERYTIM;
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

        public BigDecimal getTXNAMT() {
            return TXNAMT;
        }

        public void setTXNAMT(BigDecimal TXNAMT) {
            this.TXNAMT = TXNAMT;
        }

        public String getBENACT() {
            return BENACT;
        }

        public void setBENACT(String BENACT) {
            this.BENACT = BENACT;
        }

        public String getMPCSEQ() {
            return MPCSEQ;
        }

        public void setMPCSEQ(String MPCSEQ) {
            this.MPCSEQ = MPCSEQ;
        }
    }
}
