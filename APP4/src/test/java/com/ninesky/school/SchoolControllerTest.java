package com.ninesky.school;

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
 * Created by TOOTU on 2017/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SchoolControllerTest extends AbstractContextControllerTests{

    private static String URI = "/schoolAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getAdminPhone() throws Exception {
        this.mockMvc.perform(post("/schoolAction/getAdminPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getSchoolById() throws Exception {
        this.mockMvc.perform(post("/schoolAction/getSchoolById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.organize_pic_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_motto").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_admin_phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.domain").isNotEmpty())
                .andExpect(jsonPath("$.result.data.english_name").isNotEmpty());
    }

    @Test
    public void getSchoolApplyList() throws Exception {
        this.mockMvc.perform(post("/schoolAction/getSchoolApplyList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"status\":\"007005\",\"user_id\":1353,\"school_id\":1060,\"school_admin_phone\":\"13958192160\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.organize_pic_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_motto").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_admin_phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.domain").isNotEmpty())
                .andExpect(jsonPath("$.result.data.english_name").isNotEmpty());
    }

    @Test
    public void passSchoolApply() throws Exception {
        this.mockMvc.perform(post("/schoolAction/updateSchoolApply")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_type\":\"002010\",\"organize_pic_url\":\"images/gzh_wx.jpg\",\"school_name\":\"测试小学\"," +
                        "\"school_motto\":\"好好学习天天向上\",\"english_name\":\"csxx\",\"school_id\":1064}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getSchoolList() throws Exception {
        this.mockMvc.perform(post("/schoolAction/getSchoolList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty());
    }

    //@Test
    //public void redirectSchoolByDomain() throws Exception {
    //    this.mockMvc.perform(post("/schoolAction/redirect/{domain}"));
    //}

}