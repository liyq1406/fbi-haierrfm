package rfm.ta.common.enums;

import java.util.Hashtable;

public enum EnuTaStlType {
    STL_TYPE01("01", "现金"),
    STL_TYPE02("02", "转账"),
    STL_TYPE03("03", "支票");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaStlType> aliasEnums;

    EnuTaStlType(String code, String title) {
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

    public static EnuTaStlType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
