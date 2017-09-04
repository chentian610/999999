var school_id = localStorage.getItem('school_id');
var user_id = localStorage.getItem('user_id');
var CourseList = eval(localStorage.getItem('CourseList'));
var currentPage=1;
var limit=10;
var teacherVO = {school_id:school_id,user_id:user_id,is_filtered:1};
var teacherDutyVO = {school_id:school_id,user_id:user_id,is_filtered:0};
var deleteVO = {school_id:school_id,user_id:user_id};
var addVO = {school_id:school_id};
var TeacherVO = {school_id:school_id,user_id:user_id,is_new:1};
var updateVO = {school_id:school_id,user_id:user_id};

function initRoleList() {
    var DictVO = {"dict_group":"016","school_id":school_id};
    $.myajax({
        url : 'dictAction/getDictSchoolList',
        data:DictVO,
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var _li = '';
            var result = data.result.data;
            result = removalDuplicate(result);
            for (var i in result) {
                var item = result[i];
                if (item.dict_code == '016025'||item.dict_code == '016030') continue;
                if (i==0) {
                    teacherVO.duty = item.dict_code;
                    teacherDutyVO.duty = item.dict_code;
                    addVO.duty = item.dict_code;
                    updateVO.duty = item.dict_code;
                }
                _li += '<li '+(i==0?'class="active clickRole"':'class="clickRole"')+' data-dict_code="'+item.dict_code+'" data-dict_value="'+item.dict_value+'"><span></span>'+item.dict_value+'</li>';
            }
            $('#RoleList').empty().append(_li);
            bindClickRoleClickEvent();
            initTeacherInfo();
        }
    });
}

function bindClickRoleClickEvent() {
    $('.clickRole').unbind('click').on('click',function () {
        $('.clickRole').removeClass('active');
        $(this).addClass('active');
        $('#insertTitle').empty().append('已添加'+$(this).attr('data-dict_value'));
        teacherVO.duty = $(this).attr('data-dict_code');
        teacherDutyVO.duty =  $(this).attr('data-dict_code');
        addVO.duty = $(this).attr('data-dict_code');
        updateVO.duty = $(this).attr('data-dict_code');
        configPage($(this).attr('data-dict_code'));
    });
}

function configPage(dict_code) {
    if (dict_code == '016005') {
        $('.zjq-master-list').css('width','479');
        $('#teacherDutyList').css('width','460px');
        $('#Grouplist').removeClass('display');
        $('#Teamlist').removeClass('display');
        initGroupList();
    } else if (dict_code == '016010') {
        $('.zjq-master-list').css('width','411px');
        $('#teacherDutyList').css('width','390px');
        teacherVO.class_id = '';
        teacherDutyVO.class_id = '';
        addVO.class_id = 0;
        $('#Grouplist').removeClass('display');
        $('#Teamlist').addClass('display');
        initGroupList();
    } else {
        $('.zjq-master-list').css('width','411px');
        $('#teacherDutyList').css('width','390px');
        teacherVO.class_id = '';
        teacherDutyVO.class_id = '';
        teacherVO.grade_id = '';
        teacherDutyVO.grade_id = '';
        addVO.grade_id = 0;
        addVO.class_id = 0;
        $('#Grouplist').addClass('display');
        $('#Teamlist').addClass('display');
        initTeacherInfo();
        initTeacherInfoByDuty();
    }
}

function initGroupList() {
    $.myajax({
        url : 'gradeAction/getGradeList',
        data:{school_id:school_id},
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var _option = '';
            var result = data.result.data;
            for (var i in result) {
                var item = result[i];
                _option += '<option value="'+item.grade_id+'">'+item.grade_name+'</option>';
            }
            $('#Grouplist').empty().append(_option);
            addVO.grade_id = $('#Grouplist option:selected').val();
            teacherVO.grade_id = $('#Grouplist option:selected').val();
            teacherDutyVO.grade_id = $('#Grouplist option:selected').val();
            if (teacherDutyVO.duty == '016005') initTeamList();
            bindGradeListChangeEvent();
            initTeacherInfo();
            initTeacherInfoByDuty();
        }
    });
}

