var map = {};
var storage = window.localStorage;
var currentPage=1;
var limit=100;
var GradeId ;
var CourseId ;
var VersionId ;
var start_id;
var ExtensionName;
var Total;
var IsCount_size;
var source_id;
var source_name;
var source_type;
var source_data;
var layerIndex;
var module_code;

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
			  "timeOut": "1500",
			  "extendedTimeOut": "1000",
			  "showEasing": "swing",
			  "hideEasing": "linear",
			  "showMethod": "fadeIn",
			  "hideMethod": "fadeOut"
			};
}

//初始化页面
function initSourcePage(){
	GradeId = $('#gradeList li a[class="now"]').attr('GradeId');
	CourseId = $('#courseList li a[class="now"]').attr('CourseId');
	VersionId = $('#versionList li a[class="now"]').attr('VersionId');
	ExtensionName=$('#source_format li #source_format').val();
	loadSource(GradeId,CourseId,VersionId,ExtensionName);
	
}

//取到所有资源格式类型(教材资源)
function loadExtensionName(){
	$('#source_format select').append('<option value="">资源格式</option>'
		    +'<option value="DOC">DOC</option>'
		    +'<option value="DOCX">DOCX</option>'
		    +'<option value="PPT">PPT</option>'
		    +'<option value="PPTX">PPTX</option>'
		    +'<option value="TXT">TXT</option>'
		    +'<option value="MP4">MP4</option>'
		    +'<option value="MP3">MP3</option>'
		    +'<option value="SWF">SWF</option>'
		    +'<option value="JPG">JPG</option>'
		    +'<option value="XLS">XLS</option>'
		    +'<option value="RAR">RAR</option>'
		);
}

//取到视频的资源类型(视频资源)
function loadVideoExtensionName(){
	$('#source_format select').append('<option value="MP4">MP4</option>'
			);
}

//资源格式
function bindExtensionNameChangeEvent(){
	$('#source_format').on('change','li #source_format',function(){
		ExtensionName=$(this).val();
		loadSource(GradeId,CourseId,VersionId,ExtensionName);
	});
}

//加载年级
function bindGradeListClickEvent(){
	$('#gradeList').on('click','li a',function(){
		GradeId =$(this).attr('GradeId');
		loadSource(GradeId,CourseId,VersionId,ExtensionName);
	});
	$(".mainwrap").on("click ",".textbook #gradeList a",function(){
		$(".textbook #gradeList a").removeClass("now");
		$(this).addClass("now");
	});
}

//加载科目
function bindCourseListClickEvent(){
	$('#courseList').on('click','li a',function(){
		CourseId = $(this).attr('CourseId');
		loadSource(GradeId,CourseId,VersionId,ExtensionName);
	});
	$(".mainwrap").on("click" ,".textbook #courseList a",function(){
		$(".textbook #courseList a").removeClass("now");
		$(this).addClass("now");
	});
}
//加载版本
function bindVersionListClickEvent(){
	$('#versionList').on('click','li a',function(){
		VersionId = $(this).attr('VersionId');
		loadSource(GradeId,CourseId,VersionId,ExtensionName);
	});
	$(".mainwrap").on("click" ,".textbook #versionList a",function(){
		$(".textbook #versionList a").removeClass("now");
		$(this).addClass("now");
	});
}

//加载资源列表
function loadSource(GradeId,CourseId,VersionId,ExtensionName){
	$.myajax({
		url:'teachCloudAction/getTeachCloudSource',
		data:{start_id : (currentPage - 1) * limit,Total:Total,limit : limit,page : currentPage ,GradeId:GradeId,CourseId:CourseId,VersionId:VersionId,ExtensionName:ExtensionName},
		datatype:'json',
		type:'post',
		success:function(data){
			$('#source_list').empty();
			var result = data.result;
			var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
            addSourceList(data);
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
               	loadSource(GradeId,CourseId,VersionId,ExtensionName);
                }
            };
            $("#page_pagintor").bootstrapPaginator(options);
            $("#page_pagintor").show();
           // $('#source_list').append(loadSourcePage());
            
            }
	});
}

