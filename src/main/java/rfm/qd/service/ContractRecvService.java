package rfm.qd.service;

import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.dao.QdRsContractMapper;
import rfm.qd.repository.dao.QdRsReceiveMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsReceive;
import rfm.qd.repository.model.QdRsReceiveExample;
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
    private QdRsContractMapper contractMapper;
    @Autowired
    private QdRsReceiveMapper receiveMapper;
    @Autowired
    private CommonMapper commonMapper;

    public boolean isHasUnsend() {
        QdRsReceiveExample example = new QdRsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.COMMIT.getCode());
        if (receiveMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    public int insertRecord(QdRsReceive record) {
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

    public QdRsReceive selectRecordByAccDetail(QdRsAccDetail record) {
        QdRsReceiveExample example = new QdRsReceiveExample();
        example.createCriteria().andAccountCodeEqualTo(record.getAccountCode())
                .andTradeAccCodeEqualTo(record.getToAccountCode()).andDeletedFlagEqualTo("0")
                .andApAmountEqualTo(record.getTradeAmt()).andTradeDateEqualTo(record.getTradeDate());
        List<QdRsReceive> receiveList =  receiveMapper.selectByExample(example);
        if(receiveList.size() > 0) {
            return receiveList.get(0);
        }else {
            throw new RuntimeException("没有查询到该笔合同收款记录");
        }
    }
    public int updateRsReceiveToWorkResult(QdRsReceive qdRsReceive, WorkResult workResult) throws Exception {
        QdRsReceive originRecord = receiveMapper.selectByPrimaryKey(qdRsReceive.getPkId());
        if (qdRsReceive.getWorkResult().equalsIgnoreCase(originRecord.getWorkResult())) {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            String operName = om.getOperatorName();
            Date operDate = new Date();
            qdRsReceive.setTradeDate(SystemService.getSdfdate10());
            qdRsReceive.setLastUpdBy(operId);
            qdRsReceive.setLastUpdDate(operDate);
            qdRsReceive.setWorkResult(workResult.getCode());
            return updateRecord(qdRsReceive);
        } else {
            throw new RuntimeException("记录并发更新冲突！");
        }

    }

    public int updateRecord(QdRsReceive record) {
        QdRsReceive originRecord = receiveMapper.selectByPrimaryKey(record.getPkId());
        if (!originRecord.getModificationNum().equals(originRecord.getModificationNum())) {
            throw new RuntimeException("记录并发更新冲突！");
        }
        record.setModificationNum(record.getModificationNum() + 1);
        return receiveMapper.updateByPrimaryKeySelective(record);
    }

    public List<QdRsReceive> selectContractRecvList() {
        QdRsReceiveExample example = new QdRsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return receiveMapper.selectByExample(example);
    }

    public List<QdRsReceive> selectEditRecvList() {
        QdRsReceiveExample example = new QdRsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.CREATE.getCode());
        example.or(example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(WorkResult.NOTPASS.getCode()));
        return receiveMapper.selectByExample(example);
    }

    public List<QdRsReceive> selectContractList(WorkResult workResult) {
        QdRsReceiveExample example = new QdRsReceiveExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andWorkResultEqualTo(workResult.getCode());
        return receiveMapper.selectByExample(example);
    }

    public QdRsReceive selectContractRecv(String pkId) {
        return receiveMapper.selectByPrimaryKey(pkId);
    }
}
