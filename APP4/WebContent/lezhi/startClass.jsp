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
    <title>开课</title>
    <link rel="stylesheet" type="text/css" href="lezhi/css/build.css"/>
    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css" />
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" type="text/css" href="lezhi/css/startClass.css" />
</head>

<body>
<div class="container">
    <div class="qcontent">
        <div class="qcontent_item">
            <input type="text" class="form-control" style="margin-top: 0;" placeholder="输入兴趣班名称" id="iName"/>
            <select name="" id="course">
                <option value="">选择科目</option>
            </select>
            <select name="" id="teacher">
                <option value="">选择教师</option>
            </select>
            <select name="" id="grade">
                <option value="0">全校</option>
            </select>
            <%--<select name="" value="选择开课时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
            </select>--%>
            <input id="apply_start_date" class="form-control laydate-icon" placeholder="报名开始时间">
            <input id="apply_end_date" class="form-control laydate-icon" placeholder="报名结束时间">
            <input id="start_date" class="form-control laydate-icon" placeholder="选择开课时间" >
            <%--<select name="">--%>
                <%--<option value="">选择结束时间</option>--%>
                <%--<option>1</option>--%>
                <%--<option>2</option>--%>
                <%--<option>3</option>--%>
            <%--</select>--%>
            <input id="end_date" class="form-control laydate-icon" placeholder="选择结束时间" >
            <textarea class="form-control" rows="3" placeholder="输入备注信息" id="remarkMsg" value=""></textarea>
            <%--<select name="">--%>
                <%--<option value="">报名开课时间</option>--%>
                <%--<option>1</option>--%>
                <%--<option>2</option>--%>
                <%--<option>3</option>--%>
            <%--</select>--%>
            <div class="checkbox checkbox-success" id="grab">
                <input type="radio" name="radio" id="radio1" value="1">
                <label for="radio1" style="padding-left: 14px;margin-right: 42px;font-size: 15px;">抢报模式</label>
                <input type="radio" name="radio" id="radio2" value="0" checked>
                <label for="radio2" style="padding-left: 14px;font-size: 15px;">非抢报模式</label>
            </div>
            <input type="text" class="form-control" placeholder="输入班级人数" id="teamCount"/>
            <input type="text" class="form-control" placeholder="输入可报名人数" id="applyCount"/>
            <div class="checkbox checkbox-success">
                <input type="radio" name="radio2" id="radio3" value="1" checked>
                <label for="radio3" style="padding-left: 14px;margin-right: 42px;font-size: 15px;">上传课程表图片</label>
                <input type="radio" name="radio2" id="radio4" value="0" >
                <label for="radio4" style="padding-left: 14px;font-size: 15px;">自定义课程表</label>
            </div>
            <button class="btn btn-default btn-pic" id="pic" value="">选择图片</button>
            <button class="btn btn-default btn-pic" id="schedule">创建自定义课程表</button>
            <button class="btn btn-default btn-save" id="save">保存</button>
        </div>
    </div>
</div>
<form hidden="true" class="btn-group" action="" name="Form" id="Form" method="post" enctype="multipart/form-data" >
    <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
</form>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/startClass.js?d=${time}"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript">
    $(function(){
        var start = {
            elem: '#start_date', //选择ID为START的input
            format: 'YYYY-MM-DD', //自动生成的时间格式
            min: laydate.now(), //设定最小日期为当前日期
            istime: true, //必须填入时间
            start: laydate.now(0,"YYYY-MM-DD"),  //设置开始时间为当前时间
            choose: function(datas){
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
                apply_start.max=datas;
                apply_end.max=datas;
            }
        };
        var end = {
            elem: '#end_date',
            format: 'YYYY-MM-DD',
            min: laydate.now(),
            istime: true,
            start: laydate.now(0,"YYYY-MM-DD"),
            choose: function(datas){
                start.max = datas; //结束日选好后，重置开始日的最大日期
                apply_start.max=datas;
                apply_end.max=datas;
            }
        };
        var apply_start = {
            elem: '#apply_start_date', //选择ID为START的input
            format: 'YYYY-MM-DD hh:mm:ss', //自动生成的时间格式
            min: laydate.now(), //设定最小日期为当前日期
            istime: true, //必须填入时间
            start: laydate.now(0,"YYYY-MM-DD hh:mm:ss"),  //设置开始时间为当前时间
            choose: function(datas){
                start.min=datas;
                start.start=datas;
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
                apply_end.min=datas;
                apply_end.start=datas;
            }
        };
        var apply_end = {
            elem: '#apply_end_date',
            format: 'YYYY-MM-DD hh:mm:ss',
            min: laydate.now(),
            istime: true,
            start: laydate.now(0,"YYYY-MM-DD hh:mm:ss"),
            choose: function(datas){
                apply_start.max = datas; //结束日选好后，重置开始日的最大日期
                start.min=datas;
                start.start=datas;
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
            }
        };
        laydate(start);
        laydate(end);
        laydate(apply_start);
        laydate(apply_end);
    })
    course=<%=course%>;
    contact_name=<%=contact_name%>;
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
    schedulePicture();
    schedule();
    showCourse();
    showGradeInfo();
    showAllTeacher();
    iploadPicture();
    save();
    createSchedule();
    applyCount();
    checkContactName();
</script>
</body>
</html>