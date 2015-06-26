package rfm.ta.common.enums;

import java.util.Hashtable;

public enum AccountStatus implements EnumApp {
    INIT("N", "初始"),
    WATCH("0", "监管中"),
    CLOSE("1", "撤销监管");

    private String code = null;
    private String title = null;
    private static Hashtable<String, AccountStatus> aliasEnums;

    AccountStatus(String code, String title) {
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

    public static AccountStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
