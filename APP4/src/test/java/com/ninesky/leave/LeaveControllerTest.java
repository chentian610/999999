package com.ninesky.leave;

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
public class LeaveControllerTest extends AbstractContextControllerTests {

    private static String URI = "/leaveAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/addLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"content\":\"因为身体不适，请假去医院全面体检一次\",\"user_name\":\"张威飞\",\"end_date\":\"2017-03-03 15:00\",\n" +
                        "\"leave_type\":\"033002\",\"is_change_course\":1,\"user_type\":003005,\"app_type\":005010,\n" +
                        "\"approver_id\":1382,\"user_id\":1353,\n" +
                        "\"change_teacher_list\":\"[{\"user_id\":1276,\"user_name\":\"小乔老师\",\"head_url\":\"http://file-test.classtao.cn/APP4/user/16121414471121861929544.jpg\",\"phone\":\"18317893352\",\"user_type\":\"003005\",\"is_agree\":0},{\"user_id\":1382,\"user_name\":\"赵亮\",\"head_url\":\"http://file-test.classtao.cn/APP4/notice/161103093242100712429172.jpg\",\"phone\":\"18626865290\",\"user_type\":\"003005\",\"is_agree\":0}]\"\n" +
                        "\"client_id\":\"fc370ece6ab2a6baa4805047ab60f032\",\"start_date\":\"2017-03-02 15:00\",\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/addLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"content\":\"因为身体不适，请假去医院全面体检一次\",\"user_name\":\"张威飞\",\"end_date\":\"2017-03-03 15:00\",\n" +
                        "\"leave_type\":\"033002\",\"is_change_course\":0,\"user_type\":003005,\"app_type\":005010,\n" +
                        "\"approver_id\":1382,\"user_id\":1353,\n" +
                        "\"client_id\":\"fc370ece6ab2a6baa4805047ab60f032\",\"start_date\":\"2017-03-02 15:00\",\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty());
    }

    @Test
    public void getLeaveListOfMine() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfMine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfMine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\": 151,\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfMine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"034005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfMine")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_type\":\"033005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void getLeaveListOfApprove() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\": 151,\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"034010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_type\":\"033005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"033005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApprove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"034010,034015\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void getLeaveListHaveApproved() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getLeaveListHaveApproved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListHaveApproved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\": 151,\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListHaveApproved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"034010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListHaveApproved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_type\":\"033005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListHaveApproved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"033005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListHaveApproved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_name\":\"张威飞\",\"start_id\":0,\"user_id\":1353,\"leave_status\":\"034010,034015\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void getLeaveFlow() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getLeaveFlow")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\": 151,\"school_id\":1060,\"user_name\":\"张威飞\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.pre_statu").isNotEmpty())
                .andExpect(jsonPath("$.result.data.current_leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.oper_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.oper_name").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void doChangeCourse() throws Exception {
        this.mockMvc.perform(post("/leaveAction/doChangeCourse")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"is_agree\":1,\"content\":\"好的\",\"school_id\":1060,\"leave_id\":126,\"user_id\":1353,\"version\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void sendLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/sendLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\":152,\"school_id\":1060,\"user_id\":1353,\"leave_status\":\"034010\",\"receive_id\":1353,\"version\":0}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void changeAuther() throws Exception {
        this.mockMvc.perform(post("/leaveAction/changeApprover")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\":152,\"school_id\":1060,\"user_id\":1353,\"leave_status\":\"034010\",\"master_id\":1353,\"version\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void authLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/authLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\":152,\"school_id\":1060,\"user_id\":1353,\"leave_status\":\"034020\",\"master_id\":1353,\"version\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void passLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/passLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\":152,\"school_id\":1060,\"user_id\":1353,\"leave_status\":\"034030\",\"master_id\":1353,\"version\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void deleteLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/deleteLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_id\":152,\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data").value("删除成功!"));
    }

    @Test
    public void updateLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/updateLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"leave_status\":\"034005\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"approver_id\":1353,\n" +
                        "\"leave_id\":152,\"school_id\":1060,\"approver_name\":\"张威飞\",\"content\":\"参加别人婚礼，我结婚\",\n" +
                        "\"user_name\":\"张威飞\",\"end_date\":\"2017-03-02 15:00\",\"leave_type\":\"033004\",\"is_change_course\":0,\n" +
                        "\"user_id\":1353,\"start_date\":\"2017-03-02 15:00\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.approver_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void cancelLeave() throws Exception {
        this.mockMvc.perform(post("/leaveAction/cancelLeave")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"leave_status\":\"034005\",\"user_type\":\"003005\",\n" +
                        "\"user_id\":1353,\"leave_id\":152,\"school_id\":1060,\"version\":3}").getBytes()))
                .andExpect(jsonPath("$.result.data.version").value(4));
    }

    @Test
    public void getUntreatedCount() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getUntreatedCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"leave_status\":\"034010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getUntreatedCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"leave_status\":\"034010,034015\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
    }
    
    @Test
    public void getLeaveSumAndTotal() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getLeaveSumAndTotal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.total_count").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_count").isNotEmpty());
    }

    @Test
    public void getLeaveListOfApproveOfSomeDay() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApproveOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hours_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_days_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_counts").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApproveOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"start_date\":\"2017-03-01\",\"end_date\":\"2017-03-31\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hours_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_days_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_counts").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApproveOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"start_date\":\"2017-03-01\",\"end_date\":\"2017-03-31\",\"leave_type\":\"033002\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hours_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_days_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_counts").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getLeaveListOfApproveOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"start_date\":\"2017-03-01\",\"end_date\":\"2017-03-31\",\"leave_type\":\"033002\",\"create_by\":1312}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hours_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_days_sum").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_counts").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty());
    }

    @Test
    public void getSomeTeaLeaveListOfSomeDay() throws Exception {
        this.mockMvc.perform(post("/leaveAction/getSomeTeaLeaveListOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_day").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hour").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getSomeTeaLeaveListOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"start_date\":\"2017-03-01\",\"end_date\":\"2017-03-31\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_day").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hour").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getSomeTeaLeaveListOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"start_date\":\"2017-03-01\",\"end_date\":\"2017-03-31\",\"leave_type\":\"033002\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_day").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hour").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty());
        this.mockMvc.perform(post("/leaveAction/getSomeTeaLeaveListOfSomeDay")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"school_id\":1060,\"user_id\":1353,\"start_date\":\"2017-03-01\",\"end_date\":\"2017-03-31\",\"leave_type\":\"033002\",\"create_by\":1312}").getBytes()))
                .andExpect(jsonPath("$.result.data.leave_day").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_hour").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.leave_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.start_date").isNotEmpty());
    }
}