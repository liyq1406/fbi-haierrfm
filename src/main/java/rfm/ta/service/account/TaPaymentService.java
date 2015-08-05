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
 * Time: 下午2:12
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
     * 发送泰安房产监管系统交存验证交易
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
            tia9902001Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   交易代码       4   2001
            tia9902001Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   监管银行代码   2
            tia9902001Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   城市代码       6
            tia9902001Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   交存申请编号   14
            tia9902001Temp.header.REQ_SN=taTxnFdcPara.getReqSn();       // 05   验证流水       30
            tia9902001Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();    // 06   验证日期       10  送系统日期即可
            tia9902001Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 07   验证网点     30
            tia9902001Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 08   验证人员     30
            tia9902001Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 09   发起方         1   1_监管银行
            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);
            String strMsgid=depService.sendDepMessage(tia9902001Temp);
            Toa9902001 toaPara=(Toa9902001)depService.recvDepMessage(strMsgid);
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
            logger.error("交存验证失败", e);
            throw new RuntimeException("交存验证失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统交存记账交易
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
            tia9902002Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   交易代码       4   2002
            tia9902002Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   监管银行代码   2
            tia9902002Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   城市代码       6
            tia9902002Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   交存申请编号   14
            tia9902002Temp.body.TX_AMT=taTxnFdcPara.getTxAmt().toString();  // 08   交存资金       20 交存验证的输出项
            tia9902002Temp.body.ACC_ID=taTxnFdcPara.getAccId();             // 06   监管账号       30 交存验证的输出项
            tia9902002Temp.body.STL_TYPE=taTxnFdcPara.getStlType();         // 09   结算方式       2 01_ 现金 02_ 转账 03_ 支票
            tia9902002Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();         // 10   支票号码       30
            tia9902002Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 11   银行记账流水号 30
            tia9902002Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();        // 12   记账日期       10  送系统日期即可
            tia9902002Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 13   记账网点号     30
            tia9902002Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 14   记账人员       30
            tia9902002Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 15   发起方         1   1_监管银行
            //通过MQ发送信息到DEP
            String strMsgid=depService.sendDepMessage(tia9902002Temp);
            Toa9902002 toaPara=(Toa9902002)depService.recvDepMessage(strMsgid);
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
            logger.error("交存记账失败", e);
            throw new RuntimeException("交存记账失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统交存冲正交易
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
            tia9902011Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   交易代码       4   2002
            tia9902011Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   监管银行代码   2
            tia9902011Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   城市代码       6
            tia9902011Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   交存申请编号   14
            tia9902011Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 05   银行冲正流水   30
            tia9902011Temp.body.TX_DATE=taTxnFdcPara.getTradeDate();        // 06   冲正日期       10  送系统日期即可
            tia9902011Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07   冲正网点       30
            tia9902011Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08   冲正人员       30
            tia9902011Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09   发起方         1   1_监管银行

            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid=depService.sendDepMessage(tia9902011Temp);
            Toa9902011 toaPara=(Toa9902011)depService.recvDepMessage(strMsgid);
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
            logger.error("交存冲正失败", e);
            throw new RuntimeException("交存冲正失败", e);
        }
    }
}
