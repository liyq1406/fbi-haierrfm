package rfm.ta.view;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.EnuTaTxCode;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.account.TaPaymentService;
import rfm.ta.service.his.TaTxnFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaPaymentAction {
    private static final Logger logger = LoggerFactory.getLogger(TaPaymentAction.class);
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taPaymentService}")
    private TaPaymentService taPaymentService;

    private TaTxnFdc taTxnFdcValiSend;
    private TaTxnFdc taTxnFdcValiSendAndRecv;
    private TaTxnFdc taTxnFdcActSend;
    private TaTxnFdc taTxnFdcActSendAndRecv;
    private TaTxnFdc taTxnFdcCanclSend;
    private TaTxnFdc taTxnFdcCanclSendAndRecv;

    @PostConstruct
    public void init() {
        taTxnFdcValiSend=new TaTxnFdc();
        taTxnFdcValiSendAndRecv=new TaTxnFdc();
        taTxnFdcActSend=new TaTxnFdc();
        taTxnFdcActSendAndRecv=new TaTxnFdc();
        taTxnFdcCanclSend=new TaTxnFdc();
        taTxnFdcCanclSendAndRecv=new TaTxnFdc();
    }

    /*������֤��*/
    public void onBtnValiClick() {
        // ������֤��Ϣ
        taTxnFdcValiSend.setTxCode(EnuTaTxCode.TRADE_2001.getCode());
        taPaymentService.sendAndRecvRealTimeTxn9902001(taTxnFdcValiSend);
        /*��֤���ѯ*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*��֤���������������*/
    public void onBtnActClick() {
        try {
            // ������֤��Ϣ
            TaTxnFdc taTxnFdcTemp=new TaTxnFdc();
            BeanUtils.copyProperties(taTxnFdcTemp,taTxnFdcValiSendAndRecv);
            taTxnFdcTemp.setTxCode(EnuTaTxCode.TRADE_2002.getCode());
            taPaymentService.sendAndRecvRealTimeTxn9902002(taTxnFdcTemp);
        /*���˺��ѯ*/
            taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
        }catch (Exception e){
            logger.error("��֤��������������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*���������*/
    public void onBtnCanclClick() {
        try {
            // ���ͳ�����Ϣ
            taTxnFdcCanclSend.setTxCode(EnuTaTxCode.TRADE_2011.getCode());
            taPaymentService.sendAndRecvRealTimeTxn9902011(taTxnFdcCanclSend);
            /*����������ѯ*/
            taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcCanclSend.getPkId());
        }catch (Exception e){
            logger.error("��������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaPaymentService getTaPaymentService() {
        return taPaymentService;
    }

    public void setTaPaymentService(TaPaymentService taPaymentService) {
        this.taPaymentService = taPaymentService;
    }

    public TaTxnFdcService getTaTxnFdcService() {
        return taTxnFdcService;
    }

    public void setTaTxnFdcService(TaTxnFdcService taTxnFdcService) {
        this.taTxnFdcService = taTxnFdcService;
    }

    public TaTxnFdc getTaTxnFdcValiSend() {
        return taTxnFdcValiSend;
    }

    public void setTaTxnFdcValiSend(TaTxnFdc taTxnFdcValiSend) {
        this.taTxnFdcValiSend = taTxnFdcValiSend;
    }

    public TaTxnFdc getTaTxnFdcValiSendAndRecv() {
        return taTxnFdcValiSendAndRecv;
    }

    public void setTaTxnFdcValiSendAndRecv(TaTxnFdc taTxnFdcValiSendAndRecv) {
        this.taTxnFdcValiSendAndRecv = taTxnFdcValiSendAndRecv;
    }

    public TaTxnFdc getTaTxnFdcActSend() {
        return taTxnFdcActSend;
    }

    public void setTaTxnFdcActSend(TaTxnFdc taTxnFdcActSend) {
        this.taTxnFdcActSend = taTxnFdcActSend;
    }

    public TaTxnFdc getTaTxnFdcActSendAndRecv() {
        return taTxnFdcActSendAndRecv;
    }

    public void setTaTxnFdcActSendAndRecv(TaTxnFdc taTxnFdcActSendAndRecv) {
        this.taTxnFdcActSendAndRecv = taTxnFdcActSendAndRecv;
    }

    public TaTxnFdc getTaTxnFdcCanclSend() {
        return taTxnFdcCanclSend;
    }

    public void setTaTxnFdcCanclSend(TaTxnFdc taTxnFdcCanclSend) {
        this.taTxnFdcCanclSend = taTxnFdcCanclSend;
    }

    public TaTxnFdc getTaTxnFdcCanclSendAndRecv() {
        return taTxnFdcCanclSendAndRecv;
    }

    public void setTaTxnFdcCanclSendAndRecv(TaTxnFdc taTxnFdcCanclSendAndRecv) {
        this.taTxnFdcCanclSendAndRecv = taTxnFdcCanclSendAndRecv;
    }
}
