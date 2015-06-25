package rfm.qd.view.contract;

import rfm.qd.common.constant.ContractRecvStatus;
import rfm.qd.common.constant.ContractStatus;
import rfm.qd.common.constant.InOutFlag;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.model.QdRsReceive;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.ContractRecvService;
import rfm.qd.service.TradeService;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-6
 * Time: 下午1:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ContractRecvActAction {
    @ManagedProperty(value = "#{contractRecvService}")
    private ContractRecvService contractRecvService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;
    @ManagedProperty(value = "#{tradeService}")
    private TradeService tradeService;

    private List<QdRsReceive> pendChkoverList;
    private List<QdRsReceive> pendCommitActList;
    private List<QdRsReceive> pendSentActList;
    private QdRsReceive[] selectedRecords;
    private ContractStatus contractStatus = ContractStatus.NORMAL;
    private ContractRecvStatus recvStatus = ContractRecvStatus.BACK;
    private WorkResult workResult = WorkResult.CREATE;

    @PostConstruct
    public void init() {
        this.pendChkoverList = contractRecvService.selectContractList(WorkResult.PASS);
        this.pendCommitActList = contractRecvService.selectContractList(WorkResult.COMMIT);
        this.pendSentActList = contractRecvService.selectContractList(WorkResult.SENT);
    }

    public String onAct() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsReceive record : selectedRecords) {
                if (tradeService.handleReceiveTrade(record) == 3) {
                    if (contractRecvService.updateRsReceiveToWorkResult(record, WorkResult.COMMIT) != 1) {
                        throw new RuntimeException("入账失败！账号:" + record.getAccountCode());
                    }
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
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsReceive record : selectedRecords) {
                if (clientBiService.sendRsReceiveMsg(record, InOutFlag.IN.getCode()) != 1) {
                    throw new RuntimeException("发送失败！账号：" + record.getAccountCode());
                }
            }
            MessageUtil.addInfo("发送成功！");
            init();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.addError("操作失败." + e.getMessage());
        }
        return null;
    }

    //===========================================

    public ContractRecvService getContractRecvService() {
        return contractRecvService;
    }

    public void setContractRecvService(ContractRecvService contractRecvService) {
        this.contractRecvService = contractRecvService;
    }

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }

    public List<QdRsReceive> getPendChkoverList() {
        return pendChkoverList;
    }

    public void setPendChkoverList(List<QdRsReceive> pendChkoverList) {
        this.pendChkoverList = pendChkoverList;
    }

    public QdRsReceive[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(QdRsReceive[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public ContractRecvStatus getRecvStatus() {
        return recvStatus;
    }

    public void setRecvStatus(ContractRecvStatus recvStatus) {
        this.recvStatus = recvStatus;
    }

    public List<QdRsReceive> getPendCommitActList() {
        return pendCommitActList;
    }

    public void setPendCommitActList(List<QdRsReceive> pendCommitActList) {
        this.pendCommitActList = pendCommitActList;
    }

    public List<QdRsReceive> getPendSentActList() {
        return pendSentActList;
    }

    public void setPendSentActList(List<QdRsReceive> pendSentActList) {
        this.pendSentActList = pendSentActList;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }

    public TradeService getTradeService() {
        return tradeService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }
}
