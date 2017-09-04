<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>" target="_blank">
    <title>app设置</title>
    <meta content="text/html; charset=utf-8">
    <link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/style.min.css?v=4.0.0" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet"/>
    <link href="appDict/css/appDict.css?d=${time}" rel="stylesheet"/>
</head>
<body>
<div class="container">
    <div class="q-left">
        <img  src="appDict/images/phone_back.png"/>
        <img id="upload1" class="phone-back"/>
        <img class="phone-img" src="appDict/images/phone_content.png"/>
    </div>
    <div class="q-right">
        <div class="title">上传图片：</div>
        <div id="upload" class="phone-head">
            <span>上传图片</span><p>建议尺寸：375*185，不大于200KB</p>
            <img id="upload2"/>
            <div id="mask" class="mask"><img id="cancle" class="cancle" src="appDict/icon/pic_cancle.png"/></div>
        </div>
        <form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
            <input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
        </form>
        <div class="upload" id="updateUploadFile" hidden="hidden">重新上传</div>
    </div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="js/mathUtil.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="js/jquery.md5.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="appDict/js/appDict.js?d=${time}"></script>
<script type="text/javascript">
    initPageFunction();
</script>
</body>
</html>
