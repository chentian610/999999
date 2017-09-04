package com.ninesky.springaop;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ninesky.AbstractContextControllerTests;

/**
 * Created by TOOTU on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringControllerTest extends AbstractContextControllerTests {

    private static String URI = "/aop/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void aop1() throws Exception {
        this.mockMvc.perform(post("/aop/aop1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data").value("AOP"));
    }

    @Test
    public void aop2() throws Exception {
        this.mockMvc.perform(post("/aop/aop2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.total").value(0));
    }

    @Test
    public void getURLMapping() throws Exception {
        this.mockMvc.perform(post("/aop/url-mapping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.root.url").isNotEmpty())
                .andExpect(jsonPath("$.root.className").isNotEmpty())
                .andExpect(jsonPath("$.root.methodName").isNotEmpty())
                .andExpect(jsonPath("$.root.returnType").isNotEmpty())
                .andExpect(jsonPath("$.root.annotationName").isNotEmpty())
                .andExpect(jsonPath("$.root.parameters").isNotEmpty());
    }

    @Test
    public void index() throws Exception {
        this.mockMvc.perform(post("/aop/index1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.controllerName").isNotEmpty())
                .andExpect(jsonPath("$.result.data.requestUrl").isNotEmpty())
                .andExpect(jsonPath("$.result.data.methodName").isNotEmpty())
                .andExpect(jsonPath("$.result.data.methodParmaTypes").isNotEmpty());
    }

}