
//发送APP信息短信邀请用户
function sendMsg(){
	$('#sendMsg').on('click',function(){
		var msg=$('#msg').val();//短信内容
		var usertype=$("#user option:selected").attr("value");//接收群体类型（全体家长，所有教师，所有教师和家长）
		$.myajax({
			url:"systemAction/sendMsg",
			datatype:"json",
			data:{text:msg,user_type:usertype},
			success:function(data){
				alert('短信发送成功！');
			}
		});
	});
}    

