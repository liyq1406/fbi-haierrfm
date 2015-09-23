package rfm.ta.service.biz.reconci;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.common.enums.EnuClassifyFlag;
import rfm.ta.common.enums.EnuTaArchivedFlag;
import rfm.ta.repository.dao.TaRsCheckMapper;
import rfm.ta.repository.model.TaRsCheck;
import rfm.ta.repository.model.TaRsCheckExample;

import java.util.Date;
import java.util.List;

/**
 * 对账记录表
 */
@Service
public class TaRsCheckService {
    private static final Logger logger = LoggerFactory.getLogger(TaRsCheckService.class);

    @Autowired
    private TaRsCheckMapper taRsCheckMapper;

    /**
     * 插入或更新对账明细表
     * @param statusFlag
     */
    public void insOrUpdTaRsCheck(String statusFlag) {
        // 插入或者更新对账记录表
        List<TaRsCheck> taRsCheckList = getTodayCheckRecords();
        if(taRsCheckList == null || taRsCheckList.size() == 0) {
            Date sysdate = new Date();
            TaRsCheck taRsCheckTemp = new TaRsCheck();
            taRsCheckTemp.setCheckDate(ToolUtil.getStrLastUpdDate());                   // 对账日期
            taRsCheckTemp.setStatusFlag(statusFlag);                                    // 状态标志
            taRsCheckTemp.setClassifyFlag(EnuClassifyFlag.CLASSIFY_FLAG2.getCode());  // 对账分类
            taRsCheckTemp.setCheckTime(ToolUtil.getStrNowTime());                       // 勾对时间
            taRsCheckTemp.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG0.getCode()); // 记录删除标志
            taRsCheckTemp.setCreatedDate(sysdate);                                      // 创建时间
            taRsCheckTemp.setLastUpdDate(sysdate);                                      // 最近修改时间
            taRsCheckTemp.setModificationNum(0);                                        // 修改次数
            insertRecord(taRsCheckTemp);
        } else {
            TaRsCheck taRsCheckTemp = taRsCheckList.get(0);
            taRsCheckTemp.setStatusFlag(statusFlag);                                 // 状态标志
            taRsCheckTemp.setCheckTime(ToolUtil.getStrNowTime());                    // 勾对时间
            taRsCheckTemp.setLastUpdDate(new Date());                                // 最近修改时间
            taRsCheckTemp.setModificationNum(taRsCheckTemp.getModificationNum()+1);  // 修改次数
            updateRecord(taRsCheckTemp);
        }
    }

    /**
     * 查询
     * @return
     */
    public List<TaRsCheck> getTodayCheckRecords() {
        TaRsCheckExample example = new TaRsCheckExample();
        TaRsCheckExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        rsActCrit.andCheckDateEqualTo(ToolUtil.getStrLastUpdDate());
        return taRsCheckMapper.selectByExample(example);
    }

    /**
     * 新增记录
     *
     * @param taRsCheckPara
     */
    public void insertRecord(TaRsCheck taRsCheckPara) {
        taRsCheckMapper.insertSelective(taRsCheckPara);
    }

    /**
     * 更新
     * @param taRsCheckPara
     */
    public void updateRecord(TaRsCheck taRsCheckPara) {
        taRsCheckMapper.updateByPrimaryKey(taRsCheckPara);
    }
}
