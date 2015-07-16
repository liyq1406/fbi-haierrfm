package rfm.ta.gateway.dep.model.txn;

import rfm.ta.gateway.dep.model.base.TIA;
import rfm.ta.gateway.dep.model.base.TIABody;
import rfm.ta.gateway.dep.model.base.TIAHeader;

/**
 * ����������
 * User: zhanrui
 * Date: 2012-01-31
 */

public class TIA1001001 extends TIA {
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
        public String BANK_CODE;                 // ���д���
        public String ACCOUNT_TYPE;              // �˺����� 00-���п���01-����
        public String ACCOUNT_NO;                // �˺�
        public String ACCOUNT_NAME;              // �˻���

        public String AMOUNT;                    // ���׽��(С���������λ)
        public String PROVINCE;                  // ����������ʡ
        public String CITY;                      // ������������
        public String ACCOUNT_PROP;              // �˺�����  0-���� 1-��˾
        public String REMARK;                    // ��ע
    }
}
