package rfm.ta.gateway.sbs.domain.core;

import com.ibm.ctg.client.ECIRequest;
import com.ibm.ctg.client.JavaGateway;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pub.platform.advance.utils.PropertyManager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zhanrui
 * Date: 2009-5-10
 * Time: 20:58:47
 * To change this template use File | Settings | File Templates.
 */
public class CtgManager {

    private static Logger logger = LoggerFactory.getLogger(CtgManager.class);
    private JavaGateway javaGatewayObject;

    private boolean bDataConv = true;
    private String strDataConv = "ASCII";

    private String strProgram = "SCLMPC";     // 房产入账调用SCLMPC  不调用 SCLMAIN
    private String strChosenServer = "haier";

    private String strUrl = PropertyManager.getProperty("SBS_HOSTIP");
    private int iPort = Integer.parseInt(PropertyManager.getProperty("SBS_HOSTPORT"));

    private int iCommareaSize = 32000;

    private static String CICS_USERID = "CICSUSER";
    private static String CICS_PWD = "";

    /**
     * 处理单包查询的情况
     *
     * @throws Exception
     */
    public void processSingleResponsePkg(SBSRequest request, SBSResponse response) {

        ECIRequest eciRequestObject = null;
        javaGatewayObject = null;
        try {
            javaGatewayObject = new JavaGateway(strUrl, iPort);
            eciRequestObject = ECIRequest.listSystems(20);
            flowRequest(eciRequestObject);

            if (eciRequestObject.SystemList.isEmpty()) {
                System.out.println("No CICS servers have been defined.");
                if (javaGatewayObject.isOpen()) {
                    javaGatewayObject.close();
                }
                throw new Exception("未定义 CICS 服务器，请确认！");
            }

            //通讯耗时
            long txTotalTime = 0;
            String requestBuffer = "";

            byte[] abytCommarea = new byte[iCommareaSize];

            //包头内容，xxxx交易，010网点，MPC1终端，MPC1柜员，包头定长51个字符
            requestBuffer = "TPEI" + request.getTxncode() + "  010       "
                    + request.getTermid() + request.getTellerid()
                    + (StringUtils.isEmpty(request.getAuttlr()) ? "" : request.getAuttlr())
                    + (StringUtils.isEmpty(request.getAutpwd()) ? "" : request.getAutpwd());
            //打包包头
            System.arraycopy(getBytes(requestBuffer), 0, abytCommarea, 0, requestBuffer.length());
            //打包包体
            setBufferValues(request.getParamList(), abytCommarea);

            String sendTime = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date());
            logger.info("交易" + request.getTxncode() + " 发送报文: " + sendTime + format16(truncBuffer(abytCommarea)));

            long starttime = System.currentTimeMillis();
            //发送包
            eciRequestObject = new ECIRequest(ECIRequest.ECI_SYNC, //ECI call type
                    strChosenServer, //CICS server
                    CICS_USERID, //CICS userid
                    CICS_PWD, //CICS password
                    strProgram, //CICS program to be run
                    null, //CICS transid to be run
                    abytCommarea, //Byte array containing the COMMAREA
                    iCommareaSize, //COMMAREA length
                    ECIRequest.ECI_NO_EXTEND, //ECI extend mode
                    0);                       //ECI LUW token

            //获取返回报文
            if (flowRequest(eciRequestObject)) {
                logger.info("交易" + request.getTxncode() + " 接收报文(发送时间:" + sendTime + "): " + format16(truncBuffer(abytCommarea)));
            }

            long endtime = System.currentTimeMillis();

//            logger.info("交易" + request.getTxncode() + " 接收报文(发送时间:" + sendTime + "): " + format16(truncBuffer(resbytes)));
            byte[] databytes = truncBuffer(abytCommarea);
            byte[] resbytes = new byte[databytes.length + 4];
            System.arraycopy(databytes, 0, resbytes, 0, databytes.length);
            resbytes[databytes.length] = 0x00;
            resbytes[databytes.length + 1] = 0x00;
            resbytes[databytes.length + 2] = 0x00;
            resbytes[databytes.length + 3] = 0x00;
            response.init(resbytes);
//            response.init(abytCommarea);
//          String formcode = response.getFormcode();
//            logger.info("===返回FORMCODE:" + formcode + "   本包通讯耗时:" + (endtime - starttime) + "ms.");
            logger.info("本包通讯耗时:" + (endtime - starttime) + "ms.");
        } catch (Exception e) {
            logger.error("与SBS通讯出现问题：", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (javaGatewayObject != null) {
                if (javaGatewayObject.isOpen()) {
                    try {
                        javaGatewayObject.close();
                    } catch (IOException e) {
                        logger.error("与SBS通讯出现问题：", e);
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }
    }

    //===================================================================================================
    private byte[] getBytes(String source) throws UnsupportedEncodingException {
        if (bDataConv) {
            return source.getBytes(strDataConv);
        } else {
            return source.getBytes();
        }
    }

    private void setBufferValues(List list, byte[] bb) throws UnsupportedEncodingException {
        int start = 51;
        for (int i = 1; i <= list.size(); i++) {
            String value = list.get(i - 1).toString();    //07
            setVarData(start, value, bb);
            start = start + value.getBytes("GBK").length + 2;
        }
    }

    private void setVarData(int pos, String data, byte[] aa) throws UnsupportedEncodingException {
        short len = (short) data.getBytes("GBK").length;

        byte[] slen = new byte[2];
        slen[0] = (byte) (len >> 8);
        slen[1] = (byte) (len);
        System.arraycopy(slen, 0, aa, pos, 2);
        System.arraycopy(data.getBytes(), 0, aa, pos + 2, len);   //aa 51=0  52=2 53=48(0) 54 =  55(7)
    }


    private boolean flowRequest(ECIRequest requestObject) throws Exception {
        int iRc = javaGatewayObject.flow(requestObject);
        String msg = null;
        switch (requestObject.getCicsRc()) {
            case ECIRequest.ECI_NO_ERROR:
                if (iRc == 0) {
                    return true;
                } else {
                    if (javaGatewayObject.isOpen() == true) {
                        javaGatewayObject.close();
                    }
                    throw new Exception("SBS Gateway 出现错误("
                            + requestObject.getRcString()
                            + "), 请查明原因，重新发起交易");
                }
            case ECIRequest.ECI_ERR_SECURITY_ERROR:
                msg = "SBS CICS: 用户名或密码错误";
                break;
            case ECIRequest.ECI_ERR_TRANSACTION_ABEND:
                msg = "SBS CICS : 没有权限运行此笔CICS交易";
                break;
            default:
                msg = "SBS CICS : 出现错误，请查找原因。" + requestObject.getCicsRcString();
        }
        logger.info("ECI returned: " + requestObject.getCicsRcString());
        logger.info("Abend code was " + requestObject.Abend_Code + " ");
        if (javaGatewayObject.isOpen() == true) {
            javaGatewayObject.close();
        }
        throw new Exception(msg);
    }

    /**
     * 16进制格式化输出
     *
     * @param buffer
     * @return
     */
    private String format16(byte[] buffer) {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        int n = 0;
        byte[] lineBuffer = new byte[16];
        for (byte b : buffer) {
            if (n % 16 == 0) {
                result.append(String.format("%05x: ", n));
                lineBuffer = new byte[16];
            }
            result.append(String.format("%02x ", b));
            lineBuffer[n % 16] = b;
            n++;
            if (n % 16 == 0) {
                result.append(new String(lineBuffer));
                result.append("\n");
            }

            //TODO
            if (n >= 2048) {
                result.append("报文过大，已截断...");
                break;
            }

        }
        for (int k = 0; k < (16 - n % 16); k++) {
            result.append("   ");
        }
        result.append(new String(lineBuffer));
        result.append("\n");
        return result.toString();
    }

    /**
     * @param buffer
     * @return
     */
    private byte[] truncBuffer(byte[] buffer) {
        int count = 0;
        for (int i = 0; i < iCommareaSize; i++) {
            if (buffer[iCommareaSize - 1 - i] == 0x00) {
                count++;
            } else {
                break;
            }
        }
        byte[] outBuffer = new byte[iCommareaSize - count];
        System.arraycopy(buffer, 0, outBuffer, 0, outBuffer.length);
        return outBuffer;
    }

}
