var storage = window.localStorage;
var currentPage = 1;
var limit = 5;
var start_id= 0;
var _select = '';

// 关键信息搜索
function bindSearchButtonClickEvent() {
	$('.search-button').on('click', function() {
		bindGetAgentList($('.search').val(),$('#region-list').val());
	});
}
// 用回车键 触发搜索按钮.==13代表 键盘Q起 第13个按键4
$("body").keyup(function() {
	if (event.which == 13) {
		$(".search-button").trigger("click");
	}
});

// 关键信息搜索
function bindRegionButtonClickEvent() {
    $('#region-list').on('change', function() {
        bindGetAgentList($('.search').val(),$('#region-list').val());
    });
}
function initRegionList() {
    $.myajax({
        url : 'dictAction/getProvinceList',
        datatype : 'json',
        type : 'post',
        success : function(data) {
            $('#region-list').empty();
            var result = data.result.data;
            var _option = '<option value="">全部</option>';
            for ( var i in result) {
                var item = result[i];
                _option +=  '<option value="'+item.province_id+'">'+item.province_name+'</option>';
            }
            _select = _option;
            $('#region-list').append(_option);
            $('.set_region').empty().append(_option);
        }
    });
}
// 加载内容列表
function bindGetAgentList(search,region_code) {
	$.myajax({
		url : 'agentAction/getAgentBasicList',
		data : {
			search : search,
            region_code:region_code,
			start_id : (currentPage - 1) * limit,
			limit : limit,
			page : currentPage
		},
		datatype : 'json',
		type : 'post',
		success : function(data) {
			var result = data.result;
			var pageCount = Math.ceil(result.total / limit); // 取到pageCount的值(把返回数据转成object类型)
			addToWeb(data);
			if (pageCount < 2) {
				$("#page_pagintor").hide();
				return;
			}
			var options = {
				bootstrapMajorVersion : 3, // 版本
				currentPage : currentPage, // 当前页数
				totalPages : pageCount, // 总页数
				alignment : "center",
				itemTexts : function(type, page, current) {
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
				},// 点击事件，用于通过Ajax来刷新整个list列表
				onPageClicked : function(event, originalEvent, type, page) {
					currentPage = page;
					bindGetAgentList(search,region_code);
				}
			};
			$("#page_pagintor").bootstrapPaginator(options);
			$("#page_pagintor").show();
		}
	});
}

// 加载文章列表
function addToWeb(data) {
    $('.zjq-detail.fl').empty();
    $('.zjq-buttons.fr').empty();
	var _li = "";
	var result = data.result.data;
	for ( var i in result) {
		var item = result[i];
		var regist_date = getDateStr(item.regist_date);
		var valid_date = getDateStr(item.valid_date);
        _li += '<div class="zjq-message">'+IntegerSum(i,1)+':<div class="zjq-sure-m clearfix edit_agent_'+item.agent_id+'">'
            +' <div class="col-sm-6 col-lg-6 fl"><ul><li>姓名：&nbsp<span class="agent_name_'+item.agent_id+'">'+item.agent_name+'</span></li>'
        +'<li>区域：&nbsp<span class="region_'+item.agent_id+'" data-region_code="'+item.region_code+'">'+item.region+'</span></li><li>注册时间：&nbsp<span class="regist_date_'+item.agent_id+'">'+regist_date+'</span></li>'
        +'<li class="password'+item.agent_id+'">密码：&nbsp* * * * * * <input type="button" agent_id="'+item.agent_id+'" class="btn btn-default button btn_reset btn_reset_'+item.agent_id+'" password="'+item.pass_word+'" data-phone="'+item.phone+'" value="重置">'
		+'</li></ul></div><div class="col-sm-6 col-lg-6 fr"><ul><li>手机号：&nbsp<span class="phone_'+item.agent_id+'">'+item.phone+'</span></li>'
        +'<li>学校单价：&nbsp￥<span class="unit_price_'+item.agent_id+'">'+item.unit_price+'</span></li><li>有效期：&nbsp<span class="valid_date_'+item.agent_id+'">'+valid_date+'</span></li>'
        +'<li class="recharge_'+item.agent_id+'" data-balance="'+item.balance+'">余额：&nbsp￥<span class="balance_'+item.agent_id+'">'+item.balance+'</span><input type="button" class="btn btn-default button btn_recharge" agent_id="'+item.agent_id+'" data-phone="'+item.phone+'" data-balance="'+item.balance+'" data-version="'+item.version+'" data-user_id="'+item.user_id+'" value="充值">'
		+'</li></ul></div></div> </div>';
        $('.zjq-buttons.fr').append('<div class="zjq-button"> <div class="form-group">'
            +'<input type="button" class="btn btn-default edit edit_'+item.agent_id+' judgeEdit" data-version="'+item.version+'" data-user_id="'+item.user_id+'"  data-agent_id="'+item.agent_id+'" data-phone="'+item.phone+'" value="编辑"></div>'
            +'<div class="form-group update_btn'+item.agent_id+'"><input type="button" class="btn btn-default btn_is_enable disable disable_'+item.agent_id+'" data-is_enable="'+item.is_enable+'" data-agent_id="'+item.agent_id+'" data-phone="'+item.phone+'" value="'+isEnableText(item.is_enable)+'"></div></div>');
	}
    $('.zjq-detail.fl').append(_li);
    bindAgentRechargeClickEvent();
    bindResetClickEvent();
    bindAgentEditClickEvent();
    bindAgentIsEnableClickEvent();
}

