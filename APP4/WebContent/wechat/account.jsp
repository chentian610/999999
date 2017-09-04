<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	response.setHeader("Access-Control-Allow-Origin", "*");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
	String dict_group = "";
%>
<!DOCTYPE html>
<head>
	<base href="<%=basePath%>">  
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>学校管理后台</title>
    <link href="hplus/css/bootstrap.css" rel="stylesheet">
 	<link href="wechat/css/style.min.css" rel="stylesheet">
 	<link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
 	<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.1/summernote.css" rel="stylesheet">
 	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="ibox-content">
    <form class="form-horizontal">
		<input type="hidden" id="account_id" name="account_id">
        <div class="form-group">
            <label class="col-sm-3 control-label">公众号名称</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入公众账号名称" class="form-control" id="account_name" name="account_name">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">公众号TOKEN</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入公众账号TOKEN" class="form-control" id="account_token" name="account_token">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">原始ID</label>
            <div class="col-sm-8">
                <input type="text" placeholder="请输入公众账号原始ID" class="form-control" id="weixin_accountid" name="weixin_accountid">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">公众号类型</label>
            <div class="col-sm-8">
                <select class="form-control m-b" name="account_type" id="account_type" name="account_type">
                   <option value="1">服务号 </option> 
                   <option value="2">订阅号 </option></select>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">电子邮箱</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="account_email" name="account_email">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">公众号描述</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="account_desc" name="account_desc">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">公众号APPID</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="account_appid" name="account_appid">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">公众号APPSECRET</label>
            <div class="col-sm-8">
                <input type="text" class="form-control" id="account_appsecret" name="account_appsecret">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">有效状态</label>
            <div class="col-sm-8">
                <select class="form-control m-b" name="is_effective" id="is_effective">
                   <option value="1">有效 </option> 
                   <option value="0">无效</option></select>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-3 control-label">是否认证</label>
            <div class="col-sm-8">
                <select class="form-control m-b" name="auth_status" id="auth_status">
                   <option value="1">认证 </option> 
                   <option value="0">未认证</option></select>
                </select>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-8">
                <button type="button" class="btn btn-primary" style="padding: 5px 20px;" id="saveButton">保存</button>
            </div>
        </div>
    </form>
</div>
<script src="wechat/js/jquery-2.1.4.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script src="js/alerts.js"></script>
<script src="js/myajax.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="wechat/js/account.js"></script>
<script>
//session失效，页面跳转
 pageJump();	
 //加载栏目名称列表
 $(document).ready(function() {
	 bindSaveButton();
	 initData();
 });
</script>
</body>
</html>
