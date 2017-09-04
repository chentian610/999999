package com.ninesky.menu;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
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
 * Created by TOOTU on 2017/3/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MenuControllerTest extends AbstractContextControllerTests {

    private static String URI = "/menuAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getParentMenuList() throws Exception {
        this.mockMvc.perform(post("/menuAction/getParentMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003020\",\"is_active\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.menu_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.menu_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.title_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.css_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.children_list").isNotEmpty());
        this.mockMvc.perform(post("/menuAction/getParentMenuList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003020\",\"is_active\":1,\"parent_id\":5}").getBytes()))
                .andExpect(jsonPath("$.result.data.menu_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.menu_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.title_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty())
                .andExpect(jsonPath("$.result.data.css_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.children_list").isNotEmpty());
    }

}