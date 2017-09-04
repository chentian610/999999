var payVO = {};
var teamVO = {};
var date = {};
var dict_group = [{"dict_code":"038001","dict_value":"学校收费"},{"dict_code":"038002","dict_value":"便民收费"}]

function infoReleasePayPage(modlue_code) {
    if (modlue_code == '009029') date.dict_group = '038001';
    else if(modlue_code == '009030') date.dict_group = '038002';
    else date.dict_group = '038001';
    infoPayTypeList();
    bindOptionFeeObjectClickEvent();
    bindPreservationClickEvent();
    bindReleaseClickEvent();
    bindPreservationPayClickEvent();
}

function infoPayTypeList() {
    $.myajax({
        url:'dictAction/getPayTypeList',
        data:{dict_group:date.dict_group,school_id:localStorage.getItem("school_id")},
        datatype:'json',
        type: 'POST',
        success:function(data){
            var _span = '';
            var _option = '';
            var result = data.result.data;
            var dictVO = dict_group[date.dict_group == '038001'?0:1];
            for (var i in result) {
                var item = result[i];
                _option += '<option value="'+item.dict_code+'">'+item.dict_value+'</option>';
                payVO['dict_group'] = dictVO.dict_code;
                payVO['dict_value'] = dictVO.dict_value;
                payVO[item.dict_code] = item;
            }
            _span += '<span class="pay-type col-sm-2" dict_group="'+dictVO.dict_code+'" style="font-size: 25px;width: 100%;text-align: center;margin-left: -40px;">'+dictVO.dict_value+'</span>';
            $('.zjq-tab-sel').empty().append(_span);
            $('#sel_pay_type').empty().append(_option);
            //bindZjqElseFeeClickEvent();
        }
    });
}

function bindOptionFeeObjectClickEvent() {
    $('#Option-fee-object').on('click',function () {
        $.myajax({
            url:'classAction/getTeamList',
            data:{school_id:localStorage.getItem("school_id")},
            datatype:'json',
            type: 'POST',
            success:function(data){
                var _div = '<div class="checkbox zjq-sel-list Select-team"><label><input name="Select-team" type="checkbox" value="">全选</label></div>';
                var result = data.result.data;
                for (var i in result){
                    var item = result[i];
                    teamVO[item.team_type+item.team_id] = item;
                    _div += '<div class="checkbox zjq-sel-list check-team"><label><input name="chkItem" team-name="'+item.class_name+'" type="checkbox" class="chkItem" value="" team-id="'+item.team_type+item.team_id+'">'+item.class_name+'</label></div>';
                }
                $('.school-team-list').empty().append(_div);
                bindSelectTeamClickEvent();
                bindCheckTeamClickEvent();
            }
        });
    });
}

function bindSelectTeamClickEvent() {
    $('.Select-team').on('click',function () {
        if($('[name = Select-team]').prop("checked")) {
            $("[name = chkItem]").prop("checked", 'checked');
            $('.chkItem').attr('is_Selected','SELECTED');
        } else {
            $("[name = chkItem]").prop("checked", "");
            $('.chkItem').attr('is_Selected','');
        }
    });
}

function bindCheckTeamClickEvent() {
    $('.chkItem').on('click',function () {
        if (isNotEmpty($(this).attr('is_Selected'))) {
            $(this).attr('is_Selected','');
            $('[name = Select-team]').prop("checked",'');
        } else $(this).attr('is_Selected','SELECTED');
    });
}

function bindPreservationClickEvent() {
    $('#Preservation').on('click',function () {
        var _li = '';
        if($('[name = Select-team]').prop("checked")) {
            _li += '<li class="team" teamVO="SCHOOL_ALL">全校<img src="pay/images/Group.png" class="removeTeam"/></li>';
        } else {
            $('.chkItem').each(function () {
                if ($(this).attr('is_Selected') == 'SELECTED') {
                    var team = '{"school_id":"'+teamVO[$(this).attr('team-id')].school_id+'","team_type":"'+teamVO[$(this).attr('team-id')].team_type+'","group_id":"'+teamVO[$(this).attr('team-id')].group_id+'","team_id":"'+teamVO[$(this).attr('team-id')].team_id+'","team_name":"'+$(this).attr('team-name')+'"}';
                    _li += '<li team-id="'+$(this).attr('team-id')+'" class="team" teamVO='+team+'>'+$(this).attr('team-name')+'<img src="pay/images/Group.png" class="removeTeam"/></li>';
                }
            });
        }
        $('#pay-group-list').empty().append(_li);
        $('#pay-group-list').show();
        bindRemoveTeamClickEvent();
    });
}

function bindRemoveTeamClickEvent() {
    $('.removeTeam').on('click',function () {
        $(this).parent().remove();
    });
}

