package rfm.ta.common.gateway.dep.model.txn;

import rfm.ta.common.gateway.dep.model.base.TOA;
import rfm.ta.common.gateway.dep.model.base.TOABody;
import rfm.ta.common.gateway.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * ̩�������ʽ��ܣ�������֤
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class TOA2002001 extends TOA implements Serializable {
    public Header header = new Header();
    public Body body = new Body();

    @Override
    public TOAHeader getHeader() {
        return header;
    }

    @Override
    public TOABody getBody() {
        return body;
    }

    //====================================================================
    public static class Header extends TOAHeader {
    }

    public static class Body extends TOABody {
        /*01	���	                4   0000��ʾ�ɹ�
          02	�ʻ����	            1   0����ܻ���
          03	������	            20  �Է�Ϊ��λ
          04	���ר���˺�            30
          05    ���ר������            150
          06    Ԥ���ʽ���ƽ̨��ˮ    16
        */
        public String ACC_TYPE;           // �ʻ����       1  0��Ԥ�ۼ�ܻ�
        public String TX_AMT;             // ������	      20  �Է�Ϊ��λ
        public String ACC_ID;             // ���ר���˺�   30
        public String ACC_NAME;           // ���ר������   150
    }
}
