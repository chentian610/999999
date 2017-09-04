var storage = window.localStorage;
var currentPage=1;
var limit=10;
var user_id;
var file_name;
var user_type;
var file_type_this;
var current_parent_id=0;
var list=new Array();
var iconArray = ["doc","xls","docx","xlsx","ppt","pptx","vsd","vsdx","mp3","txt","rar","zip"];
var picArray = ["jpg","jpeg","png","gif","bmp","svg"];
var mp4Array = ["wmv","avi","mpeg","mod","rm","mp4"];
var file_upload_action = localStorage.getItem('file_upload_action');

//提示框
function cue(){
	toastr.options = {
			  "closeButton": true,
			  "debug": false,
			  "progressBar": true,
			  "positionClass": "toast-top-center",
			  "onclick": null,
			  "showDuration": "400",
			  "hideDuration": "1000",
			  "timeOut": "3000",
			  "extendedTimeOut": "1000",
			  "showEasing": "swing",
			  "hideEasing": "linear",
			  "showMethod": "fadeIn",
			  "hideMethod": "fadeOut"
			};
}

//加载云盘文件
function loadDisk(parent_id){
	$.myajax({
		url:'cloudDiskAction/getCloudDiskList',
		data:{parent_id:parent_id,start_id : (currentPage - 1) * limit,limit : limit,page : currentPage ,user_id:user_id,file_name:$('#searchInput').val(),user_type:user_type},
		datatype:'json',
		type:'post',
		success:function(data){
			$('#disk_list').empty();
			var result = data.result;
			var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
			addDiskListToWeb(data);
			current_parent_id = parent_id;
			if (pageCount<2) {
	            	$("#page_pagintor").hide();
	            	return;
	            }
	            var options = {
	                bootstrapMajorVersion: 3, //版本
	                currentPage: currentPage, //当前页数
	                totalPages: pageCount, //总页数
	                alignment:"center",
	                itemTexts: function (type, page, current) {
	                    switch (type) {
	                        case "first":
	                            return "首页";
	                        case "prev":
	                            return "上一页";
	                        case "next":
	                            return "下一页";
	                        case "last":
	                            return "末页";
	                        case "page":
	                            return page;
	                    }
	                },//点击事件，用于通过Ajax来刷新整个list列表
	                onPageClicked: function (event, originalEvent, type, page) {
	               	currentPage=page;
	               	loadDisk(file_name);
	                }
	            };	      
	            $("#page_pagintor").bootstrapPaginator(options);
	            $("#page_pagintor").show();           
		}
	});
}

//循环遍历结果
function addDiskListToWeb(data){
	$('#disk_list').empty();
	var result = data.result.data;
	for (var i in result) {
		var item = result[i];
		addDiskToWeb(item);
	}
}
//输出查询结果
function addDiskToWeb(item) {
	var file_type = (item.file_type)?item.file_type.toLowerCase():"";
	var iconClass;
	if (!file_type) iconClass = "dir-small";//文件夹
	else if ($.inArray(file_type,picArray)>-1) iconClass = "fileicon-small-pic";
	else if ($.inArray(file_type,mp4Array)>-1) iconClass = "fileicon-small-video";
	else iconClass = ($.inArray(file_type,iconArray)==-1)?"default-small":"fileicon-small-"+file_type;
	var	size=(file_type)?(Math.round(item.file_size/1024/1024*100)/100)+"M":"-";
	var textClass = (file_type)?"":"folder-line";
	$('#disk_list').append('<tr id="disk'+item.file_id+'" vlaue="'+item.file_id+'">'
		+	'<td >'
		+		'<input class = "td-left" type="checkbox" name="file" data-file_id="'+item.file_id+'" data-file_name="'+item.file_name+'" data-file_type="'+item.file_type+'" data-file_url="'+item.file_url+'"/>'
		+		'<div class = "td-left fileicon '+iconClass+'"></div><span class = "'+textClass+'"  data-file_id="'+item.file_id+'">'+item.file_name+'</span>'
		+		'<div class="td-right">'
		+			'<div class="icon4">'
		+				'<img src="teacherDisk/images/icon16.png" />'
		+				'<div class="icon4-circle"><span>+</span></div>'
		+			'</div>'
		+			'<div class="icon5">'
		+				'<img src="teacherDisk/images/icon17.png"/>'
		+				'<ul class="icon5-dowm">'
		+					'<li class="download" id="da" value="'+item.file_id+'" file_url="'+item.file_url+'"  file_type="'+item.file_type+'"> '+"下载"+'</li>'
		+					'<li class="move" value="'+item.file_id+'" file_type="'+item.file_type+'" file_name="'+item.file_name+'">移动到</li>'
		+					'<li class="rename" value="'+item.file_id+'" file_type="'+item.file_type+'">'+"重命名"+'</li>'
		+					'<li class="delete" value="'+item.file_id+'" file_name="'+item.file_name+'">'+"删除"+'</li>'
		+				'</ul>'
		+			'</div>'
		+		'</div>'
		+	'</td>'
		+	'<td>'+size+'</td><td>'+getDateStr(item.update_date,"minute")+'</td>'
		+'</tr>');
}

