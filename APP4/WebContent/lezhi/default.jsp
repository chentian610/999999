<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String role_code = request.getSession().getAttribute("role_code")+"";
			//设置缓存为空
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires",   0); 
%>

<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
	<link rel="shortcut icon" type="image/x-icon" href="" media="screen" id="link_shortcut_logo"/>
	<link rel="Bookmar" href="" id="link_bookmar_logo">
<meta charset="UTF-8">
<title id="titleName"></title>
<meta charset="UTF-8">
	<meta HTTP-EQUIV= "Pragma " CONTENT= "no-cache ">
	<meta HTTP-EQUIV= "Cache-Control " CONTENT= "no-cache ">
	<meta HTTP-EQUIV= "Expires " CONTENT= "0 ">
	<title>Lezhi</title>
	<link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<style type="text/css" media="print">
		.invite-c{width: 100%;}
		.sel,.btns{display: none}
	</style>


</head>
<body>
	<!--Navigation-->
	<nav>
		<div class="nav-top" id="LoadTheSchoolTitle">
		</div>
		<ul class="side-menu" id="sideMenu">

		</ul>
	</nav>

	<!--Wrapper-->
	<div class="wrap" id="wrapMenu">
		
	</div>
	<script src="hplus/js/jquery.min.js" type="text/javascript"></script>
	<script src="hplus/js/bootstrap.min.js" type="text/javascript"></script>
	<script src="lezhi/js/mycontabs.js" type="text/javascript"></script>
	<script src="lezhi/js/common.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="hplus/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	<script src="hplus/js/plugins/layer/layer.min.js"></script>
	<script src="lezhi/js/LoadTheHomepage.js?d=${time}" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
			$('#titleName').append(''+localStorage.getItem('school_name')+',管理后台');
		});
        localStorage.setItem('role_code','<%=role_code%>');
		var app_pic_url = localStorage.getItem('logo');
		var school_name = localStorage.getItem('school_name');
		var head_url = localStorage.getItem('head_url');
		$('#link_shortcut_logo').attr('href',app_pic_url);
		$('#link_bookmar_logo').attr('href',app_pic_url);
		$('#LoadTheSchoolTitle').append('<img src="'+app_pic_url+'" alt="" class="logo" style="width: 50px;height: 50px"><h1>'+school_name+'</h1>');
		initMenuList();
	</script>
</body>
</html>
