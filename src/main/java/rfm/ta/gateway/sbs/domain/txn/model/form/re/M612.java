package rfm.ta.gateway.sbs.domain.txn.model.form.re;

import rfm.ta.gateway.sbs.domain.core.domain.SOFFormBody;

/**
 * Created by Lichao.W At 2015/6/30 9:19
 * wanglichao@163.com
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
