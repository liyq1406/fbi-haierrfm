package rfm.ta.view.reconci;

import common.utils.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.txn.Toa900012701;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rfm.ta.common.enums.*;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.service.biz.acc.TaAccDetlService;
import rfm.ta.service.biz.acc.TaAccService;
import rfm.ta.service.biz.reconci.TaRsCheckService;
import rfm.ta.service.dep.TaSbsService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一键对账用Action
 */
@ManagedBean
@ViewScoped
public class AKeyToReconciAction implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(AKeyToReconciAction.class);

    @ManagedProperty("#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @ManagedProperty("#{taRsCheckService}")
    private TaRsCheckService taRsCheckService;

    @ManagedProperty("#{taSbsService}")
    private TaSbsService taSbsService;

    @ManagedProperty("#{taAccService}")
    private TaAccService taAccService;

    private List<TaRsAccDtl> taRsAccDtlLocalList;
    private List<TaRsAccDtl> taRsAccDtlSbsList;
    private List<TaRsAcc> taRsAccList;

    // 账户余额
    private Map<String, String> txAmtMap;

    private DecimalFormat df = new DecimalFormat("#0.00");

    /**
     * 一键对账
     * @return
     */
    public boolean aKeyToReconci() {
        try {
            txAmtMap = new HashMap<String, String>();

            // 日终对账获取本地对账数据
            TaRsAccDtl taRsAccDtlPara = new TaRsAccDtl();
            taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
            taRsAccDtlLocalList = taAccDetlService.selectedRecords(taRsAccDtlPara);

            // 日终对账从sbs获取数据
            if(!getSbsDataDayEnd()) {
                return false;
            }

            // 日终对账内部对账
            if(!reconciDayEnd()) {
                return false;
            }

            // 日终对账ftp发送
            if(!sendDayEnd()) {
                return false;
            }

            // 余额对账获取sbs数据
            if(!getSbsDataBlncReconci()) {
                return false;
            }

            // 余额对账ftp发送
            if(!sendBlncReconci()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("一键对账失败", e);
        }
        return false;
    }

    /**
     * 从SBS取数据
     */
    private boolean getSbsDataDayEnd() {
        try {
            // 更新对账明细表状态（日间对账获取中）
            taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG0.getCode());

            // 往SBS发送记账信息
            TaTxnFdc taTxnFdcPara = new TaTxnFdc();
            taTxnFdcPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
            taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());

            // 日终对账明细查询
            taRsAccDtlSbsList = taSbsService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara);

            if (taRsAccDtlSbsList != null) {
                // 更新对账明细表状态（日间对账获取完成）
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG1.getCode());

                for(TaRsAccDtl taRsAccDtl : taRsAccDtlSbsList) {
                    taRsAccDtl.setTxAmt(df.format(Double.valueOf(taRsAccDtl.getTxAmt())));
                }
                //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I001"));
                return true;
            }
        }catch (Exception e){
            logger.error("一键对账日终对账查询sbs数据失败", e);
            //MessageUtil.addError("查询对账信息失败。");
        }
        return false;
    }

    /**
     * 内对_本地记账与SBS对账（比较两个List）
     */
    private boolean reconciDayEnd(){
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
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG3.getCode());
                //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I003"));
                return true;
            } else {
                // 更新对账明细表状态（日间对账不平）
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG2.getCode());
                //MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E003"));
            }
        } catch (Exception e) {
            logger.error("一键对账日终对账内部对账失败", e);
            //MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E004"));
        }

        return false;
    }

    /**
     * 数据发送
     */
    private boolean sendDayEnd() {
        File file = null;
        try {
            TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
            taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
            taRsAccDtlPara.setCanclFlag(EnuActCanclFlag.ACT_CANCL0.getCode());
            List<TaRsAccDtl> taRsAccDtlReconci = taAccDetlService.selectedRecords(taRsAccDtlPara);
            String fileName = "PF"+ EnuTaBankId.BANK_HAIER.getCode()+
                    EnuTaCityId.CITY_TAIAN.getCode()+
                    ToolUtil.getNow("yyyyMMdd")+".dat";
            file = createDayEndFile(taRsAccDtlReconci, fileName);
            if(file != null){
                boolean result = ToolUtil.uploadFile(fileName, file);
                if(result){
                    // 更新对账明细表状态（日间对账发送成功）
                    taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG4.getCode());
                    //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I002"));
                    return true;
                } else{
                    //MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E001"));
                }
            }
        } catch (Exception e) {
            logger.error("一键对账日终对账发送ftp失败", e);
            //MessageUtil.addError("日间对账发送失败！");
        } finally {
            if(file != null && file.exists()) {
                try {
                    String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/backup/reconci");
                    File destDir = ToolUtil.createFile(filePath, file.getName());
                    FileUtils.copyFile(file, destDir);
                    file.delete();
                } catch (Exception e) {
                }
            }
        }

        return false;
    }

    private File createDayEndFile(List<TaRsAccDtl> taRsAccDtlListPara, String fileName) {
        String sysdate = ToolUtil.getStrLastUpdDate();
        File file;
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/reconci");
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
     * 余额对账获取sbs数据
     */
    private boolean getSbsDataBlncReconci() {
        try {
            // 更新对账明细表状态（余额对账获取中）
            taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG5.getCode());

            // 取得所有监管中的监管账号数据
            taRsAccList = taAccService.qryAllMonitRecords();

            if(taRsAccList.size() >0) {
                // 发送监管账号到SBS查询余额
                List<Toa900012701> toaSbs=taSbsService.sendAndRecvRealTimeTxn900012701(taRsAccList);

                if(toaSbs !=null && toaSbs.size() > 0) {
                    // 遍历是否存在查询失败
                    for(Toa900012701 toa900012701:toaSbs) {
                        if (!EnuTaTxnRtnCode.TXN_PROCESSED.getCode().equals(toa900012701.header.RETURN_CODE)) {
                            //MessageUtil.addError(toa900012701.header.RETURN_MSG);
                            return false;
                        }
                    }

                    for(Toa900012701 toa900012701:toaSbs){
                        for(Toa900012701.BodyDetail bodyDetail:toa900012701.body.DETAILS){
                            txAmtMap.put(bodyDetail.ACTNUM, bodyDetail.BOKBAL);
                        }
                    }

                    // 更新对账明细表状态（余额对账获取完成）
                    taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG6.getCode());

                    //MessageUtil.addInfo(RfmMessage.getProperty("BalanceReconciliation.I001"));
                    return true;
                }
            } else {
                // 更新对账明细表状态（余额对账获取完成）
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG6.getCode());
                return true;
            }
        }catch (Exception e){
            logger.error("一键对账余额对账获取sbs数据失败", e);
            //MessageUtil.addError(e.getMessage());
        }

        return false;
    }

    /**
     * 余额对账ftp发送
     */
    private boolean sendBlncReconci(){
        File file = null;
        try {
            if(taRsAccList.size() > 0) {
                String fileName = "BF" + EnuTaBankId.BANK_HAIER.getCode() +
                        EnuTaCityId.CITY_TAIAN.getCode() +
                        ToolUtil.getStrToday() +".dat";
                file = createBlncReconciFile(fileName);
                if(file != null){
                    boolean result = ToolUtil.uploadFile(fileName, file);
                    if(result){
                        // 更新对账明细表状态（余额对账发送成功）
                        taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG7.getCode());
                        //MessageUtil.addInfo(RfmMessage.getProperty("BalanceReconciliation.I002"));
                        return true;
                    } else{
                        //MessageUtil.addError(RfmMessage.getProperty("BalanceReconciliation.E003"));
                    }
                }
            } else {
                // 更新对账明细表状态（余额对账发送成功）
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG7.getCode());
                return true;
            }
        } catch (Exception e) {
            logger.error("一键对账余额对账发送ftp失败，", e);
            //MessageUtil.addError(e.getMessage());
        } finally {
            if(file != null && file.exists()){
                try {
                    String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/backup/reconci");
                    File destDir = ToolUtil.createFile(filePath, file.getName());
                    FileUtils.copyFile(file, destDir);
                    file.delete();
                } catch (Exception e) {
                }
            }
        }

        return false;
    }

    /**
     * 作成dat文件
     * @param fileName
     * @return
     */
    private File createBlncReconciFile(String fileName) {
        File file = null;
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/reconci");
        String newLineCh = "\r\n";
        StringBuffer line = new StringBuffer();
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            file = ToolUtil.createFile(filePath, fileName);
            if(file != null){
                for(TaRsAcc taRsAcc : taRsAccList){
                    line.append(StringUtils.rightPad(taRsAcc.getSpvsnAccId(), 30, ' '));
                    line.append("|");
                    line.append(StringUtils.rightPad(txAmtMap.get(taRsAcc.getSpvsnAccId()), 20, ' '));
                    line.append("|");
                    line.append(taRsAcc.getTxDate());
                    line.append("|");
                    line.append(newLineCh);
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

    //= = = = = = = = = = = = get set = = = = = = = = = = = =
    public TaAccService getTaAccService() {
        return taAccService;
    }

    public void setTaAccService(TaAccService taAccService) {
        this.taAccService = taAccService;
    }

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public TaRsCheckService getTaRsCheckService() {
        return taRsCheckService;
    }

    public void setTaRsCheckService(TaRsCheckService taRsCheckService) {
        this.taRsCheckService = taRsCheckService;
    }

    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
    }
}
