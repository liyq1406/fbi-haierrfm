package rfm.ta.view.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
import rfm.ta.gateway.dep.model.base.TiaXml;
import rfm.ta.gateway.dep.model.base.ToaXml;
import rfm.ta.gateway.dep.model.txn.TiaXml910012001;
import rfm.ta.gateway.dep.model.txn.ToaXml910012001;

/**
 * Created by XIANGYANG on 2015-7-20.
 * 交存验证
 */
@Component
public class DepTxn910012001Processor extends DepAbstractTxnProcessor {
    private static Logger logger = LoggerFactory.getLogger(DepTxn910012001Processor.class);

    @Override
    public ToaXml process(TiaXml tiaPara) throws Exception{
        TiaXml910012001 tiaXml910012001 = (TiaXml910012001)tiaPara;
        ToaXml910012001 toaXml910012001 = new ToaXml910012001();
        copyTiaInfoToToa(tiaXml910012001,toaXml910012001);
        try {
            toaXml910012001.getInfo().setRtncode(EnuTaTxnRtnCode.TXN_PROCESSED.getCode());
            toaXml910012001.getInfo().setRtnmsg(EnuTaTxnRtnCode.TXN_PROCESSED.getTitle());
        } catch (Exception e) {
            logger.error("交存验证交易异常.", e);
            toaXml910012001.getInfo().setRtncode(EnuTaTxnRtnCode.SERVER_EXCEPTION.getCode());
            toaXml910012001.getBody().setRtncode(EnuTaTxnRtnCode.SERVER_EXCEPTION.getCode());
            toaXml910012001.getInfo().setRtnmsg(EnuTaTxnRtnCode.SERVER_EXCEPTION.getTitle());
            toaXml910012001.getBody().setRtnmsg(EnuTaTxnRtnCode.SERVER_EXCEPTION.getTitle());
        }
        return toaXml910012001;
    }
}
