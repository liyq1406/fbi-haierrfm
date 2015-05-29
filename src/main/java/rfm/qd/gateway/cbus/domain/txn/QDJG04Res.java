package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractResMsg;

public class QDJG04Res extends AbstractResMsg {

    /*
       �㻮���	RMT-AMT-2	C(15)
       ֧���������	FM-TRNT-AMT-SQ-NO	C(8)
       ������ˮ�� 19
     */
    public String rmtAmt;
    public String fmTrntAmtSqNo;
    public String txnSerailNo;

    @Override
    public void assembleBodyFields(byte[] buffer) {
        byte[] amtBytes = new byte[15];
        System.arraycopy(buffer, 0, amtBytes, 0, 15);
        String amtStr = new String(amtBytes);
        rmtAmt = amtStr.trim();
        System.out.println("������" + rmtAmt);

        byte[] txnSqBytes = new byte[8];
        System.arraycopy(buffer, 15, txnSqBytes, 0, 8);
        String txnSqStr = new String(txnSqBytes);
        fmTrntAmtSqNo = txnSqStr.trim();
        System.out.println("��֧����š�" + fmTrntAmtSqNo);

        byte[] serialBytes = new byte[19];
        System.arraycopy(buffer, 23, serialBytes, 0, 19);
        String serialStr = new String(serialBytes);
        txnSerailNo = serialStr.trim();

        System.out.println("��������ˮ�š�" + txnSerailNo);
    }

}
