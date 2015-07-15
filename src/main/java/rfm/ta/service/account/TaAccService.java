package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PtenudetailService;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import pub.platform.utils.ToolUtil;
import rfm.ta.common.enums.TaEnumArchivedFlag;
import rfm.ta.repository.dao.TaRsAccMapper;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccExample;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: 下午2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccService {
    @Autowired
    private TaRsAccMapper accountMapper;
    @Autowired
    private PtenudetailService ptenudetailService;

    public TaRsAcc selectedRecordByPkid(String pkId) {
        return accountMapper.selectByPrimaryKey(pkId);
    }

    public TaRsAcc selectCanRecvAccountByNo(String accountNo) {
        TaRsAccExample accountExample = new TaRsAccExample();
        accountExample.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString())
        .andAccIdEqualTo(accountNo);
        List<TaRsAcc> accountList = accountMapper.selectByExample(accountExample);
        if (accountList.isEmpty()) {
            throw new RuntimeException("没有查询到已监管账户！！");
        }
        return accountList.get(0);
    }

    public TaRsAcc selectCanPayAccountByNo(String accountNo) {
        TaRsAccExample accountExample = new TaRsAccExample();
        accountExample.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString()).
                andAccIdEqualTo(accountNo);
        List<TaRsAcc> accountList = accountMapper.selectByExample(accountExample);
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
    public boolean isExistInDb(TaRsAcc account) {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andAccIdEqualTo(account.getAccId());
        return accountMapper.countByExample(example) >= 1;
    }

    /**
     * 是否并发更新冲突
     *
     * @param
     * @return
     */
    public boolean isModifiable(TaRsAcc act) {
        TaRsAcc actt = accountMapper.selectByPrimaryKey(act.getPkId());
        if (!act.getRecVersion().equals(actt.getRecVersion())) {
            return false;
        }
        return true;
    }

    /**
     * 查询所有未删除监管账户记录
     *
     * @return
     */
    public List<TaRsAcc> qryAllRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public TaRsAcc qryRecord(String pkid){
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByPrimaryKey(pkid);
    }
    public List<TaRsAcc> qryAllLockRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return accountMapper.selectByExample(example);
    }

    public List<TaRsAcc> qryAllMonitRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString());
        return accountMapper.selectByExample(example);
    }

    /**
     * 查询
     */
    public List<TaRsAcc> selectedRecordsByCondition(String strAccTypePara, String strAccIdPara, String strAccNamePara) {
        TaRsAccExample example = new TaRsAccExample();
        example.clear();
        TaRsAccExample.Criteria rsActCrit = example.createCriteria();
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
     * 新增记录
     *
     * @param account
     */
    public void insertRecord(TaRsAcc account) {
        if (isExistInDb(account)) {
            throw new RuntimeException("该账号已存在，请重新录入！");
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
     * 通过主键更新
     */
    public int updateRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // 默认用户
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("账户并发更新冲突！ActPkid=" + account.getPkId());
        }
    }

    /**
     * 通过主键删除
     */
    public int deleteRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            try {
                OperatorManager om = SystemService.getOperatorManager();
                account.setLastUpdBy(om.getOperatorId());
            } catch (Exception e) {
                // 默认用户
//                account.setLastUpdBy("");
            }
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            account.setLastUpdTime(strLastUpdTimeTemp);
            account.setRecVersion(account.getRecVersion() + 1);
            account.setDeletedFlag(TaEnumArchivedFlag.ARCHIVED_FLAG1.getCode());
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("账户并发更新冲突！ActPkid=" + account.getPkId());
        }
    }

    /**
     * 利息入账更新余额
     */

    public int updateRecordBalance(TaRsAcc qdRsAccount) {
        TaRsAccExample example = new TaRsAccExample();
        example.clear();
        example.createCriteria().andAccIdEqualTo(qdRsAccount.getAccId());
        TaRsAcc tmpRact = accountMapper.selectByExample(example).get(0);
        qdRsAccount.setPkId(tmpRact.getPkId());
        qdRsAccount.setRecVersion(tmpRact.getRecVersion());
        return updateRecord(qdRsAccount);
    }
}
