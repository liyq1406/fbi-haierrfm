package rfm.ta.view.reconci;

import common.utils.ToolUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.txn.Toa900012601;
import org.fbi.dep.model.txn.Toa900012602;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.RfmMessage;
import rfm.ta.common.enums.EnuTaBankId;
import rfm.ta.common.enums.EnuTaCityId;
import rfm.ta.common.enums.EnuTaFdcTxCode;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.biz.acc.TaAccDetlService;
import rfm.ta.service.dep.TaSbsService;

import javax.annotation.PostConstruct;
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

    private TaTxnSbs taTxnSbs;
    // ��������ϸ
    private List<TaRsAccDtl> taRsAccDtlLocalList;
    private List<TaRsAccDtl> taRsAccDtlSbsList;
    private List<TaRsAccDtl> taRsAccDtlReconci;

    private String strLocalTotalCounts;
    private String strLocalTotalAmt;
    private String strSbsTotalCounts;
    private String strSbsTotalAmt;
    private DecimalFormat df = new DecimalFormat("#.00");
    private Map<String, String> txCodeMap;

    @PostConstruct
    public void init(){
        taRsAccDtlReconci = new ArrayList<TaRsAccDtl>();
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
            taRsAccDtlReconci.addAll(taRsAccDtlLocalList);
            Double total = 0d;
            for(TaRsAccDtl taRsAccDtl:taRsAccDtlLocalList){
                total += Double.valueOf(taRsAccDtl.getTxAmt());
                taRsAccDtl.setTxAmt(df.format(Double.valueOf(taRsAccDtl.getTxAmt())));
            }
            strLocalTotalAmt = ToolUtil.getMoneyString(total);
            System.out.println("======>" + taRsAccDtlLocalList.get(0).getSpvsnAccId());
        }
    }

    public void onQrySbsData() {
        try {
            // ��SBS���ͼ�����Ϣ
            TaTxnFdc taTxnFdcPara = new TaTxnFdc();
            taTxnFdcPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
            taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());
            // ���ն���������ѯ
            Toa900012601 toa900012601Temp = (Toa900012601) taSbsService.sendAndRecvRealTimeTxn900012601(taTxnFdcPara);
            strSbsTotalCounts = toa900012601Temp.body.DRCNT;

            strSbsTotalAmt = toa900012601Temp.body.DRAMT;

            // ���ն�����ϸ��ѯ
            taRsAccDtlSbsList = taSbsService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara);

            if (taRsAccDtlSbsList.size() > 0) {
                for(TaRsAccDtl taRsAccDtl : taRsAccDtlSbsList) {
                    taRsAccDtl.setTxAmt(df.format(Double.valueOf(taRsAccDtl.getTxAmt())));
                }
                MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I001"));
            }
        }catch (Exception e){
            logger.error("��ѯ������Ϣʧ��", e);
            MessageUtil.addError("��ѯ������Ϣʧ�ܡ�");
        }
    }

    private File createFile(List<TaRsAccDtl> taRsAccDtlListPara, String fileName) {
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
                    line.append(StringUtils.rightPad(strLocalTotalCounts, 6, ' '));
                    line.append("|");
                    // �����ܽ��(20λ)|
                    line.append(StringUtils.rightPad(strLocalTotalAmt, 20, ' '));
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
                        line.append(StringUtils.rightPad(ToolUtil.getStrIgnoreNull(taRsAccDtlUnit.getTxAmt()), 20, ' '));
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

    public void onBlnc() {
        File file = null;
        try {
            String fileName = "PF"+ EnuTaBankId.BANK_HAIER.getCode()+
                    EnuTaCityId.CITY_TAIAN.getCode()+
                    ToolUtil.getNow("yyyyMMdd")+".dat";
            file = createFile(taRsAccDtlReconci, fileName);
            if(file != null){
                boolean result = ToolUtil.uploadFile(fileName, file);
                if(result){
                    MessageUtil.addInfo(RfmMessage.getProperty("DayEndReconciliation.I002"));
                } else{
                    MessageUtil.addError(RfmMessage.getProperty("DayEndReconciliation.E001"));
                }
            }
        } catch (Exception e) {
            MessageUtil.addError("�ռ���˷���ʧ�ܣ�");
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
    }

    /**
     * �Ƚ�����List
     */
    public void reconci(){
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
            if(isExist){
                taRsAccDtlList1Repeat.add(taRsAccDtl1);
            }
        }

        taRsAccDtlLocalListPara.removeAll(taRsAccDtlList1Repeat);
        taRsAccDtlSbsListPara.removeAll(taRsAccDtlList2Repeat);
    }

    /**
     * ̩���������Ľ��׺�
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
    public List<TaRsAccDtl> getTaRsAccDtlReconci() {
        return taRsAccDtlReconci;
    }

    public void setTaRsAccDtlReconci(List<TaRsAccDtl> taRsAccDtlReconci) {
        this.taRsAccDtlReconci = taRsAccDtlReconci;
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
