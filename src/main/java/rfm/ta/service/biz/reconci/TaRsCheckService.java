package rfm.ta.service.biz.reconci;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.repository.dao.TaRsCheckMapper;
import rfm.ta.repository.model.TaRsCheck;
import rfm.ta.repository.model.TaRsCheckExample;

import java.util.Date;
import java.util.List;

/**
 * ���˼�¼��
 */
@Service
public class TaRsCheckService {
    private static final Logger logger = LoggerFactory.getLogger(TaRsCheckService.class);

    @Autowired
    private TaRsCheckMapper taRsCheckMapper;

    /**
     * ��ѯ
     * @return
     */
    public List<TaRsCheck> selectRecords() {
        TaRsCheckExample example = new TaRsCheckExample();
        TaRsCheckExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        rsActCrit.andCheckDateEqualTo(ToolUtil.getStrLastUpdDate());
        return taRsCheckMapper.selectByExample(example);
    }

    /**
     * ������¼
     *
     * @param taRsCheckPara
     */
    public void insertRecord(TaRsCheck taRsCheckPara) {
        taRsCheckMapper.insertSelective(taRsCheckPara);
    }

    /**
     * ����
     * @param taRsCheckPara
     */
    public void updateRecord(TaRsCheck taRsCheckPara) {
        taRsCheckMapper.updateByPrimaryKey(taRsCheckPara);
    }
}
