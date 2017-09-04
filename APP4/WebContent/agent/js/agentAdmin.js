var storage = window.localStorage;
var currentPage = 1;
var limit = 10;
var storeMap = {};
var module_names = {};
var module_price = 0;
var moduleMap = '';

function initServerConfig(){
    $.myajax({
        url:'systemAction/getServerConfigList',
        datatype:'json',
        success:function(data){
            var result = data.result.data;
            storeMap["config_list"] = result;
            var _memory = '<option>内存</option>';
            var _disk = '<option>硬盘</option>';
            var _bandwidth = '<option>带宽</option>';
            for(var i in result){
                var item = result[i];
                if (i < 3) {
                    $('#bandwidth'+IntegerSum(i,1)+'').empty().append(''+item.bandwidth+'M');
                    $('#radio'+IntegerSum(i,1)+'').attr('bandwidth_id',item.server_config_id);
                    $('#disk'+IntegerSum(i,1)+'').empty().append(''+item.disk+'G');
                    $('#radio'+IntegerSum(i,1)+'').attr('disk_id',item.server_config_id);
                    $('#memory'+IntegerSum(i,1)+'').empty().append(''+item.memory+'G');
                    $('#radio'+IntegerSum(i,1)+'').attr('memory_id',item.server_config_id);
                }
                _memory += ' <option value="'+item.server_config_id+'">'+item.memory+'G</option>';
                _disk += ' <option value="'+item.server_config_id+'">'+item.disk+'G</option>';
                _bandwidth += ' <option value="'+item.server_config_id+'">'+item.bandwidth+'M</option>';
            }
            $('.memory_list').empty().append(_memory);
            $('.disk_list').empty().append(_disk);
            $('.bandwidth_list').empty().append(_bandwidth);
        }
    });
}

function bindCoustomPriceChangeEvent() {
    $('.custom_price').on('change',function () {
        var hid = $('.disk_list option:selected').val();
        var mid = $('.memory_list option:selected').val();
        var bid = $('.bandwidth_list option:selected').val();
        var bprice = 0;
        var hprice = 0;
        var mprice = 0;
        var disk = '';
        var memory = '';
        var bandwidth = '';
        for (var i in storeMap["config_list"]) {
            if (storeMap["config_list"][i].server_config_id == hid) {
                hprice = storeMap["config_list"][i].disk_price;
                disk =  storeMap["config_list"][i].disk;
            }
            if (storeMap["config_list"][i].server_config_id == mid) {
                mprice = storeMap["config_list"][i].memory_price;
                memory =  storeMap["config_list"][i].memory;
            }
            if (storeMap["config_list"][i].server_config_id == bid) {
                bprice = storeMap["config_list"][i].bandwidth_price;
                bandwidth =  storeMap["config_list"][i].bandwidth;
            }
        }
        $('#radio4').val('[{"disk_id":'+hid+',"disk":'+disk+',"disk_price":'+hprice+',"memory_id":'+mid+',"memory":'+memory+',"memory_price":'+mprice+',"bandwidth_id":'+bid+',"bandwidth":'+bandwidth+',"bandwidth_price":'+bprice+'}]');
        $('#custom_price').empty().append(IntegerSum(IntegerSum(hprice,mprice),bprice));
    });
}
function bindCalculationPrice() {
    for (var i = 0 ; i < 3; i++){
        var mprice = storeMap["config_list"][i].memory_price;
        var hprice = storeMap["config_list"][i].disk_price;
        var bprice = storeMap["config_list"][i].bandwidth_price;
        var id = storeMap["config_list"][i].server_config_id;
        var disk = storeMap["config_list"][i].disk;
        var memory = storeMap["config_list"][i].memory;
        var bandwidth = storeMap["config_list"][i].bandwidth;
        $('#server_price'+IntegerSum(i,1)+'').empty().append(IntegerSum(IntegerSum(mprice,hprice),bprice));
        $('#radio'+IntegerSum(i,1)+'').val('[{"disk_id":'+id+',"disk":'+disk+',"disk_price":'+hprice+',"memory_id":'+
            id+',"memory":'+memory+',"memory_price":'+mprice+',"bandwidth_id":'+id+
            ',"bandwidth":'+bandwidth+',"bandwidth_price":'+bprice+'}]');
    }
}

