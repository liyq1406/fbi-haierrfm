package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import common.utils.ToolUtil;
import rfm.ta.repository.dao.TaRsAccDtlMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaRsAccDtlExample;

import java.util.List;

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
    private TaCommonMapper commonMapper;

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
        if (taRsAccDtlPara.getAccName() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getAccName().trim())) {
            criteria.andAccNameLike("%" + taRsAccDtlPara.getAccName() + "%");
        }
        if (taRsAccDtlPara.getAccId() != null && !StringUtils.isEmpty(taRsAccDtlPara.getAccId().trim())) {
            criteria.andAccIdEqualTo(taRsAccDtlPara.getAccId());
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("Trade_Date desc,acc_id");
        return taRsAccDtlMapper.selectByExample(example);
    }

    /**
     * 插入*/
    public void insertSelectedRecord(TaRsAccDtl taRsAccDtl) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDtl.setCreatedBy(om.getOperatorId());
        taRsAccDtl.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setLastUpdBy(om.getOperatorId());
        taRsAccDtl.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setReqSn(commonMapper.selectMaxAccDetailSerial());
        taRsAccDtlMapper.insertSelective(taRsAccDtl);
    }

    /**
     * 插入
     * @param taRsAccDtl
     */
    public void insertRecord(TaRsAccDtl taRsAccDtl) {
        taRsAccDtlMapper.insert(taRsAccDtl);
    }

    /**
     * 更新
     * @param taRsAccDtl
     * @return
     */
    public int updateRecord(TaRsAccDtl taRsAccDtl) {
        return taRsAccDtlMapper.updateByPrimaryKeySelective(taRsAccDtl);
    }

    /**
     * 未发送前数据(包括退回)*/
    public List<TaRsAccDtl> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();

        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return taRsAccDtlMapper.selectByExample(example);
    }
}
