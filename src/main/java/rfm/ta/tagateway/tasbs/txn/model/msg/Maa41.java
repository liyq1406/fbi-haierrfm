package rfm.ta.tagateway.tasbs.txn.model.msg;

/**
 * Created by Lichao.W At 2015/6/29 16:53
 * wanglichao@163.com
 * ̩�����ز�����
 */
public class Maa41 extends MTia {
   private String ACTTY1 = "01";  //ת���ʻ�����
   private String IPTAC1 = "";  //ת���ʻ�
   private String DRAMD1 = "";  //ȡ�ʽ
   private String ACTNM1 = "";  //ת���ʻ�����
   private String CUSPW1 = "";  //ȡ������
   private String PASTYP = "";  //֤������
   private String PASSNO = "";  //��Χϵͳ��ˮ��
   private String PAPTYP = "";  //֧Ʊ����
   private String PAPCDE = "";  //֧Ʊ��
   private String PAPMAC = "";  //֧Ʊ����
   private String SGNDAT = "";  //ǩ������
   private String NBKFL1 = "";  //���۱�ʶ
   private String AUTSEQ = "";  //�����ֶ�
   private String AUTDAT = "";  //�����ֶ�
   private String TXNAMT = "";  //���׽��
   private String ACTTY2 = "01";  //ת���ʻ�����
   private String IPTAC2 = "";  //ת���ʻ�
   private String ACTNM2 = "";  //ת���ʻ�����
   private String NBKFL2 = "";  //���۱�ʶ
   private String TXNDAT = "";  //��������
   private String REMARK = "";  //ժҪ
   private String ANACDE = "TAFC";  //ͳ�Ʒ�����
   private String MAGFL1 = "";  //
   private String MAGFL2 = "";  //�Ƿ������1-��0-��

    public Maa41(String IPTAC1, String IPTAC2, String TXNAMT) {
        this.IPTAC1 = IPTAC1;
        this.IPTAC2 = IPTAC2;
        this.TXNAMT = TXNAMT;
    }

    public String getACTTY1() {
        return ACTTY1;
    }

    public void setACTTY1(String ACTTY1) {
        this.ACTTY1 = ACTTY1;
    }

    public String getIPTAC1() {
        return IPTAC1;
    }

    public void setIPTAC1(String IPTAC1) {
        this.IPTAC1 = IPTAC1;
    }

    public String getDRAMD1() {
        return DRAMD1;
    }

    public void setDRAMD1(String DRAMD1) {
        this.DRAMD1 = DRAMD1;
    }

    public String getACTNM1() {
        return ACTNM1;
    }

    public void setACTNM1(String ACTNM1) {
        this.ACTNM1 = ACTNM1;
    }

    public String getCUSPW1() {
        return CUSPW1;
    }

    public void setCUSPW1(String CUSPW1) {
        this.CUSPW1 = CUSPW1;
    }

    public String getPASTYP() {
        return PASTYP;
    }

    public void setPASTYP(String PASTYP) {
        this.PASTYP = PASTYP;
    }

    public String getPASSNO() {
        return PASSNO;
    }

    public void setPASSNO(String PASSNO) {
        this.PASSNO = PASSNO;
    }

    public String getPAPTYP() {
        return PAPTYP;
    }

    public void setPAPTYP(String PAPTYP) {
        this.PAPTYP = PAPTYP;
    }

    public String getPAPCDE() {
        return PAPCDE;
    }

    public void setPAPCDE(String PAPCDE) {
        this.PAPCDE = PAPCDE;
    }

    public String getPAPMAC() {
        return PAPMAC;
    }

    public void setPAPMAC(String PAPMAC) {
        this.PAPMAC = PAPMAC;
    }

    public String getSGNDAT() {
        return SGNDAT;
    }

    public void setSGNDAT(String SGNDAT) {
        this.SGNDAT = SGNDAT;
    }

    public String getNBKFL1() {
        return NBKFL1;
    }

    public void setNBKFL1(String NBKFL1) {
        this.NBKFL1 = NBKFL1;
    }

    public String getAUTSEQ() {
        return AUTSEQ;
    }

    public void setAUTSEQ(String AUTSEQ) {
        this.AUTSEQ = AUTSEQ;
    }

    public String getAUTDAT() {
        return AUTDAT;
    }

    public void setAUTDAT(String AUTDAT) {
        this.AUTDAT = AUTDAT;
    }

    public String getTXNAMT() {
        return TXNAMT;
    }

    public void setTXNAMT(String TXNAMT) {
        this.TXNAMT = TXNAMT;
    }

    public String getACTTY2() {
        return ACTTY2;
    }

    public void setACTTY2(String ACTTY2) {
        this.ACTTY2 = ACTTY2;
    }

    public String getIPTAC2() {
        return IPTAC2;
    }

    public void setIPTAC2(String IPTAC2) {
        this.IPTAC2 = IPTAC2;
    }

    public String getACTNM2() {
        return ACTNM2;
    }

    public void setACTNM2(String ACTNM2) {
        this.ACTNM2 = ACTNM2;
    }

    public String getNBKFL2() {
        return NBKFL2;
    }

    public void setNBKFL2(String NBKFL2) {
        this.NBKFL2 = NBKFL2;
    }

    public String getTXNDAT() {
        return TXNDAT;
    }

    public void setTXNDAT(String TXNDAT) {
        this.TXNDAT = TXNDAT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getANACDE() {
        return ANACDE;
    }

    public void setANACDE(String ANACDE) {
        this.ANACDE = ANACDE;
    }

    public String getMAGFL1() {
        return MAGFL1;
    }

    public void setMAGFL1(String MAGFL1) {
        this.MAGFL1 = MAGFL1;
    }

    public String getMAGFL2() {
        return MAGFL2;
    }

    public void setMAGFL2(String MAGFL2) {
        this.MAGFL2 = MAGFL2;
    }
}
