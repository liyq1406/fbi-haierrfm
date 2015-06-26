package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * ��������[0]-��ͨ��;[1]-ͨ��;[2]-�ύ;[3]-�½�
 * User: zhanrui
 * Date: 11-7-23
 * Time: ����3:30
 * To change this template use File | Settings | File Templates.
 */
public enum WorkResult implements EnumApp {
    NOTPASS("0", "�˻�"),
    PASS("1", "�Ѹ���"),
    COMMIT("2", "������"),
    RE_CHECK("6", "���θ���"),
    NOT_KNOWN("5", "�������"),
    CREATE("3", "��������"),
    SENT("4", "�ѷ���");

    private String code = null;
    private String title = null;
    private static Hashtable<String, WorkResult> aliasEnums;

    WorkResult(String code, String title) {
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

    public static WorkResult valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