function bindGradeListChangeEvent() {
    $('#Grouplist').unbind('change').on('change',function () {
        addVO.grade_id = $('#Grouplist option:selected').val();
        teacherVO.grade_id = $('#Grouplist option:selected').val();
        teacherDutyVO.grade_id = $('#Grouplist option:selected').val();
        if (teacherDutyVO.duty == '016005') {
            initTeamList();
        } else {
            initTeacherInfo();
            initTeacherInfoByDuty();
        }
    });
}

function initTeamList() {
    $.myajax({
        url : 'classAction/getClassList',
        data:{school_id:school_id,grade_id:$('#Grouplist option:selected').val()},
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var _option = '';
            var result = data.result.data;
            for (var i in result) {
                var item = result[i];
                _option += '<option value="'+item.class_id+'">'+item.class_name+'</option>';
            }
            $('#Teamlist').empty().append(_option);
            addVO.class_id = $('#Teamlist option:selected').val();
            teacherVO.class_id = $('#Teamlist option:selected').val();
            teacherDutyVO.class_id = $('#Teamlist option:selected').val();
            bindTeamListChangeEvent();
            initTeacherInfo();
            initTeacherInfoByDuty();
        }
    });
}

function bindTeamListChangeEvent() {
    $('#Teamlist').unbind('change').on('change',function () {
        addVO.class_id = $('#Teamlist option:selected').val();
        teacherVO.grade_id = $('#Grouplist option:selected').val();
        teacherDutyVO.grade_id = $('#Grouplist option:selected').val();
        teacherVO.class_id = $('#Teamlist option:selected').val();
        teacherDutyVO.class_id = $('#Teamlist option:selected').val();
        initTeacherInfo();
        initTeacherInfoByDuty();
    });
}

function initTeacherInfoByDuty() {
    $.myajax({
        url: 'userAction/getTeacherListByRole',
        data: teacherDutyVO,
        datatype: 'json',
        type: 'post',
        success: function (data) {
            var _tr = '';
            var result = data.result.data;
            if (isEmpty(result)){ $('#teacherDutyList').empty();return;}
            for (var i in result) {
                var item = result[i];
                _tr += setInsertHtml(item);
            }
            $('#teacherDutyList').empty().append(_tr);
            bindDeleteTeacherDutyClickEvent();
            bindTeacherCourseChangeEvent();
            bindTeacherChargeCheckClickEvent();
        }
    });
}

function bindDeleteTeacherDutyClickEvent() {
    $('.deleteTeacherDuty').unbind('click').on('click',function () {
        var obj = $(this).parent().parent();
        deleteVO.teacher_id = $(this).attr('data-teacher_id');
        deleteVO.duty = $('.active').attr('data-dict_code');
        deleteVO.phone = $(this).attr('data-phone');
        $.myajax({
            url: 'userAction/deleteTeacherDuty',
            data: deleteVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                obj.remove();
                toastrMassage('删除成功...');
                if($('#teacherList').find('tr').length >= 10) return;
                var result = data.result.data;
                $('#teacherList').append(setTeacherHtml(result[0]));
                bindAddTeacherDutyClickEvent();
            }
        });
    });
}

function getCourseName(course) {
    var name = '';
    for (var i in CourseList) {
        var item = CourseList[i];
        if (item.dict_code != course) continue;
        name = item.dict_value;
    }
    return name;
}

function setCourse(course) {
    var _option = '';
    var stratOption = '';
    for (var i in CourseList) {
        var item = CourseList[i];
        if (isEmpty(course)) stratOption = '<option>未设置</option>';
        if (item.dict_code == course) {
            stratOption = '<option value="'+item.dict_code+'">'+item.dict_value+'</option>';
        }
        _option += '<option value="'+item.dict_code+'">'+item.dict_value+'</option>';
    }
    _option = stratOption + _option;
    return '<select class="zjq-sel-teacher teacherCourse">'+_option+'</select>';
}

function bindSearchTeacherClickEvent() {
    $('#searchTeacher').unbind('click').on('click',function () {
        teacherVO.teacher_name = $('#search').val();
        initTeacherInfo();
    });
}

function bindSearchChangeEvent() {
    $('#search').on('change',function () {
        if(isEmpty($(this).val())) $('#searchTeacher').click();
    });
}

function initTeacherInfo() {
    teacherVO.start_id = (currentPage-1)*limit;
    teacherVO.limit = limit;
    teacherVO.page = currentPage;
    $.myajax({
        url:'userAction/getTeacherListByRole',
        data:teacherVO,
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
                    initTeacherInfo();
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
        }
    });
}

