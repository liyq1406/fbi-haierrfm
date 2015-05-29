package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractResMsg;

public class QDJG03Res extends AbstractResMsg {

    /*
       ���	15
       ������ˮ��	19
     */
    public String amt;
    public String serialNo;

    @Override
    public void assembleBodyFields(byte[] buffer) {
        byte[] amtBytes = new byte[15];
        System.arraycopy(buffer, 0, amtBytes, 0, 15);
        String amtStr = new String(amtBytes);
        amt = amtStr.trim();

        byte[] serialBytes = new byte[19];
        System.arraycopy(buffer, 15, serialBytes, 0, 19);
        String serialStr = new String(serialBytes);
        serialNo = serialStr.trim();

        System.out.println("�����׽�" + amt + "��������ˮ�š�" + serialNo);
    }
}
