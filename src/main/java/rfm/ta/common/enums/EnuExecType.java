package rfm.ta.common.enums;

import java.util.Hashtable;

/*
����;01��������-����02�ƻ����03�˿04��Ϣ��05 ���ת��09����

 */
public enum EnuExecType {
    EXEC_TYPE_DEBUG("debug", "����"),
    EXEC_TYPE_PROC("proc", "ִ��");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuExecType> aliasEnums;

    EnuExecType(String code, String title) {
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

    public static EnuExecType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