function bindAgentIsEnableClickEvent() {
    $('.btn_is_enable').unbind('click').on('click',function () {
        if ($(this).attr('data-is_enable') == '001015' && $(this).hasClass('disable')) return;
        var is_enable = $(this).attr('data-is_enable') =='001005'?'001015':'001005';
        var text = is_enable == '001015'?"禁用":"启用";
        $('.update_btn'+$(this).attr('data-agent_id')+'').empty()
            .append('<input type="button" class="btn btn-default btn_is_enable disable_'+$(this).attr('data-agent_id')+'" data-is_enable="'+is_enable+'" data-agent_id="'+$(this).attr('data-agent_id')+'" data-phone="'+$(this).attr('data-phone')+'" value="'+isEnableText(is_enable)+'">');
        $.myajax({
            url : 'agentAction/disableAgentAccount',
            data :{agent_id:$(this).attr('data-agent_id'),is_enable: is_enable},
            datatype : 'json',
            type : 'post',
            success : function(data) {
                $('.update_btn'+$(this).attr('data-agent_id')+'').empty()
                    .append('<input type="button" class="btn btn-default btn_is_enable disable_'+$(this).attr('data-agent_id')+'" data-is_enable="'+is_enable+'" data-agent_id="'+$(this).attr('data-agent_id')+'" data-phone="'+$(this).attr('data-phone')+'" value="'+isEnableText(is_enable)+'">');
                bindMessage("您已经"+text+"这个代理商账户!","修改成功！");
            }
        });
        bindAgentIsEnableClickEvent();
    });
}