function addToWeb(data) {
    if (isEmpty(data)) return;
    var _tr = '';
    var result = data.result.data;
    for (var i in result) {
        var item = result[i];
        _tr += setTeacherHtml(item);
    }
    $('#teacherList').empty().append(_tr);
    bindAddTeacherDutyClickEvent();
}

function bindTeacherChargeCheckClickEvent() {
    $('.teacherChangeCheck').unbind('click').on('click',function () {
        updateVO.teacher_id=$(this).parent().parent().parent().attr('data-teacher_id');
        updateVO.course=$(this).parent().parent().siblings('.Selected').children('.form-group').find('select option:selected').val();
        updateVO.is_charge=$(this).is(':checked')==true?1:0;
        $(this).attr('is_charge', updateVO.is_charge);
        $.myajax({
            url: 'userAction/updateTeacherDutyOfManager',
            data: updateVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                var result = data.result.data;
                if (updateVO.is_charge==1) toastrMassage('班主任权限授权成功...');
                else toastrMassage('班主任权限解除成功...');
            }
        });
    });
}

function bindTeacherCourseChangeEvent() {
    $('.teacherCourse').unbind('change').on('change',function () {
        updateVO.teacher_id= $(this).parent().parent().parent().attr('data-teacher_id');
        updateVO.course= $(this).val();
        updateVO.is_charge= $(this).parent().parent().siblings('.isChange').find('div').children('.teacherChangeCheck').attr('is_charge');
        $.myajax({
            url: 'userAction/updateTeacherDutyOfManager',
            data: updateVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                var result = data.result.data;
                toastrMassage('修改教学课程成功...');
            }
        });
    });
}

function bindAddTeacherDutyClickEvent() {
    $('.addTeacherDuty').unbind('click').on('click',function () {
        var obj = $(this).parent().parent();
        addVO.phone = $(this).attr('phone');
        addVO.user_id = $(this).attr('user_id');
        addVO.teacher_name = $(this).attr('teacher_name');
        $.myajax({
            url: 'userAction/addTeacher',
            data: addVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                var result = data.result.data;
                var _tr = setInsertHtml(result);
                obj.remove();
                $('#teacherDutyList').append(_tr);
                bindDeleteTeacherDutyClickEvent();
                bindTeacherCourseChangeEvent();
                bindTeacherChargeCheckClickEvent();
            }
        });
    });
}

function bindAddTeacherListClickEvent() {
    $('#addTeacherList').unbind('click').on('click',function () {
        $('.teacherCheck').each(function () {
            if ($(this).is(':checked')) {
                var obj = $(this).parent().parent().siblings('.addTeacher').children('.addTeacherDuty');
                addVO.user_id = obj.attr('user_id');
                addVO.phone = obj.attr('phone');
                addVO.teacher_name = obj.attr('teacher_name');
                if (isEmpty(TeacherVO.item_list)) TeacherVO.item_list = JSON.stringify(addVO);
                else TeacherVO.item_list += ',' + JSON.stringify(addVO);
            }
        });
        if (isEmpty(TeacherVO.item_list)) return;
        TeacherVO.item_list ='['+TeacherVO.item_list+']';
        TeacherVO.duty = $('.active').attr('data-dict_code');
        $.myajax({
            url: 'userAction/addTeacherList',
            data: TeacherVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                var _tr = '';
                var result = data.result.data;
                for (var i in result) {
                    var item = result[i];
                    _tr += setInsertHtml(item);
                }
                $('.teacherCheck').each(function () {
                    if ($(this).is(':checked')) {
                        $(this).parent().parent().parent().remove();
                    }
                });
                TeacherVO.item_list = null;
                $('#teacherDutyList').append(_tr);
                bindDeleteTeacherDutyClickEvent();
                bindTeacherCourseChangeEvent();
                bindTeacherChargeCheckClickEvent();
            }
        });
    });
}

