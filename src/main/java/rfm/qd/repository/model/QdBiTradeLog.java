package rfm.qd.repository.model;

import java.math.BigDecimal;
import java.util.Date;

public class QdBiTradeLog {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.PK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String pkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.TRADE_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String tradeCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.BILL_TYPE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String billType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.BILL_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String billId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.BANK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String bankId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.BANK_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String bankCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.ENTITY_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String entityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String accountCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.TO_ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String toAccountCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.TO_ACCOUNT_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String toAccountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.TO_BANK_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String toBankName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.TRADE_AMOUNT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private BigDecimal tradeAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.SERIAL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String serial;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.BANK_SERIAL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String bankSerial;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.TRADE_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String tradeDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.RESULT_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String resultFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.RESULT_DESC
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String resultDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.LAST_RESULT_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String lastResultFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.REQUEST_BODY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String requestBody;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.RESPONSE_BODY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String responseBody;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.OPERATOR_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String operatorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.OPERATOR_IP
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String operatorIp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.REMARK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.DELETED_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.ORIGIN_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.CREATED_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.CREATED_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Date createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.LAST_UPD_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.LAST_UPD_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Date lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_TRADE_LOG.MODIFICATION_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Integer modificationNum;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.PK_ID
     *
     * @return the value of QD_BI_TRADE_LOG.PK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.PK_ID
     *
     * @param pkId the value for QD_BI_TRADE_LOG.PK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.TRADE_CODE
     *
     * @return the value of QD_BI_TRADE_LOG.TRADE_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getTradeCode() {
        return tradeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.TRADE_CODE
     *
     * @param tradeCode the value for QD_BI_TRADE_LOG.TRADE_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode == null ? null : tradeCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.BILL_TYPE
     *
     * @return the value of QD_BI_TRADE_LOG.BILL_TYPE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getBillType() {
        return billType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.BILL_TYPE
     *
     * @param billType the value for QD_BI_TRADE_LOG.BILL_TYPE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setBillType(String billType) {
        this.billType = billType == null ? null : billType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.BILL_ID
     *
     * @return the value of QD_BI_TRADE_LOG.BILL_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getBillId() {
        return billId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.BILL_ID
     *
     * @param billId the value for QD_BI_TRADE_LOG.BILL_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setBillId(String billId) {
        this.billId = billId == null ? null : billId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.BANK_ID
     *
     * @return the value of QD_BI_TRADE_LOG.BANK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.BANK_ID
     *
     * @param bankId the value for QD_BI_TRADE_LOG.BANK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setBankId(String bankId) {
        this.bankId = bankId == null ? null : bankId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.BANK_CODE
     *
     * @return the value of QD_BI_TRADE_LOG.BANK_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.BANK_CODE
     *
     * @param bankCode the value for QD_BI_TRADE_LOG.BANK_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.ENTITY_ID
     *
     * @return the value of QD_BI_TRADE_LOG.ENTITY_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.ENTITY_ID
     *
     * @param entityId the value for QD_BI_TRADE_LOG.ENTITY_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId == null ? null : entityId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.ACCOUNT_CODE
     *
     * @return the value of QD_BI_TRADE_LOG.ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.ACCOUNT_CODE
     *
     * @param accountCode the value for QD_BI_TRADE_LOG.ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.TO_ACCOUNT_CODE
     *
     * @return the value of QD_BI_TRADE_LOG.TO_ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getToAccountCode() {
        return toAccountCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.TO_ACCOUNT_CODE
     *
     * @param toAccountCode the value for QD_BI_TRADE_LOG.TO_ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setToAccountCode(String toAccountCode) {
        this.toAccountCode = toAccountCode == null ? null : toAccountCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.TO_ACCOUNT_NAME
     *
     * @return the value of QD_BI_TRADE_LOG.TO_ACCOUNT_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getToAccountName() {
        return toAccountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.TO_ACCOUNT_NAME
     *
     * @param toAccountName the value for QD_BI_TRADE_LOG.TO_ACCOUNT_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName == null ? null : toAccountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.TO_BANK_NAME
     *
     * @return the value of QD_BI_TRADE_LOG.TO_BANK_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getToBankName() {
        return toBankName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.TO_BANK_NAME
     *
     * @param toBankName the value for QD_BI_TRADE_LOG.TO_BANK_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setToBankName(String toBankName) {
        this.toBankName = toBankName == null ? null : toBankName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.TRADE_AMOUNT
     *
     * @return the value of QD_BI_TRADE_LOG.TRADE_AMOUNT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.TRADE_AMOUNT
     *
     * @param tradeAmount the value for QD_BI_TRADE_LOG.TRADE_AMOUNT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.SERIAL
     *
     * @return the value of QD_BI_TRADE_LOG.SERIAL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getSerial() {
        return serial;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.SERIAL
     *
     * @param serial the value for QD_BI_TRADE_LOG.SERIAL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setSerial(String serial) {
        this.serial = serial == null ? null : serial.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.BANK_SERIAL
     *
     * @return the value of QD_BI_TRADE_LOG.BANK_SERIAL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getBankSerial() {
        return bankSerial;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.BANK_SERIAL
     *
     * @param bankSerial the value for QD_BI_TRADE_LOG.BANK_SERIAL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setBankSerial(String bankSerial) {
        this.bankSerial = bankSerial == null ? null : bankSerial.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.TRADE_DATE
     *
     * @return the value of QD_BI_TRADE_LOG.TRADE_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getTradeDate() {
        return tradeDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.TRADE_DATE
     *
     * @param tradeDate the value for QD_BI_TRADE_LOG.TRADE_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate == null ? null : tradeDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.RESULT_FLAG
     *
     * @return the value of QD_BI_TRADE_LOG.RESULT_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getResultFlag() {
        return resultFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.RESULT_FLAG
     *
     * @param resultFlag the value for QD_BI_TRADE_LOG.RESULT_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag == null ? null : resultFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.RESULT_DESC
     *
     * @return the value of QD_BI_TRADE_LOG.RESULT_DESC
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getResultDesc() {
        return resultDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.RESULT_DESC
     *
     * @param resultDesc the value for QD_BI_TRADE_LOG.RESULT_DESC
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc == null ? null : resultDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.LAST_RESULT_FLAG
     *
     * @return the value of QD_BI_TRADE_LOG.LAST_RESULT_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getLastResultFlag() {
        return lastResultFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.LAST_RESULT_FLAG
     *
     * @param lastResultFlag the value for QD_BI_TRADE_LOG.LAST_RESULT_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setLastResultFlag(String lastResultFlag) {
        this.lastResultFlag = lastResultFlag == null ? null : lastResultFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.REQUEST_BODY
     *
     * @return the value of QD_BI_TRADE_LOG.REQUEST_BODY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getRequestBody() {
        return requestBody;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.REQUEST_BODY
     *
     * @param requestBody the value for QD_BI_TRADE_LOG.REQUEST_BODY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody == null ? null : requestBody.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.RESPONSE_BODY
     *
     * @return the value of QD_BI_TRADE_LOG.RESPONSE_BODY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.RESPONSE_BODY
     *
     * @param responseBody the value for QD_BI_TRADE_LOG.RESPONSE_BODY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody == null ? null : responseBody.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.OPERATOR_ID
     *
     * @return the value of QD_BI_TRADE_LOG.OPERATOR_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getOperatorId() {
        return operatorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.OPERATOR_ID
     *
     * @param operatorId the value for QD_BI_TRADE_LOG.OPERATOR_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.OPERATOR_IP
     *
     * @return the value of QD_BI_TRADE_LOG.OPERATOR_IP
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getOperatorIp() {
        return operatorIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.OPERATOR_IP
     *
     * @param operatorIp the value for QD_BI_TRADE_LOG.OPERATOR_IP
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setOperatorIp(String operatorIp) {
        this.operatorIp = operatorIp == null ? null : operatorIp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.REMARK
     *
     * @return the value of QD_BI_TRADE_LOG.REMARK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.REMARK
     *
     * @param remark the value for QD_BI_TRADE_LOG.REMARK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.DELETED_FLAG
     *
     * @return the value of QD_BI_TRADE_LOG.DELETED_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.DELETED_FLAG
     *
     * @param deletedFlag the value for QD_BI_TRADE_LOG.DELETED_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.ORIGIN_FLAG
     *
     * @return the value of QD_BI_TRADE_LOG.ORIGIN_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.ORIGIN_FLAG
     *
     * @param originFlag the value for QD_BI_TRADE_LOG.ORIGIN_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.CREATED_BY
     *
     * @return the value of QD_BI_TRADE_LOG.CREATED_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.CREATED_BY
     *
     * @param createdBy the value for QD_BI_TRADE_LOG.CREATED_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.CREATED_DATE
     *
     * @return the value of QD_BI_TRADE_LOG.CREATED_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.CREATED_DATE
     *
     * @param createdDate the value for QD_BI_TRADE_LOG.CREATED_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.LAST_UPD_BY
     *
     * @return the value of QD_BI_TRADE_LOG.LAST_UPD_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.LAST_UPD_BY
     *
     * @param lastUpdBy the value for QD_BI_TRADE_LOG.LAST_UPD_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.LAST_UPD_DATE
     *
     * @return the value of QD_BI_TRADE_LOG.LAST_UPD_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Date getLastUpdDate() {
        return lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.LAST_UPD_DATE
     *
     * @param lastUpdDate the value for QD_BI_TRADE_LOG.LAST_UPD_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setLastUpdDate(Date lastUpdDate) {
        this.lastUpdDate = lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_TRADE_LOG.MODIFICATION_NUM
     *
     * @return the value of QD_BI_TRADE_LOG.MODIFICATION_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_TRADE_LOG.MODIFICATION_NUM
     *
     * @param modificationNum the value for QD_BI_TRADE_LOG.MODIFICATION_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }
}