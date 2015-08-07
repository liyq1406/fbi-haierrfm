package rfm.ta.service.his;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.repository.dao.TaTxnSbsMapper;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.repository.model.TaTxnSbsExample;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015-08-06
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaTxnSbsService {
    private static final Logger logger = LoggerFactory.getLogger(TaTxnSbsService.class);

    @Autowired
    private TaTxnSbsMapper taTxnSbsMapper;

    /**
     * 是否并发更新冲突
     *
     * @param
     * @return
     */
    public boolean isModifiable(TaTxnSbs taTxnSbsPara) {
        TaTxnSbs taTxnSbsTemp = taTxnSbsMapper.selectByPrimaryKey(taTxnSbsPara.getPkId());
        if (!taTxnSbsPara.getRecVersion().equals(taTxnSbsTemp.getRecVersion())) {
            return false;
        }
        return true;
    }

    /**
     * 操作查询
     */
    public TaTxnSbs selectedRecordsByKey(String strTaTxnSbsPara) {
        return taTxnSbsMapper.selectByPrimaryKey(strTaTxnSbsPara);
    }

    /**
     * 操作查询
     */
    public List<TaTxnSbs> selectedAllRecords(TaTxnSbs taTxnSbsPara) {
        TaTxnSbsExample example = new TaTxnSbsExample();
        TaTxnSbsExample.Criteria rsActCrit = example.createCriteria();
        return taTxnSbsMapper.selectByExample(example);
    }
    /**
     * 新增记录
     *
     * @param taTxnSbsPara
     */
    public void insertRecord(TaTxnSbs taTxnSbsPara) {
        String strOperId=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp= ToolUtil.getStrLastUpdTime();
        taTxnSbsPara.setCreatedBy(strOperId);
        taTxnSbsPara.setCreatedTime(strLastUpdTimeTemp);
        taTxnSbsMapper.insert(taTxnSbsPara);
    }

    /**
     * 通过主键更新
     */
    public int updateRecord(TaTxnSbs taTxnSbsPara) {
        if (isModifiable(taTxnSbsPara)) {
            String strOperId=ToolUtil.getOperatorManager().getOperatorId();
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            taTxnSbsPara.setLastUpdBy(strOperId);
            taTxnSbsPara.setLastUpdTime(strLastUpdTimeTemp);
            taTxnSbsPara.setRecVersion(taTxnSbsPara.getRecVersion() + 1);
            return taTxnSbsMapper.updateByPrimaryKeySelective(taTxnSbsPara);
        } else {
            throw new RuntimeException("并发更新冲突！Pkid=" + taTxnSbsPara.getPkId());
        }
    }
}
