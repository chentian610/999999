<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String dict_group = "";
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
    <title>代理商管理</title>
    <!-- Data Tables -->
    <link href="agent/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet" />
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" href="notice/css/style.min.css"/>
    <link rel="stylesheet" type="text/css" href="menu/css/build.css"/>
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link href="menu/css/menuTeacherConfig.css?d=${time}" rel="stylesheet" type="text/css" >
</head>
<body>
<div class="zjq-teacher-page clearfix">
    <div class="zjq-left-electorate">
        <ul class="zjq-role-sel" id="RoleList">
        </ul>
    </div>
    <div class="zjq-right-electorate clearfix">
        <div class="zjq-master-list fl">
            <h2 id="insertTitle">已添加任课老师</h2>
            <form class="form-inline zjq-form-header">
                <div class="form-group">
                    <select class="zjq-sel" id="Grouplist">
                    </select>
                </div>
                <div class="form-group">
                    <select class="zjq-sel zjq-class-sel" id="Teamlist">
                    </select>
                </div>
            </form>

            <table class="table zjq-table zwf-table">
                <tbody id="teacherDutyList">
                </tbody>
            </table>
            <div class="buttons">
                <input class="btn btn-default" type="button" id="SelectDuty" value="全选">
                <input class="btn btn-default s_btn" type="submit" id="deleteTeacherList" value="删除">
            </div>
        </div>
        <div class="zjq-student-search fl">
            <h2>联系人列表</h2>
            <form class="form-inline zjq-form-header">
                <div class="form-group zjq-search">
                    <span class="glyphicon glyphicon-search zjq-search-logo" id="searchTeacher"></span>
                    <input type="search" class="form-control" placeholder="搜索" id="search">
                </div>
            </form>
            <table class="table zjq-table">
                <tbody id="teacherList">
                </tbody>
            </table>
            <div class="zjq-number-sel">
                <div class= "pull-right">
                    <ul id="page_pagintor"></ul>
                </div>
            </div>
            <div class="buttons">
                <input class="btn btn-default" type="button" id="Select" value="全选">
                <input class="btn btn-default add_btn" type="submit" id="addTeacherList" value="添加到左侧">
            </div>
        </div>
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
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script SRC="menu/js/menuTeacherConfig.js?d=${time}"></script>
<script type="text/javascript">
    (function () {
        initRoleList();
        initGroupList();
        bindSearchChangeEvent();
        bindSearchTeacherClickEvent();
        bindSelectClickEvent();
        bindSelectDutyClickEvent();
        bindAddTeacherListClickEvent();
        bindDeleteTeacherListClickEvent();
    })();
</script>
</body>
</html>
