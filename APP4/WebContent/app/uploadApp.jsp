<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
	String dict_group = "";
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>" target="_blank">  
<title>发布APP</title>
<meta content="text/html; charset=utf-8">
	<link href="app/css/bootstrap.min.css" rel="stylesheet">
	<link href="hplus/css/font-awesome.min.css" rel="stylesheet" />
	<link href="hplus/css/animate.min.css" rel="stylesheet">
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
	<link rel="stylesheet" type="text/css" href="app/css/history.css" />
	<link rel="stylesheet" type="text/css" href="app/css/manage.css" />
	<link rel="stylesheet" type="text/css" href="app/css/build.css"/>
	<link rel="stylesheet" type="text/css" href="app/css/update.css?d=${time}" />
	<link rel="stylesheet" type="text/css" href="app/css/update2.css?d=${time}" />
</head>
<body>
	<div class="container ">
		<div class="form-inline qtop hidden_title">
			<div class="form-group ftop-serach">
				<span class="glyphicon glyphicon-search form-control-feedback "></span>
				<input type="search" class="form-control search_school_name"  placeholder="搜索" >
			</div>
			<div class="form-group">
				<button class="btn btn-default qbb-search btn_search">搜索</button>
			</div>
			<select class="qbb-select pull-right school_select">
				<option value="">全部</option>
				<option value="007025">已上线</option>
				<option value="007030">已下线</option>
				<option value="-1">定向更新</option>
			</select>
		</div>
		<div class="get_school_title"></div>
		<div class="top-line"></div>
		<ul class="list-group qcontent" id="school_list">
			<li class="list-group-item">
				<div class="media">
					<div class="media-body school-info">
						<p><span style="color: #4a4a4a;">1: 东方红小学</span><span>（1047274625）</span></p>
						<p style="color: #4a4a4a;margin-left: 20px;">上次更新：2017-1-23 16:00   版本号：232342425252355</p>
						<button class="btn btn-default btn-update">局部</button>
						<button class="btn btn-default btn-update">IOS＋Android</button>
					</div>
					<div class="media-right media-middle">
						<button class="btn btn-default btn-enabled history_version" style="margin-top: 32px;">历史版本</button>
					</div>
					<div class="media-right media-middle">
						<label style="font-size:18px;color:#4a4a4a;text-align:right;line-height:28px;width: 116px;">状态：已上线</label>
						<button class="btn btn-default btn-enabled pull-right">发布更新</button>
					</div>
				</div>
			</li>
		</ul>
		<div class= "pull-right">
			<ul id="page_pagintor"></ul>
		</div>
	</div>
	<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated bounceInRight" id="modal">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
					</button>
					<h4 class="school-title">发布更新</h4>
				</div>
				<div class="modal-body">
					<form role="form">
						<div class="form-group">
							<input type="text" class="school-name empty" id="school_name_firstname" placeholder="江南小学"  disabled="true">
						</div>
						<div class="form-group">
							<div class="checkbox checkbox-circle checkbox-success">
								<input type="radio" name="is_all" id="radio1" class="radio_is_all" value="0" checked>
								<label for="radio1">局部</label>
								<input type="radio" name="is_all" id="radio2" class="radio_is_all" value="1">
								<label for="radio2">整包</label>
								<input type="radio" name="is_all" id="radio3" class="radio_is_all" value="-1">
								<label for="radio3">定向更新(生产环境测试)</label>
							</div>
						</div>
						<div class="form-group show_input" style="display: none;">
							<input type="text" class="school-phone empty" id="phone_firstname" placeholder="输入手机号码，使用英文&quot;&#44;&quot;作为间隔">
						</div>
						<div class="form-group">
							<div class="checkbox checkbox-circle checkbox-success">
								<input type="radio" name="app_type" id="radio4" value="005" checked>
								<label for="radio4">全部</label>
								<input type="radio" name="app_type" id="radio5" value="005005">
								<label for="radio5">iOS</label>
								<input type="radio" name="app_type" id="radio6" value="005010">
								<label for="radio6">Android</label>
							</div>
						</div>
						<div class="form-group">
							<input type="text" class="school-version empty" id="version_firstname" placeholder="输入版本号">
						</div>
						<div class="form-group ">
							<input type="text" class="school-url pull-left empty" id="url_firstname" placeholder="URL">
							<input class="btn btn-default  btn-upload pull-right" id="Upload_APK" value="上传APK" />
						</div>
						<input class="btn btn-default btn-finish complete" value="完成"/>
					</form>
				</div>
			</div>
		</div>
	</div>
	<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
		<input type="File" value="上传学校logo" name="file" id="uploadLogo"/>
	</form>
	<button type="button" id="btn_error" class="btn btn-primary" style="display: none;" data-toggle="modal" data-target="#myModal2">
		沿X轴转动
	</button>
	<div class="modal inmodal" id="myModal2" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content animated flipInY">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title subtitle">信息提示！</h4>
				</div>
				<div class="modal-body">
					<p id="error_content"><strong>H+</strong> 是一个完全响应式，基于Bootstrap3.3.5最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术，她提供了诸多的强大的可以重新组合的UI组件，并集成了最新的jQuery版本(v2.1.1)，当然，也集成了很多功能强大，用途广泛的jQuery插件，她可以用于所有的Web应用程序，如网站管理后台，网站会员中心，CMS，CRM，OA等等，当然，您也可以对她进行深度定制，以做出更强系统。</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">
		&copy;1997-2016乐智公司版权所有  About LeZhiApp | 公司简介 | 联系我们 | 客服 | 代理商
	</div>
	<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
	<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script src="hplus/js/content.min.js?v=1.0.0"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
 	<script src="js/myajax.js"></script>
    <script src="app/js/uploadApp.js?d=${time}"></script>
    <script>
	    $(document).ready(function() {
			$('#form-file').attr('action',localStorage.getItem('file_upload_action'));
            // 用回车键 触发搜索按钮.==13代表 键盘Q起 第13个按键4
            $("body").keyup(function() {
                if (event.which == 13) {
                    $(".search-button").trigger("click");
                }
            });
            initSchoolList();
            bindUploadAPKClickEvent();
		    bindFileBtnClickEvent();
            bindSearchButtonClickEvent();
            bindSelectClickEvent();
            bindCompleteClickEvent();
            bindDirectionalUpdateClickEvent();
		 });
    </script>
</body>
</html>