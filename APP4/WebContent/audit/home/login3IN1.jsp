<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<html>
<head>
<title>一个学校一个app</title>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<style type="text/css">
    *{margin: 0;padding: 0;}
	body{margin: 0 auto;width: 1280px;background: url(audit/home/images/index_bg.jpg) center no-repeat;font-family: "Microsoft yahei"}
	.content-layer{width: 800px;height: 500px;position: absolute;top: 50%;left: 50%;margin: -250px 0 0 -400px;text-align: center;}
	.content-layer h1{margin-bottom: 120px;}
	.content-layer a{display: block;height: 53px;width: 224px;margin: 0 auto 20px;line-height: 53px;background: url(audit/home/images/btn_bg2.png) no-repeat;color: #5d4d72;font-size: 15px;text-decoration: none;padding-left: 40px;text-align: left;}
	.content-layer a.btn-agent{color: #fff;background: url(audit/home/images/btn_bg1.png) no-repeat;}
	.tc{width: 330px;height: 280px;background: #fff;border-radius: 10px;position: absolute;top: 50%;left: 50%;margin: -140px 0 0 -165px;display: none;z-index: 9;}
	.tc-tit{height: 56px;border-bottom: 1px solid #efeff4;line-height: 56px;text-align: center;color: #a8a8a8;}
	.tc-con{padding: 20px;}
	.tc-con li{line-height: 40px;border-bottom: 2px solid #f6f6f6;list-style: none;position: relative;}
	.tc-con li:last-child{border-bottom: none;margin-top: 25px;}
	.tc-con li span{display: inline-block;width: 30px;height: 40px;position: absolute;top: 0;left: 0}
	.tc-con li span.icon-phone{background: url(audit/home/images/icon_login.png) left center no-repeat;}
	.tc-con li span.icon-password{background: url(audit/home/images/icon.png) left center no-repeat;}
	.tc-con li input{margin-left: 40px;display: block;height: 40px;border:none;width: 250px;color: #4a4a4a;font-size: 15px;}
	.tc-con li input.validate{margin-right:10px;float:left;margin-left: 40px;height: 40px;width: 150px;color: #4a4a4a;font-size: 15px;}
	.tc-con li button{width: 100%;height: 45px;border:none;background: #668df1;border-radius: 5px;font-family: "Microsoft yahei";color: #fff;font-size: 15px;}
	.tc-con li input#getValidateCode{margin-left:8px;width: 85px;height: 35px;background: #668df1;border-radius: 5px;font-family: "Microsoft yahei";color: #fff;font-size: 15px;}
	.tc-con li input#changeBtn{width: 200px;height: 45px;border:none;background: #668df1;border-radius: 5px;font-family: "Microsoft yahei";color: #fff;font-size: 15px;}
	.tc-con p{margin-top: 10px;line-height: 30px;font-size: 15px;}
	.tc-con p a{color: #668df1;text-decoration: none;}
	.tc-con p a:hover{text-decoration: underline;}
	.tc-con p span{float: left;}
	.tc-con p em{font-style: normal;float: right;}
</style>
<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="content-layer">
	<h1><img src="audit/home/images/index_h1.png"></h1>
	<a href="javascript:;" id="btn-Admin">学校管理员登录</a>
	<a href="javascript:;" id="btn-SuperAdmin">系统管理员登录</a>
	<a href="javascript:;" class="btn-agent">代理商登录</a>
</div>
<form action="loginAction/LezhiWebLogin" method="post" class="loginForm">
<div id="SuperAdmin-tc" class="tc">
	<div class="tc-tit">系统管理员登录</div>
	<div class="tc-con">
		<ul>
			<li><span class="icon-phone"></span><input type="text" placeholder="输入手机号" name="phone"></li>
			<li><span class="icon-password"></span><input id="password" type="password" placeholder="输入密码"></li>
			<li><button id="loginBtn">登录</button></li>
		</ul>
		<input hidden="hidden" type="text" id="inputUserType" name="user_type">
		<input id="pass_word" name="pass_word"  hidden="hidden"/>
		<p><span style="margin-left:100px;"><a id="forget">忘记密码？</a></span><!-- <em><a href="#">注册新账号</a></em> --></p>
	</div>
</div>
</form>
<form action="userAction/forgetPassword" method="post">
<div id="forgetPassword" class="tc">
	<div class="tc-tit">忘记密码</div> 
	<div class="tc-con">
		<ul>
			<li><span class="icon-phone"></span><input type="text" placeholder="输入手机号" name="phone" id="phone1"></li>
			<li><span class="icon-phone"></span>
			<input class="validate" type="text" placeholder="输入验证码" name="validate_code" id="validateCode" autocomplete="off">
			<input id="getValidateCode" value="获取验证码" type="button"/></li>
			<li><span class="icon-password"></span><input id="fpassword" type="password" placeholder="输入新密码" name="pass_word"></li>
			<li><input id="changeBtn" value="确定" type="submit"/></li>
		</ul>
		 <input id="fpass_word" type="reset" hidden="hidden"/> 
	</div>
</div>
</form>
<form action="loginAction/Login" method="post" id="skipView">
<div id="agent-tc" class="tc">
	<div class="tc-con">
		<ul>
			<li><span class="icon-phone"></span><input name="user_type" id="user_type" type="text" ></li>
			<li><span class="icon-password"></span><input name="phone" id="phone" ></li>
			<li><button>登录</button></li>
		</ul>
		<p><span><a href="#">忘记密码？</a></span><em><a href="#">注册新账号</a></em></p>
	</div>
</div>
</form>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="audit/home/js/login.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script type="text/javascript" src="manager/js/jquery.md5.js"></script>
<script type="text/javascript">
	$(function(){
		bindSuperAdminBtnClickEvent();
		bindAdminBtnClickEvent();
		bindAgentBtnClickEvent();
		bindSubmitCallBackEvent();
		bindLoginClickEvent();
		bindForgetPassWordClickEvent();
		getValidateCode();
	});
</script>
</body>
</html>