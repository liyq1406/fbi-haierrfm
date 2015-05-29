package rfm.qd.service;

import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.dao.RsContractMapper;
import rfm.qd.repository.dao.RsReceiveMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsReceive;
import rfm.qd.repository.model.RsReceiveExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.util.Date;
import java.util.List;

/**
 * 主要对应合同收款.
 * User: zhanrui
 * Date: 11-8-25
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ContractRecvService {

    @Autowired
    private RsContractMapper contractMapper;
    @Autowired
    private RsReceiveMapper receiveMapper;
    @Autowired
    private CommonMapper commonMapper;

    public boolean isHasUnsend() {
        RsReceiveExample example = new RsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.COMMIT.getCode());
        if (receiveMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    public int insertRecord(RsReceive record) {
        OperatorManager om = SystemService.getOperatorManager();
        record.setCreatedBy(om.getOperatorId());
        record.setApplyUserId(om.getOperatorId());
        record.setApplyUserName(om.getOperatorName());
        record.setCreatedDate(new Date());
        String serial = commonMapper.selectMaxRecvSerial();
        record.setSerial(serial);
        record.setBankSerial(serial);
        return receiveMapper.insertSelective(record);
    }

    public RsReceive selectRecordByAccDetail(RsAccDetail record) {
        RsReceiveExample example = new RsReceiveExample();
        example.createCriteria().andAccountCodeEqualTo(record.getAccountCode())
                .andTradeAccCodeEqualTo(record.getToAccountCode()).andDeletedFlagEqualTo("0")
                .andApAmountEqualTo(record.getTradeAmt()).andTradeDateEqualTo(record.getTradeDate());
        List<RsReceive> receiveList =  receiveMapper.selectByExample(example);
        if(receiveList.size() > 0) {
            return receiveList.get(0);
        }else {
            throw new RuntimeException("没有查询到该笔合同收款记录");
        }
    }
    public int updateRsReceiveToWorkResult(RsReceive rsReceive, WorkResult workResult) throws Exception {
        RsReceive originRecord = receiveMapper.selectByPrimaryKey(rsReceive.getPkId());
        if (rsReceive.getWorkResult().equalsIgnoreCase(originRecord.getWorkResult())) {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            String operName = om.getOperatorName();
            Date operDate = new Date();
            rsReceive.setTradeDate(SystemService.getSdfdate10());
            rsReceive.setLastUpdBy(operId);
            rsReceive.setLastUpdDate(operDate);
            rsReceive.setWorkResult(workResult.getCode());
            return updateRecord(rsReceive);
        } else {
            throw new RuntimeException("记录并发更新冲突！");
        }

    }

    public int updateRecord(RsReceive record) {
        RsReceive originRecord = receiveMapper.selectByPrimaryKey(record.getPkId());
        if (!originRecord.getModificationNum().equals(originRecord.getModificationNum())) {
            throw new RuntimeException("记录并发更新冲突！");
        }
        record.setModificationNum(record.getModificationNum() + 1);
        return receiveMapper.updateByPrimaryKeySelective(record);
    }

    public List<RsReceive> selectContractRecvList() {
        RsReceiveExample example = new RsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return receiveMapper.selectByExample(example);
    }

    public List<RsReceive> selectEditRecvList() {
        RsReceiveExample example = new RsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.CREATE.getCode());
        example.or(example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.NOTPASS.getCode()));
        return receiveMapper.selectByExample(example);
    }

    public List<RsReceive> selectContractList(WorkResult workResult) {
        RsReceiveExample example = new RsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(workResult.getCode());
        return receiveMapper.selectByExample(example);
    }

    public RsReceive selectContractRecv(String pkId) {
        return receiveMapper.selectByPrimaryKey(pkId);
    }
}
