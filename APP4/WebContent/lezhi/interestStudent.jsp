<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String contact_name=request.getParameter("contact_name");
    String contact_id=request.getParameter("contact_id");
    String grade_id=request.getParameter("grade_id");
    //设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta charset="UTF-8">
    <title>学生管理</title>
    <link rel="stylesheet" type="text/css" href="lezhi/css/bootstrap.min1.css" />
    <link href="bootstrap/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="lezhi/css/build.css">
    <link rel="stylesheet" href="lezhi/css/style1.css" />
    <link href="hplus/css/style.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="lezhi/css/interestStudent.css" />
</head>
<body>
<div class="container">
    <div class="zjq-classes clearfix">
        <div class="zjq-student-list fl">
            <h2><%=contact_name%></h2>
            <div  id="content" style="height:391px;width:400px;overflow:auto"><!-- 滚动条 -->
            <table class="table zjq-table">
                <tbody id="studentList">
                </tbody>
            </table>
                </div>
            <div class="buttons">
                <input class="btn btn-default s_btn" type="button" value="全选" id="studentSelect">
                <input class="btn btn-default s_btn" type="submit" value="删除" id="delete">
            </div>
        </div>
        <div class="zjq-student-search fl">
            <form class="form-inline zjq-form-header">
                <div class="form-group">
                    <select class="zjq-sel" id="classname">
                    </select>
                </div>
                <div class="form-group zjq-search">
                    <span class="glyphicon glyphicon-search zjq-search-logo" id="search"></span>
                    <input type="search" class="form-control"  placeholder="搜索" id="studentname">
                </div>
            </form>
            <table class="table zjq-table">
                <tbody id="studentList1">

                </tbody>
            </table>
            <nav aria-label="Page navigation">
                <ul id="page_pagintor1" style="padding-left:60px;"></ul>
            </nav>
            <div class="buttons">
                <input class="btn btn-default" type="button" value="全选" id="allstudentSelect">
                <input class="btn btn-default add_btn" type="submit" value="添加到左侧" id="addLeft">
            </div>
        </div>
    </div>
   <%--<div class="zjq-buttons">--%>
         <%--<input class="btn btn-default" type="button" value="放弃" id="abandon">--%>
         <%--<input class="btn btn-default save_btn" type="submit" value="保存" id="save">--%>
    <%--</div>--%>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="lezhi/layer/layer.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/interestStudent.js?d=${time}"></script>
<script type="text/javascript">
    contact_id=<%=contact_id%>;
    grade_id=<%=grade_id%>;
    //session失效，页面跳转
    pageJump();
    groupPerson();
    showClass();
    search();
    allSelect();
    allDelete();
    addLeft();
</script>
</body>
</html>
