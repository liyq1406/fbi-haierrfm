package rfm.qd.service;

import rfm.qd.repository.dao.QdCbsAccTxnMapper;
import rfm.qd.repository.model.QdCbsAccTxn;
import rfm.qd.repository.model.QdCbsAccTxnExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-30
 * Time: ÏÂÎç3:46
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CbusActTxnService {
    @Autowired
    private QdCbsAccTxnMapper qdCbsAccTxnMapper;

    public List<QdCbsAccTxn> qryCbsAccTxns(String accName, String accNo, String startDate, String endDate) {
        QdCbsAccTxnExample example = new QdCbsAccTxnExample();
        example.createCriteria().andAccountNameLike("%" + accName + "%").andAccountNoLike("%" + accNo + "%")
                .andTxnDateBetween(startDate, endDate);
        example.setOrderByClause(" txn_date desc,txn_time desc ");
        return qdCbsAccTxnMapper.selectByExample(example);
    }
}
