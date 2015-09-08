package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 对账分类;[1]-指令对账;[2]-明细对账;
 */
public enum EnuClassifyFlag {
    CLASSIFY_FLAG1("1","指令对账"),
    CLASSIFY_FLAG2("2","明细对账");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuClassifyFlag> aliasEnums;

    EnuClassifyFlag(String code, String title){
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

    public static EnuClassifyFlag getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
