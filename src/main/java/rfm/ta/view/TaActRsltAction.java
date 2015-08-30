package rfm.ta.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.enums.EnuActRslt;
import rfm.ta.common.enums.EnuTaFdcTxCode;
import rfm.ta.common.enums.EnuTaTxnRtnCode;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.his.TaTxnFdcService;
import rfm.ta.service.dep.TaFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: 下午2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaActRsltAction {
    private static final Logger logger = LoggerFactory.getLogger(TaActRsltAction.class);
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taFdcService}")
    private TaFdcService taFdcService;

    private TaTxnFdc taTxnFdcValiSend;
    private TaTxnFdc taTxnFdcValiSendAndRecv;
    private EnuActRslt enuActRslt = EnuActRslt.SUCCESS;
    private EnuTaTxnRtnCode enuTaTxnRtnCode = EnuTaTxnRtnCode.TXN_PROCESSED;

    private String isDebugExec;

    @PostConstruct
    public void init() {
        taTxnFdcValiSend=new TaTxnFdc();
        taTxnFdcValiSendAndRecv=new TaTxnFdc();
    }

    /*划拨验证用*/
    public void onBtnQry() {
        // 发送验证信息
        taTxnFdcValiSend.setTxCode(EnuTaFdcTxCode.TRADE_2501.getCode());
        taFdcService.sendAndRecvRealTimeTxn9902501(taTxnFdcValiSend);
        /*验证后查询*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    public EnuTaTxnRtnCode getEnuTaTxnRtnCode() {
        return enuTaTxnRtnCode;
    }

    public void setEnuTaTxnRtnCode(EnuTaTxnRtnCode enuTaTxnRtnCode) {
        this.enuTaTxnRtnCode = enuTaTxnRtnCode;
    }

    public EnuActRslt getEnuActRslt() {
        return enuActRslt;
    }

    public void setEnuActRslt(EnuActRslt enuActRslt) {
        this.enuActRslt = enuActRslt;
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

    public TaFdcService getTaFdcService() {
        return taFdcService;
    }

    public void setTaFdcService(TaFdcService taFdcService) {
        this.taFdcService = taFdcService;
    }

    public String getIsDebugExec() {
        return isDebugExec= PropertyManager.getProperty("execType");
    }
}
