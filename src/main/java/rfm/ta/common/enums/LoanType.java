package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-7-23
 * Time: ����3:30
 * To change this template use File | Settings | File Templates.
 */
public enum LoanType implements EnumApp {
    SHANG_YE("01", "��ҵ����"),
    GONG_JI_JIN("02", "���������"),
    ZU_HE("03", "��ϴ���");

    private String code = null;
    private String title = null;
    private static Hashtable<String, LoanType> aliasEnums;

    LoanType(String code, String title) {
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

    public static LoanType valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
