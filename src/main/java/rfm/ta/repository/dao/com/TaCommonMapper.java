package rfm.ta.repository.dao.com;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;
import rfm.qd.repository.model.QdCbsAccTxn;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsPayout;
import rfm.qd.view.payout.ParamPlan;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-27
 * Time: ÏÂÎç2:12
 * To change this template use File | Settings | File Templates.
 */
@Component
public interface TaCommonMapper {

    @Select("select nvl(max(company_id)+1,'1000000001') from rs_fdccompany")
    String selectNewCompanyId();

    List<QdRsPayout> selectRsPayoutsByParamPlan(ParamPlan paramPlan);

    @Select("select nvl(max(SERIAL)+1,'3132000000001') from rs_payout")
    String selectMaxPayoutSerial();

    @Select("select nvl(max(SERIAL)+1,'3133000000001') from rs_receive")
    String selectMaxRecvSerial();

    @Select("select nvl(max(LOCAL_SERIAL)+1,'3134000000001') from rs_acc_detail")
    String selectMaxAccDetailSerial();

    @Select("select nvl(max(SERIAL)+1,'3135000000001') from rs_refund")
    String selectMaxRefundSerial();

    @Select("select sum(t.pl_amount) sumplamount from rs_refund t where t.deleted_flag = '0' group by t.business_no" +
            " having t.business_no = #{business_no}")
    BigDecimal selectSumPlamount(@Param("business_no") String businessNO);

    @Select("select sum(t.pl_amount) sumplamount from rs_refund t where t.deleted_flag = '0' and pk_id <> #{pkid} group by t.business_no")
    BigDecimal selectSumPlamountExceptPkid(@Param("pkid") String pkid);

    @Select(" select t.account_code, t.account_name, sum(t.trade_amt) as trade_amt , t.trade_date from RS_ACC_DETAIL t " +
            " group by t.account_code, t.account_name, t.trade_date " +
            " having t.trade_date = #{txnDate}")
    List<QdRsAccDetail> selectAcctLoanAmtListByDate(@Param("txnDate") String txnDate);

    @Select("select seq_no from rs_sys_ctl where SYS_NO = '1' for update")
    int selectSysSeq();

    @Update("update rs_sys_ctl set seq_no = #{seq}  where SYS_NO = '1'")
    public int updateSysSeq(@Param("seq") int seq);

    @Update("update rs_sys_ctl set seq_no = #{seq}, sys_date = #{date}  where SYS_NO = '1'")
    public int updateSeqAndSysDate(@Param("seq") int seq, @Param("date") String date);

    @Select("select min(t.txn_serial_no) from CBS_ACC_TXN t" +
            " where t.txn_date = #{date} " +
            " and t.send_flag is not null")
    String qryBatchSerialNo(@Param("date") String date);

    @Select("select acc.account_code as accountNo,acc.account_name as accountName, " +
            "             nvl(sum(debit_amt), '0') as debitAmt," +
            "              nvl(sum(credit_amt), '0') as creditAmt, " +
            "             t.txn_date as txnDate,'0' as sendFlag  " +
            "            from rs_account acc  " +
            "            left join " +
            "            (select * from CBS_ACC_TXN where txn_date = #{date}" +
            "             and send_flag = '0') t  " +
            "            on acc.account_code = t.account_no  " +
            "            group by acc.account_code,t.txn_date,t.send_flag,acc.status_flag,acc.account_name  " +
            "            having acc.status_flag = '0' ")
    public List<QdCbsAccTxn> qryCbsAcctxnsByDateAndFlag(@Param("date") String date);

    @Update("update CBS_ACC_TXN t set t.send_flag = '1' where t.txn_date = #{date} and t.send_flag = '0'")
    public int updateCbsActtxnsSent(@Param("date") String date);

    @Update("update CBS_ACC_TXN t set t.send_flag = '0' where t.txn_date = #{date} and t.send_flag = '1'")
    public int updateCbsActtxnsUnSent(@Param("date") String date);
}
