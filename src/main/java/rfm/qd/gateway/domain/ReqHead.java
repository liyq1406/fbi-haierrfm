package rfm.qd.gateway.domain;

import platform.service.SystemService;
import pub.platform.advance.utils.PropertyManager;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-10
 * Time: обнГ4:15
 * To change this template use File | Settings | File Templates.
 */
/*
<OpCode>2002</OpCode>
<OpDate>20090325</OpDate>
<OpTime>215515</OpTime>
<BankCode>103</BankCode>
 */
public class ReqHead {
    public String OpCode = "";
    public String OpDate = SystemService.getSdfdate8();
    public String OpTime = SystemService.getSdftime6();
    public String BankCode = PropertyManager.getProperty("bank.code.of.brz");
}
