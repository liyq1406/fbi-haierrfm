package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.Tia900010002;
import org.fbi.dep.model.txn.Toa900010002;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.EnuTaSbsTxCode;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.his.TaTxnSbsService;

/**
 * Created by Thinkpad on 2015/8/18.
 */
@Service
public class TaSbsService {
    private static final Logger logger = LoggerFactory.getLogger(TaSbsService.class);

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    /**
     * 发送SBS系统交存记账交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900010002(TaRsAccDtl taTxnFdcPara) {
        try {
            Tia900010002 tia900010002Temp=new Tia900010002();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnSbsPara.setAccId(taTxnFdcPara.getAccId().trim());         // 付款账号
            taTxnSbsPara.setRecvAccId(taTxnFdcPara.getRecvAccId().trim()); // 收款账号
            taTxnSbsPara.setTxAmt(taTxnFdcPara.getTxAmt());                // 交易金额
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn().substring(8,26));// 外围系统流水
            taTxnSbsPara.setTxDate(taTxnFdcPara.getTxDate());              // 交易日期
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));         // 交易时间
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());              // 柜员号

            tia900010002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            // 划拨是从监管户到一般账户
            tia900010002Temp.body.ACC_ID=taTxnSbsPara.getAccId();
            tia900010002Temp.body.RECV_ACC_ID=taTxnSbsPara.getRecvAccId();

            tia900010002Temp.body.TX_AMT=taTxnSbsPara.getTxAmt();
            tia900010002Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900010002Temp.body.TX_TIME=taTxnSbsPara.getTxTime();
            tia900010002Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900010002Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900010002Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            taTxnSbsPara.setRecVersion(0);
            taTxnSbsService.insertRecord(taTxnSbsPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900010002Temp);
            Toa900010002 toaPara=(Toa900010002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            return toaPara;
        } catch (Exception e) {
            logger.error("划拨记账失败", e);
            throw new RuntimeException("划拨记账失败", e);
        }
    }
}
