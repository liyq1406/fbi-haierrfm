package rfm.ta.common.enums;

import java.util.Hashtable;

/*
���˳ɹ���־��
    0�����˳ɹ���
    1������ԭ��
    2������ʧ�ܣ�
 */
public enum EnuActFlag {
    ACT_SUCCESS("0", "���˳ɹ�"),
    ACT_UNKNOWN("1", "����ԭ��"),
    ACT_FAIL("2", "����ʧ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuActFlag> aliasEnums;

    EnuActFlag(String code, String title) {
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

    public static EnuActFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
