package rfm.qd.service;

import rfm.qd.common.constant.RefundStatus;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.dao.QdRsPayoutMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsPayout;
import rfm.qd.repository.model.QdRsPayoutExample;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import rfm.qd.view.payout.ParamPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.text.SimpleDateFormat;
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
public class PayoutService {
    @Autowired
    private QdRsPayoutMapper qdRsPayoutMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private ExpensesPlanService expensesPlanService;
    @Autowired
    private TradeService tradeService;
    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    public QdRsPayout selectPayoutByPkid(String pkid) {
        return qdRsPayoutMapper.selectByPrimaryKey(pkid);
    }

    public boolean isHasUnSend() {
        QdRsPayoutExample example = new QdRsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.COMMIT.getCode());
        if (qdRsPayoutMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    public int updateRsPayout(QdRsPayout qdRsPayout) {
        QdRsPayout originRecord = qdRsPayoutMapper.selectByPrimaryKey(qdRsPayout.getPkId());
        if (!originRecord.getModificationNum().equals(qdRsPayout.getModificationNum())) {
            throw new RuntimeException("记录并发更新冲突，请重试！");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            qdRsPayout.setLastUpdBy(operId);
            qdRsPayout.setLastUpdDate(new Date());
            qdRsPayout.setModificationNum(qdRsPayout.getModificationNum() + 1);
            return qdRsPayoutMapper.updateByPrimaryKey(qdRsPayout);
        }
    }

    /**
     * @param qdRsPayout
     * @return
     */
    public int insertRsPayout(QdRsPayout qdRsPayout) {
        OperatorManager om = SystemService.getOperatorManager();
        qdRsPayout.setApplyUserId(om.getOperatorId());
        qdRsPayout.setApplyUserName(om.getOperatorName());
        qdRsPayout.setApplyDate(sdf10.format(new Date()));
        qdRsPayout.setCreatedBy(om.getOperatorId());
        qdRsPayout.setCreatedDate(new Date());
        return qdRsPayoutMapper.insertSelective(qdRsPayout);
    }

    @Transactional
    public int updateRsPayoutsToWorkResult(QdRsPayout[] qdRsPayoutList, String workResult) {
        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        String operName = om.getOperatorName();
        String operDate = sdf10.format(new Date());
        int rtnFlag = 1;
        for (QdRsPayout qdRsPayout : qdRsPayoutList) {
            qdRsPayout.setAuditDate(operDate);
            qdRsPayout.setAuditUserId(operId);
            qdRsPayout.setAuditUserName(operName);
            qdRsPayout.setWorkResult(workResult);
            if (updateRsPayout(qdRsPayout) != 1) {
                rtnFlag = -1;
                throw new RuntimeException("【记录更新失败】付款监管账号：" + qdRsPayout.getPayAccount());
            }
        }
        return rtnFlag;
    }

    @Transactional
    public int updateRsPayoutToExec(QdRsPayout qdRsPayout) {
        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        String operName = om.getOperatorName();
        String operDate = sdf10.format(new Date());
        qdRsPayout.setExecUserId(operId);
        qdRsPayout.setExecUserName(operName);
        qdRsPayout.setExecDate(operDate);
        qdRsPayout.setTradeDate(operDate);
        qdRsPayout.setStatusFlag(RefundStatus.ACCOUNT_SUCCESS.getCode());
        qdRsPayout.setWorkResult(WorkResult.COMMIT.getCode());
        qdRsPayout.setSerial(commonMapper.selectMaxPayoutSerial());
        qdRsPayout.setBankSerial(qdRsPayout.getSerial());
        return tradeService.handlePayoutTrade(qdRsPayout) + updateRsPayout(qdRsPayout);
    }

    public List<QdRsPayout> selectRecordsByWorkResult(String workResultCode) {
        QdRsPayoutExample example = new QdRsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(workResultCode);
        return qdRsPayoutMapper.selectByExample(example);
    }

    public List<QdRsPayout> qryCheckPayouts() {
        QdRsPayoutExample example = new QdRsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andWorkResultEqualTo(WorkResult.CREATE.getCode());
        example.or(example.createCriteria().andDeletedFlagEqualTo("0")
                .andWorkResultEqualTo(WorkResult.RE_CHECK.getCode()));
        return qdRsPayoutMapper.selectByExample(example);
    }

    public List<QdRsPayout> selectRsPayoutsByParamPlan(ParamPlan paramPlan) {
        return commonMapper.selectRsPayoutsByParamPlan(paramPlan);
    }

    @Transactional
    public int updateRsPayoutSent(QdRsPayout qdRsPayout) {
        qdRsPayout.setWorkResult(WorkResult.SENT.getCode());
        return updateRsPayout(qdRsPayout);
    }

    public QdRsPayout selectRecordByAccDetail(QdRsAccDetail record) {
        QdRsPayoutExample example = new QdRsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andPayAccountEqualTo(record.getAccountCode()).andRecAccountEqualTo(record.getToAccountCode())
                .andApAmountEqualTo(record.getTradeAmt()).andTradeDateEqualTo(record.getTradeDate());
        List<QdRsPayout> payoutList = qdRsPayoutMapper.selectByExample(example);
        if (payoutList.size() > 0) {
            return payoutList.get(0);
        } else {
            throw new RuntimeException("没有查询到该笔计划付款记录！");
        }

    }

    public List<QdRsPayout> selectEditRecords() {
        QdRsPayoutExample example = new QdRsPayoutExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andWorkResultEqualTo(WorkResult.CREATE.getCode());
        example.or(example.createCriteria().andDeletedFlagEqualTo("0")
                .andWorkResultEqualTo(WorkResult.NOTPASS.getCode()));
        example.or(example.createCriteria().andDeletedFlagEqualTo("0")
                        .andWorkResultEqualTo(WorkResult.RE_CHECK.getCode()));
        return qdRsPayoutMapper.selectByExample(example);
    }
}
