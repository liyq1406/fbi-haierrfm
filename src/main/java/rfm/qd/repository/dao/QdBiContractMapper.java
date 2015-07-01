package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdBiContract;
import rfm.qd.repository.model.QdBiContractExample;

public interface QdBiContractMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdBiContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdBiContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdBiContract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdBiContract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdBiContract> selectByExample(QdBiContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdBiContract selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdBiContract record, @Param("example") QdBiContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdBiContract record, @Param("example") QdBiContractExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdBiContract record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_BI_CONTRACT
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdBiContract record);
}