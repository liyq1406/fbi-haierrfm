package rfm.qd.view.account;

import rfm.qd.common.constant.AccountStatus;
import rfm.qd.common.constant.LimitStatus;
import rfm.qd.gateway.cbus.domain.txn.QDJG01Res;
import rfm.qd.gateway.service.CbusTxnService;
import rfm.qd.repository.model.QdRsAccount;
import rfm.qd.service.account.AccountService;
import rfm.qd.service.company.CompanyService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class AccountAction {
    private static final Logger logger = LoggerFactory.getLogger(AccountAction.class);
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;
    @ManagedProperty(value = "#{companyService}")
    private CompanyService companyService;
    @ManagedProperty(value = "#{cbusTxnService}")
    private CbusTxnService cbusTxnService;
    private QdRsAccount account;
    private List<QdRsAccount> accountList;
    private String confirmAccountNo;
    private AccountStatus accountStatus = AccountStatus.INIT;
    private LimitStatus limitStatus = LimitStatus.NOT_LIMIT;
    private List<SelectItem> companyList;

    @PostConstruct
    public void init() {
        this.account = new QdRsAccount();
        querySelectedRecords();
        companyList = companyService.selectItemsCompany(null);
    }

    public String qrybal() {
        try {
            QDJG01Res res = cbusTxnService.qdjg01QryActbal(account.getAccountCode());
            if (!"00".equals(res.getHeader().getRtnCode())) {
                MessageUtil.addError("��ѯ���ʧ��." + res.rtnMsg);
                return null;
            } else {
                account.setBalance(new BigDecimal(res.actbal));
                account.setBalanceUsable(new BigDecimal(res.avabal));
            }

        } catch (Exception e) {
            logger.error("��ѯ���ʧ��.", e);
            MessageUtil.addError("��ѯʧ�ܡ�" + e.getMessage());
        }
        return null;
    }

    private void querySelectedRecords() {
        accountList = accountService.qryAllRecords();
    }

    private void querySelectedRecords(QdRsAccount act) {
        accountList = accountService.selectedRecordsByCondition(act.getPresellNo(), act.getCompanyId(), act.getAccountCode(),
                act.getAccountName());
    }

    public String onBtnQueryClick() {
        querySelectedRecords(account);
        return null;
    }

    // ��
    public String insertRecord() {
        try {
            if (StringUtils.isEmpty(account.getCompanyId())) {
                MessageUtil.addError("��ѡ�񷿵ز��̣�");
                return null;
            }

            if (!confirmAccountNo.equalsIgnoreCase(account.getAccountCode())) {
                MessageUtil.addError("��������ļ���˻��Ų�һ�£�");
                return null;
            }
            // ��ʼ�ʻ�����Ϊ����
            account.setBalanceUsable(account.getBalance());
            accountService.insertRecord(account);
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("����������ɡ�");
        querySelectedRecords();
        this.account = new QdRsAccount();
        confirmAccountNo = "";
        return null;
    }

    public String reset() {
        this.account = new QdRsAccount();
        if (!accountList.isEmpty()) {
            accountList.clear();
        }
        return null;
    }

    public CbusTxnService getCbusTxnService() {
        return cbusTxnService;
    }

    public void setCbusTxnService(CbusTxnService cbusTxnService) {
        this.cbusTxnService = cbusTxnService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public List<SelectItem> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<SelectItem> companyList) {
        this.companyList = companyList;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public QdRsAccount getAccount() {
        return account;
    }

    public void setAccount(QdRsAccount account) {
        this.account = account;
    }

    public List<QdRsAccount> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<QdRsAccount> accountList) {
        this.accountList = accountList;
    }

    public String getConfirmAccountNo() {
        return confirmAccountNo;
    }

    public void setConfirmAccountNo(String confirmAccountNo) {
        this.confirmAccountNo = confirmAccountNo;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public LimitStatus getLimitStatus() {
        return limitStatus;
    }

    public void setLimitStatus(LimitStatus limitStatus) {
        this.limitStatus = limitStatus;
    }
}
