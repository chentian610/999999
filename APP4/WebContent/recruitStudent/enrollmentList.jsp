<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
    <title>招生-列表</title>
    <link rel="stylesheet" href="hplus/css/bootstrap.min.css" />
    <style type="text/css">
        body{background: #f2f2f2;}
        li{list-style: none;}
        .zjq-apply-page{background:#fff;width: 678px;margin: 100px auto;padding-top: 26px;}
        .zjq-new-apply{margin: 26px 34px 25px 34px;height:33px;text-align: center;border: 1px solid #00B493;border-radius: 2px;line-height:33px;font-size: 13px;color: #04B594;font-weight: bold;cursor: pointer;display: block;}
        a:hover{text-decoration: none;color:#04B594;}
        .zjq-apply-list{padding: 0;margin: 0;}
        .zjq-apply-list li{padding: 13px 22px 15px 32px;border-top: 1px solid #E6E9EB;position: relative;}
        .zjq-apply-list li h3{font-size: 24px;color: #323232;line-height: 54px;height: 54px;margin: 0;}
        .zjq-apply-time,.zjq-applied-num{margin-bottom: 0;font-size: 14px;color: #5B5B5B;line-height: 20px;height: 20px;}
        .zjq-applied-num{margin-top: 5px;}
        .zjq-userIcon{color: #7ED321;}
        .zjq-publish-time{font-size: 14px;color: #9B9B9B;position: absolute;right: 36px;top: 28px;}
        .zjq-apply-status{cursor:pointer;display: block;width: 114px;height: 25px;border: 1px solid #00B493;border-radius: 2px;font-size: 12px;color: #04B594;line-height: 25px;text-align: center;position: absolute;right:22px;bottom: 11px;}
        .zjq-apply-detailBtn{border: 1px solid #68583e;color: #68583e;right: 144px;}
        .zjq-apply-detailBtn:hover{color:#68583e;}
    </style>
</head>
<body>
<div class="zjq-apply-page">
    <a class="zjq-new-apply" href="recruitStudent/newRecruit.jsp">+新建招生</a>
    <ul class="zjq-apply-list" id="enrollment">

    </ul>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="recruitStudent/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="recruitStudent/js/enrollmentList.js?d=${time}"></script>
<script type="text/javascript">
    //session失效，页面跳转
    pageJump();
    showContent();
</script>
</body>
</html>
