package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TOA;
import rfm.ta.gateway.dep.model.base.TOABody;
import rfm.ta.gateway.dep.model.base.TOAHeader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SBS: �������۽����ѯ-Ӧ����
 * User: zhanrui
 * Date: 2012-09-01
 */
public class TOA900n057 extends TOA implements Serializable {
    public  Header header = new Header();
    public  Body body = new Body();

    @Override
    public TOAHeader getHeader() {
        return  header;
    }

    @Override
    public TOABody getBody() {
        return  body;
    }

    //====================================================================
    public  static class Header extends TOAHeader {
    }

    public static class Body extends TOABody {
        public String SUCCNT;
        public String FALCNT;
        public String SUCAMT;
        public String FALAMT;
        public String FLOFLG; //��������־
        public String CURCNT; //��ǰ������
        public String REMARK1;
        public String REMARK2;

        public List<BodyDetail> RET_DETAILS = new ArrayList<BodyDetail>();

        public static class BodyDetail implements Serializable{
            /*
                T541-ACTNUM	�˺�	X(32)
                T541-ACTNAM	����	X(60)
                T541-REASON	ʧ��ԭ��	X(40)
                T541-TXNAMT	���׽��	9(14).99
            */
            public String ACTNUM;
            public String ACTNAM;
            public String REASON;
            public String TXNAMT;
        }
    }
}
