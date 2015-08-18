package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.his.TaTxnFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: œ¬ŒÁ2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaTxnFdcAction {
    private static final Logger logger = LoggerFactory.getLogger(TaTxnFdcAction.class);
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    private List<TaTxnFdc> taTxnFdcList;
    private TaTxnFdc taTxnFdc;

    @PostConstruct
    public void init() {
        taTxnFdc=new TaTxnFdc();
    }

    /*≤È—Ø”√*/
    public void onBtnQryClick() {
        taTxnFdcList = taTxnFdcService.selectedAllRecords(taTxnFdc);
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaTxnFdcService getTaTxnFdcService() {
        return taTxnFdcService;
    }

    public void setTaTxnFdcService(TaTxnFdcService taTxnFdcService) {
        this.taTxnFdcService = taTxnFdcService;
    }

    public List<TaTxnFdc> getTaTxnFdcList() {
        return taTxnFdcList;
    }

    public void setTaTxnFdcList(List<TaTxnFdc> taTxnFdcList) {
        this.taTxnFdcList = taTxnFdcList;
    }

     public TaTxnFdc getTaTxnFdc() {
        return taTxnFdc;
    }

    public void setTaTxnFdc(TaTxnFdc taTxnFdc) {
        this.taTxnFdc = taTxnFdc;
    }
}
