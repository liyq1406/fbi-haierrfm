package rfm.qd.view.payout;

import rfm.qd.common.constant.RefundStatus;
import rfm.qd.repository.model.QdRsPayout;
import rfm.qd.service.PayoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PtenudetailService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-31
 * Time: ÉÏÎç10:25
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class PayoutQryAction {
    private static Logger logger = LoggerFactory.getLogger(PayoutQryAction.class);
    @ManagedProperty(value = "#{payoutService}")
    private PayoutService payoutService;
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService toolsService;
    private ParamPlan paramPlan;
    private List<QdRsPayout> payoutList;
    private List<SelectItem> refundStatusList;
    private RefundStatus statusFlag = RefundStatus.ACCOUNT_SUCCESS;
    @PostConstruct
    public void init() {
         paramPlan = new ParamPlan();
         refundStatusList = toolsService.getEnuSelectItemList("REFUND_STATUS", true, false);
    }

    public String onQuery() {
        payoutList = payoutService.selectRsPayoutsByParamPlan(paramPlan);
        return null;
    }

    //======================================

    public PayoutService getPayoutService() {
        return payoutService;
    }

    public void setPayoutService(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    public ParamPlan getParamPlan() {
        return paramPlan;
    }

    public void setParamPlan(ParamPlan paramPlan) {
        this.paramPlan = paramPlan;
    }

    public List<QdRsPayout> getPayoutList() {
        return payoutList;
    }

    public void setPayoutList(List<QdRsPayout> payoutList) {
        this.payoutList = payoutList;
    }

    public PtenudetailService getToolsService() {
        return toolsService;
    }

    public void setToolsService(PtenudetailService toolsService) {
        this.toolsService = toolsService;
    }

    public List<SelectItem> getRefundStatusList() {
        return refundStatusList;
    }

    public void setRefundStatusList(List<SelectItem> refundStatusList) {
        this.refundStatusList = refundStatusList;
    }

    public RefundStatus getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(RefundStatus statusFlag) {
        this.statusFlag = statusFlag;
    }
}
