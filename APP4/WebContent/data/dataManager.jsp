<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String table_name = (String) request.getSession().getAttribute("table_name");
//设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>

<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>Lezhi</title>
    <link rel="stylesheet" type="text/css" href="data/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" href="notice/css/style.min.css"/>
    <link rel="stylesheet" href="notice/css/load.animation.css"/>
    <link rel="stylesheet" href="data/css/dataManager.css?d=${time}"/>
</head>
<body>
<div class="container">
    <div class="div-css">
        <button class="btn btn-primary" id="insertDataA" data-toggle="modal" data-target="#myModal">插入一条数据</button>
        <button class="btn btn-danger" id="deleteALL">删除数据</button>
        <div id="CheckBoxBtn" style="display: none;width: 0; margin-left: 0;"> <button class="btn btn-primary" id="Determine">确定</button><button class="btn btn-default" id="cancelDelete">取消</button></div>
        <div>
            <input type="number" min="0" onkeyup="if(! /\d+(\.\d+)?/.test($(this).val())){$(this).val('');}" onpaste="return false;" class="form-control" id="limit" placeholder="数据查询条目..."/>
            <button class="btn btn-primary" id="btnLimit">确定</button>
        </div>
        <select id="Spinner" class="spinner-css">
            <option value="ASC">正序</option>
            <option value="DESC">倒序</option>
        </select>
    </div>
    <div class="col-sm-12 table-css">
        <table >
            <thead>
            <tr id="dataTitle">
                <th>ID</th>
                <th>姓名</th>
                <th>性别</th>
                <th>评分</th>
            </tr>
            </thead>
            <thead id="dateList">

            </thead>
        </table>
    </div>
    <div class= "pull-right">
        <ul id="page_pagintor"></ul>
    </div>
</div>
<div class="spinner" id="LoadAnimation">
    <div class="rect1"></div>
    <div class="rect2"></div>
    <div class="rect3"></div>
    <div class="rect4"></div>
    <div class="rect5"></div>
</div>
<div id="jPlayer" class="display">
    <a href="#" class="jp-play" id="play">Play</a>
    <a href="#" class="jp-pause" id="pause">Pause</a>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content animated bounceInRight">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">关闭</span>
                </button>
            </div>
            <div class="modal-footer">
                <div id="dataDetail" class="dataDetailCss">
                    <div class="form-group">
                        <label class="col-sm-3 control-label" style="margin-top: 7px;">密码：</label>
                        <div class="col-sm-8">
                            <input type="password" placeholder="密码" class="form-control">
                        </div>
                    </div>
                </div>
                <button class="btn btn-primary display" id="insertData">插入</button>
                <button class="btn btn-success" id="updateData">修改</button>
                <button type="button" class="btn btn-default" data-dismiss="modal" id="btnClose">关闭</button>
            </div>
        </div>
    </div>
</div>
<div id="nav" class="display">
    <button id="cancel"><sapn>取消(Q)...</sapn><sapn class="Shortcut-key">Ctrl+Q</sapn></button>
    <div></div>
    <button id="insert" data-toggle="modal" data-target="#myModal"><sapn>插入(I)...</sapn><sapn class="Shortcut-key">Ctrl+I</sapn></button>
    <button id="update" data-toggle="modal" data-target="#myModal"><sapn>修改(U)...</sapn><sapn class="Shortcut-key">Ctrl+U</sapn></button>
    <button id="delete"><sapn>删除(D)...</sapn><sapn class="Shortcut-key">Ctrl+D</sapn></button>
    <div></div>
    <button id="See" data-toggle="modal" data-target="#myModal"><sapn>查看(S)...</sapn><sapn class="Shortcut-key">Ctrl+S</sapn></button>
</div>
<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
    <input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
</form>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="js/mathUtil.js"></script>
<script src="notice/js/jplayer.min.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="data/js/dataManager.js?d=${time}" type="text/javascript"></script>
<script type="text/javascript">
    initDataTitle();
    bindSpinnerClickEvent();
    bindCancelClickEvent();
    bindSeeClickEvent();
    bindUpdateClickEvent();
    bindSelectedPhoto();
    bindbtnLimitClickEvent();
    ShieldMouseRightKey();
    bindDeleteClickEvent();
    bindInsertClickEvent();
    bindInsertDataAClickEvent();
    bindDeleteALLClickEvent();
    bindcancelDeleteClickEvent();
    bindDetermineClickEvent();
    $(document).ready(function () {
        $(document).mouseup(function(e){
            var _con = $('#nav');   // 设置目标区域
            if(!_con.is(e.target) && _con.has(e.target).length == 0){ // Mark 1
                $('#nav').addClass('display');
                $(document).unbind('keyup');
            }
        });
    });
</script>
</body>
</html>
