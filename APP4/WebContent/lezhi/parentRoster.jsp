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
	<link href="lezhi/css/style.min.css" rel="stylesheet"> 
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
	<script type="text/javascript" src="js/myajax.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script src="lezhi/layer/layer.js"></script>
	<script type="text/javascript" src="js/functionUtil.js"></script>
	<script type="text/javascript" src="lezhi/js/parent.js?d=${time}"></script>
</head>
<body>
<div class="mainwrap">
	<div class="ftab">
		<div class="ftab-top">
			<label id="counttotal"></label>
			<div class="col-sm-4">
				<input class="form-control" type="text" value="" placeholder="搜索" id="search_text">
			</div>
			<button id="search"><i class="fa fa-search"></i></button>
		</div>
		
		<div class="ftab-cont">
			<table class="table table-bordered">
				<thead>
					<tr><th width="84">学号</th>
						<th>姓名</th>
						<th width="140">性别</th>
						<th width="240">操作</th>
					</tr>
				</thead>
				<tbody id="teacherList">

					

				</tbody>
			</table>

			<div class="ftab-bottom">
				<ul id="page_pagintor"></ul>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">

var $_editForm = $('<div class="col-sm-2"><select name="" class="form-control"><option value="0">请选择'+
'</option><option value="003015005">父亲</option><option value="003015010">母亲</option>'+
'<option value="003015015">爷爷</option><option value="003015020">奶奶</option><option value="003015025">外公'+
'</option><option value="003015030">外婆</option><option value="003015035">其他</option>'+
'</select></div><div class="col-sm-3"><input type="text" class="form-control" value="" placeholder="姓名" />'+
'</div><div class="col-sm-3"><input type="text" class="form-control" value="" placeholder="手机号码" />'+
'</div><div class="col-sm-3"><a href="javascript:;" onclick="_cancelItem(this)" class="btn btn-sm btn-req">'+
'取消</a> <a href="javascript:;" onclick="_submitItem(this)" class="btn btn-sm btn-req">保存</a></div>');

function _editItem(obj){
	parent_id=$(obj).attr('value');
	var userid=$(obj).attr('code');
    var r=true;
    if(userid!='null' && userid!=0)  r=confirm("该家长已经成功注册登录过，确定要修改信息吗？");
    if(r==true){
	var $item = $(obj).parents("div.item");
	var rel = $(">div:first",$item).text().trim(),
		name = $(">div:eq(1)",$item).text().trim(),
		tel = $(">div:eq(2)",$item).text().trim();

		$(">div:eq(0),>div:eq(1),>div:eq(2),>div:eq(3)",$item).hide();
		var $_form = $_editForm.clone();
		$("select>option",$_form).each(function(index, el) {
			if(rel == $(el).text()){
				$(el).prop("selected",true);
			}
		});
		$("input",$_form).eq(0).val(name);
		$("input",$_form).eq(1).val(tel);
		changeTel=tel;//原手机号
		$_form.appendTo($item);
		}
}

function _cancelItem(obj){
	var $item = $(obj).parents("div.item");
	var $tr = $item.parents("tr");

	if($(">div",$item).size()<8){
		$item.fadeOut(function(){
			$item.remove();
			if($("div.item",$tr).size() == 0){
				$tr.remove();
			} 
		});
	}else{
		$("div",$item).each(function(index, el) {
			if(index<4){
				$(el).show();
			}else{
				$(el).remove();
			}
		});
	}

	if($("div:first",$item).text() == ""){
		$item.fadeOut(function(){
			$item.remove();
			if($("div.item",$tr).size() == 0){
				$tr.remove();
			} 
		});
	}
}



function _addItem(obj){
	student_name=$(obj).attr('code');
	student_code=$(obj).attr('value');
	parent_id=0;
	var $tr = $(obj).parents("tr").next();
	if(!$tr || $tr.attr('class') != "tr_do"){
		$tr = $('<tr class="tr_do"><td>&nbsp;</td><td colspan="3"></td></tr>');
		$(obj).parents("tr").after($tr);

	}

	var $_form = $_editForm.clone();
	var $_form2 = $('<div class="item editTemp" />');
	$_form2.html($_form);
	$tr.children('td').eq(1).append($_form2);
}
var search_text;
//session失效，页面跳转
pageJump();
loadSContent();
searchParent();
</script>
</body>
</html>
