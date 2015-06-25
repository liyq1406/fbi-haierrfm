package rfm.qd.service.account;

import rfm.qd.repository.dao.QdRsAccDetailMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsAccDetailExample;
import rfm.qd.repository.model.QdRsAccount;
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
    private QdRsAccDetailMapper qdRsAccDetailMapper;
    @Autowired
    private CommonMapper commonMapper;

    /**
     * 账户明细查询*/
    public List<QdRsAccDetail> selectedRecordsByTradeDate(String acctname,String acctno,String beginDate, String endDate) {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.clear();
        QdRsAccDetailExample.Criteria criteria = example.createCriteria();
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
        return qdRsAccDetailMapper.selectByExample(example);
    }

    /**
     * 插入*/
    public void insertSelectedRecord(QdRsAccDetail qdRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        qdRsAccDetail.setCreatedBy(om.getOperatorId());
        qdRsAccDetail.setCreatedDate(new Date());
        qdRsAccDetail.setLastUpdBy(om.getOperatorId());
        qdRsAccDetail.setLastUpdDate(new Date());
        qdRsAccDetail.setLocalSerial(commonMapper.selectMaxAccDetailSerial());
        qdRsAccDetail.setBankSerial(qdRsAccDetail.getLocalSerial());
        qdRsAccDetailMapper.insertSelective(qdRsAccDetail);
    }

    /**
     * 未发送前数据(包括退回)*/
    public List<QdRsAccDetail> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.clear();
        QdRsAccDetailExample.Criteria criteria = example.createCriteria();
        if (tradeType != null && !StringUtils.isEmpty(tradeType.trim())) {
            criteria.andTradeTypeEqualTo(tradeType);
        }
        if (statusflags != null && statusflags.size()>0) {
            criteria.andStatusFlagIn(statusflags);
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return qdRsAccDetailMapper.selectByExample(example);
    }

    public List<QdRsAccDetail> selectedRecordsForSend(String tradeType,String statusflag,String sendflag) {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.clear();
        QdRsAccDetailExample.Criteria criteria = example.createCriteria();
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
        return qdRsAccDetailMapper.selectByExample(example);
    }

    public QdRsAccDetail selectedByPK(String pkid) {
        return qdRsAccDetailMapper.selectByPrimaryKey(pkid);
    }

    public boolean isChecked(QdRsAccDetail qdRsAccDetail) {
        String orgn_statusflag = qdRsAccDetailMapper.selectByPrimaryKey(qdRsAccDetail.getPkId()).getStatusFlag();
        if (orgn_statusflag.equalsIgnoreCase(qdRsAccDetail.getStatusFlag())) {
            return true;
        } else {
            return false;
        }
    }

    public int updateSelectedRecord(QdRsAccDetail qdRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        qdRsAccDetail.setLastUpdBy(om.getOperatorId());
        qdRsAccDetail.setLastUpdDate(new Date());
        return qdRsAccDetailMapper.updateByPrimaryKeySelective(qdRsAccDetail);
    }

    /**
     * 入账更新*/
    public int updateSelectedRecordBook(QdRsAccDetail qdRsAccDetail,QdRsAccount qdRsAccount) {
        OperatorManager om = SystemService.getOperatorManager();
        qdRsAccDetail.setLastUpdBy(om.getOperatorId());
        qdRsAccDetail.setLastUpdDate(new Date());
        qdRsAccDetail.setBalance(qdRsAccount.getBalance());
        return qdRsAccDetailMapper.updateByPrimaryKeySelective(qdRsAccDetail);
    }

    public QdRsAccDetailMapper getQdRsAccDetailMapper() {
        return qdRsAccDetailMapper;
    }

    public void setQdRsAccDetailMapper(QdRsAccDetailMapper qdRsAccDetailMapper) {
        this.qdRsAccDetailMapper = qdRsAccDetailMapper;
    }
}
