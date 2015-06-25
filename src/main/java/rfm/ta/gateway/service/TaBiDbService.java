package rfm.ta.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rfm.ta.common.enums.ChangeFlag;
import rfm.ta.repository.dao.*;
import rfm.ta.repository.model.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-26
 * Time: ����2:37
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaBiDbService {
    @Autowired
    private TaRsAccountMapper accountMapper;
    @Autowired
    private TaRsAccDetailMapper accDetailMapper;

    /**
     * ��ѯʱ������˻����׼�¼ ȥ������
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
                .andChangeFlagNotEqualTo(ChangeFlag.CANCEL.getCode())
                .andAccountCodeEqualTo(accountCode)
                .andAccountNameEqualTo(accountName).
                andTradeDateBetween(fromDate, toDate);
        return accDetailMapper.selectByExample(example);
    }

    /**
     * �����˻�״̬
     *
     * @param account
     * @return
     */
    @Transactional
    public int updateAccount(TaRsAccount account) {
        account.setModificationNum(account.getModificationNum() + 1);
        return accountMapper.updateByPrimaryKey(account);
    }

    /**
     * �����˻�����/�������״̬
     *
     * @param account
     * @param limitStatus
     * @return
     */
    public int updateAccountToLimitStatus(TaRsAccount account, String limitStatus) {
        account.setLimitFlag(limitStatus);
        account.setModificationNum(account.getModificationNum() + 1);
        return accountMapper.updateByPrimaryKey(account);
    }

    /**
     * ���ݻ������˻��Ų����˻�
     *
     * @param accountCode
     * @param accountName
     * @return
     */
    public List<TaRsAccount> selectAccountByCodeName(String accountCode, String accountName) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andAccountCodeEqualTo(accountCode).andAccountNameEqualTo(accountName);
        return accountMapper.selectByExample(example);
    }

    public boolean isAccountExistByCodeName(String accountCode, String accountName) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0")
                .andAccountCodeEqualTo(accountCode).andAccountNameEqualTo(accountName);
        return accountMapper.countByExample(example) == 1;
    }

}
