package rfm.qd.service;

import rfm.qd.common.constant.*;
import rfm.qd.gateway.cbus.domain.txn.QDJG03Res;
import rfm.qd.gateway.cbus.domain.txn.QDJG04Res;
import rfm.qd.gateway.service.CbusTxnService;
import rfm.qd.repository.dao.RsPayoutMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsPayout;
import rfm.qd.repository.model.RsPayoutExample;
import rfm.qd.repository.model.RsPlanCtrl;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.security.OperatorManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-30
 * Time: 下午3:46
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CbusPayoutService {

    private static Logger logger = LoggerFactory.getLogger(CbusPayoutService.class);

    @Autowired
    private RsPayoutMapper rsPayoutMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ExpensesPlanService expensesPlanService;
    @Autowired
    private RsAccDetailService rsAccDetailService;
    @Autowired
    private CbusTxnService cbusTxnService;

    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public int updateRsPayout(RsPayout rsPayout) {
        RsPayout originRecord = rsPayoutMapper.selectByPrimaryKey(rsPayout.getPkId());
        if (!originRecord.getModificationNum().equals(rsPayout.getModificationNum())) {
            logger.info("记录更新版本号：" + rsPayout.getModificationNum());
            logger.info("记录原版本号：" + originRecord.getModificationNum());
            throw new RuntimeException("记录并发更新冲突，请重试！");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            rsPayout.setLastUpdBy(operId);
            rsPayout.setLastUpdDate(new Date());
            rsPayout.setModificationNum(rsPayout.getModificationNum() + 1);
            return rsPayoutMapper.updateByPrimaryKey(rsPayout);
        }
    }

    @Transactional
    public int updateRsPayoutToExec(RsPayout rsPayout) throws Exception {

        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        String operName = om.getOperatorName();
        String operDate = sdf10.format(new Date());
        rsPayout.setExecUserId(operId);
        rsPayout.setExecUserName(operName);
        rsPayout.setExecDate(operDate);
        rsPayout.setTradeDate(operDate);
        rsPayout.setStatusFlag(RefundStatus.ACCOUNT_SUCCESS.getCode());
        rsPayout.setWorkResult(WorkResult.COMMIT.getCode());
        rsPayout.setSerial(commonMapper.selectMaxPayoutSerial());
        rsPayout.setBankSerial(rsPayout.getSerial());
        // 相应计划扣款
        RsPlanCtrl planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(rsPayout.getBusinessNo());
        planCtrl.setAvAmount(planCtrl.getAvAmount().subtract(rsPayout.getApAmount()));

        //======== 开始付款 ===========
        //--------------------------------------------------
        // 新增交易明细
        RsAccDetail accDetail = new RsAccDetail();
        accDetail.setAccountCode(rsPayout.getPayAccount());
        accDetail.setAccountName(rsPayout.getPayCompanyName());
        accDetail.setToAccountCode(rsPayout.getRecAccount());
        accDetail.setToAccountName(rsPayout.getRecCompanyName());
        accDetail.setToHsBankName(rsPayout.getRecBankName());
        accDetail.setInoutFlag(InOutFlag.OUT.getCode());// 支出
        accDetail.setTradeDate(sdf10.format(new Date()));
        accDetail.setTradeAmt(rsPayout.getApAmount());
        accDetail.setBalance(new BigDecimal("0.00"));
        accDetail.setLocalSerial(rsPayout.getSerial());
        accDetail.setBankSerial(rsPayout.getBankSerial());
        accDetail.setTradeType(TradeType.PLAN_PAYOUT.getCode());
        accDetail.setPlanCtrlNo(rsPayout.getBusinessNo());
        accDetail.setRemark(rsPayout.getRemark());
        accDetail.setStatusFlag(TradeStatus.SUCCESS.getCode());
        //-----------------------------------------------------
        // 设置账户余额 和可用余额

        // 是否核心记账
        if ("cbus".equals(PropertyManager.getProperty("bank.act.flag"))) {
            //  行内转账
            if ("10".equals(rsPayout.getTransType())) {
                try {
                    QDJG03Res res03 = cbusTxnService.qdjg03payAmtInBank(rsPayout.getPayAccount(), rsPayout.getRecAccount(),
                            String.format("%.2f", rsPayout.getPlAmount()), rsPayout.getPurpose());

                    if ("00".equals(res03.getHeader().getRtnCode())) {
                        expensesPlanService.updatePlanCtrl(planCtrl);
                        rsAccDetailService.insertAccDetail(accDetail);
                        rsPayout.setBankSerial(res03.serialNo);
                        updateRsPayout(rsPayout);
                        return 1;
                    } else if ("99".equals(res03.getHeader().getRtnCode())) {
                        rsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        rsPayout.setWorkResult(WorkResult.PASS.getCode());
                        updateRsPayout(rsPayout);
                        throw new RuntimeException("交易失败！" + res03.rtnMsg);
                    } else {
                        rsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        rsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                        updateRsPayout(rsPayout);
                        throw new RuntimeException("交易结果不明！" + res03.rtnMsg);
                    }
                } catch (Exception e) {
                    rsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                    rsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                    updateRsPayout(rsPayout);
                    throw new RuntimeException("交易结果不明！" + e.getMessage() == null ? "" : e.getMessage());
                }
            }
            //  跨行转账
            /*
           String sndToBkNo, String rmtrNameFl, String rmtrAcctNo,
           String payeeNameFl, String payeeFlAcctNo, String rmtAmt, String rmtPurp
            */
            if ("20".equals(rsPayout.getTransType())) {
                try {
                    QDJG04Res res04 = cbusTxnService.qdjg04payAmtBtwnBank(rsPayout.getRecBankCode(),
                            rsPayout.getPayCompanyName(), rsPayout.getPayAccount(),
                            rsPayout.getRecCompanyName(), rsPayout.getRecAccount(),
                            String.format("%.2f", rsPayout.getPlAmount()), rsPayout.getPurpose(),
                            rsPayout.getVoucherType(), rsPayout.getDocNo(),
                            StringUtils.isEmpty(rsPayout.getRemark()) ? rsPayout.getPurpose() : rsPayout.getRemark());
                    if ("00".equals(res04.getHeader().getRtnCode())) {
                        expensesPlanService.updatePlanCtrl(planCtrl);
                        rsAccDetailService.insertAccDetail(accDetail);
                        rsPayout.setBankSerial(res04.txnSerailNo);
                        rsPayout.setWfInstanceId(res04.fmTrntAmtSqNo);
                        updateRsPayout(rsPayout);
                        return 1;
                    } else if ("99".equals(res04.getHeader().getRtnCode())) {
                        rsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        rsPayout.setWorkResult(WorkResult.PASS.getCode());
                        updateRsPayout(rsPayout);
                        throw new RuntimeException("交易失败！" + res04.rtnMsg);
                    } else {
                        rsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                        rsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                        updateRsPayout(rsPayout);
                        throw new RuntimeException("交易结果不明！" + res04.rtnMsg);
                    }
                } catch (Exception e) {
                    rsPayout.setStatusFlag(RefundStatus.ACCOUNT_FAILURE.getCode());
                    rsPayout.setWorkResult(WorkResult.NOT_KNOWN.getCode());
                    updateRsPayout(rsPayout);
                    throw new RuntimeException("交易结果不明！" + e.getMessage() == null ? "" : e.getMessage());
                }
            }
        } else {
            expensesPlanService.updatePlanCtrl(planCtrl);
            rsAccDetailService.insertAccDetail(accDetail);
            updateRsPayout(rsPayout);
            return 1;
        }

        return -1;
    }

    public List<RsPayout> selectRecordsByWorkResult(String workResultCode) {
        RsPayoutExample example = new RsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(workResultCode);
        example.setOrderByClause(" CREATED_DATE DESC ");
        return rsPayoutMapper.selectByExample(example);
    }

    public List<RsPayout> qryPrintVchrPayouts() {
        RsPayoutExample example = new RsPayoutExample();
        List<String> printWRnums = new ArrayList<String>();
        printWRnums.add(WorkResult.COMMIT.getCode());
        printWRnums.add(WorkResult.SENT.getCode());
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultIn(printWRnums);
        example.setOrderByClause(" CREATED_DATE desc ");
        return rsPayoutMapper.selectByExample(example);
    }
}
