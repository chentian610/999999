<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String contact_id=request.getParameter("contact_id");
    String contact_name=request.getParameter("contact_name");
    String grade_id=request.getParameter("grade_id");
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
    <title>抢报模式详情页</title>
    <link rel="stylesheet" href="lezhi/css/reset.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="lezhi/css/grabDetail.css"/>
</head>
<body>
<div class="container">
    <div class="zjq-overduPage">
        <%--<h3 class="zjq-title title"><input code="<%=contact_id%>-<%=contact_name%>-<%=grade_id%>" class="btn btn-default overdu-btn manage" type="button" value="学生管理"></h3>--%>
        <dl class="zjq-courseDetail" id="detail">

        </dl>
        <div class="zjq-students-number">
            <dl class="zjq-added clearfix" >
                <dt id="ratio"><input class="btn btn-default add-btn" type="button" value="添加" ></dt>
                <div id="stuInfo">

                    </div>
            </dl>
        </div>
        <input class="btn btn-default started-btn" type="hidden" value="开课">
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
    contact_name="<%=contact_name%>";
    grade_id=<%=grade_id%>;
    isMy=0;
    //session失效，页面跳转
    pageJump();
    showDetail();
    studentManage();
</script>
</body>
</html>
