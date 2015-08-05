package rfm.ta.common.enums;

import java.util.Hashtable;

public enum EnuTaAccStatus {
    ACC_INIT("0", "初始"),
    ACC_SUPV("1", "监管中"),
    ACC_CANCL("2", "撤销监管");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaAccStatus> aliasEnums;

    EnuTaAccStatus(String code, String title) {
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

    public static EnuTaAccStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
