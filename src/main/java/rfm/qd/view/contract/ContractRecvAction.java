package rfm.qd.view.contract;

import rfm.qd.common.constant.*;
import rfm.qd.repository.model.RsContract;
import rfm.qd.repository.model.RsReceive;
import rfm.qd.service.ContractRecvService;
import rfm.qd.service.contract.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.ToolsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-8-25
 * Time: ÏÂÎç2:29
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ContractRecvAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ContractRecvAction.class);

    @ManagedProperty(value = "#{contractService}")
    private ContractService contractService;
    @ManagedProperty(value = "#{contractRecvService}")
    private ContractRecvService contractRecvService;

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;

    private List<RsContract> detlList;
    private RsContract[] selectedRecords;
    private RsContract selectedRecord;

    private List<RsReceive> detlRecvList;
    private List<RsReceive> pendChkList;
    private List<RsReceive> pendActList;
    private List<RsReceive> detlEditList;
    private RsReceive[] selectedRecvRecords;
    private RsReceive selectedRecvRecord;

    private List<SelectItem> contractStatusOptions;
    private List<SelectItem> recvStatusOptions;
    private List<SelectItem> workResultOptions;
    private List<SelectItem> houseTypeOptions;
    private List<SelectItem> loanTypeOptions;
    private List<SelectItem> payupFlagOptions;
    private List<SelectItem> receiveTypeOptions;
    private List<SelectItem> purposeOptions;

    private ContractStatus contractStatus = ContractStatus.NORMAL;
   // private ContractRecvStatus recvStatus = ContractRecvStatus.BACK;
    private WorkResult workResult = WorkResult.NOTPASS;
    private HouseType houseType = HouseType.NORMAL;
    private LoanType loanType = LoanType.SHANG_YE;
    private PayupFlag payupType = PayupFlag.PEND_PAYUP;
    private ReceiveType receiveType = ReceiveType.DEPOSIT;

    @PostConstruct
    public void init() {
        this.contractStatusOptions = toolsService.getEnuSelectItemList("CONTRACT_STATUS", true, false);
        this.houseTypeOptions = toolsService.getEnuSelectItemList("HOUSE_TYPE", true, false);
        this.loanTypeOptions = toolsService.getEnuSelectItemList("LOAN_TYPE", true, false);
        this.payupFlagOptions = toolsService.getEnuSelectItemList("PAYUP_FLAG", true, false);
        this.workResultOptions = toolsService.getEnuSelectItemList("WORK_RESULT", true, false);
        // TODO
        this.recvStatusOptions = toolsService.getEnuSelectItemList("WORK_RESULT", true, false);
        this.receiveTypeOptions = toolsService.getEnuSelectItemList("RECEIVE_TYPE", true, false);
        purposeOptions = toolsService.getEnuSelectItemList("PAY_PURPOSE_TYPE", false, false);

        initList();
    }

    private void initList() {
        this.detlList = contractService.selectContractList(ContractStatus.NORMAL);
        this.pendChkList = contractRecvService.selectContractList(WorkResult.CREATE);
        this.pendActList = contractRecvService.selectContractList(WorkResult.PASS);
        this.detlRecvList = contractRecvService.selectContractRecvList();
        this.detlEditList = contractRecvService.selectEditRecvList();
    }

    public String onQuery() {
        return null;
    }

    public String onPrint() {
        return null;
    }


    //================================================

    public ContractService getContractService() {
        return contractService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public List<RsContract> getDetlList() {
        return detlList;
    }

    public void setDetlList(List<RsContract> detlList) {
        this.detlList = detlList;
    }

    public RsContract[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(RsContract[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public RsContract getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(RsContract selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public ToolsService getToolsService() {
        return toolsService;
    }

    public void setToolsService(ToolsService toolsService) {
        this.toolsService = toolsService;
    }

    public List<SelectItem> getContractStatusOptions() {
        return contractStatusOptions;
    }

    public List<SelectItem> getPurposeOptions() {
        return purposeOptions;
    }

    public void setPurposeOptions(List<SelectItem> purposeOptions) {
        this.purposeOptions = purposeOptions;
    }

    public void setContractStatusOptions(List<SelectItem> contractStatusOptions) {
        this.contractStatusOptions = contractStatusOptions;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public List<SelectItem> getHouseTypeOptions() {
        return houseTypeOptions;
    }

    public void setHouseTypeOptions(List<SelectItem> houseTypeOptions) {
        this.houseTypeOptions = houseTypeOptions;
    }

    public HouseType getHouseType() {
        return houseType;
    }

    public void setHouseType(HouseType houseType) {
        this.houseType = houseType;
    }

    public LoanType getLoanType() {
        return loanType;
    }

    public void setLoanType(LoanType loanType) {
        this.loanType = loanType;
    }

    public PayupFlag getPayupType() {
        return payupType;
    }

    public void setPayupType(PayupFlag payupType) {
        this.payupType = payupType;
    }

    public List<SelectItem> getLoanTypeOptions() {
        return loanTypeOptions;
    }

    public void setLoanTypeOptions(List<SelectItem> loanTypeOptions) {
        this.loanTypeOptions = loanTypeOptions;
    }

    public List<SelectItem> getPayupFlagOptions() {
        return payupFlagOptions;
    }

    public void setPayupFlagOptions(List<SelectItem> payupFlagOptions) {
        this.payupFlagOptions = payupFlagOptions;
    }

    public ContractRecvService getContractRecvService() {
        return contractRecvService;
    }

    public void setContractRecvService(ContractRecvService contractRecvService) {
        this.contractRecvService = contractRecvService;
    }

    public List<RsReceive> getDetlRecvList() {
        return detlRecvList;
    }

    public void setDetlRecvList(List<RsReceive> detlRecvList) {
        this.detlRecvList = detlRecvList;
    }

    public RsReceive[] getSelectedRecvRecords() {
        return selectedRecvRecords;
    }

    public void setSelectedRecvRecords(RsReceive[] selectedRecvRecords) {
        this.selectedRecvRecords = selectedRecvRecords;
    }

    public RsReceive getSelectedRecvRecord() {
        return selectedRecvRecord;
    }

    public void setSelectedRecvRecord(RsReceive selectedRecvRecord) {
        this.selectedRecvRecord = selectedRecvRecord;
    }

    public List<SelectItem> getRecvStatusOptions() {
        return recvStatusOptions;
    }

    public void setRecvStatusOptions(List<SelectItem> recvStatusOptions) {
        this.recvStatusOptions = recvStatusOptions;
    }

    public List<SelectItem> getWorkResultOptions() {
        return workResultOptions;
    }

    public void setWorkResultOptions(List<SelectItem> workResultOptions) {
        this.workResultOptions = workResultOptions;
    }

  /*  public ContractRecvStatus getRecvStatus() {
        return recvStatus;
    }

    public void setRecvStatus(ContractRecvStatus recvStatus) {
        this.recvStatus = recvStatus;
    }*/

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }


    public List<SelectItem> getReceiveTypeOptions() {
        return receiveTypeOptions;
    }

    public void setReceiveTypeOptions(List<SelectItem> receiveTypeOptions) {
        this.receiveTypeOptions = receiveTypeOptions;
    }

    public ReceiveType getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(ReceiveType receiveType) {
        this.receiveType = receiveType;
    }

    public List<RsReceive> getPendChkList() {
        return pendChkList;
    }

    public void setPendChkList(List<RsReceive> pendChkList) {
        this.pendChkList = pendChkList;
    }

    public List<RsReceive> getPendActList() {
        return pendActList;
    }

    public void setPendActList(List<RsReceive> pendActList) {
        this.pendActList = pendActList;
    }

    public List<RsReceive> getDetlEditList() {
        return detlEditList;
    }

    public void setDetlEditList(List<RsReceive> detlEditList) {
        this.detlEditList = detlEditList;
    }
}
