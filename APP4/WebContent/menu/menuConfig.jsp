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
    <link rel="stylesheet" type="text/css" href="agent/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="menu/css/build.css"/>
    <link rel="stylesheet" type="text/css" href="menu/css/permission.css?d=${time}"/>
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <style>
        #nav {width:95px; height: 28px;position:absolute;left:17.4%;top:21%;}
    </style>
</head>
<body id="body">
    <div class="zjq-permission-page clearfix">
        <div class="zjq-left-electorate">
            <ul class="zjq-role-sel" id="RoleList">
            </ul>
            <button type="button" class="btn btn-default zjq-role-btn" data-toggle="modal" data-target="#myModal">添加身份</button>
        </div>
        <div class="zjq-right-electorate">
            <div class="zjq-btn-block">
                <button type="button" class="btn btn-default zjq-role-btn zjq-save-btn" id="updateRoleMenu">保存</button>
            </div>
            <ul class="zjq-permission-list" id="RoleMenuList">
            </ul>
        </div>
    </div>
    <div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true" style="display: none;">
        <div class="modal-dialog">
            <div class="modal-content animated bounceInRight zwf-group">
                <div class="form-group zwf-group-top">
                    <label class="col-sm-5 control-label zwf-label">角色名称:</label>
                    <div class="col-sm-12">
                        <input type="text" id="RoleName" placeholder="角色名称" class="form-control" autocomplete="on">
                    </div>
                </div>
                <div class="modal-footer zwf-group-footer">
                    <button type="button" class="btn btn-white btnClose" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn zwf-btn-add" id="addRole">保存</button>
                </div>
            </div><small class="font-bold">
        </small></div><small class="font-bold">
    </small></div>
    <div id="nav" class="display">
        <div id="navIcon">
            <img src="menu/icon/edit.png" class="nav-img" id="editRole">
            <img src="menu/icon/delete.png" class="nav-img" id="deleteRole">
        </div>
        <div class="display" id="navBtn">
            <button type="button" class="btn btn-white nav-btn" id="navBtnClose">取消</button>
            <button type="button" class="btn zwf-btn-add nav-btn" id="updateRole">保存</button>
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
<script src="menu/js/menuConfig.js?d=${time}"></script>
<script type="text/javascript">
    (function () {
        initRoleList();
        bindAddRoleClickEvent();
        bindEditRoleClickEvent();
        binNavBtnCloseClickEvent();
        bindUpdateRoleClickEvent();
        bindDeleteRoleClickEvent();
        bindUpdateRoleMenuClickEvent();
        bindWindowSize();
    })();
</script>
</body>
</html>