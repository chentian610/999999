<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String school_name = (String) request.getSession().getAttribute(
			"school_name");
	String app_pic_url = (String) request.getSession().getAttribute(
			"app_pic_url");
			//设置缓存为空   	
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires",   0); 
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">
<title>Lezhi</title>
<link rel="stylesheet" type="text/css"
	href="lezhi/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css"
	href="lezhi/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
</head>
<body>
	<!--Navigation-->
	<!--Navigation-->
	<nav>
		<input id="user_type" type="text" value="003025" hidden="hidden" />
		<div class="nav-top" id="LoadTheHomePage">
			<img src="<%=app_pic_url%>" alt="" class="logo">
			<h1><%=school_name%></h1>
		</div>
		<ul class="side-menu" id="sideMenu"></ul>
	</nav>

	<!--Wrapper-->
	<div class="wrap">
		<div class="user-top">
			<div class="pull-right">
				<div class="user-name">
					<img src="" alt="" id="headUrl"><span id="userName">(代理商）</span>
				</div>
				<div class="user-do">
					<a class="J_menuItem" href="lezhi/account.html"><i
						class="fa fa-user"></i>帐号管理</a>
				</div>
				<div class="user-do">
					<a href="audit/home/login.jsp"><i class="fa fa-sign-out"></i>退出</a>
				</div>
			</div>
		</div>
		<div class="page-nav content-tabs">
			<button class="scr-left J_tabLeft">
				<i class="fa fa-backward"></i>
			</button>
			<button class="scr-right J_tabRight">
				<i class="fa fa-forward"></i>
			</button>
			<div class="page-nav-cont J_menuTabs">
				<div class="page-tabs-content">
					<a href="javascript:;" class="active J_menuTab"
						data-id="application/application.jsp">申请学校 <i
						class="fa fa-times-circle"></i>
					</a>
				</div>
			</div>
		</div>
		<div class="J_mainContent" id="content-main">
			<iframe class="J_iframe" name="iframe1" width="100%" height="100%"
				src="application/application.jsp" frameborder="0"
				data-id="application/application.jsp" seamless=""> </iframe>
		</div>
	</div>

	<script src="hplus/js/jquery.min.js" type="text/javascript"></script>
	<script src="hplus/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="lezhi/js/mycontabs.js" type="text/javascript"></script>
	<script src="lezhi/js/common.js" type="text/javascript"></script>
	<script src="lezhi/js/LoadTheHomepage.js?d=${time}" type="text/javascript"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="hplus/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="hplus/js/plugins/layer/layer.min.js"></script>
	<script type="text/javascript">		
		if (localStorage.getItem('user_name'))
				$("#userName").html(localStorage.getItem('phone')+ " (账号管理)");
		else
				$("#userName").html(localStorage.getItem('user_name')+ " (账号管理)");
		$("#headUrl").attr("src",localStorage.getItem('head_url'));			
		initMenuList();
	</script>
	<!-- <script src="hplus/js/hplus.min.js?v=4.0.0"></script>
   <script src="hplus/js/plugins/pace/pace.min.js"></script> -->
</body>
</html>
