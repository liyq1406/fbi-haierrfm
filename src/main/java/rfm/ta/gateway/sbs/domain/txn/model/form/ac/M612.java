package rfm.ta.gateway.sbs.domain.txn.model.form.ac;

import rfm.ta.gateway.sbs.domain.core.domain.SOFFormBody;

/**
 * Created by Lichao.W At 2015/6/30 9:19
 * wanglichao@163.com
 * 日期不对
 */
public class M612 extends SOFFormBody {
    {
        fieldTypes = new int[] {1};
        fieldLengths = new int[]{8};
    }

    private String M612MSG;

    public String getM612MSG() {
        return M612MSG;
    }

    public void setM612MSG(String m612MSG) {
        M612MSG = m612MSG;
    }
}
