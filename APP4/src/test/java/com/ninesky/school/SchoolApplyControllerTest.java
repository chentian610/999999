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
public class SchoolApplyControllerTest extends AbstractContextControllerTests {

    private static String URI = "/schoolApplyAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addSchool() throws Exception {
        this.mockMvc.perform(post("/schoolApplyAction/addSchool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_type\":\"002010\",\"organize_pic_url\":\"images/gzh_wx.jpg\",\"school_name\":\"测试小学\",\n" +
                        "\"school_motto\":\"好好学习天天向上\",\"domain=csxx\",\"school_admin_phone\":\"18257166300\",\"english_name\":\"csxx\",\n" +
                        "\"module_ids\":\"236,237,238,240,252,253,255,256,280,281,372\",\"agent_phone\":\"18857169874\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.organize_pic_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_motto").isNotEmpty())
                .andExpect(jsonPath("$.result.data.domain").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_admin_phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.english_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_ids").isNotEmpty())
                .andExpect(jsonPath("$.result.data.agent_phone").isNotEmpty());
    }

    @Test
    public void addLinkManFromBaidu() throws Exception {
        this.mockMvc.perform(post("/schoolApplyAction/addLinkManFromBaidu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_name\":\"铜仁市地理小学\",\"link_man\":\"郭毅\",\"link_style\":\"13985333282\",\"link_email\":\"2362145160@qq.com\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void examineDomainName() throws Exception {
        this.mockMvc.perform(post("/schoolApplyAction/examineDomainName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"domain\":\"csxx\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

}