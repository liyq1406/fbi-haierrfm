package rfm.ta.view;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.account.TaAccDetlService;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lichao.W At 2015/7/6 17:01
 * wanglichao@163.com
 */
@ManagedBean
@ViewScoped
public class TaAccDtlAction implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(TaAccDtlAction.class);

    @ManagedProperty("#{taAccDetlService}")
    private TaAccDetlService taAccDetlService;

    private TaTxnSbs taTxnSbs;
    // 账务交易明细
    private List<TaRsAccDtl> taRsAccDtlList;
    private List<TaRsAccDtl> taRsAccDtlList2;
    private String erydat = new SimpleDateFormat("yyyyMMdd").format(new Date());

    @PostConstruct
    public void init(){
        taRsAccDtlList = taAccDetlService.selectedRecords(new TaRsAccDtl());
        if(taRsAccDtlList.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getRtnAccId());
        }
    }

    public void onQryLocaldatatest() {
        taRsAccDtlList2 = taAccDetlService.selectedRecords(new TaRsAccDtl());
        if(taRsAccDtlList2.size()>0) {
            System.out.println("======>" + taRsAccDtlList.get(0).getRtnAccId());
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

    //左对齐
    public String getLeftSpaceStr(String strValue, int totleBytesLen) {
        if(strValue == null) strValue = "";
        if (strValue.getBytes().length < totleBytesLen) {
            int spacelen = totleBytesLen - strValue.getBytes().length;
            for (int i = 0; i < spacelen; i++) {
                strValue += " ";
            }
        }
        return strValue;
    }

    //ftp发送到房产中心
    public static boolean uploadFile(String path, String filename,File file) {
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
            boolean change = ftpClient.changeWorkingDirectory(path);
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

    public List<TaRsAccDtl> getTaRsAccDtlList2() {
        return taRsAccDtlList2;
    }

    public void setTaRsAccDtlList2(List<TaRsAccDtl> taRsAccDtlList2) {
        this.taRsAccDtlList2 = taRsAccDtlList2;
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

    public List<TaRsAccDtl> getTaRsAccDetailList2() {
        return taRsAccDtlList2;
    }

    public void setTaRsAccDetailList2(List<TaRsAccDtl> taRsAccDtlList2) {
        this.taRsAccDtlList2 = taRsAccDtlList2;
    }
}
