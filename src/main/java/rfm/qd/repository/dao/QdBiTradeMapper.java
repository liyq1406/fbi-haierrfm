package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdBiTrade;
import rfm.qd.repository.model.QdBiTradeExample;

public interface QdBiTradeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdBiTradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdBiTradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdBiTrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdBiTrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdBiTrade> selectByExample(QdBiTradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdBiTrade selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdBiTrade record, @Param("example") QdBiTradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdBiTrade record, @Param("example") QdBiTradeExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdBiTrade record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_TRADE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdBiTrade record);
}