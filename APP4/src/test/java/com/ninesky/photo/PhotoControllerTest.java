package com.ninesky.photo;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
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
public class PhotoControllerTest extends AbstractContextControllerTests {

    private static String URI = "/photoAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }


    @Test
    public void addPhoto() throws Exception {
        this.mockMvc.perform(post("/photoAction/addPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"fileURL\":\"http://112.124.100.26/file/APP4/photo/16031014124648667216556.ng,http://112.124.100.26/file/APP4/photo/16031014124641535318715.png,http:\n" +
                        "//112.124.100.26/file/APP4/photo/16031014124610215097374.png\",\"fileResizeURL\":\"http://112.124.100.26/file/APP4/photo/16031014124648667216556RESIZE.p\n" +
                        "ng,http://112.124.100.26/file/APP4/photo/16031014124641535318715RESIZE.png,http://112.124.100.26/file/APP4/photo/16031014124610215097374RESIZE.png\"," +
                        "\"class_id\":1113,\"school_id\":1030,\"photo_type\":\"006010\",\"team_type\":\"011005\",\"user_type\":\"003005\",\"student_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.photo_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.photo_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.photo_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty());
    }

    @Test
    public void getPhotoList() throws Exception {
        this.mockMvc.perform(post("/photoAction/getPhotoList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"class_id\":1113,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.photo_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.photo_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.photo_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.version").isNotEmpty());
    }

    @Test
    public void deletePhoto() throws Exception {
        this.mockMvc.perform(post("/photoAction/deletePhoto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"photo_ids\":447,448,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getUnreadCount() throws Exception {
        this.mockMvc.perform(post("/photoAction/getUnreadCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_type\":\"003005\",\"user_id\":1312}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
        this.mockMvc.perform(post("/photoAction/getUnreadCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_type\":\"003010\",\"user_id\":1312,\"student_id\":700}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
    }

}