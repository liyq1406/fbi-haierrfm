package rfm.ta.gateway.dep.model.txn;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.ta.gateway.dep.model.base.TOA;
import rfm.ta.gateway.dep.model.base.TOABody;
import rfm.ta.gateway.dep.model.base.TOAHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 银联：批量多笔代扣
 */
public class TOA1001003 extends TOA implements Serializable {
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
        public List<BodyDetail> RET_DETAILS = new ArrayList<BodyDetail>();

        @XStreamAlias("RET_DETAIL")
        public static class BodyDetail implements Serializable {
            public String SN = "";
            public String RET_CODE = "";
            public String ERR_MSG = "";
        }
    }
}
