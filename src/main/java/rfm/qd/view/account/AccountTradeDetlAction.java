package rfm.qd.view.account;

import rfm.qd.common.constant.TradeStatus;
import rfm.qd.common.constant.TradeType;
import rfm.qd.repository.model.QdCbsAccTxn;
import rfm.qd.repository.model.QdRsAccDetail;
import rfm.qd.service.CbusActTxnService;
import rfm.qd.service.RsAccDetailService;
import rfm.qd.service.account.AccountDetlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import platform.service.PtenudetailService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: haiyuhuang
 * Date: 11-9-6
 * Time: ����10:07
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class AccountTradeDetlAction {
    private static final Logger logger = LoggerFactory.getLogger(AccountTradeDetlAction.class);
    @ManagedProperty(value = "#{accountDetlService}")
    private AccountDetlService accountDetlService;
    @ManagedProperty(value = "#{ptenudetailService}")
    private PtenudetailService toolsService;
    @ManagedProperty(value = "#{rsAccDetailService}")
    private RsAccDetailService accDetailService;
    private Date beginDate;
    private Date endDate;
    private String acctname;
    private String acctno;
    private List<QdRsAccDetail> qdRsAccDetails;
    private QdRsAccDetail qdRsAccDetail;
    private List<QdRsAccDetail> qdRsAccDetailsInit;
    private SimpleDateFormat sdf10 = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf8 = new SimpleDateFormat("yyyyMMdd");
    private List<SelectItem> actDetlStatusOptions;

    @ManagedProperty(value = "#{cbusActTxnService}")
    private CbusActTxnService cbusActTxnService;
    private List<QdCbsAccTxn> qdCbsAccTxnList;
    private String strStartDate;
    private String strEndDate;

    @PostConstruct
    public void init() {
        actDetlStatusOptions = returnStatusOptions();
        qdRsAccDetail = new QdRsAccDetail();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        beginDate = cal.getTime();
        endDate = new Date();
        strStartDate = sdf8.format(beginDate);
        strEndDate = sdf8.format(endDate);
//        rsAccDetails = accountDetlService.selectedRecordsByTradeDate(acctname, acctno
//                , sdf10.format(beginDate), sdf10.format(endDate));
        List<String> statusfalgs = new ArrayList<String>();
        statusfalgs.add(0, TradeStatus.CANCEL.getCode());
        statusfalgs.add(1, TradeStatus.BACK.getCode());
        //�ɹ�¼��(�������˻ص�)
        qdRsAccDetailsInit = accountDetlService.selectedRecordsForChk(TradeType.INTEREST.getCode(), statusfalgs);

    }

    private List<SelectItem> returnStatusOptions() {
        List<SelectItem> items = new ArrayList<SelectItem>();
        SelectItem item;
        item = new SelectItem("", "ȫ��");
        items.add(item);
        item = new SelectItem(TradeStatus.CANCEL.getCode(), TradeStatus.CANCEL.getTitle());
        items.add(item);
        item = new SelectItem(TradeStatus.BACK.getCode(), TradeStatus.BACK.getTitle());
        items.add(item);
        return items;
    }

    public String onDelete() {
        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = context.getExternalContext().getRequestParameterMap().get("pkid").toString();
        QdRsAccDetail record = accDetailService.selectAccDetailByPkid(pkid);
        record.setDeletedFlag("1");
        if (accDetailService.updateAccDetail(record) == 1) {
            MessageUtil.addInfo("�˻�" + record.getAccountCode() + "��Ϣ��ɾ����");
            init();
        } else {
            MessageUtil.addError("�˻�" + record.getAccountCode() + "��Ϣɾ��ʧ�ܣ�");
        }
        return null;
    }

    public void onBtnQueryClick() {
        try {
            qdRsAccDetails = accountDetlService.selectedRecordsByTradeDate(acctname, acctno,
                    sdf10.format(beginDate), sdf10.format(endDate));
            if (qdRsAccDetails.isEmpty()) {
                MessageUtil.addWarn("û�в�ѯ����ϸ��¼��");
            }
        } catch (Exception e) {
            logger.error("��ѯʧ�ܡ�" + e.getMessage());
            MessageUtil.addError("��ѯʧ�ܡ�" + e.getMessage());

        }
    }

    public void onQueryCbs() {
        try {
            qdCbsAccTxnList = cbusActTxnService.qryCbsAccTxns(acctname, acctno,
                    strStartDate, strEndDate);
            if (qdCbsAccTxnList.isEmpty()) {
                MessageUtil.addWarn("û�в�ѯ����ϸ��¼��");
            }
        } catch (Exception e) {
            logger.error("��ѯʧ�ܡ�" + e.getMessage());
            MessageUtil.addError("��ѯʧ�ܡ�" + e.getMessage());
        }
    }

    // -----------------------------------------------


    public String getStrEndDate() {
        return strEndDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    public String getStrStartDate() {
        return strStartDate;
    }

    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public List<QdCbsAccTxn> getQdCbsAccTxnList() {
        return qdCbsAccTxnList;
    }

    public void setQdCbsAccTxnList(List<QdCbsAccTxn> qdCbsAccTxnList) {
        this.qdCbsAccTxnList = qdCbsAccTxnList;
    }

    public CbusActTxnService getCbusActTxnService() {
        return cbusActTxnService;
    }

    public void setCbusActTxnService(CbusActTxnService cbusActTxnService) {
        this.cbusActTxnService = cbusActTxnService;
    }

    public String getAcctname() {
        return acctname;
    }

    public void setAcctname(String acctname) {
        this.acctname = acctname;
    }

    public String getAcctno() {
        return acctno;
    }

    public void setAcctno(String acctno) {
        this.acctno = acctno;
    }

    public List<QdRsAccDetail> getQdRsAccDetailsInit() {
        return qdRsAccDetailsInit;
    }

    public void setQdRsAccDetailsInit(List<QdRsAccDetail> qdRsAccDetailsInit) {
        this.qdRsAccDetailsInit = qdRsAccDetailsInit;
    }

    public QdRsAccDetail getQdRsAccDetail() {
        return qdRsAccDetail;
    }

    public void setQdRsAccDetail(QdRsAccDetail qdRsAccDetail) {
        this.qdRsAccDetail = qdRsAccDetail;
    }

    public AccountDetlService getAccountDetlService() {
        return accountDetlService;
    }

    public RsAccDetailService getAccDetailService() {
        return accDetailService;
    }

    public void setAccDetailService(RsAccDetailService accDetailService) {
        this.accDetailService = accDetailService;
    }

    public void setAccountDetlService(AccountDetlService accountDetlService) {
        this.accountDetlService = accountDetlService;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<QdRsAccDetail> getQdRsAccDetails() {
        return qdRsAccDetails;
    }

    public void setQdRsAccDetails(List<QdRsAccDetail> qdRsAccDetails) {
        this.qdRsAccDetails = qdRsAccDetails;
    }

    public PtenudetailService getToolsService() {
        return toolsService;
    }

    public void setToolsService(PtenudetailService toolsService) {
        this.toolsService = toolsService;
    }

    public List<SelectItem> getActDetlStatusOptions() {
        return actDetlStatusOptions;
    }

    public void setActDetlStatusOptions(List<SelectItem> actDetlStatusOptions) {
        this.actDetlStatusOptions = actDetlStatusOptions;
    }
}
