package rfm.ta.gateway.sbs.domain.txn.model.msg;

/**
 * Created by Lichao.W At 2015/7/9 10:24
 * wanglichao@163.com
 * ̩���������� ����
 */
public class M8872 extends MTia {
     private String ERYDA1 = "";    //ҵ������

    public M8872() {
    }

    public M8872(String ERYDA1) {
        this.ERYDA1 = ERYDA1;
    }

    public String getERYDA1() {
        return ERYDA1;
    }

    public void setERYDA1(String ERYDA1) {
        this.ERYDA1 = ERYDA1;
    }
}
