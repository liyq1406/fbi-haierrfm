package rfm.ta.view;

import com.longtu.framework.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.enums.EnuActFlag;
import rfm.ta.common.enums.EnuDelFlag;
import rfm.ta.common.enums.EnuExecType;
import rfm.ta.common.enums.EnuTaTxCode;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.account.TaAccDetlService;
import rfm.ta.service.account.TaPayoutService;
import rfm.ta.service.his.TaTxnFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
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
public class TaPayoutAction {
    private static final Logger logger = LoggerFactory.getLogger(TaPayoutAction.class);
    public static String EXEC_TYPE = PropertyManager.getProperty("execType");
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taPayoutService}")
    private TaPayoutService taPayoutService;

    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    private TaTxnFdc taTxnFdcValiSend;
    private TaTxnFdc taTxnFdcValiSendAndRecv;
    private TaTxnFdc taTxnFdcActSend;
    private TaTxnFdc taTxnFdcActSendAndRecv;
    private TaTxnFdc taTxnFdcCanclSend;
    private TaTxnFdc taTxnFdcCanclSendAndRecv;

    private String strVisableByExecType;

    @PostConstruct
    public void init() {
        taTxnFdcValiSend=new TaTxnFdc();
        taTxnFdcValiSendAndRecv=new TaTxnFdc();
        taTxnFdcActSend=new TaTxnFdc();
        taTxnFdcActSendAndRecv=new TaTxnFdc();
        taTxnFdcCanclSend=new TaTxnFdc();
        taTxnFdcCanclSendAndRecv=new TaTxnFdc();
        if(EnuExecType.EXEC_TYPE_DEBUG.getCode().equals(EXEC_TYPE)){
            strVisableByExecType="true";
        }else{
            strVisableByExecType="false";
        }
    }

    /*划拨验证用*/
    public void onBtnValiClick() {
        // 发送验证信息
        taTxnFdcValiSend.setTxCode(EnuTaTxCode.TRADE_2101.getCode());
        taPayoutService.sendAndRecvRealTimeTxn9902101(taTxnFdcValiSend);
        /*验证后查询*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*验证后立即划拨记账用*/
    public void onBtnActClick() {
        try {
            // 本地存取（对账用）
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            BeanUtils.copyProperties(taRsAccDtlTemp, taTxnFdcValiSendAndRecv);
            taRsAccDtlTemp.setDeletedFlag(EnuDelFlag.DEL_FALSE.getCode());
            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
            taAccDetlService.insertRecord(taRsAccDtlTemp);

            // 往SBS发送记账信息
            TOA toaSbs=taPayoutService.sendAndRecvRealTimeTxn900012102(taTxnFdcValiSendAndRecv);
            if(toaSbs !=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                    taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlTemp);
                } else { // SBS记账失败的处理
                    taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_FAIL.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlTemp);
                }

                // 往泰安房地产中心发送记账信息
                TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                BeanUtils.copyProperties(taTxnFdcTemp, taTxnFdcValiSendAndRecv);
                taTxnFdcTemp.setTxCode(EnuTaTxCode.TRADE_2102.getCode());
                taPayoutService.sendAndRecvRealTimeTxn9902102(taTxnFdcTemp);
                /*记账后查询*/
                taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
            }
        }catch (Exception e){
            logger.error("验证后立即划拨记账用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*划拨冲正用*/
    public void onBtnCanclClick() {
        try {
            // 本地存取（对账用）
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            taRsAccDtlTemp.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtlTemp.setTxCode(EnuTaTxCode.TRADE_2101.getCode());
            List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTemp);
            TaRsAccDtl taRsAccDtl = null;
            if(taRsAccDtlList.size() == 1){
                taRsAccDtl = taRsAccDtlList.get(0);
                String accId = taRsAccDtl.getAccId();
                String recvAccId = taRsAccDtl.getRecvAccId();
                taRsAccDtl.setAccId(recvAccId);
                taRsAccDtl.setRecvAccId(accId);
                taRsAccDtl.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                taAccDetlService.insertRecord(taRsAccDtl);
            } else {
                logger.error("查不到该笔冲正的相关划拨信息，请确认输入的划拨申请编号");
                MessageUtil.addError("查不到该笔冲正的相关划拨信息，请确认输入的划拨申请编号");
            }

            // 往SBS发送记账信息
            TOA toaSbs=taPayoutService.sendAndRecvRealTimeTxn900012111(taTxnFdcCanclSend);
            if(toaSbs !=null) {
                if(taRsAccDtl != null) {
                    if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                        taRsAccDtl.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                        taAccDetlService.updateRecord(taRsAccDtl);
                    } else { // SBS记账失败的处理
                        taRsAccDtl.setActFlag(EnuActFlag.ACT_FAIL.getCode());
                        taAccDetlService.updateRecord(taRsAccDtl);
                    }
                }

                // 往泰安房地产中心发送记账信息
                taTxnFdcCanclSend.setTxCode(EnuTaTxCode.TRADE_2111.getCode());
                taPayoutService.sendAndRecvRealTimeTxn9902111(taTxnFdcCanclSend);
                /*划拨冲正后查询*/
                taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcCanclSend.getPkId());
            }
        }catch (Exception e){
            logger.error("划拨冲正用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }
    public TaPayoutService getTaPayoutService() {
        return taPayoutService;
    }

    public void setTaPayoutService(TaPayoutService taPayoutService) {
        this.taPayoutService = taPayoutService;
    }

    public TaTxnFdcService getTaTxnFdcService() {
        return taTxnFdcService;
    }

    public void setTaTxnFdcService(TaTxnFdcService taTxnFdcService) {
        this.taTxnFdcService = taTxnFdcService;
    }

    public TaTxnFdc getTaTxnFdcValiSend() {
        return taTxnFdcValiSend;
    }

    public void setTaTxnFdcValiSend(TaTxnFdc taTxnFdcValiSend) {
        this.taTxnFdcValiSend = taTxnFdcValiSend;
    }

    public TaTxnFdc getTaTxnFdcValiSendAndRecv() {
        return taTxnFdcValiSendAndRecv;
    }

    public void setTaTxnFdcValiSendAndRecv(TaTxnFdc taTxnFdcValiSendAndRecv) {
        this.taTxnFdcValiSendAndRecv = taTxnFdcValiSendAndRecv;
    }

    public TaTxnFdc getTaTxnFdcActSend() {
        return taTxnFdcActSend;
    }

    public void setTaTxnFdcActSend(TaTxnFdc taTxnFdcActSend) {
        this.taTxnFdcActSend = taTxnFdcActSend;
    }

    public TaTxnFdc getTaTxnFdcActSendAndRecv() {
        return taTxnFdcActSendAndRecv;
    }

    public void setTaTxnFdcActSendAndRecv(TaTxnFdc taTxnFdcActSendAndRecv) {
        this.taTxnFdcActSendAndRecv = taTxnFdcActSendAndRecv;
    }

    public TaTxnFdc getTaTxnFdcCanclSend() {
        return taTxnFdcCanclSend;
    }

    public void setTaTxnFdcCanclSend(TaTxnFdc taTxnFdcCanclSend) {
        this.taTxnFdcCanclSend = taTxnFdcCanclSend;
    }

    public TaTxnFdc getTaTxnFdcCanclSendAndRecv() {
        return taTxnFdcCanclSendAndRecv;
    }

    public void setTaTxnFdcCanclSendAndRecv(TaTxnFdc taTxnFdcCanclSendAndRecv) {
        this.taTxnFdcCanclSendAndRecv = taTxnFdcCanclSendAndRecv;
    }

    public String getStrVisableByExecType() {
        return strVisableByExecType;
    }
}