//文件搜索
function bindSearchButtonClickEvent(){
	$('#search-button').on('click', function(){
		loadDisk($('#search-disk').val());
	});
}
//新建文件夹
function bindNewFolderButtonEvent(){
	cue();
	$('#new-button').on('click',function(){
		var folder_name = $('#anew-folder').val();
		if(folder_name==""||folder_name==undefined){
			toastr.info("请先输入创建的文件夹名称！","提示:");
			return false;
		}
		$.myajax({
			url:'cloudDiskAction/addCloudDisk',
			datatype:'json',
			type:'post',
			data:{file_name:folder_name,parent_id:current_parent_id,file_type:""},
			success:function(data){
				var item = data.result.data;
				addDiskToWeb(item);
				$(".newfile-btn").hide();
				$(".sub").hide();
				$("#new-folder").show();
			}
		});
	});
}
//删除文件
function bindDeleteBtnClickEvent(){
	$('#disk_list').on('click','.delete',function(){
				var obj=$(this).parent().parent();
				var file_id=$(this).attr('value');
				var file_name=$(this).attr('file_name');
				file_id = '[{"file_id":"'+file_id+'","file_name":"'+file_name+'"}]';
				$.myajax({
					url:'cloudDiskAction/deleteCloudDisk',
					datatype:'json',
					data:{file_ids:file_id},
					success:function(data){
						$(obj).parents('tr').remove();
					}
				});
			});
	
}
//重命名文件
function bindRenameClickEvent(){
	$('#disk_list').on("click",".rename",function(){
		var $item = $(this).parents('tr');
		var $name = $(">td:first>span",$item);
		var name = $name.text();
		if (name.indexOf('.')>-1)
			name=name.substring(0,name.indexOf('.'));
		var file_id = $(this).attr('value');
		var file_type = $(this).attr('file_type');
		$name.text("");
		$name.append('<input class="form-control" type="text" file_id="'+file_id+'" value="'+name+'"><a file_id="'+file_id+'" file_type="'+file_type+'" href="javascript:;" class="btn btn-sm btn-req btn-sub" onclick="_submitEdit(this)">确认</a>').children("b").hide();
		if (file_type="") 
			$name.removeClass("folder-line");
	});
}

//重命名确定操作
function _submitEdit(obj){
	var $item = $(obj).parents('tr');
	var $name = $(">td:first>span",$item);
	var file_name = $("input[type='text']",$name).val();
	var file_id = $(obj).attr('file_id');
	var file_type = $(obj).attr('file_type');
	$.myajax({
		url:'cloudDiskAction/updateCloudDiskName',
		datatype:'json',
		type:'post',
		data:{file_name:file_name,file_id:file_id,file_type:file_type},
		success:function(data){
			$(obj).parents('tr').remove();
			var item = data.result.data;
			addDiskToWeb(item);
		}
		
	});

	
}
//显示待选择的班级名称
function showClassInfo(){
	var user_id;
	var school_id;
	$.myajax({
		url:"classAction/getClassListOfTeacher",
		datatype:"json",
		type:'post',
		data:{user_id:user_id,school_id:school_id,is_filtered:1},
		success:function(data){
			 $('#classname').empty();
			var result=data.result.data;
			for(var i in result){
				var TeacherVO=result[i];
				var li;
				if ('011005'==TeacherVO.team_type)
					li='<label><input data-user_type="003005" data-group_id="'+TeacherVO.grade_id+'" data-team_id="'+TeacherVO.class_id+'" data-team_type="'+TeacherVO.team_type+'" type="checkbox" name="classname">'+TeacherVO.class_name+'</label>';
				else if ('011015'==TeacherVO.team_type)
					li='<label><input data-user_type="003005" data-group_id="0" data-team_id="'+TeacherVO.contact_id+'" data-team_type="'+TeacherVO.team_type+'" type="checkbox" name="classname">'+TeacherVO.class_name+'</label>';
				$('#classname').append(li);
			}
		}
	}); 
}
//发送到班级
function bindSendToClassClickEvent(){
	cue();
	$('#sendToClass').on('click',function(){
		var file_list;
		var receive_list;
		//选中的文件
		$(":checkbox[name=file]:checked").each(function(){
			var file_id =$(this).attr("data-file_id");
			var file_name = $(this).attr("data-file_name");
			var file_type = $(this).attr("data-file_type");
			if(file_type!=""){
				if(file_list==undefined){
					file_list = '[{"file_id":"'+file_id+'","file_name":"'+file_name+'","file_type":"'+file_type+'"}';
				} else {
					file_list = file_list+',{"file_id":"'+file_id+'","file_name":"'+file_name+'","file_type":"'+file_type+'"}';
				}
			}
		});
		file_list=file_list+']';
		if(file_list==undefined+']'){
			toastr.info("请先选择文件！","提示:");
			return false;
		}
	//选中的班级
		$(":checkbox[name=classname]:checked").each(function(){
			var user_type =$(this).attr("data-user_type");
			var group_id = $(this).attr("data-group_id");
			var team_id = $(this).attr("data-team_id");
			var team_type = $(this).attr("data-team_type");
			if(receive_list==undefined){
				receive_list = '[{"user_type":"'+user_type+'","group_id":"'+group_id+'","team_id":"'+team_id+'","team_type":"'+team_type+'"}';
			} else {
				receive_list = receive_list+',{"user_type":"'+user_type+'","group_id":"'+group_id+'","team_id":"'+team_id+'","team_type":"'+team_type+'"}';
			}
		});
		receive_list=receive_list+']';
		if(receive_list==undefined+']'){
			toastr.info("请先选择班级！","提示:");
			return false;
		}
		$.myajax({
			url:"cloudDiskAction/sendCloudDisk",
			datatype:"json",
			type:'post',
			data:{file_list:file_list,receive_list:receive_list},
			success:function(data){
				window.location.reload();
			}
		});
	});
}

