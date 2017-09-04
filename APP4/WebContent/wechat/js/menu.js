function bindAllClickEvent(){
	//一级菜单展开关闭
	$("#menu-table").delegate("a[data-action='child-menu']", "click", function(){
		var tr = $(this).parents('tr');
		var menuid = tr.attr('menu-id');
		if($(this).find('span').hasClass('fa-angle-right')){//展开
			var loaded = tr.attr('child-loaded');
			if(loaded == 0){
				//加载子菜单
				tr.attr('child-loaded','1');
				loadChildMenus(menuid);
			}else{
				$("tr[level='" + menuid+"-child']").removeClass('hide');
			}
			$(this).find('span').removeClass('fa-angle-right').addClass('fa-angle-down');
		}else{//合上
			$(this).find('span').removeClass('fa-angle-down').addClass('fa-angle-right');
			$("tr[level='" + menuid + "-child']").addClass('hide');
		}
	});
	
	$("#menu-table").delegate("button[data-action='edit']", "click", function(){
		loadSelectParentMenus();
		var menuid = $(this).parents('tr').attr('menu-id');
		$.myajax({
			url:'wxMenuAction/getMenuById',
			data:{menuId: menuid},
			datatype:'json',
			success:function(data){
				var menuVO = data.result.data;				
				addMenuToEditBox(menuVO);
			}
		});
	});
	
	$("button[data-action='add']").click(function(){
		loadSelectParentMenus();
		emptyEditBox();
	});
	
	$("button[data-action='sync']").click(function(){
		swal({
			title : "您确定要同步所有菜单吗？",
			text : "执行同步操作将直接生效到绑定的公众号",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "同步",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxMenuAction/doSyncMenu',
				success:function(data){
					swal("同步成功！", "您已经成功执行了同步菜单操作，请耐心等待公众号上生效!", "success");
				}
			});
		});
	});
	
	$("button[data-action='save']").click(function(){
		
		var parent_id = $("#parent_id").val();
		var menu_id = $("#menu_id").val();
		
		if(menu_id == 0){
			if(parent_id == 0 && $("tr[child-loaded]").size() >= 3){
				alert("一级菜单最多只能创建3个");
				return false;
			}else if($("tr[level='" + parent_id + "-child']").size() >= 5){
				alert("二级菜单最多只能创建5个");
				return false;
			}
		}
		
		var name = $("#name").val();
		if(name == ''){
			alert('菜单名称不能为空....'); 
			return false;
		}
		var data = $("form").serialize();
		$.myajax({
			url:'wxMenuAction/saveMenu',
			data:data,
			datatype:'json',
			success:function(data){
				if(data.success){
					window.location.reload();
				}else{
					alert("菜单信息保存失败");
				}
			}
		});
		
	});
	
	$("#menu-table").delegate("button[data-action='delete']", "click", function(){
		var tr = $(this).parents('tr');
		var textContent = "删除后将无法恢复，请谨慎操作！";
		if(tr.attr("child-loaded")){
			textContent = "删除一级菜单后其子菜单也将一起删除，请谨慎操作！";
		}
		swal({
			title : "您确定要删除这个菜单吗？",
			text : textContent,
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "删除",
			closeOnConfirm : false
		},function(){
			$.myajax({
				url:'wxMenuAction/deleteMenu',
				data:{menu_id: tr.attr('menu-id')},
				datatype:'json',
				success:function(data){
					tr.remove();	
					swal("删除成功！", "您已经永久删除了这个菜单!", "success");
				}
			});
		});
	});
	
	$("input[name='msg_type']").bind('change', function(){
		var type = $("input[name='msg_type']:checked").val();
		var target = $("input[name='msg_type']:checked").attr("target");
		if(target == "view"){
			$("div[type-target='view']").removeClass('hide');
			$("div[type-target='click']").addClass('hide');
		}else if(target == "click"){
			loadTemplates(type);
			$("div[type-target='click']").removeClass('hide');
			$("div[type-target='view']").addClass('hide');
		}else{
			$("div[type-target='click']").addClass('hide');
			$("div[type-target='view']").addClass('hide');
		}
		$("#type").val(target);
	});
}

function loadTemplates(msgType){
	if(msgType == "info"){
		var opts = '<option value="022005">校园风采</option>'
			     + '<option value="022010">党员建设</option>'
			     + '<option value="022015">校园新闻</option>'
			     + '<option value="022020">校友之窗</option>'
			     + '<option value="022025">招生招聘</option>';
		$("#template_id").html(opts);
	}else{
		$.myajax({
			url:'wxMenuAction/gettemplate?msgType=' + msgType,
			datatype:'json',
			async: false,
			success:function(data){
				if(data.success && data.result.total > 0){
					var templates = data.result.data;
					var opts = '';
					if(msgType == 'news'){
						for(var i = 0; i < templates.length; i++){
							var template = templates[i];
							opts += '<option value="' + template.template_id + '">' + template.template_name + '</option>';
						}
					}else if(msgType == 'vote'){
						for(var i = 0; i < templates.length; i++){
							var template = templates[i];
							opts += '<option value="' + template.vote_id + '">' + template.title + '</option>';
						}
					}
					
					$("#template_id").html(opts);
				}else{
					$("#template_id").html('');
				}
			}
		});
	}
}

