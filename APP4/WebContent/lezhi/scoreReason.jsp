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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>打分项设置</title>
    <link rel="stylesheet" href="lezhi/css/bootstrap.min1.css" />
    <link rel="stylesheet" type="text/css" href="lezhi/css/style1.css"/>
    <link rel="stylesheet" href="lezhi/css/score.css" />
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container clearfix">
    <div class="fl zjq-list zjq-proList">
        <ul class="list-unstyled" id="typeList">

        </ul>
        <div class="zjq-add">
            <input type="button" class="btn btn-default " value="新增"/>
        </div>
    </div>
    <div class="fl zjq-scoreDetail">
        <dl id="bonus">
            <dt>加分</dt>

        </dl>
        <div class="zjq-add2">
            <input type="button" class="btn btn-default" value="新增"/>
        </div>
        <dl id="reduce">
            <dt>扣分</dt>

        </dl>
        <div class="zjq-add2">
            <input type="button" class="btn btn-default" value="新增"/>
        </div>
    </div>
</div>
<script src="hplus/js/jquery.min.js"></script>
<script src="lezhi/js/jquery-3.0.0.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/scoreReason.js?d=${time}"></script>
<script type="text/javascript">
    //session失效，页面跳转
    pageJump();
    $(function() {
        $(".zjq-proList .zjq-add .btn").click(function () { //点击纪律项的增加按钮添加输入框
            var oInput = '<li class="form-group zjq-addText">' +
                    '<span class="zjq-pro-name"></span>' +
                    '<input type="text" class="zjq-inputText1">' +
                    '<input type="button" class="btn btn-default zjq-saveBtn1" value="保存"/>' +
                    '<input type="button" class="btn btn-default zjq-cancel1" value="取消"/>'  +
                    '<input type="button" class="btn btn-default zjq-editBtn1" value="编辑"/>' +
                    '<input type="button" class="btn btn-default zjq-disableBtn1" value="禁用"/>' +
                    '</li>';
            $(this).parent().siblings('ul').append(oInput);
            $(this).parent().siblings('ul').find(".zjq-saveBtn1").click(function () {
                var a = $(this).siblings(".zjq-inputText1").val();
                addType(a);
            });
            $(this).parent().siblings('ul').find('.zjq-cancel1').click(function (){
                $(this).parent().remove();
            });
            addactive();
            btnShow();
        });
        function addactive() {//给纪律项子项增加active
            var aLi2 = $(".zjq-proList ul li")
            $.each(aLi2, function () {
                $(this).click(function () {
                    $(this).addClass("active").siblings().removeClass("active");
                });
            });
        };
        addactive();
        $(".zjq-scoreDetail .zjq-add2 .btn").click(function () { //点击加分减分项的增加按钮添加输入框
            var oInput = '<form class="form-inline zjq-addText2">' +
                    '<div class="form-group zjq-addSel">' +
                    '<select class="score">' +
                    '<option disabled selected>选择分值</option>' +
                    '<option value="3">+3分</option>' +
                    '<option value="2">+2分</option>' +
                    '<option value="1">+1分</option>' +
                    '<option value="0">0分</option>' +
                    '<option value="-1">-1分</option>' +
                    '<option value="-2">-2分</option>' +
                    '<option value="-3">-3分</option>' +
                    '</select>' +
                    '</div>' +
                    '<div class="form-group zjq-text1">' +
                    '<input type="text" placeholder="输入名称" class="reason"/>' +
                    '</div>' +
                    '<div class="form-group zjq-buttons">' +
                    '<input type="button" class="btn btn-default zjq-saveBtn" value="保存"/>' +
                    '<input type="button" class="btn btn-default zjq-cancel" value="取消"/>'  +
                    '<input type="button" class="btn btn-default zjq-editBtn" value="编辑"/>' +
                    '<input type="button" class="btn btn-default zjq-disable" value="禁用"/>' +
                    '</div>' +
                    '</form>';
            $(this).parent().before(oInput);
            $('.zjq-saveBtn').click(function () {
                var name = $(this).parent().siblings().find('.reason').val();
                var score = $(this).parent().siblings().find('.score').val();
                var obj=$(this);
                addScoreReason(name,score,obj);
            });
            $('.zjq-cancel').click(function(){
                $(this).parent().parent().remove();
            });
        });
    });
    module_code=getParameterByUrl("module_code");
    showScoreType();
</script>
</body>
</html>
