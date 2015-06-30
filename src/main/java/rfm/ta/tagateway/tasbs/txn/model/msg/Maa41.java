package rfm.ta.tagateway.tasbs.txn.model.msg;

/**
 * Created by Lichao.W At 2015/6/29 16:53
 * wanglichao@163.com
 * 泰安房地产入账
 */
public class Maa41 extends MTia {
   private String ACTTY1 = "01";  //转出帐户类型
   private String IPTAC1 = "";  //转出帐户
   private String DRAMD1 = "";  //取款方式
   private String ACTNM1 = "";  //转出帐户户名
   private String CUSPW1 = "";  //取款密码
   private String PASTYP = "";  //证件类型
   private String PASSNO = "";  //外围系统流水号
   private String PAPTYP = "";  //支票种类
   private String PAPCDE = "";  //支票号
   private String PAPMAC = "";  //支票密码
   private String SGNDAT = "";  //签发日期
   private String NBKFL1 = "";  //无折标识
   private String AUTSEQ = "";  //备用字段
   private String AUTDAT = "";  //备用字段
   private String TXNAMT = "";  //交易金额
   private String ACTTY2 = "01";  //转入帐户类型
   private String IPTAC2 = "";  //转入帐户
   private String ACTNM2 = "";  //转入帐户户名
   private String NBKFL2 = "";  //无折标识
   private String TXNDAT = "";  //交易日期
   private String REMARK = "";  //摘要
   private String ANACDE = "TAFC";  //统计分析码
   private String MAGFL1 = "";  //
   private String MAGFL2 = "";  //是否见单（1-是0-否）

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
