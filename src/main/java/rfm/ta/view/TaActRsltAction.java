package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.common.enums.EnuTaFdcTxCode;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.account.TaActRsltService;
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
public class TaActRsltAction {
    private static final Logger logger = LoggerFactory.getLogger(TaActRsltAction.class);
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taActRsltService}")
    private TaActRsltService taActRsltService;

    private TaTxnFdc taTxnFdcValiSend;
    private TaTxnFdc taTxnFdcValiSendAndRecv;

    @PostConstruct
    public void init() {
        taTxnFdcValiSend=new TaTxnFdc();
        taTxnFdcValiSendAndRecv=new TaTxnFdc();
    }

    /*������֤��*/
    public void onBtnQry() {
        // ������֤��Ϣ
        taTxnFdcValiSend.setTxCode(EnuTaFdcTxCode.TRADE_2501.getCode());
        taActRsltService.sendAndRecvRealTimeTxn9902501(taTxnFdcValiSend);
        /*��֤���ѯ*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
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

    public TaActRsltService getTaActRsltService() {
        return taActRsltService;
    }

    public void setTaActRsltService(TaActRsltService taActRsltService) {
        this.taActRsltService = taActRsltService;
    }
}
