<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	String contact_name=request.getParameter("contact_name");
	String contact_id=request.getParameter("contact_id");
	String grade_id=request.getParameter("grade_id");
	//String count=request.getParameter("count");
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
	 <script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="lezhi/js/addGroupStudent.js?d=${time}"></script>
</head>
<body>
<div class="mainwrap">
	<div class="col-2">
		<div class="ftab">
			<div class="ftab-cont">
				<div class="ftab-top ftab-top2">
					<h5><%=contact_name %></h5>
				</div>
			<div  id="content" style="height:391px;width:354px;overflow:auto"><!-- 滚动条 -->
				<table class="table table-bordered">
					<tbody id="teacherList">

					</tbody>
				</table>
			</div>
				<div class="ftab-bottom">
					<ul id="page_pagintor"></ul>
				</div>
			</div>

			<div class="ftab-btns">
				<a id="studentSelect" class="btn btn-req">全选</a>
				<a id="delete" class="btn btn-req">删除</a>
			</div>
		</div>
	</div>

	<div class="col-2 ftab-col-r">
		<div class="ftab">
			<div class="ftab-cont">
				<div class="ftab-top ftab-top2">
					<div class="col-sm-5" style="padding-left: 0;">
						<select name="" id="classname" class="form-control">
						</select>
					</div>

					<div class="col-sm-7" style="padding-left: 0;">
						<input name="" id="teachername" class="form-control" placeholder="搜索">
					</div>

					<button id="search"><i class="fa fa-search"></i></button>
				</div>

				<table class="table table-bordered">
					<tbody id="studentList">
						
					</tbody>
				</table>

				<div class="ftab-bottom">
					<ul id="page_pagintor1"></ul>
				</div>
			</div>

			<div class="ftab-btns">
				<a id="allstudentSelect" class="btn btn-req">全选</a>
				<a id="addLeft" class="btn btn-req">添加到左侧</a>
			</div>
		</div>
	</div>

	<%--<div class="ftab-sbtn"><a id="save" class="btn btn-req btn-lg">保存</a></div>--%>
</div>
<script type="text/javascript">
contact_id=<%=contact_id%>;
grade_id=<%=grade_id%>;
<%--count=<%=count%>;--%>
groupPerson();
//session失效，页面跳转
pageJump();
showClass();
search();
allSelect();
allDelete();
addLeft();
</script>
</body>
</html>
