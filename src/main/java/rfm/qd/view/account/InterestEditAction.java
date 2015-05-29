package rfm.qd.view.account;

import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsAccount;
import rfm.qd.service.account.AccountDetlService;
import rfm.qd.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.ToolsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-9-8
 * Time: 下午4:17
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class InterestEditAction {
    private static final Logger logger = LoggerFactory.getLogger(InterestEditAction.class);
    @ManagedProperty(value = "#{accountDetlService}")
    private AccountDetlService accountDetlService;
    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;

    private RsAccDetail rsAccDetail;
    private String rtnFlag;
    private List<RsAccDetail> rsAccDetailsInit;
    private String operation;
    private String accountName;

    @PostConstruct
    public void init() {
        rsAccDetail = new RsAccDetail();
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramsmap = context.getExternalContext().getRequestParameterMap();
        String paramDoType = paramsmap.get("doType");

        if (!paramDoType.equals("add")) {
            String paramPkid = paramsmap.get("pkid");
            rsAccDetail = accountDetlService.selectedByPK(paramPkid);
            accountName = rsAccDetail.getAccountName();
        } else {
            String acctPkid = paramsmap.get("acctPkid");
            RsAccount rsAccount = accountService.selectedRecordByPkid(acctPkid);
            accountName = rsAccount.getAccountName();
        }
        operation = paramDoType;
    }

    //利息录入
    public String onBtnSaveClick() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String accountno = (String) context.getExternalContext().getRequestParameterMap().get("acctno");
            String companyid = (String) context.getExternalContext().getRequestParameterMap().get("companyid");
            rsAccDetail.setAccountCode(accountno);
            rsAccDetail.setAccountName(accountName);
            rsAccDetail.setCompanyId(companyid);
            rsAccDetail.setStatusFlag("0");
            rsAccDetail.setInoutFlag("1");
            rsAccDetail.setTradeType(TradeType.INTEREST.getCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            rsAccDetail.setTradeDate(sdf.format(new Date()));
//            rsAccDetail.setLocalSerial();
            accountDetlService.insertSelectedRecord(rsAccDetail);
        } catch (Exception ex) {
            ex.printStackTrace();
            rtnFlag = "<script language='javascript'>rtnScript('false');</script>";
            return null;
        }
        rtnFlag = "<script language='javascript'>rtnScript('true');</script>";
        return null;
    }

    //利息录入修改
    public String onBtnSaveClick_Edit() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
            rsAccDetail.setPkId(pkid);
            rsAccDetail.setStatusFlag("0");
            accountDetlService.updateSelectedRecord(rsAccDetail);
        } catch (Exception ex) {
            ex.printStackTrace();
            rtnFlag = "<script language='javascript'>rtnScript('false');</script>";
            return null;
        }
        rtnFlag = "<script language='javascript'>rtnScript('true');</script>";
        return null;
    }

    public AccountDetlService getAccountDetlService() {
        return accountDetlService;
    }

    public void setAccountDetlService(AccountDetlService accountDetlService) {
        this.accountDetlService = accountDetlService;
    }

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public RsAccDetail getRsAccDetail() {
        return rsAccDetail;
    }

    public void setRsAccDetail(RsAccDetail rsAccDetail) {
        this.rsAccDetail = rsAccDetail;
    }

    public String getRtnFlag() {
        return rtnFlag;
    }

    public void setRtnFlag(String rtnFlag) {
        this.rtnFlag = rtnFlag;
    }

    public List<RsAccDetail> getRsAccDetailsInit() {
        return rsAccDetailsInit;
    }

    public void setRsAccDetailsInit(List<RsAccDetail> rsAccDetailsInit) {
        this.rsAccDetailsInit = rsAccDetailsInit;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
