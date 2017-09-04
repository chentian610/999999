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
 	<link href="wechat/css/wechat.css" rel="stylesheet">
 	<link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
 	<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.1/summernote.css" rel="stylesheet">
 	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
		.itembox{width:200px;height:200px;border: 1px solid #d3d3d3;cursor:pointer}
    	.itembox:hover{border: 1px solid #A1A1A1}    	
    	.itemimg{height:170px;text-align:center} .itemimg img{width:120px;margin-top:10px}
    	.itemtitle{height:170px;text-align:center}
    </style>
</head>
<body>
<div class="ibox-content">
    <form class="form-horizontal">
    	<input type="hidden" name="msgtype" value="text">
        <div class="form-group">
            <label class="col-sm-2 control-label">群发内容</label>
            <div class="col-sm-7">
            	<div class="c_cont">
            		<div class="c_tree">
						<ul>
							<li>
								<a href="javascript:void(0);" title="文本消息" data-action="text">
									<i style="background-position: 0px -30px;"></i>
								</a>
							</li>
							<li>
								<a href="javascript:void(0);" title="图文消息" data-action="newstemplate" data-toggle="modal" data-target="#newstemplate-modal">
									<i style="background-position: 0px -240px;"></i>
								</a>
							</li>
						</ul>
					</div>
            	</div>
            	<div class="c_bj" id="contentEditor">
            		<textarea class="wz" placeholder="请输入文本内容" name="textcontent"></textarea>
            	</div>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-8">
                <button type="button" class="btn btn-primary" style="padding: 5px 20px;" id="sendButton">群发</button>
            </div>
        </div>
        
        <!-- 图文消息选择弹框 -->  
	    <div class="modal inmodal fade in" id="newstemplate-modal" tabindex="-1" role="dialog"  aria-hidden="true">
	    	<div class="modal-dialog" style="width: 850px;">
				<div class="ibox-content" >
					<div class="form-group">
	                 <button type="button" class="close" data-dismiss="modal" data-action="close-newstemplate-editor"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
	                 </button>
	            	</div>
					<div class="form-horizontal" style="height:600px;overflow-x: hidden; overflow-y: auto;">
						<table id="newstemplateTable">
						</table>
				   </div>
				</div>
			</div>
		</div>
    </form>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="hplus/js/bootstrap-wysiwyg.js"></script>
<script src="hplus/js/jquery.hotkeys.js"></script>
<script src="hplus/js/prettify.js"></script>
<script type="text/javascript" src="js/jquery.form.js"></script>
<script src="wechat/js/groupMessage.js"></script>
<script>
//session失效，页面跳转
 pageJump();	
 $(document).ready(function() {
	 bindAllClickButton();
 });
</script>
</body>
</html>
