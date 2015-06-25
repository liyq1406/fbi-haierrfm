package rfm.ta.gateway.fdc.T000;

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
public class T0007Req extends BaseBean {
    @XStreamAlias("Head")
    public ReqHead head = new ReqHead();
    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        @XStreamImplicit
        public List<Record> recordList = new ArrayList<Record>();

        @XStreamAlias("RecordSet")
        public static class Record {
            public String BankSerial = "";
            public String Date = "";
            public String Time = "";
            public String Flag = "";
            public String Type = "";
            public String ContractNum = "";
            public String PlanDetailNO = "";
            public String AcctName = "";
            public String Acct = "";
            public String ToName = "";
            public String ToAcct = "";
            public String ToBankName = "";
            public String Amt = "";
            public String Purpose = "";
        }
    }

    public static Param.Record getRecord() {
        return new Param.Record();
    }
}
