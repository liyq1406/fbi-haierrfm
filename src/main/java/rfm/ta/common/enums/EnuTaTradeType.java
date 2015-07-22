package rfm.ta.common.enums;

import java.util.Hashtable;

/*
类型;01房款收入-定金、02计划付款、03退款、04利息、05 房款划转、09其他

 */
public enum EnuTaTradeType {
    TRADE_INCOME("01", "交存"),
    TRADE_PAYMENT("02", "划拨"),
    TRADE_BACK("03", "返还"),
    TRADE_OTHERS("04", "其他");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaTradeType> aliasEnums;

    EnuTaTradeType(String code, String title) {
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

    public static EnuTaTradeType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
