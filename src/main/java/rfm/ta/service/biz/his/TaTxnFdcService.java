package rfm.ta.service.biz.his;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.repository.dao.TaTxnFdcMapper;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnFdcExample;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 2015-08-06
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaTxnFdcService {
    private static final Logger logger = LoggerFactory.getLogger(TaTxnFdcService.class);

    @Autowired
    private TaTxnFdcMapper taTxnFdcMapper;

    /**
     * 是否并发更新冲突
     *
     * @param
     * @return
     */
    public boolean isModifiable(TaTxnFdc taTxnFdcPara) {
        TaTxnFdc taTxnFdcTemp = taTxnFdcMapper.selectByPrimaryKey(taTxnFdcPara.getPkId());
        if (!taTxnFdcPara.getRecVersion().equals(taTxnFdcTemp.getRecVersion())) {
            return false;
        }
        return true;
    }

    /**
     * 操作查询
     */
    public TaTxnFdc selectedRecordsByKey(String strTaTxnFdcPara) {
        return taTxnFdcMapper.selectByPrimaryKey(strTaTxnFdcPara);
    }

    /**
     * 操作查询
     */
    public List<TaTxnFdc> selectedAllRecords(TaTxnFdc taTxnFdcPara) {
        TaTxnFdcExample example = new TaTxnFdcExample();
        TaTxnFdcExample.Criteria rsActCrit = example.createCriteria();
        if (ToolUtil.getStrIgnoreNull(taTxnFdcPara.getTxCode()).trim().length()!=0) {
            rsActCrit.andTxCodeLike("%"+taTxnFdcPara.getTxCode()+"%");
        }
        if (ToolUtil.getStrIgnoreNull(taTxnFdcPara.getBizId()).trim().length()!=0) {
            rsActCrit.andBizIdLike("%"+taTxnFdcPara.getBizId().trim()+"%");
        }
        if (ToolUtil.getStrIgnoreNull(taTxnFdcPara.getTxDate()).trim().length()!=0) {
            rsActCrit.andTxDateEqualTo(taTxnFdcPara.getTxDate().trim());
        }
        return taTxnFdcMapper.selectByExample(example);
    }
    /**
     * 新增记录
     *
     * @param taTxnFdcPara
     */
    public void insertRecord(TaTxnFdc taTxnFdcPara) {
        taTxnFdcPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taTxnFdcPara.setRecVersion(0);
        taTxnFdcMapper.insert(taTxnFdcPara);
    }

    /**
     * 通过主键更新
     */
    public int updateRecord(TaTxnFdc taTxnFdcPara) {
        if (isModifiable(taTxnFdcPara)) {
            taTxnFdcPara.setLastUpdTime(ToolUtil.getStrLastUpdTime());
            taTxnFdcPara.setRecVersion(taTxnFdcPara.getRecVersion() + 1);
            return taTxnFdcMapper.updateByPrimaryKeySelective(taTxnFdcPara);
        } else {
            throw new RuntimeException("并发更新冲突！Pkid=" + taTxnFdcPara.getPkId());
        }
    }
}
