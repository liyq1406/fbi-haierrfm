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
import platform.auth.MD5Helper;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.acc.TaAccService;
import rfm.ta.service.biz.his.TaTxnFdcService;
import rfm.ta.service.biz.his.TaTxnSbsService;

/**����SBS���˽���
 * Created by Thinkpad on 2015/8/18.
 */
@Service
public class TaFdcService {
    private static final Logger logger = LoggerFactory.getLogger(TaFdcService.class);

    @Autowired
    private TaAccService taAccService;

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    // �������
    /**
     * ����̩���������ϵͳ������ܽ���
     *
     * @param taRsAccPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9901001(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901001 tia9901001Temp=new Tia9901001();
            tia9901001Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901001Temp.header.TX_CODE= EnuTaFdcTxCode.TRADE_1001.getCode();                     // 01   ���״���       4   1001
            tia9901001Temp.body.BANK_ID= EnuTaBankId.BANK_HAIER.getCode();                      // 02   ������д���   2
            tia9901001Temp.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();                      // 03   ���д���       6
            tia9901001Temp.header.BIZ_ID=taRsAccPara.getBizId();                                  // 04   ���������   14
            tia9901001Temp.body.ACC_TYPE=taRsAccPara.getAccType();                                // 05   �ʻ����       1   0��Ԥ�ۼ�ܻ�
            tia9901001Temp.body.ACC_ID=taRsAccPara.getAccId();                                    // 06   ���ר���˺�    30
            tia9901001Temp.body.ACC_NAME=taRsAccPara.getAccName();                                // 07   ���ר������   150
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
            }
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
    public void sendAndRecvRealTimeTxn9901002(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901002 tia9901002Temp=new Tia9901002() ;
            tia9901002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901002Temp.header.TX_CODE= EnuTaFdcTxCode.TRADE_1002.getCode();                     // 01   ���״���       4   1002
            tia9901002Temp.body.BANK_ID= EnuTaBankId.BANK_HAIER.getCode();                      // 02   ������д���   2
            tia9901002Temp.body.CITY_ID= EnuTaCityId.CITY_TAIAN.getCode();                      // 03   ���д���       6
            tia9901002Temp.header.BIZ_ID=taRsAccPara.getBizId();                                  // 04   ��ֹ֤�����  14
            tia9901002Temp.body.ACC_ID=taRsAccPara.getAccId();                                    // 05   ���ר���˺�  30
            tia9901002Temp.body.ACC_NAME=taRsAccPara.getAccName();                                // 06   ���ר������  150
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
            }
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
            taTxnFdcPara.setBankId(tia9902001Temp.body.BANK_ID);       // 02   ������д���   2
            taTxnFdcPara.setCityId(tia9902001Temp.body.CITY_ID);       // 03   ���д���       6
            taTxnFdcPara.setBizId(tia9902001Temp.header.BIZ_ID);       // 04   ����������   14
            taTxnFdcPara.setReqSn(tia9902001Temp.header.REQ_SN);       // 05   ��֤��ˮ       30
            taTxnFdcPara.setTxDate(tia9902001Temp.body.TX_DATE);    // 06   ��֤����       10  ��ϵͳ���ڼ���
            taTxnFdcPara.setBranchId(tia9902001Temp.body.BRANCH_ID);   // 07   ��֤����     30
            taTxnFdcPara.setUserId(tia9902001Temp.header.USER_ID);     // 08   ��֤��Ա     30
            taTxnFdcPara.setInitiator(tia9902001Temp.body.INITIATOR);  // 09   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            //taTxnFdcService.insertRecord(taTxnFdcPara);

            String strMsgid= depMsgSendAndRecv.sendDepMessage(tiaPara);
            Toa9902001 toaPara=(Toa9902001) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	�ʻ����	            1   0����ܻ���
                  03	������	            20  �Է�Ϊ��λ
                  04	���ר���˺�            30
                  05    ���ר������            150
                  06    Ԥ���ʽ���ƽ̨��ˮ    16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setAccType(toaPara.body.ACC_TYPE);
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT.trim());
                taTxnFdcPara.setAccId(toaPara.body.ACC_ID);
                taTxnFdcPara.setAccName(toaPara.body.ACC_NAME);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                //taTxnFdcService.updateRecord(taTxnFdcPara);
                return toaPara;
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                //taTxnFdcService.updateRecord(taTxnFdcPara);
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
            }
        } catch (Exception e) {
            logger.error("������֤ʧ��", e);
            throw new RuntimeException("������֤ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ������˽���
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012002(TIA tiaPara) {
        try {
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            Tia900010002 tia900010002Temp =(Tia900010002)tiaPara ;
            taTxnSbsPara.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnSbsPara.setAccId(tia900010002Temp.body.ACC_ID);   // �����˺�
            taTxnSbsPara.setRecvAccId(tia900010002Temp.body.RECV_ACC_ID);   // �տ��˺�
            taTxnSbsPara.setTxAmt(tia900010002Temp.body.TX_AMT);   // ���׽��
            taTxnSbsPara.setReqSn(tia900010002Temp.header.REQ_SN); // ��Χϵͳ��ˮ
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // ��������
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));    // ����ʱ��
            taTxnSbsPara.setUserId(tia900010002Temp.header.USER_ID);// ��Ա��

            taTxnSbsPara.setRecVersion(0);
            taTxnSbsService.insertRecord(taTxnSbsPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900010002Temp);
            Toa900010002 toaPara=(Toa900010002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(taTxnSbsPara.getRtnReqSn().equals(taTxnSbsPara.getReqSn())){
                /*01 ��������Χϵͳ��ˮ��
                  02 �����Ľ��׽��*/
                taTxnSbsPara.setRtnReqSn(toaPara.body.RTN_REQ_SN);
                taTxnSbsPara.setRtnTxAmt(toaPara.body.RTN_TX_AMT);
                taTxnSbsService.updateRecord(taTxnSbsPara);
                return toaPara;
            }
        } catch (Exception e) {
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
        return null;
    }
    /**
     * ����̩���������ϵͳ������˽���
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902002(TIA tiaPara) {
        try {
            TaTxnFdc taTxnFdcPara=new TaTxnFdc();
            Tia9902002 tia9902002Temp=(Tia9902002)tiaPara ;
            taTxnFdcPara.setTxCode(tia9902002Temp.header.TX_CODE);              // 01   ���״���       4   2002
            taTxnFdcPara.setBankId(tia9902002Temp.body.BANK_ID);                // 02   ������д���   2
            taTxnFdcPara.setCityId(tia9902002Temp.body.CITY_ID);                // 03   ���д���       6
            taTxnFdcPara.setBizId(tia9902002Temp.header.BIZ_ID);               // 04   ����������   14
            taTxnFdcPara.setTxAmt(tia9902002Temp.body.TX_AMT);                 // 08   �����ʽ�       20 ������֤�������
            taTxnFdcPara.setAccId(tia9902002Temp.body.ACC_ID);                  // 06   ����˺�       30 ������֤�������
            taTxnFdcPara.setStlType(tia9902002Temp.body.STL_TYPE);             // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            taTxnFdcPara.setCheckId(tia9902002Temp.body.CHECK_ID);             // 10   ֧Ʊ����       30
            taTxnFdcPara.setReqSn(tia9902002Temp.header.REQ_SN);               // 11   ���м�����ˮ�� 30
            taTxnFdcPara.setTxDate(tia9902002Temp.body.TX_DATE);               // 12   ��������       10  ��ϵͳ���ڼ���
            taTxnFdcPara.setBranchId( tia9902002Temp.body.BRANCH_ID);          // 13   ���������     30
            taTxnFdcPara.setUserId(tia9902002Temp.header.USER_ID);            // 14   ������Ա       30
            taTxnFdcPara.setInitiator(tia9902002Temp.body.INITIATOR);         // 15   ����         1   1_�������

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902002Temp);
            Toa9902002 toaPara=(Toa9902002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                return toaPara;
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
            }
        } catch (Exception e) {
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
    }

    /**
     * ����̩���������ϵͳ������˽���
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012011(TIA tiaPara) {
        try {
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            Tia900010002 tia900010002Temp =(Tia900010002)tiaPara ;
            taTxnSbsPara.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnSbsPara.setAccId(tia900010002Temp.body.ACC_ID);   // �����˺�
            taTxnSbsPara.setRecvAccId(tia900010002Temp.body.RECV_ACC_ID);   // �տ��˺�
            taTxnSbsPara.setTxAmt(tia900010002Temp.body.TX_AMT);   // ���׽��
            taTxnSbsPara.setReqSn(tia900010002Temp.header.REQ_SN); // ��Χϵͳ��ˮ
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // ��������
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));    // ����ʱ��
            taTxnSbsPara.setUserId(tia900010002Temp.header.USER_ID);// ��Ա��

            taTxnSbsPara.setRecVersion(0);
            taTxnSbsService.insertRecord(taTxnSbsPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900010002Temp);
            Toa900010002 toaPara=(Toa900010002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(taTxnSbsPara.getRtnReqSn().equals(taTxnSbsPara.getReqSn())){
                /*01 ��������Χϵͳ��ˮ��
                  02 �����Ľ��׽��*/
                taTxnSbsPara.setRtnReqSn(toaPara.body.RTN_REQ_SN);
                taTxnSbsPara.setRtnTxAmt(toaPara.body.RTN_TX_AMT);
                taTxnSbsService.updateRecord(taTxnSbsPara);
                return toaPara;
            }
        } catch (Exception e) {
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
        return null;
    }
    /**
     * ����̩���������ϵͳ�����������
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902011(TIA tiaPara) {
        try {
            TaTxnFdc taTxnFdcPara=new TaTxnFdc();

            Tia9902011 tia9902011Temp=new Tia9902011() ;
            taTxnFdcPara.setTxCode(tia9902011Temp.header.TX_CODE);         // 01   ���״���       4   2011
            taTxnFdcPara.setBankId(tia9902011Temp.body.BANK_ID);          // 02   ������д���   2
            taTxnFdcPara.setCityId(tia9902011Temp.body.CITY_ID);          // 03   ���д���       6
            taTxnFdcPara.setBizId(tia9902011Temp.header.BIZ_ID);           // 04   ����������   14
            taTxnFdcPara.setReqSn(tia9902011Temp.header.REQ_SN);           // 05   ���г�����ˮ   30
            taTxnFdcPara.setTxDate(tia9902011Temp.body.TX_DATE);        // 06   ��������       10  ��ϵͳ���ڼ���
            taTxnFdcPara.setBranchId(tia9902011Temp.body.BRANCH_ID);       // 07   ��������       30
            taTxnFdcPara.setUserId(tia9902011Temp.header.USER_ID);         // 08   ������Ա       30
            taTxnFdcPara.setInitiator(tia9902011Temp.body.INITIATOR);      // 09   ����         1   1_�������

            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902011Temp);
            Toa9902011 toaPara=(Toa9902011) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	���	                4   0000��ʾ�ɹ�
                  02	Ԥ���ʽ���ƽ̨��ˮ	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                return toaPara;
            }else{
                 /*01	���ؽ��	    4
                  02	����ԭ������	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
            }
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902101 tia9902101Temp=new Tia9902101() ;
            tia9902101Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902101Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   ���״���       4   2101
            tia9902101Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   ������д���   2
            tia9902101Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   ���д���       6
            tia9902101Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   ����ҵ����   14
            tia9902101Temp.header.PASSWORD=taTxnFdcPara.getPassword();  // 05   ��������       32 MD5
            tia9902101Temp.header.REQ_SN=taTxnFdcPara.getReqSn();       // 06   ��֤��ˮ       30
            tia9902101Temp.body.TX_DATE=taTxnFdcPara.getTxDate();    // 07   ��֤����       10  ��ϵͳ���ڼ���
            tia9902101Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 08   ��֤�����     30
            tia9902101Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 09   ��֤��Ա��     30
            tia9902101Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 10   ����         1   1_�������

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902101Temp);
            Toa9902101 toaPara=(Toa9902101) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                taTxnFdcPara.setAccId(toaPara.body.ACC_ID);
                taTxnFdcPara.setAccName(toaPara.body.ACC_NAME);
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT);
                taTxnFdcPara.setRecvBank(toaPara.body.RECV_BANK);
                taTxnFdcPara.setRecvAccId(toaPara.body.RECV_ACC_ID);
                taTxnFdcPara.setRecvAccName(toaPara.body.RECV_ACC_NAME);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902102 tia9902102Temp=new Tia9902102() ;
            tia9902102Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902102Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   ���״���       4   2102
            tia9902102Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   ������д���   2
            tia9902102Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902102Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   ����ҵ����   14
            tia9902102Temp.header.PASSWORD=taTxnFdcPara.getPassword();      // 05   ��������       32 MD5
            tia9902102Temp.body.ACC_ID=taTxnFdcPara.getAccId();              // 06   ����˺�       30 ������֤�������
            tia9902102Temp.body.RECV_ACC_ID=taTxnFdcPara.getRecvAccId();    // 07   �տλ�˺�   30 ������֤�������
            tia9902102Temp.body.TX_AMT=taTxnFdcPara.getTxAmt();              // 08   �����ʽ�       20 ������֤�������
            tia9902102Temp.body.STL_TYPE=taTxnFdcPara.getStlType();          // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            tia9902102Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();          // 10   ֧Ʊ����       30
            tia9902102Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 11   ���м�����ˮ�� 30
            tia9902102Temp.body.TX_DATE=taTxnFdcPara.getTxDate();            // 12   ��������       10  ��ϵͳ���ڼ���
            tia9902102Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 13   ���������     30
            tia9902102Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 14   ��Ա��         30
            tia9902102Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 15   ����         1   1_�������

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902102Temp);
            Toa9902102 toaPara=(Toa9902102) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902111 tia9902111Temp=new Tia9902111() ;
            tia9902111Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902111Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   ���״���       4   2102
            tia9902111Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   ������д���   2
            tia9902111Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902111Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   ����ҵ����   14
            tia9902111Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 05   ���м�����ˮ�� 30
            tia9902111Temp.body.TX_DATE=taTxnFdcPara.getTxDate();        // 06   ��������       10  ��ϵͳ���ڼ���
            tia9902111Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07   ���������     30
            tia9902111Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08   ��Ա��         30
            tia9902111Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09   ����         1   1_�������

            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902111Temp);
            Toa9902111 toaPara=(Toa9902111) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902201 tia9902201Temp=new Tia9902201() ;
            tia9902201Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902201Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   ���״���       4   2201
            tia9902201Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   ������д���   2
            tia9902201Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   ���д���       6
            tia9902201Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   ����ҵ����   14
            tia9902201Temp.header.PASSWORD=taTxnFdcPara.getPassword();  // 05   ��������       32 MD5
            tia9902201Temp.header.REQ_SN=taTxnFdcPara.getReqSn();       // 06   ��֤��ˮ       30
            tia9902201Temp.body.TX_DATE=taTxnFdcPara.getTxDate();    // 07   ��֤����       10  ��ϵͳ���ڼ���
            tia9902201Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 08   ��֤�����     30
            tia9902201Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 09   ��֤��Ա��     30
            tia9902201Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 10   ����         1   1_�������

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902201Temp);
            Toa9902201 toaPara=(Toa9902201) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                taTxnFdcPara.setAccId(toaPara.body.ACC_ID);
                taTxnFdcPara.setAccName(toaPara.body.ACC_NAME);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902202 tia9902202Temp=new Tia9902202() ;
            tia9902202Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902202Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   ���״���       4   2202
            tia9902202Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   ������д���   2
            tia9902202Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902202Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   ����ҵ����   14
            tia9902202Temp.header.PASSWORD=taTxnFdcPara.getPassword();      // 05   ��������       32 MD5
            tia9902202Temp.body.ACC_ID=taTxnFdcPara.getAccId();              // 06   ����˺�       30 ������֤�������
            tia9902202Temp.body.TX_AMT=taTxnFdcPara.getTxAmt();              // 07   �����ʽ�       20 ������֤�������
            tia9902202Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 08   ���м�����ˮ�� 30
            tia9902202Temp.body.TX_DATE=taTxnFdcPara.getTxDate();            // 09   ��������       10  ��ϵͳ���ڼ���
            tia9902202Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 10   ���������     30
            tia9902202Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 11   ��Ա��         30
            tia9902202Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 12   ����         1   1_�������

            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902202Temp);
            Toa9902202 toaPara=(Toa9902202) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902211 tia9902211Temp=new Tia9902211() ;
            tia9902211Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902211Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   ���״���       4   2202
            tia9902211Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   ������д���   2
            tia9902211Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902211Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   ����ҵ����   14
            tia9902211Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 05  ���г�����ˮ��  30
            tia9902211Temp.body.TX_DATE=taTxnFdcPara.getTxDate();        // 06  ��������        10  ��ϵͳ���ڼ���
            tia9902211Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07  ���������      30
            tia9902211Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08  ��Ա��          30
            tia9902211Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09  ����          1   1_�������

            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902211Temp);
            Toa9902211 toaPara=(Toa9902211) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902501 tia9902501Temp=new Tia9902501() ;
            tia9902501Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902501Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   ���״���       4   2501
            tia9902501Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   ������д���   2
            tia9902501Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   ���д���       6
            tia9902501Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   ҵ����       14
            tia9902501Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 05   ��ѯ����       30
            tia9902501Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 06   ��ѯ��Ա       30
            tia9902501Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 07   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902501Temp);
            Toa9902501 toaPara=(Toa9902501) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                  /*  01    ���	                  4   0000��ʾ�ɹ�
                      02    ���˽��	              1   0_�ɹ� 1_ʧ��
                      03    Ԥ���ʽ���ƽ̨������ˮ  16  ҵ�������ˮ
                      04    ������м�����ˮ	      30  ҵ�������ˮ
                      05    Ԥ���ʽ���ƽ̨��ˮ	  16
                  */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setActRstl(toaPara.body.ACT_RSTL);
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
                logger.error("MQ��Ϣ����ʧ��");
                throw new RuntimeException("MQ��Ϣ����ʧ��");
            }
        } catch (Exception e) {
            logger.error("������֤ʧ��", e);
            throw new RuntimeException("������֤ʧ��", e);
        }
    }
}
