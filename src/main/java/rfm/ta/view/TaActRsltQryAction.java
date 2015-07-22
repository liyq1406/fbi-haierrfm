package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.service.PtenudetailService;
import rfm.ta.common.enums.EnuTaTxCode;
import rfm.ta.repository.model.TaRsAccDtl;
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

    private List<TaRsAccDtl> taRsAccDtlList;
    private TaRsAccDtl taRsAccDtlQry;
    private List<SelectItem> taTxCodeList;

    @PostConstruct
    public void init() {
        taRsAccDtlQry=new TaRsAccDtl();
        taTxCodeList=ptenudetailService.getTxCodeList();
    }

    /*划拨验证用*/
    public void onBtnValiClick() {
        /*验证后查询*/
        onBtnQryClick();
    }
    /*划拨验证查询用*/
    public void onBtnQryClick() {
        taRsAccDtlQry.setTxCode(EnuTaTxCode.TRADE_2501.getCode());
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public List<TaRsAccDtl> getTaRsAccDtlList() {
        return taRsAccDtlList;
    }

    public void setTaRsAccDtlList(List<TaRsAccDtl> taRsAccDtlList) {
        this.taRsAccDtlList = taRsAccDtlList;
    }

    public TaRsAccDtl getTaRsAccDtlQry() {
        return taRsAccDtlQry;
    }

    public void setTaRsAccDtlQry(TaRsAccDtl taRsAccDtlQry) {
        this.taRsAccDtlQry = taRsAccDtlQry;
    }

    public List<SelectItem> getTaTxCodeList() {
        return taTxCodeList;
    }

    public void setTaTxCodeList(List<SelectItem> taTxCodeList) {
        this.taTxCodeList = taTxCodeList;
    }

    public PtenudetailService getPtenudetailService() {
        return ptenudetailService;
    }

    public void setPtenudetailService(PtenudetailService ptenudetailService) {
        this.ptenudetailService = ptenudetailService;
    }
}
