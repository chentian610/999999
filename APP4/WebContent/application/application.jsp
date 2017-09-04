<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String dict_group = "";
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragrma","no-cache");
	response.setDateHeader("Expires",0);
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>" target="_blank">
<title>填写学校信息</title>
<meta content="text/html; charset=utf-8">
	<link href="application/css/bootstrap.min.css?d=${time}" rel="stylesheet">
	<link href="hplus/css/font-awesome.min.css?v=4.4.0" rel="stylesheet">
	<link href="hplus/css/animate.min.css" rel="stylesheet">
	<link href="hplus/css/style.min.css?v=4.0.0" rel="stylesheet">
	<link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
	<link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
	<link rel="stylesheet" href="application/css/agency_page.css?d=${time}" />
	<link rel="stylesheet" type="text/css" href="application/css/build.css">
</head>
<body>
<div class="container">
	<div class="zjq-agency-page" id="FirstStep">
		<div class="process_l">
			<ul id="stepUl">
				<li style="left:0;" class="active">
					<span>填写学校信息</span>
				</li>
				<li style="left:50%;">
					<span>配置APP</span>
				</li>
				<li style="left:100%;">
					<span>学校管理员账号</span>
				</li>
			</ul>
		</div>
		<div class="zjq-form-block">
			<form role="form" class="form-horizontal">
				<div class="form-group">
					<label class="col-sm-3 control-label">输入学校名称：</label>
					<div class="col-sm-8">
						<input type="text" name="" class="form-control" id="school_name" placeholder="请输入学校名称...">
					</div>
					<span class="zjq-star-icon">*</span>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">选择学校类型：</label>
					<div class="col-sm-8">
						<select class="form-control" name="" id="school_type">
							<option>请选择</option>
							<option>选项 2</option>
							<option>选项 3</option>
							<option>选项 4</option>
						</select>
					</div>
					<span class="zjq-star-icon">*</span>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">输入学校名称：</label>
					<div class="col-sm-8">
						<div class="zjq-plus-block" id="schoolLogo"><img src="application/icon/plus.png"/></div>
						<p class="zjq-form-text">
							<span class="zjq-school-logo">学校LOGO</span>
							<span>不能小于1024*1024分辨率正方形图片</span>
						</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label" style="transform: translateX(10px);padding-right: 0;">输入学校官网域名：</label>
					<div class="col-sm-6" style="width: 56%;">
						<input type="text" name="" class="form-control" id="domain"  onkeyup="value=value.replace(/[^a-zA-Z0-9]/g,'');document.getElementById('domainOld').innerText = value;"
							   onpaste="value=value.replace(/[^a-zA-Z0-9]/g,'');document.getElementById('domainOld').innerText = value;" placeholder="请输入学校官网域名..."
							   oncontextmenu = "value=value.replace(/[^a-zA-Z0-9]/g,'');document.getElementById('domainOld').innerText = value;"
                               onblur="value=value.replace(/[^a-zA-Z0-9]/g,'');document.getElementById('domainOld').innerText = value;">
						<span class="help-block m-b-none">请输入字母、数字，不要输入特殊符号（比如：-、/、~等）</span>
					</div>
					<span class="zjq-website-name">.classtao.cn</span>
					<span style="margin: 10px 0 0 10px;" class="zjq-star-icon">*</span>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label" style="transform: translateX(10px);padding-right: 0;">学校管理后台域名：</label>
					<div class="col-sm-8 zjq-website-sure">
						<span id="domainOld"></span>-m.classtao.cn
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">输入学校校训：</label>
					<div class="col-sm-8">
						<input type="text" name="" class="form-control" id="school_motto" placeholder="请输入学校校训...">
					</div>
					<span class="zjq-star-icon">*</span>
				</div>
			</form>
		</div>
	</div>
	<div class="zjq-agency-page zjq-second-process" id="SecondSteps" hidden="hidden">
		<div class="process_l">
			<ul>
				<li style="left:0;" class="zjq-done-block">
					<span>填写学校信息</span>
				</li>
				<li style="left:50%;" class="active">
					<span>配置APP</span>
				</li>
				<li style="left:100%;">
					<span>学校管理员账号</span>
				</li>
			</ul>
		</div>
		<div class="zjq-self-block">
			<div class="zjq-template-sel">
				<h3>请选择APP模板</h3>
				<div class="zjq-template-list">
					<span class="zjq-arrow-left"><img src="application/icon/arrow_left.png"/></span>
					<div class="zjq-pic-list">
						<div class="zjq-mobile-model" id="thisModule"></div>
						<ul class="clearfix" id="zjq-picList">
						</ul>
					</div>
					<span class="zjq-arrow-right"><img src="application/icon/arrow_right.png"/></span>
				</div>
			</div>
			<div class="zjq-template-sel" id="moduleList">
			</div>
			<div class="zjq-template-sel">
				<h3>服务器配置</h3>
				<div class="zjq-table-style" id="ConfigList">
				</div>
			</div>
			<div class="zjq-template-sel">
				<h3>平台选择</h3>
				<div class="zjq-function-module">
					<ul class="zjq-must-sel zjq-function-block zjq-platform clearfix">
						<li>
							<div class="zjq-mess-block">
								<img src="application/icon/ios.png"/>
								<i class="fa fa-check"></i>
							</div>
							<p>ios</p>
						</li>
						<li>
							<div class="zjq-mess-block">
								<img src="application/icon/Android.png"/>
								<i class="fa fa-check"></i>
							</div>
							<p>Android</p>
						</li>
					</ul>
				</div>
			</div>
			<div class="zjq-total-fees">费用总计：￥<span id="totalPrice"></span>/年</div>
		</div>
	</div>
    <div class="zjq-agency-page zjq-third-process" id="ThirdSteps" hidden="hidden">
        <div class="process_l">
            <ul>
                <li style="left:0;" class="zjq-done-block">
                    <span>填写学校信息</span>
                </li>
                <li style="left:50%;" class="zjq-done-block">
                    <span>配置APP</span>
                </li>
                <li style="left:100%;" class="active">
                    <span>学校管理员账号</span>
                </li>
            </ul>
        </div>
        <div class='zjq-manager-block'>
            <table class="table zjq-message-table">
                <tbody id="adminList"></tbody>
            </table>
            <div class="zjq-add-block" id="addSchoolAdmin" data-toggle="modal" data-target="#myModal4">
                <img src="application/icon/zengjia.png"/>
                <span>添加学校管理员账号</span>
            </div>
        </div>
    </div>
    <div class="zjq-success-page" id="FourthSteps" hidden="hidden">
        <h3>申请成功！</h3>
        <img src="application/icon/success.png">
        <h5>我们将在2个工作日内完成审核</h5>
        <p>审核结果将以短信的形式发送到您和学校管理员手机<span id="userPhoneList"></span> <br> 请注意查收</p>
        <div class="zjq-buttons">
            <button type="button" class="btn zjq-green-btn" id="btnDetermine">确定</button>
        </div>
    </div>
    <div class="zjq-buttons" id="BtnStep">
		<button type="button" class="btn" style="display: none;" id="PreviousStep" data-index="2">上一步</button>
		<button type="button" class="btn zjq-green-btn" id="NextStep" data-index="1">下一步</button>
	</div>
