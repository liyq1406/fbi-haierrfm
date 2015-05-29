package rfm.qd.gateway.domain.T200;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.qd.gateway.domain.BaseBean;
import rfm.qd.gateway.domain.ReqHead;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T2007Req extends BaseBean {
    @XStreamAlias("Head")
    public ReqHead head = new ReqHead();
    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        public String Acct = "";
        public String AcctName = "";
        public String BuyerName = "";
        public String BuyerAcct = "";
        public String BuyerBankName = "";
        public String BuyerIDType = "";
        public String BuyerIDCode = "";
        public String ContractNum = "";
        public String TotalAmt = "";
        public String HouseAddress = "";
        public String EndReason = "";
        public String TransBuyerAmt = "";
    }

}
