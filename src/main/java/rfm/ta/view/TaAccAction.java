package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.PtenudetailService;
import rfm.ta.gateway.sbs.taservice.TaSbsService;
import rfm.ta.repository.model.TaRsAccount;
import rfm.ta.service.account.TaAccService;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaAccAction {
    private static final Logger logger = LoggerFactory.getLogger(TaAccAction.class);
    @ManagedProperty(value = "#{taAccService}")
    private TaAccService taAccService;
    @ManagedProperty(value = "#{taSbsTxnService}")
    private TaSbsService taSbsTxnService;
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService ptenudetailService;

    private List<TaRsAccount> taRsAccountList;
    private String confirmAccountNo;

    private TaRsAccount taRsAccount;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String strActionTypeTemp = params.get("actionType");
        String strPkidTemp = params.get("pkid");
        // 一下这样写对于初始化后是否即可查询没有用，因为菜单调用传不过参数来，如果平台换了，是可以起作用的。
        if (strActionTypeTemp != null) {
            taRsAccount = taAccService.qryRecord(strPkidTemp);
            if("Qry".equals(strActionTypeTemp)) {

            }else{
                taRsAccountList = taAccService.qryAllRecords();
            }
        }else{
            taRsAccount=new TaRsAccount();
            taRsAccountList = taAccService.qryAllRecords();
        }
    }

    /*画面查询用*/
    public void onBtnQueryClick() {
        taRsAccountList = taAccService.selectedRecordsByCondition(taRsAccount.getAccType(), taRsAccount.getAccId(), taRsAccount.getAccName());
    }

    public String reset() {
        this.taRsAccount = new TaRsAccount();
        if (!taRsAccountList.isEmpty()) {
            taRsAccountList.clear();
        }
        return null;
    }

    /*登记画面用*/
    public String onAdd() {
        try {
            if (!confirmAccountNo.equalsIgnoreCase(taRsAccount.getAccId())) {
                MessageUtil.addError("两次输入的监管账户号不一致！");
                return null;
            }
            // 初始帐户余额均为可用
            taAccService.insertRecord(taRsAccount);
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("新增数据完成。");
        taRsAccountList = taAccService.qryAllRecords();
        this.taRsAccount = new TaRsAccount();
        confirmAccountNo = "";
        return null;
    }

    /*管理明细画面用*/
    public String onUpd(){
        try {
            taAccService.updateRecord(taRsAccount);
        } catch (Exception e) {
            logger.error("修改数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("修改数据完成。");
        confirmAccountNo = "";
        return null;
    }
    public String onDel(){
        try {
            taAccService.deleteRecord(taRsAccount);
        } catch (Exception e) {
            logger.error("删除数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("删除数据完成。");
        return null;
    }

    public String onClick_ListToDetail(String strActionTypePara,String strPkidPara) {
        if("Upd".equals(strActionTypePara)) {
            return "accountEditDtl_Upd.xhtml?faces-redirect=true&actionType=" + strActionTypePara + "&amp;pkid=" + strPkidPara;
        }else if("Del".equals(strActionTypePara)) {
            return "accountEditDtl_Del.xhtml?faces-redirect=true&actionType=" + strActionTypePara + "&amp;pkid=" + strPkidPara;
        }else {
            return null;
        }
    }
    public String onClick_DetailToList(String strPkidPara) {
        return "accountEdit.xhtml?faces-redirect=true&pkid=" +strPkidPara;
    }

    /*启用*/
    public String onClick_Enable(TaRsAccount taRsAccountPara){
        try {
            List<SelectItem> taAccStatusListTemp=ptenudetailService.getTaAccStatusList();
            // 枚举变量在数据库中，启用标志
            taRsAccountPara.setStatusFlag(taAccStatusListTemp.get(1).getValue().toString());
            taAccService.updateRecord(taRsAccountPara);
        } catch (Exception e) {
            logger.error("启用数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("启用数据成功。");
        confirmAccountNo = "";
        return null;
    }
    /*撤销*/
    public String onClick_Unable(TaRsAccount taRsAccountPara){
        try {
            List<SelectItem> taAccStatusListTemp=ptenudetailService.getTaAccStatusList();
            // 枚举变量在数据库中，撤销标志
            taRsAccountPara.setStatusFlag(taAccStatusListTemp.get(2).getValue().toString());
            taAccService.updateRecord(taRsAccountPara);
        } catch (Exception e) {
            logger.error("启用数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("启用数据成功。");
        confirmAccountNo = "";
        return null;
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaSbsService getTaSbsTxnService() {
        return taSbsTxnService;
    }

    public void setTaSbsTxnService(TaSbsService taSbsTxnService) {
        this.taSbsTxnService = taSbsTxnService;
    }

    public TaAccService getTaAccService() {
        return taAccService;
    }

    public void setTaAccService(TaAccService taAccService) {
        this.taAccService = taAccService;
    }

    public String getConfirmAccountNo() {
        return confirmAccountNo;
    }

    public void setConfirmAccountNo(String confirmAccountNo) {
        this.confirmAccountNo = confirmAccountNo;
    }

    public List<TaRsAccount> getTaRsAccountList() {
        return taRsAccountList;
    }

    public void setTaRsAccountList(List<TaRsAccount> taRsAccountList) {
        this.taRsAccountList = taRsAccountList;
    }

    public TaRsAccount getTaRsAccount() {
        return taRsAccount;
    }

    public void setTaRsAccount(TaRsAccount taRsAccount) {
        this.taRsAccount = taRsAccount;
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
    }
}
