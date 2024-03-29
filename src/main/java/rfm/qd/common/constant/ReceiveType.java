package rfm.qd.common.constant;

import java.util.Hashtable;

/**
 * 交款类型。[1]-定金;[2]-首付;[3]-贷款;[9]-其他。
 * User: zhanrui
 * Date: 11-7-23
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public enum ReceiveType implements EnumApp {
    DEPOSIT("1", "定金"),
    DOWN_PAYMENT("2", "首付"),
    LOAN("3", "贷款"),
    OTHER("9", "其他");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ReceiveType> aliasEnums;

    ReceiveType(String code, String title) {
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

    public static ReceiveType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
