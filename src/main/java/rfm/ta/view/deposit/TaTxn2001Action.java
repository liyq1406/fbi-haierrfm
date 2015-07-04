package rfm.ta.view.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbTiaXml2001;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbToaXml2001;

import java.io.IOException;

/**
 * Created by XIANGYANG on 2015-7-1.
 * ������֤
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
            //1.��֯�뷿������ͨѶ���ģ����뷿������ͨѶ

            //2.���ݷ������ķ�����Ϣ����֯������ͨѶ����
            toa.getInfo().setRtncode("0000");
            toa.getInfo().setRtnmsg("���׳ɹ�");
        } catch (Exception e) {
            logger.error("������֤�����쳣.", e);
            toa.getInfo().setRtncode("1000");
            toa.getBody().setRtncode("1000");
            toa.getInfo().setRtnmsg("�����쳣");
            toa.getBody().setRtnmsg("�����쳣");
        }
        return toa.toString();
    }
}
