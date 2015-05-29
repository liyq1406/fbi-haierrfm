package rfm.qd.repository.dao;

import rfm.qd.repository.model.RsAccDetail;
import rfm.qd.repository.model.RsAccDetailExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RsAccDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int countByExample(RsAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int deleteByExample(RsAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int insert(RsAccDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int insertSelective(RsAccDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    List<RsAccDetail> selectByExample(RsAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    RsAccDetail selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByExampleSelective(@Param("record") RsAccDetail record, @Param("example") RsAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByExample(@Param("record") RsAccDetail record, @Param("example") RsAccDetailExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByPrimaryKeySelective(RsAccDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_ACC_DETAIL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByPrimaryKey(RsAccDetail record);
}