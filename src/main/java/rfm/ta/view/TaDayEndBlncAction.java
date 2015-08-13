package rfm.ta.view;

import common.utils.ToolUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.fbi.dep.model.base.TOA;
import org.fbi.dep.model.txn.Toa900012601;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lichao.W At 2015/7/6 17:01
 * wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class TaDayEndBlncAction implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(TaDayEndBlncAction.class);

    @ManagedProperty("#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    @ManagedProperty("#{taDayEndBlncService}")
    private TaDayEndBlncService taDayEndBlncService;

    private TaTxnSbs taTxnSbs;
    // 账务交易明细
    private List<TaRsAccDtl> taRsAccDtlList;
    private List<TaRsAccDtl> taRsAccDtlSbsList;
    private String erydat = new SimpleDateFormat("yyyyMMdd").format(new Date());

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
        TaRsAccDtl taRsAccDtlPara=new TaRsAccDtl();
        taRsAccDtlPara.setTradeDate(ToolUtil.getNow("yyyy-MM-dd"));
        taRsAccDtlList = taAccDetlService.selectedRecords(taRsAccDtlPara);
        strLocalTotalCounts=String.valueOf(taRsAccDtlList.size());
        if(taRsAccDtlList.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getAccId());
        }
    }

    public void onQrySbsData() {
    // 往SBS发送记账信息
        TaTxnFdc taTxnFdcPara = new TaTxnFdc();
        taTxnFdcPara.setTradeDate(ToolUtil.getNow("yyyyMMdd"));
        taTxnFdcPara.setReqSn(ToolUtil.getStrReqSn_Back());
        Toa900012601 toa900012601Temp= (Toa900012601)taDayEndBlncService.sendAndRecvRealTimeTxn900012601(taTxnFdcPara);
        strSbsTotalCounts=toa900012601Temp.body.DRCNT;
        strSbsTotalAmt=toa900012601Temp.body.DRAMT;
        //taRsAccDtlSbsList = taAccDetlService.selectedRecords(new TaRsAccDtl());
        if(taRsAccDtlSbsList.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getAccId());
        }
    }

    private File createFile(String filePath, String fileName) throws IOException {
        File dir = new File(filePath);
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }
        File tempFile = new File(filePath, fileName);
        if (tempFile.exists()) {
            tempFile.delete();
            tempFile.createNewFile();
        }
        return tempFile;
    }

    public void onCreatFile() {
        File file;
        String filePath = "d:";
        String fileName = "PF12370900"+erydat+".dat";//PF?????????BB???????д???2λ????CCCCCC ????д???6λ??YYYYMMDD??????????
        String newLineCh = "\r\n";       // ???? ??????windows??
        StringBuffer line = new StringBuffer("");
        StringBuffer body = new StringBuffer("");
        try {
            file = createFile(filePath, fileName);

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
                    uploadFile("/home/feb/tmp",fileName,file);
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
        uploadFile(targetPath, filename, file);
    }

    /**
     * ftp发送到房产中心
     * @param targetPath ftp服务器目标路径
     * @param filename
     * @param file
     * @return
     */
    public static boolean uploadFile(String targetPath, String filename,File file) {
        String fcurl = PropertyManager.getProperty("tarfmfdc_fcurl");
        String fcusername = PropertyManager.getProperty("tarfmfdc_fcusername");
        String fcpasswd = PropertyManager.getProperty("tarfmfdc_fcpasswd");
        String encoding = PropertyManager.getProperty("tarfmfdc_fileEncoding");
        FTPClient ftpClient = new FTPClient();
        boolean result = false;
        try {
            int reply;
            ftpClient.connect(fcurl);
            ftpClient.login(fcusername, fcpasswd);
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                logger.error("连接失败！");
                ftpClient.disconnect();
                return result;
            }
            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(targetPath);
            ftpClient.enterLocalPassiveMode(); //被动模式  默认为主动模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(3072);
            ftpClient.setControlEncoding("UTF-8");
            FileInputStream input = null;
            if (change) {
                input= new FileInputStream(file);
                result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
                if (result) {
                    logger.info("ftp发送房产中心成功!"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }else {
                    logger.error("ftp发送房产中心成功!"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            }
            input.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
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
