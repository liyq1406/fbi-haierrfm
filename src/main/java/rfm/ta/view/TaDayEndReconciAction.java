package rfm.ta.view;

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
import rfm.ta.service.account.TaAccDetlService;
import rfm.ta.service.account.TaDayEndBlncService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @ManagedProperty("#{taDayEndBlncService}")
    private TaDayEndBlncService taDayEndBlncService;

    private TaTxnSbs taTxnSbs;
    // ��������ϸ
    private List<TaRsAccDtl> taRsAccDtlList;
    private List<TaRsAccDtl> taRsAccDtlSbsList;

    private String strLocalTotalCounts;
    private String strLocalTotalAmt;
    private String strSbsTotalCounts;
    private String strSbsTotalAmt;

    @PostConstruct
    public void init(){
        strLocalTotalCounts="0";
        strLocalTotalAmt="0";
        strSbsTotalCounts="0";
        strSbsTotalAmt="0";
        taRsAccDtlSbsList=new ArrayList<>();
        TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
        taRsAccDtlPara.setTxDate(ToolUtil.getNow("yyyy-MM-dd"));
        taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlPara);
        strLocalTotalCounts=String.valueOf(taRsAccDtlList.size());
        if(taRsAccDtlList.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getAccId());
        }
    }

    public void onQrySbsData() {
        try {
            // ��SBS���ͼ�����Ϣ
            TaTxnFdc taTxnFdcPara = new TaTxnFdc();
            taTxnFdcPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
            taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());
            // ���ն���������ѯ
            Toa900012601 toa900012601Temp = (Toa900012601) taDayEndBlncService.sendAndRecvRealTimeTxn900012601(taTxnFdcPara);
            strSbsTotalCounts = toa900012601Temp.body.DRCNT;

            strSbsTotalAmt = toa900012601Temp.body.DRAMT;

            String totcnt = "";
            String curcnt = "";
            int m = 0;//ȡ��
            int n = 0;//ȡ��
            // ���ն�����ϸ��ѯ
            Toa900012602 toa900012602Temp = (Toa900012602) taDayEndBlncService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara, "0");
            List<Toa900012602.Body.BodyDetail> detailsTemp = new ArrayList<>();
            if (toa900012602Temp != null && toa900012602Temp.body != null) {
                if ("0000".equals(toa900012602Temp.header.RETURN_CODE)) {
                    detailsTemp = toa900012602Temp.body.DETAILS;
                    totcnt = toa900012602Temp.body.TOTCNT;
                    curcnt = toa900012602Temp.body.CURCNT;
                }
            }

            // ��ʱ��
            TaRsAccDtl taRsAccDtlTemp=new TaRsAccDtl();
            for(Toa900012602.Body.BodyDetail bdUnit:detailsTemp){
                taRsAccDtlTemp.setTxCode(bdUnit.ACTNUM);
                taRsAccDtlTemp.setRecvAccId(bdUnit.BENACT);
                taRsAccDtlTemp.setTxAmt(bdUnit.TXNAMT);
                taRsAccDtlTemp.setTxDate(bdUnit.ERYTIM);
                taRsAccDtlSbsList.add(taRsAccDtlTemp);
            }

            if (!totcnt.isEmpty() && totcnt != "") {

                //��Ϊ totcnt��ȫ�ֱ����������ڵ�һ�β�ѯ֮�󣬷���ڶ��ν���ʱtotcnt�Ͳ�Ϊ�գ�����Ҫ�ڵ�һ�η�����ʱ���
                m = Integer.parseInt(totcnt) / Integer.parseInt(curcnt);
                n = Integer.parseInt(totcnt) % Integer.parseInt(curcnt);
                if (m > 0 && n > 0) {
                    String tmp = "";
                    for (int j = 1; j <= m; j++) {
                        tmp = j * Integer.parseInt(curcnt) + 1 + "";
                        toa900012602Temp = (Toa900012602) taDayEndBlncService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara, tmp);
                        if ("0000".equals(toa900012602Temp.header.RETURN_CODE)) {
                            detailsTemp = toa900012602Temp.body.DETAILS;
                            totcnt = toa900012602Temp.body.TOTCNT;
                            curcnt = toa900012602Temp.body.CURCNT;
                        }
                    }
                }
            }
            //taRsAccDtlSbsList = taAccDetlService.selectedRecords(new TaRsAccDtl());
            if (taRsAccDtlSbsList.size() > 0) {
                MessageUtil.addInfo("��ȡSBS������Ϣ�ɹ���");
                System.out.println("======>" + taRsAccDtlList.get(0).getAccId());
            }
        }catch (Exception e){
            logger.error("��ѯ������Ϣʧ��", e);
            MessageUtil.addError("��ѯ������Ϣʧ�ܡ�");
        }
    }

    private void sendReconciFile(List<TaRsAccDtl> taRsAccDtlListPara) {
        String sysdate = ToolUtil.getStrLastUpdDate();
        File file;
        String filePath = "d:";

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
                    line.append(StringUtils.rightPad(taRsAccDtlUnit.getTxAmt().toString(), 20, ' '));
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

    public String onBlnc() {
        try {
            sendReconciFile(taRsAccDtlSbsList);
        } catch (Exception e) {
            MessageUtil.addError("??????");
        }
        //onCreatFile();
        //????????
        return null;
    }
    public static void main(String[] args) {
        String targetPath = "rfmtest";
        String filename = "a.dat";
        Path path = Paths.get("D:\\��Ŀ����", filename);
        File file = path.toFile();
        ToolUtil.uploadFile(targetPath, filename, file);
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

    public List<TaRsAccDtl> getTaRsAccDtlList() {
        return taRsAccDtlList;
    }

    public void setTaRsAccDtlList(List<TaRsAccDtl> taRsAccDtlList) {
        this.taRsAccDtlList = taRsAccDtlList;
    }

    public TaAccDetlService getTaAccDetlService() {
        return taAccDetlService;
    }

    public void setTaAccDetlService(TaAccDetlService taAccDetlService) {
        this.taAccDetlService = taAccDetlService;
    }

    public List<TaRsAccDtl> getTaRsAccDetailList() {
        return taRsAccDtlList;
    }

    public void setTaRsAccDetailList(List<TaRsAccDtl> taRsAccDtlList) {
        this.taRsAccDtlList = taRsAccDtlList;
    }

    public TaDayEndBlncService getTaDayEndBlncService() {
        return taDayEndBlncService;
    }

    public void setTaDayEndBlncService(TaDayEndBlncService taDayEndBlncService) {
        this.taDayEndBlncService = taDayEndBlncService;
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
