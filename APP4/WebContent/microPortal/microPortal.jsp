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
    <link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" href="microPortal/css/rootPage.css?d=${time}" />
    <link href="article/css/cropper.css" rel="stylesheet">
</head>
<body>
<div class="zjq-wmh-page clearfix" id="editMicroPortal" style="display: none;">
    <div class="zjq-left-electorate">
        <img src="microPortal/images/mobile_model.png" class="zjq-mobile-model">
        <div class="zjq-app-view" id="preview">
            <img src="microPortal/images/pre_view.png">
            <span>预览</span>
        </div>
    </div>
    <div class="zjq-right-electorate">
        <div class="zjq-btn-block">
            <button type="button" class="btn btn-default zjq-role-btn zjq-save-btn" id="Preservation">保存</button>
        </div>
        <div class="zjq-edit-list">
            <div class="zjq-edit-block">
                <h3>上传图片：</h3>
                <div class="zjq-upload-block zjq-edit-detail" id="MicroPortalFile">
                    <img src="microPortal/icon/upload_logo.png" class="zjq-plus-icon">
                    <span>上传图片</span>
                </div>
            </div>
            <div class="zjq-edit-block">
                <h3>构建学校门户：</h3>
                <ul class="zjq-column-list" id="columnList">
                    <li>
                        <div class="zjq-edit-detail addMicroPortal">
                            <input type="text" class="form-control columnName" placeholder="输入栏目名称">
                            <textarea class="form-control columnValue" rows="3" placeholder="输入栏目内容"></textarea>
                        </div>
                        <img src="microPortal/icon/del_logo.png" class="removeColumn">
                    </li>
                </ul>
                <div class="zjq-addBtn-block zjq-edit-detail">
                    <button type="button" class="btn btn-default zjq-add-btn" id="addColumn">添加新栏目</button>
                </div>
            </div>
            <div class="zjq-edit-block zjq-school-edit">
                <h3>学校基本信息：</h3>
                <ul class="zjq-column-list" id="CampusList">
                    <li>
                        <div class="zjq-edit-detail addMicroPortal">
                            <input type="text" class="form-control campusName" placeholder="输入校区名称">
                            <textarea class="form-control address" rows="3" placeholder="输入校区地址"></textarea>
                            <input type="text" class="form-control phone" placeholder="输入联系电话">
                            <input type="text" class="form-control email" placeholder="输入联系邮箱">
                        </div>
                        <img src="microPortal/icon/del_logo.png" class="removeCampus">
                    </li>
                </ul>
                <div class="zjq-addBtn-block zjq-edit-detail">
                    <button type="button" class="btn btn-default zjq-add-btn" id="addCampus">添加新校区</button>
                </div>
            </div>
        </div>
    </div>
</div>
<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
    <input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
</form>
<button type="button" class="btn btn-primary" id="BtnModal" data-toggle="modal" data-target="#myModal" style="display: none;">
</button>
<div class="modal inmodal in" id="myModal" tabindex="-1" role="dialog" aria-hidden="true" style="display: none; padding-left: 6px;">
    <div class="modal-dialog" style="width: 479px;">
        <div class="modal-content animated bounceInRight" id="modalContent">
                <div class="zjq-pic-sel">
                    <h3>选取图片</h3>
                    <div class="zjq-image-container">
                        <img src="" id="fileUrl">
                    </div>
                    <div class="zjq-btns clearfix">
                        <button type="button" class="btn btn-default fl zwf-btn" id="Step" style="display: none;">上一步</button>
                        <button type="button" class="btn btn-default fl zwf-btn" id="cutImages">裁剪图片</button>
                        <button type="button" class="btn btn-default fr zwf-btn" id="uploadImages" data-dismiss="modal">确定上传</button>
                    </div>
                </div>
        </div>
    </div></div>
<div class="zjq-wmh-page" style="display: none;" id="previewEffect">
    <div class="zjq-editBtn-block">
        <button type="button" class="btn btn-default" id="editContent">编辑</button>
    </div>
    <div class="zjq-modelView-block">
        <div class="zjq-model-view">
            <div class="zjq-content-block">
                <div class="zjq-content-view">
                    <div class="zjq-header">
                        <img src="microPortal/icon/state_logo.png" class="zjq-state-logo"/>
                        <h3>
                            <img src="microPortal/icon/return_logo.png" class="zjq-return-logo"/>
                            <span class="schoolName"></span>
                        </h3>
                    </div>
                    <div class="zjq-banner">
                        <div id="banner-pic1">
                        </div>
                        <div id="banner-nav1">
                        </div>
                    </div>
                    <div class="zjq-school-content" id="previewContent">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<button type="button" class="btn btn-primary" id="BtnModal1" data-toggle="modal" data-target="#myModal5" style="display: none;">
</button>
<div class="modal inmodal fade in" id="myModal5" tabindex="-1" role="dialog" aria-hidden="true" style="display: none; padding-left: 6px;">
    <div class="modal-dialog modal-lg" style="width: 700px;">
            <div class="zjq-school-view">
                    <div class="zjq-wmh-page">
                        <div class="zjq-model-view">
                            <div class="zjq-content-block">
                                <div class="zjq-content-view">
                                    <div class="zjq-header">
                                        <img src="microPortal/icon/state_logo.png" class="zjq-state-logo"/>
                                        <h3>
                                            <img src="microPortal/icon/return_logo.png" class="zjq-return-logo"/>
                                            <span class="schoolName">课淘小学</span>
                                        </h3>
                                    </div>
                                    <div class="zjq-banner">
                                        <div id="banner-pic">
                                        </div>
                                        <div id="banner-nav">
                                            <span></span>
                                            <span></span>
                                            <span></span>
                                        </div>
                                    </div>
                                    <div class="zjq-school-content" id="Content">
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
    </div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="article/js/cropper.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="hplus/js/prettify.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="microPortal/js/microPortal.js?d=${time}"></script>
<script type="text/javascript">
    (function () {
        initPage();
    })();
</script>
</body>
</html>
