<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String contact_name=request.getParameter("contact_name");
String contact_id=request.getParameter("contact_id");
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
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/addGroupTeacher.js?d=${time}"></script>
</head>
<body>
<div class="mainwrap">
	<div class="col-2 ftab-col-t">
		<div class="ftab">
			<div class="ftab-cont">
				<div class="ftab-top ftab-top2">
					<h5><%=contact_name %></h5>
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

			<div class="ftab-btns">
				<a class="btn btn-req" id="teacherListSelect">全选</a>
				<a class="btn btn-req" id="delete">删除</a>
			</div>
		</div>
	</div>

	<div class="col-2 ftab-col-r  ftab-col-t">
		<div class="ftab">
			<div class="ftab-cont">
				<div class="ftab-top ftab-top2">
					<h5>所有教师</h5>

					<div class="b">
						<div class="col-sm-10" style="padding-left: 50px;">
							<input type="text" class="form-control" placeholder="搜索" id="teachername">
						</div>

						<button id="search"><i class="fa fa-search"></i></button>
					</div>
				</div>

				<table class="table table-bordered">
					<tbody id="allTeacher">
						
					</tbody>
				</table>

				<div class="ftab-bottom">
					<div class="pages">
						<ul id="page_pagintor1"></ul>
					</div>
				</div>
			</div>

			<div class="ftab-btns">
				<a class="btn btn-req" id="allTeacherSelect">全选</a>
				<a class="btn btn-req" id="addLeft">添加到左侧</a>
			</div>
		</div>
	</div>

	<div class="ftab-sbtn"><a class="btn btn-req btn-lg" id="save">保存</a></div>
</div>
<script type="text/javascript">
contact_id=<%=contact_id%>
groupPerson();
//session失效，页面跳转
pageJump();
showAllTeacher();
search();
allSelect();
allDelete();
addLeft();
save();
</script>
</body>
</html>
