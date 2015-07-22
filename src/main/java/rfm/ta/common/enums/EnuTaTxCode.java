package rfm.ta.common.enums;

import java.util.Hashtable;

public enum EnuTaTxCode {
    TRADE_1001("1001", "建立监管"),
    TRADE_1002("1002", "解除监管"),
    TRADE_2001("2001", "交存验证"),
    TRADE_2002("2002", "交存记账"),
    TRADE_2011("2011", "交存冲正"),
    TRADE_2101("2101", "划拨验证"),
    TRADE_2102("2102", "划拨记账"),
    TRADE_2111("2111", "划拨冲正"),
    TRADE_2201("2201", "返还验证"),
    TRADE_2202("2202", "返还记账"),
    TRADE_2211("2211", "返还冲正"),
    TRADE_2501("2501", "记账结果查询");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaTxCode> aliasEnums;

    EnuTaTxCode(String code, String title) {
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

    public static EnuTaTxCode valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
