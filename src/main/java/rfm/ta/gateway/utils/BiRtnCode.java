package rfm.ta.gateway.utils;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-26
 * Time: ионГ10:55
 * To change this template use File | Settings | File Templates.
 */
public enum BiRtnCode {

    BI_RTN_CODE_SUCCESS("0000"),
    BI_RTN_CODE_FAILED("4000"),
    BI_RTN_CODE_FORMAT_ERROR("5000"),
    BI_RTN_CODE_NO_ACCOUNT("6000");
    private String code;

    private BiRtnCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
