package rfm.ta.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.ta.repository.model.TaRsAcc;
import rfm.ta.repository.model.TaRsAccExample;

public interface TaRsAccMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int countByExample(TaRsAccExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int deleteByExample(TaRsAccExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int insert(TaRsAcc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int insertSelective(TaRsAcc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    List<TaRsAcc> selectByExample(TaRsAccExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    TaRsAcc selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int updateByExampleSelective(@Param("record") TaRsAcc record, @Param("example") TaRsAccExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int updateByExample(@Param("record") TaRsAcc record, @Param("example") TaRsAccExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int updateByPrimaryKeySelective(TaRsAcc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_RS_ACC
     *
     * @mbggenerated Thu Aug 27 08:29:26 CST 2015
     */
    int updateByPrimaryKey(TaRsAcc record);
}