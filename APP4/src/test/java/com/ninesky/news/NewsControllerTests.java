package com.ninesky.news;

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

@RunWith(SpringJUnit4ClassRunner.class)
public class NewsControllerTests extends AbstractContextControllerTests {

	private static String URI = "/newsAction/{action}";

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(this.wac).alwaysExpect(status().isOk()).build();
	}

	@Test
	public void getNews1() throws Exception {
		this.mockMvc.perform(post("/newsAction/getNews1")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{ \"news_id\": 100}".getBytes()))
					.andExpect(jsonPath("$.result.data.news_id").value(100))
					.andExpect(jsonPath("$.result.data.title").isNotEmpty())
					.andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,\"create_by\":1265,\"create_date\":1457601046000,\"")));
	}

	@Test
	public void  addNews() throws Exception {
		this.mockMvc.perform(post("/newsAction/addNews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"content\":\"<span style=\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\"><font size=\"3\">小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和</font></span><span style=\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\"><font size=\"3\">好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！</font></span><div style=\"font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\"><span style=\"color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\"><font size=\"3\"><br></font></span></div><img src=\"http://file.test.classtao.cn/APP4/default/16101715224360274260766.JPG\" style=\"max-width: 100%; font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\">\",\"title\":\"校园美术作业一览\",\"main_pic_url\":\"https://file-test.classtao.cn/APP4/news/17030611030794817189895.JPG\",\n" +
						"\"item_list\":\"[{\"content\":\"<span style=\\\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\\\"><font size=\\\"3\\\">小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和</font></span><span style=\\\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\\\"><font size=\\\"3\\\">好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！</font></span><div style=\\\"font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\\\"><span style=\\\"color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\\\"><font size=\\\"3\\\"><br></font></span></div><img src=\\\"http://file.test.classtao.cn/APP4/default/16101715224360274260766.JPG\\\" style=\\\"max-width: 100%; font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\\\">\",\"content_text\":\"小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！\"}]\",\n" +
						"\"content_text\":\"小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！\",\n" +
						"\"dict_group\":\"022005\",\"file_list\":\"[{\"file_url\":\"https://file-test.classtao.cn/APP4/news/17030611030794817189895.JPG\"}]\",\"news_code\":\"025019\",\n" +
						"\"dept_name\":\"玲玲\",\"deploy_date\":\"2017-03-06\",\"module_code\":\"009018\",\"template_type\":\"035025\",\"is_main\":0}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
	}

	@Test
	public void getNewsList() throws Exception {
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":1,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":1,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"deploy_date\":\"2017\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":1,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"search\":\"全国创\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":0,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"news_code\":\"025021\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":0,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"news_code\":\"025021\",\"search\":\"全国创\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":0,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"news_code\":\"025021\",\"deploy_date\":\"2017\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
	}

	public void getNewsListForAPP() throws Exception {
		this.mockMvc.perform(post("/newsAction/getNewsListForAPP")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"022\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_valu").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListForAPP")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"news_group\":\"022\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.code_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListForAPP")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"022005\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_valu").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListForAPP")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"news_group\":\"022005\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.code_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
	}

	public void getMainPageNewsList() throws Exception {
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":1,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":1,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"deploy_date\":\"2017\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":1,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"search\":\"全国创\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":0,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"news_code\":\"025021\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":0,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"news_code\":\"025021\",\"search\":\"全国创\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"limit\":20,\"is_resize\":0,\"is_text\":0,\"user_type\":\"003005\",\"direction\":0,\n" +
						"\"app_type\":\"005010\",\"dict_group\":\"022005\",\"school_id\":1060,\"user_name\":\"张威飞\",\n" +
						"\"start_id\":0,\"user_id\":1353,\"news_code\":\"025021\",\"deploy_date\":\"2017\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.module_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty());
	}

	@Test
	public void deleteNews() throws Exception {
		this.mockMvc.perform(post("/newsAction/deleteNews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"news_id\":574}").getBytes()))
				.andExpect(content().string(startsWith("{\"result\":{\"data\":{}")));
	}

	public void updateNews() throws Exception {
		this.mockMvc.perform(post("/newsAction/updateNews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"content\":\"<span style=\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\"><font size=\"3\">小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和</font></span><span style=\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\"><font size=\"3\">好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！：测试</font></span><div style=\"font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\"><span style=\"color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\"><font size=\"3\"><br></font></span></div><img src=\"http://file.test.classtao.cn/APP4/default/16101715224360274260766.JPG\" style=\"max-width: 100%; font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\">\",\n" +
						"\"news_id\":574,\"title\":\"校园美术作业一览:测试\",\"main_pic_url\":\"https://file-test.classtao.cn/APP4/news/17030611030794817189895.JPG\",\n" +
						"\"item_list\":\"[{\"content\":\"<span style=\\\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\\\"><font size=\\\"3\\\">小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和</font></span><span style=\\\"font-size: 13.3333339691162px; color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\\\"><font size=\\\"3\\\">好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！：测试</font></span><div style=\\\"font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\\\"><span style=\\\"color: rgb(51, 51, 51); font-family: 宋体; line-height: 32px; text-indent: 28px; background-color: rgb(247, 246, 242);\\\"><font size=\\\"3\\\"><br></font></span></div><img src=\\\"http://file.test.classtao.cn/APP4/default/16101715224360274260766.JPG\\\" style=\\\"max-width: 100%; font-family: 'Microsoft yahei'; font-size: 13.3333339691162px; line-height: 20.6349201202393px;\\\">\",\"content_text\":\"小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！：测试\"}]\",\n" +
						"\"content_text\":\"小学美术组举报了一年一度的优秀美术课堂作业展。美术课堂作业展是我校的传统项目，在我校历届举办，得到学校的大力支持和好评，将孩子们最得意的课堂作业展示出来，是对孩子们美术知识和技能学习的肯定，通过展示让孩子们对自己喜欢的作品加以评价，也是孩子们鉴赏能力和艺术评论的风采展现，通过这样的活动让孩子们在实际生活中感受美！发现美！创造美！：测试\"\n" +
						"\"file_list\":\"[{\"file_url\":\"https://file-test.classtao.cn/APP4/news/17030611030794817189895.JPG\"}]\",\"dept_name\":\"玲玲：测试\",\"deploy_date\":\"2017-03-06\",\"template_type\":\"035025\",\"is_main\":1}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content_text").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dept_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.template_type").isNotEmpty())
				.andExpect(content().string(startsWith("{\"result\":{\"data\":{\"app_sql\":null,\"order_sql\":null,")));
	}

	@Test
	public void getAPPNewsList() throws Exception {
		this.mockMvc.perform(post("/newsAction/getAPPNewsList")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"school_id\":1060,\"user_id\":1353}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_id").isNotEmpty())
				.andExpect(jsonPath("$.result.data.title").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content").isNotEmpty())
				.andExpect(jsonPath("$.result.data.content_text").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.deploy_date").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_code").isNotEmpty());
	}

	public void getNewsListOfLogin() throws Exception {
		this.mockMvc.perform(post("/newsAction/getNewsListOfLogin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_valu").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListOfLogin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"022\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_valu").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListOfLogin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"news_group\":\"022\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.code_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListOfLogin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"dict_group\":\"022005\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_valu").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.dict_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
		this.mockMvc.perform(post("/newsAction/getNewsListOfLogin")
				.contentType(MediaType.APPLICATION_JSON)
				.content(("{\"user_name\":\"张威飞\",\"user_type\":\"003005\",\"app_type\":\"005010\",\"user_id\":1353,\"school_id\":1060,\"news_group\":\"022005\"}").getBytes()))
				.andExpect(jsonPath("$.result.data.news_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_list").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_group").isNotEmpty())
				.andExpect(jsonPath("$.result.data.code_name").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_value").isNotEmpty())
				.andExpect(jsonPath("$.result.data.css_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.other_field").isNotEmpty())
				.andExpect(jsonPath("$.result.data.news_code").isNotEmpty())
				.andExpect(jsonPath("$.result.data.sort").isNotEmpty());
	}
	////@Test
	public void writeString() throws Exception {
		this.mockMvc.perform(get(URI, "string"))
			.andExpect(content().string("Wrote a string"));
	}

	////@Test
	public void readForm() throws Exception {
		this.mockMvc.perform(
				post(URI, "form")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.param("foo", "bar")
					.param("fruit", "apple"))
				.andExpect(content().string("Read x-www-form-urlencoded: JavaBean {foo=[bar], fruit=[apple]}"));
	}

	////@Test
	public void writeForm() throws Exception {
		this.mockMvc.perform(get(URI, "form"))
				.andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(content().string("foo=bar&fruit=apple"));
	}

	private static String XML =
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			"<javaBean><foo>bar</foo><fruit>apple</fruit></javaBean>";

	////@Test
	public void readXml() throws Exception {
		this.mockMvc.perform(
				post(URI, "xml")
					.contentType(MediaType.APPLICATION_XML)
					.content(XML.getBytes()))
				.andExpect(content().string("Read from XML: JavaBean {foo=[bar], fruit=[apple]}"));
	}

	////@Test
	public void writeXml() throws Exception {
		this.mockMvc.perform(get(URI, "xml").accept(MediaType.APPLICATION_XML))
				.andExpect(content().xml(XML));
	}

	////@Test
	public void readJson() throws Exception {
		this.mockMvc.perform(
				post(URI, "json")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{ \"foo\": \"bar\", \"fruit\": \"apple\" }".getBytes()))
				.andExpect(content().string("Read from JSON: JavaBean {foo=[bar], fruit=[apple]}"));
	}

	////@Test
	public void writeJson() throws Exception {
		this.mockMvc.perform(get(URI, "json").accept(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"));
	}

	////@Test
	public void writeJson2() throws Exception {
		this.mockMvc.perform(get(URI, "json").accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$.foo").value("bar"))
				.andExpect(jsonPath("$.fruit").value("apple"));
	}

	private static String ATOM_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<feed xmlns=\"http://www.w3.org/2005/Atom\"><title>My Atom feed</title></feed>";

	////@Test
	public void readAtom() throws Exception {
		this.mockMvc.perform(
				post(URI, "atom")
					.contentType(MediaType.APPLICATION_ATOM_XML)
					.content(ATOM_XML.getBytes()))
				.andExpect(content().string("Read My Atom feed"));
	}

	////@Test
	public void writeAtom() throws Exception {
		this.mockMvc.perform(get(URI, "atom").accept(MediaType.APPLICATION_ATOM_XML))
				.andExpect(content().xml(ATOM_XML));
	}

	////@Test
	public void readRss() throws Exception {

		String rss = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <rss version=\"2.0\">" +
				"<channel><title>My RSS feed</title></channel></rss>";

		this.mockMvc.perform(
				post(URI, "rss")
					.contentType(MediaType.valueOf("application/rss+xml"))
					.content(rss.getBytes()))
				.andExpect(content().string("Read My RSS feed"));
	}

	////@Test
	public void writeRss() throws Exception {

		String rss = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<rss version=\"2.0\"><channel><title>My RSS feed</title><link>http://localhost:8080/mvc-showcase/rss</link><description>Description</description></channel></rss>";

		this.mockMvc.perform(get(URI, "rss").accept(MediaType.valueOf("application/rss+xml")))
				.andExpect(content().xml(rss));
	}

}
