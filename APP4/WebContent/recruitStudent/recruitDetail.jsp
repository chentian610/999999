<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String recruit_id=request.getParameter("recruit_id");
    String title=request.getParameter("title");
//设置缓存为空
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
    <title>招生-列表-详情页</title>
    <link rel="stylesheet" href="hplus/css/bootstrap.min.css" />
    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="recruitStudent/css/build.css">
    <style type="text/css">
        *{margin: 0;padding: 0;}
        body{background: #f2f2f2;}
        li{list-style: none;}
        .form-horizontal{padding: 0 58px 0 0;margin-top: 36px;}
        .form-horizontal .form-group { margin: 20px 0 0 0;}
        .form-horizontal .control-label {text-align: left;font-size: 14px;color: #686B6D;}
        .col-sm-10,.col-sm-2{padding: 0;}
        a:hover{text-decoration: none;color:#04B594;}
        .zjq-apply-page{background:#fff;width: 678px;margin: 100px auto 0;padding-top: 53px;position: relative;}
        .zjq-contentPage{padding: 61px 107px 45px 107px;}
        .zjq-makeSurePage .zjq-publish{border-top: 1px solid #EEEFEF;margin-top: 15px;}
        .zjq-selBtns{position: absolute;right: 53px;}
        .zjq-selBtn{margin-right:5px;display: inline-block;width: 115px;height: 25px;line-height: 25px;border: 1px solid #00B493;border-radius: 2px;font-size: 12px;color: #04B594;text-align: center;cursor: pointer;}
        .zjq-editIcon{margin-right: 5px;}
    </style>
</head>
<body>
<div class="zjq-apply-page">
    <div class="zjq-selBtns" id="status">
        <a id="edit" class="zjq-selBtn" href="recruitStudent/updateRecruit.jsp?recruit_id=<%=recruit_id%>"><span class="glyphicon glyphicon-pencil zjq-editIcon"></span> 编辑</a>
    </div>
    <div class="zjq-contentPage" id="recruitContent">

    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="recruitStudent/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="recruitStudent/js/enrollmentList.js?d=${time}"></script>
<script type="text/javascript">
    //session失效，页面跳转
    pageJump();
    var recruit_id=<%=recruit_id%>;
    showDetail();
</script>
</body>
</html>
