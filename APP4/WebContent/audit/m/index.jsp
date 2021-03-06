<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html lang="en" wp-site wp-site-is-master-page class="">
    <head>
        <meta charset="utf-8">
        <base href="<%=basePath%>">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">
        <title>乐智教育系统  系统管理</title>
        <!-- Bootstrap core CSS -->
        <link href="audit/m/bootstrap/css/bootstrap.css" rel="stylesheet">
        <!-- Custom styles for this template -->
        <link href="audit/m/dashboard.css" rel="stylesheet">
        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    </head>
    <body class="">
  		<nav class="navbar navbar-inverse navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="javascript:void(0)" style="background: url(audit/detail/img/lezhi_logo.png) no-repeat 16px ;">乐智教育系统&nbsp; &nbsp;系统管理</a>
                </div>
            </div>
        </nav>
        <div class="container-fluid">
            <div class="row">
                <div class="col-sm-3 col-md-2 sidebar">
                    <ul class="nav nav-sidebar">
                        <li class="active">
                            <a href="audit/m/index.html" target="_self">申请管理</a>
                        </li>
                        <li>
                            <a href="app/uploadApp.jsp" target="_blank">学校APP上传发布</a>
                        </li>
                        <li>
                            <a href="javascript:void(0)">学校管理</a>
                        </li>
                        <li>
                            <a href="javascript:void(0)">用户管理</a>
                        </li>
                        <li>
                            <a href="javascript:void(0)">渠道管理</a>
                        </li>
                    </ul>
                    <ul class="nav nav-sidebar">
                        <li>
                            <a href="javascript:void(0)">基础数据维护</a>
                        </li>
                        <li>
                            <a href="javascript:void(0)">日志分析</a>
                        </li>
                        <li>
                            <a href="javascript:void(0)">数据统计</a>
                        </li>
                    </ul>
                </div>
                <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
                    <div id="navbar" class="navbar-collapse collapse">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="">
                                <a href="javascript:void(0)" class="" id="phone">
                                    <img src="audit/detail/img/arrow_gray_down.png" />
                                </a>
                            </li>
                            <li>
                                <a href="javascript:void(0)">用户信息</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)">系统设置</a>
                            </li>
                            <li>
                                <a href="javascript:void(0)">帮助</a>
                            </li>
                        </ul>
                        <form class="navbar-form navbar-right">
                            <input type="text" class="form-control" placeholder="Search...">
                        </form>
                    </div>
                    <h1 class="page-header">学校申请</h1>
                    <div class="row placeholders" id="school-apply-list">
                    </div>
				     <div class= "pull-right">
						<ul id="page_pagintor"></ul>
					</div>
                </div>
            </div>
        </div>
        <!-- Bootstrap core JavaScript
    ================================================== -->
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="audit/m/assets/js/jquery.min.js"></script>
        <script src="audit/m/bootstrap/js/bootstrap.min.js"></script>
        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <script src="audit/m/assets/js/ie10-viewport-bug-workaround.js"></script>
        <script src="js/myajax.js"></script>
		<script src="js/functionUtil.js"></script>
		<script src="audit/m/js/schoolAudit.js"></script>
		<script src="hplus/js/plugins/sweetalert/sweetalert.min.js"></script>
		<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
		<script>
			initPaginator();
			$("#phone").html('<img src="audit/detail/img/arrow_gray_down.png" />'+localStorage.getItem('phone'));
		</script>
    </body>
</html>
