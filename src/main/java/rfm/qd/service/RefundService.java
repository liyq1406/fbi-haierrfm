package rfm.qd.service;

import rfm.qd.common.constant.RefundStatus;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.dao.RsContractMapper;
import rfm.qd.repository.dao.RsRefundMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsRefund;
import rfm.qd.repository.model.RsRefundExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: zhanrui
 * Date: 11-8-25
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RefundService {

    @Autowired
    private RsContractMapper contractMapper;
    @Autowired
    private RsRefundMapper refundMapper;
    @Autowired
    private CommonMapper commonMapper;
    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    @Transactional
    public int insertRecord(RsRefund record) {
        OperatorManager om = SystemService.getOperatorManager();
        record.setCreatedBy(om.getOperatorId());
        record.setCreatedDate(new Date());
        record.setApplyDate(sdf10.format(new Date()));
        record.setApplyUserId(om.getOperatorId());
        record.setApplyUserName(om.getOperatorName());
        String serial = commonMapper.selectMaxRefundSerial();
        record.setSerial(serial);
        record.setBankSerial(serial);
        return refundMapper.insertSelective(record);
    }

    public BigDecimal selectSumPlamount(String businessNo) {
        return commonMapper.selectSumPlamount(businessNo);
    }

    public BigDecimal selectSumPlamountExceptPkid(String pkid) {
        return commonMapper.selectSumPlamountExceptPkid(pkid);
    }

    public List<RsRefund> selectRefundList() {
        RsRefundExample example = new RsRefundExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return refundMapper.selectByExample(example);
    }

    public RsRefund selectRecordByAccDetail(RsAccDetail record) {
        RsRefundExample example = new RsRefundExample();
        example.createCriteria().andPayAccountEqualTo(record.getAccountCode())
                .andRecAccountEqualTo(record.getToAccountCode()).andDeletedFlagEqualTo("0")
                .andApAmountEqualTo(record.getTradeAmt()).andTradeDateEqualTo(record.getTradeDate());
        List<RsRefund> recordList =  refundMapper.selectByExample(example);
        if(recordList.size() > 0) {
            return recordList.get(0);
        }else {
            throw new RuntimeException("没有查询到该笔合同退款记录");
        }
    }

    public boolean isHasUnsend() {
        RsRefundExample example = new RsRefundExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.COMMIT.getCode());
        if (refundMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    public List<RsRefund> selectRefundList(RefundStatus status) {
        RsRefundExample example = new RsRefundExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(status.getCode());
        return refundMapper.selectByExample(example);
    }

    public List<RsRefund> selectRefundList(WorkResult status) {
        RsRefundExample example = new RsRefundExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(status.getCode());
        return refundMapper.selectByExample(example);
    }

    public RsRefund selectRefundByPkid(String pkId) {
        return refundMapper.selectByPrimaryKey(pkId);
    }

    @Transactional
    public int updateRecord(RsRefund record) {
        RsRefund originRecord = selectRefundByPkid(record.getPkId());
        if (!record.getModificationNum().equals(originRecord.getModificationNum())) {
            throw new RuntimeException("并发更新冲突！");
        }
        OperatorManager om = SystemService.getOperatorManager();
        record.setLastUpdBy(om.getOperatorId());
        record.setLastUpdDate(new Date());
        record.setModificationNum(record.getModificationNum() + 1);
        return refundMapper.updateByPrimaryKeySelective(record);
    }

    public List<RsRefund> selectEditList() {
        RsRefundExample example = new RsRefundExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.CREATE.getCode());
        example.or(example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.NOTPASS.getCode()));
        return refundMapper.selectByExample(example);
    }
}
