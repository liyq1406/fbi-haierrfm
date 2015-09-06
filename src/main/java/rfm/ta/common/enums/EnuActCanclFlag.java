package rfm.ta.common.enums;

import java.util.Hashtable;

/*
记账冲正标志：
    0和空：记账未冲正；
    1：记账冲正；
 */
public enum EnuActCanclFlag {
    ACT_CANCL0("0", "记账未冲正"),
    ACT_CANCL1("1", "记账冲正");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuActCanclFlag> aliasEnums;

    EnuActCanclFlag(String code, String title) {
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

    public static EnuActCanclFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
