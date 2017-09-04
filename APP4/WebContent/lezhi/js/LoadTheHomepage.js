var url = localStorage.getItem('url');
var ObjectVO = {
	setRoleCodes:function () {
		var roleCode = localStorage.getItem("role_code");
		if (roleCode == '003020' || roleCode == '003025' || roleCode == '003099') return roleCode;
		var role_codes = '';
		var roleList = eval(roleCode);
		for (var i in roleList) {
			var item = roleList[i];
			role_codes = ObjectVO.isEmpty(role_codes)?item.duty:role_codes+','+item.duty;
		}
		return role_codes;
	},
	isEmpty:function (str) {
		return str == 0 || str === null || str === ''|| str === ""|| str === '""'|| str == 'null'|| str === undefined|| str === []||str === {}|| str === '[]'||str === '{}'|| str.length ==0;
	}
};

function initMenuList() {
	var user_type = localStorage.getItem('user_type');
	initTheStaticPage(user_type);
	if (localStorage.getItem('user_name'))
		$("#userName").html(localStorage.getItem('phone') + " (账号管理)");
	else
		$("#userName").html(localStorage.getItem('user_name')+ " (账号管理)");
	$("#headUrl").attr("src",localStorage.getItem('head_url'));
	if (user_type == "003025"){
		$('#LoadTheSchoolTitle').empty();
		$('#LoadTheSchoolTitle').append('<img src="lezhi/images/lezhilogo30.png" alt="" class="logo"><h1>代理商</h1>');
	}
	getFeatureListByUserType();
	initSchoolConfigAction();
	initConfigAction();
    initCourseList();
}

function initSchoolConfigAction(){
	$.myajax({
		url:'systemAction/getSchoolNewsConfig',
		data:{key:"NEWS_TEMPLATE_ON"},
		datatype:'json',
		success:function(data){
			var result = data.result.data;
			var school_news_action = result;
			localStorage.setItem('NEWS_TEMPLATE_ON', school_news_action);
		}
	});
}

function initConfigAction(){
	$.myajax({
		url:'systemAction/getAllSysConfig',
		datatype:'json',
		success:function(data){
			var result = data.result.data;
            localStorage.setItem('amr_to_mp3_action', result.AMR_TO_MP3_ACTION);
			localStorage.setItem('file_upload_action', result.FILE_UPLOAD_ACTION);
			localStorage.setItem('image_cut_action',result.IMAGE_CUT_ACTION);
			localStorage.setItem('model_download_url',result.MODEL_DOWNLOAD_URL);
			localStorage.setItem('teacher_model_download_url',result.TEACHER_MODEL_DOWNLOAD_URL);
		}
	});
}

function initCourseList() {
    var DictVO = {dict_group:"015",school_id:localStorage.getItem('school_id')};
    $.myajax({
        url : 'dictAction/getDictSchoolList',
        data:DictVO,
        datatype : 'json',
        type : 'post',
        success : function(data) {
            var result = data.result.data;
            localStorage.setItem('CourseList',JSON.stringify(result));
        }
    });
}

function initTheStaticPage(userType){
	var userUrl = null;
	var userName = null;
	var titleName = null;
	var time = (new Date()).getTime();
	if (userType == '003020') {
		userUrl = 'lezhi/print.jsp?d='+time;
		userName = '邀请使用';
		titleName = '学校管理员';
	}else if (userType == '003025') {
		userUrl = 'application/application.jsp?d='+time;
		userName = '申请学校';
		titleName = '代理商';
	}else if (userType == '003099') {
		userUrl = 'application/applicationManager.jsp?d='+time;
		userName = '申请管理';
		titleName = '系统管理员';
	}
	$('#wrapMenu').append('<div class="user-top"><div class="pull-right"><div class="user-name">'+
		'<img src="" alt="" id="headUrl"><span id="userName">（'+titleName+'）</span></div><div class="user-do">'+
		'<a class="J_menuItem" href="lezhi/account.html"><iclass="fa fa-user"></i>帐号管理</a>'+
		'</div><div class="user-do">'+
		'<a href="'+url+'" onclick="emptySession()"><i class="fa fa-sign-out"></i>退出</a></div></div></div>'+
	    '<div class="page-nav content-tabs">'+
		'<button class="scr-left J_tabLeft">'+
		'<i class="fa fa-backward"></i></button>'+
		'<button class="scr-right J_tabRight">'+
		'<i class="fa fa-forward"></i></button>'+
		'<div class="page-nav-cont J_menuTabs">'+
		'<div class="page-tabs-content" id="FeatureTltle">'+
		'<a href="javascript:;" class="active J_menuTab"'+
		'data-id="'+userUrl+'">'+userName+' <i class="fa fa-times-circle"></i>'+
		'</a></div></div></div><div class="J_mainContent" id="content-main">'+
		'<iframe class="J_iframe" name="iframe1" width="100%" height="100%"'+
		'src="'+userUrl+'" frameborder="0" data-id="'+userUrl+'"'+
		'seamless=""></iframe></div>');
    contabs();
}

function emptySession(){
	$.myajax({
		url : 'loginAction/emptySession',
		datatype : 'json',
		type : 'post',
		success : function(data) {}
	});		
}

function getFeatureListByUserType() {
	var menuVO = {};
	menuVO.school_id = localStorage.getItem("school_id");
    menuVO.user_type = localStorage.getItem('user_type');
    menuVO.is_active = 1;
    menuVO.role_code = ObjectVO.setRoleCodes();
	$.myajax({
		url : 'menuAction/getParentMenuList',
		data : menuVO,
		datatype : 'json',
		type : 'post',
		success : function(data) {
			var result = data.result.data;
			var  _li = '';
			for ( var i in result) {
				var item = result[i];
				if (item.parent_id != 0) continue;
				var url = item.title_url;
				if (item.target == '_blank') url += '?school_id='+menuVO.school_id;
                _li += '<li><a href="'+url+'" target="'+item.target+'"><i class="'+item.css_name+'"></i><span class="fa fa-angle-left"></span>'
                    + '<input id="'+ item.menu_id+ '" hidden="hidden" value="'+ item.parent_id+ '"/>'
                    + item.menu_name + '</a><ul>';
                _li += getMenuListByTitleType(result,item.menu_id);
                _li += '</ul></li>';
			}
            $('#sideMenu').empty().append(_li);
            contabs();
		}
	});		
}

function getMenuListByTitleType(data,menu_id) {
	var _li = '';
	var time = Date.parse(new Date());
	for(var i in data){
        var item = data[i];
        if (item.parent_id != menu_id) continue;
		var title_url = item.title_url.indexOf("?")>0?item.title_url+"&d="+time:item.title_url+"?d="+time;
        _li += '<li ><a class="J_menuItem" href="' + title_url
			+ '" target="iframe1" data-index="0">' + item.menu_name+ '</a></li>';
	}
	return _li;
}
