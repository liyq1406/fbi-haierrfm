package rfm.qd.view.cbus;

import rfm.qd.common.constant.SendFlag;
import rfm.qd.repository.model.CbsAccTxn;
import rfm.qd.service.account.CbusFdcActtxnService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class CbusAccDetailAction {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CbusAccDetailAction.class);

    private String startDate;
    private String endDate;
    private List<CbsAccTxn> cbsAccTxnList;
    @ManagedProperty(value = "#{cbusFdcActtxnService}")
    private CbusFdcActtxnService cbusFdcActtxnService;
    private SendFlag sendFlag = SendFlag.UN_SEND;
    private String isSent;

    @PostConstruct
    public void init() {
    }

    // TODO ��ѯ���м���˻��Ľ�����ϸ
    public String onQuery() {
        try {
            endDate = startDate;
            if (cbusFdcActtxnService.isSentActtxns(endDate)) {
                MessageUtil.addWarn(endDate + "������������ѷ�����ɣ�");
                //return null;
            }else {
                MessageUtil.addWarn(endDate + "�����������δ���ͣ�");
            }

            boolean isQryed = cbusFdcActtxnService.isQryedActtxns(endDate);
            if (!isQryed) {
                cbusFdcActtxnService.qrySaveActtxnsCbusByDate(startDate, endDate);
            }
            MessageUtil.addInfo(endDate + "������ϸ�����ѴӺ���ϵͳ��ȡ��ɡ�");

            cbsAccTxnList = cbusFdcActtxnService.qryCbsAccTotalTxnsByDateAndFlag(endDate);
            if (cbsAccTxnList.isEmpty()) {
                MessageUtil.addWarn(endDate + "������ϸ����Ϊ�գ�");
            }
        } catch (Exception e) {
            logger.error("����ʧ�ܡ�", e);
            MessageUtil.addError("����ʧ�ܡ�" + e.getMessage());
        }
        return null;
    }

    public String onSend() {
        try {
            if (StringUtils.isEmpty(startDate)) {
                MessageUtil.addError("���ڲ���Ϊ�ա�");
                return null;
            }
            endDate = startDate;
           /* if (cbusFdcActtxnService.isSentActtxns(endDate)) {
                MessageUtil.addWarn(endDate + "������������ѷ�����ɣ�");
                return null;
            }*/
            cbusFdcActtxnService.updateCbsActtxnsUnSent(endDate);

            cbsAccTxnList = cbusFdcActtxnService.qryCbsAccTotalTxnsByDateAndFlag(endDate);
            if (cbsAccTxnList == null || cbsAccTxnList.isEmpty()) {
                MessageUtil.addWarn("û�д��������ݣ�");
                return null;
            } else {
                MessageUtil.addInfo(endDate + "������ϸ�����ѴӺ���ϵͳ��ȡ��ɡ�");
                cbusFdcActtxnService.sendAccTotalLoanTxns(endDate, cbsAccTxnList);
            }
            MessageUtil.addInfo(endDate + "����׻��ܷ��ͳɹ���");
        } catch (Exception e) {
            logger.error("����ʧ�ܡ�", e);
            MessageUtil.addError("����ʧ�ܡ�" + e.getMessage());
        }
        return null;
    }

    //=======================================


    public String getSent() {
        return isSent;
    }

    public void setSent(String sent) {
        isSent = sent;
    }

    public SendFlag getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(SendFlag sendFlag) {
        this.sendFlag = sendFlag;
    }

    public List<CbsAccTxn> getCbsAccTxnList() {
        return cbsAccTxnList;
    }

    public void setCbsAccTxnList(List<CbsAccTxn> cbsAccTxnList) {
        this.cbsAccTxnList = cbsAccTxnList;
    }

    public CbusFdcActtxnService getCbusFdcActtxnService() {
        return cbusFdcActtxnService;
    }

    public void setCbusFdcActtxnService(CbusFdcActtxnService cbusFdcActtxnService) {
        this.cbusFdcActtxnService = cbusFdcActtxnService;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
