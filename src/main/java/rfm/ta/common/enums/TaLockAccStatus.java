package rfm.ta.common.enums;

import java.util.Hashtable;

public enum TaLockAccStatus {
    UN_LOCK("0", "�ⶳ"),
    PART_LOCK("1", "���ֶ���"),
    SINGLE_LOCK("3", "���򶳽�"),
    FULL_LOCK("2","ȫ������");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TaLockAccStatus> aliasEnums;

    TaLockAccStatus(String code, String title) {
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

    public static TaLockAccStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
