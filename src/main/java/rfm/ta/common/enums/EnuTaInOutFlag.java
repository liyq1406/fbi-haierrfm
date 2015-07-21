package rfm.ta.common.enums;

import java.util.Hashtable;

public enum EnuTaInOutFlag {
    IN("1", "½è"),
    OUT("2", "´û");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaInOutFlag> aliasEnums;

    EnuTaInOutFlag(String code, String title) {
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

    public static EnuTaInOutFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
