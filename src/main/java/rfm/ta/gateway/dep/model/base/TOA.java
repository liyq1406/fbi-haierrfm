package rfm.ta.gateway.dep.model.base;

import java.io.Serializable;

/**
 * DEP��RFM���͵�Java���󣨳����ࣩ
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public abstract class TOA implements Serializable {
    public abstract TOAHeader getHeader();
    public abstract TOABody getBody();
}
