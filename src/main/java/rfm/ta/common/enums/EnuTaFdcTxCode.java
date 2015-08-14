package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 泰安房产中心交易号
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
public enum EnuTaFdcTxCode {
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
    TRADE_2501("2501", "记账结果查询"),
    TRADE_2601("2601", "日终对账"),
    TRADE_2701("2701", "余额对账");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaFdcTxCode> aliasEnums;

    EnuTaFdcTxCode(String code, String title) {
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

    public static EnuTaFdcTxCode valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
