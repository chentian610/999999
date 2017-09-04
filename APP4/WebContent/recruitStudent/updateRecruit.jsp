<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String recruit_id=request.getParameter("recruit_id");
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
    <title>招生-新建招生</title>
    <link rel="stylesheet" href="hplus/css/bootstrap.min.css" />
    <link rel="stylesheet" href="bootstrap/css/font-awesome.min.css" />
    <link rel="stylesheet" type="text/css" href="recruitStudent/css/wangEditor-css/wangEditor.min.css"/>
    <link rel="stylesheet" type="text/css" href="recruitStudent/css/build.css">
    <style type="text/css">
        body{background: #f2f2f2;}
        li{list-style: none;}
        .form-horizontal{padding: 0 58px 0 0;margin-top: 36px;}
        .form-horizontal .form-group { margin: 20px 0 0 0;}
        .form-horizontal .control-label {text-align: left;font-size: 14px;color: #686B6D;}
        .checkbox+.checkbox, .radio+.radio {margin-top: 0;}
        .checkbox input[type=checkbox]{margin-top: 12px;}
        .checkbox label{font-weight: bold;}
        .col-sm-10,.col-sm-2{padding: 0;}
        .zjq-apply-page{background:#fff;width: 678px;margin: 100px auto 0;}
        .zjq-form-page{padding-left:38px;}
        .zjq-tab-sel{font-size: 14px;font-weight: bold;line-height: 44px;height: 44px;background: #f2f2f2;}
        .zjq-tab-sel span{display: inline-block;cursor:pointer;padding:0 38px;color:#A6B1C3;}
        .zjq-apply-page .zjq-apply-sure{background: #fff;color: #4A4A4A;}
        .zjq-inputSet{position: relative;}
        .zjq-icon{position: absolute;top: 8px;right:10px;}
        .zjq-top{width: 0;height: 0;border: 4px solid transparent;border-bottom: 4px solid  #686B6D;}
        .zjq-bot{width: 0;height: 0;border: 4px solid transparent;border-top: 4px solid #686B6D;margin-top: 2px;}
        .zjq-feeScope{cursor:pointer;font-weight: bold;display: block;margin-top:20px;border: 1px solid #00B493;border-radius: 2px;font-size: 13px;color: #04B594;width: 118px;height: 33px;line-height: 33px;text-align: center; }
        .zjq-nextBtn.btn{border-color: #1CBB9E;font-weight: bold;background: #1CBB9E;border-radius: 5px;color: #fff;height: 40px;line-height: 30px;font-size: 17px;margin: 22px 0;}
        .zjq-tablePage{display: none;}
        .zjq-detail-list{padding-top: 35px;}
        .zjq-detail-list li.checkbox{margin: 0 0 17px 0;}
        .zjq-detail-list .checkbox label{font-weight: normal;font-size: 14px;color: #4A4A4A;}
        .zjq-publish{text-align: center;}
        .zjq-apply-page .zjq-publishBtn.btn,.zjq-makeSurePage .zjq-publishBtn.btn{width:502px;font-weight: bold;background: #1CBB9E;border-radius: 5px;color: #fff;height: 40px;line-height: 30px;font-size: 17px;margin: 54px 0 44px 0;border-color: #1CBB9E;}
        .zjq-select-modal{background: rgba(91,91,91,.4);position: absolute; top:0px; left: 0px;right: 0;bottom: 0;display: none;min-height: 100%;}
        .zjq-makeSurePage{width: 766px;background: #fff;margin: 100px auto 0;}
        .zjq-sureApply{width:100%;background: #F8F8F8;font-size: 14px;color: #323232;height: 41px;line-height: 41px;padding-left: 22px;}
        .zjq-contentPage{padding: 61px 107px 45px 107px;}
        .zjq-scroll-content{min-height:450px;overflow-y:auto;max-height:600px;}
        .zjq-table-sure{padding: 0 107px;border-top: 1px solid #EEEFEF;}
        .zjq-makeSurePage .zjq-publish{border-top: 1px solid #EEEFEF;margin-top: 15px;}
        .zjq-table-sure h3{font-size: 18px;color: #4A4A4A;line-height: 90px;}
        .zjq-tableList{padding-left: 10px;margin-bottom: 35px;}
        .zjq-tableList li{margin-bottom: 17px;font-size: 14px;color: #4A4A4A;}
        .zjq-sureIcon{color:#2E8F7B;margin-right: 10px;}
    </style>
</head>
<body>
<div class="zjq-apply-page">
    <h3 class="zjq-tab-sel">
        <span class="zjq-apply-sure zjq-formBtn">发布招生简章</span>
        <span class="zjq-tableBtn">确认招生信息表格</span>
    </h3>
    <form class="form-horizontal zjq-form-page">
        <div class="form-group">
            <label for="input4" class="col-sm-2 control-label">输入标题：</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="input4">
            </div>
        </div>
        <div class="form-group">
            <label for="input3" class="col-sm-2 control-label">输入内容：</label>
            <div class="col-sm-10">
					   <textarea id="textarea1" rows="15"  style="width:100%">

					   </textarea>
            </div>
        </div>
        <div class="form-group zjq-inputSet">
            <div>
                <input type="text" class="form-control laydate-icon" placeholder="报名开始日期" id="start_date">
            </div>

        </div>
        <div class="form-group zjq-inputSet">
            <div>
                <input type="text" class="form-control laydate-icon" placeholder="报名截止日期" id="end_date">
            </div>

        </div>
        <div class="form-group">
            <input class="btn btn-default col-sm-12  zjq-nextBtn" type="button" value="下一步">
        </div>
    </form>
    <div class="zjq-tablePage">
        <ul class="zjq-detail-list">
            <li class="checkbox checkbox-success">
                <input id="checkbox1" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox1">姓名</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox2" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox2">性别</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox3" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox3">身份证号码</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox4" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox4">一寸照</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox5" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox5">就读初中学校</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox6" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox6">报考学校</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox7" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox7">是否色盲</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox8" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox8">个人特长</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox9" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox9">获奖情况</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox10" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox10">家长姓名</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox11" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox11">与孩子的关系</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox12" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox12">家长单位</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox13" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox13">家长手机号码</label>
            </li>
            <li class="checkbox checkbox-success">
                <input id="checkbox14" class="styled" type="checkbox" checked disabled="true">
                <label for="checkbox14">住宿选择</label>
            </li>
        </ul>
        <div class="zjq-publish">
            <input class="btn btn-default zjq-publishBtn" type="button" value="发布">
        </div>
    </div>
    <div class="zjq-select-modal">
        <div class="zjq-makeSurePage">
            <h4 class="zjq-sureApply">确认发布报名</h4>
            <div class="zjq-scroll-content">
                <div class="zjq-contentPage" id="checkContent">

                </div>
                <div class="zjq-table-sure">
                    <h3>招生信息表格</h3>
                    <ul class="zjq-tableList">
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 姓名</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 性别</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 身份证号码</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 一寸照</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 就读初中学校</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 报考学校</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 是否色盲</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 个人特长</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 获奖情况</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 家长姓名</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 与孩子的关系</li>
                        <li><span class="glyphicon glyphicon-ok zjq-sureIcon"></span> 家长单位</li>
                    </ul>
                </div>
            </div>
            <div class="zjq-publish">
                <input class="btn btn-default zjq-publishBtn" type="button" value="确认发布" id="update">
            </div>
        </div>
    </div>
</div>
<form hidden="true" class="btn-group" name="Form" id="Form" method="post" enctype="multipart/form-data">
    <input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
</form>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="recruitStudent/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="recruitStudent/js/wangEditor.js?d=${time}"></script>
<script type="text/javascript" src="hplus/js/bootstrap.min.js"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script type="text/javascript" src="recruitStudent/js/enrollmentList.js?d=${time}"></script>
<script type="text/javascript">
    //session失效，页面跳转
    pageJump();
    $(function(){
        var start = {
            elem: '#start_date', //选择ID为START的input
            format: 'YYYY-MM-DD', //自动生成的时间格式
            min: laydate.now(), //设定最小日期为当前日期
            istime: true, //必须填入时间
            start: laydate.now(0,"YYYY-MM-DD"),  //设置开始时间为当前时间
            choose: function(datas){
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
            }
        };
        var end = {
            elem: '#end_date',
            format: 'YYYY-MM-DD',
            min: laydate.now(),
            istime: true,
            start: laydate.now(0,"YYYY-MM-DD"),
            choose: function(datas){
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(start);
        laydate(end);

        $(".zjq-form-page .zjq-nextBtn").click(function(){
            var title=$('#input4').val();
            var content=$('.wangEditor-txt').html();
            var apply_start_date=$('#start_date');
            var apply_end_date=$('#end_date');
            if (title=='' || content=='' || apply_start_date=='' || apply_end_date=='') {
                layer.msg('请将内容填写完整！', {icon: 0});
                return false;
            }

            $(".zjq-tab-sel .zjq-tableBtn").addClass("zjq-apply-sure").siblings().removeClass("zjq-apply-sure")
            $(".zjq-form-page").hide();
            $(".zjq-tablePage").show();
            $(".zjq-tab-sel .zjq-formBtn").click(function(){
                $(this).addClass("zjq-apply-sure").siblings().removeClass("zjq-apply-sure");
                $(".zjq-tablePage").hide();
                $(".zjq-form-page").show();
                $(".zjq-tab-sel .zjq-tableBtn").click(function(){
                    $(this).addClass("zjq-apply-sure").siblings().removeClass("zjq-apply-sure");
                    $(".zjq-form-page").hide();
                    $(".zjq-tablePage").show();
                });
            });

            $(".zjq-tablePage .zjq-publishBtn").click(function(){
                $('#checkContent').html($('.wangEditor-txt').html());
                $(".zjq-select-modal").show();
                $(".zjq-select-modal .zjq-publishBtn").click(function(){
                    window.location.href="recruitStudent/enrollmentList.jsp";
                });
            });
        });
    });
    var recruit_id=<%=recruit_id%>;
    updateDetail();
    saveRecruit();
</script>
</body>
</html>
