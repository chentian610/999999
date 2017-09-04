<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<head>
	<base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>学校管理后台</title>
    <link href="hplus/css/bootstrap.css" rel="stylesheet">
  	<link href="article/css/page.css?d=${time}" rel="stylesheet">
	<link href="article/css/adminnews.css?d=${time}" rel="stylesheet">
 	<link href="article/css/style.min.css?d=${time}" rel="stylesheet">
    <link href="article/css/main.css?d=${time}" rel="stylesheet">
    <link href="article/css/font-awesome.min.css?d=${time}" rel="stylesheet">
    <link href="article/css/cropper.css?d=${time}" rel="stylesheet">
 	<link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <style type="text/css">
        .border-radius {     border-radius: 90px;
            width: 90px !important;
            height: 90px !important;
            margin: 0 auto !important;
            overflow: hidden !important;
            margin-bottom: 50px !important;}
        .zwf-border-radius {     border-radius: 90px;
            width: 120px !important;
            height: 120px !important;
            margin: 0 auto !important;
            overflow: hidden !important;
            margin-bottom: 50px !important;}
        .zwf-border-radius img {
            width:120px;
            height:120px;
            border-radius: 90px;
            display:block;
            margin: 0 auto;
        }
        .zl-content-text img {
            width: 235px;
            float: left;
        }
        .editor img {
            width: 100%;
            float: left;
        }
    </style>
