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
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-cont">
			<div class="ftab-top ftab-top2">
				<p>已创建的寝室</p>
			</div>

			<table class="table table-bordered">
				<thead>
					<tr>
						<th>寝室名称</th>
						<th>性别</th>
						<th>人数</th>
						<th>删除</th>
					</tr>
				</thead>

				<tbody id="bedroom">
	
				</tbody>

				<tfoot>
					<tr><td colspan="4">
							<div class="col-sm-3" id="nBtn"><a href="javascript:;" class="btn btn-sm btn-req" onclick="_newRoom(this)">+新建</a></div>
							
							<div id="nDiv" style="display: none;">
								<div class="col-sm-3"><input id="bedroomname" type="text" class="form-control" placeholder="输入寝室名称"></div>
                                <div class="col-sm-3">
									<select name="" class="form-control" id="bedroomsex">
										<option>性别</option>
										<option value="0">男</option>
										<option value="1">女</option>
									</select>
								</div>

								<div class="col-sm-3">
									<a id="save" class="btn btn-sm btn-req">保存</a>
									<a href="javascript:;" onclick="_cancelNewRoom(this)" class="btn btn-sm btn-req">取消</a>
								</div>
							</div>
						</td>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>
</div>

<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/bedroom.js?d=${time}"></script>
<script type="text/javascript">
function _newRoom(obj){
	$("#nBtn").hide();
	$("#nDiv").show();
}

function _cancelNewRoom(obj){
	$("#nBtn").show();
	$("#nDiv").hide();
}
//session失效，页面跳转
pageJump();
showBedroom();
saveBedroom();
</script>
</body>
</html>
