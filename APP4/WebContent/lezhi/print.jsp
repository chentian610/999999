<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
	<title></title>
	<link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
	<style type="text/css" media="print">
		.invite-c{width: 100%;}
		.sel,.btns{display: none}
	</style>
</head>
<body>
<div class="mainwrap">
		<div class="invite-c">
			<div class="sel">
				<a href="lezhi/invite.jsp">短信邀请</a>
				<a class="active" href="lezhi/print.jsp">打印</a>
			</div>

			<div class="printer">
				<div class="btns">
					<%--<a class="btn btn-danger">保存图片</a>--%>
					<a href="javascript:window.print()" class="btn btn-warning">打印</a>
				</div>

				<div class="cont">
					<h2 id="SchoolTitle"></h2>

					<div class="c1">
						大家好，为了方便大家学习和交流，我校已经制作了自己的专属APP，请大家及时下载，使用自己的手机号码登录。
					</div>

					<h3>下载方式</h3>

					<div class="c2">
						<p>1：扫描下方二维码－用手机浏览器打开</p>
						<span id="InstallUrl"></span>
						<!-- <img src="lezhi/images/zhicai.png" alt=""> -->
						<div id="qrcodeTable" style="margin-left: 90px;"></div> 
					</div>
				</div>
			</div>
		</div>
</div>
<!-- 自动生成二维码 -->
<a href="javascript:void();" onclick="creat();" style="display:none;">生成</a>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script type="text/javascript" src="js/qrcode.js"></script> 
<script type="text/javascript" src="js/jquery.qrcode.js"></script> 
<script type="text/javascript">
var install_url = localStorage.getItem('install_url');
var app_pic_url = localStorage.getItem('logo');
var school_name = localStorage.getItem('school_name');
   function creat(){
      $("#qrcodeTable").html("");
       var url=$("#url").val();
	   $("#qrcodeTable").qrcode({
		render	: "table",
		text	: url,
		width:"150",
		height:"150"
	});
   }
$(document).ready(function(){
	$("#qrcodeTable").qrcode(install_url);	
});
$('#SchoolTitle').append('<img src="'+app_pic_url+'" alt="" style="width: 50px;height: 50px">'+school_name+'官方客户端下载通知');
$('#InstallUrl').append('<p>2：手机浏览器直接访问：'+install_url+'</p><p>3：iOS用户也可以搜索“'+school_name+'官方客户端”</p>');
</script>
</body>
</html>
