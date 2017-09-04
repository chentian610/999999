<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//String dict_group = "";
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
%>
  
<!DOCTYPE html>
<html>
  <head>
<base href="<%=basePath%>">  
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>学校管理后台</title>
    <link href="hplus/css/bootstrap.css" rel="stylesheet">
  	<link href="fame/css/page.css" rel="stylesheet">
 	<link href="fame/css/style.min.css" rel="stylesheet">
 	<link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
 	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
 	<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.1/summernote.css" rel="stylesheet">
  </head>
  
  <body>
    <div class="ibox-content">
    <form class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-3 control-label">姓名</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入姓名:(必填)" class="form-control" id="FameName">
                <!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
            </div>
        </div>
         <div class="form-group">
            <label class="col-sm-3 control-label">出生日期</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入:年-月-日" class="form-control" id="birthday"  onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
                <!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
            </div>
         </div>
         <div class="form-group">
            <label class="col-sm-3 control-label">毕业时间</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入: 年-月-日" class="form-control" id="graduationDate" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
                <!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
            </div>
         </div>
      <!--   <div class="form-group">
            <label class="col-sm-3 control-label">发件人</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入发件人" class="form-control" id="createBy">
                <span class="help-block m-b-none">请输入您注册时所填的E-mail</span>
            </div>
        </div> -->
        <div class="form-group">
            <label class="col-sm-3 control-label"></label>
            <div class="col-sm-9">
              <div class="sch-logo ">
	               <img src="images/gzh_wx.jpg" id="showLogo"/>
	                  <button id="uploadPic" type="button" class="btn btn-primary btn-upload">上传名人图片</button>
	               <span>提示:仅支持JPG、JPEG、GIF、PNG格式的图片文件，建议200x200分辨率,文件不能大于2MB</span>
              </div>  
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label" >名人事迹概括</label>
        	<div class="col-sm-8">
        		<!-- 文本编辑器 -->
	      	<textarea  rows="15" cols="120" id ="description">
                  </textarea>
			</div>
        </div>
      <!--   <div class="form-group">
            <label class="col-sm-3 control-label">发送时间</label>
            <div class="col-sm-8">
                <div class="checkbox i-checks">
                    <input id="createDate" class="form-control layer-date" placeholder="输入发布时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD', choose: function(date){searchByDate(date);}})">
                </div>
            </div>
        </div> -->
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-8">
                <button type="button" class="btn btn-primary" style="padding: 5px 20px;" id="addButton">发送</button>
            </div>
        </div>
    </form>
    <form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden='hidden'action="">
	    <input type="File" value="上传名人墙logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
	 </form>
</div>
<script src="fame/js/jquery-2.1.4.js"></script>
<script src="hplus/js/bootstrap.js"></script>
<script src="fame/js/summernote.js"></script>
<script src="js/alerts.js"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="js/functionUtil.js"></script>
<script src="fame/js/fameDeploy.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script>
    $('#form-file').attr('action',localStorage.getItem('file_upload_action'));
		//加载栏目名称列表
		// var dict_group = getParameterByUrl("dict_group");
		// var module_code = getParameterByUrl("module_code");
		// $(document).ready(function() {
		// 	//生成文本编辑器
		//	$('#summernote').summernote({
	 	//		 height : 300,                 
		//		 callbacks: {//重写图片上传的方法，使图片上传到服务器
	    //			onImageUpload: function(files) {
	    // 				sendFile(files[0], this);
	    //		    }
	  	//		 }      
		// 	});
		 	
	//	 });
//session失效，页面跳转
pageJump();
//定义事件
 bindAddButtonClickEvent();
 bindUploadPicClickEvent();
 bindUploadLogoClickEvent();
</script>
  </body>
</html>
