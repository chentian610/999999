<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String install_url=(String) request.getSession().getAttribute("install_url");
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
	<link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<style type="text/css" media="print">
		.invite-c{width: 100%;}
		.sel,.btns{display: none}
	</style>
	<script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/inviteUser.js?d=${time}"></script>
</head>
<body>
<div class="mainwrap">
	<div class="invite-c">
		<div class="sel">
			<a class="active" href="lezhi/invite.jsp">短信邀请</a>
			<a href="lezhi/print.jsp">打印</a>
		</div>

		<div class="invite-msg">
			<div class="item">
				<span class="flabel">发送给</span>
				<div class="col-sm-5">
					<select name="" class="form-control" id="user">
						<option value="003">全部教师和家长</option>
						<option value="003005">全部教师</option>
						<option value="003015">全部家长</option>
					</select>
				</div>
			</div>

			<div class="item">
				<span class="flabel">&nbsp;</span>
				<div class="col-sm-8">
					<textarea cols="80" rows="10" class="form-control" readonly="readonly" id="msg">
					</textarea>
				</div>
			</div>

			<div class="item">
				<span class="flabel">&nbsp;</span>
				<div class="col-sm-6">
					<button style="width: 100%;" class="btn btn-req" id="sendMsg">立即发送</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
//session失效，页面跳转
$('#msg').html('我校官方APP已正式上线，请各位老师家长点击以下地址安装使用'+localStorage.getItem('install_url')+'');
pageJump();
sendMsg();
</script>
</body>
</html>
