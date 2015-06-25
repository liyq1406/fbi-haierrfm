package rfm.qd.service;

import rfm.qd.repository.dao.QdRsSysCtlMapper;
import rfm.qd.repository.dao.common.CommonMapper;
import rfm.qd.repository.model.QdRsSysCtl;
import rfm.qd.repository.model.QdRsSysCtlExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 12-4-26
 * Time: ÉÏÎç1:06
 * To change this template use File | Settings | File Templates.
 */
@Service
public class RsSysctlService {

    @Autowired
    private QdRsSysCtlMapper qdRsSysCtlMapper;
    @Autowired
    private CommonMapper commonMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int generateTxnSeq(String sysNo) {
        QdRsSysCtlExample example = new QdRsSysCtlExample();
        example.createCriteria().andSysNoEqualTo(sysNo);
        QdRsSysCtl qdRsSysCtl = qdRsSysCtlMapper.selectByExample(example).get(0);

        String date = new SimpleDateFormat("yyMMdd").format(new Date());
        int txnSeq = commonMapper.selectSysSeq();
        if (date.equals(qdRsSysCtl.getSysDate())) {
            commonMapper.updateSysSeq(txnSeq + 1);
        } else {
            qdRsSysCtl.setSysDate(date);
            commonMapper.updateSeqAndSysDate(100000, date);
        }
        return txnSeq;
    }
}
