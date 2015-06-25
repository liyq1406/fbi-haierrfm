package rfm.ta.gateway.sbs.domain.txn;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.sbs.domain.base.AbstractResMsg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QDJG02Res extends AbstractResMsg {

    /*
       ��ҳ��һ�����ϵ�KEY ֵ	64	����ֵ������һ������ķ�ҳ������Ϣ������64 λ�Ҳ��ո񣬡�
       ��ҳ���һ�����ϵ�KEY ֵ	64	 ����ֵ������һ������ķ�ҳ������Ϣ������64 λ�Ҳ��ո�
       "�Ƿ���ʣ������"	1	1 ��ʾû����һҳ 0 ����null ��ʾ����һҳ
     //  ��ϸ����	7	�����ϸ����
       // ����ѭ��
       ** ��ϸ�� 7
       �跽������ 	15	����跽������
       ����������	15	�������������
       �������	1	"1-�ֽ� 2-ת��"
       ������ˮ��	19	���������ˮ��
       ժҪ����	4	���ժҪ����
       ��ע	40	�����ע
       ��������	8	�����������
       ����ʱ��	6	�������ʱ��
     */

    public String thisFirstKey;       //   ��ҳ��һ�����ϵ�KEY ֵ	64	����ֵ������һ������ķ�ҳ������Ϣ������64 λ�Ҳ��ո񣬡�
    public String thisLastKey;        //   ��ҳ���һ�����ϵ�KEY ֵ	64	 ����ֵ������һ������ķ�ҳ������Ϣ������64 λ�Ҳ��ո�
    public String isLast = "1";             //   1 ��ʾû����һҳ 0 ����null ��ʾ����һҳ
//    public String detailCnt;          //   ��ϸ����   7
    // ����ѭ��

    @Override
    public void assembleBodyFields(byte[] buffer) throws IllegalAccessException {
        // thisFirstKey  ��ҳ��һ�����ϵ�KEY ֵ	64	����ֵ������һ������ķ�ҳ������Ϣ������64 λ�Ҳ��ո�
        System.out.println("�������峤�ȡ�" + buffer.length);
        byte[] firstKeyBytes = new byte[64];
        System.arraycopy(buffer, 0, firstKeyBytes, 0, firstKeyBytes.length);
        String firstKeyStr = new String(firstKeyBytes);
        thisFirstKey = firstKeyStr.trim();

        // thisLastKey  ��ҳ���һ�����ϵ�KEY ֵ	64	 ����ֵ������һ������ķ�ҳ������Ϣ������64 λ�Ҳ��ո�
        byte[] lastKeyBytes = new byte[64];
        System.arraycopy(buffer, 64, lastKeyBytes, 0, lastKeyBytes.length);
        String lastKeyStr = new String(lastKeyBytes);
        thisLastKey = lastKeyStr.trim();

        System.out.println("��ҳ��һ�����ϵ�KEY��" + thisFirstKey);
        System.out.println("��ҳ���һ�����ϵ�KEY��" + thisLastKey);


        // isLast 1 ��ʾû����һҳ 0 ����null ��ʾ����һҳ
        isLast = new String(new byte[]{buffer[128]});
        System.out.println("�Ƿ���ʣ�����ݣ�" + isLast);

        // detailCnt ��ϸ����   7
        /* byte[] detailCntBytes = new byte[7];
      System.arraycopy(buffer, 129, detailCntBytes, 0, detailCntBytes.length);
      String detailCntStr = new String(detailCntBytes);
      detailCnt = detailCntStr.trim();
      System.out.println("��ϸ������" + detailCnt);*/

        // int cnt = Integer.parseInt(detailCnt);
        int cnt = 5;
        if (cnt >= 1) {
            byte[] recordsBytes = new byte[buffer.length - 129];
            System.arraycopy(buffer, 129, recordsBytes, 0, recordsBytes.length);
            System.out.println("����ϸ�����ܳ��ȡ�" + recordsBytes.length);
            for (int i = 0; i < cnt; i++) {
                System.out.println("��¼��ţ�" + (i + 1));
                byte[] tmpBytes = new byte[115];
                TxnRecord txnRecord = new TxnRecord();
                System.arraycopy(recordsBytes, tmpBytes.length * i, tmpBytes, 0, tmpBytes.length);
                txnRecord.assembleMyFields(tmpBytes);
                if (txnRecord.seqNo == null || "0000000".equals(txnRecord.seqNo)
                        || StringUtils.isEmpty(txnRecord.seqNo.trim())) {
                    break;
                } else {
                    recordList.add(txnRecord);
                }
            }
        }
    }

    public List<TxnRecord> recordList = new ArrayList<TxnRecord>();

    // ���� 115
    public class TxnRecord {
        public String seqNo;              //  ��ϸ�� 7
        public String debitAmt;           //  �跽������ 	15	����跽������
        public String creditAmt;          //  ����������	15	�������������
        public String txnType;            //  �������	1	"1-�ֽ� 2-ת��"
        public String txnSerialNo;        //  ������ˮ��	19	���������ˮ��
        public String summaryCode;        //  ժҪ����	4	���ժҪ����
        public String remark;             //  ��ע	40	�����ע
        public String txnDate;            //  �������� 8
        public String txnTime;            //  ����ʱ�� 6

        public void assembleMyFields(byte[] buf) throws IllegalAccessException {

            System.out.println("����ϸ���ġ�" + new String(buf));
            Class clazz = TxnRecord.class;
            Field[] fields = clazz.getDeclaredFields();

            int offset = 0;
            int[] fieldLengths = new int[]{7, 15, 15, 1, 19, 4, 40, 8, 6};
//           int[] fieldLengths = new int[]{15, 15, 1, 19, 4, 40, 8, 6};

            int pos = offset;
            for (int i = 0; i < fieldLengths.length; i++) {
                byte[] bytes = new byte[fieldLengths[i]];
                System.arraycopy(buf, pos, bytes, 0, bytes.length);
                fields[i].setAccessible(true);
                String s = new String(bytes);
                System.out.println("��������ϸ������" + fields[i].getName() + " : " + s);
                fields[i].set(this, s);
                pos += bytes.length;
            }
        }
    }
}
