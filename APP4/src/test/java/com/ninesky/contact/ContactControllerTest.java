package com.ninesky.contact;

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
 * Created by TOOTU on 2017/2/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ContactControllerTest extends AbstractContextControllerTests {

    private static String URI = "/contactAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void getContactList() throws Exception {
        this.mockMvc.perform(post("/contactAction/getContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"class_id\":1103,\"user_id\":1353}").getBytes()))
                 .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void addContactGroup() throws Exception {
        this.mockMvc.perform(post("/contactAction/addContactGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"contact_name\":\"体育班\",\n" +
                        "\"create_by\":1353,\"create_date\":\"2015-11-24 20:11:41\",\"item_list\":\"[{\n" +
                        "\"school_id\":1030,\"user_id\":1353,\"user_type\":\"003015\",\"student_id\":543,\"first_letter\":\"WWQ\",\"all_letter\":\"WANGWENQI\",\"phone\":\"\",\"create_by\":1353,\"create_date\":\"2015-11-24 20:11:41\"},{\n" +
                        "\"school_id\":1030,\"user_id\":1353,\"user_type\":\"003015\",\"student_id\":545,\"first_letter\":\"JYT\",\"all_letter\":\"JINYUTING\",\"phone\":\"\",\"create_by\":1353,\"create_date\":\"2015-11-24 20:11:41\"}]\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_name").isNotEmpty());
    }

    @Test
    public void deleteContactGroup() throws Exception {
        this.mockMvc.perform(post("/contactAction/deleteContactGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"contact_id\":749,\"user_id\":1353}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void addContactListByGorup() throws Exception {
        this.mockMvc.perform(post("/contactAction/addContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"contact_id\":749,\"contact_name\":\"体育班\",\n" +
                        "\"create_by\":1353,\"create_date\":\"2015-11-24 20:11:41\",\"item_list\":\"[{\n" +
                        "\"school_id\":1030,\"user_id\":1353,\"user_type\":\"003015\",\"user_name\":\"王雯琪\",\"student_id\":543,\"phone\":\"\"},{\n" +
                        "\"school_id\":1030,\"user_id\":1353,\"user_type\":\"003015\",\"user_name\":\"金玉婷\",\"student_id\":545,\"phone\":\"\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void deleteContactByGorup() throws Exception {
        this.mockMvc.perform(post("/contactAction/deleteContactByGorup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"contact_id\":749,\"phone\":\"\",\"student_id\":,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty());
    }

    @Test
    public void getContactGroupList() throws Exception {
        this.mockMvc.perform(post("/contactAction/getContactGroupList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1030,\"user_type\":\"003005\",\"is_active\":1}").getBytes()))
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty());
    }

    @Test
    public void getContactListByGroup() throws Exception {
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"duty\":\"016005\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"duty\":\"016010\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"duty\":\"016015\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"duty\":\"016020\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"duty\":\"016030\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"duty\":\"016025\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getContactListByGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"contact_id\":749,\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
    }

    @Test
    public void addContact() throws Exception {
        this.mockMvc.perform(post("/contactAction/addContact")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"user_type\":\"003005\",\"contact_name\":\"篮球一队\",\"course\":\"015045010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
               
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void getContactGroupListOfManager() throws Exception {
        this.mockMvc.perform(post("/contactAction/getContactGroupListOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.count").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void getTeacherContactListByClassID() throws Exception {
        this.mockMvc.perform(post("/contactAction/getTeacherContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"class_id\":1103,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty());
    }

    @Test
    public void getTeacherListByPhone() throws Exception {
        this.mockMvc.perform(post("/contactAction/getTeacherListByUserID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"phone\":\"18257166300\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty());
    }

    @Test
    public void getParentContactList() throws Exception {
        this.mockMvc.perform(post("/contactAction/getParentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"class_id\":1103,\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_cod").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getParentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_cod").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getParentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"grade_id\":1038}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_cod").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getParentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"student_id\":5303}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_cod").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getParentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353,\"parent_name\":\"陈天\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_cod").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty());
    }

    @Test
    public void getStudentContactList() throws Exception {
        this.mockMvc.perform(post("/contactAction/getStudentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"class_id\":1103,\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").value(1030))
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").value(DictConstants.USERTYPE_STUDENT));
        this.mockMvc.perform(post("/contactAction/getStudentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").value(1030))
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").value(DictConstants.USERTYPE_STUDENT));
        this.mockMvc.perform(post("/contactAction/getStudentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"grade_id\":1039,\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").value(1030))
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").value(DictConstants.USERTYPE_STUDENT));
        this.mockMvc.perform(post("/contactAction/getStudentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_name\":\"顾欣\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").value(1030))
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").value(DictConstants.USERTYPE_STUDENT));
    }

    @Test
    public void getContactByName() throws Exception {
        this.mockMvc.perform(post("/contactAction/getContactByName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"search\":\"张\",\"school_id\":1030,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty());
    }

    @Test
    public void getTeaStudentContactList() throws Exception {
        this.mockMvc.perform(post("/contactAction/getTeaStudentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"team_type\":\"011015\",\"school_id\":1030,\"user_id\":1353,\"team_id\":749}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty());
        this.mockMvc.perform(post("/contactAction/getTeaStudentContactList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"team_type\":\"011005\",\"school_id\":1030,\"user_id\":1353,\"team_id\":1220}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty());
    }
}