</head>
<body>
<div class="ibox-content">
    <form class="form-horizontal">
        <div class="row">
            <div class="col-sm-8  col-sm-left">
                <div class="form-group">
                    <label class="col-sm-2 control-label">标题</label>
                    <div class="col-sm-8">
                        <input type="text" placeholder="请输入标题" class="zwf-news-keyup form-control" id="article_title">
                        <!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">选择发布栏目</label>
                    <div class="col-sm-8">
                        <select class="form-control m-b" name="account" style="display: none" id="article-classify">
                            <option>选项 1</option>
                            <option>选项 2</option>
                            <option>选项 3</option>
                            <option>选项 4</option>
                        </select>
                    </div>
                    <div class="col-sm-8 get-width">
                        <div class="zwf-news-div get-width" style=" line-height: 32px;display: none" >
                            <span id="zwf-code-name" data-printscreen="1" data-news_code="" data-news_group="" style=" margin-left: 12px;">请选择发布栏目</span>
                            <span data-is-hide="none" id="btnSelect"><img style="margin-top: 8px; float: right; margin-right: 1px;" src="article/images/selectArrow.png"/></span>
                        </div>
                        <div class="zwf-news-div-css" style="display: none" id="news-code-list">
                            <div>
                                <span data-is-hide="none" data-news_group="022005" class="zwf-news-select" style="width: 100%;height: 20px;"><img style="margin-top: 2px; float: left;" src="article/images/selectArrow.png"/>
                                <span>1</span></span>
                                <div style="display: none;" data-news_group="022005" class="zwf-news-group022005">
                                    <div data-news_code="025001" class="zwf-news-code">2</div>
                                    <div data-news_code="025002" class="zwf-news-code">2</div>
                                    <div data-news_code="025003" class="zwf-news-code">2</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="zwf-news-change col-sm-2 control-label">发件人</label>
                    <div class="col-sm-8">
                        <input type="text" placeholder="请输入发件人" class="zwf-news-keyup form-control" id="dept_name">
                        <!--<span class="help-block m-b-none">请输入您注册时所填的E-mail</span>-->
                    </div>
                </div>
                <div class="zwf-news-module form-group" hidden="hidden">
                    <label class="col-sm-2 control-label">选择模板</label>
                    <div class="col-sm-8">
                        <select class="zwf-news-change form-control m-b" name="account" id="article-module">
                            <option>选项 1</option>
                            <option>选项 2</option>
                            <option>选项 3</option>
                            <option>选项 4</option>
                        </select>
                    </div>
                </div>
                <div class="zwf-news-img form-group">
                    <label class="col-sm-2 control-label">图片</label>
                    <div class="zwf-news-logo col-sm-8">
                        <div class="sch-logo" hidden="hidden">
                            <img src="images/gzh_wx.jpg" id="showLogo" class="zwf-news-change"/>
                            <button id="uploadPic" type="button" class="btn btn-primary btn-upload" title="请先选择发布栏目">请先选择发布栏目</button>
                            <span><!-- 建议200x200分辨率,仅支持JPG、JPEG、GIF、PNG格式的图片文件，文件不能大于2MB --></span>
                        </div>
                        <div id="UploadFileBox" hidden="hidden">
                            <div id="zwfMsg" class="zl-uploadfile-box">
                                <img  id="showPhoto" height="auto" width="100%" style="position:relative;z-index: 0;left:0%;" src="article/images/imgPhoto.png"/>
                                <img  id="showLogoOld" data-is_cut="false"  height="auto" width="100%"  style=" display:block; position:relative;z-index: 0;left:0%;border:none;" src="article/images/imgPhoto.png"/>
                                <div id="hiddenDiv" style="position: absolute; width: 100%;top: 36%;"><img class="zl-img-upload" src="article/images/uploadtip.png" alt="">上传图片<span>(上传后在此处预览)</span></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="zl-box-tip" id="uploadPicOld" style="display: none;">
                        <button id="selectedPhoto" type="button" class="btn btn-primary btn-outline btn-upload" title="请先选择发布栏目">请先选择发布栏目</button>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">发布内容</label>
                    <div class="col-sm-8">
                        <div class="btn-toolbar ibox-content" data-role="editor-toolbar" data-target="#editor">
                            <div class="zwf-news-ys btn-group">
                                <a class="btn dropdown-toggle" data-toggle="dropdown" title="Font"><i class="icon-font"></i><b class="caret"></b></a>
                                <ul class="dropdown-menu"> </ul> </div> <div class="zwf-news-ys btn-group">
                            <a class="btn dropdown-toggle" data-toggle="dropdown" title="Font Size"><i class="icon-text-height"></i>&nbsp;<b class="caret"></b></a>
                            <ul class="dropdown-menu"> <li><a data-edit="fontSize 5"><font size="5">大</font></a></li>
                                <li><a data-edit="fontSize 3"><font size="3">中</font></a></li> <li><a data-edit="fontSize 1"><font size="1">小</font></a></li>
                                </ul> </div> <div class="btn-group"> <a class="btn" data-edit="bold" title="Bold (Ctrl/Cmd+B)"><i class="icon-bold"></i></a>
                            <a class="btn" data-edit="italic" title="Italic (Ctrl/Cmd+I)"><i class="icon-italic"></i></a>
                            <a class="btn" data-edit="strikethrough" title="Strikethrough"><i class="icon-strikethrough"></i></a>
                            <a class="btn" data-edit="underline" title="Underline (Ctrl/Cmd+U)"><i class="icon-underline"></i></a> </div>
                            <div class="zwf-news-ys btn-group"> <a class="btn" data-edit="insertunorderedlist" title="Bullet list"><i class="icon-list-ul"></i></a>
                            <a class="btn" data-edit="insertorderedlist" title="Number list"><i class="icon-list-ol"></i></a>
                            <a class="btn" data-edit="outdent" title="Reduce indent (Shift+Tab)"><i class="icon-indent-left"></i></a>
                            <a class="btn" data-edit="indent" title="Indent (Tab)"><i class="icon-indent-right"></i></a> </div>
                            <div class="zwf-news-ys btn-group"> <a class="btn" data-edit="justifyleft" title="Align Left (Ctrl/Cmd+L)"><i class="icon-align-left"></i></a>
                            <a class="btn" data-edit="justifycenter" title="Center (Ctrl/Cmd+E)"><i class="icon-align-center"></i></a>
                            <a class="btn" data-edit="justifyright" title="Align Right (Ctrl/Cmd+R)"><i class="icon-align-right"></i></a>
                            <a class="btn" data-edit="justifyfull" title="Justify (Ctrl/Cmd+J)"><i class="icon-align-justify"></i></a> </div>
                            <div class="zwf-news-ys btn-group"> <a class="btn dropdown-toggle" data-toggle="dropdown" title="Hyperlink"><i class="icon-link"></i></a>
                            <div class="dropdown-menu input-append"> <input class="span2" placeholder="URL" type="text" data-edit="createLink"/>
                                <button class="btn" type="button">Add</button> </div> <a class="btn" data-edit="unlink" title="Remove Hyperlink"><i class="icon-cut"></i></a>
                            </div> <div class="zwf-news-ys btn-group"> <a class="btn" title="图片" id="picturebtn"><i class="icon-picture"></i></a>
                            <a class="btn" data-edit="undo" title="Undo (Ctrl/Cmd+Z)"><i class="icon-undo"></i></a>
                            <a class="btn" data-edit="redo" title="Redo (Ctrl/Cmd+Y)"><i class="icon-repeat"></i></a> </div>
                        </div>
                        <!-- 文本容器 -->
                        <div id="editor" class="zwf-news-keyup editor">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">发送时间</label>
                    <div class="col-sm-8">
                        <div class="checkbox i-checks">
                            <input id="deploy_date" class="form-control layer-date" placeholder="输入发布时间" onclick="laydate({istime: true, format: 'YYYY-MM-DD'})">
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-3 col-sm-8">
                        <button type="button" class="btn btn-primary btn-outline" style="padding: 5px 20px;" id="addButton">发送</button>
                    </div>
                </div>
            </div>
            <div class="col-sm-4 col-sm-right">
                <div id="zwf-news-yl" hidden="hidden">
                    <div id="news-template-type-035005" class="zl-preview-box-img" >
                        <div class="zl-title-new" ></div>
                        <div  class="zl-content">
                            <div id="UpLoadTitle" class="zl-new-title zwf-news-empty"></div>
                            <div id="UpLoader" class="zl-new-people zwf-news-empty">发件人:<span class="zwf-news-empty zwf-dept-name"></span></div>
                            <div class="zl-line-img"><img src="article/images/new-line.jpg"/></div>
                            <div class="zwf-img-preview " style="margin: 0 auto; float:none;"><img id="UploaderLookImgs" class="zl-content-img zwf-upload-photo" src="article/images/imgPhoto.png"/></div>
                            <div class="zl-content-box">
                                <p class="zl-content-text zwf-news-empty"></p>
                            </div>
                        </div>
                    </div>
                    <div id="news-template-type-035030" class="zl-preview-box-img">
                        <div class="zl-title-new" ></div>
                        <div  class="zl-content zl-content-school">
                            <div class="zwf-img-preview zl-img-background" style="margin: 0 auto; float:none;"><img id="UploaderLookImgs" class="zl-content-img zwf-upload-photo zl-img-forschool-speak" src="article/images/imgPhoto.png"/></div>
                            <div class="zl-content-box zl-content-box-text">
                                <div class="zl-content-top">
                                    <div class="zl-content-title-top">校长致辞</div>
                                    <div class="zl-content-speake-top">亲爱的同学们:</div>
                                </div>
                                <p class="zl-content-text zwf-news-empty zl-content-box-school"></p>
                                <div id="UpLoader" class="zl-new-people zwf-news-empty zl-school-name-people"><span class="zwf-news-empty zwf-dept-name zl-school-name-peopele-top">校长名</span></div>
                            </div>

                        </div>
                    </div>
                    <div id="news-template-type-035010" class="zl-preview-box-img">
                        <div class="zl-title-new"></div>
                        <div  class="zl-content">
                            <div id="UpLoadTitleNo" class="zl-new-title zwf-news-empty"></div>
                            <div id="UpLoaderNo" class="zl-new-people">发件人:<span class="zwf-news-empty zwf-dept-name"></span></div>
                            <div class="zl-content-box">
                                <p class="zl-content-text zwf-news-empty"></p>
                            </div> </div>
                    </div>
                    <div id="news-template-type-035015" class="zl-preview-box-img">
                        <div class="zl-title-new"></div>
                        <div  class="zl-content">
                            <div class="zl-hong-title"><span id="school_name" class="zwf-news-empty"></span>宣传文件</div>
                            <div id="UpLoaderHong" class="zl-new-people">发件人:<span class="zwf-news-empty zwf-dept-name"></span></div>
                            <div class="zl-line-img"><img src="article/images/new-linehongtou.jpg"/></div>
                            <div class="zl-content-box">
                                <div id="UpLoadTitleHong" class="zl-new-title zwf-news-empty"></div>
                                <p class="zl-content-text zwf-news-empty"></p>
                            </div> </div>
                    </div>
                    <div id="news-template-type-035020" class="zl-preview-box-img" >
                        <div class="zl-title-new"></div>
                        <div  class="zl-content">
                            <div id="UpLoadTitle" class="zl-new-title zwf-news-empty"></div>
                            <div class="zl-active-top"><img src="article/images/active-1.png"/></div>
                            <div id="UpLoader" class="zl-new-people"><img src="article/images/active-pen.png" width="15px" height="auto"/> <span class="zwf-news-empty zwf-dept-name"></span></div>
                            <div class="zwf-img-preview" style="margin: 0 auto; float:none;"><img id="UploaderLookImg" class="zl-content-img zwf-upload-photo" src="article/images/imgPhoto.png"/></div>
                            <div class="zl-content-box ">
                                <div class="zl-active-backimg"><div class="zl-content-text zwf-news-contest-css"></div></div>
                                <img src="article/images/active_back.png" width="240px" height="50px"/>
                            </div> </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
        <input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
    </form>
    <form hidden="true" class="btn-group" name="Form" id="Form" method="post" enctype="multipart/form-data">
        <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
    </form>
