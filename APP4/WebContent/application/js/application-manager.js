var store = window.localStorage;
var currentPage = 1;
var limit = 10;
var version = 0;
var balance = 0;
var module_price = 0;

function initSchoolTypeList() {
    $.myajax({
        url : 'dictAction/getDictionary?dict_group=002',
        datatype : 'json',
        type : 'post',
        success : function(data) {
            $('.school_type_list').empty();
            var result = data.result.data;
            var _option = '<option value="">选择学校类型</option>';
            for ( var i in result) {
                var item = result[i];
                _option +=  '<option value="'+item.dict_code+'">'+item.dict_value+'</option>';
            }
            $('.school_type_list').append(_option);
        }
    });
}

function initAgentList() {
    $.myajax({
        url : 'agentAction/getAgentList',
        datatype : 'json',
        type : 'post',
        success : function(data) {
            $('.agent_list').empty();
            var result = data.result.data;
            var _option = '<option value="">选择代理商</option>';
            for ( var i in result) {
                var item = result[i];
                _option +=  '<option value="'+item.phone+'">'+item.agent_name+'</option>';
            }
            $('.agent_list').append(_option);
        }
    });
}

function bindSearchSchoolNameClickEvent() {
	$('#search_school_name').on('click',function () {
        initSchoolList($('.search_school_name').val());
    });
}

function bindAgentSchoolListClickEvent() {
	$('.agent_list').on('change',function () {
        initSchoolList($('.search_school_name').val());
    });
}

function bindSchoolTypeListClickEvent() {
    $('.school_type_list').on('change',function () {
        initSchoolList($('.search_school_name').val());
    });
}

//初始化代理商申请学校列表
function initSchoolList(school_name) {
    $('.school_list').empty();
    $.myajax({
        url:'schoolAction/getAgentApplySchoolList',
        data:{school_name:school_name,agent_phone:$('.agent_list option:selected').val(),school_type:$('.school_type_list option:selected').val(),start_id:(currentPage-1)*limit,limit:limit,page:currentPage,order_sql:'desc'},
        datatype:'json',
        type:'post',
        success:function(data){
            var result = data.result;
            var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
            addSchoolListToWeb(data);
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
                    initSchoolList(school_name);
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
        }
    });
}

function addSchoolListToWeb(data) {
    var _li = '';
    var result = data.result.data;
    for ( var i in result) {
        var item = result[i];
        var organize_pic_url = '';
        if (isEmpty(item.organize_pic_url)) organize_pic_url = 'images/gzh_wx.jpg';
        else organize_pic_url = item.organize_pic_url;
        _li += '  <li class="list-group-item" school_id="'+item.school_id+'" agent_id="'+item.agent_id+'" school_admin_phone="'+item.school_admin_phone+'" school_type="'+item.school_type+'" school_name="'+item.school_name+'" domain="'+item.domain+'" agent_phone="'+item.agent_phone+'" organize_pic_url="'+item.organize_pic_url+'" school_price="'+item.unit_price+'">' +
            '<div class="media"><div class="media-left media-middle">'
            +'<img class="media-object img-circle school-head" src="'+organize_pic_url+'" alt=""> </div>'
            +'<div class="media-body school-info">'
            +'<p><span style="color: #4a4a4a;">'+item.school_name+'【'+item.school_id+'】</span></p>'
            +'<p style="color: #4a4a4a;">'+item.agent_name+'／¥'+item.unit_price+'／'+item.agent_phone+'</p>'
            +'<p>到期时间：'+getDateStr(item.valid_date)+'</p></div>'+judgeAppStatus(item.status,item.valid_date)+'</div></li>';
    }
    $('.school_list').append(_li);
    bindUpdateStatusClickEvent();
    bindQueryDetailsClickEvent();
    bindIsUpperLowerLineClickEvent();
    bindRenewClickEvent();
}

