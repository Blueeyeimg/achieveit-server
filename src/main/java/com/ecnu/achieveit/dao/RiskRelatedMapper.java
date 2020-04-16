package com.ecnu.achieveit.dao;

import com.ecnu.achieveit.model.Employee;
import com.ecnu.achieveit.model.RiskRelatedKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RiskRelatedMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_related
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(RiskRelatedKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_related
     *
     * @mbg.generated
     */
    int insert(RiskRelatedKey record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table risk_related
     *
     * @mbg.generated
     */
    int insertSelective(RiskRelatedKey record);


    boolean clear(@Param("projectId") String projectId, @Param("riskId") String riskId);


    List<RiskRelatedKey> selectByProjectRiskId(@Param("projectId") String projectId, @Param("riskId") String riskId);


    List<Employee> selectRelates();


}