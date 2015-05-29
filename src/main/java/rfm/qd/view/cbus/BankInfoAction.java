package rfm.qd.view.cbus;

import rfm.qd.service.BankInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlSelectOneListbox;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-30
 * Time: ����3:41
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class BankInfoAction {
    private Logger logger = LoggerFactory.getLogger(BankInfoAction.class);

    @ManagedProperty(value = "#{bankInfoService}")
    private BankInfoService bankInfoService;

    private List<SelectItem> bankList;
    private String code;
    private String name;

    @PostConstruct
    public void init() {
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        HtmlSelectOneListbox selListBox = (HtmlSelectOneListbox) viewRoot.findComponent("selFrom:selListBox");
        selListBox.setRendered(false);
    }

    public String onQuery() {
        try {

            if(StringUtils.isEmpty(code) && StringUtils.isEmpty(name)) {
                MessageUtil.addWarn("����������һ���ѯ������");
                return null;
            }
            bankList = bankInfoService.qryBankInfoListByNoAndName(code, name);
            if (!bankList.isEmpty()) {
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                HtmlSelectOneListbox selListBox = (HtmlSelectOneListbox) viewRoot.findComponent("selFrom:selListBox");
                selListBox.setRendered(true);
            } else {
                MessageUtil.addWarn("û�в�ѯ����������Ϣ��");
            }
        } catch (Exception e) {
            logger.error("��ѯ���д���ϵͳ����Ӧ��", e.getMessage());
            MessageUtil.addWarn("��ѯ���д���ϵͳ����Ӧ��" + e.getMessage());
        }
        return null;
    }

    // ================================================

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankInfoService getBankInfoService() {
        return bankInfoService;
    }

    public void setBankInfoService(BankInfoService bankInfoService) {
        this.bankInfoService = bankInfoService;
    }

    public List<SelectItem> getBankList() {
        return bankList;
    }

    public void setBankList(List<SelectItem> bankList) {
        this.bankList = bankList;
    }
}
