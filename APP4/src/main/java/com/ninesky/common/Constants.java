package com.ninesky.common;


public class Constants {
	
	public static final String DEFALHEADPATH = "http://112.124.100.26/file/APP4/user/img_user_big.jpg"; 

	/**
	 * 降序排序
	 */
	public static final String SORT_DOWN="0";
	
	/**
	 * 升序排序
	 */
	public static final String SORT_UP="1";
	
	/**
	 * 向后取数
	 */
	public static final String DIRECTION_BACK="0";

	/**
	 * 向前取数
	 */
	public static final String DIRECTION_PRE="1";
	

	/**
	 * 上拉刷新
	 */
	public static final Integer IS_PULLDOWN=1;
	
	/**
	 * 默认一个页面的开始值
	 */
	public static final Integer DEFAULT_START=0;
	
	/**
	 * 默认一个页面的数量
	 */
	public static final Integer DEFAULT_LIMIT=20;
	
	/**
	 * 默认文件显示数量
	 */
	public static final Integer DEFAULT_FILE_LIMIT=3; 
	
	/**
	 * 默认字符串的截取长度
	 */
	public static final Integer DEFAULT_SUBSTRING_LENGTH=60;
	
	/**
	 * 图片缩放的宽度
	 */
	public static final Integer PICTURE_RESIZE_WIDTH=150;
	
	/**
	 * 图片缩放的高度
	 */
	public static final Integer PICTURE_RESIZE_HEIGHT=150;
	
	/**
	 * 图片缩放的质量
	 */
	public static final float PICTURE_QUAILTY=0.60f;
	
	
	/**
	 * 图片缩放的高度
	 */
	public static final String RESIZE="RESIZE";
	
	/**
	 * 文件路径：公用，各个模块都可以存放
	 */
	public static final String FILE_PATH_DEFAULT="/file/APP4/default/";
	
	/**
	 * 文件路径：用户模块
	 */
	public static final String FILE_PATH_USER="/file/APP4/user/";
	
	/**
	 * 文件路径：评价模块
	 */
	public static final String FILE_PATH_COMMENT="/file/APP4/comment/";
	
	/**
	 * 文件路径：评价模块
	 */
	public static final String FILE_PATH_HOMEWORK="/file/APP4/homework/";
	
	/**
	 * 文件路径：模块
	 */
	public static final String FILE_PATH_MODULE="/file/APP4/module/";
	
	/**
	 * 文件路径：通知模块
	 */
	public static final String FILE_PATH_NOTICE="/file/APP4/notice/";
	
	/**
	 * 文件路径：相册模块
	 */
	public static final String FILE_PATH_PHOTO="/file/APP4/photo/";
	
	/**
	 * 文件路径：扣分模块
	 */
	public static final String FILE_PATH_SCOTE="/file/APP4/score/";
	
	/**
	 * 文件路径：课件模块
	 */
	public static final String FILE_PATH_NOTE="/file/APP4/note/";
	
	/**
	 * 文件路径：学校申请
	 */
	public static final String FILE_PATH_SCHOOL="/file/APP4/school/";
	
	/**
	 * 文件路径：APP下载地址
	 */
	public static final String FILE_PATH_APP="/file/APP4/app/";
	
	/**
	 * 文件路径：文章下载地址
	 */
	public static final String FILE_PATH_NEWS="/file/APP4/news/";
	
	/**
	 * 名人墙文件路径：文件下载地址
	 */
	public static final String FILE_PATH_FAME="/file/APP4/fame/";
	

	public static final String DEFAULT_PASSWORD = "123456";
	
	//加密的密码
	public static final String RESET_PASSWORD = "e10adc3949ba59abbe56e057f20f883e";

	/**
	 * 参数：单条新闻资讯启用模板标记
	 */
	public static final String NEWS_TEMPLATE_ON ="NEWS_TEMPLATE_ON";

	/**
	 * 是否启用标记：开启
	 */
	public static final String ON = "TRUE";


	/**
	 * 是否启用标记：关闭
	 */
	public static final String OFF = "FALSE";

	/**
	 * 分隔符 ,
	 */
	public static final String SPLIT_FLAG = ",";

	/**
	 * 数据库中是否标记：是 1
	 */
	public static final Integer TRUE_FLAG = 1;


	/**
	 * 数据库中是否标记：否 0
	 */
	public static final Integer FALSE_FLAG = 0;

	/**
	 * 相册类型：班级
	 */
	public static final String DEFAULT_CLASS_IMG_URL = "images/class_btn_class.png";

	/**
	 * 同步公众号接口
	 */
	public static final String WECHAT_API_SYNCACCOUNT_URL = "/apiController.do?doSyncAccount";
	
	/**
	 * 同步自定义菜单接口
	 */
	public static final String WECHAT_API_SYNCMENU_URL = "/apiController.do?doSyncMenu";
	
	/**
	 * 同步图文素材模板接口
	 */
	public static final String WECHAT_API_SYNC_NEWSTEMPLATE_URL = "/apiController.do?doSyncNewsTemplate";
	
	/**
	 * 删除图文素材模板接口
	 */
	public static final String WECHAT_API_SYNC_DELETE_NEWSTEMPLATE_URL = "/apiController.do?doDeleteNewsTemplate";
	
