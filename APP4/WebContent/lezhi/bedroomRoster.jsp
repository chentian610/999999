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
  <base href="<%=basePath %>">
	<meta charset="UTF-8">
	<title>Lezhi</title>
	<link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
	<link href="hplus/css/style.min.css" rel="stylesheet">
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/bedroom.js?d=${time}"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-menu">
			<ul id="bedroom">
				
			</ul>
		</div>

		<div class="ftab-cont ftab-r">
			<div class="ftab-top ftab-top2">
				<p id="count"></p>
				<a class="r btn btn-req" id="addStudent">+添加</a>
			</div>

			<table class="table table-bordered">
				<thead>
					<tr>
						<th>床号</th>
						<th>班级</th>
						<th>学号</th>
						<th>姓名</th>
						<th>性别</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody id="student">

					
				</tbody>
			</table>

			<div class="ftab-bottom">
				<div class="ftab-btn">
					<!-- <a class="btn btn-sm btn-req">选择</a> -->
				</div>
				
				<div class="pages">
					<ul></ul>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
//session失效，页面跳转
pageJump();
showBedroomMana();
addBedStudent();
</script>
</body>
</html>
