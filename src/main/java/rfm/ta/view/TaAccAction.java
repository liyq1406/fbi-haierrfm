package rfm.ta.view;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import platform.common.utils.MessageUtil;
import platform.service.PtenudetailService;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.utils.SerialNumber;
import rfm.qd.service.RsSysctlService;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.service.account.TaAccService;
import rfm.ta.service.dep.DepService;

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
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService ptenudetailService;
    @ManagedProperty(value = "#{rsSysctlService}")
    private RsSysctlService rsSysctlService;

    private List<TaRsAcc> taRsAccList;
    private String confirmAccountNo;

    private TaRsAcc taRsAcc;

    @PostConstruct
    public void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String strActionTypeTemp = params.get("actionType");
        String strPkidTemp = params.get("pkid");
        // 一下这样写对于初始化后是否即可查询没有用，因为菜单调用传不过参数来，如果平台换了，是可以起作用的。
        if (strActionTypeTemp != null) {
            taRsAcc = taAccService.qryRecord(strPkidTemp);
            if("Qry".equals(strActionTypeTemp)) {

            }else{
                taRsAccList = taAccService.qryAllRecords();
            }
        }else{
            taRsAcc=new TaRsAcc();
            //taRsAccList = taAccService.qryAllRecords();
        }
    }

    /*画面查询用*/
    public void onBtnQueryClick() {
        taRsAccList = taAccService.selectedRecordsByCondition(taRsAcc.getAccType(), taRsAcc.getAccId(), taRsAcc.getAccName());
    }

    public String reset() {
        this.taRsAcc = new TaRsAcc();
        if (!taRsAccList.isEmpty()) {
            taRsAccList.clear();
        }
        return null;
    }

    /*登记画面用*/
    public String onAdd() {
        try {
            if (!confirmAccountNo.equalsIgnoreCase(taRsAcc.getAccId())) {
                MessageUtil.addError("两次输入的监管账户号不一致！");
                return null;
            }
            // 初始帐户余额均为可用
            taAccService.insertRecord(taRsAcc);
        } catch (Exception e) {
            logger.error("新增数据失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("新增数据完成。");
        taRsAccList = taAccService.qryAllRecords();
        this.taRsAcc = new TaRsAcc();
        confirmAccountNo = "";
        return null;
    }

    /*管理明细画面用*/
    public String onUpd(){
        try {
            taAccService.updateRecord(taRsAcc);
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
            taAccService.deleteRecord(taRsAcc);
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
    public String onClick_Enable(TaRsAcc taRsAccPara){
        try {
            taAccService.sendAndRecvRealTimeTxn9901001(taRsAccPara);
        } catch (Exception e) {
            logger.error("启用监管失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("启用监管成功。");
        confirmAccountNo = "";
        return null;
    }
    /*撤销*/
    public String onClick_Unable(TaRsAcc taRsAccPara){
        try {
            taAccService.sendAndRecvRealTimeTxn9901002(taRsAccPara);
        } catch (Exception e) {
            logger.error("解除监管失败，", e);
            MessageUtil.addError(e.getMessage());
            return null;
        }
        MessageUtil.addInfo("解除监管成功。");
        confirmAccountNo = "";
        return null;
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
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

    public List<TaRsAcc> getTaRsAccList() {
        return taRsAccList;
    }

    public void setTaRsAccList(List<TaRsAcc> taRsAccList) {
        this.taRsAccList = taRsAccList;
    }

    public TaRsAcc getTaRsAcc() {
        return taRsAcc;
    }

    public void setTaRsAcc(TaRsAcc taRsAcc) {
        this.taRsAcc = taRsAcc;
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
    }

    public RsSysctlService getRsSysctlService() {
        return rsSysctlService;
    }

    public void setRsSysctlService(RsSysctlService rsSysctlService) {
        this.rsSysctlService = rsSysctlService;
    }
}
