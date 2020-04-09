package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class TimesheetControllerTest {

    private MockMvc mockMvc;

    private String userId = "wy001";

    private HttpHeaders headers;

    @Autowired
    private TimesheetController timesheetController;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(timesheetController).build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void listProjectId() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/timesheet/projectid")
                .requestAttr("userId",userId)
                .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectIds = response.getJSONArray("data");
        assertTrue(projectIds.size() == 2);

        String projectId1 = projectIds.getString(0);
        String projectId2 = projectIds.getString(1);
        assertTrue(projectId1.equals("2019-dfs3-M-09"));
        assertTrue(projectId2.equals("2020-adgd-D-04"));
    }

    @Test
    void listPrimaryFunction() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/timesheet/primaryfunction")
                        .param("projectId","2020-adgd-D-04")
                        .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectFunctions = response.getJSONArray("data");
        assertTrue(projectFunctions.size()==3);

        String projectFunction1 = projectFunctions.getString(0);
        String projectFunction2 = projectFunctions.getString(1);
        String projectFunction3 = projectFunctions.getString(2);
        assertTrue(projectFunction1.equals("Authorization"));
        assertTrue(projectFunction2.equals("ProjectState"));
        assertTrue(projectFunction3.equals("TimeSheet"));

    }

    @Test
    void listSecondaryFunction() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/timesheet/secondaryfunction")
                        .param("projectId","2020-adgd-D-04")
                        .param("primaryFunction","Authorization")
                        .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectFunctions = response.getJSONArray("data");
        assertTrue(projectFunctions.size()==3);

        String secondaryFunction1 = projectFunctions.getString(0);
        String secondaryFunction2 = projectFunctions.getString(1);
        String secondaryFunction3 = projectFunctions.getString(2);
        assertTrue(secondaryFunction1.equals("auth"));
        assertTrue(secondaryFunction2.equals("login"));
        assertTrue(secondaryFunction3.equals("logout"));

    }

    @Test
    void listPrimaryActivity() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .get("/timesheet/primaryactivity")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectActivities = response.getJSONArray("data");
        assertTrue(projectActivities.size()==4);

        String projectActivity1 = projectActivities.getString(0);
        String projectActivity2 = projectActivities.getString(1);
        String projectActivity3 = projectActivities.getString(2);
        String projectActivity4 = projectActivities.getString(3);
        assertTrue(projectActivity1.equals("工程活动"));
        assertTrue(projectActivity2.equals("管理活动"));
        assertTrue(projectActivity3.equals("外包活动"));
        assertTrue(projectActivity4.equals("支持活动"));
    }

    @Test
    void listSecondaryActivity() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .get("/timesheet/secondaryactivity")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("primaryActivity","外包活动")
                        .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray secondaryActivities = response.getJSONArray("data");
        assertTrue(secondaryActivities.size() == 3);

        String secondaryActivity1 = secondaryActivities.getString(0);
        String secondaryActivity2 = secondaryActivities.getString(1);
        String secondaryActivity3 = secondaryActivities.getString(2);
        assertTrue(secondaryActivity1.equals("外包管理"));
        assertTrue(secondaryActivity2.equals("外包验收"));
        assertTrue(secondaryActivity3.equals("外包支持"));
    }

    @Test
    void saveTimesheet() throws Exception{
        /*Timesheet timesheet = new Timesheet();
        timesheet.setProjectId("2020-adgd-D-04");
        timesheet.setEmployeeId("nst001");
        timesheet.setPrimaryFunction("Authorization");
        timesheet.setSecondaryFunction("login");
        timesheet.setPrimaryActivity("外包活动");
        timesheet.setSecondaryActivity("外包管理");
        timesheet.setDate(Date.valueOf("2020-04-08"));
        timesheet.setStartTime(Time.valueOf("09:00:00"));
        timesheet.setEndTime(Time.valueOf("21:00:00"));
        timesheet.setState("草稿");*/
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .post("/timesheet")
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("projectId","2020-adgd-D-04")
                        .param("primaryFunction","Authorization")
                        .param("secondaryFunction","login")
                        .param("primaryActivity","外包活动")
                        .param("secondaryActivity","外包管理")
                        .param("date","2020-04-08")
                        .param("startTime","09:00:00")
                        .param("endTime","21:00:00")
                        .param("state","已提交")
                        .requestAttr("userId","nst001");

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void listTimesheetForEdit() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .get("/timesheet/user")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .requestAttr("userId","nst001")
                        .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectActivities = response.getJSONArray("data");
        assertTrue(projectActivities.size()==3);
    }

    @Test
    void listTimesheetForConfirm() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .get("/timesheet/boss")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .requestAttr("userId",userId)
                        .headers(headers);

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectActivities = response.getJSONArray("data");
        assertTrue(projectActivities.size()==1);
    }

    @Test
    void changeTimesheetState() throws Exception{
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders
                        .post("/timesheet/confirm")
                        .headers(headers)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("timesheetId","1")
                        .param("state","已确认");

        MvcResult mvcResult =
                mockMvc.perform(request)
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());
        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void isOvertime() throws Exception{
        Date date1 = Date.valueOf("2020-04-08");
        Date date2 = Date.valueOf("2020-04-09");
        Date date3 = Date.valueOf("2020-04-12");
        assertEquals(false,TimesheetController.isOvertime(date1,date2));
        assertEquals(true,TimesheetController.isOvertime(date1,date3));
    }
}