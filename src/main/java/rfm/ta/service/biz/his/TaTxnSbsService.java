package rfm.ta.service.biz.his;

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
        taTxnSbsPara.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taTxnSbsPara.setRecVersion(0);
        taTxnSbsMapper.insert(taTxnSbsPara);
    }
}
