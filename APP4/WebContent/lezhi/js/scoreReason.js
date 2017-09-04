var module_code;
var teamtype;
var scoretype;


function showScoreType(){
    $(".zjq-proList").show();//点击第一列子项纪律项出现
    if ('009025'==module_code){
        $(".zjq-proList").hide();
        teamtype='011005';
        scoretype='012020';
        showScore(teamtype,scoretype);
    } else {
        showContent();
    }
}

function showContent(){
    $.myajax({
        url: "dictAction/getDictSchoolList?dict_group=012",
        datatype: "json",
        success: function (data) {
            $('#typeList').empty();
            var result = data.result.data;
            for (var i in result) {
                if (result[i].other_field == module_code) {
                    if (result[i].is_active==1) {
                        $('#typeList').append('<li code="'+result[i].dict_code+'" class="active">' +
                            '<span class="zjq-pro-name">' + result[i].dict_value +'</span>' +
                            '<input type="text" class="zjq-inputText1">' +
                            '<input code="'+result[i].dict_code+'" type="button" class="btn btn-default zjq-saveBtn1" value="保存"/>' +
                            '<input type="button" class="btn btn-default zjq-cancel1" value="取消"/>' +
                            '<input type="button" class="btn btn-default zjq-editBtn1" value="编辑"/>' +
                            '<input code="' + result[i].dict_code + '" type="button" class="btn btn-default zjq-disableBtn1" value="禁用"/></li>');
                    } else {
                        $('#typeList').append('<li code="'+result[i].dict_code+'" class="active">' +
                            '<span class="zjq-pro-name">' + result[i].dict_value +'</span>' +
                            '<input type="text" class="zjq-inputText1">' +
                            '<input disabled="disabled" code="' + result[i].dict_code + '" type="button" class="btn btn-default zjq-disableBtn1" value="已禁用"/></li>');
                    }
                }
            }
            showScoreReason();
            $('#typeList li:first').click();
            disableType();
            btnShow();
            if ('009023'==module_code || '009021'==module_code) {
                $('.zjq-add').hide();
                $('.zjq-editBtn1').hide();
                $('.zjq-disableBtn1').hide();
                $('.zjq-cancel1').hide();
            } else $('.zjq-add').show();
        }
    });
}

function showScoreReason(){
    $('#typeList li').on('click',function(){
        $('#typeList li').removeClass('active');
        $(this).addClass('active');
        scoretype=$(this).attr('code');
        if (module_code=='009021') teamtype='011010';
        else teamtype='011005';
        showScore(teamtype,scoretype);
     });
}

function showScore(teamtype,scoretype){
    $.myajax({
        url:"scoreAction/getScoreReason",
        datatype:"json",
        data:{team_type:teamtype,score_type:scoretype},
        success:function(data){
            $('#bonus dd').remove();
            $('#reduce dd').remove();
            var result=data.result.data;
            for (var i in result){
                if (result[i].score>0){
                    var dd='<dd class="zjq-detail"><form class="form-inline">' +
                        '<div class="form-group zjq-addSel"><select disabled="disabled"><option value="'+result[i].score+'">+'+result[i].score+
                        '分</option></select></div><div class="form-group zjq-text1"><input disabled="disabled" type="text" value="'
                        +result[i].score_reason+'"/></div><div class="form-group zjq-buttons">' +
                        '<input type="button" class="btn btn-default zjq-saveBtn" value="保存"/>' +
                        '<input type="button" class="btn btn-default cancel" value="取消"/>'+
                        '<input code="'+result[i].id+'" type="button" class="btn btn-default zjq-editBtn" value="编辑"/>' +
                        '<input code="'+result[i].id+'" type="button" class="btn btn-default zjq-disable" value="禁用"/></div></form>' +
                        '</dd>';
                    $('#bonus').append(dd);
                } else {
                    var dd='<dd class="zjq-detail"><form class="form-inline">' +
                        '<div class="form-group zjq-addSel"><select disabled="disabled"><option value="'+result[i].score+'">'+result[i].score+
                        '分</option></select></div><div class="form-group zjq-text1"><input disabled="disabled" type="text" value="'
                        +result[i].score_reason+'"/></div><div class="form-group zjq-buttons">' +
                        '<input type="button" class="btn btn-default zjq-saveBtn" value="保存"/>' +
                        '<input type="button" class="btn btn-default cancel" value="取消"/>'+
                        '<input code="'+result[i].id+'" type="button" class="btn btn-default zjq-editBtn" value="编辑"/>' +
                        '<input code="'+result[i].id+'" type="button" class="btn btn-default zjq-disable" value="禁用"/></div></form>' +
                        '</dd>';
                    $('#reduce').append(dd);
                }
            }
            $('.cancel').hide();
            _delItem();
            updateScoreReason();
            cancelUpdate();
        }
    });
}

