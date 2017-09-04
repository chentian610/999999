<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()	+ path + "/" ;
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <!-- Data Tables -->
    <link href="notice/css/bootstrap.min.css" rel="stylesheet">
    <link href="hplus/css/font-awesome.min.css" rel="stylesheet"/>
    <link href="hplus/css/animate.min.css" rel="stylesheet">
    <link href="hplus/css/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="hplus/css/font-awesome.min3.0.1.css" />
    <link rel="stylesheet" href="notice/css/noticeManager.css?d=${time}"/>
    <link rel="stylesheet" href="notice/css/load.animation.css"/>
    <link rel="stylesheet" href="notice/css/style.min.css"/>
    <link rel="stylesheet" href="notice/css/noticeDetail.css?d=${time}"/>
</head>
<body>
<div class="container" id="noticeList">
    <ul id="notice_list">
    </ul>
    <div class="zjq-selList">
        <nav aria-label="Page navigation">
            <div class= "pull-right" style=" margin-right: 30%;">
                <ul id="page_pagintor"></ul>
            </div>
        </nav>
    </div>
</div >
<div class="spinner display" id="LoadAnimation">
    <div class="rect1"></div>
    <div class="rect2"></div>
    <div class="rect3"></div>
    <div class="rect4"></div>
    <div class="rect5"></div>
</div>
<div class="container containers display" id="noticeByID">
    <input id="return" type="button" value="返回" class="btn btn-primary" style="margin-top: -90px; width: 95px;"/>
    <div>
        <div class="zjq-notice-lists">
            <div class="zjq-noticeList-titles">
                <h4 id="notice_h4"></h4>
                <time id="send_time"></time>
            </div>
            <div class="zjq-noticeList-details">
                <h2 id="notice_title"></h2>
                <p id="notice_content"></p>
            </div>
        </div>
    </div>
    <div class="hxx-container" id="file_list">
    </div>
    <div id="jPlayer" class="display">
        <a href="#" class="jp-play" id="play">Play</a>
        <a href="#" class="jp-pause" id="pause">Pause</a>
    </div>
    <ul id="myTab" class="clearfix">
        <li class="active unread"><a href="#unread"><img src="notice/images/notice-8.png" alt="" /><img class="zjq-unread-icon" src="notice/images/notice-7.png" alt="" />未读 (<span id="Unread_count"></span>)</a></li>
        <li class="answer"><a href="#answer"><img src="notice/images/notice-4.png" alt="" /><img class="zjq-sel-icon"  src="notice/images/notice-5.png" alt="" />回复 (<span id="Answer_count"></span>)</a></li>
    </ul>
    <div class="tab-content" style="max-height: 500px;overflow-y: auto;width: 100%;">
        <div class="tab-pane active" id="unread">
            <ul id="unread_notice">
            </ul>
        </div>
        <div class="tab-pane" id="answer">
            <ul id="Reply_notice">
            </ul>
        </div>
    </div>
    <div class="zjq-selLists">
        <nav aria-label="Page navigation">
            <div class= "pull-right" style=" margin-right: 45%;">
                <ul id="page_pagintor1"></ul>
            </div>
        </nav>
    </div>
</div>
<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content animated bounceInRight">
            <div class="modal-header" style="padding: 15px;">
                <button type="button" class="close" data-dismiss="modal"><img src="notice/images/notice-6.png" class="qbb-close">
                </button>
                <h4 class="modal-title" style="text-align: center;" id="ReplyReceiveName">回复王明明</h4>
            </div>
            <small class="font-bold">
                <div class="modal-body" style="padding: 0;">
                    <div class="qbb-chat-content" id="Reply_list">
                        <div class="qbb-chat-group" style=" display: inline-block;">
                            <img class="qbb-chat-head fl" src="notice/images/notice-13.png">
                            <div class="qbb-chat-text fl">
                                <span class="qbb-chat-name">王明明</span>
                                <div class="qbb_triangle_border clearfix">
                                    <div class="popup qbb-left">
                                        <span class="qbb-left"><em class="qbb-left"></em></span>收到了，在哪集合，怎么通知别人
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="qbb-chat-footer">
                        <input type="text" id="reply_content">
                        <span id="SendOut">发送</span>
                    </div>
                </div>
            </small>
        </div>
    </div>
</div>
<div class="modal inmodal" id="myModalPhoto" tabindex="-1" role="dialog" aria-hidden="true" style=" margin-top: 5%;display: none;">
    <div class="modal-dialog">
        <div class="modal-content animated bounceInRight">
            <div class="modal-body" style="padding: 0;">
                <div id="fileCarousel">
                </div>
                <div class="zwf-div-left" id="leftCarousel"><img src="notice/images/left.png" style=" margin-left: 20%;"></div>
                <div class="zwf-div-right" id="rightCarousel"><img src="notice/images/right.png" style="margin-right: 20%;"></div>
                <div id="download" class="zwf-div-download"><img src="notice/images/download.png"></div>
            </div>
        </div>
    </div>
</div>
<script src="hplus/js/jquery.min.js?v=2.1.4"></script>
<script src="js/myajax.js"></script>
<script src="hplus/js/bootstrap.min.js?v=3.3.5"></script>
<script type="text/javascript" src="hplus/js/bootstrap-paginator.js"></script>
<script src="js/functionUtil.js"></script>
<script src="js/mathUtil.js"></script>
<script src="hplus/js/plugins/toastr/toastr.min.js"></script>
<script src="hplus/js/prettify.js"></script>
<script src="hplus/js/content.min.js?v=1.0.0"></script>
<script src="notice/js/jplayer.min.js"></script>
<script src="notice/js/noticeManager.js?d=${time}"></script>
<script type="text/javascript">
    initHomePage();
</script>
<script type="text/javascript">
    $(function () {
        $('.audio-btn').on('click', function (e) {
            e.preventDefault();
            if ($(this).hasClass('audio-play')) {
                $(this).removeClass('audio-play').addClass('audio-stop');
            } else {
                $(this).removeClass('audio-stop').addClass('audio-play');
            }
        });
        $('#myTab .answer a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
            $('.zjq-unread-icon').show().siblings('img').hide();
            $('.zjq-sel-icon').show().siblings('img').hide();
        });
        $('#myTab .unread a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
            $('.zjq-unread-icon').hide().siblings('img').show();
            $('.zjq-sel-icon').hide().siblings('img').show();
        });
        $('.qbb-reply').click(function (e) {
            e.preventDefault();
            $('.model').show();
            $('body').addClass('unscroll');
            console.log(1);
        })
        $('.qbb-close').click(function (e) {
            e.preventDefault();
            $('body').removeClass('unscroll');
            $('.model').hide();
            console.log(2);
        });
    });
    $(function () {
        $('#leftCarousel').on('click',function () {
            if ($('#fileCarousel div.active').prev().length == 0)  $('#fileCarousel div.active').removeClass('active').addClass('display').siblings('.end').removeClass('display').addClass('active');
            else $('#fileCarousel div.active').removeClass('active').addClass('display').prev().removeClass('display').addClass('active');
        });

        $('#rightCarousel').on('click',function () {
            if ($('#fileCarousel div.active').next().length == 0) $('#fileCarousel div.active').removeClass('active').addClass('display').siblings('.start').removeClass('display').addClass('active');
            else $('#fileCarousel div.active').removeClass('active').addClass('display').next().removeClass('display').addClass('active');
        });
    });
</script>
</body>
</html>
