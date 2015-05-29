package rfm.qd.common.constant;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-7-23
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public enum ContractStatus implements EnumApp {
    NORMAL("1", "正常进行"),
    TRANS("6", "撤销待退款"),
    CANCELING("7", "撤销中"),
    END("8", "交易完成"),
    CANCEL("9", "撤销终止");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ContractStatus> aliasEnums;

    ContractStatus(String code, String title) {
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

    public static ContractStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