//取消修改
function cancelUpdate(){
    $('.cancel').on('click',function (){
        showScore(teamtype,scoretype);
    });
}

//添加扣分类型score_type
function addType(typeName){
    if (typeName=='') {layer.msg('请填写名称！',{icon:0}); return false;}
    $.myajax({
        url:"dictAction/addDictSchool",
        datatype:"json",
        data: {dict_value:typeName,dict_group:'012',other_field:module_code},
        success:function(data){
            showContent();
        }
    });
}

//禁用扣分类型score_type
function disableType(){
   $('.zjq-disableBtn1').on('click',function(){
       var dict_code=$(this).attr('code');
       var obj=$(this);
       $.myajax({
           url: "dictAction/forbiddenDictSchool",
           datatype: "json",
           data: {dict_code: dict_code},
           success: function (data) {
               obj.attr('value','已禁用');
               obj.attr('disabled','disabled');
               obj.parent().find('input.zjq-editBtn1').hide();
           }
       });
   });
}

//修改扣分类型score_type
function btnShow(){
    var editBtn=$(".zjq-proList").find(".zjq-editBtn1");
    $.each(editBtn,function(){
        $(this).click(function(){
            $(this).hide().siblings().hide().siblings('.zjq-saveBtn1').show().siblings('.zjq-inputText1').show().siblings('.zjq-cancel1').show();
            $(this).siblings('.zjq-saveBtn1').click(function(){
                var a=$(this).siblings(".zjq-inputText1").val();
                if (a=='') {
                    layer.msg('请填写名称！',{icon:0});
                    return false;
                }
                var dictcode=$(this).attr('code');
                var obj=$(this);
                $.myajax({
                    url:"dictAction/updateDictSchool",
                    datatype:"json",
                    data:{dict_code:dictcode,dict_value:a,other_field:module_code},
                    success:function(data){
                        obj.siblings(".zjq-pro-name").html(a);
                        obj.hide().siblings().show();
                        obj.siblings(".zjq-inputText1").hide();
                    }
                });
            });
            $(this).siblings('.zjq-cancel1').click(function(){
                showContent();
            });
        });
    });
}

//添加扣分原因
function addScoreReason(nameVal,selectVal,obj){
    if (nameVal=='' || selectVal==null) {
        layer.msg('请填写分值和名称！',{icon:0});
        return false;
    }
    $.myajax({
        url:"scoreAction/addScoreReason",
        datatype:"json",
        data:{team_type:teamtype,score_type:scoretype,score_reason:nameVal,score:selectVal},
        success:function(data){
            obj.parent().parent().hide();
            showScore(teamtype,scoretype);
        }
    });
}

//禁用扣分原因
function _delItem(){
    $('.zjq-disable').on('click',function(){
        var id=$(this).attr('code');
        var obj=$(this);
        $.myajax({
            url:'scoreAction/deleteScoreReason',
            datatype:'json',
            data:{id:id},
            success:function(data){
                obj.parent().parent().remove();
            }
        });
    });
}

//更新扣分原因
function updateScoreReason() {
    $(".zjq-editBtn").click(function () {
        var id=$(this).attr('code');
        $(this).hide().siblings(".zjq-disable").hide().siblings(".zjq-saveBtn").show().siblings('.cancel').show();
        $(this).parent().siblings().children().removeAttr('disabled');
        var aa=$(this).parent().siblings().find('select').val();
        $(this).parent().siblings().find('select').empty();
        $(this).parent().siblings().find('select').append('<option value="3">+3分</option><option value="2">+2分</option><option value="1">+1分</option><option value="0">0分</option><option value="-1">-1分</option><option value="-2">-2分</option><option value="-3">-3分</option>');
        $(this).parent().siblings().find('select').val(aa);
        var obj=$(this);
        $(this).siblings(".zjq-saveBtn").click(function () {
            var selectVal=obj.parent().siblings().find('select').val();
            var nameVal=obj.parent().siblings().find('input:first').val();
            if (selectVal==null || nameVal==''){
                layer.msg('请填写分值和名称！',{icon:0});
                return false;
            }
            $.myajax({
                url:'scoreAction/updateScoreReason',
                datatype:'json',
                data:{id:id,score:selectVal,score_reason:nameVal},
                success:function(data){
                    showScore(teamtype,scoretype);
                }
            });
        });
    });
}