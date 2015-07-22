package rfm.ta.view.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
import rfm.ta.gateway.dep.model.base.TiaXml;
import rfm.ta.gateway.dep.model.base.ToaXml;
import rfm.ta.gateway.dep.model.txn.TiaXml910012002;
import rfm.ta.gateway.dep.model.txn.ToaXml910012002;
import rfm.ta.gateway.sbs.domain.txn.model.msg.Maa41;

/**
 * Created by XIANGYANG on 2015-7-20.
 * 交存记账
 */
@Component
public class DepTxn910012002Processor extends DepAbstractTxnProcessor {
    private static Logger logger = LoggerFactory.getLogger(DepTxn910012002Processor.class);

    @Override
    public ToaXml process(TiaXml tiaPara)  throws Exception {
        TiaXml910012002 tiaXml910012002 = (TiaXml910012002)tiaPara;
        ToaXml910012002 toaXml910012002 = new ToaXml910012002();
        Maa41 maa41 = null;
        copyTiaInfoToToa(tiaXml910012002, toaXml910012002);
        try {
            toaXml910012002.getInfo().setRtncode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());
            toaXml910012002.getInfo().setRtnmsg(EnuTaTxnRtnCode.TXN_PROCESSED.getTitle());
        } catch (Exception e) {
            logger.error("交存记账交易异常.", e);
            toaXml910012002.getInfo().setRtncode(EnuTaTxnRtnCode.SERVER_EXCEPTION.getCode());
            toaXml910012002.getBody().setRtncode(EnuTaTxnRtnCode.SERVER_EXCEPTION.getCode());
            toaXml910012002.getInfo().setRtnmsg(EnuTaTxnRtnCode.SERVER_EXCEPTION.getTitle());
            toaXml910012002.getBody().setRtnmsg(EnuTaTxnRtnCode.SERVER_EXCEPTION.getTitle());
        }
        return toaXml910012002;
    }
}