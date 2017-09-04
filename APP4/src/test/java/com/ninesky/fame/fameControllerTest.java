package com.ninesky.fame;

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
 * Created by TOOTU on 2017/3/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class fameControllerTest extends AbstractContextControllerTests {

    private static String URI = "/fameAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addFame() throws Exception {
        this.mockMvc.perform(post("/fameAction/addFame")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"name\":\"谷健之\",\"description\":\"高级政工师。现任浙江省电力设计院副院长，院党委副书记。       \",\"head_url\":\"http://112.124.100.26/file/APP4/fame/16061618423855024623781.jpg\"," +
                        "\"birthday\":\"2011-02-09\",\"graduation_date\":\"2017-02-09\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.fame_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.birthday").isNotEmpty())
                .andExpect(jsonPath("$.result.data.graduation_date").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getFameList() throws Exception {
        this.mockMvc.perform(post("/fameAction/getFameList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"fame_id\":20,\"user_id\":1353,\"school_id\":1030}").getBytes()))
                .andExpect(jsonPath("$.result.data.fame_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.birthday").isNotEmpty())
                .andExpect(jsonPath("$.result.data.graduation_date").isNotEmpty());
    }

    @Test
    public void getFameListForWeb() throws Exception {
        this.mockMvc.perform(post("/fameAction/getFameListForWeb")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1030,\"search\":\"副书记\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.fame_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.birthday").isNotEmpty())
                .andExpect(jsonPath("$.result.data.graduation_date").isNotEmpty());
        this.mockMvc.perform(post("/fameAction/getFameListForWeb")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1030}").getBytes()))
                .andExpect(jsonPath("$.result.data.fame_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.birthday").isNotEmpty())
                .andExpect(jsonPath("$.result.data.graduation_date").isNotEmpty());
    }

    @Test
    public void deleteFame() throws Exception {
        this.mockMvc.perform(post("/fameAction/deleteFame")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"fame_id\":20,\"user_id\":1353,\"school_id\":1030}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void updateFame() throws Exception {
        this.mockMvc.perform(post("/fameAction/updateFame")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"fame_id\":20,\"school_id\":1030,\"name\":\"谷健之\",\"description\":\"高级政工师。现任浙江省电力设计院副院长，院党委副书记。\",\"head_url\":\"http://112.124.100.26/file/APP4/fame/16061618423855024623781.jpg\"," +
                        "\"birthday\":\"2011-02-09\",\"graduation_date\":\"2017-02-09\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.fame_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.description").isNotEmpty())
                .andExpect(jsonPath("$.result.data.birthday").isNotEmpty())
                .andExpect(jsonPath("$.result.data.graduation_date").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

}