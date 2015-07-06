package rfm.qd.view.account;

import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsAccount;
import rfm.qd.service.account.AccountDetlService;
import rfm.qd.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PtenudetailService;

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
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService toolsService;
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;

    private QdRsAccDetail qdRsAccDetail;
    private String rtnFlag;
    private List<QdRsAccDetail> qdRsAccDetailsInit;
    private String operation;
    private String accountName;

    @PostConstruct
    public void init() {
        qdRsAccDetail = new QdRsAccDetail();
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramsmap = context.getExternalContext().getRequestParameterMap();
        String paramDoType = paramsmap.get("doType");

        if (!paramDoType.equals("add")) {
            String paramPkid = paramsmap.get("pkid");
            qdRsAccDetail = accountDetlService.selectedByPK(paramPkid);
            accountName = qdRsAccDetail.getAccountName();
        } else {
            String acctPkid = paramsmap.get("acctPkid");
            QdRsAccount qdRsAccount = accountService.selectedRecordByPkid(acctPkid);
            accountName = qdRsAccount.getAccountName();
        }
        operation = paramDoType;
    }

    //利息录入
    public String onBtnSaveClick() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String accountno = (String) context.getExternalContext().getRequestParameterMap().get("acctno");
            String companyid = (String) context.getExternalContext().getRequestParameterMap().get("companyid");
            qdRsAccDetail.setAccountCode(accountno);
            qdRsAccDetail.setAccountName(accountName);
            qdRsAccDetail.setCompanyId(companyid);
            qdRsAccDetail.setStatusFlag("0");
            qdRsAccDetail.setInoutFlag("1");
            qdRsAccDetail.setTradeType(TradeType.INTEREST.getCode());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            qdRsAccDetail.setTradeDate(sdf.format(new Date()));
//            rsAccDetail.setLocalSerial();
            accountDetlService.insertSelectedRecord(qdRsAccDetail);
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
            qdRsAccDetail.setPkId(pkid);
            qdRsAccDetail.setStatusFlag("0");
            accountDetlService.updateSelectedRecord(qdRsAccDetail);
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

    public PtenudetailService getToolsService() {
        return toolsService;
    }

    public void setToolsService(PtenudetailService toolsService) {
        this.toolsService = toolsService;
    }

    public QdRsAccDetail getQdRsAccDetail() {
        return qdRsAccDetail;
    }

    public void setQdRsAccDetail(QdRsAccDetail qdRsAccDetail) {
        this.qdRsAccDetail = qdRsAccDetail;
    }

    public String getRtnFlag() {
        return rtnFlag;
    }

    public void setRtnFlag(String rtnFlag) {
        this.rtnFlag = rtnFlag;
    }

    public List<QdRsAccDetail> getQdRsAccDetailsInit() {
        return qdRsAccDetailsInit;
    }

    public void setQdRsAccDetailsInit(List<QdRsAccDetail> qdRsAccDetailsInit) {
        this.qdRsAccDetailsInit = qdRsAccDetailsInit;
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
