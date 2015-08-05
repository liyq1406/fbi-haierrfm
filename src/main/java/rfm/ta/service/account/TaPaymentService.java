package rfm.ta.service.account;

import common.utils.ToolUtil;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.auth.MD5Helper;
import rfm.ta.common.enums.EnuTaBankId;
import rfm.ta.common.enums.EnuTaCityId;
import rfm.ta.common.enums.EnuTaInitiatorId;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
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
public class TaPaymentService {
    private static final Logger logger = LoggerFactory.getLogger(TaPaymentService.class);

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
    public void sendAndRecvRealTimeTxn9902001(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902001 tia9902001Temp=new Tia9902001() ;
            tia9902001Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902001Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   ���״���       4   2001
            tia9902001Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   ������д���   2
            tia9902001Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   ���д���       6
            tia9902001Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   ����������   14
            tia9902001Temp.header.REQ_SN=taTxnFdcPara.getReqSn();       // 05   ��֤��ˮ       30
            tia9902001Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();    // 06   ��֤����       10  ��ϵͳ���ڼ���
            tia9902001Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 07   ��֤����     30
            tia9902001Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 08   ��֤��Ա     30
            tia9902001Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 09   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);
            String strMsgid=depService.sendDepMessage(tia9902001Temp);
            Toa9902001 toaPara=(Toa9902001)depService.recvDepMessage(strMsgid);
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
     * ����̩���������ϵͳ������˽���
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902002(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902002 tia9902002Temp=new Tia9902002() ;
            tia9902002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902002Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   ���״���       4   2002
            tia9902002Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   ������д���   2
            tia9902002Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902002Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   ����������   14
            tia9902002Temp.body.TX_AMT=taTxnFdcPara.getTxAmt().toString();  // 08   �����ʽ�       20 ������֤�������
            tia9902002Temp.body.ACC_ID=taTxnFdcPara.getAccId();             // 06   ����˺�       30 ������֤�������
            tia9902002Temp.body.STL_TYPE=taTxnFdcPara.getStlType();         // 09   ���㷽ʽ       2 01_ �ֽ� 02_ ת�� 03_ ֧Ʊ
            tia9902002Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();         // 10   ֧Ʊ����       30
            tia9902002Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 11   ���м�����ˮ�� 30
            tia9902002Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();        // 12   ��������       10  ��ϵͳ���ڼ���
            tia9902002Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 13   ���������     30
            tia9902002Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 14   ������Ա       30
            tia9902002Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 15   ����         1   1_�������
            //ͨ��MQ������Ϣ��DEP
            String strMsgid=depService.sendDepMessage(tia9902002Temp);
            Toa9902002 toaPara=(Toa9902002)depService.recvDepMessage(strMsgid);
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
    public void sendAndRecvRealTimeTxn9902011(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902011 tia9902011Temp=new Tia9902011() ;
            tia9902011Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902011Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   ���״���       4   2002
            tia9902011Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   ������д���   2
            tia9902011Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   ���д���       6
            tia9902011Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   ����������   14
            tia9902011Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 05   ���г�����ˮ   30
            tia9902011Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();        // 06   ��������       10  ��ϵͳ���ڼ���
            tia9902011Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07   ��������       30
            tia9902011Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08   ������Ա       30
            tia9902011Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09   ����         1   1_�������

            //ͨ��MQ������Ϣ��DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //ͨ��MQ������Ϣ��DEP
            String strMsgid=depService.sendDepMessage(tia9902011Temp);
            Toa9902011 toaPara=(Toa9902011)depService.recvDepMessage(strMsgid);
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
            logger.error("�������ʧ��", e);
            throw new RuntimeException("�������ʧ��", e);
        }
    }
}
