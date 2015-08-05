package rfm.qd.view.cbus;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import common.utils.ToolUtil;
import rfm.qd.common.constant.RefundStatus;
import rfm.qd.common.constant.WorkResult;
import rfm.qd.gateway.utils.StringUtil;
import rfm.qd.repository.model.QdRsPayout;
import rfm.qd.repository.model.QdRsPlanCtrl;
import rfm.qd.service.CbusPayoutService;
import rfm.qd.service.ClientBiService;
import rfm.qd.service.PayoutService;
import rfm.qd.service.expensesplan.ExpensesPlanService;
import org.apache.commons.lang.StringUtils;
import org.primefaces.component.commandbutton.CommandButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.security.OperatorManager;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class CbusPayoutExecAction {
    private Logger logger = LoggerFactory.getLogger(CbusPayoutExecAction.class);

    private QdRsPayout qdRsPayout;
    @ManagedProperty(value = "#{cbusPayoutService}")
    private CbusPayoutService cbusPayoutService;
    @ManagedProperty(value = "#{clientBiService}")
    private ClientBiService clientBiService;
    @ManagedProperty(value = "#{payoutService}")
    private PayoutService payoutService;
    @ManagedProperty(value = "#{expensesPlanService}")
    private ExpensesPlanService expensesPlanService;

    private List<QdRsPayout> passPayoutList;
    private List<QdRsPayout> payOverList;
    private List<QdRsPayout> payFailList;
    private List<QdRsPayout> sendOverList;
    private List<QdRsPayout> printVchList;
    private QdRsPayout selectedRecord;
    private QdRsPayout[] selectedRecords;
    private QdRsPayout[] toSendRecords;
    private WorkResult workResult = WorkResult.CREATE;
    private RefundStatus statusFlag = RefundStatus.ACCOUNT_SUCCESS;
    private QdRsPlanCtrl planCtrl;
    private boolean isRunning;
    private String jscript;

    @PostConstruct
    public void init() {
//        rsPayout = new RsPayout();
        passPayoutList = cbusPayoutService.selectRecordsByWorkResult(WorkResult.PASS.getCode());
        payOverList = cbusPayoutService.selectRecordsByWorkResult(WorkResult.COMMIT.getCode());
        payFailList = cbusPayoutService.selectRecordsByWorkResult(WorkResult.NOT_KNOWN.getCode());

        sendOverList = cbusPayoutService.selectRecordsByWorkResult(WorkResult.SENT.getCode());
        printVchList = cbusPayoutService.qryPrintVchrPayouts();

        FacesContext context = FacesContext.getCurrentInstance();
        String pkid = (String) context.getExternalContext().getRequestParameterMap().get("pkid");
        String action = (String) context.getExternalContext().getRequestParameterMap().get("action");

        if (!StringUtils.isEmpty(pkid) && "act".equalsIgnoreCase(action)) {
            qdRsPayout = payoutService.selectPayoutByPkid(pkid);
            planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(qdRsPayout.getBusinessNo());
            UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
            CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:directPrint");
            saveBtn.setDisabled(true);
        } else if (!StringUtils.isEmpty(pkid) && "print".equalsIgnoreCase(action)) {
            qdRsPayout = payoutService.selectPayoutByPkid(pkid);
            planCtrl = expensesPlanService.selectPlanCtrlByPlanNo(qdRsPayout.getBusinessNo());
            UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
            CommandButton saveBtn = (CommandButton) viewRoot.findComponent("form:directPrint");
            saveBtn.setDisabled(false);
        }
    }

    public String onSingleExecute() {

        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("请选择一笔记录！");
        } else if (selectedRecords.length > 1) {
            MessageUtil.addWarn("只能选择一笔记录进行入账！");

        } else {
            try {
                if (isRunning) {
                    MessageUtil.addWarn("系统已进行入账，请稍候，勿重复操作入账!");
                    return null;
                }
                isRunning = true;
                for (QdRsPayout record : selectedRecords) {
                    QdRsPayout originPayout = payoutService.selectPayoutByPkid(record.getPkId());
                    if (!WorkResult.PASS.getCode().equals(originPayout.getWorkResult())) {
                        MessageUtil.addWarn("并发入账冲突，请勿重复操作入账!");
                        return null;
                    }
                    int cnt = 1;
                    cnt = cbusPayoutService.updateRsPayoutToExec(record);
                    if (cnt != 1) {
                        throw new RuntimeException("【记录更新失败】付款监管账号：" + record.getPayAccount());
                    }
                    MessageUtil.addInfo("入账完成!");
                    int sentResult = 1;
                    try {
                        sentResult = clientBiService.sendRsPayoutMsg(record);
                    } catch (Exception e) {
                        logger.error("发送失败." + e.getMessage());
                        MessageUtil.addError("发送失败." + e.getMessage());
                        return null;
                    }
                    if (sentResult != 1) {
                        MessageUtil.addError("发送失败!");
                    } else {
                        MessageUtil.addInfo("发送完成！");
                    }
                }
            } catch (Exception e) {
                logger.error("操作失败." + e.getMessage());
                MessageUtil.addError("操作失败." + e.getMessage());
                return null;
            }

            init();
        }
        return null;
    }

    public String onExecute() {
        try {
            if (isRunning) {
                MessageUtil.addWarn("系统已进行入账，请稍候，勿重复操作入账!");
                return null;
            }
            QdRsPayout originPayout = payoutService.selectPayoutByPkid(qdRsPayout.getPkId());
            if (!WorkResult.PASS.getCode().equals(originPayout.getWorkResult())) {
                MessageUtil.addWarn("并发入账冲突，请勿重复操作入账!");
                return null;
            }
            isRunning = true;
            // TODO
            int cnt = cbusPayoutService.updateRsPayoutToExec(qdRsPayout);
//            int cnt = 1;
            UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
            CommandButton execBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
            execBtn.setDisabled(true);

            if (cnt == 1) {
                MessageUtil.addInfo("入账完成!");
                int sentResult = 1;
                // TODO
                sentResult = clientBiService.sendRsPayoutMsg(qdRsPayout);
                if (sentResult != 1) {
                    throw new RuntimeException("发送失败");
                }
                MessageUtil.addInfo("发送完成！");
                CommandButton prtnBtn = (CommandButton) viewRoot.findComponent("form:directPrint");
                prtnBtn.setDisabled(false);

            }
        } catch (Exception e) {
            logger.error("入账异常.", e);
            MessageUtil.addError("入账异常." + (e.getMessage() == null ? "" : e.getMessage()));
            UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
            CommandButton execBtn = (CommandButton) viewRoot.findComponent("form:saveBtn");
            execBtn.setDisabled(true);
            return null;
        }
        init();
        return null;
    }

    public void onPrintVoucher(ActionEvent event) {
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
            Document document = new Document(PageSize.A4, 16, 16, 36, 90);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, bos);
            writer.setPageEvent(new PdfPageEventHelper());
