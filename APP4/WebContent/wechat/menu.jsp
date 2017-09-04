<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
	String dict_group = "";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">     
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
    <!-- Data Tables -->
    <link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="article/css/style.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="article/css/page.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
   	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
</head>

<body>
<div class="wrapper wrapper-content">
    <div class="ss-btn-box clear">
        <div class="ss-box input-group" style="width:230px;">
            <span class="input-group-btn"> 
            	<button type="button" class="btn btn-success btn-sm" data-action="add" data-toggle="modal" data-target="#edit-modal">
            		<span class="glyphicon glyphicon-plus-sign"></span> 录入菜单
            	</button> 
            </span>
            <span class="input-group-btn"> 
            	<button type="button" class="btn btn-primary btn-sm" data-action="sync">
            		<span class="glyphicon glyphicon-retweet"></span> 菜单同步到微信
            	</button> 
            </span>                            
        </div>                            
    </div>
    
    
    <div class="ftab">
		<div class="ftab-cont">
			<table class="table table-bordered" id="menu-table">
				<thead>
					<tr>
						<th>微信菜单名称</th>
						<th>微信菜单类型</th>
						<th>微信菜单位置</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="menulist">
				</tbody>
			</table>
		</div>
	</div>
    
    <!-- 编辑弹框 -->  
    <div class="modal inmodal fade" id="edit-modal" tabindex="-1" role="dialog"  aria-hidden="true">
    	<div class="modal-dialog">
			<div class="ibox-content" >
				<div class="form-group">
                 <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                 </button>
            	</div>
				<form class="form-horizontal">
					<input type="hidden" name="menu_id" id="menu_id"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">上级菜单</label>
						<div class="col-sm-9" >
							<select class="form-control m-b" name="parent_id" id="parent_id">
								<option value="0">(无)</option>
            				</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单名称</label>
						<div class="col-sm-9">
							<input type="text" placeholder="菜单名称" class="form-control"
								id="name" name="name">
						</div>
					</div>
					<div class="form-group hide">
						<label class="col-sm-2 control-label">动作类型</label>
						<div class="col-sm-9" >
							<select class="form-control m-b" name="type" id="type">
				                <option value="view">网页链接类</option>
				                <option value="click">消息触发类</option>
            				</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单类型</label>
						<div class="col-sm-9">
							  <input type="radio" name="msg_type" value="info" target="click" checked/>
							 	 校园资讯
							  <input type="radio" name="msg_type" value="news" target="click" />
							 	 图文消息
							  <input type="radio" name="msg_type" value="vote" target="click">
							 	 校园投票
							  <input type="radio" name="msg_type" value="parent_client" target="parent_client">
							 	 微信家长端
							  <input type="radio" name="msg_type" value="url" target="view">
							 	 外部网页链接
						</div>
					</div>
					
					<div class="form-group" type-target="click">
						<label class="col-sm-2 control-label">选择模板</label>
						<div class="col-sm-9">
							<select class="form-control m-b" name="template_id" id="template_id">
            				</select>
						</div>
					</div>
					
					<div class="form-group hide" type-target="view">
						<label class="col-sm-2 control-label">网页地址</label>
						<div class="col-sm-9">
							<input type="text" class="form-control"
								id="url" name="url">
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">排序</label>
						<div class="col-sm-9">
							<select class="form-control m-b" name="orders" id="orders">
								<optgroup label="位置1">
						      		<option value="1" selected="selected">一级菜单1</option>
						      		<option value="11">子菜单1*1</option>
						      		<option value="12">子菜单1*2</option>
						      		<option value="13">子菜单1*3</option>
						      		<option value="14">子菜单1*4</option>
						      		<option value="15">子菜单1*5</option>
						      	</optgroup>
						      	<optgroup label="位置2">
						      		<option value="2">一级菜单2</option>
						      		<option value="21">子菜单2*1</option>
						      		<option value="22">子菜单2*2</option>
						      		<option value="23">子菜单2*3</option>
						      		<option value="24">子菜单2*4</option>
						      		<option value="25">子菜单2*5</option>
						      	</optgroup>
						      	<optgroup label="位置3">
						      		<option value="3">一级菜单3</option>
						      		<option value="31">子菜单3*1</option>
						      		<option value="32">子菜单3*2</option>
						      		<option value="33">子菜单3*3</option>
						      		<option value="34">子菜单3*4</option>
						      		<option value="35">子菜单3*5</option>
						      	</optgroup>
					      	</select>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-11">
                            <button type="button" class="btn btn-primary" data-action="save" id="eidt-save">保存</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal" id="edit-close">关闭</button>
						</div>
					</div>
			   </form>
			</div>
		</div>
	</div>
    <div class= "pull-right">
		<ul id="page_pagintor"></ul>
	</div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="hplus/js/bootstrap-wysiwyg.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script src="wechat/js/menu.js"></script>
<script type="text/javascript">
	//session失效，页面跳转
	pageJump();
	$(function(){
		loadTopMenus();
		bindAllClickEvent();
	});
</script>
</body>
</html>