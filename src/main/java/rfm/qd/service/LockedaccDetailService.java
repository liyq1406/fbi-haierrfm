package rfm.qd.service;

import rfm.qd.common.constant.SendFlag;
import rfm.qd.repository.dao.RsLockedaccDetailMapper;
import rfm.qd.repository.model.RsLockedaccDetail;
import rfm.qd.repository.model.RsLockedaccDetailExample;
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
    private RsLockedaccDetailMapper lockedaccDetailMapper;

    public boolean isHasUnSend() {
        RsLockedaccDetailExample example = new RsLockedaccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
        if (lockedaccDetailMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public int insertRecord(RsLockedaccDetail record) {
        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        record.setCreatedBy(operId);
        record.setCreatedDate(new Date());
        return lockedaccDetailMapper.insertSelective(record);
    }

    public List<RsLockedaccDetail> selectRecordsBySendflagAndLockstatus(String sendflag, String lockstatus) {
        RsLockedaccDetailExample example = new RsLockedaccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andSendFlagEqualTo(sendflag).andStatusFlagEqualTo(lockstatus);
        return lockedaccDetailMapper.selectByExample(example);
    }

    public List<RsLockedaccDetail> selectRecordsBySendflagAndNotEqualLockstatus(String sendflag, String lockstatus) {
        RsLockedaccDetailExample example = new RsLockedaccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andSendFlagEqualTo(sendflag).andStatusFlagNotEqualTo(lockstatus);
        return lockedaccDetailMapper.selectByExample(example);
    }

    @Transactional
    public int updateRecordToSendflag(RsLockedaccDetail record, String sendflag) {
        record.setSendFlag(sendflag);
        return updateRecord(record);
    }

    public boolean isSent(RsLockedaccDetail record) {
        RsLockedaccDetail originRecord = lockedaccDetailMapper.selectByPrimaryKey(record.getPkId());
        if (SendFlag.SENT.getCode().equalsIgnoreCase(originRecord.getSendFlag())) {
            return true;
        }
        return false;
    }

    public int updateRecord(RsLockedaccDetail record) {
        RsLockedaccDetail originRecord = lockedaccDetailMapper.selectByPrimaryKey(record.getPkId());
        if (!record.getModificationNum().equals(originRecord.getModificationNum())) {
            throw new RuntimeException("并发更新异常！待冻结账号 ：" + record.getAccountCode());
        }
        record.setModificationNum(record.getModificationNum() + 1);
        return lockedaccDetailMapper.updateByPrimaryKeySelective(record);
    }
}
