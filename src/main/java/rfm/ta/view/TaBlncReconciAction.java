package rfm.ta.view;

import org.fbi.dep.model.txn.Toa900012701;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.service.account.TaAccService;
import rfm.ta.service.account.TaBlncReconciService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * ��̩��������Action
 * Created by Thinkpad on 2015/8/13.
 */
@ManagedBean
@ViewScoped
public class TaBlncReconciAction {
    private static final Logger logger = LoggerFactory.getLogger(TaBlncReconciAction.class);

    @ManagedProperty(value = "#{taBlncReconciService}")
    private TaBlncReconciService taBlncReconciService;

    @ManagedProperty(value = "#{taAccService}")
    private TaAccService taAccService;

    private List<TaRsAcc> taRsAccList;

    @PostConstruct
    public void init() {
        // ȡ�����м���еļ���˺�����
        taRsAccList = taAccService.qryAllMonitRecords();
    }

    /**
     * ��ȡsbs����
     */
    public void onQryLocaldata() {
        try {
            if(taRsAccList.size() <=0) {
                return;
            }

            // ���ͼ���˺ŵ�SBS��ѯ���
            List<Toa900012701> toaSbs=taBlncReconciService.sendAndRecvRealTimeTxn900012701(taRsAccList);
            if(toaSbs !=null) {
//                if(("0000").equals(toaSbs.getHeader().RETURN_CODE)){ // SBS���˳ɹ��Ĵ���
//                    taRsAccDtl.setActFlag(EnuActFlag.ACT_SUCCESS.getCode());
//                    taAccDetlService.updateRecord(taRsAccDtl);
//                } else { // SBS����ʧ�ܵĴ���
//                    taAccDetlService.deleteRecord(taRsAccDtl.getPkId());
//                }
            }
        }catch (Exception e){
            logger.error("��ȡsbs���ݣ�", e);
            MessageUtil.addError(e.getMessage());
        }
    }

    //= = = = = = = = = = = = = = =  get set = = = = = = = = = = = = = = = =
    public TaBlncReconciService getTaBlncReconciService() {
        return taBlncReconciService;
    }

    public void setTaBlncReconciService(TaBlncReconciService taBlncReconciService) {
        this.taBlncReconciService = taBlncReconciService;
    }

    public TaAccService getTaAccService() {
        return taAccService;
    }

    public void setTaAccService(TaAccService taAccService) {
        this.taAccService = taAccService;
    }

    public List<TaRsAcc> getTaRsAccList() {
        return taRsAccList;
    }

    public void setTaRsAccList(List<TaRsAcc> taRsAccList) {
        this.taRsAccList = taRsAccList;
    }
}
