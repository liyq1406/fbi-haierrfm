package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * Created by XIANGYANG on 2015-7-1.
 */

public enum EnuTaTxnRtnCode {
    TXN_PROCESSED("0000", "交易成功"),
    MSG_ANALYSIS_ILLEGAL("1000", "交易失败"),
    SERVER_EXCEPTION("9000", "服务器异常");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaTxnRtnCode> aliasEnums;

    EnuTaTxnRtnCode(String code, String title) {
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

    public static EnuTaTxnRtnCode valueOfAlias(String alias) {
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
