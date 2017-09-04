package com.ninesky.template;

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
 * Created by TOOTU on 2017/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TemplateControllerTest extends AbstractContextControllerTests {

    private static String URI = "/templateAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getTemplateList() throws Exception {
        this.mockMvc.perform(post("/templateAction/getTemplateList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_type\":\"002005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.template_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.template_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.template_pic_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_active").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty());
    }

    @Test
    public void getModuleList() throws Exception {
        this.mockMvc.perform(post("/templateAction/getModuleList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{}").getBytes()))
                .andExpect(jsonPath("$.result.data.module_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.icon_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_must").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_inactive").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_news").isNotEmpty());
    }

    //@Test
    //public void getSchoolApp() throws Exception {
    //    this.mockMvc.perform(post("/templateAction/getSchoolApp")
    //            .contentType(MediaType.APPLICATION_JSON)
    //            .content(("{}").getBytes()))
    //}

}