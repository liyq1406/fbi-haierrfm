package rfm.ta.service.dep;

import org.fbi.dep.model.base.TIA;
import org.fbi.dep.model.base.TOA;
import org.springframework.transaction.annotation.Transactional;


public abstract class DepAbstractTxnProcessor {

    public abstract TOA process(TIA tia) throws Exception;

    @Transactional
    public TOA run(TIA tia) throws Exception {
        return process(tia);
    }
}
