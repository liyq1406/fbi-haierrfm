package rfm.ta.gateway.sbs.domain.base;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-25
 * Time: 下午1:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractResMsg {
    protected MsgHeader header = new MsgHeader();
    public String rtnMsg = "";

    public void assembleFields(byte[] buffer) throws IllegalAccessException {
        byte[] headBytes = new byte[59];
        System.arraycopy(buffer, 0, headBytes, 0, 59);
        header.assembleFields(headBytes);
        int bodyLength = Integer.parseInt(header.getDataLength());
        byte[] bodyBytes;
        if (!"00".equals(header.getRtnCode())) {
            bodyBytes = new byte[100];
            System.arraycopy(buffer, 59, bodyBytes, 0, bodyBytes.length);
//           throw new RuntimeException("与核心交易失败。" + new String(bodyBytes));
            rtnMsg = new String(bodyBytes);
        } else {
            bodyBytes = new byte[bodyLength];
            System.arraycopy(buffer, 59, bodyBytes, 0, bodyBytes.length);
            assembleBodyFields(bodyBytes);
        }

    }

    public abstract void assembleBodyFields(byte[] bodyBuffer) throws IllegalAccessException;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }
}
