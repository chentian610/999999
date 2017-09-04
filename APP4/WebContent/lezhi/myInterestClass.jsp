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
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta charset="utf-8" />
    <title>所有兴趣班</title>
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css" />
    <link href="hplus/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="lezhi/css/allInterestClass.css" />
</head>

<body>
<div class="container">
    <div class="form-inline qtop">
        <span style="font-size: 15px;color: #A7A7A7;">我创建的兴趣班</span>
        <div class="pull-right">
            <select class="qbb-select" id="status">
                <option class="" value="">全部</option>
                <option value="1">未过期</option>
                <option value="0">已结束</option>
            </select>
            <select class="qbb-select" id="course">
                <option class="" value="">全部科目</option>
            </select>
            <select class="qbb-select" id="grade">
                <option class="" value="0">所有年级</option>
            </select>
        </div>
    </div>
    <div class="qcontent" id="groupmenu">

    </div>
    <div class="ftab-bottom">
        <ul id="page_pagintor1" style="padding-left: 200px;padding-bottom:50px;"></ul>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/interestClass.js?d=${time}"></script>
<script type="text/javascript">
    var isMy=1;
    //session失效，页面跳转
    pageJump();
    showCourse();
    showGradeInfo();
    showGroup();
    changeStatus();
    changeCourse();
    changeGrade();
</script>
</body>
</html>
