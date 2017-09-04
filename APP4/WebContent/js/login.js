var dataMap = {};

function initShowMessage(){
	$.formValidator.initConfig( 
		{formID:"loginForm",submitOnce:true,
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
	if ($.formValidator.pageIsValid('1'))
		$('#loginForm').submit();
	});
}
