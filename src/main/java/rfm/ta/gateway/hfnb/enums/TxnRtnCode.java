package rfm.ta.gateway.hfnb.enums;


import java.util.Hashtable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

public enum TxnRtnCode {
    TXN_PROCESSED("0000", "服务器接收解析成功"),

    MSG_ANALYSIS_ILLEGAL("1000", "报文解析错误"),

    MSG_VERIFY_MAC_ILLEGAL("2001", "报文MAC校验错"),
    MSG_VERIFY_USER_ILLEGAL("2002", "报文错误：用户不存在"),
    MSG_VERIFY_DATE_ILLEGAL("2002", "报文错误：交易日期错"),

    TXN_NOT_EXIST("3001", "交易不可用：交易不存在"),
    TXN_NOT_ALLOWED("3002", "交易不可用：不允许使用"),
    TXN_NOT_OPEN("3003", "交易不可用：接口已关闭"),

    TXN_ACT_CHECK_ERR("4000", "付款账号不允许转出"),
    TXN_SYSID_CHECK_ERR("4001", "系统ID错,不允许交易"),
    TXN_CHECK_ERR("4099", "交易数据未通过闸口校验"),

    SERVER_EXCEPTION("9000", "服务器异常");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TxnRtnCode> aliasEnums;

    TxnRtnCode(String code, String title) {
        this.init(code, title);
    }

    @SuppressWarnings("unchecked")
    private void init(String code, String title) {
        this.code = code;
        this.title = title;
        synchronized (this.getClass()) {
            if (aliasEnums == null) {
                aliasEnums = new Hashtable();
            }
        }
        aliasEnums.put(code, this);
        aliasEnums.put(title, this);
    }

    public static TxnRtnCode valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }

    public String toRtnMsg() {
        return this.code + "|" + this.title;
    }
}
