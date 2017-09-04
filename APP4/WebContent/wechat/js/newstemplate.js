var file_upload_action = localStorage.getItem('file_upload_action');

//文本编辑器的上传文件至服务器方法
function sendFile(file, el) {
    var formData = new FormData();
    formData.append("photo", file);
    formData.append("module_code", "009050");
    $.myajax({
    	 type: "POST",   
         url: file_upload_action, 
         cache: false,
         dataType : "JSON",
         data: formData,
         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
         processData: false, // 告诉jQuery不要去处理发送的数据
        success: function (result) {
        	var item = result.result.data;
        	for(var i in item){
        		var imageNode = document.createElement('img');
            	imageNode.setAttribute('src', item[i].file_url);
            	$(el).summernote('insertNode', imageNode);
        	}
        }
    });
}

function bindEditorPicBtnClickEvent() {
	$("#picturebtn").on("click", function() {
		$("#upload_pic").click();
	});
	$('#upload_pic').unbind("change").on('change',function(){ 
		$("#Form").submit();
	});
	$("#Form").ajaxForm({  
		dataType: "json",  
		success: function(data){  
			var item = data.result.data;
			for(var i in item){
				$('#editor').append('<img src="'+item[i].file_url+'">');
			}
		} 
	});
}

//添加图片的隐藏按钮被触发.向后台发送请求并返回数据
function bindUploadPicClickEventX(){
	$('#uploadLogo').change(function(){
		if(!checkUploadImage()){
			return false;
		}
		var formData = new FormData(document.getElementById("form-file"));
		formData.append("module_code", "009050");
	    $.myajax({
	    	 type: "POST",   
	         url: file_upload_action, 
	         cache: false,
	         dataType : "JSON",
	         data: formData,
	         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
	         processData: false, // 告诉jQuery不要去处理发送的数据
	        success: function (result) {
	        	var item = result.result.data;
        		$('#showLogo').attr('src', item[0].file_url);//给main_pic_url赋值
        		$("#itemForm").find("input[name='image_path']").val(item[0].file_url);
	        }
	    });
	});
}

//触发添加图片的隐藏按钮
function bindUploadLogoClickEventY(){
	$('#uploadPic').on('click', function(){//触发点击事件.
		$('#uploadLogo').click();          //触发logo的点击事件
	});
}

