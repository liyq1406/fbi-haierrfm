package rfm.ta.view.deposit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbTiaXml2002;
import rfm.ta.gateway.hfnb.model.txn.TaHfnbToaXml2002;
import rfm.ta.gateway.sbs.domain.txn.model.msg.Maa41;
import rfm.ta.gateway.sbs.helper.XmlToBean;

import java.io.IOException;

/**
 * Created by XIANGYANG on 2015-7-1.
 * �������
 */

public class TaTxn2002Action extends AbstractTxnProcessor {
    private static Logger logger = LoggerFactory.getLogger(TaTxn2002Action.class);

    @Override
    public String process(String userid, String msgData) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        TaHfnbTiaXml2002 tia = (TaHfnbTiaXml2002) (new TaHfnbTiaXml2002().getTia(msgData));
        TaHfnbToaXml2002 toa = new TaHfnbToaXml2002();
        Maa41 maa41 = null;
        copyTiaInfoToToa(tia,toa);
        XmlToBean.CopyXmlToBean(tia, maa41);  //xmlת����Bean
        try {
            //1.��֯��SBSͨѶ�뱨�ģ�����SBSͨѶ
//            SOFForm form = taSbsService.callSbsTxn("aa41", maa41).get(0);
//            String formcode = form.getFormHeader().getFormCode();
            //2.����SBS������Ϣ����֯�뷿������ͨѶ����

            //3.���ݷ������ķ�����Ϣ����֯������ͨѶ����
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