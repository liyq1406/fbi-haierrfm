package rfm.qd.service.account;

import rfm.qd.common.constant.AccountStatus;
import rfm.qd.common.constant.LimitStatus;
import rfm.qd.repository.dao.RsAccountMapper;
import rfm.qd.repository.model.RsAccount;
import rfm.qd.repository.model.RsAccountExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AccountService {
    @Autowired
    private RsAccountMapper accountMapper;

    public RsAccount selectedRecordByPkid(String pkId) {
        return accountMapper.selectByPrimaryKey(pkId);
    }

    public RsAccount selectCanRecvAccountByNo(String accountNo) {
        RsAccountExample accountExample = new RsAccountExample();
        accountExample.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(AccountStatus.WATCH.getCode())
                .andAccountCodeEqualTo(accountNo);
        List<RsAccount> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("没有查询到已监管账户！！");
        }
        return accountList.get(0);
    }

    public RsAccount selectCanPayAccountByNo(String accountNo) {
        RsAccountExample accountExample = new RsAccountExample();
        accountExample.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(AccountStatus.WATCH.getCode())
                .andLimitFlagEqualTo(LimitStatus.NOT_LIMIT.getCode()).andAccountCodeEqualTo(accountNo);
        List<RsAccount> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("没有查询到未限制的已监管账户！请确认该账户已开启监管并未限制付款！");
        }
        return accountList.get(0);
    }

    /**
     * 判断账号是否已存在
     *
     * @param account
     * @return
     */
    public boolean isExistInDb(RsAccount account) {
        RsAccountExample example = new RsAccountExample();
        example.createCriteria().andAccountCodeEqualTo(account.getAccountCode());
        return accountMapper.countByExample(example) >= 1;
    }

    /**
     * 是否并发更新冲突
     *
     * @param
     * @return
     */
    public boolean isModifiable(RsAccount act) {
        RsAccount actt = accountMapper.selectByPrimaryKey(act.getPkId());
        if (!act.getModificationNum().equals(actt.getModificationNum())) {
            return false;
        }
        return true;
    }

    /**
     * 查询所有未删除监管账户记录
     *
     * @return
     */
    public List<RsAccount> qryAllRecords() {
        RsAccountExample example = new RsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public List<RsAccount> qryAllLockRecords() {
        RsAccountExample example = new RsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andBalanceLockGreaterThan(new BigDecimal(0));
        return accountMapper.selectByExample(example);
    }

    public List<RsAccount> qryAllMonitRecords() {
        RsAccountExample example = new RsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(AccountStatus.WATCH.getCode());
        return accountMapper.selectByExample(example);
    }

    /**
     * 查询
     */
    public List<RsAccount> selectedRecordsByCondition(String presellNo, String companyId, String accountCode, String accountName) {
        RsAccountExample example = new RsAccountExample();
        example.clear();
        RsAccountExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        if (presellNo != null && !StringUtils.isEmpty(presellNo.trim())) {
            rsActCrit.andPresellNoEqualTo(presellNo);
        }
        if (companyId != null && !StringUtils.isEmpty(companyId.trim())) {
            rsActCrit.andCompanyIdEqualTo(companyId);
        }
        if (accountCode != null && !StringUtils.isEmpty(accountCode.trim())) {
            rsActCrit.andAccountCodeEqualTo(accountCode);
        }
        if (accountName != null && !StringUtils.isEmpty(accountName.trim())) {
            rsActCrit.andAccountNameLike(accountName + "%");
        }
        return accountMapper.selectByExample(example);
    }

    /**
     * 新增记录
     *
     * @param account
     */
    public void insertRecord(RsAccount account) {
        if (isExistInDb(account)) {
            throw new RuntimeException("该账号已存在，请重新录入！");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            account.setCreatedBy(om.getOperatorId());
            account.setCreatedDate(new Date());
            account.setLastUpdBy(om.getOperatorId());
            account.setLastUpdDate(new Date());
            accountMapper.insertSelective(account);
        }

    }

    /**
     * 通过主键更新
     */
    public int updateRecord(RsAccount account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // 默认用户
//                account.setLastUpdBy("");
            }
            account.setLastUpdDate(new Date());
            account.setModificationNum(account.getModificationNum() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("账户并发更新冲突！ActPkid=" + account.getPkId());
        }
    }

    /**
     * 利息入账更新余额
     */

    public int updateRecordBalance(RsAccount rsAccount) {
        BigDecimal tradeAmt = rsAccount.getBalance();
        RsAccountExample example = new RsAccountExample();
        example.clear();
        example.createCriteria().andAccountCodeEqualTo(rsAccount.getAccountCode()).andCompanyIdEqualTo(rsAccount.getCompanyId());
        RsAccount tmpRact = accountMapper.selectByExample(example).get(0);
        rsAccount.setPkId(tmpRact.getPkId());
        rsAccount.setModificationNum(tmpRact.getModificationNum());
        rsAccount.setBalance(tmpRact.getBalance().add(tradeAmt));
        rsAccount.setBalanceUsable(tmpRact.getBalanceUsable().add(tradeAmt));
        return updateRecord(rsAccount);
    }
}
