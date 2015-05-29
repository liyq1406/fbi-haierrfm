package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractResMsg;

public class QDJG01Res extends AbstractResMsg {

    /*
       ’Àªß”‡∂Ó£¨ø…”√”‡∂Ó  15
     */
    public String actbal;
    public String avabal;
    public String rtnMsg;

    @Override
    public void assembleBodyFields(byte[] buffer) {
        byte[] actbalBytes = new byte[15];
        System.arraycopy(buffer, 0, actbalBytes, 0, 15);
        String actbalStr = new String(actbalBytes);
        actbal = actbalStr.trim();
        System.out.println("°æ”Ú°øactbal:" + actbal);

        byte[] avabalBytes = new byte[15];
        System.arraycopy(buffer, 15, avabalBytes, 0, 15);
        String avabalStr = new String(avabalBytes);
        avabal = avabalStr.trim();
        System.out.println("°æ”Ú°øavabal:" + avabal);
    }
}
