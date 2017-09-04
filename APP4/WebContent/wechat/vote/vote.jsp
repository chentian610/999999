<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
	String dict_group = "";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">     
	<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
    <!-- Data Tables -->
    <link href="hplus/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="article/css/style.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/sweetalert/sweetalert.css" rel="stylesheet">
    <link href="article/css/page.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
   	<link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" type="text/css" href="hplus/css/editor.css">
    <script src="hplus/js/jquery.min.js?v=2.1.4"></script>
	<script src="js/myajax.js"></script>
	<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
	<script src="hplus/js/plugins/layer/laydate/laydate.js"></script>
	<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
	<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
	<script src="js/functionUtil.js"></script>
	<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
	<script src="hplus/js/bootstrap-wysiwyg.js"></script>
	<script type="text/javascript" src="js/jquery.form.js"></script>
	<script src="wechat/js/vote.js"></script>
</head>

<body>
<div class="wrapper wrapper-content">
    <div class="ss-btn-box clear">
        <div class="ss-box input-group" style="width:250px;">
            <span class="input-group-btn"> 
            	<button type="button" class="btn btn-success btn-sm" data-action="add-vote" data-toggle="modal" data-target="#vote-edit-modal">
            		<span class="glyphicon glyphicon-plus-sign"></span> 录入投票主题
            	</button> 
            </span>
        </div>                            
    </div>
    
    <div class="ftab">
		<div class="ftab-cont">
			<table class="table table-bordered" id="menu-table">
				<thead>
					<tr>
						<th style="width:19%">投票主题</th>
						<th style="width:16%">主题描述</th>
						<th style="width:15%">开始时间</th>
						<th style="width:15%">结束时间</th>
						<th style="width:10%">发布状态</th>
						<th style="width:25%">操作</th>
					</tr>
				</thead>
				<tbody id="votelist">
				</tbody>
			</table>
		</div>
	</div>
    
    <!-- 主题编辑弹框 -->  
    <div class="modal inmodal fade" id="vote-edit-modal" tabindex="-1" role="dialog"  aria-hidden="true">
		<jsp:include page="votesubject-edit.jsp"/>
	</div>
	
	<!-- 问题编辑弹框 -->  
    <div class="modal inmodal fade" id="question-edit-modal" tabindex="-1" role="dialog"  aria-hidden="true">
		<jsp:include page="votequestion-edit.jsp"/>
	</div>
	
	<!-- 投票结果展示弹框 -->
	<div class="modal inmodal fade" id="result-show-modal" tabindex="-1" role="dialog"  aria-hidden="true" style="width:80%;margin-left:10%;height:90%;margin-top:2%;margin-bottom:2%;">
		<div class="ibox-content" >
			<div class="form-group">
	             <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>
	             </button>
	       	</div>
	    </div>
	    <iframe class="J_iframe" data-name="result-iframe" width="100%" height="100%" src="" frameborder="0"></iframe>
	</div>
	
    <div class= "pull-right">
		<ul id="page_pagintor"></ul>
	</div>
</div>
<script type="text/javascript">
	//session失效，页面跳转
	pageJump();
	$(function(){
		loadResultUrl();
		loadVotes();
		bindClickEvent();
	});
</script>
</body>
</html>