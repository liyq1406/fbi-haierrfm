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
import pub.platform.advance.utils.RfmMessage;
import pub.platform.system.manage.dao.PtOperBean;
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
public class TaPayoutAction {
    private static final Logger logger = LoggerFactory.getLogger(TaPayoutAction.class);

    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @ManagedProperty(value = "#{taFdcService}")
    private TaFdcService taFdcService;

    @ManagedProperty(value = "#{taSbsService}")
    private TaSbsService taSbsService;

    private String isDebugExec;

    private EnuTaTxnRtnCode enuTaTxnRtnCode = EnuTaTxnRtnCode.TXN_PROCESSED;

    // 冲正标志
    private EnuActCanclFlag enuActCanclFlag = EnuActCanclFlag.ACT_CANCL0;

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
    }

    /*划拨验证用*/
    public void onBtnValiClick() {
        // 发送验证信息
        taTxnFdcValiSend.setTxCode(EnuTaFdcTxCode.TRADE_2101.getCode());
        taTxnFdcValiSend.setPassword(MD5Helper.getMD5String(taTxnFdcValiSend.getPassword()));
        taFdcService.sendAndRecvRealTimeTxn9902101(taTxnFdcValiSend);
        /*验证后查询*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
        if(taTxnFdcValiSendAndRecv == null) {
            MessageUtil.addError(RfmMessage.getProperty("TransferVerification.E001"));
        }
    }

    /*验证后立即划拨记账用*/
    public void onBtnActClick() {
        try {
            if(taTxnFdcValiSendAndRecv.getReturnCode() == null ||
                    !EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(taTxnFdcValiSendAndRecv.getReturnCode())||
                    StringUtils.isEmpty(taTxnFdcValiSendAndRecv.getSpvsnAccId())) {
                MessageUtil.addError(RfmMessage.getProperty("TransferVerification.E004"));
                return;
            }
            // 验证重复记账
            TaRsAccDtl taRsAccDtlTempQry = new TaRsAccDtl();
            taRsAccDtlTempQry.setBizId(taTxnFdcValiSendAndRecv.getBizId());
            taRsAccDtlTempQry.setTxCode(EnuTaFdcTxCode.TRADE_2102.getCode());
            taRsAccDtlTempQry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // 未冲正
            List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtlTempQry);
            if(taRsAccDtlListQry.size() == 1){
                String actFlag = taRsAccDtlListQry.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("TransferVerification.E002"));
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("TransferVerification.E003"));
                }
                return;
            }

            // 本地存取（对账用）
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            BeanUtils.copyProperties(taRsAccDtlTemp, taTxnFdcValiSendAndRecv);
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2102.getCode());
            taRsAccDtlTemp.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG0.getCode());
            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
            taRsAccDtlTemp.setStlType(EnuTaStlType.STL_TYPE02.getCode());             // 结算方式
            taRsAccDtlTemp.setCheckId("");                                             // 支票号码

            taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taRsAccDtlTemp.setTxDate(ToolUtil.getStrLastUpdDate());
            PtOperBean ptOperBeanTemp=ToolUtil.getOperatorManager().getOperator();
            taRsAccDtlTemp.setBranchId(ptOperBeanTemp.getDeptid());
            taRsAccDtlTemp.setUserId(ptOperBeanTemp.getOperid());
            taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
            taRsAccDtlTemp.setCreatedBy(taRsAccDtlTemp.getUserId());

            taAccDetlService.insertRecord(taRsAccDtlTemp);

            // 往SBS发送记账信息
            sendAndRecvSBSAndFDC(taRsAccDtlTemp);
        }catch (Exception e){
            logger.error("验证后立即划拨记账用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*划拨冲正用*/
    public void onBtnCanclClick() {
        try {
            // 验证重复冲正
            TaRsAccDtl taRsAccDtl2111Qry = new TaRsAccDtl();
            taRsAccDtl2111Qry.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtl2111Qry.setTxCode(EnuTaFdcTxCode.TRADE_2111.getCode());
            taRsAccDtl2111Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // 已冲正
            List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2111Qry);
            if(taRsAccDtlListQry.size() == 1){
                String actFlag = taRsAccDtlListQry.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("TransferCorrection.E001"));
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("TransferCorrection.E002"));
                }
                return;
            }

            // 本地存取（对账用）
            TaRsAccDtl taRsAccDtl2102Qry = new TaRsAccDtl();
            taRsAccDtl2102Qry.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtl2102Qry.setTxCode(EnuTaFdcTxCode.TRADE_2102.getCode());
            taRsAccDtl2102Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // 未冲正
            taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2102Qry);
            if(taRsAccDtlListQry.size() == 1){
                TaRsAccDtl taRsAccDtlTemp = taRsAccDtlListQry.get(0);
                // 与划拨记账：收款账号和付款账号关系正好颠倒
                taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2111.getCode());
                taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
                taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());

                taAccDetlService.insertRecord(taRsAccDtlTemp);
                // 往SBS和FDC发送记账信息
                sendAndRecvSBSAndFDC(taRsAccDtlTemp);
            } else {
                logger.error(RfmMessage.getProperty("TransferCorrection.E001"));
                MessageUtil.addError(RfmMessage.getProperty("TransferCorrection.E003"));
            }
        }catch (Exception e){
            logger.error("划拨冲正用，", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*画面查询用*/
    public void onBtnQueryClick() {
        taRsAccDtl.setTxCode(EnuTaFdcTxCode.TRADE_2101.getCode().substring(0,2));
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

    /*验证后立即划拨记账用*/
    public Boolean sendAndRecvSBSAndFDC(TaRsAccDtl taRsAccDtlPara) {
        try {
            // 往SBS发送记账信息
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlPara);
            if(toaSbs !=null) {
                if((EnuTaTxnRtnCode.TXN_PROCESSED.getCode()).equals(toaSbs.getHeader().RETURN_CODE)){ // SBS记账成功的处理
                    taRsAccDtlPara.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlPara);

                    // 往泰安房地产中心发送记账信息
                    TaTxnFdc taTxnFdcTemp=new TaTxnFdc();
                    BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlPara);
                    if(EnuTaFdcTxCode.TRADE_2102.getCode().equals(taTxnFdcTemp.getTxCode())){ // 划拨记账
                        taFdcService.sendAndRecvRealTimeTxn9902102(taTxnFdcTemp);
                        /*记账后查询*/
                        taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
                    }else if(EnuTaFdcTxCode.TRADE_2111.getCode().equals(taTxnFdcTemp.getTxCode())){ // 划拨冲正
                        taFdcService.sendAndRecvRealTimeTxn9902111(taTxnFdcTemp);
                        /*记账后查询*/
                        taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());

                        // 修改交存冲正的冲正标志
                        taRsAccDtlPara.setCanclFlag(EnuActCanclFlag.ACT_CANCL1.getCode());
                        taAccDetlService.updateRecord(taRsAccDtlPara);

                        // 修改划拨记账的冲正标志
                        TaRsAccDtl taRsAccDtl2102Qry = new TaRsAccDtl();
                        taRsAccDtl2102Qry.setBizId(taTxnFdcCanclSend.getBizId());
                        taRsAccDtl2102Qry.setTxCode(EnuTaFdcTxCode.TRADE_2102.getCode());
                        taRsAccDtl2102Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // 未冲正
                        List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2102Qry);
                        if(taRsAccDtlListQry.size() == 1) {
                            TaRsAccDtl taRsAccDtlTemp = taRsAccDtlListQry.get(0);
                            taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL1.getCode());
                            taAccDetlService.updateRecord(taRsAccDtlTemp);
                        }
                    }
                    MessageUtil.addInfo(toaSbs.getHeader().RETURN_MSG);
                } else { // SBS记账失败的处理
                    taAccDetlService.deleteRecord(taRsAccDtlPara.getPkId());
                    MessageUtil.addError(toaSbs.getHeader().RETURN_MSG);
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            logger.error("验证后立即划拨记账用，", e);
            MessageUtil.addError(e.getMessage());
            return false;
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public EnuTaTxnRtnCode getEnuTaTxnRtnCode() {
        return enuTaTxnRtnCode;
    }

    public EnuActCanclFlag getEnuActCanclFlag() {
        return enuActCanclFlag;
    }

    public void setEnuActCanclFlag(EnuActCanclFlag enuActCanclFlag) {
        this.enuActCanclFlag = enuActCanclFlag;
    }

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

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
    }

    public String getIsDebugExec() {
        return isDebugExec=PropertyManager.getProperty("isDebugExec");
    }
}
