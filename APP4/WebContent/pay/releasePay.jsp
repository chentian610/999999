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
	<link href="pay/css/releasePay.css?d=${time}" rel="stylesheet">
</head>
<body>
<div class="container">
	<div class="zjq-pay-page">
		<h3 class="zjq-tab-sel">
		</h3>
		<form class="form-horizontal zjq-form-page">
			<div class="form-group zjq-inputSet">
				<div>
					<select id="sel_pay_type" class="form-control" value="费用类别"></select>
				</div>
			</div>
			<div class="form-group zjq-inputSet">
				<div>
					<input type="text" class="form-control empty-input" id="end-date" placeholder="截止日期" onclick="laydate({istime: true, format: 'YYYY-MM-DD',min:laydate.now()})">
				</div>
			</div>
			<div class="form-group">
				<label for="pay-content" class="col-sm-2 control-label">费用说明:</label>
				<div class="col-sm-10">
					<textarea class="form-control empty-input" rows="3" id="pay-content"></textarea>
				</div>
			</div>
			<div class="form-group">
				<label for="input_money" class="col-sm-2 control-label">输入金额:</label>
				<div class="col-sm-10">
					<input type="number" class="form-control empty-input" id="input_money" min="0" onkeyup="if(! /\d+(\.\d+)?/.test($(this).val())){$(this).val('');}" onpaste="return false;">
				</div>
			</div>
			<div class="form-group">
				<label for="input-Sender" class="col-sm-2 control-label">输入发起人:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control empty-input" id="input-Sender">
				</div>
			</div>
			<span class="col-sm-offset-2 zjq-feeScope" id="Option-fee-object" data-toggle="modal" data-target="#myModal">选择收费范围</span>
			<ul class="col-sm-offset-2 zjq-classSel empty-date" id="pay-group-list">
				<li>三年级1班<img src="pay/images/Group.png" /></li>
				<li>三年级2班<img src="pay/images/Group.png" /></li>
				<li>三年级3班<img src="pay/images/Group.png" /></li>
			</ul>
			<div class="form-group">
				<input class="btn btn-default col-sm-10 col-sm-offset-2 zjq-submitBtn" id="Release" type="button" data-toggle="modal" data-target="#myModal2" value="发布">
			</div>
		</form>
	</div>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated flipInY">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">选择班级</h4>
			</div>
			<div class="modal-body school-team-list">

			</div>
			<div class="modal-footer">
				<button class="btn btn-default zjq-sure-btn" id="Preservation" type="submit" data-dismiss="modal">保存</button>
				<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<div class="modal inmodal" id="myModal2" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated flipInY">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="zjq-sel-title">确认发布缴费</h4>
			</div>
			<div class="modal-body">
				<div class="zjq-fee-list">
					<h3 class="zjq-fee-sort" id="input_title"><img src="pay/icon/jiaofei1.png" alt="" />学校收费：学费</h3>
					<h2 class="zjq-fee-price" id="input_pay_money">￥3000.00</h2>
					<p class="zjq-fee-time" id="input_end_date">截至日期：2017.6.1</p>
					<p class="zjq-fee-initiator" id="input_sender_name">发起人：浙江小学</p>
					<p class="zjq-fee-initiator" id="input_pay_range" style="height: 95px;">收费范围：全校</p>
				</div>
			</div>
			<div class="modal-footer">
				<button class="btn btn-default zjq-sure-btn" type="submit" id="Preservation_pay">保存</button>
				<button type="button" class="btn btn-white" data-dismiss="modal" id="Close_model">关闭</button>
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
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="pay/js/releasePay.js?d=${time}"></script>
<script type="text/javascript">
    $(document).ready(function() {$.getJSON('', {}, function(rep) {});});
    var module_code = getParameterByUrl("module_code");
    infoReleasePayPage(module_code);
</script>
</body>
</html>