package rfm.ta.tagateway.tasbs.txn.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rfm.ta.tagateway.tasbs.core.SBSResponse;
import rfm.ta.tagateway.tasbs.core.domain.SOFForm;
import rfm.ta.tagateway.tasbs.service.CoreTxnService;
import rfm.ta.tagateway.tasbs.txn.model.msg.MTia;
import rfm.ta.tagateway.tasbs.txn.model.msg.Maa41;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lichao.W At 2015/6/29 17:53
 * wanglichao@163.com
 * 泰安房地产入账
 */
@Component
public class Txnaa41Action extends AbstractTxnAction {

    private static Logger logger = LoggerFactory.getLogger(Txnaa41Action.class);
    @Autowired
    private CoreTxnService coreTxnService;

    @Override
    public List<SOFForm> process(String termid, String tellerid, String auttlr, String autpwd, MTia tia) throws Exception {

        Maa41 maa41 = (Maa41) tia;
        logger.info("[aa41-房地产入账交易] 转出账号：" + maa41.getIPTAC1()+"转入账号"+maa41.getIPTAC2());

        List<String> paramList = new ArrayList<>();

        paramList.add(maa41.getACTTY1());
        paramList.add(maa41.getIPTAC1());
        paramList.add(maa41.getDRAMD1());
        paramList.add(maa41.getACTNM1());
        paramList.add(maa41.getCUSPW1());
        paramList.add(maa41.getPASTYP());
        paramList.add(maa41.getPASSNO());
        paramList.add(maa41.getPAPTYP());
        paramList.add(maa41.getPAPCDE());
        paramList.add(maa41.getPAPMAC());
        paramList.add(maa41.getSGNDAT());
        paramList.add(maa41.getNBKFL1());
        paramList.add(maa41.getAUTSEQ());
        paramList.add(maa41.getAUTDAT());
        paramList.add(maa41.getTXNAMT());
        paramList.add(maa41.getACTTY2());
        paramList.add(maa41.getIPTAC2());
        paramList.add(maa41.getACTNM2());
        paramList.add(maa41.getNBKFL2());
        paramList.add(maa41.getTXNDAT());
        paramList.add(maa41.getREMARK());
        paramList.add(maa41.getANACDE());
        paramList.add(maa41.getMAGFL1());
        paramList.add(maa41.getMAGFL2());

        // 执行sbs交易
        SBSResponse response = coreTxnService.execute(termid, tellerid, "aa41", paramList);

        StringBuffer rtnFormCodes = new StringBuffer("[aa41-房地产入账交易] 转出账号：" + maa41.getIPTAC1()+"转入账号"+maa41.getIPTAC2()
                + " 返回码：");
        for (String formcode : response.getFormCodes()) {
            rtnFormCodes.append("[").append(formcode).append("]");
        }
        logger.info(rtnFormCodes.toString());
        return response.getSofForms();
    }
}
