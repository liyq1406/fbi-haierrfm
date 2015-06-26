package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 处理结果。[0]-不通过;[1]-通过;[2]-提交;[3]-新建
 * User: zhanrui
 * Date: 11-7-23
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public enum WorkResult implements EnumApp {
    NOTPASS("0", "退回"),
    PASS("1", "已复核"),
    COMMIT("2", "已入账"),
    RE_CHECK("6", "二次复核"),
    NOT_KNOWN("5", "结果不明"),
    CREATE("3", "联机记账"),
    SENT("4", "已发送");

    private String code = null;
    private String title = null;
    private static Hashtable<String, WorkResult> aliasEnums;

    WorkResult(String code, String title) {
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

    public static WorkResult valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
