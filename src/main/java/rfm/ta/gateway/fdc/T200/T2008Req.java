package rfm.ta.gateway.fdc.T200;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import rfm.ta.gateway.fdc.BaseBean;
import rfm.ta.gateway.fdc.ReqHead;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T2008Req extends BaseBean {
    @XStreamAlias("Head")
    public ReqHead head = new ReqHead();
    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        public String Acct = "";
        public String AcctName = "";
        public String PlanNO = "";
        public String SubmitDate = "";
        public String PlanAmt = "";
        public String PlanNum = "";
        @XStreamImplicit
        public List<Record> recordList = new ArrayList<Record>();

        @XStreamAlias("RecordSet")
        public static class Record {
            public String PlanDetailNO = "";
            public String ToAcctName = "";
            public String ToAcct = "";
            public String ToBankName = "";
            public String Amt = "";
            public String PlanDate = "";
            public String Purpose = "";
            public String Remark = "";
        }
    }

    public static Param.Record getRecord() {
        return new Param.Record();
    }
}
