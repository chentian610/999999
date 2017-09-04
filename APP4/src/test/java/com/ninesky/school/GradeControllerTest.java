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
public class GradeControllerTest extends AbstractContextControllerTests {

    private static String URI = "/gradeAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getGradeList() throws Exception {
        this.mockMvc.perform(post("/gradeAction/getGradeList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.enrollment_year").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_name").isNotEmpty());
    }

    @Test
    public void addGrade() throws Exception {
        this.mockMvc.perform(post("/gradeAction/addGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"grade_num\":4,\"grade_name\":\"七年级\",\"sort\":4,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_num").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_name").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void updateGrade() throws Exception {
        this.mockMvc.perform(post("/gradeAction/updateGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"grade_num\":4,\"grade_name\":\"七年级\",\"sort\":4,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteGrade() throws Exception {
        this.mockMvc.perform(post("/gradeAction/deleteGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"grade_num\":4,\"grade_name\":\"七年级\",\"grade_id\":1046}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getGradeAndClass() throws Exception {
        this.mockMvc.perform(post("/gradeAction/getGradeAndClass")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.grade_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_list").isNotEmpty());
    }

}