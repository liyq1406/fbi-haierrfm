package rfm.qd.view.accdetail;

import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.RsAccDetailService;
import rfm.qd.service.TradeService;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-9-8
 * Time: ����11:36
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class AccDetailAction {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AccDetailAction.class);
    @ManagedProperty(value = "#{rsAccDetailService}")
    private RsAccDetailService accDetailService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;
    private List<RsAccDetail> todayAccDetailList;
    private List<RsAccDetail> todayLoanAccDetailList;
    @ManagedProperty(value = "#{tradeService}")
    private TradeService tradeService;

    private String txnDate;

    @PostConstruct
    public void init() {
        todayAccDetailList = accDetailService.selectTodayAccDetails();
        todayLoanAccDetailList = accDetailService.selectTodayLoanAccDetails();
    }

    public String onSend() {
        // Ĭ���ȼ���Ƿ��е��ո���δ���˽���
        try {
            if (!tradeService.isHasUnsendTrade()) {
                if (todayAccDetailList.isEmpty()) {
                    MessageUtil.addWarn("û�д����ͼ�¼��");
                    return null;
                }
                if (clientBiService.sendTodayAccDetails(todayAccDetailList) == 1) {
                    MessageUtil.addInfo("���͵��ս�����ϸ��ɣ�");
                } else {
                    MessageUtil.addError("���͵��ս�����ϸʧ�ܣ�");
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("����ʧ��." + e.getMessage());
            logger.error("���͵��ս�����ϸʧ�ܣ�", e.getMessage());
        }
        return null;
    }

    // 2012-4-5 �������ʹ���׼�¼
    public String onSendLoanDetails() {
        try {
            if (clientBiService.sendTodayLoanAccDetails(todayLoanAccDetailList, txnDate) == 1) {
                MessageUtil.addInfo("���ʹ����¼��ɣ����ʹ��������" + todayLoanAccDetailList.size());
            } else {
                MessageUtil.addError("���ʹ����¼ʧ�ܣ�");
            }
        } catch (Exception e) {
            MessageUtil.addError("����ʧ��." + e.getMessage());
            logger.error("���͵��մ���׼�¼ʧ�ܣ�", e.getMessage());
        }
        return null;
    }

    //=======================================

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }

    public RsAccDetailService getAccDetailService() {
        return accDetailService;
    }

    public TradeService getTradeService() {
        return tradeService;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public void setAccDetailService(RsAccDetailService accDetailService) {
        this.accDetailService = accDetailService;
    }

    public List<RsAccDetail> getTodayAccDetailList() {
        return todayAccDetailList;
    }

    public void setTodayAccDetailList(List<RsAccDetail> todayAccDetailList) {
        this.todayAccDetailList = todayAccDetailList;
    }

    public List<RsAccDetail> getTodayLoanAccDetailList() {
        return todayLoanAccDetailList;
    }

    public void setTodayLoanAccDetailList(List<RsAccDetail> todayLoanAccDetailList) {
        this.todayLoanAccDetailList = todayLoanAccDetailList;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }
}
