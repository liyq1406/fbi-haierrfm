package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * ״̬��־��[0]-�ռ���˻�ȡ��;[1]-�ռ���˻�ȡ��ɣ�[2]�ռ���˲�ƽ��[3]�ռ����ƽ��[4]�ռ���˷��ͳɹ���
 *          [5]�����˻�ȡ�У�[6]�����˻�ȡ��ɣ�[7]�����˷��ͳɹ���
 */
public enum EnuStatusFlag {
    STATUS_FLAG0("0","�ռ���˻�ȡ��"),
    STATUS_FLAG1("1","�ռ���˻�ȡ���"),
    STATUS_FLAG2("2","�ռ���˲�ƽ"),
    STATUS_FLAG3("3","�ռ����ƽ"),
    STATUS_FLAG4("4","�ռ���˷��ͳɹ�"),
    STATUS_FLAG5("5","�����˻�ȡ��"),
    STATUS_FLAG6("6","�����˻�ȡ���"),
    STATUS_FLAG7("7","�����˷��ͳɹ�");

    private String code = null;
    private String title = null;
    private static Hashtable<String, EnuStatusFlag> aliasEnums;

    EnuStatusFlag(String code, String title){
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

    public static EnuStatusFlag getValueByKey(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
