package rfm.ta.common.gateway.dep.model.txn;

import rfm.ta.common.gateway.dep.model.base.TIA;
import rfm.ta.common.gateway.dep.model.base.TIABody;
import rfm.ta.common.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * ̩�������ʽ��ܣ��������
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class TIA2001001 extends TIA implements Serializable {
    public  Header header = new Header();
    public  Body body = new Body();

    @Override
    public TIAHeader getHeader() {
        return  header;
    }

    @Override
    public TIABody getBody() {
        return  body;
    }

    //====================================================================
    public  static class Header extends TIAHeader {
    }

    public static class Body extends TIABody {
        /*01	���״���	    4	2001
          02	������д���	2
          03	���д���	    6
          04	���������    14
          05    �ʻ����        1  0��Ԥ�ۼ�ܻ�
          06    ���ר���˺�    30
          07    ���ר������    150
          08	��ˮ��    	    30
          09	����	        10	��ϵͳ���ڼ���
          10	�����	        30
          11	��Ա��	        30
          12	����	        1	1_�������*/
        public String ACC_TYPE;           // �ʻ����       1  0��Ԥ�ۼ�ܻ�
        public String ACC_ID;             // ���ר���˺�   30
        public String ACC_NAME;           // ���ר������   150
        public String TX_DATE;            // ����           10  ��ϵͳ���ڼ���
    }
}
