package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.acc.TaAccService;
import rfm.ta.service.biz.his.TaTxnFdcService;

/**����SBS���˽���
 * Created by Thinkpad on 2015/8/18.
 */
@Service
public class TaFdcNotRecvRtnService {
    private static final Logger logger = LoggerFactory.getLogger(TaFdcNotRecvRtnService.class);

    @Autowired
    private TaAccService taAccService;

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    // �������
    /**
     * ����̩���������ϵͳ������ܽ���
     *
     * @param taRsAccPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9901001(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901001 tia9901001Temp=new Tia9901001();
            tia9901001Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901001Temp.header.TX_CODE= EnuTaFdcTxCode.TRADE_1001.getCode();                 // 01   ���״���       4   1001
            tia9901001Temp.body.SPVSN_BANK_ID= EnuTaBankId.BANK_HAIER.getCode();               // 02   ������д���   2
            tia9901001Temp.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();                      // 03   ���д���       6
            tia9901001Temp.header.BIZ_ID=taRsAccPara.getBizId();                                  // 04   ���������   14
            tia9901001Temp.body.SPVSN_ACC_TYPE=taRsAccPara.getSpvsnAccType();                    // 05   �ʻ����       1   0��Ԥ�ۼ�ܻ�
            tia9901001Temp.body.SPVSN_ACC_ID=taRsAccPara.getSpvsnAccId();                        // 06   ���ר���˺�    30
            tia9901001Temp.body.SPVSN_ACC_NAME=taRsAccPara.getSpvsnAccName();                    // 07   ���ר������   150
            tia9901001Temp.header.REQ_SN=taRsAccPara.getReqSn();                                  // 08   ��ˮ��         30
            tia9901001Temp.body.TX_DATE=ToolUtil.getStrLastUpdDate() ;                            // 09   ����           10  ��ϵͳ���ڼ���
            tia9901001Temp.body.BRANCH_ID=ToolUtil.getOperatorManager().getOperator().getDeptid();// 10   �����         30
            tia9901001Temp.header.USER_ID=ToolUtil.getOperatorManager().getOperatorId();          // 11   ��Ա��         30
            tia9901001Temp.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode() ;                 // 12   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9901001Temp);
            Toa9901001 toaPara=(Toa9901001) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                taRsAccPara.setPreSalePerName(toaPara.body.PRE_SALE_PER_NAME);
                taRsAccPara.setPreSaleProAddr(toaPara.body.PRE_SALE_PRO_ADDR);
                taRsAccPara.setPreSaleProName(toaPara.body.PRE_SALE_PRO_NAME);
                taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_SUPV.getCode());
                taAccService.updateRecord(taRsAccPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
    }

    // ������
    /**
     * ����̩���������ϵͳ�����ܽ���
     *
     * @param taRsAccPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9901002(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901002 tia9901002Temp=new Tia9901002() ;
            tia9901002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901002Temp.header.TX_CODE= EnuTaFdcTxCode.TRADE_1002.getCode();                // 01   ���״���       4   1002
            tia9901002Temp.body.SPVSN_BANK_ID= EnuTaBankId.BANK_HAIER.getCode();               // 02   ������д���   2
            tia9901002Temp.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();                      // 03   ���д���       6
            tia9901002Temp.header.BIZ_ID=taRsAccPara.getBizId();                                  // 04   ��ֹ֤�����  14
            tia9901002Temp.body.SPVSN_ACC_ID=taRsAccPara.getSpvsnAccId();                        // 05   ���ר���˺�  30
            tia9901002Temp.body.SPVSN_ACC_NAME=taRsAccPara.getSpvsnAccName();                    // 06   ���ר������  150
            tia9901002Temp.header.REQ_SN=taRsAccPara.getReqSn();                                  // 07   ��ˮ��        30
            tia9901002Temp.body.TX_DATE=ToolUtil.getStrLastUpdDate() ;                            // 08   ����          10  ��ϵͳ���ڼ���
            tia9901002Temp.body.BRANCH_ID=ToolUtil.getOperatorManager().getOperator().getDeptid();// 09   �����        30
            tia9901002Temp.header.USER_ID=ToolUtil.getOperatorManager().getOperatorId();          // 10   ��Ա��        30
            tia9901002Temp.body.INITIATOR=EnuTaInitiatorId.INITIATOR.getCode() ;                 // 11   ����        1   1_�������
            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9901002Temp);
            Toa9901002 toaPara=(Toa9901002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_CANCL.getCode());
                taAccService.updateRecord(taRsAccPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taAccService.updateRecord(taRsAccPara);
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("������ʧ��", e);
            throw new RuntimeException("������ʧ��", e);
        }
    }

    // ���潻��
    /**
     * ����̩���������ϵͳ������֤����
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902001(TIA tiaPara) {
        try {
            Tia9902001 tia9902001Temp=(Tia9902001)tiaPara;
            TaTxnFdc taTxnFdcPara=new TaTxnFdc();
            taTxnFdcPara.setTxCode(tia9902001Temp.header.TX_CODE);     // 01   ���״���       4   2001
            taTxnFdcPara.setSpvsnBankId(tia9902001Temp.body.SPVSN_BANK_ID);       // 02   ������д���   2
            taTxnFdcPara.setCityId(tia9902001Temp.body.CITY_ID);       // 03   ���д���       6
            taTxnFdcPara.setBizId(tia9902001Temp.header.BIZ_ID);       // 04   ����������   14
            taTxnFdcPara.setReqSn(tia9902001Temp.header.REQ_SN);       // 05   ��֤��ˮ       30
            taTxnFdcPara.setTxDate(tia9902001Temp.body.TX_DATE);    // 06   ��֤����       10  ��ϵͳ���ڼ���
            taTxnFdcPara.setBranchId(tia9902001Temp.body.BRANCH_ID);   // 07   ��֤����     30
            taTxnFdcPara.setUserId(tia9902001Temp.header.USER_ID);     // 08   ��֤��Ա     30
            taTxnFdcPara.setInitiator(tia9902001Temp.body.INITIATOR);  // 09   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            String strMsgid= depMsgSendAndRecv.sendDepMessage(tiaPara);
            Toa9902001 toaPara=(Toa9902001) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	�ʻ����	            1   0����ܻ���
                  03	������	            20  �Է�Ϊ��λ
                  04	���ר���˺�            30
                  05    ���ר������            150
                  06    Ԥ���ʽ���ƽ̨��ˮ    16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setSpvsnAccType(toaPara.body.SPVSN_ACC_TYPE);
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT.trim());
                taTxnFdcPara.setSpvsnAccId(toaPara.body.SPVSN_ACC_ID);
                taTxnFdcPara.setSpvsnAccName(toaPara.body.SPVSN_ACC_NAME);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("������֤ʧ��", e);
            throw new RuntimeException("������֤ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ������˽���
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902002(TaTxnFdc taTxnFdcPara) {
        try {
            Tia9902002 tia9902002Temp=new Tia9902002() ;
            tia9902002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902002Temp.header.TX_CODE=taTxnFdcPara.getTxCode();              // 01   ���״���       4   2002
            tia9902002Temp.body.SPVSN_BANK_ID = taTxnFdcPara.getSpvsnBankId();         // 02   ������д���   2
            tia9902002Temp.body.CITY_ID = taTxnFdcPara.getCityId();                // 03   ���д���       6
            tia9902002Temp.header.BIZ_ID = taTxnFdcPara.getBizId();                // 04   ����������   14
            tia9902002Temp.body.TX_AMT = taTxnFdcPara.getTxAmt();                  // 08   �����ʽ�       20 ������֤�������
            tia9902002Temp.body.SPVSN_ACC_ID = taTxnFdcPara.getSpvsnAccId();      // 06   ����˺�       30 ������֤�������
            tia9902002Temp.body.STL_TYPE = taTxnFdcPara.getStlType();             // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            tia9902002Temp.body.CHECK_ID = taTxnFdcPara.getCheckId();             // 10   ֧Ʊ����       30
            tia9902002Temp.header.REQ_SN = taTxnFdcPara.getReqSn();               // 11   ���м�����ˮ�� 30
            tia9902002Temp.body.TX_DATE = taTxnFdcPara.getTxDate();               // 12   ��������       10  ��ϵͳ���ڼ���
            tia9902002Temp.body.BRANCH_ID = taTxnFdcPara.getBranchId();          // 13   ���������     30
            tia9902002Temp.header.USER_ID = taTxnFdcPara.getUserId();             // 14   ������Ա       30
            tia9902002Temp.body.INITIATOR = taTxnFdcPara.getInitiator();          // 15   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902002Temp);
            Toa9902002 toaPara=(Toa9902002) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ�����������
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902011(TaTxnFdc taTxnFdcPara) {
        try {
            Tia9902011 tia9902011Temp=new Tia9902011() ;
            tia9902011Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902011Temp.header.TX_CODE = taTxnFdcPara.getTxCode();        // 01   ���״���       4   2011
            tia9902011Temp.body.SPVSN_BANK_ID = taTxnFdcPara.getSpvsnBankId();   // 02   ������д���   2
            tia9902011Temp.body.CITY_ID = taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902011Temp.header.BIZ_ID = taTxnFdcPara.getBizId();          // 04   ����������   14
            tia9902011Temp.header.REQ_SN = taTxnFdcPara.getReqSn();          // 05   ���г�����ˮ   30
            tia9902011Temp.body.TX_DATE = taTxnFdcPara.getTxDate();          // 06   ��������       10  ��ϵͳ���ڼ���
            tia9902011Temp.body.BRANCH_ID = taTxnFdcPara.getBranchId();      // 07   ��������       30
            tia9902011Temp.header.USER_ID = taTxnFdcPara.getUserId();        // 08   ������Ա       30
            tia9902011Temp.body.INITIATOR = taTxnFdcPara.getInitiator();     // 09   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902011Temp);
            Toa9902011 toaPara=(Toa9902011) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
    }

    // ��������
    /**
     * ����̩���������ϵͳ������֤����
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902101(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902101 tia9902101Temp=new Tia9902101() ;
            tia9902101Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902101Temp.header.TX_CODE=taTxnFdcPara.getTxCode();          // 01   ���״���       4   2101
            tia9902101Temp.body.SPVSN_BANK_ID=taTxnFdcPara.getSpvsnBankId();// 02   ������д���   2
            tia9902101Temp.body.CITY_ID=taTxnFdcPara.getCityId();            // 03   ���д���       6
            tia9902101Temp.header.BIZ_ID=taTxnFdcPara.getBizId();            // 04   ����ҵ����   14
            tia9902101Temp.header.PASSWORD=taTxnFdcPara.getPassword();       // 05   ��������       32 MD5
            tia9902101Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 06   ��֤��ˮ       30
            tia9902101Temp.body.TX_DATE=taTxnFdcPara.getTxDate();            // 07   ��֤����       10  ��ϵͳ���ڼ���
            tia9902101Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 08   ��֤�����     30
            tia9902101Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 09   ��֤��Ա��     30
            tia9902101Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 10   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902101Temp);
            Toa9902101 toaPara=(Toa9902101) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	����˺�                30
                  03    ����˻�����            150
                  04	�������	            20  �Է�Ϊ��λ
                  05	�տ�����	            90
                  06	�տλ�˺�	        30
                  07	�տλ����	        150
                  08	��Ŀ����	            128
                  09	������ҵ����	        256
                  10    Ԥ���ʽ���ƽ̨��ˮ    16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setSpvsnAccId(toaPara.body.SPVSN_ACC_ID);
                taTxnFdcPara.setSpvsnAccName(toaPara.body.SPVSN_ACC_NAME);
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT);
                taTxnFdcPara.setGerlBank(toaPara.body.GERL_BANK);
                taTxnFdcPara.setGerlAccId(toaPara.body.GERL_ACC_ID);
                taTxnFdcPara.setGerlAccName(toaPara.body.GERL_ACC_NAME);
                taTxnFdcPara.setProgName(toaPara.body.PROG_NAME);
                taTxnFdcPara.setCompName(toaPara.body.COMP_NAME);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("������֤ʧ��", e);
            throw new RuntimeException("������֤ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ�������˽���
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902102(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902102 tia9902102Temp=new Tia9902102() ;
            tia9902102Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902102Temp.header.TX_CODE=taTxnFdcPara.getTxCode();              // 01   ���״���       4   2102
            tia9902102Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();   // 02   ������д���   2
            tia9902102Temp.body.CITY_ID= taTxnFdcPara.getCityId();               // 03   ���д���       6
            tia9902102Temp.header.BIZ_ID=taTxnFdcPara.getBizId();                // 04   ����ҵ����   14
            tia9902102Temp.header.PASSWORD=taTxnFdcPara.getPassword();           // 05   ��������       32 MD5
            tia9902102Temp.body.SPVSN_ACC_ID=taTxnFdcPara.getSpvsnAccId();       // 06   ����˺�       30 ������֤�������
            tia9902102Temp.body.GERL_ACC_ID=taTxnFdcPara.getGerlAccId();         // 07   �տλ�˺�   30 ������֤�������
            tia9902102Temp.body.TX_AMT=taTxnFdcPara.getTxAmt();                   // 08   �����ʽ�       20 ������֤�������
            tia9902102Temp.body.STL_TYPE=taTxnFdcPara.getStlType();               // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            tia9902102Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();               // 10   ֧Ʊ����       30
            tia9902102Temp.header.REQ_SN=taTxnFdcPara.getReqSn();                 // 11   ���м�����ˮ�� 30
            tia9902102Temp.body.TX_DATE=taTxnFdcPara.getTxDate();                 // 12   ��������       10  ��ϵͳ���ڼ���
            tia9902102Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();             // 13   ���������     30
            tia9902102Temp.header.USER_ID=taTxnFdcPara.getUserId();               // 14   ��Ա��         30
            tia9902102Temp.body.INITIATOR=taTxnFdcPara.getInitiator();            // 15   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902102Temp);
            Toa9902102 toaPara=(Toa9902102) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("��������ʧ��", e);
            throw new RuntimeException("��������ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ������������
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902111(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902111 tia9902111Temp=new Tia9902111() ;
            tia9902111Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902111Temp.header.TX_CODE=taTxnFdcPara.getTxCode();             // 01   ���״���       4   2102
            tia9902111Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();  // 02   ������д���   2
            tia9902111Temp.body.CITY_ID= taTxnFdcPara.getCityId();              // 03   ���д���       6
            tia9902111Temp.header.BIZ_ID=taTxnFdcPara.getBizId();               // 04   ����ҵ����   14
            tia9902111Temp.header.REQ_SN=taTxnFdcPara.getReqSn();               // 05   ���м�����ˮ�� 30
            tia9902111Temp.body.TX_DATE=taTxnFdcPara.getTxDate();               // 06   ��������       10  ��ϵͳ���ڼ���
            tia9902111Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();           // 07   ���������     30
            tia9902111Temp.header.USER_ID=taTxnFdcPara.getUserId();             // 08   ��Ա��         30
            tia9902111Temp.body.INITIATOR=taTxnFdcPara.getInitiator();          // 09   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902111Temp);
            Toa9902111 toaPara=(Toa9902111) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("��������ʧ��", e);
            throw new RuntimeException("��������ʧ��", e);
        }
    }

    // ��������
    /**
     * ����̩���������ϵͳ������֤����
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902201(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902201 tia9902201Temp=new Tia9902201() ;
            tia9902201Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902201Temp.header.TX_CODE=taTxnFdcPara.getTxCode();          // 01   ���״���       4   2201
            tia9902201Temp.body.SPVSN_BANK_ID=taTxnFdcPara.getSpvsnBankId();// 02   ������д���   2
            tia9902201Temp.body.CITY_ID=taTxnFdcPara.getCityId();            // 03   ���д���       6
            tia9902201Temp.header.BIZ_ID=taTxnFdcPara.getBizId();            // 04   ����ҵ����   14
            tia9902201Temp.header.PASSWORD=taTxnFdcPara.getPassword();       // 05   ��������       32 MD5
            tia9902201Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 06   ��֤��ˮ       30
            tia9902201Temp.body.TX_DATE=taTxnFdcPara.getTxDate();            // 07   ��֤����       10  ��ϵͳ���ڼ���
            tia9902201Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 08   ��֤�����     30
            tia9902201Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 09   ��֤��Ա��     30
            tia9902201Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 10   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902201Temp);
            Toa9902201 toaPara=(Toa9902201) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                 /*01	���	                4   0000��ʾ�ɹ�
                  02	����˺�                30
                  03    ����˻�����            150
                  04	�������	            20  �Է�Ϊ��λ
                  05	ҵ������	            80
                  06	֤������    	        30
                  07	֤������                255
                  08    Ԥ���ʽ���ƽ̨��ˮ    16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setSpvsnAccId(toaPara.body.SPVSN_ACC_ID);
                taTxnFdcPara.setSpvsnAccName(toaPara.body.SPVSN_ACC_NAME);
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT);
                taTxnFdcPara.setOwnerName(toaPara.body.OWNER_NAME);
                taTxnFdcPara.setCtficType(toaPara.body.CTFIC_TYPE);
                taTxnFdcPara.setCtficId(toaPara.body.CTFIC_ID);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("������֤ʧ��", e);
            throw new RuntimeException("������֤ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ�������˽��ף�TA_FDC)
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902202(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902202 tia9902202Temp=new Tia9902202() ;
            tia9902202Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902202Temp.header.TX_CODE=taTxnFdcPara.getTxCode();            // 01   ���״���       4   2202
            tia9902202Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId(); // 02   ������д���   2
            tia9902202Temp.body.CITY_ID= taTxnFdcPara.getCityId();             // 03   ���д���       6
            tia9902202Temp.header.BIZ_ID=taTxnFdcPara.getBizId();              // 04   ����ҵ����   14
            tia9902202Temp.header.PASSWORD=taTxnFdcPara.getPassword();         // 05   ��������       32 MD5
            tia9902202Temp.body.SPVSN_ACC_ID=taTxnFdcPara.getSpvsnAccId();     // 06   ����˺�       30 ������֤�������
            tia9902202Temp.body.TX_AMT=taTxnFdcPara.getTxAmt();                 // 07   �����ʽ�       20 ������֤�������
            tia9902202Temp.header.REQ_SN=taTxnFdcPara.getReqSn();               // 08   ���м�����ˮ�� 30
            tia9902202Temp.body.TX_DATE=taTxnFdcPara.getTxDate();               // 09   ��������       10  ��ϵͳ���ڼ���
            tia9902202Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();           // 10   ���������     30
            tia9902202Temp.header.USER_ID=taTxnFdcPara.getUserId();             // 11   ��Ա��         30
            tia9902202Temp.body.INITIATOR=taTxnFdcPara.getInitiator();          // 12   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902202Temp);
            Toa9902202 toaPara=(Toa9902202) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("��������ʧ��", e);
            throw new RuntimeException("��������ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ������������
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902211(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902211 tia9902211Temp=new Tia9902211() ;
            tia9902211Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902211Temp.header.TX_CODE=taTxnFdcPara.getTxCode();             // 01   ���״���       4   2202
            tia9902211Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();  // 02   ������д���   2
            tia9902211Temp.body.CITY_ID= taTxnFdcPara.getCityId();              // 03   ���д���       6
            tia9902211Temp.header.BIZ_ID=taTxnFdcPara.getBizId();               // 04   ����ҵ����   14
            tia9902211Temp.header.REQ_SN=taTxnFdcPara.getReqSn();               // 05  ���г�����ˮ��  30
            tia9902211Temp.body.TX_DATE=taTxnFdcPara.getTxDate();               // 06  ��������        10  ��ϵͳ���ڼ���
            tia9902211Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();           // 07  ���������      30
            tia9902211Temp.header.USER_ID=taTxnFdcPara.getUserId();             // 08  ��Ա��          30
            tia9902211Temp.body.INITIATOR=taTxnFdcPara.getInitiator();          // 09  ����          1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902211Temp);
            Toa9902211 toaPara=(Toa9902211) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("��������ʧ��", e);
            throw new RuntimeException("��������ʧ��", e);
        }
    }

    // ���˽����ѯ
    /**
     * ���˽����ѯ
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902501(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902501 tia9902501Temp=new Tia9902501() ;
            tia9902501Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902501Temp.header.TX_CODE=taTxnFdcPara.getTxCode();           // 01   ���״���       4   2501
            tia9902501Temp.body.SPVSN_BANK_ID=taTxnFdcPara.getSpvsnBankId(); // 02   ������д���   2
            tia9902501Temp.body.CITY_ID=taTxnFdcPara.getCityId();             // 03   ���д���       6
            tia9902501Temp.header.BIZ_ID=taTxnFdcPara.getBizId();             // 04   ҵ����       14
            tia9902501Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();         // 05   ��ѯ����       30
            tia9902501Temp.header.USER_ID=taTxnFdcPara.getUserId();           // 06   ��ѯ��Ա       30
            tia9902501Temp.body.INITIATOR=taTxnFdcPara.getInitiator();        // 07   ����         1   1_�������

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902501Temp);
            Toa9902501 toaPara=(Toa9902501) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                  /*  01    ���	                  4   0000��ʾ�ɹ�
                      02    ���˽��	              1   0_�ɹ� 1_ʧ��
                      03    Ԥ���ʽ���ƽ̨������ˮ  16  ҵ�������ˮ
                      04    ������м�����ˮ	      30  ҵ�������ˮ
                      05    Ԥ���ʽ���ƽ̨��ˮ	  16
                  */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setActRstl(toaPara.body.SPVSN_ACT_RSTL);
                taTxnFdcPara.setFdcActSn(toaPara.body.FDC_ACT_SN);
                taTxnFdcPara.setFdcBankActSn(toaPara.body.FDC_BANK_ACT_SN);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("������֤ʧ��", e);
            throw new RuntimeException("������֤ʧ��", e);
        }
    }
}
