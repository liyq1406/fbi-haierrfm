package rfm.ta.gateway.sbs.domain.txn.model.msg;

/**
 * Created by Lichao.W At 2015/7/9 10:24
 * wanglichao@163.com
 * ̩����������
 */
public class M8873 extends MTia {
     private String ERYDA1 = "";    //ҵ������
     private String BEGNUM = "";   //��ʼ����  "�ý��������������-Ӧ��Ľ�����
                                     //    ��һ����0����n����д�ۼ�n-1��ʵ���յ��ļ�¼��+1��
                                     //    ���磬�����ܹ��ɲ�ѯ��123����ϸ������һ�������������20�ʡ�
                                     //    ��һ��������д000000���յ�Ӧ���յ�2�ʣ�01-20
                                     //    �ڶ���������д000021���յ�Ӧ���յ�20�ʣ�21-40
                                     //    ���һ��������д000121���յ�Ӧ���յ�3�ʣ�121-123"


    public String getERYDA1() {
        return ERYDA1;
    }

    public void setERYDA1(String ERYDA1) {
        this.ERYDA1 = ERYDA1;
    }

    public String getBEGNUM() {
        return BEGNUM;
    }

    public void setBEGNUM(String BEGNUM) {
        this.BEGNUM = BEGNUM;
    }
}