function bindRenewClickEvent() {
    $('.renew').on('click',function () {
        var agent_id = $(this).parent().parent().parent().attr('agent_id');
        var school_price = $(this).parent().parent().parent().attr('school_price');
        $.myajax({
            url:'balanceAction/consumptionBalance',
            data:{consumption_money:school_price,agent_id:agent_id},
            datatype:'json',
            type: 'POST',
            success:function(data){
                bindMessage('续费成功...','信息提示！');
            }
        });
    });
}

function bindIsUpperLowerLineClickEvent() {
    $('.is-upper_lower_line').on('click',function () {
        var school_id = $(this).parent().parent().parent().attr('school_id');
        var app_status = $(this).attr('app_status');
        judgeAppStatusAppend($(this));
        $.myajax({
            url:'schoolAction/updateSchool',
            data:{school_id:school_id,status:app_status},
            datatype:'json',
            type: 'POST',
            success:function(data){
                bindMessage('操作成功...','信息提示！');
            }
        });
    });
}

function bindQueryDetailsClickEvent() {
    $('.query_details').on('click',function () {
        var organize_pic_url = '';
        if (isEmpty($(this).parent().parent().parent().attr('organize_pic_url'))) organize_pic_url = 'images/gzh_wx.jpg';
        else organize_pic_url = $(this).parent().parent().parent().attr('organize_pic_url');
        $('#sample_school_name').empty().append($(this).parent().parent().parent().attr('school_name'));
        $('#sample_school_type').empty().append(judgeSchoolType($(this).parent().parent().parent().attr('school_type')));
        $('#sample_domain').empty().append($(this).parent().parent().parent().attr('domain'));
        $('#school_log').empty().attr('src',organize_pic_url);
        $('#btn_div').attr('school_id',$(this).parent().parent().parent().attr('school_id'));
        $('#btn_div').attr('agent_id',$(this).parent().parent().parent().attr('agent_id'));
        $('#btn_div').attr('agent_phone',$(this).parent().parent().parent().attr('agent_phone'));
        $('#btn_div').attr('school_price',$(this).parent().parent().parent().attr('school_price'));
        loadSchoolAppTemplateList($(this).parent().parent().parent().attr('school_id'),$(this).parent().parent().parent().attr('school_type'));
        var school_admin_phone = $(this).parent().parent().parent().attr('school_admin_phone').split(',');
        var _phone = '';
        for (var i in school_admin_phone) {
            _phone += '<span class="font-bold margin-left font-content" style=" margin-left: 30px;">'+school_admin_phone[i]+'</span>';
        }
        $('.insert_phone').empty().append(_phone);
        VerificationServerConfig($(this).parent().parent().parent().attr('school_id'));
    });
}

function bindUpdateStatusClickEvent() {
	$('.update_status').unbind('click').on('click',function () {
	    var obj = $(this);
        var school_id = '';
        var school_price = '';
        var agent_id = '';
        var app_status = $(this).attr('app_status');
	    if ($(this).attr('is_modal') == 'true') {
            school_id = $(this).parent().attr('school_id');
            school_price = $(this).parent().attr('school_price');
            agent_id = $(this).parent().attr('agent_id');
        } else {
            school_id = $(this).parent().parent().parent().attr('school_id');
            school_price = $(this).parent().parent().parent().attr('school_price');
            agent_id = $(this).parent().parent().parent().attr('agent_id');
        }
        $.myajax({
            url:'schoolAction/updateSchool',
            data:{school_id:school_id,status:app_status,unit_price:school_price,agent_id:agent_id},
            datatype:'json',
            type: 'POST',
            success:function(data){
                judgeAppStatusAppend(obj);
                toastrMessage('操作成功...','信息提示！');
            }
        });
    });
}

