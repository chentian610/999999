function initData(){
    //加载模板
    $.myajax({
        url:"dictAction/getDictSchoolList?dict_group="+dict_group,
        datatype:"json",
        success:function(data){
            var result = data.result.data;
            for(var i in result) {
                if (result[i].is_active==1) {
                    var tr = '<tr id="code'+result[i].dict_code+'"><td class="zjq-courseName zjq-course-text">' + result[i].dict_value + '</td><td class="zjq-courseName zjq-course-input"><input class="zjq-course-name"  type="text" /></td>' +
                        '<td class="zjq-course-right" style="padding-right: 13px;">' +
                        '<input class="btn btn-default zjq-rename" type="button" value="重命名" code="'+result[i].dict_code+'"><span class="zjq-sel_btns">' +
                        '<input class="btn btn-default zjq-cancel" type="button" value="取消">' +
                        '<input class="btn btn-default zjq-sure" type="button" value="确定"></span>' +
                        '<div class="checkbox checkbox-slider--b checkbox-slider-md"><label><input type="checkbox" checked onclick=deleteAttend("' +
                        result[i].dict_code + '","'+result[i].dict_value+'")>' +
                        '<span></span></label></div></td></tr>';
                    $('#course').append(tr);
                } else {
                    var tr = '<tr id="code'+result[i].dict_code+'"><td class="zjq-courseName zjq-course-text">' + result[i].dict_value + '</td><td class="zjq-courseName zjq-course-input"><input class="zjq-course-name"  type="text" /></td>' +
                        '<td class="zjq-course-right" style="padding-right: 13px;">' +
                        '<input class="btn btn-default zjq-rename" type="button" value="重命名" code="'+result[i].dict_code+'"><span class="zjq-sel_btns">' +
                        '<input class="btn btn-default zjq-cancel" type="button" value="取消">' +
                        '<input class="btn btn-default zjq-sure" type="button" value="确定"></span>' +
                        '<div class="checkbox checkbox-slider--b checkbox-slider-md"><label><input type="checkbox" onclick=activeAttend("'+
                        result[i].dict_code+'","'+result[i].dict_value+'")>'+
                        '<span></span></label></div></td></tr>';
                    $('#course').append(tr);
                }
            }
            reName();
        }
    });
}

//禁用考勤项目
function deleteAttend(code,value){
    layer.confirm('确认禁用？', {
        btn: ['确定','取消'] //按钮
    }, function(){
        $.myajax({
            url: "dictAction/forbiddenDictSchool",
            datatype: "json",
            data: {dict_code: code},
            success: function (data) {
                layer.msg('禁用成功!', {icon: 1});
                $('#code'+code).replaceWith('<tr id="code'+code+'"><td class="zjq-courseName zjq-course-text">' +value +'</td><td class="zjq-courseName zjq-course-input"><input class="zjq-course-name"  type="text" /></td>' +
                    '<td class="zjq-course-right" style="padding-right: 13px;">' +
                    '<input class="btn btn-default zjq-rename" type="button" value="重命名" code="'+code+'"><span class="zjq-sel_btns">' +
                    '<input class="btn btn-default zjq-cancel" type="button" value="取消">' +
                    '<input class="btn btn-default zjq-sure" type="button" value="确定"></span>' +
                    '<div class="checkbox checkbox-slider--b checkbox-slider-md"><label><input type="checkbox" onclick=activeAttend("'+
                    code+'","'+value+'")><span></span></label></div></td></tr>');
                reName();
            }
        });
    }, function(){
        $('#code'+code).find('label input').prop('checked',true);
    });
}

//启用
function activeAttend(code,value){
    $.myajax({
        url:"dictAction/updateDictSchool",
        datatype:"json",
        data:{dict_code:code,is_active:1},
        success:function(data){
            $('#code'+code).replaceWith('<tr id="code'+code+'"><td class="zjq-courseName zjq-course-text">' +value +'</td><td class="zjq-courseName zjq-course-input"><input class="zjq-course-name"  type="text" /></td>' +
                '<td class="zjq-course-right" style="padding-right: 13px;">' +
                '<input class="btn btn-default zjq-rename" type="button" value="重命名" code="'+code+'"><span class="zjq-sel_btns">' +
                '<input class="btn btn-default zjq-cancel" type="button" value="取消">' +
                '<input class="btn btn-default zjq-sure" type="button" value="确定"></span>' +
                '<div class="checkbox checkbox-slider--b checkbox-slider-md"><label><input type="checkbox" checked onclick=deleteAttend("' +
                code + '","'+value+'")><span></span></label></div></td></tr>');
            reName();
            layer.msg('启用成功!', {icon: 1});
        }
    });
}

function reName(){
    $(".zjq-rename").on('click',function(){
        var dict_code=$(this).attr('code');
        var oText=$(this).parent().siblings(".zjq-course-text").html();
        $(this).parent().siblings('.zjq-course-input').find(".zjq-course-name").val(oText);
        $(this).hide();
        $(this).siblings(".zjq-sel_btns").show();
        $(this).parent().siblings(".zjq-course-text").hide();
        $(this).parent().siblings(".zjq-course-input").show();
        $(this).siblings(".zjq-sel_btns").find(".zjq-cancel").click(function(){
            $(this).parent().parent().siblings(".zjq-course-text").show().html(oText);
            $(this).parent().parent().siblings(".zjq-course-input").hide();
            $(this).parent().hide();
            $(this).parent().siblings(".zjq-rename").show();
        });
        $(this).siblings(".zjq-sel_btns").find(".zjq-sure").click(function(){//确定
            var oVal=$(this).parent().parent().siblings(".zjq-course-input").find('.zjq-course-name').val();
            $(this).parent().parent().siblings(".zjq-course-text").show().html(oVal);
            $(this).parent().parent().siblings(".zjq-course-input").hide();
            $(this).parent().hide();
            $(this).parent().siblings(".zjq-rename").show();
            updateAttendName(dict_code,oVal);
        });
    });
}

//重命名
function updateAttendName(dictcode,dictvalue){
    $.myajax({
        url:"dictAction/updateDictSchool",
        datatype:"json",
        data:{dict_code:dictcode,dict_value:dictvalue},
        success:function(data){
             $(".zjq-sure").unbind('click');//避免后续执行错乱
        }
    });
}

//添加栏目
function bindBtnSaveClick(a){
    $.myajax({
        url:"dictAction/addDictSchool",
        datatype:"json",
        data: {dict_value:a,dict_group:dict_group},
        success:function(data){
        }
    });
}