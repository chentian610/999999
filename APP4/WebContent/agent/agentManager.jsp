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
	<link rel="stylesheet" href="agent/css/reset.css" />
	<link rel="stylesheet" type="text/css" href="agent/css/style.css"/>
	<link rel="stylesheet" href="agent/css/agent.css?d=${time}" />
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container">
	<div class="qtop">
		<div class="ftop-serach">
			<input type="search" class="form-control search"  placeholder="搜索" >
			<span class="glyphicon glyphicon-search"></span>
		</div>
		<input type="button" class="btn btn-default qbb-search search-button" value="搜索"/>
		<select  id="region-list" class="qbb-select">
			<option class="">全部</option>
			<option>1</option>
		</select>
		<div class="pull-right" style="margin-right: 48px;">
			<input type="button" class="btn btn-default qbb-search add_agent" value="新增代理商" data-toggle="modal" data-target="#myModal"/>
		</div>
	</div>
	<div class="zjq-user clearfix">
		<div class="zjq-detail fl">
		</div>
		<div class="zjq-buttons fr">
			<div class="zjq-button">
				<div class="form-group">
					<input type="button" class="btn btn-default edit" value="编辑">
				</div>
				<div class="form-group">
					<input type="button" class="btn btn-default disable" value="禁用">
				</div>
			</div>
		</div>
	</div>
	<div class= "pull-right">
		<ul id="page_pagintor"></ul>
	</div>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content animated bounceInRight" id="modal">
			<div class="modal-header box">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
				</button>
				<h1>新增代理商</h1>
			</div>
			<div class="modal-footer box">
				<div class="fl">
					<div class="form-group">
						<label for="label1" class="col-sm-2 col-lg-2 control-label">姓名</label>
						<div class="col-sm-10 col-lg-10">
							<input type="text" class="form-control set_agent_name set_null" id="label1">
						</div>
					</div>
					<div class="form-group">
						<label for="label3" class="col-sm-2 control-label">区域</label>
						<div class="col-sm-10">
							<select  class="qbb-select set_region" style="height: 30px;width: 85px;border-radius: 5px;border: 1px solid #ccc;">
								<option class="">全部</option>
								<option>1</option>
								<option>2</option>
								<option>3</option>
								<option>4</option>
								<option>5</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label for="label3" class="col-sm-4 control-label">注册时间</label>
						<div class="col-sm-8">
							<input type="text" class="form-control set_regist_date set_null" id="label3" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
						</div>
					</div>
				<%--	<div class="form-group">
						<label for="label4" class="col-sm-2 control-label">余额</label>
						<div class="col-sm-6">
							<input type="number" class="form-control set_balance set_null" id="label4">
						</div>
						<div>
							<input type="button" class="btn btn-default button btn_recharg_balance " onpaste="return false;" onkeydown="bindInputBalanceChangeEvent()" onblur="bindInputBalanceChangeEvent()" value="充值">
						</div>
					</div>--%>
				</div>
				<div class="fr">
					<div class="form-group">
						<label for="label5" class="col-sm-3 control-label">手机号</label>
						<div class="col-sm-9 ">
							<input type="text" class="form-control set_phone set_null" id="label5">
						</div>
					</div>
					<div class="form-group">
						<label for="label6" class="col-sm-4 control-label">学校单价</label>
						<div class="col-sm-8">
							<input type="text" class="form-control set_unit_price set_null" id="label6">
						</div>
					</div>
					<div class="form-group">
						<label for="label7" class="col-sm-3 control-label">有效期</label>
						<div class="col-sm-9">
							<input type="text" class="form-control set_valid_date set_null" id="label7" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<input type="button" class="btn btn-default done buttons complete" value="完成"/>
			</div>
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
<script src="agent/js/agentManager.js?d=${time}"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="js/jquery.md5.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
      $(document).ready(function() {$.getJSON('', {}, function(rep) {});});
	  bindGetAgentList();
      initRegionList();
      bindRegionButtonClickEvent();
	  bindSearchButtonClickEvent();
      bindCompleteClickEvent();
     //bindrechargeBalanceClickEvent();
</script>
</body>
</html>