function bindAgentEditClickEvent() {
	$('.edit').unbind('click').on('click',function (e) {
	    if (judgeEdit()){toastrMassage("请先编辑当前代理商！");return;}
        var agent_id = $(this).attr('data-agent_id');
        $('.edit_'+agent_id+'').addClass('disable');
        $('.edit_'+agent_id+'').removeClass('edit');
        $('.disable_'+agent_id+'').removeClass("disable ");
        var user_id = $(this).attr('data-user_id');
		var obj = $('.edit_agent_'+agent_id+'');
		var agent_name = $('.agent_name_'+agent_id+'').text();
        var region = $('.region_'+agent_id+'').text();
        var region_code =  $('.region_'+agent_id+'').attr('data-region_code');
        var regist_date = $('.regist_date_'+agent_id+'').text();
        var password = $('.btn_reset_'+agent_id+'').attr('password');
        var phone = $('.phone_'+agent_id+'').text();
        var unit_price = $('.unit_price_'+agent_id+'').text();
        var valid_date = $('.valid_date_'+agent_id+'').text();
        var balance = $('.recharge_'+agent_id+'').attr('data-balance');
        var version = $(this).attr('data-version');
        obj.empty();
		obj.append('<div class="col-sm-6 col-lg-6 fl">'
            +'<div class="form-group"><label for="label1" class="col-sm-2 col-lg-2 control-label lable-width">姓名</label>'
            +'<div class="col-sm-10 col-lg-10 zjq-input input-width"><input type="text" class="form-control update_agent_name" agent_name="'+agent_name+'" placeholder="'+agent_name+'" id="label1" value="">'
            +'</div></div><div class="form-group"><label for="label2" class="col-sm-2 control-label lable-width">区域</label>'
            +'<div class="col-sm-10 zjq-input input-width"><select id="select_region" class="update_region select-css" data-region_code="'+region_code+'" data-region="'+region+'">'+_select+'</select>'
            +'</div></div><div class="form-group"><label for="label3" class="col-sm-4 control-label lable-width ">注册时间</label>'
            +'<div class="col-sm-8 zjq-input input-width"><input type="text" class="form-control update_regist_date laydate" regist_date="'+regist_date+'" placeholder="'+regist_date+'" id="label3" value="">'
            +'</div></div><div class="form-group"><label for="label4" class="col-sm-2 control-label lable-width">密码</label>'
            +'<div class="col-sm-6 zjq-input input-width"><input type="password" class="form-control update_password " password="'+password+'" id="label4" value="" placeholder="**********">'
            +'<input type="button" class="btn btn-default button button_password button-css" password="'+password+'" data-agent_id="'+agent_id+'" data-phone="'+phone+'" value="重置"> </div></div></div>'
            +'<div class="col-sm-6 col-lg-6 fr"><div class="form-group"><label for="label5" class="col-sm-3 control-label lable-width">手机号</label>'
            +'<div class="col-sm-9 zjq-input input-width"><input type="text" class="form-control update_phone " phone="'+phone+'" placeholder="'+phone+'" id="label5" value="">'
            +'</div></div><div class="form-group"><label for="label6" class="col-sm-4 control-label lable-width">学校单价</label>'
            +'<div class="col-sm-8 zjq-input input-width"><input type="text" class="form-control update_unit_price " unit_price="'+unit_price+'" placeholder="￥'+unit_price+'" id="label6" value="">'
            +'</div></div><div class="form-group"><label for="label7" class="col-sm-3 control-label lable-width">有效期</label>'
            +'<div class="col-sm-9 zjq-input input-width"><input type="text" class="form-control laydate update_valid_date" valid_date="'+valid_date+'" id="label7" placeholder="'+valid_date+'" value=""></div>'
            +'</div><div class="form-group"><label for="label4" class="col-sm-2 control-label lable-width">余额</label>'
            +'<div class="col-sm-6 zjq-input input-width"><input type="number" class="form-control update_balance judgeNumber" min="0" onpaste="return false;" balance="'+balance+'" id="label4" placeholder="¥'+balance+'">'
            +'<input type="button" class="btn btn-default button button_recharge_balance button-css" data-user_id="'+user_id+'" data-agent_id="'+agent_id+'" value="充值">' +
			'</div></div></div>	<input type="submit" class="btn btn-default zjq-submit" data-user_id="'+user_id+'" data-agent_id="'+agent_id+'" data-version="'+version+'" value="完成" style="margin-left: 18%;display: inline;">');
        bindSubmitClickEvent();
        bindUpdatePasswordClickEvent();
        bindUpdateRechargeBalanceClickEvent();
        bindLaydateClickEvent();
        judgeNumber();
	});
}

function bindLaydateClickEvent() {
    $('.laydate').on('click',function(e){
        laydate(e);
    });
}

function judgeEdit() {
    var  ret = false;
    $('.judgeEdit').each(function () {
        if ($(this).hasClass('disable')) ret = true;
    })
    return ret;
}


