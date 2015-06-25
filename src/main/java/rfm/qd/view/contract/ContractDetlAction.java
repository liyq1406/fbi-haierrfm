package rfm.qd.view.contract;

import rfm.qd.repository.model.QdRsContract;
import rfm.qd.service.contract.ContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-8-25
 * Time: ÏÂÎç9:21
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ContractDetlAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ContractDetlAction.class);
    @ManagedProperty(value = "#{contractService}")
    private ContractService contractService;

    private QdRsContract selectedRecord;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        contractDetlQry(pkid);
    }

    private void contractDetlQry(String pkid) {
        selectedRecord = contractService.selectRecordContract(pkid);
    }

    public ContractService getContractService() {
        return contractService;
    }

    public void setContractService(ContractService contractService) {
        this.contractService = contractService;
    }

    public QdRsContract getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(QdRsContract selectedRecord) {
        this.selectedRecord = selectedRecord;
    }
}
