package rfm.ta.gateway.sbs.domain.txn.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rfm.ta.gateway.sbs.domain.core.SBSResponse;
import rfm.ta.gateway.sbs.domain.core.domain.SOFForm;
import rfm.ta.gateway.sbs.domain.service.CoreTxnService;
import rfm.ta.gateway.sbs.domain.txn.model.msg.M8873;
import rfm.ta.gateway.sbs.domain.txn.model.msg.MTia;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichao.W At 2015/7/9 10:31
 * wanglichao@163.com
 * 泰安房产对账
 */
@Component
public class Txn8873Action extends AbstractTxnAction{
    private static Logger logger = LoggerFactory.getLogger(Txn8873Action.class);

    @Autowired
    private CoreTxnService coreTxnService;
    @Override
    protected List<SOFForm> process(String termid, String tellerid, String auttlr, String autpwd, MTia tia) throws Exception {

        M8873 m8873 = (M8873)tia;

        logger.info("[8873-房地产对账] 对账日期：" + m8873.getERYDA1());

        List<String> paramList = new ArrayList<>();
        paramList.add(m8873.getERYDA1());
        paramList.add(m8873.getBEGNUM());

        // 执行sbs交易
        SBSResponse response = coreTxnService.execute(termid, tellerid, "8873", paramList);

        StringBuffer rtnFormCodes = new StringBuffer("[8873-房地产对账] 对账日期：" + m8873.getERYDA1()
                + " 返回码：");
        for (String formcode : response.getFormCodes()) {
            rtnFormCodes.append("[").append(formcode).append("]");
        }
        logger.info(rtnFormCodes.toString());
        return response.getSofForms();
    }
}
