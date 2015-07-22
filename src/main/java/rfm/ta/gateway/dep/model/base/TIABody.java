package rfm.ta.gateway.dep.model.base;

import java.io.Serializable;

/**
 * RFM往DEP发送的Java对象的报文体
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class TIABody  implements Serializable {
    public String BANK_ID;            // 监管银行代码	  2
    public String CITY_ID;            // 城市代码	      6
    public String BRANCH_ID;          // 网点号         30
    public String INITIATOR;          // 发起方         1   1_监管银行
}