function bindSubmitClickEvent() {
	$('.zjq-submit').unbind('click').on('click',function () {
        var item = {};
        item.agent_name = $('.update_agent_name').val();
        item.region_code =$(".update_region option:selected").val();
        item.region = isNotEmpty($(".update_region option:selected").val())?$(".update_region option:selected").text():'';
        item.regist_date = $('.update_regist_date').val();
        item.password = $('.update_password').val();
        item.phone = $('.update_phone').val();
        item.unit_price = $('.update_unit_price').val();
        item.valid_date = $('.update_valid_date').val();
        item.balance = $('.update_balance').val();
        var data = {};
        data.agent_id = $(this).attr('data-agent_id');
        data.version = $(this).attr('data-version');
        data.user_id = $(this).attr('data-user_id');
        data.agent_name = $('.update_agent_name').attr('agent_name');
        data.region = $('.update_region').attr('data-region');
        data.region_code = $('.update_region').attr('data-region_code');
        data.regist_date = $('.update_regist_date').attr('regist_date');
        data.password = $('.update_password').attr('password');
        data.phone = $('.update_phone').attr('phone');
        data.unit_price = $('.update_unit_price').attr('unit_price');
        data.valid_date = $('.update_valid_date').attr('valid_date');
        data.balance = $('.update_balance').attr('balance');
        if(bindUpdateAgent(item,data)) return;
        bindUpdateAgentWeb(item,data);
    });
}

function bindUpdateAgentWeb(item,data) {
    var agent_id = data.agent_id;
    var version = data.version;
    var user_id = data.user_id;
    $('.edit_agent_'+agent_id+'').empty();
    var agent_name = isNotEmpty(item.agent_name)?item.agent_name:data.agent_name;
    var region = isNotEmpty(item.region)?item.region:data.region;
    var region_code = isNotEmpty(item.region_code)?item.region_code:data.region_code;
    var regist_date = isNotEmpty(item.regist_date)?item.regist_date:data.regist_date;
    var phone = isNotEmpty(item.phone)?item.phone:data.phone;
    var unit_price = isNotEmpty(item.unit_price)?item.unit_price:data.unit_price;
    var valid_date = isNotEmpty(item.valid_date)?item.valid_date:data.valid_date;
    var balance = isNotEmpty(item.balance)?IntegerSum(item.balance,data.balance):data.balance;
    var pass_word = isNotEmpty(item.password)?item.password:data.password;
    var _li = '<div class="col-sm-6 col-lg-6 fl"><ul><li>姓名：&nbsp<span class="agent_name_'+agent_id+'">'+agent_name+'</span></li>'
        +'<li>区域：&nbsp<span class="region_'+agent_id+'" data-region_code="'+region_code+'">'+region+'</span></li><li>注册时间：&nbsp<span class="regist_date_'+agent_id+'">'+regist_date+'</span></li>'
        +'<li class="password'+agent_id+'">密码：&nbsp* * * * * * <input type="button" agent_id="'+agent_id+'" class="btn btn-default button btn_reset btn_reset_'+agent_id+'" password="'+pass_word+'" data-phone="'+phone+'" value="重置">'
        +'</li></ul></div><div class="col-sm-6 col-lg-6 fr"><ul><li>手机号：&nbsp<span class="phone_'+agent_id+'">'+phone+'</span></li>'
        +'<li>学校单价：&nbsp￥<span class="unit_price_'+agent_id+'">'+unit_price+'</span></li><li>有效期：&nbsp<span class="valid_date_'+agent_id+'">'+valid_date+'</span></li>'
        +'<li class="recharge_'+agent_id+'">余额：&nbsp￥<span class="balance_'+agent_id+'">'+balance+'</span><input type="button" class="btn btn-default button btn_recharge" data-user_id="'+user_id+'" agent_id="'+agent_id+'" value="充值">'
        +'</li></ul></div>';
    $('.disable_'+agent_id+'').addClass('disable');
    $('.edit_'+agent_id+'').addClass('edit');
    $('.edit_'+agent_id+'').removeClass('disable');
    $('.edit_agent_'+agent_id+'').append(_li);
    bindAgentRechargeClickEvent();
    bindResetClickEvent();
}

