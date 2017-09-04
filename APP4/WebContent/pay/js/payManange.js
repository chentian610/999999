var currentPage=1;
var limit=5;
var detail_currentPage=1;
var detail_limit=10;
var team_currentPage=1;
var team_limit=10;
var payList = {};
var teamVO = {};
var pay_type = '';

function initPayRecordPage(module_code) {
    pay_type = module_code == '009029'?'038001':'038002';
    initPayRecordList();
    bindReturnClickEvent();
    bindIDViewUnpaidClickEvent();
    bindIsPayClickEvent();
}

function initPayRecordList() {
    $('html,body').animate({scrollTop:0},'slow');
    //加载内容列表
    $.myajax({
        url:'payAction/getPayList',
        data:{order_sql:'desc',school_id:localStorage.getItem("school_id"),start_id:(currentPage-1)*limit,limit:limit,page:currentPage,pay_type:pay_type},
        datatype:'json',
        type:'post',
        success:function(data){
            var result = data.result;
            var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
            addToWeb(data);
            if (pageCount<2) {
                $("#page_pagintor").hide();
                return;
            }
            var options = {
                bootstrapMajorVersion: 3, //版本
                currentPage: currentPage, //当前页数
                totalPages: pageCount, //总页数
                alignment:"center",
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
                    currentPage=page;
                    initPayRecordList();
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
        }
    });
}

function addToWeb(data) {
    if (isEmpty(data)) return;
    var _li = '';
    var result = data.result.data;
    for(var i in result){
        var item = result[i];
        payList[item.pay_id] = item;
        _li += '<li class="zjq-fee-list See-detail" pay_id="'+item.pay_id+'"><h3 class="zjq-fee-sort">'+judgeIcon(item.pay_type)+item.pay_type_name+'：'+item.pay_category_name+'<time>'+CalculationDateDiffer(item.create_date)+'</time></h3>'
            + ' <h2 class="zjq-fee-price">￥'+isDot(item.pay_money)+'</h2><p class="zjq-fee-range">收费范围：<span id="Charge-range-'+item.pay_id+'">'+item.pay_team_names+'</span></p><p class="zjq-fee-time">截至日期：'+getDateStr(item.end_date)+'</p>'
            +'<p class="zjq-fee-initiator">发起人：'+item.sender_name+'</p> </li>';
    }
    $('.pay-list').empty().append(_li);
    bindSeeDetailClickEvent();
}

function bindSeeDetailClickEvent() {
    $('.See-detail').on('click',function () {
        var payVO = payList[$(this).attr('pay_id')];
        var title = ''+judgeIcon(payVO.pay_type)+payVO.pay_type_name+'：'+payVO.pay_category_name+'<time>'+CalculationDateDiffer(payVO.create_date)+'</time>';
        var pay_money = '￥'+isDot(payVO.pay_money);
        var end_date = '截至日期：'+getDateStr(payVO.end_date);
        $('#pay_id').attr('pay-id',payVO.pay_id);
        $('#end-date').empty().append(end_date);
        $('#pay-money').empty().append(pay_money);
        $('#pay-title').empty().append(title);
        team_currentPage = 1;
        initPayGroupList(payVO);
        $('.pay-details').addClass('display');
        $('.single-pull-right').removeClass('display');
    });
}

function initPayGroupList(payVO) {
    var date = {};
    var pay_id = payVO.pay_id;
    date.school_id = localStorage.getItem("school_id");
    date.pay_id = pay_id;
    date.user_type = payVO.user_type;
    $.myajax({
        url:'payAction/getPayGroupList',
        data:date,
        datatype:'json',
        type:'post',
        success:function(data) {
            addTeamWeb(data,pay_id);
        }
    });
}

function addTeamWeb(data,pay_id) {
    var payVO = payList[pay_id];
    var pay_money = payVO.pay_money;
    var count_done = 0;
    var count = 0;
    var result = eval(data.result.data);
    var _dl = '<dt>班级缴费详情</dt>';
    for(var i in result){
        var teamVO = result[i];
        var countVO = eval('('+teamVO.pay_group_count+')');
        _dl += '<dd data-toggle="modal" data-target="#myModal" team-id="'+teamVO.team_id+'" team-type="'+teamVO.team_type+'" class="View_unpaid">'+teamVO.team_name+'<span>已缴费'+countVO.count_done+'/'+countVO.count+'</span></dd>';
        count_done = accAdd(count_done,countVO.count_done);
        count = accAdd(count,countVO.count);
    }
    $('#team-fee-details').empty().append(_dl);
    var pay_count = {};
    pay_count.count = count;
    pay_count.count_done = count_done;
    pay_count.pay_money = pay_money;
    var progress = payProgressBar(pay_count);
    $('#Charge-range').empty().append($('#Charge-range-'+pay_id+'').text());
    $('#Payment-schedule').empty().append(count_done+'/'+count);
    $('#Total-amount').empty().append('总金额：<span class="zjq-scale"><span class="zjq-scale-pay" style="width: '+progress+'px;"></span></span>'+accMul(count_done,pay_money)+'/'+accMul(count,pay_money)+'');
    bindClassViewUnpaidClickEvent();
}

function bindIsPayClickEvent() {
    $('.is-pay').on('click',function () {
        if ($(this).attr('pay-status') != '003') teamVO.pay_status = $(this).attr('pay-status');
        else teamVO.pay_status = '';
        getUserPayRecordList();
    });
}

function bindClassViewUnpaidClickEvent() {
    $('.View_unpaid').on('click',function () {
        $('#is_pay').show();
        teamVO.team_id = $(this).attr('team-id');
        teamVO.team_type = $(this).attr('team-type');
        teamVO.user_type = '003010';
        detail_currentPage = 1;
        getUserPayRecordList();
    });
}

function bindIDViewUnpaidClickEvent() {
    $('#View_unpaid').on('click',function () {
        $('#is_pay').hide();
        teamVO.user_type = '003';
        detail_currentPage = 1;
        getUserPayRecordList();
    });
}

function getUserPayRecordList() {
    var date = {};
    date.school_id = localStorage.getItem("school_id");
    date.pay_id =  $('#pay_id').attr('pay-id');
    date.start_id = (detail_currentPage-1)*detail_limit;
    date.limit = detail_limit;
    date.page = detail_currentPage;
    date.order_sql = 'DESC';
    teamVO.trade_status = 'TRADE_SUCCESS';
    if (teamVO.user_type == '003010') {
        date.team_id = teamVO.team_id;
        date.team_type = teamVO.team_type;
        if (isNotEmpty(teamVO.pay_status)) date.pay_status = teamVO.pay_status;
    } else {
        if (payList[date.pay_id].user_type == '003010') date.user_type = payList[date.pay_id].user_type;
        date.pay_status = 0;
    }
    if (isNotEmpty(teamVO.trade_status)) date.trade_status = teamVO.trade_status;
    $.myajax({
        url:'payAction/getUserPayRecordList',
        data:date,
        datatype:'json',
        type:'post',
        success:function(data){
            var result = data.result;
            var pageCount = Math.ceil(result.total/detail_limit); //取到pageCount的值(把返回数据转成object类型)
            insertStudentPayList(data);
            if (pageCount<2) {
                $("#detail_page_pagintor").hide();
                return;
            }
            var options = {
                bootstrapMajorVersion: 3, //版本
                currentPage: detail_currentPage, //当前页数
                totalPages: pageCount, //总页数
                alignment:"center",
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
                    detail_currentPage=page;
                    getUserPayRecordList();
                }
            };
            $("#detail_page_pagintor").bootstrapPaginator(options);
            $("#detail_page_pagintor").show();
        }
    });
}

