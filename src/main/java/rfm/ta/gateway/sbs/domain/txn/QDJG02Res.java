package rfm.ta.gateway.sbs.domain.txn;

import org.apache.commons.lang.StringUtils;
import rfm.ta.gateway.sbs.domain.base.AbstractResMsg;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class QDJG02Res extends AbstractResMsg {

    /*
       本页第一笔资料的KEY 值	64	将该值送入下一笔请求的翻页控制信息，不足64 位右补空格，。
       本页最后一笔资料的KEY 值	64	 将该值送入下一笔请求的翻页控制信息，不足64 位右补空格
       "是否还有剩余数据"	1	1 表示没有下一页 0 或者null 表示有下一页
     //  明细笔数	7	输出明细笔数
       // 以下循环
       ** 明细号 7
       借方发生额 	15	输出借方发生额
       贷方发生额	15	输出贷方发生额
       交易类别	1	"1-现金 2-转账"
       交易流水号	19	输出交易流水号
       摘要代码	4	输出摘要代码
       备注	40	输出备注
       交易日期	8	输出交易日期
       交易时间	6	输出交易时间
     */

    public String thisFirstKey;       //   本页第一笔资料的KEY 值	64	将该值送入下一笔请求的翻页控制信息，不足64 位右补空格，。
    public String thisLastKey;        //   本页最后一笔资料的KEY 值	64	 将该值送入下一笔请求的翻页控制信息，不足64 位右补空格
    public String isLast = "1";             //   1 表示没有下一页 0 或者null 表示有下一页
//    public String detailCnt;          //   明细笔数   7
    // 以下循环

    @Override
    public void assembleBodyFields(byte[] buffer) throws IllegalAccessException {
        // thisFirstKey  本页第一笔资料的KEY 值	64	将该值送入下一笔请求的翻页控制信息，不足64 位右补空格，
        System.out.println("【报文体长度】" + buffer.length);
        byte[] firstKeyBytes = new byte[64];
        System.arraycopy(buffer, 0, firstKeyBytes, 0, firstKeyBytes.length);
        String firstKeyStr = new String(firstKeyBytes);
        thisFirstKey = firstKeyStr.trim();

        // thisLastKey  本页最后一笔资料的KEY 值	64	 将该值送入下一笔请求的翻页控制信息，不足64 位右补空格
        byte[] lastKeyBytes = new byte[64];
        System.arraycopy(buffer, 64, lastKeyBytes, 0, lastKeyBytes.length);
        String lastKeyStr = new String(lastKeyBytes);
        thisLastKey = lastKeyStr.trim();

        System.out.println("本页第一笔资料的KEY：" + thisFirstKey);
        System.out.println("本页最后一笔资料的KEY：" + thisLastKey);


        // isLast 1 表示没有下一页 0 或者null 表示有下一页
        isLast = new String(new byte[]{buffer[128]});
        System.out.println("是否还有剩余数据：" + isLast);

        // detailCnt 明细笔数   7
        /* byte[] detailCntBytes = new byte[7];
      System.arraycopy(buffer, 129, detailCntBytes, 0, detailCntBytes.length);
      String detailCntStr = new String(detailCntBytes);
      detailCnt = detailCntStr.trim();
      System.out.println("明细笔数：" + detailCnt);*/

        // int cnt = Integer.parseInt(detailCnt);
        int cnt = 5;
        if (cnt >= 1) {
            byte[] recordsBytes = new byte[buffer.length - 129];
            System.arraycopy(buffer, 129, recordsBytes, 0, recordsBytes.length);
            System.out.println("【明细报文总长度】" + recordsBytes.length);
            for (int i = 0; i < cnt; i++) {
                System.out.println("记录序号：" + (i + 1));
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

    // 长度 115
    public class TxnRecord {
        public String seqNo;              //  明细号 7
        public String debitAmt;           //  借方发生额 	15	输出借方发生额
        public String creditAmt;          //  贷方发生额	15	输出贷方发生额
        public String txnType;            //  交易类别	1	"1-现金 2-转账"
        public String txnSerialNo;        //  交易流水号	19	输出交易流水号
        public String summaryCode;        //  摘要代码	4	输出摘要代码
        public String remark;             //  备注	40	输出备注
        public String txnDate;            //  交易日期 8
        public String txnTime;            //  交易时间 6

        public void assembleMyFields(byte[] buf) throws IllegalAccessException {

            System.out.println("【明细报文】" + new String(buf));
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
                System.out.println("【交易明细】【域】" + fields[i].getName() + " : " + s);
                fields[i].set(this, s);
                pos += bytes.length;
            }
        }
    }
}
