package rfm.ta.service.his;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.repository.dao.TaTxnFdcMapper;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnFdcExample;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-24
 * Time: ����2:04
 * To change this template use File | Settings | File Templates.
 */
@Service
public class TaTxnFdcService {
    private static final Logger logger = LoggerFactory.getLogger(TaTxnFdcService.class);

    @Autowired
    private TaTxnFdcMapper taTxnFdcMapper;

    /**
     * �Ƿ񲢷����³�ͻ
     *
     * @param
     * @return
     */
    public boolean isModifiable(TaTxnFdc taTxnFdcPara) {
        TaTxnFdc taTxnFdcTemp = taTxnFdcMapper.selectByPrimaryKey(taTxnFdcPara.getPkId());
        if (!taTxnFdcPara.getRecVersion().equals(taTxnFdcTemp.getRecVersion())) {
            return false;
        }
        return true;
    }

    /**
     * ������ѯ
     */
    public TaTxnFdc selectedRecordsByKey(String strTaTxnFdcPara) {
        return taTxnFdcMapper.selectByPrimaryKey(strTaTxnFdcPara);
    }

    /**
     * ������ѯ
     */
    public List<TaTxnFdc> selectedAllRecords(TaTxnFdc taTxnFdcPara) {
        TaTxnFdcExample example = new TaTxnFdcExample();
        TaTxnFdcExample.Criteria rsActCrit = example.createCriteria();
        if (ToolUtil.getStrIgnoreNull(taTxnFdcPara.getTxCode()).trim().length()!=0) {
            rsActCrit.andTxCodeLike("%"+taTxnFdcPara.getTxCode()+"%");
        }
        if (ToolUtil.getStrIgnoreNull(taTxnFdcPara.getBizId()).trim().length()!=0) {
            rsActCrit.andBizIdLike("%"+taTxnFdcPara.getBizId().trim()+"%");
        }
        if (ToolUtil.getStrIgnoreNull(taTxnFdcPara.getTradeDate()).trim().length()!=0) {
            rsActCrit.andTradeDateEqualTo(taTxnFdcPara.getTradeDate().trim());
        }
        return taTxnFdcMapper.selectByExample(example);
    }
    /**
     * ������¼
     *
     * @param taTxnFdcPara
     */
    public void insertRecord(TaTxnFdc taTxnFdcPara) {
        String strOperId=ToolUtil.getOperatorManager().getOperatorId();
        String strLastUpdTimeTemp= ToolUtil.getStrLastUpdTime();
        taTxnFdcPara.setCreatedBy(strOperId);
        taTxnFdcPara.setCreatedTime(strLastUpdTimeTemp);
        taTxnFdcMapper.insert(taTxnFdcPara);
    }

    /**
     * ͨ����������
     */
    public int updateRecord(TaTxnFdc taTxnFdcPara) {
        if (isModifiable(taTxnFdcPara)) {
            String strOperId=ToolUtil.getOperatorManager().getOperatorId();
            String strLastUpdTimeTemp=ToolUtil.getStrLastUpdTime();
            taTxnFdcPara.setLastUpdBy(strOperId);
            taTxnFdcPara.setLastUpdTime(strLastUpdTimeTemp);
            taTxnFdcPara.setRecVersion(taTxnFdcPara.getRecVersion() + 1);
            return taTxnFdcMapper.updateByPrimaryKeySelective(taTxnFdcPara);
        } else {
            throw new RuntimeException("�������³�ͻ��Pkid=" + taTxnFdcPara.getPkId());
        }
    }
}
