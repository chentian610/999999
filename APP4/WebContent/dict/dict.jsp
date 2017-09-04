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
    <link href="dict/css/bootstrap.min.css?d=${time}" rel="stylesheet">
    <link href="dict/css/animate.min.css?d=${time}" rel="stylesheet">
    <link href="dict/css/style.css?d=${time}" rel="stylesheet">
   	<link href="dict/css/page.css?d=${time}" rel="stylesheet">
   	<link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet">
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/style.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <base target="_blank">
    <style type="text/css">
        .hxx-container-left{
            border: 1px solid #F2F2F2;
            float: left;
            margin-left: 25px;
            width: 175px;
            padding-left: 18px;
            padding-right: 18px;
            height: 300px;
            background-color: #F2F2F2;
            background:url(dict/images/background.png) no-repeat;
            background-size: 100% 100%;
            overflow: hidden;
            text-align: center;
            line-height: 246px;
        }
        .hxx-style-cell.active:after{
            display: block;
            position: relative;
            top: -134%;
            content: "";
            width: 100%;
            height: 136%;
            background-color: rgba(0,0,0,0.3);
            background-image: url(dict/images/surebtn2.png);
            background-size: 40%;
            background-position: center;
            background-repeat: no-repeat;
        }
        .zl-phone-box-left{
            position: absolute;
            width: 270px;
            height:350px;
            background: url("dict/images/phone.png") center center no-repeat;
            background-size: 100% 100%;
            bottom: 0;
            left:10%;
            overflow-y:hidden;
        }
        .zl-surebtn-img{
            position: relative;
            display:block;
            float: left;
            width: 21px;
            height: 21px;
            left: 9px;
            margin-top: 4%;
            background:url("dict/images/surebtn.png") center center no-repeat;
            background-size: 100% 100%;
        }
        .zl-left-add{
            position: absolute;
            display: block;
            width: 22px;
            height: 38px;
            top:47%;
            left:2%;
            background:url("dict/images/leftparrows.png") center center no-repeat;
            background-size: 100% 100%;
        }
        .zl-right-add{
            position: absolute;
            display: block;
            width: 22px;
            height: 38px;
            background:url("dict/images/rightarrows.png") center center no-repeat;
            background-size: 100% 100%;
            top:47%;
            right:2%;
        }
        .zl-delete-markbox{
            position: absolute;
            display: block;
            width: 19px;
            height:20px;
            top:24px;
            right:24px;
            background:url("dict/images/closeMark.png") center center no-repeat;
            background-size: 100% 100%;
        }
        .zl-surebtn-img-after{
            background:url("dict/images/surebtn2.png") center center no-repeat;
            background-size: 100% 100%;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="ss-btn-box clear">
        <h4 class="zwf-p">点击拖动可改变现有栏目顺序</h4>
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
            <div class="new-add-btn" style="display: none"><button type="button" class="btn btn-success btn-outline" id="add-dict-group">新增栏目</button></div>
        </div>
         <textarea id="nestable2-output" class="form-control" style="display:none"></textarea>
    </div>
    <div class="zl-mark-float zwf-css-mark" style="display: none;"><div class="zl-content-mark-box zwf-mark-content"> <div class="zl-left-add zwf-mark-left-click"></div> <div class="zl-right-add zwf-mark-right-click"></div> <div class="zl-delete-markbox zwf-mark-click"></div>
        <div class="zl-phone-box-left"> <div class="zl-name-title-model zwf-code-name"></div> <div class="zl-scroll-box">
            <img class="zl-img-model zwf-css-photo" src="" alt=""> </div> </div> <div class="zl-explain-box-right">
            <div class="zl-model-right-name zwf-css-name"></div><p class="zl-model-right-text zwf-css-content"></p>
            <div class="zl-sure-btn update-css"><div class="zl-surebtn-img "></div><div class="zl-surebtn-text">选择此模版</div></div></div>
        </div> </div> </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="hplus/js/plugins/nestable/jquery.nestable.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="dict/js/dict.js?d=${time}"></script>
<script type="text/javascript">
    //session失效，页面跳转
pageJump();
   $(document).ready(
			function() {
                var tempJson;
				var updateOutput = function(e) {
				var list = e.length ? e: $(e.target);
				if (window.JSON) {
				   var json = window.JSON.stringify(list.nestable("serialize"));
				   if (json==tempJson) {
				   		return;
				   }
				   if ((json.indexOf("context"))>0) return ;
					updateDictSort(window.JSON.stringify(list.nestable("serialize")));
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
    var dict_group=getParameterByUrl("dict_group");initData();
    initNewsCssList();
    bindAddDictGroupClick();
    $(document).ready(function () {
        $(document).mouseup(function(e){
            var _con = $('.zwf-mark-content');   // 设置目标区域
            if(!_con.is(e.target) && _con.has(e.target).length == 0){ // Mark 1
                $('.zwf-css-mark').hide();
            }
        });
    });
</script>
</body>
</html>