function VerificationServerConfig(school_id) {
    $.myajax({
        url:'schoolAction/getSchoolServerConfig',
        data:{school_id:school_id},
        datatype:'json',
        type: 'POST',
        success:function(data){
            var result = data.result.data;
            if (isEmpty(result)) {
                $('#server_tent').hide();
                $('#server_config').attr('is_config',false);
                $('#prompt_text').empty().append('该学校没有选择服务器配置...');
            } else {
                $('#prompt_text').empty();
                $('#memory').empty().append(result.memory+"G");
                $('#disk').empty().append(result.disk+"G");
                $('#bandwidth').empty().append(result.bandwidth+"M");
                $('#server_price').empty().append(IntegerSum(IntegerSum(result.memory_price,result.disk_price),result.bandwidth_price));
                $('#server_config').attr('is_config',true);
                $('#server_tent').show();
            }
        }
    });
}

//加载模板列表
function loadSchoolAppTemplateList(school_id,school_type){
    var date = {};
    $.myajax({
        url:'moduleAction/getSchoolModuleBasicsList',
        data:{school_type:school_type,is_inactive:0,school_id:school_id},
        datatype:'json',
        success:function(data){
            var result = data.result.data;
            for(var i in result) {
                var item = result[i];
                date[item.module_id] = item;
            }
            loadAppModuleList(school_type,date);
        }
    });
}
//加载模板列表
function loadAppModuleList(school_type,date){
    $.myajax({
        url:'templateAction/getModuleBasicsList',
        data:{school_type:school_type},
        datatype:'json',
        success:function(data){
            $('#model').empty();
            module_price = 0;
            var result = data.result.data;
            var _li = '<div class="row">';
            for(var i in result) {
                var item = result[i];
                _li += setDisplayModuleHtml(item,date);
            }
            _li += '</div>';
            $('#model').append(_li);
            $('#set_module_price').empty().append(CalculationTotalAmount());
        }
    });
}

//转换日期格式
function CalculationTotalAmount() {
    $('.btn_module').each(function () {
        if($(this).prop('checked')) module_price = IntegerSum(module_price,$(this).attr('module_price'));
    });
    return module_price;
}

function setDisplayModuleHtml(item,date) {
    var _li = '';
    var checkedValue = '';
    var click = 'onclick="return false;"';
    if (isNotEmpty(date[item.module_id])){
        checkedValue = 'checked';
    }
    _li += '<div class="checkbox checkbox-success col-md-6" style=" margin-top: 10px;">'
        +'<input class="styled btn_module" module_id="'+item.module_id+'" module_price="'+item.module_price+'" type="checkbox" '+checkedValue+' '+click+'>'
        +'<label for="checkbox4"></label><span class="price">'+item.module_name+'&nbsp;&nbsp;<i>'+item.module_price+'元</i></span></div>';
    return _li;
}

function judgeAppStatus(str,parameter1) {
	if (Date.parse(parameter1) == Date.parse(getDateStr(new Date()))) return '<div class="media-right media-middle btn_remove"> <button class="btn btn-default btn-enabled">激活</button></div>';
	else if (str == '007005') return '<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled query_details" style="margin-top: 32px;" data-toggle="modal" data-target="#myModal">查看详情</button> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled update_status" style="margin-top: 32px;" app_status="007010">通过</button> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<label style="font-size:18px;color:#1cbb9e;line-height:28px;text-align:right;width: 90px;">新学校申请</label>'
        +'<button class="btn btn-default btn-online update_status" app_status="007015">拒绝</button></div>';
	else if (str == '007010') return ' <div class="media-right media-middle btn_remove">'
        +'<label style="color: #ff8200;">生成中</label></div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled is-upper_lower_line" app_status="007025">上线</button> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled renew">续费</button> </div>';
	else if (str == '007015'|| str == '007020') return ' <div class="media-right media-middle btn_remove">'
        +'<label style="color: #f60e0e;width: 150px;">已拒绝：资料不全</label> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled query_details" data-toggle="modal" data-target="#myModal">查看详情</button> </div>';
	else if (str == '007025') return ' <div class="media-right media-middle btn_remove">'
        +'<label style="color: #4a4a4a;">已上线</label> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-online is-upper_lower_line" app_status="007030">下线</button> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled renew">续费</button></div>';
	else if (str == '007030') return ' <div class="media-right media-middle btn_remove">'
        +'<label style="color: #9b9b9b;">已下线</label> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled is-upper_lower_line" app_status="007025">上线</button> </div>'
        +'<div class="media-right media-middle btn_remove">'
        +'<button class="btn btn-default btn-enabled renew">续费</button> </div>';
}

