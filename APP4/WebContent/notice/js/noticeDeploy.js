var file_upload_action = localStorage.getItem('file_upload_action');
var page = {};
var NoticeDate = {};
NoticeDate.module_code = '009001';

function initHomePage() {
    bindSendOutNoticeClickEvent();
    bindDeleteComfirmClickEvent();
    bindUploadPicturesClickEvent();
    bindFuzzySearchClickEvent();
    bindSelectGroupsClickEvent();
    bindPreservationNoticeClickEvent();
    bindSelectedPhoto();
    loadContactList();
}

function loadContactList() {
    var date = {};
    date.school_id = localStorage.getItem('school_id');
    date.user_id = localStorage.getItem('user_id');
    date.phone = localStorage.getItem('phone');
    $.myajax({
        url:'contactAction/getTeacherListByUserID',
        data:date,
        datatype:'json',
        type:'post',
        success:function(data){
            addNoticeContactListToWeb(data);
        }
    });
}

function addNoticeContactListToWeb(data) {
    var _li = '';
    var result = data.result.data;
    result = DuplicateRemoval(result);
    for (var i in result) {
        var item = result[i];
        if (item.is_team) {
            _li += '<li class="box-item" i="'+i+'" student_id="0" user_type="003010" grade_id="'+item.grade_id+'" team_id="'+item.team_id+'" team_type="'+item.team_type+'" team-name="'+item.class_name+'"><span id="contact'+i+'" class="check Choice"></span>'+item.class_name+'</li>';
        } else {
            _li += '<li class="box-item" i="'+i+'" user_type="003010" grade_id="0" team_id="0" team_type="011005" student_id="0" team-name="全校"><span id="contact'+i+'" class="check Choice"></span>全校</li>';
        }
    }
    $('#ContactList').empty().append(_li);
    bindChoiceClickEvent();
}

function DuplicateRemoval(result) {
    var data = [];
    var isGroup = {};
    for (var i in result) {
        var item = result[i];
        var team_id = isEmpty(item.class_id)?isEmpty(item.contact_id)?0:item.contact_id:item.class_id;
        var grade_id = isEmpty(item.grade_id)?0:item.grade_id;
        if (isGroup[grade_id+team_id]) continue;
        item.team_id = team_id;
        item.grade_id = grade_id;
        item.is_team = (isEmpty(team_id) && isEmpty(grade_id))?false:true;
        data.push(item);
        isGroup[grade_id+team_id] = true;
    }
    return data;
}

function bindSendOutNoticeClickEvent() {
    $('#SendOutNotice').unbind('click').on('click',function () {
        if (isEmpty($('#SelectContacts').val())) {toastrMassage('请选择联系人......');return false;}
        if (isEmpty($('#NoticeTitle').val())) {toastrMassage('请输入通知标题......');return false;}
        if (isEmpty($('#NoticeContent').val())) {toastrMassage('请输入通知内容......');return false;}
        $('#SendOutNotice').attr("disabled", true);
        NoticeDate.notice_title = $('#NoticeTitle').val();
        NoticeDate.notice_content = $('#NoticeContent').val();
        NoticeDate.file_list = getNoticeFileList();
        $.myajax({
            url:'noticeAction/addNotice',
            data:NoticeDate,
            datatype:'json',
            type:'post',
            success:function(data){
                if (data.success == true) {
                    $('#SelectContacts').val('');
                    $('#NoticeTitle').val('');
                    $('#NoticeContent').val('');
                    $('#FileList').empty();
                    toastrMassage('通知发送成功...');
                } else toastrMassage('发送失败，请刷新页面后再次尝试...');
                $('#SendOutNotice').removeAttr("disabled");
            }
        });
    })
}

function getNoticeFileList() {
    if(isEmpty($('#FileList').html())) return '';
    var  file_list = '';
    $('#FileList div.FileDate').each(function () {
        var file = '{"file_type":"'+$(this).attr('file_type')+'","file_url":"'+$(this).attr('file_url')+'","file_resize_url":"'+$(this).attr('file_resize_url')+'","file_name":"'+$(this).attr('file_real_name')+'"}';
        if (isNotEmpty(file_list)) file_list += ',' + file;
        else file_list = file;
    });
    return '['+file_list+']';
}

function bindPreservationNoticeClickEvent() {
    $('#PreservationNotice').unbind('click').on('click',function () {
        var team_names = '';
        var receive_list = '';
        $('#SelectedList li.Selected').each(function () {
            if (isNotEmpty(team_names)) team_names += '；'+ $(this).attr('team_name');
            else team_names = $(this).attr('team_name');
            var date = '{"user_type":"'+$(this).attr('user_type')+'","group_id":"'+$(this).attr('grade_id')+'","team_id":"'+$(this).attr('team_id')+'","team_type":"'+$(this).attr('team_type')+'","student_id":"'+$(this).attr('student_id')+'"}';
            if (isNotEmpty(receive_list)) receive_list += ',' + date;
            else receive_list = date;
        });
        $('#SelectContacts').val(team_names);
        if (isEmpty(receive_list)) return '';
        NoticeDate.receive_list = '['+receive_list+']';
    });
}

function bindDeleteComfirmClickEvent() {
    $('#deleteComfirm').unbind('click').on('click',function () {
        $('#SelectedList').empty();
        $('#ContactList').empty();
        $('#SearchFrame').removeClass('display');
        $('#SelectGroups').removeClass('display');
        $('#showTip').addClass('display');
        $('#ContactList').css('height','260px');
    });
}

