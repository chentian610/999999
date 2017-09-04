var currentZIndex = 1;   // 代表当前最大的z-index
var currentPicIndex = 0; // 代表当前焦点图片的索引
var timer;          // 保存定时器变量
var delay = 3000;   // 图片切换的延时
var objectVO = {
    school_name:localStorage.getItem('school_name'),
    school_id:localStorage.getItem('school_id'),
    user_id:localStorage.getItem('user_id'),
    user_type:localStorage.getItem('user_type'),
    module_code:'009053',
    is_trigger:true,
    image_cut_action:localStorage.getItem('image_cut_action'),
    file_upload_action:localStorage.getItem('file_upload_action'),
    ObjectVO:{},
    removeVO:{
        file_ids:'',
        column_ids:'',
        info_ids:''
    },
    insertIDs:function (type,id) {//初始化删除ids(1==图片，2==栏目，3==校区信息)
        if (type == 1) {
            if (isEmpty(objectVO.removeVO.file_ids)) objectVO.removeVO.file_ids = id;
            else objectVO.removeVO.file_ids += ',' + id;
        } else if(type == 2) {
            if (isEmpty(objectVO.removeVO.column_ids)) objectVO.removeVO.column_ids = id;
            else objectVO.removeVO.column_ids += ',' + id;
        } else if (type == 3) {
            if (isEmpty(objectVO.removeVO.info_ids)) objectVO.removeVO.info_ids = id;
            else objectVO.removeVO.info_ids += ',' + id;
        }
    },
    initCut:function ($file) {//初始化裁剪插件
        $file.cropper('destroy').cropper({
            aspectRatio: 7 / 4,
            zoomable:false,
            preview: '.img-preview',
            background:false,
            crop: function (e) {
            }
        });
    },
    initFileList:function () {//初始化图片标签
        var _ul = '<ul class="clearfix" id="ImgList">';
        var fileList = eval(objectVO.ObjectVO.file_list);
        for (var i in fileList) {
            var item = fileList[i];
            _ul += '<li> <img file_url="'+item.file_url+'" file_resize_url="'+item.file_resize_url
                +'" file_id="'+item.file_id+'" src="'+item.file_resize_url+'?d='+(new Date()).getTime()+'" file_name="'+item.file_name+'" class="zjq-base-pic">'
                +'<img src="microPortal/icon/close_icon.png" class="zjq-close-icon removeFile"> </li> ';
        }
        _ul += '<li class="active" '+(fileList.length == 5?'style="display:none;':'')+'"><img src="microPortal/icon/upload_logo.png" class="zjq-plus-icon"> </li> </ul>';
        $('#MicroPortalFile').empty().append(_ul);
        $('#MicroPortalFile').unbind('click');
        bindActiveClickEvent();
        bindRemoveFileClickEvent();
    },
    initCampusList:function () {//初始化校区标签
        var _li = '';
        var schoolList = eval(objectVO.ObjectVO.school_list);
        for (var i in schoolList) {
            var item = schoolList[i];
            _li += ' <li><div class="zjq-edit-detail" info_id="'+item.info_id+'">'
                +'<input type="text" class="form-control campusName" placeholder="输入校区名称" value="'+item.campus_name+'">'
                +'<textarea class="form-control address" rows="3" placeholder="输入校区地址">'+item.address+'</textarea>'
                +'<input type="text" class="form-control phone" placeholder="输入联系电话" value="'+item.phone +'">'
                +'<input type="text" class="form-control email" placeholder="输入联系邮箱" value="'+item.email+'"> </div>'
                +'<img src="microPortal/icon/del_logo.png" class="removeCampus"> </li>';
        }
        $('#CampusList').empty().append(_li);
        bindRemoveCampusClickEvent();
    },
    initColumnList:function () {//初始化栏目标签
        var _li = '';
        var columnList = eval(objectVO.ObjectVO.column_list);
        for (var i in columnList) {
            var item = columnList[i];
            _li += ' <li> <div class="zjq-edit-detail" column_id="'+item.column_id+'">'
                +'<input type="text" class="form-control columnName" placeholder="输入栏目名称" value="'+item.column_name+'">'
                +'<textarea class="form-control columnValue" rows="3" placeholder="输入栏目内容">'+item.column_content+'</textarea> </div>'
                +'<img src="microPortal/icon/del_logo.png" class="removeColumn"></li>';
        }
        $('#columnList').empty().append(_li);
        bindRemoveColumnClickEvent();
    },
    initContent:function () {//进入微门户初始化页面内容
        $.myajax({
            type: "POST",
            url: 'microPortalAction/getMicroPortal',
            cache: false,
            dataType : "JSON",
            data: {school_id:objectVO.school_id,user_id:objectVO.user_id,user_type:objectVO.user_type},
            success: function (result) {
                var item = result.result.data;
                if (isNotEmpty(item.file_list)||isNotEmpty(item.school_list)||isNotEmpty(item.column_list)) {
                    objectVO.initPreviewPage(item);
                } else {
                    objectVO.initEditMicroPortalPage();
                }
            }
        });
    },
    initPreviewPage:function (item) {
        objectVO.ObjectVO = item;
        bindEditContentClickEvent();
        objectVO.insertPreviewHtmlPage($('#previewContent'),$('#banner-pic1'),$('#banner-nav1'),item);
        $('#previewEffect').show();
        $('#editMicroPortal').hide();
    },
    initEditMicroPortalPage:function () {//初始化微门户编辑内容
        $('#editMicroPortal').show();
        $('#previewEffect').hide();
        bindFileUploadClickEvent();
        bindCutImagesClickEvent();
        bindCutPhotoClickEvent();
        bindStepClickEvent();
        bindAddColumnClickEvent();
        bindRemoveColumnClickEvent();
        bindAddCampusClickEvent();
        bindRemoveCampusClickEvent();
        bindPreservationClickEvent();
        bindPreviewClickEvent();
        bindOnPasteEvent();
    },
    insertPreviewHtmlPage:function ($content,$pic,$nav,MicroPortalVO) {//初始化页面预览效果
        $('#schoolName').empty().append(objectVO.school_name);
        if (isNotEmpty(MicroPortalVO.file_list)) {
            var _file = '';
            var _span = '';
            var fileList = eval(MicroPortalVO.file_list);
            for (var i in fileList) {
                var item = fileList[i];
                _file += '<span><img src="'+item.file_url+'"></span>';
                _span += '<span></span>';
            }
            $pic.empty().append(_file);
            $nav.empty().append(_span);
        }
        if (isNotEmpty(MicroPortalVO.column_list)) {
            var _column = '<ul>';
            var columnList = eval(MicroPortalVO.column_list);
            for (var i in columnList) {
                var item = columnList[i];
                if ((columnList.length-1) > i) {
                    _column += ' <li class="zjq-school-corner">'
                        +'<h4><img src="microPortal/icon/school_logo.png"/><span>'+item.column_name+'</span></h4>'
                        +'<p>'+item.column_content+'</p><div class="zjq-column-block"></div></li>';
                } else {
                    _column += ' <li class="zjq-school-corner">'
                        +'<h4><img src="microPortal/icon/school_logo.png"/><span>'+item.column_name+'</span></h4>'
                        +'<p>'+item.column_content+'</p></li>';
                }
            }
            _column += '</ul>';
            $content.empty().append(_column);
        }
        if (isNotEmpty(MicroPortalVO.school_list)) {
            var _school = '';
            var schoolList = eval(MicroPortalVO.school_list);
            for (var i in schoolList) {
                var item = schoolList[i];
                _school += ' <div class="zjq-school-corner">'
                    +'<h4><img src="microPortal/icon/school_logo.png"><span>'+item.campus_name+'</span></h4>'
                    +'<div class="zjq-school-message"><span>地址：'+item.address+'</span>'
                    +'<img src="microPortal/icon/location.png"> </div>'
                    +'<div class="zjq-school-message"><span>电话：'+item.phone+'</span>'
                    +'<img src="microPortal/icon/telephone.png"> </div>'
                    +'<div class="zjq-school-message"><span>邮箱：'+item.email+'</span>'
                    +'<img src="microPortal/icon/mail.png"> </div></div>';
            }
            $content.append(_school);
        }
        objectVO.carousel($pic,$nav);
    },
    insertHtml:function (item) {//插入添加后的图片标签
        var _ul = '';
        if ($('#MicroPortalFile').hasClass('zjq-upload-block')){
            $('#MicroPortalFile').removeClass('zjq-upload-block');
            $('#MicroPortalFile').addClass('zjq-edit-pic');
            _ul = '<ul class="clearfix" id="ImgList"> <li> <img file_url="'+item.file_url+'" file_resize_url="'+item.file_resize_url
                +'" src="'+item.file_resize_url+'?d='+(new Date()).getTime()+'" file_name="'+item.file_name+'" class="zjq-base-pic">'
                +'<img src="microPortal/icon/close_icon.png" class="zjq-close-icon removeFile"> </li> <li class="active">'
                +'<img src="microPortal/icon/upload_logo.png" class="zjq-plus-icon"> </li> </ul>';
            $('#MicroPortalFile').empty().append(_ul);
            bindActiveClickEvent();
        } else {
            $('.active').before('<li> <img file_url="'+item.file_url+'" file_resize_url="'+item.file_resize_url
                +'" src="'+item.file_resize_url+'?d='+(new Date()).getTime()+'" file_name="'+item.file_name+'" class="zjq-base-pic">'
                +'<img src="microPortal/icon/close_icon.png" class="zjq-close-icon removeFile"> </li>');
        }
        objectVO.is_cut = false;
        if ($('#ImgList li').length >= 6) $('.active').hide();
        bindRemoveFileClickEvent();
    },
    editContentClick:function () {
        $('#MicroPortalFile').removeClass('zjq-upload-block');
        $('#MicroPortalFile').addClass('zjq-edit-pic');
        objectVO.initFileList();
        objectVO.initColumnList();
        objectVO.initCampusList();
        $('#editMicroPortal').show();
        $('#previewEffect').hide();
        bindCutImagesClickEvent();
        bindCutPhotoClickEvent();
        bindStepClickEvent();
        bindAddColumnClickEvent();
        bindAddCampusClickEvent();
        bindPreservationClickEvent();
        bindPreviewClickEvent();
        bindOnPasteEvent();
        bindFormControlInputEvent();
    },
    setFileUrl:function (file) {
        if (isEmpty(file)) return;
        var url = null;
        if (window.createObjectURL != undefined) {
            url = window.createObjectURL(file)
        } else if (window.URL != undefined) {
            url = window.URL.createObjectURL(file)
        } else if (window.webkitURL != undefined) {
            url = window.webkitURL.createObjectURL(file)
        }
        return url;
    },
    insertText:function (obj,str) {//请注意:obj是document对象，jquery传入时:$(this)[0]
        if (document.selection) {
            var sel = document.selection.createRange();
            sel.text = str;
        } else if (typeof obj.selectionStart === 'number' && typeof obj.selectionEnd === 'number') {
            var startPos = obj.selectionStart,
                endPos = obj.selectionEnd,
                cursorPos = startPos,
                tmpStr = obj.value;
            obj.value = tmpStr.substring(0, startPos) + str + tmpStr.substring(endPos, tmpStr.length);
            cursorPos += str.length;
            obj.selectionStart = obj.selectionEnd = cursorPos;
        } else {
            obj.value += str;
        }
    },
    carousel:function ($pic,$nav) {
        // 初始化banner,将第一张图片放在最上面,第一个索引添加active
        $pic.find("span:first").css("zIndex", currentZIndex);
        $nav.children().first().removeClass().addClass("active");
        // 添加鼠标悬停事件响应
        $pic.hover(
            function () {
                // 停止图片播放
                clearInterval(timer);
            },
            function () {
                objectVO.playBanner($pic,$nav);   // 继续播放
            }
        );
        objectVO.playBanner($pic,$nav);
    },
    playBanner:function ($pic,$nav) {
        var picNum = $pic.find("span").length;
        clearInterval(timer);
        timer = setInterval(anim, delay);
        function anim() {
            // 选取下一张图片
            var nextIndex = currentPicIndex + 1;
            if (nextIndex == picNum) {
                nextIndex = 0;
            }
            $pic.find("span").eq(nextIndex).css({left: "345px", zIndex: currentZIndex++}).animate({left: "0px"});
            $nav.find("span").eq(nextIndex).addClass('active').siblings().removeClass("active");
            currentPicIndex = nextIndex;
        }
    }
};

