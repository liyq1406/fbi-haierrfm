package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * ̩�������ʽ��ܣ�������
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2001002 extends TIA implements Serializable {
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
          04	��ֹ֤�����    14
          05    ���ר���˺�    30
          06    ���ר������    150
          07	��ˮ��    	    30
          08	����	        10	��ϵͳ���ڼ���
          09	�����	        30
          10	��Ա��	        30
          11	����	        1	1_�������*/
        public String ACC_ID;             // ���ר���˺�   30
        public String ACC_NAME;           // ���ר������   150
        public String TX_DATE;            // ����           10  ��ϵͳ���ڼ���
    }
}
