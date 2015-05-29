package rfm.qd.view.account;

import rfm.qd.common.constant.LockAccStatus;
import rfm.qd.common.constant.SendFlag;
import rfm.qd.repository.model.RsAccount;
import rfm.qd.repository.model.RsLockedaccDetail;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.LockedaccDetailService;
import rfm.qd.service.TradeService;
import rfm.qd.service.account.AccountService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.TabChangeEvent;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-5
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class AccountLockAction {

    private RsAccount rsAccount;
    private RsLockedaccDetail rsLockedaccDetail;
    private List<RsAccount> accountList;
    private List<RsLockedaccDetail> unSendLockDetailList;
    private List<RsLockedaccDetail> sentLockDetailList;
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;
    @ManagedProperty(value = "#{lockedaccDetailService}")
    private LockedaccDetailService lockedaccDetailService;
    @ManagedProperty(value = "#{tradeService}")
    private TradeService tradeService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;
    private BigDecimal lockConfirmAmt;
    private LockAccStatus lockStatus = LockAccStatus.FULL_LOCK;
    private RsLockedaccDetail[] selectedRecords;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        if (!StringUtils.isEmpty(pkid)) {
            rsAccount = accountService.selectedRecordByPkid(pkid);
        }
        rsLockedaccDetail = new RsLockedaccDetail();
        accountList = accountService.qryAllRecords();
        initLockDetailList();
    }

    public void initLockDetailList() {
        if(unSendLockDetailList != null) {
            unSendLockDetailList.clear();
        }
        if(sentLockDetailList != null) {
            sentLockDetailList.clear();
        }
        unSendLockDetailList = lockedaccDetailService.selectRecordsBySendflagAndNotEqualLockstatus(
                SendFlag.UN_SEND.getCode(), LockAccStatus.UN_LOCK.getCode());
        sentLockDetailList = lockedaccDetailService.selectRecordsBySendflagAndNotEqualLockstatus(
                SendFlag.SENT.getCode(), LockAccStatus.UN_LOCK.getCode());
    }

    public void onTabChange(TabChangeEvent event) {
        if(accountList != null) {
            accountList.clear();
        }
        accountList = accountService.qryAllRecords();
        initLockDetailList();
    }

    public String onSave() {
        if (lockConfirmAmt.compareTo(rsLockedaccDetail.getBalanceLock()) != 0) {
            MessageUtil.addError("两次输入的冻结金额不一致！");
            return null;
        }
        if (rsLockedaccDetail.getBalanceLock().compareTo(rsAccount.getBalanceUsable()) > 0) {
            MessageUtil.addError("冻结金额数不可大于账户可用余额！");
            return null;
        }
        if(lockConfirmAmt.compareTo(new BigDecimal(0)) <= 0) {
             MessageUtil.addError("冻结金额数必须大于0.00！");
            return null;
        }

        try {
            if (tradeService.handleLockAccountByDetail(rsAccount, rsLockedaccDetail) == 2) {
                MessageUtil.addInfo("成功冻结账户！");
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
                initLockDetailList();
            } else {
                MessageUtil.addError("账户冻结失败，请联系管理员！");
            }
        } catch (Exception e) {
            MessageUtil.addError("操作失败！" + e.getMessage());
        }
        return null;
    }

    public String onAllSend() {
        if (unSendLockDetailList.isEmpty()) {
            MessageUtil.addWarn("没有待发送数据！");
            return null;
        }
        try {
            for (RsLockedaccDetail record : unSendLockDetailList) {
                if(sendOneLockDetail(record) != 1){
                   throw new RuntimeException("发送失败！账号："+record.getAccountCode());
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("操作失败！" + e.getMessage());
        }
        MessageUtil.addInfo("发送成功！");
        initLockDetailList();
        return null;
    }

    public String onMultiSend() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选中一项数据记录！");
            return null;
        }
        try {
            for (RsLockedaccDetail record : selectedRecords) {
                if(sendOneLockDetail(record) != 1){
                   throw new RuntimeException("发送失败！账号："+record.getAccountCode());
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("操作失败！" + e.getMessage());
        }
        MessageUtil.addInfo("发送成功！");
        initLockDetailList();
        return null;
    }

    private int sendOneLockDetail(RsLockedaccDetail record) throws Exception {

        return clientBiService.sendLockAccDetail(record);
    }

    //==========================================

    public RsAccount getRsAccount() {
        return rsAccount;
    }

    public void setRsAccount(RsAccount rsAccount) {
        this.rsAccount = rsAccount;
    }

    public List<RsAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<RsAccount> accountList) {
        this.accountList = accountList;
    }

    public List<RsLockedaccDetail> getSentLockDetailList() {
        return sentLockDetailList;
    }

    public void setSentLockDetailList(List<RsLockedaccDetail> sentLockDetailList) {
        this.sentLockDetailList = sentLockDetailList;
    }

    public List<RsLockedaccDetail> getUnSendLockDetailList() {
        return unSendLockDetailList;
    }

    public void setUnSendLockDetailList(List<RsLockedaccDetail> unSendLockDetailList) {
        this.unSendLockDetailList = unSendLockDetailList;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public BigDecimal getLockConfirmAmt() {
        return lockConfirmAmt;
    }

    public void setLockConfirmAmt(BigDecimal lockConfirmAmt) {
        this.lockConfirmAmt = lockConfirmAmt;
    }

    public RsLockedaccDetail getRsLockedaccDetail() {
        return rsLockedaccDetail;
    }

    public void setRsLockedaccDetail(RsLockedaccDetail rsLockedaccDetail) {
        this.rsLockedaccDetail = rsLockedaccDetail;
    }

    public LockedaccDetailService getLockedaccDetailService() {
        return lockedaccDetailService;
    }

    public void setLockedaccDetailService(LockedaccDetailService lockedaccDetailService) {
        this.lockedaccDetailService = lockedaccDetailService;
    }

    public LockAccStatus getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(LockAccStatus lockStatus) {
        this.lockStatus = lockStatus;
    }

    public TradeService getTradeService() {
        return tradeService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public RsLockedaccDetail[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(RsLockedaccDetail[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }
}
