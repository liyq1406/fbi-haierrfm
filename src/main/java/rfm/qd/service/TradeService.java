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
 * Time: ����2:22
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

    // R-���� D-��Ʊ
    @Transactional
    public int handleCancelAccDetail(RsAccDetail record, ChangeFlag changeFlag) throws Exception {
        // ��ѯδ�����Ѽ��δɾ���˻�
        RsAccount account = accountService.selectCanPayAccountByNo(record.getAccountCode());
        if (account == null) {
            throw new RuntimeException("����˺Ų����ڣ�");
        }

        //-----------------------------------------------
        // ����������ϸ
        RsAccDetail accDetail = new RsAccDetail();
        accDetail.setAccountCode(record.getAccountCode());
        accDetail.setAccountName(record.getAccountName());
        accDetail.setToAccountCode(record.getToAccountCode());
        accDetail.setToAccountName(record.getToAccountName());
        accDetail.setToHsBankName(record.getToHsBankName());
        accDetail.setTradeType(TradeType.OTHERS.getCode());
        accDetail.setTradeAmt(record.getTradeAmt());
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setContractNo(record.getContractNo());
        accDetail.setChangeFlag(changeFlag.getCode());      // ���: R-���� D-��Ʊ
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
        if (ChangeFlag.CANCEL.getCode().equalsIgnoreCase(changeFlag.getCode())) {  // ����

            //  ��������ҵ�����
            if (handleCancelBiBack(record) == 1) {
                rtnCnt = accDetailService.insertAccDetail(accDetail) + accDetailService.updateAccDetail(record)
                        + accountService.updateRecord(account);
            }
            if (clientBiService.sendAccDetailCancel(record) == -1) {
                throw new RuntimeException("���ͳ������׼�¼ʧ��!");
            }

        } else if (ChangeFlag.BACK.getCode().equalsIgnoreCase(changeFlag.getCode())) {   // ��Ʊ

            //  ������Ʊҵ�����
            if (handleCancelBiBack(record) == 1) {
                rtnCnt = accDetailService.insertAccDetail(accDetail) + accDetailService.updateAccDetail(record)
                        + accountService.updateRecord(account);
            }
            if (clientBiService.sendAccDetailBack(record) == -1) {
                throw new RuntimeException("������Ʊ���׼�¼ʧ��!");
            }
        }
        return rtnCnt;
    }

    //  ����ҵ�����

    private int handleCancelBiBack(RsAccDetail record) {
        // �ƻ��������
        if (StringUtils.isEmpty(record.getContractNo()) && !StringUtils.isEmpty(record.getPlanCtrlNo())) {
            RsPayout origiPayout = payoutService.selectRecordByAccDetail(record);
            RsPayout payout = new RsPayout();
            BeanHelper.copy(payout, origiPayout);
            payout.setPkId(null);
            payout.setApAmount(payout.getApAmount().multiply(new BigDecimal("-1")));
            payout.setPlAmount(payout.getPlAmount().multiply(new BigDecimal("-1")));
            payout.setWorkResult(WorkResult.SENT.getCode());

            RsPlanCtrl planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(record.getPlanCtrlNo());
            planCtrl.setAvAmount(planCtrl.getAvAmount().add(record.getTradeAmt()));
            if (payoutService.insertRsPayout(payout) == 1) {
                return expensesPlanService.updatePlanCtrl(planCtrl);
            } else {
                throw new RuntimeException("�ƻ������¼����ʧ�ܣ�");
            }
        }
        // ��ͬ�տ�
        else if (!StringUtils.isEmpty(record.getContractNo()) && StringUtils.isEmpty(record.getPlanCtrlNo())
                && InOutFlag.IN.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            RsReceive OrigiReceive = receiveService.selectRecordByAccDetail(record);
            RsReceive newReceive = new RsReceive();
            BeanHelper.copy(newReceive, OrigiReceive);
            newReceive.setPkId(null);
            newReceive.setWorkResult(WorkResult.SENT.getCode());
            newReceive.setApAmount(newReceive.getApAmount().multiply(new BigDecimal("-1")));
            newReceive.setPlAmount(newReceive.getPlAmount().multiply(new BigDecimal("-1")));

            RsContract contract = contractService.selectContractByNo(record.getContractNo());
            contract.setReceiveAmt(contract.getReceiveAmt().subtract(record.getTradeAmt()));
            if (receiveService.insertRecord(newReceive) == 1) {
                return contractService.updateRecord(contract);
            }

        }
        // ��ͬ�˿�
        else if (!StringUtils.isEmpty(record.getContractNo()) && StringUtils.isEmpty(record.getPlanCtrlNo())
                && InOutFlag.OUT.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            RsRefund originRefund = refundService.selectRecordByAccDetail(record);
            RsRefund newRefund = new RsRefund();
            BeanHelper.copy(newRefund, originRefund);
            newRefund.setPkId(null);
            newRefund.setWorkResult(WorkResult.SENT.getCode());
            newRefund.setApAmount(newRefund.getApAmount().multiply(new BigDecimal("-1")));
            newRefund.setPlAmount(newRefund.getPlAmount().multiply(new BigDecimal("-1")));
            RsContract contract = contractService.selectContractByNo(record.getContractNo());
            contract.setReceiveAmt(contract.getReceiveAmt().add(record.getTradeAmt()));
            if (refundService.insertRecord(newRefund) == 1) {
                return contractService.updateRecord(contract);
            }
        }
        // ��Ϣ¼�����
        else if (StringUtils.isEmpty(record.getContractNo()) && StringUtils.isEmpty(record.getPlanCtrlNo())
                && InOutFlag.IN.getCode().equalsIgnoreCase(record.getInoutFlag())) {
            return 1;
        }
        return -1;
    }

    /**
     * ������ͬ�տ�
     *
     * @param receive
     * @return
     */
    @Transactional
    public int handleReceiveTrade(RsReceive receive) {
        // ��ѯδ�����Ѽ��δɾ���˻�
        RsAccount account = accountService.selectCanRecvAccountByNo(receive.getAccountCode());
        if (account == null) {
            throw new RuntimeException("����˺Ų����ڣ�");
        }
        //------------------------------------------------
        // ����������ϸ
        RsAccDetail accDetail = new RsAccDetail();
        accDetail.setAccountCode(receive.getAccountCode());
        accDetail.setAccountName(receive.getCompanyName());
        accDetail.setToAccountCode(receive.getTradeAccCode());
        accDetail.setToAccountName(receive.getTradeAccName());
        accDetail.setToHsBankName(receive.getTradeBankName());
        accDetail.setInoutFlag(InOutFlag.IN.getCode());// ����
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
        // �����˻���� �Ϳ������
        account.setBalance(account.getBalance().add(receive.getApAmount()));
        account.setBalanceUsable(account.getBalanceUsable().add(receive.getApAmount()));

        RsContract contract = contractService.selectContractByNo(receive.getBusinessNo());
        contract.setReceiveAmt(receive.getApAmount());

        int rtnCnt = contractService.updateRecord(contract)
                + accDetailService.insertAccDetail(accDetail)
                + accountService.updateRecord(account);
        return rtnCnt;
    }

    /**
     * ������ͬ�˿��
     *
     * @param refund
     * @return
     */
    @Transactional
    public int handleRefundTrade(RsRefund refund) {
        // ��ѯδ�����Ѽ��δɾ���˻�
        int rtnCnt = 0;
        RsAccount account = accountService.selectCanPayAccountByNo(refund.getPayAccount());
        RsContract contract = contractService.selectContractByNo(refund.getBusinessNo());
        // ������
        if (refund.getApAmount().compareTo(account.getBalanceUsable()) > 0) {
            throw new RuntimeException("�˻����㣡");
        }

        //======== ��ʼ���� ===========
        //--------------------------------------------------
        // ����������ϸ
        RsAccDetail accDetail = new RsAccDetail();
        accDetail.setAccountCode(refund.getPayAccount());
        accDetail.setAccountName(refund.getPayCompanyName());
        accDetail.setToAccountCode(refund.getRecAccount());
        accDetail.setToAccountName(refund.getRecCompanyName());
        accDetail.setToHsBankName(refund.getRecBankName());
        accDetail.setInoutFlag(InOutFlag.OUT.getCode());// ֧��
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
        // �����˻���� �Ϳ������
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
     * �����ƻ������
     *
     * @param payout
     * @return
     */
    @Transactional
    public int handlePayoutTrade(RsPayout payout) {
        // ��ѯδ�����Ѽ��δɾ���˻�
        RsAccount account = accountService.selectCanPayAccountByNo(payout.getPayAccount());
        // ������
        if (payout.getApAmount().compareTo(account.getBalanceUsable()) > 0) {
            throw new RuntimeException("�˻����㣡");
        }
        // ��Ӧ�ƻ��ۿ�
        RsPlanCtrl planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(payout.getBusinessNo());
        planCtrl.setAvAmount(planCtrl.getAvAmount().subtract(payout.getApAmount()));

        //======== ��ʼ���� ===========
        //--------------------------------------------------
        // ����������ϸ
        RsAccDetail accDetail = new RsAccDetail();
        accDetail.setAccountCode(payout.getPayAccount());
        accDetail.setAccountName(payout.getPayCompanyName());
        accDetail.setToAccountCode(payout.getRecAccount());
        accDetail.setToAccountName(payout.getRecCompanyName());
        accDetail.setToHsBankName(payout.getRecBankName());
        accDetail.setInoutFlag(InOutFlag.OUT.getCode());// ֧��
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
        // �����˻���� �Ϳ������
        account.setBalance(account.getBalance().subtract(payout.getApAmount()));
        account.setBalanceUsable(account.getBalanceUsable().subtract(payout.getApAmount()));
        int rtnCnt = expensesPlanService.updatePlanCtrl(planCtrl)
                + accDetailService.insertAccDetail(accDetail)
                + accountService.updateRecord(account);
        return rtnCnt;
    }

    // �˻�����
    @Transactional
    public int handleLockAccountByDetail(RsAccount rsAccount, RsLockedaccDetail rsLockedaccDetail) {
        rsLockedaccDetail.setAccountCode(rsAccount.getAccountCode());
        rsLockedaccDetail.setAccountName(rsAccount.getAccountName());
        rsLockedaccDetail.setBalance(rsAccount.getBalance());
        rsAccount.setBalanceLock(rsAccount.getBalanceLock().add(rsLockedaccDetail.getBalanceLock()));
        rsAccount.setBalanceUsable(rsAccount.getBalance().subtract(rsAccount.getBalanceLock()));
        if (rsLockedaccDetail.getBalanceLock().equals(rsAccount.getBalanceUsable())) {
            rsLockedaccDetail.setStatusFlag(LockAccStatus.FULL_LOCK.getCode());
        } else if (rsLockedaccDetail.getBalanceLock().compareTo(rsAccount.getBalanceUsable()) < 0) {
            rsLockedaccDetail.setStatusFlag(LockAccStatus.PART_LOCK.getCode());
        }
        return accountService.updateRecord(rsAccount) + lockedaccDetailService.insertRecord(rsLockedaccDetail);
    }

    // �˻��ⶳ
    @Transactional
    public int handleUnlockAccountByDetail(RsAccount rsAccount, RsLockedaccDetail rsLockedaccDetail) {
        rsLockedaccDetail.setAccountCode(rsAccount.getAccountCode());
        rsLockedaccDetail.setAccountName(rsAccount.getAccountName());
        rsLockedaccDetail.setBalance(rsAccount.getBalance());
        rsAccount.setBalanceLock(rsAccount.getBalanceLock().subtract(rsLockedaccDetail.getBalanceLock()));
        rsAccount.setBalanceUsable(rsAccount.getBalance().subtract(rsAccount.getBalanceLock()));
        rsLockedaccDetail.setStatusFlag(LockAccStatus.UN_LOCK.getCode());
        return accountService.updateRecord(rsAccount) + lockedaccDetailService.insertRecord(rsLockedaccDetail);
    }

    // ����Ƿ�������δ���͵Ľ���
    public boolean isHasUnsendTrade() {
        if (receiveService.isHasUnsend()) {
            throw new RuntimeException("�������˵�δ���͵ĺ�ͬ�����¼,���ȷ��ͣ�");
        }
        if (lockedaccDetailService.isHasUnSend()) {
            throw new RuntimeException("�д����͵Ķ����ⶳ��¼�����ȷ��ͣ�");
        }
        if (payoutService.isHasUnSend()) {
            throw new RuntimeException("�������˵�δ���͵ļƻ������¼�����ȷ��ͣ�");
        }
        if (accDetailService.isHasUnSendCancelRecord()) {
            throw new RuntimeException("��δ���͵���Ʊ�������¼�����ȷ��ͣ�");
        }
        if (refundService.isHasUnsend()) {
            throw new RuntimeException("��δ���͵������˺�ͬ�˿��¼�����ȷ��ͣ�");
        }
        if (accDetailService.isHasUnSendInterest()) {
            throw new RuntimeException("��δ���͵���������Ϣ��¼�����ȷ��ͣ�");
        }
        return false;
    }
}