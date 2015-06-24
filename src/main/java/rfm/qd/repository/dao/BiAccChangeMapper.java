package rfm.qd.repository.dao;

import rfm.qd.repository.model.BiAccChange;
import rfm.qd.repository.model.BiAccChangeExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BiAccChangeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int countByExample(BiAccChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int deleteByExample(BiAccChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int insert(BiAccChange record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int insertSelective(BiAccChange record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    List<BiAccChange> selectByExample(BiAccChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    BiAccChange selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int updateByExampleSelective(@Param("record") BiAccChange record, @Param("example") BiAccChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int updateByExample(@Param("record") BiAccChange record, @Param("example") BiAccChangeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int updateByPrimaryKeySelective(BiAccChange record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.BI_ACC_CHANGE
     *
     * @mbggenerated Wed Sep 07 18:28:59 CST 2011
     */
    int updateByPrimaryKey(BiAccChange record);
}