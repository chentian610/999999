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
    <style type="text/css">
    	.itembox{width:200px;height:220px;border: 1px solid #d3d3d3;cursor:pointer}
    	.itembox:hover{border: 1px solid #A1A1A1}    	
    	.itemimg{height:170px;text-align:center} .itemimg img{width:120px;margin-top:10px}
    	.itemtitle{height:170px;text-align:center;font-family:"宋体"}
    	.itemactive{border: 2px solid #000;}
    </style>
</head>

<body>
<div class="wrapper wrapper-content">
    <div class="ss-btn-box clear">
        <div class="ss-box input-group" style="width:260px;">
            <span class="input-group-btn"> 
            	<button type="button" class="btn btn-success btn-sm" data-action="addTemplate" data-toggle="modal" data-target="#template-edit-modal">
            		<span class="glyphicon glyphicon-plus-sign"></span> 添加图文模板
            	</button>
            </span>
            <span class="input-group-btn"> 
            	<button type="button" class="btn btn-primary btn-sm" data-action="uploadTemplate">
            		<span class="glyphicon glyphicon-retweet"></span> 上传选中图文模板
            	</button> 
            </span> 
        </div>                            
    </div>
    
    
    <div class="ftab">
		<div class="ftab-cont">
			<table class="table table-bordered" id="template-table">
				<thead>
					<tr>
						<th style="width:30px;"></th>
						<th>图文模板名称</th>
						<th>是否上传  <a title="上传后的图文模板可用于群发消息"><i class="icon-question-sign"></i></a></th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody id="newstemplatelist">
				</tbody>
			</table>
		</div>
	</div>
    
    <!-- 图文模板编辑弹框 -->  
    <div class="modal inmodal fade" id="template-edit-modal" tabindex="-1" role="dialog"  aria-hidden="true">
    	<div class="modal-dialog">
			<div class="ibox-content" >
				<div class="form-group">
                 <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                 </button>
            	</div>
				<form class="form-horizontal" id="templateForm">
					<input type="hidden" name="template_id"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">模板名称</label>
						<div class="col-sm-9">
							<input type="text" class="form-control" name="template_name">
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-11">
                            <button type="button" class="btn btn-primary" data-action="saveTemplate">保存</button>
                            <button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
						</div>
					</div>
			   </form>
			</div>
		</div>
	</div>
	
	<!-- 图文内容编辑弹框 -->  
    <div class="modal inmodal fade" id="item-edit-modal" tabindex="-1" role="dialog"  aria-hidden="true">
    	<div class="modal-dialog" style="width: 1000px;">
			<div class="ibox-content" >
				<div class="form-group">
                 <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
                 </button>
            	</div>
				<form class="form-horizontal" id="itemForm">
					<input type="hidden" name="item_id"/>
					<input type="hidden" name="news_template_id"/>
					<div class="ftab">
						<div class="ftab-cont">
							<table class="table table-bordered" id="template-table">
								<tbody>
									<tr>
										<td width="20%" data-lo="items" style="vertical-align:top;">
										</td>
										<td width="80%" data-lo="edititem" style="vertical-align:top;">
											<div class="form-group">
												<label class="col-sm-2 control-label">标题</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="title" placeholder="请输入标题">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">作者</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="author" placeholder="请输入作者（选填）">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">封面图片</label>
												<div class="col-sm-9">
													<div class="sch-logo ">
							                           <img src="images/gzh_wx.jpg" id="showLogo"/>
							                              <button id="uploadPic" type="button" class="btn btn-primary btn-upload">请您上传封面图片</button>
							                           <span>仅支持JPG、JPEG、GIF、PNG格式的图片，请不要大于2MB</span>
						                           </div>
						                           <input type="hidden"  name="image_path">
						                           <input type="checkbox" value="1" id="display_cover_flag">封面图片显示在正文中
						                           <input type="hidden" name="display_cover_flag">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">摘要</label>
												<div class="col-sm-9">
													<textarea rows="2" class="form-control" cols="60" id="description"></textarea>
												</div>
												<input type="hidden" name="description">
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">外部链接</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="url" placeholder="请输入外部链接（选填）">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">正文</label>
												<div class="col-sm-9">
									                <div class="btn-toolbar ibox-content" data-role="editor-toolbar" data-target="#editor">
												      <div class="btn-group">
												        <a class="btn dropdown-toggle" data-toggle="dropdown" title="Font"><i class="icon-font"></i><b class="caret"></b></a>
												          <ul class="dropdown-menu">
												          </ul>
												        </div>
												      <div class="btn-group">
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
												      <div class="btn-group">
												        <a class="btn" data-edit="insertunorderedlist" title="Bullet list"><i class="icon-list-ul"></i></a>
												        <a class="btn" data-edit="insertorderedlist" title="Number list"><i class="icon-list-ol"></i></a>
												        <a class="btn" data-edit="outdent" title="Reduce indent (Shift+Tab)"><i class="icon-indent-left"></i></a>
												        <a class="btn" data-edit="indent" title="Indent (Tab)"><i class="icon-indent-right"></i></a>
												      </div>
												      <div class="btn-group">
												        <a class="btn" data-edit="justifyleft" title="Align Left (Ctrl/Cmd+L)"><i class="icon-align-left"></i></a>
												        <a class="btn" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)"><i class="icon-align-center"></i></a>
												        <a class="btn" data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)"><i class="icon-align-right"></i></a>
												        <a class="btn" data-edit="justifyfull" title="Justify (Ctrl/Cmd+J)"><i class="icon-align-justify"></i></a>
												      </div>
												      <div class="btn-group">
														  <a class="btn dropdown-toggle" data-toggle="dropdown" title="Hyperlink"><i class="icon-link"></i></a>
														    <div class="dropdown-menu input-append">
															    <input class="span2" placeholder="URL" type="text" data-edit="createLink"/>
															    <button class="btn" type="button">Add</button>
												        </div>
												        <a class="btn" data-edit="unlink" title="Remove Hyperlink"><i class="icon-cut"></i></a>
												
												      </div>
												      <div class="btn-group">
												        <a class="btn" title="图片" id="picturebtn"><i class="icon-picture"></i></a>
												        <a class="btn" data-edit="undo" title="Undo (Ctrl/Cmd+Z)"><i class="icon-undo"></i></a>
												        <a class="btn" data-edit="redo" title="Redo (Ctrl/Cmd+Y)"><i class="icon-repeat"></i></a>
												      </div>
												    </div>
												    <!-- 文本容器 -->
													<div id="editor" class="editor">
													</div>
												</div>
												<input type="hidden" name="content">
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">原文链接</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="original_link" placeholder="请输入原文链接（选填）">
												</div>
											</div>
											<div class="form-group">
												<label class="col-sm-2 control-label">序号</label>
												<div class="col-sm-9">
													<input type="text" class="form-control" name="orders" style="width:50px;">
												</div>
											</div>
											<div class="form-group">
												<div class="col-sm-offset-3 col-sm-11">
						                            <button type="button" class="btn btn-primary" data-action="saveItem">保存</button>
						                            <button type="button" class="btn btn-danger hide" data-action="removeItem">删除</button>
						                            <button type="button" class="btn btn-primary" data-dismiss="modal" id="item-edit-close">关闭</button>
												</div>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
			   </form>
			   <form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden='hidden'>	
	              <input type="File" value="" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
	           </form>
	           	<form hidden="true" class="btn-group" action="" name="Form" id="Form" method="post" enctype="multipart/form-data" >
			      <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
			      <img id="tempimg" dynsrc="" src="" style="display:none" />
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
<script src="hplus/js/jquery.hotkeys.js"></script>
<script src="hplus/js/prettify.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script src="wechat/js/newstemplate.js"></script>

<script type="text/javascript">
	//session失效，页面跳转
	pageJump();
	$(function(){
		$("#form-file").attr('action',localStorage.getItem('file_upload_action'));
		$("#Form").attr('action',localStorage.getItem('file_upload_action'));
		bindAllClickEvent();
		loadTemplates();
		bindUploadLogoClickEventY();//图片按钮事件定义
		bindUploadPicClickEventX();//触发事件定义
		bindEditorPicBtnClickEvent();
	});
</script>
</body>
</html>