package rfm.ta.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.ta.repository.model.TaRsAccDtl;
import rfm.ta.repository.model.TaRsAccDtlExample;

public interface TaRsAccDtlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int countByExample(TaRsAccDtlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int deleteByExample(TaRsAccDtlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int insert(TaRsAccDtl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int insertSelective(TaRsAccDtl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    List<TaRsAccDtl> selectByExample(TaRsAccDtlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    TaRsAccDtl selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int updateByExampleSelective(@Param("record") TaRsAccDtl record, @Param("example") TaRsAccDtlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int updateByExample(@Param("record") TaRsAccDtl record, @Param("example") TaRsAccDtlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int updateByPrimaryKeySelective(TaRsAccDtl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC_DTL
     *
     * @mbggenerated Sun Sep 06 17:38:14 CST 2015
     */
    int updateByPrimaryKey(TaRsAccDtl record);
}