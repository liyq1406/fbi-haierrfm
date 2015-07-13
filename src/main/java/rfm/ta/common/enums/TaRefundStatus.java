package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * ״̬��־��[0]-�˻�;[1]-��ʼ;[2]-�����;[4]-������;[5]-֧���ɹ�δ�Ǽ�;[6]-SBSδ����;[70]-�ύ��SBS;[71]-�ύ������;[72]-���г�ʱ;[9]-֧�����;[B]-֧��ʧ�ܸ���;[Y]-����֧��;[Z]-��Ʊ
 * ״̬��־��[0]-�˻�;[1]-��ʼ;[2]-�����;[4]-������;[5]-֧���ɹ�δ����;[6]-֧���ɹ�δ�Ǽ�;[7]-֧����;[9]-֧�����;[B]-֧��ʧ�ܸ���;[Y]-����֧��;[Z]-��Ʊ
 * User: zhanrui
 * Date: 11-7-23
 * Time: ����3:30
 * To change this template use File | Settings | File Templates.
 */
public enum TaRefundStatus {
    BACK("0", "�˻�"),
    INIT("1", "��ʼ"),
    CHECKING("2", "�����"),
    CHECKED("3", "�����"),
    PEND_PAY("4", "������"),
    PAYED_NOACCOUNT("5", "֧���ɹ�δ����"),
    PAYED_NORECORD("6", "֧���ɹ�δ�Ǽ�"),
    ACCOUNT("7", "֧����"),
    ACCOUNT_SUCCESS("9", "֧�����"),
    ACCOUNT_FAILURE("B", "֧��ʧ�ܸ���"),
    ABANDON_PAY("Y", "����֧��"),
    REFUNDED("Z", "��Ʊ");

    private String code = null;
    private String title = null;
    private static Hashtable<String, TaRefundStatus> aliasEnums;

    TaRefundStatus(String code, String title) {
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

    public static TaRefundStatus valueOfAlias(String alias) {
        return aliasEnums.get(alias);
    }

    public String getCode() {
        return this.code;
    }

    public String getTitle() {
        return this.title;
    }
}
