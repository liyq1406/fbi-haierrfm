package rfm.qd.gateway.domain.T200;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import rfm.qd.gateway.domain.BaseBean;
import rfm.qd.gateway.domain.ResHead;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 */
@XStreamAlias("root")
public class T2007Res extends BaseBean {
    @XStreamAlias("Head")
    public ResHead head = new ResHead();
}
