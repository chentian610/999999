var bedroom_id;
var bedroom_name;
var currentPage=1;
var limit=10;
var search_text;
var class_id;

//显示该校寝室
function showBedroom(){
	$.myajax({
		url:"bedroomAction/showBedroom",
		success:function(data){
			var result=data.result.data;
			var sex;
			for(var i in result){
				if(result[i].sex==0) sex='男'; else sex='女';
				var tr='<tr><td>'+result[i].bedroom_name+'</td><td>'+sex+'</td><td>'+result[i].count+
				'</td><td><a class="btn btn-sm btn-req delete" value="'+result[i].bedroom_id+'">删除</a></td></tr>';
				$('#bedroom').append(tr);
			}
			deleteBedroom();
		}
	});
}

//新增寝室
function saveBedroom(){
	$('#save').on('click',function(){
		var bedroomname=$('#bedroomname').val();
		var bedroomsex=$('#bedroomsex option:selected').val();
		if(bedroomname=='' || bedroomsex=='性别'){
			alert('请填写完整！');
		}else{
			var sex;
			$.myajax({
				url:"bedroomAction/insertBedroom",
				datatype:'json',
				data:{'bedroom_name':bedroomname,'sex':bedroomsex},
				success:function(data){
					var bedroomid=data.result.data.bedroom_id;
					if(bedroomsex==0) sex='男'; else sex='女';
					$('#bedroom').append('<tr><td>'+bedroomname+'</td><td>'+sex+'</td><td>0</td><td>'+
							'<a class="btn btn-sm btn-req delete" value="'+bedroomid+'">删除</a></td></tr>');
					$("#nBtn").show();
					$("#nDiv").hide();
					deleteBedroom();
				}
			});
		}
	});
}

//删除寝室
function deleteBedroom(){
	$('.delete').unbind('click').on('click',function(){
		var obj=$(this).parent().parent();
		var bedroomid=$(this).attr('value');
		$.myajax({
			url:'bedroomAction/deleteBedroom',
			datatype:'json',
			data:{'bedroom_id':bedroomid},
			success:function(data){
				obj.remove();
			}
		});
	});
}

//显示该校寝室
function showBedroomMana(){
	$.myajax({
		url:"bedroomAction/showBedroom",
		success:function(data){
			var result=data.result.data;
			for(var i in result){
				var tr='<li value="'+result[i].bedroom_id+'" code="'+result[i].bedroom_name+'"><a>'+
				result[i].bedroom_name+'（'+result[i].count+'）</a></li>';
				$('#bedroom').append(tr);
			}
			showStudentBed();
			$('#bedroom li:first').click();
		}
	});
}

//显示个寝室人员
function showStudentBed(){
	$('#bedroom').on('click','li',function(){
		var bedroomid=$(this).val();
		bedroom_id=bedroomid;
		bedroom_name=$(this).attr('code');
		$('#bedroom li a').removeClass('active');
		$(this).find('a').addClass('active');
		$.myajax({
			url:"bedroomAction/getBedroomStuList",
			datatype:'json',
			data:{'bedroom_id':bedroomid},
			success:function(data){
				$('#count').text('共'+data.result.total+'人');
				$('#student').empty();
				var result=data.result.data;
				for(var i in result){
					var sex;
					if(result[i].sex==0) sex='男';
					else if(result[i].sex==1) sex='女';
					else sex='未设置';
					var tr='<tr id="student'+result[i].student_id+'"><td>'+result[i].bed_code+'</td><td>'+
					result[i].class_name+'</td><td>'+result[i].student_code+'</td><td>'+result[i].student_name+
					'</td><td>'+sex+'</td><td><a class="btn btn-sm btn-req delete" value="'+result[i].student_id+
                    '" onclick="_delItem(this)">删除</a></td></tr>';
					//<a class="btn btn-sm btn-req btn-bdr"><i class="fa fa-bars"></i></a>
					$('#student').append(tr);
				}
			}
		});
	});
}

//删除寝室人员
function _delItem(obj){
		var sid=$(obj).attr('value');
		layer.confirm('确认删除？', {
			btn: ['确定','取消'] //按钮
		}, function(){
			$.myajax({
				url:"bedroomAction/deleteStudentPosition",
				datatype:"json",
				data:{bedroom_id:bedroom_id,student_id:sid},
				success:function(data){
					$('#student'+sid).remove();
					layer.msg('删除成功', {icon: 1});
				}
			});
		});
}

//跳转到添加页面
function addBedStudent(){
	$('#addStudent').on('click',function(){
		window.location.href='lezhi/addBedroomStudent.jsp?bedroom_id='+bedroom_id+'&bedroom_name='+bedroom_name;
	});
}

//显示寝室已有学生
function showStudent(){
	$.myajax({
		url:"bedroomAction/getBedroomStuList",
		datatype:'json',
		data:{'bedroom_id':bedroom_id},
		success:function(data){
			$('#student').empty();
			var result=data.result.data;
			for(var i in result){
				var sex;
				if(result[i].sex==0) sex='男';
				else if(result[i].sex==1) sex='女';
				else sex='未设置';
				var tr='<tr><td><input type="checkbox" name="student" id="sid'+result[i].student_id+'" code="1"></td><td>'+
				result[i].class_name+'</td><td>'+result[i].student_code+'</td><td>'+result[i].student_name+
				'</td><td>'+sex+'</td><td><a class="btn btn-normal"><i class="fa fa-bars"></i></a></td></tr>';
				$('#bedStudent').append(tr);
			}
		}
	});
}

