package rfm.qd.gateway.domain.T000;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import rfm.qd.gateway.domain.BaseBean;
import rfm.qd.gateway.domain.ResHead;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T0002Res extends BaseBean {
    @XStreamAlias("Head")
    public ResHead head = new ResHead();
    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        public String DetailNum = "";
        @XStreamImplicit
        public List<Record> recordList = new ArrayList<Record>();

        @XStreamAlias("RecordSet")
        public static class Record {
            public String Date = "";
            public String Time = "";
            public String Flag = "";
            public String Type = "";
            public String ContractNum = "";
            public String PlanDetailNO = "";
            public String ToAcctName = "";
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
