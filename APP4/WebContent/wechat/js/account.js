function bindSaveButton(){
	 $("#saveButton").click(function(){
		 swal({
			title : "您确定保存配置信息吗？",
			text : "请仔细核查配置参数，错误的修改将导致微信功能失效，请谨慎操作",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "保存",
			closeOnConfirm : false
		},function(){
			 var data = $("form").serialize();
			 $.myajax({
					url:'wxAccountAction/saveAccount',
					type: 'POST',
					data: data,
					datatype:'json',
					success:function(data){
						swal({
							title : "操作提示",
							text : "公众账号信息已经保存！"
						}, function() {
						});
					}
			});  
		});
	 });
 }
 function initData(){
		$.myajax({
			url:'wxAccountAction/getAccount',
			datatype:'json',
			success:function(data){
				if(data.success && data.result.total > 0){
					var acc = data.result.data;
					$("#account_name").val(acc.account_name);
					$("#account_token").val(acc.account_token);
					$("#weixin_accountid").val(acc.weixin_accountid);
					$("#account_email").val(acc.account_email);
					$("#account_desc").val(acc.account_desc);
					$("#account_appid").val(acc.account_appid);
					$("#account_appsecret").val(acc.account_appsecret);
					$("#account_id").val(acc.account_id);
					
					$("#account_type").val(acc.account_type);
					$("#is_effective").val(acc.is_effective);
					$("#auth_status").val(acc.auth_status);
					
				}
			}
		});
	};