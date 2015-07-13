package rfm.ta.common.enums;

import java.util.Hashtable;

/**
 * 状态标志，[0]-退回;[1]-初始;[2]-审核中;[4]-待付款;[5]-支付成功未登记;[6]-SBS未记账;[70]-提交给SBS;[71]-提交给银行;[72]-银行超时;[9]-支付完成;[B]-支付失败搁置;[Y]-放弃支付;[Z]-退票
 * 状态标志，[0]-退回;[1]-初始;[2]-审核中;[4]-待付款;[5]-支付成功未记账;[6]-支付成功未登记;[7]-支付中;[9]-支付完成;[B]-支付失败搁置;[Y]-放弃支付;[Z]-退票
 * User: zhanrui
 * Date: 11-7-23
 * Time: 下午3:30
 * To change this template use File | Settings | File Templates.
 */
public enum TaRefundStatus {
    BACK("0", "退回"),
    INIT("1", "初始"),
    CHECKING("2", "审核中"),
    CHECKED("3", "已审核"),
    PEND_PAY("4", "待付款"),
    PAYED_NOACCOUNT("5", "支付成功未记账"),
    PAYED_NORECORD("6", "支付成功未登记"),
    ACCOUNT("7", "支付中"),
    ACCOUNT_SUCCESS("9", "支付完成"),
    ACCOUNT_FAILURE("B", "支付失败搁置"),
    ABANDON_PAY("Y", "放弃支付"),
    REFUNDED("Z", "退票");

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
