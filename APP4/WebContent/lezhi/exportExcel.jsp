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
    <title>统计报表管理</title>
    <link rel="stylesheet" href="hplus/css/bootstrap.min.css" />
    <link rel="stylesheet" href="hplus/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="lezhi/css/build.css">
    <script type="text/javascript" src="lezhi/js/jquery-3.0.0.min.js"></script>
    <style>
        body{
            background: #f2f2f2;
        }
        li{
            list-style: none;
        }
        .zjq-countManage-page{
            background:#fff;
            width: 926px;
            margin: 100px auto 0;
            padding: 5px 99px 0 92px;
        }
        .zjq-countManage-page .radio-info input[type="radio"]:checked + label::after {
            background-color: #1CBB9E;
        }
        .zjq-countManage-page .radio-info input[type="radio"] + label::after {
            background-color: #1CBB9E;
        }
        .zjq-countManage-page .radio-info input[type="radio"]:checked + label::before {
            border-color: #D7D3CD;
        }
        .zjq-countManage-page .radio label{
            padding-left: 15px;
            margin-right: 13px;
        }
        .zjq-countManage-page .zjq-count-detail{

        }
        .zjq-countManage-page .zjq-count-detail h3{
            font-size: 18px;
            color: #4A4A4A;
            margin:60px 0 23px 0;
        }
        .zjq-countManage-page .zjq-check-box{
            padding: 0 0 46px 40px;
            font-size: 16px;
            color: #4A4A4A;
            height: 20px;
            border-bottom: 1px solid #E0E0E0;
        }
        .zjq-countManage-page .zjq-check-box .radio-inline{
            min-width: 128px;
        }
        .zjq-countManage-page .zjq-check-box .zjq-self-sel.radio-inline{
            min-width: 54px;
        }
        .zjq-countManage-page .zjq-self-sel.radio label{
            padding-left: 15px;
            margin-right: 0;
        }
        .zjq-countManage-page .zjq-active-icon{
            display: block;
            width: 0;
            height: 0;
            border-left: 7px solid transparent;
            border-right: 7px solid transparent;
            border-bottom: 7px solid transparent;
            margin-left: 46%;
            margin-top: 16px;
        }
        .zjq-countManage-page .box-active .zjq-active-icon{
            border-bottom: 7px solid #E0E0E0;
        }
        .zjq-countManage-page .zjq-sel-grade,.zjq-countManage-page .zjq-sel-class{
            font-size: 14px;
            color: #4A4A4A;
            margin-top: 22px;
            display: none;
        }
        .zjq-countManage-page .zjq-sel-grade .radio-inline,.zjq-countManage-page .zjq-sel-class .radio-inline{
            min-width: 84px;
            margin: 0 8px 20px 0;
        }
        .zjq-countManage-page .zjq-sel-grade .radio label,.zjq-countManage-page .zjq-sel-class .radio label{
            padding-left: 10px;
            margin-right: 0;
        }
        .zjq-countManage-page .zjq-sel-class{
            margin: 0 0 42px 0;
        }
        .zjq-countManage-page .zjq-class-choice{
            margin-left: 120px;
            margin-top: -17px;
        }
        .zjq-countManage-page .zjq-table-sel{
            margin-top: 25px;
            font-size: 14px;
            width: 100%;
            display: none;
        }
        .zjq-countManage-page .zjq-table-sel .zjq-top-input{
            margin-bottom: 8px;
        }
        .zjq-countManage-page .zjq-append-table{
            width: 100%;
            height: 35px;
            line-height: 23px;
            background: #1CBB9E;
            border: #1CBB9E;
            font-size: 15px;
            color: #FFFFFF;
            margin: 84px 0 77px 0;
        }
        .zjq-countManage-page .zjq-append-table img{
            width: 19px;
            height: 20px;
            margin-right: 7px;
            vertical-align: top;
        }
        .zjq-countManage-page .zjq-check-box.zjq-sort-count div:nth-child(4n+1){
            margin-left: 0;
         }
         .zjq-countManage-page .zjq-check-box.zjq-sort-count{
             height: auto;
             padding-bottom:4px;
         }
         .zjq-countManage-page .zjq-check-box.zjq-sort-count .radio-inline{
             margin-bottom: 20px;
         }
    </style>
