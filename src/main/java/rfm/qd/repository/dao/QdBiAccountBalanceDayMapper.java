package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdBiAccountBalanceDay;
import rfm.qd.repository.model.QdBiAccountBalanceDayExample;

public interface QdBiAccountBalanceDayMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdBiAccountBalanceDayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdBiAccountBalanceDayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdBiAccountBalanceDay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdBiAccountBalanceDay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdBiAccountBalanceDay> selectByExample(QdBiAccountBalanceDayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdBiAccountBalanceDay selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdBiAccountBalanceDay record, @Param("example") QdBiAccountBalanceDayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdBiAccountBalanceDay record, @Param("example") QdBiAccountBalanceDayExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdBiAccountBalanceDay record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_ACCOUNT_BALANCE_DAY
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdBiAccountBalanceDay record);
}