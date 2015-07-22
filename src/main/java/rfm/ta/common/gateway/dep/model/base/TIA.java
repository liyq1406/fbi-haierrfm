package rfm.ta.common.gateway.dep.model.base;

import java.io.Serializable;

/**
 * RFM往DEP发送的Java对象（抽象类）
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public abstract class TIA implements Serializable {
    public abstract TIAHeader getHeader();
    public abstract TIABody getBody();
}
