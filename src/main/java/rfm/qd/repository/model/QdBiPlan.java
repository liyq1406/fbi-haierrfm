package rfm.qd.repository.model;

import java.math.BigDecimal;
import java.util.Date;

public class QdBiPlan {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.PK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String pkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String accountCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.ACCOUNT_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String accountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.PLAN_NO
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String planNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.SUBMIT_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String submitDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.PLAN_AMOUNT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private BigDecimal planAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.PLAN_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Integer planNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.REMARK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.NOTIFY_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String notifyId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.GET_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String getFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.GET_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Integer getNum;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.GET_MSG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String getMsg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.DELETED_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.ORIGIN_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.ORIGIN_APP
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String originApp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.CREATED_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.CREATED_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Date createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.LAST_UPD_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.LAST_UPD_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Date lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column QD_BI_PLAN.MODIFICATION_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    private Integer modificationNum;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.PK_ID
     *
     * @return the value of QD_BI_PLAN.PK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.PK_ID
     *
     * @param pkId the value for QD_BI_PLAN.PK_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.ACCOUNT_CODE
     *
     * @return the value of QD_BI_PLAN.ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getAccountCode() {
        return accountCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.ACCOUNT_CODE
     *
     * @param accountCode the value for QD_BI_PLAN.ACCOUNT_CODE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.ACCOUNT_NAME
     *
     * @return the value of QD_BI_PLAN.ACCOUNT_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.ACCOUNT_NAME
     *
     * @param accountName the value for QD_BI_PLAN.ACCOUNT_NAME
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.PLAN_NO
     *
     * @return the value of QD_BI_PLAN.PLAN_NO
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getPlanNo() {
        return planNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.PLAN_NO
     *
     * @param planNo the value for QD_BI_PLAN.PLAN_NO
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setPlanNo(String planNo) {
        this.planNo = planNo == null ? null : planNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.SUBMIT_DATE
     *
     * @return the value of QD_BI_PLAN.SUBMIT_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getSubmitDate() {
        return submitDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.SUBMIT_DATE
     *
     * @param submitDate the value for QD_BI_PLAN.SUBMIT_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate == null ? null : submitDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.PLAN_AMOUNT
     *
     * @return the value of QD_BI_PLAN.PLAN_AMOUNT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public BigDecimal getPlanAmount() {
        return planAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.PLAN_AMOUNT
     *
     * @param planAmount the value for QD_BI_PLAN.PLAN_AMOUNT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setPlanAmount(BigDecimal planAmount) {
        this.planAmount = planAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.PLAN_NUM
     *
     * @return the value of QD_BI_PLAN.PLAN_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Integer getPlanNum() {
        return planNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.PLAN_NUM
     *
     * @param planNum the value for QD_BI_PLAN.PLAN_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setPlanNum(Integer planNum) {
        this.planNum = planNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.REMARK
     *
     * @return the value of QD_BI_PLAN.REMARK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.REMARK
     *
     * @param remark the value for QD_BI_PLAN.REMARK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.NOTIFY_ID
     *
     * @return the value of QD_BI_PLAN.NOTIFY_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getNotifyId() {
        return notifyId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.NOTIFY_ID
     *
     * @param notifyId the value for QD_BI_PLAN.NOTIFY_ID
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId == null ? null : notifyId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.GET_FLAG
     *
     * @return the value of QD_BI_PLAN.GET_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getGetFlag() {
        return getFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.GET_FLAG
     *
     * @param getFlag the value for QD_BI_PLAN.GET_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setGetFlag(String getFlag) {
        this.getFlag = getFlag == null ? null : getFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.GET_NUM
     *
     * @return the value of QD_BI_PLAN.GET_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Integer getGetNum() {
        return getNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.GET_NUM
     *
     * @param getNum the value for QD_BI_PLAN.GET_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setGetNum(Integer getNum) {
        this.getNum = getNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.GET_MSG
     *
     * @return the value of QD_BI_PLAN.GET_MSG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getGetMsg() {
        return getMsg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.GET_MSG
     *
     * @param getMsg the value for QD_BI_PLAN.GET_MSG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setGetMsg(String getMsg) {
        this.getMsg = getMsg == null ? null : getMsg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.DELETED_FLAG
     *
     * @return the value of QD_BI_PLAN.DELETED_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.DELETED_FLAG
     *
     * @param deletedFlag the value for QD_BI_PLAN.DELETED_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.ORIGIN_FLAG
     *
     * @return the value of QD_BI_PLAN.ORIGIN_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.ORIGIN_FLAG
     *
     * @param originFlag the value for QD_BI_PLAN.ORIGIN_FLAG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.ORIGIN_APP
     *
     * @return the value of QD_BI_PLAN.ORIGIN_APP
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getOriginApp() {
        return originApp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.ORIGIN_APP
     *
     * @param originApp the value for QD_BI_PLAN.ORIGIN_APP
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setOriginApp(String originApp) {
        this.originApp = originApp == null ? null : originApp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.CREATED_BY
     *
     * @return the value of QD_BI_PLAN.CREATED_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.CREATED_BY
     *
     * @param createdBy the value for QD_BI_PLAN.CREATED_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.CREATED_DATE
     *
     * @return the value of QD_BI_PLAN.CREATED_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.CREATED_DATE
     *
     * @param createdDate the value for QD_BI_PLAN.CREATED_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.LAST_UPD_BY
     *
     * @return the value of QD_BI_PLAN.LAST_UPD_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.LAST_UPD_BY
     *
     * @param lastUpdBy the value for QD_BI_PLAN.LAST_UPD_BY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.LAST_UPD_DATE
     *
     * @return the value of QD_BI_PLAN.LAST_UPD_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Date getLastUpdDate() {
        return lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.LAST_UPD_DATE
     *
     * @param lastUpdDate the value for QD_BI_PLAN.LAST_UPD_DATE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setLastUpdDate(Date lastUpdDate) {
        this.lastUpdDate = lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column QD_BI_PLAN.MODIFICATION_NUM
     *
     * @return the value of QD_BI_PLAN.MODIFICATION_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column QD_BI_PLAN.MODIFICATION_NUM
     *
     * @param modificationNum the value for QD_BI_PLAN.MODIFICATION_NUM
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }
}