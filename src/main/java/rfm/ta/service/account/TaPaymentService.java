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
 * Time: 下午2:12
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
     * 发送泰安房产监管系统交存验证交易
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902001(TIA tiaPara) {
        try {
            Tia9902001 tia9902001Temp=(Tia9902001)tiaPara;
            TaTxnFdc taTxnFdcPara=new TaTxnFdc();
            taTxnFdcPara.setTxCode(tia9902001Temp.header.TX_CODE);     // 01   交易代码       4   2001
            taTxnFdcPara.setBankId(tia9902001Temp.body.BANK_ID);       // 02   监管银行代码   2
            taTxnFdcPara.setCityId(tia9902001Temp.body.CITY_ID);       // 03   城市代码       6
            taTxnFdcPara.setBizId(tia9902001Temp.header.BIZ_ID);       // 04   交存申请编号   14
            taTxnFdcPara.setReqSn(tia9902001Temp.header.REQ_SN);       // 05   验证流水       30
            taTxnFdcPara.setTradeDate(tia9902001Temp.body.TX_DATE);    // 06   验证日期       10  送系统日期即可
            taTxnFdcPara.setBranchId(tia9902001Temp.body.BRANCH_ID);   // 07   验证网点     30
            taTxnFdcPara.setUserId(tia9902001Temp.header.USER_ID);     // 08   验证人员     30
            taTxnFdcPara.setInitiator(tia9902001Temp.body.INITIATOR);  // 09   发起方         1   1_监管银行
            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            //taTxnFdcService.insertRecord(taTxnFdcPara);

            String strMsgid= depMsgSendAndRecv.sendDepMessage(tiaPara);
            Toa9902001 toaPara=(Toa9902001) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	帐户类别	            1   0：监管户；
                  03	交存金额	            20  以分为单位
                  04	监管专户账号            30
                  05    监管专户户名            150
                  06    预售资金监管平台流水    16
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
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                //taTxnFdcService.updateRecord(taTxnFdcPara);
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
            }
        } catch (Exception e) {
            logger.error("交存验证失败", e);
            throw new RuntimeException("交存验证失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统交存记账交易
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012002(TIA tiaPara) {
        try {
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            Tia900010002 tia900010002Temp =(Tia900010002)tiaPara ;
            taTxnSbsPara.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnSbsPara.setAccId(tia900010002Temp.body.ACC_ID);   // 付款账号
            taTxnSbsPara.setRecvAccId(tia900010002Temp.body.RECV_ACC_ID);   // 收款账号
            taTxnSbsPara.setTxAmt(tia900010002Temp.body.TX_AMT);   // 交易金额
            taTxnSbsPara.setReqSn(tia900010002Temp.header.REQ_SN); // 外围系统流水
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // 交易日期
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));    // 交易时间
            taTxnSbsPara.setUserId(tia900010002Temp.header.USER_ID);// 柜员号

            taTxnSbsPara.setRecVersion(0);
            taTxnSbsService.insertRecord(taTxnSbsPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900010002Temp);
            Toa900010002 toaPara=(Toa900010002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(taTxnSbsPara.getRtnReqSn().equals(taTxnSbsPara.getReqSn())){
                /*01 返还的外围系统流水号
                  02 返还的交易金额*/
                taTxnSbsPara.setRtnReqSn(toaPara.body.RTN_REQ_SN);
                taTxnSbsPara.setRtnTxAmt(toaPara.body.RTN_TX_AMT);
                taTxnSbsService.updateRecord(taTxnSbsPara);
                return toaPara;
            }
        } catch (Exception e) {
            logger.error("交存记账失败", e);
            throw new RuntimeException("交存记账失败", e);
        }
        return null;
    }
    /**
     * 发送泰安房产监管系统交存记账交易
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902002(TIA tiaPara) {
        try {
            TaTxnFdc taTxnFdcPara=new TaTxnFdc();
            Tia9902002 tia9902002Temp=(Tia9902002)tiaPara ;
            taTxnFdcPara.setTxCode(tia9902002Temp.header.TX_CODE);              // 01   交易代码       4   2002
            taTxnFdcPara.setBankId(tia9902002Temp.body.BANK_ID);                // 02   监管银行代码   2
            taTxnFdcPara.setCityId(tia9902002Temp.body.CITY_ID);                // 03   城市代码       6
            taTxnFdcPara.setBizId(tia9902002Temp.header.BIZ_ID);               // 04   交存申请编号   14
            taTxnFdcPara.setTxAmt(new BigDecimal(tia9902002Temp.body.TX_AMT)); // 08   交存资金       20 交存验证的输出项
            taTxnFdcPara.setAccId(tia9902002Temp.body.ACC_ID);                  // 06   监管账号       30 交存验证的输出项
            taTxnFdcPara.setStlType(tia9902002Temp.body.STL_TYPE);             // 09   结算方式       2 01_ 现金 02_ 转账 03_ 支票
            taTxnFdcPara.setCheckId(tia9902002Temp.body.CHECK_ID);             // 10   支票号码       30
            taTxnFdcPara.setReqSn(tia9902002Temp.header.REQ_SN);               // 11   银行记账流水号 30
            taTxnFdcPara.setTradeDate(tia9902002Temp.body.TX_DATE);            // 12   记账日期       10  送系统日期即可
            taTxnFdcPara.setBranchId( tia9902002Temp.body.BRANCH_ID);          // 13   记账网点号     30
            taTxnFdcPara.setUserId(tia9902002Temp.header.USER_ID);            // 14   记账人员       30
            taTxnFdcPara.setInitiator(tia9902002Temp.body.INITIATOR);         // 15   发起方         1   1_监管银行

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902002Temp);
            Toa9902002 toaPara=(Toa9902002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	预售资金监管平台流水	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                return toaPara;
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
            }
        } catch (Exception e) {
            logger.error("交存记账失败", e);
            throw new RuntimeException("交存记账失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统交存记账交易
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012011(TIA tiaPara) {
        try {
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            Tia900010002 tia900010002Temp =(Tia900010002)tiaPara ;
            taTxnSbsPara.setTxCode(EnuTaSbsTxCode.TRADE_0002.getCode());
            taTxnSbsPara.setAccId(tia900010002Temp.body.ACC_ID);   // 付款账号
            taTxnSbsPara.setRecvAccId(tia900010002Temp.body.RECV_ACC_ID);   // 收款账号
            taTxnSbsPara.setTxAmt(tia900010002Temp.body.TX_AMT);   // 交易金额
            taTxnSbsPara.setReqSn(tia900010002Temp.header.REQ_SN); // 外围系统流水
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // 交易日期
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));    // 交易时间
            taTxnSbsPara.setUserId(tia900010002Temp.header.USER_ID);// 柜员号

            taTxnSbsPara.setRecVersion(0);
            taTxnSbsService.insertRecord(taTxnSbsPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900010002Temp);
            Toa900010002 toaPara=(Toa900010002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(taTxnSbsPara.getRtnReqSn().equals(taTxnSbsPara.getReqSn())){
                /*01 返还的外围系统流水号
                  02 返还的交易金额*/
                taTxnSbsPara.setRtnReqSn(toaPara.body.RTN_REQ_SN);
                taTxnSbsPara.setRtnTxAmt(toaPara.body.RTN_TX_AMT);
                taTxnSbsService.updateRecord(taTxnSbsPara);
                return toaPara;
            }
        } catch (Exception e) {
            logger.error("交存记账失败", e);
            throw new RuntimeException("交存记账失败", e);
        }
        return null;
    }
    /**
     * 发送泰安房产监管系统交存冲正交易
     *
     * @param tiaPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9902011(TIA tiaPara) {
        try {
            TaTxnFdc taTxnFdcPara=new TaTxnFdc();

            Tia9902011 tia9902011Temp=new Tia9902011() ;
            taTxnFdcPara.setTxCode(tia9902011Temp.header.TX_CODE);         // 01   交易代码       4   2011
            taTxnFdcPara.setBankId(tia9902011Temp.body.BANK_ID);          // 02   监管银行代码   2
            taTxnFdcPara.setCityId(tia9902011Temp.body.CITY_ID);          // 03   城市代码       6
            taTxnFdcPara.setBizId(tia9902011Temp.header.BIZ_ID);           // 04   交存申请编号   14
            taTxnFdcPara.setReqSn(tia9902011Temp.header.REQ_SN);           // 05   银行冲正流水   30
            taTxnFdcPara.setTradeDate(tia9902011Temp.body.TX_DATE);        // 06   冲正日期       10  送系统日期即可
            taTxnFdcPara.setBranchId(tia9902011Temp.body.BRANCH_ID);       // 07   冲正网点       30
            taTxnFdcPara.setUserId(tia9902011Temp.header.USER_ID);         // 08   冲正人员       30
            taTxnFdcPara.setInitiator(tia9902011Temp.body.INITIATOR);      // 09   发起方         1   1_监管银行

            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902011Temp);
            Toa9902011 toaPara=(Toa9902011) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	预售资金监管平台流水	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                return toaPara;
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
            }
        } catch (Exception e) {
            logger.error("交存冲正失败", e);
            throw new RuntimeException("交存冲正失败", e);
        }
    }
}
