package rfm.ta.common.gateway.dep.model.txn;

import rfm.ta.common.gateway.dep.model.base.TIA;
import rfm.ta.common.gateway.dep.model.base.TIABody;
import rfm.ta.common.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * ̩�������ʽ��ܣ�������֤
 * User: hanjianlong
 * Date: 2015-07-16
 */

public class TIA2002001 extends TIA implements Serializable {
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
          04	����������    14
          05	��֤��ˮ    	30
          06	��֤����	    10	��ϵͳ���ڼ���
          07	��֤����	    30
          08	��֤��Ա	    30
          09	����	        1	1_�������*/
        public String TX_DATE;            // ����           10  ��ϵͳ���ڼ���
    }
}
