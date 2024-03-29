package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.auth.MD5Helper;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.acc.TaAccService;
import rfm.ta.service.biz.his.TaTxnFdcService;
import rfm.ta.service.biz.his.TaTxnSbsService;

/**发送SBS记账交易
 * Created by Thinkpad on 2015/8/18.
 */
@Service
public class TaFdcService {
    private static final Logger logger = LoggerFactory.getLogger(TaFdcService.class);

    @Autowired
    private TaAccService taAccService;

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    // 建立监管
    /**
     * 发送泰安房产监管系统建立监管交易
     *
     * @param taRsAccPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9901001(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901001 tia9901001Temp=new Tia9901001();
            tia9901001Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901001Temp.header.TX_CODE= taRsAccPara.getTxCode();                    // 01   交易代码       4   1001
            tia9901001Temp.body.SPVSN_BANK_ID= taRsAccPara.getSpvsnBankId();          // 02   监管银行代码   2
            tia9901001Temp.body.CITY_ID= taRsAccPara.getCityId();                      // 03   城市代码       6
            tia9901001Temp.header.BIZ_ID=taRsAccPara.getBizId();                       // 04   监管申请编号   14
            tia9901001Temp.body.SPVSN_ACC_TYPE=taRsAccPara.getSpvsnAccType();         // 05   帐户类别       1   0：预售监管户
            tia9901001Temp.body.SPVSN_ACC_ID=taRsAccPara.getSpvsnAccId();             // 06   监管专户账号    30
            tia9901001Temp.body.SPVSN_ACC_NAME=taRsAccPara.getSpvsnAccName();         // 07   监管专户户名   150
            tia9901001Temp.header.REQ_SN=taRsAccPara.getReqSn();                       // 08   流水号         30
            tia9901001Temp.body.TX_DATE=taRsAccPara.getTxDate();                       // 09   日期           10  送系统日期即可
            tia9901001Temp.body.BRANCH_ID=taRsAccPara.getBranchId();                   // 10   网点号         30
            tia9901001Temp.header.USER_ID=taRsAccPara.getUserId();                     // 11   柜员号         30
            tia9901001Temp.body.INITIATOR=taRsAccPara.getInitiator();                  // 12   发起方         1   1_监管银行

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9901001Temp);
            Toa9901001 toaPara=(Toa9901001) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                taRsAccPara.setFdcSn(toaPara.header.REQ_SN);
                taRsAccPara.setPreSalePerName(toaPara.body.PRE_SALE_PER_NAME);
                taRsAccPara.setPreSaleProAddr(toaPara.body.PRE_SALE_PRO_ADDR);
                taRsAccPara.setPreSaleProName(toaPara.body.PRE_SALE_PRO_NAME);
                taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_SUPV.getCode());
                taAccService.updateRecord(taRsAccPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
                // 已建立监管
                if("1501".equals(taRsAccPara.getReturnCode())){
                    taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_SUPV.getCode());
                    taAccService.updateRecord(taRsAccPara);
                }else{
                    taAccService.deleteRecord(taRsAccPara);
                }
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("建立监管失败！", e);
            throw new RuntimeException("建立监管失败！", e);
        }
    }

    // 解除监管
    /**
     * 发送泰安房产监管系统解除监管交易
     *
     * @param taRsAccPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn9901002(TaRsAcc taRsAccPara) {
        try {
            taRsAccPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            Tia9901002 tia9901002Temp=new Tia9901002() ;
            tia9901002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9901002Temp.header.TX_CODE= taRsAccPara.getTxCode();            // 01   交易代码       4   1002
            tia9901002Temp.body.SPVSN_BANK_ID= taRsAccPara.getSpvsnBankId();  // 02   监管银行代码   2
            tia9901002Temp.body.CITY_ID= taRsAccPara.getCityId();              // 03   城市代码       6
            tia9901002Temp.header.BIZ_ID=taRsAccPara.getBizId();               // 04   终止证明编号  14
            tia9901002Temp.body.SPVSN_ACC_ID=taRsAccPara.getSpvsnAccId();     // 05   监管专户账号  30
            tia9901002Temp.body.SPVSN_ACC_NAME=taRsAccPara.getSpvsnAccName(); // 06   监管专户户名  150
            tia9901002Temp.header.REQ_SN=taRsAccPara.getReqSn();               // 07   流水号        30
            tia9901002Temp.body.TX_DATE=taRsAccPara.getTxDate();               // 08   日期          10  送系统日期即可
            tia9901002Temp.body.BRANCH_ID=taRsAccPara.getBranchId();           // 09   网点号        30
            tia9901002Temp.header.USER_ID=taRsAccPara.getUserId();             // 10   柜员号        30
            tia9901002Temp.body.INITIATOR=taRsAccPara.getInitiator() ;         // 11   发起方        1   1_监管银行
            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9901002Temp);
            Toa9901002 toaPara=(Toa9901002) depMsgSendAndRecv.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_CANCL.getCode());
                taAccService.updateRecord(taRsAccPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taRsAccPara.setReturnCode(toaPara.header.RETURN_CODE);
                taRsAccPara.setReturnMsg(toaPara.header.RETURN_MSG);
                // 已撤销监管
                if("2004".equals(taRsAccPara.getReturnCode())){
                    taRsAccPara.setStatusFlag(EnuTaAccStatus.ACC_CANCL.getCode());
                }
                taAccService.updateRecord(taRsAccPara);
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("解除监管失败", e);
            throw new RuntimeException("解除监管失败", e);
        }
    }

    // 交存交易
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
            taTxnFdcPara.setSpvsnBankId(tia9902001Temp.body.SPVSN_BANK_ID);       // 02   监管银行代码   2
            taTxnFdcPara.setCityId(tia9902001Temp.body.CITY_ID);       // 03   城市代码       6
            taTxnFdcPara.setBizId(tia9902001Temp.header.BIZ_ID);       // 04   交存申请编号   14
            taTxnFdcPara.setReqSn(tia9902001Temp.header.REQ_SN);       // 05   验证流水       30
            taTxnFdcPara.setTxDate(tia9902001Temp.body.TX_DATE);    // 06   验证日期       10  送系统日期即可
            taTxnFdcPara.setBranchId(tia9902001Temp.body.BRANCH_ID);   // 07   验证网点     30
            taTxnFdcPara.setUserId(tia9902001Temp.header.USER_ID);     // 08   验证人员     30
            taTxnFdcPara.setInitiator(tia9902001Temp.body.INITIATOR);  // 09   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            String strMsgid= depMsgSendAndRecv.sendDepMessage(tiaPara);
            Toa9902001 toaPara=(Toa9902001) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                /*01	结果	                4   0000表示成功
                  02	帐户类别	            1   0：监管户；
                  03	交存金额	            20  以分为单位
                  04	监管专户账号            30
                  05    监管专户户名            150
                  06    预售资金监管平台流水    16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setSpvsnAccType(toaPara.body.SPVSN_ACC_TYPE);
                toaPara.body.TX_AMT=ToolUtil.getFinToYuan(toaPara.body.TX_AMT.trim());
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT);
                taTxnFdcPara.setSpvsnAccId(toaPara.body.SPVSN_ACC_ID);
                taTxnFdcPara.setSpvsnAccName(toaPara.body.SPVSN_ACC_NAME);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
            return toaPara;
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
    public TOA sendAndRecvRealTimeTxn9902002(TaTxnFdc taTxnFdcPara) {
        try {
            Tia9902002 tia9902002Temp=new Tia9902002() ;
            tia9902002Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902002Temp.header.TX_CODE=taTxnFdcPara.getTxCode();                // 01   交易代码       4   2002
            tia9902002Temp.body.SPVSN_BANK_ID = taTxnFdcPara.getSpvsnBankId();    // 02   监管银行代码   2
            tia9902002Temp.body.CITY_ID = taTxnFdcPara.getCityId();                // 03   城市代码       6
            tia9902002Temp.header.BIZ_ID = taTxnFdcPara.getBizId();                // 04   交存申请编号   14
            tia9902002Temp.body.TX_AMT = ToolUtil.getYuanToFin(taTxnFdcPara.getTxAmt()).toString();// 08   交存资金       20 交存验证的输出项
            tia9902002Temp.body.SPVSN_ACC_ID = taTxnFdcPara.getSpvsnAccId();      // 06   监管账号       30 交存验证的输出项
            tia9902002Temp.body.STL_TYPE = taTxnFdcPara.getStlType();             // 09   结算方式       2 01_ 现金 02_ 转账 03_ 支票
            tia9902002Temp.body.CHECK_ID = taTxnFdcPara.getCheckId();             // 10   支票号码       30
            tia9902002Temp.header.REQ_SN = taTxnFdcPara.getReqSn();               // 11   银行记账流水号 30
            tia9902002Temp.body.TX_DATE = taTxnFdcPara.getTxDate();               // 12   记账日期       10  送系统日期即可
            tia9902002Temp.body.BRANCH_ID = taTxnFdcPara.getBranchId();          // 13   记账网点号     30
            tia9902002Temp.header.USER_ID = taTxnFdcPara.getUserId();             // 14   记账人员       30
            tia9902002Temp.body.INITIATOR = taTxnFdcPara.getInitiator();          // 15   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902002Temp);
            Toa9902002 toaPara=(Toa9902002) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
            }
            return toaPara;
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
    public TOA sendAndRecvRealTimeTxn9902011(TaTxnFdc taTxnFdcPara) {
        try {
            Tia9902011 tia9902011Temp=new Tia9902011() ;
            tia9902011Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902011Temp.header.TX_CODE = taTxnFdcPara.getTxCode();           // 01   交易代码       4   2011
            tia9902011Temp.body.SPVSN_BANK_ID = taTxnFdcPara.getSpvsnBankId(); // 02   监管银行代码   2
            tia9902011Temp.body.CITY_ID = taTxnFdcPara.getCityId();             // 03   城市代码       6
            tia9902011Temp.header.BIZ_ID = taTxnFdcPara.getBizId();             // 04   交存申请编号   14
            tia9902011Temp.header.REQ_SN = taTxnFdcPara.getReqSn();             // 05   银行冲正流水   30
            tia9902011Temp.body.TX_DATE = taTxnFdcPara.getTxDate();             // 06   冲正日期       10  送系统日期即可
            tia9902011Temp.body.BRANCH_ID = taTxnFdcPara.getBranchId();         // 07   冲正网点       30
            tia9902011Temp.header.USER_ID = taTxnFdcPara.getUserId();           // 08   冲正人员       30
            tia9902011Temp.body.INITIATOR = taTxnFdcPara.getInitiator();        // 09   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902011Temp);
            Toa9902011 toaPara=(Toa9902011) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
            }
            return toaPara;
        } catch (Exception e) {
            logger.error("交存冲正失败", e);
            throw new RuntimeException("交存冲正失败", e);
        }
    }

    // 划拨交易
    /**
     * 发送泰安房产监管系统划拨验证交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902101(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902101 tia9902101Temp=new Tia9902101() ;
            tia9902101Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902101Temp.header.TX_CODE=taTxnFdcPara.getTxCode();          // 01   交易代码       4   2101
            tia9902101Temp.body.SPVSN_BANK_ID=taTxnFdcPara.getSpvsnBankId();// 02   监管银行代码   2
            tia9902101Temp.body.CITY_ID=taTxnFdcPara.getCityId();            // 03   城市代码       6
            tia9902101Temp.header.BIZ_ID=taTxnFdcPara.getBizId();            // 04   划拨业务编号   14
            tia9902101Temp.header.PASSWORD=taTxnFdcPara.getPassword();       // 05   划拨密码       32 MD5
            tia9902101Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 06   验证流水       30
            tia9902101Temp.body.TX_DATE=taTxnFdcPara.getTxDate();            // 07   验证日期       10  送系统日期即可
            tia9902101Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 08   验证网点号     30
            tia9902101Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 09   验证柜员号     30
            tia9902101Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 10   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902101Temp);
            Toa9902101 toaPara=(Toa9902101) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
                taTxnFdcPara.setSpvsnAccId(toaPara.body.SPVSN_ACC_ID);
                taTxnFdcPara.setSpvsnAccName(toaPara.body.SPVSN_ACC_NAME);
                toaPara.body.TX_AMT=ToolUtil.getFinToYuan(toaPara.body.TX_AMT.trim());
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT);
                taTxnFdcPara.setGerlBank(toaPara.body.GERL_BANK);
                taTxnFdcPara.setGerlAccId(toaPara.body.GERL_ACC_ID);
                taTxnFdcPara.setGerlAccName(toaPara.body.GERL_ACC_NAME);
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
            }
        } catch (Exception e) {
            logger.error("划拨验证失败", e);
            throw new RuntimeException("划拨验证失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统划拨记账交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902102(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902102 tia9902102Temp=new Tia9902102() ;
            tia9902102Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902102Temp.header.TX_CODE=taTxnFdcPara.getTxCode();                              // 01   交易代码       4   2102
            tia9902102Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();                   // 02   监管银行代码   2
            tia9902102Temp.body.CITY_ID= taTxnFdcPara.getCityId();                               // 03   城市代码       6
            tia9902102Temp.header.BIZ_ID=taTxnFdcPara.getBizId();                                // 04   划拨业务编号   14
            tia9902102Temp.header.PASSWORD=taTxnFdcPara.getPassword();                           // 05   划拨密码       32 MD5
            tia9902102Temp.body.SPVSN_ACC_ID=taTxnFdcPara.getSpvsnAccId();                       // 06   监管账号       30 划拨验证的输出项
            tia9902102Temp.body.GERL_ACC_ID=taTxnFdcPara.getGerlAccId();                         // 07   收款单位账号   30 划拨验证的输出项
            tia9902102Temp.body.TX_AMT=ToolUtil.getYuanToFin(taTxnFdcPara.getTxAmt()).toString(); // 08   划拨资金       20 划拨验证的输出项
            tia9902102Temp.body.STL_TYPE=taTxnFdcPara.getStlType();                               // 09   结算方式       2 01_ 现金 02_ 转账 03_ 支票
            tia9902102Temp.body.CHECK_ID=taTxnFdcPara.getCheckId();                               // 10   支票号码       30
            tia9902102Temp.header.REQ_SN=taTxnFdcPara.getReqSn();                                 // 11   银行记账流水号 30
            tia9902102Temp.body.TX_DATE=taTxnFdcPara.getTxDate();                                 // 12   记账日期       10  送系统日期即可
            tia9902102Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();                             // 13   记账网点号     30
            tia9902102Temp.header.USER_ID=taTxnFdcPara.getUserId();                               // 14   柜员号         30
            tia9902102Temp.body.INITIATOR=taTxnFdcPara.getInitiator();                            // 15   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902102Temp);
            Toa9902102 toaPara=(Toa9902102) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
            }
        } catch (Exception e) {
            logger.error("划拨记账失败", e);
            throw new RuntimeException("划拨记账失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统划拨冲正交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902111(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902111 tia9902111Temp=new Tia9902111() ;
            tia9902111Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902111Temp.header.TX_CODE=taTxnFdcPara.getTxCode();             // 01   交易代码       4   2102
            tia9902111Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();  // 02   监管银行代码   2
            tia9902111Temp.body.CITY_ID= taTxnFdcPara.getCityId();              // 03   城市代码       6
            tia9902111Temp.header.BIZ_ID=taTxnFdcPara.getBizId();               // 04   划拨业务编号   14
            tia9902111Temp.header.REQ_SN=taTxnFdcPara.getReqSn();               // 05   银行记账流水号 30
            tia9902111Temp.body.TX_DATE=taTxnFdcPara.getTxDate();               // 06   记账日期       10  送系统日期即可
            tia9902111Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();           // 07   记账网点号     30
            tia9902111Temp.header.USER_ID=taTxnFdcPara.getUserId();             // 08   柜员号         30
            tia9902111Temp.body.INITIATOR=taTxnFdcPara.getInitiator();          // 09   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902111Temp);
            Toa9902111 toaPara=(Toa9902111) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
            }
        } catch (Exception e) {
            logger.error("划拨冲正失败", e);
            throw new RuntimeException("划拨冲正失败", e);
        }
    }

    // 返还交易
    /**
     * 发送泰安房产监管系统返还验证交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902201(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902201 tia9902201Temp=new Tia9902201() ;
            tia9902201Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902201Temp.header.TX_CODE=taTxnFdcPara.getTxCode();          // 01   交易代码       4   2201
            tia9902201Temp.body.SPVSN_BANK_ID=taTxnFdcPara.getSpvsnBankId();// 02   监管银行代码   2
            tia9902201Temp.body.CITY_ID=taTxnFdcPara.getCityId();            // 03   城市代码       6
            tia9902201Temp.header.BIZ_ID=taTxnFdcPara.getBizId();            // 04   返还业务编号   14
            tia9902201Temp.header.PASSWORD=taTxnFdcPara.getPassword();       // 05   返还密码       32 MD5
            tia9902201Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 06   验证流水       30
            tia9902201Temp.body.TX_DATE=taTxnFdcPara.getTxDate();            // 07   验证日期       10  送系统日期即可
            tia9902201Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 08   验证网点号     30
            tia9902201Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 09   验证柜员号     30
            tia9902201Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 10   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902201Temp);
            Toa9902201 toaPara=(Toa9902201) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                 /*01	结果	                4   0000表示成功
                  02	监管账号                30
                  03    监管账户户名            150
                  04	返还金额	            20  以分为单位
                  05	业主姓名	            80
                  06	证件类型    	        30
                  07	证件号码                255
                  08    预售资金监管平台流水    16
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setSpvsnAccId(toaPara.body.SPVSN_ACC_ID);
                taTxnFdcPara.setSpvsnAccName(toaPara.body.SPVSN_ACC_NAME);
                toaPara.body.TX_AMT=ToolUtil.getFinToYuan(toaPara.body.TX_AMT.trim());
                taTxnFdcPara.setTxAmt(toaPara.body.TX_AMT);
                taTxnFdcPara.setOwnerName(toaPara.body.OWNER_NAME);
                taTxnFdcPara.setCtficType(toaPara.body.CTFIC_TYPE);
                taTxnFdcPara.setCtficId(toaPara.body.CTFIC_ID);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("返还验证失败", e);
            throw new RuntimeException("返还验证失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统返还记账交易（TA_FDC)
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902202(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902202 tia9902202Temp=new Tia9902202() ;
            tia9902202Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902202Temp.header.TX_CODE=taTxnFdcPara.getTxCode();                               // 01   交易代码       4   2202
            tia9902202Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();                    // 02   监管银行代码   2
            tia9902202Temp.body.CITY_ID= taTxnFdcPara.getCityId();                                // 03   城市代码       6
            tia9902202Temp.header.BIZ_ID=taTxnFdcPara.getBizId();                                 // 04   返还业务编号   14
            tia9902202Temp.header.PASSWORD=taTxnFdcPara.getPassword();                            // 05   返还密码       32 MD5
            tia9902202Temp.body.SPVSN_ACC_ID=taTxnFdcPara.getSpvsnAccId();                        // 06   监管账号       30 返还验证的输出项
            tia9902202Temp.body.TX_AMT=ToolUtil.getYuanToFin(taTxnFdcPara.getTxAmt()).toString();  // 07   返还资金       20 返还验证的输出项
            tia9902202Temp.header.REQ_SN=taTxnFdcPara.getReqSn();                                  // 08   银行记账流水号 30
            tia9902202Temp.body.TX_DATE=taTxnFdcPara.getTxDate();                                  // 09   记账日期       10  送系统日期即可
            tia9902202Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();                              // 10   记账网点号     30
            tia9902202Temp.header.USER_ID=taTxnFdcPara.getUserId();                                // 11   柜员号         30
            tia9902202Temp.body.INITIATOR=taTxnFdcPara.getInitiator();                             // 12   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902202Temp);
            Toa9902202 toaPara=(Toa9902202) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
            }
        } catch (Exception e) {
            logger.error("返还记账失败", e);
            throw new RuntimeException("返还记账失败", e);
        }
    }

    /**
     * 发送泰安房产监管系统返还冲正交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902211(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902211 tia9902211Temp=new Tia9902211() ;
            tia9902211Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902211Temp.header.TX_CODE=taTxnFdcPara.getTxCode();             // 01   交易代码       4   2202
            tia9902211Temp.body.SPVSN_BANK_ID= taTxnFdcPara.getSpvsnBankId();  // 02   监管银行代码   2
            tia9902211Temp.body.CITY_ID= taTxnFdcPara.getCityId();              // 03   城市代码       6
            tia9902211Temp.header.BIZ_ID=taTxnFdcPara.getBizId();               // 04   返还业务编号   14
            tia9902211Temp.header.REQ_SN=taTxnFdcPara.getReqSn();               // 05  银行冲正流水号  30
            tia9902211Temp.body.TX_DATE=taTxnFdcPara.getTxDate();               // 06  记账日期        10  送系统日期即可
            tia9902211Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();           // 07  记账网点号      30
            tia9902211Temp.header.USER_ID=taTxnFdcPara.getUserId();             // 08  柜员号          30
            tia9902211Temp.body.INITIATOR=taTxnFdcPara.getInitiator();          // 09  发起方          1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902211Temp);
            Toa9902211 toaPara=(Toa9902211) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

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
            }
        } catch (Exception e) {
            logger.error("返还冲正失败", e);
            throw new RuntimeException("返还冲正失败", e);
        }
    }

    // 记账结果查询
    /**
     * 记账结果查询
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902501(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setSpvsnBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902501 tia9902501Temp=new Tia9902501() ;
            tia9902501Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902501Temp.header.TX_CODE=taTxnFdcPara.getTxCode();           // 01   交易代码       4   2501
            tia9902501Temp.body.SPVSN_BANK_ID=taTxnFdcPara.getSpvsnBankId(); // 02   监管银行代码   2
            tia9902501Temp.body.CITY_ID=taTxnFdcPara.getCityId();             // 03   城市代码       6
            tia9902501Temp.header.BIZ_ID=taTxnFdcPara.getBizId();             // 04   业务编号       14
            tia9902501Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();         // 05   查询网点       30
            tia9902501Temp.header.USER_ID=taTxnFdcPara.getUserId();           // 06   查询人员       30
            tia9902501Temp.body.INITIATOR=taTxnFdcPara.getInitiator();        // 07   发起方         1   1_监管银行

            taTxnFdcPara.setCreatedBy(taTxnFdcPara.getUserId());

            taTxnFdcService.insertRecord(taTxnFdcPara);

            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902501Temp);
            Toa9902501 toaPara=(Toa9902501) depMsgSendAndRecv.recvDepMessage(strMsgid);

            taTxnFdcPara.setLastUpdBy(taTxnFdcPara.getUserId());

            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                  /*  01    结果	                  4   0000表示成功
                      02    记账结果	              1   0_成功 1_失败
                      03    预售资金监管平台记账流水  16  业务记账流水
                      04    监管银行记账流水	      30  业务记账流水
                      05    预售资金监管平台流水	  16
                  */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setActRstl(toaPara.body.SPVSN_ACT_RSTL);
                taTxnFdcPara.setFdcActSn(toaPara.body.FDC_ACT_SN);
                taTxnFdcPara.setFdcBankActSn(toaPara.body.FDC_BANK_ACT_SN);
                taTxnFdcPara.setFdcSn(toaPara.header.REQ_SN);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }else{
                 /*01	返回结果	    4
                  02	错误原因描述	60
                */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setReturnMsg(toaPara.header.RETURN_MSG);
                taTxnFdcService.updateRecord(taTxnFdcPara);
            }
        } catch (Exception e) {
            logger.error("返还验证失败", e);
            throw new RuntimeException("返还验证失败", e);
        }
    }
}
