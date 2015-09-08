package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 状态标志：[0]-日间对账获取中;[1]-日间对账获取完成；[2]日间对账不平；[3]日间对账平；[4]日间对账发送成功；
 *          [5]余额对账获取中；[6]余额对账获取完成；[7]余额对账发送成功；
 */
public enum EnuStatusFlag {
    STATUS_FLAG0("0","日间对账获取中"),
    STATUS_FLAG1("1","日间对账获取完成"),
    STATUS_FLAG2("2","日间对账不平"),
    STATUS_FLAG3("3","日间对账平"),
    STATUS_FLAG4("4","日间对账发送成功"),
    STATUS_FLAG5("5","余额对账获取中"),
    STATUS_FLAG6("6","余额对账获取完成"),
    STATUS_FLAG7("7","余额对账发送成功");

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
