package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * ̩�������ʽ��ܣ��������
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2002002 extends TIA implements Serializable {
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
        /*01	���״���	    4	2002
          02	������д���	2
          03	���д���	    6
          04	����������	14
          05	������	    32	MD5
          06	����˺�	    30	������֤�������
          07	���㷽ʽ	    2	01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
          08	֧Ʊ����	    30
          09	���м�����ˮ	30
          10	��������	    10	��ϵͳ���ڼ���
          11	��������	    30
          12	������Ա	    30
          13	����	        1	1_�������*/
        public String REQ_CONTEXT;             // ������������

        public String REMARK;                   // ��ע
    }
}