//加載資源列表
function addSourceList(data){
	var result = data.result.data;
	for (var i in result) {
		var item = result[i];
		sourceListToWed(item);
		
	}
}
//弹框
function _setJob(obj){
	showClassInfo();
	layer.open({
		type: 1,
		area: '820px',
		title: false,
		closeBtn: 0,
		shadeClose: true,
		content: $("#Popup"),
		success: function(res,index){
			layerIndex = index;
			source_id = $(obj).attr("data-source_id");
			source_name = $(obj).attr("data-source_name");
			source_type = $(obj).attr("data-source_type");
			source_data = $(obj).attr("data-source_data");
			resourceUrl = $(obj).attr("data-resourceUrl");
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

//教师推荐资源到班级（WEB端）
function submit(){
	cue();
	$('#save').on('click',function(){
		var receive_list;
		
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
				
				toastr.info("您未选择班级，请先选择班级！","提示:");
				return false;
			}
		var source_list='[{"source_data":'+source_data+',"source_name":"'+source_name+'","source_type":"'+source_type+'","source_id":"'+source_id+'","resourceUrl":"'+resourceUrl+'"}]';
		var remark=$('#source_remark textarea').val();
		if(remark==""||remark=="请在此处填写留言内容"){
			toastr.info("留言不能为空，请输入留言内容！","提示:");
			return false;
		}
		$.myajax({
			url:"teachCloudAction/addSource",
			datatype:"json",
			type:'post',
			data:{source_list:source_list,receive_list:receive_list,remark:remark,module_code:module_code},
			success:function(data){
				window.location.reload();
			}
		});
		
	});
	
}


function sourceListToWed(item){
	var img="";
	if (item.source_type=="DOCX"||item.source_type=="DOC") {
		img='<img src="teachCloud/images/icon3.jpg" class="icon1" />';
	} else if (item.source_type=="MP4"){
		img='<img src="teachCloud/images/icon2.jpg" class="icon1" />';
	} else if (item.source_type=="PPT"||item.source_type=="PPTX"){
		img='<img src="teachCloud/images/icon1.jpg" class="icon1" />';
	} else if (item.source_type=="SWF"){
		img='<img src="teachCloud/images/icon9.jpg" class="icon1" />';
	} else if (item.source_type=="JPG"){
		img='<img src="teachCloud/images/icon8.jpg" class="icon1" />';
	} else if (item.source_type=="TXT"){
		img='<img src="teachCloud/images/icon10.jpg" class="icon1" />';
	} else if (item.source_type=="MP3"){
		img='<img src="teachCloud/images/icon11.jpg" class="icon1" />';
	}
	$('#source_list').append('<tr value="'+item.source_id+'">' +
			'<td style="width:32%">'+img+'<a target="_blank" href="http://jiaoxueyun.classtao.cn:8080/teachCloud/detail.jsp?id='+item.source_id+'&source_name='+item.source_name+'&source_type='+item.source_type+'&resourceUrl='+item.resourceUrl+'&browse_count='+item.browse_count+'&download_count='+item.download_count+'&fav_count='+item.fav_count+'" class="resource">'+item.source_name+'</a></td>' +
			'<td style="width:10%">'+
			'<span>'+'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'<b class="ic ic-star-s-on"/>'+
			'</span>'
			+'</td>' +
			'<td style="width:6%">'+item.browse_count+'</td>' +
			'<td style="width:6%">'+item.praise_count+'</td>' +
			'<td style="width:10%">'+item.resourceSize+'</td>' +
			'<td style="width:20%">'+getDateStr(item.updateTime,"day")+'</td>' +
			'<td style="width:16%">'+'<a id="source'+item.source_id+'"  href="javascript:;"  onclick="_setJob(this)" ><img src="teachCloud/images/icon6.jpg"  class="icon2"/></a>'+'</td></tr>');
			
			$('#source'+item.source_id).attr("data-source_name",item.source_name);
			$('#source'+item.source_id).attr("data-source_id",item.source_id);
			$('#source'+item.source_id).attr("data-source_type",item.source_type);
			$('#source'+item.source_id).attr("data-source_data",item.source_data);
			$('#source'+item.source_id).attr("data-resourceUrl",item.resourceUrl);
	//$('#source'+item.source_id).on('click',_setJob(this));
	
}