//            writer.setPageEvent(new PdfVoucherHelper());
            document.open();

            document.add(transToPdfTable());
            document.close();
            response.reset();
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "inline");
            response.setContentLength(bos.size());
            response.setHeader("Cache-Control", "max-age=30");
            bos.writeTo(out);
            out.flush();
            out.close();
            ctx.responseComplete();

        } catch (Exception e) {
            logger.error("凭证打印异常.", e);
            MessageUtil.addError("凭证打印异常." + (e.getMessage() == null ? "" : e.getMessage()));
        }

    }

    //
    private PdfPTable transToPdfTable() throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(new float[]{900f});// 建立一个pdf表格
        OperatorManager om = ToolUtil.getOperatorManager();

        table.setSpacingBefore(130f);// 设置表格上面空白宽度
        table.setTotalWidth(835);// 设置表格的宽度
        table.setLockedWidth(false);// 设置表格的宽度固定
        table.setSpacingAfter(120f);
        table.getDefaultCell().setBorder(0);//设置表格默认为无边框
        BaseFont bfChinese = BaseFont.createFont("c:\\windows\\fonts\\simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font headFont1 = new Font(bfChinese, 14, Font.BOLD);// 设置字体大小
        Font headFont2 = new Font(bfChinese, 10, Font.NORMAL);// 设置字体大小
        // 电汇凭证
        if ("20".equals(qdRsPayout.getTransType())) {
            PdfPCell cell = new PdfPCell(new Paragraph("汇划业务凭证", headFont1));
            cell.setBorder(0);
            cell.setFixedHeight(40);//单元格高度
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);

            String bankSerial = "";
            if (StringUtils.isEmpty(qdRsPayout.getBankSerial())) {
                bankSerial = " ";
            } else if (qdRsPayout.getBankSerial().length() >= 12) {
                bankSerial = qdRsPayout.getBankSerial().substring(12);
            } else {
                bankSerial = qdRsPayout.getBankSerial();
            }

            int[] rowWidths = new int[]{25, 34, 25};
            String[][] rows = new String[][]{
                    new String[]{"机构代号：" + om.getOperator().getDeptid(),
                            "记账时间：" + new SimpleDateFormat("yyyy/MM/dd hh:MM:ss").format(new Date()),
                            "流水号：" + bankSerial},
                    new String[]{"录入员代号：" + (StringUtils.isEmpty(qdRsPayout.getApplyUserId()) ? " " : qdRsPayout.getApplyUserId().substring(9)),
                            "复核员代码：" + (StringUtils.isEmpty(qdRsPayout.getAuditUserId()) ? " " : qdRsPayout.getAuditUserId().substring(9)),
                            "主管代号：" + (StringUtils.isEmpty(qdRsPayout.getApplyUserId()) ? " " : qdRsPayout.getApplyUserId().substring(9))},
                    new String[]{" ", " ", " "},
                    new String[]{"币别代码：人民币",
                            "凭证号：" + qdRsPayout.getDocNo(),
                            "汇划日期：" + qdRsPayout.getTradeDate()},
                    new String[]{"支付渠道：大额支付",
                            "支付交易序号：" + qdRsPayout.getWfInstanceId(),
                            "业务种类：普通汇兑"},
                    new String[]{"接收行行号：" + qdRsPayout.getRecBankCode(),
                            "接收行名称：" + qdRsPayout.getRecBankName(),
                            " "},
            };
            //内容
            for (String[] row : rows) {
                cell = new PdfPCell(new Paragraph(getStrLine(rowWidths, row), headFont2));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            cell = new PdfPCell(new Paragraph(StringUtil.rightPad4ChineseToByteLength("付款人账号：" + qdRsPayout.getPayAccount()
                    , 40, " ") + "付款人名称："
                    + qdRsPayout.getPayCompanyName(), headFont2));
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(StringUtil.rightPad4ChineseToByteLength("收款人账号：" + qdRsPayout.getRecAccount()
                    , 40, " ") + "收款人名称："
                    + qdRsPayout.getRecCompanyName(), headFont2));
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("汇出金额：" + String.format("%.2f", qdRsPayout.getApAmount()), headFont2));
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        } else {
            PdfPCell cell = new PdfPCell(new Paragraph("单位活期取款凭证", headFont1));
            cell.setBorder(0);
            cell.setFixedHeight(40);//单元格高度
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);// 设置内容水平居中显示
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            int[] rowWidths = new int[]{25, 34, 25};
            String bankSerial = "";
            if (StringUtils.isEmpty(qdRsPayout.getBankSerial())) {
                bankSerial = " ";
            } else if (qdRsPayout.getBankSerial().length() >= 12) {
                bankSerial = qdRsPayout.getBankSerial().substring(12);
            } else {
                bankSerial = qdRsPayout.getBankSerial();
            }
            String[][] rows = new String[][]{
                    new String[]{"机构代号：" + om.getOperator().getDeptid(),
                            "记账时间：" + new SimpleDateFormat("yyyy/MM/dd hh:MM:ss").format(new Date()),
                            "交易流水号：" + bankSerial},
                    new String[]{"柜员代号：" + qdRsPayout.getApplyUserId().substring(9),
                            "主管代号：" + qdRsPayout.getAuditUserId().substring(9),
                            "币别：人民币"},
            };
            //内容
            for (String[] row : rows) {
                cell = new PdfPCell(new Paragraph(getStrLine(rowWidths, row), headFont2));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }

            String[] singleRows = new String[]{"付款人账号：" + qdRsPayout.getPayAccount(), "付款人户名：" + qdRsPayout.getPayCompanyName(),
                    "收款人账号：" + qdRsPayout.getRecAccount(), "收款人户名：" + qdRsPayout.getRecCompanyName(),
                    "交易金额：" + StringUtils.rightPad(String.format("%.2f", qdRsPayout.getApAmount()), 40, " ") + "交易类别：转账",
                    "存款种类：" + StringUtil.rightPad4ChineseToByteLength(" ", 40, " ") + "存单存折印刷号：",
                    "凭证号码："
            };
            for (String snglRow : singleRows) {
                cell = new PdfPCell(new Paragraph(snglRow, headFont2));
                cell.setBorder(0);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
            }
        }
        return table;

    }

    private String getStrLine(int[] rowWidths, String[] strs) {
        String str = "";
        for (int i = 0; i < rowWidths.length; i++) {
            if (" ".equals(strs[i])) {
                str += " ";
            } else if (strs[i].getBytes().length <= rowWidths[i]) {
                str += StringUtil.rightPad4ChineseToByteLength(strs[i], rowWidths[i], " ");
            } else {
                str += strs[i];
            }
        }

        return str;
    }

    public String onAllSend() {
        if (payOverList.isEmpty()) {
            MessageUtil.addWarn("可发送记录为空！");
        } else {
            int sentResult = 1;
            try {
                for (QdRsPayout record : payOverList) {
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
                for (QdRsPayout record : toSendRecords) {
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

    public String onReCheck() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔记录！");
        } else {
            try {
                for (QdRsPayout record : selectedRecords) {
                    record.setWorkResult(WorkResult.RE_CHECK.getCode());
                    payoutService.updateRsPayout(record);
                }
                MessageUtil.addInfo("状态修改完成！");
            } catch (Exception e) {
                logger.error("操作失败." + e.getMessage());
                MessageUtil.addError("操作失败." + e.getMessage());
                return null;
            }
            init();
        }
        return null;
    }

    public String onAlreadyExec() {
        if (selectedRecords == null || selectedRecords.length == 0) {
            MessageUtil.addWarn("请至少选择一笔记录！");
        } else {
            try {
                for (QdRsPayout record : selectedRecords) {
                    payoutService.updateRsPayoutToExec(record);
                    clientBiService.sendRsPayoutMsg(record);
                }
                MessageUtil.addInfo("入账完成！");
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

    public List<QdRsPayout> getPrintVchList() {
        return printVchList;
    }

    public void setPrintVchList(List<QdRsPayout> printVchList) {
        this.printVchList = printVchList;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public List<QdRsPayout> getPayFailList() {
        return payFailList;
    }

    public void setPayFailList(List<QdRsPayout> payFailList) {
        this.payFailList = payFailList;
    }

    public ExpensesPlanService getExpensesPlanService() {
        return expensesPlanService;
    }

    public void setExpensesPlanService(ExpensesPlanService expensesPlanService) {
        this.expensesPlanService = expensesPlanService;
    }

    public PayoutService getPayoutService() {
        return payoutService;
    }

    public void setPayoutService(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    public QdRsPlanCtrl getPlanCtrl() {
        return planCtrl;
    }

    public void setPlanCtrl(QdRsPlanCtrl planCtrl) {
        this.planCtrl = planCtrl;
    }

    public QdRsPayout getQdRsPayout() {
        return qdRsPayout;
    }

    public void setQdRsPayout(QdRsPayout qdRsPayout) {
        this.qdRsPayout = qdRsPayout;
    }

    public CbusPayoutService getCbusPayoutService() {
        return cbusPayoutService;
    }

    public void setCbusPayoutService(CbusPayoutService cbusPayoutService) {
        this.cbusPayoutService = cbusPayoutService;
    }

    public QdRsPayout getSelectedRecord() {
        return selectedRecord;
    }

    public void setSelectedRecord(QdRsPayout selectedRecord) {
        this.selectedRecord = selectedRecord;
    }

    public WorkResult getWorkResult() {
        return workResult;
    }

    public void setWorkResult(WorkResult workResult) {
        this.workResult = workResult;
    }

    public QdRsPayout[] getSelectedRecords() {
        return selectedRecords;
    }

    public void setSelectedRecords(QdRsPayout[] selectedRecords) {
        this.selectedRecords = selectedRecords;
    }

    public List<QdRsPayout> getPassPayoutList() {
        return passPayoutList;
    }

    public void setPassPayoutList(List<QdRsPayout> passPayoutList) {
        this.passPayoutList = passPayoutList;
    }

    public List<QdRsPayout> getPayOverList() {
        return payOverList;
    }

    public void setPayOverList(List<QdRsPayout> payOverList) {
        this.payOverList = payOverList;
    }

    public QdRsPayout[] getToSendRecords() {
        return toSendRecords;
    }

    public void setToSendRecords(QdRsPayout[] toSendRecords) {
        this.toSendRecords = toSendRecords;
    }

    public ClientBiService getClientBiService() {
        return clientBiService;
    }

    public void setClientBiService(ClientBiService clientBiService) {
        this.clientBiService = clientBiService;
    }

    public List<QdRsPayout> getSendOverList() {
        return sendOverList;
    }

    public void setSendOverList(List<QdRsPayout> sendOverList) {
        this.sendOverList = sendOverList;
    }

    public RefundStatus getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(RefundStatus statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getJscript() {
        return jscript;
    }

    public void setJscript(String jscript) {
        this.jscript = jscript;
    }
}