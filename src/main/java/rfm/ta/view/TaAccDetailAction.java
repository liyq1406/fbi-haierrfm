package rfm.ta.view;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import platform.common.utils.MessageUtil;
import pub.platform.advance.utils.PropertyManager;
import rfm.ta.gateway.sbs.domain.core.domain.SOFForm;
import rfm.ta.gateway.sbs.domain.txn.model.form.ac.T846;
import rfm.ta.gateway.sbs.domain.txn.model.form.re.T924;
import rfm.ta.gateway.sbs.domain.txn.model.msg.M8872;
import rfm.ta.gateway.sbs.domain.txn.model.msg.M8873;
import rfm.ta.gateway.sbs.helper.BeanHelper;
import rfm.ta.gateway.sbs.taservice.TaSbsService;
import rfm.ta.repository.model.TaRsAccDetail;
import rfm.ta.repository.model.TaTxnSbs;
import rfm.ta.service.account.TaAccDetailService;
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
public class TaAccDetailAction implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(TaAccDetailAction.class);

    @ManagedProperty("#{taAccDetailService}")
    private TaAccDetailService taAccDetailService;

    @ManagedProperty("#{taSbsService}")
    private TaSbsService taSbsService;

    private TaTxnSbs taTxnSbs;
    M8873 m8873 = new M8873();
    M8872 m8872 = new M8872();
    T924 t924 = new T924();
    T846 t846 = new T846();
    private List<T924.Bean> dataList = new ArrayList<>();
    private List<TaRsAccDetail> taRsAccDetailList;
    private List<TaRsAccDetail> taRsAccDetailList2;
    private String erydat = new SimpleDateFormat("yyyyMMdd").format(new Date());

    @PostConstruct
    public void init(){
        taRsAccDetailList = taAccDetailService.detailAllList();
        System.out.println("======>" + taRsAccDetailList.get(0).getAccId());
    }

    public void onQryLocaldatatest() {
        taRsAccDetailList2 = taAccDetailService.detailAllList();
        System.out.println("======>" + taRsAccDetailList.get(0).getAccId());
    }

    public void onQrySBSdata() {
        try {
            m8873 = new M8873();
            m8873.setERYDA1(erydat);
            m8873.setBEGNUM("1");
            SOFForm form = taSbsService.callSbsTxn("8873", m8873).get(0);
            String formcode = form.getFormHeader().getFormCode();
            dataList = new ArrayList<>();
            if ("T924".equals(formcode)) {
                t924 = (T924) form.getFormBody();
                dataList.addAll(t924.getBeanList());
                int m = Integer.parseInt(t924.getTotcnt()) / Integer.parseInt(t924.getCurcnt());
                int n = Integer.parseInt(t924.getTotcnt()) % Integer.parseInt(t924.getCurcnt());
                if (m > 0 && n > 0) {
                    String tmp = "";
                    for (int i = 1; i <= m; i++) {
                        try {
                            tmp = i * Integer.parseInt(t924.getCurcnt()) + 1 + "";
                            m8873.setBEGNUM(tmp);
                            SOFForm form2 = taSbsService.callSbsTxn("8123", m8873).get(0);
                            String formcode2 = form.getFormHeader().getFormCode();
                            if ("T924".equals(formcode)) {
                                t924 = (T924) form2.getFormBody();
                                dataList.addAll(t924.getBeanList());
                            } else {
                                logger.error(formcode2);
                                MessageUtil.addErrorWithClientID("msgs", formcode2);
                            }
                        } catch (NumberFormatException e) {
                            logger.error("查询失败", e);
                            MessageUtil.addError("查询失败." + (e.getMessage() == null ? "" : e.getMessage()));
                        }
                    }
                }
                for (T924.Bean bean : dataList) {
                    try {
                        BeanHelper.copyFields(bean, taTxnSbs);
                        taAccDetailService.sbsdatcopy(taTxnSbs);
                    } catch (Exception e) {
                        logger.error("Bean 转换异常！", e);
                        MessageUtil.addError("Bean 转换异常！." + (e.getMessage() == null ? "" : e.getMessage()));
                    }
                }
            } else if ("W107".equals(formcode)) {
                MessageUtil.addInfoWithClientID("msgs", formcode);
            } else {
                logger.error(formcode);
                MessageUtil.addErrorWithClientID("msgs", formcode);
            }
        } catch (Exception e) {
            logger.error("查询失败", e);
            MessageUtil.addError("查询失败." + (e.getMessage() == null ? "" : e.getMessage()));
        }
    }

    public String onReconciliation() {
        if (taRsAccDetailList != null) {
            try {
                for (TaRsAccDetail taRsAccDetail : taRsAccDetailList) {
                    for (int i = 0; i < dataList.size(); i++) {
                        if (dataList.get(i).getMPCSEQ().equals(taRsAccDetail.getFdcSerial())) {
                            taRsAccDetailList.remove(taRsAccDetail);
                            dataList.remove(i);
                        }
                    }
                }
                if (taRsAccDetailList == null) {
                    MessageUtil.addInfo("对账成功！");
                }
            } catch (Exception e) {
                MessageUtil.addError("对账异常");
            }
        } else {
            MessageUtil.addInfo("今日无对账数据！");
        }
        onCreatFile();
        //修改系统时间
        return null;
    }

    public String onSendAll() {
        /*File file =new File("D:/PF1237090020150710.dat");
        boolean b = uploadFile("/tmp","PF12370900"+erydat+".dat",file);
        if (b){
            System.out.println("发送成功！");
        }*/
        return null;
    }

    public void onCreatFile() {
        File file;
        String filePath = "D:/brzfdc";
        String fileName = "PF12370900"+erydat+".dat";//PF为固定字符，BB指监管银行代码（2位），CCCCCC 指城市代码（6位）YYYYMMDD为对账日期。
        String newLineCh = "\r\n";       // 换行 适用于windows系统
        StringBuffer line = new StringBuffer("");
        StringBuffer body = new StringBuffer("");
        try {
            file = createFile(filePath, fileName);

        } catch (IOException e) {
            throw new RuntimeException(filePath + fileName + " 文件创建失败。", e);
        }
        try{
            m8872 = new M8872(erydat);
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
                .append(getLeftSpaceStr(taRsAccDetail.getBusiApplyId(), 14)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getInoutFlag(), 1)).append("|")
                .append(getLeftSpaceStr(new DecimalFormat("#####0.00").format(taRsAccDetail.getTradeAmt()), 20)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getAccId(), 30)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getFdcSerial(), 16)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getBankSerial(), 30)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getBankBranchId(), 30)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getBankOperId(), 30)).append("|")
                .append(getLeftSpaceStr(taRsAccDetail.getTradeDate(), 10)).append("|");
            }
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
                    throw new RuntimeException(filePath + fileName + " 文件写入错误。", e);
                }
            }
        }catch (Exception e){
            logger.error("对账文件生成失败", e);
            MessageUtil.addError("对账文件生成失败." + (e.getMessage() == null ? "" : e.getMessage()));
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

    public List<TaRsAccDetail> getTaRsAccDetailList2() {
        return taRsAccDetailList2;
    }

    public void setTaRsAccDetailList2(List<TaRsAccDetail> taRsAccDetailList2) {
        this.taRsAccDetailList2 = taRsAccDetailList2;
    }

    public M8873 getM8873() {
        return m8873;
    }

    public void setM8873(M8873 m8873) {
        this.m8873 = m8873;
    }

    public T924 getT924() {
        return t924;
    }

    public void setT924(T924 t924) {
        this.t924 = t924;
    }

    public List<TaRsAccDetail> getTaRsAccDetailList() {
        return taRsAccDetailList;
    }

    public void setTaRsAccDetailList(List<TaRsAccDetail> taRsAccDetailList) {
        this.taRsAccDetailList = taRsAccDetailList;
    }

    public TaAccDetailService getTaAccDetailService() {
        return taAccDetailService;
    }

    public void setTaAccDetailService(TaAccDetailService taAccDetailService) {
        this.taAccDetailService = taAccDetailService;
    }

    public TaSbsService getTaSbsService() {
        return taSbsService;
    }

    public void setTaSbsService(TaSbsService taSbsService) {
        this.taSbsService = taSbsService;
    }


    public List<T924.Bean> getDataList() {
        return dataList;
    }

    public void setDataList(List<T924.Bean> dataList) {
        this.dataList = dataList;
    }

    public M8872 getM8872() {
        return m8872;
    }

    public void setM8872(M8872 m8872) {
        this.m8872 = m8872;
    }

    public T846 getT846() {
        return t846;
    }

    public void setT846(T846 t846) {
        this.t846 = t846;
    }
}
