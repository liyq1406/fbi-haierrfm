package rfm.ta.service.account;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.dep.DepMsgSendAndRecv;
import rfm.ta.service.his.TaTxnSbsService;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaDayEndBlncService {
    private static final Logger logger = LoggerFactory.getLogger(TaDayEndBlncService.class);

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    /**
     * ����̩���������ϵͳ������˽���
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012601(TaTxnFdc taTxnFdcPara) {
        try {
            Tia900012601 tia900012601Temp=new Tia900012601();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaFdcTxCode.TRADE_2601.getCode());
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn());           // ��Χϵͳ��ˮ
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // ��������
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());         // ��Ա��

            tia900012601Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            tia900012601Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900012601Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900012601Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900012601Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            //ͨ��MQ������Ϣ��DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012601Temp);
            TOA toaPara = depMsgSendAndRecv.recvDepMessage(strMsgid);
            return toaPara;
        } catch (Exception e) {
            logger.error("��SBS�ռ��������˲�ѯʧ��", e);
            throw new RuntimeException("��SBS�ռ��������˲�ѯʧ��", e);
        }
    }
}
