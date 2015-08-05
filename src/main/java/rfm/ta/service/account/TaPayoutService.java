package rfm.ta.service.account;

import common.utils.ToolUtil;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.auth.MD5Helper;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.dep.DepService;
import rfm.ta.service.his.TaTxnFdcService;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaPayoutService {
    private static final Logger logger = LoggerFactory.getLogger(TaPayoutService.class);

    @Autowired
    private TaTxnFdcService taTxnFdcService;
    @Autowired
    private DepService depService;

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
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
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
            tia9902101Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();    // 07   ��֤����       10  ��ϵͳ���ڼ���
            tia9902101Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 08   ��֤�����     30
            tia9902101Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 09   ��֤��Ա��     30
            tia9902101Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 10   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);
            String strMsgid=depService.sendDepMessage(tia9902101Temp);
            Toa9902101 toaPara=(Toa9902101)depService.recvDepMessage(strMsgid);
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
                taTxnFdcPara.setTxAmt(new BigDecimal(toaPara.body.TX_AMT.trim()));
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
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
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
            tia9902102Temp.body.TX_AMT=taTxnFdcPara.getTxAmt().toString();   // 08   �����ʽ�       20 ������֤�������
            tia9902102Temp.body.STL_TYPE=taTxnFdcPara.getStlType();          // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            tia9902102Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();          // 10   ֧Ʊ����       30
            tia9902102Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 11   ���м�����ˮ�� 30
            tia9902102Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();         // 12   ��������       10  ��ϵͳ���ڼ���
            tia9902102Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 13   ���������     30
            tia9902102Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 14   ��Ա��         30
            tia9902102Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 15   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            String strMsgid=depService.sendDepMessage(tia9902102Temp);
            Toa9902102 toaPara=(Toa9902102)depService.recvDepMessage(strMsgid);
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
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
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
            tia9902111Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();        // 06   ��������       10  ��ϵͳ���ڼ���
            tia9902111Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07   ���������     30
            tia9902111Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08   ��Ա��         30
            tia9902111Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09   ����         1   1_�������

            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid=depService.sendDepMessage(tia9902111Temp);
            Toa9902111 toaPara=(Toa9902111)depService.recvDepMessage(strMsgid);
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