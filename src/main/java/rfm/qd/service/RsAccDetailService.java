package rfm.qd.service;

import rfm.qd.common.constant.*;
import rfm.qd.repository.dao.RsAccDetailMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsAccDetailExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-2
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
/*
 */
@Service
public class RsAccDetailService {

    @Autowired
    private RsAccDetailMapper accDetailMapper;
    @Autowired
    private CommonMapper commonMapper;
    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    public List<RsAccDetail> selectTodayAccDetails() {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andDcheckFlagEqualTo("0")
                .andChangeFlagNotEqualTo(ChangeFlag.CANCEL.getCode())
                .andTradeTypeNotEqualTo(TradeType.HOUSE_CREDIT.getCode())
                .andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
                //.andTradeDateEqualTo(sdf10.format(new Date()));
        return accDetailMapper.selectByExample(example);
    }

    public List<RsAccDetail> selectTodayLoanAccDetails() {
            RsAccDetailExample example = new RsAccDetailExample();
            example.createCriteria().andDeletedFlagEqualTo("0")
                    .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                    .andTradeTypeEqualTo(TradeType.HOUSE_CREDIT.getCode())
                    .andChangeFlagNotEqualTo(ChangeFlag.CANCEL.getCode());
                    //.andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
                    //.andTradeDateEqualTo(sdf10.format(new Date()));
            example.setOrderByClause(" BANK_SERIAL ");
            return accDetailMapper.selectByExample(example);
        }

    public boolean isHasUnSendInterest() {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andTradeTypeEqualTo(TradeType.INTEREST.getCode())
                .andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
        if(accDetailMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }


    public boolean isHasUnSendCancelRecord() {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andChangeFlagEqualTo(ChangeFlag.CANCEL.getCode())
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
       example.or(example.createCriteria().andDeletedFlagEqualTo("0")
                .andChangeFlagEqualTo(ChangeFlag.BACK.getCode())
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andSendFlagEqualTo(SendFlag.UN_SEND.getCode()));

        if (accDetailMapper.countByExample(example) > 0) {
            return true;
        }
        return false;
    }

    public RsAccDetail selectAccDetailByPkid(String pkid) {
        return accDetailMapper.selectByPrimaryKey(pkid);
    }

    public List<RsAccDetail> selectAccDetailsByStatus(TradeStatus tradeStatus) {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(tradeStatus.getCode());
        return accDetailMapper.selectByExample(example);
    }

    // 冲正交易
    public List<RsAccDetail> selectCancelAccDetails() {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andChangeFlagEqualTo(ChangeFlag.NORMAL.getCode())
                .andTradeDateGreaterThanOrEqualTo(sdf10.format(new Date()));
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    // 冲正申请交易
    public List<RsAccDetail> selectAPCancelAccDetails() {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andChangeFlagEqualTo(ChangeFlag.AP_CANCEL.getCode());
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    // 退票交易
    public List<RsAccDetail> selectBackAccDetails() {
        RsAccDetailExample example = new RsAccDetailExample();
        try {
            example.createCriteria().andDeletedFlagEqualTo("0")
                    .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                    .andInoutFlagEqualTo(InOutFlag.OUT.getCode())
                    .andChangeFlagEqualTo(ChangeFlag.NORMAL.getCode())
                    .andTradeDateGreaterThan(SystemService.getTodayAddDays(-15));
                    //.andTradeDateLessThan(sdf10.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误！");
        }
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    // 退票申请交易
    public List<RsAccDetail> selectAPBackAccDetails() {
        RsAccDetailExample example = new RsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andInoutFlagEqualTo(InOutFlag.OUT.getCode())
                .andChangeFlagEqualTo(ChangeFlag.AP_BACK.getCode());
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    public int insertAccDetail(RsAccDetail rsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        rsAccDetail.setCreatedBy(operId);
        rsAccDetail.setCreatedDate(new Date());
        rsAccDetail.setLastUpdBy(operId);
        rsAccDetail.setLastUpdDate(new Date());
        rsAccDetail.setLocalSerial(commonMapper.selectMaxAccDetailSerial());
        rsAccDetail.setBankSerial(rsAccDetail.getLocalSerial());
        return accDetailMapper.insertSelective(rsAccDetail);
    }

    public int updateAccDetail(RsAccDetail rsAccDetail) {
        RsAccDetail originRecord = accDetailMapper.selectByPrimaryKey(rsAccDetail.getPkId());
        if (!originRecord.getModificationNum().equals(rsAccDetail.getModificationNum())) {
            throw new RuntimeException("交易明细记录并发更新冲突，请重试！");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            rsAccDetail.setLastUpdBy(operId);
            rsAccDetail.setLastUpdDate(new Date());
            rsAccDetail.setModificationNum(rsAccDetail.getModificationNum() + 1);
            return accDetailMapper.updateByPrimaryKeySelective(rsAccDetail);
        }
    }
}