</div>
<script src="article/js/jquery-2.1.4.js"></script>
<script src="hplus/js/bootstrap.js"></script>
<script src="article/js/cropper.js?d=${time}"></script>
<script src="article/js/main.js?d=${time}"></script>
<script src="js/alerts.js"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="js/functionUtil.js"></script>
<script src="article/js/article_deploy.js?d=${time}"></script>
<script src="hplus/js/bootstrap-wysiwyg.js"></script>
<script src="hplus/js/jquery.hotkeys.js"></script>
<script src="hplus/js/prettify.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>

<script>
    //session失效，页面跳转
    pageJump();
    $(document).ready(function () {
        $(document).mouseup(function(e){
            var _con = $('.get-width');   // 设置目标区域
            if(!_con.is(e.target) && _con.has(e.target).length == 0){ // Mark 1
                $('#news-code-list').hide();
                $('#btnSelect').attr('data-is-hide','none');
                $('.zwf-news-div').css('border','1px solid #e5e5e5');
            }
        });
    });
    //加载栏目名称列表
    $(document).ready(function() {
        $('#form-file').attr('action',localStorage.getItem('file_upload_action'));
        $('#Form').attr('action',localStorage.getItem('file_upload_action'));
        var dict_group=getParameterByUrl("dict_group");
        loadModuleClassifyList();
        bindAddButtonClickEvent();
        bindUploadPicClickEvent();
        bindUploadLogoClickEvent();
        bindEditorPicBtnClickEvent();
        initSelectBtuClickEvent();
        initPage();
        initPreview();
        initKeyUpEvent();
        if (dict_group.length>4) loadArticleClassifyList();
        else loadDictSchoolList();
    });
</script>
</body>
</html>