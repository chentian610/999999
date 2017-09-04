<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <!-- Data Tables -->
    <link href="notice/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" href="notice/css/load.animation.css"/>
    <link rel="stylesheet" href="notice/css/noticeDeploy.css?d=${time}"/>
    <link rel="stylesheet" href="notice/css/style.min.css"/>
</head>
<body>
<div class="container">
    <div class="input-group">
        <textarea type="text" class="form-control" placeholder="选择联系人" id="SelectContacts" rows="1" onfocus=this.blur()></textarea>
        <span class="input-group-btn">
            <button id="showClass" type="button" class="btn btn-default" data-toggle="modal" data-target="#myModal5">选择</button>
        </span>
    </div>
    <br/>
    <div class="input-group">
        <input type="text" class="form-control" placeholder="输入通知标题" id="NoticeTitle">
    </div>
    <br>
    <div class="input-group">
        <textarea placeholder="输入通知内容" class="form-control" rows="7" id="NoticeContent"></textarea>
    </div>
    <div class="hxx-container" id="FileList">
    </div>

    <span class="input-group">
        <button id="UploadPictures" class="btn btn-primary">上传图片</button>
    </span>

    <button type="submit" class="btn btn-block btn-primary" style="width: 350px;margin-top: 15px;" id="SendOutNotice">发送</button>
</div>
<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
    <input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
</form>
<div class="modal inmodal fade" id="myModal5" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal"><span class="box-btn-close"></span></button>
                    <div class="box box-sender" style="margin-top: -25px;">
                        <div class="sender-container">
                            <div class="sender-selected">
                                <h5 class="box-header">已选择</h5>
                                <ul class="box-body" id="SelectedList">
                                </ul>
                            </div>
                            <div class="group-selected" id="ContactGrouping">
                                <h5 class="box-header">通讯录<span id="showTip" class="btn btn-primary" data-toggle="modal" data-target="#myModal6" data-dismiss="modal" >选择单个学生</span><span class="btn btn-primary display" id="SelectGroups">选择分组</span></h5>
                                <div class="form-group display" id="SearchFrame">
                                    <div class="col-xs-9 col-md-9 col-sm-9 col-lg-9" style="position:relative; margin-top: 5px;">
                                        <input type="search" name="" id="Search" class="form-control input-search" value="" placeholder="输入人名" required="required" title="" style=" padding-left: 30px;">
                                        <span class="icon-search"></span>
                                    </div>
                                    <span for="Search" class="col-xs-2 col-md-2 col-sm-2 col-lg-2 control-label btn btn-primary" id="FuzzySearch" style=" margin-top: 5px;">搜索</span>
                                </div>
                                <ul class="box-body" id="ContactList">
                                </ul>
                            </div>
                        </div>
                        <div class="btn-footer row">
                            <div class="col-xs-3 col-md-3 col-sm-3 col-lg-3 zwf-button-left"><button type="button" class="btn btn-default btn-cancel" data-dismiss="modal">取消</button></div>
                            <div class="col-xs-3 col-md-3 col-sm-3 col-lg-3 zwf-button-right"><button type="button" class="btn btn-primary btn-comfirm" data-dismiss="modal" data-toggle="modal" id="PreservationNotice">保存</button></div>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>
<div class="modal inmodal fade" id="myModal6" tabindex="-1" role="dialog"  aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content" style="width: 500px;left: -35%;top: 100px;">
            <div class="modal-body">
                <button type="button" class="close" data-dismiss="modal"><span class="box-btn-close"></span></button>
                <div class="box box-tip" style="margin-top: -25px;">
                    <div class="tip-header">提示</div>
                    <div class="tip-body">你无法在选择分组的时候同时选择单个学生，如果选择单个学生，已选择的分组将被清空，是否继续？</div>
                    <div class="tip-footer row">
                        <div class="col-xs-offset-3 col-md-offset-3 col-sm-offset-3 col-lg-offset-3 col-xs-3 col-md-3 col-sm-3 col-lg-3"><button type="button" class="btn btn-white btn-cancel" data-dismiss="modal" data-toggle="modal" data-target="#myModal5">取消</button></div>
                        <div class="col-xs-3 col-md-3 col-sm-3 col-lg-3"><button type="button" id="deleteComfirm" class="btn btn-danger btn-comfirm" data-dismiss="modal" data-toggle="modal" data-target="#myModal5">删除分组</button></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="hplus/js/prettify.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="notice/js/noticeDeploy.js?d=${time}"></script>
<script type="text/javascript">
    initHomePage();
</script>
</body>
</html>