</head>
<body >
<div class="zjq-countManage-page" style="margin-top: 20px;">
    <ul>
        <li class="zjq-count-detail" id="classroom">
            <h3>统计范围：</h3>
            <div class="zjq-check-box">
                <div class="radio radio-info radio-inline zjq-school-name" >
                    <input type="radio" id="inlineRadio1" value="028010" name="count">
                    <label for="inlineRadio1">按全校范围</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline zjq-grade-name">
                    <input type="radio" id="inlineRadio2" value="028010" name="count">
                    <label for="inlineRadio2">按年级范围</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline zjq-class-name">
                    <input type="radio" id="inlineRadio3" value="028005" name="count">
                    <label for="inlineRadio3">按班级范围</label>
                    <span class="zjq-active-icon"></span>
                </div>
            </div>
            <div class="zjq-sel-grade">
                请选择年级：
                <div class="zjq-class-choice" id="grade">

                </div>
            </div>
            <div class="zjq-sel-class">
                请选择班级：
                <div class="zjq-class-choice" id="classname">

                </div>
            </div>
        </li>
        <li class="zjq-count-detail" id="bedroom">
            <h3>统计范围：</h3>
            <div class="zjq-check-box">
                <div class="radio radio-info radio-inline zjq-school-name" >
                    <input type="radio" id="inlineRadio4" value="028010" name="count">
                    <label for="inlineRadio4">按全校范围</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline zjq-grade-name">
                    <input type="radio" id="inlineRadio5" value="028005" name="count">
                    <label for="inlineRadio5">按寝室范围</label>
                    <span class="zjq-active-icon"></span>
                </div>
            </div>
            <div class="zjq-sel-grade">
                请选择寝室：
                <div class="zjq-class-choice" id="bed">

                </div>
            </div>
        </li>
        <li class="zjq-count-detail" id="typeList">
            <h3>统计类别：</h3>
            <div class="zjq-check-box zjq-sort-count" id="type">

            </div>
        </li>
        <li class="zjq-count-detail">
            <h3>统计时间：</h3>
            <div class="zjq-check-box">
                <div class="radio radio-info radio-inline">
                    <input type="radio" id="inlineRadio19" value="027005" name="time">
                    <label for="inlineRadio19">今天</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline">
                    <input type="radio" id="inlineRadio20" value="027010" name="time">
                    <label for="inlineRadio20">本周</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline">
                    <input type="radio" id="inlineRadio21" value="027015" name="time">
                    <label for="inlineRadio21">当月</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline">
                    <input type="radio" id="inlineRadio22" value="027020" name="time">
                    <label for="inlineRadio22">今年</label>
                    <span class="zjq-active-icon"></span>
                </div>
                <div class="radio radio-info radio-inline zjq-self-sel">
                    <input type="radio" id="inlineRadio23" value="027" name="time">
                    <label for="inlineRadio23">自定义</label>
                    <span class="zjq-active-icon"></span>
                </div>
            </div>
            <div class="form-horizontal zjq-table-sel">
                <div class="zjq-top-input">
                    <input id="start_date" type="text" class="form-control laydate-icon" placeholder="统计起始时间">
                </div>
                <div>
                    <input id="end_date" type="text" class="form-control laydate-icon" placeholder="统计结束时间">
                </div>
            </div>
            <button id="exporttongji" type="button" class="btn btn-default zjq-append-table"><img src="lezhi/icon/table_append.png"/>导出Excel表格</button>
        </li>
    </ul>
</div>
<a id="download"><span id="dl"></span></a>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script src="lezhi/js/jquery-3.0.0.min.js" type="text/javascript"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script type="text/javascript" src="lezhi/js/exportExcel.js?d=${time}"></script>
<script type="text/javascript">
    $(function(){
        var start = {
            elem: '#start_date', //选择ID为START的input
            format: 'YYYY-MM-DD', //自动生成的时间格式
            istime: true, //必须填入时间
            start: laydate.now(0,"YYYY-MM-DD")  //设置开始时间为当前时间
        };
        var end = {
            elem: '#end_date',
            format: 'YYYY-MM-DD',
            istime: true,
            start: laydate.now(0,"YYYY-MM-DD")
        };
        laydate(start);
        laydate(end);
        $('#inlineRadio23').click(function(){
            $('.zjq-self-sel').addClass("box-active");
            $('.zjq-table-sel').show();
            $(this).parent().siblings().children().click(function(){
                $('.zjq-self-sel').removeClass("box-active");
                $('.zjq-table-sel').hide();
            });
        });
        $('.zjq-grade-name').click(function(){
            $(this).addClass("box-active");
            $('.zjq-sel-grade').show();
            $('.zjq-class-name').click(function(){
                $('.zjq-grade-name').removeClass("box-active");
                $(this).addClass("box-active");
                $('.zjq-sel-class').show();
            });
            $('.zjq-school-name').click(function(){
                $('.zjq-grade-name').removeClass("box-active");
                $('.zjq-class-name').removeClass("box-active");
                $('.zjq-sel-class').hide();
                $('.zjq-sel-grade').hide();
            });
        });
        $('.zjq-class-name').click(function(){
            $(this).addClass("box-active");
            $('.zjq-sel-grade').show();
            $('.zjq-sel-class').show();
            $('.zjq-grade-name').click(function(){
                $(this).addClass("box-active");
                $('.zjq-class-name').removeClass("box-active");
                $('.zjq-sel-class').hide();
            });
            $('.zjq-school-name').click(function(){
                $('.zjq-grade-name').removeClass("box-active");
                $('.zjq-class-name').removeClass("box-active");
                $('.zjq-sel-class').hide();
                $('.zjq-sel-grade').hide();
            });
        });

    });
    var dict_group=getParameterByUrl("dict_group");
    var module_code=getParameterByUrl("module_code");
    if (module_code=='009021') $('#classroom').hide();
    else $('#bedroom').hide();
    if (module_code=='009025') $('#typeList').hide();
    if (module_code=='009022') $('#inlineRadio3').parent().hide();
    if (module_code=='009008') $('#typeList').hide();
    if (module_code=='009027') {
        $('#typeList').hide();
        $('#classroom').hide();
    }
    //session失效，页面跳转
    pageJump();
    showGradeInfo();
    showType();
    showBedroom();
    exporttongji();
</script>
</body>
</html>

