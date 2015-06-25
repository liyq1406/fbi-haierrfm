package rfm.ta.common.enums;

import rfm.qd.common.constant.EnumApp;

import java.util.Hashtable;

public enum ChangeFlag implements EnumApp {
    NORMAL("N", "Õý³£"),
    CANCEL("R", "³åÕý"),
    BACK("D", "ÍËÆ±"),
    AP_CANCEL("A", "ÉêÇë³åÕý"),
    AP_BACK("B", "ÉêÇëÍËÆ±");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ChangeFlag> aliasEnums;

    ChangeFlag(String code, String title) {
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

    public static ChangeFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
