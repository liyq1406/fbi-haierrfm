package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import pub.platform.utils.ToolUtil;
import rfm.ta.repository.dao.TaRsAccDtlMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaRsAccDtlExample;
import rfm.ta.repository.model.TaRsAcc;

import java.util.Date;
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

    /**
     * 账户明细查询*/
    public List<TaRsAccDtl> selectedRecords(TaRsAccDtl taRsAccDtlPara) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        if (taRsAccDtlPara.getBusiApplyId() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getBusiApplyId().trim())) {
            criteria.andBusiApplyIdEqualTo(taRsAccDtlPara.getBusiApplyId());
        }
        if (taRsAccDtlPara.getTradeId() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getTradeId().trim())) {
            criteria.andTradeIdEqualTo(taRsAccDtlPara.getTradeId());
        }
        if (taRsAccDtlPara.getRtnAccName() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getRtnAccName().trim())) {
            criteria.andRtnAccNameLike("%" + taRsAccDtlPara.getRtnAccName() + "%");
        }
        if (taRsAccDtlPara.getRtnAccId() != null && !StringUtils.isEmpty(taRsAccDtlPara.getRtnAccId().trim())) {
            criteria.andRtnAccIdEqualTo(taRsAccDtlPara.getRtnAccId());
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("Trade_Date desc,acc_id");
        //return taRsAccDtlMapper.selectByExample(example);
        return null;
    }

    /**
     * 插入*/
    public void insertSelectedRecord(TaRsAccDtl taRsAccDtl) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDtl.setCreatedBy(om.getOperatorId());
        taRsAccDtl.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setLastUpdBy(om.getOperatorId());
        taRsAccDtl.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setSerial(commonMapper.selectMaxAccDetailSerial());
        //taRsAccDtl.setToSerial(taRsAccDtl.gett());
        //taRsAccDtlMapper.insertSelective(taRsAccDtl);
    }

    /**
     * 未发送前数据(包括退回)*/
    public List<TaRsAccDtl> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();

        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return null;
        //return taRsAccDtlMapper.selectByExample(example);
    }
}
