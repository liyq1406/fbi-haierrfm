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
 * Time: ����2:12
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

    private String strVisableByExecType;

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
        if(EnuExecType.EXEC_TYPE_DEBUG.getCode().equals(EXEC_TYPE)){
            strVisableByExecType="true";
        }else{
            strVisableByExecType="false";
        }
    }

    /*������֤��*/
    public void onBtnValiClick() {
        // ������֤��Ϣ
        taTxnFdcValiSend.setTxCode(EnuTaFdcTxCode.TRADE_2201.getCode());
        taFdcService.sendAndRecvRealTimeTxn9902201(taTxnFdcValiSend);
        /*��֤���ѯ*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*��֤����������������*/
    public void onBtnActClick() {
        try {
            if(StringUtils.isEmpty(taTxnFdcValiSendAndRecv.getRecvAccId())){
                MessageUtil.addError("�������뷵���˺ţ�");
                return;
            }

            // ��֤�ظ�����
            TaRsAccDtl taRsAccDtl = new TaRsAccDtl();
            taRsAccDtl.setBizId(taTxnFdcValiSendAndRecv.getBizId());
            taRsAccDtl.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtl);
            if(taRsAccDtlList.size() == 1){
                String actFlag = taRsAccDtlList.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError("�÷����������Ѽ��ˣ��������ظ����ˣ�");
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError("�÷��������ż���ʱ���ڲ���ԭ��ʧ�ܣ����ڷ�����ѯ������м��ˣ�");
                }
                return;
            }

            // ���ش�ȡ�������ã�
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            BeanUtils.copyProperties(taRsAccDtlTemp, taTxnFdcValiSendAndRecv);
            taRsAccDtlTemp.setReqSn(ToolUtil.getStrReqSn_Back());
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtlTemp.setDeletedFlag(EnuDelFlag.DEL_FALSE.getCode());
            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
            taAccDetlService.insertRecord(taRsAccDtlTemp);

            // ��SBS��FDC���ͼ�����Ϣ
            if(sendAndRecvSBSAndFDC(taRsAccDtlTemp)) {
                MessageUtil.addInfo("�������˳ɹ���");
            }else{
                MessageUtil.addInfo("��������ʧ�ܣ�");
            }
        }catch (Exception e){
            logger.error("��֤���������������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*��֤����������������*/
    public Boolean sendAndRecvSBSAndFDC(TaRsAccDtl taRsAccDtlPara) {
        try {
            // ��SBS���ͼ�����Ϣ
            taRsAccDtlPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taRsAccDtlPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taRsAccDtlPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taRsAccDtlPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taRsAccDtlPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlPara);
            if(toaSbs!=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
                    taRsAccDtlPara.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlPara);

                    // ��̩�����ز����ķ��ͼ�����Ϣ
                    TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                    BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlPara);
                    taTxnFdcTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
                    taFdcService.sendAndRecvRealTimeTxn9902202(taTxnFdcTemp);
                    /*���˺��ѯ*/
                    taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
                } else { // SBS����ʧ�ܵĴ���
                    taAccDetlService.deleteRecord(taRsAccDtlPara.getPkId());
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
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            taRsAccDtlTemp.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
            List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTemp);
            if(taRsAccDtlList.size() == 1){
                String actFlag = taRsAccDtlList.get(0).getActFlag();
                if(actFlag.equals(EnuActFlag.ACT_SUCCESS.getCode())){
                    MessageUtil.addError("�÷����������ѳ������������ظ�������");
                } else if(actFlag.equals(EnuActFlag.ACT_UNKNOWN.getCode())){
                    MessageUtil.addError("�÷��������ų���ʱ���ڲ���ԭ��ʧ�ܣ����ڷ�����ѯ������г�����");
                }
                return;
            }

            // ���ش�ȡ�������ã�
            taRsAccDtlTemp = new TaRsAccDtl();
            taRsAccDtlTemp.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2202.getCode());
            taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTemp);
            taRsAccDtlTemp = null;
            if(taRsAccDtlList.size() == 1){
                taRsAccDtlTemp = taRsAccDtlList.get(0);
                // �뷵�����ˣ��տ��˺ź͸����˺Ź�ϵ���õߵ�
                taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
                String accId = taRsAccDtlTemp.getAccId();
                taRsAccDtlTemp.setAccId(taRsAccDtlTemp.getRecvAccId());
                taRsAccDtlTemp.setRecvAccId(accId);
                taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                taAccDetlService.insertRecord(taRsAccDtlTemp);
            } else {
                logger.error("�鲻���ñʳ�������ػ�����Ϣ����ȷ������Ļ���������");
                MessageUtil.addError("�鲻���ñʳ�������ػ�����Ϣ����ȷ������Ļ���������");
                return;
            }

            // ��SBS���ͼ�����Ϣ
            taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlTemp);
            if(toaSbs!=null) {
                if(taRsAccDtlTemp != null) {
                    if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
                        taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                        taAccDetlService.updateRecord(taRsAccDtlTemp);

                        // ��̩�����ز����ķ��ͼ�����Ϣ
                        TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                        BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlTemp);
                        taTxnFdcCanclSend.setTxCode(EnuTaFdcTxCode.TRADE_2211.getCode());
                        taFdcService.sendAndRecvRealTimeTxn9902211(taTxnFdcTemp);
                        /*�����������ѯ*/
                        taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
                    } else { // SBS����ʧ�ܵĴ���
                        taAccDetlService.deleteRecord(taRsAccDtlTemp.getPkId());
                    }
                }
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
