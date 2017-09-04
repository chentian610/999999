package com.ninesky.agent;

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
 * Created by TOOTU on 2017/2/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class AgentControllerTest extends AbstractContextControllerTests {

    private static String URI = "/agentAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addAgentVO() throws Exception {
        this.mockMvc.perform(post("/agentAction/addAgentVO")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"agent_name\":\"张威飞\",\"regist_date\":\"2016-07-07 12:22:40\"," +
                        "\"valid_date\":\"2017-07-07 12:22:43\",\"create_by\":1265,\"create_date\":\"2016-07-07 12:22:44\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
    }

    @Test
    public void getAgentListByID() throws Exception {
        this.mockMvc.perform(post("/agentAction/getAgentListByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"agent_id\":68,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").value(68))
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
    }

    @Test
    public void getAgentList() throws Exception {
        this.mockMvc.perform(post("/agentAction/getAgentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
        this.mockMvc.perform(post("/agentAction/getAgentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"search\":\"安徽\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
        this.mockMvc.perform(post("/agentAction/getAgentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"search\":\"188\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
        this.mockMvc.perform(post("/agentAction/getAgentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"agent_name\":\"安徽代理商\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
        this.mockMvc.perform(post("/agentAction/getAgentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18812345678\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
    }

    @Test
    public void dateleAgentByID() throws Exception {
        this.mockMvc.perform(post("/agentAction/dateleAgentByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"agent_id\":68,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void updateAgentByID() throws Exception {
        this.mockMvc.perform(post("/agentAction/updateAgentByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"agent_id\":68,\"phone\":\"18257166300\",\"agent_name\":\"张威飞\",\"regist_date\":\"2016-07-07 12:22:40\"," +
                        "\"valid_date\":\"2017-07-07 12:22:43\",\"update_by\":1265,\"update_date\":\"2016-07-07 12:22:44\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.agent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.regist_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.valid_date").isNotEmpty());
    }

}