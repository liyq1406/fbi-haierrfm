package rfm.ta.common.enums;

import java.util.Hashtable;

public enum EnuTaBankId {
    BANK_01("", ""),
    BANK_02("", ""),
    BANK_03("", ""),
    BANK_04("", ""),
    BANK_05("", ""),
    BANK_06("", ""),
    BANK_07("", ""),
    BANK_08("", ""),
    BANK_09("", ""),
    BANK_10("", ""),
    BANK_11("", ""),
    BANK_HAIER("12", "º£¶û");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaBankId> aliasEnums;

    EnuTaBankId(String code, String title) {
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

    public static EnuTaBankId valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
