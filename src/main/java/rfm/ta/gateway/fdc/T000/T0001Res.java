package rfm.ta.gateway.fdc.T000;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.ta.gateway.fdc.BaseBean;
import rfm.ta.gateway.fdc.ResHead;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T0001Res extends BaseBean {
    @XStreamAlias("Head")
    public ResHead head = new ResHead();

    @XStreamAlias("Param")
    public Param param = new Param();

    public static class Param {
        public String Balance = "";
        public String UsableBalance = "";
    }
}
