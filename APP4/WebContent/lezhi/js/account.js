//绑定改变密码按钮点击事件
function bindChangePassBtnClickEvent(){
	$('#reBtn').on('click',function(){
		$("#reBtn").hide();
		$("#vailCode").empty();
		$("#newPassWord").empty();
		$("#reCont").show();
	});	
}
//绑定获取验证码按钮点击事件
function bindGetVailcodeBtnClickEvent(){
	$('#getValidateCode').on('click',function(){
		var phone = localStorage.getItem('phone');
		var val = $('#getValidateCode');
		if (val.attr("disabled")) return;
		countdown=60;
		val.attr("disabled",true);
		settime(val);
		$.myajax({
			url:"../userAction/getValidateCode",
			data:{phone:phone},
			datatype:'json',
			success:function(data){
				alert("验证码已发送!");
			}
		});
	});
}
//绑定提交按钮点击事件
function bindSubmitClickEvent(){
	$('#submitClick').unbind('click').on('click', function(){
		var vailCode = $("#vailCode").val();
  		var passWord = $.md5($("#newPassWord").val());
  		var phone = localStorage.getItem('phone');
		if(vailCode && passWord){
			$.myajax({
				url:"../userAction/resetPassword",
				data:{phone:phone,validate_code:vailCode,pass_word:passWord},
				datatype:'json',
				success:function(data){	
					alert("修改密码成功！");
					$("#vailCode").empty();
					$("#newPassWord").empty();
					$("#reBtn").show();
					$("#reCont").hide();
				}
			});
		}else{
			alert("验证码或密码不能为空!");
		}
	});

}
var countdown=60; 							//设置倒计时最大时间
function settime(val) { 
	if (countdown == 0) { 
		val.html("重新发送验证码");
		val.attr("disabled",false);
	} else { 
		val.html("重新发送（"+countdown+"）");
		--countdown; 
		setTimeout(function(){				//设置倒计时速度时时间走起来
			settime(val);
		},1000)
	} 	
}
