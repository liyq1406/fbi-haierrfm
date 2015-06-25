package rfm.qd.view.company;

import rfm.qd.repository.model.QdRsFdccompany;
import rfm.qd.service.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-8-28
 * Time: ÏÂÎç8:09
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class CompanyEditAction implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(CompanyEditAction.class);
    @ManagedProperty(value = "#{companyService}")
    private CompanyService companyService;
    private QdRsFdccompany fdccompany;
    private String rtnFlag;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!context.isPostback()) {
            Map<String, String> paramsmap = context.getExternalContext().getRequestParameterMap();
            String paramDoType = paramsmap.get("doType");
            String paramCmpnyID = paramsmap.get("cmpnyPKID");
            fdccompany = companyService.selectedReocrdByPK(paramCmpnyID);
        }
    }

    public String onBtnSaveClick() throws IOException {
        try {
            companyService.updateRsFdccompany(fdccompany);
        } catch (Exception ex) {
            ex.printStackTrace();
            rtnFlag = "<script language='javascript'>rtnScript('false');</script>";
            return null;
        }
        rtnFlag = "<script language='javascript'>rtnScript('true');</script>";
        return null;
    }

    public String getRtnFlag() {
        return rtnFlag;
    }

    public void setRtnFlag(String rtnFlag) {
        this.rtnFlag = rtnFlag;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public QdRsFdccompany getFdccompany() {
        return fdccompany;
    }

    public void setFdccompany(QdRsFdccompany fdccompany) {
        this.fdccompany = fdccompany;
    }
}
