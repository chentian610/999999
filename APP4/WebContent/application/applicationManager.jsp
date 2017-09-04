<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
    response.setHeader("Cache-Control","no-store");
    response.setHeader("Pragrma","no-cache");
    response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">  
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>学校管理后台</title>
    <link href="application/css/bootstrap.min.css?d=${time}" rel="stylesheet">
    <link href="application/css/applicationManager.css?d=${time}" rel="stylesheet"/>
    <link href="hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
    <link rel="stylesheet" href="application/css/reset.css?d=${time}" />
    <link rel="stylesheet" type="text/css" href="application/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="application/css/build.css"/>
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container">
    <div class="qtop">
        <div class="ftop-serach">
            <input type="search" class="form-control search_school_name"  placeholder="搜索" >
            <span class="glyphicon glyphicon-search"></span>
        </div>
        <button class="btn btn-default qbb-search" id="search_school_name">搜索</button>
        <div class="pull-right">
            <select class="qbb-select agent_list">
                <option value="">全部</option>
                <option>1</option>
            </select>
            <select class="qbb-select school_type_list">
                <option value="">全部</option>
                <option>1</option>
            </select>
        </div>
    </div>
    <div class="top-line"></div>
    <ul class="list-group qcontent school_list">
        <li class="list-group-item">
            <div class="media">
            </div>
        </li>
        <li class="list-group-item school_list">
            <div class="media">
            </div>
        </li>
    </ul>
    <div class= "pull-right">
        <ul id="page_pagintor"></ul>
    </div>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content animated bounceInRight">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span></button>
            </div>
            <div class="modal-body">
                <form action="" class="d_form" role="form" id="modal">
                    <div class="modal-header box">
                        </button>
                        <h1 class="modal-title font-content title">确认申请</h1>
                        <div class="modal-body ">
                            <small class="font-bold font-content">学校名称：<span id="sample_school_name" class="empty_date">小星星幼儿园</span></small>
                        </div>
                        <div class="modal-body ">
                            <small class="font-bold font-content">学校类型：<span id="sample_school_type" class="empty_date">幼儿园</span></small>
                        </div>
                        <div class="modal-body">
                            <small class="font-bold font-content">学校域名：<span id="sample_domain" class="empty_date">www.ls.com</span></small>
                        </div>
                        <small class="font-bold margin-left font-content" style=" margin-left: 15px;">学校log：</small>
                        <div class="modal-body logo" style=" margin-left: 30px;">
                            <small class="font-bold margin-left"><img id="school_log" src="application/images/school_logo1.png" alt="" /></small>
                        </div>
                    </div>
                    <small>
                        <h2 class="font-bold margin-left font-content" style=" margin-left: 30px;">功能模块&nbsp&nbsp&nbsp收费统计：¥<span id="set_module_price"></span>元／年</h2>
                        <div class="modal-body font-content" id="model" style=" margin-left: 50px;"></div>
                    </small>
                    <small>
                        <span class="font-bold margin-left font-content" style=" margin-left: 30px;" id="server_config">服务器配置：
                            <div id="server_tent" style=" margin-left: 40px; margin-top: 15px;">
                                <span>&nbsp&nbsp内存：<span id="memory"></span></span>
                                <span>&nbsp&nbsp磁盘：<span id="disk"></span></span>
                                <span>&nbsp&nbsp带宽：<span id="bandwidth"></span></span>
                                <i>￥<span id="server_price"></span>元</i>
                            </div>
                            <span id="prompt_text"></span>
                        </span>
                    </small>
                    <div style="width: 100%;margin-top: 15px;"><small class="font-bold font-content" style=" margin-left: 30px;">平台：</small></div>
                    <div class="modal-body margin-left checkbox checkbox-success plat" style=" margin-left: 30px;">
                        <input type="checkbox" name="radio5" id="radio5" value="option1" onclick="return false;" checked>
                        <label for="radio5"  style="margin: 0 24% 0 33px;">iOS</label>
                        <input type="checkbox" name="radio5" id="radio6" value="option1" onclick="return false;" checked>
                        <label for="radio6">Android</label>
                    </div>
                    <small class="font-bold margin-left font-content" style=" margin-left: 30px;">已添加的学校管理员帐号：</small>
                    <div class="modal-body margin-left number insert_phone">

                    </div>
                    <div class="modal-body" style="left: 20%;" id="btn_div">
                        <input class="btn btn-default btn-online r_btn update_status" status="007015" app_status="007015" is_modal="true" id="btn_refuse" type="reset" data-dismiss="modal" value="拒绝">
                        <input class="btn btn-default btn-enabled s_btn update_status" status="007010" app_status="007010" is_modal="true" type="submit" data-dismiss="modal" id="btn_submit" value="通过">
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<button type="button" id="btn_error" class="btn btn-primary" style="display: none;" data-toggle="modal" data-target="#myModal2">
    沿X轴转动
</button>
<div class="modal inmodal" id="myModal2" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content animated flipInY">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title subtitle">信息提示！</h4>
            </div>
            <div class="modal-body">
                <p id="error_content"> </p>
        </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="js/myajax.js"></script>
<script src="js/functionUtil.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="application/js/application-manager.js?d=${time}"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script>
    initSchoolTypeList();
    initAgentList();
    initSchoolList();
    bindSearchSchoolNameClickEvent();
    bindAgentSchoolListClickEvent();
    bindSchoolTypeListClickEvent();
</script>
</body>
</html>