function bindAllClickEvent(){
	
	$("#template-table").delegate("button[data-action='editTemplate']", "click", function(){
		var tr = $(this).parents('tr');
		addTemplateToEditBox(tr.attr('template-id'), tr.attr('template-name'));
	});
	
	$("#template-table").delegate("button[data-action='addItem']", "click", function(){
		emptyItemEditBox();
		var templateId = $(this).parents('tr').attr('template-id');
		$("#itemForm").find("input[name='news_template_id']").val(templateId);
		$("button[data-action='removeItem']").addClass('hide');
		$("td[data-lo='items']").addClass('hide');
		$("td[data-lo='edititem']").removeClass('hide');
	});
	
	$("#template-table").delegate("button[data-action='editItem']", "click", function(){
		$("td[data-lo='items']").html('');
		$("td[data-lo='items']").removeClass('hide');
		$("td[data-lo='edititem']").addClass('hide');
		var templateId = $(this).parents('tr').attr('template-id');
		loadItems(templateId);
		selectDefaultItem();
		$("button[data-action='removeItem']").removeClass('hide');
	});

	$("#template-table").delegate("button[data-action='deleteNewsTemplate']", "click", function(){
		var tr = $(this).parents('tr');
		swal({
			title : "您确定要删除这个图文素材模板吗？",
			text : "删除后将无法恢复，请谨慎操作！",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxNewsTemplateAction/deleteTemplate',
				data:{templateId: tr.attr('template-id')},
				datatype:'json',
				success:function(data){
					tr.remove();	
					swal("删除成功！", "您已经永久删除了这个图文素材模板!", "success");
				}
			});
		});
	});
	
	$("#item-edit-modal").delegate('div[data-lo="item"]', "click", function(){
		var itemId = $(this).attr('item-id');
		loadItem(itemId);
		$("div[data-lo='item']").removeClass('itemactive');
		$(this).addClass('itemactive');
		$("td[data-lo='edititem']").removeClass('hide');
	});
	
	
	$("#template-table").delegate("button[data-action='editTemplate']", "click", function(){
		var tr = $(this).parents('tr');
		addTemplateToEditBox(tr.attr('template-id'), tr.attr('template-name'));
	});
	
	$("button[data-action='addTemplate']").click(function(){
		emptyTemplateEditBox();
	});
	
	$("button[data-action='uploadTemplate']").click(function(){
		if($("input[data-name='tmpbox']:checked").size() <= 0){
			alert("至少选择一个模板再上传");
			return false;
		}
		var templateids = '';
		$("input[data-name='tmpbox']:checked").each(function(){
			templateids += $(this).attr('data-id') + ",";
		});
		
		swal({
			title : "您确定要上传这些图文素材吗？",
			text : "上传后的图文素材模板可用于群发消息",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "上传",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxNewsTemplateAction/uploadTemplates',
				data:{templateids: templateids},
				datatype:'json',
				success:function(data){
					window.location.reload();
				}
			});
		});
		
	});
	
	$("button[data-action='saveTemplate']").click(function(){
		var form = $("#templateForm");
		var templatename = form.find("input[name='template_name']").val();
		if(templatename == ""){
			alert('模板名称不能为空....'); 
			return false;
		}
		var data = form.serialize();
		$.myajax({
			url:'wxNewsTemplateAction/saveTemplate',
			data:data,
			datatype:'json',
			success:function(data){
				if(data.success){
					window.location.reload();
				}else{
					alert("图文模板保存失败");
				}
			}
		});
	});
	
	$("button[data-action='removeItem']").click(function(){
		var itemId = $("#itemForm").find("input[name='item_id']").val();
		if(itemId == ''){
			alert("无法删除未指定的图文");
			return false;
		}
		swal({
			title : "您确定要删除这个图文素材吗？",
			text : "删除后将无法恢复，请谨慎操作！",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxNewsTemplateAction/deleteItem',
				data:{itemId: itemId},
				datatype:'json',
				success:function(data){
					$("div[data-lo='item'][item-id='"+itemId+"']").remove();
					selectDefaultItem();
					swal("删除成功！", "您已经永久删除了这个图文素材!", "success");
				}
			});
		});
		
	});
	
	$("button[data-action='saveItem']").click(function(){
		
		var title = $("#itemForm").find("input[name='title']").val();
		if(title == ''){
			alert('标题不能为空....'); 
			$("#itemForm").find("input[name='title']").focus();
			return false;
		}
		var image_path = $("#itemForm").find("input[name='image_path']").val();
		if(image_path == ''){
			alert('请上传封面图片....'); 
			return false;
		}
		
		var orders = $("#itemForm").find("input[name='orders']").val(); 
		if (orders == "" || isNaN(orders)) { 
			alert("请输入有效数字");
			$("#itemForm").find("input[name='orders']").focus();
			return false;
		} 
		
		var display_cover_flag = "0";
		if($("#display_cover_flag").is(':checked')){
			display_cover_flag = "1";
		}
		$("#itemForm").find("input[name='display_cover_flag']").val(display_cover_flag);
		$("#itemForm").find("input[name='content']").val($("#editor").html());
		$("#itemForm").find("input[name='description']").val($("#description").val());
		var data = $("#itemForm").serialize();
		$.myajax({
			type: 'POST',
			url:'wxNewsTemplateAction/saveItem',
			data:data,
			datatype:'json',
			success:function(data){
				if(data.success){
					swal("保存成功！", "您已经成功保存了该图文素材！", "success");
					var itemId = $("#itemForm").find("input[name='item_id']").val();
					if(itemId == ''){
						$('#item-edit-close').click();
					}
				}else{
					alert("图文模板保存失败");
				}
			}
		});
	});
}

function loadTemplates(){
	$.myajax({
		url:'wxNewsTemplateAction/getTemplates',
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				var templates = data.result.data;
				for(var i = 0; i < templates.length; i++){
                    var template = templates[i];
                    var isupload = "<span style='color:#000'>未上传</span>";
                    if(template.upload_status == 1){
                    	isupload = "<span style='color:#000'>正在上传  <img src='wechat/images/uploading.gif' style='width:20px;'></span>";
                    }else if(template.upload_status == 2){
                    	isupload = "<span style='color:#008B00'>已上传</span>";
                    }
                    var tr = '<tr upload-status="' + template.upload_status + '" template-id="' + template.template_id + '" template-name="' +  template.template_name + '">' 
                           + '	<td><input type="checkbox" data-name="tmpbox" data-id="' + template.template_id + '"></td>'
                    	   + '	<td>' + template.template_name + '</td>'
                    	   + '	<td data-lo="statusTxt">' + isupload + '</td>'
                           + '  <td>'
						   + '		<div class="col-sm-10 art-opt-btn">'
						   + '			<button type="button" data-action="editTemplate" class="btn btn-success btn-sm" data-toggle="modal" data-target="#template-edit-modal">编辑</button>'
						   + '			<button type="button" data-action="addItem" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#item-edit-modal">添加图文</button>'
						   + '			<button type="button" data-action="editItem" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#item-edit-modal">编辑图文</button>'
						   + '			<button type="button" data-action="deleteNewsTemplate" class="btn btn-danger btn-sm">删除</button>'
						   + '		</div>'
						   + '	</td>';
                    $("#newstemplatelist").append(tr);
                    setInterval(updateUploadJob, 5000);
                } 
			}
		}
	});
}

function loadItems(templateId){
	$.myajax({
		url:'wxNewsTemplateAction/getItems?templateId=' + templateId,
		datatype:'json',
		async: false,
		success:function(data){
			if(data.success && data.result.total > 0){
				var items = data.result.data;
				for(var i = 0; i < items.length; i++){
                    var item = items[i];
                    var tr = '<div data-lo="item" class="itembox" title="编辑" item-id="' + item.item_id + '">'
						   + '	<div class="itemimg"><img src="' + item.image_path + '" /></div>'
						   + '  <div class="itemtitle"><span>' + item.title + '</span></div>'
				           + '</div>';
                    $("td[data-lo='items']").append(tr);
                } 
			}
		}
	});
}

