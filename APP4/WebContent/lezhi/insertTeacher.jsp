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
	<style >
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
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/addTeacher.js?d=${time}"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-top">
			为教师花名册添加联系人
		</div>
		
		<div class="ftab-cont">
			<table class="table table-bordered">
				<thead>
					<tr><th>必填</th>
						<th>必填</th>
						<th>选填</th>
						<th>选填</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody  id="nextInsert">
					
				</tbody>
			</table>

			<div class="ftab-bottom">
				<a style="width: 240px;" class="btn btn-req btn-lg" id="save">保存</a>
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
			<a href="javascript:;" class="btn btn-sm btn_s new">填写身份资料</a>
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
		<a href="javascript:;" class="btn btn-req btn-submit" >保存</a>
	</div>
	</div>
</div>
	
</div>
<script type="text/javascript">
//session失效，页面跳转
pageJump();
$(function(){

	$('.dialog-content .dialog-cont').hide();
	$('.dialog-content .dialog-cont:first-child').show();
	oneduty();
});
var layerIndex;
$(function(){
	$('#dialogSf').on("click","a.btn-clear",function(){
		$('#dialogSf input').attr("checked",false);
	}).on("click","a.btn-submit",function(){
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear btn_red btn_del">删除</a><a href="javascript:;" class="btn btn-req  btn-reivse">修改</a>');
		$('#dialogSf input').attr("disabled",true);
	}).on("click","a.btn-reivse",function(){
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
		$('#dialogSf input').attr("disabled",false);
		saveduty();
	}).on("click",".add",function(){
		if ($('#duty a.new').length>0) {
			layer.msg('请先保存当前身份！', {icon: 0});
			return false;
		}
		$('#dialogSf input').attr("checked",false);
		var num = $('.dialog-btn1 a').length;
		
		if(num <=2){
		$('.dialog-btn1').animate({left:0});
		}else{
		if($(this).offset().left >"450"){
			var  oLeft = '-'+ (num-2) * 150 +"px";
			$('.dialog-btn1').animate({left:oLeft});
		}else{
			var  oLeft = '-'+ (num-2) * 150  +"px";
			$('.dialog-btn1').animate({left:oLeft});
		}
	}
		
			var html1 = '<a href="javascript:;" class="btn btn-sm new">填写身份资料</a>';
			$('.dialog-btn1').append(html1);
			$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
		$('#dialogSf input').attr("disabled",false);
			$('.dialog-btn1 a').removeClass('btn_s');
			$('.dialog-btn1 a:last-child').addClass('btn_s');
		
		saveduty();
	});
});
showInsert();
saveduty();
quit();
save();
oneduty();
deleteDuty();
</script>
</body>
</html>
