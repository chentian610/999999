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
	<div class="appsuc">
		<h2>恭喜你，学校已经开通</h2>
		<h5>为了保证正常使用，请先完善师生数据</h5>

		<div class="item">
			<h3>1. 行政班级管理</h3>
			<p>建立班级结构</p>
			<a href="lezhi/generalClass.jsp" class="btn btn-req">去补充资料</a>
		</div>

		<div class="item">
			<h3>2. 教师花名册管理</h3>
			<p>上传教师的花名册</p>
			<a href="lezhi/teacherRoster.jsp" class="btn btn-req">去补充资料</a>
		</div>
		
		<div class="item">
			<h3>3. 学生花名册管理</h3>
			<p>上传学生的花名册</p>
			<a href="lezhi/studentRoster.jsp" class="btn btn-req">去补充资料</a>
		</div>
		
		<div class="item">
			<h3>4. 家长花名册管理</h3>
			<p>将家长关联到学生</p>
			<a href="lezhi/parentRoster.jsp" class="btn btn-req">去补充资料</a>
		</div>
		
		<div class="item">
			<h3>5. 兴趣班课程管理</h3>
			<p>兴趣班需关联的课程</p>
			<a href="lezhi/code.jsp?dict_group=015045" class="btn btn-req">去补充资料</a>
		</div>
		
		<div class="item">
			<h3>6. 建立兴趣班级</h3>
			<p>兴趣班的成员需要从行政班级中选择</p>
			<a href="lezhi/interestClass.jsp" class="btn btn-req">去补充资料</a>
		</div>

		<div class="item">
			<h3>7. 兴趣班学生花名册管理</h3>
			<p>兴趣班的成员需要从行政班级中选择</p>
			<a href="lezhi/studentGroup.jsp" class="btn btn-req">去补充资料</a>
		</div>

		<div class="appb">
			<label><input type="checkbox" name="agree">我已完善好学校资料</label>
			<a class="btn btn-req btn-lg" id="invite" onclick="isAgree()">邀请教师／学生／家长使用APP</a>
		</div>
	</div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
function isAgree(){
		if($(':checkbox[name=agree]:checked').size()>0){
			window.location.href='lezhi/invite.jsp';
		}else{
			alert('如果已完善好学校资料，请打勾！');
		}
}
</script>
</body>
</html>
