package rfm.ta.gateway.sbs.domain.txn.model.form.ac;

import rfm.ta.gateway.sbs.domain.core.domain.SOFFormBody;

/**
 * Created by Lichao.W At 2015/7/10 8:53
 * wanglichao@163.com
 */
public class T846 extends SOFFormBody {
    {
        fieldTypes = new int[]{1, 1, 1, 1};
        fieldLengths = new int[]{10, 21, 10, 21};
    }
    private String DRCNT;   //本日交易总笔数
    private String DRAMT;   //本日交易总金额
    private String CRCNT;   //保留字段
    private String CRAMT;   //保留字段

    public String getDRCNT() {
        return DRCNT;
    }

    public void setDRCNT(String DRCNT) {
        this.DRCNT = DRCNT;
    }

    public String getDRAMT() {
        return DRAMT;
    }

    public void setDRAMT(String DRAMT) {
        this.DRAMT = DRAMT;
    }

    public String getCRCNT() {
        return CRCNT;
    }

    public void setCRCNT(String CRCNT) {
        this.CRCNT = CRCNT;
    }

    public String getCRAMT() {
        return CRAMT;
    }

    public void setCRAMT(String CRAMT) {
        this.CRAMT = CRAMT;
    }
}
