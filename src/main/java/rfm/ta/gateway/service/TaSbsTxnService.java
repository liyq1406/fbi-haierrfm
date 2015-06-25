package rfm.ta.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PlatformService;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.security.OperatorManager;
import rfm.qd.service.RsSysctlService;
import rfm.ta.gateway.sbs.CbusSocketClient;
import rfm.ta.gateway.sbs.domain.base.MsgHeader;
import rfm.ta.gateway.sbs.domain.txn.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-25
 * Time: 下午9:28
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaSbsTxnService {
    private static Logger logger = LoggerFactory.getLogger(TaSbsTxnService.class);

    private static final String CBUS_SERVER_IP = PropertyManager.getProperty("bank.core.server.ip");
    private static final int CBUS_SERVER_PORT = PropertyManager.getIntProperty("bank.core.server.port");
    private static final int CBUS_SERVER_TIMEOUT = PropertyManager.getIntProperty("bank.core.server.timeout");
    @Autowired
    private RsSysctlService rsSysctlService;

    // 余额查询 测试帐号 "6228571080001329608"
    public QDJG01Res qdjg01QryActbal(String acccountNo) throws Exception {
        QDJG01Req qdjg01Req = new QDJG01Req();
        MsgHeader header = qdjg01Req.getHeader();
        header.setSerialNo(String.valueOf(rsSysctlService.generateTxnSeq("1")));
        String operId = null;
        /*try {
            OperatorManager om = PlatformService.getOperatorManager();
            operId = om.getOperatorId();
        } catch (Exception e) {*/
        // TODO
        operId = PropertyManager.getProperty("brzfdc.wrd.default.operid");
        // }
        header.setOperId(operId);
        qdjg01Req.accountNo = acccountNo;
        String reqStr = qdjg01Req.toString();
        byte[] rtnBytes = sendUntilRcv(reqStr);
        QDJG01Res qdjg01Res = new QDJG01Res();
        qdjg01Res.assembleFields(rtnBytes);
        return qdjg01Res;
    }

    // 虚柜员 余额查询 FOR 系统自动更新账户余额
/*
    public QDJG05Res qdjg05QryActbal(String acccountNo) throws Exception {
        QDJG05Req qdjg05Req = new QDJG05Req();
        MsgHeader header = qdjg05Req.getHeader();
        header.setOperId(PropertyManager.getProperty("brzfdc.sys.default.operid"));
        header.setSerialNo(String.valueOf(rsSysctlService.generateTxnSeq("1")));
        qdjg05Req.accountNo = acccountNo;
        String reqStr = qdjg05Req.toString();
        byte[] rtnBytes = sendUntilRcv(reqStr);
        QDJG05Res qdjg05Res = new QDJG05Res();
        qdjg05Res.assembleFields(rtnBytes);
        return qdjg05Res;
    }
*/


    public List<QDJG02Res> qdjg02qryActtxnsByParams(String accountNo, String startDate, String endDate)
            throws Exception {
        QDJG02Req qdjg02Req = new QDJG02Req();
        qdjg02Req.accountNo = accountNo;
        qdjg02Req.startDate = startDate;
        qdjg02Req.endDate = endDate;

        String operId = null;
        /*try {
            OperatorManager om = PlatformService.getOperatorManager();
            operId = om.getOperatorId();
        } catch (Exception e) {*/
        operId = PropertyManager.getProperty("brzfdc.wrd.default.operid");
        //}
        qdjg02Req.getHeader().setOperId(operId);
        return qdjg02qryAllActtxnsByReq(qdjg02Req);
    }

    // 查询全部交易明细
    private List<QDJG02Res> qdjg02qryAllActtxnsByReq(QDJG02Req qdjg02Req) throws Exception {
        QDJG02Res qdjg02Res = qdjg02qryOneActtxnsByReq(qdjg02Req);
        List<QDJG02Res> resList = new ArrayList<QDJG02Res>();
        resList.add(qdjg02Res);
        int pageIndex = 1;   // 1- 第二页
        System.out.println("【是否最后一页】" + qdjg02Res.isLast);
        while (!"1".equals(qdjg02Res.isLast)) { // 不是最后一页
            qdjg02Req.firstFlag = String.valueOf(pageIndex);
            qdjg02Req.preFirstKey = qdjg02Res.thisFirstKey;
            qdjg02Req.preLastKey = qdjg02Res.thisLastKey;
//            int newSeqNo = Integer.parseInt(qdjg02Res.recordList.get(qdjg02Res.recordList.size() - 1).seqNo) + 1;
//            qdjg02Req.startSeqNo = String.valueOf(newSeqNo);
            qdjg02Res = qdjg02qryOneActtxnsByReq(qdjg02Req);
            resList.add(qdjg02Res);
            pageIndex++;
        }
        return resList;
    }

    //  单次查询交易明细
    private QDJG02Res qdjg02qryOneActtxnsByReq(QDJG02Req qdjg02Req) throws Exception {

        qdjg02Req.getHeader().setSerialNo(String.valueOf(rsSysctlService.generateTxnSeq("1"))); //  生成交易流水号
        String reqStr = qdjg02Req.toString();
        byte[] rtnBytes = sendUntilRcv(reqStr);
        QDJG02Res qdjg02Res = new QDJG02Res();
        qdjg02Res.assembleFields(rtnBytes);
        return qdjg02Res;
    }

    // 行内转账
    public QDJG03Res qdjg03payAmtInBank(String payOutAct, String payInAct, String payAmt, String remark) throws Exception {
        QDJG03Req qdjg03Req = new QDJG03Req();
        MsgHeader header = qdjg03Req.getHeader();
        try {
            OperatorManager om = PlatformService.getOperatorManager();
            header.setOperId(om.getOperatorId());
        } catch (Exception e) {
            header.setOperId(PropertyManager.getProperty("brzfdc.wrd.default.operid"));
        }
        header.setSerialNo(String.valueOf(rsSysctlService.generateTxnSeq("1"))); //  生成交易流水号
        qdjg03Req.payOutAccount = payOutAct;
        qdjg03Req.payInAccount = payInAct;
        qdjg03Req.payAmt = payAmt;
        qdjg03Req.remark = remark;

        String reqStr = qdjg03Req.toString();
        byte[] rtnBytes = sendUntilRcv(reqStr);
        QDJG03Res qdjg03Res = new QDJG03Res();
        qdjg03Res.assembleFields(rtnBytes);
        return qdjg03Res;
    }

    // 电汇
    /*
    凭证种类  4
    凭证号码  16
    暂时无上述两字段
     */
    public QDJG04Res qdjg04payAmtBtwnBank(String sndToBkNo, String rmtrNameFl, String rmtrAcctNo,
                                          String payeeNameFl, String payeeFlAcctNo, String rmtAmt,
                                          String rmtPurp, String voucherType, String voucherNo, String remark)
            throws Exception {
        QDJG04Req qdjg04Req = new QDJG04Req();
        qdjg04Req.sndToBkNo = sndToBkNo;
        qdjg04Req.rmtrNameFl = rmtrNameFl;
        qdjg04Req.rmtrAcctNo = rmtrAcctNo;
        qdjg04Req.payeeNameFl = payeeNameFl;
        qdjg04Req.payeeFlAcctNo = payeeFlAcctNo;
        qdjg04Req.rmtAmt = rmtAmt;
        qdjg04Req.rmtPurp = rmtPurp;
//        qdjg04Req.voucherType = voucherType;
//        qdjg04Req.voucherNo = voucherNo;
        qdjg04Req.remark = remark;

        return qdjg04payAmtBtwnBankByReq(qdjg04Req);
    }

    // 电汇
    public QDJG04Res qdjg04payAmtBtwnBankByReq(QDJG04Req qdjg04Req) throws Exception {
        MsgHeader header = qdjg04Req.getHeader();
        try {
            OperatorManager om = PlatformService.getOperatorManager();
            header.setOperId(om.getOperatorId());
        } catch (Exception e) {
            header.setOperId(PropertyManager.getProperty("brzfdc.wrd.default.operid"));
        }
        header.setSerialNo(String.valueOf(rsSysctlService.generateTxnSeq("1"))); //  生成交易流水号
        String reqStr = qdjg04Req.toString();
        byte[] rtnBytes = sendUntilRcv(reqStr);
        QDJG04Res qdjg04Res = new QDJG04Res();
        qdjg04Res.assembleFields(rtnBytes);
        return qdjg04Res;
    }

    // ----------------------------------------------------

    private byte[] sendUntilRcv(String strData) throws Exception {
        logger.info("【本地客户端】【请求核心】" + strData);
        logger.info("【本地客户端】【请求核心报文长度】" + strData.getBytes().length);
        CbusSocketClient socketBlockClient = new CbusSocketClient(CBUS_SERVER_IP, CBUS_SERVER_PORT, CBUS_SERVER_TIMEOUT);
        byte[] rtnBytes = socketBlockClient.sendDataUntilRcv(strData.getBytes());
        logger.info("【本地客户端】【核心响应】" + new String(rtnBytes));
        socketBlockClient.close();
        return rtnBytes;
    }
}