//已选中课件
function bindCheckFileClickEvent(){
	$('#checkFile').on('click',function(){
		$('#course_list').empty();
		$(":checkbox[name=file]:checked").each(function(){
			var file_name =$(this).attr("data-file_name");
			var file_type = $(this).attr("data-file_type");
			if(file_type!=""){
			 $('#course_list').append('<li>'+file_name+'<img src="teacherDisk/images/icon18.png" class="icon7" id="file'+file_name+'"/>'+'</li>');
			 $('.icon7').on('click',function(){
				$('#file'+file_name+'').prop("checked",false);
			 });
			}
		});
	});
}

//触发按钮点击事件
function bindUploadFileClickEvent(){
	$('#uploadFile').on('click',function(){
		$('#uploadLogo').click();
	});
}


//上传文件
function uploadFileClickEvent(){
	$('#uploadLogo').change(function(){
		var formData = new FormData(document.getElementById("form-file"));
		 $.myajax({
		        url :file_upload_action,
		        cache: false,
		        data: formData,
		        contentType: false, // 告诉jQuery不要去设置Content-Type请求头
		        processData: false, // 告诉jQuery不要去处理发送的数据
		        type: 'POST',
		        success: function (result) {
		        	addCloudDisk(result);
		        }
		    });
	});
}

//上传的文件添加到数据库
function addCloudDisk(obj){
	var result = obj.result.data;
	var item = result[0];
	var file_name = item.file_real_name;
	var file_url = item.file_url;
	var file_type = item.file_type;
	var file_size = item.file_size;
	$.myajax({
		url:"cloudDiskAction/addCloudDisk",
		datatype:'json',
		type:'post',
		data:{file_name:file_name,parent_id:current_parent_id,file_url:file_url,file_type:file_type,file_size:file_size},
		success:function(data){
			var item = data.result.data;
			addDiskToWeb(item);
		}
	});
}

//文件下载 
function bindDownloadClickEvent(){
	$('#disk_list').on('click',".download",function(){
		var file_url = $(this).attr('file_url');
		var file_type=$(this).attr("file_type");
		if(file_type==""){
			toastr.info("请选择文件进行下载！","提示:");
			return false;
		}
		window.open(file_url);
	});
}

