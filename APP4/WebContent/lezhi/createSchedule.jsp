<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String course=request.getParameter("course");
    String contact_name=request.getParameter("contact_name");
    String phone=request.getParameter("phone");
    String grade_id=request.getParameter("grade_id");
    String start_date=request.getParameter("start_date");
    String end_date=request.getParameter("end_date");
    String remark=request.getParameter("remark");
    String apply_start_date=request.getParameter("apply_start_date");
    String apply_end_date=request.getParameter("apply_end_date");
    String is_grab=request.getParameter("is_grab");
    String team_count=request.getParameter("team_count");
    String apply_count=request.getParameter("apply_count");
    String jsondata=request.getParameter("jsondata");
    String contact_id=request.getParameter("contact_id");
    String schedule_url=request.getParameter("schedule_url");
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
    <title>创建自定义课程表</title>
    <link rel="stylesheet" type="text/css" href="lezhi/css/build.css"/>
    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="lezhi/css/createSchedule.css" />
</head>
<body>
<div class="container">
    <div class="form-inline qtop">
        <span style="font-size: 15px;color: #A7A7A7;">班级名称：<%=contact_name%></span>
    </div>
    <div class="qcontent" id="scheduleTable">

        <div class="qcontent_item">
            <input class="form-control" placeholder="选择日期" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm',min: laydate.now()})">
            <input type="text" value="" placeholder="输入地点"/>
            <button class="btn btn-default btn-add">添加</button>
        </div>

    </div>
    <div class="qcontent">
        <button class="btn btn-default btn-save">保存</button>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/startClass.js?d=${time}"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript">
    course=<%=course%>;
    contact_name="<%=contact_name%>";
    phone=<%=phone%>;
    grade_id=<%=grade_id%>;
    start_date=<%=start_date%>;
    end_date=<%=end_date%>;
    remark=<%=remark%>;
    apply_start_date=<%=apply_start_date%>;
    apply_end_date=<%=apply_end_date%>;
    is_grab=<%=is_grab%>;
    team_count=<%=team_count%>;
    apply_count=<%=apply_count%>;
    jsondata=<%=jsondata%>;
    contact_id=<%=contact_id%>;
    schedule_url=<%=schedule_url%>;
    //session失效，页面跳转
    pageJump();
    initdata();
    addSchedule();
    saveSchedule();
</script>
</body>
</html>