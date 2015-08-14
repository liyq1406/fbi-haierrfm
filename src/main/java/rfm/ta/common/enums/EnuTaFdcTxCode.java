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
public enum EnuTaFdcTxCode {
    TRADE_1001("1001", "�������"),
    TRADE_1002("1002", "������"),
    TRADE_2001("2001", "������֤"),
    TRADE_2002("2002", "�������"),
    TRADE_2011("2011", "�������"),
    TRADE_2101("2101", "������֤"),
    TRADE_2102("2102", "��������"),
    TRADE_2111("2111", "��������"),
    TRADE_2201("2201", "������֤"),
    TRADE_2202("2202", "��������"),
    TRADE_2211("2211", "��������"),
    TRADE_2501("2501", "���˽����ѯ"),
    TRADE_2601("2601", "���ն���"),
    TRADE_2701("2701", "������");

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
