package rfm.qd.service.expensesplan;

import rfm.qd.repository.dao.RsPlanCtrlMapper;
import rfm.qd.repository.model.RsPlanCtrl;
import rfm.qd.repository.model.RsPlanCtrlExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import platform.service.SystemService;
import pub.platform.security.OperatorManager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 11-8-10
 * Time: ����4:45
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ExpensesPlanService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RsPlanCtrlMapper rsPlanCtrlMapper;

    @Transactional
    public int updatePlanCtrl(RsPlanCtrl planCtrl) {
        RsPlanCtrl originRecord = selectPlanCtrlByPkid(planCtrl.getPkId());
        if (!originRecord.getModificationNum().equals(planCtrl.getModificationNum())) {
            logger.info("��ExpensesPlanService.updatePlanCtrl���¼�¼�汾�ţ�" + planCtrl.getModificationNum() );
            logger.info("��ExpensesPlanService.updatePlanCtrl��ԭ��¼�汾�ţ�" + originRecord.getModificationNum() );
            throw new RuntimeException("��¼�������³�ͻ�������ԣ�");
        } else {
            OperatorManager om = SystemService.getOperatorManager();
            String operId = om.getOperatorId();
            planCtrl.setLastUpdBy(operId);
            planCtrl.setLastUpdDate(new Date());
            planCtrl.setModificationNum(planCtrl.getModificationNum() + 1);
            return rsPlanCtrlMapper.updateByPrimaryKey(planCtrl);
        }
    }

    public RsPlanCtrl selectPlanCtrlByPlanNo(String planNo) {

        RsPlanCtrlExample example = new RsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andPlanCtrlNoEqualTo(planNo);
        List<RsPlanCtrl> planCtrlList = rsPlanCtrlMapper.selectByExample(example);
        if (planCtrlList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ���ƻ���ϸ��");
        }
        return planCtrlList.get(0);
    }

    public RsPlanCtrl selectPlanCtrlByPkid(String pkid) {
        return rsPlanCtrlMapper.selectByPrimaryKey(pkid);
    }

    public List<RsPlanCtrl> selectPlanList() {
        RsPlanCtrlExample example = new RsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return rsPlanCtrlMapper.selectByExample(example);
    }

    public List<RsPlanCtrl> selectPlanListByFields(String companyName, String accountCode, String payContractNo) {
        RsPlanCtrlExample example = new RsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andCompanyNameLike("%" + companyName + "%")
                .andAccountCodeLike("%" + accountCode + "%").andPayContractNoLike("%" + payContractNo + "%");
        return rsPlanCtrlMapper.selectByExample(example);
    }

}
