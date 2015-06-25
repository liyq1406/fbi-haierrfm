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
 * Time: ����2:28
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
    //�Ѹ������� ������
    private List<QdRsAccDetail> qdRsAccDetailsChk;
    private QdRsAccDetail[] selectedRecords;
    //����������
    private List<QdRsAccDetail> qdRsAccDetailsSend;
    private QdRsAccDetail[] selectedRecordsSend;
    //�ѷ�������
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
     * ����˻���Ϣ����
     */
    public String onBook() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("����ѡ��һ�����ݼ�¼��");
            return null;
        }
        try {
            for (QdRsAccDetail record : selectedRecords) {
                QdRsAccount qdRsAccount = new QdRsAccount();
                qdRsAccount.setAccountCode(record.getAccountCode());
                qdRsAccount.setCompanyId(record.getCompanyId());
                //����������ӵ��˻�����ֶ���
                qdRsAccount.setBalance(record.getTradeAmt());
                //���¼���˻����
                if (accountService.updateRecordBalance(qdRsAccount) == 1) {
                    //�����˻���ϸ����ֶΡ�״̬�ֶ�
                    record.setStatusFlag(TradeStatus.SUCCESS.getCode());
                    qdRsAccount = accountService.selectCanRecvAccountByNo(record.getAccountCode());
                    if (accountDetlService.updateSelectedRecordBook(record, qdRsAccount) != 1) {
                        throw new RuntimeException("����ʧ�ܣ�");
                    }
                } else {
                    throw new RuntimeException("����ʧ�ܣ�");
                }
            }
            MessageUtil.addInfo("���˳ɹ���");
            init();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.addError("����ʧ��." + e.getMessage());
        }
        return null;
    }

    public String onSend() {
        if(qdRsAccDetailsSend.isEmpty()) {
            MessageUtil.addWarn("û�д����ͼ�¼��");
            return null;
        }
        try {
            for(QdRsAccDetail record : qdRsAccDetailsSend) {
                if(clientBiService.sendInterestRecord(record) != 1) {
                   throw new RuntimeException("����ʧ�ܣ������¼�˺ţ�"+record.getAccountCode());
                }
            }
            MessageUtil.addInfo("������ɣ�");
            init();
        }catch (Exception e) {
            e.printStackTrace();
           MessageUtil.addError("����ʧ��." + e.getMessage());
           logger.error("����ʧ��", e.getMessage());
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

