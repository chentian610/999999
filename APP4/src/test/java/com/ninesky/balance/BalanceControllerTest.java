package com.ninesky.balance;

import com.ninesky.AbstractContextControllerTests;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by TOOTU on 2017/4/1.
 */
public class BalanceControllerTest extends AbstractContextControllerTests {
    private static String URI = "/balanceAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }
    @Test
    public void getBalanceByPhone() throws Exception {
        this.mockMvc.perform(post("/balanceAction/getBalanceByPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.balance_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.balance").isNotEmpty());
    }

    @Test
    public void rechargeBalance() throws Exception {
        this.mockMvc.perform(post("/balanceAction/consumptionBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"balance\":1000,\"consumption_money\":200,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void consumptionBalance() throws Exception {
        this.mockMvc.perform(post("/balanceAction/rechargeBalance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"balance\":1000,\"consumption_money\":200,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

}