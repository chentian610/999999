package com.ninesky.info;

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
public class InfoControllerTest extends AbstractContextControllerTests {

    private static String URI = "/infoAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getInformation() throws Exception {
        this.mockMvc.perform(post("/infoAction/getInformation")
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

    @Test
    public void addInformation() throws Exception {
        this.mockMvc.perform(post("/infoAction/addInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":003005,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void updateInformation() throws Exception {
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":003005,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":003005,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312,\"class_id\":1051}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":003005,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312,\"info_id\":2025}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_type\":003005,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312,\"info_id\":2025,\"class_id\":1051}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));

        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":2793,\"school_id\":1060,\"user_type\":003010,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":2793,\"school_id\":1060,\"user_type\":003010,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312,\"class_id\":1051}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":2793,\"school_id\":1060,\"user_type\":003010,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312,\"info_id\":2025}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
        this.mockMvc.perform(post("/infoAction/updateInformation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":2793,\"school_id\":1060,\"user_type\":003010,\"sender_id\":1312,\"module_code\":009003,\"\tmodule_pkid\":\"463\",\"info_type\":\"021010\",\"info_title\":\"不同的鸟\",\"\tinfo_content\":\"寻找生活中的鸟类\",\"show_type\":\"023\",\"info_date\":\"2016-05-23\",\n" +
                        "\"user_id\":1312,\"info_id\":2025,\"class_id\":1051}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void getUnreadCount() throws Exception {
        this.mockMvc.perform(post("/infoAction/getUnreadCount")
                .content(("{\"school_id\":1060,\"user_type\":003010,\"user_id\":1321,\"student_id\":2793}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
        this.mockMvc.perform(post("/infoAction/getUnreadCount")
                .content(("{\"school_id\":1060,\"user_type\":003005,\"user_id\":1321}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
    }

}