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
 * User: haiyuhuang
 * Date: 11-9-6
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccountDetlService {
    @Autowired
    private TaRsAccDtlMapper taRsAccDetailMapper;
    @Autowired
    private TaCommonMapper commonMapper;

    /**
     * 账户明细查询*/
    public List<TaRsAccDtl> selectedRecordsByTradeDate(String acctname,String acctno,String beginDate, String endDate) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        if (acctname !=null && !StringUtils.isEmpty(acctname.trim())) {
            criteria.andRtnAccNameLike("%" + acctname + "%");
        }
        if (acctno != null && !StringUtils.isEmpty(acctno.trim())) {
            criteria.andRtnAccIdEqualTo(acctno);
        }
        if (beginDate != null && endDate != null) {
            criteria.andTradeDateBetween(beginDate, endDate);
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("Trade_Date desc,account_code,local_serial");
        return taRsAccDetailMapper.selectByExample(example);
    }

    /**
     * 插入*/
    public void insertSelectedRecord(TaRsAccDtl taRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDetail.setCreatedBy(om.getOperatorId());
        taRsAccDetail.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taRsAccDetail.setLastUpdBy(om.getOperatorId());
        taRsAccDetail.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        /*taRsAccDetail.setFdcSerial(commonMapper.selectMaxAccDetailSerial());
        taRsAccDetail.setBankSerial(taRsAccDetail.getFdcSerial());*/
        taRsAccDetailMapper.insertSelective(taRsAccDetail);
    }

    /**
     * 未发送前数据(包括退回)*/
    public List<TaRsAccDtl> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return taRsAccDetailMapper.selectByExample(example);
    }

    public List<TaRsAccDtl> selectedRecordsForSend(String tradeType,String statusflag,String sendflag) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("account_code,local_serial");
        return taRsAccDetailMapper.selectByExample(example);
    }

    public TaRsAccDtl selectedByPK(String pkid) {
        return taRsAccDetailMapper.selectByPrimaryKey(pkid);
    }

    public int updateSelectedRecord(TaRsAccDtl taRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDetail.setLastUpdBy(om.getOperatorId());
        taRsAccDetail.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        return taRsAccDetailMapper.updateByPrimaryKeySelective(taRsAccDetail);
    }

    /**
     * 入账更新*/
    public int updateSelectedRecordBook(TaRsAccDtl taRsAccDetail,TaRsAcc taRsAccount) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDetail.setLastUpdBy(om.getOperatorId());
        taRsAccDetail.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        return taRsAccDetailMapper.updateByPrimaryKeySelective(taRsAccDetail);
    }

    public TaRsAccDtlMapper getTaRsAccDtlMapper() {
        return taRsAccDetailMapper;
    }

    public void setTaRsAccDtlMapper(TaRsAccDtlMapper taRsAccDetailMapper) {
        this.taRsAccDetailMapper = taRsAccDetailMapper;
    }
}
