<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String contact_id=request.getParameter("contact_id");
    //设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta charset="UTF-8">
    <title>已过期详情页</title>
    <link rel="stylesheet" href="lezhi/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="lezhi/css/outDateDetail.css"/>
</head>
<body>
<div class="container">
    <div class="zjq-overduPage">
        <h3 class="zjq-title title"><input class="btn btn-default overdu-btn" type="button" value="已过期"></h3>
        <dl class="zjq-courseDetail">
            <div id="detail"></div>
            <dd>
                所有学生
                <ul class="zjq-student clearfix" id="stuInfo">

                </ul>
            </dd>
        </dl>

    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/interestClass.js?d=${time}"></script>
<script type="text/javascript">
    contact_id=<%=contact_id%>;
    isMy=0;
    //session失效，页面跳转
    pageJump();
    showAlreadyDetail();
</script>
</body>
</html>
