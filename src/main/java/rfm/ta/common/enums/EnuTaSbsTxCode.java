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
public enum EnuTaSbsTxCode {
    TRADE_0002("0002", "SBS记账"),
    TRADE_2601("2601", "SBS日终总数查询"),
    TRADE_2602("2602", "SBS日终明细查询"),
    TRADE_2701("2701", "SBS监管账户余额查询");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuTaSbsTxCode> aliasEnums;

    EnuTaSbsTxCode(String code, String title) {
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

    public static EnuTaSbsTxCode valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
