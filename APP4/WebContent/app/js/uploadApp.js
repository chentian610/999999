var storage = window.localStorage;
var currentPage = 1;
var limit = 10;
var currentPages = 1;
var limits = 10;
var file_upload_action = localStorage.getItem('file_upload_action');

// 关键信息搜索
function bindSearchButtonClickEvent() {
    $('.btn_search').on('click', function() {
        initSchoolList($('.search_school_name').val());
    });
}

function bindSelectClickEvent() {
	$('.school_select').on('change',function () {
        initSchoolList("",$('.school_select option:selected').val());
    });
}

function bindGetSchoolAppUPdateListClickEvent() {
    $('.history_version').on('click',function () {
        var school_id =  $(this).attr('school_id');
        if (!isEmpty($(this).attr('school_id'))) {
			$('.hidden_title').hide();
			$('.get_school_title').append('<div class="qtop"> <span class="qtitle">'+$(this).attr('school_name')+' | ID：'+$(this).attr('school_id')+' | '+judgeAPPStatus($(this).attr('status'))+'</span>'
				+'<button class="btn btn-default pull-right btn_return">返回</button><button class="btn btn-default pull-right release_update" school_name="'+$(this).attr('school_name')+'" school_id="'+school_id+'" data-toggle="modal" data-target="#myModal">发布更新</button> </div>');
        }
        initSchoolAPPUpdateList(school_id);
        bindReturnSchoolAppListClickEvent();
        bindReleaseUpdateClickEvent();
	});
}

function bindReturnSchoolAppListClickEvent() {
	$('.btn_return').on('click',function () {
        $('.hidden_title').show();
        $('.get_school_title').empty();
        initSchoolList();
    });
}

/**
 * 获取所有学校列表
 */
function initSchoolAPPUpdateList(school_id){
        if (isNotEmpty(school_id))  $('#school_list').empty();
        $.myajax({
            url : 'schoolAction/getSchoolAPPUpdateListByID',
            data : {
                school_id : school_id,
                start_id : (currentPages - 1) * limits,
                limit : limits,
                page : currentPages
            },
            datatype : 'json',
            type : 'post',
            success : function(data) {
                var result = data.result;
                var pageCount = Math.ceil(result.total / limits); // 取到pageCount的值(把返回数据转成object类型)
                addSchoolAPPToWeb(data);
                if (pageCount < 2) {
                    $("#page_pagintor").hide();
                    return;
                }
                var options = {
                    bootstrapMajorVersion : 3, // 版本
                    currentPage : currentPages, // 当前页数
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
                        currentPages = page;
                        initSchoolAPPUpdateList(school_id);
                    }
                };
                $("#page_pagintor").bootstrapPaginator(options);
                $("#page_pagintor").show();
            }
        });
}

function addSchoolAPPToWeb(data) {
	var _li = '';
    var result = data.result.data;
    if (isEmpty(result)) {errorPrompt('信息提示！','该学校还未上传安装包，所以没有历史版本！');return;}
    for ( var i in result) {
        var item = result[i];
        _li += '<li class="list-group-item"><div class="media"> <div class="media-body school-info">'
              +'<p>更新：'+getDateStr(item.create_date)+'</p><p>版本号：'+item.app_version+'    '+judgeIsAll(item.is_all)+'    '+judgeAppType(item.app_type)+'</p>'
         	  +'</div><div class="media-right media-middle">'
              +'<button class="btn btn-default btn-enabled download" is_disable="'+item.is_disable+'" update_url="'+item.update_url+'">下载</button></div>'
              +'<div class="media-right media-middle"><button id="'+item.id+'" is_disable="'+item.is_disable+'" class="btn btn-default btn-enabled btn_is_disable">'+isDisable(item.is_disable)+'</button>'
              +'</div> </div> </li>';
    }
    $('#school_list').append(_li);
    bindSchoolAppDownloadClickEvent();
    bindBtnIsDisableClickEvent();
}