function initPage() {//初始化页面
    objectVO.initContent();
}

function bindEditContentClickEvent() {
    $('#editContent').unbind('click').on('click',function () {
        objectVO.editContentClick();
    });
}

function bindFileUploadClickEvent() {
    $('.zjq-upload-block').unbind('click').on('click',function () {
        if (!objectVO.is_trigger){objectVO.is_trigger = true;return;}
        $('#uploadLogo').click();//触发隐藏的from事件，
        bindUploadImagesChangeEvent();
    });
}

function bindUploadImagesChangeEvent() {
    $('#uploadLogo').unbind('change').on('change',function () {
        var file_url = objectVO.setFileUrl(document.getElementById('uploadLogo').files[0]);
        $('#fileUrl').attr('src',file_url);
        $('#modalContent').css({width:'479px',height:'409px',margin:'127px auto'});
        $('#BtnModal').click();
        $('#fileUrl').removeClass('cropper-hidden');
        $('.cropper-container').remove();
        $('#cutImages').show();
        $('#Step').hide();
    });
}

function bindCutImagesClickEvent() {
    $('#cutImages').unbind('click').on('click',function () {
        objectVO.initCut($('#fileUrl'));
        objectVO.is_cut = true;
        $('#Step').show();
        $(this).hide();
        bindCutPhotoClickEvent();
    });
}

