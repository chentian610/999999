package com.ninesky.module;

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
 * Created by TOOTU on 2017/3/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ModuleControllerTest extends AbstractContextControllerTests {

    private static String URI = "/moduleAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getModuleList() throws Exception {
        this.mockMvc.perform(post("/moduleAction/getModuleList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":\"003\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.icon_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty());
        this.mockMvc.perform(post("/moduleAction/getModuleList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.icon_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty());
        this.mockMvc.perform(post("/moduleAction/getModuleList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":\"003010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.icon_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty());
        this.mockMvc.perform(post("/moduleAction/getModuleList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":\"003015\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.icon_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty());
    }

    @Test
    public void getSchoolModuleCodeListBySchoolID() throws Exception {
        this.mockMvc.perform(post("/moduleAction/getSchoolModuleCodeListBySchoolID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty());
    }

}