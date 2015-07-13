package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PtenudetailService;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import pub.platform.utils.ToolUtil;
import rfm.ta.common.enums.TaEnumArchivedFlag;
import rfm.ta.repository.dao.TaRsAccountMapper;
import rfm.ta.repository.model.TaRsAccount;
import rfm.ta.repository.model.TaRsAccountExample;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: ����2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccService {
    @Autowired
    private TaRsAccountMapper accountMapper;
    @Autowired
    private PtenudetailService ptenudetailService;

    public TaRsAccount selectedRecordByPkid(String pkId) {
        return accountMapper.selectByPrimaryKey(pkId);
    }

    public TaRsAccount selectCanRecvAccountByNo(String accountNo) {
        TaRsAccountExample accountExample = new TaRsAccountExample();
        accountExample.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString())
        .andAccIdEqualTo(accountNo);
        List<TaRsAccount> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ���Ѽ���˻�����");
        }
        return accountList.get(0);
    }

    public TaRsAccount selectCanPayAccountByNo(String accountNo) {
        TaRsAccountExample accountExample = new TaRsAccountExample();
        accountExample.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString()).
                andAccIdEqualTo(accountNo);
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
        example.createCriteria().andAccIdEqualTo(account.getAccId());
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
        if (!act.getRecVersion().equals(actt.getRecVersion())) {
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

    public TaRsAccount qryRecord(String pkid){
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByPrimaryKey(pkid);
    }
    public List<TaRsAccount> qryAllLockRecords() {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public List<TaRsAccount> qryAllMonitRecords() {
        TaRsAccountExample example = new TaRsAccountExample();
        example.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString());
        return accountMapper.selectByExample(example);
    }

    /**
     * ��ѯ
     */
    public List<TaRsAccount> selectedRecordsByCondition(String strAccTypePara, String strAccIdPara, String strAccNamePara) {
        TaRsAccountExample example = new TaRsAccountExample();
        example.clear();
        TaRsAccountExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        if (ToolUtil.getStrIgnoreNull(strAccTypePara).trim().length()!=0) {
            rsActCrit.andAccTypeEqualTo(strAccTypePara);
        }
        if (ToolUtil.getStrIgnoreNull(strAccIdPara).trim().length()!=0) {
            rsActCrit.andAccIdLike("%"+strAccIdPara+"%");
        }
        if (ToolUtil.getStrIgnoreNull(strAccNamePara).trim().length()!=0) {
            rsActCrit.andAccNameLike("%" + strAccNamePara + "%");
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
            String strLastUpdTimeTemp= ToolUtil.getStrLastUpdTime();
            account.setCreatedBy(om.getOperatorId());
            account.setCreatedTime(strLastUpdTimeTemp);
            account.setLastUpdBy(om.getOperatorId());
            account.setLastUpdTime(strLastUpdTimeTemp);
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
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("�˻��������³�ͻ��ActPkid=" + account.getPkId());
        }
    }

    /**
     * ͨ������ɾ��
     */
    public int deleteRecord(TaRsAccount account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // Ĭ���û�
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            account.setDeletedFlag(TaEnumArchivedFlag.ARCHIVED_FLAG1.getCode());
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
        example.createCriteria().andAccIdEqualTo(qdRsAccount.getAccId());
        TaRsAccount tmpRact = accountMapper.selectByExample(example).get(0);
        qdRsAccount.setPkId(tmpRact.getPkId());
        qdRsAccount.setRecVersion(tmpRact.getRecVersion());
        return updateRecord(qdRsAccount);
    }
}
