package rfm.qd.gateway.cbus.domain.txn;

import rfm.qd.gateway.cbus.domain.base.AbstractReqMsg;
import org.apache.commons.lang.StringUtils;

public class QDJG02Req extends AbstractReqMsg {

    /*
       0-第一次
    上页第一笔资料的KEY 值	64	从上一笔的返回报文获取
    上页最后一笔资料的KEY 值	64	从上一笔的返回报文获取
    跳页方向标	1	1 表示上翻 2 表示下翻（请固定使用2）
    帐号                                                  	28	左对齐，右补空格
     起始明细号                                      	7	左对齐，右补空格（首次为1，其他为前一次最后一笔明细号+1）
    起始日期	8	左对齐，右补空格
    寄对帐单标志	1	不上送，补空格
    密码	16	默认为账号，左对齐，右补空格
    终止日期	8	左对齐，右补空格
    功能	1	1 账户明细查询
     */

    public String firstFlag = "0";         // 0-第一次
    public String preFirstKey = "0";       //  上页第一笔资料的KEY 值	64	从上一笔的返回报文获取
    public String preLastKey = "0";        //  上页最后一笔资料的KEY 值	64	从上一笔的返回报文获取
    public String turnPageNo = "2";  //  跳页方向标 长度1  固定值2
    public String accountNo = "";         //  帐号  28
    public String startSeqNo = "1";     //   起始明细号  7 （首次为1，其他为前一次最后一笔明细号+1）
    public String startDate = "";         //  起始日期  8
    public String chkActPrFlag = " "; // 寄对帐单标志  长度1 补空格
    public String password = "";           // 密码	16	默认为账号，左对齐，右补空格
    public String endDate = "";            // 终止日期	8	左对齐，右补空格
    public String function = "1";     // 功能	1	1 账户明细查询

    public String bodyToString() {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(firstFlag);
        strBuilder.append(StringUtils.rightPad(preFirstKey, 64, ' '));
        strBuilder.append(StringUtils.rightPad(preLastKey, 64, ' '));
        strBuilder.append(turnPageNo);
        strBuilder.append(StringUtils.rightPad(accountNo, 28, ' '));
        strBuilder.append(StringUtils.rightPad(startSeqNo, 7, ' '));
        strBuilder.append(StringUtils.rightPad(startDate, 8, ' '));
        strBuilder.append(chkActPrFlag);
        strBuilder.append(StringUtils.rightPad(password, 16, ' '));
        strBuilder.append(StringUtils.rightPad(endDate, 8, ' '));
        strBuilder.append(function);

        return strBuilder.toString();
    }
}