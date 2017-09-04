<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String enroll_count=request.getParameter("enroll_count");
    String recruit_id=request.getParameter("recruit_id");
    String ratio=request.getParameter("ratio");
    String title=request.getParameter("title");
//设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>招生-列表-生成正式录取名单</title>
    <link rel="stylesheet" href="hplus/css/bootstrap.min.css" />
    <style type="text/css">
        body{background: #f2f2f2;}
        *{margin: 0;padding: 0;}
        a:hover{text-decoration: none;}
        .zjq-apply-page{width: 678px;margin: 100px auto 0;background: #fff;}
        .fl{float: left;}
        .clearfix:after{content: "";display: block;height: 0;clear: both;*zoom: 1;}
        .zjq-createPage{position: relative;}
        .zjq-createPage h2{font-size: 25px;color: #4A4A4A;line-height: 45px;text-align: center;padding-top: 45px;}
        .zjq-applyDetail{padding: 25px 29px 32px 32px;}
        .zjq-applyDetail dd{height: 46px;line-height: 46px;margin: 14px 0;position: relative;}
        .zjq-applyDetail dd span{font-size: 18px;color: #4A4A4A;}
        .zjq-applyDetail dd img{width: 46px;height: 46px;display: inline-block;border-radius: 50%;margin-right: 35px;}
        .zjq-detailBtns{position: absolute;right: 20px;top: 0;}
        .zjq-applyDetail dd .zjq-greenBtn{font-size: 12px;color: #04B594;margin-right:5px;display: inline-block;width: 97px;height: 25px;line-height: 25px;border: 1px solid #00B493;border-radius: 2px;font-size: 12px;color: #04B594;text-align: center;cursor: pointer;}
        .zjq-applyDetail dd .zjq-admittedBtn{border-color: #9B9B9B;color: #9B9B9B;}
        .zjq-detailItem-title{padding: 31px 0 24px 51px;border-bottom: 1px solid #EEEFEF;}
        .zjq-circleIcon{position:relative;width: 96px;height: 96px;border-radius: 100%;background: #D8D8D8;background-image: linear-gradient(to right, transparent 50%, #7ED321 0);overflow：hidden;}
        .zjq-circleIcon:before{content: "";background: #D8D8D8;margin-left:50%;height:100%;display: block;border-radius:0 100% 100% 0/50%;transform-origin:0 50%;transform:rotate(126deg);}
        .zjq-circleIcon p{margin:0;width: 65px;height: 65px;background: #fff;border-radius: 100%;position: absolute;left: 15px;top: 15px;z-index: 112;text-align: center;font-family: PingFangSC-Regular;font-size: 12px;color: #4A4A4A;padding-top: 15px;}
        .zjq-circleIcon p span{font-size: 18px;}
        .zjq-apply-stute{margin:13px 0 0 46px;}
        .zjq-selList{text-align: center;padding: 10px 0;}
        .zjq-numberList{margin: 0;}
        .zjq-numberList li a{color:#4A4A4A;}
        .zjq-createBtn{text-align: center;}
        .zjq-createBtn .btn{width:502px;font-weight: bold;background: #1CBB9E;border-radius: 5px;color: #fff;height: 40px;line-height: 30px;font-size: 17px;margin: 24px 0 24px 0;border-color: #1CBB9E;}
        .zjq-detail-style{font-size: 16px;color: #4A4A4A;height: 22px;line-height: 22px;margin: 0 25px 0 0;}
        .zjq-detail-style .zjq-scaleStick{margin-left:10px;width: 60px;height: 7px;background: #F8327C;border-radius: 100px;display: inline-block;position: relative;vertical-align: middle;}
        .zjq-detail-style .zjq-scaleStick:before{content: "";height: 7px;width: 24px;background: #00BCD4;border-radius: 100px;position: absolute;left: 0;}
        .zjq-select-modal{background: rgba(91,91,91,.4);position: absolute; top:0px; left: 0px;right: 0;bottom: 0;min-height: 120%;display: none;z-index: 1133;}
        .zjq-makeSurePage{width: 766px;background: #fff;margin: 100px auto 0;}
        .zjq-sureApply{background: #F8F8F8;font-size: 14px;color: #323232;height: 41px;line-height: 41px;padding-left: 22px;}
        .zjq-contentPage{padding: 61px 107px 45px 107px;}
        .zjq-table-sure{padding: 25px 0 0 56px;}
        .zjq-makeSurePage .zjq-publish{margin-top: 15px;}
        .zjq-table-sure dt{font-size: 26px;color: #686B6D;line-height:70px;height:70px;}
        .zjq-table-sure dt span.zjq-left-detail,.zjq-table-sure dd span.zjq-left-detail{text-align: right;width: 86px;display: inline-block;}
        .zjq-table-sure dt span.zjq-right-detail,.zjq-table-sure dd span.zjq-right-detail{margin-left: 24px;text-align: left;}
        .zjq-table-sure dd{font-size: 13px;color: #686B6D;line-height:18px;margin-bottom: 40px;}
        .zjq-child-pic{width: 80px;height: 108px;vertical-align: top;display: inline-block;margin-top: 25px;margin-left: -100px;}
        .zjq-publishBtn{text-align: center;border-top: 1px solid #EEEFEF;}
        .zjq-publishBtn .btn{width:502px;font-weight: bold;background: #1CBB9E;border-radius: 5px;color: #fff;height: 40px;line-height: 30px;font-size: 17px;margin: 24px 0 24px 0;border-color: #1CBB9E;}
    </style>
</head>
<body>
<div class="zjq-apply-page">
    <div class="zjq-createPage">
        <h2>待确认正式录取名单</h2>
        <div class="zjq-detailItem-title clearfix">
            <p class="zjq-detail-style fl" id="enrollCount">录取人数：<%=enroll_count%></p>
            <p class="zjq-detail-style fl" id="ratio">男女比例：<%=ratio%><span class="zjq-scaleStick"></span></p>
        </div>
        <dl class="zjq-applyDetail" id="studentList">

        </dl>
        <div class="zjq-selList">
            <nav aria-label="Page navigation">
                <ul class="pagination zjq-numberList" id="page_pagintor">

                </ul>
            </nav>
        </div>
        <div class="zjq-createBtn">
            <a type="button" class="btn btn-default">确认</a>
        </div>
    </div>
    <div class="zjq-select-modal clearfix">
        <div class="zjq-makeSurePage">
            <h4 class="zjq-sureApply">新生报名详情页</h4>
            <dl class="zjq-table-sure">
                <dt><span class="zjq-left-detail">姓名</span><span class="zjq-right-detail" id="student_name"></span></dt>
                <dd><span class="zjq-left-detail">性别</span><span class="zjq-right-detail" id="sex"></span></dd>
                <dd>
                    <span class="zjq-left-detail">身份证号码</span>
                    <span class="zjq-right-detail" id="id_number"></span>
                </dd>
                <dd><span class="zjq-left-detail">就读初中</span><span class="zjq-right-detail" id="middle"></span></dd>
                <dd><span class="zjq-left-detail">报考学校</span><span class="zjq-right-detail" id="register"></span></dd>
                <dd><span class="zjq-left-detail">是否色盲</span><span class="zjq-right-detail" id="color"></span></dd>
                <dd><span class="zjq-left-detail">个人特长</span><span class="zjq-right-detail" id="specialty"></span></dd>
                <dd><span class="zjq-left-detail">获奖情况</span><span class="zjq-right-detail" id="award"></span></dd>
                <dd><span class="zjq-left-detail">家长姓名</span><span class="zjq-right-detail" id="parent"></span></dd>
                <dd><span class="zjq-left-detail">与孩子关系</span><span class="zjq-right-detail" id="relation"></span></dd>
                <dd><span class="zjq-left-detail">家长单位</span><span class="zjq-right-detail" id="company"></span></dd>
                <dd><span class="zjq-left-detail">家长手机号码</span><span class="zjq-right-detail" id="phone"></span></dd>
                <dd><span class="zjq-left-detail">住宿选择</span><span class="zjq-right-detail" id="accommodate"></span></dd>
            </dl>
            <div class="zjq-publishBtn">
                <%--<a type="button" class="btn btn-default">确认</a>--%>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="hplus/js/jquery.min.js"></script>
<script type="text/javascript" src="recruitStudent/js/jquery-3.0.0.min.js"></script>
<script type="text/javascript" src="js/myajax.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script type="text/javascript" src="js/functionUtil.js"></script>
<script type="text/javascript" src="recruitStudent/js/enrollmentList.js?d=${time}"></script>
<script type="text/javascript">
    //session失效，页面跳转
    pageJump();
    $(function(){
        $(".zjq-applyDetail .zjq-admittedBtn").click(function(){
            $(this).parent().parent().remove();
        });
        $(".zjq-applyDetail .zjq-checkDetail").click(function(){
            $(".zjq-select-modal").show();
            $(".zjq-select-modal .zjq-publishBtn .btn").click(function(){
                $(".zjq-select-modal").hide();
            });
        });
    });
    var recruit_id=<%=recruit_id%>;
    var title='<%=title%>';
    enroll_status='043002';
    showEnrollingPeople();
    createAdmission();
</script>
</body>
</html>
