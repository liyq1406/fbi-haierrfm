package rfm.qd.view.payout;

import rfm.qd.common.constant.LimitStatus;
import rfm.qd.common.constant.RefundStatus;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.model.RsAccount;
import rfm.qd.repository.model.RsPayout;
import rfm.qd.repository.model.RsPlanCtrl;
import rfm.qd.service.PayoutService;
import rfm.qd.service.account.AccountService;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.ToolsService;

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

    private RsPayout rsPayout;
    @ManagedProperty(value = "#{payoutService}")
    private PayoutService payoutService;
    @ManagedProperty(value = "#{expensesPlanService}")
    private ExpensesPlanService expensesPlanService;
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;
    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    private List<RsPayout> rsPayoutList;
    private List<RsPayout> chkPayoutList;
    private List<RsPayout> editPayoutList;
    private List<RsPayout> passPayoutList;
    private List<RsPayout> refusePayoutList;
    private List<RsPlanCtrl> rsPlanCtrlList;
    private List<SelectItem> bankCodeList;

    private RsPayout selectedRecord;
    private RsPayout[] selectedRecords;
    private WorkResult workResult = WorkResult.CREATE;
    private RefundStatus statusFlag = RefundStatus.ACCOUNT_SUCCESS;
    private RsPlanCtrl planCtrl;

    @PostConstruct
    public void init() {
        rsPayout = new RsPayout();
        planCtrl = new RsPlanCtrl();
        rsPayoutList = new ArrayList<RsPayout>();
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
            rsPayout.setBusinessNo(planCtrl.getPlanCtrlNo());
            rsPayout.setRecAccount(planCtrl.getToAccountCode());
            rsPayout.setRecCompanyName(planCtrl.getToAccountName());
            rsPayout.setRecBankName(planCtrl.getToHsBankName());
            rsPayout.setPayCompanyName(planCtrl.getCompanyName());
            rsPayout.setPayAccount(planCtrl.getAccountCode());
            rsPayout.setPurpose(planCtrl.getPlanDesc());
            bankCodeList = toolsService.getEnuSelectItemList("BANK_CODE", false, false);
            return true;
        } else if (!StringUtils.isEmpty(pkid) && "edit".equalsIgnoreCase(action)) {
            rsPayout = payoutService.selectPayoutByPkid(pkid);
            planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(rsPayout.getBusinessNo());
            bankCodeList = toolsService.getEnuSelectItemList("BANK_CODE", false, false);
            return true;
        } else if (!StringUtils.isEmpty(pkid) && "query".equalsIgnoreCase(action)) {
            rsPayout = payoutService.selectPayoutByPkid(pkid);
            planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(rsPayout.getBusinessNo());
        }
        return false;
    }

    public void initTabList() {
        chkPayoutList = payoutService.selectRecordsByWorkResult(WorkResult.CREATE.getCode());
        editPayoutList = payoutService.selectEditRecords();
        passPayoutList = payoutService.selectRecordsByWorkResult(WorkResult.PASS.getCode());
        refusePayoutList = payoutService.selectRecordsByWorkResult(WorkResult.NOTPASS.getCode());
        rsPlanCtrlList = expensesPlanService.selectPlanList();

    }

    public String onSave() {

        try {
            RsAccount account = accountService.selectCanPayAccountByNo(rsPayout.getPayAccount());
            if (account.getLimitFlag().equalsIgnoreCase(LimitStatus.LIMITED.getCode())) {
                MessageUtil.addError("该账户已被限制付款！");
                return null;
            }

            if (rsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            if (rsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            rsPayout.setApAmount(rsPayout.getPlAmount());
            if (payoutService.insertRsPayout(rsPayout) == 1) {
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
            RsAccount account = accountService.selectCanPayAccountByNo(rsPayout.getPayAccount());
            if (account.getLimitFlag().equalsIgnoreCase(LimitStatus.LIMITED.getCode())) {
                MessageUtil.addError("该账户已被限制付款！");
                return null;
            }
            if (rsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            if (rsPayout.getPlAmount().compareTo(planCtrl.getAvAmount()) > 0) {
                MessageUtil.addError("申请金额不得大于可用金额！");
                return null;
            }
            rsPayout.setApAmount(rsPayout.getPlAmount());
            rsPayout.setWorkResult(WorkResult.CREATE.getCode());
            if (payoutService.updateRsPayout(rsPayout) == 1) {
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

            rsPayout.setDeletedFlag("1");
            if (payoutService.updateRsPayout(rsPayout) == 1) {
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

    public RsPayout getRsPayout() {
        return rsPayout;
    }

    public void setRsPayout(RsPayout rsPayout) {
        this.rsPayout = rsPayout;
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

    public List<RsPayout> getEditPayoutList() {
        return editPayoutList;
    }

    public void setEditPayoutList(List<RsPayout> editPayoutList) {
        this.editPayoutList = editPayoutList;
    }

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public void setPayoutService(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    public List<RsPayout> getRsPayoutList() {
        return rsPayoutList;
    }

    public void setRsPayoutList(List<RsPayout> rsPayoutList) {
        this.rsPayoutList = rsPayoutList;
    }

    public List<RsPayout> getChkPayoutList() {
        return chkPayoutList;
    }

    public void setChkPayoutList(List<RsPayout> chkPayoutList) {
        this.chkPayoutList = chkPayoutList;
    }

    public RsPayout getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(RsPayout selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }

    public RsPayout[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(RsPayout[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public List<RsPayout> getPassPayoutList() {
        return passPayoutList;
    }

    public void setPassPayoutList(List<RsPayout> passPayoutList) {
        this.passPayoutList = passPayoutList;
    }

    public List<RsPayout> getRefusePayoutList() {
        return refusePayoutList;
    }

    public void setRefusePayoutList(List<RsPayout> refusePayoutList) {
        this.refusePayoutList = refusePayoutList;
    }

    public ExpensesPlanService getExpensesPlanService() {
        return expensesPlanService;
    }

    public void setExpensesPlanService(ExpensesPlanService expensesPlanService) {
        this.expensesPlanService = expensesPlanService;
    }

    public List<RsPlanCtrl> getRsPlanCtrlList() {
        return rsPlanCtrlList;
    }

    public void setRsPlanCtrlList(List<RsPlanCtrl> rsPlanCtrlList) {
        this.rsPlanCtrlList = rsPlanCtrlList;
    }

    public RsPlanCtrl getPlanCtrl() {
        return planCtrl;
    }

    public void setPlanCtrl(RsPlanCtrl planCtrl) {
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
