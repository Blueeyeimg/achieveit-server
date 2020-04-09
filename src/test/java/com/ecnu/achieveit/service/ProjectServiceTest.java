package com.ecnu.achieveit.service;

import com.ecnu.achieveit.model.ProjectBasicInfo;
import com.ecnu.achieveit.util.LogUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ProjectServiceTest {
    @Autowired
    private ProjectService projectService;

    private ProjectBasicInfo projectBasicInfoNew;

    private ProjectBasicInfo projectBasicInfoExist;

    @BeforeEach
    void setUp() {
        ProjectBasicInfo projectBasicInfo2 = new ProjectBasicInfo();
        projectBasicInfo2.setProjectId("2018-qwer-D-02");
        projectBasicInfo2.setProjectName("测试");
        projectBasicInfo2.setClientId("pwc-001");
        projectBasicInfo2.setState("申请立项");
        projectBasicInfo2.setExpStartDate(Date.valueOf("2020-04-09"));
        projectBasicInfo2.setExpEndDate(Date.valueOf("2020-04-10"));
        projectBasicInfo2.setProjectBossId("sni025");
        projectBasicInfo2.setMilestone("里程碑");
        projectBasicInfo2.setTechnology("技术点");
        projectBasicInfo2.setBusinessDomain("业务范围");
        projectBasicInfo2.setMainFunctions("主要功能");
        projectBasicInfo2.setOutputLink("对外的链接");
        projectBasicInfo2.setGitAddress("https://github.com/Blueeyeimg/achieveit-server");
        projectBasicInfo2.setFileSystemAddress("172.25.125.24");
        this.projectBasicInfoNew = projectBasicInfo2;
        ProjectBasicInfo projectBasicInfo3 = new ProjectBasicInfo();
        projectBasicInfo3.setProjectId("2020-adgd-D-04");
        projectBasicInfo3.setProjectName("智趣识图3");
        projectBasicInfo3.setClientId("pwc-001");
        projectBasicInfo3.setState("同意立项");
        projectBasicInfo3.setExpStartDate(Date.valueOf("2020-03-25"));
        projectBasicInfo3.setExpEndDate(Date.valueOf("2020-05-13"));
        projectBasicInfo3.setProjectBossId("sni025");
        projectBasicInfo3.setGitAddress("https://github.com/Blueeyeimg/achieveit-server");
        projectBasicInfo3.setFileSystemAddress("172.25.125.24");
        this.projectBasicInfoExist = projectBasicInfo3;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addProject() {
        assertTrue(projectService.addProject(projectBasicInfoNew));
    }

    @Test
    void updateProject() {
        assertFalse(projectService.updateProject(projectBasicInfoNew));
        assertTrue(projectService.updateProject(projectBasicInfoExist));
    }

    @Test
    void updateProjectState() {
        String state = "已结束";
        assertTrue(projectService.updateProjectState(projectBasicInfoExist,state));
        assertFalse(projectService.updateProjectState(projectBasicInfoExist,"同意立项"));
    }

    @Test
    void updateProjectStateById() {
        String projectId = projectBasicInfoExist.getProjectId();
        String state = "已结束";
        assertTrue(projectService.updateProjectStateById(projectId,state));
        assertFalse(projectService.updateProjectStateById(projectId,"同意立项"));
    }

    @Test
    void querryProjectByPrimaryKey() {
        String projectId = projectBasicInfoExist.getProjectId();
        assertNotNull(projectService.querryProjectByPrimaryKey(projectId));
    }

    @Test
    void queryProjectByEmployeeId() {
        String employeeId = "wy001";
        assertNotNull(projectService.queryProjectByEmployeeId(employeeId));
    }

    @Test
    void queryProjectByClientId() {
        String clientId = "pwc-001";
        assertNotNull(projectService.queryProjectByClientId(clientId));
    }

    @Test
    void queryProjectByState() {
        assertNotNull(projectService.queryProjectByState(projectBasicInfoExist.getState()));
    }

    @Test
    void queryProjectByBossId() {
        assertNotNull(projectService.queryProjectByBossId(projectBasicInfoExist.getProjectBossId()));
    }

    @Test
    void queryProjectByKeyWord() {
        assertNotNull(projectService.queryProjectByKeyWord(projectBasicInfoExist.getProjectName(),"wy001"));
    }

    @Test
    void queryAssetItem() {
        assertNotNull(projectService.queryAssetItem());
    }
}