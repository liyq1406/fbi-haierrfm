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
public class TaActRsltService {
    private static final Logger logger = LoggerFactory.getLogger(TaActRsltService.class);

    @Autowired
    private TaTxnFdcService taTxnFdcService;
    @Autowired
    private DepService depService;

    /**
     * 发送泰安房产监管系统返还验证交易
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
            tia9902501Temp.header.TX_CODE=taTxnFdcPara.getTxCode();     // 01   交易代码       4   2501
            tia9902501Temp.body.BANK_ID=taTxnFdcPara.getBankId();       // 02   监管银行代码   2
            tia9902501Temp.body.CITY_ID=taTxnFdcPara.getCityId();       // 03   城市代码       6
            tia9902501Temp.header.BIZ_ID=taTxnFdcPara.getBizId();       // 04   业务编号       14
            tia9902501Temp.body.BRANCH_ID=taTxnFdcPara.getBranchId();   // 05   查询网点       30
            tia9902501Temp.header.USER_ID=taTxnFdcPara.getUserId();     // 06   查询人员       30
            tia9902501Temp.body.INITIATOR=taTxnFdcPara.getInitiator();  // 07   发起方         1   1_监管银行
            //通过MQ发送信息到DEP
            taTxnFdcPara.setRecVersion(0);
            taTxnFdcService.insertRecord(taTxnFdcPara);
            String strMsgid=depService.sendDepMessage(tia9902501Temp);
            Toa9902501 toaPara=(Toa9902501)depService.recvDepMessage(strMsgid);
            if(EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toaPara.header.RETURN_CODE)){
                  /*  01    结果	                  4   0000表示成功
                      02    记账结果	              1   0_成功 1_失败
                      03    预售资金监管平台记账流水  16  业务记账流水
                      04    监管银行记账流水	      30  业务记账流水
                      05    预售资金监管平台流水	  16
                  */
                taTxnFdcPara.setReturnCode(toaPara.header.RETURN_CODE);
                taTxnFdcPara.setActRstl(toaPara.body.ACT_RSTL);
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
                logger.error("MQ消息返回失败");
                throw new RuntimeException("MQ消息返回失败");
            }
        } catch (Exception e) {
            logger.error("返还验证失败", e);
            throw new RuntimeException("返还验证失败", e);
        }
    }
}