</div>
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal6" id="btn_error" style="display: none;">
</button>
<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal5" id="btnMyModal5" style="display: none;">
</button>
<div class="modal inmodal fade in" id="myModal6" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;background: rgba(0,0,0,.3);">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title" id="subtitle">学校类型错误</h4>
			</div>
			<div class="modal-body">
				<p id="error_content">请选择学校类型</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-white" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<div class="modal inmodal fade in" id="myModal4" tabindex="-1" role="dialog"  aria-hidden="true" style="display: none;background: rgba(0,0,0,.3);">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="zjq-form-check">
                <div class="form-group">
                    <label>请输入在校管理员昵称：</label>
                    <input type="text" placeholder="请输入昵称" class="form-control" id="adminName">
                </div>
                <div class="form-group">
                    <label>请输入在校管理员手机号：</label>
                    <input type="text" placeholder="请输入11位在校管理员手机号" class="form-control" id="adminPhone">
                </div>
                <div class="form-group">
                    <input type="text" placeholder="请再次输入" class="form-control" id="adminPhoneOld">
                </div>
                <div class="zjq-modal-buttons">
                    <button type="button" class="btn btn-white" data-dismiss="modal">取消</button>
                    <button type="button" class="btn zjq-green-btn" id="Preservation">添加</button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal inmodal fade in" id="myModal5" tabindex="-1" role="dialog"  aria-hidden="true" style="display:none;background: rgba(0,0,0,.3);">
    <div class="modal-dialog">
        <div class="modal-content zjq-modal-content">
            <div class="zjq-sure-page">
                <h2>确认申请</h2>
                <div class="zjq-message-sure">
                    <div class="zjq-scroll-message">
                        <div class="zjq-message-block">
                            <h3>填写学校信息</h3>
                            <div class="zjq-mess-detail" id="schoolName"></div>
                            <div class="zjq-mess-detail" id="schoolType"></div>
                            <div class="zjq-mess-detail" >学校logo：<img id="schoolLog" class="logo_icon" src="" alt="" /></div>
                            <div class="zjq-mess-detail" id="schoolMotto"></div>
                            <div class="zjq-mess-detail" id="schoolDomain"></div>
                        </div>
                        <div class="zjq-message-block">
                            <h3>配置APP</h3>
                            <div class="zjq-mess-detail">
                                选择APP模板:
                                <span class="zjq-mobile-block"><img src="" id="schoolModuleImg"/></span>
                            </div>
                            <div class="zjq-mess-detail">选择模块:
                                <span class="zjq-fee-block" id="schoolModulePrice"></span>
                                <ul class="zjq-function-block clearfix" id="schoolModuleList">
                                </ul>
                            </div>
                            <div class="zjq-mess-detail">
                                服务器配置：
                                <table class="table zjq-table">
                                    <tbody>
                                    <tr class="zjq-service-sel">
                                        <td>内存</td>
                                        <td>硬盘</td>
                                        <td>带宽</td>
                                        <td>元/年</td>
                                    </tr>
                                    <tr class="zjq-service-sel" id="schoolConfig">
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="zjq-mess-detail">
                                平台选择：
                                <ul class="zjq-function-block clearfix">
                                    <li>
                                        <div class="zjq-mess-block">
                                            <img src="application/icon/ios.png"/>
                                            <span>苹果</span>
                                        </div>
                                        <p>ios</p>
                                    </li>
                                    <li>
                                        <div class="zjq-mess-block">
                                            <img src="application/icon/Android.png"/>
                                            <span>安卓</span>
                                        </div>
                                        <p>Android</p>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="zjq-message-block">
                            <h3>学校管理员账号</h3>
                            <div class="zjq-mess-detail">
                                <table class="table zjq-message-table">
                                    <tbody id="schoolAdmin">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="zjq-total-fees" id="schoolTotalPrice"></div>
                    </div>
                </div>
                <div class="zjq-buttons">
                    <button type="button" class="btn" data-dismiss="modal">返回编辑</button>
                    <button type="button" class="btn zjq-green-btn" id="Determine" data-dismiss="modal">确定</button>
                </div>
            </div>
        </div>
    </div>
</div>
	<form enctype="multipart/form-data" method="post" name="fileinfo" id="form-file" hidden=hidden>
		<input type="File" value="上传学校logo" name="file" id="uploadLogo" accept="image/gif, image/jpeg, image/png, image/jpg"/>
	</form>
	<form hidden="true" class="btn-group" name="Form" id="Form" method="post" enctype="multipart/form-data">
		<input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>
	</form>
<div class="footer" style="text-align: center;">&copy;1997-2016乐智公司版权所有 About LeZhiApp | 公司简介
	| 联系我们 | 客服 | 代理商</div>
	<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
	<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
	<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script src="application/js/myajax.js"></script>
	<script src="js/functionUtil.js"></script>
	<script src="js/mathUtil.js"></script>
	<script src="hplus/js/content.min.js?v=1.0.0"></script>
	<script src="application/js/application.js?d=${time}"></script>
	<script type="text/javascript">
		initContent();
	</script>
</body>
</html>