function judgeAppStatusAppend(obj) {
    var str = obj.attr('app_status');
    var className = 'btn_remove';
    if (obj.parent().hasClass('btn_remove')) className = 'btn_remove_old';
    else className = 'btn_remove';
    if (str == '007005') obj.parent().parent().append( '<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled query_details" style="margin-top: 32px;" data-toggle="modal" data-target="#myModal">查看详情</button> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled update_status" style="margin-top: 32px;" app_status="007010">通过</button> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<label style="font-size:18px;color:#1cbb9e;line-height:28px;text-align:right;width: 90px;">新学校申请</label>'
        +'<button class="btn btn-default btn-online update_status" app_status="007015">拒绝</button></div>');
    else if (str == '007010') obj.parent().parent().append( ' <div class="media-right media-middle '+className+'">'
        +'<label style="color: #ff8200;">生成中</label></div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled is-upper_lower_line" app_status="007025">上线</button> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled renew">续费</button> </div>');
    else if (str == '007015'|| str == '007020') obj.parent().parent().append( ' <div class="media-right media-middle '+className+'">'
        +'<label style="color: #f60e0e;width: 150px;">已拒绝：资料不全</label> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled query_details" data-toggle="modal" data-target="#myModal">查看详情</button> </div>');
    else if (str == '007025') obj.parent().parent().append( ' <div class="media-right media-middle '+className+'">'
        +'<label style="color: #4a4a4a;">已上线</label> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-online is-upper_lower_line" app_status="007030">下线</button> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled renew">续费</button></div>');
    else if (str == '007030')obj.parent().parent().append( ' <div class="media-right media-middle '+className+'">'
        +'<label style="color: #9b9b9b;">已下线</label> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled is-upper_lower_line" app_status="007025">上线</button> </div>'
        +'<div class="media-right media-middle '+className+'">'
        +'<button class="btn btn-default btn-enabled renew">续费</button> </div>');
    if (obj.parent().hasClass('btn_remove')) obj.parent().parent().children('.btn_remove').remove();
    else obj.parent().parent().children('.btn_remove_old').remove();
    bindUpdateStatusClickEvent();
    bindQueryDetailsClickEvent();
    bindIsUpperLowerLineClickEvent();
    bindRenewClickEvent();
}

function judgeSchoolType(str) {
    if (str == '002005') return '幼儿园';
    else if (str == '002010') return '小学';
    else if (str == '002015') return '初中';
    else if (str == '002020') return '高中';
    else if (str == '002020') return '大学';
    else  return '培训班';
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

function bindMessage(content,title) {
    $('.subtitle').empty().append(title);
    $('#error_content').empty().append(content);
    $('#btn_error').click();
}

function isNotEmpty(str) {
    return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.length !=0;
}

function isEmpty(str) {
    return str === 'null'|| str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'|| str.length ==0;
}

function isDateEmpty(date) {
    var date_length = 0;
    var length = 0;
    for(var s in date){
        if (isEmpty(date[s])) length++;
        date_length ++;
    }
    if (date_length == length) return true;
    else return false;
}

function isEnableText(is_enable) {
    if (is_enable == '001015') return "启用";
    else return "禁用";
}

function IntegerSum(str1,str2) {
    return (parseInt(str1) + parseInt(str2));
}

function isInteger(obj) {
    return obj%3 === 0;
}

function IntegerDivision(obj) {
    return obj%3;
}

function IntegerReduce(str1,str2) {
    return (parseInt(str1) - parseInt(str2));
}

function isEmptyObject(e) {
    var t;
    for (t in e)
        return !1;
    return !0
}


function toastrMessage(content) {
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
    toastr.info(content,"提示！");
}