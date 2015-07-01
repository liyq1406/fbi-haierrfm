package rfm.ta.service.account;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.common.enums.SendLogResult;
import rfm.ta.gateway.sbs.domain.txn.QDJG01Res;
import rfm.ta.gateway.sbs.domain.txn.QDJG02Res;
import rfm.ta.gateway.sbs.domain.service.TaSbsTxnService;
import rfm.ta.repository.dao.TaRsSendLogMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.*;
import rfm.ta.service.TaClientBiService;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-26
 * Time: 下午3:07
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaSbsFdcActtxnService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(TaSbsFdcActtxnService.class);

    @Autowired
    private TaAccountService accountService;
    @Autowired
    private TaSbsTxnService qdSbsTxnService;
    @Autowired
    private TaRsSendLogMapper qdRsSendLogMapper;
    @Autowired
    private TaCommonMapper commonMapper;
    @Autowired
    private TaClientBiService clientBiService;

    // TODO 自动发送昨日贷款交易汇总
    public int autoSendLoanTxns() {

        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.DAY_OF_MONTH, -1);//取当前日期的前一天.

        String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        //List<TaCbsAccTxn> accTxnList = new ArrayList<TaCbsAccTxn>();
        boolean isSent = isSentActtxns(yesterday);

        try {
            if (!isSent) {
                qrySaveActtxnsCbusByDate(yesterday, yesterday);
                //accTxnList = qryCbsAccTotalTxnsByDateAndFlag(yesterday);
            }
        } catch (Exception e) {
            logger.error("自动获取当日贷款交易明细异常。", e);
            insertNewSendLog("QDJG02", "CBUS查询交易明细", yesterday, SendLogResult.QRYED_ERR.getCode());
            return -1;
        }
        try {
            if (!isSent) {
                /*if (!accTxnList.isEmpty()) {
                    sendAccTotalLoanTxns(yesterday, accTxnList);
                }*/
            }
        } catch (Exception e) {
            logger.error("自动发送当日账户按揭贷款汇总异常。", e);
            insertNewSendLog("0007", "发送按揭贷款交易金额汇总", yesterday, SendLogResult.SEND_ERR.getCode());
            return -1;
        }
        return 0;
    }

    public int updateCbsActtxnsUnSent(String date) {
        return commonMapper.updateCbsActtxnsUnSent(date);
    }

    public int deleteTxnsByDate(String startDate, String endDate) {
        /*TaCbsAccTxnExample example = new TaCbsAccTxnExample();
        example.createCriteria().andTxnDateBetween(startDate, endDate);
        return qdCbsAccTxnMapper.deleteByExample(example);*/
        return 1;
    }

    public boolean isQryedActtxns(String endDate) {
        TaRsSendLogExample example = new TaRsSendLogExample();
        example.createCriteria().andTxnDateEqualTo(endDate).andTxnResultEqualTo("1");
        int cnt = qdRsSendLogMapper.countByExample(example);
        logger.info("【发送日志里的记录数】：" + cnt);
        return (cnt > 0);
    }

    public boolean isSentActtxns(String endDate) {
        TaRsSendLogExample example = new TaRsSendLogExample();
        example.createCriteria().andTxnDateEqualTo(endDate).andTxnResultEqualTo(SendLogResult.SEND_OVER.getCode());
        return qdRsSendLogMapper.countByExample(example) > 0;
    }

    public int qrySaveTodayCbusTxnsAndBals() throws Exception {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
       /* List<TaRsAccount> accountList = accountService.qryAllMonitRecords();
        for (TaRsAccount account : accountList) {
            QDJG01Res res01 = qdSbsTxnService.qdjg01QryActbal(account.getAccountCode());
            account.setBalance(new BigDecimal(res01.actbal));
            account.setBalanceUsable(new BigDecimal(res01.avabal));
            accountService.updateRecord(account);
        }*/
        return qrySaveActtxnsCbusByDate(date, date);
    }

    // 查询并保存所有监管账户的交易明细
    public int qrySaveActtxnsCbusByDate(String startDate, String endDate) throws Exception {
        deleteTxnsByDate(startDate, endDate);
        List<TaRsAccount> accountList = new ArrayList<>();//accountService.qryAllMonitRecords();
        int cnt = 0;
        try {
            for (TaRsAccount account : accountList) {
                List<QDJG02Res> resList = qdSbsTxnService.qdjg02qryActtxnsByParams(account.getAccountCode(), startDate, endDate);
                for (QDJG02Res res : resList) {
                    for (QDJG02Res.TxnRecord txnRecord : res.recordList) {
                        /*TaCbsAccTxn qdCbsAccTxn = new TaCbsAccTxn();
                        qdCbsAccTxn.setPkid(UUID.randomUUID().toString());
                        qdCbsAccTxn.setAccountNo(account.getAccountCode());
                        qdCbsAccTxn.setAccountName(account.getAccountName());
                        qdCbsAccTxn.setBankId(res.getHeader().getBankId());
                        qdCbsAccTxn.setOperId(res.getHeader().getOperId());
                        qdCbsAccTxn.setSerialNo(res.getHeader().getSerialNo());
                        qdCbsAccTxn.setQryDate(res.getHeader().getTxnDate());
                        qdCbsAccTxn.setQryTime(res.getHeader().getTxnTime());
                        qdCbsAccTxn.setSeqNo(txnRecord.seqNo);
                        qdCbsAccTxn.setDebitAmt(txnRecord.debitAmt);
                        qdCbsAccTxn.setCreditAmt(txnRecord.creditAmt);
                        qdCbsAccTxn.setTxnType(txnRecord.txnType);
                        qdCbsAccTxn.setTxnSerialNo(txnRecord.txnSerialNo);
                        qdCbsAccTxn.setSummaryCode(txnRecord.summaryCode);
                        qdCbsAccTxn.setRemark(txnRecord.remark);
                        qdCbsAccTxn.setTxnDate(txnRecord.txnDate);
                        qdCbsAccTxn.setTxnTime(txnRecord.txnTime);
                        //  判断该笔交易是否是按揭贷款项
                        if (!StringUtils.isEmpty(txnRecord.remark) && txnRecord.remark.contains("按揭贷款")) {
                            qdCbsAccTxn.setSendFlag("0");
                        } else {
                            qdCbsAccTxn.setSendFlag(null);
                        }
                        if (qdCbsAccTxnMapper.insert(qdCbsAccTxn) == 1) {
                            cnt++;
                        }*/
                    }
                }
            }
            if (accountList.size() > 0) {
                insertNewSendLog("QDJG02", "CBUS查询交易明细", endDate, SendLogResult.QRYED.getCode());
            }
        } catch (Exception e) {
            logger.error("查询交易明细异常。", e);
            insertNewSendLog("QDJG02", "CBUS查询交易明细", endDate, SendLogResult.QRYED_ERR.getCode());
            return -1;
        }
        return cnt;
    }

   /* public List<TaCbsAccTxn> qryAccTxns(String accountNo, String startDate, String endDate) {
        TaCbsAccTxnExample example = new TaCbsAccTxnExample();
        example.createCriteria().andAccountNoEqualTo(accountNo).andTxnDateBetween(startDate, endDate);
        return qdCbsAccTxnMapper.selectByExample(example);
    }


    public List<TaCbsAccTxn> qryCbsAccTotalTxnsByDateAndFlag(String date) {
        return commonMapper.qryCbsAcctxnsByDateAndFlag(date);
    }

    public int sendAccTotalLoanTxns(String txnDate, List<TaCbsAccTxn> qdCbsAccTxnList) throws Exception {
        T0007Req req = new T0007Req();
        req.head.OpCode = "0007";
        String bankSerial = null;
        for (TaCbsAccTxn accTxn : qdCbsAccTxnList) {
            T0007Req.Param.Record record = T0007Req.getRecord();
            bankSerial = commonMapper.qryBatchSerialNo(txnDate);
            record.Date = txnDate;
            record.BankSerial = bankSerial;
            record.Time = "121212";
            record.Flag = InOutFlag.IN.getCode();
            record.Type = TradeType.HOUSE_CREDIT.getCode();
            record.ContractNum = "";
            record.PlanDetailNO = "";
            record.AcctName = accTxn.getAccountName();
            record.Acct = accTxn.getAccountNo();
            record.ToName = "";
            record.ToAcct = "";
            record.ToBankName = "";
            if (StringUtils.isEmpty(accTxn.getCreditAmt())) {
                record.Amt = "000";
            } else {
                record.Amt = StringUtil.toBiformatAmt(new BigDecimal(accTxn.getCreditAmt()));
            }
            record.Purpose = TradeType.HOUSE_CREDIT.getTitle();
            req.param.recordList.add(record);
        }
        String dataGram = req.toFDCDatagram();                // 报文

        CommonRes res = clientBiService.sendMsgAndRecvRes(dataGram);
        if (!"0000".equalsIgnoreCase(res.head.RetCode)) {
            return -1;
        } else {
            commonMapper.updateCbsActtxnsSent(txnDate);
            return insertNewSendLog("0007", "发送按揭贷款交易金额汇总", txnDate, SendLogResult.SEND_OVER.getCode());
        }
    }*/

    private int insertNewSendLog(String txnCode, String txnName, String txnDate, String txnResult) {
        TaRsSendLog qdRsSendLog = new TaRsSendLog();
        qdRsSendLog.setSysDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        qdRsSendLog.setTxnCode(txnCode);
        qdRsSendLog.setTxnDate(txnDate);
        qdRsSendLog.setTxnNam(txnName);
        qdRsSendLog.setTxnResult(txnResult);
        qdRsSendLog.setPkid(UUID.randomUUID().toString());
        return qdRsSendLogMapper.insert(qdRsSendLog);
    }
}
