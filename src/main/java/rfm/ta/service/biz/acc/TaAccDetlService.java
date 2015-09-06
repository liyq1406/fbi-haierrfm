package rfm.ta.service.biz.acc;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import common.utils.ToolUtil;
import rfm.ta.common.enums.EnuActFlag;
import rfm.ta.repository.dao.TaRsAccDtlMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaRsAccDtlExample;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-07-14
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccDetlService {

    @Autowired
    private TaRsAccDtlMapper taRsAccDtlMapper;

    /**
     * 账户明细查询*/
    public List<TaRsAccDtl> selectedRecords(TaRsAccDtl taRsAccDtlPara) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        if (taRsAccDtlPara.getBizId() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getBizId().trim())) {
            criteria.andBizIdEqualTo(taRsAccDtlPara.getBizId());
        }
        if (taRsAccDtlPara.getTxCode() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getTxCode().trim())) {
            criteria.andTxCodeEqualTo(taRsAccDtlPara.getTxCode());
        }
        if (taRsAccDtlPara.getTxDate()!= null && !StringUtils.isEmpty(taRsAccDtlPara.getTxDate().trim())) {
            criteria.andTxDateEqualTo(taRsAccDtlPara.getTxDate());
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("SPVSN_ACC_NAME");
        return taRsAccDtlMapper.selectByExample(example);
    }

    /**
     * 操作查询
     */
    public List<TaRsAccDtl> selectedRecordsByCondition(TaRsAccDtl taRsAccDtlPara) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        if (ToolUtil.getStrIgnoreNull(taRsAccDtlPara.getActFlag()).trim().length()!=0) {
            criteria.andActFlagEqualTo(taRsAccDtlPara.getActFlag().trim());
        }

        if (ToolUtil.getStrIgnoreNull(taRsAccDtlPara.getBizId()).trim().length()!=0) {
            criteria.andBizIdLike("%"+taRsAccDtlPara.getBizId().trim()+"%");
        }

        if (ToolUtil.getStrIgnoreNull(taRsAccDtlPara.getTxCode()).trim().length()!=0) {
            criteria.andTxCodeLike(taRsAccDtlPara.getTxCode() + "%");
        }

        criteria.andDeletedFlagEqualTo("0");
        return taRsAccDtlMapper.selectByExample(example);
    }

    /**
     * 取得记账成功标志List
     * @return
     */
    public List<SelectItem> getActFlagList() {
        List<SelectItem> actFlagList = new ArrayList<SelectItem>();
        actFlagList.add(new SelectItem("", "全部"));
        for(EnuActFlag actFlag:EnuActFlag.values()) {
            actFlagList.add(new SelectItem(actFlag.getCode(), actFlag.getTitle()));
        }
        return actFlagList;
    }

    /**
     * 取得记账成功标志Map
     * @return
     */
    public Map<String, String> getActFlagMap() {
        Map<String, String> actFlagMap = new HashMap<String, String>();
        for(EnuActFlag actFlag:EnuActFlag.values()) {
            actFlagMap.put(actFlag.getCode(), actFlag.getTitle());
        }
        return actFlagMap;
    }

    /**
     * 插入
     * @param taRsAccDtl
     */
    public void insertRecord(TaRsAccDtl taRsAccDtl) {
        taRsAccDtl.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setRecVersion(0);
        taRsAccDtlMapper.insert(taRsAccDtl);
    }

    /**
     * 更新
     * @param taRsAccDtl
     * @return
     */
    public int updateRecord(TaRsAccDtl taRsAccDtl) {
        taRsAccDtl.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setRecVersion(taRsAccDtl.getRecVersion() + 1);
        return taRsAccDtlMapper.updateByPrimaryKeySelective(taRsAccDtl);
    }

    /**
     * 删除
     * @param pkId
     * @return
     */
    public int deleteRecord(String pkId) {
        return taRsAccDtlMapper.deleteByPrimaryKey(pkId);
    }
}
