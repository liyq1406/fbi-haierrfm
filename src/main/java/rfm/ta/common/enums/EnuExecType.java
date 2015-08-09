package rfm.ta.common.enums;

import java.util.Hashtable;

/*
类型;01房款收入-定金、02计划付款、03退款、04利息、05 房款划转、09其他

 */
public enum EnuExecType {
    EXEC_TYPE_DEBUG("debug", "调试"),
    EXEC_TYPE_PROC("proc", "执行");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuExecType> aliasEnums;

    EnuExecType(String code, String title) {
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

    public static EnuExecType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
