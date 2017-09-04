<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String dict_group = "";
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title>代理商管理</title>
	<!-- Data Tables -->
	<link href="agent/css/bootstrap.min.css" rel="stylesheet">
	<link href="hplus/css/font-awesome.min.css" rel="stylesheet" />
	<link href="hplus/css/animate.min.css" rel="stylesheet">
	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
	<link rel="stylesheet" href="agent/css/reset.css?d=${time}" />
	<link rel="stylesheet" href="agent/css/agentAdmin.css?d=${time}" />
	<link rel="stylesheet" type="text/css" href="agent/css/style.css"/>
	<link rel="stylesheet" type="text/css" href="app/css/build.css"/>
	<link rel="stylesheet" href="agent/css/agentAdminOld.css?d=${time}" />
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container">
	<p class="money" >余额：¥<span id="Balance"></span>
		<input type="button" class="btn btn-default recharge" id="recharge" value="充值"/>
	</p>
	<ul class="school_list">
	</ul>
	<div class= "pull-right">
		<ul id="page_pagintor"></ul>
	</div>
</div>
<div class="modal inmodal fade in" id="myModal" tabindex="-1" role="dialog"  aria-hidden="true" style="display:none;background: rgba(0,0,0,.3);">
	<div class="modal-dialog">
		<div class="modal-content zjq-modal-content">
			<div class="zjq-sure-page">
				<h2>确认申请</h2>
				<div class="zjq-message-sure">
					<div class="zjq-scroll-message">
						<div class="zjq-message-block">
							<h3>配置APP</h3>
							<div class="zjq-mess-detail" id="schoolModuleList">选择模块:
							</div>
							<div class="zjq-mess-detail">
								服务器配置
								<div class="zjq-table-style" id="ConfigList">
								</div>
							</div>
							<div class="zjq-mess-detail">
								官网配置:
								<div class="checkbox checkbox-success">
									<input type="text" id="MainUrl" style="" placeholder="请输入官网地址" class="form-control">
									<input type="radio" name="radio" id="yesMainUrl" value="option1">
									<label for="yesMainUrl" style="margin: 0 24% 0 33px;">是</label>
									<input type="radio" name="radio" id="noMainUrl" value="option1">
									<label for="noMainUrl">否</label>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="zjq-buttons">
					<button type="button" class="btn" data-dismiss="modal">取消编辑</button>
					<button type="button" class="btn zjq-green-btn" id="Determine" data-dismiss="modal">确定</button>
				</div>
			</div>
		</div>
	</div>
</div>
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
				<p id="error_content"></p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="js/mathUtil.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="agent/js/agentAdminOld.js?d=${time}"></script>
<script src="js/jquery.md5.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {$.getJSON('', {}, function(rep) {});});
	initAgentAdminPage();
//    initAgentBalance();
//    initSchoolList();
//    bindRechargeClickEvent();
//    loadAppModuleClassIficationList();
//    initServerConfig();
//    bindCoustomPriceChangeEvent();
//    bindSubmitClickEvent();
//    bindYesMainUrlClickEvent();
//    bindNooMainUrlClickEvent();
</script>
</body>
</html>