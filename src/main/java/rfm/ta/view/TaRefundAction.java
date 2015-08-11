package rfm.ta.view;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
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
import rfm.ta.service.account.TaRefundService;
import rfm.ta.service.his.TaTxnFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
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

    @ManagedProperty(value = "#{taRefundService}")
    private TaRefundService taRefundService;

    @ManagedProperty(value = "#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

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
        taTxnFdcValiSend.setTxCode(EnuTaTxCode.TRADE_2201.getCode());
        taRefundService.sendAndRecvRealTimeTxn9902201(taTxnFdcValiSend);
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
            // ���ش�ȡ�������ã�
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            BeanUtils.copyProperties(taRsAccDtlTemp, taTxnFdcValiSendAndRecv);
            taRsAccDtlTemp.setDeletedFlag(EnuDelFlag.DEL_FALSE.getCode());
            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
            taAccDetlService.insertRecord(taRsAccDtlTemp);

            // ��SBS���ͼ�����Ϣ
            TOA toaSbs=taRefundService.sendAndRecvRealTimeTxn900012202(taTxnFdcValiSendAndRecv);
            if(toaSbs!=null) {
                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
                    taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlTemp);
                } else { // SBS����ʧ�ܵĴ���
                    taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_FAIL.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlTemp);
                }

                // ��̩�����ز����ķ��ͼ�����Ϣ
                TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                BeanUtils.copyProperties(taTxnFdcTemp, taTxnFdcValiSendAndRecv);
                taTxnFdcTemp.setTxCode(EnuTaTxCode.TRADE_2202.getCode());
                taRefundService.sendAndRecvRealTimeTxn9902202(taTxnFdcTemp);
            /*���˺��ѯ*/
                taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
            }
        }catch (Exception e){
            logger.error("��֤���������������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*����������*/
    public void onBtnCanclClick() {
        try {
            // ���ش�ȡ�������ã�
            TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
            taRsAccDtlTemp.setBizId(taTxnFdcCanclSend.getBizId());
            taRsAccDtlTemp.setTxCode(EnuTaTxCode.TRADE_2201.getCode());
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
                logger.error("�鲻���ñʳ�������ػ�����Ϣ����ȷ������Ļ���������");
                MessageUtil.addError("�鲻���ñʳ�������ػ�����Ϣ����ȷ������Ļ���������");
            }

            // ��SBS���ͼ�����Ϣ
            TOA toaSbs=taRefundService.sendAndRecvRealTimeTxn900012211(taTxnFdcValiSendAndRecv);
            if(toaSbs!=null) {
                if(taRsAccDtl != null) {
                    if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
                        taRsAccDtl.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                        taAccDetlService.updateRecord(taRsAccDtl);
                    } else { // SBS����ʧ�ܵĴ���
                        taRsAccDtl.setActFlag(EnuActFlag.ACT_FAIL.getCode());
                        taAccDetlService.updateRecord(taRsAccDtl);
                    }
                }

                // ��̩�����ز����ķ��ͼ�����Ϣ
                TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                BeanUtils.copyProperties(taTxnFdcTemp, taTxnFdcCanclSend);
                taTxnFdcCanclSend.setTxCode(EnuTaTxCode.TRADE_2211.getCode());
                taRefundService.sendAndRecvRealTimeTxn9902211(taTxnFdcTemp);
                /*�����������ѯ*/
                taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
            }
        }catch (Exception e){
            logger.error("���������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*�����ѯ��*/
    public void onBtnQueryClick() {
        taRsAccDtlList = taAccDetlService.selectedRecordsByCondition(taRsAccDtl.getActFlag(),
                EnuTaTxCode.TRADE_2201.getCode().substring(0,2));
    }

    /*����*/
    public String onClick_Enable(TaRsAccDtl taRsAccDtl){
//        try {
//            taAccService.sendAndRecvRealTimeTxn9901001(taRsAccPara);
//        } catch (Exception e) {
//            logger.error("���ü��ʧ�ܣ�", e);
//            MessageUtil.addError(e.getMessage());
//            return null;
//        }
//        MessageUtil.addInfo("���ü�ܳɹ���");
//        confirmAccountNo = "";
        return null;
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
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
