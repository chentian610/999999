<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>课道</title>
	<link href="pay/css/bootstrap.min.css?v=3.3.5" rel="stylesheet">
	<style>
		.text-label{font-size: 25px;}
		.content-background{background-color: #edffcc;min-height: 40px;border-radius: 6px;}
		div{margin-top: 15px;}
		.label-content{ text-align: inherit;
			font-size: 18px;
			margin-left: 30%;
			margin-bottom: 15px;}
		.success-img{
			position: absolute;
			left: 22%;
			border-radius: 30px;
		}
		footer{font-size: 14px;color: #fff;position: absolute;left: 35%;color:#0b0b0b;margin-top: 190px;}
	</style>
</head>
<body class="gray-bg">
<div class="container">
	<div class="form-group"><label class="text-label WordArt">课淘</label> <label class="text-label"> | 收银台</label></div>
	<div class="form-group">
		<div class="content-background col-sm-12" style=" padding: 15px;">
			<img src="pay/images/success.png" alt="" class="success-img">
			<label class="col-sm-10 control-label label-content">您已支付成功!</label>
			<label class="col-sm-10 control-label label-content" id="outTradeNo"></label>
			<label class="col-sm-10 control-label label-content">请手动返回页面...</label>
		</div>
	</div>
	<footer>©2014-2016 掌上智慧校园&nbsp;&nbsp;<span id="webDomainRecord">浙ICP备15010643号</span></footer>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/functionUtil.js"></script>
<script>
	(function () {
		$('#outTradeNo').append('您的订单号是:'+getParameterByUrl("outTradeNo"));
		$('#webDomainRecord').empty().append(localStorage.getItem('web_domain_record'));
    })();
</script>
</body>
</html>