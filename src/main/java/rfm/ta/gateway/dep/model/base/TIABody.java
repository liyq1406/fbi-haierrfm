package rfm.ta.gateway.dep.model.base;

import java.io.Serializable;

/**
 * RFM��DEP���͵�Java����ı�����
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class TIABody  implements Serializable {
    public String BANK_ID;            // ������д���	  2
    public String CITY_ID;            // ���д���	      6
    public String BRANCH_ID;          // �����         30
    public String INITIATOR;          // ����         1   1_�������
}