function initAgentBalance() {
    $.myajax({
        url : 'balanceAction/getBalanceByPhone',
        data:{phone:localStorage.getItem('phone')},
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var result = data.result.data;
            var balance = result.balance;
            $('#Balance').empty().append(balance);
            $('#Balance').attr('balance',balance);
            $('#Balance').attr('version',result.version);
        }
    });
}

//加载模板列表
function loadAppModuleClassIficationList(){
    $.myajax({
        url:'templateAction/getModuleList?parent_code=009',
        datatype:'json',
        success:function(data){
            var result = data.result.data;
            storeMap["class_ification"] = result;
        }
    });
}

//初始化代理商申请学校列表
function initSchoolList() {
    $('.school_list').empty();
    $.myajax({
        url:'schoolAction/getAgentApplySchoolList',
        data:{agent_phone:localStorage.getItem('phone'),start_id:(currentPage-1)*limit,limit:limit,page:currentPage,order_sql:'desc'},
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
                    initSchoolList();
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
    $('#recharge').attr("agent_id",result[0].agent_id);
    for ( var i in result) {
        var item = result[i];
        var organize_pic_url
        if (isEmpty(item.organize_pic_url)) organize_pic_url = 'images/gzh_wx.jpg';
        else organize_pic_url = item.organize_pic_url;
        _li += '<li school_type="'+item.school_type+'" school_id="'+item.school_id+'" agent_id="'+item.agent_id+'" main_url="'+item.main_url+'"><img src="'+organize_pic_url+'" alt="" />'
            +'<span class="time">'+item.school_name+'<i>申请时间：'+getDateStr(item.create_date)+'</i></span>'
            +judgeAppStatus(item.status,item.valid_date,item.version)+'</li>';
    }
    $('.school_list').append(_li);
    initButtonClickEvent();
}

function initButtonClickEvent() {
    bindRecoverClickEvent();//绑定编辑事件
    bindRecoveryClickEvent();//绑定恢复事件
    bindRemoveCompletelyClickEvent();//绑定删除事件
}

function bindRemoveCompletelyClickEvent() {
    $('.Remove_completely').on('click',function () {
        var school_id = $(this).parent().parent().attr('school_id');
        $.myajax({
            url:'schoolAction/removeSchool',
            data:{school_id:school_id},
            datatype:'json',
            type: 'POST',
            success:function(data){
                bindMessage('该学校删除成功...','删除提示！');
                initSchoolList();
            }
        });
    })
}

function bindRecoveryClickEvent() {
    $('.recovery').on('click',function () {
        var school_id = $(this).parent().parent().attr('school_id');
        judgeAppStatusAppend($(this));
        $.myajax({
            url:'schoolAction/updateSchool',
            data:{school_id:school_id,status:$(this).attr('app_status')},
            datatype:'json',
            type: 'POST',
            success:function(data){
                bindMessage('操作成功...','信息提示！');
            }
        });
    });
}

function bindRechargeClickEvent() {
    $('#recharge').on('click',function () {
        var _content = '<div><div>输入充值金额:<input class="form-control judgeNumber" type="number" min="0" onpaste="return false;" id="btn_balance" placeholder="￥'+$('#Balance').text()+'" style=" margin-top: 15px;" min="0"/></div>' +
            '<button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn-info" id="DetermineRecharge">确定</button><button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn_Close" data-dismiss="modal">关闭</button></div>';
        bindMessage(_content,'账户充值');
        bindDetermineRechargeClickEvent();
        judgeNumber();
    });
}

function judgeNumber() {
    $('.judgeNumber').keyup(function () {
        if(! /\d+(\.\d+)?/.test($(this).val())){$(this).val('');}
    });
}


function bindDetermineRechargeClickEvent() {
    $('#DetermineRecharge').on('click',function () {
        if (isEmpty($('#btn_balance').val()) || judgeSign($('#btn_balance').val())) {toastrMassage('请输入正确的收费金额...');return false;}
        var consumption_money = $('#btn_balance').val();
        var  agent_id = $('#recharge').attr("agent_id");
        var _content = '<div><div>跳转成功，是/否成功充值......</div>' +
            '<button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn-info" data-dismiss="modal" id="Yes" agent_id="'+$('#recharge').attr("agent_id")+'">是</button><button style="margin-right: 15px;margin-top: 5px;" type="button" class="btn btn-white btn_Close" data-dismiss="modal">否</button></div>';
        $('#error_content').empty().append(_content);
        bindYesClickEvent();
        window.open("balanceAction/webPayment?consumption_money="+ consumption_money +"&agent_id="+ agent_id +"");
    });
}

function bindYesClickEvent() {
    $('#myModal2').on('click',function () {
        if (isEmpty($('#Yes').attr("agent_id"))) return;
        $.myajax({
            url:'balanceAction/getBalanceByID',
            data:{agent_id:$('#Yes').attr("agent_id")},
            datatype:'json',
            type: 'POST',
            success:function(data){
                var result = data.result.data;
                $('#Balance').attr("balance",result.balance);
                $('#Balance').empty().append(result.balance);
            }
        });
    });
}

function bindSubmitClickEvent() {
    $('#btn_submit').on('click',function () {
        var schoolVO = {};
        schoolVO.school_id = $(this).attr('school_id');
        schoolVO.school_type = $(this).attr('school_type');
        schoolVO.module_ids = checkSelectedModuleInfo();
        schoolVO.school_server = $('input:radio[name="radio4"]:checked').val();
        schoolVO.main_url = $('#MainUrl').val();
        $.myajax({
            url:'schoolApplyAction/addSchool',
            data:schoolVO,
            datatype:'json',
            type: 'POST',
            success:function(data){
                bindMessage('该学校修改学校成功...','信息提示！');
            }
        });
    });
}

function bindRecoverClickEvent() {
    $('.recover').on('click',function () {
        bindCalculationPrice();
        module_price = 0;
        var school_type = $(this).parent().parent().attr('school_type');
        var school_id = $(this).parent().parent().attr('school_id');
        if (isNotEmpty($(this).parent().parent().attr('main_url'))) {
            $('#yesMainUrl').prop('checked',true);
            $('#noMainUrl').prop('checked',false);
            $('#MainUrl').show();
            $('#MainUrl').val($(this).parent().parent().attr('main_url'));
        } else {
            $('#MainUrl').val('');
            $('#MainUrl').hide();
            $('#noMainUrl').prop('checked',true);
            $('#yesMainUrl').prop('checked',false);
        }
        $('#btn_submit').attr('school_id',school_id);
        $('#btn_submit').attr('school_type',school_type);
        loadSchoolServerConfig(school_id);
        loadSchoolAppTemplateList(school_type,school_id);
        loadAppTemplateList(school_type,school_id);
    });
}

function loadSchoolServerConfig(school_id) {
    $.myajax({
        url:'schoolAction/getSchoolServerConfig',
        data:{school_id:school_id},
        datatype:'json',
        success:function(data){
            var result = data.result.data;
            if (isEmpty(result)) {
                $('#current_memory').empty().append('当前学校没有配置服务器，请配置...');
                $('#current_server_price').parent().hide();
                return;
            }
            var current_price = IntegerSum(IntegerSum(result.memory_price,result.disk_price),result.bandwidth_price);
            $('#current_radio').click();
            $('#current_memory').empty().append(result.memory+"G");
            $('#current_hard_disk').empty().append(result.disk+"G");
            $('#current_bandwidth').empty().append(result.bandwidth+"M");
            $('#current_server_price').empty().append(current_price);
        }
    });
}

//加载模板列表
function loadAppTemplateList(school_type,school_id){
    $.myajax({
        url:'templateAction/getMandatoryTemplateList',
        data:{school_type:school_type},
        datatype:'json',
        success:function(data){
            var result = data.result.data;
            for(var i in result){
                var item = result[i];
                storeMap["module_list"] = item.module_list;
                loadAppModuleList(school_type,school_id);
            }
        }
    });
}

//加载模板列表
function loadSchoolAppTemplateList(school_type,school_id){
    $.myajax({
        url:'moduleAction/getSchoolModuleBasicsList',
        data:{school_type:school_type,is_inactive:0,school_id:school_id},
        datatype:'json',
        success:function(data){
            var result = data.result.data;
            for(var i in result){
                var item = result[i];
                storeMap[item.module_id] = item;
            }
        }
    });
}

//加载模板列表
function loadAppModuleList(school_type,school_id){
    $.myajax({
        url:'templateAction/getModuleBasicsList',
        data:{school_type:school_type},
        datatype:'json',
        success:function(data){
            $('#model').empty();
            var result = data.result.data;
            var _li = '<h2>1.功能模块&nbsp&nbsp&nbsp收费模块：¥<span id="set_module_price"></span>元／年</h2>';
            for(var j in storeMap["class_ification"]) {
                var ification = storeMap["class_ification"][j];
                _li += '<div class="model1"><h3>'+ification.module_name+'</h3><div class="row">';
                for(var i in result) {
                    var item = result[i];
                    if (ification.module_code == item.parent_code) _li += setDisplayModuleHtml(item);
                }
                _li += '</div></div>';
            }
            $('#model').append(_li);
            $('#set_module_price').append(CalculationTotalAmount());
            bindInputCheckedClickEvent();
            bindShowModuleIntroduceClickEvent();
            bindChoiceAggregateClickEvent();
        }
    });
}

function bindInputCheckedClickEvent() {
    $('.btn_module').click(function () {
        if ($(this).attr('is_must') == 1) return false;
        var price = Intercept($(this).parent().find('i').text(),0,'元');
        if ($(this).is(':checked')) module_price = IntegerSum(module_price,price);
        else module_price = IntegerReduce(module_price,price);
        $('#set_module_price').empty().append(module_price);
    });
}

function bindShowModuleIntroduceClickEvent() {
    $('.module_introduce').on('click',function () {
        bindMessage($(this).attr('introduce'),"模块介绍");
    });
}

function bindChoiceAggregateClickEvent() {
    $('.Choice-aggregate').on('click',function () {
        if ($(this).hasClass('Selected'))  $(this).removeClass('Selected')
        else $(this).addClass('Selected')
    });
}

function bindYesMainUrlClickEvent() {
    $('#yesMainUrl').unbind('click').on('click',function () {
        $('#MainUrl').show();
    });
}

function bindNooMainUrlClickEvent() {
    $('#noMainUrl').unbind('click').on('click',function () {
        $('#MainUrl').hide();
    });
}
//功能型代码区域
//转换日期格式
//检查设置模块信息
function checkSelectedModuleInfo() {
    var module_ids;
    $('.Selected').each(function(){
        if (isNotEmpty(module_ids)){
            module_ids = module_ids+','+$(this).attr('module_id');
        }else{
            module_ids = $(this).attr('module_id');
        }
    });
    return module_ids;
}

function CalculationTotalAmount() {
    $('.btn_module').each(function () {
        if($(this).prop('checked')) module_price = IntegerSum(module_price,$(this).attr('module_price'));
    });
    return module_price;
}

function setDisplayModuleHtml(item) {
    var _li = '';
    var data = {};
    module_names[item.module_id] = item.module_name;
    data.module_name =isNotEmpty(storeMap[item.module_id])?storeMap[item.module_id].module_name:item.module_name;
    data.module_id = item.module_id;
    data.module_price = item.module_price;
    data.checkedValue = '';
    data.is_must = 0;
    data.click_css = 'Choice-aggregate';
    if (isNotEmpty(storeMap["module_list"])) {
        for (var n in storeMap["module_list"]) {
            if (isNotEmpty(_li)) return _li;
            var module = storeMap["module_list"][n];
            if (item.module_code == module.module_code ) {
                if (item.module_code == "009025" && item.user_type == "003015") {
                    data.checked = "";
                } else {
                    data.checked = "checked";
                    data.checkedValue = 'onclick="return false;"';
                    data.click_css = '';
                    data.is_must = 1;
                }
            }
        }
    } else data.checked = item.is_must == 1?"checked":' ';
    if (isNotEmpty(storeMap[item.module_id])) data.checked = "checked";
    _li += '<div class="checkbox checkbox-success col-md-6" style=" margin-bottom: 10px;">'
        +'<input class="styled btn_module '+data.click_css+'" '+data.checkedValue+' module_id="'+data.module_id+'" is_must="'+data.is_must+'" module_price="'+data.module_price+'" type="checkbox" '+data.checked+'>'
        +'<label for="checkbox4"></label><span class="price">'+data.module_name+'&nbsp;&nbsp;<i>'+data.module_price+'元</i></span>'
        +'<a href="javascript:;" class="module_introduce" introduce="'+item.introduce+'">模块介绍</a></div>';
    return _li;
}

function judgeAppStatus(str,parameter1,parameter2) {
    if (str == '007005'&& isEmpty(parameter1) && Date.parse(parameter1) == Date.parse(getDateStr(new Date()))) return '<span class="pasted btn_remove">已过期<i>过期时间：'+getDateStr(parameter1)+'</i></span>';
    else if (str == '007005' && parameter2 > 0 && isNotEmpty(parameter2)) return '<span class="state">更新审核中</span>';
    else if (str == '007005') return '<span class="state btn_remove">待审核</span>';
    else if (str == '007010') return '<span class="state btn_remove">生成中</span>';
    else if (str == '007015') return '<span class="buttons btn_remove"><span class="refuse">被拒绝：资料不全</span> '
        +'<input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/>'
        +'<input type="button" class="btn btn-default recovery" app_status="007005" value="重新提交"/>'
        +'<input type="button" class="btn btn-default del recovery" app_status="007020" value="删除"/></span>';
    else if (str == '007025') return '<span class="online btn_remove">已上线 <input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/></span>';
    else if (str == '007030') return '<span class="online btn_remove">已下线 </span>';
    else if (str == '007020') return '<span class="buttons btn_remove"><input type="button" class="btn btn-default recovery" app_status="007015" value="恢复"/>'
        +'<input type="button" class="btn btn-default del Remove_completely" value="彻底删除"/></span>';
}

function judgeAppStatusAppend(obj) {
    var str = obj.attr('app_status');
    var className = 'btn_remove';
    if (obj.parent().hasClass('btn_remove')) className = 'btn_remove_old';
    else className = 'btn_remove';
    if (str == '007005') obj.parent().parent().append( '<span class="state '+className+'">待审核</span>');
    else if (str == '007010') obj.parent().parent().append( '<span class="state '+className+'">生成中</span>');
    else if (str == '007015') obj.parent().parent().append( '<span class="buttons '+className+'"><span class="refuse">被拒绝：资料不全</span> '
        +'<input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/>'
        +'<input type="button" class="btn btn-default recovery" app_status="007005" value="重新提交"/>'
        +'<input type="button" class="btn btn-default del recovery" app_status="007020" value="删除"/></span>');
    else if (str == '007025') obj.parent().parent().append( '<span class="online '+className+'">已上线 <input type="button" class="btn btn-default recover" data-toggle="modal" data-target="#myModal" value="编辑"/></span>');
    else if (str == '007030') obj.parent().parent().append( '<span class="online '+className+'">已下线 </span>');
    else if (str == '007020') obj.parent().parent().append( '<span class="buttons '+className+'"><input type="button" class="btn btn-default recovery" app_status="007015" value="恢复"/>'
        +'<input type="button" class="btn btn-default del Remove_completely" value="彻底删除"/></span>');
    if (obj.parent().hasClass('btn_remove')) obj.parent().parent().children('.btn_remove').remove();
    else obj.parent().parent().children('.btn_remove_old').remove();
    initButtonClickEvent();
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


function IntegerReduce(str1,str2) {
    return (parseInt(str1) - parseInt(str2));
}

function Intercept(str,status,end){
    return str.substring(status,str.indexOf(end));
}

function judgeSign(num) {
    var reg = new RegExp("^-?[0-9]*.?[0-9]*$");
    if ( reg.test(num) ) {
        var absVal = Math.abs(num);
        return num==absVal?false:true;
    } else {
        return true;
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
    toastr.info(content,"提示");
}