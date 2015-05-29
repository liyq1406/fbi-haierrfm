package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractReqMsg;
import org.apache.commons.lang.StringUtils;

public class QDJG02Req extends AbstractReqMsg {

    /*
       0-��һ��
    ��ҳ��һ�����ϵ�KEY ֵ	64	����һ�ʵķ��ر��Ļ�ȡ
    ��ҳ���һ�����ϵ�KEY ֵ	64	����һ�ʵķ��ر��Ļ�ȡ
    ��ҳ�����	1	1 ��ʾ�Ϸ� 2 ��ʾ�·�����̶�ʹ��2��
    �ʺ�                                                  	28	����룬�Ҳ��ո�
     ��ʼ��ϸ��                                      	7	����룬�Ҳ��ո��״�Ϊ1������Ϊǰһ�����һ����ϸ��+1��
    ��ʼ����	8	����룬�Ҳ��ո�
    �Ķ��ʵ���־	1	�����ͣ����ո�
    ����	16	Ĭ��Ϊ�˺ţ�����룬�Ҳ��ո�
    ��ֹ����	8	����룬�Ҳ��ո�
    ����	1	1 �˻���ϸ��ѯ
     */

    public String firstFlag = "0";         // 0-��һ��
    public String preFirstKey = "0";       //  ��ҳ��һ�����ϵ�KEY ֵ	64	����һ�ʵķ��ر��Ļ�ȡ
    public String preLastKey = "0";        //  ��ҳ���һ�����ϵ�KEY ֵ	64	����һ�ʵķ��ر��Ļ�ȡ
    public String turnPageNo = "2";  //  ��ҳ����� ����1  �̶�ֵ2
    public String accountNo = "";         //  �ʺ�  28
    public String startSeqNo = "1";     //   ��ʼ��ϸ��  7 ���״�Ϊ1������Ϊǰһ�����һ����ϸ��+1��
    public String startDate = "";         //  ��ʼ����  8
    public String chkActPrFlag = " "; // �Ķ��ʵ���־  ����1 ���ո�
    public String password = "";           // ����	16	Ĭ��Ϊ�˺ţ�����룬�Ҳ��ո�
    public String endDate = "";            // ��ֹ����	8	����룬�Ҳ��ո�
    public String function = "1";     // ����	1	1 �˻���ϸ��ѯ

    public String bodyToString() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(firstFlag);
        strBuilder.append(StringUtils.rightPad(preFirstKey, 64, ' '));
        strBuilder.append(StringUtils.rightPad(preLastKey, 64, ' '));
        strBuilder.append(turnPageNo);
        strBuilder.append(StringUtils.rightPad(accountNo, 28, ' '));
        strBuilder.append(StringUtils.rightPad(startSeqNo, 7, ' '));
        strBuilder.append(StringUtils.rightPad(startDate, 8, ' '));
        strBuilder.append(chkActPrFlag);
        strBuilder.append(StringUtils.rightPad(password, 16, ' '));
        strBuilder.append(StringUtils.rightPad(endDate, 8, ' '));
        strBuilder.append(function);

        return strBuilder.toString();
    }
}