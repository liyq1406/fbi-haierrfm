package rfm.qd.common.constant;

import java.util.Hashtable;

public enum TradeStatus implements EnumApp {
    CANCEL("0", "��ʼ"),
    SUCCESS("1", "���׳ɹ�"),
    CHECKED("2","���ͨ��"),
    BACK("3","�˻�");
    private String code = null;
    private String title = null;
    private static Hashtable<String, TradeStatus> aliasEnums;

    TradeStatus(String code, String title) {
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

    public static TradeStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