function bindStepClickEvent() {
    $('#Step').unbind('click').on('click',function () {
        objectVO.is_cut = false;
        $('#fileUrl').removeClass('cropper-hidden');
        $('.cropper-container').remove();
        $('#cutImages').show();
        $(this).hide();
    });
}

function bindCutPhotoClickEvent() {
    $('#uploadImages').unbind('click').on('click',function() {
        var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
        if (mb > 1) toastrMassage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
        var formData = new FormData(document.getElementById("form-file"));
        formData.append("module_code", objectVO.module_code);
        formData.append("school_id",objectVO.school_id);
        $.myajax({
            type: "POST",
            url: objectVO.file_upload_action,//file_upload_action
            cache: false,
            dataType : "JSON",
            data: formData,
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            processData: false, // 告诉jQuery不要去处理发送的数据
            success: function (result) {
                var item = result.result.data;
                $('#uploadLogo').val('');
                if (objectVO.is_cut) {
                    cutImages(item[0]);
                    return;
                }
                objectVO.is_cut = false;
                objectVO.insertHtml(item[0]);
            }
        });
    });
}

function cutImages(item) {
    var cut_data = '{width:'+$('.cropper-crop-box').css('width')+',height:'+$('.cropper-crop-box').css('height')+',left:'+$('.cropper-crop-box').css('left')+',top:'+$('.cropper-crop-box').css('top')+'}';
    var image = new Image();
    image.src = item.file_url;
    image.onload = function () {
        var resize_rate = $(".cropper-canvas").width()/image.width;
        $.myajax({
            url: objectVO.image_cut_action,
            dataType : "JSON",
            data: {cut_data:cut_data,module_code:objectVO.module_code,file_name:item.file_name,resize_rate:resize_rate,school_id:objectVO.school_id},
            success: function (result) {
                objectVO.insertHtml(item);
                $('#fileUrl').removeClass('cropper-hidden');
                $('.cropper-container').remove();
            }
        });
    }
}