function loadSelectParentMenus(){
	$.myajax({
		url:'wxMenuAction/getTopMenus',
		datatype:'json',
		async: false,
		success:function(data){
			if(data.success && data.result.total > 0){
				var topMenus = data.result.data;
				var opts = '<option value="0">(无)</option>';
				for(var i = 0; i < topMenus.length; i++){
                    var menu = topMenus[i];
                    opts += '<option value="' + menu.menu_id + '">' + menu.name + '</option>';
                } 
				$("#edit-modal").find("#parent_id").html(opts);
			}
		}
	});
}

function loadTopMenus(){
	$.myajax({
		url:'wxMenuAction/getTopMenus',
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				var topMenus = data.result.data;
				for(var i = 0; i < topMenus.length; i++){
                    var menu = topMenus[i];
                    var type = "";
                    if(menu.msg_type == "news"){
                    	type = "图文消息";
                    }else if(menu.msg_type == "vote"){
                    	type = "校园投票";
                    }else if(menu.msg_type == "info"){
                    	type = "校园资讯";
                    }else if(menu.msg_type == "url"){
                    	type = "外部网页链接";
                    }else if(menu.msg_type == "parent_client"){
                    	type = "家长端链接";
                    }
                    
                    var orders = "一级菜单" + menu.orders;
                    
                    var tr = '<tr level="' + menu.orders + '" menu-id="' + menu.menu_id + '" child-loaded="0">'
                           + '	<td>'
						   + '		<a href="javascript:void(0);" data-action="child-menu"><span class="fa fa-angle-right"></span>&nbsp;' + menu.name + '</a>'
						   + '	</td>'
						   + '	<td>' + type + '</td>'
						   + '	<td>' + orders + '</td>'
						   + '  <td>'
						   + '		<div class="col-sm-5 art-opt-btn">'
						   + '			<button type="button" data-action="edit" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit-modal">编辑</button>'
						   + '			<button type="button" data-action="delete" class="btn btn-danger btn-sm">删除</button>'
						   + '		</div>'
						   + '	</td>'
                           + '</tr>';
                    $("#menulist").append(tr);
                } 
			}
		}
	});
}

function loadChildMenus(pid){
	$.myajax({
		url:'wxMenuAction/getChildMenus',
		data:{parentId:pid},
		datatype:'json',
		success:function(data){
			if(data.success && data.result.total > 0){
				var topMenus = data.result.data;
				var trs = '';
				for(var i = 0; i < topMenus.length; i++){
                    var menu = topMenus[i];
                    var type = "";
                    if(menu.msg_type == "news"){
                    	type = "图文消息";
                    }else if(menu.msg_type == "vote"){
                    	type = "校园投票";
                    }else if(menu.msg_type == "info"){
                    	type = "校园资讯";
                    }else if(menu.msg_type == "url"){
                    	type = "外部网页链接";
                    }
                    var orders = "子菜单" + menu.orders;
                    
                    trs += '<tr level="' + pid + '-child" menu-id="' + menu.menu_id + '">'
                           + '	<td>'
						   + '		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + menu.name
						   + '	</td>'
						   + '	<td>' + type + '</td>'
						   + '	<td>' + orders + '</td>'
						   + '  <td>'
						   + '		<div class="col-sm-5 art-opt-btn">'
						   + '			<button type="button" data-action="edit" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#edit-modal">编辑</button>'
						   + '			<button type="button" data-action="delete" class="btn btn-danger btn-sm">删除</button>'
						   + '		</div>'
						   + '	</td>'
                           + '</tr>';
                } 
				$('tr[menu-id="'+pid+'"]').after(trs);
			}
		}
	});
}

function addMenuToEditBox(menuVo){
	$("#menu_id").val(menuVo.menu_id);
	$("#parent_id").val(menuVo.parent_id);
	$("#name").empty().val(menuVo.name);
	$("#type").val(menuVo.type);
	$("#url").empty().val(menuVo.url);
	if(menuVo.msg_type != null){
		$("input[name='msg_type'][value='" + menuVo.msg_type + "']").prop('checked', true);
		$("input[name='msg_type']").trigger("change");
		if(menuVo.msg_type != 'parent_client'){
			$("#template_id").find('option[value="' + menuVo.template_id + '"]').attr("selected", true);
		}
	}
	$("#orders").val(menuVo.orders);
}

function emptyEditBox(){
	$("#menu_id").val(0);
	$("#parent_id").val(0);
	$("#name").val("");
	$("#type").val("click");
	$("input[name='msg_type'][value='info']").prop('checked', true);
	loadTemplates("info");
	$("#url").val("");
	$("#orders").val(1);
}