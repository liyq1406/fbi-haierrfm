package rfm.ta.service.dep;

import common.utils.ToolUtil;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.EnuTaFdcTxCode;
import rfm.ta.common.enums.EnuTaSbsTxCode;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.his.TaTxnSbsService;

import java.util.ArrayList;
import java.util.List;

/**发送SBS记账交易
 * Created by Thinkpad on 2015/8/18.
 */
@Service
public class TaSbsService {
    private static final Logger logger = LoggerFactory.getLogger(TaSbsService.class);

    @Autowired
    private TaTxnSbsService taTxnSbsService;

    @Autowired
    private DepMsgSendAndRecv depMsgSendAndRecv;

    // 发送SBS系统交存记账交易
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

    // 查询SBS日间总笔数
    /**
     * 查询SBS日间总笔数
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012601(TaTxnFdc taTxnFdcPara) {
        try {
            Tia900012601 tia900012601Temp=new Tia900012601();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaFdcTxCode.TRADE_2601.getCode());
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn());      // 外围系统流水
            taTxnSbsPara.setTxDate(taTxnFdcPara.getTxDate());    // 交易日期
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());    // 柜员号

            tia900012601Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            tia900012601Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900012601Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900012601Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900012601Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012601Temp);
            TOA toaPara = depMsgSendAndRecv.recvDepMessage(strMsgid);
            return toaPara;
        } catch (Exception e) {
            logger.error("从SBS日间总数对账查询失败", e);
            throw new RuntimeException("从SBS日间总数对账查询失败", e);
        }
    }

    // 查询SBS日间记账流水
    /**
     * 查询SBS日间记账流水
     *
     * @param taTxnFdcPara
     */
    @Transactional
    public TOA sendAndRecvRealTimeTxn900012602(TaTxnFdc taTxnFdcPara,String strBeginNumPara) {
        try {
            Tia900012602 tia900012602Temp=new Tia900012602();
            TaTxnSbs taTxnSbsPara=new TaTxnSbs();
            taTxnSbsPara.setTxCode(EnuTaFdcTxCode.TRADE_2602.getCode());
            taTxnSbsPara.setReqSn(taTxnFdcPara.getReqSn());           // 外围系统流水
            taTxnSbsPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));    // 交易日期
            taTxnSbsPara.setUserId(taTxnFdcPara.getUserId());         // 柜员号

            tia900012602Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS;
            tia900012602Temp.body.TX_DATE=taTxnSbsPara.getTxDate();
            tia900012602Temp.body.BEGNUM=strBeginNumPara;
            tia900012602Temp.header.REQ_SN=taTxnSbsPara.getReqSn();
            tia900012602Temp.header.USER_ID=taTxnSbsPara.getUserId();
            tia900012602Temp.header.TX_CODE=taTxnSbsPara.getTxCode();

            //通过MQ发送信息到DEP
            String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012602Temp);
            TOA toaPara = depMsgSendAndRecv.recvDepMessage(strMsgid);
            return toaPara;
        } catch (Exception e) {
            logger.error("从SBS日间总数对账查询失败", e);
            throw new RuntimeException("从SBS日间总数对账查询失败", e);
        }
    }

    // 发送监管账号到SBS查询余额
    /**
     * 发送监管账号到SBS查询余额
     *
     * @param taRsAccList
     */
    @Transactional
    public List<Toa900012701> sendAndRecvRealTimeTxn900012701(List<TaRsAcc> taRsAccList) {
        try {
            int pageCount = 0; // 总页数
            int pageSize = 3; // 每页记录个数
            int totalCount = taRsAccList.size(); // 总条数
            int loopCount = 0; // 循环条件

            Tia900012701 tia900012701Temp = new Tia900012701();
            tia900012701Temp.header.CHANNEL_ID=ToolUtil.DEP_CHANNEL_ID_SBS; // 渠道
            tia900012701Temp.header.TX_CODE=EnuTaFdcTxCode.TRADE_2701.getCode(); // 交易代码

            if(totalCount % pageSize == 0){
                pageCount = totalCount / pageSize;
            } else {
                pageCount = totalCount / pageSize + 1;
            }

            List<Toa900012701> toaParas = null;
            List<Tia900012701.BodyDetail> details = null;
            Tia900012701.BodyDetail bodyDetail = null;
            for(int i=0; i<pageCount;i++){
                details = new ArrayList<Tia900012701.BodyDetail>();
                if(i == pageCount-1){ // 最后一页的处理
                    loopCount = totalCount;
                } else{
                    loopCount = (i+1)*pageSize;
                }
                for(int j=i*pageSize;j<loopCount;j++){
                    bodyDetail = new Tia900012701.BodyDetail();
                    bodyDetail.setACTNM(taRsAccList.get(j).getAccId());
                    details.add(bodyDetail);
                }

                tia900012701Temp.body.DETAILS = details;

                //通过MQ发送信息到DEP
                String strMsgid= depMsgSendAndRecv.sendDepMessage(tia900012701Temp);
                Toa900012701 toaPara=(Toa900012701) depMsgSendAndRecv.recvDepMessage(strMsgid);
                toaParas.add(toaPara);
            }

            return toaParas;
        } catch (Exception e) {
            logger.error("发送监管账号到SBS查询余额失败", e);
            throw new RuntimeException("发送监管账号到SBS查询余额失败", e);
        }
    }
}
