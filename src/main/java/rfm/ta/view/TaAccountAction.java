package rfm.ta.view;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.gateway.sbs.taservice.TaSbsService;
import rfm.ta.repository.model.TaRsAccount;
import rfm.ta.service.account.TaAccountService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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

    private List<TaRsAccount> taRsAccountList;
    private String confirmAccountNo;

    private TaRsAccount taRsAccountQry;
    private TaRsAccount taRsAccountAdd;
    private TaRsAccount taRsAccountUpd;
    private String rtnFlag;

    @PostConstruct
    public void init() {
        this.taRsAccountAdd = new TaRsAccount();
        this.taRsAccountUpd = new TaRsAccount();
        this.taRsAccountQry= new TaRsAccount();
        querySelectedRecords();
    }

    private void querySelectedRecords() {
        taRsAccountList = taAccountService.qryAllRecords();
    }

    private void querySelectedRecords(TaRsAccount act) {
        taRsAccountList = taAccountService.selectedRecordsByCondition(act.getAccType(), act.getAccId(), act.getAccName());
    }

    public void onBtnQueryClick() {
        querySelectedRecords(taRsAccountQry);
    }
    public String onBtnSaveClick() {
        try {
            taAccountService.updateRecord(taRsAccountUpd);
        } catch (Exception ex) {
            ex.printStackTrace();
            rtnFlag = "<script language='javascript'>rtnScript('false');</script>";
            return null;
        }
        rtnFlag = "<script language='javascript'>rtnScript('true');</script>";
        return null;
    }    public String insertRecord() {
        try {
            if (!confirmAccountNo.equalsIgnoreCase(taRsAccountAdd.getAccId())) {
                MessageUtil.addError("两次输入的监管账户号不一致！");
                return null;
            }
            // 初始帐户余额均为可用
            taAccountService.insertRecord(taRsAccountAdd);
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("新增数据完成。");
        querySelectedRecords();
        this.taRsAccountAdd = new TaRsAccount();
        confirmAccountNo = "";
        return null;
    }

    public void selectRecordAction(
            String strSubmitTypePara,
            TaRsAccount taRsAccountPara) {
        try {
            // 查询
            if (strSubmitTypePara.equals("Sel")) {
                taRsAccountQry = (TaRsAccount) BeanUtils.cloneBean(taRsAccountPara);
            } else if (strSubmitTypePara.equals("Add")) {
                taRsAccountAdd = new TaRsAccount();
            } else if (strSubmitTypePara.equals("Upd")) {
                taRsAccountUpd = (TaRsAccount) BeanUtils.cloneBean(taRsAccountPara);
            } else if (strSubmitTypePara.equals("Del")) {
                taRsAccountQry = (TaRsAccount) BeanUtils.cloneBean(taRsAccountPara);
            } else {
                taRsAccountQry = (TaRsAccount) BeanUtils.cloneBean(taRsAccountPara);
            }
        } catch (Exception e) {
            MessageUtil.addError(e.getMessage());
        }
    }

    public String reset() {
        this.taRsAccountAdd = new TaRsAccount();
        if (!taRsAccountList.isEmpty()) {
            taRsAccountList.clear();
        }
        return null;
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

    public TaRsAccount getTaRsAccountQry() {
        return taRsAccountQry;
    }

    public void setTaRsAccountQry(TaRsAccount taRsAccountQry) {
        this.taRsAccountQry = taRsAccountQry;
    }

    public TaRsAccount getTaRsAccountAdd() {
        return taRsAccountAdd;
    }

    public void setTaRsAccountAdd(TaRsAccount taRsAccountAdd) {
        this.taRsAccountAdd = taRsAccountAdd;
    }

    public List<TaRsAccount> getTaRsAccountList() {
        return taRsAccountList;
    }

    public void setTaRsAccountList(List<TaRsAccount> taRsAccountList) {
        this.taRsAccountList = taRsAccountList;
    }

    public TaRsAccount getTaRsAccountUpd() {
        return taRsAccountUpd;
    }

    public void setTaRsAccountUpd(TaRsAccount taRsAccountUpd) {
        this.taRsAccountUpd = taRsAccountUpd;
    }

    public String getRtnFlag() {
        return rtnFlag;
    }

    public void setRtnFlag(String rtnFlag) {
        this.rtnFlag = rtnFlag;
    }
}
