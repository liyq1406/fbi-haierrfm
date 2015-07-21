package rfm.ta.view.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
import rfm.ta.gateway.dep.model.base.TiaXml;
import rfm.ta.gateway.dep.model.base.ToaXml;
import rfm.ta.gateway.dep.model.txn.TiaXml910012011;
import rfm.ta.gateway.dep.model.txn.ToaXml910012011;

/**
 * Created by XIANGYANG on 2015-7-20.
 * 交存冲正
 */

@Component
public class DepTxn910012011Processor extends DepAbstractTxnProcessor {
    private static Logger logger = LoggerFactory.getLogger(DepTxn910012001Processor.class);

    @Override
    public ToaXml process(TiaXml tiaPara)  throws Exception{
        TiaXml910012011 tiaXml910012011 = (TiaXml910012011)tiaPara;
        ToaXml910012011 toaXml910012011 = new ToaXml910012011();
        copyTiaInfoToToa(tiaXml910012011,toaXml910012011);
        try {
            toaXml910012011.getInfo().setRtncode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());
            toaXml910012011.getInfo().setRtnmsg(EnuTaTxnRtnCode.TXN_PROCESSED.getTitle());
        } catch (Exception e) {
            logger.error("交存冲正交易异常.", e);
            toaXml910012011.getInfo().setRtncode(EnuTaTxnRtnCode.SERVER_EXCEPTION.getCode());
            toaXml910012011.getBody().setRtncode(EnuTaTxnRtnCode.SERVER_EXCEPTION.getCode());
            toaXml910012011.getInfo().setRtnmsg(EnuTaTxnRtnCode.SERVER_EXCEPTION.getTitle());
            toaXml910012011.getBody().setRtnmsg(EnuTaTxnRtnCode.SERVER_EXCEPTION.getTitle());
        }
        return toaXml910012011;
    }
}