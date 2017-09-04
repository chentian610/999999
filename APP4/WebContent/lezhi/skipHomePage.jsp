<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  response.setHeader("Cache-Control","no-store");
  response.setHeader("Pragrma","no-cache");
  response.setDateHeader("Expires",0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title id="titleName">管理后台,提示页面</title>
    <script type="text/javascript" src="js/myajax.js"></script>
  	<script type="text/javascript" src="js/jquery.js"></script>
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <script src="hplus/js/plugins/toastr/toastr.min.js"></script>
  <script type="text/javascript" src="js/jquery.form.js"></script>
  <script type="text/javascript" src="lezhi/js/skipHomePage.js?d=${time}"></script>
  <script type="text/javascript">
  	$(function (){
		bindSkipClickEvent();
	});  
  </script>
  </body>
</html>
