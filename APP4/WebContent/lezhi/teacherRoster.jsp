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
	<style>
		.btn_red{background-color: #ff6666;}
		.btn_s{background-color: rgb(46, 64, 79);border-radius:0;color: #fff!important;display: inline-block;height: 40px;width: 150px;line-height: 28px;}
		.btn_s:hover{color:#fff;}
		.dialog-sf .dialog-btn2{width:600px;float:left;padding:0;overflow:hidden;}
		.dialog-sf .dialog-btn2 a.btn{margin:0;}
		.dialog-btn1{position:relative;height: 40px;overflow:hidden;float:left;white-space:nowrap;}
		.dialog-btn1 a{border-radius:0;border-right:1px solid #ddd;color: #000;display: inline-block;height: 40px;width: 150px;line-height: 28px;-o-text-overflow:ellipsis;text-overflow:ellipsis;white-space:nowrap;overflow: hidden;}
		.dialog-sf .dialog-top{padding: 0;}
		.line{width:100%;background-color: rgb(46, 64, 79);height: 3px;clear: both;}
		.dialog-sf .dialog-top a.close{margin-right: 8px;margin-top: 8px;}
		.dialog-btn1{display: inline-block;}
		.dialog-cont{display: none;}
		.add{margin-top:5px;}
	</style>
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/teacher.js?d=${time}"></script>
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-top">
			<label id="teacherCount"></label>
			<div class="col-sm-4">
				<input class="form-control" type="text" value="" placeholder="搜索" id="teachername">
			</div>
			<button id="search"><i class="fa fa-search"></i></button>
			<a href="lezhi/insertTeacher.jsp" class="r btn btn-req">+添加</a>
			<a class="r btn btn-req" id="importbtn">excel导入花名册</a>
			<a class="r btn btn-req" id="exportbtn">excel模版下载</a>
		</div>
		
		<div class="ftab-cont">
			<table class="table table-bordered">
				<thead>
					<tr><th>姓名</th>
						<th>手机号码</th>
						<th>性别</th>
						<th>职务</th>
						<th>操作</th>
					</tr>
				</thead>

				<tbody id="teacherList">
			
				</tbody>
			</table>

			<div class="ftab-bottom">
				<div class="ftab-btn">
				</div>

				<div class="pages">
					<ul id="page_pagintor"></ul>
				</div>
			</div>
		</div>
	</div>
</div>


<!--添加身份-->
<div class="dialog-sf" id="dialogSf">
	<div class="dialog-top">
		<a href="javascript:;" class="close"><i class="fa fa-close"></i></a>
		<!-- <a href="javascript:;" class="btn btn-sm btn-req">一（1）班语文老师</a>
		<a href="javascript:;" class="btn btn-sm btn-req">+</a> -->
	 <div class="dialog-btn2"> 
		<div class="dialog-btn1" id="duty">
			<!-- <a href="javascript:;" class="btn btn-sm btn_s new">填写身份资料</a> -->
			<!-- <a href="javascript:;" class="btn btn-sm ">身份2</a> -->
		</div>
		 </div> 
		<a href="javascript:;" class="btn btn-sm btn-req add">+新建身份</a>
		<div class="line"></div>
	</div>
<div class="dialog-content">
	<div class="dialog-cont">
		<table class="table table-bordered">
			<tbody>
				<tr>
					<td width="17%" id="dutyname">
					</td>

					<td width="17%" id="grade">
					</td>

					<td width="17%" id="classname">
					</td>

					<td width="17%" id="subject">
					</td>
					
					<%--<td width="17%" id="interest">--%>
					<%--</td>--%>
					
					<td id="charge"><label><input type="checkbox" name="">我是班主任</label>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="dialog-btn">
		<a href="javascript:;" class="btn btn-req btn-clear">清空</a>
		<a href="javascript:;" class="btn btn-req btn-submit">保存</a>
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
$(function(){

	$('.dialog-content .dialog-cont').hide();
	$('.dialog-content .dialog-cont:first-child').show();
	
	$('.dialog-btn1').on("click","a",function(){
		$('.dialog-btn1 a').removeClass('btn_s');
		$(this).addClass('btn_s');
		
	});
	
	oneduty();
});

function _editItem(obj){
    if ($('.save').length>0) {
        layer.msg('请先完成当前操作', {icon: 0});
        return false;
    }
	phone=$(obj).attr('value');
	userid=$(obj).attr('code');
	var r=true;
	if(userid!='null') r=confirm("该教师已经成功注册登录过，确定要修改信息吗？");
	if(r==true){	  
        	
	var $item = $(obj).parents('tr');
	var $name = $(">td:first",$item), $tel = $(">td:eq(1)",$item), $sex = $(">td:eq(2)",$item), $job = $(">td:eq(3)",$item), $btns = $(obj).parent();

	//name
	var name = $name.text();
	$name.append('<input class="form-control" type="text" value="'+name+'" onchange="changeName(this.value)">').children("span").hide();

	//tel
	var tel = $tel.text();
	$tel.append('<input class="form-control" type="text" value="'+tel+'" onchange="changeTel(this.value)">').children("span").hide();

	//sex
	var sex = $sex.text();
	if(sex == "男"){
		$sex.append('<select class="form-control" onchange="changeSex()" id="changeSex"><option value="0" selected>男</option><option value="1">女</option></select>').children("span").hide();
	}else if(sex == "女"){
		$sex.append('<select class="form-control" onchange="changeSex()" id="changeSex"><option value="0">男</option><option value="1" selected>女</option></select>').children("span").hide();
	} else {
		$sex.append('<select class="form-control" onchange="changeSex()" id="changeSex"><option value="0">男</option><option value="1" >女</option><option value="-1" selected>未设置</option></select>').children("span").hide();
	}

	//job
	var job = $job.text();
	$job.append('<a href="javascript:;" class="btn btn-req btn-sm" onclick="_setJob(this)" id="p_'+phone+'">'+job+'</a>').children("span").hide();

	//btns
	$btns.append('<a href="javascript:;" class="btn btn-sm btn-req" onclick="_cancelEdit(this)">取消</a> <a href="javascript:;" class="btn btn-sm btn-req save" onclick="_submitEdit(this)">保存</a>').children('a:eq(0),a:eq(1)').hide();
}
}

function _cancelEdit(obj){
	var $item = $(obj).parents('tr');
	var $name = $(">td:first",$item), $tel = $(">td:eq(1)",$item), $sex = $(">td:eq(2)",$item), $job = $(">td:eq(3)",$item), $btns = $(obj).parent();

	$("input",$name).remove();
	$("input",$tel).remove();
	$("select",$sex).remove();
	$("a",$job).remove();
	$("span",$item).show();
	$('a:eq(0),a:eq(1)',$btns).show();
	$('a:eq(2),a:eq(3)',$btns).remove();
}

var layerIndex;

$(function(){
	$('#dialogSf').on("click","a.btn-clear",function(){
		$('#dialogSf input').attr("checked",false);
	});
});

var search_text="";
loadContent();
search();//搜索
addOneDuty();
editSomeDuty();
deleteDuty();
bindSBtnClickEvent();
exportTExcel();//下载花名册模版
</script>
</body>
</html>
