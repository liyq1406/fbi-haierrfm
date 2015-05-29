package rfm.qd.view.contract;

import rfm.qd.common.constant.*;
import rfm.qd.repository.model.RsContract;
import rfm.qd.repository.model.RsRefund;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.ContractRecvService;
import rfm.qd.service.RefundService;
import rfm.qd.service.TradeService;
import rfm.qd.service.contract.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.SystemService;
import platform.service.ToolsService;
import pub.platform.security.OperatorManager;

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
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class RefundAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(RefundAction.class);

    @ManagedProperty(value = "#{contractService}")
    private ContractService contractService;
    @ManagedProperty(value = "#{contractRecvService}")
    private ContractRecvService contractRecvService;
    @ManagedProperty(value = "#{refundService}")
    private RefundService refundService;
    @ManagedProperty(value = "#{tradeService}")
    private TradeService tradeService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;

    @ManagedProperty(value = "#{toolsService}")
    private ToolsService toolsService;

    private List<RsContract> detlList;
    private RsContract[] selectedRecords;
    private RsContract selectedRecord;

    private List<RsRefund> detlRefundList;
    private List<RsRefund> pendChkList;
    private List<RsRefund> pendNotpassList;
    private List<RsRefund> pendActList;
    private List<RsRefund> pendToSendList;
    private List<RsRefund> pendSentList;
    private List<RsRefund> detlEditList;

    private RsRefund[] selectedRefundRecords;
    private RsRefund selectedRefundRecord;

    private List<SelectItem> contractStatusOptions;
    private List<SelectItem> refundStatusOptions;
    private List<SelectItem> workResultOptions;
    private List<SelectItem> houseTypeOptions;
    private List<SelectItem> loanTypeOptions;
    private List<SelectItem> payupFlagOptions;
    private List<SelectItem> receiveTypeOptions;

    private ContractStatus contractStatus = ContractStatus.NORMAL;
    private RefundStatus refundStatus = RefundStatus.BACK;
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
        this.refundStatusOptions = toolsService.getEnuSelectItemList("REFUND_STATUS", true, false);
        this.receiveTypeOptions = toolsService.getEnuSelectItemList("RECEIVE_TYPE", true, false);
        initList();
    }

    private void initList() {
        this.detlList = contractService.selectContractList(ContractStatus.TRANS);
        this.pendChkList = refundService.selectRefundList(WorkResult.CREATE);
        this.pendNotpassList = refundService.selectRefundList(WorkResult.NOTPASS);
        this.pendActList = refundService.selectRefundList(WorkResult.PASS);
        this.pendToSendList = refundService.selectRefundList(WorkResult.COMMIT);
        this.pendSentList = refundService.selectRefundList(WorkResult.SENT);
        this.detlRefundList = refundService.selectRefundList();
        this.detlEditList = refundService.selectEditList();
    }

    public String onCheck() {
        if (selectedRefundRecords == null || selectedRefundRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔数据记录！");
            return null;
        }
        try {
            OperatorManager om = SystemService.getOperatorManager();
            String date10 = SystemService.getSdfdate10();
            for (RsRefund record : selectedRefundRecords) {
                record.setWorkResult(WorkResult.PASS.getCode());
                record.setApAmount(record.getPlAmount());
                record.setAuditUserId(om.getOperatorId());
                record.setAuditUserName(om.getOperatorName());
                record.setAuditDate(date10);
                if (refundService.updateRecord(record) != 1) {
                    throw new RuntimeException("复核更新异常！");
                }
            }
            MessageUtil.addInfo("复核完成！");
            initList();
        } catch (Exception e) {
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    public String onBack() {
         if (selectedRefundRecords == null || selectedRefundRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔数据记录！");
            return null;
        }
        try {
            for (RsRefund record : selectedRefundRecords) {
                record.setWorkResult(WorkResult.NOTPASS.getCode());
                if (refundService.updateRecord(record) != 1) {
                    throw new RuntimeException("退回更新异常！");
                }
            }
            MessageUtil.addInfo("退回完成！");
            initList();
        } catch (Exception e) {
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    public String onExecAccount() {
         if (selectedRefundRecords == null || selectedRefundRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔数据记录！");
            return null;
        }
        try {
            OperatorManager om = SystemService.getOperatorManager();
            String date10 = SystemService.getSdfdate10();
            for (RsRefund record : selectedRefundRecords) {
                record.setExecDate(date10);
                record.setExecUserId(om.getOperatorId());
                record.setExecUserName(om.getOperatorName());
                if (tradeService.handleRefundTrade(record) != 3) {
                    throw new RuntimeException("入账过程发生异常！");
                }
            }
            MessageUtil.addInfo("入账完成！");
            initList();
        } catch (Exception e) {
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    public String onSend() {
        if (selectedRefundRecords == null || selectedRefundRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔数据记录！");
            return null;
        }
        try {
            for (RsRefund record : selectedRefundRecords) {
                if (clientBiService.sendRsRefundMsg(record, InOutFlag.OUT.getCode()) != 1) {
                    throw new RuntimeException("发送过程发生异常！");
                }
            }
            MessageUtil.addInfo("发送完成！");
            initList();
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    //================================================

    public ContractService getContractService() {
        return contractService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }

    public List<RsContract> getDetlList() {
        return detlList;
    }

    public List<RsRefund> getDetlEditList() {
        return detlEditList;
    }

    public List<RsRefund> getPendNotpassList() {
        return pendNotpassList;
    }

    public void setPendNotpassList(List<RsRefund> pendNotpassList) {
        this.pendNotpassList = pendNotpassList;
    }

    public void setDetlEditList(List<RsRefund> detlEditList) {
        this.detlEditList = detlEditList;
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


    public List<RsRefund> getDetlRefundList() {
        return detlRefundList;
    }

    public void setDetlRefundList(List<RsRefund> detlRefundList) {
        this.detlRefundList = detlRefundList;
    }

    public RsRefund[] getSelectedRefundRecords() {
        return selectedRefundRecords;
    }

    public void setSelectedRefundRecords(RsRefund[] selectedRefundRecords) {
        this.selectedRefundRecords = selectedRefundRecords;
    }

    public RsRefund getSelectedRefundRecord() {
        return selectedRefundRecord;
    }

    public void setSelectedRefundRecord(RsRefund selectedRefundRecord) {
        this.selectedRefundRecord = selectedRefundRecord;
    }

    public List<SelectItem> getWorkResultOptions() {
        return workResultOptions;
    }

    public void setWorkResultOptions(List<SelectItem> workResultOptions) {
        this.workResultOptions = workResultOptions;
    }

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

    public List<RsRefund> getPendChkList() {
        return pendChkList;
    }

    public void setPendChkList(List<RsRefund> pendChkList) {
        this.pendChkList = pendChkList;
    }

    public List<RsRefund> getPendActList() {
        return pendActList;
    }

    public void setPendActList(List<RsRefund> pendActList) {
        this.pendActList = pendActList;
    }

    public RefundService getRefundService() {
        return refundService;
    }

    public void setRefundService(RefundService refundService) {
        this.refundService = refundService;
    }

    public ContractRecvService getContractRecvService() {
        return contractRecvService;
    }

    public void setContractRecvService(ContractRecvService contractRecvService) {
        this.contractRecvService = contractRecvService;
    }

    public List<SelectItem> getRefundStatusOptions() {
        return refundStatusOptions;
    }

    public void setRefundStatusOptions(List<SelectItem> refundStatusOptions) {
        this.refundStatusOptions = refundStatusOptions;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public TradeService getTradeService() {
        return tradeService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public List<RsRefund> getPendSentList() {
        return pendSentList;
    }

    public void setPendSentList(List<RsRefund> pendSentList) {
        this.pendSentList = pendSentList;
    }

    public List<RsRefund> getPendToSendList() {
        return pendToSendList;
    }

    public void setPendToSendList(List<RsRefund> pendToSendList) {
        this.pendToSendList = pendToSendList;
    }
}
