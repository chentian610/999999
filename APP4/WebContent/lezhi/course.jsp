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
    <meta charset="UTF-8">
    <title>科目管理</title>
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css" />
    <link rel="stylesheet" href="lezhi/css/titatoggle-dist.css" />
    <link rel="stylesheet" href="lezhi/css/style.css" />
    <link rel="stylesheet" type="text/css" href="lezhi/css/course.css" />
</head>
<body>
<div class="container">
    <div class="zjq-coursePage">
        <h4 class="zjq-title">科目管理</h4>
        <table class="table zjq-courseList">
            <thead>
            <tr class="zjq-course" >
                <th class="zjq-course-left">科目名称</th>
                <th class="zjq-course-right">操作</th>
            </tr>
            </thead>
            <tbody id="course">

            </tbody>
        </table>
        <div id="newObject">
        </div>
        <input class="btn btn-default zjq-addCourse" type="button" value="新增科目" id="new-add-btn">
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script src="lezhi/js/jquery-3.0.0.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/course.js?d=${time}"></script>
<script type="text/javascript">
    $(function(){
        $(".zjq-addCourse").click(function(){
            var oTr='<tr class="zjq-course-list zjq-add-course">'+
                    '<td class="zjq-courseName zjq-course-input" style="display: none;"><input class="zjq-course-name"  type="text" /></td>'+
                    '<td class="zjq-courseName zjq-add-input"><input class="zjq-course-name"  type="text" placeholder="请输入项目名称"/></td>'+
                    '<td class="zjq-course-right" style="padding-right: 13px;">'+
                    '<input class="btn btn-default zjq-rename" type="button" value="重命名">'+
                    '<span class="zjq-sel" style="display: inline-block;">'+
                    '<input class="btn btn-default  zjq-reCancel" type="button" value="取消">'+
                    '<input class="btn btn-default  zjq-reSure" type="button" value="确定">'+
                    '</span>'+
                    '<span class="zjq-sel_btns" style="display: none;">'+
                    '<input class="btn btn-default  zjq-cancel" type="button" value="取消">'+
                    '<input class="btn btn-default  zjq-sure" type="button" value="确定">'+
                    '</span>'+
                    '<div class="checkbox checkbox-slider--b checkbox-slider-md">'+
                    '<label><input type="checkbox" checked><span></span></label>'+
                    '</div>'+
                    '</td>'+
                    '</tr>';
            $("tbody").append(oTr);
            var oAdd=$(".zjq-add-course");
            $.each(oAdd,function(){
                $(this).find(".zjq-reCancel").click(function(){
                    $(this).parent().parent().parent().hide();
                });
                $(this).find(".zjq-reSure").click(function(){
                    var a=$(this).parent().parent().siblings(".zjq-add-input").find(".zjq-course-name").val();
                    var b='<td class="zjq-courseName zjq-course-text">'+a+'</td>';
                    $(this).parent().parent().siblings(".zjq-course-input").before(b);
                    $(this).parent().hide();
                    $(this).parent().siblings(".zjq-rename").show();
                    $(this).parent().parent().siblings(".zjq-add-input").hide();
                    bindBtnSaveClick(a);
                });
            });
            reName();
        });
    });
    var dict_group=getParameterByUrl("dict_group");
    //session失效，页面跳转
    pageJump();
    initData();
</script>
</body>
</html>
