var store = window.localStorage;
var map = {};//参数容器
var storeMap = {};
var isSended = false; //验证码是否发送
var isChecked = false; //是否已经验证过
var successed = false; //是否提交申请
var wait=60;
var file_upload_action = localStorage.getItem('file_upload_action');

//初始化页面
function initAppliactionPage(){
	loadAppTemplateList();//加载app模板列表
	loadAppModuleList();//加载模块列表
	uploadSchoolLogo();//绑定上传学校logo
	bindGetValidateButtonClickEvent();//绑定获取验证码单击事件
}	
//加载模板列表
function loadAppTemplateList(){
	$('#schoolType').unbind('change').on('change',function(){
		var school_type = $(this).val();
		$.myajax({
			url:'templateAction/getTemplateList',
			data:{school_type:school_type},
			datatype:'json',
			success:function(data){	
				$('#app-list').empty();
				var result = data.result.data;
				for(var i in result){
					var item = result[i];
					$('#app-list').append('<li value="'+item.template_id+'">'+
							'<img src="'+item.template_pic_url+'">'+
							'<span>'+item.template_name+'</span><em></em></li>');
					storeMap[item.template_id] = item.module_list;
				}
			}
		});
	})
}
//模板点击事件
function bindTemplateClickEvent(){
	$('#app-list').on('click','li',function(){
		var array = [];
		var module_list = storeMap[$(this).val()];
		for(var i in module_list){
			array.push(module_list[i].module_code);
		}
		selectModuleBytemplateID(array);
	});
}
function selectModuleBytemplateID(array){
	$('#app-module-list').children().each(function(){
		var module_code = $(this).attr('value');
		if($.inArray(module_code, array) == -1){
			$(this).removeClass('licheck');
		}else{
			$(this).addClass('licheck');
		}
	});

}
//加载模块列表
function loadAppModuleList(){
	$.myajax({
		url:'templateAction/getModuleList',
		data:{},
		datatype:'json',
		success:function(data){
			$('#app-module-list').empty();
			var result = data.result.data;
			for(var i in result){
				var item = result[i];
				$('#app-module-list').append('<li value="'+item.module_code+'">'+
						'<img src="'+item.icon_url+'">'+
						'<span>'+item.module_name+'</span><em></em></li>');
			}
			bindTemplateClickEvent();
		}
	});
}

//绑定文件上传功能
function uploadSchoolLogo(){
	$('#uploadLogo').change(function(){
		var formData = new FormData(document.getElementById("form-file"));
		formData.append("module_code", "009030");
	    $.myajax({
	        url: file_upload_action,
	        data: formData,
	        cache: false,
	        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
	        processData: false, // 告诉jQuery不要去处理发送的数据
	        type: 'POST',
	        success: function (result) {
	        	var item = result.result.data;
        		$('#showLogo').attr('src', item[0].file_url);
	        }
	    });
	});
}

//检查设置学校信息
function checkShoolInfo(){
	if($('#schoolType').val() == '0'){
		swal({title : "请选择学校类型...",type : "warning",confirmButtonColor : "#DD6B55",});
		return;
	}
	if(!$('#schoolName').val() || $('#schoolName').val() == ''){
		swal({title : "请填写学校名称...",type : "warning",confirmButtonColor : "#DD6B55",});
		return;
	}
	map['organize_pic_url'] = $('#showLogo').attr('src');
	map['school_type'] = $('#schoolType').val();
	map['school_name'] = $('#schoolName').val();
	map['english_name'] = $('#englishName').val();
	map['school_motto'] = $('#schoolMotto').val();
	return true;
}

//检查设置模块信息
function checkModuleInfo(){
	var _li = $('#app-module-list').children('.licheck');
	var module_codes;
	_li.each(function(){
		if(module_codes)
			module_codes = module_codes+','+$(this).attr('value');
		else
			module_codes = $(this).attr('value');
	});
	map['module_codes'] = module_codes;
	return true;
}

//验证码验证
function checkAdminInfo(){
	if(isChecked) return true;
	if(!isSended){
		swal({title : "请先获取验证码...",type : "warning",confirmButtonColor : "#DD6B55",});
		return;
	}
	var validate_code = $('#validateCode').val().trim();
	$.myajax({
		url:'userAction/getValidateCodePhone',
		async:false,
		data:{validate_code:validate_code, phone:map['school_admin_phone']},
		datatype:'json',
		success:function(data){
			isChecked = true;
		}
	});
	return isChecked;
}

//获取验证码
function bindGetValidateButtonClickEvent(){
	$('#getValidateCode').click(function(){
		var ismobile = "^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
		var phone = $('#adminPhone').val();
		if(!phone.match(ismobile)){
			//alert("请输入有效的手机号码...");
			swal({title : "请输入有效的手机号码...",type : "warning",confirmButtonColor : "#DD6B55",});
			return;
		}
		time(this);
		$.myajax({
			url:'userAction/getValidateCodePhone',
			data:{phone:phone},
			datatype:'json',
			success:function(data){
				map['school_admin_phone'] = phone;//学校管理员电话
				isSended = true;
			}
		});
	});
}
//验证码发送后倒计时1分钟
function time(o) {  
        if (wait == 0) {  
            o.removeAttribute("disabled");            
            o.value="免费获取验证码";  
            wait = 60;  
        } else {  
            o.setAttribute("disabled", true);  
            o.value="重新发送(" + wait + ")";  
            wait--;  
            setTimeout(function() {  
                time(o);  
            },  
            1000)  
        }  
}  

//添加学校申请
function addSchool(){
	if(successed) return true;
	$.myajax({
		url:'schoolApplyAction/addSchool',
		async:false,
		data:map,
		datatype:'json',
		type: 'POST',
		success:function(data){
			successed = true;
			$('#successTips').empty().append('<img src="application/images/icon-success.jpg">'+
				'<p>学校APP审核中，我们将在2个工作日内完成审核。<br>审核结果将以短信的形式发送到手机：<span>'+map['school_admin_phone']+'</span>,请注意查收。</p>');
		}
	});
	return successed;
}
