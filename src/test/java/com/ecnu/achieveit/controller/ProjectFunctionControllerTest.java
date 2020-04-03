package com.ecnu.achieveit.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecnu.achieveit.modelview.FunctionItem;
import com.ecnu.achieveit.modelview.FunctionView;
import com.ecnu.achieveit.util.LogUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ProjectFunctionControllerTest {

    @Autowired
    private ProjectFunctionController projectFunctionController;

    private MockMvc mockMvc;

    private String userId = "wy001";

    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectFunctionController).build();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void upload() throws Exception {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/project_function_demo.xlsx");

        MockMultipartFile firstFile = new MockMultipartFile("file", "project_function_demo.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fis);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.fileUpload("/upload/function")
                        .file(firstFile);

        MvcResult mvcResult = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        LogUtil.i(response.toJSONString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONObject functionView = response.getJSONObject("data");
        assertTrue(functionView.getString("projectId").equals(""));

        JSONArray functionItems = functionView.getJSONArray("functions");

        assertTrue(functionItems.size() == 3);

    }

    @Test
    void get() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get("/function/2020-adgd-D-04")
                        .headers(headers);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());


        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));

        JSONObject functionView = response.getJSONObject("data");
        assertTrue(functionView.getString("projectId").equals("2020-adgd-D-04"));

        JSONArray functionItems = functionView.getJSONArray("functions");

        assertTrue(functionItems.size() > 2);
        assertTrue(functionItems.getJSONObject(0).getString("primaryFunction").equals("TimeSheet"));
    }

    @Test
    void addFaultFlow() throws Exception {

        FunctionView functionView = new FunctionView();

        functionView.setProjectId("2020-adgd-D-04");
        functionView.setFunctions(new ArrayList<>());

        FunctionItem item = new FunctionItem();

        item.setPrimaryFunction("中文");
        List<String> secondaryFunction = new ArrayList<>();
        secondaryFunction.add("s1");
        secondaryFunction.add("s2");
        item.setSecondaryFunction(secondaryFunction);


        functionView.getFunctions().add(item);

        LogUtil.i(JSON.toJSON(functionView).toString());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/function")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(functionView).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(2,response.getIntValue("code"));
        assertNotNull(response.getString("data"));
    }

    @Test
    void addHappyPass() throws Exception {

        FunctionView functionView = new FunctionView();

        functionView.setProjectId("2019-dfs3-M-09");
        functionView.setFunctions(new ArrayList<>());

        FunctionItem item1 = new FunctionItem();

        item1.setPrimaryFunction("中文");
        List<String> secondaryFunction = new ArrayList<>();
        secondaryFunction.add("s1");
        secondaryFunction.add("s2");
        item1.setSecondaryFunction(secondaryFunction);

        FunctionItem item2 = new FunctionItem();

        item2.setPrimaryFunction("who cares");
        List<String> secondaryFunction2 = new ArrayList<>();
        secondaryFunction2.add("s3");
        secondaryFunction2.add("s4");
        item2.setSecondaryFunction(secondaryFunction2);

        functionView.getFunctions().add(item1);
        functionView.getFunctions().add(item2);

        LogUtil.i(JSON.toJSON(functionView).toString());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/function")
                        .headers(headers)
                        .requestAttr("userId",userId)
                        .content(JSON.toJSON(functionView).toString());

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        JSONObject response = JSONObject.parseObject(mvcResult.getResponse().getContentAsString());

        assertEquals(0,response.getIntValue("code"));
        assertNotNull(response.getString("data"));
    }

    @Test
    void update() throws Exception {

        StringBuffer strbuffer = new StringBuffer();
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/functions.json");
        InputStreamReader inputStreamReader = new InputStreamReader(fis, "UTF-8");
        BufferedReader in  = new BufferedReader(inputStreamReader);

        String str;
        while ((str = in.readLine()) != null) {
            strbuffer.append(str);  //new String(str,"UTF-8")
        }
        in.close();

        JSONObject msg = JSONObject.parseObject(strbuffer.toString());

        LogUtil.i(msg.toJSONString());

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.put("/function")
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