//移动到选择文件夹
function fileMove(obj){
		cue();
		parent_id = $(obj).attr("file_id");//文件夹的ID，作为移动后文件的parent_id
		file_id_old = file_id_old;//文件本身的ID
	    file_type = file_type_this;
	    file_name = file_name_this;
	    $.myajax({
			url:'cloudDiskAction/updateCloudDisk',
			datatype:'json',
			type:'post',
			data:{file_id:file_id_old,parent_id:parent_id},
			success:function(data){
				window.location.reload();
			}
		});
}
//获取所有的文件夹
function bindGetAllFolderEvent(){
	$('#disk_list').on('click',".move",function(){
		 file_id_old =$(this).attr('value');//所点击文件的ID
		 file_type_this = $(this).attr('file_type');//文件本身的类型
		 file_name_this = $(this).attr('file_name');//点击文件的name;
		 var file_type="";
		$.myajax({
			url:'cloudDiskAction/getCloudDiskList',
			datatype:'json',
			type:'post',
			data:{user_id:user_id,file_type:file_type},
			success:function(data){
				$('#file_list').empty();
				fileAllToWeb(data);
			}
		});
	});
}
//对查询到的文件夹进行遍历
function fileAllToWeb(data){
	var result = data.result.data;
	for (var i in result) {
		 var item = result[i];
		fileListToWeb(item);
	}
}
//取出所有的文件夹
function fileListToWeb(item){
	 $('#file_list').append('<li id="'+file_id_old+'"> <a id="file'+item.file_id+'" href="javascript:;" onclick="fileMove(this)">'+item.file_name+'</a></li>');
	 $('#file'+item.file_id).attr("file_id",item.file_id); 
}
//选中后的删除
function bindFileDeleteCheckEvent(){
	cue();
	var file_ids;
	$('#deleteCheck').on('click',function(){
		var file_id;
		$(":checkbox[name=file]:checked").each(function(){
			var obj=$(this).parent().parent();
			file_id = $(this).attr("data-file_id");
			file_name = $(this).attr("data-file_name");
			if(file_ids==undefined){
				file_ids = '[{"file_id":"'+file_id+'","file_name":"'+file_name+'"}';
			} else {
				file_ids = file_ids+',{"file_id":"'+file_id+'","file_name":"'+file_name+'"}';
			}
		});
		file_ids=file_ids+']';
		$.myajax({
			url:'cloudDiskAction/deleteCloudDisk',
			datatype:'json',
			data:{file_ids:file_ids},
			success:function(data){
				window.location.reload();
			}
		});
	});
}
//获取所有的文件夹(选中的文件)
function bindFileAllCheck(){
	$('#moveCheck').on('click',function(){
		var file_type="";
		$.myajax({
			url:'cloudDiskAction/getCloudDiskList',
			datatype:'json',
			type:'post',
			data:{user_id:user_id,file_type:file_type},
			success:function(data){
				$('#file_list').empty();
				fileAllCheckToWeb(data);
			}
		});
	});
}
//对查询到的文件夹进行遍历(选中的移动)
function fileAllCheckToWeb(data){
	var result = data.result.data;
	for (var i in result) {
		var item = result[i];
		fileListCheckToWeb(item);
	}
}
function fileListCheckToWeb(item){
	 $('#file_list').append('<li> <a id="file'+item.file_id+'" href="javascript:;" onclick="fileMoveCheck(this)">'+item.file_name+'</a></li>');
	 $('#file'+item.file_id).attr("file_id",item.file_id); 
}
//选中的移动到
function fileMoveCheck(obj){
	cue();
	$(":checkbox[name=file]:checked").each(function(){
			file_id = $(obj).attr("file_id");//文件夹的ID，作为移动后文件的parent_id
			file_id_old =$(this).attr("data-file_id");//文件本身的ID;
			file_type_this = $(this).attr("data-file_type");
			$.myajax({
				url:'cloudDiskAction/updateCloudDisk',
				datatype:'json',
				type:'post',
				data:{file_id:file_id_old,parent_id:file_id},
				success:function(data){
					window.location.reload();
				}
			});
	});
}

//选中的多个文件下载
function bindMorefileDownloadCheck(){
	$('#downloadCheck').on('click',function(){
		$(":checkbox[name=file]:checked").each(function(){
			var file_url = $(this).attr("data-file_url");
			var file_type=$(this).attr("data-file_type");
			if(file_type==""){
				toastr.info("请选择文件进行下载！","提示:");
				$('#disk_list input').prop({"checked":false});
				indent=0;
				$(".icon6-circle").text(indent);
				$(".course-btn2 span").text(indent);
				$(".disk-txt2 span").text(indent);
				return false;
			}
			window.open(file_url);
		});
	});
}
//绑定文件夹点击事件
function bindFolderCheckEvent(){
	$('#disk_list').on("click",'span[class="folder-line"]',function(){
		var last_parent_id = current_parent_id;
		var file_id = $(this).attr("data-file_id");
		$("#a"+last_parent_id).addClass("index-folder");
		$("#folderIndex").append('<span class="separator-gt">&gt;</span>');
		$("#folderIndex").append('<a id = "a'+file_id+'" >'+$(this).text()+'</a>');
		loadDisk(file_id);
	});
}

//抬头的文件列表点击事件
function bindFolderIndexClickEvent(){
	$('#folderIndex').on("click",'a[class="index-folder"]',function(){
		var file_id = $(this).attr("id").replace("a","");
		loadDisk(file_id);
		$(this).nextAll().remove();
		$(this).removeClass("index-folder");
	});
}

