package rfm.ta.tagateway.tasbs.txn.model.form.re;

import rfm.ta.tagateway.tasbs.core.domain.SOFFormBody;

/**
 * Created by Lichao.W At 2015/6/30 10:23
 * wanglichao@163.com
 */
public class T999 extends SOFFormBody {
    {
        fieldTypes = new int[]{1};
        fieldLengths = new int[]{99};
    }
    private String T999MSG;

    public String getT999MSG() {
        return T999MSG;
    }

    public void setT999MSG(String t999MSG) {
        T999MSG = t999MSG;
    }
}