function bindUpdateAgent(item,data) {
    var ret = false;
    if (isDateEmpty(item)) return ret;
    var update = {};
    if ( isNotEmpty(item.agent_name)&&item.agent_name != data.agent_name) update.agent_name = item.agent_name;
    if ( isNotEmpty(item.region_code)&&item.region_code != data.region_code) update.region_code = item.region_code;
    if ( isNotEmpty(item.regist_date) && item.regist_date != data.regist_date) {
        //验证生日日期格式
        var BIRTHDAY_FORMAT = /^\d{4}-(?:0\d|1[0-2])-(?:[0-2]\d|3[01])( (?:[01]\d|2[0-3])\:[0-5]\d\:[0-5]\d)?$/;
        if(!item.regist_date.match(BIRTHDAY_FORMAT)){
            toastrMassage("请输入正确的时间格式!");
            return ret;
        }
        update.regist_date = getDateStr(item.regist_date);
    }
    if ( isNotEmpty(item.password)&&item.password != data.password) update.password = $.md5(item.password);
    if ( isNotEmpty(item.phone)&&item.phone != data.phone) {
        var mobile="^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
        if(!item.phone.match(mobile)){//对手机号的验证
            toastrMassage("请输入正确有效的手机号码!");
            return ret;
        }
        update.phone = item.phone;
    }
    if ( isNotEmpty(item.unit_price)&&item.unit_price != data.unit_price) update.unit_price = item.unit_price;
    if (  isNotEmpty(item.valid_date)&&item.valid_date != data.valid_date) {
        //验证毕业时间日期格式
        var GRADUTION_FORMAT = /^\d{4}-(?:0\d|1[0-2])-(?:[0-2]\d|3[01])( (?:[01]\d|2[0-3])\:[0-5]\d\:[0-5]\d)?$/;
        if(!item.valid_date.match(GRADUTION_FORMAT)){
            toastrMassage("请输入正确的时间格式!");
            return ret;
        }
        update.valid_date = getDateStr(item.valid_date);
    }
    if (isNotEmpty(item.balance)&& item.balance != data.balance && isNotEmpty(item.balance)) {
        update.balance = item.balance;
        update.version = IntegerSum(data.version,1);
    }
    if (isDateEmpty(update)) return ret;
    update.agent_id = data.agent_id;
    update.user_id = data.user_id;
    $.myajax({
        url : 'agentAction/updateAgentByID',
        data :update,
        datatype : 'json',
        type : 'post',
        success : function(data) {
            bindMessage("您已经修改了这位代理商的个人信息!","修改成功！");
        }
    });
}

function bindUpdatePasswordClickEvent() {
    $('.button_password').unbind('click').on('click',function () {
        var password =$(this).attr('password');
        var resetPwd = $.md5( $('.update_password').val());
        if (isEmpty($('.update_password').val())) {bindMessage("重置密码失败，请输入重置密码！","提示！"); return;}
        if (password == resetPwd) {bindMessage("输入密码为原密码！","提示！"); return;}
        var phone = $(this).attr('data-phone');
        $.myajax({
            url : 'userAction/ResetAgentPassword',
            data : {
                phone : phone,
                pass_word : resetPwd
            },
            datatype : 'json',
            type : 'post',
            success : function(data) {
                bindMessage("您已经重置了这位代理商的登录密码!","重置密码成功！");
                $('.update_password').val('');
            }
        });
    });
}

function bindUpdateRechargeBalanceClickEvent() {
    $('.button_recharge_balance').unbind('click').on('click',function () {
        var agent_id = $(this).attr('data-agent_id');
        if (isEmpty($('.update_balance').val()) || judgeSign($('.update_balance').val())) {toastrMassage('请输入正确的收费金额...');return false;}
        var date = {};
        var balance = $('.update_balance').attr('balance');
        var consumption_money =  $('.update_balance').val();
        date.agent_id = agent_id;
        date.consumption_money = consumption_money;
        date.user_type = localStorage.getItem("user_type");
        bindRechargrBalanceEvent(date,true);
    });
}

