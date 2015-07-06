package rfm.qd.view.contract;

import rfm.qd.common.constant.ReceiveType;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.model.QdRsContract;
import rfm.qd.repository.model.QdRsReceive;
import rfm.qd.service.ContractRecvService;
import rfm.qd.service.contract.ContractService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.PtenudetailService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-8-25
 * Time: 下午9:21
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ContractRecvDetlAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ContractRecvDetlAction.class);
    @ManagedProperty(value = "#{contractService}")
    private ContractService contractService;
    @ManagedProperty(value = "#{contractRecvService}")
    private ContractRecvService contractRecvService;
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService toolsService;

    private QdRsReceive selectedRecord;
    private QdRsContract contract;

    private List<SelectItem> recvTypeOptions;
    private List<SelectItem> purposeOptions;

    private ReceiveType receiveType = ReceiveType.OTHER;
    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        String action = (String) context.getExternalContext().getRequestParameterMap().get("action");
        if ("query".equals(action)) {
            selectedRecord = contractRecvService.selectContractRecv(pkid);
            contract = contractService.selectContractByNo(selectedRecord.getBusinessNo());
        } else if("edit".equals(action)) {
            selectedRecord = contractRecvService.selectContractRecv(pkid);
            contract = contractService.selectContractByNo(selectedRecord.getBusinessNo());
            recvTypeOptions = toolsService.getEnuSelectItemList("RECEIVE_TYPE", false, false);
            purposeOptions = toolsService.getEnuSelectItemList("PAY_PURPOSE_TYPE", false, false);
        }
        else if (!StringUtils.isEmpty(pkid)) {
            contract = contractService.selectRecordContract(pkid);
            selectedRecord = new QdRsReceive();
            copyFieldsFromContract();
            recvTypeOptions = toolsService.getEnuSelectItemList("RECEIVE_TYPE", false, false);
            purposeOptions = toolsService.getEnuSelectItemList("PAY_PURPOSE_TYPE", false, false);

        }
    }

    private void copyFieldsFromContract() {
        selectedRecord.setBusinessNo(contract.getContractNo());
        selectedRecord.setCompanyName(contract.getAccountName());
        selectedRecord.setAccountCode(contract.getAccountCode());
        selectedRecord.setBuyerAccCode(contract.getBuyerAccCode());
        selectedRecord.setBuyerAccName(contract.getBuyerAccName());
        selectedRecord.setBuyerCertType(contract.getBuyerCertType());
        selectedRecord.setBuyerCertNo(contract.getBuyerCertNo());
        selectedRecord.setBuyerName(contract.getBuyerName());
        selectedRecord.setBuyerBankName(contract.getBuyerBankName());
        selectedRecord.setApplyDate(sdf10.format(new Date()));
        selectedRecord.setWorkResult(WorkResult.CREATE.getCode());

        selectedRecord.setTradeAccCode(selectedRecord.getBuyerAccCode());
        selectedRecord.setTradeAccName(selectedRecord.getBuyerAccName());
        selectedRecord.setTradeBankName(selectedRecord.getBuyerBankName());
        selectedRecord.setExecUserName(selectedRecord.getBuyerName());
        selectedRecord.setExecDate(sdf10.format(new Date()));
    }

    public String onSave() {
        try {
            selectedRecord.setApAmount(selectedRecord.getPlAmount());

            //contract.setReceiveAmt(contract.getReceiveAmt().add(selectedRecord.getApAmount()));

            if (contract.getReceiveAmt().add(selectedRecord.getApAmount()).compareTo(contract.getTotalAmt()) > 0) {
                MessageUtil.addError("申请缴款金额不得大于房屋总价！");
                return null;
            }
            if (contractRecvService.insertRecord(selectedRecord) == 1) {
                   // && contractService.updateRecord(contract) == 1) {
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
                MessageUtil.addInfo("缴款申请成功！");
            }
        } catch (Exception e) {
            logger.error("缴款保存失败", e);
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    public String onEdit() {
        try {
            selectedRecord.setApAmount(selectedRecord.getPlAmount());
            selectedRecord.setWorkResult(WorkResult.CREATE.getCode());
           // contract.setReceiveAmt(contract.getReceiveAmt().add(selectedRecord.getApAmount()));


            if (contract.getReceiveAmt().compareTo(contract.getTotalAmt()) > 0) {
                MessageUtil.addError("申请缴款金额不得大于房屋总价！");
                return null;
            }
            if (contractRecvService.updateRecord(selectedRecord) == 1) {
                 //   && contractService.updateRecord(contract) == 1) {
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
                MessageUtil.addInfo("缴款申请修改成功！");
            }
        } catch (Exception e) {
            logger.error("缴款保存失败", e);
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    public String onDelete() {
        try {
            selectedRecord.setDeletedFlag("1");
            if (contractRecvService.updateRecord(selectedRecord) == 1) {
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
                MessageUtil.addInfo("合同交款申请删除成功！");
            }
        } catch (Exception e) {
            logger.error("合同交款删除失败", e);
            MessageUtil.addError("操作失败。" + e.getMessage());
        }
        return null;
    }

    //======================================================

    public ContractService getContractService() {
        return contractService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public QdRsReceive getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(QdRsReceive selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public ContractRecvService getContractRecvService() {
        return contractRecvService;
    }

    public void setContractRecvService(ContractRecvService contractRecvService) {
        this.contractRecvService = contractRecvService;
    }

    public QdRsContract getContract() {
        return contract;
    }

    public void setContract(QdRsContract contract) {
        this.contract = contract;
    }

    public List<SelectItem> getRecvTypeOptions() {
        return recvTypeOptions;
    }

    public void setRecvTypeOptions(List<SelectItem> recvTypeOptions) {
        this.recvTypeOptions = recvTypeOptions;
    }

    public PtenudetailService getToolsService() {
        return toolsService;
    }

    public void setToolsService(PtenudetailService toolsService) {
        this.toolsService = toolsService;
    }

    public ReceiveType getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(ReceiveType receiveType) {
        this.receiveType = receiveType;
    }

    public List<SelectItem> getPurposeOptions() {
        return purposeOptions;
    }

    public void setPurposeOptions(List<SelectItem> purposeOptions) {
        this.purposeOptions = purposeOptions;
    }
}
