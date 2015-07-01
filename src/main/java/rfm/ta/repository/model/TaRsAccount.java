package rfm.ta.repository.model;

public class TaRsAccount {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.PK_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String pkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.ORIGIN_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String originId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.ACC_TYPE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String accType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.ACC_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String accId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.ACC_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String accName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.PRE_SALE_PER_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String preSalePerName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.PRE_SALE_PRO_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String preSaleProName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.PRE_SALE_PRO_ADDR
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String preSaleProAddr;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.START_DATE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String startDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.END_DATE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String endDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.STATUS_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String statusFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.DELETED_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.ORIGIN_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.ORIGIN_APP
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String originApp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.CREATED_BY
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.CREATED_TIME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String createdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.LAST_UPD_BY
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.LAST_UPD_TIME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String lastUpdTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.REC_VERSION
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private Integer recVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column TA_RS_ACCOUNT.SEND_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    private String sendFlag;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.PK_ID
     *
     * @return the value of TA_RS_ACCOUNT.PK_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.PK_ID
     *
     * @param pkId the value for TA_RS_ACCOUNT.PK_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.ORIGIN_ID
     *
     * @return the value of TA_RS_ACCOUNT.ORIGIN_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getOriginId() {
        return originId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.ORIGIN_ID
     *
     * @param originId the value for TA_RS_ACCOUNT.ORIGIN_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setOriginId(String originId) {
        this.originId = originId == null ? null : originId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.ACC_TYPE
     *
     * @return the value of TA_RS_ACCOUNT.ACC_TYPE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getAccType() {
        return accType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.ACC_TYPE
     *
     * @param accType the value for TA_RS_ACCOUNT.ACC_TYPE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setAccType(String accType) {
        this.accType = accType == null ? null : accType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.ACC_ID
     *
     * @return the value of TA_RS_ACCOUNT.ACC_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getAccId() {
        return accId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.ACC_ID
     *
     * @param accId the value for TA_RS_ACCOUNT.ACC_ID
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setAccId(String accId) {
        this.accId = accId == null ? null : accId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.ACC_NAME
     *
     * @return the value of TA_RS_ACCOUNT.ACC_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getAccName() {
        return accName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.ACC_NAME
     *
     * @param accName the value for TA_RS_ACCOUNT.ACC_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setAccName(String accName) {
        this.accName = accName == null ? null : accName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.PRE_SALE_PER_NAME
     *
     * @return the value of TA_RS_ACCOUNT.PRE_SALE_PER_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getPreSalePerName() {
        return preSalePerName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.PRE_SALE_PER_NAME
     *
     * @param preSalePerName the value for TA_RS_ACCOUNT.PRE_SALE_PER_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setPreSalePerName(String preSalePerName) {
        this.preSalePerName = preSalePerName == null ? null : preSalePerName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.PRE_SALE_PRO_NAME
     *
     * @return the value of TA_RS_ACCOUNT.PRE_SALE_PRO_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getPreSaleProName() {
        return preSaleProName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.PRE_SALE_PRO_NAME
     *
     * @param preSaleProName the value for TA_RS_ACCOUNT.PRE_SALE_PRO_NAME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setPreSaleProName(String preSaleProName) {
        this.preSaleProName = preSaleProName == null ? null : preSaleProName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.PRE_SALE_PRO_ADDR
     *
     * @return the value of TA_RS_ACCOUNT.PRE_SALE_PRO_ADDR
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getPreSaleProAddr() {
        return preSaleProAddr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.PRE_SALE_PRO_ADDR
     *
     * @param preSaleProAddr the value for TA_RS_ACCOUNT.PRE_SALE_PRO_ADDR
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setPreSaleProAddr(String preSaleProAddr) {
        this.preSaleProAddr = preSaleProAddr == null ? null : preSaleProAddr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.START_DATE
     *
     * @return the value of TA_RS_ACCOUNT.START_DATE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.START_DATE
     *
     * @param startDate the value for TA_RS_ACCOUNT.START_DATE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate == null ? null : startDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.END_DATE
     *
     * @return the value of TA_RS_ACCOUNT.END_DATE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.END_DATE
     *
     * @param endDate the value for TA_RS_ACCOUNT.END_DATE
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate == null ? null : endDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.STATUS_FLAG
     *
     * @return the value of TA_RS_ACCOUNT.STATUS_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getStatusFlag() {
        return statusFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.STATUS_FLAG
     *
     * @param statusFlag the value for TA_RS_ACCOUNT.STATUS_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setStatusFlag(String statusFlag) {
        this.statusFlag = statusFlag == null ? null : statusFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.DELETED_FLAG
     *
     * @return the value of TA_RS_ACCOUNT.DELETED_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.DELETED_FLAG
     *
     * @param deletedFlag the value for TA_RS_ACCOUNT.DELETED_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.ORIGIN_FLAG
     *
     * @return the value of TA_RS_ACCOUNT.ORIGIN_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.ORIGIN_FLAG
     *
     * @param originFlag the value for TA_RS_ACCOUNT.ORIGIN_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.ORIGIN_APP
     *
     * @return the value of TA_RS_ACCOUNT.ORIGIN_APP
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getOriginApp() {
        return originApp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.ORIGIN_APP
     *
     * @param originApp the value for TA_RS_ACCOUNT.ORIGIN_APP
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setOriginApp(String originApp) {
        this.originApp = originApp == null ? null : originApp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.CREATED_BY
     *
     * @return the value of TA_RS_ACCOUNT.CREATED_BY
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.CREATED_BY
     *
     * @param createdBy the value for TA_RS_ACCOUNT.CREATED_BY
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.CREATED_TIME
     *
     * @return the value of TA_RS_ACCOUNT.CREATED_TIME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getCreatedTime() {
        return createdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.CREATED_TIME
     *
     * @param createdTime the value for TA_RS_ACCOUNT.CREATED_TIME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime == null ? null : createdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.LAST_UPD_BY
     *
     * @return the value of TA_RS_ACCOUNT.LAST_UPD_BY
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.LAST_UPD_BY
     *
     * @param lastUpdBy the value for TA_RS_ACCOUNT.LAST_UPD_BY
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.LAST_UPD_TIME
     *
     * @return the value of TA_RS_ACCOUNT.LAST_UPD_TIME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getLastUpdTime() {
        return lastUpdTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.LAST_UPD_TIME
     *
     * @param lastUpdTime the value for TA_RS_ACCOUNT.LAST_UPD_TIME
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setLastUpdTime(String lastUpdTime) {
        this.lastUpdTime = lastUpdTime == null ? null : lastUpdTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.REC_VERSION
     *
     * @return the value of TA_RS_ACCOUNT.REC_VERSION
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public Integer getRecVersion() {
        return recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.REC_VERSION
     *
     * @param recVersion the value for TA_RS_ACCOUNT.REC_VERSION
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setRecVersion(Integer recVersion) {
        this.recVersion = recVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column TA_RS_ACCOUNT.SEND_FLAG
     *
     * @return the value of TA_RS_ACCOUNT.SEND_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public String getSendFlag() {
        return sendFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column TA_RS_ACCOUNT.SEND_FLAG
     *
     * @param sendFlag the value for TA_RS_ACCOUNT.SEND_FLAG
     *
     * @mbggenerated Mon Jun 29 15:22:34 CST 2015
     */
    public void setSendFlag(String sendFlag) {
        this.sendFlag = sendFlag == null ? null : sendFlag.trim();
    }
}