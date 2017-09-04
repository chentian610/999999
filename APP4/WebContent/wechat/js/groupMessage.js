function bindAllClickButton(){
	
	$("#newstemplateTable").delegate("div[data-action='uploadTemplate']", "click", function(){
		var _this = $(this);
		var template_name = _this.find('.itemtitle span').html();
		var show_pic = _this.find('.itemimg img').attr('src');
		var newsTemplateId = _this.attr('data-id');
		var htm = '<div class="media_preview_area pp" style="width:320px;">'
				+ '	  <div class="appmsg multi editing">'
				+ '	   <div id="js_appmsg_preview" class="appmsg_content">'
				+ ' 	   <div id="appmsgItem1" class="js_appmsg_item has_thumb">'
				+ '		       <div class="appmsg_info">'
				+ '			        <h3>' + template_name + '</h3>'
				+ '			        <em class="appmsg_date"></em>'
				+ '			   </div>'
				+ ' 		   <div class="cover_appmsg_item">'
				+ '					<h4 class="appmsg_title">'
				+ '			        	<a target="_blank" onclick="return false;" href="javascript:void(0);"></a>'
				+ '		           </h4>'
				+ '			       <div class="appmsg_thumb_wrp">'
				+ '			           <img class="js_appmsg_thumb appmsg_thumb" src="' + show_pic + '">'
				+ '				   </div>'
				+ '			   </div>'
			    + ' 	    </div>'
			    + ' 	</div>'
			    + '   </div>'
			    + '</div>'
			    + '<input type="hidden" name="newsTemplateId" value="' + newsTemplateId + '">';
		$("#contentEditor").html(htm);
		$("button[data-action='close-newstemplate-editor']").trigger('click');
	});
	
	//切换到文本消息
	$("a[data-action='text']").click(function(){
		$("#contentEditor").html('<textarea class="wz" placeholder="请输入文本内容" name="textcontent"></textarea>');
		$("input[name='msgtype']").val("text");
	});
	
	//切换到图文消息
	$("a[data-action='newstemplate']").click(function(){
		loadAllUploadNewsTemplate();
		$("input[name='msgtype']").val("mpnews");
	});
	
	//群发
	$("#sendButton").click(function(){
		var msgtype = $("input[name='msgtype']").val();
		var textcontent = '';
		var newsTemplateId = '';
		if(msgtype == "text"){
			textcontent = $("textarea[name='textcontent']").val();
		}else if(msgtype = "mpnews"){
			newsTemplateId = $("input[name='newsTemplateId']").val();
		}else{
			alert("消息内容有误，请重新编辑");
			return false;
		}
		
		$.myajax({
			url:'wxGroupMessageAction/sendMessage',
			datatype:'json',
			data:{msgtype:msgtype,textcontent:textcontent,newsTemplateId:newsTemplateId},
			success:function(data){
				swal("请求成功！", "您已经成功执行了群发请求，请耐心等待公众号上生效!", "success");
			}
		});
		
	});
	
}

function loadAllUploadNewsTemplate(){
	$.myajax({
		url:'wxNewsTemplateAction/loadAllUploadNewsTemplate',
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				var templates = data.result.data;
				var htm = "<tr>";
				for(var i = 0; i < templates.length; i++){
					var template = templates[i];
					var td = '<td>'
						   + '	<div class="itembox" data-action="uploadTemplate" data-id="' + template.platform_message_id + '">'
						   + '		<div class="itemimg">'
						   + '			<img src="' + template.showPic + '"/>'
						   + '		</div>'
						   + '		<div class="itemtitle"><span>' + template.template_name + '</span></div>'
				           + '	</div>'
						   + '</td>';
					htm += td;
					if((i + 1)/4 == 0){
						htm += '</tr>'
							+  '<tr>';
					}
                } 
				htm += '</tr>';
				$("#newstemplateTable").html(htm);
			}
		}
	});
}