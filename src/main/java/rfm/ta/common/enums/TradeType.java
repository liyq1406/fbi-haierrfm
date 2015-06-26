package rfm.ta.common.enums;

import java.util.Hashtable;

/*
类型;01房款收入-定金、02计划付款、03退款、04利息、05 房款划转、09其他

 */
public enum TradeType implements EnumApp {
    HOUSE_INCOME("01", "房款收入"),
    HOUSE_DOWN_PAYMENT("07", "首付"),
    HOUSE_CREDIT("08", "按揭贷款"),
    PLAN_PAYOUT("02", "计划付款"),
    TRANS_BACK("03", "退款"),
    INTEREST("04", "利息"),
    HOUSE_TRANS("05", "房款划转"),
    OTHERS("09", "其他");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TradeType> aliasEnums;

    TradeType(String code, String title) {
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

    public static TradeType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
