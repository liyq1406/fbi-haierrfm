package common.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.advance.utils.PropertyManager;
import pub.platform.form.config.SystemAttributeNames;
import pub.platform.security.OperatorManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {

    private static final Logger logger = LoggerFactory.getLogger(ToolUtil.class);
    public static String DEP_CHANNEL_ID_RFM = "990";
    public static String DEP_CHANNEL_ID_SBS = "900";
    public static String DEP_APPID = PropertyManager.getProperty("app_id");
    public static final BigDecimal bigDecimal0=new BigDecimal(0);
    public static Boolean strIsDigit(String strPara){
        String strRegex = "[1-9]\\d*(\\.[1-9]\\d*)*";
        if (strPara.matches(strRegex) ){
            return true;
        }
        return false;
    }

    public static int lookIndex(String strTempPara,char charPara,Integer intPara){
        int count=0;
        char arr[] = strTempPara.toCharArray();

        for(int i=0;i<arr.length;i++){
            if(charPara==arr[i]){
                count++;
                if(count==intPara){
                    return i;//返回所寻找的字符第五次出现的索引
                }
            }
        }
        return -1;//代表要寻找的字符没有出现五次
    }

    public static Object CopyBeanToBean(Object obj1,Object obj2) throws Exception{
        Method[] method1=obj1.getClass().getMethods();
        Method[] method2=obj2.getClass().getMethods();
        String methodName1;
        String methodFix1;
        String methodName2;
        String methodFix2;
        for(int i=0;i<method1.length;i++){
            methodName1=method1[i].getName();
            methodFix1=methodName1.substring(3,methodName1.length());
            if(methodName1.startsWith("get")){
                for(int j=0;j<method2.length;j++){
                    methodName2=method2[j].getName();
                    methodFix2=methodName2.substring(3,methodName2.length());
                    if(methodName2.startsWith("set")){
                        if(methodFix2.equals(methodFix1)){
                            Object[] objs1=new Object[0];
                            Object[] objs2=new Object[1];
                            objs2[0]=method1[i].invoke(obj1,objs1);//激活obj1的相应的get的方法，objs1数组存放调用该方法的参数,此例中没有参数，该数组的长度为0
                            method2[j].invoke(obj2,objs2);//激活obj2的相应的set的方法，objs2数组存放调用该方法的参数continue;
                         }
                    }
                }
            }
        }
        return obj2;
    }

    public static BigDecimal getBdIgnoreNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?new BigDecimal(0):bigDecimalPara;
    }

    public static String getFinToYuan(String stFenPara){
        DecimalFormat df = new DecimalFormat("#.00");
        return (df.format(Double.valueOf(stFenPara) / 100));
    }

    public static String getYuanToFin(String strYuanPara){
        DecimalFormat df = new DecimalFormat("#");
        return (df.format(Double.valueOf(strYuanPara) * 100));
    }

    public static Boolean Is0Null (BigDecimal bigDecimalPara){
        return bigDecimalPara==null?true:(bigDecimal0.compareTo(bigDecimalPara)==0?true:false);
    }

    public static BigDecimal getBdFrom0ToNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?null:(bigDecimal0.compareTo(bigDecimalPara)==0?null:bigDecimalPara);
    }

    public static String getStrIgnoreNull(String strValue){
        return strValue==null?"":strValue;
    }

    public static Integer getIntIgnoreNull(Integer intValue){
        return intValue==null?0:intValue ;
    }

    public static String getStrReqSn_Back() {
        Date date =new Date();
        return dateFormat(date, "yyyyMMddHHmmssSSSZ");
    }

    public static String getStrAppReqSn_Back() {
        Date date =new Date();
        return DEP_APPID+dateFormat(date, "yyyyMMddHHmmssSSSZ");
    }

    public static String getStrLastUpdDate() {
        Date date =new Date();
        return dateFormat(date, "yyyy-MM-dd");
    }
    public static String getStrLastUpdTime() {
        Date date =new Date();
        return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String getNow(String strPara) {
        Date date =new Date();
        return dateFormat(date, strPara);
    }

    public static String getStrToday() {
        Date date =new Date();
        return dateFormat(date, "yyyyMMdd");
    }

    public static int getDateByStr(String strStartDate,String strEndDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        int intDiff=0;
        try {
            Date startDate=simpleDateFormat.parse(strStartDate);
            Date endDate=simpleDateFormat.parse(strEndDate);
            if(startDate.after(endDate)){
                Date t=startDate;
                startDate=endDate;
                endDate=t;
            }
            Calendar startCalendar=Calendar.getInstance();
            startCalendar.setTime(startDate);
            Calendar endCalendar=Calendar.getInstance();
            endCalendar.setTime(endDate);
            //intDiff=endDate.getMonth()-startDate.getMonth();
            int intYear=endCalendar.get(Calendar.YEAR)-startCalendar.get(Calendar.YEAR);
            int intMonth=endCalendar.get(Calendar.MONTH)-startCalendar.get(Calendar.MONTH);
            intDiff = intYear*12+intMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return intDiff;
    }

    public static Date getLastMonthDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        return cal.getTime();
    }

    /**
     * 获取当前登录用户信息
     * @return
     */
    public static OperatorManager getOperatorManager(){
        ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        OperatorManager om = (OperatorManager) session.getAttribute(SystemAttributeNames.USER_INFO_NAME);
        if (om == null) {
            throw new RuntimeException("用户未登录！");
        }
        return om;
    }

    /**
     * 将长时间格式化
     * @param date
     * @param strPattern
     * @return
     */
    public static String dateFormat(Date date,String strPattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(strPattern);
        return formatter.format(date);
    }

    public static Calendar getDate(String sDate) {
        if ( sDate == null )
            return null;
        sDate = sDate.trim();
        if ( sDate.length() == 7 ) {
            sDate += "-01";
        }
        StringTokenizer st = new StringTokenizer(sDate,"-");
        int year=2100;
        int month=0;
        int day=1;
        try {
            year  = Integer.parseInt(st.nextToken());
            month = Integer.parseInt(st.nextToken())-1;
            day   = Integer.parseInt(st.nextToken());
        } catch ( Exception e ) {
            return null;
        }
        return new GregorianCalendar(year,month,day);
    }
    public static String getDateString(Calendar sDate) {
        if ( sDate == null )
            return "";
        return (new SimpleDateFormat("yyyy-MM-dd")).format(sDate.getTime());
    }
    public static String getYearMonth(Calendar sDate) {
        if ( sDate == null )
            return "";
        return (new SimpleDateFormat("yyyy-MM")).format(sDate.getTime());
    }

    public static String getMoneyString(double money) {
        NumberFormat nf =NumberFormat.getInstance();
        ((DecimalFormat) nf).applyPattern("#,000.00");
        String moneyStr=nf.format(money);
        return ""+moneyStr;
    }

    public static String getDateString(String date) {
        if ( date == null )
            return date;
        String[] dates = date.split("-");
        if ( dates.length >= 2 ) {
            return dates[0] + "年" + dates[1] + "月";
        } else {
            return date;
        }
    }
    public static String getChnDate(String date) {
        if ( date == null )
            return date;
        String[] dates = date.split("-");
        return dates[0]+"年"+dates[1]+"月"+dates[2]+"日";
    }
    public static String inttostr(String intStr){
        String returnStr ="";
        for (int i=0; i <intStr.length() ;i++ ){
            if ( String.valueOf(intStr.charAt(i)).equals("1"))
                returnStr +="a";
            if ( String.valueOf(intStr.charAt(i)).equals("2"))
                returnStr +="b";
            if ( String.valueOf(intStr.charAt(i)).equals("3"))
                returnStr +="c";
            if ( String.valueOf(intStr.charAt(i)).equals("4"))
                returnStr +="d";
            if ( String.valueOf(intStr.charAt(i)).equals("5"))
                returnStr +="e";
            if ( String.valueOf(intStr.charAt(i)).equals("6"))
                returnStr +="f";
            if ( String.valueOf(intStr.charAt(i)).equals("7"))
                returnStr +="g";
            if ( String.valueOf(intStr.charAt(i)).equals("8"))
                returnStr +="h";
            if ( String.valueOf(intStr.charAt(i)).equals("9"))
                returnStr +="i";
            if ( String.valueOf(intStr.charAt(i)).equals("0"))
                returnStr +="j";
        }
        return returnStr;
    }

    public static String strtoint(String intStr){
        String returnStr ="";
        for (int i=0; i <intStr.length() ;i++ ){
            if ( String.valueOf(intStr.charAt(i)).equals("a"))
                returnStr +="1";
            if ( String.valueOf(intStr.charAt(i)).equals("b"))
                returnStr +="2";
            if ( String.valueOf(intStr.charAt(i)).equals("c"))
                returnStr +="3";
            if ( String.valueOf(intStr.charAt(i)).equals("d"))
                returnStr +="4";
            if ( String.valueOf(intStr.charAt(i)).equals("e"))
                returnStr +="5";
            if ( String.valueOf(intStr.charAt(i)).equals("f"))
                returnStr +="6";
            if ( String.valueOf(intStr.charAt(i)).equals("g"))
                returnStr +="7";
            if ( String.valueOf(intStr.charAt(i)).equals("h"))
                returnStr +="8";
            if ( String.valueOf(intStr.charAt(i)).equals("i"))
                returnStr +="9";
            if ( String.valueOf(intStr.charAt(i)).equals("j"))
                returnStr +="0";
        }
        return returnStr;
    }

    public static BigDecimal getBdIgnoreZeroNull(BigDecimal bigDecimalPara){
        return bigDecimalPara==null?bigDecimal0:(bigDecimal0.compareTo(bigDecimalPara)==0?bigDecimal0:bigDecimalPara);
    }
    //算小计大计bd,str并存
    public static BigDecimal getBdFromStrOrBdIgnoreNull(Object objPara){
        if(objPara==null){
            return  bigDecimal0;
        }else if(objPara.getClass().equals((new BigDecimal(0)).getClass())){
            return (BigDecimal)objPara;
        }else {
            return objPara.equals("")?bigDecimal0:new BigDecimal(objPara.toString().replace(",","").replace("%",""));
        }
    }
    //将%以bd形式存入数据库
    public static BigDecimal getBdFromStrInPercent(String strPara){
        return   getStrIgnoreNull(strPara).equals("")?bigDecimal0:(new BigDecimal(strPara.replace("%","")).divide(new BigDecimal(100)));
    }
    //将数据库bd取出并转化为str
    public static String getStrFromBdIgnoreZeroNull(String strFormatPara, BigDecimal bigDecimalPara){
        if (bigDecimal0.compareTo(getBdIgnoreNull(bigDecimalPara))==0){
            return "";
        }else{
            DecimalFormat  df =new DecimalFormat(strFormatPara);
            return df.format(getBdIgnoreZeroNull(bigDecimalPara));
        }

    }

    /**
     * 获取两子串之间的字符串内容
     *
     * @param fromStr
     * @param startStr
     * @param endStr
     * @return
     */
    public static String getSubstrBetweenStrs(String fromStr, String startStr, String endStr) {
        int length = startStr.length();
        int start = fromStr.indexOf(startStr) + length;
        int end = fromStr.indexOf(endStr);
        return fromStr.substring(start, end);
    }

    /**
     * 将amt单位元转换为分，最少三位、如：0.01元——》001
     *
     * @param amt
     * @return
     */
    public static String toBiformatAmt(BigDecimal amt) {
        String rtnStrAmt = "";
        if (amt.compareTo(new BigDecimal(1)) < 0) {
            if (amt.compareTo(new BigDecimal(0.1)) > 0) {
                rtnStrAmt = "0" + String.valueOf(amt.multiply(new BigDecimal(100)));
            } else {
                rtnStrAmt = "00" + String.valueOf(amt.multiply(new BigDecimal(100)));
            }
        } else {
            rtnStrAmt = String.valueOf(amt.multiply(new BigDecimal(100)));
        }
        return rtnStrAmt.split("\\.")[0];
    }

    /**
     * 将字符串中旧子串内容替换为新内容
     *
     * @param fromStr
     * @param oldSubstr
     * @param newSubstr
     * @return
     */
    public static String replaceOldstrToNewstr(String fromStr, String oldSubstr, String newSubstr) {
        String toStr = fromStr;
        Pattern pattern = null;
        Matcher matcher = null;
        pattern = Pattern.compile(oldSubstr);
        matcher = pattern.matcher(toStr);
        if (matcher.find()) {
            toStr = matcher.replaceAll(newSubstr);
        }
        return toStr;
    }

    public static String transDate10ToDate8(String strDate10) {
        return strDate10.substring(0, 4) + strDate10.substring(5, 7) + strDate10.substring(8, 10);
    }

    public static String transDate8ToDate10(String strDate8) {
        return strDate8.substring(0, 4) + "-" + strDate8.substring(4, 6) + "-" + strDate8.substring(6, 8);
    }

    public static String rightPad4ChineseToByteLength(String srcStr, int totalByteLength, String padStr) {
        if (srcStr == null) {
            return null;
        }
        int srcByteLength = srcStr.getBytes().length;

        if (padStr == null || "".equals(padStr)) {
            padStr = " ";
        } else if (padStr.getBytes().length > 1 || totalByteLength <= 0) {
            throw new RuntimeException("参数错误");
        }
        StringBuilder rtnStrBuilder = new StringBuilder();
        if (totalByteLength >= srcByteLength) {
            rtnStrBuilder.append(srcStr);
            for (int i = 0; i < totalByteLength - srcByteLength; i++) {
                rtnStrBuilder.append(padStr);
            }
        } else {
            byte[] rtnBytes = new byte[totalByteLength];
            System.arraycopy(srcStr.getBytes(), 0, rtnBytes, 0, totalByteLength);
            rtnStrBuilder.append(rtnBytes);
        }
        return rtnStrBuilder.toString();
    }

    public static void main(String[] argv) {
        System.out.println(getDateString("2004-10-20"));
    }

    public static File createFile(String filePath, String fileName) throws IOException {
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

    /**
     * ftp发送到房产中心
     * @param filename
     * @param file
     * @return
     */
    public static boolean uploadFile(String filename,File file) {
        String fcurl = PropertyManager.getProperty("tarfmfdc_fcurl");
        String targetPath = PropertyManager.getProperty("tarfmfdc_fcpath");
        String fcusername = PropertyManager.getProperty("tarfmfdc_fcusername");
        String fcpasswd = PropertyManager.getProperty("tarfmfdc_fcpasswd");
        String encoding = PropertyManager.getProperty("tarfmfdc_fileEncoding");
        FTPClient ftpClient = new FTPClient();
        boolean result = false;
        FileInputStream input = null;
        try {
            int reply;
            ftpClient.connect(fcurl);
            ftpClient.login(fcusername, fcpasswd);
            ftpClient.setControlEncoding(encoding);
            // 检验是否连接成功
            reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                //logger.error("连接失败！");
                ftpClient.disconnect();
                return result;
            }
            // 转移工作目录至指定目录下
            boolean change = ftpClient.changeWorkingDirectory(targetPath);
            ftpClient.enterLocalPassiveMode(); //被动模式  默认为主动模式
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setBufferSize(3072);
            ftpClient.setControlEncoding("UTF-8");
            if (change) {
                input= new FileInputStream(file);
                result = ftpClient.storeFile(new String(filename.getBytes(encoding),"iso-8859-1"), input);
                if (result) {
                    //logger.info("ftp发送房产中心成功!"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }else {
                    //logger.error("ftp发送房产中心成功!"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if( input!=null){
                    input.close();
                }
                ftpClient.logout();
                if (ftpClient.isConnected()) {
                    ftpClient.disconnect();
                }
            }
            catch (IOException ioe) {
            }
        }
        return result;
    }
}