function bindRemoveFileClickEvent() {
    $('.removeFile').unbind('click').on('click',function () {
        if (isNotEmpty($(this).prev().attr('file_id'))) objectVO.insertIDs(1,$(this).prev().attr('file_id'));
        $(this).parent().remove();
        if ($('#ImgList li').length <= 5) {
            if ($('#ImgList li').length <= 1) {
                $('#MicroPortalFile').addClass('zjq-upload-block');
                $('#MicroPortalFile').removeClass('zjq-edit-pic');
                $('#MicroPortalFile').empty().append('<img src="microPortal/icon/upload_logo.png" class="zjq-plus-icon"><span>上传图片</span>');
                objectVO.is_trigger = false;
                bindFileUploadClickEvent();
            } else {
                $('.active').show();
            }
        }
    });
}

function bindAddColumnClickEvent() {
    $('#addColumn').unbind('click').on('click',function () {
        var _li = '<li><div class="zjq-edit-detail addMicroPortal"><input type="text" class="form-control columnName" placeholder="输入栏目名称">'
            +'<textarea class="form-control columnValue" rows="3" placeholder="输入栏目内容"></textarea> </div>'
            +'<img src="microPortal/icon/del_logo.png" class="removeColumn"> </li>'
        $('#columnList').append(_li);
        bindRemoveColumnClickEvent();
    });
}

