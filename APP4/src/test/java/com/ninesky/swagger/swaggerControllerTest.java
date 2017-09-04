package com.ninesky.swagger;

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
public class swaggerControllerTest extends AbstractContextControllerTests {

    private static String URI = "/swagger/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void aop1() throws Exception {
        this.mockMvc.perform(post("/swagger/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.tags.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.tags.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.summary").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.tags").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.produces").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.parameters.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.parameters.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.parameters.type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.parameters.required").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.parameters.in").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.consumes").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.responses.200.schema.type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.responses.200.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.responses.401.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.responses.400.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./api/user/login.get.responses.403.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.basePath").isNotEmpty())
                .andExpect(jsonPath("$.result.data.host").isNotEmpty())
                .andExpect(jsonPath("$.result.data.swagger").isNotEmpty());
    }

    @Test
    public void aop2() throws Exception {
        this.mockMvc.perform(post("/swagger/test2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.swagger").isNotEmpty())
                .andExpect(jsonPath("$.result.data.host").isNotEmpty())
                .andExpect(jsonPath("$.result.data.basePath").isNotEmpty())
                .andExpect(jsonPath("$.result.data.tags.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.tags.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.controllerName").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.methodName").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.requestUrl").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.methodParmaTypes").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.consumes").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.produces").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.responses.200.schema.type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.responses.403.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.responses.400.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.paths./agentAction/addAgentVO.get.responses.401.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info.title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info.version").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info.termsOfService").isNotEmpty());
    }

}