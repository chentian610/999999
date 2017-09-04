<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>">  
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>学校管理后台</title>
    <link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet">
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/style.min.css" rel="stylesheet">
    <link href="application/css/page.css" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="sch-head-info clear">
        <div class="l-sch-info" id="info-head">申请单－西湖小学－2016-1-1</div>
        <div class="btn-box clear">
            <button type="button" class="btn btn-primary btn-refuse" id="btn-refuse">拒绝</button>
            <button type="button" class="btn btn-primary" id="agree-apply">通过</button>
        </div>
    </div>
    <div class="sch-con-info">
        <h3>申请人</h3>
        <div class="sch-form-info clear">
            <input type="text" placeholder="" class="form-control input-phone" id="apply-people" disabled='disabled'>
            <label><input type="checkbox" checked name="">首次申请</label>
            <span id="createTime"></span>
        </div>
    </div>
    <div class="sch-con-info">
        <h3>学校信息</h3>
        <div class="sch-form-info clear">
            <input type="text" placeholder="西湖小学" class="form-control input-phone" id="school-name" disabled='disabled'>
            <!-- <select class="form-control m-b" name="school" style="width: 200px;margin-left: 10px;float: left">
                 <option>请选择小学</option> 
                <option>小学一</option> 
            </select> -->
            <div class="sch-logo">
                <img src="images/gzh_wx.jpg" id="schoolLogo">
                <input type="file" value="上传学校logo">
                <!-- <button type="button" class="btn btn-primary btn-upload">上传学校logo</button>
                <span>建议200x200分辨率</span> -->
            </div>
        </div>
    </div>
    <div class="sch-con-info">
        <h3>系统管理账号</h3>
        <div class="sch-form-info clear">
            <input type="text" placeholder="" class="form-control input-phone" id="admin-phone" disabled='disabled'>
            <!-- <button type="button" class="btn btn-primary btn-modify">修改</button> -->
        </div>
        <div class="sch-form-info clear">
            <!-- <button type="button" class="btn btn-primary">增加</button> -->
        </div>
    </div>
    <div class="sch-con-info">
        <h3>APP模板</h3>
        <div class="sch-form-info clear">
            <ul class="model-list clear" id="app-list">
                <li>
                    <div><img src="images/gzh_wx.jpg"></div>
                    <p><label><input type="checkbox" checked>默认</label></p>
                </li>
            </ul>
        </div>
    </div>
    <div class="sch-con-info">
        <h3>功能模块</h3>
        <div class="sch-form-info clear" >
            <ul class="gn-list clear" id="app-module-list">
                <li><img src="images/gn_notice.png" /><span>公告通知</span><i></i></li>
                <li><img src="images/gn_photo.png" /><span>学校相册</span><i></i></li>
                <li><img src="images/gn_homework.png" /><span>作业</span><i></i></li>
            </ul>
        </div>
    </div>
   <!-- <div class="sch-con-info">
        <h3>选择平台</h3>
        <div class="sch-form-info clear">
            <label><input type="checkbox" checked name="plat">IOS</label>
            <label><input type="checkbox"  name="plat">Android</label>
        </div>
    </div>-->
</div>
<div id="refuse-box" class="tc-box">
    <em class="close_btn" onclick="EV_closeAlert()">关闭</em>
    <div class="tc-tit">拒绝</div>
    <div class="tc-con">
        <textarea class="textarea-css" id="refuse-reason">您的申请不符合规范，请重新申请。</textarea>
    </div>
    <div class="tc-btn">
        <button type="button" class="btn btn-primary" onclick="EV_closeAlert()">取消</button>
        <button type="button" class="btn btn-primary" id="refuse-apply" value="发送">发送</button>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/tc_js.js"></script>
<script src="application/js/myajax.js"></script>
<script src="js/functionUtil.js"></script>
<script src="application/js/application-details.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script type="text/javascript">
    $(function(){
        $('.gn-list').on('click','li',function(){
            if($(this).find('i').is(':visible')){
                $(this).find('i').hide();
                $(this).removeClass('licur');
            }else{
                $(this).find('i').show();
                $(this).addClass('licur');
            }
        });
    });
    
    loadApplyedSchool();
</script>
</body>
</html>