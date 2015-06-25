package rfm.ta.gateway.fdc.T200;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.ta.gateway.fdc.BaseBean;
import rfm.ta.gateway.fdc.ResHead;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T2001Res extends BaseBean {
    @XStreamAlias("Head")
    public ResHead head = new ResHead();
}
