package rfm.qd.service;

import rfm.qd.common.constant.*;
import rfm.qd.repository.dao.QdRsAccDetailMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.repository.model.QdRsAccDetailExample;
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
    private QdRsAccDetailMapper accDetailMapper;
    @Autowired
    private CommonMapper commonMapper;
    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");

    public List<QdRsAccDetail> selectTodayAccDetails() {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andDcheckFlagEqualTo("0")
                .andChangeFlagNotEqualTo(ChangeFlag.CANCEL.getCode())
                .andTradeTypeNotEqualTo(TradeType.HOUSE_CREDIT.getCode())
                .andSendFlagEqualTo(SendFlag.UN_SEND.getCode());
                //.andTradeDateEqualTo(sdf10.format(new Date()));
        return accDetailMapper.selectByExample(example);
    }

    public List<QdRsAccDetail> selectTodayLoanAccDetails() {
            QdRsAccDetailExample example = new QdRsAccDetailExample();
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
        QdRsAccDetailExample example = new QdRsAccDetailExample();
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
        QdRsAccDetailExample example = new QdRsAccDetailExample();
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

    public QdRsAccDetail selectAccDetailByPkid(String pkid) {
        return accDetailMapper.selectByPrimaryKey(pkid);
    }

    public List<QdRsAccDetail> selectAccDetailsByStatus(TradeStatus tradeStatus) {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(tradeStatus.getCode());
        return accDetailMapper.selectByExample(example);
    }

    // 冲正交易
    public List<QdRsAccDetail> selectCancelAccDetails() {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andStatusFlagEqualTo(TradeStatus.SUCCESS.getCode())
                .andChangeFlagEqualTo(ChangeFlag.NORMAL.getCode())
                .andTradeDateGreaterThanOrEqualTo(sdf10.format(new Date()));
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    // 冲正申请交易
    public List<QdRsAccDetail> selectAPCancelAccDetails() {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andChangeFlagEqualTo(ChangeFlag.AP_CANCEL.getCode());
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    // 退票交易
    public List<QdRsAccDetail> selectBackAccDetails() {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
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
    public List<QdRsAccDetail> selectAPBackAccDetails() {
        QdRsAccDetailExample example = new QdRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andInoutFlagEqualTo(InOutFlag.OUT.getCode())
                .andChangeFlagEqualTo(ChangeFlag.AP_BACK.getCode());
        example.setOrderByClause("Trade_Date desc");
        return accDetailMapper.selectByExample(example);
    }

    public int insertAccDetail(QdRsAccDetail qdRsAccDetail) {
        OperatorManager om = SystemService.getOperatorManager();
        String operId = om.getOperatorId();
        qdRsAccDetail.setCreatedBy(operId);
        qdRsAccDetail.setCreatedDate(new Date());
        qdRsAccDetail.setLastUpdBy(operId);
        qdRsAccDetail.setLastUpdDate(new Date());
        qdRsAccDetail.setLocalSerial(commonMapper.selectMaxAccDetailSerial());
        qdRsAccDetail.setBankSerial(qdRsAccDetail.getLocalSerial());
        return accDetailMapper.insertSelective(qdRsAccDetail);
    }

    public int updateAccDetail(QdRsAccDetail qdRsAccDetail) {
        QdRsAccDetail originRecord = accDetailMapper.selectByPrimaryKey(qdRsAccDetail.getPkId());
        if (!originRecord.getModificationNum().equals(qdRsAccDetail.getModificationNum())) {
            throw new RuntimeException("交易明细记录并发更新冲突，请重试！");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            qdRsAccDetail.setLastUpdBy(operId);
            qdRsAccDetail.setLastUpdDate(new Date());
            qdRsAccDetail.setModificationNum(qdRsAccDetail.getModificationNum() + 1);
            return accDetailMapper.updateByPrimaryKeySelective(qdRsAccDetail);
        }
    }
}