function loadItem(itemId){
	$.myajax({
		url:'wxNewsTemplateAction/getItem?itemId=' + itemId,
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				var itemVO = data.result.data;
				addItemToEditBox(itemVO);
			}
		}
	});
}

function addTemplateToEditBox(id, name){
	$("#templateForm").find("input[name='template_id']").val(id);
	$("#templateForm").find("input[name='template_name']").val(name);
}

function emptyTemplateEditBox(){
	$("#templateForm").find("input[name='template_id']").val("");
	$("#templateForm").find("input[name='template_name']").val("");
}

function addItemToEditBox(itemVo){
	$("#itemForm").find("input[name='item_id']").val(itemVo.item_id);
	$("#itemForm").find("input[name='title']").val(itemVo.title);
	$("#itemForm").find("input[name='author']").val(itemVo.author);
	$("#itemForm").find("input[name='image_path']").val(itemVo.image_path);
	$("#showLogo").attr('src', itemVo.image_path);
	if(itemVo.display_cover_flag == "1"){
		$("#itemForm").find("#display_cover_flag").prop("checked",true);
	}else{
		$("#itemForm").find("#display_cover_flag").prop("checked",false);
	}
	$("#itemForm").find("input[name='display_cover_flag']").val(itemVo.display_cover_flag);
	$("#itemForm").find("input[name='description']").val(itemVo.description);
	$("#description").val(itemVo.description);
	$("#itemForm").find("input[name='url']").val(itemVo.url);
	$("#itemForm").find("input[name='content']").val(itemVo.content);
	$('#editor').empty().append(itemVo.content);
	$("#itemForm").find("input[name='original_link']").val(itemVo.original_link);
	$("#itemForm").find("input[name='orders']").val(itemVo.orders);
	$("#itemForm").find("input[name='news_template_id']").val(itemVo.news_template_id);
}

function emptyItemEditBox(){
	$("#itemForm").find("input[type='text']").val("");
	$("#itemForm").find("input[type='hidden']").val("");
	$("#itemForm").find("textarea").val("");
	$('#showLogo').attr('src', 'images/gzh_wx.jpg');
	$("#itemForm").find("#display_cover_flag").prop("checked",false);
	$('#editor').empty();
}

function selectDefaultItem(){
	if($("div[data-lo='item']").size() > 0){
		$("div[data-lo='item']").eq(0).trigger("click");
		$("div[data-lo='item']").removeClass('itemactive');
		$("div[data-lo='item']").eq(0).addClass('itemactive');
	}else{
		$('#item-edit-close').click();
	}
}

function updateUploadJob(){
	//获取上传中的模板
	var tr = $("#newstemplatelist").find("tr[upload-status='1']");
	tr.each(function(){
		var templateId = $(this).attr("template-id");
		var _tr = $(this);
		$.myajax({
			url:'wxNewsTemplateAction/getTemplate',
			datatype:'json',
			data:{templateId:templateId},
			success:function(data){
				if(data.success && data.result.total > 0){
					var upload_status = data.result.data.upload_status;
					if(upload_status == "0"){
						_tr.attr("upload_status", "0");
						_tr.find("td[data-lo='statusTxt']").html("<span style='color:#000'>未上传</span>");
					}else if(upload_status == "2"){
						_tr.attr("upload_status", "2");
						_tr.find("td[data-lo='statusTxt']").html("<span style='color:#008B00'>已上传</span>");
					}
				}
			}
		});
	});
}

function checkUploadImage(){
	var filepath = $('#uploadLogo').val();
	var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart, filepath.length).toUpperCase();
    //图片格式验证
    if (ext != ".PNG" && ext != ".GIF" && ext != ".JPG" && ext != ".JPEG") {
      alert("图片限于png,gif,jpeg,jpg格式");
      return false;
    } 
    
    var maxsize = 2*1024*1024;//2M
    var errMsg = "上传的文件不能超过2M";
    var tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过2M，建议使用IE、火狐、谷歌浏览器。";
   
    var  browserCfg = {};
    var ua = window.navigator.userAgent;
    if (ua.indexOf("MSIE") >= 1){
    	browserCfg.ie = true;
    }else if(ua.indexOf("Firefox") >= 1){
    	browserCfg.firefox = true;
    }else if(ua.indexOf("Chrome") >= 1){
    	browserCfg.chrome = true;
    }
    
    var obj_file = document.getElementById("uploadLogo");
    var filesize = 0;
    if(browserCfg.firefox || browserCfg.chrome ){
    	filesize = obj_file.files[0].size;
    }else if(browserCfg.ie){
    	var obj_img = document.getElementById('tempimg');
    	obj_img.dynsrc = obj_file.value;
    	filesize = obj_img.fileSize;
    }else{
    	alert(tipMsg);
    	return true;
    }
    
    if(filesize > maxsize){
    	alert(errMsg);
    	return false;
    }
	return true;
}
