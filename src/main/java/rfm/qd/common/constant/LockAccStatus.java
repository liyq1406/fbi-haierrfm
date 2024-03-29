package rfm.qd.common.constant;

import java.util.Hashtable;

public enum LockAccStatus implements EnumApp {
    UN_LOCK("0", "解冻"),
    PART_LOCK("1", "部分冻结"),
    SINGLE_LOCK("3", "单向冻结"),
    FULL_LOCK("2","全部冻结");

    private String code = null;
    private String title = null;
    private static Hashtable<String, LockAccStatus> aliasEnums;

    LockAccStatus(String code, String title) {
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

    public static LockAccStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
