package rfm.ta.view.acc;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.acc.TaAccDetlService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.util.List;

/**
 * Created by Lichao.W At 2015/7/6 17:01
 * wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class TaAccDtlAction implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(TaAccDtlAction.class);

    @ManagedProperty("#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    private TaTxnSbs taTxnSbs;
    // 账务交易明细
    private List<TaRsAccDtl> taRsAccDtlList;
    private List<TaRsAccDtl> taRsAccDtlSbsList;

    @PostConstruct
    public void init(){
        TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
        taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
        taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlPara);
        if(taRsAccDtlList.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getSpvsnAccId());
        }
    }

    //= = = = = = = = = = = = get set = = = = = = = = = = = =

    public TaTxnSbs getTaTxnSbs() {
        return taTxnSbs;
    }

    public void setTaTxnSbs(TaTxnSbs taTxnSbs) {
        this.taTxnSbs = taTxnSbs;
    }

    public List<TaRsAccDtl> getTaRsAccDtlSbsList() {
        return taRsAccDtlSbsList;
    }

    public void setTaRsAccDtlSbsList(List<TaRsAccDtl> taRsAccDtlSbsList) {
        this.taRsAccDtlSbsList = taRsAccDtlSbsList;
    }

    public List<TaRsAccDtl> getTaRsAccDtlList() {
        return taRsAccDtlList;
    }

    public void setTaRsAccDtlList(List<TaRsAccDtl> taRsAccDtlList) {
        this.taRsAccDtlList = taRsAccDtlList;
    }

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public List<TaRsAccDtl> getTaRsAccDetailList() {
        return taRsAccDtlList;
    }

    public void setTaRsAccDetailList(List<TaRsAccDtl> taRsAccDtlList) {
        this.taRsAccDtlList = taRsAccDtlList;
    }
}
