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
public class TOA9902101 extends TOA implements Serializable {
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
          02	����˺�                30
          03    ����˻�����            150
          04	�������	            20  �Է�Ϊ��λ
          05	�տ�����	            90
          06	�տλ�˺�	        30
          07	�տλ����	        150
          08	��Ŀ����	            128
          09	������ҵ����	        256
          10    Ԥ���ʽ���ƽ̨��ˮ    16
        */
        public String ACC_ID;             // 02	����˺�        30
        public String ACC_NAME;           // 03	����˻�����    150
        public String TX_AMT;             // 04   �������	    20  �Է�Ϊ��λ
        public String RECV_BANK;          // 05   �տ�����       90
        public String RECV_ACC_ID;       // 06	�տλ�˺�	30
        public String RECV_ACC_NAME;     // 07   �տλ����	150
        public String PROG_NAME;         // 07   �տλ����	150
        public String COMP_NAME;         // 07   �տλ����	150
    }
}
