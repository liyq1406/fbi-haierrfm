package rfm.qd.common.constant;

import java.util.Hashtable;

/**
 * 合同缴款状态标志，[0]-退回;[1]-初始;[2]-审核中;[3]-已审核;[7]-记账中;[9]-记账完成
 * User: zhanrui
 * Date: 11-7-23
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public enum ContractRecvStatus implements EnumApp {
    BACK("0", "退回"),
    INIT("1", "初始"),
    CHECKING("2", "审核中"),
    CHECKED("3", "已审核"),
    ACCOUNT("7", "记账中"),
    ACCOUNTOVER("9", "记账完成");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ContractRecvStatus> aliasEnums;

    ContractRecvStatus(String code, String title) {
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

    public static ContractRecvStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
