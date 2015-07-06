package rfm.ta.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.ta.repository.model.TaTxnFdc;
import rfm.ta.repository.model.TaTxnFdcExample;

public interface TaTxnFdcMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    int countByExample(TaTxnFdcExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    int deleteByExample(TaTxnFdcExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    int insert(TaTxnFdc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    int insertSelective(TaTxnFdc record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    List<TaTxnFdc> selectByExample(TaTxnFdcExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    int updateByExampleSelective(@Param("record") TaTxnFdc record, @Param("example") TaTxnFdcExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table TA_TXN_FDC
     *
     * @mbggenerated Thu Jul 02 18:27:17 CST 2015
     */
    int updateByExample(@Param("record") TaTxnFdc record, @Param("example") TaTxnFdcExample example);
}