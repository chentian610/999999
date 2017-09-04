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
	<link href="hplus/css/style.min.css" rel="stylesheet"> 
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/teacherGroup.js?d=${time}"></script>
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-menu">
			<ul id='groupmenu'>
				
			</ul>
		</div>

		<div class="ftab-cont ftab-r">
			<div class="ftab-ct">
				<a id="addGroupTeacher" class="btn btn-req">＋选择人员</a>
			</div>
			<table class="table table-bordered">
				<tbody id="teacherList">
					
				</tbody>
			</table>

			<div class="ftab-bottom">
				<div class="pages">
					<ul id="page_pagintor"></ul>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
//session失效，页面跳转
pageJump();
insertGroup();
addGroupTeacher();
</script>
</body>
</html>
