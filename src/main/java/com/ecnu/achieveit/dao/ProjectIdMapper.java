package com.ecnu.achieveit.dao;

import com.ecnu.achieveit.model.ProjectId;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectIdMapper {
    int deleteByPrimaryKey(String projectId);

    int insert(ProjectId record);

    int insertSelective(ProjectId record);

    int updateProjectId(@Param("oldProjectId") String oldProjectId,
                        @Param("newProjectId") String newProjectId);

    List<ProjectId> selectAll();
}