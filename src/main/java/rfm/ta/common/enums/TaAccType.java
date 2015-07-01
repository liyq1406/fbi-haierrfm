package rfm.ta.common.enums;

import java.util.Hashtable;

public enum TaAccType implements EnumApp {
    AccType0("0", "≥ı º");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TaAccType> aliasEnums;

    TaAccType(String code, String title) {
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

    public static TaAccType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
