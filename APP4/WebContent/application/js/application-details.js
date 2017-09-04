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
	$('#createTime').text(getDateStr(school.create_date,'day'));
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
		url:'moduleAction/getSchoolModuleCodeListBySchoolID',
		data:{school_id:school.school_id},
		datatype:'json',
		success:function(data){
			var result = data.result.data;
			var code=new Array();
			var type=new Array();
			for(var i in result){
				var item = result[i];
				code.push(item.module_code);
				type.push(item.user_type);
			}
			$.myajax({
				url:'templateAction/getModuleList',
				data:{},
				datatype:'json',
				success:function(data){
					$('#app-module-list').empty();
					var result = data.result.data;
					for(var i in result){
						var item = result[i];
						if($.inArray(item.module_code,code) != -1 && $.inArray(item.user_type,type) != -1)
						$('#app-module-list').append('<li value="'+item.module_code+'" class="licur">'+
								'<img src="'+item.icon_url+'">'+
								'<span>'+item.module_name+'</span><input type="checkbox" checked="checked"/></li>');
						else
							$('#app-module-list').append('<li value="'+item.module_code+'" class="licur">'+
									'<img src="'+item.icon_url+'">'+
								'<span>'+item.module_name+'</span></i></li>');
					}
				}
			});
		}
	});
}

//拒绝申请
function bindrefuseApplyEvent(school){
	$('#btn-refuse').unbind('click').on('click',function(){
		$.myajax({
			url:'schoolAction/updateSchoolApply',
			data:{school_id:school.school_id, status:"007015", school_admin_phone:school.school_admin_phone,
				content:$('#refuse-reason').text(),school_name:school.school_name},
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
		$.myajax({
			url:'schoolAction/updateSchoolApply',
			data:{school_id:school.school_id, status:"007010", school_admin_phone:school.school_admin_phone,
				school_name:school.school_name},
			datatype:'json',
			success:function(data){
				window.location.href = 'application/applicationManager.jsp';
			}
		});
	});
}
