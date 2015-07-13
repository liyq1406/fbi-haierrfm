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
public class TaAccDetlService {
    @Autowired
    private TaRsAccDetailMapper taRsAccDetailMapper;
    @Autowired
    private TaCommonMapper commonMapper;

    /**
     * 账户明细查询*/
    public List<TaRsAccDetail> selectedRecords(TaRsAccDetail taRsAccDetailPara) {
        TaRsAccDetailExample example = new TaRsAccDetailExample();
        example.clear();
        TaRsAccDetailExample.Criteria criteria = example.createCriteria();
        if (taRsAccDetailPara.getBusiApplyId() !=null && !StringUtils.isEmpty(taRsAccDetailPara.getBusiApplyId().trim())) {
            criteria.andBusiApplyIdEqualTo(taRsAccDetailPara.getBusiApplyId());
        }
        if (taRsAccDetailPara.getTradeId() !=null && !StringUtils.isEmpty(taRsAccDetailPara.getTradeId().trim())) {
            criteria.andTradeIdEqualTo(taRsAccDetailPara.getTradeId());
        }
        if (taRsAccDetailPara.getAccName() !=null && !StringUtils.isEmpty(taRsAccDetailPara.getAccName().trim())) {
            criteria.andAccNameLike("%" + taRsAccDetailPara.getAccName() + "%");
        }
        if (taRsAccDetailPara.getAccId() != null && !StringUtils.isEmpty(taRsAccDetailPara.getAccId().trim())) {
            criteria.andAccIdEqualTo(taRsAccDetailPara.getAccId());
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("Trade_Date desc,acc_id");
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

        if (statusflag != null && !StringUtils.isEmpty(tradeType.trim())) {
            criteria.andStatusFlagEqualTo(statusflag);
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
