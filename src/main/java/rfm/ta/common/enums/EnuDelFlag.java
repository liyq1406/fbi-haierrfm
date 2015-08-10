package rfm.ta.common.enums;

import java.util.Hashtable;

/*
É¾³ý±êÖ¾£º
    0£ºÎ´É¾³ý£»
    1£ºÉ¾³ý£»
 */
public enum EnuDelFlag {
    DEL_FALSE("0", "Î´É¾³ý"),
    DEL_TRUE("1", "É¾³ý");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuDelFlag> aliasEnums;

    EnuDelFlag(String code, String title) {
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

    public static EnuDelFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
