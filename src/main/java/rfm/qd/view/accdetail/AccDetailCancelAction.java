package rfm.qd.view.accdetail;

import rfm.qd.common.constant.ChangeFlag;
import rfm.qd.common.constant.InOutFlag;
import rfm.qd.common.constant.TradeStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.service.RsAccDetailService;
import rfm.qd.service.TradeService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-6
 * Time: ����3:37
 * To change this template use File | Settings | File Templates.
 */
// ����
@ManagedBean
@ViewScoped
public class AccDetailCancelAction {

    @ManagedProperty(value = "#{rsAccDetailService}")
    private RsAccDetailService accDetailService;
    @ManagedProperty(value = "#{tradeService}")
    private TradeService tradeService;
    private QdRsAccDetail accDetail;
    private List<QdRsAccDetail> accDetailList;
    private List<QdRsAccDetail> accDetailApList;

    private InOutFlag inoutFlag = InOutFlag.IN;
    private TradeType tradeType = TradeType.HOUSE_INCOME;
    private TradeStatus tradeStatus = TradeStatus.SUCCESS;


    @PostConstruct
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        // String action = (String) context.getExternalContext().getRequestParameterMap().get("action");
        if (!StringUtils.isEmpty(pkid)) {
            accDetail = accDetailService.selectAccDetailByPkid(pkid);
        } else {
            accDetailList = accDetailService.selectCancelAccDetails();
            accDetailApList = accDetailService.selectAPCancelAccDetails();
        }
    }

    public String onApplyCancel() {
        try {
            accDetail.setChangeFlag(ChangeFlag.AP_CANCEL.getCode());
            if (accDetailService.updateAccDetail(accDetail)== 1) {
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
                MessageUtil.addInfo("���׳�������ɹ�������ˣ�");
            }else {
                MessageUtil.addError("���׳�������ʧ�ܣ�");
            }
        } catch (Exception e) {
            MessageUtil.addError("����ʧ��." + e.getMessage());
        }
        return null;
    }

    public String onCancel() {
        try {
            if (tradeService.handleCancelAccDetail(accDetail, ChangeFlag.CANCEL) == 3) {
                UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
                CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
                saveBtn.setDisabled(true);
                MessageUtil.addInfo("���׳����ɹ���");
            }else {
                MessageUtil.addError("���׳���ʧ�ܣ�");
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.addError("����ʧ��." + e.getMessage());
        }
        return null;
    }


    //=================================

    public QdRsAccDetail getAccDetail() {
        return accDetail;
    }

    public void setAccDetail(QdRsAccDetail accDetail) {
        this.accDetail = accDetail;
    }

    public List<QdRsAccDetail> getAccDetailList() {
        return accDetailList;
    }

    public void setAccDetailList(List<QdRsAccDetail> accDetailList) {
        this.accDetailList = accDetailList;
    }

    public RsAccDetailService getAccDetailService() {
        return accDetailService;
    }

    public void setAccDetailService(RsAccDetailService accDetailService) {
        this.accDetailService = accDetailService;
    }

    public InOutFlag getInoutFlag() {
        return inoutFlag;
    }

    public void setInoutFlag(InOutFlag inoutFlag) {
        this.inoutFlag = inoutFlag;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public TradeService getTradeService() {
        return tradeService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public List<QdRsAccDetail> getAccDetailApList() {
        return accDetailApList;
    }

    public void setAccDetailApList(List<QdRsAccDetail> accDetailApList) {
        this.accDetailApList = accDetailApList;
    }
}
