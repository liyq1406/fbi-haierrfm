package rfm.qd.service.expensesplan;

import rfm.qd.repository.dao.QdRsPlanCtrlMapper;
import rfm.qd.repository.model.QdRsPlanCtrl;
import rfm.qd.repository.model.QdRsPlanCtrlExample;
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
    private QdRsPlanCtrlMapper qdRsPlanCtrlMapper;

    @Transactional
    public int updatePlanCtrl(QdRsPlanCtrl planCtrl) {
        QdRsPlanCtrl originRecord = selectPlanCtrlByPkid(planCtrl.getPkId());
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
            return qdRsPlanCtrlMapper.updateByPrimaryKey(planCtrl);
        }
    }

    public QdRsPlanCtrl selectPlanCtrlByPlanNo(String planNo) {

        QdRsPlanCtrlExample example = new QdRsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andPlanCtrlNoEqualTo(planNo);
        List<QdRsPlanCtrl> planCtrlList = qdRsPlanCtrlMapper.selectByExample(example);
        if (planCtrlList.isEmpty()) {
            throw new RuntimeException("û�в�ѯ���ƻ���ϸ��");
        }
        return planCtrlList.get(0);
    }

    public QdRsPlanCtrl selectPlanCtrlByPkid(String pkid) {
        return qdRsPlanCtrlMapper.selectByPrimaryKey(pkid);
    }

    public List<QdRsPlanCtrl> selectPlanList() {
        QdRsPlanCtrlExample example = new QdRsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return qdRsPlanCtrlMapper.selectByExample(example);
    }

    public List<QdRsPlanCtrl> selectPlanListByFields(String companyName, String accountCode, String payContractNo) {
        QdRsPlanCtrlExample example = new QdRsPlanCtrlExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andCompanyNameLike("%" + companyName + "%")
                .andAccountCodeLike("%" + accountCode + "%").andPayContractNoLike("%" + payContractNo + "%");
        return qdRsPlanCtrlMapper.selectByExample(example);
    }

}
