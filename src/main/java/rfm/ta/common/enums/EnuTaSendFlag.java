package rfm.ta.common.enums;

import java.util.Hashtable;

public enum EnuTaSendFlag {
    UN_SEND("0", "Î´·¢ËÍ"),
    SENT("1", "ÒÑ·¢ËÍ");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaSendFlag> aliasEnums;

    EnuTaSendFlag(String code, String title) {
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

    public static EnuTaSendFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
