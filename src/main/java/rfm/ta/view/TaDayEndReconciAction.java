package rfm.ta.view;

import common.utils.ToolUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.fbi.dep.model.txn.Toa900012601;
import org.fbi.dep.model.txn.Toa900012602;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
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
import java.text.SimpleDateFormat;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
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
    // 账务交易明细
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
    // 往SBS发送记账信息
        TaTxnFdc taTxnFdcPara = new TaTxnFdc();
        taTxnFdcPara.setTxDate(ToolUtil.getNow("yyyyMMdd"));
        taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());
        // 日终对账总数查询
        Toa900012601 toa900012601Temp= (Toa900012601)taDayEndBlncService.sendAndRecvRealTimeTxn900012601(taTxnFdcPara);
        strSbsTotalCounts=toa900012601Temp.body.DRCNT;
        int intSendTimes=Integer.parseInt(strSbsTotalCounts);

        strSbsTotalAmt=toa900012601Temp.body.DRAMT;

        String totcnt = "";
        String curcnt = "";
        int m = 0;//取整
        int n = 0;//取余
        // 日终对账明细查询
        Toa900012602 toa900012602Temp= (Toa900012602)taDayEndBlncService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara, "0");
        List<Toa900012602.BodyDetail> detailsTemp =new ArrayList<>();
        if (toa900012602Temp != null && toa900012602Temp.body!=null) {
            if("0000".equals(toa900012602Temp.header.RETURN_CODE)) {
                detailsTemp = toa900012602Temp.body.DETAILS;
                totcnt = toa900012602Temp.body.TOTCNT;
                curcnt = toa900012602Temp.body.CURCNT;
            }
        }

        if (!totcnt.isEmpty()&&totcnt!=""){
            //因为 totcnt是全局变量，所有在第一次查询之后，发起第二次交易时totcnt就不为空，所有要在第一次发起交易时清空
            m = Integer.parseInt(totcnt) / Integer.parseInt(curcnt);
            n = Integer.parseInt(totcnt) % Integer.parseInt(curcnt);
            if (m>0&&n>0){
                String tmp = "";
                for (int j = 1; j <= m; j++) {
                    tmp = j * Integer.parseInt(curcnt) + 1 + "";
                    toa900012602Temp= (Toa900012602)taDayEndBlncService.sendAndRecvRealTimeTxn900012602(taTxnFdcPara,tmp);
                    if("0000".equals(toa900012602Temp.header.RETURN_CODE)) {
                        detailsTemp = toa900012602Temp.body.DETAILS;
                        totcnt = toa900012602Temp.body.TOTCNT;
                        curcnt = toa900012602Temp.body.CURCNT;
                    }
                }
            }
        }
        //taRsAccDtlSbsList = taAccDetlService.selectedRecords(new TaRsAccDtl());
        if(taRsAccDtlSbsList.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getAccId());
        }
    }

    public void onCreatFile() {
        File file;
        String filePath = "d:";
        // PFBBCCCCCCYYYYMMDD.dat，PF为固定字符，BB指监管银行代码（2位），CCCCCC 指城市代码（6位）YYYYMMDD为对账日期。
        String fileName = "PF"+ EnuTaBankId.BANK_HAIER.getCode()+
                                 EnuTaCityId.CITY_TAIAN.getCode()+
                                 ToolUtil.getNow("yyyyMMdd")+".dat";
        String newLineCh = "\r\n";
        StringBuffer line = new StringBuffer("");
        StringBuffer body = new StringBuffer("");
        try {
            file = ToolUtil.createFile(filePath, fileName);

        } catch (IOException e) {
            throw new RuntimeException(filePath + fileName + " ???????????", e);
        }
        try{
            /*m8872 = new M8872(erydat);
            SOFForm form = taSbsService.callSbsTxn("8872", m8872).get(0);
            String formcode = form.getFormHeader().getFormCode();

            if ("T846".equals(formcode)){
                t846 =(T846) form.getFormBody();
                if ("0".equals(t846.getDRCNT())&&"0.00".equals(t846.getDRAMT())){
                    line.append(getLeftSpaceStr(t846.getDRCNT(),6)).append("|").append(getLeftSpaceStr("0",20)).append("|");
                }else
                    line.append(getLeftSpaceStr(t846.getDRCNT(),6)).append("|").append(getLeftSpaceStr(t846.getDRAMT(),20)).append("|");
            }else {
                logger.error(formcode);
                MessageUtil.addErrorWithClientID("msgs", formcode);
            }
            for (TaRsAccDetail taRsAccDetail:taRsAccDetailList){
                body.append(newLineCh).append(getLeftSpaceStr(taRsAccDetail.getTradeId(),4)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getOriginId(), 14)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getInoutFlag(), 1)).append("|")
                        .append(getLeftSpaceStr(new DecimalFormat("#####0.00").format(taRsAccDetail.getTradeAmt()), 20)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getAccountCode(), 30)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getFdcSerial(), 16)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getBankSerial(), 30)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getBankBranchId(), 30)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getBankOperId(), 30)).append("|")
                        .append(getLeftSpaceStr(taRsAccDetail.getTradeDate(), 10)).append("|");
            }*/
            if (file!=null){
                try {
                    FileWriter fw = new FileWriter(file);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(line.toString());
                    bw.write(body.toString());
                    bw.flush();
                    fw.close();
                    bw.close();
                    ToolUtil.uploadFile("/home/feb/tmp", fileName, file);
                }catch (Exception e){
                    throw new RuntimeException(filePath + fileName + " ???д?????", e);
                }
            }
        }catch (Exception e){
            logger.error("??????????????", e);
            MessageUtil.addError("??????????????." + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public String onBlnc() {
        try {

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
        Path path = Paths.get("D:\\项目资料", filename);
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
