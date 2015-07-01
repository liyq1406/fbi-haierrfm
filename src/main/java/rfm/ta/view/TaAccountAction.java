package rfm.ta.view;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.TaAccStatus;
import rfm.ta.common.enums.TaAccType;
import rfm.ta.gateway.sbs.taservice.TaSbsService;
import rfm.ta.repository.model.TaRsAccount;
import rfm.ta.service.account.TaAccountService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    private TaRsAccount taRsAccount;
    private List<TaRsAccount> taRsAccountList;
    private String confirmAccountNo;
    private TaAccStatus taAccStatus = TaAccStatus.INIT;

    private List<SelectItem> accTypeList;

    @PostConstruct
    public void init() {
        this.taRsAccount = new TaRsAccount();
        accTypeList=new ArrayList<>();
        SelectItem selectItem=new SelectItem(TaAccType.AccType0.getCode(),TaAccType.AccType0.getTitle());
        accTypeList.add(selectItem);
        querySelectedRecords();
    }

    private void querySelectedRecords() {
        taRsAccountList = taAccountService.qryAllRecords();
    }

    private void querySelectedRecords(TaRsAccount act) {
        /*taRsAccountList = taAccountService.selectedRecordsByCondition(act.getPresellNo(), act.getCompanyId(), act.getAccountCode(),
                act.getAccountName());*/
    }

    public String onBtnQueryClick() {
        querySelectedRecords(taRsAccount);
        return null;
    }

    // 增
    public String insertRecord() {
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

    public String reset() {
        this.taRsAccount = new TaRsAccount();
        if (!taRsAccountList.isEmpty()) {
            taRsAccountList.clear();
        }
        return null;
    }

    public List<SelectItem> getAccTypeList() {
        return accTypeList;
    }

    public void setAccTypeList(List<SelectItem> accTypeList) {
        this.accTypeList = accTypeList;
    }

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

    public TaAccStatus getTaAccStatus() {
        return taAccStatus;
    }

    public void setTaAccStatus(TaAccStatus taAccStatus) {
        this.taAccStatus = taAccStatus;
    }

    public TaRsAccount getTaRsAccount() {
        return taRsAccount;
    }

    public void setTaRsAccount(TaRsAccount taRsAccount) {
        this.taRsAccount = taRsAccount;
    }

    public List<TaRsAccount> getTaRsAccountList() {
        return taRsAccountList;
    }

    public void setTaRsAccountList(List<TaRsAccount> taRsAccountList) {
        this.taRsAccountList = taRsAccountList;
    }
}
