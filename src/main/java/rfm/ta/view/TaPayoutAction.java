package rfm.ta.view;

import com.longtu.framework.util.BeanUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.fbi.dep.model.base.TOA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.common.enums.EnuExecType;
import rfm.ta.common.enums.EnuTaTxCode;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.account.TaPayoutService;
import rfm.ta.service.his.TaTxnFdcService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: hanjianlong
 * Date: 15-6-30
 * Time: ����2:12
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class TaPayoutAction {
    private static final Logger logger = LoggerFactory.getLogger(TaPayoutAction.class);
    public static String EXEC_TYPE = PropertyManager.getProperty("execType");
    @ManagedProperty(value = "#{taTxnFdcService}")
    private TaTxnFdcService taTxnFdcService;

    @ManagedProperty(value = "#{taPayoutService}")
    private TaPayoutService taPayoutService;

    private TaTxnFdc taTxnFdcValiSend;
    private TaTxnFdc taTxnFdcValiSendAndRecv;
    private TaTxnFdc taTxnFdcActSend;
    private TaTxnFdc taTxnFdcActSendAndRecv;
    private TaTxnFdc taTxnFdcCanclSend;
    private TaTxnFdc taTxnFdcCanclSendAndRecv;

    private String strVisableByExecType;

    @PostConstruct
    public void init() {
        taTxnFdcValiSend=new TaTxnFdc();
        taTxnFdcValiSendAndRecv=new TaTxnFdc();
        taTxnFdcActSend=new TaTxnFdc();
        taTxnFdcActSendAndRecv=new TaTxnFdc();
        taTxnFdcCanclSend=new TaTxnFdc();
        taTxnFdcCanclSendAndRecv=new TaTxnFdc();
        if(EnuExecType.EXEC_TYPE_DEBUG.getCode().equals(EXEC_TYPE)){
            strVisableByExecType="true";
        }else{
            strVisableByExecType="false";
        }
    }

    /*������֤��*/
    public void onBtnValiClick() {
        // ������֤��Ϣ
        taTxnFdcValiSend.setTxCode(EnuTaTxCode.TRADE_2101.getCode());
        taPayoutService.sendAndRecvRealTimeTxn9902101(taTxnFdcValiSend);
        /*��֤���ѯ*/
        taTxnFdcValiSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcValiSend.getPkId());
    }

    /*��֤����������������*/
    public void onBtnActClick() {
        try {
            // ��SBS���ͼ�����Ϣ
            TOA toaSbs=taPayoutService.sendAndRecvRealTimeTxn900012102(taTxnFdcValiSendAndRecv);
            if(toaSbs !=null) {
                // ��̩�����ز����ķ��ͼ�����Ϣ
                TaTxnFdc taTxnFdcTemp = new TaTxnFdc();
                BeanUtils.copyProperties(taTxnFdcTemp, taTxnFdcValiSendAndRecv);
                taTxnFdcTemp.setTxCode(EnuTaTxCode.TRADE_2102.getCode());
                taPayoutService.sendAndRecvRealTimeTxn9902102(taTxnFdcTemp);
                /*���˺��ѯ*/
                taTxnFdcActSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcTemp.getPkId());
            }
        }catch (Exception e){
            logger.error("��֤���������������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    /*����������*/
    public void onBtnCanclClick() {
        try {
            // ��SBS���ͼ�����Ϣ
            TOA toaSbs=taPayoutService.sendAndRecvRealTimeTxn900012111(taTxnFdcCanclSend);
            if(toaSbs !=null) {
                // ��̩�����ز����ķ��ͼ�����Ϣ
                taTxnFdcCanclSend.setTxCode(EnuTaTxCode.TRADE_2111.getCode());
                taPayoutService.sendAndRecvRealTimeTxn9902111(taTxnFdcCanclSend);
                /*�����������ѯ*/
                taTxnFdcCanclSendAndRecv = taTxnFdcService.selectedRecordsByKey(taTxnFdcCanclSend.getPkId());
            }
        }catch (Exception e){
            logger.error("���������ã�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaPayoutService getTaPayoutService() {
        return taPayoutService;
    }

    public void setTaPayoutService(TaPayoutService taPayoutService) {
        this.taPayoutService = taPayoutService;
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

    public String getStrVisableByExecType() {
        return strVisableByExecType;
    }
}