function bindBtnIsDisableClickEvent() {
    $('.btn_is_disable').on('click',function () {
        var  is_disable = $(this).attr('is_disable') == 1?0:1;
        $(this).attr('is_disable',is_disable);
        $(this).empty().append(isDisable(is_disable));
        $.myajax({
            url: "systemAction/updateAppVersion",
            data: {id:$(this).attr('id'),is_disable:is_disable},
            datatype:'json',
            type: 'POST',
            success: function (result) {
                errorPrompt("APP状态修改成功！","APP已经成功"+(is_disable == 1?"禁用":"启用")+"！");
            }
        });
    });
}

function bindSchoolAppDownloadClickEvent() {
	$('.download').on('click',function () {
	    if ($(this).attr('is_disable') == 1) {errorPrompt("提示！","该APP已被禁用，如有疑问，请类型系统管理员！"); return;}
        window.location.href = $(this).attr('update_url');
    });
}

/**
 * 获取所有学校列表
 */
function initSchoolList(school_name,status){
	$('#school_list').empty();
    $.myajax({
        url : 'schoolAction/getSchoolAPPUpdateList',
        data : {
            school_name : school_name,
            status : status,
            start_id : (currentPage - 1) * limit,
            limit : limit,
            page : currentPage
        },
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var result = data.result;
            var pageCount = Math.ceil(result.total / limit); // 取到pageCount的值(把返回数据转成object类型)
            addSchoolToWeb(data);
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
                    initSchoolList(school_name,status);
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
        }
    });
}

function addSchoolToWeb(data) {
	var _li = '';
    var result = data.result.data;
    for ( var i in result) {
        var item = result[i];
        _li += '<li class="list-group-item"><div class="media"><div class="media-body school-info">'
			 +'<p><span style="color: #4a4a4a;" class="get_school_name" school_name="'+item.school_name+'">'+IntegerSum(i,1)+': '+item.school_name+'</span><span class="get_school_id" school_id="'+item.school_id+'">（'+item.school_id+'）</span></p>'
             +'<p style="color: #4a4a4a;margin-left: 20px;" class="get_update_date_version" create_date="'+item.create_date+'" app_version="'+item.app_version+'">'+(isEmpty(item.app_version) || isEmpty(item.create_date)?"该学校还未上传安装包！":"上次更新："+getDateStr(item.create_date)+"   版本号："+item.app_version+"")+'</p>'
             +'<div class="btn-update get_app_type" app_type="'+item.app_type+'">'+judgeIsAll(item.is_all)+'</div>'
             +'<div class="btn-update get_app_type" app_type="'+item.app_type+'" '+(isEmpty(item.app_type)?'style="display: none"':"")+'>'+judgeAppType(item.app_type)+'</div></div>'
			 +'<div class="media-right media-middle"><button class="btn btn-default btn-enabled history_version" style="margin-top: 45px;" status="'+item.status+'" school_name="'+item.school_name+'" school_id="'+item.school_id+'">历史版本</button>'
             +'</div><div class="media-right media-middle">'
             +'<label style="font-size:18px;color:#4a4a4a;text-align:right;line-height:28px;width: 116px;" class="get_status" status="'+item.status+'">'+judgeAPPStatus(item.status)+'</label>'
             +'<button class="btn btn-default btn-enabled pull-right release_update" school_name="'+item.school_name+'" school_id="'+item.school_id+'" data-toggle="modal" data-target="#myModal">发布更新</button> </div></div></li>';
    }
    $('#school_list').append(_li);
    bindGetSchoolAppUPdateListClickEvent();
    bindReleaseUpdateClickEvent();
}

function bindReleaseUpdateClickEvent() {
	$('.release_update').on('click',function () {
		$('#school_name_firstname').val($(this).attr('school_name')+"APP");
        $('#modal').attr('school_id',$(this).attr('school_id'));
    });
}
function bindUploadAPKClickEvent() {
    $('#Upload_APK').on('click',function () {
        $('#uploadLogo').click();
    });
}
/**
 * 获取所有学校列表
 */