	/**
	 * 删除图文素材模板接口
	 */
	public static final String WECHAT_API_SYNC_DELETE_NEWSITEM_URL = "/apiController.do?doDeleteNewsItem";
	
	/**
	 * 同步图文素材接口
	 */
	public static final String WECHAT_API_SYNC_NEWSITEM_URL = "/apiController.do?doSyncNewsItem";
	
	/**
	 * 上传图文素材模板接口
	 */
	public static final String WECHAT_API_UPLOAD_NEWStEMPLATE_URL = "/apiController.do?doUploadNewsTemplate";
	
	/**
	 * 群发消息接口
	 */
	public static final String WECHAT_API_GROUP_MESSAGE_URL = "/apiController.do?doSendGroupMessage";
	
	/**
	 * 发布投票接口
	 */
	public static final String WECHAT_API_PUBLISH_VOTE_URL = "/apiController.do?doSyncVote";
	
	/**
	 * 获取jsapi凭证接口
	 */
	public static final String WECHAT_API_GET_JSAPI_TICKET = "/apiController.do?getJsapiTicket";
	
	/**
	 * 获取accesstoken接口
	 */
	public static final String WECHAT_API_GET_ACCESSTOKEN = "/apiController.do?getAccesstoken";
	
	/**
	 * 图文素材上传状态 （未上传）
	 */
	public static final Integer WECHAT_NEWSTEMPLATE_UPLOADSTATUS_NONE = 0;
	
	/**
	 * 图文素材上传状态 （正在上传）
	 */
	public static final Integer WECHAT_NEWSTEMPLATE_UPLOADSTATUS_ING = 1;
	
	/**
	 * 图文素材上传状态 （已上传）
	 */
	public static final Integer WECHAT_NEWSTEMPLATE_UPLOADSTATUS_SUCCESS = 2;
	
	/**
	 * 投票发布状态（未发布）
	 */
	public static final String WECHAT_VOTE_STATEMENT_UNPUBLISH = "0";
	
	/**
	 * 投票发布状态（正在发布）
	 */
	public static final String WECHAT_VOTE_STATEMENT_PUBLISHING = "1";
	
	/**
	 * 投票发布状态（已发布）
	 */
	public static final String WECHAT_VOTE_STATEMENT_PUBLISHED = "2";
	
	/**
	 * 投票活动举办地址
	 */
	public static final String WECHAT_VOTE_ACTIVITY_URL = "/weixinSurveyController.do?goSurveyVote&accountid=ACCOUNTID&surveyid=VOTEID";

	/**
	 * 投票结果预览地址
	 */
	public static final String WECHAT_VOTE_RESULT_URL = "/weixinSurveyMainController.do?goSurveyMainCalculate&id=";
	
	/**
	 * 新闻资讯栏目地址
	 */
	public static final String WECHAT_NEWS_CHANNEL_URL = "/wechatclient/news/xymh/index.html?schoolId=SCHOOLID&dictId=DICTID";
	
	/**
	 * 微信家长端地址
	 */
	public static final String WECHAT_PARENT_CLIENT_URL = "/wechatAuthAction/login?accountId=ACCOUNTID";
	
	/**
	 * SNS微信类型
	 */
	public static final String SNS_TYPE_WECHAT = "wechat";
	
	/**
	 * 微信oauth2地址
	 */
	public static final String WECHAT_OAUTH2_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
	
	/**
	 * 微信access_token请求地址
	 */
	public static final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code&appid=%s&secret=%s&code=%s";
	
	/**
	 * 微信用户信息请求地址
	 */
	public static final String WECHAT_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
	
	/**
	 * 微信模板消息id获取接口
	 */
	public static final String WECHAT_LOAD_TEMPLATEID_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
	
	/**
	 * 微信模板消息发送接口
	 */
	public static final String WECHAT_SEND_TEMPLATE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	
	/**
	 * 微信模板消息编号（私信）
	 */
	public static final String WECHAT_MESSAGE_TEMPLATE_SX = "OPENTM207719517";
	
	/**
	 * 微信模板消息编号（考勤）
	 */
	public static final String WECHAT_MESSAGE_TEMPLATE_KQ = "TM00768";
	
	/**
	 * 微信模板消息内容文字颜色
	 */
	public static final String WECHAT_MESSAGE_TEMPLATE_COLOR = "#173177";
	
	/**
	 * 微信平台地址的配置key
	 */
	public static final String WECHAT_PLATFORM_CONFIG_KEY = "WECHAT_PLATFORM_DOMAIN";
	
	/**
	 * APP4平台地址的配置key
	 */
	public static final String APP4_PLATFORM_CONFIG_KEY = "API_PLATFORM_DOMAIN";

	/**
	 *  课淘web端后台菜单配置
	 */
	public  static  final  String PARTNER_CODE = "PARTNER_CODE";

	/**
	 * 默认用户头像
	 */
	public  static  final  String HEAD_URL_DEFAULT= "https://file-test.classtao.cn/APP4/user/img_user_big.jpg";

	public  static  final  String UTF_8 = "utf-8";

	public  static  final  String JSON_TYPE = "application/json";
}
