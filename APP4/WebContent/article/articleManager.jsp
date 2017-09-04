<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
	String dict_group = "";
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",   0);
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
    <link href="article/css/style.min.css?d=${time}" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="article/css/page.css?d=${time}" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
   	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
	<link href="article/css/main.css?d=${time}" rel="stylesheet">
	<link href="article/css/cropper.css?d=${time}" rel="stylesheet">
</head>

<body id="body">
<div class="wrapper wrapper-content">
    <div class="ss-btn-box clear">
        <div class="ss-box input-group">
            <input type="text" class="form-control" placeholder="关键字搜索" id="article-keywords">
            <span class="input-group-btn"> <button type="button" class="btn btn-primary btn-outline btn-sm" id="search-button">搜索
            </button> </span>
        </div>
        <div class="r-sel-box clear">
            <input id="article-date" class="form-control m-b layer-date" placeholder="发布时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD', choose: function(date){searchByDate(date);}})">
          <!--<select class="form-control m-b" name="account">
                <option>今天</option>
                <option>昨天</option>
                <option>其它</option>
            </select> -->
			<select class="form-control m-b" name="account" id="article-dict">
				<option>栏目分类</option>
				<option>栏目名</option>
				<option>栏目名</option>
			</select>
            <select class="form-control m-b" name="account" id="article-classify">
                <option>栏目分类</option>
                <option>栏目名</option>
                <option>栏目名</option>
            </select>
        </div>
    </div>
    <div class="article-list">
        <ul id="article-list">
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
    <div class="modal inmodal fade" id="edit-modal" tabindex="-1" role="dialog"  aria-hidden="true">
    	<div class="modal-dialog">
			<div class="ibox-content" >
				<div class="form-group">
                 <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                 </button>
            	</div>
				<form class="form-horizontal">
					<div class="form-group">
						<label class="col-sm-2 control-label">标题</label>
						<div class="col-sm-9">
							<input type="text" placeholder="请输入标题" class="form-control"
								id="article_title">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">栏目分类</label>
						<div class="col-sm-9" >
							<select class="form-control m-b" name="account" id="article-classify-edit">
				                <option>栏目分类</option>
            				</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">发件人</label>
						<div class="col-sm-9">
							<input type="text" placeholder="请输入发件人" class="form-control"
								id="dept_name">
						</div>
					</div>
					<div class="zwf-news-module form-group" hidden="hidden">
						<label class="col-sm-2 control-label">选择模板</label>
						<div class="col-sm-9">
							<select class="zwf-news-change form-control m-b" name="account" id="article-module">
								<option>选项 1</option>
								<option>选项 2</option>
								<option>选项 3</option>
								<option>选项 4</option>
							</select>
						</div>
					</div>
					<div class="form-group zwf-news-img">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-9">
							<div  style="width: 410px;height: auto;"><img src="images/gzh_wx.jpg" dara-printscreen="0" id="showLogo" style="width: 100%;height: auto;"/></div>
							<div style="margin-top: 10px;margin-left: 3px;">
								<button id="uploadPic" type="button" class="btn btn-primary btn-outline btn-upload" style="width: 133px;">请您上传主页图片</button>
								<button id="cutPhoto" type="button" class="btn btn-primary btn-upload btn-outline cut-photo">确认裁剪图片</button>
	                            <span style=" float: left;">仅支持JPG、JPEG、GIF、PNG格式的图片，请不要大于2MB(如果需要裁剪图片请点击"确认裁剪按钮")</span>
                           </div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">发布内容</label>
						<div class="col-sm-9">
			                <div class="btn-toolbar ibox-content" data-role="editor-toolbar" data-target="#editor">
								<div class="zwf-news-ys btn-group">
									<a class="btn dropdown-toggle" data-toggle="dropdown" title="Font"><i class="icon-font"></i><b class="caret"></b></a>
									<ul class="dropdown-menu">
									</ul>
								</div>
								<div class="zwf-news-ys btn-group">
									<a class="btn dropdown-toggle" data-toggle="dropdown" title="Font Size"><i class="icon-text-height"></i>&nbsp;<b class="caret"></b></a>
									<ul class="dropdown-menu">
										<li><a data-edit="fontSize 5"><font size="5">大</font></a></li>
										<li><a data-edit="fontSize 3"><font size="3">中</font></a></li>
										<li><a data-edit="fontSize 1"><font size="1">小</font></a></li>
									</ul>
								</div>
								<div class="btn-group">
									<a class="btn" data-edit="bold" title="Bold (Ctrl/Cmd+B)"><i class="icon-bold"></i></a>
									<a class="btn" data-edit="italic" title="Italic (Ctrl/Cmd+I)"><i class="icon-italic"></i></a>
									<a class="btn" data-edit="strikethrough" title="Strikethrough"><i class="icon-strikethrough"></i></a>
									<a class="btn" data-edit="underline" title="Underline (Ctrl/Cmd+U)"><i class="icon-underline"></i></a>
								</div>
								<div class="zwf-news-ys btn-group">
									<a class="btn" data-edit="insertunorderedlist" title="Bullet list"><i class="icon-list-ul"></i></a>
									<a class="btn" data-edit="insertorderedlist" title="Number list"><i class="icon-list-ol"></i></a>
									<a class="btn" data-edit="outdent" title="Reduce indent (Shift+Tab)"><i class="icon-indent-left"></i></a>
									<a class="btn" data-edit="indent" title="Indent (Tab)"><i class="icon-indent-right"></i></a>
								</div>
								<div class="zwf-news-ys btn-group">
									<a class="btn" data-edit="justifyleft" title="Align Left (Ctrl/Cmd+L)"><i class="icon-align-left"></i></a>
									<a class="btn" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)"><i class="icon-align-center"></i></a>
									<a class="btn" data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)"><i class="icon-align-right"></i></a>
									<a class="btn" data-edit="justifyfull" title="Justify (Ctrl/Cmd+J)"><i class="icon-align-justify"></i></a>
								</div>
								<div class="zwf-news-ys btn-group">
									<a class="btn dropdown-toggle" data-toggle="dropdown" title="Hyperlink"><i class="icon-link"></i></a>
									<div class="dropdown-menu input-append">
										<input class="span2" placeholder="URL" type="text" data-edit="createLink"/>
										<button class="btn" type="button">Add</button>
									</div>
									<a class="btn" data-edit="unlink" title="Remove Hyperlink"><i class="icon-cut"></i></a>

								</div>
								<div class="zwf-news-ys btn-group">
									<a class="btn" title="图片" id="picturebtn"><i class="icon-picture"></i></a>
									<a class="btn" data-edit="undo" title="Undo (Ctrl/Cmd+Z)"><i class="icon-undo"></i></a>
									<a class="btn" data-edit="redo" title="Redo (Ctrl/Cmd+Y)"><i class="icon-repeat"></i></a>
								</div>
						    </div>
						    <!-- 文本容器 -->
							<div id="editor" class="editor">
							</div>
					</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">发送时间</label>
						<div class="col-sm-9">
							<div class="checkbox i-checks">
								<input id="deploy_date" class="form-control layer-date"
									placeholder="输入发布时间"
									onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-11">
                            <button type="button" class="btn btn-primary btn-outline" id="edit-save">保存</button>
                            <button type="button" class="btn btn-danger btn-outline" data-dismiss="modal" id="edit-close">关闭</button>
						</div>
					</div>
			   </form>
				<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden='hidden' action="">
	              <input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
	           </form>
	           	<form hidden="true" class="btn-group" name="Form" id="Form" method="post" enctype="multipart/form-data" action="">
			      <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
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
<script src="article/js/summernote.js"></script>
<script src="article/js/cropper.js?d=${time}"></script>
<script src="article/js/cropper.min.js?d=${time}"></script>
<script src="article/js/main.js?d=${time}"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="article/js/article_manager.js?d=${time}"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="hplus/js/bootstrap-wysiwyg.js"></script>
<script src="hplus/js/jquery.hotkeys.js"></script>
<script src="hplus/js/prettify.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script>
	$("#form-file").attr('action',localStorage.getItem('file_upload_action'));
	$("#Form").attr('action',localStorage.getItem('file_upload_action'));
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
	loadArticleClassifyList();//栏目分类选项
	loadContent(dict_group);//内容列表
	bindSearchButtonClickEvent(); //关键字搜索
	bindUploadLogoClickEventY();//图片按钮事件定义
	bindUploadPicClickEventX();//触发事件定义
	bindEditorPicBtnClickEvent();
	loadArticleDictSchoolList();
	bindArticleDictList();
	bindSelectedPhoto();
	var school_news_action = localStorage.getItem('NEWS_TEMPLATE_ON');
	if (school_news_action=="TRUE"){
		$('.zwf-news-ys').hide();
		loadModuleClassifyList();
		bindModuleChange();
	}
</script>
</body>
</html>