package rfm.qd.service;

import rfm.qd.repository.dao.QdRsSendLogMapper;
import rfm.qd.repository.model.QdRsSendLog;
import rfm.qd.repository.model.QdRsSendLogExample;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-5-14
 * Time: ÏÂÎç2:56
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SendLogService {

    @Autowired
    private QdRsSendLogMapper sendLogMapper;

    /*public List<RsSendLog> qryNotSendDays() {
        RsSendLogExample example = new RsSendLogExample();
        example.createCriteria().andTxnResultNotEqualTo("2");
    }*/

    public List<QdRsSendLog> qrySendLogs(String startdate, String enddate, String txnResult) {
        QdRsSendLogExample example = new QdRsSendLogExample();
        if (StringUtils.isEmpty(txnResult)) {
            example.createCriteria().andTxnDateBetween(startdate, enddate);
        } else {
            example.createCriteria().andTxnDateBetween(startdate, enddate).andTxnResultEqualTo(txnResult.trim());
        }
        example.setOrderByClause("TXN_DATE DESC");
        return sendLogMapper.selectByExample(example);
    }
}
