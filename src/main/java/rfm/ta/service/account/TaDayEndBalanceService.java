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
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaDayEndBalanceService {
    private static final Logger logger = LoggerFactory.getLogger(TaDayEndBalanceService.class);

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    /**
     * 发送泰安房产监管系统交存记账交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012601(TaTxnFdc taTxnFdcPara) {
        try {
            Tia900012601 tia900012601Temp=new Tia900012601();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaTxCode.TRADE_2601.getCode());
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn().substring(8, 26));           // 外围系统流水
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));                    // 交易日期
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());                         // 柜员号

            tia900012601Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            tia900012601Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900012601Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900012601Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900012601Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            taTxnSbsPara.setRecVersion(0);
            taTxnSbsService.insertRecord(taTxnSbsPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012601Temp);
            Toa900012601 toaPara=(Toa900012601) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(taTxnSbsPara.getRtnReqSn().equals(taTxnSbsPara.getReqSn())){
                /*01 返还的外围系统流水号
                  02 返还的交易金额*/
                taTxnSbsService.updateRecord(taTxnSbsPara);
                return toaPara;
            }
        } catch (Exception e) {
            logger.error("交存记账失败", e);
            throw new RuntimeException("交存记账失败", e);
        }
        return null;
    }
}
