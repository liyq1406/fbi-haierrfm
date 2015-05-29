package rfm.qd.gateway.domain.T200;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.qd.gateway.domain.BaseBean;
import rfm.qd.gateway.domain.ReqHead;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T2004Req extends BaseBean {
    @XStreamAlias("Head")
    public ReqHead head = new ReqHead();
    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        public String Acct = "";
        public String AcctName = "";
        public String BankSerial = "";
        public String Date = "";
        public String Time = "";
        public String Flag = "0";
        public String Type = "02";
        public String ToAcctName = "";
        public String ToAcct = "";
        public String ToBankName = "";
        public String Amt = "";
        public String Purpose = "";
        public String PlanDetailNO = "";
    }

}
