package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PtenudetailService;
import rfm.ta.repository.model.TaRsAccDetail;
import rfm.ta.service.account.TaAccDetlService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaActRsltQryAction {
    private static final Logger logger = LoggerFactory.getLogger(TaActRsltQryAction.class);
    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService ptenudetailService;

    private List<TaRsAccDetail> taRsAccDetailList;
    private TaRsAccDetail taRsAccDetail;
    private List<SelectItem> taTradeIdList;

    @PostConstruct
    public void init() {
        taRsAccDetail=new TaRsAccDetail();
        taTradeIdList=ptenudetailService.getTradeIdList();
    }

    /*������֤��*/
    public void onBtnValiClick() {
        /*��֤���ѯ*/
        onBtnValiQryClick();
    }
    /*������֤��ѯ��*/
    public void onBtnValiQryClick() {
        taRsAccDetail.setTradeId(taTradeIdList.get(5).getValue().toString());
        taRsAccDetailList = taAccDetlService.selectedRecords(taRsAccDetail);
    }

    /*����������*/
    public void onBtnActClick() {
        /*���˺��ѯ*/
        onBtnActQryClick();
    }
    /*�������˲�ѯ��*/
    public void onBtnActQryClick() {
        taRsAccDetail.setTradeId(taTradeIdList.get(6).getValue().toString());
        taRsAccDetailList = taAccDetlService.selectedRecords(taRsAccDetail);
    }

    /*����������*/
    public void onBtnCanclClick() {
        /*�������ѯ*/
        onBtnCanclQryClick();
    }
    /*����������ѯ��*/
    public void onBtnCanclQryClick() {
        taRsAccDetail.setTradeId(taTradeIdList.get(7).getValue().toString());
        taRsAccDetailList = taAccDetlService.selectedRecords(taRsAccDetail);
    }

    /*������ѯ��*/
    public void onBtnQryClick() {
        taRsAccDetailList = taAccDetlService.selectedRecords(taRsAccDetail);
    }

    public String reset() {
        this.taRsAccDetail = new TaRsAccDetail();
        if (!taRsAccDetailList.isEmpty()) {
            taRsAccDetailList.clear();
        }
        return null;
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public List<TaRsAccDetail> getTaRsAccDetailList() {
        return taRsAccDetailList;
    }

    public void setTaRsAccDetailList(List<TaRsAccDetail> taRsAccDetailList) {
        this.taRsAccDetailList = taRsAccDetailList;
    }

    public TaRsAccDetail getTaRsAccDetail() {
        return taRsAccDetail;
    }

    public void setTaRsAccDetail(TaRsAccDetail taRsAccDetail) {
        this.taRsAccDetail = taRsAccDetail;
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
    }
}
