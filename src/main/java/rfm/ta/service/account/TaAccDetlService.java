package rfm.ta.service.account;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;
import common.utils.ToolUtil;
import rfm.ta.common.enums.EnuActFlag;
import rfm.ta.repository.dao.TaRsAccDtlMapper;
import rfm.ta.repository.dao.com.TaCommonMapper;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaRsAccDtlExample;

import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-07-14
 * Time: ����10:15
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaAccDetlService {

    @Autowired
    private TaCommonMapper commonMapper;

    @Autowired
    private TaRsAccDtlMapper taRsAccDtlMapper;

    /**
     * �˻���ϸ��ѯ*/
    public List<TaRsAccDtl> selectedRecords(TaRsAccDtl taRsAccDtlPara) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        if (taRsAccDtlPara.getBizId() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getBizId().trim())) {
            criteria.andBizIdEqualTo(taRsAccDtlPara.getBizId());
        }
        if (taRsAccDtlPara.getTxCode() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getTxCode().trim())) {
            criteria.andTxCodeEqualTo(taRsAccDtlPara.getTxCode());
        }
        if (taRsAccDtlPara.getAccName() !=null && !StringUtils.isEmpty(taRsAccDtlPara.getAccName().trim())) {
            criteria.andAccNameLike("%" + taRsAccDtlPara.getAccName() + "%");
        }
        if (taRsAccDtlPara.getAccId() != null && !StringUtils.isEmpty(taRsAccDtlPara.getAccId().trim())) {
            criteria.andAccIdEqualTo(taRsAccDtlPara.getAccId());
        }
        if (taRsAccDtlPara.getTradeDate()!= null && !StringUtils.isEmpty(taRsAccDtlPara.getTradeDate().trim())) {
            criteria.andAccIdEqualTo(taRsAccDtlPara.getTradeDate());
        }
        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("Trade_Date desc,acc_id");
        return taRsAccDtlMapper.selectByExample(example);
    }

    /**
     * ������ѯ
     */
    public List<TaRsAccDtl> selectedRecordsByCondition(String actFlag, String txCode) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();
        if (ToolUtil.getStrIgnoreNull(actFlag).trim().length()!=0) {
            criteria.andActFlagEqualTo(actFlag.trim());
        }

        if (txCode !=null && !StringUtils.isEmpty(txCode.trim())) {
            criteria.andTxCodeLike(txCode + "%");
        }

        criteria.andDeletedFlagEqualTo("0");
        return taRsAccDtlMapper.selectByExample(example);
    }

    /**
     * ȡ�ü��˳ɹ���־List
     * @return
     */
    public List<SelectItem> getActFlagList() {
        List<SelectItem> actFlagList = new ArrayList<SelectItem>();
        actFlagList.add(new SelectItem("", "ȫ��"));
        for(EnuActFlag actFlag:EnuActFlag.values()) {
            actFlagList.add(new SelectItem(actFlag.getCode(), actFlag.getTitle()));
        }
        return actFlagList;
    }

    /**
     * ȡ�ü��˳ɹ���־Map
     * @return
     */
    public Map<String, String> getActFlagMap() {
        Map<String, String> actFlagMap = new HashMap<>();
        for(EnuActFlag actFlag:EnuActFlag.values()) {
            actFlagMap.put(actFlag.getCode(), actFlag.getTitle());
        }

        return actFlagMap;
    }

    /**
     * ����*/
    public void insertSelectedRecord(TaRsAccDtl taRsAccDtl) {
        OperatorManager om = SystemService.getOperatorManager();
        taRsAccDtl.setCreatedBy(om.getOperatorId());
        taRsAccDtl.setCreatedTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setLastUpdBy(om.getOperatorId());
        taRsAccDtl.setLastUpdTime(ToolUtil.getStrLastUpdTime());
        taRsAccDtl.setReqSn(commonMapper.selectMaxAccDetailSerial());
        taRsAccDtlMapper.insertSelective(taRsAccDtl);
    }

    /**
     * ����
     * @param taRsAccDtl
     */
    public void insertRecord(TaRsAccDtl taRsAccDtl) {
        taRsAccDtlMapper.insert(taRsAccDtl);
    }

    /**
     * ����
     * @param taRsAccDtl
     * @return
     */
    public int updateRecord(TaRsAccDtl taRsAccDtl) {
        return taRsAccDtlMapper.updateByPrimaryKeySelective(taRsAccDtl);
    }

    /**
     * δ����ǰ����(�����˻�)*/
    public List<TaRsAccDtl> selectedRecordsForChk(String tradeType, List<String> statusflags) {
        TaRsAccDtlExample example = new TaRsAccDtlExample();
        example.clear();
        TaRsAccDtlExample.Criteria criteria = example.createCriteria();

        criteria.andDeletedFlagEqualTo("0");
        example.setOrderByClause("account_code,local_serial");
        return taRsAccDtlMapper.selectByExample(example);
    }
}
