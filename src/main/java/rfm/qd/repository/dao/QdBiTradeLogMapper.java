package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdBiTradeLog;
import rfm.qd.repository.model.QdBiTradeLogExample;

public interface QdBiTradeLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdBiTradeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdBiTradeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdBiTradeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdBiTradeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdBiTradeLog> selectByExample(QdBiTradeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdBiTradeLog selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdBiTradeLog record, @Param("example") QdBiTradeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdBiTradeLog record, @Param("example") QdBiTradeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdBiTradeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE_LOG
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdBiTradeLog record);
}