function bindSelectGroupsClickEvent() {
    $('#SelectGroups').unbind('click').on('click',function () {
        $('#Search').val('');
        $('#SelectedList').empty();
        $('#ContactList').empty();
        $('#SearchFrame').addClass('display');
        $('#SelectGroups').addClass('display');
        $('#showTip').removeClass('display');
        $('#ContactList').css('height','295px');
        loadContactList();
    });
}

function bindFuzzySearchClickEvent() {
    $('#FuzzySearch').unbind('click').on('click',function () {
        StudentPage();
    });
}

//用回车键 触发搜索按钮.==13代表 键盘Q起  第13个按键
$("body").keyup(function () {
    if (event.which == 13){
        $("#FuzzySearch").trigger("click");
    }
});

function StudentPage() {
    var date = {};
    date.school_id = localStorage.getItem('school_id');
    date.user_id = localStorage.getItem('user_id');
    date.student_name = $('#Search').val();
    $.myajax({
        url:'userAction/getStudentList',
        data:date,
        datatype:'json',
        type:'post',
        success:function(data){
            addNoticeStudentListToWeb(data);
        }
    });
}

function addNoticeStudentListToWeb(data) {
    var _li = '';
    var result = data.result.data;
    if (isEmpty(result)) return;
    for (var i in result) {
        var item = result[i];
        _li += ' <li class="box-item" i="'+i+'" user_type="003010" grade_id="0" team_id="0" team_type="011005" student_id="'+item.student_id+'" team-name="'+item.student_name+'"><span id="contact'+i+'" class="check Choice"></span>'+item.student_name+'</li>';
    }
    $('#ContactList').empty().append(_li);
    bindChoiceClickEvent();
}

function bindChoiceClickEvent() {
    $('.Choice').unbind('click').on('click',function () {
        var i = $(this).parent().attr('i');
        if ($(this).hasClass('checked')) {
            $(this).removeClass('checked').addClass('check');
            $('#checked'+i+'').remove();
        } else {
            var student_id = $(this).parent().attr('student_id');
            var user_type = $(this).parent().attr('user_type');
            var grade_id = $(this).parent().attr('grade_id');
            var team_id = $(this).parent().attr('team_id');
            var team_type = $(this).parent().attr('team_type');
            if (CyclicTraversalCheckedStudent(student_id) == true && !isEmpty(student_id)) {toastrMassage('该学生已存在...');return;}
            $(this).removeClass('check').addClass('checked');
            var team_name = $(this).parent().attr('team-name');
            var _li = '<li class="box-item Selected" i="'+i+'" id="checked'+i+'" team_name="'+team_name+'" user_type="'+user_type+'" grade_id="'+grade_id+'" team_id="'+team_id+'" team_type="'+team_type+'" student_id="'+student_id+'">'+team_name+'<span class="item-checked remove-checked"></span></li>';
            $('#SelectedList').append(_li);
        }
        bindRemoveCheckedClickEvent();
    });
}

function CyclicTraversalCheckedStudent(student_id) {
    var isChecked = false;
    $('#SelectedList li.Selected').each(function () {
        if (student_id == $(this).attr('student_id')) isChecked = true;
    });
    return isChecked;
}

function bindRemoveCheckedClickEvent() {
    $('.remove-checked').unbind('click').on('click',function () {
        $('#contact'+$(this).parent().attr('i')+'').removeClass('checked').addClass('check');
        $(this).parent().remove();
    });
}

//触发按钮点击事件
function bindUploadPicturesClickEvent() {
    $('#UploadPictures').unbind('click').on('click', function(){
        $('#uploadLogo').click();//触发隐藏的from事件，
    });
}

//绑定文件上传功能
function bindSelectedPhoto(){
    $('#uploadLogo').unbind('change').unbind('change').change(function() {
        var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
        if (mb > 1) toastrMassage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
        if ($('#FileList div').siblings().length >= 9) {$('#UploadPictures').attr('disabled',false);toastrMassage("图片上传总数为9张...");return false;}
        var formData = new FormData(document.getElementById("form-file"));
        formData.append("module_code", "009001");
        formData.append("school_id", localStorage.getItem("school_id"));
        $.myajax({
            type: "POST",
            url: file_upload_action,//file_upload_action
            cache: false,
            dataType : "JSON",
            data: formData,
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            processData: false, // 告诉jQuery不要去处理发送的数据
            success: function (result) {
                var item = result.result.data;
                var img = ' <div class="hxx-cell FileDate" file_type="020005" file_url="'+item[0].file_url+'" file_resize_url="'+item[0].file_resize_url+'" file_name="'+item[0].file_name+'"><div class="hxx-img" style="background: url('+item[0].file_resize_url+') no-repeat center #d6d6d6;background-size: contain;"><span class="hxx-btn-remove"></span></div></div>';
                $('#FileList').append(img);
                $('#uploadLogo').val('');
                bindFileRemoveClickEvent();
            }
        });
    });
}

function bindFileRemoveClickEvent() {
    $('.hxx-btn-remove').unbind('click').on('click',function () {
        $(this).parent().parent().remove();
    });
}

function isIndexOf(str,str1) {
    var is = str.indexOf(str1);
    if (is >= 0) return true;
    else return false;
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