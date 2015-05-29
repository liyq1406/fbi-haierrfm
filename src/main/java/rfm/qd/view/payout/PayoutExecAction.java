package rfm.qd.view.payout;

import rfm.qd.common.constant.WorkResult;
import rfm.qd.repository.model.RsPayout;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.PayoutService;
import org.slf4j.Logger;
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
 * Date: 11-8-30
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class PayoutExecAction {
    private Logger logger = LoggerFactory.getLogger(PayoutExecAction.class);

    private RsPayout rsPayout;
    @ManagedProperty(value = "#{payoutService}")
    private PayoutService payoutService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;
    private List<RsPayout> passPayoutList;
    private List<RsPayout> payOverList;
    private List<RsPayout> sendOverList;
    private RsPayout selectedRecord;
    private RsPayout[] selectedRecords;
    private RsPayout[] toSendRecords;
    private WorkResult workResult = WorkResult.CREATE;

    @PostConstruct
    public void init() {
        rsPayout = new RsPayout();
        passPayoutList = payoutService.selectRecordsByWorkResult(WorkResult.PASS.getCode());
        payOverList = payoutService.selectRecordsByWorkResult(WorkResult.COMMIT.getCode());
        sendOverList = payoutService.selectRecordsByWorkResult(WorkResult.SENT.getCode());
    }

    public String onAllExecute() {
        if (passPayoutList.isEmpty()) {
            MessageUtil.addWarn("可入账记录为空！");
        } else {
            try {
                for (RsPayout record : passPayoutList) {
                    if (payoutService.updateRsPayoutToExec(record) == -1) {
                        throw new RuntimeException("【记录更新失败】付款监管账号：" + record.getPayAccount());
                    }
                }
            } catch (Exception e) {
                logger.error("操作失败." + e.getMessage());
                MessageUtil.addError("操作失败." + e.getMessage());
                return null;
            }
            MessageUtil.addInfo("入账完成!");
            init();
        }
        return null;
    }

    public String onMultiExecute() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔记录！");
        } else {
            try {
                for (RsPayout record : selectedRecords) {
                    if (payoutService.updateRsPayoutToExec(record) == -1) {
                        throw new RuntimeException("【记录更新失败】付款监管账号：" + record.getPayAccount());
                    }
                }
            } catch (Exception e) {
                logger.error("操作失败." + e.getMessage());
                MessageUtil.addError("操作失败." + e.getMessage());
                return null;
            }
            MessageUtil.addInfo("入账完成!");

            init();
        }
        return null;
    }

    public String onAllSend() {
        if (payOverList.isEmpty()) {
            MessageUtil.addWarn("可发送记录为空！");
        } else {
            int sentResult = 1;
            try {
                for (RsPayout record : payOverList) {
                    sentResult = clientBiService.sendRsPayoutMsg(record);
                    if (sentResult != 1) {
                        throw new RuntimeException("发送失败");
                    }
                }
                MessageUtil.addInfo("发送完成！");
            } catch (Exception e) {
                logger.error("操作失败." + e.getMessage());
                MessageUtil.addError("操作失败." + e.getMessage());
                return null;
            }
            init();
        }
        return null;
    }

    public String onMultiSend() {
        if (toSendRecords == null || toSendRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔待发送记录！");
        } else {
            int sentResult = 1;
            try {
                for (RsPayout record : toSendRecords) {
                    sentResult = clientBiService.sendRsPayoutMsg(record);
                    if (sentResult != 1) {
                        throw new RuntimeException("发送失败");
                    }
                }
                MessageUtil.addInfo("发送完成！");
            } catch (Exception e) {
                logger.error("操作失败." + e.getMessage());
                MessageUtil.addError("操作失败." + e.getMessage());
                return null;
            }
            init();
        }
        return null;
    }

    //=========================================

    public RsPayout getRsPayout() {
        return rsPayout;
    }

    public void setRsPayout(RsPayout rsPayout) {
        this.rsPayout = rsPayout;
    }

    public PayoutService getPayoutService() {
        return payoutService;
    }

    public void setPayoutService(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    public RsPayout getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(RsPayout selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }

    public RsPayout[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(RsPayout[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public List<RsPayout> getPassPayoutList() {
        return passPayoutList;
    }

    public void setPassPayoutList(List<RsPayout> passPayoutList) {
        this.passPayoutList = passPayoutList;
    }

    public List<RsPayout> getPayOverList() {
        return payOverList;
    }

    public void setPayOverList(List<RsPayout> payOverList) {
        this.payOverList = payOverList;
    }

    public RsPayout[] getToSendRecords() {
        return toSendRecords;
    }

    public void setToSendRecords(RsPayout[] toSendRecords) {
        this.toSendRecords = toSendRecords;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }

    public List<RsPayout> getSendOverList() {
        return sendOverList;
    }

    public void setSendOverList(List<RsPayout> sendOverList) {
        this.sendOverList = sendOverList;
    }
}
