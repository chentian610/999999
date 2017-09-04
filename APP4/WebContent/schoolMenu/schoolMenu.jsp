<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
	<meta http-equiv="X-UA-Compatible" content="IE=Edge">
	<title>代理商管理</title>
	<!-- Data Tables -->
	<link href="hplus/css/bootstrap.min.css" rel="stylesheet">
	<link href="hplus/css/font-awesome.min.css" rel="stylesheet" />
	<link href="hplus/css/animate.min.css" rel="stylesheet">
	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<link href="schoolMenu/css/menu.css?d=${time}" rel="stylesheet">
</head>
<body>
<div class="container">
	<div class="zjq-menu-page">
		<span class="zjq-arrow-left"><img src="schoolMenu/icon/arrow_left.png"/></span>
		<div class="zjq-menu-list">
			<ul class="clearfix" id="zjq-menuList">
			</ul>
		</div>
		<span class="zjq-arrow-right"><img src="schoolMenu/icon/arrow_right.png"/></span>
	</div>
	<div class="modal inmodal fade in" id="myModal" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;background: rgba(0,0,0,.3);">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="zjq-form-menu">
					<h3>星期一菜谱</h3>
					<div class="form-group zjq-form-text">
						<label class="control-label">食物名称：</label>
						<input type="text"  class="form-control" style=" word-break: break-all; " id="menuName" placeholder="请输入菜品名称(16字以内)" maxlength="16"/>
					</div>
					<div class="zjq-menu-pic clearfix">
						<span class="zjq-food-title fl">食物图片：</span>
						<ul class="clearfix">
							<li class="zjq-upload-img" id="UploadFile"><img src="schoolMenu/icon/upload_pic.png"/></li>
						</ul>
					</div>
					<div class="zjq-menu-buttons">
						<button type="button" class="btn btn-white data-dismiss" data-dismiss="modal">取消</button>
						<button type="button" class="btn zjq-green-btn" id="Preservation">保存</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal inmodal fade in" id="myModal7" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;background: rgba(0,0,0,.3);">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">确认框</h4>
			</div>
			<div class="modal-body">
				<p>你确定要删除此项吗？</p>
			</div>
			<div class="modal-footer zjq-modal-buttons">
				<button type="button" class="btn btn-white cancel" data-dismiss="modal">取消</button>
				<button type="button" class="btn zjq-green-btn" id="Determine">确定</button>
			</div>
		</div>
	</div>
</div>
<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
	<input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
</form>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="js/mathUtil.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="schoolMenu/js/schoolMenu.js?d=${time}"></script>
<script type="text/javascript">
	initPageFunction();
</script>
</body>
</html>