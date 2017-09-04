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
	<link href="pay/css/bootstrap.min.css" rel="stylesheet">
	<link href="hplus/css/font-awesome.min.css" rel="stylesheet" />
	<link href="hplus/css/animate.min.css" rel="stylesheet">
	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<link href="pay/css/payManange.css?d=${time}" rel="stylesheet">
</head>
<body>
<div class="container pay-details">
	<div class="zjq-fee-page">
		<ul class="zjq-list pay-list">

		</ul>
		<div class= "pull-right">
			<ul id="page_pagintor"></ul>
		</div>
	</div>
</div>
<div class="container single-pull-right display">
	<div class="zjq-fee-page" id="pay_id">
		<div class="zjq-fee-list">
			<h3 class="zjq-fee-sort" id="pay-title"><img src="pay/icon/jiaofei1.png" alt="" />学校收费：学费<time>0分钟前</time></h3>
			<h2 class="zjq-fee-price" id="pay-money">￥3000.00</h2>
			<p class="zjq-fee-range">收费范围：
				<span id="Charge-range">
							全校
						</span>
			</p>
			<p class="zjq-fee-time" id="end-date">截至日期：</p>
			<p class="zjq-fee-progress">缴费进度：<span id="Payment-schedule"></span> <button class="btn btn-default btn-button" id="View_unpaid" data-toggle="modal" data-target="#myModal">查看未缴费学生</button> </p>
			<p class="zjq-total-fee" id="Total-amount">总金额：<span class="zjq-scale"><span class="zjq-scale-pay"></span></span></p>
		</div>
		<dl class="zjq-feeDetail" id="team-fee-details">
			<dt>班级缴费详情</dt>
		</dl>
		<button class="btn btn-default " style="background: #1CBB9E;margin-left: 80%; width: 103px; color: aliceblue;" id="Return" type="submit">返回</button>
	</div>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated flipInY">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">学生缴费明细<div id="is_pay"><button class="zwf-whole is-pay" pay-status="003">全部</button><button class="zwf-already-paid is-pay" pay-status="1">已缴费</button><button class="zwf-unpaid is-pay" pay-status="0">未缴费</button></div></h4>

			</div>
			<div class="modal-body school-team-list">
				<dl class="zjq-feeDetail2 student-pay-list">
					<dd>阮大师<img src="pay/icon/boy.png" alt="" />三年级2班&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;A002451<span class="zjq-unpaid">未缴费</span></dd>
				</dl>
				<div class="modal-footer">
					<div class= "pull-right" style="height: 55px;">
						<ul id="detail_page_pagintor"></ul>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
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
				<p id="error_content"><strong>H+</strong> 是一个完全响应式，基于Bootstrap3.3.5最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术，她提供了诸多的强大的可以重新组合的UI组件，并集成了最新的jQuery版本(v2.1.1)，当然，也集成了很多功能强大，用途广泛的jQuery插件，她可以用于所有的Web应用程序，如网站管理后台，网站会员中心，CMS，CRM，OA等等，当然，您也可以对她进行深度定制，以做出更强系统。</p>
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
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="pay/js/payManange.js?d=${time}"></script>
<script type="text/javascript">
    $(document).ready(function() {$.getJSON('', {}, function(rep) {});});
    var module_code=getParameterByUrl("module_code");
    initPayRecordPage(module_code);
</script>
</body>
</html>