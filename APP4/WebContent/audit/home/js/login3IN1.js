var dataMap = {};
var msk = $('<div id="msk"></div>').css({
	'width':'100%',
	'height':$(window).height(),
	'position':'absolute',
	'top':0,
	'left':0,
	'bottom':0,
	'background':'rgba(0,0,0,.4)',
	'z-index':1
});

function bindSuperAdminBtnClickEvent(){
	$('#btn-SuperAdmin').on('click',function(){
		$('body').append(msk);
		$('.tc-tit').html("超级管理员登录");
		$('#inputUserType').val("003099");
		$('#SuperAdmin-tc').show();
		$('#msk').on('click',function(e){
			e.stopPropagation();
			$(this).remove();
			$('#SuperAdmin-tc').hide();
		});
	});
}
function bindAdminBtnClickEvent(){
	$('#btn-Admin').on('click',function(){
		$('body').append(msk);
		$('.tc-tit').html("学校管理员登录");
		$('#inputUserType').val("003020");
		$('#SuperAdmin-tc').show();
		$('#msk').on('click',function(e){
			e.stopPropagation();
			$(this).remove();
			$('#SuperAdmin-tc').hide();
		});
	});
}

//忘记密码功能
function bindForgetPassWordClickEvent(){
	$('#forget').on('click',function(){
		$('#SuperAdmin-tc').hide();
		$('body').append(msk);
		$('.tc-tit').html("忘记密码");
		$('#fpass_word').click();
		$('#forgetPassword').show();
		$('#msk').on('click',function(e){
			e.stopPropagation();
			$(this).remove();
			$('#forgetPassword').hide();
		});
	});
}

//获取验证码
function getValidateCode(){
	$('#getValidateCode').on('click',function(){
		$.myajax({
			url:"userAction/getValidateCodePhone",
			data:{phone:$('#phone1').val()},
			datatype:'json',
			success:function(data){}
		});
	});
}

function bindAgentBtnClickEvent(){
	$('.btn-agent').on('click',function(){
		$('body').append(msk);
		$('#inputUserType').val("003025");
		$('.tc-tit').html("代理商登录");
		$('#SuperAdmin-tc').show();
		$('#msk').on('click',function(e){
			e.stopPropagation();
			$(this).remove();
			$('#SuperAdmin-tc').hide();
		});
	});
}

function initShowMessage(){
	$.formValidator.initConfig( 
		{formID:"loginForm",submitOnce:true,
//			onSuccess:function(msg,obj,errorlist){
//				alert(222);
//				return true;
//			},
			onError:function(msg,obj,errorlist){
				$("#errorlist").empty();
				$.map(errorlist,function(msg){
					$("#errorlist").append("<li>" + msg + "</li>");
				});
				//alert(msg);
			},
			ajaxPrompt : '有数据正在异步验证，请稍等...'
		});
	$("#phone").formValidator({ onShowText: "输入手机号码",onShow:"", onFocus: "手机号码", onCorrect: "输入正确" }).regexValidator({ regExp: "mobile", dataType: "enum", onError: "手机号码无效" }); 
	$("#password").formValidator({onShowText:"",onShow:"",onFocus:"",onCorrect:"输入正确"}).inputValidator({min:6,empty:{leftEmpty:false,rightEmpty:false,emptyError:"请不要输入空格符号"},onError:"密码无效"});
}


function bindLoginClickEvent(){
	$("#loginBtn").unbind("click").on('click', function() {
		$('#pass_word').val($.md5($("#password").val()));
		$('#loginForm').submit();
	});
}

function bindSubmitCallBackEvent(){
	$(".loginForm").ajaxForm({
	    beforeSend: function() {
	    	toastr.clear();
	    },
	    success: function(data) {
	    	var code = data.code;
	    	if (code != 1) {
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
	    		toastr.info(data.msg,"提示");
	    		return;
	    	}
	    	var user = data.result.data;
	    	localStorage.setItem('user_name', user.user_name);
	    	localStorage.setItem('phone', user.phone);
	    	localStorage.setItem('head_url', user.head_url);
	    	$('#user_type').val($('#inputUserType').val());
	    	$('#phone').val(user.phone);
	    	$('#skipView').submit();
	    }
	});
} 
