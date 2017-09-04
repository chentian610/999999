<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String dict_group = "";
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<!-- Data Tables -->
<link href="hplus/css/bootstrap.min.css" rel="stylesheet">
<link href="hplus/css/font-awesome.min.css" rel="stylesheet" />
<link href="hplus/css/animate.min.css" rel="stylesheet">
<link href="fame/css/style.min.css" rel="stylesheet">
<link href="hplus/css/plugins/sweetalert/sweetalert.css"
	rel="stylesheet">
<link href="fame/css/page.css" rel="stylesheet">
<link
	href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.1/summernote.css"
	rel="stylesheet">
<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet"
	type="text/css" />
<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
<link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
</head>

<body>
	<div class="wrapper wrapper-content">
		<div class="ss-btn-box clear">
			<div class="ss-box input-group">
				<input type="text" class="form-control" placeholder="关键字搜索"
					id="fame-keywords"> <span class="input-group-btn">
					<button type="submit" class="btn btn-primary btn-sm"
						id="search-button">搜索</button> </span>
			</div>
		<!-- 	<div class="r-sel-box clear">
				<input id="fame-date" class="form-control m-b layer-date"
					placeholder="发布时间"
					onclick="laydate({istime: true, format: 'YYYY-MM-DD', choose: function(date){searchByDate(date);}})">
				<select class="form-control m-b" name="account">
                <option>今天</option>
                <option>昨天</option>
                <option>其它</option>
            </select>
			</div> -->
		</div>
		<div class="fame-list">
			<ul id="fame-list">
				<!--             <li>
                <h3><a href="#">标题标题标题标题标题</a> </h3>
                <p><span></span><span></span></p>
                <div class="art-opt-btn">
                    <button type="button" class="btn btn-primary btn-sm">编辑</button>
                    <button type="button" class="btn btn-primary btn-sm">移动</button>
                    <button type="button" class="btn btn-primary btn-sm">删除</button>
                </div>
            </li> -->
			</ul>
		</div>
		<!-- 编辑弹框 -->
		<div class="modal inmodal fade" id="edit-modal" tabindex="-1"
			role="dialog" aria-hidden="true">
			<div class="modal-dialog">
				<div class="ibox-content">
					<div class="form-group">
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
						</button>
					</div>
					<form class="form-horizontal">
						<div class="form-group">
							<label class="col-sm-3 control-label">姓名</label>
							<div class="col-sm-8">
								<input type="text" placeholder="请输入姓名" class="form-control"
									id="FameName">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">出生日期</label>
							<div class="col-sm-8">
								<input type="text" placeholder="请输入年-月-日格式" class="form-control"
									id="birthday" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
								<!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">毕业时间</label>
							<div class="col-sm-8">
								<input type="text" placeholder="请输入年-月-日格式" class="form-control"
									id="graduationDate" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
								<!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
							</div>
						</div>
					<!-- 	<div class="form-group">
							<label class="col-sm-3 control-label">发件人</label>
							<div class="col-sm-8">
								<input type="text" placeholder="请输入发件人" class="form-control"
									id="createBy">
								<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>
							</div>
						</div>  -->
						<div class="form-group">
							<label class="col-sm-3 control-label"></label>
							<div class="col-sm-8">
								<div class="sch-logo ">
									<img src="images/gzh_wx.jpg" id="showFameLogo" />
									<button id="uploadFamePic" type="button"
										class="btn btn-primary btn-upload">请上传名人图片</button>
									<span></span>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label">名人简介</label>
							<div class="col-sm-8">
								<!-- 文本编辑器 -->
								<textarea rows="20" cols="50" id="description">
                  </textarea>
							</div>
						</div>
						<!-- <div class="form-group">
							<label class="col-sm-3 control-label">更新时间</label>
							<div class="col-sm-8">
								<div class="checkbox i-checks">
									<input id="update" class="form-control layer-date"
										placeholder="输入更新时间"
										onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
								</div>
							</div>
						</div> -->
						<div class="form-group">
							<div class="col-sm-offset-3 col-sm-11">
								<button type="button" class="btn btn-primary" id="edit-save">保存</button>
								<button type="button" class="btn btn-primary"
									data-dismiss="modal" id="edit-close">关闭</button>
							</div>
						</div>
					</form>
					<form enctype="multipart/form-data" method="post" name="fileinfo"
						id="form-file" hidden='hidden' action="">
						<input type="File" value="上传学校logo" name="file" id="uploadFameLogo"
							accept="image/gif, image/jpeg, image/png, image/jpg" />
					</form>
					<form hidden="true" class="btn-group" name="Form" id="Form" method="post"
						enctype="multipart/form-data">
						<input id="upload_pic" name="image" type="file"
							style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px" />
					</form>
				</div>
			</div>
		</div>
		<div class="pull-right">
			<ul id="page_pagintor"></ul>
		</div>
	</div>
	<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
	<script src="js/myajax.js"></script>
	<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
	<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
	<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script src="fame/js/summernote.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script src="js/functionUtil.js"></script>
	<script src="fame/js/fameManager.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script src="hplus/js/bootstrap-wysiwyg.js"></script>
	<script src="hplus/js/jquery.hotkeys.js"></script>
	<script src="hplus/js/prettify.js"></script>
	<script type="text/javascript" src="js/jquery.form.js"></script>
	<script>
		$('#Form').attr('action',localStorage.getItem('file_upload_action'));
		//生成文本编辑器
		$('#summernote').summernote({
			height : 300,
			callbacks : {//重写图片上传的方法，使图片上传到服务器
				onImageUpload : function(files) {
					sendFile(files[0], this);
				}
			}
		});
	</script>
	<script type="text/javascript">
		var dict_group = getParameterByUrl("dict_group");
		var module_code = getParameterByUrl("module_code");
		//session失效，页面跳转
	    pageJump();
		bindSearchButtonClickEvent();//按照内容查找
		loadContent(dict_group);//内容列表
		bindUploadPicClickEventX();//名人墙图片时间定义
		bindUploadLogoClickEventY();//上传图片的隐藏按钮触发事件
	</script>
</body>
</html>