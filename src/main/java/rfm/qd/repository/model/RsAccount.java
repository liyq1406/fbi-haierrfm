package rfm.qd.repository.model;

import java.math.BigDecimal;
import java.util.Date;

public class RsAccount {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.PK_ID
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String pkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.COMPANY_ID
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String companyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.ACCOUNT_CODE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String accountCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.ACCOUNT_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String accountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.PRO_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String proName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.PRO_ADDR
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String proAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.PRESELL_NO
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String presellNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.START_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String startDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.END_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String endDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.BALANCE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private BigDecimal balance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.BALANCE_USABLE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private BigDecimal balanceUsable;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.BALANCE_LOCK
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private BigDecimal balanceLock;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.STATUS_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String statusFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.LIMIT_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String limitFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.DELETED_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.ORIGIN_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.ORIGIN_APP
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String originApp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.CREATED_BY
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.CREATED_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private Date createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.LAST_UPD_BY
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.LAST_UPD_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private Date lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.MODIFICATION_NUM
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private Integer modificationNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.STATUS_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String statusName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.AGRNUM
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String agrnum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.RS_ACCOUNT.SEND_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    private String sendFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.PK_ID
     *
     * @return the value of BRZFDC.RS_ACCOUNT.PK_ID
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.PK_ID
     *
     * @param pkId the value for BRZFDC.RS_ACCOUNT.PK_ID
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.COMPANY_ID
     *
     * @return the value of BRZFDC.RS_ACCOUNT.COMPANY_ID
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.COMPANY_ID
     *
     * @param companyId the value for BRZFDC.RS_ACCOUNT.COMPANY_ID
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId == null ? null : companyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.ACCOUNT_CODE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.ACCOUNT_CODE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.ACCOUNT_CODE
     *
     * @param accountCode the value for BRZFDC.RS_ACCOUNT.ACCOUNT_CODE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.ACCOUNT_NAME
     *
     * @return the value of BRZFDC.RS_ACCOUNT.ACCOUNT_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.ACCOUNT_NAME
     *
     * @param accountName the value for BRZFDC.RS_ACCOUNT.ACCOUNT_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.PRO_NAME
     *
     * @return the value of BRZFDC.RS_ACCOUNT.PRO_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getProName() {
        return proName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.PRO_NAME
     *
     * @param proName the value for BRZFDC.RS_ACCOUNT.PRO_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.PRO_ADDR
     *
     * @return the value of BRZFDC.RS_ACCOUNT.PRO_ADDR
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getProAddr() {
        return proAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.PRO_ADDR
     *
     * @param proAddr the value for BRZFDC.RS_ACCOUNT.PRO_ADDR
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setProAddr(String proAddr) {
        this.proAddr = proAddr == null ? null : proAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.PRESELL_NO
     *
     * @return the value of BRZFDC.RS_ACCOUNT.PRESELL_NO
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getPresellNo() {
        return presellNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.PRESELL_NO
     *
     * @param presellNo the value for BRZFDC.RS_ACCOUNT.PRESELL_NO
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setPresellNo(String presellNo) {
        this.presellNo = presellNo == null ? null : presellNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.START_DATE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.START_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.START_DATE
     *
     * @param startDate the value for BRZFDC.RS_ACCOUNT.START_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? null : startDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.END_DATE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.END_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.END_DATE
     *
     * @param endDate the value for BRZFDC.RS_ACCOUNT.END_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.BALANCE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.BALANCE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.BALANCE
     *
     * @param balance the value for BRZFDC.RS_ACCOUNT.BALANCE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.BALANCE_USABLE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.BALANCE_USABLE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public BigDecimal getBalanceUsable() {
        return balanceUsable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.BALANCE_USABLE
     *
     * @param balanceUsable the value for BRZFDC.RS_ACCOUNT.BALANCE_USABLE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setBalanceUsable(BigDecimal balanceUsable) {
        this.balanceUsable = balanceUsable;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.BALANCE_LOCK
     *
     * @return the value of BRZFDC.RS_ACCOUNT.BALANCE_LOCK
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public BigDecimal getBalanceLock() {
        return balanceLock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.BALANCE_LOCK
     *
     * @param balanceLock the value for BRZFDC.RS_ACCOUNT.BALANCE_LOCK
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setBalanceLock(BigDecimal balanceLock) {
        this.balanceLock = balanceLock;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.STATUS_FLAG
     *
     * @return the value of BRZFDC.RS_ACCOUNT.STATUS_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getStatusFlag() {
        return statusFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.STATUS_FLAG
     *
     * @param statusFlag the value for BRZFDC.RS_ACCOUNT.STATUS_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag == null ? null : statusFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.LIMIT_FLAG
     *
     * @return the value of BRZFDC.RS_ACCOUNT.LIMIT_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getLimitFlag() {
        return limitFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.LIMIT_FLAG
     *
     * @param limitFlag the value for BRZFDC.RS_ACCOUNT.LIMIT_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setLimitFlag(String limitFlag) {
        this.limitFlag = limitFlag == null ? null : limitFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.DELETED_FLAG
     *
     * @return the value of BRZFDC.RS_ACCOUNT.DELETED_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.DELETED_FLAG
     *
     * @param deletedFlag the value for BRZFDC.RS_ACCOUNT.DELETED_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.ORIGIN_FLAG
     *
     * @return the value of BRZFDC.RS_ACCOUNT.ORIGIN_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.ORIGIN_FLAG
     *
     * @param originFlag the value for BRZFDC.RS_ACCOUNT.ORIGIN_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.ORIGIN_APP
     *
     * @return the value of BRZFDC.RS_ACCOUNT.ORIGIN_APP
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getOriginApp() {
        return originApp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.ORIGIN_APP
     *
     * @param originApp the value for BRZFDC.RS_ACCOUNT.ORIGIN_APP
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setOriginApp(String originApp) {
        this.originApp = originApp == null ? null : originApp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.CREATED_BY
     *
     * @return the value of BRZFDC.RS_ACCOUNT.CREATED_BY
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.CREATED_BY
     *
     * @param createdBy the value for BRZFDC.RS_ACCOUNT.CREATED_BY
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.CREATED_DATE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.CREATED_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.CREATED_DATE
     *
     * @param createdDate the value for BRZFDC.RS_ACCOUNT.CREATED_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.LAST_UPD_BY
     *
     * @return the value of BRZFDC.RS_ACCOUNT.LAST_UPD_BY
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.LAST_UPD_BY
     *
     * @param lastUpdBy the value for BRZFDC.RS_ACCOUNT.LAST_UPD_BY
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.LAST_UPD_DATE
     *
     * @return the value of BRZFDC.RS_ACCOUNT.LAST_UPD_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public Date getLastUpdDate() {
        return lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.LAST_UPD_DATE
     *
     * @param lastUpdDate the value for BRZFDC.RS_ACCOUNT.LAST_UPD_DATE
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setLastUpdDate(Date lastUpdDate) {
        this.lastUpdDate = lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.MODIFICATION_NUM
     *
     * @return the value of BRZFDC.RS_ACCOUNT.MODIFICATION_NUM
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.MODIFICATION_NUM
     *
     * @param modificationNum the value for BRZFDC.RS_ACCOUNT.MODIFICATION_NUM
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.STATUS_NAME
     *
     * @return the value of BRZFDC.RS_ACCOUNT.STATUS_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.STATUS_NAME
     *
     * @param statusName the value for BRZFDC.RS_ACCOUNT.STATUS_NAME
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName == null ? null : statusName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.AGRNUM
     *
     * @return the value of BRZFDC.RS_ACCOUNT.AGRNUM
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getAgrnum() {
        return agrnum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.AGRNUM
     *
     * @param agrnum the value for BRZFDC.RS_ACCOUNT.AGRNUM
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setAgrnum(String agrnum) {
        this.agrnum = agrnum == null ? null : agrnum.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.RS_ACCOUNT.SEND_FLAG
     *
     * @return the value of BRZFDC.RS_ACCOUNT.SEND_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public String getSendFlag() {
        return sendFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.RS_ACCOUNT.SEND_FLAG
     *
     * @param sendFlag the value for BRZFDC.RS_ACCOUNT.SEND_FLAG
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag == null ? null : sendFlag.trim();
    }
}