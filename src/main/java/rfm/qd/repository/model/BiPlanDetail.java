package rfm.qd.repository.model;

import java.math.BigDecimal;
import java.util.Date;

public class BiPlanDetail {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.PK_ID
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String pkId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.PLAN_ID
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String planId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.PLAN_CTRL_NO
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String planCtrlNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_CODE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String toAccountCode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_NAME
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String toAccountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.TO_HS_BANK_NAME
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String toHsBankName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.PLAN_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String planDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.PLAN_DESC
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String planDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.PL_AMOUNT
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private BigDecimal plAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.REMARK
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String remark;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.DELETED_FLAG
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String deletedFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.ORIGIN_FLAG
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String originFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.ORIGIN_APP
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String originApp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.CREATED_BY
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String createdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.CREATED_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private Date createdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.LAST_UPD_BY
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private String lastUpdBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.LAST_UPD_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private Date lastUpdDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column BRZFDC.BI_PLAN_DETAIL.MODIFICATION_NUM
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    private Integer modificationNum;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.PK_ID
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.PK_ID
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.PK_ID
     *
     * @param pkId the value for BRZFDC.BI_PLAN_DETAIL.PK_ID
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setPkId(String pkId) {
        this.pkId = pkId == null ? null : pkId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_ID
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.PLAN_ID
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getPlanId() {
        return planId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_ID
     *
     * @param planId the value for BRZFDC.BI_PLAN_DETAIL.PLAN_ID
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setPlanId(String planId) {
        this.planId = planId == null ? null : planId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_CTRL_NO
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.PLAN_CTRL_NO
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getPlanCtrlNo() {
        return planCtrlNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_CTRL_NO
     *
     * @param planCtrlNo the value for BRZFDC.BI_PLAN_DETAIL.PLAN_CTRL_NO
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setPlanCtrlNo(String planCtrlNo) {
        this.planCtrlNo = planCtrlNo == null ? null : planCtrlNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_CODE
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_CODE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getToAccountCode() {
        return toAccountCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_CODE
     *
     * @param toAccountCode the value for BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_CODE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setToAccountCode(String toAccountCode) {
        this.toAccountCode = toAccountCode == null ? null : toAccountCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_NAME
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_NAME
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getToAccountName() {
        return toAccountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_NAME
     *
     * @param toAccountName the value for BRZFDC.BI_PLAN_DETAIL.TO_ACCOUNT_NAME
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName == null ? null : toAccountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.TO_HS_BANK_NAME
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.TO_HS_BANK_NAME
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getToHsBankName() {
        return toHsBankName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.TO_HS_BANK_NAME
     *
     * @param toHsBankName the value for BRZFDC.BI_PLAN_DETAIL.TO_HS_BANK_NAME
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setToHsBankName(String toHsBankName) {
        this.toHsBankName = toHsBankName == null ? null : toHsBankName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_DATE
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.PLAN_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getPlanDate() {
        return planDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_DATE
     *
     * @param planDate the value for BRZFDC.BI_PLAN_DETAIL.PLAN_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setPlanDate(String planDate) {
        this.planDate = planDate == null ? null : planDate.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_DESC
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.PLAN_DESC
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getPlanDesc() {
        return planDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.PLAN_DESC
     *
     * @param planDesc the value for BRZFDC.BI_PLAN_DETAIL.PLAN_DESC
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setPlanDesc(String planDesc) {
        this.planDesc = planDesc == null ? null : planDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.PL_AMOUNT
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.PL_AMOUNT
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public BigDecimal getPlAmount() {
        return plAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.PL_AMOUNT
     *
     * @param plAmount the value for BRZFDC.BI_PLAN_DETAIL.PL_AMOUNT
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setPlAmount(BigDecimal plAmount) {
        this.plAmount = plAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.REMARK
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.REMARK
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.REMARK
     *
     * @param remark the value for BRZFDC.BI_PLAN_DETAIL.REMARK
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.DELETED_FLAG
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.DELETED_FLAG
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getDeletedFlag() {
        return deletedFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.DELETED_FLAG
     *
     * @param deletedFlag the value for BRZFDC.BI_PLAN_DETAIL.DELETED_FLAG
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setDeletedFlag(String deletedFlag) {
        this.deletedFlag = deletedFlag == null ? null : deletedFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.ORIGIN_FLAG
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.ORIGIN_FLAG
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getOriginFlag() {
        return originFlag;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.ORIGIN_FLAG
     *
     * @param originFlag the value for BRZFDC.BI_PLAN_DETAIL.ORIGIN_FLAG
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setOriginFlag(String originFlag) {
        this.originFlag = originFlag == null ? null : originFlag.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.ORIGIN_APP
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.ORIGIN_APP
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getOriginApp() {
        return originApp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.ORIGIN_APP
     *
     * @param originApp the value for BRZFDC.BI_PLAN_DETAIL.ORIGIN_APP
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setOriginApp(String originApp) {
        this.originApp = originApp == null ? null : originApp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.CREATED_BY
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.CREATED_BY
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.CREATED_BY
     *
     * @param createdBy the value for BRZFDC.BI_PLAN_DETAIL.CREATED_BY
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy == null ? null : createdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.CREATED_DATE
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.CREATED_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.CREATED_DATE
     *
     * @param createdDate the value for BRZFDC.BI_PLAN_DETAIL.CREATED_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.LAST_UPD_BY
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.LAST_UPD_BY
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public String getLastUpdBy() {
        return lastUpdBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.LAST_UPD_BY
     *
     * @param lastUpdBy the value for BRZFDC.BI_PLAN_DETAIL.LAST_UPD_BY
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setLastUpdBy(String lastUpdBy) {
        this.lastUpdBy = lastUpdBy == null ? null : lastUpdBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.LAST_UPD_DATE
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.LAST_UPD_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public Date getLastUpdDate() {
        return lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.LAST_UPD_DATE
     *
     * @param lastUpdDate the value for BRZFDC.BI_PLAN_DETAIL.LAST_UPD_DATE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setLastUpdDate(Date lastUpdDate) {
        this.lastUpdDate = lastUpdDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column BRZFDC.BI_PLAN_DETAIL.MODIFICATION_NUM
     *
     * @return the value of BRZFDC.BI_PLAN_DETAIL.MODIFICATION_NUM
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public Integer getModificationNum() {
        return modificationNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column BRZFDC.BI_PLAN_DETAIL.MODIFICATION_NUM
     *
     * @param modificationNum the value for BRZFDC.BI_PLAN_DETAIL.MODIFICATION_NUM
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    public void setModificationNum(Integer modificationNum) {
        this.modificationNum = modificationNum;
    }
}