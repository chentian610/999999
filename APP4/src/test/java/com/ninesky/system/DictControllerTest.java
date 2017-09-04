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
public class DictControllerTest extends AbstractContextControllerTests {

    private static String URI = "/dictAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getDictionary() throws Exception {
        this.mockMvc.perform(post("/dictAction/getDictionary")
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
        this.mockMvc.perform(post("/dictAction/addDictionary")
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
    public void updateDictSort() throws Exception {
        this.mockMvc.perform(post("/dictAction/updateDictSort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"json_array\":\"[{\"dict_code\":\"022005\",\"id\":79,\"children\":" +
                        "[{\"dict_code\":\"025003\",\"id\":80},{\"dict_code\":\"025002\",\"id\":81},{\"dict_code\":\"025001\",\"id\":82},{\"dict_code\":\"025019\",\"id\":83}]}," +
                        "{\"dict_code\":\"022015\",\"id\":88,\"children\":[{\"dict_code\":\"025009\",\"id\":89},{\"dict_code\":\"025010\",\"id\":90},{\"dict_code\":\"025021\",\"id\":91}]}," +
                        "{\"dict_code\":\"022010\",\"id\":100,\"children\":[{\"dict_code\":\"025008\",\"id\":101},{\"dict_code\":\"025007\",\"id\":102},{\"dict_code\":\"025006\",\"id\":103}]}," +
                        "{\"dict_code\":\"022025\",\"id\":95,\"children\":[{\"dict_code\":\"025014\",\"id\":96},{\"dict_code\":\"025015\",\"id\":97}]}," +
                        "{\"dict_code\":\"022020\",\"id\":107,\"children\":[{\"dict_code\":\"025023\",\"id\":108},{\"dict_code\":\"025024\",\"id\":109},{\"dict_code\":\"025025\",\"id\":110}]}," +
                        "{\"dict_code\":\"022026\",\"id\":114,\"children\":[{\"dict_code\":\"025026\",\"id\":115},{\"dict_code\":\"025027\",\"id\":116},{\"dict_code\":\"025028\",\"id\":117}]}]\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateDictSchoolSort() throws Exception {
        this.mockMvc.perform(post("/dictAction/updateDictSort")
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
    public void getDict() throws Exception {
        this.mockMvc.perform(post("/dictAction/getDict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_code\":\"022005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getNewsCssList() throws Exception {
        this.mockMvc.perform(post("/dictAction/getNewsCssList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"022005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getDictSchoolList() throws Exception {
        this.mockMvc.perform(post("/dictAction/getDictSchoolList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"011\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_active").isNotEmpty());
    }

    @Test
    public void addDictSchool() throws Exception {
        this.mockMvc.perform(post("/dictAction/addDictSchool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_group\":\"022026\",\"dict_value\":\"天天向上\",\"other_field\":\"032025\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_active").isNotEmpty());
    }

    @Test
    public void forbiddenDictSchool() throws Exception {
        this.mockMvc.perform(post("/dictAction/forbiddenDictSchool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dcit_code\":\"015045001\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateDictSchool() throws Exception {
        this.mockMvc.perform(post("/dictAction/updateDictSchool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_value\":\"嘻嘻嘻\",\"dict_code\":\"025027\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteNewsDictSchool() throws Exception {
        this.mockMvc.perform(post("/dictAction/deleteNewsDictSchool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_code\":\"025027\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteNewsDictSchoolByCode() throws Exception {
        this.mockMvc.perform(post("/dictAction/deleteNewsDictSchoolByCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_code\":\"025027\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void addNewsDictSchool() throws Exception {
        this.mockMvc.perform(post("/dictAction/addDictSchool")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"dict_group\":\"022026\",\"dict_value\":\"天天向上\",\"other_field\":\"032025\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_active").isNotEmpty());
    }

    @Test
    public void getDictSchoolList1() throws Exception {
        this.mockMvc.perform(post("/dictAction/getNewsDictSchoolList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_code\":\"022005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.other_field").isNotEmpty());
    }

    @Test
    public void getNewsDictionary() throws Exception {
        this.mockMvc.perform(post("/dictAction/getNewsDictSchoolList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"022\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
                .andExpect(jsonPath("$.result.data.dict_value").isNotEmpty())
                .andExpect(jsonPath("$.result.data.news_code_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.other_field").isNotEmpty());
    }

}