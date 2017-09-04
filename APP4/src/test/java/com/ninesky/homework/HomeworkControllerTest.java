package com.ninesky.homework;

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
public class HomeworkControllerTest extends AbstractContextControllerTests {

    private static String URI = "/homeworkAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }

    @Test
    public void addHomework() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/addHomework")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_type\":\"003005\",\n" +
                        "\"item_list\":\"[{\"content\":\"测试用例1\",\"file_list\":[{\"play_time\":1,\"file_type\":\"020010\",\"file_url\":\"https://file-test.classtao.cn/APP4/homework/17030209493919313479657.amr\",\"file_path\":\"_doc/audio/1488419363393.amr\",\"file_name\":\"1488419363393.amr\",\"file_resize_url\":\"https://file-test.classtao.cn/APP4/homework/17030209493919313479657.amr\",\"playing\":false,\"file_index\":0}]}," +
                        "{\"content\":\"测试用例2\",\"file_list\":[{\"play_time\":1,\"file_type\":\"020010\",\"file_url\":\"https://file-test.classtao.cn/APP4/homework/17030209493947648065759.amr\",\"file_path\":\"_doc/audio/1488419370686.amr\",\"file_name\":\"1488419370686.amr\",\"file_resize_url\":\"https://file-test.classtao.cn/APP4/homework/17030209493947648065759.amr\",\"playing\":false,\"file_index\":1},{\"play_time\":6,\"file_type\":\"020010\",\"file_url\":\"https://file-test.classtao.cn/APP4/homework/17030209493949381585966.amr\",\"file_path\":\"_doc/audio/1488419375230.amr\",\"file_name\":\"1488419375230.amr\",\"file_resize_url\":\"https://file-test.classtao.cn/APP4/homework/17030209493949381585966.amr\",\"playing\":false,\"file_index\":2}]}]\",\n" +
                        "\"app_type\":\"005010\",\"school_id\":1060,\"course\":\"015020\",\"user_name\":\"张威飞\",\n" +
                        "\"end_date\":\"2017-03-03 09:00\",\"title\":\"title\",\n" +
                        "\"receive_list\":\"[{\"user_type\":\"003010\",\"checked\":true,\"team_type\":\"011005\",\"group_id\":1040,\"team_id\":1224,\"team_name\":\"一年级（1）班\",\"course_name\":\"科学\",\"course\":\"015020\"}]\",\n" +
                        "\"user_id\":1353,\"client_id\":\"fc370ece6ab2a6baa4805047ab60f032\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.count_list").isNotEmpty());
    }

    @Test
    public void getHomeworkList() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/getHomeworkList")//教师端获取作业列表
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.count_list").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkList")//教师端获取作业信息
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"homework_id\":921}").getBytes()))
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.item_list").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkList")//家长端获取作业列表
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"student_id\":5305}").getBytes()))
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_submit").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkList")//家长端获取作业信息
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"student_id\":5305,\"homework_id\":921}").getBytes()))
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.create_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.item_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty());
    }

    @Test
    public void getFileList() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/getFileList")//获取整个作业的附件
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"homework_id\":921}").getBytes()))
                .andExpect(jsonPath("$.result.data.file_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.play_time").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getFileList")//获取作业子项的附件
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"homework_id\":934,\"item_id\":1236}").getBytes()))
                .andExpect(jsonPath("$.result.data.file_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.play_time").isNotEmpty());
    }

    @Test
    public void getItemList() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/getItemList")//根据作业ID获取子项目列表
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"homework_id\":934,\"user_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.item_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_list").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getItemList")//根据学生ID获取作业列表
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"student_id\":5305}").getBytes()))
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.item_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.course").isNotEmpty())
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.end_date").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty());
    }

    @Test
    public void getDoneList() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/getDoneList")//教师端获取学生完成情况
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"homework_id\":934,\"user_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getDoneList")//获取学生作业子项的完成情况
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"homework_id\":934,\"user_type\":\"003005\",\"student_id\":5305,\"item_id\":1236}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty());
    }

    @Test
    public void updateItemDone() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/updateItemDone")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"item_id\":1236,\"homework_id\":934,\n" +
                        "\"content\":\"作业完成了\",\"student_id\":5305,\"is_done\":1,\n" +
                        "\"file_list\":\"[{\\\"app_sql\\\":\\\"\\\",\\\"create_by\\\":1353,\\\"create_date\\\":1488419344000,\\\"direction\\\":0,\\\"end_time\\\":null,\\\"file_name\\\":\\\"1488419363393.amr\\\",\\\"file_resize_url\\\":\\\"https://file-test.classtao.cn/APP4/homework/17030209493919313479657.amr\\\",\\\"file_type\\\":\\\"020010\\\",\\\"file_url\\\":\\\"https://file-test.classtao.cn/APP4/homework/17030209493919313479657.amr\\\",\\\"homework_id\\\":934,\\\"id\\\":874,\\\"item_id\\\":1235,\\\"limit\\\":0,\\\"order_sql\\\":\\\"\\\",\\\"play_time\\\":1,\\\"start\\\":0,\\\"start_time\\\":null,\\\"update_by\\\":0,\\\"update_date\\\":null,\\\"version\\\":0}]\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.is_done").value(1))
                .andExpect(jsonPath("$.result.data.content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.homework_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.item_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"")));
    }

    @Test
    public void getUnreadCount() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/getUnreadCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"student_id\":5305}").getBytes()))
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
    }

    @Test
    public void addRemind() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/addRemind")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"student_id\":5305,\"student_name\":\"张威飞\",\"homework_id\":934}").getBytes()))
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{")));
    }

    @Test
    public void getHomeworkCountFromRedis() throws Exception {
        //统计行政班级
        //统计到学生
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按天统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027005\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按周统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027010\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按月统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027015\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按年统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027020\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按学期统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027025\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        //end
        //统计到班级
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按天统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027005\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按周统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027010\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按月统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027015\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按年统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027020\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按学期统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027025\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        //end
        //end
        //统计兴趣班
        //统计到学生
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按天统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027005\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按周统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027010\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按月统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027015\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按年统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027020\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按学期统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011005\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027025\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        //end
        //统计到班级
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按天统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011015\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027005\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按周统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011015\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027010\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按月统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011015\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027015\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按年统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011015\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027020\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkCountFromRedis")//按学期统计
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\"course\":\"015005\",\n" +
                        "\"team_type\":\"011015\",\"score_date\":\"2017-03-02\",\"sum_type\":\"027025\",\"team_id\":1224,\"group_id\":1040}").getBytes()))
                .andExpect(jsonPath("$.result.data.student_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty());
        //end
        //end
    }

    @Test
    public void getTableHead() throws Exception {
        this.mockMvc.perform(post("/homeworkAction/getHomeworkTableHead")//获取行政班级作业统计到学生的表头
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\n" +
                        "\"team_type\":\"011005\"}")))
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkTableHead")//获取行政班级作业统计到班级的表头
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\n" +
                        "\"team_type\":\"011005\"}")))
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkTableHead")//获取兴趣班作业统计到学生的表头
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028005\",\n" +
                        "\"team_type\":\"011015\"}")))
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty());
        this.mockMvc.perform(post("/homeworkAction/getHomeworkTableHead")//获取兴趣班作业统计到班级的表头
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"user_type\":\"003005\",\"count_type\":\"028010\",\n" +
                        "\"team_type\":\"011015\"}")))
                .andExpect(jsonPath("$.result.data.team_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.field_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sort").isNotEmpty());
    }

}