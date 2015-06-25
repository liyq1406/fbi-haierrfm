package rfm.qd.view.contract;

import rfm.qd.common.constant.ContractRecvStatus;
import rfm.qd.common.constant.ContractStatus;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.model.QdRsReceive;
import rfm.qd.service.ContractRecvService;
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
public class ContractRecvChkAction {
    @ManagedProperty(value = "#{contractRecvService}")
    private ContractRecvService contractRecvService;

    private List<QdRsReceive> pendChkList;
    private List<QdRsReceive> pendChkoverList;
    private List<QdRsReceive> pendChkUnpassList;
    private QdRsReceive[] selectedRecords;
    private ContractStatus contractStatus = ContractStatus.NORMAL;
    private ContractRecvStatus recvStatus = ContractRecvStatus.BACK;
    private WorkResult workResult = WorkResult.CREATE;

    @PostConstruct
    public void init() {
        this.pendChkList = contractRecvService.selectContractList(WorkResult.CREATE);
        this.pendChkoverList = contractRecvService.selectContractList(WorkResult.PASS);
        this.pendChkUnpassList = contractRecvService.selectContractList(WorkResult.NOTPASS);
    }

    public String onCheck() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsReceive record : selectedRecords) {

                if (contractRecvService.updateRsReceiveToWorkResult(record, WorkResult.PASS) != 1) {
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

    public String onBack() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("至少选择一笔数据记录！");
            return null;
        }
        try {
            for (QdRsReceive record : selectedRecords) {

                if (contractRecvService.updateRsReceiveToWorkResult(record, WorkResult.NOTPASS) != 1) {
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

    //===========================================

    public ContractRecvService getContractRecvService() {
        return contractRecvService;
    }

    public void setContractRecvService(ContractRecvService contractRecvService) {
        this.contractRecvService = contractRecvService;
    }

    public List<QdRsReceive> getPendChkList() {
        return pendChkList;
    }

    public void setPendChkList(List<QdRsReceive> pendChkList) {
        this.pendChkList = pendChkList;
    }

    public List<QdRsReceive> getPendChkoverList() {
        return pendChkoverList;
    }

    public void setPendChkoverList(List<QdRsReceive> pendChkoverList) {
        this.pendChkoverList = pendChkoverList;
    }

    public List<QdRsReceive> getPendChkUnpassList() {
        return pendChkUnpassList;
    }

    public void setPendChkUnpassList(List<QdRsReceive> pendChkUnpassList) {
        this.pendChkUnpassList = pendChkUnpassList;
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

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }
}
