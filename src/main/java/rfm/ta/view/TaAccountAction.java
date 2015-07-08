package rfm.ta.view;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.gateway.sbs.helper.BeanHelper;
import rfm.ta.gateway.sbs.taservice.TaSbsService;
import rfm.ta.repository.model.TaRsAccount;
import rfm.ta.service.account.TaAccountService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaAccountAction {
    private static final Logger logger = LoggerFactory.getLogger(TaAccountAction.class);
    @ManagedProperty(value = "#{taAccountService}")
    private TaAccountService taAccountService;
    @ManagedProperty(value = "#{taSbsTxnService}")
    private TaSbsService taSbsTxnService;

    private List<TaRsAccount> taRsAccountList;
    private String confirmAccountNo;

    private TaRsAccount taRsAccount;
    private TaRsAccount taRsAccountSeled;
    private String rtnFlag;
    private String action;
    private String pkid;
    private boolean updateable = false;
    private boolean deleteable = false;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        action = params.get("actionType");
        pkid = params.get("pkid");
        querySelectedRecords();
        if (action != null) {
            taRsAccount = taAccountService.qryRecord(pkid);
            if ("update".equals(action)) {
                updateable = true;
            }
            if ("del".equals(action)) {
                deleteable = true;
            }
        }else{
            taRsAccount=new TaRsAccount();
        }
    }

    private void querySelectedRecords() {
        taRsAccountList = taAccountService.qryAllRecords();
    }

    private void querySelectedRecords(TaRsAccount act) {
        taRsAccountList = taAccountService.selectedRecordsByCondition(act.getAccType(), act.getAccId(), act.getAccName());
    }

    public void onBtnQueryClick() {
        querySelectedRecords(taRsAccount);
    }

    public String reset() {
        this.taRsAccount = new TaRsAccount();
        if (!taRsAccountList.isEmpty()) {
            taRsAccountList.clear();
        }
        return null;
    }

    public String onAdd() {
        try {
            if (!confirmAccountNo.equalsIgnoreCase(taRsAccount.getAccId())) {
                MessageUtil.addError("两次输入的监管账户号不一致！");
                return null;
            }
            // 初始帐户余额均为可用
            taAccountService.insertRecord(taRsAccount);
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("新增数据完成。");
        querySelectedRecords();
        this.taRsAccount = new TaRsAccount();
        confirmAccountNo = "";
        return null;
    }

    public String onUpd(){
        try {
            if (!confirmAccountNo.equalsIgnoreCase(taRsAccount.getAccId())) {
                MessageUtil.addError("两次输入的监管账户号不一致！");
                return null;
            }
            // 初始帐户余额均为可用
            taAccountService.insertRecord(taRsAccount);
        } catch (Exception e) {
            logger.error("修改数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("修改数据完成。");
        querySelectedRecords();
        this.taRsAccount = new TaRsAccount();
        confirmAccountNo = "";
        return null;
    }
    public String onDel(){
        return null;
    }

    public String onClick_ListToDetail() {
        return "accountEditDetail.xhtml?faces-redirect=true";
    }
    public String onClick_DetailToList() {
        return "accountEdit.xhtml?faces-redirect=true";
    }

    public String onEnable(){

        return null;
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaSbsService getTaSbsTxnService() {
        return taSbsTxnService;
    }

    public void setTaSbsTxnService(TaSbsService taSbsTxnService) {
        this.taSbsTxnService = taSbsTxnService;
    }

    public TaAccountService getTaAccountService() {
        return taAccountService;
    }

    public void setTaAccountService(TaAccountService taAccountService) {
        this.taAccountService = taAccountService;
    }

    public String getConfirmAccountNo() {
        return confirmAccountNo;
    }

    public void setConfirmAccountNo(String confirmAccountNo) {
        this.confirmAccountNo = confirmAccountNo;
    }

    public List<TaRsAccount> getTaRsAccountList() {
        return taRsAccountList;
    }

    public void setTaRsAccountList(List<TaRsAccount> taRsAccountList) {
        this.taRsAccountList = taRsAccountList;
    }

    public String getRtnFlag() {
        return rtnFlag;
    }

    public void setRtnFlag(String rtnFlag) {
        this.rtnFlag = rtnFlag;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public TaRsAccount getTaRsAccount() {
        return taRsAccount;
    }

    public void setTaRsAccount(TaRsAccount taRsAccount) {
        this.taRsAccount = taRsAccount;
    }

    public boolean isUpdateable() {
        return updateable;
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

    public boolean isDeleteable() {
        return deleteable;
    }

    public void setDeleteable(boolean deleteable) {
        this.deleteable = deleteable;
    }

    public TaRsAccount getTaRsAccountSeled() {
        return taRsAccountSeled;
    }

    public void setTaRsAccountSeled(TaRsAccount taRsAccountSeled) {
        this.taRsAccountSeled = taRsAccountSeled;
    }
}
