package rfm.qd.view.company;

import rfm.qd.repository.model.RsFdccompany;
import rfm.qd.service.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-8-27
 * Time: ����10:58
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class CompanyAction {
    private Logger logger = LoggerFactory.getLogger(CompanyAction.class);
    @ManagedProperty(value = "#{companyService}")
    private CompanyService companyService;
    private RsFdccompany fdccompany;
    private String companyName;
    private List<RsFdccompany> fdccompanyList;

    @PostConstruct
    public void init() {
        fdccompany = new RsFdccompany();
        qrySelectedRecords("");
    }

    // ��
    public String insertNewFdccompany() {
        try {
            companyService.insertRsFdccompany(fdccompany);
            fdccompanyList.add(fdccompany);
        } catch (Exception e) {
            logger.error("��������ʧ�ܣ�", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("����������ɡ�");
        fdccompany = new RsFdccompany();
        return null;
    }

    // ��
    public String updateNewFdccompany() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            String pkid = context.getExternalContext().getRequestParameterMap().get("pkid").toString();
            String modno = context.getExternalContext().getRequestParameterMap().get("modno").toString();
            RsFdccompany up = new RsFdccompany();
            up.setPkId(pkid);
            up.setModificationNum(Integer.parseInt(modno));
            up.setDeleteFlag("1");
            companyService.updateRsFdccompany(up);
        } catch (Exception e) {
            logger.error("�����쳣", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("�޸ĳɹ���");
        qrySelectedRecords(companyName);
        return null;
    }

    //  ��
    public String qryFdccompanys() {
        try {
            qrySelectedRecords(companyName);
        } catch (Exception e) {
            logger.error("��ѯ�쳣", e);
            MessageUtil.addError(e.getMessage());
        }
        return null;
    }

    private void qrySelectedRecords(String comName) {
        fdccompanyList = companyService.qryRsFdccompanyByName(comName);
    }

    public RsFdccompany getFdccompany() {
        return fdccompany;
    }

    public void setFdccompany(RsFdccompany fdccompany) {
        this.fdccompany = fdccompany;
    }

    public CompanyService getCompanyService() {
        return companyService;
    }

    public void setCompanyService(CompanyService companyService) {
        this.companyService = companyService;
    }

    public List<RsFdccompany> getFdccompanyList() {
        return fdccompanyList;
    }

    public void setFdccompanyList(List<RsFdccompany> fdccompanyList) {
        this.fdccompanyList = fdccompanyList;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
