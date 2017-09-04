var url = localStorage.getItem('url');
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
/**
 * 判断跳转首页还是重新登录
 */
function bindSkipClickEvent(){
   	if (url != null){
   		toastr.info("您的账号或者页面已过期,请回到首页重新登录!","提示");
   		setShowDuration();
   	} else {
   		toastr.info("自动跳转地址失效,请手动返回登录首页!","提示");
   	}
}

var showDuration=1; 
function setShowDuration() {
	if (showDuration == 0) { 
		window.location.href = url;
	} else { 
		--showDuration; 
		setTimeout(function(){				//设置倒计时速度时时间走起来
			setShowDuration();
		},1000);
	} 	
}