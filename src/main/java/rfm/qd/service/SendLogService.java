package rfm.qd.service;

import rfm.qd.repository.dao.RsSendLogMapper;
import rfm.qd.repository.model.RsSendLog;
import rfm.qd.repository.model.RsSendLogExample;
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
    private RsSendLogMapper sendLogMapper;

    /*public List<RsSendLog> qryNotSendDays() {
        RsSendLogExample example = new RsSendLogExample();
        example.createCriteria().andTxnResultNotEqualTo("2");
    }*/

    public List<RsSendLog> qrySendLogs(String startdate, String enddate, String txnResult) {
        RsSendLogExample example = new RsSendLogExample();
        if (StringUtils.isEmpty(txnResult)) {
            example.createCriteria().andTxnDateBetween(startdate, enddate);
        } else {
            example.createCriteria().andTxnDateBetween(startdate, enddate).andTxnResultEqualTo(txnResult.trim());
        }
        example.setOrderByClause("TXN_DATE DESC");
        return sendLogMapper.selectByExample(example);
    }
}
