package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import rfm.ta.repository.dao.TaRsAccDetailMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.TaRsAccDetail;
import rfm.ta.repository.model.TaRsAccDetailExample;
import rfm.ta.repository.model.TaRsAccount;

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
    private TaRsAccDetailMapper taRsAccDetailMapper;
    @Autowired
    private TaCommonMapper commonMapper;

    /**
     * 账户明细查询*/
    public List<TaRsAccDetail> selectedRecordsByTradeDate(String acctname,String acctno,String beginDate, String endDate) {
        TaRsAccDetailExample example = new TaRsAccDetailExample();
        example.clear();
        TaRsAccDetailExample.Criteria criteria = example.createCriteria();
        if (acctname !=null && !StringUtils.isEmpty(acctname.trim())) {
            criteria.andAccountNameLike("%" + acctname + "%");
        }
        if (acctno != null && !StringUtils.isEmpty(acctno.trim())) {
            criteria.andAccountCodeEqualTo(acctno);
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
    public void insertSelectedRecord(TaRsAccDetail taRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDetail.setCreatedBy(om.getOperatorId());
        taRsAccDetail.setCreatedDate(new Date());
        taRsAccDetail.setLastUpdBy(om.getOperatorId());
        taRsAccDetail.setLastUpdDate(new Date());
        taRsAccDetail.setFdcSerial(commonMapper.selectMaxAccDetailSerial());
        taRsAccDetail.setBankSerial(taRsAccDetail.getFdcSerial());
        taRsAccDetailMapper.insertSelective(taRsAccDetail);
    }

    /**
     * 未发送前数据(包括退回)*/
    public List<TaRsAccDetail> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        TaRsAccDetailExample example = new TaRsAccDetailExample();
        example.clear();
        TaRsAccDetailExample.Criteria criteria = example.createCriteria();
        if (tradeType != null && !StringUtils.isEmpty(tradeType.trim())) {
            criteria.andTradeTypeEqualTo(tradeType);
        }
        if (statusflags != null && statusflags.size()>0) {
            criteria.andStatusFlagIn(statusflags);
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return taRsAccDetailMapper.selectByExample(example);
    }

    public List<TaRsAccDetail> selectedRecordsForSend(String tradeType,String statusflag,String sendflag) {
        TaRsAccDetailExample example = new TaRsAccDetailExample();
        example.clear();
        TaRsAccDetailExample.Criteria criteria = example.createCriteria();
        if (tradeType != null && !StringUtils.isEmpty(tradeType.trim())) {
            criteria.andTradeTypeEqualTo(tradeType);
        }
        if (statusflag != null && !StringUtils.isEmpty(tradeType.trim())) {
            criteria.andStatusFlagEqualTo(statusflag);
        }
        if (sendflag != null && !StringUtils.isEmpty(sendflag.trim())) {
            criteria.andSendFlagEqualTo(sendflag);
        }
        example.setOrderByClause("account_code,local_serial");
        return taRsAccDetailMapper.selectByExample(example);
    }

    public TaRsAccDetail selectedByPK(String pkid) {
        return taRsAccDetailMapper.selectByPrimaryKey(pkid);
    }

    public boolean isChecked(TaRsAccDetail taRsAccDetail) {
        String orgn_statusflag = taRsAccDetailMapper.selectByPrimaryKey(taRsAccDetail.getPkId()).getStatusFlag();
        if (orgn_statusflag.equalsIgnoreCase(taRsAccDetail.getStatusFlag())) {
            return true;
        } else {
            return false;
        }
    }

    public int updateSelectedRecord(TaRsAccDetail taRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDetail.setLastUpdBy(om.getOperatorId());
        taRsAccDetail.setLastUpdDate(new Date());
        return taRsAccDetailMapper.updateByPrimaryKeySelective(taRsAccDetail);
    }

    /**
     * 入账更新*/
    public int updateSelectedRecordBook(TaRsAccDetail taRsAccDetail,TaRsAccount taRsAccount) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDetail.setLastUpdBy(om.getOperatorId());
        taRsAccDetail.setLastUpdDate(new Date());
        return taRsAccDetailMapper.updateByPrimaryKeySelective(taRsAccDetail);
    }

    public TaRsAccDetailMapper getTaRsAccDetailMapper() {
        return taRsAccDetailMapper;
    }

    public void setTaRsAccDetailMapper(TaRsAccDetailMapper taRsAccDetailMapper) {
        this.taRsAccDetailMapper = taRsAccDetailMapper;
    }
}
