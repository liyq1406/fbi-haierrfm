package rfm.qd.common.constant;

import java.util.Hashtable;

/**
 * ��ͬ�ɿ�״̬��־��[0]-�˻�;[1]-��ʼ;[2]-�����;[3]-�����;[7]-������;[9]-�������
 * User: zhanrui
 * Date: 11-7-23
 * Time: ����3:30
 * To change this template use File | Settings | File Templates.
 */
public enum ContractRecvStatus implements EnumApp {
    BACK("0", "�˻�"),
    INIT("1", "��ʼ"),
    CHECKING("2", "�����"),
    CHECKED("3", "�����"),
    ACCOUNT("7", "������"),
    ACCOUNTOVER("9", "�������");

    private String code = null;
    private String title = null;
    private static Hashtable<String, ContractRecvStatus> aliasEnums;

    ContractRecvStatus(String code, String title) {
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

    public static ContractRecvStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