function bindRemoveColumnClickEvent() {
    $('.removeColumn').unbind('click').on('click',function () {
        if ($('#columnList li').length <= 1) {
            $(this).prev().children('.columnName').val('');
            $(this).prev().children('.columnValue').val('');
        } else {
            if (isNotEmpty($(this).prev().attr('column_id'))) objectVO.insertIDs(2,$(this).prev().attr('column_id'));
            $(this).parent().remove();
        }
    });
}

function bindAddCampusClickEvent() {
    $('#addCampus').unbind('click').on('click',function () {
        var _li = '<li> <div class="zjq-edit-detail addMicroPortal">'
            +'<input type="text" class="form-control campusName" placeholder="输入校区名称">'
            +'<textarea class="form-control address" rows="3" placeholder="输入校区地址"></textarea>'
            +'<input type="text" class="form-control phone" placeholder="输入联系电话">'
            +'<input type="text" class="form-control email" placeholder="输入联系邮箱"> </div>'
            +'<img src="microPortal/icon/del_logo.png" class="removeCampus"> </li>';
        $('#CampusList').append(_li);
        bindRemoveCampusClickEvent();
    });
}

function bindRemoveCampusClickEvent() {
    $('.removeCampus').unbind('click').on('click',function () {
        if ($('#CampusList li').length <= 1) {
            $(this).prev().children('.campusName').val('');
            $(this).prev().children('.address').val('');
            $(this).prev().children('.phone').val('');
            $(this).prev().children('.email').val('');
        } else {
            if (isNotEmpty($(this).prev().attr('info_id'))) objectVO.insertIDs(3,$(this).prev().attr('info_id'));
            $(this).parent().remove();
        }
    });
}

function bindActiveClickEvent() {
    $('.active').unbind('click').on('click',function () {
        $('#MicroPortalFile').unbind('click');
        $('#uploadLogo').click();//触发隐藏的from事件，
        bindUploadImagesChangeEvent();
    });
}

function bindPreviewClickEvent() {
    $('#preview').unbind('click').on('click',function () {
        var MicroPortalVO = {file_list:setFileList(true),column_list:setColumnList(true),school_list:setInfoList(true)};
        if (isEmpty(setFileList(true))) {toastrMassage('请上传图片...');return;}
        if (isEmpty(setColumnList(true))) {toastrMassage('请输入栏目信息...');return;}
        if (objectVO.is_error || isEmpty(setInfoList(true))) {if (objectVO.is_error) return; toastrMassage('请输入校区信息...');return; }
        objectVO.insertPreviewHtmlPage($('#Content'),$('#banner-pic'),$('#banner-nav'),MicroPortalVO);
        $('#BtnModal1').click();
    });
}

function bindPreservationClickEvent() {
    $('#Preservation').unbind('click').on('click',function () {
        var MicroPortalVO = {school_id:objectVO.school_id,user_id:objectVO.user_id,user_type:objectVO.user_type,file_list:setFileList(),column_list:setColumnList(),school_list:setInfoList()};
        if (isNotEmpty(objectVO.removeVO.file_ids)) MicroPortalVO.file_ids = objectVO.removeVO.file_ids;
        if (isNotEmpty(objectVO.removeVO.column_ids)) MicroPortalVO.column_ids = objectVO.removeVO.column_ids;
        if (isNotEmpty(objectVO.removeVO.info_ids)) MicroPortalVO.info_ids = objectVO.removeVO.info_ids;
        if (isEmpty(setFileList(true)) && (isNotEmpty(objectVO.removeVO.file_ids)||isEmpty(objectVO.removeVO.file_ids))) {toastrMassage('请上传图片...');return;}
        if (isEmpty(setColumnList(true)) && (isNotEmpty(objectVO.removeVO.column_ids)||isEmpty(objectVO.removeVO.column_ids))) {toastrMassage('请输入栏目信息...');return;}
        if (objectVO.is_error || (isEmpty(setInfoList(true)) && (isNotEmpty(objectVO.removeVO.info_ids)||isEmpty(objectVO.removeVO.info_ids)))) {if (objectVO.is_error) return; toastrMassage('请输入校区信息...');return; }
        $.myajax({
            type: "POST",
            url: 'microPortalAction/addMicroPortal',
            cache: false,
            dataType : "JSON",
            data: MicroPortalVO,
            success: function (result) {
                var item = result.result.data;
                objectVO.initContent();
            }
        });
    });
}

