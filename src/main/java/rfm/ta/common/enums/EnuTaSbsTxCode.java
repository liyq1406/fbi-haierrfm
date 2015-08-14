package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * ̩���������Ľ��׺�
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
public enum EnuTaSbsTxCode {
    TRADE_0002("0002", "SBS����"),
    TRADE_2601("2601", "SBS����������ѯ"),
    TRADE_2602("2602", "SBS������ϸ��ѯ"),
    TRADE_2701("2701", "SBS����˻�����ѯ");

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
