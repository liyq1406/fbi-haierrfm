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
import rfm.ta.common.enums.EnuTaSbsTxCode;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
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
public class TaPaymentService {
    private static final Logger logger = LoggerFactory.getLogger(TaPaymentService.class);

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

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
            taTxnFdcPara.setTradeDate(tia9902001Temp.body.TX_DATE);    // 06   ��֤����       10  ��ϵͳ���ڼ���
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
                taTxnFdcPara.setTxAmt(new BigDecimal(toaPara.body.TX_AMT.trim()));
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
            taTxnFdcPara.setTxAmt(new BigDecimal(tia9902002Temp.body.TX_AMT)); // 08   �����ʽ�       20 ������֤�������
            taTxnFdcPara.setAccId(tia9902002Temp.body.ACC_ID);                  // 06   ����˺�       30 ������֤�������
            taTxnFdcPara.setStlType(tia9902002Temp.body.STL_TYPE);             // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            taTxnFdcPara.setCheckId(tia9902002Temp.body.CHECK_ID);             // 10   ֧Ʊ����       30
            taTxnFdcPara.setReqSn(tia9902002Temp.header.REQ_SN);               // 11   ���м�����ˮ�� 30
            taTxnFdcPara.setTradeDate(tia9902002Temp.body.TX_DATE);            // 12   ��������       10  ��ϵͳ���ڼ���
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
            taTxnFdcPara.setTradeDate(tia9902011Temp.body.TX_DATE);        // 06   ��������       10  ��ϵͳ���ڼ���
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
}
