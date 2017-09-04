package com.ninesky.dynamic;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.ninesky.common.DictConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ninesky.AbstractContextControllerTests;

/**
 * Created by TOOTU on 2017/3/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class DynamicControllerTest extends AbstractContextControllerTests {

    private static String URI = "/dynamicAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getDynamicrmation() throws Exception {
        this.mockMvc.perform(post("/dynamicAction/getDynamicList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"start_key\":,\"limit\":20,\"user_type\":\"003005\",\"direction\":1,\n" +
                        "\"app_type\":\"005010\",\"school_id\":1060,\"user_name\":\"张威飞\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_pkid").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_cod").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.info_ur").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_read").isNotEmpty())
                .andExpect(jsonPath("$.result.data.key").isNotEmpty())
                .andExpect(jsonPath("$.result.data.link_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.total").isNotEmpty());
    }

    //@Test
    public void insertDynamic() throws Exception {

    }

    @Test
    public void updateReadFlag() throws Exception {
        this.mockMvc.perform(post("/dynamicAction/updateReadFlag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"start_key\":\"\"}")))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void haveNewInfo() throws Exception {
        this.mockMvc.perform(post("/dynamicAction/haveNewInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.haveNewInfo").isNotEmpty());
    }

}