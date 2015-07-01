package rfm.ta.gateway.sbs.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import rfm.ta.gateway.sbs.domain.core.domain.SOFForm;
import rfm.ta.gateway.sbs.domain.txn.action.AbstractTxnAction;
import rfm.ta.gateway.sbs.domain.txn.model.msg.MTia;

import java.util.List;

/**
 * SBS交易发起点
 */
@Service
public class SbsTxnService {
    private static Logger logger = LoggerFactory.getLogger(SbsTxnService.class);

    public List<SOFForm> execute(String termid, String tellid, String auttlr, String autpwd, String txnCode, MTia tia) {
        WebApplicationContext springContext = ContextLoader.getCurrentWebApplicationContext();
        AbstractTxnAction txnAction = (AbstractTxnAction) springContext.getBean("txn" + txnCode + "Action");
        return txnAction.run(termid, tellid, auttlr, autpwd, tia);
    }

    public List<SOFForm> execute(String termid, String tellid, String txnCode, MTia tia) {
        WebApplicationContext springContext = ContextLoader.getCurrentWebApplicationContext();
        AbstractTxnAction txnAction = (AbstractTxnAction) springContext.getBean("txn" + txnCode + "Action");
        return txnAction.run(termid, tellid, tia);
    }
}
