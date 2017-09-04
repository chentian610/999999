package com.ninesky.teachCloud;

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
public class TeachCloudControllerTest extends AbstractContextControllerTests {

    private static String URI = "/teachCloudAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addSource() throws Exception {
        this.mockMvc.perform(post("/teachCloudAction/addSource")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"receive_list\":\"[{\"user_type\":\"003005\",\"group_id\":\"1005\",\"team_id\":\"1103\",\"team_type\":\"011005\"},{\"user_type\":\"003005\",\"group_id\":\"1038\",\"team_id\":\"0\",\"team_type\":\"011005\"}]\",\"remark\":\"好好学习，天天向上\",\n" +
                        "\"source_list\":\"[{\"source_data\":{\"VersionId\":\"V02\",\"GradeId\":\"G04\",\"CoursesId\":\"K01\",\"CourseListId\":\"1320\",\"ResourceSpeciesId\":\"T01,T004;\",\"ResourceName\":\"1 数字歌 拓展资源1\",\"Creator\":\"13473155709\",\"UserName\":\"13473155709\"," +
                        "\"SiId\":\"\",\"ResPackageId\":\"\",\"IsFree\":\"1\",\"ExtensionName\":\"MP4\",\"KnowledgePoint\":\"\",\"ProvinceCode\":\"65000000\",\"Res_source\":\"ecp\",\"ResSourceName\":\"乐智网\",\"ResourceSize\":\"15778.82KB\",\"UpdateTime\":\"2016-10-25 09:29:45\"," +
                        "\"Duration\":\"00:02:01\",\"Res_channel\":\"1\",\"Browse_count\":27,\"Download_count\":2,\"Fav_count\":0,\"Recommend_count\":0,\"Praise_count\":0,\"Res_evaluate\":\"5\",\"AdminRecommend\":\"\",\"IsTop\":\"\"," +
                        "\"ResourceUrl\":\"http://118.180.5.95:8088/hls/playlist.m3u8?param=eyJBcHBJZCI6IjRHYTNLOHlUNlgiLCJTb3VyY2UiOiJTaG91SmlEdWFuIiwiVHlwZSI6Ik5PUk1BTCIsIlJlc291cmNlSWQiOiJmZjgwODA4MTU3ZjZkOGUyMDE1N2Y5Nzg4NTMxMGU4MyIsIk9QIjoiYnJvd3NlIn0\",\"ResourceIcon\":\"http://118.180.8.123/res-share!getPic.do?info=5a6d59344d4467774f4445314e3259325a44686c4d6a41784e54646d4f5463344f44557a4d54426c4f444d734d4377305232457a537a683556445a594c464e6f6233564b61555231595734\",\"ResourceId\":\"ff80808157f6d8e20157f97885310e83\"}," +
                        "\"source_name\":\"1 数字歌 拓展资源1\",\"source_type\":\"MP4\",\"source_id\":\"ff80808157f6d8e20157f97885310e83\",\"resourceUrl\":\"http://118.180.5.95:8088/hls/playlist.m3u8?param=eyJBcHBJZCI6IjRHYTNLOHlUNlgiLCJTb3VyY2UiOiJTaG91SmlEdWFuIiwiVHlwZSI6Ik5PUk1BTCIsIlJlc291cmNlSWQiOiJmZjgwODA4MTU3ZjZkOGUyMDE1N2Y5Nzg4NTMxMGU4MyIsIk9QIjoiYnJvd3NlIn0\"}]\",\"module_code\":\"009011\"}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getSourceList() throws Exception {
        this.mockMvc.perform(post("/teachCloudAction/getSourceList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003005\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.source_data").isNotEmpty())
                .andExpect(jsonPath("$.result.data.remark").isNotEmpty())
                .andExpect(jsonPath("$.result.data.source_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty());
    }

    @Test
    public void getSourceQuery() throws Exception {
        this.mockMvc.perform(post("/teachCloudAction/getTeachCloudSource")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"limit\":100,\"CourseId\":\"K01\",\"start_id\":0,\"page\":1,\"GradeId\":\"G04\",\"ExtensionName\":\"MP4\",\n" +
                        "\"VersionId\":\"V02\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.source_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.source_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.source_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.source_data").isNotEmpty())
                .andExpect(jsonPath("$.result.data.param").isNotEmpty())
                .andExpect(jsonPath("$.result.data.updateTime").isNotEmpty())
                .andExpect(jsonPath("$.result.data.resourceSize").isNotEmpty())
                .andExpect(jsonPath("$.result.data.browse_count").isNotEmpty())
                .andExpect(jsonPath("$.result.data.praise_count").isNotEmpty())
                .andExpect(jsonPath("$.result.data.fav_count").isNotEmpty())
                .andExpect(jsonPath("$.result.data.download_count").isNotEmpty())
                .andExpect(jsonPath("$.result.data.resourceUrl").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getSourceByID() throws Exception {
        this.mockMvc.perform(post("/teachCloudAction/getSourceByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"id\":44,\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.source_data").isNotEmpty())
                .andExpect(jsonPath("$.result.data.remark").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.source_id").isNotEmpty());
    }

}