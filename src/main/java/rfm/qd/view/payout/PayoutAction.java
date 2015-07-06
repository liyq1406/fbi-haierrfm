package rfm.qd.view.payout;

import rfm.qd.common.constant.LimitStatus;
import rfm.qd.common.constant.RefundStatus;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.model.QdRsAccount;
import rfm.qd.repository.model.QdRsPayout;
import rfm.qd.repository.model.QdRsPlanCtrl;
import rfm.qd.service.PayoutService;
import rfm.qd.service.account.AccountService;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.PtenudetailService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-30
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class PayoutAction {
    private Logger logger = LoggerFactory.getLogger(PayoutAction.class);

    private QdRsPayout qdRsPayout;
    @ManagedProperty(value = "#{payoutService}")
    private PayoutService payoutService;
    @ManagedProperty(value = "#{expensesPlanService}")
    private ExpensesPlanService expensesPlanService;
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService toolsService;
    private List<QdRsPayout> qdRsPayoutList;
    private List<QdRsPayout> chkPayoutList;
    private List<QdRsPayout> editPayoutList;
    private List<QdRsPayout> passPayoutList;
    private List<QdRsPayout> refusePayoutList;
    private List<QdRsPlanCtrl> qdRsPlanCtrlList;
    private List<SelectItem> bankCodeList;

    private QdRsPayout selectedRecord;
    private QdRsPayout[] selectedRecords;
    private WorkResult workResult = WorkResult.CREATE;
    private RefundStatus statusFlag = RefundStatus.ACCOUNT_SUCCESS;
    private QdRsPlanCtrl planCtrl;

    @PostConstruct
    public void init() {
        qdRsPayout = new QdRsPayout();
        planCtrl = new QdRsPlanCtrl();
        qdRsPayoutList = new ArrayList<QdRsPayout>();
        if (!initPayout()) {
            initTabList();
        }
    }

    private boolean initPayout() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        String action = (String) context.getExternalContext().getRequestParameterMap().get("action");

        if (!StringUtils.isEmpty(pkid) && "insert".equalsIgnoreCase(action)) {
            planCtrl = expensesPlanService.selectPlanCtrlByPkid(pkid);
            qdRsPayout.setBusinessNo(planCtrl.getPlanCtrlNo());
            qdRsPayout.setRecAccount(planCtrl.getToAccountCode());
            qdRsPayout.setRecCompanyName(planCtrl.getToAccountName());
            qdRsPayout.setRecBankName(planCtrl.getToHsBankName());
            qdRsPayout.setPayCompanyName(planCtrl.getCompanyName());
            qdRsPayout.setPayAccount(planCtrl.getAccountCode());
            qdRsPayout.setPurpose(planCtrl.getPlanDesc());
            bankCodeList = toolsService.getEnuSelectItemList("BANK_CODE", false, false);
            return true;
        } else if (!StringUtils.isEmpty(pkid) && "edit".equalsIgnoreCase(action)) {
            qdRsPayout = payoutService.selectPayoutByPkid(pkid);
            planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(qdRsPayout.getBusinessNo());
            bankCodeList = toolsService.getEnuSelectItemList("BANK_CODE", false, false);
            return true;
        } else if (!StringUtils.isEmpty(pkid) && "query".equalsIgnoreCase(action)) {
            qdRsPayout = payoutService.selectPayoutByPkid(pkid);
            planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(qdRsPayout.getBusinessNo());
        }
        return false;
    }

    public void initTabList() {
        chkPayoutList = payoutService.selectRecordsByWorkResult(WorkResult.CREATE.getCode());
        editPayoutList = payoutService.selectEditRecords();
        passPayoutList = payoutService.selectRecordsByWorkResult(WorkResult.PASS.getCode());
        refusePayoutList = payoutService.selectRecordsByWorkResult(WorkResult.NOTPASS.getCode());
        qdRsPlanCtrlList = expensesPlanService.selectPlanList();

    }

    public String onSave() {

        try {
            QdRsAccount account = accountService.selectCanPayAccountByNo(qdRsPayout.getPayAccount());
            if (account.getLimitFlag().equalsIgnoreCase(LimitStatus.LIMITED.getCode())) {
                MessageUtil.addError("该账户已被限制付款！");
                return null;
            }

            if (qdRsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            if (qdRsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            qdRsPayout.setApAmount(qdRsPayout.getPlAmount());
            if (payoutService.insertRsPayout(qdRsPayout) == 1) {
                MessageUtil.addInfo("受理用款成功！");
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
            } else {
                MessageUtil.addError("受理用款失败！");
            }
        } catch (Exception e) {
            logger.error("受理用款失败！", e.getMessage());
            MessageUtil.addError("受理用款失败！"+e.getMessage());
        }
        return null;
    }

    public String onEdit() {

        try {
            QdRsAccount account = accountService.selectCanPayAccountByNo(qdRsPayout.getPayAccount());
            if (account.getLimitFlag().equalsIgnoreCase(LimitStatus.LIMITED.getCode())) {
                MessageUtil.addError("该账户已被限制付款！");
                return null;
            }
            if (qdRsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            if (qdRsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            qdRsPayout.setApAmount(qdRsPayout.getPlAmount());
            qdRsPayout.setWorkResult(WorkResult.CREATE.getCode());
            if (payoutService.updateRsPayout(qdRsPayout) == 1) {
                MessageUtil.addInfo("受理用款修改成功！");
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
            } else {
                MessageUtil.addError("受理用款修改失败！");
            }
        } catch (Exception e) {
            logger.error("受理用款修改失败！", e.getMessage());
            MessageUtil.addError("受理用款修改失败！"+e.getMessage());
        }
        return null;
    }

    public String onDelete() {

        try {

            qdRsPayout.setDeletedFlag("1");
            if (payoutService.updateRsPayout(qdRsPayout) == 1) {
                MessageUtil.addInfo("受理用款修删除成功！");
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
            } else {
                MessageUtil.addError("受理用款删除失败！");
            }
        } catch (Exception e) {
            logger.error("受理用款删除失败！", e.getMessage());
            MessageUtil.addError("受理用款删除失败！"+e.getMessage());
        }
        return null;
    }

    public String onCheck() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔记录！");
        } else {
            try {
                payoutService.updateRsPayoutsToWorkResult(selectedRecords, WorkResult.PASS.getCode());
            } catch (Exception e) {
                logger.error("复核失败." + e.getMessage());
                MessageUtil.addError("复核失败." + e.getMessage());
                return null;
            }
            MessageUtil.addInfo("复核成功!");
            initTabList();
        }
        return null;
    }

    public String onRefuse() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔记录！");
        } else {
            try {
                payoutService.updateRsPayoutsToWorkResult(selectedRecords, WorkResult.NOTPASS.getCode());
            } catch (Exception e) {
                logger.error("退回失败." + e.getMessage());
                MessageUtil.addError("退回失败." + e.getMessage());
                return null;
            }
            MessageUtil.addInfo("退回完成!");
            initTabList();
        }
        return null;
    }

    /* public void onTabChange(TabChangeEvent event) {
         initTabList();
      }*/

    //=========================================

    public QdRsPayout getQdRsPayout() {
        return qdRsPayout;
    }

    public void setQdRsPayout(QdRsPayout qdRsPayout) {
        this.qdRsPayout = qdRsPayout;
    }

    public PayoutService getPayoutService() {
        return payoutService;
    }

    public List<SelectItem> getBankCodeList() {
        return bankCodeList;
    }

    public void setBankCodeList(List<SelectItem> bankCodeList) {
        this.bankCodeList = bankCodeList;
    }

    public List<QdRsPayout> getEditPayoutList() {
        return editPayoutList;
    }

    public void setEditPayoutList(List<QdRsPayout> editPayoutList) {
        this.editPayoutList = editPayoutList;
    }

    public PtenudetailService getToolsService() {
        return toolsService;
    }

    public void setToolsService(PtenudetailService toolsService) {
        this.toolsService = toolsService;
    }

    public void setPayoutService(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    public List<QdRsPayout> getQdRsPayoutList() {
        return qdRsPayoutList;
    }

    public void setQdRsPayoutList(List<QdRsPayout> qdRsPayoutList) {
        this.qdRsPayoutList = qdRsPayoutList;
    }

    public List<QdRsPayout> getChkPayoutList() {
        return chkPayoutList;
    }

    public void setChkPayoutList(List<QdRsPayout> chkPayoutList) {
        this.chkPayoutList = chkPayoutList;
    }

    public QdRsPayout getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(QdRsPayout selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }

    public QdRsPayout[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(QdRsPayout[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public List<QdRsPayout> getPassPayoutList() {
        return passPayoutList;
    }

    public void setPassPayoutList(List<QdRsPayout> passPayoutList) {
        this.passPayoutList = passPayoutList;
    }

    public List<QdRsPayout> getRefusePayoutList() {
        return refusePayoutList;
    }

    public void setRefusePayoutList(List<QdRsPayout> refusePayoutList) {
        this.refusePayoutList = refusePayoutList;
    }

    public ExpensesPlanService getExpensesPlanService() {
        return expensesPlanService;
    }

    public void setExpensesPlanService(ExpensesPlanService expensesPlanService) {
        this.expensesPlanService = expensesPlanService;
    }

    public List<QdRsPlanCtrl> getQdRsPlanCtrlList() {
        return qdRsPlanCtrlList;
    }

    public void setQdRsPlanCtrlList(List<QdRsPlanCtrl> qdRsPlanCtrlList) {
        this.qdRsPlanCtrlList = qdRsPlanCtrlList;
    }

    public QdRsPlanCtrl getPlanCtrl() {
        return planCtrl;
    }

    public void setPlanCtrl(QdRsPlanCtrl planCtrl) {
        this.planCtrl = planCtrl;
    }

    public RefundStatus getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(RefundStatus statusFlag) {
        this.statusFlag = statusFlag;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
