<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
%>
<!DOCTYPE HTML >
<html>
<head>
<base href="<%=basePath%>">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta name="keywords" content="H+后台主题,后台bootstrap框架,会员中心主题,后台HTML,响应式后台">
    <meta name="description" content="H+是一个完全响应式，基于Bootstrap3最新版本开发的扁平化主题，她采用了主流的左右两栏式布局，使用了Html5+CSS3等现代技术">
    <title>学校管理后台</title>
      <link rel="shortcut icon" href="hplus/favicon.ico"> 
    <link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet">
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/style.min.css" rel="stylesheet">
    <link href="lezhi/css/page.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <base target="_blank">
</head>
<body>
<div class="wrapper wrapper-content ">
    <div class="ss-btn-box clear">
        <div class="r-sel-box clear">
            <div class="btn-box">
            </div>
        </div>
    </div>
    <div  class="column-list dd" id="nestable2">
        <ol  class="dd-list" id="mainol">
        </ol>
         <div class="new-add-column">
            <div class="add-input-box clear" id="newObject">
            </div>
            <div class="new-add-btn"><button type="button" class="btn btn-primary" id="new-add-btn">新增项目</button></div>
        </div> 
         <textarea id="nestable2-output" class="form-control" style="display:none"></textarea>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="hplus/js/plugins/nestable/jquery.nestable.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/code.js?d=${time}"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript">
//session失效，页面跳转
pageJump();
$(document).ready(
        function() {
            var updateOutput = function(e) {
                var list = e.length ? e: $(e.target);
                if (window.JSON) {
                    var json = window.JSON.stringify(list.nestable("serialize"));
                    if (json==tempJson) {
                        return;
                    }
                    if ((json.indexOf("context"))>0) return ;
                    updateAttendSort(window.JSON.stringify(list.nestable("serialize")));
                } else {
                    alert("浏览器不支持JSON数据转换！");
                }};
            $("#nestable2").nestable({group : 1}).on("change", updateOutput);
            updateOutput($("#nestable2").data("output",$("#nestable2-output")));
            $("#nestable-menu").on("click",function(e) {
                var target = $(e.target), action = target.data("action");
                if (action === "expand-all") {
                    $(".dd").nestable("expandAll");
                }
                if (action === "collapse-all") {
                    $(".dd").nestable("collapseAll");}
            });
        });
		$(function(){
        var par;
        var _input = $('<input type="text" placeholder="请输入项目名称" class="input-text" id="input1">');
        var _inpnum = $('<input type="text" class="input-num">').css({
            'position':'absolute',
            'width':'30px',
            'height':'28px',
            'border':'1px solid #e5e5e5',
            'top':'0',
            'left':'0'
        });
        $('.column-list').on('click','.btn-rename ',function(){
            var par = $(this).parent();
            par.prepend(_input);
            var uName = $(this).parent().parent().attr("data-dict_value");
            par.children(".input-text").attr("placeholder",uName);
             bindInputClick();
            $(this).text('确定').addClass('btn-qd').removeClass('btn-rename');
            $(this).after('<button type="button" class="btn btn-primary btn-sm btn-qx pull-right" style="margin-left: 5px;">取消</button>');
               updateAttendName();
            bindBtnAddClick();
        });
       
        $('.column-list').on('click','.btn-qx',function(){
            $('.input-text').remove();
            $(this).remove();
            $('.btn-qd').text('重命名').addClass('btn-rename').removeClass('btn-qd');
            bindBtnAddClick();
        });
    });
    var dict_group=getParameterByUrl("dict_group");
    initData();
    newxiangmu();
</script>
</body>
</html>