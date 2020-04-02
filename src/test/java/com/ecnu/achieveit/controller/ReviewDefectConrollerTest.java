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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ReviewDefectConrollerTest {

    @Autowired
    private ReviewDefectConroller reviewDefectConroller;

    private MockMvc mockMvc;

    private String userId = "wy001";

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reviewDefectConroller).build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void listByProjectId() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/review_defect/project/2020-adgd-D-04")
                        .headers(headers);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray reviewDefect = response.getJSONArray("data");
        assertTrue(reviewDefect.size() > 0);
        assertTrue(reviewDefect.getJSONObject(0).getString("providerId").equals("nst002"));
    }

    @Test
    void listByProjectIdAndState() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/review_defect/state")
                        .headers(headers)
                        .param("projectId","2020-adgd-D-04")
                        .param("state","1");

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray reviewDefect = response.getJSONArray("data");
        assertTrue(reviewDefect.size() > 0);
        assertTrue(reviewDefect.getJSONObject(0).getString("providerId").equals("nst002"));
        assertTrue(reviewDefect.getJSONObject(0).getString("solverId").equals("nst001"));
    }

    @Test
    void listByProjectIdAndType() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/review_defect/type")
                        .headers(headers)
                        .param("projectId","2020-adgd-D-04")
                        .param("type","defect");

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray reviewDefect = response.getJSONArray("data");
        assertTrue(reviewDefect.size() > 0);
        assertTrue(reviewDefect.getJSONObject(0).getString("providerId").equals("nst002"));
    }

    @Test
    void listByProviderId() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/review_defect/provider/nst002")
                        .headers(headers)
                        .requestAttr("userId","nst002");

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray reviewDefect = response.getJSONArray("data");
        assertTrue(reviewDefect.size() > 0);
        assertTrue(reviewDefect.getJSONObject(0).getString("providerId").equals("nst002"));
    }

    @Test
    void listBySolverId() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/review_defect/solver/nst001")
                        .headers(headers)
                        .requestAttr("userId","nst001");

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray reviewDefect = response.getJSONArray("data");
        assertTrue(reviewDefect.size() > 0);
        assertTrue(reviewDefect.getJSONObject(0).getString("solverId").equals("nst001"));
    }

    @Test
    void add() throws Exception {
        JSONObject msg = new JSONObject();
        msg.put("projectId","2020-adgd-D-04");
        msg.put("type","defect");
        msg.put("providerId","nst002");
        msg.put("state",0);
        msg.put("description","heiehiehi");
        msg.put("link","link link link");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/review_defect")
                        .headers(headers)
                        .requestAttr("userId","nst002")
                        .content(msg.toJSONString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));
    }

    @Test
    void update() throws Exception {
        JSONObject msg = new JSONObject();
        msg.put("reviewDefectId",3);
        msg.put("projectId","2020-adgd-D-04");
        msg.put("type","defect");
        msg.put("providerId","nst002");


        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/review_defect")
                        .headers(headers)
                        .requestAttr("userId","nst001")
                        .content(msg.toJSONString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));
    }

}