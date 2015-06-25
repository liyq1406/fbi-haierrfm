package rfm.qd.repository.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rfm.qd.repository.model.QdRsPlanCtrl;
import rfm.qd.repository.model.QdRsPlanCtrlExample;

public interface QdRsPlanCtrlMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int countByExample(QdRsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByExample(QdRsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int deleteByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insert(QdRsPlanCtrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int insertSelective(QdRsPlanCtrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    List<QdRsPlanCtrl> selectByExample(QdRsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    QdRsPlanCtrl selectByPrimaryKey(String pkId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExampleSelective(@Param("record") QdRsPlanCtrl record, @Param("example") QdRsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByExample(@Param("record") QdRsPlanCtrl record, @Param("example") QdRsPlanCtrlExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKeySelective(QdRsPlanCtrl record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table QD_RS_PLAN_CTRL
     *
     * @mbggenerated Thu Jun 25 14:54:01 CST 2015
     */
    int updateByPrimaryKey(QdRsPlanCtrl record);
}