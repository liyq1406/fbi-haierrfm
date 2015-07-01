package rfm.ta.common.enums;

import java.util.Hashtable;

public enum TaInOutFlag implements EnumApp {
    OUT("0", "支出"),
    IN("1", "收入");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TaInOutFlag> aliasEnums;

    TaInOutFlag(String code, String title) {
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

    public static TaInOutFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
