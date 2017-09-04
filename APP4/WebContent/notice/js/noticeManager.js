var currentPage = 1;
var limit = 20;

function initHomePage() {
    loadContent();
    bindReturnClickEvent();
}

//加载内容列表
function loadContent() {
    $('html,body').animate({scrollTop:0},'slow');
    $('#LoadAnimation').removeClass('display');
    $.myajax({
        url: 'noticeAction/getNoticeList',
        data: {
            module_code: "009001",
            user_type: "003005",
            user_id: localStorage.getItem("user_id"),
            school_id: localStorage.getItem("school_id"),
            order_sql: "DESC",
            start_id: (currentPage - 1) * limit,
            limit: limit,
            page: currentPage
        },
        datatype: 'json',
        type: 'post',
        success: function (data) {
            var result = data.result;
            var pageCount = Math.ceil(result.total / limit); //取到pageCount的值(把返回数据转成object类型)
            addNoticeToWeb(data);
            if (pageCount < 2) {
                $("#page_pagintor").hide();
                return;
            }
            var options = {
                bootstrapMajorVersion: 3, //版本
                currentPage: currentPage, //当前页数
                totalPages: pageCount, //总页数
                alignment: "center",
                itemTexts: function (type, page, current) {
                    switch (type) {
                        case "first":
                            return "首页";
                        case "prev":
                            return "上一页";
                        case "next":
                            return "下一页";
                        case "last":
                            return "末页";
                        case "page":
                            return page;
                    }
                },//点击事件，用于通过Ajax来刷新整个list列表
                onPageClicked: function (event, originalEvent, type, page) {
                    currentPage = page;
                    loadContent();
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
        }
    });
}

//加载文章列表
function addNoticeToWeb(data) {
    if (isEmpty(data)) return;
    var _li = '';
    var result = data.result.data;
    for (var i in result) {
        var item = result[i];
        var view = inspectNoticeState(item.count_list, '008015');
        var reply = inspectNoticeState(item.count_list, '008020');
        var have_file = item.have_file == 1 ? '' : 'display';
        _li += '<li class="zjq-notice-list notice-list" notice-id="' + item.notice_id + '"><div class="zjq-noticeList-title"><h4>'
            + '' + item.sender_name + ' － ' + item.team_name + ''
            + '</h4><time>' + CalculationDateDiffer(item.send_time) + '</time></div><div class="zjq-noticeList-detail">'
            + '<h2>' + item.notice_title + '</h2>'
            + '<img src="notice/images/notice-3.png" alt="" class="zjq-notice-hitIcon ' + have_file + '">'
            + '<p>' + item.notice_content + '</p>'
            +(isNotEmpty(item.count_list)?'<div class="zjq-detail-icon"><span><img src="notice/images/notice-10.png" alt="" />' + view + '</span>' +
            '<span><img src="notice/images/notice-9.png" alt="" />' + reply + '</span> </div>':'')
            + '</div></li>';
    }
    $('#notice_list').empty().append(_li);
    $('#LoadAnimation').addClass('display');
    bindNoticeClickEvent();
}

function bindNoticeClickEvent() {
    $('.notice-list').unbind('click').on('click', function () {
        $('#LoadAnimation').removeClass('display');
        $('#noticeList').addClass('display');
        $('#noticeByID').removeClass('display');
        initDetailHomePage($(this).attr('notice-id'));
    });
}

function bindReturnClickEvent() {
    $('#return').unbind('click').on('click', function () {
        $('#noticeByID').addClass('display');
        $('#noticeList').removeClass('display');
        $('#file_list').empty();
        $('#notice_h4').empty();
        $('#send_time').empty();
        $('#notice_content').empty();
        $('#notice_title').empty();
        $('#Unread_count').empty();
        $('#unread_notice').empty();
        $('#Answer_count').empty();
        $('#Reply_notice').empty();
    });
}

function inspectNoticeState(date, str) {
    if (isEmpty(date)) return;
    var result = eval(date);
    if (str == '008015') return result[0].count + '人';
    else return result[1].count + '人';
}

function CalculationDateDiffer(date) {
    var sendDate = new Date(date);
    var year = sendDate.getFullYear();
    var month = ("0" + (sendDate.getMonth() + 1)).slice(-2);
    var day = ("0" + sendDate.getDate()).slice(-2);
    var myDate = new Date();
    var year1 = myDate.getFullYear();
    if (year1 > year) return year + '年' + month + '月' + day + '日';
    else {
        var DifferDate = '';
        var date3 = myDate.getTime() - date;
        //计算出相差天数
        var days = Math.floor(date3 / (24 * 3600 * 1000));
        //计算出小时数
        var leave1 = date3 % (24 * 3600 * 1000);    //计算天数后剩余的毫秒数
        var hours = Math.floor(leave1 / (3600 * 1000));
        //计算相差分钟数
        var leave2 = leave1 % (3600 * 1000);        //计算小时数后剩余的毫秒数
        var minutes = Math.floor(leave2 / (60 * 1000));
        // // //计算相差秒数
        // var leave3=leave2%(60*1000);      //计算分钟数后剩余的毫秒数
        // var seconds=Math.round(leave3/1000);
        if (days > 30) DifferDate = month + '月' + day + '日';
        else {
            if (isNotEmpty(days) && days > 0) DifferDate += days + '天';
            if (isNotEmpty(hours) && hours > 0) DifferDate += hours + '小时前';
            if (isNotEmpty(minutes) && isEmpty(days) && isEmpty(hours) && minutes < 60) DifferDate = minutes+'分钟前';
            if (isNotEmpty(minutes) && isEmpty(days) && isEmpty(hours) && minutes > 60) DifferDate = '刚刚';
        }
        return DifferDate;
    }
}

function toastrMassage(content) {
    if (isEmpty(content)) return;
    toastr.options = {
        "closeButton": true,
        "debug": false,
        "progressBar": true,
        "positionClass": "toast-top-center",
        "onclick": null,
        "showDuration": "400",
        "hideDuration": "1000",
        "timeOut": "1500",
        "extendedTimeOut": "1000",
        "showEasing": "swing",
        "hideEasing": "linear",
        "showMethod": "fadeIn",
        "hideMethod": "fadeOut"
    };
    toastr.info(content, "提示");
}


//
var ReplyCurrentPage = 1;
var ReplyLimit = 50;
var amr_to_mp3_action = localStorage.getItem('amr_to_mp3_action');
var ReplyDate = {};
var UnreadDate = {};
ReplyDate.school_id = localStorage.getItem('school_id');
ReplyDate.user_id = localStorage.getItem('user_id');
UnreadDate.school_id = localStorage.getItem('school_id');
UnreadDate.user_id = localStorage.getItem('user_id');

function initDetailHomePage(notice_id) {
    if (isEmpty(notice_id)) return false;
    ReplyDate.notice_id = notice_id;
    UnreadDate.notice_id = notice_id;
    loadByIDContent(notice_id);
    getUnreadCount(notice_id);
    getAnswerCount();
    bindSendOutClickEvent();
    bindMyModalClickEvent();
    bindDownloadClickEvent();
}


function loadByIDContent(notice_id) {
    if (isEmpty(notice_id)) return;
    $.myajax({
        url: 'noticeAction/getNoticeById',
        data: {notice_id: notice_id},
        datatype: 'json',
        success: function (data) {
            var result = data.result.data;
            $('#notice_h4').append(result.sender_name + '－ ' + result.team_name);
            $('#send_time').append(CalculationDateDiffer(result.send_time));
            $('#notice_title').append(result.notice_title);
            $('#notice_content').append(result.notice_content);
            getFileList(result.file_list);
        }
    });
}

function bindReplyNoticeClickEvent() {
    $('.ReplyNotice').unbind('click').on('click', function () {
        $('#ReplyReceiveName').empty().append('回复'+$(this).attr('receive_name'));
        ReplyDate.notice_id = $(this).attr('notice_id');
        ReplyDate.receive_id = $(this).attr('receive_id');
        ReplyDate.receive_type = '003010';
        getAnswerCount();
    });
}

function bindSendOutClickEvent() {
    $('#SendOut').unbind('click').on('click', function () {
        var date = {};
        date.reply_content = $('#reply_content').val();
        if (isEmpty(date.reply_content)) {
            return false;
        }
        date.notice_id = $(this).attr('notice_id');
        date.receive_id = $(this).attr('receive_id');
        date.receive_type = $(this).attr('receive_type');
        date.school_id = localStorage.getItem('school_id');
        date.user_id = localStorage.getItem('user_id');
        date.head_url = localStorage.getItem('head_url');
        date.user_type = '003005';
        $.myajax({
            url: 'noticeAction/replyNotice',
            data: date,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                if (data.success == true) {
                    var result = data.result.data;
                    var _div = '<div class="qbb-chat-group">'
                        + '<img class="qbb-chat-head fr" src="' + result.head_url + '">'
                        + '<div class="qbb-chat-text fr">'
                        + '<span class="qbb-chat-name"></span>'
                        + '<div class="qbb_triangle_border">'
                        + '<div class="popup qbb-right">'
                        + '<span class="qbb-right"></span>' + result.reply_content
                        + '</div> </div> </div> </div>';
                    $('#reply_content').val('')
                    $('#Reply_list').append(_div);
                }
            }
        });
    });
}

function bindMyModalClickEvent() {
    $('#myModal').unbind('click').on('click', function () {
        ReplyDate.notice_id = $('#SendOut').attr('notice_id');
        ReplyDate.receive_id = '';
        ReplyDate.receive_type = '';
        getAnswerCount();
    });
}

function bindDownloadClickEvent() {
    $('#download').unbind('click').on('click', function () {
        var file_url = $('#fileCarousel div.active img').attr('src');
        var file_name = file_url.substring(file_url.indexOf('notice/') + 7);
        var $a = $("<a></a>").attr("href", file_url).attr("download", file_name);
        $a[0].click();
    });
}

function bindPhotoClickEvent() {
    $('.zwf-file').unbind('click').on('click', function () {
        $('#fileCarousel div').removeClass('active').addClass('display');
        var show_no = $(this).attr('show_no');
        $('#file' + show_no + '').removeClass('display').addClass('active');
    });
}

function InterceptReplyContent(str) {
    if (40 > str.length || isEmpty(str)) return str;
    var str1 = str.substring(0, 40);
    return str1 + '......';
}

function getUnreadCount() {
    $.myajax({
        url: 'noticeAction/getUnreadUserList',
        data: UnreadDate,
        datatype: 'json',
        type: 'post',
        success: function (data) {
            addNoticeUnreadToWeb(data);
        }
    });
}

function addNoticeUnreadToWeb(data) {
    var _li = '';
    var total = 0;
    var result = data.result.data;
    for (var i in result) {
        var item = result[i];
        _li += '<li>' + item.receive_name + '</li>';
        total++;
    }
    $('#Unread_count').append(total);
    $('#unread_notice').append(_li);
    $('.spinner').addClass('display');
}

function getAnswerCount() {
    ReplyDate.start_id = (ReplyCurrentPage - 1) * ReplyLimit;
    ReplyDate.limit = ReplyLimit;
    ReplyDate.page = ReplyCurrentPage;
    $.myajax({
        url: 'noticeAction/getNoticeReplyList',
        data: ReplyDate,
        datatype: 'json',
        type: 'post',
        success: function (data) {
            var result = data.result;
            var pageCount = Math.ceil(result.total / ReplyLimit); //取到pageCount的值(把返回数据转成object类型)
            addNoticeReplyToWeb(data, ReplyDate.receive_id);
            if (pageCount < 2) {
                $("#page_pagintor1").hide();
                return;
            }
            var options = {
                bootstrapMajorVersion: 3, //版本
                currentPage: ReplyCurrentPage, //当前页数
                totalPages: pageCount, //总页数
                alignment: "center",
                itemTexts: function (type, page, current) {
                    switch (type) {
                        case "first":
                            return "首页";
                        case "prev":
                            return "上一页";
                        case "next":
                            return "下一页";
                        case "last":
                            return "末页";
                        case "page":
                            return page;
                    }
                },//点击事件，用于通过Ajax来刷新整个list列表
                onPageClicked: function (event, originalEvent, type, page) {
                    ReplyCurrentPage = page;
                    getAnswerCount();
                }
            };
            $("#page_pagintor1").bootstrapPaginator(options);
            $("#page_pagintor1").show();
        }
    });
}

function addNoticeReplyToWeb(data, receive_id) {
    var result = data.result.data;
    if (isEmpty(receive_id)) {
        var _li = '';
        for (var i in result) {
            var item = result[i];
            var reply_content = InterceptReplyContent(item.reply_content);
            _li += '<li>' + item.receive_name + '：' + reply_content + '<a class="qbb-reply ReplyNotice" receive_name="' + item.receive_name + '" data-toggle="modal" data-target="#myModal" receive_id="' + item.receive_id + '" notice_id="' + item.notice_id + '">回复</a></li>';
        }
        $('#Answer_count').empty().append(data.result.total);
        $('#Reply_notice').empty().append(_li);
        bindReplyNoticeClickEvent();
    } else {
        var _div = "";
        for (var i in result) {
            var item = result[i];
            if (item.user_type == '003005')
                _div += '<div class="qbb-chat-group">'
                    + '<img class="qbb-chat-head fr" src="' + item.head_url + '">'
                    + '<div class="qbb-chat-text fr">'
                    + '<span class="qbb-chat-name fr" style="margin-right: 19px;">' + item.reply_name + '</span>'
                    + '<div class="qbb_triangle_border">'
                    + '<div class="popup qbb-right">'
                    + '<span class="qbb-right"></span>' + item.reply_content
                    + '</div> </div> </div> </div>';
            else
                _div += '<div class="qbb-chat-group" style=" display: inline;">'
                    + '<img class="qbb-chat-head fl" src="' + item.head_url + '">'
                    + '<div class="qbb-chat-text fl">'
                    + '<span class="qbb-chat-name fl" style="margin-left: 15px;">' + item.receive_name + '</span>'
                    + '<div class="qbb_triangle_border clearfix">'
                    + '<div class="popup qbb-left">'
                    + '<span class="qbb-left"><em class="qbb-left"></em></span>' + item.reply_content
                    + '</div></div></div></div>';
        }
        $('#SendOut').attr('notice_id', item.notice_id);
        $('#SendOut').attr('receive_id', item.receive_id);
        $('#SendOut').attr('receive_type', item.receive_type);
        $('#Reply_list').empty().append(_div);
    }
}

function getFileList(str) {
    if (isEmpty(str)) {
        return;
    }
    var date = eval(str);
    var _img = '';
    var _amr = '';
    var sort = 0;
    var _fileCarousel = '';
    var show_no = 0;
    for (var i in date) {
        var item = date[i];
        if (item.file_type == '020005') {
            if (isEmpty(_fileCarousel)) _fileCarousel = item.file_url;
            else _fileCarousel += ',' + item.file_url;
            _img += '<div class="hxx-cell"><div class="hxx-img zwf-file" show_no="' + show_no + '" style="background:url(' + item.file_resize_url + ') no-repeat center #d6d6d6;background-size: contain;" data-toggle="modal" data-target="#myModalPhoto"></div></div>';
            show_no++;
        } else {
            var file_url = AmrToMp3(item.file_url);
            var play_time = item.play_time + '"';
            _amr += '<div class="hxx-cell"><div class="hxx-audio zwf-file jPlayer" amr_sort="' + sort + '" amr_url="' + file_url + '"><div class="audio-btn audio-play jPlayer-play"></div><h6 class="audio-time" play_time="'+play_time+'">' + play_time + '</h6></div></div>';
            if (sort == 0) initjPlayer(file_url);
            sort++;
        }
    }
    var _file = _img + _amr;
    if (isEmpty(_file)) return false;
    $('#file_list').append(_file);
    addCarouselFile(_fileCarousel);
    bindPhotoClickEvent();
    jPlayer();
}

function initjPlayer(str) {
    $("#jPlayer").jPlayer({
        ready: function () {
            $(this).jPlayer("setMedia", {
                mp3: str
            });
        },
        swfPath: "/js",
        supplied: "mp3"
    });
}

function addCarouselFile(str) {
    var _fileCarousel = '';
    var str1 = str.split(',');
    for (var i in str1) {
        var file_url = str1[i] + '?d=' + (new Date()).getTime();
        if (i == 0) {
            if (accAdd(i, 1) == str1.length) {
                $('#leftCarousel').addClass('display');
                $('#rightCarousel').addClass('display');
            }
            else {
                $('#leftCarousel').removeClass('display');
                $('#rightCarousel').removeClass('display');
            }
            _fileCarousel += '<div class="start display" id="file' + i + '"><img src="' + file_url + '"></div>';
        } else if (accAdd(i, 1) == str1.length) _fileCarousel += '<div class="end display" id="file' + i + '"><img src="' + file_url + '"></div>';
        else _fileCarousel += '<div class="display" id="file' + i + '"><img src="' + file_url + '"></div>';
    }
    $('#fileCarousel').empty().append(_fileCarousel);
    errorImg();
}

var is_pause = false;
function jPlayer() {
    $('.jPlayer').unbind('click').on('click', function () {
        if ($(this).children('.jPlayer-play').hasClass('audio-stop')) {
            $("#jPlayer").jPlayer("pause");
            $(this).children('.jPlayer-play').addClass('audio-play').removeClass('audio-stop');
            if (isEmpty(showDuration)) $(this).children('.audio-time').text($(this).children('.audio-time').attr('play_time')+'"');
            is_pause = true;
        } else {
            is_pause = false;
            obj = $(this);
            if (isEmpty(showDuration)) showDuration = obj.children('.audio-time').text().replace('"', '');
            setShowDuration(obj);
            var startTime = accSub($(this).children('.audio-time').attr('play_time').replace('"', ''),showDuration);
            $("#jPlayer").jPlayer("play",startTime);
            $(this).children('.jPlayer-play').addClass('audio-stop').removeClass('audio-play');
            initjPlayer($(this).attr('amr_url'));
        }
    });
}

var showDuration = 0;
var obj;
function setShowDuration() {
    if (is_pause) return;
    if (showDuration == 0) {
        obj.click();
    } else {
        obj.children('.audio-time').text(showDuration + '"');
        --showDuration;
        setTimeout(function () {				//设置倒计时速度时时间走起来
            setShowDuration();
        }, 1000);
    }
}

function AmrToMp3(str) {
    if (isEmpty(str)) return;
    var file_type = str.substring(accSub(str.length, 3), str.length);
    if (file_type == 'amr') {
        $.myajax({
            url: amr_to_mp3_action,
            data: {amrUrl: str},
            datatype: 'json',
            type: 'post',
            success: function (data) {
                if (data.success == true) console.log('转换成功...');
                else console.log('转换失败...');
            }
        });
        return str.replace('amr', 'mp3');
    } else return str;
}

function errorImg() {
    $('img').error(function(){
        $(this).attr('src', "notice/images/error.png");
    });
}