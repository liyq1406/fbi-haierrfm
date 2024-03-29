package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdBiNotifyMessage;
import rfm.qd.repository.model.QdBiNotifyMessageExample;

public interface QdBiNotifyMessageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdBiNotifyMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdBiNotifyMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdBiNotifyMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdBiNotifyMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdBiNotifyMessage> selectByExample(QdBiNotifyMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdBiNotifyMessage selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdBiNotifyMessage record, @Param("example") QdBiNotifyMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdBiNotifyMessage record, @Param("example") QdBiNotifyMessageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdBiNotifyMessage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_NOTIFY_MESSAGE
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdBiNotifyMessage record);
}