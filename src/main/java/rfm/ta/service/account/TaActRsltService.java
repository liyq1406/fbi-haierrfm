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
public class TaActRsltService {
    private static final Logger logger = LoggerFactory.getLogger(TaActRsltService.class);

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
    public void sendAndRecvRealTimeTxn9902501(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTradeDate(ToolUtil.getStrLastUpdDate());
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
            String strMsgid=depService.sendDepMessage(tia9902501Temp);
            Toa9902501 toaPara=(Toa9902501)depService.recvDepMessage(strMsgid);
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
