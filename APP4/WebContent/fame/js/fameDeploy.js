var file_upload_action = localStorage.getItem('file_upload_action');
//触发按钮点击事件
function bindUploadPicClickEvent() {
	$('#uploadPic').on('click', function(){
		$('#uploadLogo').click();//触发隐藏的from事件，
	});
}

//绑定文件上传功能
function bindUploadLogoClickEvent(){
	$('#uploadLogo').unbind('change').on('change',function(){
		var formData = new FormData(document.getElementById("form-file"));
		formData.append("module_code", "009031");
	    $.myajax({
	        url: file_upload_action,
	        cache: false,
	        data: formData,
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

//添加名人信息
function bindAddButtonClickEvent(){
	$('#addButton').unbind('click').on('click', function(){		
		//验证生日日期格式
		var BIRTHDAY_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;
		var birthday = $('#birthday').val();
		 if(!birthday.match(BIRTHDAY_FORMAT)){
			 swal({title : "请输入正确生日日期格式!",type : "warning",confirmButtonColor : "#DD6B55",});
				return;
		  }
		 //验证毕业时间日期格式
		 var GRADUTION_FORMAT = /^[0-9]{4}-[0-1]?[0-9]{1}-[0-3]?[0-9]{1}$/;
		var graduation_date = $('#graduationDate').val();
		if(!graduation_date.match(GRADUTION_FORMAT)){
			 swal({title : "请输入正确毕业时间格式!",type : "warning",confirmButtonColor : "#DD6B55",});
				return;
		  }	
		var name = $('#FameName').val();		
		var description = $('#description').val();	
		//var create_date = $('#createDate').val();
		var create_by = $('#createBy').val();
		if(name == ''){
			alert('标题不能为空....'); return;
		}
		var head_url;
		if ($('#showLogo').attr('src') == 'images/gzh_wx.jpg'){
			alert('不能为默认，请上传名人图片'); return;
		}else{
			head_url = $('#showLogo').attr('src');
		}
		$.myajax({
			url:'fameAction/addFame',
			type: 'POST',
			data:{name:name, description:description, head_url:head_url,
				birthday:birthday, graduation_date:graduation_date},
			datatype:'json',
			success:function(data){
				swal({
					title : "添加成功！",
					text : "请到主页查看文章详情！"
				}, function() {
					//清除添加框内容
					initDeployPage();
				});
			}
		});  
	});
}
//初始化部署页面
function initDeployPage(){
	$('#createBy').val('');
	$('#birthday').val('');
	$('#graduationDate').val('');
	$('#description').val('');
	$('#showLogo').val('');
	$('#FameName').val('');
	//$('#createDate').val('');
	$('#FameName option').eq(0).prop('selected', true);
}
