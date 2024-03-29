package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdRsRefund;
import rfm.qd.repository.model.QdRsRefundExample;

public interface QdRsRefundMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdRsRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdRsRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdRsRefund record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdRsRefund record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdRsRefund> selectByExample(QdRsRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdRsRefund selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdRsRefund record, @Param("example") QdRsRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdRsRefund record, @Param("example") QdRsRefundExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdRsRefund record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_REFUND
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdRsRefund record);
}