function bindReleaseClickEvent() {
    $('#Release').on('click',function () {
        date.pay_type = payVO['dict_group'];
        date.pay_type_name = payVO['dict_value'];
        if (isEmpty($('#sel_pay_type option:selected').val())) {toastrMassage('请选择发布类型...');return false;}
        date.pay_category = $('#sel_pay_type option:selected').val();
        date.pay_category__name = $('#sel_pay_type option:selected').text();
        if (isEmpty($('#end-date').val())) {toastrMassage('请选择截止日期...');return false;}
        //验证生日日期格式
        var BIRTHDAY_FORMAT = /^\d{4}-(?:0\d|1[0-2])-(?:[0-2]\d|3[01])( (?:[01]\d|2[0-3])\:[0-5]\d\:[0-5]\d)?$/;
        if(!$('#end-date').val().match(BIRTHDAY_FORMAT)){toastrMassage('请输入正确的截止日期...');return;}
        date.end_date = $('#end-date').val();
        if (isEmpty($('#pay-content').val())) {toastrMassage('请输入收费描述...');return false;}
        date.pay_content = $('#pay-content').val();
        if (isEmpty($('#input_money').val())) {toastrMassage('请输入收费金额...');return false;}
        if (judgeSign($('#input_money').val())){toastrMassage('请输入正确的收费金额...');return false;}
        date.pay_money = $('#input_money').val();
        if (isEmpty($('#input-Sender').val())) {toastrMassage('发件人不能为空...');return false;}
        date.sender_name = $('#input-Sender').val();
        date.pay_group_list = generateChargeTeam();
        if (isEmpty(date.pay_group_list)) {toastrMassage('收费班级不能为空...');return false;}
        var team_names = setTeamNamesList(date.pay_group_list);
        $('#input_title').empty().append(judgeIcon(date.pay_type)+ date.pay_type_name+'：'+ date.pay_category__name);
        $('#input_pay_money').empty().append('￥'+isDot(date.pay_money));
        $('#input_end_date').empty().append('截至日期：'+date.end_date);
        $('#input_sender_name').empty().append('发起人：'+date.sender_name);
        $('#input_pay_range').empty().append('收费范围：'+team_names);
    });
}

function isDot(num) {
    var result = (num.toString()).indexOf(".");
    if(result != -1) {
        return num;
    } else {
        return num + '.00';
    }
}

function setTeamNamesList(str) {
    if (str == '003') return '全校';
    var teamNames = '';
    var obj = eval(str);
    for (var i in obj) {
        var team = obj[i];
        if  (isNotEmpty(teamNames)) teamNames = teamNames + ',' + team.team_name;
        else teamNames = team.team_name;
    }
    return teamNames;
}

function bindPreservationPayClickEvent() {
    $('#Preservation_pay').on('click',function () {
        $('#Preservation_pay').attr("disabled", true);
        $('#Close_model').click();
        $.myajax({
            url:'payAction/addPay',
            data:date,
            datatype:'json',
            type: 'POST',
            success:function(data){
                $('.empty-input').val('');
                $('#pay-group-list').empty();
                $('#Preservation_pay').removeAttr("disabled");
                toastrMassage(''+data.msg+'...');
            }
        });
    });
}

function generateChargeTeam() {
    var chargeTeam = '';
    if (isEmpty($('.team').attr("teamVO"))) return "";
    if ($('.team').attr("teamVO") == 'SCHOOL_ALL') {
        chargeTeam = '003';
    } else {
        $('.team').each(function () {
            if (isNotEmpty(chargeTeam)) chargeTeam = chargeTeam + ',' + $(this).attr('teamVO');
            else chargeTeam =  $(this).attr('teamVO');
        });
        chargeTeam = '['+chargeTeam+']';
    }
    return chargeTeam;
}

// function bindZjqElseFeeClickEvent() {
//     $(".zjq-tab-sel span").click(function(){
//         var _option = '';
//         var data =  eval(payVO[$(this).attr('dict_group')].other_field);
//         for (var i in data) {
//             var item = data[i];
//             _option += '<option value="'+item.dict_code+'">'+item.dict_value+'</option>';
//         }
//         $('#sel_pay_type').empty().append(_option);
//         payVO['dict_group'] = $(this).attr('dict_group');
//         payVO['dict_value'] = $(this).text();
//         $(this).removeClass("zjq-else-fee").siblings().addClass("zjq-else-fee");
//     });
// }

// function widthCalculation(str1) {
//     return parseInt(12) -(parseInt(str1)*parseInt(2));
// }

function judgeSign(num) {
    var reg = new RegExp("^-?[0-9]*.?[0-9]*$");
    if ( reg.test(num) ) {
        var absVal = Math.abs(num);
        return num==absVal?false:true;
    } else {
        return true;
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
