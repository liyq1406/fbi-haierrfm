package rfm.ta.gateway.fdc.T200;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.ta.gateway.fdc.BaseBean;
import rfm.ta.gateway.fdc.ReqHead;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T2005Req extends BaseBean {
    @XStreamAlias("Head")
    public ReqHead head = new ReqHead();
    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        public String Acct = "";
        public String AcctName = "";
        public String ContractNum = "";
        public String BankSerial = "";
        public String Date = "";
        public String Time = "";
        public String Flag = "";
        public String Type = "";
        public String ToAcctName = "";
        public String ToAcct = "";
        public String ToBankName = "";
        public String Amt = "";
        public String Purpose = "";
    }
}
