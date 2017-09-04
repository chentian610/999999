var contact_id;
var studentsex;
var currentPage=1;
var limit=10;
var search_text;
var class_id;
var studentIDList;

//显示兴趣班学生
function groupPerson(){
	$.myajax({
		url:"userAction/getStudentListGroup",
		datatype:'json',
		data:{contact_id:contact_id,user_type:'003010',user_name:""},
		success:function(data){
			$('#teacherList').empty();
			var result = data.result.data;
			for(var i in result) {
				var studentVO = result[i];
				appendToWeb(studentVO);
			}
		}
	});
}

function appendToWeb(studentVO){
	if(studentVO.sex==0) studentsex='男';
	else if(studentVO.sex==1) studentsex='女';
	else studentsex='未设置';
	var tr='<tr><td><input type="checkbox" name="student" id="sid'+studentVO.student_id+'" code="1"></td><td>'+
	studentVO.class_name+'</td><td>'+studentVO.student_code+'</td><td>'+studentVO.student_name+'</td><td>'+
	studentsex+'</td><td><a class="btn btn-normal"><i class="fa fa-bars"></i></a></td></tr>';
	$('#teacherList').append(tr);
}

//显示班级
function showClass(){
	$.myajax({
		url:"classAction/getClassOfManager",
		datatype:'json',
		data:{grade_id:grade_id},
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
		url:"classAction/getStudentListOfManagerGroup",
		data:{start_id:(currentPage-1)*limit,limit:limit,page:currentPage,student_name:search_text,class_id:class_id,contact_id:contact_id},
		datatype:'json',
		success:function(data){
			spage(data);//分页
            }
	});
}

//班级人员分页
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
	else if (studentVO.sex==1) studentsex='女';
	else studentsex='未设置';
	var li='<tr><td><input type="checkbox" name="astudent" id="sid_'+studentVO.student_id+'" code="'+
	studentVO.student_name+'"></td><td>'+studentVO.class_name+'</td><td>'+studentVO.student_code+'</td><td>'+
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
		$('#teacherList input').prop({"checked":true});
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
			if (studentIDList==undefined) studentIDList=sid;
			else studentIDList=studentIDList+','+sid;
		});
		$(":checkbox[name=astudent]:checked").each(function(){
			var suid=$(this).attr('id').substring(4);
			if (studentIDList==undefined) studentIDList=suid;
			else studentIDList=studentIDList+','+suid;
		});
		var arr=studentIDList.split(',');
		for (var i=0;i<arr.length;i++) {
			$.myajax({
				url: "contactAction/deleteContactByGorup",
				datatype: "json",
				data: {contact_id: contact_id, student_id: arr[i]},
				success: function (data) {
					showClassPeople();
				}
			});
		}
		$(":checkbox[name=student]:checked").each(function(){
			$(this).parent().parent().remove();
		});
		$(":checkbox[name=astudent]:checked").each(function(){
			$(this).parent().parent().remove();
		});
	});
}

//添加到左侧
function addLeft(){
	$('#addLeft').on('click',function(){
		if ($(":checkbox[name=astudent]:checked").size() == 0) {
			layer.msg("您还没有选择学生哦！",{icon:0}); return false;
		}
		var jsondata;
		$(":checkbox[name=astudent]:checked").each(function(){
			var student_id =$(this).attr("id").substring(4);
			var student_name=$(this).attr("code");
			if (jsondata == undefined) {
				jsondata = '[{"user_type":"003010","user_name":"' + student_name + '","student_id":' + student_id + ',"user_id":0}';
			} else {
				jsondata = jsondata + ',{"user_type":"003010","user_name":"' + student_name + '","student_id":' + student_id + ',"user_id":0}';
			}
		});
		jsondata = jsondata + ']';
		$.myajax({
			url: "contactAction/addContactListByGroup",
			datatype: "json",
			data: {contact_id: contact_id, item_list: jsondata},
			success: function (data) {
				$(":checkbox[name=astudent]:checked").each(function(){
					var student_id =$(this).attr("id").substring(4);
					 var tr='<tr>'+$('#sid_'+student_id).parent().parent().html()+'</tr>';
					 $('#teacherList').append(tr);
					 $(this).parent().parent().remove();
				});
				$('#studentList input').prop({"checked":false});
				$('#content').scrollTop( $('#content')[0].scrollHeight );//让滚动条自动滚到底部
			}
		});
	});
}

// 				if ($('#teacherList tr').length>count) {
// 					layer.msg('当前添加人数已超过班级人数限制！',{icon:0});
// 					return false;
// 				}