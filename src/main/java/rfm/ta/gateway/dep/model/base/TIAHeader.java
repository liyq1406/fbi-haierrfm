package rfm.ta.gateway.dep.model.base;

import java.io.Serializable;

/**
 * RFM��DEP���͵�Java����ı���ͷ
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class TIAHeader implements Serializable {
    public String CHANNEL_ID;                // ����
    public String APP_ID;                    // Ӧ��ID
    public String BIZ_ID;                    // ҵ��ID                       ���������  14
    public String REQ_SN;                    // ��ˮ�� �ͻ���Ӧ��ϵͳ��Ψһ  ��ˮ��        30
    public String USER_ID;                   // ����ԱID                     ��Ա��        30
    public String PASSWORD;                  // ����Ա����                   ��������      32
    public String TX_CODE;                   // ����������                   ���״���       4
    public String SIGNED_MSG;                // ���ױ���ǩ����Ϣ
}
