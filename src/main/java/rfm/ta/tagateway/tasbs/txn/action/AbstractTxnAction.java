package rfm.ta.tagateway.tasbs.txn.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.tagateway.tasbs.core.domain.SOFForm;
import rfm.ta.tagateway.tasbs.txn.model.msg.MTia;

import java.util.List;

public abstract class AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(AbstractTxnAction.class);

    public List<SOFForm> run(String termid, String tellerid,  MTia tia) {
        try {
            return process(termid, tellerid, null, null, tia);
        } catch (Exception e) {
            logger.error("交易异常", e);
            throw new RuntimeException(e.getMessage() == null ? "交易异常." : e.getMessage());
        }
    }

    public List<SOFForm> run(String termid, String tellerid, String auttlr, String autpwd, MTia tia) {
        try {
            return process(termid, tellerid, auttlr, autpwd, tia);
        } catch (Exception e) {
            logger.error("交易异常", e);
            throw new RuntimeException(e.getMessage() == null ? "交易异常." : e.getMessage());
        }
    }

    abstract protected List<SOFForm> process(String termid, String tellerid, String auttlr, String autpwd, MTia tia) throws Exception;
}
