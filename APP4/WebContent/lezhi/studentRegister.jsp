<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>

<!DOCTYPE>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>Lezhi</title>
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
    <link href="lezhi/css/style.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="hplus/js/jquery.min.js"></script>
    <script type="text/javascript" src="js/myajax.js"></script>
    <script src="hplus/js/plugins/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
    <script type="text/javascript" src="js/jquery.form.js"></script>
    <script src="lezhi/layer/layer.js"></script>
    <script type="text/javascript" src="js/functionUtil.js"></script>
    <script type="text/javascript" src="lezhi/js/studentRegister.js"></script>
    <style type="text/css" media="print">
        .invite-c{width: 100%;}
        .sel,.btns{display: none}
    </style>
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <script src="hplus/js/plugins/toastr/toastr.min.js"></script>
</head>
<body>
<div class="mainwrap">
    <div class="ftab">
        <div class="ftab-cont" style="width: 650px;padding-left: 20px;margin-left: -1px;overflow: visible;margin: 0 auto;">
                <div class="col-sm-4 ftab-search" style="margin-top: 20px;width: 635px;margin-left: -15px;">
                    <select class="form-control" style="height: 40px;" id="year">

                    </select>
                </div>

            <table class="table table-bordered" style="margin-top: 100px;TABLE-LAYOUT: fixed;">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>身份证号码</th>
                    <th>就读初中</th>
                    <th>是否住校</th>
                </tr>
                </thead>

                <tbody id="studentList">

                </tbody>
            </table>

            <div class="ftab-bottom">
                <div class="ftab-btn">
                    <!-- <a href="#" class="btn btn-sm btn-req">选择</a> -->
                </div>

                <div class="pages">
                    <ul id="page_pagintor"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    //session失效，页面跳转
    pageJump();
    showRegisterYear();
</script>
</body>
</html>
