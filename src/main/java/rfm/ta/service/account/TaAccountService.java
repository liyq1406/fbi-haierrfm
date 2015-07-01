package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import rfm.ta.common.enums.AccountStatus;
import rfm.ta.common.enums.LimitStatus;
import rfm.ta.repository.dao.TaRsAccountMapper;
import rfm.ta.repository.model.TaRsAccount;
import rfm.ta.repository.model.TaRsAccountExample;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: ����2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccountService {
    @Autowired
    private TaRsAccountMapper accountMapper;

    public TaRsAccount selectedRecordByPkid(String pkId) {
        return accountMapper.selectByPrimaryKey(pkId);
    }

    public TaRsAccount selectCanRecvAccountByNo(String accountNo) {
        TaRsAccountExample accountExample = new TaRsAccountExample();
        accountExample.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(AccountStatus.WATCH.getCode())
                .andAccountCodeEqualTo(accountNo);
        List<TaRsAccount> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ���Ѽ���˻�����");
        }
        return accountList.get(0);
    }

    public TaRsAccount selectCanPayAccountByNo(String accountNo) {
        TaRsAccountExample accountExample = new TaRsAccountExample();
        accountExample.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(AccountStatus.WATCH.getCode())
                .andLimitFlagEqualTo(LimitStatus.NOT_LIMIT.getCode()).andAccountCodeEqualTo(accountNo);
        List<TaRsAccount> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ��δ���Ƶ��Ѽ���˻�����ȷ�ϸ��˻��ѿ�����ܲ�δ���Ƹ��");
        }
        return accountList.get(0);
    }

    /**
     * �ж��˺��Ƿ��Ѵ���
     *
     * @param account
     * @return
     */
    public boolean isExistInDb(TaRsAccount account) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andAccountCodeEqualTo(account.getAccountCode());
        return accountMapper.countByExample(example) >= 1;
    }

    /**
     * �Ƿ񲢷����³�ͻ
     *
     * @param
     * @return
     */
    public boolean isModifiable(TaRsAccount act) {
        TaRsAccount actt = accountMapper.selectByPrimaryKey(act.getPkId());
        if (!act.getModificationNum().equals(actt.getModificationNum())) {
            return false;
        }
        return true;
    }

    /**
     * ��ѯ����δɾ������˻���¼
     *
     * @return
     */
    public List<TaRsAccount> qryAllRecords() {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public List<TaRsAccount> qryAllLockRecords() {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public List<TaRsAccount> qryAllMonitRecords() {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(AccountStatus.WATCH.getCode());
        return accountMapper.selectByExample(example);
    }

    /**
     * ��ѯ
     */
    public List<TaRsAccount> selectedRecordsByCondition(String presellNo, String companyId, String accountCode, String accountName) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.clear();
        TaRsAccountExample.Criteria rsActCrit = example.createCriteria();
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
     * ������¼
     *
     * @param account
     */
    public void insertRecord(TaRsAccount account) {
        if (isExistInDb(account)) {
            throw new RuntimeException("���˺��Ѵ��ڣ�������¼�룡");
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
     * ͨ����������
     */
    public int updateRecord(TaRsAccount account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // Ĭ���û�
//                account.setLastUpdBy("");
            }
            account.setLastUpdDate(new Date());
            account.setModificationNum(account.getModificationNum() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("�˻��������³�ͻ��ActPkid=" + account.getPkId());
        }
    }

    /**
     * ��Ϣ���˸������
     */

    public int updateRecordBalance(TaRsAccount qdRsAccount) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.clear();
        example.createCriteria().andAccountCodeEqualTo(qdRsAccount.getAccountCode()).andCompanyIdEqualTo(qdRsAccount.getCompanyId());
        TaRsAccount tmpRact = accountMapper.selectByExample(example).get(0);
        qdRsAccount.setPkId(tmpRact.getPkId());
        qdRsAccount.setModificationNum(tmpRact.getModificationNum());
        return updateRecord(qdRsAccount);
    }
}