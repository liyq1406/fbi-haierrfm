package rfm.qd.view.account;

import rfm.qd.common.constant.SendFlag;
import rfm.qd.common.constant.TradeStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsAccount;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.account.AccountDetlService;
import rfm.qd.service.account.AccountService;
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
 * Time: 下午2:28
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class InterestBookAction {
    private static final Logger logger = LoggerFactory.getLogger(InterestBookAction.class);
    @ManagedProperty(value = "#{accountDetlService}")
    private AccountDetlService accountDetlService;
    @ManagedProperty(value = "#{accountService}")
    private AccountService accountService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;
    //已复核数据 待入账
    private List<QdRsAccDetail> qdRsAccDetailsChk;
    private QdRsAccDetail[] selectedRecords;
    //待发送数据
    private List<QdRsAccDetail> qdRsAccDetailsSend;
    private QdRsAccDetail[] selectedRecordsSend;
    //已发送数据
    private List<QdRsAccDetail> qdRsAccDetailsSended;
    private QdRsAccDetail[] selectedRecordsSended;

    @PostConstruct
    public void init() {
        List<String> statusfalgs = new ArrayList<String>();
        statusfalgs.add(0, TradeStatus.CHECKED.getCode());
        qdRsAccDetailsChk = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(),statusfalgs);
        qdRsAccDetailsSend = accountDetlService.selectedRecordsForSend(TradeType.INTEREST.getCode(), TradeStatus.SUCCESS.getCode()
                , SendFlag.UN_SEND.getCode());
        qdRsAccDetailsSended = accountDetlService.selectedRecordsForSend(TradeType.INTEREST.getCode(), TradeStatus.SUCCESS.getCode()
                , SendFlag.SENT.getCode());
    }

    /**
     * 监管账户利息入账
     */
    public String onBook() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsAccDetail record : selectedRecords) {
                QdRsAccount qdRsAccount = new QdRsAccount();
                qdRsAccount.setAccountCode(record.getAccountCode());
                qdRsAccount.setCompanyId(record.getCompanyId());
                //将发生额添加到账户余额字段中
                qdRsAccount.setBalance(record.getTradeAmt());
                //更新监管账户余额
                if (accountService.updateRecordBalance(qdRsAccount) == 1) {
                    //更新账户明细余额字段、状态字段
                    record.setStatusFlag(TradeStatus.SUCCESS.getCode());
                    qdRsAccount = accountService.selectCanRecvAccountByNo(record.getAccountCode());
                    if (accountDetlService.updateSelectedRecordBook(record, qdRsAccount) != 1) {
                        throw new RuntimeException("入账失败！");
                    }
                } else {
                    throw new RuntimeException("入账失败！");
                }
            }
            MessageUtil.addInfo("入账成功！");
            init();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.addError("操作失败." + e.getMessage());
        }
        return null;
    }

    public String onSend() {
        if(qdRsAccDetailsSend.isEmpty()) {
            MessageUtil.addWarn("没有待发送记录！");
            return null;
        }
        try {
            for(QdRsAccDetail record : qdRsAccDetailsSend) {
                if(clientBiService.sendInterestRecord(record) != 1) {
                   throw new RuntimeException("发送失败！出错记录账号："+record.getAccountCode());
                }
            }
            MessageUtil.addInfo("发送完成！");
            init();
        }catch (Exception e) {
            e.printStackTrace();
           MessageUtil.addError("操作失败." + e.getMessage());
           logger.error("操作失败", e.getMessage());
        }
        return null;
    }

    public AccountDetlService getAccountDetlService() {
        return accountDetlService;
    }

    public void setAccountDetlService(AccountDetlService accountDetlService) {
        this.accountDetlService = accountDetlService;
    }

    public List<QdRsAccDetail> getQdRsAccDetailsChk() {
        return qdRsAccDetailsChk;
    }

    public void setQdRsAccDetailsChk(List<QdRsAccDetail> qdRsAccDetailsChk) {
        this.qdRsAccDetailsChk = qdRsAccDetailsChk;
    }

    public QdRsAccDetail[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(QdRsAccDetail[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<QdRsAccDetail> getQdRsAccDetailsSend() {
        return qdRsAccDetailsSend;
    }

    public void setQdRsAccDetailsSend(List<QdRsAccDetail> qdRsAccDetailsSend) {
        this.qdRsAccDetailsSend = qdRsAccDetailsSend;
    }

    public QdRsAccDetail[] getSelectedRecordsSend() {
        return selectedRecordsSend;
    }

    public void setSelectedRecordsSend(QdRsAccDetail[] selectedRecordsSend) {
        this.selectedRecordsSend = selectedRecordsSend;
    }

    public List<QdRsAccDetail> getQdRsAccDetailsSended() {
        return qdRsAccDetailsSended;
    }

    public void setQdRsAccDetailsSended(List<QdRsAccDetail> qdRsAccDetailsSended) {
        this.qdRsAccDetailsSended = qdRsAccDetailsSended;
    }

    public QdRsAccDetail[] getSelectedRecordsSended() {
        return selectedRecordsSended;
    }

    public void setSelectedRecordsSended(QdRsAccDetail[] selectedRecordsSended) {
        this.selectedRecordsSended = selectedRecordsSended;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }
}

