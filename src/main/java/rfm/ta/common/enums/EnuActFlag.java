package rfm.ta.common.enums;

import java.util.Hashtable;

/*
记账成功标志：
    0：记账成功；
    1：不明原因；
    2：记账失败；
 */
public enum EnuActFlag {
    ACT_SUCCESS("0", "记账成功"),
    ACT_UNKNOWN("1", "不明原因"),
    ACT_FAIL("2", "记账失败");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuActFlag> aliasEnums;

    EnuActFlag(String code, String title) {
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

    public static EnuActFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
