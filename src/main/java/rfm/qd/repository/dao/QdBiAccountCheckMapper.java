package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdBiAccountCheck;
import rfm.qd.repository.model.QdBiAccountCheckExample;

public interface QdBiAccountCheckMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdBiAccountCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdBiAccountCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdBiAccountCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdBiAccountCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdBiAccountCheck> selectByExample(QdBiAccountCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdBiAccountCheck selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdBiAccountCheck record, @Param("example") QdBiAccountCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdBiAccountCheck record, @Param("example") QdBiAccountCheckExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdBiAccountCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_CHECK
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdBiAccountCheck record);
}