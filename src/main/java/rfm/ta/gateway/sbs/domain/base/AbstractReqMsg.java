package rfm.ta.gateway.sbs.domain.base;

import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-25
 * Time: ÏÂÎç5:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractReqMsg {
    protected MsgHeader header = new MsgHeader();

    public abstract String bodyToString();

    {
        header.setTxnCode(this.getClass().getSimpleName().substring(0, 6));
    }

    public String toString() {
        String bodyStr = bodyToString();
        header.setDataLength(StringUtils.leftPad("" + bodyStr.getBytes().length, 4, "0"));
        String headerStr = header.toString();
        String totalLength = StringUtils.leftPad("" + (headerStr + bodyStr).getBytes().length, 6, "0");
        return totalLength + headerStr + bodyStr;
    }

    // -----------------------------------

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }
}
