function bindPreservationClickEvent() {
    $('#Preservation').unbind('click').on('click',function () {
        if ($('#teacherStartHour ').val() == 'true' || $('#teacherEndHour ').val() ==  'true'
            || $('#studentStartHour ').val() ==  'true' || $('#studentEndHour ').val() ==  'true')
            return toastrMassage('请输入教师上下班或学生上学和放学的时间...');
        var schoolVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type};
        schoolVO.start_work_date = $('#teacherStartHour ').val() + ':'+ $('#teacherStartBranch ').val() +':59';
        schoolVO.end_work_date = $('#teacherEndHour ').val() + ':'+ $('#teacherEndBranch ').val()+':00';
        schoolVO.start_school_date =  $('#studentStartHour ').val() + ':'+ $('#studentStartBranch ').val()+':59';
        schoolVO.end_school_date =  $('#studentEndHour ').val() + ':'+ $('#studentEndBranch ').val()+':00';
        $.myajax({
            url : 'schoolAction/updateSchoolSetInfo',
            data:schoolVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                return toastrMassage('设置成功...');
            }
        });
    });
}

function initSchoolSetInfo() {
        var schoolVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type};
        $.myajax({
            url : 'schoolAction/getSchoolById',
            data:schoolVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var result = data.result.data;
                if (isEmpty(result.start_work_date) || isEmpty(result.end_work_date)
                    || isEmpty(result.start_school_date) || isEmpty(result.end_school_date)) return;
                var startWorkDate = result.start_work_date.split(':');
                var endWorkDate = result.end_work_date.split(':');
                var startSchoolDate = result.start_school_date.split(':');
                var endSchoolDate = result.end_school_date.split(':');
                $('#teacherStartHour').empty().append(setHour(startWorkDate[0]));
                $('#teacherStartBranch').empty().append(setBranch(startWorkDate[1]));
                $('#teacherEndHour ').empty().append(setHour(endWorkDate[0]));
                $('#teacherEndBranch ').empty().append(setBranch(endWorkDate[1]));
                $('#studentStartHour ').empty().append(setHour(startSchoolDate[0]));
                $('#studentStartBranch ').empty().append(setBranch(startSchoolDate[1]));
                $('#studentEndHour ').empty().append(setHour(endSchoolDate[0]));
                $('#studentEndBranch ').empty().append(setBranch(endSchoolDate[1]));
            }
        });
}

function setHour(hour) {
    var _option = '<option>'+hour+'</option>';
    for (var i = 0;i<=23;++i) {
        if (i > 9)  _option +=  '<option>'+i+'</option>';
        else _option +=  '<option>0'+i+'</option>';
    }
    return _option;
}

function setBranch(branch) {
    var _option = '<option>'+branch+'</option>';
    for (var i = 0;i< 60;++i) {
        if (i > 9)  _option +=  '<option>'+i+'</option>';
        else _option +=  '<option>0'+i+'</option>';
    }
    return _option;
}

var Parameter = {
    school_name: localStorage.getItem('school_name'),
    school_id: localStorage.getItem('school_id'),
    user_type: localStorage.getItem('user_type'),
    user_id: localStorage.getItem('user_id'),
    image_cut_action: localStorage.getItem('image_cut_action'),
    file_upload_action: localStorage.getItem('file_upload_action'),
    phone: localStorage.getItem('phone')
};

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