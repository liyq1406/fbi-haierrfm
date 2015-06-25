package rfm.qd.service;

import rfm.qd.common.constant.*;
import rfm.qd.repository.model.*;
import rfm.qd.service.account.AccountService;
import rfm.qd.service.contract.ContractService;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.common.utils.BeanHelper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-2
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TradeService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RsAccDetailService accDetailService;
    @Autowired
    private PayoutService payoutService;
    @Autowired
    private ExpensesPlanService expensesPlanService;
    @Autowired
    private LockedaccDetailService lockedaccDetailService;
    @Autowired
    private ClientBiService clientBiService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private ContractRecvService receiveService;
    @Autowired
    private ContractService contractService;

    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    // R-冲正 D-退票
    @Transactional
    public int handleCancelAccDetail(QdRsAccDetail record, ChangeFlag changeFlag) throws Exception {
        // 查询未限制已监管未删除账户
        QdRsAccount account = accountService.selectCanPayAccountByNo(record.getAccountCode());
        if (account == null) {
            throw new RuntimeException("监管账号不存在！");
        }

        //-----------------------------------------------
        // 新增交易明细
        QdRsAccDetail accDetail = new QdRsAccDetail();
        accDetail.setAccountCode(record.getAccountCode());
        accDetail.setAccountName(record.getAccountName());
        accDetail.setToAccountCode(record.getToAccountCode());
        accDetail.setToAccountName(record.getToAccountName());
        accDetail.setToHsBankName(record.getToHsBankName());
        accDetail.setTradeType(TradeType.OTHERS.getCode());
        accDetail.setTradeAmt(record.getTradeAmt());
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setContractNo(record.getContractNo());
        accDetail.setChangeFlag(changeFlag.getCode());      // 标记: R-冲正 D-退票
        record.setChangeFlag(changeFlag.getCode());
        accDetail.setPlanCtrlNo(record.getPlanCtrlNo());
        accDetail.setStatusFlag(TradeStatus.SUCCESS.getCode());
        accDetail.setSendFlag(SendFlag.SENT.getCode());
        if (InOutFlag.IN.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            accDetail.setInoutFlag(InOutFlag.OUT.getCode());
            accDetail.setBalance(account.getBalance().subtract(record.getTradeAmt()));
            account.setBalance(account.getBalance().subtract(record.getTradeAmt()));
            account.setBalanceUsable(account.getBalanceUsable().subtract(record.getTradeAmt()));
        } else {
            accDetail.setInoutFlag(InOutFlag.IN.getCode());
            accDetail.setBalance(account.getBalance().add(record.getTradeAmt()));
            account.setBalance(account.getBalance().add(record.getTradeAmt()));
            account.setBalanceUsable(account.getBalanceUsable().add(record.getTradeAmt()));
        }
        int rtnCnt = 0;
        if (ChangeFlag.CANCEL.getCode().equalsIgnoreCase(changeFlag.getCode())) {  // 冲正

            //  处理冲正业务回退
            if (handleCancelBiBack(record) == 1) {
                rtnCnt = accDetailService.insertAccDetail(accDetail) + accDetailService.updateAccDetail(record)
                        + accountService.updateRecord(account);
            }
            if (clientBiService.sendAccDetailCancel(record) == -1) {
                throw new RuntimeException("发送冲正交易记录失败!");
            }

        } else if (ChangeFlag.BACK.getCode().equalsIgnoreCase(changeFlag.getCode())) {   // 退票

            //  处理退票业务回退
            if (handleCancelBiBack(record) == 1) {
                rtnCnt = accDetailService.insertAccDetail(accDetail) + accDetailService.updateAccDetail(record)
                        + accountService.updateRecord(account);
            }
            if (clientBiService.sendAccDetailBack(record) == -1) {
                throw new RuntimeException("发送退票交易记录失败!");
            }
        }
        return rtnCnt;
    }

    //  处理业务回退

    private int handleCancelBiBack(QdRsAccDetail record) {
        // 计划付款回退
        if (StringUtils.isEmpty(record.getContractNo()) && !StringUtils.isEmpty(record.getPlanCtrlNo())) {
            QdRsPayout origiPayout = payoutService.selectRecordByAccDetail(record);
            QdRsPayout payout = new QdRsPayout();
            BeanHelper.copy(payout, origiPayout);
            payout.setPkId(null);
            payout.setApAmount(payout.getApAmount().multiply(new BigDecimal("-1")));
            payout.setPlAmount(payout.getPlAmount().multiply(new BigDecimal("-1")));
            payout.setWorkResult(WorkResult.SENT.getCode());

            QdRsPlanCtrl planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(record.getPlanCtrlNo());
            planCtrl.setAvAmount(planCtrl.getAvAmount().add(record.getTradeAmt()));
            if (payoutService.insertRsPayout(payout) == 1) {
                return expensesPlanService.updatePlanCtrl(planCtrl);
            } else {
                throw new RuntimeException("计划付款记录回退失败！");
            }
        }
        // 合同收款
        else if (!StringUtils.isEmpty(record.getContractNo()) && StringUtils.isEmpty(record.getPlanCtrlNo())
                && InOutFlag.IN.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            QdRsReceive OrigiReceive = receiveService.selectRecordByAccDetail(record);
            QdRsReceive newReceive = new QdRsReceive();
            BeanHelper.copy(newReceive, OrigiReceive);
            newReceive.setPkId(null);
            newReceive.setWorkResult(WorkResult.SENT.getCode());
            newReceive.setApAmount(newReceive.getApAmount().multiply(new BigDecimal("-1")));
            newReceive.setPlAmount(newReceive.getPlAmount().multiply(new BigDecimal("-1")));

            QdRsContract contract = contractService.selectContractByNo(record.getContractNo());
            contract.setReceiveAmt(contract.getReceiveAmt().subtract(record.getTradeAmt()));
            if (receiveService.insertRecord(newReceive) == 1) {
                return contractService.updateRecord(contract);
            }

        }
        // 合同退款
        else if (!StringUtils.isEmpty(record.getContractNo()) && StringUtils.isEmpty(record.getPlanCtrlNo())
                && InOutFlag.OUT.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            QdRsRefund originRefund = refundService.selectRecordByAccDetail(record);
            QdRsRefund newRefund = new QdRsRefund();
            BeanHelper.copy(newRefund, originRefund);
            newRefund.setPkId(null);
            newRefund.setWorkResult(WorkResult.SENT.getCode());
            newRefund.setApAmount(newRefund.getApAmount().multiply(new BigDecimal("-1")));
            newRefund.setPlAmount(newRefund.getPlAmount().multiply(new BigDecimal("-1")));
            QdRsContract contract = contractService.selectContractByNo(record.getContractNo());
            contract.setReceiveAmt(contract.getReceiveAmt().add(record.getTradeAmt()));
            if (refundService.insertRecord(newRefund) == 1) {
                return contractService.updateRecord(contract);
            }
        }
        // 利息录入冲正
        else if (StringUtils.isEmpty(record.getContractNo()) && StringUtils.isEmpty(record.getPlanCtrlNo())
                && InOutFlag.IN.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            return 1;
        }
        return -1;
    }

    /**
     * 处理合同收款
     *
     * @param receive
     * @return
     */
    @Transactional
    public int handleReceiveTrade(QdRsReceive receive) {
        // 查询未限制已监管未删除账户
        QdRsAccount account = accountService.selectCanRecvAccountByNo(receive.getAccountCode());
        if (account == null) {
            throw new RuntimeException("监管账号不存在！");
        }
        //------------------------------------------------
        // 新增交易明细
        QdRsAccDetail accDetail = new QdRsAccDetail();
        accDetail.setAccountCode(receive.getAccountCode());
        accDetail.setAccountName(receive.getCompanyName());
        accDetail.setToAccountCode(receive.getTradeAccCode());
        accDetail.setToAccountName(receive.getTradeAccName());
        accDetail.setToHsBankName(receive.getTradeBankName());
        accDetail.setInoutFlag(InOutFlag.IN.getCode());// 收入
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setTradeAmt(receive.getApAmount());
        accDetail.setBalance(account.getBalance().add(accDetail.getTradeAmt()));
        accDetail.setLocalSerial(receive.getSerial());
        accDetail.setBankSerial(receive.getBankSerial());
        if (ReceiveType.LOAN.getCode().equalsIgnoreCase(receive.getReceiveType())) {
            accDetail.setTradeType(TradeType.HOUSE_CREDIT.getCode());
        } else if (ReceiveType.DOWN_PAYMENT.getCode().equalsIgnoreCase(receive.getReceiveType())) {
            accDetail.setTradeType(TradeType.HOUSE_DOWN_PAYMENT.getCode());
        } else {
            accDetail.setTradeType(TradeType.HOUSE_INCOME.getCode());
        }
        accDetail.setContractNo(receive.getBusinessNo());
        accDetail.setRemark(receive.getRemark());
        accDetail.setStatusFlag(TradeStatus.SUCCESS.getCode());
        //----------------------------------------------------
        // 设置账户余额 和可用余额
        account.setBalance(account.getBalance().add(receive.getApAmount()));
        account.setBalanceUsable(account.getBalanceUsable().add(receive.getApAmount()));

        QdRsContract contract = contractService.selectContractByNo(receive.getBusinessNo());
        contract.setReceiveAmt(receive.getApAmount());

        int rtnCnt = contractService.updateRecord(contract)
                + accDetailService.insertAccDetail(accDetail)
                + accountService.updateRecord(account);
        return rtnCnt;
    }

    /**
     * 处理合同退款交易
     *
     * @param refund
     * @return
     */
    @Transactional
    public int handleRefundTrade(QdRsRefund refund) {
        // 查询未限制已监管未删除账户
        int rtnCnt = 0;
        QdRsAccount account = accountService.selectCanPayAccountByNo(refund.getPayAccount());
        QdRsContract contract = contractService.selectContractByNo(refund.getBusinessNo());
        // 检查余额
        if (refund.getApAmount().compareTo(account.getBalanceUsable()) > 0) {
            throw new RuntimeException("账户余额不足！");
        }

        //======== 开始付款 ===========
        //--------------------------------------------------
        // 新增交易明细
        QdRsAccDetail accDetail = new QdRsAccDetail();
        accDetail.setAccountCode(refund.getPayAccount());
        accDetail.setAccountName(refund.getPayCompanyName());
        accDetail.setToAccountCode(refund.getRecAccount());
        accDetail.setToAccountName(refund.getRecCompanyName());
        accDetail.setToHsBankName(refund.getRecBankName());
        accDetail.setInoutFlag(InOutFlag.OUT.getCode());// 支出
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setTradeAmt(refund.getApAmount());
        accDetail.setBalance(account.getBalance().subtract(accDetail.getTradeAmt()));
        accDetail.setLocalSerial(refund.getSerial());
        accDetail.setBankSerial(refund.getBankSerial());
        accDetail.setTradeType(TradeType.TRANS_BACK.getCode());
        accDetail.setContractNo(refund.getBusinessNo());
        accDetail.setRemark(refund.getRemark());
        accDetail.setStatusFlag(TradeStatus.SUCCESS.getCode());
        //-----------------------------------------------------
        // 设置账户余额 和可用余额
        account.setBalance(account.getBalance().subtract(refund.getApAmount()));
        account.setBalanceUsable(account.getBalanceUsable().subtract(refund.getApAmount()));

        if (ContractStatus.CANCELING.getCode().equalsIgnoreCase(contract.getStatusFlag())) {
            contract.setStatusFlag(ContractStatus.END.getCode());
            contractService.updateRecord(contract);
        }
        refund.setWorkResult(WorkResult.COMMIT.getCode());
        rtnCnt = refundService.updateRecord(refund)
                + accDetailService.insertAccDetail(accDetail)
                + accountService.updateRecord(account);
        return rtnCnt;
    }

    /**
     * 处理计划付款交易
     *
     * @param payout
     * @return
     */
    @Transactional
    public int handlePayoutTrade(QdRsPayout payout) {
        // 查询未限制已监管未删除账户
        QdRsAccount account = accountService.selectCanPayAccountByNo(payout.getPayAccount());
        // 检查余额
        if (payout.getApAmount().compareTo(account.getBalanceUsable()) > 0) {
            throw new RuntimeException("账户余额不足！");
        }
        // 相应计划扣款
        QdRsPlanCtrl planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(payout.getBusinessNo());
        planCtrl.setAvAmount(planCtrl.getAvAmount().subtract(payout.getApAmount()));

        //======== 开始付款 ===========
        //--------------------------------------------------
        // 新增交易明细
        QdRsAccDetail accDetail = new QdRsAccDetail();
        accDetail.setAccountCode(payout.getPayAccount());
        accDetail.setAccountName(payout.getPayCompanyName());
        accDetail.setToAccountCode(payout.getRecAccount());
        accDetail.setToAccountName(payout.getRecCompanyName());
        accDetail.setToHsBankName(payout.getRecBankName());
        accDetail.setInoutFlag(InOutFlag.OUT.getCode());// 支出
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setTradeAmt(payout.getApAmount());
        accDetail.setBalance(account.getBalance().subtract(accDetail.getTradeAmt()));
        accDetail.setLocalSerial(payout.getSerial());
        accDetail.setBankSerial(payout.getBankSerial());
        accDetail.setTradeType(TradeType.PLAN_PAYOUT.getCode());
        accDetail.setPlanCtrlNo(payout.getBusinessNo());
        accDetail.setRemark(payout.getRemark());
        accDetail.setStatusFlag(TradeStatus.SUCCESS.getCode());
        //-----------------------------------------------------
        // 设置账户余额 和可用余额
        account.setBalance(account.getBalance().subtract(payout.getApAmount()));
        account.setBalanceUsable(account.getBalanceUsable().subtract(payout.getApAmount()));
        int rtnCnt = expensesPlanService.updatePlanCtrl(planCtrl)
                + accDetailService.insertAccDetail(accDetail)
                + accountService.updateRecord(account);
        return rtnCnt;
    }

    // 账户冻结
    @Transactional
    public int handleLockAccountByDetail(QdRsAccount qdRsAccount, QdRsLockedaccDetail qdRsLockedaccDetail) {
        qdRsLockedaccDetail.setAccountCode(qdRsAccount.getAccountCode());
        qdRsLockedaccDetail.setAccountName(qdRsAccount.getAccountName());
        qdRsLockedaccDetail.setBalance(qdRsAccount.getBalance());
        qdRsAccount.setBalanceLock(qdRsAccount.getBalanceLock().add(qdRsLockedaccDetail.getBalanceLock()));
        qdRsAccount.setBalanceUsable(qdRsAccount.getBalance().subtract(qdRsAccount.getBalanceLock()));
        if (qdRsLockedaccDetail.getBalanceLock().equals(qdRsAccount.getBalanceUsable())) {
            qdRsLockedaccDetail.setStatusFlag(LockAccStatus.FULL_LOCK.getCode());
        } else if (qdRsLockedaccDetail.getBalanceLock().compareTo(qdRsAccount.getBalanceUsable()) < 0) {
            qdRsLockedaccDetail.setStatusFlag(LockAccStatus.PART_LOCK.getCode());
        }
        return accountService.updateRecord(qdRsAccount) + lockedaccDetailService.insertRecord(qdRsLockedaccDetail);
    }

    // 账户解冻
    @Transactional
    public int handleUnlockAccountByDetail(QdRsAccount qdRsAccount, QdRsLockedaccDetail qdRsLockedaccDetail) {
        qdRsLockedaccDetail.setAccountCode(qdRsAccount.getAccountCode());
        qdRsLockedaccDetail.setAccountName(qdRsAccount.getAccountName());
        qdRsLockedaccDetail.setBalance(qdRsAccount.getBalance());
        qdRsAccount.setBalanceLock(qdRsAccount.getBalanceLock().subtract(qdRsLockedaccDetail.getBalanceLock()));
        qdRsAccount.setBalanceUsable(qdRsAccount.getBalance().subtract(qdRsAccount.getBalanceLock()));
        qdRsLockedaccDetail.setStatusFlag(LockAccStatus.UN_LOCK.getCode());
        return accountService.updateRecord(qdRsAccount) + lockedaccDetailService.insertRecord(qdRsLockedaccDetail);
    }

    // 检查是否有入账未发送的交易
    public boolean isHasUnsendTrade() {
        if (receiveService.isHasUnsend()) {
            throw new RuntimeException("有已入账但未发送的合同交款记录,请先发送！");
        }
        if (lockedaccDetailService.isHasUnSend()) {
            throw new RuntimeException("有待发送的冻结或解冻记录，请先发送！");
        }
        if (payoutService.isHasUnSend()) {
            throw new RuntimeException("有已入账但未发送的计划付款记录，请先发送！");
        }
        if (accDetailService.isHasUnSendCancelRecord()) {
            throw new RuntimeException("有未发送的退票或冲正记录，请先发送！");
        }
        if (refundService.isHasUnsend()) {
            throw new RuntimeException("有未发送的已入账合同退款记录，请先发送！");
        }
        if (accDetailService.isHasUnSendInterest()) {
            throw new RuntimeException("有未发送的已入账利息记录，请先发送！");
        }
        return false;
    }
}
