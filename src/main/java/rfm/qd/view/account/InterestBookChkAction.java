package rfm.qd.view.account;

import rfm.qd.common.constant.TradeStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.service.account.AccountDetlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-9-7
 * Time: ����7:22
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class InterestBookChkAction {
    private static final Logger logger = LoggerFactory.getLogger(InterestBookChkAction.class);
    @ManagedProperty(value = "#{accountDetlService}")
    private AccountDetlService accountDetlService;

    private List<RsAccDetail> rsAccDetails;
    private RsAccDetail[] selectedRecords;
    private List<RsAccDetail> rsAccDetailsChk;
    private RsAccDetail[] selectedRecordsChk;


    @PostConstruct
    public void init() {
        List<String> statusfalg_init = new ArrayList<String>();
        statusfalg_init.add(0, TradeStatus.CANCEL.getCode());
        //�����˼�¼
        rsAccDetails = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(), statusfalg_init);
        List<String> statusfalg_checked = new ArrayList<String>();
        statusfalg_checked.add(0, TradeStatus.CHECKED.getCode());
        //�Ѹ��˼�¼
        rsAccDetailsChk = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(), statusfalg_checked);
    }

    public String onCheck() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("����ѡ��һ�����ݼ�¼��");
            return null;
        }
        try {
            for (RsAccDetail record : selectedRecords) {
                record.setStatusFlag(TradeStatus.CHECKED.getCode());
                if (accountDetlService.updateSelectedRecord(record) != 1) {
                    throw new RuntimeException("����ʧ�ܣ�");
                }
            }
            MessageUtil.addInfo("���˳ɹ���");
            init();
        } catch (Exception e) {
            MessageUtil.addError("����ʧ��." + e.getMessage());
        }
        return null;
    }

    /**
     * �����������˻�
     */
    public String onBack() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("����ѡ��һ�����ݼ�¼��");
            return null;
        }
        try {
            for (RsAccDetail record : selectedRecords) {
                record.setStatusFlag(TradeStatus.BACK.getCode());
                if (accountDetlService.updateSelectedRecord(record) != 1) {
                    throw new RuntimeException("�˻�ʧ�ܣ�");
                }
            }
            MessageUtil.addInfo("�˻سɹ���");
            init();
        } catch (Exception e) {
            MessageUtil.addError("����ʧ��." + e.getMessage());
        }
        return null;
    }

    /**
     * �Ѹ��˼�¼�˻�
     */

    public String onBackForChk() {
        if (selectedRecordsChk ==null || selectedRecordsChk.length == 0){
            MessageUtil.addWarn("����ѡ��һ�����ݼ�¼��");
            return null;
        }

        try {
            for (RsAccDetail record : selectedRecordsChk) {
                record.setStatusFlag(TradeStatus.BACK.getCode());
                if (accountDetlService.updateSelectedRecord(record) != 1) {
                    throw new RuntimeException("�˻�ʧ�ܣ�");
                }
            }
            MessageUtil.addInfo("�˻سɹ���");
            init();
        } catch (Exception e) {
            MessageUtil.addError("����ʧ��." + e.getMessage());
        }
        return null;
    }

    public List<RsAccDetail> getRsAccDetailsChk() {
        return rsAccDetailsChk;
    }

    public void setRsAccDetailsChk(List<RsAccDetail> rsAccDetailsChk) {
        this.rsAccDetailsChk = rsAccDetailsChk;
    }

    public AccountDetlService getAccountDetlService() {
        return accountDetlService;
    }

    public void setAccountDetlService(AccountDetlService accountDetlService) {
        this.accountDetlService = accountDetlService;
    }

    public List<RsAccDetail> getRsAccDetails() {
        return rsAccDetails;
    }

    public void setRsAccDetails(List<RsAccDetail> rsAccDetails) {
        this.rsAccDetails = rsAccDetails;
    }

    public RsAccDetail[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(RsAccDetail[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public RsAccDetail[] getSelectedRecordsChk() {
        return selectedRecordsChk;
    }

    public void setSelectedRecordsChk(RsAccDetail[] selectedRecordsChk) {
        this.selectedRecordsChk = selectedRecordsChk;
    }
}
