var num = 0;
var len = 0;
var ul_left = 0;
var Parameter = {
    school_name:localStorage.getItem('school_name'),
    school_id:localStorage.getItem('school_id'),
    user_id:localStorage.getItem('user_id'),
    user_type:localStorage.getItem('user_type'),
    image_cut_action:localStorage.getItem('image_cut_action'),
    file_upload_action:localStorage.getItem('file_upload_action'),
    phone:localStorage.getItem('phone'),
    Monday:'',
    Tuesday:'',
    Wednesday:'',
    Thursday:'',
    Friday:'',
    thisMonday:'',
    length:0,
    $this:'',
    file_list:{},
    file_length:0,
    FileList:{}
};

var Method = {
    toastrMassage:function (content) {
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
    },initSchoolMenuList:function () {
        Parameter.thisMonday = Method.getMondayDate(new Date());
        Parameter.nextMonday = Parameter.thisMonday;
        Parameter.lastMonday = Parameter.thisMonday;
        var SchoolMenuVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type,monday_data:Parameter.thisMonday};
        $.myajax({
            url : 'schoolMenuAction/getSchoolMenuList',
            data:SchoolMenuVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var date = {};
                var result = data.result.data;
                for (var i in result) {
                    var item = result[i];
                    Method.initContent(item);
                    date[item.school_menu_id] = item;
                }
                Parameter['schoolMenuList'] = date;
                var _li = '<li style="width: 350px;"></li><li data-time="'+Parameter.thisMonday+'"><time>'+Method.setMondayDate((new Date()))+'</time><div class="zjq-menu-scroll">'
                    +'<dl class="zjq-menu-manage"> <dt>每周菜谱管理</dt>'
                    +'<dd>星期一菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="1" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Monday+'</dd>'
                    +'<dd>星期二菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="2" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Tuesday+'</dd>'
                    +'<dd>星期三菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="3" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Wednesday+'</dd>'
                    +'<dd>星期四菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="4" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Thursday+'</dd>'
                    +'<dd>星期五菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="5" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Friday+'</dd></dl></div></li>';
                $('#zjq-menuList').empty().append(_li);
                len = $('#zjq-menuList li').length;
                Parameter.length++;
                Method.initFunction();
                Method.initNextSchoolMenuList(7);
                Method.bindUploadFileClickEvent();
                Method.bindAddImgClickEvent();
                Method.bindRemoveMenuClickEvent();
                Method.bindDetermineClickEvent();
                Method.bindUpdateMenuClickEvent();
            }
        });
    },initNextSchoolMenuList:function (n) {
        var Monday = '';
        if (n >= 0) {
            Parameter.nextMonday = Method.getDate(Parameter.nextMonday,n);
            Monday = Parameter.nextMonday;
        } else {
            Parameter.lastMonday = Method.getDate(Parameter.lastMonday,n);
            Monday = Parameter.lastMonday;
        }
        var SchoolMenuVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type,monday_data:Monday};
        $.myajax({
            url : 'schoolMenuAction/getSchoolMenuList',
            data:SchoolMenuVO,
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var date = {};
                var result = data.result.data;
                Parameter.Monday = '';
                Parameter.Tuesday = '';
                Parameter.Wednesday = '';
                Parameter.Thursday = '';
                Parameter.Friday = '';
                if (isNotEmpty(result)) {
                    for (var i in result) {
                        var item = result[i];
                        Method.initContent(item);
                        date[item.school_menu_id] = item;
                    }
                    Parameter['schoolMenuList'] = date;
                }
                var date = '';
                if (n >= 0) date = Method.setMondayDate(new Date(Parameter.nextMonday));
                else date = Method.setMondayDate(new Date(Parameter.lastMonday));
                var _li = '<li data-time="'+(n>=0?Parameter.nextMonday:Parameter.lastMonday)+'"><time>'+date+'</time><div class="zjq-menu-scroll">'
                    +'<dl class="zjq-menu-manage"> <dt>每周菜谱管理</dt>'
                    +'<dd>星期一菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="1" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Monday+'</dd>'
                    +'<dd>星期二菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="2" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Tuesday+'</dd>'
                    +'<dd>星期三菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="3" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Wednesday+'</dd>'
                    +'<dd>星期四菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="4" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Thursday+'</dd>'
                    +'<dd>星期五菜谱<span><img class="click" src="schoolMenu/icon/zengjia.png" data-index="5" data-toggle="modal" data-target="#myModal"/></span>'+Parameter.Friday+'</dd></dl></div></li>';
                if (n >= 0) $('#zjq-menuList').append(_li);
                else $('#zjq-menuList li').eq(0).before(_li);
                len = $('#zjq-menuList li').length;
                $("#zjq-menuList").width(len*350 + 'px');
                Method.initFunction();
                Method.bindUploadFileClickEvent();
                Method.bindAddImgClickEvent();
                Method.bindRemoveMenuClickEvent();
                Method.bindDetermineClickEvent();
                Method.bindUpdateMenuClickEvent();
            }
        });
    },initContent:function (item) {
        if (isEmpty(item)) return;
        var day = (new Date(item.menu_date)).getDay();
        switch (day) {
            case 1:
                Parameter.Monday += Method.setSchoolMenuHtml(item);
                break;
            case 2:
                Parameter.Tuesday += Method.setSchoolMenuHtml(item);
                break;
            case 3:
                Parameter.Wednesday += Method.setSchoolMenuHtml(item);
                break;
            case 4:
                Parameter.Thursday += Method.setSchoolMenuHtml(item);
                break;
            case 5:
                Parameter.Friday += Method.setSchoolMenuHtml(item);
                break;
            default:
                break;
        }
    },analysisFileListReturnHtml:function (file_list) {
        var _div = '';
        var fileList = eval(file_list);
        for (var i in fileList) {
            var item = fileList[i];
            var id = item.file_id;
            if (isEmpty(id)) {
                id = Parameter.file_length;
                Parameter.file_length ++;
            }
            _div += '<div class="zjq-pic-item"><img src="'+item.file_url+'" data-id="'+id+'" style="width:60px;height: 60px;"/></div>';
            Parameter.FileList[id] = item;
        }
        return _div;
    },setSchoolMenuHtml:function (item) {
        return '<div class="zjq-menu-detail">'
            +'<h4>'+item.menu_name+'</h4>'
            +'<div class="zjq-pic-list clearfix"> ' + Method.analysisFileListReturnHtml(item.file_list)
            +'</div><div class="zjq-menu-edit">'
            +'<img src="schoolMenu/icon/edit_pic.png" date-id="'+item.school_menu_id+'" data-toggle="modal" data-target="#myModal" class="zjq-edit-pic updateMenu"/>'
            +'<img src="schoolMenu/icon/del_circle.png" date-id="'+item.school_menu_id+'" data-toggle="modal" data-target="#myModal7" class="zjq-del-pic removeMenu"/>'
            +'</div> </div>';
    },setMondayDate:function (date) {
        var now = new Date(date);
        var nowTime = now.getTime() ;
        var day = now.getDay();
        var oneDayLong = 24*60*60*1000 ;
        var Monday = nowTime - (day-1)*oneDayLong ;
        var Sunday = nowTime + (7 - day)*oneDayLong ;
        var MondayDate = new Date(Monday);
        var SundayDate = new Date(Sunday);
        var month = ("0" + (MondayDate.getMonth() + 1)).slice(-2);
        return month +'月'+ ("0" + MondayDate.getDate()).slice(-2) + '日-'+("0" + SundayDate.getDate()).slice(-2)+'日';
    },getMondayDate:function (date) {
        var now = new Date(date);
        var nowTime = now.getTime() ;
        var day = now.getDay();
        var oneDayLong = 24*60*60*1000 ;
        var dayTime = nowTime - (day-1)*oneDayLong ;
        return getDateStr(dayTime,'day');
    },initFunction:function () {
        ul_left = $('#zjq-menuList').position().left;
        $("#zjq-menuList").width(len*350 + 'px');//计算ul的宽度
        $(".zjq-arrow-right").unbind('click').click(function(){ //右滑
            num += 1;
            ul_left = $('#zjq-menuList').position().left;
            len = $('#zjq-menuList li').length;
            if(num >= len - 2){
                Method.initNextSchoolMenuList(7);
            }
            $('#zjq-menuList').animate({left: ul_left-350 + 'px'},500);
        });
        $(".zjq-arrow-left").unbind('click').click(function(){ //左滑
            ul_left = $('#zjq-menuList').position().left;
            num--;
            if(num < 0){
                $('#zjq-menuList').css("left",-350);
                Method.initNextSchoolMenuList(-7);
                ul_left = $('#zjq-menuList').position().left;
                num = 0;
                $('#zjq-menuList').animate({left: 0},500);
                return;
            }
            $('#zjq-menuList').animate({left: ul_left+350 + 'px'},500);
        });
    },getDate:function(date,n){
        var myDate = new Date(date);
        myDate.setDate(myDate.getDate()+n);
        var dateStr = getDateStr(myDate,'day');
        return dateStr;
    },bindSelectedPhoto:function (){
        $('#uploadLogo').unbind('change').change(function() {
            var mb = ((($('#uploadLogo')[0].files[0].size)/1024)/1024).toFixed(2);
            if (mb > 1) Method.toastrMassage("您上传的图片过大,页面显示较为缓慢,请耐心等待！");
            var formData = new FormData(document.getElementById("form-file"));
            formData.append("module_code", "009033");
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
                    var file_url = item[0].file_resize_url == null || item[0].file_resize_url == ''?item[0].file_url:item[0].file_resize_url;
                    Parameter.file_list[Parameter.file_length] = item[0];
                    $('#UploadFile').before('<li class="Img" data-id="'+Parameter.file_length+'" data-index="'+Parameter.file_length+'"><img src="'+file_url+'" style="width: 100%;height: 100%;"/><span></span></li>');
                    Parameter.file_length++;
                    $('#uploadLogo').val('');
                    Method.bindRemoveImgClickEvent();
                }
            });
        });
    },bindUploadFileClickEvent:function () {
        $('#UploadFile').unbind('click').on('click',function () {
            $('#uploadLogo').click();
            Method.bindSelectedPhoto();
        });
    },getDateDay:function (monday_data,index) {
        var now = new Date(monday_data);
        var nowTime = now.getTime() ;
        var day = now.getDay();
        var oneDayLong = 24*60*60*1000 ;
        var MondayTime = nowTime - (day-parseInt(index))*oneDayLong  ;
        return getDateStr(MondayTime,'day');
    },bindPreservationClickEvent:function () {
        $('#Preservation').unbind('click').on('click',function () {
            if (isEmpty($('#menuName').val())) return Method.toastrMassage('请输入菜品名称...');
            var monday_data = Parameter.$this.parent().parent().parent().parent().parent().attr('data-time');
            var menu_date = Parameter.$this.attr('data-index') == 0?monday_data:Method.getDateDay(monday_data,Parameter.$this.attr('data-index'));
            var file_list = [];
            for (var i in  Parameter.file_list) {
                if (isEmpty(Parameter.file_list[i])) continue;
                file_list.push(Parameter.file_list[i]);
            }
            if (isEmpty(file_list)) return Method.toastrMassage('请上传菜品图片名称...');
            var SchoolMenuVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type,
                monday_data:monday_data,menu_date:menu_date,file_list:JSON.stringify(file_list),menu_name:$('#menuName').val()};
            $.myajax({
                url : 'schoolMenuAction/addSchoolMenu',
                data:SchoolMenuVO,
                datatype : 'json',
                type : 'post',
                success : function(data) {
                    var item = data.result.data;
                    var _div = '<div class="zjq-menu-detail">'
                        +'<h4>'+SchoolMenuVO.menu_name+'</h4>'
                        +'<div class="zjq-pic-list clearfix">'+Method.analysisFileListReturnHtml(SchoolMenuVO.file_list)+'</div>'
                        +'<div class="zjq-menu-edit"> <img src="schoolMenu/icon/edit_pic.png" date-id="'+item.school_menu_id+'" data-toggle="modal" data-target="#myModal" class="zjq-edit-pic updateMenu"/>'
                        +'<img src="schoolMenu/icon/del_circle.png" date-id="'+item.school_menu_id+'" data-toggle="modal" data-target="#myModal7" class="zjq-del-pic removeMenu"/></div> </div>';
                    Parameter.$this.parent().parent().append(_div);
                    Method.bindRemoveMenuClickEvent();
                    Method.bindDetermineClickEvent();
                    Method.bindUpdateMenuClickEvent();
                    $('#UploadFile').val('');
                    Parameter.file_list = [];
                    $('.data-dismiss').click();
                    Method.toastrMassage('添加菜品成功...');
                }
            });
        });
    },bindAddImgClickEvent:function () {
        $('.click').unbind('click').on('click',function () {
            $('#menuName').val('');
            $('#UploadFile').parent().children('.Img').remove();
            Method.bindPreservationClickEvent();
            Parameter.$this = $(this);
            Parameter.file_list = [];
        });
    },bindRemoveMenuClickEvent:function () {
        $('.removeMenu').unbind('click').on('click',function () {
            Parameter.$this = $(this);
        });
    },bindDetermineClickEvent:function () {
        $('#Determine').unbind('click').on('click',function () {
            var SchoolMenuVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type,school_menu_id:Parameter.$this.attr('date-id')};
            $.myajax({
                url : 'schoolMenuAction/deleteSchoolMenu',
                data:SchoolMenuVO,
                datatype : 'json',
                type : 'post',
                success : function(data) {
                    Parameter.$this.parent().parent().remove();
                    $('.cancel').click();
                    Method.toastrMassage('删除菜品成功...');
                }
            });
        });
    },bindRemoveImgClickEvent:function () {
        $('.Img').unbind('click').on('click',function () {
            Parameter.file_list[$(this).attr('data-index')] = null;
            $(this).remove();
        });
    },bindUpdateMenuClickEvent:function () {
        $('.updateMenu').unbind('click').on('click',function () {
            Parameter.file_list = [];
            Parameter.$this = $(this);
            $('#menuName').val($(this).parent().parent().find('h4').text());
            $('#UploadFile').parent().children('.Img').remove();
            $(this).parent().parent().children('.zjq-pic-list').find('div').each(function () {
                Parameter.file_list[$(this).find('img').attr('data-id')] = Parameter.FileList[$(this).find('img').attr('data-id')];
                $('#UploadFile').before('<li class="Img" data-index="'+$(this).find('img').attr('data-id')+'"><img src="'+ $(this).find('img').attr('src')+'" style="width: 100%;height: 100%;"/><span></span></li>');
            });
            Parameter.$this = $(this);
            Method.bindPreservationClickEventOld();
            Method.bindRemoveImgClickEvent();
        });
    },bindPreservationClickEventOld:function () {
        $('#Preservation').unbind('click').on('click',function () {
            var file_list = [];
            for (var i in  Parameter.file_list) {
                if (isEmpty(Parameter.file_list[i])) continue;
                file_list.push(Parameter.file_list[i]);
            }
            if (isEmpty(file_list)) return Method.toastrMassage('请上传菜品图片...');
            var SchoolMenuVO = {school_id:Parameter.school_id,user_id:Parameter.user_id,user_type:Parameter.user_type,school_menu_id:Parameter.$this.attr('date-id'),file_list:JSON.stringify(file_list),menu_name:$('#menuName').val()};
            if (isEmpty(SchoolMenuVO.menu_name)) return Method.toastrMassage('请输入菜品名称...');
            $.myajax({
                url : 'schoolMenuAction/updateSchoolMenu',
                data:SchoolMenuVO,
                datatype : 'json',
                type : 'post',
                success : function(data) {
                    Parameter.file_list = [];
                    Parameter.$this.parent().parent().find('h4').empty().append(SchoolMenuVO.menu_name);
                    var _div = Method.analysisFileListReturnHtml(file_list);
                    Parameter.$this.parent().parent().children('.zjq-pic-list').empty().append(_div);
                    $('.data-dismiss').click();
                    Method.toastrMassage('修改菜品成功...');
                }
            });
        });
    }
};

function initPageFunction() {
    Method.initSchoolMenuList();
    Method.bindPreservationClickEvent();
}
