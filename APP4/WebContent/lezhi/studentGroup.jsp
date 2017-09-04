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
	<script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/studentRoster.js?d=${time}"></script>
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-menu">
			<ul id="groupmenu">
				
			</ul>
		</div>

		<div class="ftab-cont ftab-r">
			<div class="ftab-top ftab-top2">
				<p id="studentCount"></p>
				<div class="col-sm-4 ftab-search">
					<input type="text" class="form-control" placeholder="搜索" id="searchGroup_text">
				</div>
				<button id="searchGroup"><i class="fa fa-search"></i></button>
				<div class="r">
					<a class="btn btn-req" id="exportbtn">excel模版下载</a>
					<a class="btn btn-req" id="Iimportbtn">excel导入花名册</a>
					<a id="addGroupStudent" class="btn btn-req">+添加</a>
				</div>
			</div>

			<table class="table table-bordered">
				<thead>
					<tr>
						<th>班级</th>
						<th>学号</th>
						<th>姓名</th>
						<th>性别</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody id="teacherList">
					
				</tbody>
			</table>

			<div class="ftab-bottom">
				<div class="ftab-btn">
					<!-- <a href="#" class="btn btn-sm btn-req">选择</a> -->
				</div>
				
				<div class="pages">
					<ul id="page_pagintor1"></ul>
				</div>
			</div>
		</div>
	</div>
</div>
<form hidden="true" class="btn-group" action="" name="Form" id="Form" method="post" enctype="multipart/form-data" >
      <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
</form>
<script type="text/javascript">
//session失效，页面跳转
pageJump();
showGroup();
searchStudentOfGroup();
addGroupStudent();
bindIBtnClickEvent();//导入学生
exportSExcel();//下载花名册模版
</script>
</body>
</html>
