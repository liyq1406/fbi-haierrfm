package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PtenudetailService;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.modelshow.TaActRsltQryModelShow;
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
 * Time: 下午2:12
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

    private List<TaActRsltQryModelShow> taActRsltQryModelShowList;
    private TaActRsltQryModelShow taActRsltQryModelShow;
    private List<SelectItem> taTradeIdList;

    @PostConstruct
    public void init() {
        taActRsltQryModelShow=new TaActRsltQryModelShow();
        taTradeIdList=ptenudetailService.getTxCodeList();
    }

    /*划拨验证用*/
    public void onBtnValiClick() {
        /*验证后查询*/
        onBtnQryClick();
    }
    /*划拨验证查询用*/
    public void onBtnQryClick() {
        taActRsltQryModelShow.setTradeId(taTradeIdList.get(5).getValue().toString());
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public List<TaActRsltQryModelShow> getTaActRsltQryModelShowList() {
        return taActRsltQryModelShowList;
    }

    public void setTaActRsltQryModelShowList(List<TaActRsltQryModelShow> taActRsltQryModelShowList) {
        this.taActRsltQryModelShowList = taActRsltQryModelShowList;
    }

    public TaActRsltQryModelShow getTaActRsltQryModelShow() {
        return taActRsltQryModelShow;
    }

    public void setTaActRsltQryModelShow(TaActRsltQryModelShow taActRsltQryModelShow) {
        this.taActRsltQryModelShow = taActRsltQryModelShow;
    }

    public List<SelectItem> getTaTradeIdList() {
        return taTradeIdList;
    }

    public void setTaTradeIdList(List<SelectItem> taTradeIdList) {
        this.taTradeIdList = taTradeIdList;
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
    }
}
