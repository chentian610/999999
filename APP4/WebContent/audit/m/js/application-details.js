var store = window.localStorage;
//加载学校
function loadApplyedSchool(){
	$.myajax({
		url:'schoolAction/getSchoolById',
		data:{school_id:getParameterByUrl('school_id')},
		datatype:'json',
		success:function(data){
			var school = data.result.data;
			addSchoolToWeb(school);
			bindrefuseApplyEvent(school);
			bindAgreeApplyEvent(school);
		}
	});
}

function addSchoolToWeb(school){
	$('#info-head').empty().text('申请单－'+school.school_name+
			'－'+getDateStr(school.create_date,'day'));
	$('#school-name').attr('placeholder',school.school_name);
	$('#schoolLogo').attr('src', school.organize_pic_url);
	$('#apply-people').attr('placeholder',school.school_admin_phone);
	$('#admin-phone').attr('placeholder',school.school_admin_phone);
	loadAppTemplateList(school);
	loadAppModuleList(school);
}
//加载模板列表
function loadAppTemplateList(school){
	$.myajax({
		url:'templateAction/getTemplateList',
		data:{school_type:school.school_type},
		datatype:'json',
		success:function(data){	
			$('#app-list').empty();
			var result = data.result.data;
			for(var i in result){
				var item = result[i];
				$('#app-list').append('<li value="'+item.template_id+'">'+
						'<img src="'+item.template_pic_url+'"></li>');
			}
		}
	});
}
//加载申请的模块列表
function loadAppModuleList(school){
	$.myajax({
		url:'moduleAction/getModuleList',
		data:{school_id:school.school_id},
		datatype:'json',
		success:function(data){
			$('#app-module-list').empty();
			var result = data.result.data;
			for(var i in result){
				var item = result[i];
				$('#app-module-list').append('<li value="'+item.module_code+'" class="licur">'+
						'<img src="'+item.icon_url+'">'+
						'<span>'+item.module_name+'</span><i style="display: inline;"></i></li>');
			}
		}
	});
}

//拒绝申请
function bindrefuseApplyEvent(school){
	$('#refuse-apply').unbind('click').on('click',function(){
		var content = $('#refuse-reason').text();
		var status = "007015";//申请驳回
		$.myajax({
			url:'schoolAction/updateSchoolApply',
			data:{school_id:school.school_id, status:status, school_admin_phone:school.school_admin_phone,
				content:content,school_name:school.school_name},
			datatype:'json',
			success:function(data){
				window.location.href = 'application/applicationManager.jsp';
			}
		});
	});
}
//通过申请
function bindAgreeApplyEvent(school){
	$('#agree-apply').unbind('click').on('click',function(){
		var status = "007010";//申请通过
		$.myajax({
			url:'schoolAction/updateSchoolApply',
			data:{school_id:school.school_id, status:status, school_admin_phone:school.school_admin_phone,
				school_name:school.school_name},
			datatype:'json',
			success:function(data){
				window.location.href = 'demo/m/lezhi_web.html';
			}
		});
	});
}