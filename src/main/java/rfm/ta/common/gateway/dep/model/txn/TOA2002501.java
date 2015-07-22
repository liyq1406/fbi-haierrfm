package rfm.ta.common.gateway.dep.model.txn;

import rfm.ta.common.gateway.dep.model.base.TOA;
import rfm.ta.common.gateway.dep.model.base.TOABody;
import rfm.ta.common.gateway.dep.model.base.TOAHeader;

import java.io.Serializable;

/**
 * ̩�������ʽ��ܣ����˽����ѯ
 * User: hanjianlong
 * Date: 2015-07-16
 */
public class TOA2002501 extends TOA implements Serializable {
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
          02	���˽��	            1   0_�ɹ� 1_ʧ��
          03	Ԥ���ʽ���ƽ̨������ˮ16  ҵ�������ˮ
          04	������м�����ˮ	    30  ҵ�������ˮ
          05	Ԥ���ʽ���ƽ̨��ˮ	16
        */
        public String RES_CONTEXT;             // ������������

        public String REMARK;                   // ��ע
    }
}
