package org.fbi.dep.model.base;

import java.io.Serializable;

/**
 * DEP��RFM���͵�Java����ı���ͷ
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class TOAHeader implements Serializable {
    public String CHANNEL_ID;                // ����
    public String APP_ID;                    // Ӧ��ID
    public String BIZ_ID;                    // ҵ��ID
    public String REQ_SN;                    // ��ˮ�� �ͻ���Ӧ��ϵͳ��Ψһ   ��ˮ 16
    public String TX_CODE;                   // ����������
    public String RETURN_CODE;               // ������Ӧ��                   ��� 4 0000��ʾ�ɹ�
    public String RETURN_MSG;                // ������Ӧ��Ϣ                 ���� 60
    public String SIGNED_MSG;                // ���ױ���ǩ����Ϣ
}
