package rfm.qd.service.contract;

import rfm.qd.common.constant.ContractStatus;
import rfm.qd.repository.dao.QdBiContractCloseMapper;
import rfm.qd.repository.dao.QdRsContractMapper;
import rfm.qd.repository.model.QdBiContractClose;
import rfm.qd.repository.model.QdBiContractCloseExample;
import rfm.qd.repository.model.QdRsContract;
import rfm.qd.repository.model.QdRsContractExample;
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
 * Date: 11-8-25
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ContractService {

    @Autowired
    private QdRsContractMapper contractMapper;
    @Autowired
    private QdBiContractCloseMapper contractCloseMapper;

    public List<QdRsContract> selectContractList(){
        QdRsContractExample example = new QdRsContractExample();
        example.createCriteria().andDeletedFlagEqualTo("0");
        return  contractMapper.selectByExample(example);
    }

    public QdRsContract selectContractByNo(String contractNo) {
        QdRsContractExample example = new QdRsContractExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andContractNoEqualTo(contractNo);
        List<QdRsContract> list = contractMapper.selectByExample(example);
        if(list.isEmpty()) {
            return null;
        }else return list.get(0);
    }
    public QdBiContractClose selectCloseContractByNo(String contractNo) {
        QdBiContractCloseExample example = new QdBiContractCloseExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andContractNoEqualTo(contractNo);
        List<QdBiContractClose> list = contractCloseMapper.selectByExample(example);
        if(list.isEmpty()) {
            return null;
        }else return list.get(0);
    }

    public List<QdRsContract> selectContractList(ContractStatus contractStatus){
        QdRsContractExample example = new QdRsContractExample();
        example.createCriteria().andDeletedFlagEqualTo("0").andStatusFlagEqualTo(contractStatus.getCode());
        return  contractMapper.selectByExample(example);
    }
    public QdRsContract selectRecordContract(String pkid) {
        return contractMapper.selectByPrimaryKey(pkid);
    }

    @Transactional
    public int updateRecord(QdRsContract contract) {
        OperatorManager om = SystemService.getOperatorManager();
        contract.setLastUpdBy(om.getOperatorId());
        contract.setLastUpdDate(new Date());
        QdRsContract originRecord = contractMapper.selectByPrimaryKey(contract.getPkId());
        if(!originRecord.getModificationNum().equals(contract.getModificationNum())) {
            throw new RuntimeException("并发更新冲突!");
        }
        return contractMapper.updateByPrimaryKeySelective(contract);
    }

    @Transactional
    public int insertContract(QdRsContract contract) {
         return contractMapper.insertSelective(contract);
    }

}
