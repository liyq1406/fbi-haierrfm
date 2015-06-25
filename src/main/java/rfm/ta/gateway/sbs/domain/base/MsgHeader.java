package rfm.ta.gateway.sbs.domain.base;

import org.apache.commons.lang.StringUtils;
import pub.platform.advance.utils.PropertyManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MsgHeader extends AbstractFrameMsg {
    {
        offset = 0;
        fieldTypes = new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        fieldLengths = new int[]{4, 6, 9, 12, 6, 8, 6, 2, 2, 4};
    }

    private String msgType = "0200";    // ��Ϣ����	4
    private String txnCode;    // ������	    6
    private String bankId = "810201011";     // ������	9
    private String operId = PropertyManager.getProperty("brzfdc.wrd.default.operid");     // ����Ա���	12	Ա����
    private String serialNo;   //  ����Ա��ˮ��	6	����Ψһ
    private String txnDate = new SimpleDateFormat("yyyyMMdd").format(new Date());    // ��������	8
    private String txnTime = new SimpleDateFormat("HHmmss").format(new Date());    // ����ʱ��	6
    private String channel = "08";    // ����	    2
    private String rtnCode = "00";    // ��Ӧ��	    2
    private String dataLength; // �������ĳ���	4	�������ĵĳ���

    public String toString() {
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(msgType);
        dataBuilder.append(txnCode);
        dataBuilder.append(bankId);
        dataBuilder.append(StringUtils.rightPad(operId, 12, ' '));
        dataBuilder.append(serialNo);
        dataBuilder.append(txnDate);
        dataBuilder.append(txnTime);
        dataBuilder.append(channel);
        dataBuilder.append(rtnCode);
        dataBuilder.append(StringUtils.leftPad(dataLength, 4, "0"));
        return dataBuilder.toString();
    }

    // ----------------------------------------------

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDataLength() {
        return dataLength;
    }

    public void setDataLength(String dataLength) {
        this.dataLength = dataLength;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTxnCode() {
        return txnCode;
    }

    public void setTxnCode(String txnCode) {
        this.txnCode = txnCode;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }
}
