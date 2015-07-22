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
public class TOA2002201 extends TOA implements Serializable {
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
          05	ҵ������	            80
          06	֤������    	        30
          07	֤������                255
          08    Ԥ���ʽ���ƽ̨��ˮ    16
        */
        public String RES_CONTEXT;             // ������������

        public String REMARK;                   // ��ע
    }
}
