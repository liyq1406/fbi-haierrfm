package rfm.ta.gateway.sbs.domain.txn.model.msg;

/**
 * Created by Lichao.W At 2015/7/9 10:24
 * wanglichao@163.com
 * 泰安房产对账
 */
public class M8873 extends MTia {
     private String ERYDA1 = "";    //业务日期
     private String BEGNUM = "";   //起始笔数  "该交易需做多次请求-应答的交互：
                                     //    第一次填0，第n次填写累计n-1次实际收到的记录数+1。
                                     //    比如，假设总共可查询出123笔明细，并且一个报文最多容纳20笔。
                                     //    第一次请求：填写000000，收到应答，收到2笔：01-20
                                     //    第二次请求：填写000021，收到应答，收到20笔：21-40
                                     //    最后一次请求：填写000121，收到应答，收到3笔：121-123"


    public String getERYDA1() {
        return ERYDA1;
    }

    public void setERYDA1(String ERYDA1) {
        this.ERYDA1 = ERYDA1;
    }

    public String getBEGNUM() {
        return BEGNUM;
    }

    public void setBEGNUM(String BEGNUM) {
        this.BEGNUM = BEGNUM;
    }
}
