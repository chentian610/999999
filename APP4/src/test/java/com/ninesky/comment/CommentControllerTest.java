package com.ninesky.comment;


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
public class CommentControllerTest extends AbstractContextControllerTests {
    private static String URI = "/commentAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addComment() throws Exception {
        this.mockMvc.perform(post("/commentAction/saveComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"class_id\":1103,\"comment_date\":\"2015-11-24\",\"student_id\":545," +
                        "\"student_code\":\"150103\",\"comment\":\"作业及时完成\",\"create_by\":1268," +
                        "\"create_date\":\"2015-11-24 10:35:37\",\"is_read\":1,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.comment_id").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getStudentList() throws Exception {
        this.mockMvc.perform(post("/commentAction/getStudentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"class_id\":1103,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.studnet_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment_date").isNotEmpty());
    }

    @Test
    public void getComment() throws Exception {
        this.mockMvc.perform(post("/commentAction/getCommentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"class_id\":1103,\"student_id\":,\"comment_id\":,\"comment_date\":,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.comment_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void deleteComment() throws Exception {
        this.mockMvc.perform(post("/commentAction/deleteComment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"comment_id\":601,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.comment_id").value(601))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void getTemplate() throws Exception {
        this.mockMvc.perform(post("/commentAction/getTemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.template_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void addTemplate() throws Exception {
        this.mockMvc.perform(post("/commentAction/addTemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"comment\":\"好好学习天天向上\",\"is_read\":0,\"create_by\":1353,\"create_date\":\"2016-05-19 11:39:06\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_read").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_by").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void upateTemplate() throws Exception {
        this.mockMvc.perform(post("/commentAction/updateTemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"comment\":\"好好学习天天向上\",\"template_id\":763}").getBytes()))
                .andExpect(jsonPath("$.result.data.template_id").value(763))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void deleteTemplate() throws Exception {
        this.mockMvc.perform(post("/commentAction/deleteTemplate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"template_id\":763,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.template_id").value(763))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void getUnreadCount() throws Exception {
        this.mockMvc.perform(post("/commentAction/getUnreadCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":543,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getCommentByID() throws Exception {
        this.mockMvc.perform(post("/commentAction/getCommentByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"comment_id\":763,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.comment_id").value(763))
                .andExpect(jsonPath("$.result.data.comment_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.comment").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty());
    }

}