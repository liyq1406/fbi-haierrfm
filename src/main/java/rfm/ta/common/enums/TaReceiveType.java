package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * �������͡�[1]-����;[2]-�׸�;[3]-����;[9]-������
 * User: zhanrui
 * Date: 11-7-23
 * Time: ����3:30
 * To change this template use File | Settings | File Templates.
 */
public enum TaReceiveType implements EnumApp {
    DEPOSIT("1", "����"),
    DOWN_PAYMENT("2", "�׸�"),
    LOAN("3", "����"),
    OTHER("9", "����");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TaReceiveType> aliasEnums;

    TaReceiveType(String code, String title) {
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

    public static TaReceiveType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
