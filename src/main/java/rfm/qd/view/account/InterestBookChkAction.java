package rfm.qd.view.account;

import rfm.qd.common.constant.TradeStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.QdRsAccDetail;
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
 * Time: 上午7:22
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class InterestBookChkAction {
    private static final Logger logger = LoggerFactory.getLogger(InterestBookChkAction.class);
    @ManagedProperty(value = "#{accountDetlService}")
    private AccountDetlService accountDetlService;

    private List<QdRsAccDetail> qdRsAccDetails;
    private QdRsAccDetail[] selectedRecords;
    private List<QdRsAccDetail> qdRsAccDetailsChk;
    private QdRsAccDetail[] selectedRecordsChk;


    @PostConstruct
    public void init() {
        List<String> statusfalg_init = new ArrayList<String>();
        statusfalg_init.add(0, TradeStatus.CANCEL.getCode());
        //待复核记录
        qdRsAccDetails = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(), statusfalg_init);
        List<String> statusfalg_checked = new ArrayList<String>();
        statusfalg_checked.add(0, TradeStatus.CHECKED.getCode());
        //已复核记录
        qdRsAccDetailsChk = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(), statusfalg_checked);
    }

    public String onCheck() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsAccDetail record : selectedRecords) {
                record.setStatusFlag(TradeStatus.CHECKED.getCode());
                if (accountDetlService.updateSelectedRecord(record) != 1) {
                    throw new RuntimeException("复核失败！");
                }
            }
            MessageUtil.addInfo("复核成功！");
            init();
        } catch (Exception e) {
            MessageUtil.addError("操作失败." + e.getMessage());
        }
        return null;
    }

    /**
     * 待复核数据退回
     */
    public String onBack() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsAccDetail record : selectedRecords) {
                record.setStatusFlag(TradeStatus.BACK.getCode());
                if (accountDetlService.updateSelectedRecord(record) != 1) {
                    throw new RuntimeException("退回失败！");
                }
            }
            MessageUtil.addInfo("退回成功！");
            init();
        } catch (Exception e) {
            MessageUtil.addError("操作失败." + e.getMessage());
        }
        return null;
    }

    /**
     * 已复核记录退回
     */

    public String onBackForChk() {
        if (selectedRecordsChk ==null || selectedRecordsChk.length == 0){
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }

        try {
            for (QdRsAccDetail record : selectedRecordsChk) {
                record.setStatusFlag(TradeStatus.BACK.getCode());
                if (accountDetlService.updateSelectedRecord(record) != 1) {
                    throw new RuntimeException("退回失败！");
                }
            }
            MessageUtil.addInfo("退回成功！");
            init();
        } catch (Exception e) {
            MessageUtil.addError("操作失败." + e.getMessage());
        }
        return null;
    }

    public List<QdRsAccDetail> getQdRsAccDetailsChk() {
        return qdRsAccDetailsChk;
    }

    public void setQdRsAccDetailsChk(List<QdRsAccDetail> qdRsAccDetailsChk) {
        this.qdRsAccDetailsChk = qdRsAccDetailsChk;
    }

    public AccountDetlService getAccountDetlService() {
        return accountDetlService;
    }

    public void setAccountDetlService(AccountDetlService accountDetlService) {
        this.accountDetlService = accountDetlService;
    }

    public List<QdRsAccDetail> getQdRsAccDetails() {
        return qdRsAccDetails;
    }

    public void setQdRsAccDetails(List<QdRsAccDetail> qdRsAccDetails) {
        this.qdRsAccDetails = qdRsAccDetails;
    }

    public QdRsAccDetail[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(QdRsAccDetail[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public QdRsAccDetail[] getSelectedRecordsChk() {
        return selectedRecordsChk;
    }

    public void setSelectedRecordsChk(QdRsAccDetail[] selectedRecordsChk) {
        this.selectedRecordsChk = selectedRecordsChk;
    }
}
