package rfm.ta.service.sbsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rfm.ta.gateway.sbs.domain.core.domain.SOFForm;
import rfm.ta.gateway.sbs.domain.service.SbsTxnService;
import rfm.ta.gateway.sbs.domain.txn.model.msg.MTia;

import java.util.List;


/**
 * ���⽻�׷���
 */
@Service
public class DataExchangeService {

    private static Logger logger = LoggerFactory.getLogger(DataExchangeService.class);
    @Autowired
    private SbsTxnService sbsTxnService;

    // SBS����ִ�е�  ��Ա��Ϊ�̶�
    public List<SOFForm> callSbsTxn(String txnCode, MTia tia) {
        String tellerid = "EMS1";
        String termid = tellerid;
        return sbsTxnService.execute(termid, tellerid, txnCode, tia);
    }

    // SBS����ִ�е�,��������Ȩ
    public List<SOFForm> callSbsTxn(String auttlr, String autpwd, String txnCode, MTia tia) {
        String tellerid = "EMS1";
        String termid = tellerid;
        return sbsTxnService.execute(termid, tellerid, auttlr, autpwd, txnCode, tia);
    }
}
