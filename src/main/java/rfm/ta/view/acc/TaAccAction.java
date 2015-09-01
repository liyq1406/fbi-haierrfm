package rfm.ta.view.acc;

import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.PtenudetailService;
import pub.platform.advance.utils.RfmMessage;
import rfm.qd.service.RsSysctlService;
import rfm.ta.common.enums.EnuActRslt;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.service.biz.acc.TaAccService;
import rfm.ta.service.dep.TaFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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

    @ManagedProperty(value = "#{taFdcService}")
    private TaFdcService taFdcService;

    private List<TaRsAcc> taRsAccList;

    private TaRsAcc taRsAcc;
    private TaRsAcc taRsAccRecv;

    @PostConstruct
    public void init() {
        taRsAcc=new TaRsAcc();
        taRsAccRecv=new TaRsAcc();
    }

    /*画面查询用*/
    public void onBtnQueryClick() {
        taRsAccList = taAccService.selectedRecordsByCondition(
                taRsAcc.getSpvsnAccType(),
                taRsAcc.getSpvsnAccId(),
                taRsAcc.getSpvsnAccName());
    }

    /*启用*/
    public void onClick_Enable(){
        try {
            String strRtn=taAccService.isExistInDb(taRsAcc);
            if(strRtn!=null){
                MessageUtil.addError(strRtn);
                return;
            }

            TOA toaTa = taFdcService.sendAndRecvRealTimeTxn9901001(taRsAcc);
            if(toaTa !=null) {
                Toa9901001 toa9901001 = (Toa9901001)toaTa;
                if ((EnuTaTxnRtnCode.TXN_PROCESSED.getCode()).equals(toaTa.getHeader().RETURN_CODE)) { // TA成功的处理
                    taRsAccRecv.setReturnCode(toa9901001.header.RETURN_CODE);
                    taRsAccRecv.setFdcSn(toa9901001.header.REQ_SN);
                    taRsAccRecv.setPreSalePerName(toa9901001.body.PRE_SALE_PER_NAME);
                    taRsAccRecv.setPreSaleProAddr(toa9901001.body.PRE_SALE_PRO_ADDR);
                    taRsAccRecv.setPreSaleProName(toa9901001.body.PRE_SALE_PRO_NAME);
                    taRsAccRecv.setReturnMsg(toa9901001.header.RETURN_MSG);
                    MessageUtil.addInfo(RfmMessage.getProperty("AccountOpening.I001"));
                }else{
                    MessageUtil.addError(toaTa.getHeader().RETURN_MSG);
                }
            }else{
                MessageUtil.addInfo("启用监管失败!");
            }
        } catch (Exception e) {
            logger.error("启用监管失败，", e);
            MessageUtil.addError(taRsAccRecv.getReturnMsg()+e.getMessage());
        }
    }
    /*撤销*/
    public void onClick_Unable(){
        try {
            List<TaRsAcc> taRsAccListTemp = taAccService.selectRecords(taRsAcc);
            if(taRsAccListTemp.size() == 0){
                MessageUtil.addError(RfmMessage.getProperty("AccountCancel.E001"));
                return;
            } else {
                if(!taRsAccListTemp.get(0).getStatusFlag().equals(EnuTaAccStatus.ACC_SUPV.getCode())) {
                    MessageUtil.addError(RfmMessage.getProperty("AccountCancel.E002"));
                    return;
                }
            }
            taRsAcc = taRsAccListTemp.get(0);
            TOA toaTa = taFdcService.sendAndRecvRealTimeTxn9901002(taRsAcc);
            if(toaTa !=null) {
                if ((EnuTaTxnRtnCode.TXN_PROCESSED.getCode()).equals(toaTa.getHeader().RETURN_CODE)) { // TA成功的处理
                    // 初始帐户余额均为可用
                    taAccService.insertRecord(taRsAcc);

                    taRsAccRecv.setReturnCode(toaTa.getHeader().RETURN_CODE);
                    taRsAccRecv.setFdcSn(toaTa.getHeader().REQ_SN);
                    taRsAccRecv.setReturnMsg(toaTa.getHeader().RETURN_MSG);
                    MessageUtil.addInfo(RfmMessage.getProperty("AccountCancel.I001"));
                }else{
                    MessageUtil.addError(toaTa.getHeader().RETURN_MSG);
                }
            }else{
                MessageUtil.addInfo("启用监管失败!");
            }
        } catch (Exception e) {
            logger.error("解除监管失败，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaAccService getTaAccService() {
        return taAccService;
    }

    public void setTaAccService(TaAccService taAccService) {
        this.taAccService = taAccService;
    }

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
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

    public TaRsAcc getTaRsAccRecv() {
        return taRsAccRecv;
    }

    public void setTaRsAccRecv(TaRsAcc taRsAccRecv) {
        this.taRsAccRecv = taRsAccRecv;
    }

    public RsSysctlService getRsSysctlService() {
        return rsSysctlService;
    }

    public void setRsSysctlService(RsSysctlService rsSysctlService) {
        this.rsSysctlService = rsSysctlService;
    }
}
