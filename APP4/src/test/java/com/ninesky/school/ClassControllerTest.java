package com.ninesky.school;


import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ninesky.AbstractContextControllerTests;

/**
 * Created by TOOTU on 2017/3/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ClassControllerTest extends AbstractContextControllerTests{


    private static String URI = "/classAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    //@Test
    //public void addClass() throws Exception {
    //
    //}

    //@Test
    //public void getClassListBySchool() throws Exception {
    //
    //}

    //@Test
    //public void updateClassList() throws Exception {
    //
    //}

    @Test
    public void getClassListByTeacher() throws Exception {
        this.mockMvc.perform(post("/classAction/getClassListOfTeacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003010\",\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phon").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_lette").isNotEmpty());
    }

    @Test
    public void getChildOfParent() throws Exception {
        this.mockMvc.perform(post("/classAction/getChildOfParent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003010\",\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.parent_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.parent_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.relation").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_lette").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_list").isNotEmpty());
    }

    //@Test
    //public void getClassBySchoolID() throws Exception {
    //
    //}

    @Test
    public void updateClass() throws Exception {
        this.mockMvc.perform(post("/classAction/updateClass")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"class_id\":1045,\"school_id\":1006,\"grade_id\":1007,\"class_code\":\"989890\",\"school_type\":\"002010\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.checked").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_code").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getClassList() throws Exception {
        this.mockMvc.perform(post("/classAction/getClassList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"class_id\":1224,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.enrollment_year").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_num").isNotEmpty())
                .andExpect(jsonPath("$.result.data.checked").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.img_url").isNotEmpty());
    }

    @Test
    public void getTeacherList() throws Exception {
        this.mockMvc.perform(post("/classAction/getTeacherList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty());
    }

    @Test
    public void getStudentList() throws Exception {
        this.mockMvc.perform(post("/classAction/getStudentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void getStudentListByStuName() throws Exception {
        this.mockMvc.perform(post("/classAction/getStudentListByStuName")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_name\":\"陈锦浩\",\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
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
    public void addStudent() throws Exception {
        this.mockMvc.perform(post("/classAction/addStudent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"grade_id\":1040,\"class_id\":1224,\"student_name\":\"张威飞\",\"student_code\":\"0000925685\",\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.all_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.first_letter").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void addStudentList() throws Exception {
        this.mockMvc.perform(post("/classAction/addStudentList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"item_list\":\"[{\n" +
                        "\"user_id\":1353,\"school_id\":1060,\"grade_id\":1040,\"class_id\":1224,\"stu\n" +
                        "dent_code\":\"0000925683\",\"student_name\":\"陈锦浩\",\"sex\":0},{\n" +
                        "\"user_id\":1353,\"school_id\":1060,\"grade_id\":1040,\"class_id\":1224,\"stu\n" +
                        "dent_code\":\"0000926145\",\"student_name\":\"陈佳俊\",\"sex\":0}]\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.msg").isNotEmpty());
    }

    @Test
    public void getGroupList() throws Exception {
        this.mockMvc.perform(post("/classAction/getGroupList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"user_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.group_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty());
    }

    @Test
    public void getTeacherListOfManager() throws Exception {
        this.mockMvc.perform(post("/classAction/getTeacherListOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"teacher_name\":\"张威飞\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty());
    }

    @Test
    public void getTeacherListOfManagerGroup() throws Exception {
        this.mockMvc.perform(post("/classAction/getTeacherListOfManagerGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"teacher_name\":\"张威飞\",\"contact_id\":947}").getBytes()))
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty());
    }

    @Test
    public void showDutyNameOfSome() throws Exception {
        this.mockMvc.perform(post("/classAction/getTeacherListOfManagerGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"phone\":\"18257166300\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.teacher_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty())
                .andExpect(jsonPath("$.result.data.teacher_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.phone").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_confirm").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_graduate").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_charge").isNotEmpty())
                .andExpect(jsonPath("$.result.data.duty_name").isNotEmpty());
    }

    @Test
    public void getTeacherListOfGroup() throws Exception {
        this.mockMvc.perform(post("/classAction/getTeacherListOfManagerGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"user_type\":\"003010\",\"contact_id\":947}").getBytes()))
                .andExpect(jsonPath("$.result.data.contact_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
    }

    @Test
    public void getStudentListOfManager() throws Exception {
        this.mockMvc.perform(post("/classAction/getStudentOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty());
    }

    @Test
    public void getStudentListOfManagerGroup() throws Exception {
        this.mockMvc.perform(post("/classAction/getStudentListOfManagerGroup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"student_name\":\"张\",\"class_id\":1224}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sex").isNotEmpty());
    }

    @Test
    public void getClassOfManager() throws Exception {
        this.mockMvc.perform(post("/classAction/getClassOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.enrollment_year").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_num").isNotEmpty())
                .andExpect(jsonPath("$.result.data.checked").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.img_url").isNotEmpty());
    }

    @Test
    public void addSomeClass() throws Exception {
        this.mockMvc.perform(post("/classAction/addSomeClass")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"enrollment_year\":2007,\"class_num\":3}").getBytes()))
                .andExpect(jsonPath("$.result.data.picture_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.add_date").isNotEmpty());
    }

    @Test
    public void updateClassOfManager() throws Exception {
        this.mockMvc.perform(post("/classAction/updateClassOfManager")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"enrollment_year\":\"2007\",\"class_num\":4,,\"grade_num\":4}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

    //@Test
    //public void delete() throws Exception {
    //    this.mockMvc.perform(post("/classAction/delete")
    //            .contentType(MediaType.APPLICATION_JSON)
    //            .content(("{}").getBytes()));
    //}

    //@Test
    //public void classroomjilv() throws Exception {
    //
    //}
    //
    //@Test
    //public void classroomws() throws Exception {
    //
    //}
    //
    //@Test
    //public void classroomjiancha() throws Exception {
    //
    //}

    @Test
    public void setClassIsGraduateByGradeID() throws Exception {
        this.mockMvc.perform(post("/classAction/setClassIsGraduateByGradeID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"grade_id\":1046}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{},")));
    }

}