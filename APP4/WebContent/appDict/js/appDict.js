var Parameter = {
    school_name:localStorage.getItem('school_name'),
    school_id:localStorage.getItem('school_id'),
    user_id:localStorage.getItem('user_id'),
    user_type:localStorage.getItem('user_type'),
    image_cut_action:localStorage.getItem('image_cut_action'),
    file_upload_action:localStorage.getItem('file_upload_action'),
    phone:localStorage.getItem('phone')
};

var Method = {
    bindUploadFileClickEvent:function () {
        $('#upload').unbind('click').on('click',function () {
            $("#uploadLogo").click();
        });
    },bindFormInputChangeEvent:function () {
        $('#uploadLogo').unbind('change').on('change',function () {
            var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
            if (mb > 1) Method.toastrMassage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
            var formData = new FormData(document.getElementById("form-file"));
            formData.append("module_code", "009");
            formData.append("school_id", Parameter.school_id);
            $.myajax({
                type: "POST",
                url: Parameter.file_upload_action,//file_upload_action
                cache: false,
                dataType : "JSON",
                data: formData,
                contentType: false, // 告诉jQuery不要去设置Content-Type请求头
                processData: false, // 告诉jQuery不要去处理发送的数据
                success: function (result) {
                    var item = result.result.data;
                    $("#upload1").css('display','block');
                    $("#upload1").attr('src',item[0].file_url);
                    $("#upload2").attr('src',item[0].file_url);
                    $("#upload").addClass("active");
                    $('#uploadLogo').val('');
                    $('#upload').unbind('click');
                    $('#updateUploadFile').show();
                    Method.setSchoolAppMainUrl(item[0].file_url);
                }
            });
        });
    },bindCancleClickEvent:function () {
        $('#cancle').unbind('click').on('click', function (e) {
            e.stopPropagation();
            $("#upload1").css('display','none');
            $("#upload1").attr('src',"");
            $("#upload2").attr('src',"");
            $("#upload").removeClass("active");
        });
    },bindUpdateUploadFileClickEvent:function () {
        $('#updateUploadFile').unbind('click').on('click',function () {
            $("#uploadLogo").click();
        });
    },setSchoolAppMainUrl:function (file_url) {
        var schoolVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type,app_main_url:file_url};
        $.myajax({
            url : 'schoolAction/updateSchoolSetInfo',
            data:schoolVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                return Method.toastrMassage('设置成功...');
            }
        });
    },getSchoolAppMainUrl:function () {
        var schoolVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type};
        $.myajax({
            url : 'schoolAction/getSchoolById',
            data:schoolVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var item = data.result.data;
                if (isEmpty(item.app_main_url)) {
                    return;
                }
                $("#upload1").css('display','block');
                $("#upload1").attr('src',item.app_main_url);
                $("#upload2").attr('src',item.app_main_url);
                $("#upload").addClass("active");
                $('#upload').unbind('click');
                $('#updateUploadFile').show();
            }
        });
    },toastrMassage:function (content) {
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
};

function initPageFunction() {
    Method.getSchoolAppMainUrl();
    Method.bindUploadFileClickEvent();
    Method.bindFormInputChangeEvent();
    Method.bindCancleClickEvent();
    Method.bindUpdateUploadFileClickEvent();
}