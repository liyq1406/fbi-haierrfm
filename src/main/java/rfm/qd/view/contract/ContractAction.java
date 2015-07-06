package rfm.qd.view.contract;

import platform.service.PtenudetailService;
import rfm.qd.common.constant.ContractStatus;
import rfm.qd.common.constant.HouseType;
import rfm.qd.common.constant.LoanType;
import rfm.qd.common.constant.PayupFlag;
import rfm.qd.repository.model.QdRsContract;
import rfm.qd.service.contract.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-8-25
 * Time: ÏÂÎç2:29
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ContractAction implements Serializable{
    private static final Logger logger = LoggerFactory.getLogger(ContractAction.class);

    @ManagedProperty(value = "#{contractService}")
    private ContractService contractService;

    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService ptenudetailService;

    private List<QdRsContract> detlList;
    private QdRsContract[] selectedRecords;
    private QdRsContract selectedRecord;

    private List<SelectItem> contractStatusOptions;
    private List<SelectItem> houseTypeOptions;
    private List<SelectItem> loanTypeOptions;
    private List<SelectItem> payupTypeOptions;

    private ContractStatus contractStatus = ContractStatus.NORMAL;
    private HouseType houseType = HouseType.NORMAL;
    private LoanType loanType = LoanType.SHANG_YE;
    private PayupFlag payupType = PayupFlag.PEND_PAYUP;

    @PostConstruct
    public void init() {
        this.contractStatusOptions = ptenudetailService.getEnuSelectItemList("CONTRACT_STATUS", false, false);
        this.houseTypeOptions = ptenudetailService.getEnuSelectItemList("HOUSE_TYPE", false, false);
        this.loanTypeOptions = ptenudetailService.getEnuSelectItemList("LOAN_TYPE", false, false);
        this.payupTypeOptions = ptenudetailService.getEnuSelectItemList("PAYUP_TYPE", false, false);
        initList();
    }

    private void initList(){
       this.detlList = contractService.selectContractList();
    }

    public  String onQuery(){
         return null;
    }
    public  String onPrint(){
         return null;
    }

        public String onShowDetail() {
        return "contractDetlForm";
    }

    public void showDetailListener(ActionEvent event) {
        String pkid = (String) event.getComponent().getAttributes().get("pkId");
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        sessionMap.put("pkId", pkid);
    }


    //================================================

    public ContractService getContractService() {
        return contractService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public List<QdRsContract> getDetlList() {
        return detlList;
    }

    public void setDetlList(List<QdRsContract> detlList) {
        this.detlList = detlList;
    }

    public QdRsContract[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(QdRsContract[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public QdRsContract getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(QdRsContract selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
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

    public List<SelectItem> getPayupTypeOptions() {
        return payupTypeOptions;
    }

    public void setPayupTypeOptions(List<SelectItem> payupTypeOptions) {
        this.payupTypeOptions = payupTypeOptions;
    }
}
