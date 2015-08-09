package rfm.ta.view;

import org.apache.commons.beanutils.BeanUtils;
import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.enums.EnuExecType;
import rfm.ta.common.enums.EnuTaTxCode;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.account.TaPayoutService;
import rfm.ta.service.account.TaRefundService;
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
public class TaRefundAction {
    private static final Logger logger = LoggerFactory.getLogger(TaRefundAction.class);
    public static String EXEC_TYPE = PropertyManager.getProperty("execType");
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taRefundService}")
    private TaRefundService taRefundService;

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
        taTxnFdcValiSend.setTxCode(EnuTaTxCode.TRADE_2201.getCode());
        taRefundService.sendAndRecvRealTimeTxn9902201(taTxnFdcValiSend);
        /*验证后查询*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*验证后立即划拨记账用*/
    public void onBtnActClick() {
        try {
            // 往SBS发送记账信息
            TOA toaSbs=taRefundService.sendAndRecvRealTimeTxn900012202(taTxnFdcValiSendAndRecv);
            if(toaSbs!=null) {
                // 往泰安房地产中心发送记账信息
                TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                BeanUtils.copyProperties(taTxnFdcTemp, taTxnFdcValiSendAndRecv);
                taTxnFdcTemp.setTxCode(EnuTaTxCode.TRADE_2202.getCode());
                taRefundService.sendAndRecvRealTimeTxn9902202(taTxnFdcTemp);
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
            // 往SBS发送记账信息
            TOA toaSbs=taRefundService.sendAndRecvRealTimeTxn900012211(taTxnFdcValiSendAndRecv);
            if(toaSbs!=null) {
                // 往泰安房地产中心发送记账信息
                TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                BeanUtils.copyProperties(taTxnFdcTemp, taTxnFdcCanclSend);
                taTxnFdcCanclSend.setTxCode(EnuTaTxCode.TRADE_2211.getCode());
                taRefundService.sendAndRecvRealTimeTxn9902211(taTxnFdcTemp);
                /*划拨冲正后查询*/
                taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
            }
        }catch (Exception e){
            logger.error("划拨冲正用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =

    public TaRefundService getTaRefundService() {
        return taRefundService;
    }

    public void setTaRefundService(TaRefundService taRefundService) {
        this.taRefundService = taRefundService;
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
