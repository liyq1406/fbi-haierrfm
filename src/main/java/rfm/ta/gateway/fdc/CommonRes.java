package rfm.ta.gateway.fdc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-25
 * Time: ионГ11:30
 * To change this template use File | Settings | File Templates.
 */
@XStreamAlias("root")
public class CommonRes extends BaseBean {
    @XStreamAlias("Head")
    public ResHead head = new ResHead();
}
