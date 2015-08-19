package rfm.ta.view;

import common.utils.ToolUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.auth.MD5Helper;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.acc.TaAccDetlService;
import rfm.ta.service.dep.TaFdcService;
import rfm.ta.service.dep.TaSbsService;
import rfm.ta.service.biz.his.TaTxnFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
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
public class TaRefundAction {
    private static final Logger logger = LoggerFactory.getLogger(TaRefundAction.class);
    public static String EXEC_TYPE = PropertyManager.getProperty("execType");
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taFdcService}")
    private TaFdcService taFdcService;

    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @ManagedProperty(value = "#{taSbsService}")
    private TaSbsService taSbsService;

    // 记账成功标志
    private List<SelectItem> actFlagList;
    private Map<String, String> actFlagMap;
    private TaRsAccDtl taRsAccDtl;
    private List<TaRsAccDtl> taRsAccDtlList;

    private TaTxnFdc taTxnFdcValiSend;
    private TaTxnFdc taTxnFdcValiSendAndRecv;
    private TaTxnFdc taTxnFdcActSend;
    private TaTxnFdc taTxnFdcActSendAndRecv;
    private TaTxnFdc taTxnFdcCanclSend;
    private TaTxnFdc taTxnFdcCanclSendAndRecv;

    private String strVisableByExecType;

    @PostConstruct
    public void init() {
        // 查询用初始化
        actFlagList = taAccDetlService.getActFlagList();
        actFlagMap = taAccDetlService.getActFlagMap();
        taRsAccDtl = new TaRsAccDtl();
        taRsAccDtl.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());

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

    /*返还验证用*/
    public void onBtnValiClick() {
        // 发送验证信息
        taTxnFdcValiSend.setTxCode(EnuTaFdcTxCode.TRADE_2201.getCode());
        taFdcService.sendAndRecvRealTimeTxn9902201(taTxnFdcValiSend);
        /*验证后查询*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*验证后立即返还记账用*/
    public void onBtnActClick() {
        try {
            if(StringUtils.isEmpty(taTxnFdcValiSendAndRecv.getRecvAccId())){
                MessageUtil.addError("必须输入返还账号！");
                return;
            }

            // 验证重复记账
            TaRsAccDtl taRsAccDtl = new TaRsAccDtl();
            taRsAccDtl.setBizId(taTxnFdcValiSendAndRecv.getBizId());
            taRsAccDtl.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtl);
            if(taRsAccDtlList.size() == 1){
                String actFlag = taRsAccDtlList.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError("该返还申请编号已记账，不允许重复记账！");
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError("该返还申请编号记账时存在不明原因失败，请在返还查询画面进行记账！");
                }
                return;
            }

            // 本地存取（对账用）
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            BeanUtils.copyProperties(taRsAccDtlTemp, taTxnFdcValiSendAndRecv);
            taRsAccDtlTemp.setReqSn(ToolUtil.getStrReqSn_Back());
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtlTemp.setDeletedFlag(EnuDelFlag.DEL_FALSE.getCode());
            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
            taAccDetlService.insertRecord(taRsAccDtlTemp);

            // 往SBS和FDC发送记账信息
            if(sendAndRecvSBSAndFDC(taRsAccDtlTemp)) {
                MessageUtil.addInfo("返还记账成功！");
            }else{
                MessageUtil.addInfo("返还记账失败！");
            }
        }catch (Exception e){
            logger.error("验证后立即返还记账用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*验证后立即划拨记账用*/
    public Boolean sendAndRecvSBSAndFDC(TaRsAccDtl taRsAccDtlPara) {
        try {
            // 往SBS发送记账信息
            taRsAccDtlPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taRsAccDtlPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taRsAccDtlPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taRsAccDtlPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taRsAccDtlPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlPara);
            if(toaSbs!=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                    taRsAccDtlPara.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlPara);

                    // 往泰安房地产中心发送记账信息
                    TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                    BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlPara);
                    taTxnFdcTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
                    taFdcService.sendAndRecvRealTimeTxn9902202(taTxnFdcTemp);
                    /*记账后查询*/
                    taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
                } else { // SBS记账失败的处理
                    taAccDetlService.deleteRecord(taRsAccDtlPara.getPkId());
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            logger.error("验证后立即返还记账用，", e);
            MessageUtil.addError(e.getMessage());
            return false;
        }
    }

    /*返还冲正用*/
    public void onBtnCanclClick() {
        try {
            // 验证重复冲正
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            taRsAccDtlTemp.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
            List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTemp);
            if(taRsAccDtlList.size() == 1){
                String actFlag = taRsAccDtlList.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError("该返还申请编号已冲正，不允许重复冲正！");
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError("该返还申请编号冲正时存在不明原因失败，请在返还查询画面进行冲正！");
                }
                return;
            }

            // 本地存取（对账用）
            taRsAccDtlTemp = new TaRsAccDtl();
            taRsAccDtlTemp.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTemp);
            taRsAccDtlTemp = null;
            if(taRsAccDtlList.size() == 1){
                taRsAccDtlTemp = taRsAccDtlList.get(0);
                // 与返还记账：收款账号和付款账号关系正好颠倒
                taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
                String accId = taRsAccDtlTemp.getAccId();
                taRsAccDtlTemp.setAccId(taRsAccDtlTemp.getRecvAccId());
                taRsAccDtlTemp.setRecvAccId(accId);
                taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                taAccDetlService.insertRecord(taRsAccDtlTemp);
            } else {
                logger.error("查不到该笔冲正的相关划拨信息，请确认输入的划拨申请编号");
                MessageUtil.addError("查不到该笔冲正的相关划拨信息，请确认输入的划拨申请编号");
                return;
            }

            // 往SBS发送记账信息
            taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlTemp);
            if(toaSbs!=null) {
                if(taRsAccDtlTemp != null) {
                    if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                        taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                        taAccDetlService.updateRecord(taRsAccDtlTemp);

                        // 往泰安房地产中心发送记账信息
                        TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                        BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlTemp);
                        taTxnFdcCanclSend.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
                        taFdcService.sendAndRecvRealTimeTxn9902211(taTxnFdcTemp);
                        /*划拨冲正后查询*/
                        taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
                    } else { // SBS记账失败的处理
                        taAccDetlService.deleteRecord(taRsAccDtlTemp.getPkId());
                    }
                }
            }
        }catch (Exception e){
            logger.error("返还冲正用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*画面查询用*/
    public void onBtnQueryClick() {
        taRsAccDtl.setTxCode(EnuTaFdcTxCode.TRADE_2201.getCode().substring(0,2));
        taRsAccDtlList = taAccDetlService.selectedRecordsByCondition(taRsAccDtl);
    }

    /*记账*/
    public void onClick_Enable(TaRsAccDtl taRsAccDtl){
        try {
            sendAndRecvSBSAndFDC(taRsAccDtl);
            onBtnQueryClick();
        } catch (Exception e) {
            logger.error("记账，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
    }

    public List<SelectItem> getActFlagList() {
        return actFlagList;
    }

    public void setActFlagList(List<SelectItem> actFlagList) {
        this.actFlagList = actFlagList;
    }

    public Map<String, String> getActFlagMap() {
        return actFlagMap;
    }

    public void setActFlagMap(Map<String, String> actFlagMap) {
        this.actFlagMap = actFlagMap;
    }

    public TaRsAccDtl getTaRsAccDtl() {
        return taRsAccDtl;
    }

    public void setTaRsAccDtl(TaRsAccDtl taRsAccDtl) {
        this.taRsAccDtl = taRsAccDtl;
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

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
    }
}
