package rfm.qd.repository.dao;

import rfm.qd.repository.model.RsPlanCtrl;
import rfm.qd.repository.model.RsPlanCtrlExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RsPlanCtrlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int countByExample(RsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int deleteByExample(RsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int insert(RsPlanCtrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int insertSelective(RsPlanCtrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    List<RsPlanCtrl> selectByExample(RsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    RsPlanCtrl selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByExampleSelective(@Param("record") RsPlanCtrl record, @Param("example") RsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByExample(@Param("record") RsPlanCtrl record, @Param("example") RsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByPrimaryKeySelective(RsPlanCtrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table BRZFDC.RS_PLAN_CTRL
     *
     * @mbggenerated Wed Sep 07 18:29:53 CST 2011
     */
    int updateByPrimaryKey(RsPlanCtrl record);
}