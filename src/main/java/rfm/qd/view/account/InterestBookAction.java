package rfm.qd.view.account;

import rfm.qd.common.constant.SendFlag;
import rfm.qd.common.constant.TradeStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsAccount;
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
    private List<RsAccDetail> rsAccDetailsChk;
    private RsAccDetail[] selectedRecords;
    //����������
    private List<RsAccDetail> rsAccDetailsSend;
    private RsAccDetail[] selectedRecordsSend;
    //�ѷ�������
    private List<RsAccDetail> rsAccDetailsSended;
    private RsAccDetail[] selectedRecordsSended;

    @PostConstruct
    public void init() {
        List<String> statusfalgs = new ArrayList<String>();
        statusfalgs.add(0, TradeStatus.CHECKED.getCode());
        rsAccDetailsChk = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(),statusfalgs);
        rsAccDetailsSend = accountDetlService.selectedRecordsForSend(TradeType.INTEREST.getCode(), TradeStatus.SUCCESS.getCode()
                , SendFlag.UN_SEND.getCode());
        rsAccDetailsSended = accountDetlService.selectedRecordsForSend(TradeType.INTEREST.getCode(), TradeStatus.SUCCESS.getCode()
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
            for (RsAccDetail record : selectedRecords) {
                RsAccount rsAccount = new RsAccount();
                rsAccount.setAccountCode(record.getAccountCode());
                rsAccount.setCompanyId(record.getCompanyId());
                //����������ӵ��˻�����ֶ���
                rsAccount.setBalance(record.getTradeAmt());
                //���¼���˻����
                if (accountService.updateRecordBalance(rsAccount) == 1) {
                    //�����˻���ϸ����ֶΡ�״̬�ֶ�
                    record.setStatusFlag(TradeStatus.SUCCESS.getCode());
                    rsAccount = accountService.selectCanRecvAccountByNo(record.getAccountCode());
                    if (accountDetlService.updateSelectedRecordBook(record,rsAccount) != 1) {
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
        if(rsAccDetailsSend.isEmpty()) {
            MessageUtil.addWarn("û�д����ͼ�¼��");
            return null;
        }
        try {
            for(RsAccDetail record : rsAccDetailsSend) {
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

    public List<RsAccDetail> getRsAccDetailsChk() {
        return rsAccDetailsChk;
    }

    public void setRsAccDetailsChk(List<RsAccDetail> rsAccDetailsChk) {
        this.rsAccDetailsChk = rsAccDetailsChk;
    }

    public RsAccDetail[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(RsAccDetail[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<RsAccDetail> getRsAccDetailsSend() {
        return rsAccDetailsSend;
    }

    public void setRsAccDetailsSend(List<RsAccDetail> rsAccDetailsSend) {
        this.rsAccDetailsSend = rsAccDetailsSend;
    }

    public RsAccDetail[] getSelectedRecordsSend() {
        return selectedRecordsSend;
    }

    public void setSelectedRecordsSend(RsAccDetail[] selectedRecordsSend) {
        this.selectedRecordsSend = selectedRecordsSend;
    }

    public List<RsAccDetail> getRsAccDetailsSended() {
        return rsAccDetailsSended;
    }

    public void setRsAccDetailsSended(List<RsAccDetail> rsAccDetailsSended) {
        this.rsAccDetailsSended = rsAccDetailsSended;
    }

    public RsAccDetail[] getSelectedRecordsSended() {
        return selectedRecordsSended;
    }

    public void setSelectedRecordsSended(RsAccDetail[] selectedRecordsSended) {
        this.selectedRecordsSended = selectedRecordsSended;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }
}

