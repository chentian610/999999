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
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-top">
			<label>已创建的兴趣班</label>
		</div>

		<div class="ftab-cont">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>班级名称</th>
						<th>课程名称</th>
						<th>人数</th>
						<th>禁用</th>
					</tr>
				</thead>

				<tbody id="groupmenu">

					<tr class="tr_no">
						<td colspan="4"><!-- 列合并，合并的列数 -->
							<a id="cBtn" href="javascript:;" style="float: left;" class="btn btn-req btn-sm" onclick="_createNew(this)">+新建</a>

							<div id="nData" style="display: none;">
								<div class="col-sm-3">
									<input type="text" class="form-control" placeholder="输入班级名称" id="interest">
								</div>
								<div class="col-sm-3">
								<select id="course" class="form-control"></select>
								</div>
								<div class="col-sm-4" style="text-align: left;">
									<a id="save" class="btn btn-req btn-sm">保存</a>
									<a href="javascript:;" class="btn btn-req btn-sm" onclick="_cancelCreate(this)">取消</a>
								</div>
							</div>
						</td>
					</tr>
					
				</tbody>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/interestClass.js?d=${time}"></script>
<script type="text/javascript">
function _createNew(obj){
	$("#cBtn").hide();
	$("#nData").show();
}

function _cancelCreate(obj){
	$("#cBtn").show();
	$("#nData").hide();
}
//session失效，页面跳转
pageJump();
showGroup();
showCourse();
save();
</script>
</body>
</html>
