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
 * һ��������Action
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

    // �˻����
    private Map<String, String> txAmtMap;

    private DecimalFormat df = new DecimalFormat("#0.00");

    /**
     * һ������
     * @return
     */
    public boolean aKeyToReconci() {
        try {
            txAmtMap = new HashMap<String, String>();

            // ���ն��˻�ȡ���ض�������
            TaRsAccDtl taRsAccDtlPara = new TaRsAccDtl();
            taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
            taRsAccDtlLocalList = taAccDetlService.selectedRecords(taRsAccDtlPara);

            // ���ն��˴�sbs��ȡ����
            if(!getSbsDataDayEnd()) {
                return false;
            }

            // ���ն����ڲ�����
            if(!reconciDayEnd()) {
                return false;
            }

            // ���ն���ftp����
            if(!sendDayEnd()) {
                return false;
            }

            // �����˻�ȡsbs����
            if(!getSbsDataBlncReconci()) {
                return false;
            }

            // ������ftp����
            if(!sendBlncReconci()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("һ������ʧ��", e);
        }
        return false;
    }

    /**
     * ��SBSȡ����
     */
    private boolean getSbsDataDayEnd() {
        try {
            // ���¶�����ϸ��״̬���ռ���˻�ȡ�У�
            taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG0.getCode());

            // ��SBS���ͼ�����Ϣ
            TaTxnFdc taTxnFdcPara = new TaTxnFdc();
            taTxnFdcPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
            taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());

            // ���ն�����ϸ��ѯ
            taRsAccDtlSbsList = taSbsService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara);

            if (taRsAccDtlSbsList != null) {
                // ���¶�����ϸ��״̬���ռ���˻�ȡ��ɣ�
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG1.getCode());

                for(TaRsAccDtl taRsAccDtl : taRsAccDtlSbsList) {
                    taRsAccDtl.setTxAmt(df.format(Double.valueOf(taRsAccDtl.getTxAmt())));
                }
                //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I001"));
                return true;
            }
        }catch (Exception e){
            logger.error("һ���������ն��˲�ѯsbs����ʧ��", e);
            //MessageUtil.addError("��ѯ������Ϣʧ�ܡ�");
        }
        return false;
    }

    /**
     * �ڶ�_���ؼ�����SBS���ˣ��Ƚ�����List��
     */
    private boolean reconciDayEnd(){
        try {
            List<TaRsAccDtl> taRsAccDtlLocalListPara = taRsAccDtlLocalList;
            List<TaRsAccDtl> taRsAccDtlSbsListPara = taRsAccDtlSbsList;
            // List1�ظ���
            List<TaRsAccDtl> taRsAccDtlList1Repeat = new ArrayList<TaRsAccDtl>();
            // List2�ظ���
            List<TaRsAccDtl> taRsAccDtlList2Repeat = new ArrayList<TaRsAccDtl>();
            boolean isExist = false;
            // ����List1
            for(TaRsAccDtl taRsAccDtl1:taRsAccDtlLocalListPara){
                isExist = false;
                if(taRsAccDtl1.getTxCode().equals(EnuTaFdcTxCode.TRADE_2002.getCode()) ||
                        taRsAccDtl1.getTxCode().equals(EnuTaFdcTxCode.TRADE_2111.getCode()) ||
                        taRsAccDtl1.getTxCode().equals(EnuTaFdcTxCode.TRADE_2211.getCode())){ // һ���˻�������˻�
                    // ����List2
                    for(TaRsAccDtl taRsAccDtl2:taRsAccDtlSbsListPara){
                        if(taRsAccDtl1.getReqSn().substring(8,26).equals(taRsAccDtl2.getReqSn())
                                &&taRsAccDtl1.getGerlAccId().equals(taRsAccDtl2.getSpvsnAccId())
                                &&taRsAccDtl1.getSpvsnAccId().equals(taRsAccDtl2.getGerlAccId())
                                &&taRsAccDtl1.getTxAmt().equals(taRsAccDtl2.getTxAmt())){
                            isExist = true;
                            taRsAccDtlList2Repeat.add(taRsAccDtl2);
                        }
                    }
                } else { // ����˻���һ���˻�
                    // ����List2
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
                // ���¶�����ϸ��״̬���ռ����ƽ��
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG3.getCode());
                //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I003"));
                return true;
            } else {
                // ���¶�����ϸ��״̬���ռ���˲�ƽ��
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG2.getCode());
                //MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E003"));
            }
        } catch (Exception e) {
            logger.error("һ���������ն����ڲ�����ʧ��", e);
            //MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E004"));
        }

        return false;
    }

    /**
     * ���ݷ���
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
                    // ���¶�����ϸ��״̬���ռ���˷��ͳɹ���
                    taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG4.getCode());
                    //MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I002"));
                    return true;
                } else{
                    //MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E001"));
                }
            }
        } catch (Exception e) {
            logger.error("һ���������ն��˷���ftpʧ��", e);
            //MessageUtil.addError("�ռ���˷���ʧ�ܣ�");
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
                    // ������Ϣ
                    // �����ܱ���(6λ)|
                    Integer intTotalCounts=taRsAccDtlListPara.size();
                    line.append(StringUtils.rightPad(intTotalCounts.toString(), 6, ' '));
                    line.append("|");
                    // �����ܽ��(20λ)|
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
                    // ��ϸ��Ϣ
                    for(TaRsAccDtl taRsAccDtlUnit:taRsAccDtlListPara){
                        // ���״���(4λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getTxCode()), 4, ' '));
                        line.append("|");
                        // ҵ����(14λ���������������š�����ҵ���š�����ҵ����)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getBizId()), 14, ' '));
                        line.append("|");
                        txCode = taRsAccDtlUnit.getTxCode().substring(0,2);
                        if(txCode.equals("20")) {
                            // �����־(1λ��1_��/2_��������/��Ϣ=1������/����=2)|
                            line.append(StringUtils.rightPad("1", 1, ' '));
                        } else{
                            // �����־(1λ��1_��/2_��������/��Ϣ=1������/����=2)|
                            line.append(StringUtils.rightPad("2", 1, ' '));
                        }
                        line.append("|");
                        // ���׽��(20λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getYuanToFin(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getTxAmt())).toString(), 20, ' '));
                        line.append("|");
                        // ����˺�(30λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getSpvsnAccId()), 30, ' '));
                        line.append("|");
                        // Ԥ���ʽ���ƽ̨��ˮ(16λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getFdcSn()), 16, ' '));
                        line.append("|");
                        // ������м�����ˮ(30λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getFdcBankActSn()), 30, ' '));
                        line.append("|");
                        // ������м�������(30λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getSpvsnBankId()), 30, ' '));
                        line.append("|");
                        // ������м�����Ա(30λ)|
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getUserId()), 30, ' '));
                        line.append("|");
                        // ��������(10λ��YYYY-MM-DD)|
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
     * �����˻�ȡsbs����
     */
    private boolean getSbsDataBlncReconci() {
        try {
            // ���¶�����ϸ��״̬�������˻�ȡ�У�
            taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG5.getCode());

            // ȡ�����м���еļ���˺�����
            taRsAccList = taAccService.qryAllMonitRecords();

            if(taRsAccList.size() >0) {
                // ���ͼ���˺ŵ�SBS��ѯ���
                List<Toa900012701> toaSbs=taSbsService.sendAndRecvRealTimeTxn900012701(taRsAccList);

                if(toaSbs !=null && toaSbs.size() > 0) {
                    // �����Ƿ���ڲ�ѯʧ��
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

                    // ���¶�����ϸ��״̬�������˻�ȡ��ɣ�
                    taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG6.getCode());

                    //MessageUtil.addInfo(RfmMessage.getProperty("BalanceReconciliation.I001"));
                    return true;
                }
            } else {
                // ���¶�����ϸ��״̬�������˻�ȡ��ɣ�
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG6.getCode());
                return true;
            }
        }catch (Exception e){
            logger.error("һ�����������˻�ȡsbs����ʧ��", e);
            //MessageUtil.addError(e.getMessage());
        }

        return false;
    }

    /**
     * ������ftp����
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
                        // ���¶�����ϸ��״̬�������˷��ͳɹ���
                        taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG7.getCode());
                        //MessageUtil.addInfo(RfmMessage.getProperty("BalanceReconciliation.I002"));
                        return true;
                    } else{
                        //MessageUtil.addError(RfmMessage.getProperty("BalanceReconciliation.E003"));
                    }
                }
            } else {
                // ���¶�����ϸ��״̬�������˷��ͳɹ���
                taRsCheckService.insOrUpdTaRsCheck(EnuStatusFlag.STATUS_FLAG7.getCode());
                return true;
            }
        } catch (Exception e) {
            logger.error("һ�����������˷���ftpʧ�ܣ�", e);
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
     * ����dat�ļ�
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
