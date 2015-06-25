package rfm.qd.service;

import rfm.qd.common.constant.SendFlag;
import rfm.qd.repository.dao.QdRsLockedaccDetailMapper;
import rfm.qd.repository.model.QdRsLockedaccDetail;
import rfm.qd.repository.model.QdRsLockedaccDetailExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-5
 * Time: 下午5:11
 * To change this template use File | Settings | File Templates.
 */
@Service
public class LockedaccDetailService {
    @Autowired
    private QdRsLockedaccDetailMapper lockedaccDetailMapper;

    public boolean isHasUnSend() {
        QdRsLockedaccDetailExample example = new QdRsLockedaccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
        if (lockedaccDetailMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public int insertRecord(QdRsLockedaccDetail record) {
        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        record.setCreatedBy(operId);
        record.setCreatedDate(new Date());
        return lockedaccDetailMapper.insertSelective(record);
    }

    public List<QdRsLockedaccDetail> selectRecordsBySendflagAndLockstatus(String sendflag, String lockstatus) {
        QdRsLockedaccDetailExample example = new QdRsLockedaccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andSendFlagEqualTo(sendflag).andStatusFlagEqualTo(lockstatus);
        return lockedaccDetailMapper.selectByExample(example);
    }

    public List<QdRsLockedaccDetail> selectRecordsBySendflagAndNotEqualLockstatus(String sendflag, String lockstatus) {
        QdRsLockedaccDetailExample example = new QdRsLockedaccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andSendFlagEqualTo(sendflag).andStatusFlagNotEqualTo(lockstatus);
        return lockedaccDetailMapper.selectByExample(example);
    }

    @Transactional
    public int updateRecordToSendflag(QdRsLockedaccDetail record, String sendflag) {
        record.setSendFlag(sendflag);
        return updateRecord(record);
    }

    public boolean isSent(QdRsLockedaccDetail record) {
        QdRsLockedaccDetail originRecord = lockedaccDetailMapper.selectByPrimaryKey(record.getPkId());
        if (SendFlag.SENT.getCode().equalsIgnoreCase(originRecord.getSendFlag())) {
            return true;
        }
        return false;
    }

    public int updateRecord(QdRsLockedaccDetail record) {
        QdRsLockedaccDetail originRecord = lockedaccDetailMapper.selectByPrimaryKey(record.getPkId());
        if (!record.getModificationNum().equals(originRecord.getModificationNum())) {
            throw new RuntimeException("并发更新异常！待冻结账号 ：" + record.getAccountCode());
        }
        record.setModificationNum(record.getModificationNum() + 1);
        return lockedaccDetailMapper.updateByPrimaryKeySelective(record);
    }
}
