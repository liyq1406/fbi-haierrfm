package rfm.ta.common.enums;

import java.util.Hashtable;

/*
ɾ����־��
    0��δɾ����
    1��ɾ����
 */
public enum EnuDelFlag {
    DEL_FALSE("0", "δɾ��"),
    DEL_TRUE("1", "ɾ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuDelFlag> aliasEnums;

    EnuDelFlag(String code, String title) {
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

    public static EnuDelFlag valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
