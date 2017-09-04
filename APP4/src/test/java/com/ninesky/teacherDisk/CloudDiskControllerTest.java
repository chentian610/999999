package com.ninesky.teacherDisk;

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
 * Created by TOOTU on 2017/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class CloudDiskControllerTest extends AbstractContextControllerTests {

    private static String URI = "/cloudDiskAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }


    @Test
    public void getCloudDiskList() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/getCloudDiskList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"limit\":10,\"start_id\":0,\"page\":1,\"file_name\":\"\",\"parent_id\":0}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_private").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getFileType() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/getFileType")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{}").getBytes()))
                .andExpect(jsonPath("$.result.data.file_type").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void addCloudDisk() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/addCloudDisk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"file_size\":16205,\"file_name\":\"1.3需求表格.docx\",\"file_url\":\"https://file-test.classtao.cn/APP4/default/17030910004146413567225.docx\",\"parent_id\":20,\"file_type\":\"DOCX\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.file_size").value(16205))
                .andExpect(jsonPath("$.result.data.file_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").value(20))
                .andExpect(jsonPath("$.result.data.file_type").value("DOCX"))
                .andExpect(jsonPath("$.result.data.file_id").isNotEmpty());
    }

    @Test
    public void updateCloudDisk() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/updateCloudDisk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"file_id\":31,\"parent_id\":21}").getBytes()))
                .andExpect(jsonPath("$.result.data.parent_id").value(21))
                .andExpect(jsonPath("$.result.data.file_id").value(31));
    }

    @Test
    public void updateCloudDiskName() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/updateCloudDiskName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"file_id\":31,\"file_name\":\"天天向上\",\"file_type\":\"DOCX\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.file_name").value("天天向上"))
                .andExpect(jsonPath("$.result.data.file_type").value("DOCX"))
                .andExpect(jsonPath("$.result.data.file_id").value(31));
    }

    @Test
    public void deleteCloudDisk() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/deleteCloudDisk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"file_ids\":\"[{\"file_id\":\"31\",\"file_name\":\"天天向上\"}]\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void sendCloudDisk() throws Exception {
        this.mockMvc.perform(post("/cloudDiskAction/sendCloudDisk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"receive_list\":\"[{\"user_type\":\"003005\",\"group_id\":\"1038\",\"team_id\":\"0\",\"team_type\":\"011005\"}]\"" +
                        ",\"file_list\":\"[{\"file_id\":\"15\",\"file_name\":\"Wildlife.wmv\",\"file_type\":\"WMV\"}]\"}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

}