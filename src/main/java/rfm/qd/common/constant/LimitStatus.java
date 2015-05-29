package rfm.qd.common.constant;

import java.util.Hashtable;

public enum LimitStatus implements EnumApp {
    NOT_LIMIT("0", "未限制"),
    LIMITED("1", "限制");

    private String code = null;
    private String title = null;
    private static Hashtable<String, LimitStatus> aliasEnums;

    LimitStatus(String code, String title) {
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

    public static LimitStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
