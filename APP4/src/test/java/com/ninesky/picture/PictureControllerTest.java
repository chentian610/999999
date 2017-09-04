package com.ninesky.picture;


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
public class PictureControllerTest extends AbstractContextControllerTests {


    private static String URI = "/pictureAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addPicture() throws Exception {
        this.mockMvc.perform(post("/pictureAction/addPicture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"class_id\":1113,\"school_id\":1030,\"picture_type\":\"006010\",\"user_type\":\"003005\",\"user_id\":1353,\"title\":\"天天向上\",\n" +
                        "\"file_list\":\"[{\"file_url\":\"http://112.124.100.26/file/APP4/photo/16031014124648667216556.p\n" +
                        "ng\",\"file_resize_url\":\"http://112.124.100.26/file/APP4/photo/16031014124648667216556RESIZE.p\n" +
                        "ng\"},{\"file_url\":\"http://112.124.100.26/file/APP4/photo/16031014124641535318715.png\",\"file_resize_url\":\"http://112.124.100.26/file/APP4/photo/16031014124641535318715RESIZE.png\"}{\"file_url\":\"http:\n" +
                        "//112.124.100.26/file/APP4/photo/16031014124610215097374.png\",\"file_resize_url\":\"http://112.124.100.26/file/APP4/photo/16031014124610215097374RESIZE.png\"}]\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.picture_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.picture_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.picture_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.add_date").isNotEmpty());
    }

    @Test
    public void getPictureList() throws Exception {
        this.mockMvc.perform(post("/pictureAction/getPictureList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"class_id\":1113,\"school_id\":1030,\"picture_type\":\"006010\",\"user_type\":\"003005\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.picture_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.picture_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.picture_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.picture_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.add_date").isNotEmpty());
    }

    @Test
    public void deletePicture() throws Exception {
        this.mockMvc.perform(post("/pictureAction/deletePicture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"picture_id\":1,\"user_type\":\"003005\",\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

}