package com.ninesky.login;

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
 * Created by TOOTU on 2017/3/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class LoginControllerTest extends AbstractContextControllerTests {

    private static String URI = "/leaveAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void login() throws Exception {
        this.mockMvc.perform(post("/loginAction/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"phone\":\"18257166300\",\"pass_word\":\"e10adc3949ba59abbe56e057f20f883e\",\n" +
                        "\"user_type\":\"003005\",\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.pass_word").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
               .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void admlogin() throws Exception {
        this.mockMvc.perform(post("/loginAction/admlogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"phone\":\"18257166300\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void lezhiWebLogin() throws Exception {
        this.mockMvc.perform(post("/loginAction/LezhiWebLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18857169874\",\"pass_word\":\"e10adc3949ba59abbe56e057f20f883e\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.pass_word").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void Login() throws Exception {
        this.mockMvc.perform(post("/loginAction/Login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003020\",\"phone\":\"18857169874\",\"school_id\":1030,\"time\":1488446169162}").getBytes()))
                .andExpect(jsonPath("$.result.data.view").isNotEmpty());
    }

    @Test
    public void emptySession() throws Exception {
        this.mockMvc.perform(post("/loginAction/emptySession")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18857169874\"}").getBytes()))
                .andExpect(jsonPath("$.result.total").value(0));
    }

    @Test
    public void loginout() throws Exception {
        this.mockMvc.perform(post("/loginAction/loginout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"app_type\":\"005010\",\"client_id\":\"fc370ece6ab2a6baa4805047ab60f032\",\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.total").value(0));
    }

    @Test
    public void sign() throws Exception {
        this.mockMvc.perform(post("/loginAction/sign")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.total").value(0));
    }

    ////@Test
    //public void checkAppVerson() throws Exception {
    //    this.mockMvc.perform(post("/loginAction/checkAppVerson")
    //            .contentType(MediaType.APPLICATION_JSON)
    //            .content(("{}").getBytes()));
    //}

}