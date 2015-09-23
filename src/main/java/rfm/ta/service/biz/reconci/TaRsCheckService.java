package rfm.ta.service.biz.reconci;

import common.utils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.common.enums.EnuClassifyFlag;
import rfm.ta.common.enums.EnuTaArchivedFlag;
import rfm.ta.repository.dao.TaRsCheckMapper;
import rfm.ta.repository.model.TaRsCheck;
import rfm.ta.repository.model.TaRsCheckExample;

import java.util.Date;
import java.util.List;

/**
 * ���˼�¼��
 */
@Service
public class TaRsCheckService {
    private static final Logger logger = LoggerFactory.getLogger(TaRsCheckService.class);

    @Autowired
    private TaRsCheckMapper taRsCheckMapper;

    /**
     * �������¶�����ϸ��
     * @param statusFlag
     */
    public void insOrUpdTaRsCheck(String statusFlag) {
        // ������߸��¶��˼�¼��
        List<TaRsCheck> taRsCheckList = getTodayCheckRecords();
        if(taRsCheckList == null || taRsCheckList.size() == 0) {
            Date sysdate = new Date();
            TaRsCheck taRsCheckTemp = new TaRsCheck();
            taRsCheckTemp.setCheckDate(ToolUtil.getStrLastUpdDate());                   // ��������
            taRsCheckTemp.setStatusFlag(statusFlag);                                    // ״̬��־
            taRsCheckTemp.setClassifyFlag(EnuClassifyFlag.CLASSIFY_FLAG2.getCode());  // ���˷���
            taRsCheckTemp.setCheckTime(ToolUtil.getStrNowTime());                       // ����ʱ��
            taRsCheckTemp.setDeletedFlag(EnuTaArchivedFlag.ARCHIVED_FLAG0.getCode()); // ��¼ɾ����־
            taRsCheckTemp.setCreatedDate(sysdate);                                      // ����ʱ��
            taRsCheckTemp.setLastUpdDate(sysdate);                                      // ����޸�ʱ��
            taRsCheckTemp.setModificationNum(0);                                        // �޸Ĵ���
            insertRecord(taRsCheckTemp);
        } else {
            TaRsCheck taRsCheckTemp = taRsCheckList.get(0);
            taRsCheckTemp.setStatusFlag(statusFlag);                                 // ״̬��־
            taRsCheckTemp.setCheckTime(ToolUtil.getStrNowTime());                    // ����ʱ��
            taRsCheckTemp.setLastUpdDate(new Date());                                // ����޸�ʱ��
            taRsCheckTemp.setModificationNum(taRsCheckTemp.getModificationNum()+1);  // �޸Ĵ���
            updateRecord(taRsCheckTemp);
        }
    }

    /**
     * ��ѯ
     * @return
     */
    public List<TaRsCheck> getTodayCheckRecords() {
        TaRsCheckExample example = new TaRsCheckExample();
        TaRsCheckExample.Criteria rsActCrit = example.createCriteria();
        rsActCrit.andDeletedFlagEqualTo("0");
        rsActCrit.andCheckDateEqualTo(ToolUtil.getStrLastUpdDate());
        return taRsCheckMapper.selectByExample(example);
    }

    /**
     * ������¼
     *
     * @param taRsCheckPara
     */
    public void insertRecord(TaRsCheck taRsCheckPara) {
        taRsCheckMapper.insertSelective(taRsCheckPara);
    }

    /**
     * ����
     * @param taRsCheckPara
     */
    public void updateRecord(TaRsCheck taRsCheckPara) {
        taRsCheckMapper.updateByPrimaryKey(taRsCheckPara);
    }
}
