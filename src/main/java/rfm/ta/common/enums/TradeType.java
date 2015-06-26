package rfm.ta.common.enums;

import java.util.Hashtable;

/*
����;01��������-����02�ƻ����03�˿04��Ϣ��05 ���ת��09����

 */
public enum TradeType implements EnumApp {
    HOUSE_INCOME("01", "��������"),
    HOUSE_DOWN_PAYMENT("07", "�׸�"),
    HOUSE_CREDIT("08", "���Ҵ���"),
    PLAN_PAYOUT("02", "�ƻ�����"),
    TRANS_BACK("03", "�˿�"),
    INTEREST("04", "��Ϣ"),
    HOUSE_TRANS("05", "���ת"),
    OTHERS("09", "����");

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
