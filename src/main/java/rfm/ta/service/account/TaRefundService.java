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
import platform.auth.MD5Helper;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAccDtl;
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
public class TaRefundService {
    private static final Logger logger = LoggerFactory.getLogger(TaRefundService.class);

    @Autowired
    private TaTxnFdcService taTxnFdcService;

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    /**
     * 发送泰安房产监管系统返还验证交易
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public void sendAndRecvRealTimeTxn9902201(TaTxnFdc taTxnFdcPara) {
        try {
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902201 tia9902201Temp=new Tia9902201() ;
            tia9902201Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902201Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   交易代码       4   2201
            tia9902201Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   监管银行代码   2
            tia9902201Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   城市代码       6
            tia9902201Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   返还业务编号   14
            tia9902201Temp.header.PASSWORD=taTxnFdcPara.getPassword();  // 05   返还密码       32 MD5
            tia9902201Temp.header.REQ_SN=taTxnFdcPara.getReqSn();       // 06   验证流水       30
            tia9902201Temp.body.TX_DATE=taTxnFdcPara.getTxDate();    // 07   验证日期       10  送系统日期即可
            tia9902201Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 08   验证网点号     30
            tia9902201Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 09   验证柜员号     30
            tia9902201Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 10   发起方         1   1_监管银行

            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902201Temp);
            Toa9902201 toaPara=(Toa9902201) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
                taTxnFdcPara.setAccId(toaPara.body.ACC_ID);
                taTxnFdcPara.setAccName(toaPara.body.ACC_NAME);
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
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setPassword(MD5Helper.getMD5String(ToolUtil.TAFDC_MD5_KEY));
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902202 tia9902202Temp=new Tia9902202() ;
            tia9902202Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902202Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   交易代码       4   2202
            tia9902202Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   监管银行代码   2
            tia9902202Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   城市代码       6
            tia9902202Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   返还业务编号   14
            tia9902202Temp.header.PASSWORD=taTxnFdcPara.getPassword();      // 05   返还密码       32 MD5
            tia9902202Temp.body.ACC_ID=taTxnFdcPara.getAccId();              // 06   监管账号       30 返还验证的输出项
            tia9902202Temp.body.TX_AMT=taTxnFdcPara.getTxAmt().toString();   // 07   返还资金       20 返还验证的输出项
            tia9902202Temp.header.REQ_SN=taTxnFdcPara.getReqSn();            // 08   银行记账流水号 30
            tia9902202Temp.body.TX_DATE=taTxnFdcPara.getTxDate();         // 09   记账日期       10  送系统日期即可
            tia9902202Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();        // 10   记账网点号     30
            tia9902202Temp.header.USER_ID=taTxnFdcPara.getUserId();          // 11   柜员号         30
            tia9902202Temp.body.INITIATOR=taTxnFdcPara.getInitiator();       // 12   发起方         1   1_监管银行

            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902202Temp);
            Toa9902202 toaPara=(Toa9902202) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
            taTxnFdcPara.setBankId(EnuTaBankId.BANK_HAIER.getCode());
            taTxnFdcPara.setCityId(EnuTaCityId.CITY_TAIAN.getCode());
            taTxnFdcPara.setReqSn(ToolUtil.getStrAppReqSn_Back());
            taTxnFdcPara.setTxDate(ToolUtil.getStrLastUpdDate());
            taTxnFdcPara.setBranchId(ToolUtil.getOperatorManager().getOperator().getDeptid());
            taTxnFdcPara.setUserId(ToolUtil.getOperatorManager().getOperatorId());
            taTxnFdcPara.setInitiator(EnuTaInitiatorId.INITIATOR.getCode());

            Tia9902211 tia9902211Temp=new Tia9902211() ;
            tia9902211Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_RFM;
            tia9902211Temp.header.TX_CODE=taTxnFdcPara.getTxCode();         // 01   交易代码       4   2202
            tia9902211Temp.body.BANK_ID= taTxnFdcPara.getBankId();          // 02   监管银行代码   2
            tia9902211Temp.body.CITY_ID= taTxnFdcPara.getCityId();          // 03   城市代码       6
            tia9902211Temp.header.BIZ_ID=taTxnFdcPara.getBizId();           // 04   返还业务编号   14
            tia9902211Temp.header.REQ_SN=taTxnFdcPara.getReqSn();           // 05  银行冲正流水号  30
            tia9902211Temp.body.TX_DATE=taTxnFdcPara.getTxDate();        // 06  记账日期        10  送系统日期即可
            tia9902211Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();       // 07  记账网点号      30
            tia9902211Temp.header.USER_ID=taTxnFdcPara.getUserId();         // 08  柜员号          30
            tia9902211Temp.body.INITIATOR=taTxnFdcPara.getInitiator();      // 09  发起方          1   1_监管银行

            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia9902211Temp);
            Toa9902211 toaPara=(Toa9902211) depMsgSendAndRecv.recvDepMessage(strMsgid);
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
            logger.error("返还冲正失败", e);
            throw new RuntimeException("返还冲正失败", e);
        }
    }
}
