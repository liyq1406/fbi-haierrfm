package rfm.ta.service.account;

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
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.dep.DepMsgSendAndRecv;
import rfm.ta.service.his.TaTxnFdcService;
import rfm.ta.service.his.TaTxnSbsService;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaRefundService {
    private static final Logger logger = LoggerFactory.getLogger(TaRefundService.class);

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

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
            tia9902202Temp.body.TX_AMT=taTxnFdcPara.getTxAmt().toString();   // 07   �����ʽ�       20 ������֤�������
            tia9902202Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 08   ���м�����ˮ�� 30
            tia9902202Temp.body.TX_DATE=taTxnFdcPara.getTxDate();         // 09   ��������       10  ��ϵͳ���ڼ���
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
}
