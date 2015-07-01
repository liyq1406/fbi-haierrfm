package rfm.ta.gateway.sbs.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.TaChangeFlag;
import rfm.ta.repository.dao.*;
import rfm.ta.repository.model.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-26
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaBiDbService {
    @Autowired
    private TaRsAccountMapper accountMapper;
    @Autowired
    private TaRsAccDetailMapper accDetailMapper;

    /**
     * 查询时间段内账户交易记录 去除冲正
     *
     * @param accountCode
     * @param accountName
     * @param fromDate
     * @param toDate
     * @return
     * @throws java.text.ParseException
     */
    public List<TaRsAccDetail> selectAccDetailsByCodeNameDate(String accountCode, String accountName, String fromDate, String toDate) {
        TaRsAccDetailExample example = new TaRsAccDetailExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andChangeFlagNotEqualTo(TaChangeFlag.CANCEL.getCode())
                .andAccountCodeEqualTo(accountCode)
                .andAccountNameEqualTo(accountName).
                andTradeDateBetween(fromDate, toDate);
        return accDetailMapper.selectByExample(example);
    }

    /**
     * 更新账户状态
     *
     * @param account
     * @return
     */
    @Transactional
    public int updateAccount(TaRsAccount account) {
        account.setRecVersion(account.getRecVersion() + 1);
        return accountMapper.updateByPrimaryKey(account);
    }

    /**
     * 更新账户限制/解除限制状态
     *
     * @param account
     * @param limitStatus
     * @return
     */
    public int updateAccountToLimitStatus(TaRsAccount account, String limitStatus) {
        account.setRecVersion(account.getRecVersion() + 1);
        return accountMapper.updateByPrimaryKey(account);
    }

    /**
     * 根据户名和账户号查找账户
     *
     * @param accountCode
     * @param accountName
     * @return
     */
    public List<TaRsAccount> selectAccountByCodeName(String accountCode, String accountName) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andAccIdEqualTo(accountCode).andAccNameEqualTo(accountName);
        return accountMapper.selectByExample(example);
    }

    public boolean isAccountExistByCodeName(String accountCode, String accountName) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andAccIdEqualTo(accountCode).andAccNameEqualTo(accountName);
        return accountMapper.countByExample(example) == 1;
    }

}
