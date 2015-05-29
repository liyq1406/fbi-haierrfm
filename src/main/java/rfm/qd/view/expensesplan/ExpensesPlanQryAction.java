package rfm.qd.view.expensesplan;

import rfm.qd.repository.model.RsPlanCtrl;
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
    private RsPlanCtrl rsPlanCtrl;
    private List<RsPlanCtrl> rsPlanCtrlList;

    @ManagedProperty(value = "#{expensesPlanService}")
    private ExpensesPlanService expensesPlanService;

    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        //String action = (String) context.getExternalContext().getRequestParameterMap().get("action");
        if(StringUtils.isEmpty(pkid)) {
        rsPlanCtrl = new RsPlanCtrl();
         rsPlanCtrlList = expensesPlanService.selectPlanList();
        }else {
           rsPlanCtrl = expensesPlanService.selectPlanCtrlByPkid(pkid);
        }
    }

    public String onQuery() {
        rsPlanCtrlList = expensesPlanService.selectPlanListByFields(rsPlanCtrl.getCompanyName(),
                rsPlanCtrl.getAccountCode(), rsPlanCtrl.getPayContractNo());
        if(rsPlanCtrlList.isEmpty()) {
            MessageUtil.addWarn("没有查询到符合条件的数据记录！");
        }
        return null;
    }

    public void onPrint() {
        rsPlanCtrlList = expensesPlanService.selectPlanList();
    }

    //======================================================================


    public RsPlanCtrl getRsPlanCtrl() {
        return rsPlanCtrl;
    }

    public void setRsPlanCtrl(RsPlanCtrl rsPlanCtrl) {
        this.rsPlanCtrl = rsPlanCtrl;
    }

    public List<RsPlanCtrl> getRsPlanCtrlList() {
        return rsPlanCtrlList;
    }

    public void setRsPlanCtrlList(List<RsPlanCtrl> rsPlanCtrlList) {
        this.rsPlanCtrlList = rsPlanCtrlList;
    }

   /* public void setPlatformService(PlatformService platformService) {
        this.platformService = platformService;
    } */

    public void setExpensesPlanService(ExpensesPlanService expensesPlanService) {
        this.expensesPlanService = expensesPlanService;
    }
}
