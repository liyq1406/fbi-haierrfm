package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 状态标志，[0]-初始;[1]-完成
 */
public enum EnuStatusFlag {
    STATUS_FLAG0("0","初始"),
    STATUS_FLAG1("1","完成");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuStatusFlag> aliasEnums;

    EnuStatusFlag(String code, String title){
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

    public static EnuStatusFlag getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
