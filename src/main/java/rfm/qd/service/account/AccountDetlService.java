package rfm.qd.service.account;

import rfm.qd.repository.dao.RsAccDetailMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsAccDetailExample;
import rfm.qd.repository.model.RsAccount;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

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
public class AccountDetlService {
    @Autowired
    private RsAccDetailMapper rsAccDetailMapper;
    @Autowired
    private CommonMapper commonMapper;

    /**
     * 账户明细查询*/
    public List<RsAccDetail> selectedRecordsByTradeDate(String acctname,String acctno,String beginDate, String endDate) {
        RsAccDetailExample example = new RsAccDetailExample();
        example.clear();
        RsAccDetailExample.Criteria criteria = example.createCriteria();
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
        return rsAccDetailMapper.selectByExample(example);
    }

    /**
     * 插入*/
    public void insertSelectedRecord(RsAccDetail rsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        rsAccDetail.setCreatedBy(om.getOperatorId());
        rsAccDetail.setCreatedDate(new Date());
        rsAccDetail.setLastUpdBy(om.getOperatorId());
        rsAccDetail.setLastUpdDate(new Date());
        rsAccDetail.setLocalSerial(commonMapper.selectMaxAccDetailSerial());
        rsAccDetail.setBankSerial(rsAccDetail.getLocalSerial());
        rsAccDetailMapper.insertSelective(rsAccDetail);
    }

    /**
     * 未发送前数据(包括退回)*/
    public List<RsAccDetail> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        RsAccDetailExample example = new RsAccDetailExample();
        example.clear();
        RsAccDetailExample.Criteria criteria = example.createCriteria();
        if (tradeType != null && !StringUtils.isEmpty(tradeType.trim())) {
            criteria.andTradeTypeEqualTo(tradeType);
        }
        if (statusflags != null && statusflags.size()>0) {
            criteria.andStatusFlagIn(statusflags);
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return rsAccDetailMapper.selectByExample(example);
    }

    public List<RsAccDetail> selectedRecordsForSend(String tradeType,String statusflag,String sendflag) {
        RsAccDetailExample example = new RsAccDetailExample();
        example.clear();
        RsAccDetailExample.Criteria criteria = example.createCriteria();
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
        return rsAccDetailMapper.selectByExample(example);
    }

    public RsAccDetail selectedByPK(String pkid) {
        return rsAccDetailMapper.selectByPrimaryKey(pkid);
    }

    public boolean isChecked(RsAccDetail rsAccDetail) {
        String orgn_statusflag = rsAccDetailMapper.selectByPrimaryKey(rsAccDetail.getPkId()).getStatusFlag();
        if (orgn_statusflag.equalsIgnoreCase(rsAccDetail.getStatusFlag())) {
            return true;
        } else {
            return false;
        }
    }

    public int updateSelectedRecord(RsAccDetail rsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        rsAccDetail.setLastUpdBy(om.getOperatorId());
        rsAccDetail.setLastUpdDate(new Date());
        return rsAccDetailMapper.updateByPrimaryKeySelective(rsAccDetail);
    }

    /**
     * 入账更新*/
    public int updateSelectedRecordBook(RsAccDetail rsAccDetail,RsAccount rsAccount) {
        OperatorManager om = SystemService.getOperatorManager();
        rsAccDetail.setLastUpdBy(om.getOperatorId());
        rsAccDetail.setLastUpdDate(new Date());
        rsAccDetail.setBalance(rsAccount.getBalance());
        return rsAccDetailMapper.updateByPrimaryKeySelective(rsAccDetail);
    }

    public RsAccDetailMapper getRsAccDetailMapper() {
        return rsAccDetailMapper;
    }

    public void setRsAccDetailMapper(RsAccDetailMapper rsAccDetailMapper) {
        this.rsAccDetailMapper = rsAccDetailMapper;
    }
}
