package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;

/**
 * ̩�������ʽ��ܣ���������
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2002102 extends TIA implements Serializable {
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
        /*01	���״���	    4	2102
          02	������д���	2
          03	���д���	    6
          04	����ҵ����	14
          05	��������	    32	MD5
          06	����˺�	    30	������֤�������
          07	�տλ�˺�	30	������֤�������
          08	�����ʽ�	    20	������֤�������
          09	���㷽ʽ	    2	01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
          10	֧Ʊ����	    30
          11	���м�����ˮ	30
          12	��������	    10	��ϵͳ���ڼ���
          13	��������	    30
          14	������Ա	    30
          15	����	        1	1_�������*/

        public String ACC_ID;             // 06	����˺�	    30	������֤�������
        public String RECV_ACC_ID;       // 07	�տλ�˺�	30	������֤�������
        public String TX_AMT;             // 08	�����ʽ�	    20	������֤�������
        public String STL_TYPE;           // 09   ���㷽ʽ	    2	01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
        public String CHECK_ID;           // 10   ֧Ʊ����	    30
        public String TX_DATE;            // ����           10  ��ϵͳ���ڼ���
    }
}
