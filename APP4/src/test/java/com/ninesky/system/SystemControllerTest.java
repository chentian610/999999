package com.ninesky.system;

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
 * Created by TOOTU on 2017/3/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class SystemControllerTest extends AbstractContextControllerTests {

    private static String URI = "/systemAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getMainView() throws Exception {
        this.mockMvc.perform(post("/systemAction/getMainView")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{}").getBytes()))
                .andExpect(jsonPath("$.result.data").value("http://112.124.100.26/demo/startup/1031/index.html"));
    }

    @Test
    public void getLastVersion() throws Exception {
        this.mockMvc.perform(post("/systemAction/getLastVersion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.min_version").isNotEmpty())
                .andExpect(jsonPath("$.result.data.min_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_version").isNotEmpty());
    }

    @Test
    public void addAppVersion() throws Exception {
        this.mockMvc.perform(post("/systemAction/addAppVersion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteAppVersion() throws Exception {
        this.mockMvc.perform(post("/systemAction/deleteAppVersion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"id\":1060}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateAppVersion() throws Exception {
        this.mockMvc.perform(post("/systemAction/updateAppVersion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"app_type\":\"005010\",\"app_code\":\"70010001\",\n" +
                        "\"app_name\":\"课淘App\",\"app_version\":\"1.0\",\t\"update_url\":\"www.android.com.update\",\"update_msg\":\"本次更新了作业、通知等模块\",\"must_update\":0}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getDictionary() throws Exception {
        this.mockMvc.perform(post("/systemAction/getDictionary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"011\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void addDictionary() throws Exception {
        this.mockMvc.perform(post("/systemAction/addDictionary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"015\",\"地理\":\"地理\",\"地理\":\"地理\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void deleteDictionary() throws Exception {
        this.mockMvc.perform(post("/systemAction/deleteDictionary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_code\":\"011010\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateDictSort() throws Exception {
        this.mockMvc.perform(post("/systemAction/updateDictSort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_group\":\"022\",\"json_array\":\"[{\"dict_code\":\"022005\",\"id\":79,\"children\":" +
                        "[{\"dict_code\":\"025003\",\"id\":80},{\"dict_code\":\"025002\",\"id\":81},{\"dict_code\":\"025001\",\"id\":82},{\"dict_code\":\"025019\",\"id\":83}]}," +
                        "{\"dict_code\":\"022015\",\"id\":88,\"children\":[{\"dict_code\":\"025009\",\"id\":89},{\"dict_code\":\"025010\",\"id\":90},{\"dict_code\":\"025021\",\"id\":91}]}," +
                        "{\"dict_code\":\"022010\",\"id\":100,\"children\":[{\"dict_code\":\"025008\",\"id\":101},{\"dict_code\":\"025007\",\"id\":102},{\"dict_code\":\"025006\",\"id\":103}]}," +
                        "{\"dict_code\":\"022025\",\"id\":95,\"children\":[{\"dict_code\":\"025014\",\"id\":96},{\"dict_code\":\"025015\",\"id\":97}]}," +
                        "{\"dict_code\":\"022020\",\"id\":107,\"children\":[{\"dict_code\":\"025023\",\"id\":108},{\"dict_code\":\"025024\",\"id\":109},{\"dict_code\":\"025025\",\"id\":110}]}," +
                        "{\"dict_code\":\"022026\",\"id\":114,\"children\":[{\"dict_code\":\"025026\",\"id\":115},{\"dict_code\":\"025027\",\"id\":116},{\"dict_code\":\"025028\",\"id\":117}]}]\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void sendMsg() throws Exception {
        this.mockMvc.perform(post("/systemAction/sendMsg")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003005\",\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateDictName() throws Exception {
        this.mockMvc.perform(post("/systemAction/updateDictName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_value\":\"嘻嘻嘻\",\"dict_code\":\"025027\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getSysConfig() throws Exception {
        this.mockMvc.perform(post("/systemAction/getSysConfig")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"key\":\"PARTNER_CODE\"}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getSchoolNewsConfig() throws Exception {
        this.mockMvc.perform(post("/systemAction/getSchoolNewsConfig")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"key\":\"NEWS_TEMPLATE_ON\"}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getAllSysConfig() throws Exception {
        this.mockMvc.perform(post("/systemAction/getAllSysConfig")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{}").getBytes()))
                .andExpect(jsonPath("$.result.data.MESSAGE_BASEURL").isNotEmpty())
                .andExpect(jsonPath("$.result.data.API_PLATFORM_DOMAIN").isNotEmpty())
                .andExpect(jsonPath("$.result.data.TEACHER_MODEL_DOWNLOAD_URL").isNotEmpty())
                .andExpect(jsonPath("$.result.data.FILE_UPLOAD_ACTION").isNotEmpty())
                .andExpect(jsonPath("$.result.data.MESSAGE_KEY").isNotEmpty())
                .andExpect(jsonPath("$.result.data.JIAOXUEYUN_SECRETKEY").isNotEmpty())
                .andExpect(jsonPath("$.result.data.MESSAGE_CHECKBASEURL").isNotEmpty())
                .andExpect(jsonPath("$.result.data.MESSAGE_SID").isNotEmpty())
                .andExpect(jsonPath("$.result.data.host_ip_port").isNotEmpty())
                .andExpect(jsonPath("$.result.data.main_pic_url_party").isNotEmpty())
                .andExpect(jsonPath("$.result.data.CACHE_ON").isNotEmpty())
                .andExpect(jsonPath("$.result.data.MODEL_DOWNLOAD_URL").isNotEmpty())
                .andExpect(jsonPath("$.result.data.WEB_DOMAIN_RECORD").isNotEmpty())
                .andExpect(jsonPath("$.result.data.WECHAT_PLATFORM_DOMAIN").isNotEmpty())
                .andExpect(jsonPath("$.result.data.main_pic_url_style").isNotEmpty())
                .andExpect(jsonPath("$.result.data.PARTNER_CODE").isNotEmpty())
                .andExpect(jsonPath("$.result.data.JIAOXUEYUN_SOURCE").isNotEmpty())
                .andExpect(jsonPath("$.result.data.MESSAGE_CPID").isNotEmpty())
                .andExpect(jsonPath("$.result.data.IMAGE_CUT_ACTION").isNotEmpty())
                .andExpect(jsonPath("$.result.data.MESSAGE_ENVIRONMENT").isNotEmpty())
                .andExpect(jsonPath("$.result.data.JIAOXUEYUN_URL").isNotEmpty())
                .andExpect(jsonPath("$.result.data.JIAOXUEYUN_APPID").isNotEmpty());
    }

    @Test
    public void getHeaderDataByTableName() throws Exception {
        this.mockMvc.perform(post("/systemAction/getHeaderDataByTableName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"table_name\":\"kt_sys_app\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.column_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.column_comment").isNotEmpty());
    }

    @Test
    public void getDataByTableName() throws Exception {
        this.mockMvc.perform(post("/systemAction/getHeaderDataByTableName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"table_name\":\"kt_sys_app\",\"table_id\":\"id\",\"limit\":10,\"start_id\":0,\"page\",1}").getBytes()))
                .andExpect(jsonPath("$.result.data.column_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.column_comment").isNotEmpty());
    }

}