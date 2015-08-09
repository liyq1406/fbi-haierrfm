package rfm.ta.service.account;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.auth.MD5Helper;
import rfm.ta.common.enums.*;
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
public class TaPayoutService {
    private static final Logger logger = LoggerFactory.getLogger(TaPayoutService.class);

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    /**
     * 发送泰安房产监管系统划拨验证交易
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
            tia9902101Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   交易代码       4   2101
            tia9902101Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   监管银行代码   2
            tia9902101Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   城市代码       6
            tia9902101Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   划拨业务编号   14
            tia9902101Temp.header.PASSWORD=taTxnFdcPara.getPassword();  // 05   划拨密码       32 MD5
            tia9902101Temp.header.REQ_SN=taTxnFdcPara.getReqSn();       // 06   验证流水       30
            tia9902101Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();    // 07   验证日期       10  送系统日期即可
            tia9902101Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 08   验证网点号     30
            tia9902101Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 09   验证柜员号     30
            tia9902101Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 10   发起方         1   1_监管银行

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902101Temp);
            Toa9902101 toaPara=(Toa9902101) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	监管账号                30
                  03    监管账户户名            150
                  04	划拨金额	            20  以分为单位
                  05	收款银行	            90
                  06	收款单位账号	        30
                  07	收款单位户名	        150
                  08	项目名称	            128
                  09	开发企业名称	        256
                  10    预售资金监管平台流水    16
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
            logger.error("划拨验证失败", e);
            throw new RuntimeException("划拨验证失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统交存记账交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012102(TaTxnFdc taTxnFdcPara) {
        try {
            Tia900010002 tia900010002Temp=new Tia900010002();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaTxCode.TRADE_2102.getCode());
            taTxnSbsPara.setAccId(taTxnFdcPara.getAccId().substring(0,14));           // 付款账号
            taTxnSbsPara.setRecvAccId(taTxnFdcPara.getRecvAccId().substring(0,14));   // 收款账号
            taTxnSbsPara.setTxAmt(taTxnFdcPara.getTxAmt().toString());                // 交易金额
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn().substring(8,26));           // 外围系统流水
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));                    // 交易日期
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));                    // 交易时间
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());                         // 柜员号

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
     * 发送泰安房产监管系统划拨记账交易
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
            tia9902102Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   交易代码       4   2102
            tia9902102Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   监管银行代码   2
            tia9902102Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   城市代码       6
            tia9902102Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   划拨业务编号   14
            tia9902102Temp.header.PASSWORD=taTxnFdcPara.getPassword();      // 05   划拨密码       32 MD5
            tia9902102Temp.body.ACC_ID=taTxnFdcPara.getAccId();              // 06   监管账号       30 划拨验证的输出项
            tia9902102Temp.body.RECV_ACC_ID=taTxnFdcPara.getRecvAccId();    // 07   收款单位账号   30 划拨验证的输出项
            tia9902102Temp.body.TX_AMT=taTxnFdcPara.getTxAmt().toString();   // 08   划拨资金       20 划拨验证的输出项
            tia9902102Temp.body.STL_TYPE=taTxnFdcPara.getStlType();          // 09   结算方式       2 01_ 现金 02_ 转账 03_ 支票
            tia9902102Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();          // 10   支票号码       30
            tia9902102Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 11   银行记账流水号 30
            tia9902102Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();         // 12   记账日期       10  送系统日期即可
            tia9902102Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 13   记账网点号     30
            tia9902102Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 14   柜员号         30
            tia9902102Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 15   发起方         1   1_监管银行

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902102Temp);
            Toa9902102 toaPara=(Toa9902102) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	预售资金监管平台流水	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
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
            logger.error("划拨记账失败", e);
            throw new RuntimeException("划拨记账失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统交存记账交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012111(TaTxnFdc taTxnFdcPara) {
        try {
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            Tia900010002 tia900010002Temp=new Tia900010002();
            taTxnSbsPara.setAccId(taTxnFdcPara.getAccId());           // 付款账号
            taTxnSbsPara.setRecvAccId(taTxnFdcPara.getRecvAccId());   // 收款账号
            taTxnSbsPara.setTxAmt(taTxnFdcPara.getTxAmt().toString());// 交易金额
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn());           // 外围系统流水
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));     // 交易日期
            taTxnSbsPara.setTxTime(ToolUtil.getNow("HH:mm:ss"));     // 交易时间
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());          // 柜员号

            // 冲正是划拨的逆向，故从一般户退回到监管账号
            tia900010002Temp.body.ACC_ID=taTxnSbsPara.getRecvAccId();
            tia900010002Temp.body.RECV_ACC_ID=taTxnSbsPara.getAccId();

            tia900010002Temp.body.TX_AMT=taTxnSbsPara.getTxAmt();
            tia900010002Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900010002Temp.body.TX_TIME=taTxnSbsPara.getTxTime();
            tia900010002Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900010002Temp.header.USER_ID=taTxnSbsPara.getUserId();

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
     * 发送泰安房产监管系统划拨冲正交易
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
            tia9902111Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   交易代码       4   2102
            tia9902111Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   监管银行代码   2
            tia9902111Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   城市代码       6
            tia9902111Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   划拨业务编号   14
            tia9902111Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 05   银行记账流水号 30
            tia9902111Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();        // 06   记账日期       10  送系统日期即可
            tia9902111Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07   记账网点号     30
            tia9902111Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08   柜员号         30
            tia9902111Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09   发起方         1   1_监管银行

            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902111Temp);
            Toa9902111 toaPara=(Toa9902111) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	预售资金监管平台流水	16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
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
            logger.error("划拨冲正失败", e);
            throw new RuntimeException("划拨冲正失败", e);
        }
    }
}
