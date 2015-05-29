package rfm.qd.repository.dao;

import rfm.qd.repository.model.CbsAccTxn;
import rfm.qd.repository.model.CbsAccTxnExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CbsAccTxnMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int countByExample(CbsAccTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int deleteByExample(CbsAccTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int deleteByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int insert(CbsAccTxn record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int insertSelective(CbsAccTxn record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    List<CbsAccTxn> selectByExample(CbsAccTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    CbsAccTxn selectByPrimaryKey(String pkid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int updateByExampleSelective(@Param("record") CbsAccTxn record, @Param("example") CbsAccTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int updateByExample(@Param("record") CbsAccTxn record, @Param("example") CbsAccTxnExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int updateByPrimaryKeySelective(CbsAccTxn record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.CBS_ACC_TXN
     *
     * @mbggenerated Thu May 03 10:32:26 CST 2012
     */
    int updateByPrimaryKey(CbsAccTxn record);
}