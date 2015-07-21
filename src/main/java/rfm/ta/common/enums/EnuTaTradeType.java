package rfm.ta.common.enums;

import java.util.Hashtable;

/*
����;01��������-����02�ƻ����03�˿04��Ϣ��05 ���ת��09����

 */
public enum EnuTaTradeType {
    TRADE_INCOME("01", "����"),
    TRADE_PAYMENT("02", "����"),
    TRADE_BACK("03", "����"),
    TRADE_OTHERS("04", "����");

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
