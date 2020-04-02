package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.util.JwtConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ProjectDeviceControllerTest {

    @Autowired
    private ProjectDeviceController projectDeviceController;

    private MockMvc mockMvc;

    private String userId = "wy001";

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectDeviceController).build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown() {

        /*MultiValueMap<String, String> params = new LinkedMultiValueMap();
        params.add("fileId","32");
        params.add("userId","20");*/
    }

    @Test
    void getNew() throws Exception {

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/device/new")
                        .headers(headers)
                        .requestAttr("userId",userId);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray devices = response.getJSONArray("data");

        assertTrue(devices.size() > 0);
    }

    @Test
    void listByProject() throws Exception {

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/device/project/2020-adgd-D-04")
                        .headers(headers)
                        .requestAttr("userId",userId);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectDevices = response.getJSONArray("data");
        assertTrue(projectDevices.getJSONObject(0).getString("deviceManagerId").equals("wy001"));
    }

    @Test
    void listByEmployee() throws Exception {

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/device/owner/wy001")
                        .headers(headers)
                        .requestAttr("userId",userId);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray projectDevices = response.getJSONArray("data");
        assertTrue(projectDevices.getJSONObject(0).getString("projectId").equals("2020-adgd-D-04"));
    }

    @Test
    void add() throws Exception {

        JSONObject msg = new JSONObject();
        msg.put("projectId","2020-adgd-D-04");
        msg.put("deviceId","no-this-device-001");
        msg.put("type","no-sense");
        msg.put("state",0);
        msg.put("deviceManagerId","wy001");
        msg.put("totalUseTime",15);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/device")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(msg.toJSONString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

    }

    @Test
    void check() throws Exception {

        JSONObject msg = new JSONObject();
        msg.put("projectId","2020-adgd-D-04");
        msg.put("deviceId","pad-003");
        msg.put("type","ipad");
        msg.put("state",0);
        msg.put("deviceManagerId","wy001");
        msg.put("totalUseTime",15);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/device/check")
                        .headers(headers)
                        .requestAttr("userId",userId)
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
        msg.put("projectId","2020-adgd-D-04");
        msg.put("deviceId","pad-003");
        msg.put("type","ipad");
        msg.put("state",0);
        msg.put("deviceManagerId","wy001");
        msg.put("totalUseTime",15);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/device/return")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(msg.toJSONString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));
    }

}