//显示班级
function showClass(){
	$.myajax({
		url:"classAction/getClassOfManager",
		datatype:'json',
		success:function(data){
			var result=data.result.data;
			for(var i in result){
				var classVO=result[i];
				if (classVO.is_graduate==1) continue;
				var li='<option value="'+classVO.class_id+'">'+classVO.class_name+'</option>';
				$('#classname').append(li);
			}
			showClassPerson();
			$('#classname option:first').change();
            }
	});
}

//显示班级人员
function showClassPerson(){
	$('#classname').on('change',function(){
		class_id=$(this).val();
		currentPage=1;
		showClassPeople();
	});
}

//显示班级人员
function showClassPeople(){
	if(search_text==undefined) search_text="";
	$.myajax({
		url:"classAction/getStudentListNotBedroom",
		data:{start_id:(currentPage-1)*limit,limit:limit,page:currentPage,student_name:search_text,class_id:class_id},
		datatype:'json',
		success:function(data){
			spage(data);//分页
            }
	});
}

//分页
function spage(data){
	var result = data.result;
	if(result==null) return;
    var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
    addSToWeb(data);
    if (pageCount<2) {
    	$("#page_pagintor1").hide();
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
       	showClassPeople();
        }
    };
    $("#page_pagintor1").bootstrapPaginator(options);
    $("#page_pagintor1").show();
}

function addSToWeb(data){
	$('#studentList').empty();
	var result = data.result.data;
	for(var i in result) {
		var studentVO = result[i];
		appendSToWeb(studentVO);
	}
}

function appendSToWeb(studentVO){
	if(studentVO.sex==0) studentsex='男'; 
	else if(studentVO.sex==1) studentsex='女';
	else studentsex='未设置';
	var li='<tr><td><input type="checkbox" name="astudent" id="sid_'+studentVO.student_id+'" code="'+
	studentVO.student_code+'"></td><td>'+studentVO.class_name+'</td><td>'+studentVO.student_code+'</td><td>'+
	studentVO.student_name+'</td><td>'+studentsex+'</td><td><a class="btn btn-normal">'+
	'<i class="fa fa-bars"></i></a></td></tr>';
	$('#studentList').append(li);
}

//搜索
function search(){
	$('#search').on('click',function(){
		currentPage=1;
		search_text=$('#teachername').val();
		showClassPeople();
	});
}

//全选
function allSelect(){
	$('#studentSelect').on('click',function(){
		$('#bedStudent input').prop({"checked":true});
	});
	$('#allstudentSelect').on('click',function(){
		$('#studentList input').prop({"checked":true});
	});
}

//全选删除
function allDelete(){
	$('#delete').on('click',function(){
		if ($(":checkbox[name=student]:checked").size() == 0 && $(":checkbox[name=astudent]:checked").size() == 0) {
			layer.msg("您没有选择要删除的学生！",{icon:0}); return false;
		}
		$(":checkbox[name=student]:checked").each(function(){
			var sid=$(this).attr('id').substring(3);
			oneDelete(sid);//为了多次执行success中的语句
		});
		$(":checkbox[name=astudent]:checked").each(function(){
			var suid=$(this).attr('id').substring(4);
			oneDelete(suid);//为了多次执行success中的语句
		});
		$(":checkbox[name=student]:checked").each(function(){
			$(this).parent().parent().remove();
		});
		$(":checkbox[name=astudent]:checked").each(function(){
			$(this).parent().parent().remove();
		});
	});
}

//为了多次执行success中的语句
function oneDelete(sid){
	$.myajax({
		url:"bedroomAction/deleteStudentPosition",
		datatype:"json",
		data:{bedroom_id:bedroom_id,student_id:sid},
		success:function(data){
			showClassPeople();
		}
	});
}

//添加到左侧
function addLeft(){
	$('#addLeft').on('click',function(){
		if ($(":checkbox[name=astudent]:checked").size() == 0) {
			layer.msg("您还没有选择学生哦！",{icon:0}); return false;
		}
		var jsondata;
		$(':checkbox[name=astudent]:checked').each(function(){
			var student_code=$(this).attr('code');
			if(jsondata==undefined){
				jsondata='[{"bedroom_id":'+bedroom_id+',"bedroom_name":"'+bedroom_name+'","student_code":"'+student_code+'"}';
			}else{
				jsondata=jsondata+',{"bedroom_id":'+bedroom_id+',"bedroom_name":"'+bedroom_name+'","student_code":"'+student_code+'"}';
			}
		});
		jsondata=jsondata+']';
		$.myajax({
			url:"bedroomAction/addStudentListForBed",
			datatype:"json",
			data:{item_list:jsondata},
			success:function(data){
				$(":checkbox[name=astudent]:checked").each(function(){
					var student_id =$(this).attr("id").substring(4);
					var tr='<tr>'+$('#sid_'+student_id).parent().parent().html()+'</tr>';
					$('#bedStudent').append(tr);
					$(this).parent().parent().remove();
				});
				$('#studentList input').prop({"checked":false});
				$('#content').scrollTop( $('#content')[0].scrollHeight );//让滚动条自动滚到底部
			}
		});
	});
}