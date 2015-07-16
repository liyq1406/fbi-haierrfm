package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

import java.io.Serializable;

/**
 * SBS �������۽����ѯ-������
 * User: zhanrui
 * Date: 2012-09-01
 */

public class TIA900n052 extends TIA implements Serializable {
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
        /*
        CTF-FBTIDX	��Χϵͳ��ˮ��	X(18)	����룬����ʱ�Ҳ��ո�
        CTF-ADVNUM	ԭ�������к�	9(16)
        CTF-ORDDAT	��������	X(8)
        CTF-BEGNUM	��ʼ����	9(6)	�Ҷ��룬��0
        */
        public String FBTIDX;
        public String ADVNUM;
        public String ORDDAT;
        public String BEGNUM = "000001";
    }

}