function insertStudentPayList(data) {
    var _dd = '';
    var result = data.result.data;
    for (var i in result) {
        var item = result[i];
        _dd += '<dd>'+item.user_name+judgeSex(item.sex)+item.class_name+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+item.student_code+judgeIsPay(item.pay_status)+'</dd>';
    }
    $('.student-pay-list').empty().append(_dd);
}

function bindReturnClickEvent() {
    $('#Return').on('click',function () {
        $('.pay-details').removeClass('display');
        $('.single-pull-right').addClass('display');
    });
}

function getDateStr(date) {
    var myDate = new Date(date);
    var year = myDate.getFullYear();
    var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
    var day = ("0" + myDate.getDate()).slice(-2);
    // var h = ("0" + myDate.getHours()).slice(-2);
    // var m = ("0" + myDate.getMinutes()).slice(-2);
    // var s = ("0" + myDate.getSeconds()).slice(-2);
    return year + "-" + month + "-" + day;
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

function payProgressBar(str) {
    var total_width = '230';
    var total_money = parseFloat(accMul(str.count,str.pay_money));
    var already_money = parseFloat(accMul(str.count_done,str.pay_money));
    var proportion = already_money == 0 ? "0" : (Math.round((already_money / total_money)*100)/100);
    return accMul(total_width,proportion);
}

function judgeSex(str) {
    if (str == 1) return '<img src="pay/icon/girl.png" alt="" />';
    else return '<img src="pay/icon/boy.png" alt="" />';
}

function judgeIsPay(str) {
    if (str == 1) return '<span>已缴费</span>';
    else return '<span class="zjq-unpaid">未缴费</span>';
}

function isDot(num) {
    var result = (num.toString()).indexOf(".");
    if(result != -1) {
        return num;
    } else {
        return num + '.00';
    }
}

function judgeIcon(str) {
    if (str == '038001') return '<img src="pay/icon/jiaofei1.png" alt="" />';
    else return '<img src="pay/icon/jiaofei2.png" alt="" />';
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
    toastr.info(content,"提示");
}
