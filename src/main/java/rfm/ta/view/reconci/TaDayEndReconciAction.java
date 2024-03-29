package rfm.ta.view.reconci;

import common.utils.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.txn.Toa900012601;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.RfmMessage;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.acc.TaAccDetlService;
import rfm.ta.service.biz.reconci.TaRsCheckService;
import rfm.ta.service.dep.TaSbsService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Lichao.W At 2015/7/6 17:01
 * wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class TaDayEndReconciAction implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(TaDayEndReconciAction.class);

    @ManagedProperty("#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @ManagedProperty("#{taSbsService}")
    private TaSbsService taSbsService;

    @ManagedProperty("#{taRsCheckService}")
    private TaRsCheckService taRsCheckService;

    private TaTxnSbs taTxnSbs;
    // 账务交易明细
    private List<TaRsAccDtl> taRsAccDtlLocalList;
    private List<TaRsAccDtl> taRsAccDtlSbsList;

    private String strLocalTotalCounts;
    private String strLocalTotalAmt;
    private String strSbsTotalCounts;
    private String strSbsTotalAmt;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private Map<String, String> txCodeMap;

    @PostConstruct
    public void init(){
        txCodeMap = getTxCodeMapByEnum();
        strLocalTotalCounts="0";
        strLocalTotalAmt="0";
        strSbsTotalCounts="0";
        strSbsTotalAmt="0";
        taRsAccDtlSbsList=new ArrayList<TaRsAccDtl>();
        TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
        taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
        taRsAccDtlLocalList = taAccDetlService.selectedRecords(taRsAccDtlPara);
        strLocalTotalCounts=String.valueOf(taRsAccDtlLocalList.size());
        if(taRsAccDtlLocalList.size()>0) {
            Double total = 0d;
            for(TaRsAccDtl taRsAccDtl:taRsAccDtlLocalList){
                total += Double.valueOf(taRsAccDtl.getTxAmt());
                taRsAccDtl.setTxAmt(df.format(Double.valueOf(taRsAccDtl.getTxAmt())));
            }
            strLocalTotalAmt = df.format(total);
        }
    }

    /**
     * 从SBS取数据
     */
    public boolean onQrySbsData() {
        try {
            // 更新对账明细表状态（日间对账获取中）
            taRsCheckService.insOrUpdTaRsCheck(EnuChkRstlStatusFlag.STATUS_FLAG0.getCode());

            // 往SBS发送记账信息
            TaTxnFdc taTxnFdcPara = new TaTxnFdc();
            taTxnFdcPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
            taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());
            // 日终对账总数查询
            Toa900012601 toa900012601Temp = (Toa900012601) taSbsService.sendAndRecvRealTimeTxn900012601(taTxnFdcPara);
            strSbsTotalCounts = toa900012601Temp.body.DRCNT;

            strSbsTotalAmt = toa900012601Temp.body.DRAMT;

            // 日终对账明细查询
            List errMsg = new ArrayList();
            taRsAccDtlSbsList = taSbsService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara, errMsg);

            if (taRsAccDtlSbsList != null) {
                // 更新对账明细表状态（日间对账获取完成）
                taRsCheckService.insOrUpdTaRsCheck(EnuChkRstlStatusFlag.STATUS_FLAG1.getCode());

                for(TaRsAccDtl taRsAccDtl : taRsAccDtlSbsList) {
                    taRsAccDtl.setTxAmt(df.format(Double.valueOf(taRsAccDtl.getTxAmt())));
                }
                //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I001"));
                return true;
            }
        }catch (Exception e){
            logger.error("查询对账信息失败", e);
            MessageUtil.addError("查询对账信息失败。");
        }
        return false;
    }

    private File createFile(List<TaRsAccDtl> taRsAccDtlListPara, String fileName) {
        String sysdate = ToolUtil.getStrLastUpdDate();
        File file;
        String filePath = "/upload/reconci";
        StringBuffer line = new StringBuffer();
        FileWriter fw = null;
        BufferedWriter bw = null;
        String newLineCh = "\r\n";
        try {
            file = ToolUtil.createFile(filePath, fileName);
            if(file != null){
                if(taRsAccDtlListPara == null || taRsAccDtlListPara.size() == 0) {
                    line.append("0     |0                |");
                } else {
                    // 汇总信息
                    // 交易总笔数(6位)|
                    Integer intTotalCounts=taRsAccDtlListPara.size();
                    line.append(StringUtils.rightPad(intTotalCounts.toString(), 6, ' '));
                    line.append("|");
                    // 交易总金额(20位)|
                    if(taRsAccDtlListPara.size()>0) {
                        Long total = 0L;
                        for(TaRsAccDtl taRsAccDtlUnit:taRsAccDtlListPara){
                            total += ToolUtil.getYuanToFin(taRsAccDtlUnit.getTxAmt());
                        }
                        line.append(StringUtils.rightPad(total.toString(), 20, ' '));
                    }else {
                        line.append(StringUtils.rightPad("0", 20, ' '));
                    }

                    line.append("|");

                    line.append(newLineCh);
                    String txCode = null;
                    // 明细信息
                    for(TaRsAccDtl taRsAccDtlUnit:taRsAccDtlListPara){
                        // 交易代码(4位)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getTxCode()), 4, ' '));
                        line.append("|");
                        // 业务编号(14位，包括交存申请编号、划拨业务编号、返还业务编号)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getBizId()), 14, ' '));
                        line.append("|");
                        txCode = taRsAccDtlUnit.getTxCode().substring(0,2);
                        if(txCode.equals("20")) {
                            // 借贷标志(1位，1_借/2_贷：交存/利息=1、划拨/返还=2)|
                            line.append(StringUtils.rightPad("1", 1, ' '));
                        } else{
                            // 借贷标志(1位，1_借/2_贷：交存/利息=1、划拨/返还=2)|
                            line.append(StringUtils.rightPad("2", 1, ' '));
                        }
                        line.append("|");
                        // 交易金额(20位)|
                        line.append(StringUtils.rightPad(ToolUtil.getYuanToFin(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getTxAmt())).toString(), 20, ' '));
                        line.append("|");
                        // 监管账号(30位)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getSpvsnAccId()), 30, ' '));
                        line.append("|");
                        // 预售资金监管平台流水(16位)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getFdcSn()), 16, ' '));
                        line.append("|");
                        // 监管银行记账流水(30位)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getFdcBankActSn()), 30, ' '));
                        line.append("|");
                        // 监管银行记账网点(30位)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getSpvsnBankId()), 30, ' '));
                        line.append("|");
                        // 监管银行记账人员(30位)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getUserId()), 30, ' '));
                        line.append("|");
                        // 记账日期(10位，YYYY-MM-DD)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getTxDate()), 10, ' '));
                        line.append("|");
                        line.append(newLineCh);
                    }
                }

                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(line.toString());
                bw.flush();
            }
            return file;
        } catch (Exception e) {
            throw new RuntimeException(filePath + fileName + ".dat", e);
        } finally {
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e) {
                }
            }
            if(bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 数据发送
     */
    public void onBlnc() {
        File file = null;
        try {
            TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
            taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
            taRsAccDtlPara.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
            List<TaRsAccDtl> taRsAccDtlReconci = taAccDetlService.selectedRecords(taRsAccDtlPara);
            String fileName = "PF"+ EnuTaBankId.BANK_HAIER.getCode()+
                    EnuTaCityId.CITY_TAIAN.getCode()+
                    ToolUtil.getNow("yyyyMMdd")+".dat";
            file = createFile(taRsAccDtlReconci, fileName);
            if(file != null){
                boolean result = ToolUtil.uploadFile(fileName, file);
                if(result){
                    // 更新对账明细表状态（日间对账发送成功）
                    taRsCheckService.insOrUpdTaRsCheck(EnuChkRstlStatusFlag.STATUS_FLAG4.getCode());
                    MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I002"));
                } else{
                    MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E001"));
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("日间对账发送失败！");
        } finally {
            if(file != null && file.exists()) {
                try {
                    String filePath = "/backup/reconci";
                    File destDir = ToolUtil.createFile(filePath, file.getName());
                    FileUtils.copyFile(file, destDir);
                    file.delete();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 内对_本地记账与SBS对账（比较两个List）
     */
    public void reconci(){
        try {
            List<TaRsAccDtl> taRsAccDtlLocalListPara = taRsAccDtlLocalList;
            List<TaRsAccDtl> taRsAccDtlSbsListPara = taRsAccDtlSbsList;
            // List1重复项
            List<TaRsAccDtl> taRsAccDtlList1Repeat = new ArrayList<TaRsAccDtl>();
            // List2重复项
            List<TaRsAccDtl> taRsAccDtlList2Repeat = new ArrayList<TaRsAccDtl>();
            boolean isExist = false;
            // 遍历List1
            for(TaRsAccDtl taRsAccDtl1:taRsAccDtlLocalListPara){
                isExist = false;
                if(taRsAccDtl1.getTxCode().equals(EnuTaFdcTxCode.TRADE_2002.getCode()) ||
                    taRsAccDtl1.getTxCode().equals(EnuTaFdcTxCode.TRADE_2111.getCode()) ||
                        taRsAccDtl1.getTxCode().equals(EnuTaFdcTxCode.TRADE_2211.getCode())){ // 一般账户→监管账户
                    // 遍历List2
                    for(TaRsAccDtl taRsAccDtl2:taRsAccDtlSbsListPara){
                        if(taRsAccDtl1.getReqSn().substring(8,26).equals(taRsAccDtl2.getReqSn())
                                &&taRsAccDtl1.getGerlAccId().equals(taRsAccDtl2.getSpvsnAccId())
                                &&taRsAccDtl1.getSpvsnAccId().equals(taRsAccDtl2.getGerlAccId())
                                &&taRsAccDtl1.getTxAmt().equals(taRsAccDtl2.getTxAmt())){
                            isExist = true;
                            taRsAccDtlList2Repeat.add(taRsAccDtl2);
                        }
                    }
                } else { // 监管账户→一般账户
                    // 遍历List2
                    for(TaRsAccDtl taRsAccDtl2:taRsAccDtlSbsListPara){
                        if(taRsAccDtl1.getReqSn().substring(8,26).equals(taRsAccDtl2.getReqSn())
                                &&taRsAccDtl1.getSpvsnAccId().equals(taRsAccDtl2.getSpvsnAccId())
                                &&taRsAccDtl1.getGerlAccId().equals(taRsAccDtl2.getGerlAccId())
                                &&taRsAccDtl1.getTxAmt().equals(taRsAccDtl2.getTxAmt())){
                            isExist = true;
                            taRsAccDtlList2Repeat.add(taRsAccDtl2);
                        }
                    }
                }

                if(isExist){
                    taRsAccDtlList1Repeat.add(taRsAccDtl1);
                }
            }

            taRsAccDtlLocalListPara.removeAll(taRsAccDtlList1Repeat);
            taRsAccDtlSbsListPara.removeAll(taRsAccDtlList2Repeat);

            if(taRsAccDtlLocalListPara.size() == 0 && taRsAccDtlSbsListPara.size() == 0) {
                // 更新对账明细表状态（日间对账平）
                taRsCheckService.insOrUpdTaRsCheck(EnuChkRstlStatusFlag.STATUS_FLAG3.getCode());
                MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I003"));
            } else {
                // 更新对账明细表状态（日间对账不平）
                taRsCheckService.insOrUpdTaRsCheck(EnuChkRstlStatusFlag.STATUS_FLAG2.getCode());
                MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E003"));
            }
        } catch (Exception e) {
            logger.error(RfmMessage.getProperty("DayEndReconciliation.E004"), e);
            MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E004"));
        }
    }

    /**
     * 泰安房产中心交易号
     *
     * @return
     */
    private Map<String, String> getTxCodeMapByEnum() {
        Map<String, String> map = new HashMap<String, String>();
        for(EnuTaFdcTxCode txCode:EnuTaFdcTxCode.values()) {
            map.put(txCode.getCode(), txCode.getTitle());
        }
        return map;
    }

    //= = = = = = = = = = = = get set = = = = = = = = = = = =
    public TaRsCheckService getTaRsCheckService() {
        return taRsCheckService;
    }

    public void setTaRsCheckService(TaRsCheckService taRsCheckService) {
        this.taRsCheckService = taRsCheckService;
    }

    public Map<String, String> getTxCodeMap() {
        return txCodeMap;
    }

    public void setTxCodeMap(Map<String, String> txCodeMap) {
        this.txCodeMap = txCodeMap;
    }

    public TaTxnSbs getTaTxnSbs() {
        return taTxnSbs;
    }

    public void setTaTxnSbs(TaTxnSbs taTxnSbs) {
        this.taTxnSbs = taTxnSbs;
    }

    public List<TaRsAccDtl> getTaRsAccDtlSbsList() {
        return taRsAccDtlSbsList;
    }

    public void setTaRsAccDtlSbsList(List<TaRsAccDtl> taRsAccDtlSbsList) {
        this.taRsAccDtlSbsList = taRsAccDtlSbsList;
    }

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public List<TaRsAccDtl> getTaRsAccDtlLocalList() {
        return taRsAccDtlLocalList;
    }

    public void setTaRsAccDtlLocalList(List<TaRsAccDtl> taRsAccDtlLocalList) {
        this.taRsAccDtlLocalList = taRsAccDtlLocalList;
    }

    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
    }

    public String getStrLocalTotalCounts() {
        return strLocalTotalCounts;
    }

    public void setStrLocalTotalCounts(String strLocalTotalCounts) {
        this.strLocalTotalCounts = strLocalTotalCounts;
    }

    public String getStrSbsTotalCounts() {
        return strSbsTotalCounts;
    }

    public void setStrSbsTotalCounts(String strSbsTotalCounts) {
        this.strSbsTotalCounts = strSbsTotalCounts;
    }

    public String getStrLocalTotalAmt() {
        return strLocalTotalAmt;
    }

    public void setStrLocalTotalAmt(String strLocalTotalAmt) {
        this.strLocalTotalAmt = strLocalTotalAmt;
    }

    public String getStrSbsTotalAmt() {
        return strSbsTotalAmt;
    }

    public void setStrSbsTotalAmt(String strSbsTotalAmt) {
        this.strSbsTotalAmt = strSbsTotalAmt;
    }
}
