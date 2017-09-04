package com.ninesky.notice;

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
 * Created by TOOTU on 2017/3/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class NoticeControllerTest extends AbstractContextControllerTests {

    private static String URI = "/noticeAction/{action}";

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
    }


    @Test
    public void getNoticeListOld() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeListOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1030,\"user_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_conten").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_status").isNotEmpty());
    }

    @Test
    public void getNoticeList() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"limit\":20,\"user_name\":\"张威飞\",\"start_id\":0,\"user_type\":\"003005\",\n" +
                        "\"direction\":1,\"app_type\":\"005010\",\"user_id\":1353,\"module_code\":\"009001\",\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.team_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.count_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_conten").isNotEmpty());
    }

    @Test
    public void getNoticeFileByID() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeFileByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":636,\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_resize_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_type").isNotEmpty());
    }

    @Test
    public void replyNotice() throws Exception {
        this.mockMvc.perform(post("/noticeAction/replyNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"receive_id\":1312,\"receive_type\":\"003005\",\"notice_id\":1551,\"school_id\":1060,\n" +
                        "\"user_id\":1312,\"user_type\":\"003005\",\"reply_content\":\"收到\",\"reply_time\":\"2017-02-15 09:17:00\",\n" +
                        "\"head_url\":\"test.classtao.cn/APP4/user/17011316424948672593326.jpg\",\"is_read\":0}").getBytes()))
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_read").isNotEmpty())
                .andExpect(jsonPath("$.result.data.file_type").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void addNotice() throws Exception {
        this.mockMvc.perform(post("/noticeAction/addNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_name\":\"张威飞\",\"receive_list\":\"[{\"user_type\":\"003010\",\"checked\":true,\"team_type\":\"011005\",\"group_id\":1040,\"team_id\":1224,\"team_name\":\"一年级（1）班\"},{\"user_type\":\"003010\",\"checked\":true,\"team_type\":\"011005\",\"group_id\":1044,\"team_id\":1239,\"team_name\":\"五年级（4）班\"}]\",\n" +
                        "\"user_type\":\"003005\",\"notice_title\":\"天天向上\",\"app_type\":\"005010\",\"notice_content\":\"今天是一个普天同庆的好日子\",\"user_id\":1353,\"module_code\":\"009001\",\"school_id\":1060}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_list").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void addSchoolNotice() throws Exception {
        this.mockMvc.perform(post("/noticeAction/addSchoolNotice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"user_id\":1353,\"school_id\":1060,\"user_type\":\"003005\",\"sender_id\":1353,\"module_code\":\"009001\",\"notice_title\":\"安全讲座\",\n" +
                        "\"notice_content\":\"今天下午3点学校大礼堂举行安全知识讲座请各位老师及部分参加的同学准时参加，切勿迟到，谢谢。\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.school_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_content").isNotEmpty())
                .andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
    }

    @Test
    public void getNoticeReceiveByIDOld() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeReceiveByIDOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":636,\"school_id\":1030,\"user_id\":1312}").getBytes()))
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
    }

    @Test
    public void getNoticeReceiveByID() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeReceiveByID")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("\"module_code\":\"009001\",\"notice_id\":1561,\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.count").isNotEmpty());
    }

    @Test
    public void getUnreadCountOld() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getUnreadCountOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"student_id\":6010,\"school_id\":1060,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getUnreadCount() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getUnreadCount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"school_id\":1060,\"user_id\":1353,\"module_code\":\"009001\"}").getBytes()))
                .andExpect(jsonPath("$.result.data").isNotEmpty());
    }

    @Test
    public void getNoticeReplyListOld() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeReplyListOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":1551,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.reply_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
        this.mockMvc.perform(post("/noticeAction/getNoticeReplyListOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":1551,\"user_id\":1353,\"receive_id\":1312,\"receive_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.reply_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void getNoticeReplyList() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeReplyList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":1551,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_type").isNotEmpty());
        this.mockMvc.perform(post("/noticeAction/getNoticeReplyListOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":1551,\"user_id\":1353,\"receive_id\":1312,\"receive_type\":\"003005\"}").getBytes()))
                .andExpect(jsonPath("$.result.data.reply_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.reply_time").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void getUnreadUserListOld() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getUnreadUserListOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":1551,\"user_id\":1353}").getBytes()))
                 .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void getUnreadUserList() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getUnreadUserList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":1551,\"user_id\":1353}").getBytes()))
                .andExpect(jsonPath("$.result.data.user_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.head_url").isNotEmpty());
    }

    @Test
    public void getNoticeByIdOld() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeByIdOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":636,\"user_id\":1353,\"school_id\":1030,\"user_type\":003010}").getBytes()))
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_read").isNotEmpty());
        this.mockMvc.perform(post("/noticeAction/getNoticeByIdOld")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":636,\"user_id\":1353,\"school_id\":1030,\"user_type\":003005}").getBytes()))
                .andExpect(jsonPath("$.result.data.receive_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.grade_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.class_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.student_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.receive_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_name").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.send_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_status").isNotEmpty())
                .andExpect(jsonPath("$.result.data.is_read").isNotEmpty());
    }

    @Test
    public void getNoticeById() throws Exception {
        this.mockMvc.perform(post("/noticeAction/getNoticeById")
                .contentType(MediaType.APPLICATION_JSON)
                .content(("{\"notice_id\":636,\"user_id\":1353,\"school_id\":1030}").getBytes()))
                .andExpect(jsonPath("$.result.data.notice_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.user_type").isNotEmpty())
                .andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
                .andExpect(jsonPath("$.result.data.sender_id").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_title").isNotEmpty())
                .andExpect(jsonPath("$.result.data.notice_content").isNotEmpty())
                .andExpect(jsonPath("$.result.data.readCount").isNotEmpty())
                .andExpect(jsonPath("$.result.data.replyCount").isNotEmpty());
    }

}