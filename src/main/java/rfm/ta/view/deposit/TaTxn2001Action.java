package rfm.ta.view.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbTiaXml2001;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbToaXml2001;

import java.io.IOException;

/**
 * Created by XIANGYANG on 2015-7-1.
 * 交存验证
 */

public class TaTxn2001Action extends AbstractTxnProcessor {
    private static Logger logger = LoggerFactory.getLogger(TaTxn2001Action.class);

    @Override
    public String process(String userid, String msgData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        TaHfnbTiaXml2001 tia = (TaHfnbTiaXml2001) (new TaHfnbTiaXml2001().getTia(msgData));
        TaHfnbToaXml2001 toa = new TaHfnbToaXml2001();
        copyTiaInfoToToa(tia,toa);
        try {
//            toa.getInfo().setTxncode();
            //1.组织与房产中心通讯报文，并与房产中心通讯

            //2.根据房产中心返回信息，组织与网银通讯报文
            toa.getInfo().setRtncode("0000");
            toa.getInfo().setRtnmsg("交易成功");
        } catch (Exception e) {
            logger.error("交存验证交易异常.", e);
            toa.getInfo().setRtncode("1000");
            toa.getBody().setRtncode("1000");
            toa.getInfo().setRtnmsg("交易异常");
            toa.getBody().setRtnmsg("交易异常");
        }
        return toa.toString();
    }
}
