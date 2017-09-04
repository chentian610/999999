<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String class_name=request.getParameter("class_name");
String class_id=request.getParameter("class_id");
String grade_id=request.getParameter("grade_id");
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
	
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="lezhi/layer/layer.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/studentRoster.js?d=${time}"></script>
	<style type="text/css" media="print">
		.invite-c{width: 100%;}
		.sel,.btns{display: none}
	</style>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-top">
			<label>为<%=class_name %>花名册添加学生</label>
		</div>

		<div class="ftab-cont">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>必填</th>
						<th>必填</th>
						<th>选填</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody id="nextInsert">
					
				</tbody>
			</table>

			<div class="ftab-bottom">
				<div class="ftab-btn2">
					<a style="width: 240px" class="btn btn-lg btn-req" id="save">保存</a>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
class_id=<%=class_id%>;
grade_id=<%=grade_id%>;
//session失效，页面跳转
pageJump();
showInsert();
nextInsert();
save();
</script>
</body>
</html>
