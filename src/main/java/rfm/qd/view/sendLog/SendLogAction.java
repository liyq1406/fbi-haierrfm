package rfm.qd.view.sendLog;

import rfm.qd.common.constant.SendLogResult;
import rfm.qd.repository.model.RsSendLog;
import rfm.qd.service.SendLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhangxiaobo
 * Date: 11-8-30
 * Time: 下午3:41
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@ViewScoped
public class SendLogAction {
    private Logger logger = LoggerFactory.getLogger(SendLogAction.class);
    @ManagedProperty(value = "#{sendLogService}")
    private SendLogService sendLogService;
    private List<RsSendLog> sendLogList;
    private String startDate;
    private String endDate;
    private SendLogResult sendLogResult = SendLogResult.SEND_OVER;
    private List<SelectItem> txnResultOptions;
    private String txnResult;
    @PostConstruct
    public void init() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance(Locale.CHINA);//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.MONTH, -1);
        String monthAgo = sdf.format(cal.getTime());
        String today = sdf.format(new Date());
        logger.info(monthAgo);
        logger.info(today);
        sendLogList = sendLogService.qrySendLogs(monthAgo, today, "");
        txnResultOptions = new ArrayList<SelectItem>();
        txnResultOptions.add(new SelectItem("", ""));
        txnResultOptions.add(new SelectItem(SendLogResult.SEND_ERR.getCode(), SendLogResult.SEND_ERR.getTitle()));
        txnResultOptions.add(new SelectItem(SendLogResult.QRYED_ERR.getCode(), SendLogResult.QRYED_ERR.getTitle()));
        txnResultOptions.add(new SelectItem(SendLogResult.SEND_OVER.getCode(), SendLogResult.SEND_OVER.getTitle()));
        txnResultOptions.add(new SelectItem(SendLogResult.QRYED.getCode(), SendLogResult.QRYED.getTitle()));
    }

    public String onQuery() {
        try {
            sendLogList = sendLogService.qrySendLogs(startDate, endDate, txnResult);
            boolean hasErr = false;
            for(RsSendLog record : sendLogList) {
                if(SendLogResult.QRYED_ERR.getCode().equals(record.getTxnResult().trim())
                        || SendLogResult.SEND_ERR.getCode().equals(record.getTxnResult().trim())) {
                   hasErr = true;
                   break;
                }
            }
            if(hasErr) {
                MessageUtil.addError("存在交易失败日，请检查交易失败日期，手动发送数据。");
            }
        } catch (Exception e) {
            logger.error("查询失败。", e);
            MessageUtil.addError("查询失败。" + e.getMessage());
        }
        return null;
    }

    // ================================================


    public String getTxnResult() {
        return txnResult;
    }

    public void setTxnResult(String txnResult) {
        this.txnResult = txnResult;
    }

    public List<SelectItem> getTxnResultOptions() {
        return txnResultOptions;
    }

    public void setTxnResultOptions(List<SelectItem> txnResultOptions) {
        this.txnResultOptions = txnResultOptions;
    }

    public SendLogResult getSendLogResult() {
        return sendLogResult;
    }

    public void setSendLogResult(SendLogResult sendLogResult) {
        this.sendLogResult = sendLogResult;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<RsSendLog> getSendLogList() {
        return sendLogList;
    }

    public void setSendLogList(List<RsSendLog> sendLogList) {
        this.sendLogList = sendLogList;
    }

    public SendLogService getSendLogService() {
        return sendLogService;
    }

    public void setSendLogService(SendLogService sendLogService) {
        this.sendLogService = sendLogService;
    }
}
