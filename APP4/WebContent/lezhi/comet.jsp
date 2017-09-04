<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>错误日志实时更新</title>
    <link rel="stylesheet" type="text/css" href="hplus/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="hplus/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="lezhi/css/style.css">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="mainwrap">
    <div class="ftab">
        <div class="ftab-cont">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>方法</th>
                    <th>参数</th>
                    <th>错误信息</th>
                    <th>IP地址</th>
                    <th>生成时间</th>
                </tr>
                </thead>
                <tbody id="msg">
                </tbody>
                <tfoot>
                <tr><td colspan="5">
                    <input type="text" id="msgid">
                    <input type="button" id="go" value="查询">
                    <input type="button" id="exporttongji" value="导出">
                </td>
                </tr>
                </tfoot>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="lezhi/js/comet.js"></script>
<script type="text/javascript">
    getId();
    exporttongji();
</script>
</body>
</html>
