package com.ninesky.app;

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
public class AppControllerTest extends AbstractContextControllerTests
{
    private static String URI = "/appAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void saveSetting() throws Exception {
        this.mockMvc.perform(post("/appAction/saveSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"set_type\":\"018010\",\"is_open\":1,\"create_by\":1353," +
                        "\"create_date\":\"2016-10-17 11:40:51\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void getUserSetting() throws Exception {
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"id\":1,\"set_type\":\"018010\",\"is_open\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"id\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"id\":1,\"set_type\":\"018010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"set_type\":\"018010\",\"is_open\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"id\":1,\"is_open\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"set_type\":\"018010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"is_open\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
        this.mockMvc.perform(post("/appAction/getUserSetting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.set_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_open").isNotEmpty());
    }

    @Test
    public void addSuggest() throws Exception {
        this.mockMvc.perform(post("/appAction/addSuggest")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1329,\"user_name\":\"黄晓旭\",\"content\":\"哦名字\"," +
                        "\"create_by\":1329,\"create_date\":\"2016-04-20 21:35:58\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void getSuggestList() throws Exception {
        this.mockMvc.perform(post("/appAction/getSuggestList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"start_time\":\"2016\",\"end_time\":\"2017\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

}