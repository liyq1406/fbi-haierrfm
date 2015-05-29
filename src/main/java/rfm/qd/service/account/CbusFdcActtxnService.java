package rfm.qd.service.account;

import rfm.qd.common.constant.InOutFlag;
import rfm.qd.common.constant.SendLogResult;
import rfm.qd.common.constant.TradeType;
import rfm.qd.gateway.cbus.domain.txn.QDJG01Res;
import rfm.qd.gateway.cbus.domain.txn.QDJG02Res;
import rfm.qd.gateway.domain.CommonRes;
import rfm.qd.gateway.domain.T000.T0007Req;
import rfm.qd.gateway.service.CbusTxnService;
import rfm.qd.gateway.utils.StringUtil;
import rfm.qd.repository.dao.CbsAccTxnMapper;
import rfm.qd.repository.dao.RsSendLogMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.*;
import rfm.qd.service.ClientBiService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class CbusFdcActtxnService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CbusFdcActtxnService.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private CbusTxnService cbusTxnService;
    @Autowired
    private CbsAccTxnMapper cbsAccTxnMapper;
    @Autowired
    private RsSendLogMapper rsSendLogMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ClientBiService clientBiService;

    // TODO 自动发送昨日贷款交易汇总
    public int autoSendLoanTxns() {

        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.DAY_OF_MONTH, -1);//取当前日期的前一天.

        String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        List<CbsAccTxn> accTxnList = new ArrayList<CbsAccTxn>();
        boolean isSent = isSentActtxns(yesterday);

        try {
            if (!isSent) {
                qrySaveActtxnsCbusByDate(yesterday, yesterday);
                accTxnList = qryCbsAccTotalTxnsByDateAndFlag(yesterday);
            }
        } catch (Exception e) {
            logger.error("自动获取当日贷款交易明细异常。", e);
            insertNewSendLog("QDJG02", "CBUS查询交易明细", yesterday, SendLogResult.QRYED_ERR.getCode());
            return -1;
        }
        try {
            if (!isSent) {
                if (!accTxnList.isEmpty()) {
                    sendAccTotalLoanTxns(yesterday, accTxnList);
                }
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
        CbsAccTxnExample example = new CbsAccTxnExample();
        example.createCriteria().andTxnDateBetween(startDate, endDate);
        return cbsAccTxnMapper.deleteByExample(example);
    }

    public boolean isQryedActtxns(String endDate) {
        RsSendLogExample example = new RsSendLogExample();
        example.createCriteria().andTxnDateEqualTo(endDate).andTxnResultEqualTo("1");
        int cnt = rsSendLogMapper.countByExample(example);
        logger.info("【发送日志里的记录数】：" + cnt);
        return (cnt > 0);
    }

    public boolean isSentActtxns(String endDate) {
        RsSendLogExample example = new RsSendLogExample();
        example.createCriteria().andTxnDateEqualTo(endDate).andTxnResultEqualTo(SendLogResult.SEND_OVER.getCode());
        return rsSendLogMapper.countByExample(example) > 0;
    }

    public int qrySaveTodayCbusTxnsAndBals() throws Exception {
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        List<RsAccount> accountList = accountService.qryAllMonitRecords();
        for (RsAccount account : accountList) {
            QDJG01Res res01 = cbusTxnService.qdjg01QryActbal(account.getAccountCode());
            account.setBalance(new BigDecimal(res01.actbal));
            account.setBalanceUsable(new BigDecimal(res01.avabal));
            accountService.updateRecord(account);
        }
        return qrySaveActtxnsCbusByDate(date, date);
    }

    // 查询并保存所有监管账户的交易明细
    public int qrySaveActtxnsCbusByDate(String startDate, String endDate) throws Exception {
        deleteTxnsByDate(startDate, endDate);
        List<RsAccount> accountList = accountService.qryAllMonitRecords();
        int cnt = 0;
        try {
            for (RsAccount account : accountList) {
                List<QDJG02Res> resList = cbusTxnService.qdjg02qryActtxnsByParams(account.getAccountCode(), startDate, endDate);
                for (QDJG02Res res : resList) {
                    for (QDJG02Res.TxnRecord txnRecord : res.recordList) {
                        CbsAccTxn cbsAccTxn = new CbsAccTxn();
                        cbsAccTxn.setPkid(UUID.randomUUID().toString());
                        cbsAccTxn.setAccountNo(account.getAccountCode());
                        cbsAccTxn.setAccountName(account.getAccountName());
                        cbsAccTxn.setBankId(res.getHeader().getBankId());
                        cbsAccTxn.setOperId(res.getHeader().getOperId());
                        cbsAccTxn.setSerialNo(res.getHeader().getSerialNo());
                        cbsAccTxn.setQryDate(res.getHeader().getTxnDate());
                        cbsAccTxn.setQryTime(res.getHeader().getTxnTime());
                        cbsAccTxn.setSeqNo(txnRecord.seqNo);
                        cbsAccTxn.setDebitAmt(txnRecord.debitAmt);
                        cbsAccTxn.setCreditAmt(txnRecord.creditAmt);
                        cbsAccTxn.setTxnType(txnRecord.txnType);
                        cbsAccTxn.setTxnSerialNo(txnRecord.txnSerialNo);
                        cbsAccTxn.setSummaryCode(txnRecord.summaryCode);
                        cbsAccTxn.setRemark(txnRecord.remark);
                        cbsAccTxn.setTxnDate(txnRecord.txnDate);
                        cbsAccTxn.setTxnTime(txnRecord.txnTime);
                        //  判断该笔交易是否是按揭贷款项
                        if (!StringUtils.isEmpty(txnRecord.remark) && txnRecord.remark.contains("按揭贷款")) {
                            cbsAccTxn.setSendFlag("0");
                        } else {
                            cbsAccTxn.setSendFlag(null);
                        }
                        if (cbsAccTxnMapper.insert(cbsAccTxn) == 1) {
                            cnt++;
                        }
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

    public List<CbsAccTxn> qryAccTxns(String accountNo, String startDate, String endDate) {
        CbsAccTxnExample example = new CbsAccTxnExample();
        example.createCriteria().andAccountNoEqualTo(accountNo).andTxnDateBetween(startDate, endDate);
        return cbsAccTxnMapper.selectByExample(example);
    }


    public List<CbsAccTxn> qryCbsAccTotalTxnsByDateAndFlag(String date) {
        return commonMapper.qryCbsAcctxnsByDateAndFlag(date);
    }

    public int sendAccTotalLoanTxns(String txnDate, List<CbsAccTxn> cbsAccTxnList) throws Exception {
        T0007Req req = new T0007Req();
        req.head.OpCode = "0007";
        String bankSerial = null;
        for (CbsAccTxn accTxn : cbsAccTxnList) {
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
    }

    private int insertNewSendLog(String txnCode, String txnName, String txnDate, String txnResult) {
        RsSendLog rsSendLog = new RsSendLog();
        rsSendLog.setSysDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        rsSendLog.setTxnCode(txnCode);
        rsSendLog.setTxnDate(txnDate);
        rsSendLog.setTxnNam(txnName);
        rsSendLog.setTxnResult(txnResult);
        rsSendLog.setPkid(UUID.randomUUID().toString());
        return rsSendLogMapper.insert(rsSendLog);
    }
}
