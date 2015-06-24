package rfm.qd.repository.model;

import java.math.BigDecimal;
import java.util.Date;

public class RsLockedaccDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.PK_ID
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String pkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_CODE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String accountCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_NAME
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String accountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private BigDecimal balance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE_LOCK
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private BigDecimal balanceLock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.STATUS_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String statusFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.DELETED_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_BY
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_DATE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private Date createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.MODIFICATION_NUM
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private Integer modificationNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_LOCKEDACC_DETAIL.SEND_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    private String sendFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.PK_ID
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.PK_ID
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.PK_ID
     *
     * @param pkId the value for BRZFDC.RS_LOCKEDACC_DETAIL.PK_ID
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_CODE
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_CODE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_CODE
     *
     * @param accountCode the value for BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_CODE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_NAME
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_NAME
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_NAME
     *
     * @param accountName the value for BRZFDC.RS_LOCKEDACC_DETAIL.ACCOUNT_NAME
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE
     *
     * @param balance the value for BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE_LOCK
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE_LOCK
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public BigDecimal getBalanceLock() {
        return balanceLock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE_LOCK
     *
     * @param balanceLock the value for BRZFDC.RS_LOCKEDACC_DETAIL.BALANCE_LOCK
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setBalanceLock(BigDecimal balanceLock) {
        this.balanceLock = balanceLock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.STATUS_FLAG
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.STATUS_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getStatusFlag() {
        return statusFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.STATUS_FLAG
     *
     * @param statusFlag the value for BRZFDC.RS_LOCKEDACC_DETAIL.STATUS_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag == null ? null : statusFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.DELETED_FLAG
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.DELETED_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.DELETED_FLAG
     *
     * @param deletedFlag the value for BRZFDC.RS_LOCKEDACC_DETAIL.DELETED_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_BY
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_BY
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_BY
     *
     * @param createdBy the value for BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_BY
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_DATE
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_DATE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_DATE
     *
     * @param createdDate the value for BRZFDC.RS_LOCKEDACC_DETAIL.CREATED_DATE
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.MODIFICATION_NUM
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.MODIFICATION_NUM
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.MODIFICATION_NUM
     *
     * @param modificationNum the value for BRZFDC.RS_LOCKEDACC_DETAIL.MODIFICATION_NUM
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.SEND_FLAG
     *
     * @return the value of BRZFDC.RS_LOCKEDACC_DETAIL.SEND_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public String getSendFlag() {
        return sendFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_LOCKEDACC_DETAIL.SEND_FLAG
     *
     * @param sendFlag the value for BRZFDC.RS_LOCKEDACC_DETAIL.SEND_FLAG
     *
     * @mbggenerated Mon Sep 05 15:31:05 CST 2011
     */
    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag == null ? null : sendFlag.trim();
    }
}