function bindFileBtnClickEvent(){
	$('#uploadLogo').change(function(){
		var formData = new FormData(document.getElementById("form-file"));
		formData.append("module_code", "009000");
	    $.myajax({
	        url: file_upload_action,
	        data: formData,
	        cache: false,
	        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
	        processData: false, // 告诉jQuery不要去处理发送的数据
	        type: 'POST',
	        success: function (result) {
	        	var item = result.result.data;
	    		$("#url_firstname").val(item[0].file_url);
                errorPrompt("文件上传提示！","文件上传成功！");
	        }
	    });
	});	
};

function bindCompleteClickEvent() {
	$('.complete').on('click',function () {
        addAppVersion();
        $('.empty').empty();
    });
}

function bindDirectionalUpdateClickEvent() {
	$('.radio_is_all').on('click',function () {
        if ($('input:radio[name="is_all"]:checked').val() == '-1') $('.show_input').show();
        else $('.show_input').hide();
    });
}

function addAppVersion(){
	if (!$("#url_firstname").val()) {
        errorPrompt("错误提示！","更新文件不能为空！");
		return;
	}
	var update = {};
    update.school_id = $('#modal').attr('school_id');
    update.is_all = getIsAll();
    update.app_type = getAppType();
    update.app_version = $("#version_firstname").val();
    if ($('input:radio[name="is_all"]:checked').val() == '-1'){
        if (isNotEmpty( $("#phone_firstname").val())) {
            var update_phone = $("#phone_firstname").val().split(',');
            for (var i in update_phone) {
                var mobile = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
                if (!update_phone[i].match(mobile)) {//对手机号的验证
                    errorPrompt("错误提示！", update_phone[i]+"不是正确有效的手机号码，请重新输入");
                    return;
                }
            }
            update.update_phone = $("#phone_firstname").val();
        } else {
            errorPrompt("错误提示！","请输入正确有效的手机号码！");
            return;
        }
    }
    if (isNotEmpty($("#school_name_firstname").val()))update.app_name =$("#school_name_firstname").val();
    update.update_url = $("#url_firstname").val();
	$.myajax({
		url:'systemAction/addAppVersion',
		data: update,
		datatype:'json',
		success:function(data){
			$("#update_url").attr('src',"");
			$('.close').click();
            initSchoolList();
            errorPrompt("APP更新提示！","APP文件已更新到服务器...");
		}
	});
}

function getIsAll() {
    return $('input:radio[name="is_all"]:checked').val();
}

function getAppType() {
    return $('input:radio[name="app_type"]:checked').val();
}

function isNotEmpty(str) {
    return str !== null && str !== ''&& str !== ""&& str !== '""'&& str !== undefined && str !== '[]'&& str !== '{}'&&str.length !=0;
}

function isEmpty(str) {
    return str === null || str === ''|| str === ""|| str === '""'|| str === undefined || str === '[]'||str === '{}'||str.length ==0;
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
	
function IntegerSum(str1,str2) {
	return (parseInt(str1) + parseInt(str2));
}

function judgeIsAll(str) {
	if (str == 1) return "整包";
	else if (str == 0) return "局部";
	else if (str == -1) return "定向更新";
	else return "未更新";
}

function judgeAppType(str) {
    if (str == '005') return "IOS＋Android";
    else if (str == '005005') return "IOS";
    else if (str == '005010') return "Android";
    else return "未更新";
}

function judgeAPPStatus(str) {
	if (str == "007025") return "状态：已上线";
    else return "状态：已下线";
}

//转换日期格式
function getDateStr(date) {
    var myDate = new Date(date);
    var year = myDate.getFullYear();
    var month = ("0" + (myDate.getMonth() + 1)).slice(-2);
    var day = ("0" + myDate.getDate()).slice(-2);
    var h = ("0" + myDate.getHours()).slice(-2);
    var m = ("0" + myDate.getMinutes()).slice(-2);
    //var s = ("0" + myDate.getSeconds()).slice(-2);
    return year + "-" + month + "-" + day + "  " +h+ ":" + m;
}

function isDisable(str) {
	if (str == 1) return "启用";
	else return "禁用";
}

function errorPrompt(title,content) {
    $('.subtitle').empty().append(title);
    $('#error_content').empty().append(content);
    $('#btn_error').click();
}
