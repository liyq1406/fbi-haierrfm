package rfm.ta.common.gateway.dep.model.base;

import java.io.Serializable;

/**
 * DEP��RFM���͵�Java����ı���ͷ����
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015/07/21
 * Time: 15:02
 */
public class ToaXmlHeader implements Serializable {
    public String TRX_CODE;                // ����������
    public String REQ_SN;                  // ��ˮ�� �ͻ���Ӧ��ϵͳ��Ψһ
    public String WSYS_ID;                 // ����ϵͳID
    public String RET_CODE;                // ������Ӧ��
    public String ERR_MSG;                 // ������Ӧ��Ϣ
}
