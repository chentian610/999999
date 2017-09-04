package com.ninesky.user;

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
public class UserControllerTest extends AbstractContextControllerTests {

    private static String URI = "/userAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getValidateCode() throws Exception {
        this.mockMvc.perform(post("/userAction/getValidateCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").value("18257166300"))
                .andExpect(jsonPath("$.result.data.validate_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_use").value(0))
                .andExpect(jsonPath("$.result.data.update_by").isNotEmpty())
                .andExpect(jsonPath("$.result.data.update_date").isNotEmpty());
    }

    @Test
    public void getValidateCodePhone() throws Exception {
        this.mockMvc.perform(post("/userAction/getValidateCodePhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").value("18257166300"))
                .andExpect(jsonPath("$.result.data.validate_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_use").value(0))
                .andExpect(jsonPath("$.result.data.update_by").isNotEmpty())
                .andExpect(jsonPath("$.result.data.update_date").isNotEmpty());
    }

    @Test
    public void checkValidateCode() throws Exception {
        this.mockMvc.perform(post("/userAction/checkValidateCode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"validate_code\":\"471523\"}").getBytes()))
                .andExpect(jsonPath("$.result.data").value(null));
    }

    @Test
    public void getUserInfo() throws Exception {
        this.mockMvc.perform(post("/userAction/getUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").value(0))
                .andExpect(jsonPath("$.result.data.phone").value("18257166300"))
                .andExpect(jsonPath("$.result.data.pass_word").isNotEmpty())
                .andExpect(jsonPath("$.result.data.status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty());
    }

    @Test
    public void resetPassword() throws Exception {
        this.mockMvc.perform(post("/userAction/resetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"validate_code\":\"471523\",\"pass_word\":\"e10adc3949ba59abbe56e057f20f883e\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").value("密码重置成功"));
    }

    @Test
    public void updatePassword() throws Exception {
        this.mockMvc.perform(post("/userAction/updatePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"pass_word_old\":\"e10adc3949ba59abbe56e057f20f883e\",\"pass_word\":\"e10adc3949ba59abbe56e057f20f883e\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void forgetPassword() throws Exception {
        this.mockMvc.perform(post("/userAction/forgetPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"validate_code\":\"471523\",\"pass_word\":\"e10adc3949ba59abbe56e057f20f883e\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").value("18257166300"))
                .andExpect(jsonPath("$.result.data.pass_word").value("e10adc3949ba59abbe56e057f20f883e"))
                .andExpect(jsonPath("$.result.data.validate_code").value("471523"));
    }

    @Test
    public void modifyPhone() throws Exception {
        this.mockMvc.perform(post("/userAction/resetPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"validate_code\":\"471523\",\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").value("18257166300"))
                .andExpect(jsonPath("$.result.data.validate_code").value("471523"));
    }

    @Test
    public void completeUserInfo() throws Exception {
        this.mockMvc.perform(post("/userAction/updateUserInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.pass_word").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_list").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void insertTeacher() throws Exception {
        this.mockMvc.perform(post("/userAction/addTeacherDuty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"course\":\"015025\",\"class_id\":1106,\"phone\":\"18257166300\",\n" +
                        "\"sex\":0,\"duty\":\"016005\",\"teacher_name\":\"张威飞\",\n" +
                        "\"is_charge\":0,\"grade_id\":1006,\"class_name\":\"三年级（1）班\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty());
    }

    @Test
    public void insertParent() throws Exception {
        this.mockMvc.perform(post("/userAction/addParentChild")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"class_id\":1224,\"user_type\":\"003015\",\n" +
                        "\"student_name\":\"唐雅琪\",\"student_id\":5331,\"student_code\":\"0000930879\",\n" +
                        "\"relation\":\"003015035\",\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty());
    }

    @Test
    public void updateParentChild() throws Exception {
        this.mockMvc.perform(post("/userAction/updateChildInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"class_id\":1224,\"user_type\":\"003015\",\"student_code\":\"0000930879\",\n" +
                        "\"relation\":\"003015035\",\"user_id\":1353,\"school_id\":1060,\"student_sex\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty());
    }

    @Test
    public void bindSnsAccount() throws Exception {
        this.mockMvc.perform(post("/userAction/bindSnsAccount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"sns_type\":\"wechat\",\"account\":\"oYca8uIIktwYA7IrnxdbfL4tTEpo\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sns_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sns_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.account").isNotEmpty());
    }

    @Test
    public void getSnsAccountList() throws Exception {
        this.mockMvc.perform(post("/userAction/getSnsAccountList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1329}")))
                .andExpect(jsonPath("$.result.data.sns_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sns_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.account").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getStudentPosistionOfBed() throws Exception {
        this.mockMvc.perform(post("/userAction/getStudentPosistionOfBed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"bedroom_id\":81}")))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.bed_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.bedroom_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.bedroom_name").isNotEmpty());
    }

    @Test
    public void deleteTeacherDuty() throws Exception {
        this.mockMvc.perform(post("/userAction/deleteTeacherDuty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"teacher_id\":29397}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty());
    }

    @Test
    public void showClassOfGrade() throws Exception {
        this.mockMvc.perform(post("/userAction/showClassOfGrade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"grade_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void addTeacher() throws Exception {
        this.mockMvc.perform(post("/userAction/addTeacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"item_list\":\"[{\"grade_id\":1038,\"class_id\":1218,\"course\":\"015035\",\"duty\":\"016005\",\"is_charge\":0," +
                        "\"teacher_name\":\"张天天\",\"phone\":\"18257164892\",\"class_name\":\"一年级（3）班\",\"sex\":\"0\"}]\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":[],")));

    }

    @Test
    public void addTeacherList() throws Exception {
        this.mockMvc.perform(post("/userAction/addTeacherList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"item_list\":\"[{\"grade_id\":1038,\"class_id\":1218,\"course\":\"015035\",\"duty\":\"016005\",\"is_charge\":0," +
                        "\"teacher_name\":\"张天天\",\"phone\":\"18257164892\",\"class_name\":\"一年级（3）班\",\"sex\":\"0\"}]\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteStudent() throws Exception {
        this.mockMvc.perform(post("/userAction/deleteStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":543}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateTeacherConfirmFlag() throws Exception {
        this.mockMvc.perform(post("/userAction/updateTeacherConfirmFlag")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"teacher_id\":29398}").getBytes()))
                .andExpect(jsonPath("$.result.data").value("已成功确认教师身份！"));
    }

    @Test
    public void updateTeacherDuty() throws Exception {
        this.mockMvc.perform(post("/userAction/updateTeacherDuty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"teacher_id\":29398,\"duty\":\"016005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty());
    }

    @Test
    public void updateTeacherDutyOfManager() throws Exception {
        this.mockMvc.perform(post("/userAction/updateTeacherDutyOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"course\":\"015\",\"class_id\":0,\"phone\":\"13216106626\",\"duty\":\"016010\",\n" +
                        "\"is_charge\":0,\"grade_id\":1038,\"teacher_id\":28762,\"class_name\":\"一年级\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty());
    }

    @Test
    public void deleteChild() throws Exception {
        this.mockMvc.perform(post("/userAction/deleteChild")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"user_type\":\"003015\",\"user_id\":1353,\"school_id\":1060,\"parent_id\":2930}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteTeacherOfManager() throws Exception {
        this.mockMvc.perform(post("/userAction/deleteTeacherOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateTeacherNamePhone() throws Exception {
        this.mockMvc.perform(post("userAction//updateTeacherNamePhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"user_type\":\"003015\",\"user_id\":1353,\"school_id\":1060,\"phone\":\"18257166300\",\"teacher_name\":\"张威飞\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateStudent() throws Exception {
        this.mockMvc.perform(post("/userAction/updateStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"sex\":0,\"student_name\":\"陈鑫\",\"student_code\":\"121212\",\"student_id\":5301}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getParentOfMng() throws Exception {
        this.mockMvc.perform(post("/userAction/getParentOfMng")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_name\":\"陈\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));

    }

    @Test
    public void addParent() throws Exception {
        this.mockMvc.perform(post("/userAction/addParent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"student_name\":\"陈鑫\",\"relation\":\"003015035\",\n" +
                        "\"student_code\":\"121212\",\"parent_name\":\"张文\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void deleteAllParent() throws Exception {
        this.mockMvc.perform(post("/userAction/deleteAllParent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_code\":\"121212\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void updateParent() throws Exception {
        this.mockMvc.perform(post("/userAction/updateParent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"13668176115\",\"relation\":\"003015005\",\"parent_name\":\"林老师\",\"parent_id\":2870\n}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void getStudentListGroup() throws Exception {
        this.mockMvc.perform(post("/userAction/getStudentListGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"limit\":10,\"user_name\":\"\",\"contact_id\":749,\"user_type\":\"003010\",\"start_id\":0,\"page\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void updateLetter() throws Exception {
        this.mockMvc.perform(post("/userAction/updateLetter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003005\"}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    @Test
    public void resetAgentPassword() throws Exception {
        this.mockMvc.perform(post("/userAction/ResetAgentPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"validate_code\":\"471523\",\"pass_word\":\"e10adc3949ba59abbe56e057f20f883e\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").value("密码重置成功"));
    }

    @Test
    public void addUserRole() throws Exception {
        this.mockMvc.perform(post("/userAction/addUserRole")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"school_id\":1030}").getBytes()))
                .andExpect(jsonPath("$.result.data").value(null));
    }

    @Test
    public void getAllTeacher() throws Exception {
        this.mockMvc.perform(post("/userAction/getAllTeacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty());
    }

    @Test
    public void getStudent() throws Exception {
        this.mockMvc.perform(post("/userAction/getStudentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_name\":\"陈\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty());
    }

    @Test
    public void getStudentInformationByID() throws Exception {
        this.mockMvc.perform(post("/userAction/getStudentInformationByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":5302}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty());
    }

    @Test
    public void getTeacherListByPhone() throws Exception {
        this.mockMvc.perform(post("/userAction/getTeacherListByPhone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getTeacherList() throws Exception {
        this.mockMvc.perform(post("/userAction/getTeacherList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

}