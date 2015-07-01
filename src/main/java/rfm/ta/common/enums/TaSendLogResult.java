package rfm.ta.common.enums;

import java.util.Hashtable;

public enum TaSendLogResult implements EnumApp {
    SEND_OVER("2", "�������ݳɹ�"),
    SEND_ERR("20", "��������ʧ��"),
    QRYED("1", "��ѯ��ϸ�ɹ�"),
    QRYED_ERR("10", "��ѯ��ϸʧ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TaSendLogResult> aliasEnums;

    TaSendLogResult(String code, String title) {
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

    public static TaSendLogResult valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
