<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//设置缓存为空   	
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires",   0); 
%>

<!DOCTYPE>
<html>
 <head>
 <base href="<%=basePath%>">
	<meta charset="UTF-8">
	<title>Lezhi</title>
	<link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
	<link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
	<link href="lezhi/css/style.min.css" rel="stylesheet"> 
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script type="text/javascript" src="js/jquery.form.js"></script>
	<script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/studentRoster.js?d=${time}"></script>
	<style type="text/css" media="print">
		.invite-c{width: 100%;}
		.sel,.btns{display: none}
	</style>
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-menu">
			<ul id="classname">
				<!-- <li><a class="active" href="#">2015-1班</a></li> -->
				
			</ul>
		</div>

		<div class="ftab-cont ftab-r">
			<div class="ftab-top ftab-top2">
				<p id="teacherCount"></p>
				<div class="col-sm-4 ftab-search">
					<input type="text" class="form-control" placeholder="搜索" id="teachername">
				</div>
	      <button id="search"><i class="fa fa-search"></i></button>
				<div class="r">
					<a class="btn btn-req" id="exportbtn">excel模版下载</a>
					<a class="btn btn-req" id="importbtn">excel导入花名册</a>
					<a id="add" class="btn btn-req">+添加</a>
				</div>
			</div>

			<table class="table table-bordered">
				<thead>
					<tr>
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
					<ul id="page_pagintor"></ul>
				</div>
			</div>
		</div>
	</div>
</div>
<form hidden="true" class="btn-group" action="" name="Form" id="Form" method="post" enctype="multipart/form-data" >
      <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
</form>
<script type="text/javascript">

function _editItem(obj){
	studentID=$(obj).attr('value');
	var $item = $(obj).parents('tr');
	var $sid = $(">td:first",$item), $name = $(">td:eq(1)",$item), $sex = $(">td:eq(2)",$item), $btns = $(obj).parent();

	//name
	var name = $name.text();
	$name.append('<input class="form-control" type="text" value="'+name+'">').children("span").hide();

	//student id
	var sid = $sid.text();
	$sid.append('<input class="form-control" type="text" value="'+sid+'">').children("span").hide();

	//sex
	var sex = $sex.text();
	if(sex == "男"){
		$sex.append('<select class="form-control"><option value="-1" >未设置</option><option value="0" selected>男</option><option value="1">女</option></select>').children("span").hide();
	}else if(sex == "女"){
		$sex.append('<select class="form-control"><option value="-1" >未设置</option><option value="0">男</option><option value="1" selected>女</option></select>').children("span").hide();
	}else {
		$sex.append('<select class="form-control"><option value="-1" selected>未设置</option><option value="0">男</option><option value="1">女</option></select>').children("span").hide();
	}

	//btns
	$btns.append('<a href="javascript:;" class="btn btn-sm btn-req" onclick="_cancelEdit(this)">取消</a> <a href="javascript:;" class="btn btn-sm btn-req" onclick="_submitEdit(this)">保存</a>').children('a:eq(0),a:eq(1)').hide();

}

function _cancelEdit(obj){
	var $item = $(obj).parents('tr');
	var $name = $(">td:first",$item), $sid = $(">td:eq(1)",$item), $sex = $(">td:eq(2)",$item), $btns = $(obj).parent();

	$("input",$name).remove();
	$("input",$sid).remove();
	$("select",$sex).remove();
	$("span",$item).show();
	$('a:eq(0),a:eq(1)',$btns).show();
	$('a:eq(2),a:eq(3)',$btns).remove();
}
//session失效，页面跳转
pageJump();
showClass();
search();
addShow();
bindSBtnClickEvent();//上传文件
exportSExcel();//下载花名册模版
</script>
</body>
</html>
