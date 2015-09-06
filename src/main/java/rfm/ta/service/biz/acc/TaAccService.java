package rfm.ta.service.biz.acc;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.PtenudetailService;
import pub.platform.advance.utils.RfmMessage;
import rfm.ta.common.enums.EnuTaArchivedFlag;
import rfm.ta.repository.dao.TaRsAccMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccExample;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccService {
    private static final Logger logger = LoggerFactory.getLogger(TaAccService.class);

    @Autowired
    private TaRsAccMapper accountMapper;
    @Autowired
    private PtenudetailService ptenudetailService;
    @Autowired
    private TaCommonMapper taCommonMapper;

    /**
     * �ж��˺��Ƿ��Ѵ���
     *
     * @param taRsAcc
     * @return
     */
    public String isExistInDb(TaRsAcc taRsAcc) {
        List<TaRsAcc> taRsAccList = taCommonMapper.selectTaRsAcc(taRsAcc);
        for(TaRsAcc taRsAcc1 : taRsAccList) {
            if(taRsAcc1.getBizId().equals(taRsAcc.getBizId())) {
                return RfmMessage.getProperty("AccountRegistration.E002");
            } else if(taRsAcc1.getSpvsnAccId().equals(taRsAcc.getSpvsnAccId())) {
                return RfmMessage.getProperty("AccountRegistration.E003");
            } else if(taRsAcc1.getSpvsnAccName().equals(taRsAcc.getSpvsnAccName())) {
                return RfmMessage.getProperty("AccountRegistration.E004");
            }
        }
        return null;
    }

    /**
     * ��ѯ
     * @param taRsAccPara
     * @return
     */
    public List<TaRsAcc> selectRecords(TaRsAcc taRsAccPara) {
        TaRsAccExample example = new TaRsAccExample();
        TaRsAccExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        if (ToolUtil.getStrIgnoreNull(taRsAccPara.getSpvsnAccId()).trim().length()!=0) {
            rsActCrit.andSpvsnAccIdEqualTo(taRsAccPara.getSpvsnAccId().trim());
        }
        return accountMapper.selectByExample(example);
    }

    /**
     * �Ƿ񲢷����³�ͻ
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
     * ��ѯ����δɾ������˻���¼
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

    public List<TaRsAcc> qryAllMonitRecords() {
        TaRsAccExample example = new TaRsAccExample();
        example.createCriteria().
                andDeletedFlagEqualTo("0").
                andStatusFlagEqualTo(ptenudetailService.getEnuSelectItem("TA_ACC_STATUS", 1).getValue().toString());
        return accountMapper.selectByExample(example);
    }

    /**
     * ��ѯ
     */
    public List<TaRsAcc> selectedRecordsByCondition(String strAccTypePara, String strAccIdPara, String strAccNamePara) {
        TaRsAccExample example = new TaRsAccExample();
        example.clear();
        TaRsAccExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        if (ToolUtil.getStrIgnoreNull(strAccTypePara).trim().length()!=0) {
            rsActCrit.andSpvsnAccTypeEqualTo(strAccTypePara);
        }
        if (ToolUtil.getStrIgnoreNull(strAccIdPara).trim().length()!=0) {
            rsActCrit.andSpvsnAccIdLike("%"+strAccIdPara+"%");
        }
        if (ToolUtil.getStrIgnoreNull(strAccNamePara).trim().length()!=0) {
            rsActCrit.andSpvsnAccNameLike("%" + strAccNamePara + "%");
        }
        return accountMapper.selectByExample(example);
    }

    /**
     * ������¼
     *
     * @param account
     */
    public void insertRecord(TaRsAcc account) {
        account.setCreatedTime(ToolUtil.getStrLastUpdTime());
        account.setRecVersion(0);
        accountMapper.insertSelective(account);
    }

    /**
     * ͨ����������
     */
    public int updateRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            account.setLastUpdTime(ToolUtil.getStrLastUpdTime());
            account.setRecVersion(account.getRecVersion() + 1);
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("�˻��������³�ͻ��ActPkid=" + account.getPkId());
        }
    }

    /**
     * ͨ������ɾ��
     */
    public int deleteRecord(TaRsAcc account) {
        if (isModifiable(account)) {
            account.setLastUpdTime(ToolUtil.getStrLastUpdTime());
            account.setRecVersion(account.getRecVersion() + 1);
            account.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG1.getCode());
            return accountMapper.updateByPrimaryKeySelective(account);
        } else {
            throw new RuntimeException("�˻��������³�ͻ��ActPkid=" + account.getPkId());
        }
    }
}
