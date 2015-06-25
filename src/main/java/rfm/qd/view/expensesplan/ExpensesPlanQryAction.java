package rfm.qd.view.expensesplan;

import rfm.qd.repository.model.QdRsPlanCtrl;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-8-10
 * Time: 下午3:38
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class ExpensesPlanQryAction implements Serializable {
    private QdRsPlanCtrl qdRsPlanCtrl;
    private List<QdRsPlanCtrl> qdRsPlanCtrlList;

    @ManagedProperty(value = "#{expensesPlanService}")
    private ExpensesPlanService expensesPlanService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        //String action = (String) context.getExternalContext().getRequestParameterMap().get("action");
        if(StringUtils.isEmpty(pkid)) {
        qdRsPlanCtrl = new QdRsPlanCtrl();
         qdRsPlanCtrlList = expensesPlanService.selectPlanList();
        }else {
           qdRsPlanCtrl = expensesPlanService.selectPlanCtrlByPkid(pkid);
        }
    }

    public String onQuery() {
        qdRsPlanCtrlList = expensesPlanService.selectPlanListByFields(qdRsPlanCtrl.getCompanyName(),
                qdRsPlanCtrl.getAccountCode(), qdRsPlanCtrl.getPayContractNo());
        if(qdRsPlanCtrlList.isEmpty()) {
            MessageUtil.addWarn("没有查询到符合条件的数据记录！");
        }
        return null;
    }

    public void onPrint() {
        qdRsPlanCtrlList = expensesPlanService.selectPlanList();
    }

    //======================================================================


    public QdRsPlanCtrl getQdRsPlanCtrl() {
        return qdRsPlanCtrl;
    }

    public void setQdRsPlanCtrl(QdRsPlanCtrl qdRsPlanCtrl) {
        this.qdRsPlanCtrl = qdRsPlanCtrl;
    }

    public List<QdRsPlanCtrl> getQdRsPlanCtrlList() {
        return qdRsPlanCtrlList;
    }

    public void setQdRsPlanCtrlList(List<QdRsPlanCtrl> qdRsPlanCtrlList) {
        this.qdRsPlanCtrlList = qdRsPlanCtrlList;
    }

   /* public void setPlatformService(PlatformService platformService) {
        this.platformService = platformService;
    } */

    public void setExpensesPlanService(ExpensesPlanService expensesPlanService) {
        this.expensesPlanService = expensesPlanService;
    }
}