function setFileList(isPreview) {
    var data = [];
    if ($('#MicroPortalFile').hasClass('zjq-upload-block')) return;
    $('#MicroPortalFile li').each(function () {
        var item = {};
        if (!$(this).hasClass('active')) {
            item.file_id = $(this).children('.zjq-base-pic').attr('file_id');
            if (isEmpty(item.file_id) || isPreview) {
                var file_url = $(this).children('.zjq-base-pic').attr('file_url');
                var file_resize_url = $(this).children('.zjq-base-pic').attr('file_resize_url');
                var file_name = $(this).children('.zjq-base-pic').attr('file_name');
                var file_type = '020005';
                if (isNotEmpty(file_url) && isNotEmpty(file_name) && isNotEmpty(file_resize_url))
                    data.push({
                        file_url: file_url,
                        file_type: file_type,
                        file_name: file_name,
                        file_resize_url: file_resize_url,
                        school_id: objectVO.school_id
                    });
            }
        }
    });
    if (isEmpty(data)) return;
    return JSON.stringify(data);
}

function setColumnList(isPreview) {
    var data = [];
    $('#columnList li').each(function () {
        var item;
        if ($(this).find('div').hasClass('addMicroPortal') || $(this).find('div').hasClass('updateValue') || isPreview) {
            if ($(this).find('div').hasClass('addMicroPortal') || isPreview)
                item = {
                    school_id: objectVO.school_id,
                    column_name: $(this).find('div').children('.columnName').val(),
                    column_content: $(this).find('div').children('.columnValue').val()
                };
            if ($(this).find('div').hasClass('updateValue'))
                item = {
                    school_id: objectVO.school_id,
                    column_name: $(this).find('div').children('.columnName').val(),
                    column_content: $(this).find('div').children('.columnValue').val(),
                    column_id: $(this).find('div').attr('column_id')
                };
            if (isNotEmpty(item.column_name) && isNotEmpty(item.column_content)) {
                data.push(item);
                objectVO.is_column_error = false;
            }
        } else objectVO.is_column_error = true;
    });
    if (objectVO.is_column_error) return;
    if (isEmpty(data)) return;
    return JSON.stringify(data);
}

function setInfoList(isPreview) {
    var data = [];
    $('#CampusList li').each(function () {
        var item;
        if ($(this).find('div').hasClass('addMicroPortal') || $(this).find('div').hasClass('updateValue') || isPreview) {
            if ($(this).find('div').hasClass('addMicroPortal') || isPreview)
                item = {school_id:objectVO.school_id};
            if ($(this).find('div').hasClass('updateValue'))
                item = {school_id:objectVO.school_id,info_id:$(this).find('div').attr('info_id')};
            item.campus_name = $(this).find('div').children('.campusName').val();
            item.address = $(this).find('div').children('.address').val();
            item.phone = $(this).find('div').children('.phone').val();
            item.email = $(this).find('div').children('.email').val();
            var mobile = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
            var mEmail = "^[a-zA-Z0-9_\\.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$";
            if (isNotEmpty(item.campus_name)&&isNotEmpty(item.address)&&isNotEmpty(item.phone)&&isNotEmpty(item.email)) {
                if (!item.phone.match(mobile)) {toastrMassage(item.campus_name+'的联系电话错误，请重新输入...');objectVO.is_error=true;return;}
                if (!item.email.match(mEmail)) {toastrMassage(item.campus_name+'的Email错误，请重新输入...');objectVO.is_error=true;return;}
                data.push(item);
                objectVO.is_school_error = false;
                objectVO.is_error = false;
            }
        } else objectVO.is_school_error = true;
    });
    if (objectVO.is_school_error) return;
    if (isEmpty(data)) return;
    return JSON.stringify(data);
}

function bindFormControlInputEvent() {
    $('.form-control').bind('input change',function () {
        $(this).parent().addClass('updateValue');
    });
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

function bindOnPasteEvent() {
    $(".form-control ").on("paste",function(e) {
        var pastedText = undefined;
        if (window.clipboardData && window.clipboardData.getData) { // IE
            pastedText = window.clipboardData.getData('Text');
        } else {
            pastedText = e.originalEvent.clipboardData.getData('Text');//e.clipboardData.getData('text/plain');
        }
        $(this).parent().addClass('updateValue');
        objectVO.insertText($(this)[0],pastedText);
        return false;
    });
}
