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
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaRefundAction {
    private static final Logger logger = LoggerFactory.getLogger(TaRefundAction.class);
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taFdcService}")
    private TaFdcService taFdcService;

    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @ManagedProperty(value = "#{taSbsService}")
    private TaSbsService taSbsService;

    private EnuTaTxnRtnCode enuTaTxnRtnCode = EnuTaTxnRtnCode.TXN_PROCESSED;

    // ������־
    private EnuActCanclFlag enuActCanclFlag = EnuActCanclFlag.ACT_CANCL0;

    // ���˳ɹ���־
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

    private String isDebugExec;

    @PostConstruct
    public void init() {
        // ��ѯ�ó�ʼ��
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

    /*������֤��*/
    public void onBtnValiClick() {
        // ������֤��Ϣ
        taTxnFdcValiSend.setTxCode(EnuTaFdcTxCode.TRADE_2201.getCode());
        taTxnFdcValiSend.setPassword(MD5Helper.getMD5String(taTxnFdcValiSend.getPassword()));
        taFdcService.sendAndRecvRealTimeTxn9902201(taTxnFdcValiSend);
        /*��֤���ѯ*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*��֤����������������*/
    public void onBtnActClick() {
        try {
            if(StringUtils.isEmpty(taTxnFdcValiSendAndRecv.getGerlAccId())){
                MessageUtil.addError(RfmMessage.getProperty("ReturnVerification.E001"));
                return;
            }

            if(taTxnFdcValiSendAndRecv.getReturnCode() == null ||
                    !taTxnFdcValiSendAndRecv.getReturnCode().equals(taTxnFdcValiSendAndRecv.getReturnCode())||
                    StringUtils.isEmpty(taTxnFdcValiSendAndRecv.getSpvsnAccId())) {
                MessageUtil.addError(RfmMessage.getProperty("ReturnVerification.E002"));
                return;
            }

            // ��֤�ظ�����
            TaRsAccDtl taRsAccDtl = new TaRsAccDtl();
            taRsAccDtl.setBizId(taTxnFdcValiSendAndRecv.getBizId());
            taRsAccDtl.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtl.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // δ����
            List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl);
            if(taRsAccDtlListQry.size() == 1){
                String actFlag = taRsAccDtlListQry.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("ReturnVerification.E003"));
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("ReturnVerification.E004"));
                }
                return;
            }

            // ���ش�ȡ�������ã�
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            BeanUtils.copyProperties(taRsAccDtlTemp, taTxnFdcValiSendAndRecv);
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtlTemp.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG0.getCode());
            taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());        // δ����
            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
            taRsAccDtlTemp.setStlType(EnuTaStlType.STL_TYPE02.getCode());             // ���㷽ʽ
            taRsAccDtlTemp.setCheckId("");                                             // ֧Ʊ����

            taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taRsAccDtlTemp.setTxDate(ToolUtil.getStrLastUpdDate());
            taRsAccDtlTemp.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taRsAccDtlTemp.setUserId(ToolUtil.getOperatorManager().getOperatorId());

            taRsAccDtlTemp.setCreatedBy(taRsAccDtlTemp.getUserId());

            taAccDetlService.insertRecord(taRsAccDtlTemp);

            // ��SBS��FDC���ͼ�����Ϣ
            sendAndRecvSBSAndFDC(taRsAccDtlTemp);
        }catch (Exception e){
            logger.error("��֤���������������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*��֤����������������*/
    public Boolean sendAndRecvSBSAndFDC(TaRsAccDtl taRsAccDtlPara) {
        try {
            // ��SBS���ͼ�����Ϣ
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlPara);
            if(toaSbs!=null) {
                if((EnuTaTxnRtnCode.TXN_PROCESSED.getCode()).equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
                    taRsAccDtlPara.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlPara);

                    // ��̩�����ز����ķ��ͼ�����Ϣ
                    TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                    BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlPara);
                    if(EnuTaFdcTxCode.TRADE_2202.getCode().equals(taTxnFdcTemp.getTxCode())){ // ��������
                        taFdcService.sendAndRecvRealTimeTxn9902202(taTxnFdcTemp);
                        /*���˺��ѯ*/
                        taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
                    }else if(EnuTaFdcTxCode.TRADE_2211.getCode().equals(taTxnFdcTemp.getTxCode())){ // ��������
                        taFdcService.sendAndRecvRealTimeTxn9902211(taTxnFdcTemp);
                        /*���˺��ѯ*/
                        taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());

                        // �޸Ľ�������ĳ�����־
                        taRsAccDtlPara.setCanclFlag(EnuActCanclFlag.ACT_CANCL1.getCode());
                        taAccDetlService.updateRecord(taRsAccDtlPara);

                        // �޸ķ������˵ĳ�����־
                        TaRsAccDtl taRsAccDtl2202Qry = new TaRsAccDtl();
                        taRsAccDtl2202Qry.setBizId(taTxnFdcCanclSend.getBizId());
                        taRsAccDtl2202Qry.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
                        taRsAccDtl2202Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // δ����
                        List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2202Qry);
                        if(taRsAccDtlListQry.size() == 1) {
                            TaRsAccDtl taRsAccDtlTemp = taRsAccDtlListQry.get(0);
                            taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL1.getCode());
                            taAccDetlService.updateRecord(taRsAccDtlTemp);
                        }
                    }

                    MessageUtil.addInfo(toaSbs.getHeader().RETURN_MSG);
                } else { // SBS����ʧ�ܵĴ���
                    taAccDetlService.deleteRecord(taRsAccDtlPara.getPkId());
                    MessageUtil.addInfo(toaSbs.getHeader().RETURN_MSG);
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            logger.error("��֤���������������ã�", e);
            MessageUtil.addError(e.getMessage());
            return false;
        }
    }

    /*����������*/
    public void onBtnCanclClick() {
        try {
            // ��֤�ظ�����
            TaRsAccDtl taRsAccDtl2211Qry = new TaRsAccDtl();
            taRsAccDtl2211Qry.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtl2211Qry.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
            taRsAccDtl2211Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
            List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2211Qry);
            if(taRsAccDtlListQry.size() == 1){
                String actFlag = taRsAccDtlListQry.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("ReturnCorrection.E001"));
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError(RfmMessage.getProperty("ReturnCorrection.E002"));
                }
                return;
            }

            // ���ش�ȡ�������ã�
            TaRsAccDtl taRsAccDtl2202Qry = new TaRsAccDtl();
            taRsAccDtl2202Qry.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtl2202Qry.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtl2202Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
            taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2202Qry);
            if(taRsAccDtlListQry.size() == 1){
                TaRsAccDtl taRsAccDtlTemp = taRsAccDtlListQry.get(0);
                // �뷵�����ˣ��տ��˺ź͸����˺Ź�ϵ���õߵ�
                taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
                taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
                taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());

                taAccDetlService.insertRecord(taRsAccDtlTemp);
                // ��SBS��FDC���ͼ�����Ϣ
                sendAndRecvSBSAndFDC(taRsAccDtlTemp);
            } else {
                logger.error(RfmMessage.getProperty("ReturnCorrection.E003"));
                MessageUtil.addError(RfmMessage.getProperty("ReturnCorrection.E003"));
            }
        }catch (Exception e){
            logger.error("���������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ѯ��*/
    public void onBtnQueryClick() {
        taRsAccDtl.setTxCode(EnuTaFdcTxCode.TRADE_2201.getCode().substring(0,2));
        taRsAccDtlList = taAccDetlService.selectedRecordsByCondition(taRsAccDtl);
    }

    /*����*/
    public void onClick_Enable(TaRsAccDtl taRsAccDtl){
        try {
            sendAndRecvSBSAndFDC(taRsAccDtl);
            onBtnQueryClick();
        } catch (Exception e) {
            logger.error("���ˣ�", e);
            MessageUtil.addError(e.getMessage());
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

    public String getIsDebugExec() {
        return isDebugExec=PropertyManager.getProperty("isDebugExec");
    }

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
    }
}