//重置代理商的密码
function bindResetClickEvent() {
	$('.btn_reset').unbind('click').on('click',function () {
		var obj =  $('.password'+$(this).attr("agent_id")+'');
        var password =$(this).attr('password');
        obj.empty();
        obj.append('<div class="form-group"> <label for="label4" class="col-sm-2 control-label">密码</label>'
            +'<div class="col-sm-6 zjq-input"><input type="password" class="form-control btn_pass_word" id="label4" placeholder="**********" style="margin-left: 0px;width: 100px;height: 33px;background: #fff;color: #0b0b0b;">'
            +'</div><div><input type="button" class="btn btn-default button btn_reset_pass_word" password="'+$('.btn_reset').attr('password')+'" agent_id="'+$(this).attr("agent_id")+'" value="确定" phone="'+$(this).attr('data-phone')+'" style="width: 60px;height: 33px;margin-left: 0;"></div></div>');
        bindResetPasswordClickEvent();
    });
}

function bindResetPasswordClickEvent() {
    $('.btn_reset_pass_word').unbind('click').on('click',function () {
        var obj =  $('.password'+$(this).attr("agent_id")+'');
        var password =$(this).attr('password');
        var resetPwd = $.md5( $('.btn_pass_word').val());
        obj.empty();
        obj.append('密码：&nbsp* * * * * * <input type="button" agent_id="'+$(this).attr("agent_id")+'" class="btn btn-default button btn_reset btn_reset_'+$(this).attr("agent_id")+'" password="'+$.md5(resetPwd)+'" data-phone="'+$(this).attr('phone')+'" value="重置">');
        if (isEmpty($('.btn_pass_word').val())) {bindResetClickEvent(); return;}
        if (password == resetPwd) {bindMessage("输入密码为原密码！","提示！");bindResetClickEvent(); return;}
        var phone = $(this).attr('phone');
        $.myajax({
            url : 'userAction/ResetAgentPassword',
            data : {
                phone : phone,
                pass_word : resetPwd
            },
            datatype : 'json',
            type : 'post',
            success : function(data) {
                bindMessage("您已经重置了这位代理商的登录密码!","重置密码成功！");
                bindGetAgentList();
            }
        });
        bindResetClickEvent();
    });
}

function bindAgentRechargeClickEvent() {
    $('.btn_recharge').unbind('click').on('click',function () {
        var obj =  $('.recharge_'+$(this).attr("agent_id")+'');
        var balance = $(this).parent().attr('data-balance');
        obj.empty().append('<div class="form-group"><label for="label4" class="col-sm-2 control-label">余额</label>'
            +'<div class="col-sm-6 zjq-input"><input type="number" class="form-control consumption_money judgeNumber" min="0" onpaste="return false;" id="label4" value="" placeholder="￥'+balance+'元" style="margin-left: 0px;width: 100px;height: 33px;background: #fff;color: #0b0b0b;">'
            +'</div><div><input type="button" class="btn btn-default button btn_determine_recharge" data-user_id="'+$(this).attr("data-user_id")+'" agent_id="'+$(this).attr("agent_id")+'" value="确定" balance="'+balance+'" style=" margin-left: 0;width: 60px;height: 33px;"></div></div>');
        bindRechargeBalanceClickEvent();//onkeyup="if(! /\d+(\.\d+)?/.test($(this).val())){$(this).val(\'\');}"
        judgeNumber();
    });
}

function judgeNumber() {
    $('.judgeNumber').keyup(function () {
        if(! /\d+(\.\d+)?/.test($(this).val())){$(this).val('');}
    });
}

function bindRechargeBalanceClickEvent() {
	$('.btn_determine_recharge').unbind('click').on('click',function () {
        var date = {};
	    var agent_id = $(this).attr("agent_id");
        var obj =  $('.recharge_'+agent_id+'');
        var consumption_money = $('.consumption_money').val();
        var agent_id = $(this).attr('agent_id');
        obj.empty().append('余额：&nbsp￥<span class="balance_'+agent_id+'">'+$(this).attr("balance")+'</span><input type="button" class="btn btn-default button btn_recharge btn_recharge_'+agent_id+'" agent_id="'+agent_id+'" data-user_id value="充值">');
        if (isEmpty(consumption_money)){bindAgentRechargeClickEvent(); return;}
        if (judgeSign(consumption_money)) {toastrMassage('请输入正确的收费金额...');bindAgentRechargeClickEvent();return false;}
        date.agent_id = agent_id;
        date.consumption_money = consumption_money;
        date.user_type = localStorage.getItem("user_type");
        bindRechargrBalanceEvent(date,false);
        bindAgentRechargeClickEvent();
	});
}

