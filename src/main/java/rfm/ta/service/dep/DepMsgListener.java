package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.RfmMessage;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.acc.TaAccDetlService;
import rfm.ta.service.biz.reconci.TaAKeyToReconciService;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.HashMap;
import java.util.List;

@Component
public class DepMsgListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DepMsgListener.class);
    @Autowired
    private JmsTemplate jmsRfmOutTemplate;

    @Autowired
    private TaAKeyToReconciService taAKeyToReconciService;

    @Autowired
    private TaSbsService taSbsService;

    @Autowired
    private TaFdcService taFdcService;

    @Autowired
    private TaAccDetlService taAccDetlService;

    @Override
    public void onMessage(Message message) {
        try {
            String correlationID = message.getJMSCorrelationID();
            HashMap<String, String> propertyMap = new HashMap<String, String>();
            propertyMap.put("JMSX_CHANNELID", message.getStringProperty("JMSX_CHANNELID"));
            propertyMap.put("JMSX_APPID", message.getStringProperty("JMSX_APPID"));
            propertyMap.put("JMSX_SRCMSGFLAG", message.getStringProperty("JMSX_SRCMSGFLAG"));

            ObjectMessage objMsg = (ObjectMessage) message;

            // һ������
            if("1001".equals(objMsg.getStringProperty("JMSX_BIZID"))) {
                TiaXml9100001 tiaTmp = (TiaXml9100001) objMsg.getObject();
                TOA toaFdc;
                String txnCode = tiaTmp.INFO.getTXNCODE();
                String strRtnMsg = taAKeyToReconciService.aKeyToReconci();
                ToaXml9100001 toaTmp = new ToaXml9100001();
                toaTmp.getINFO().setREQSN(ToolUtil.getStrAppReqSn_Back());
                toaTmp.getINFO().setRTNCODE("0000");
                toaTmp.getINFO().setRTNMSG(strRtnMsg);
                toaTmp.getINFO().setTXNCODE(txnCode);
                toaTmp.getINFO().setVERSION("");
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaTmp, correlationID, propertyMap));
            }if("1002".equals(objMsg.getStringProperty("JMSX_BIZID"))){
                TiaXml9100002 tiaTmp = (TiaXml9100002) objMsg.getObject();
                TOA toaFdc;
                String txnCode = tiaTmp.INFO.getTXNCODE();
                String strRtnMsg= taAKeyToReconciService.getCheckRslt();
                ToaXml9100002 toaTmp=new ToaXml9100002();
                toaTmp.getINFO().setREQSN(ToolUtil.getStrAppReqSn_Back());
                toaTmp.getINFO().setRTNCODE("0000");
                toaTmp.getINFO().setRTNMSG(strRtnMsg);
                toaTmp.getINFO().setTXNCODE(txnCode);
                toaTmp.getINFO().setVERSION("");
                jmsRfmOutTemplate.send(new ObjectMessageCreator(toaTmp, correlationID, propertyMap));
            }else {//������֤��������ˣ��������
                TIA tiaTmp = (TIA) objMsg.getObject();
                TOA toaFdc;
                String txnCode = tiaTmp.getHeader().TX_CODE;
                tiaTmp.getHeader().REQ_SN = ToolUtil.getStrAppReqSn_Back();// ��������������ˮ����32λUUID
                if (EnuTaFdcTxCode.TRADE_2001.getCode().equals(txnCode)) {
                /*01	���״���	    4	2001
                  02	������д���	2
                  03	���д���	    6
                  04	����������    14
                  05	��֤��ˮ    	30
                  06	��֤����	    10	��ϵͳ���ڼ���
                  07	��֤����	    30
                  08	��֤��Ա	    30
                  09	����	        1	1_�������*/
                    Tia9902001 tia9902001 = (Tia9902001) tiaTmp;
                    tia9902001.header.CHANNEL_ID = ToolUtil.DEP_CHANNEL_ID_RFM;
                    tia9902001.body.SPVSN_BANK_ID = EnuTaBankId.BANK_HAIER.getCode(); // 02   ������д���   2
                    tia9902001.body.CITY_ID = EnuTaCityId.CITY_TAIAN.getCode();        // 03   ���д���       6
                    tia9902001.body.TX_DATE = ToolUtil.getStrLastUpdDate();              // 06   ��֤����       10  ��ϵͳ���ڼ���
                    tia9902001.body.INITIATOR = EnuTaInitiatorId.INITIATOR.getCode();  // 09   ����         1   1_�������
                    toaFdc = taFdcService.sendAndRecvRealTimeTxn9902001(tia9902001);
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaFdc, correlationID, propertyMap));
                } else if (EnuTaFdcTxCode.TRADE_2002.getCode().equals(txnCode)) {// ����
                    Tia900010002 tia900010002Temp = (Tia900010002) tiaTmp;

                    // ��֤�ظ�����
                    TaRsAccDtl taRsAccDtlTempQry = new TaRsAccDtl();
                    taRsAccDtlTempQry.setBizId(tia900010002Temp.header.BIZ_ID);            // ҵ����
                    taRsAccDtlTempQry.setTxCode(tia900010002Temp.header.TX_CODE);          // ���׺�
                    taRsAccDtlTempQry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // δ����
                    List<TaRsAccDtl> taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlTempQry);
                    if (taRsAccDtlList.size() == 1) {
                        TaRsAccDtl taRsAccDtlValiExists = taRsAccDtlList.get(0);
                        if (taRsAccDtlValiExists.getActFlag().equals(EnuActFlag.ACT_SUCCESS.getCode())) {
                            Toa900010002 toa900010002 = new Toa900010002();
                            toa900010002.header.RETURN_CODE = "E001";
                            toa900010002.header.RETURN_MSG = RfmMessage.getProperty("Payment.E001");
                            toa900010002.header.REQ_SN = taRsAccDtlValiExists.getReqSn();
                            jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                        } else {
                            TaRsAccDtl taRsAccDtlTemp = taRsAccDtlList.get(0);
                            sendAndRecvSBSAndFDC(taRsAccDtlTemp, correlationID, propertyMap);
                        }
                    } else {
                        TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
                        taRsAccDtlTemp.setTxCode(tia900010002Temp.header.TX_CODE);                // ���״���
                        taRsAccDtlTemp.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());          // �������
                        taRsAccDtlTemp.setCityId(EnuTaCityId.CITY_TAIAN.getCode());               // ���д���
                        taRsAccDtlTemp.setBizId(tia900010002Temp.header.BIZ_ID);                  // ����������
                        taRsAccDtlTemp.setTxAmt(tia900010002Temp.body.TX_AMT);                    // ���׽��
                        taRsAccDtlTemp.setSpvsnAccId(tia900010002Temp.body.IN_ACC_ID);           // ����˺�
                        taRsAccDtlTemp.setGerlAccId(tia900010002Temp.body.OUT_ACC_ID);          // һ���˺�
                        taRsAccDtlTemp.setStlType(EnuTaStlType.STL_TYPE02.getCode());             // ���㷽ʽ
                        taRsAccDtlTemp.setCheckId("");                                             // ֧Ʊ����
                        String reqsn = tiaTmp.getHeader().REQ_SN;
                        if (reqsn == null) {
                            taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());                // ���м�����ˮ
                        } else if (reqsn.length() > 30) {
                            reqsn = reqsn.substring(0, 30);
                        }
                        taRsAccDtlTemp.setReqSn(reqsn);                                             // ���м�����ˮ
                        taRsAccDtlTemp.setTxDate(ToolUtil.getStrLastUpdDate());                     // ��������
                        taRsAccDtlTemp.setBranchId(tia900010002Temp.body.BANK_BRANCH_ID);        // ��������
                        taRsAccDtlTemp.setUserId(tia900010002Temp.header.USER_ID);                // ������Ա
                        taRsAccDtlTemp.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());         // ����

                        taRsAccDtlTemp.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG0.getCode());// ɾ����־
                        taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());              // ����״̬
                        taRsAccDtlTemp.setReturnCode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());
                        taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
                        taAccDetlService.insertRecord(taRsAccDtlTemp);
                        sendAndRecvSBSAndFDC(taRsAccDtlTemp, correlationID, propertyMap);
                    }
                } else if (EnuTaFdcTxCode.TRADE_2011.getCode().equals(txnCode)) {// ����
                    Tia900010002 tia900010002Temp = (Tia900010002) tiaTmp;

                    // ��֤�ظ�����
                    TaRsAccDtl taRsAccDtlQry = new TaRsAccDtl();
                    taRsAccDtlQry.setBizId(tia900010002Temp.header.BIZ_ID);            // ҵ����
                    taRsAccDtlQry.setTxCode(tia900010002Temp.header.TX_CODE);          // ���׺�
                    taRsAccDtlQry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // δ����
                    List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtlQry);
                    if (taRsAccDtlListQry.size() == 1) {
                        TaRsAccDtl taRsAccDtlValiExists = taRsAccDtlListQry.get(0);
                        if (taRsAccDtlValiExists.getActFlag().equals(EnuActFlag.ACT_SUCCESS.getCode())) { // �Ѿ������ɹ��Ĵ���
                            Toa900010002 toa900010002 = new Toa900010002();
                            toa900010002.header.RETURN_CODE = "E001";
                            toa900010002.header.RETURN_MSG = RfmMessage.getProperty("Payment.E001");
                            toa900010002.header.REQ_SN = taRsAccDtlValiExists.getReqSn();
                            jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                        } else { // �Ѿ��������Ǵ��ڲ���ԭ���ʧ�ܵĴ���
                            TaRsAccDtl taRsAccDtlTemp = taRsAccDtlListQry.get(0);
                            sendAndRecvSBSAndFDC(taRsAccDtlTemp, correlationID, propertyMap);
                        }
                    } else { // δ���й������Ĵ���
                        TaRsAccDtl taRsAccDtlTemp = new TaRsAccDtl();
                        taRsAccDtlTemp.setBizId(tia900010002Temp.header.BIZ_ID);
                        taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2002.getCode());
                        taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // δ����
                        taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtlTemp);
                        if (taRsAccDtlListQry.size() == 1) { // ���ڽ���Ĵ���
                            taRsAccDtlTemp = taRsAccDtlListQry.get(0);

                            // �뽻����ˣ��տ��˺ź͸����˺Ź�ϵ���õߵ�
                            taRsAccDtlTemp.setTxCode(EnuTaFdcTxCode.TRADE_2011.getCode());
                            taRsAccDtlTemp.setActFlag(EnuActFlag.ACT_UNKNOWN.getCode());
                            taRsAccDtlTemp.setReqSn(ToolUtil.getStrAppReqSn_Back());
                            taRsAccDtlTemp.setReturnCode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());
                            taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
                            taAccDetlService.insertRecord(taRsAccDtlTemp);
                            sendAndRecvSBSAndFDC(taRsAccDtlTemp, correlationID, propertyMap);
                        } else { // �����ڽ���Ĵ���
                            Toa900010002 toa900010002 = new Toa900010002();
                            toa900010002.header.RETURN_CODE = "E002";
                            toa900010002.header.RETURN_MSG = RfmMessage.getProperty("Payment.E002");
                            toa900010002.header.REQ_SN = ToolUtil.getStrAppReqSn_Back();
                            jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[DEP]��Ϣ�����쳣!", e);
        }
    }

    /*��֤���������������*/
    public Boolean sendAndRecvSBSAndFDC(TaRsAccDtl taRsAccDtlPara,String correlationID,HashMap<String, String> propertyMap) {
        try {
            // ��SBS���ͼ�����Ϣ            ;
            TOA toaSbs=taSbsService.sendAndRecvRealTimeTxn900010002(taRsAccDtlPara);
            if(toaSbs!=null) {
                if((EnuTaTxnRtnCode.TXN_PROCESSED.getCode()).equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
                    taRsAccDtlPara.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
                    taAccDetlService.updateRecord(taRsAccDtlPara);
                    TOA toaFdc;
                    // ��̩�����ز����ķ��ͼ�����Ϣ
                    TaTxnFdc taTxnFdcTemp=new TaTxnFdc();
                    BeanUtils.copyProperties(taTxnFdcTemp, taRsAccDtlPara);
                    if(taTxnFdcTemp.getTxCode().contains(EnuTaFdcTxCode.TRADE_2002.getCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902002(taTxnFdcTemp);
                        Toa900010002 toa900010002=new Toa900010002();
                        toa900010002.header.RETURN_CODE=toaFdc.getHeader().RETURN_CODE;
                        if(!EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900010002.header.RETURN_CODE)) {
                            toa900010002.header.RETURN_MSG = toaFdc.getHeader().RETURN_MSG;
                        }
                        toa900010002.header.REQ_SN=toaFdc.getHeader().REQ_SN;
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));
                    }else if(taTxnFdcTemp.getTxCode().contains(EnuTaFdcTxCode.TRADE_2011.getCode())){
                        toaFdc=taFdcService.sendAndRecvRealTimeTxn9902011(taTxnFdcTemp);
                        Toa900010002 toa900010002=new Toa900010002();
                        toa900010002.header.RETURN_CODE=toaFdc.getHeader().RETURN_CODE;
                        if(!EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900010002.header.RETURN_CODE)) {
                            toa900010002.header.RETURN_MSG = toaFdc.getHeader().RETURN_MSG;
                        }
                        toa900010002.header.REQ_SN=toaFdc.getHeader().REQ_SN;
                        jmsRfmOutTemplate.send(new ObjectMessageCreator(toa900010002, correlationID, propertyMap));

                        // �޸Ľ�������ĳ�����־
                        taRsAccDtlPara.setCanclFlag(EnuActCanclFlag.ACT_CANCL1.getCode());
                        taAccDetlService.updateRecord(taRsAccDtlPara);

                        // �޸Ľ�����˵ĳ�����־
                        TaRsAccDtl taRsAccDtl2002Qry = new TaRsAccDtl();
                        taRsAccDtl2002Qry.setBizId(taTxnFdcTemp.getBizId());
                        taRsAccDtl2002Qry.setTxCode(EnuTaFdcTxCode.TRADE_2002.getCode());
                        taRsAccDtl2002Qry.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());  // δ����
                        List<TaRsAccDtl> taRsAccDtlListQry = taAccDetlService.selectedRecords(taRsAccDtl2002Qry);
                        if(taRsAccDtlListQry.size() == 1) {
                            TaRsAccDtl taRsAccDtlTemp = taRsAccDtlListQry.get(0);
                            taRsAccDtlTemp.setCanclFlag(EnuActCanclFlag.ACT_CANCL1.getCode());
                            taAccDetlService.updateRecord(taRsAccDtlTemp);
                        }
                    }
                } else { // SBS����ʧ�ܵĴ���
                    logger.error("�����쳣:�����루" + toaSbs.getHeader().RETURN_CODE + ");������Ϣ��" + toaSbs.getHeader().RETURN_MSG + ")");
                    taAccDetlService.deleteRecord(taRsAccDtlPara.getPkId());
                    jmsRfmOutTemplate.send(new ObjectMessageCreator(toaSbs, correlationID, propertyMap));
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            logger.error("��֤��������������ã�", e);
            MessageUtil.addError(e.getMessage());
            return false;
        }
    }

    public TaAKeyToReconciService getTaAKeyToReconciService() {
        return taAKeyToReconciService;
    }

    public void setTaAKeyToReconciService(TaAKeyToReconciService taAKeyToReconciService) {
        this.taAKeyToReconciService = taAKeyToReconciService;
    }

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
    }

    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
    }

    public JmsTemplate getJmsRfmOutTemplate() {
        return jmsRfmOutTemplate;
    }

    public void setJmsRfmOutTemplate(JmsTemplate jmsRfmOutTemplate) {
        this.jmsRfmOutTemplate = jmsRfmOutTemplate;
    }

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }
}
