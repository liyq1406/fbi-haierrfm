package rfm.qd.service;

import rfm.qd.repository.dao.CbsAccTxnMapper;
import rfm.qd.repository.model.CbsAccTxn;
import rfm.qd.repository.model.CbsAccTxnExample;
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
    private CbsAccTxnMapper cbsAccTxnMapper;

    public List<CbsAccTxn> qryCbsAccTxns(String accName, String accNo, String startDate, String endDate) {
        CbsAccTxnExample example = new CbsAccTxnExample();
        example.createCriteria().andAccountNameLike("%" + accName + "%").andAccountNoLike("%" + accNo + "%")
                .andTxnDateBetween(startDate, endDate);
        example.setOrderByClause(" txn_date desc,txn_time desc ");
        return cbsAccTxnMapper.selectByExample(example);
    }
}