function bindRechargrBalanceEvent(date,str) {
    $.myajax({
        url:'balanceAction/rechargeBalance',
        data:date,
        datatype:'json',
        type: 'POST',
        success:function(data){
            var balance = data.result.data;
            if (str){
                $('.update_balance').val('');
                $('.update_balance').attr('balance',balance.toFixed(2));
                $('.update_balance').attr('placeholder','￥'+balance.toFixed(2));
            } else {
                $('.balance_'+date.agent_id+'').empty().append(balance.toFixed(2));
            }
            toastrMassage('充值成功...');
        }
    });
}

//转换日期格式
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

function bindCompleteClickEvent() {
    $('.complete').unbind('click').on('click',function () {
        var item = {};
        if (isEmpty($('.set_agent_name').val())) {bindMessage("请输入代理商名称！","提示！"); return;}
        item.agent_name = $('.set_agent_name').val();
        if (isEmpty($('.set_region option:selected').val())) {bindMessage("请输入代理商所代理区域！","提示！"); return;}
        item.region_code = $('.set_region option:selected').val();
        if (isEmpty($('.set_regist_date').val())) {bindMessage("请输入代理商注册时间！","提示！"); return;}
        item.regist_date = getDateStr($('.set_regist_date').val());
        if (isEmpty($('.set_phone').val())) {bindMessage("请输入代理商类型电话！","提示！"); return;}
        item.phone = $('.set_phone').val();
        if (isEmpty($('.set_unit_price').val())) {bindMessage("请输入代理商类型电话！","提示！"); return;}
        item.unit_price = $('.set_unit_price').val();
        if (isEmpty($('.set_valid_date').val())) {bindMessage("请输入代理商类型电话！","提示！"); return;}
        item.valid_date = getDateStr($('.set_valid_date').val());
        $('.set_null').val('');
        submitEditAddAgent(item);
    });
}
//添加代理商
function submitEditAddAgent(item) {
	var mobile="^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
	if(!item.phone.match(mobile)){//对手机号的验证
		swal({title : "请输入正确有效的手机号码!",type : "warning",confirmButtonColor : "#DD6B55",});
		return;
	}
	//验证生日日期格式
	var BIRTHDAY_FORMAT = /^\d{4}-(?:0\d|1[0-2])-(?:[0-2]\d|3[01])( (?:[01]\d|2[0-3])\:[0-5]\d\:[0-5]\d)?$/;
	if(!item.regist_date.match(BIRTHDAY_FORMAT)){
		 swal({title : "请输入正确的时间格式!",type : "warning",confirmButtonColor : "#DD6B55",});
		 return;
	  }
	 //验证毕业时间日期格式
	var GRADUTION_FORMAT = /^\d{4}-(?:0\d|1[0-2])-(?:[0-2]\d|3[01])( (?:[01]\d|2[0-3])\:[0-5]\d\:[0-5]\d)?$/;
	if(!item.valid_date.match(GRADUTION_FORMAT)){
		 swal({title : "请输入正确的时间格式!",type : "warning",confirmButtonColor : "#DD6B55",});
		 return;
	 }	
	$.myajax({
		url : 'agentAction/addAgentVO',
		data : item,
		datatype : 'json',
		type : 'POST',
		success : function(data) {
		    $('.close').click();
            bindMessage("您已经成功的添加了这位代理商信息!","添加成功！");
			bindGetAgentList();
		}
	});
}

function bindMessage(content,title) {
    $('.subtitle').empty().append(title);
    $('#error_content').empty().append(content);
    $('#btn_error').click();
}

function isNotEmpty(str) {
   return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.replace(/(^s*)|(s*$)/g, "").length !=0;
}

function isEmpty(str) {
   return str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'||str.replace(/(^s*)|(s*$)/g, "").length ==0;
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
