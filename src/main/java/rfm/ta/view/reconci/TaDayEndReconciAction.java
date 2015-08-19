package rfm.ta.view.reconci;

import common.utils.ToolUtil;
import org.apache.commons.lang.StringUtils;
import org.fbi.dep.model.txn.Toa900012601;
import org.fbi.dep.model.txn.Toa900012602;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import rfm.ta.common.enums.EnuTaBankId;
import rfm.ta.common.enums.EnuTaCityId;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    private String strLocalTotalCounts;
    private String strLocalTotalAmt;
    private String strSbsTotalCounts;
    private String strSbsTotalAmt;
    private DecimalFormat df = new DecimalFormat("#.00");

    @PostConstruct
    public void init(){
        strLocalTotalCounts="0";
        strLocalTotalAmt="0";
        strSbsTotalCounts="0";
        strSbsTotalAmt="0";
        taRsAccDtlSbsList=new ArrayList<>();
        TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
        taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
        taRsAccDtlLocalList = taAccDetlService.selectedRecords(taRsAccDtlPara);
        strLocalTotalCounts=String.valueOf(taRsAccDtlLocalList.size());
        if(taRsAccDtlLocalList.size()>0) {
            Double total = 0d;
            for(TaRsAccDtl taRsAccDtl:taRsAccDtlLocalList){
                total += Double.valueOf(taRsAccDtl.getTxAmt());
                taRsAccDtl.setTxAmt(StringUtils.leftPad(df.format(Double.valueOf(taRsAccDtl.getTxAmt())),16,"0"));
            }
            strLocalTotalAmt = ToolUtil.getMoneyString(total);
            System.out.println("======>" + taRsAccDtlLocalList.get(0).getAccId());
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
                MessageUtil.addInfo("��ȡSBS������Ϣ�ɹ���");
            }
        }catch (Exception e){
            logger.error("��ѯ������Ϣʧ��", e);
            MessageUtil.addError("��ѯ������Ϣʧ�ܡ�");
        }
    }

    private void sendReconciFile(List<TaRsAccDtl> taRsAccDtlListPara) {
        String sysdate = ToolUtil.getStrLastUpdDate();
        File file;
        String filePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/upload/reconci");

        String fileName = "PF"+ EnuTaBankId.BANK_HAIER.getCode()+
                EnuTaCityId.CITY_TAIAN.getCode()+
                ToolUtil.getNow("yyyyMMdd")+".dat";

        StringBuffer line = new StringBuffer();
        FileWriter fw = null;
        BufferedWriter bw = null;
        String newLineCh = "\r\n";
        try {
            file = ToolUtil.createFile(filePath, fileName);
            if(file != null){
                // ������Ϣ
                // �����ܱ���(6λ)|
                line.append(StringUtils.rightPad(strLocalTotalCounts, 6, ' '));
                line.append("|");
                // �����ܽ��(20λ)|
                line.append(StringUtils.rightPad(strLocalTotalAmt, 20, ' '));
                line.append("|");

                line.append(newLineCh);

                // ��ϸ��Ϣ
                for(TaRsAccDtl taRsAccDtlUnit:taRsAccDtlListPara){
                    // ���״���(4λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getTxCode(), 4, ' '));
                    line.append("|");
                    // ҵ����(14λ���������������š�����ҵ���š�����ҵ����)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getBizId(), 14, ' '));
                    line.append("|");
                    // �����־(1λ��1_��/2_��������/��Ϣ=1������/����=2)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getInoutFlag(), 1, ' '));
                    line.append(sysdate);
                    // ���׽��(20λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getTxAmt(), 20, ' '));
                    line.append("|");
                    // ����˺�(30λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getAccId(), 30, ' '));
                    line.append("|");
                    // Ԥ���ʽ���ƽ̨��ˮ(16λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getFdcSn(), 16, ' '));
                    line.append("|");
                    // ������м�����ˮ(30λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getFdcBankActSn(), 30, ' '));
                    line.append("|");
                    // ������м�������(30λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getBankId(), 30, ' '));
                    line.append("|");
                    // ������м�����Ա(30λ)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getUserId(), 30, ' '));
                    line.append("|");
                    // ��������(10λ��YYYY-MM-DD)|
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getTxDate(), 10, ' '));
                    line.append("|");
                }
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write(line.toString());
                bw.flush();
                ToolUtil.uploadFile("rfmtest", fileName, file);
                file.delete();
            }
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
        try {
            List<TaRsAccDtl> taRsAccDtls = new ArrayList<>();
            taRsAccDtls.addAll(taRsAccDtlLocalList);
            if(reconci(taRsAccDtlLocalList,taRsAccDtlSbsList)) {
                sendReconciFile(taRsAccDtls);
            }
        } catch (Exception e) {
            MessageUtil.addError("�ռ���˷���ʧ�ܣ�");
        }
    }
    public static void main(String[] args) {
        String targetPath = "rfmtest";
        String filename = "a.dat";
        Path path = Paths.get("D:\\��Ŀ����", filename);
        File file = path.toFile();
        ToolUtil.uploadFile(targetPath, filename, file);
    }

    /**
     * �Ƚ�����List
     * @param taRsAccDtlLocalListPara
     * @param taRsAccDtlSbsListPara
     */
    private Boolean reconci(List<TaRsAccDtl> taRsAccDtlLocalListPara, List<TaRsAccDtl> taRsAccDtlSbsListPara){
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
                        &&taRsAccDtl1.getAccId().equals(taRsAccDtl2.getAccId())
                        &&taRsAccDtl1.getRecvAccId().equals(taRsAccDtl2.getRecvAccId())
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

        if(taRsAccDtlLocalListPara.size() == 0 && taRsAccDtlSbsListPara.size() == 0){
            return true;
        }

        return false;
    }

    //= = = = = = = = = = = = get set = = = = = = = = = = = =

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
