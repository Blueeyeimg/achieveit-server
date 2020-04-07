package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.model.ProjectRisk;
import com.ecnu.achieveit.model.ProjectRiskKey;
import com.ecnu.achieveit.model.RiskRelatedKey;
import com.ecnu.achieveit.util.LogUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class RiskControllerTest {

    @Autowired
    private RiskController riskController;

    private MockMvc mockMvc;

    private String userId = "wy001";

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(riskController).build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void upload() throws Exception {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/project_risk_demo.xlsx");

        MockMultipartFile firstFile = new MockMultipartFile("file", "project_risk_demo.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fis);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.fileUpload("/upload/risk")
                        .file(firstFile);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        LogUtil.i(response.toJSONString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray risks = response.getJSONArray("data");
        assertTrue(risks.size() == 2);

        JSONObject risk = risks.getJSONObject(0);
        assertTrue(risk.getString("riskId").equals("risk-001"));
    }

    @Test
    void listByProjectId() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/risk/project/2020-adgd-D-04")
                        .headers(headers);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray risks = response.getJSONArray("data");
        assertTrue(risks.size() == 2);

        JSONObject risk = risks.getJSONObject(0);
        assertTrue(risk.getString("projectId").equals("2020-adgd-D-04"));
    }

    @Test
    void listByRelatedId() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/risk/related/wy001")
                        .headers(headers);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray risks = response.getJSONArray("data");
        assertTrue(risks.size() == 2);

        JSONObject risk = risks.getJSONObject(0);
        assertTrue(risk.getString("projectId").equals("2020-adgd-D-04"));
    }

    @Test
    void listByOwnerId() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/risk/owner/nst001")
                        .headers(headers);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray risks = response.getJSONArray("data");
        assertTrue(risks.size() == 1);

        JSONObject risk = risks.getJSONObject(0);
        assertTrue(risk.getString("projectId").equals("2020-adgd-D-04"));
    }

    @Test
    void addList() throws Exception {
        List<ProjectRisk> risks = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            ProjectRisk risk = new ProjectRisk();
            risk.setProjectId("2020-adgd-D-04");
            risk.setRiskId("risk-25" + i);
            risk.setType("logout");
            risk.setDescription("hei,description" + i);
            risk.setRiskLevel(i);
            risk.setInfluence("test - influence" + i);
            risk.setReactiveStrategy("test - strategy" + i);
            risk.setRiskState("待解决");
            risk.setRiskOwnerId("nst001");
            risk.setRiskTrackFrequency(0.12*i);

            risks.add(risk);
        }

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/risks")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(risks).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void add() throws Exception {
        ProjectRisk risk = new ProjectRisk();

        risk.setProjectId("2020-adgd-D-04");
        risk.setRiskId("risk-250");
        risk.setType("logout");
        risk.setDescription("hei,description");
        risk.setRiskLevel(1);
        risk.setInfluence("test - influence");
        risk.setReactiveStrategy("test - strategy");
        risk.setRiskState("未出现");
        risk.setRiskOwnerId("nst001");
        risk.setRiskTrackFrequency(0.5);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/risk")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(risk).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void update() throws Exception {
        ProjectRisk risk = new ProjectRisk();

        risk.setProjectId("2020-adgd-D-04");
        risk.setRiskId("risk-001");
        risk.setType("logout");
        risk.setDescription("hei,description");
        risk.setRiskLevel(1);
        risk.setInfluence("test - influence");
        risk.setReactiveStrategy("test - strategy");
        risk.setRiskTrackFrequency(0.5);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/risk")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(risk).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void delete() throws Exception {
        ProjectRiskKey risk = new ProjectRiskKey();

        risk.setProjectId("2020-adgd-D-04");
        risk.setRiskId("risk-002");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete("/risk")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(risk).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void getRelated() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/risk/related")
                        .headers(headers)
                        .param("projectId", "2020-adgd-D-04")
                        .param("riskId", "risk-001");

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONArray riskRelates = response.getJSONArray("data");
        assertTrue(riskRelates.size() == 2);

        JSONObject riskRelated = riskRelates.getJSONObject(0);
        assertTrue(riskRelated.getString("projectId").equals("2020-adgd-D-04"));
    }

    @Test
    void addRelated() throws Exception {
        RiskRelatedKey relatedKey = new RiskRelatedKey();

        relatedKey.setProjectId("2020-adgd-D-04");
        relatedKey.setRiskId("risk-001");
        relatedKey.setRiskRelatedId("lxq100");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/risk/related")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(relatedKey).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void deleteRelated() throws Exception {
        RiskRelatedKey relatedKey = new RiskRelatedKey();

        relatedKey.setProjectId("2020-adgd-D-04");
        relatedKey.setRiskId("risk-001");
        relatedKey.setRiskRelatedId("wy001");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.delete("/risk/related")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(relatedKey).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

    @Test
    void updateRelates() throws Exception {

        List<RiskRelatedKey> relatedKeys = new ArrayList<>();

        RiskRelatedKey relatedKey = new RiskRelatedKey();

        relatedKey.setProjectId("2020-adgd-D-04");
        relatedKey.setRiskId("risk-001");
        relatedKey.setRiskRelatedId("wy001");

        RiskRelatedKey relatedKey2 = new RiskRelatedKey();

        relatedKey2.setProjectId("2020-adgd-D-04");
        relatedKey2.setRiskId("risk-001");
        relatedKey2.setRiskRelatedId("lxq100");


        relatedKeys.add(relatedKey);
        relatedKeys.add(relatedKey2);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/risk/relates")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(relatedKeys).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
    }

}