function bindDeleteTeacherListClickEvent() {
    $('#deleteTeacherList').unbind('click').on('click',function () {
        TeacherVO.teacher_ids = '';
        $('.teacherDutyCheck').each(function () {
            if($(this).is(':checked')) {
                var obj = $(this).parent().parent().parent();
                if (isEmpty(obj.attr('data-teacher_id'))) return;
                if (isNotEmpty(TeacherVO.teacher_ids)) TeacherVO.teacher_ids += ',' + obj.attr('data-teacher_id');
                else TeacherVO.teacher_ids = obj.attr('data-teacher_id');
                obj.remove();
            }
        });
        if (isEmpty(TeacherVO.teacher_ids)) return;
        $.myajax({
            url: 'userAction/deleteTeacherList',
            data: TeacherVO,
            datatype: 'json',
            type: 'post',
            success: function (data) {
                var _tr = '';
                toastrMassage('删除成功...');
                if($('#teacherList').find('tr').length >= 10) return;
                var result = data.result.data;
                for (var i in result) {
                    var item = result[i];
                    _tr += setTeacherHtml(item);
                }
                $('#teacherList').append(_tr);
                bindAddTeacherDutyClickEvent();
            }
        });
    });
}

function bindSelectClickEvent() {
    $('#Select').unbind('click').on('click',function () {
        $('.teacherCheck').prop('checked',true);
    })
}

function bindSelectDutyClickEvent() {
    $('#SelectDuty').unbind('click').on('click',function () {
        $('.teacherDutyCheck').prop('checked',true);
    })
}
var list = 0;
function setInsertHtml(item) {
    list++;
    var _tr = '';
    if ($('.active').attr('data-dict_code') == '016005') {
        _tr += '<tr class="zjq-student" data-user_id="'+(isEmpty(item.user_id)?0:item.user_id)+'" data-teacher_id="'+item.teacher_id+'" data-phone="'+item.phone+'"><td>'
            +'<div class="checkbox checkbox-success"><input class="styled teacherDutyCheck" type="checkbox" id="list'+list+'">'
            +'<label for="list'+list+'"></label></div></td><td class="TeacherName">'+item.teacher_name+'</td><td class="Phone">'+item.phone+'</td>'
            +'<td class="Selected"><div class="form-group">' + setCourse(item.course)
            +'</div></td><td class="isChange"><div class="checkbox checkbox-success">'
            +'<input class="styled teacherChangeCheck" type="checkbox" '+(item.is_charge==1?'checked':'')+' is_charge="'+item.is_charge+'" id="checked'+list+'"><label for="checked'+list+'">班主任</label></div></td>'
            +'<td class="deleteTeacher"><span class="deleteTeacherDuty" data-user_id="'+item.user_id+'" data-teacher_id="'+item.teacher_id+'" data-phone="'+item.phone+'"><img src="menu/icon/shanchu.png"></span></td></tr>';
    } else {
        _tr += '<tr class="zjq-student" data-user_id="'+item.user_id+'" data-teacher_id="'+item.teacher_id+'" data-phone="'+item.phone+'"><td><div class="checkbox checkbox-success">'
            +'<input class="styled teacherDutyCheck" type="checkbox" id="list'+list+'"><label for="list'+list+'"></label></div>'
            +'</td><td style="width: 100px;">'+item.teacher_name+'</td><td style="width: 200px;">'+item.phone+'</td><td class="deleteTeacher">'
            +'<span class="deleteTeacherDuty" data-teacher_id="'+item.teacher_id+'" data-phone="'+item.phone+'"><img src="menu/icon/shanchu.png"></span></td></tr>';
    }
    return _tr;
}
var sort = 0;
function setTeacherHtml(item) {
    sort++;
    return '<tr class="zjq-student" user_id="'+item.user_id+'" phone="'+item.phone+'" teacher_name="'+item.teacher_name+'"><td><div class="checkbox checkbox-success">'
        +'<input class="styled teacherCheck" type="checkbox" id="sort'+sort+'"><label for="sort'+sort+'"></label></div>'
        +'</td><td>'+item.teacher_name+'</td><td>'+item.phone+'</td><td class="addTeacher">'
        +'<span class="addTeacherDuty" user_id="'+item.user_id+'" phone="'+item.phone+'" teacher_name="'+item.teacher_name+'">' +
        '<img src="menu/icon/zengjia.png"></span></td></tr>';
}

function removalDuplicate(data) {
    var date = [];
    var roleCodes = {};
    for (var i in data) {
        var item = data[i];
        if (roleCodes[item.dict_code]) continue;
        date.push(item);
        roleCodes[item.dict_code] = true;
    }
    return date;
}

function isNotEmpty(str) {
    return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.length !=0;
}

function isEmpty(str) {
    return str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'|| str.length ==0;
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
