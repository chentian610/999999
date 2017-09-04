var class_id;
var currentPage=1;
var limit=10;
var studentsex;
var search_text;
var class_name;
var grade_id;
var contact_name;
var contact_id;
var studentID;
var MODEL_DOWNLOAD_URL=localStorage.getItem('model_download_url');
var file_upload_action = localStorage.getItem('file_upload_action');

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
				var li='<li value="'+classVO.class_id+'" code="'+classVO.class_name+'" vode="'+classVO.grade_id+
				'"><a>'+classVO.class_name+'</a></li>';
				$('#classname').append(li);
			}
			showClassPerson();
			$('#classname li:first').click();
            }
	});
}

//显示班级人员
function showClassPerson(){
	$('#classname').on('click','li',function(){
		$('#classname li a').removeClass('active');
		$(this).find('a').addClass('active');
		class_id=$(this).val();
		class_name=$(this).attr('code');
		grade_id=$(this).attr('vode');
		currentPage=1;
		showClassPeople();
	});
}

//显示班级人员
function showClassPeople(){
	if(search_text==undefined) search_text="";
	$.myajax({
		url:"classAction/getStudentOfManager",
		data:{start_id:(currentPage-1)*limit,limit:limit,page:currentPage,student_name:search_text,class_id:class_id},
		datatype:'json',
		type:'post',
		success:function(data){
			$('#teacherCount').text("共"+data.result.total+"人");
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
       	showClassPeople();
        }
    };
    $("#page_pagintor").bootstrapPaginator(options);
    $("#page_pagintor").show();
}

function addSToWeb(data){
	$('#teacherList').empty();
	var result = data.result.data;
	for(var i in result) {
		var studentVO = result[i];
		appendSToWeb(studentVO);
	}
}

function appendSToWeb(studentVO){
	if(studentVO.sex==0) studentsex='男'; 
	else if(studentVO.sex==1)  studentsex='女';
	else studentsex='未设置';
	var li='<tr id="sid'+studentVO.student_id+'"><td><span>'+studentVO.student_code+'</span></td><td><span>'+
	studentVO.student_name+'</span></td><td><span>'+studentsex+'</span></td><td>'+
	'<a href="javascript:;" class="btn btn-sm btn-req delete" onclick="_delItem(this)" value="'+
	studentVO.student_id+'">删除</a><a href="javascript:;" class="btn btn-sm btn-req" onclick="_editItem(this)" value="'+studentVO.student_id+
	'">编辑</a></td></tr>';
	$('#teacherList').append(li);
}

//搜索
function search(){
	$('#search').on('click',function(){
		currentPage=1;
		search_text=$('#teachername').val();
		showClassPeople();
	});
}

//添加学生页面
function addShow(){
	$('#add').on('click',function(){
		window.location.href='lezhi/addStudent.jsp?class_name='+class_name+'&class_id='+class_id+'&grade_id='+
		grade_id;
	});
}

//添加学生页面的添加框
function showInsert(){
	var tr='<tr><td><input type="text" class="form-control" placeholder="输入学号"></td><td>'+
	'<input type="text" class="form-control" placeholder="输入姓名"></td><td>'+
	'<select class="form-control"><option >选择性别</option><option value="0">男</option><option value="1">女'+
	'</option></select></td><td><a id="nextInsert" class="btn btn-sm btn-req insert">确定</a></td></tr>';
	$('#nextInsert').append(tr);
}

//添加学生页面的确定按钮
function nextInsert(){
	$('#nextInsert').on('click','.insert',function(){
		var now=$(this).parent().parent();
		var now1=$(this);
		var studentcode=now.find('input:first').val();
		var studentname=now.find('input:eq(1)').val();
		if($.trim(studentcode)=='' || $.trim(studentname)==''){
			layer.msg('请输入学生姓名和学号！', {icon: 0});
		}else{
		$.myajax({
			url:"classAction/getStudentOfManager",
			data:{student_name:"",class_id:class_id},
			datatype:'json',
			success:function(data){
				var a=new Array();
				var result=data.result.data;
				for(var i in result){
					a.push(result[i].student_code);
				}
				var b=$.inArray(studentcode,a);
				if(b!=-1){
					layer.msg("已存在学号"+studentcode+"的学生！", {icon: 0});
					now.remove();
					showInsert();
				}else{
					now1.hide();
					showInsert();
				}
	            }
		});
		}
	});
}

//添加学生（保存）
function save(){
	$('#save').on('click',function(){
		// if ($('#nextInsert tr').size()==1){
		// 	layer.msg('请按确定按钮添加学生后保存！', {icon: 0});
		// 	return false;
		// } else{
			var jsondata;
			for(var i=0;i<$('#nextInsert tr').size();i++){
			var studentcode=$('#nextInsert tr').eq(i).find('input').eq(0).val();
			var studentname=$('#nextInsert tr').eq(i).find('input').eq(1).val();
			var sex=$('#nextInsert tr').eq(i).find('option:selected').attr("value");
			if (sex==undefined) sex='-1';
			if(jsondata==undefined){
				jsondata='[{"grade_id":'+grade_id+',"class_id":'+class_id+',"student_name":"'+studentname+'","student_code":"'+studentcode+'","sex":"'+sex+'"}';
			}else{
				jsondata=jsondata+',{"grade_id":'+grade_id+',"class_id":'+class_id+',"student_name":"'+studentname+'","student_code":"'+studentcode+'","sex":"'+sex+'"}';
			}
		}
			jsondata=jsondata+']';
			$.myajax({
				url:"classAction/addStudentList",
				datatype:"json",
				data:{item_list:jsondata},
				success:function(data){
					window.location.href='lezhi/studentRoster.jsp';
					if (data.result.data!="")
						alert(data.result.data+'该学号已存在，故添加失败，其它学生信息添加成功！');
					
				}
			});
		//}
		//func($('#nextInsert tr').length-1);
	});
}

//由于for循环速度快，异步赶不上，造成添加缺失，故采用递归方式
function func(times){
	if(times <=0){
		window.location.href='lezhi/studentRoster.jsp';
		return false; 
	}
	var studentcode=$('#nextInsert tr').eq(times-1).find('input').eq(0).val();
	var studentname=$('#nextInsert tr').eq(times-1).find('input').eq(1).val();
	var sex=$('#nextInsert tr').eq(times-1).find('option:selected').attr("value");
	$.myajax({
		url:"classAction/addStudent",
		datatype:"json",
		data:{grade_id:grade_id,class_id:class_id,student_name:studentname,student_code:studentcode,
			sex:sex},
		success:function(data){
			times --;
			func(times); //递归调用
		}
	});
	}

//修改学生信息
function _submitEdit(obj){
	var $item = $(obj).parents('tr');
	var $name = $(">td:first",$item), $sid = $(">td:eq(1)",$item), $sex = $(">td:eq(2)",$item), $btns = $(obj).parent();

	var name = $("input",$name).val(),sid = $("input",$sid).val(),sex = $("option:selected",$sex).text();
	var studentsex=$("option:selected",$sex).val();
	if($.trim(name) == "" || $.trim(sid) == ""){
		layer.msg('请填写完整',{icon: 0});
		return false;
	}
	 $.myajax({
		url:"userAction/updateStudent",
		datatype:"json",
		data:{student_name:sid,student_code:name,sex:studentsex,student_id:studentID},
		success:function(data){
			if (data.result.code==0) jConfirm(data.result.msg, '警告');
			else{
				$("span",$name).text(name).show();//学号
				$("span",$sid).text(sid).show();//姓名
				$("span",$sex).text(sex).show();
				$("input",$name).remove();
				$("input",$sid).remove();
				$("select",$sex).remove();

				$('a:eq(0),a:eq(1)',$btns).show();
				$('a:eq(2),a:eq(3)',$btns).remove();
			}
		}
	});
}

//删除学生
function _delItem(obj){
	var student_id=$(obj).attr('value');
	layer.confirm('确认删除？', {
		btn: ['确定','取消'] //按钮
	}, function(){
		$(obj).parents("tr").fadeOut();
		$.myajax({
			url:"userAction/deleteStudent",
			datatype:"json",
			data:{student_id:student_id},
			success:function(data){
				$('#sid'+student_id).remove();
				layer.msg('删除成功', {icon: 1});
			}
		});
	});
} 

//学生兴趣班
function showGroup(){
	$.myajax({
		url:"contactAction/getContactGroupListOfManager",
		datatype:"json",
		data:{user_type:"003010"},
		success:function(data){
			var result=data.result.data;
			for(var i in result){
				var li='<li id="c'+result[i].contact_id+'" value="'+result[i].contact_name+'"><a>'+
				result[i].contact_name+'</a></li>';
				$('#groupmenu').append(li);
			}
			showGroupPerson();
			$('#groupmenu li:first').click();
		}
	});
}

//显示兴趣班学生
function showGroupPerson(){
	$('#groupmenu').on('click','li',function(){
		$('#groupmenu li a').removeClass('active');
		$(this).find('a').addClass('active');
		contact_id=$(this).attr('id').substring(1);
		contact_name=$(this).attr('value');
		currentPage=1;
		groupPerson();
	});
}

//显示兴趣班学生
function groupPerson(){
	if(search_text==undefined) search_text="";
	$.myajax({
		url:"userAction/getStudentListGroup",
		datatype:'json',
		data:{contact_id:contact_id,user_type:'003010',start_id:(currentPage-1)*limit,limit:limit,
			page:currentPage,user_name:search_text},
		success:function(data){
			$('#studentCount').text("共"+data.result.total+"人");
			page(data);//分页
		}
	});
}

//分页
function page(data){
	var result = data.result;
	if(result==null) return;
    var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
    addsToWeb(data);
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
       	groupPerson();
        }
    };
    $("#page_pagintor1").bootstrapPaginator(options);
    $("#page_pagintor1").show();
}

//加载列表
function addsToWeb(data){
	$('#teacherList').empty();
	var result = data.result.data;
	for(var i in result) {
		var studentVO = result[i];
		appendsToWeb(studentVO);
	}
	deleteStudentGroup();
}

function appendsToWeb(studentVO){
	if(studentVO.sex==0)  studentsex='男'; 
	else if(studentVO.sex==1) studentsex='女';
	else studentsex='未设置';
	var tr='<tr id="id'+studentVO.student_id+'"><td>'+studentVO.class_name+'</td><td>'+studentVO.student_code+
	'</td><td>'+studentVO.student_name+'</td><td>'+studentsex+'</td><td>'+
	'<a class="btn btn-sm btn-req deleteGroup" id="sid_'+studentVO.student_id+'">删除</a></td></tr>';	
	$('#teacherList').append(tr);
}

//搜索
function searchStudentOfGroup(){
	$('#searchGroup').on('click',function(){
		currentPage=1;
		search_text=$('#searchGroup_text').val();
		groupPerson();
	});
}

//增加兴趣班学生（跳转页面）
function addGroupStudent(){
	$('#addGroupStudent').on('click',function(){
		window.location.href='lezhi/addGroupStudent.jsp?contact_name='+contact_name+'&contact_id='+contact_id;
	});
}

//删除兴趣班中学生
function deleteStudentGroup(){
	$('.deleteGroup').on('click',function(){
		var student_id=$(this).attr('id').substring(4);
		$.myajax({
			url:"contactAction/deleteContactByGorup",
			datatype:"json",
			data:{contact_id:contact_id,student_id:student_id},
			success:function(data){
				$('#id'+student_id).remove();
			}
		});
	});
}

//上传文件
function bindSBtnClickEvent() {
	$("#importbtn").on("click", function() {
		layer.confirm('确认将学生信息导入到'+class_name+'？', {
			btn: ['确定','取消'] //按钮
		}, function(){
			$("#upload_pic").click();
		});
		
	});
	$('#upload_pic').unbind("change").on('change',function(){
			var formData = new FormData(document.getElementById("Form"));
		    $.myajax({
		    	 type: "POST",   
		         url: "fileAction/uploadFile", 
		         cache: false,
		         dataType : "JSON",
		         data: formData,
		         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
		         processData: false, // 告诉jQuery不要去处理发送的数据
		         success: function (result) {
		        	var item = result.result.data;
		        	exportStudentList(item[0].file_url);
		        }
		    });
		});
}

//添加学生
function exportStudentList(url){
		$.myajax({
			url:'excelAction/importStudentNew',
			data:{url:url,grade_id:grade_id,class_id:class_id},
			datatype:'json',
			success:function(data){
				layer.msg(data.result.data, {icon: 1});
				search_text="";
				showClassPeople();
				//清空input，避免同名文件提交不执行
				$('#upload_pic').replaceWith('<input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>');
				$('#upload_pic').unbind("change").on('change',function(){ 
					var formData = new FormData(document.getElementById("Form"));
				    $.myajax({
				    	 type: "POST",   
				         url: "fileAction/uploadFile", 
				         cache: false,
				         dataType : "JSON",
				         data: formData,
				         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
				         processData: false, // 告诉jQuery不要去处理发送的数据
				         success: function (result) {
				        	var item = result.result.data;
				        	exportStudentList(item[0].file_url);
				        }
				    });
				});
			}
	});
}

//下载花名册模版
function exportSExcel(){
	$("#exportbtn").on("click",function(){
		window.open(MODEL_DOWNLOAD_URL);
	});
}

//上传文件(兴趣班)
function bindIBtnClickEvent() {
	$("#Iimportbtn").on("click", function() {
		layer.confirm('确认将学生信息导入到'+contact_name+'？', {
			btn: ['确定','取消'] //按钮
		}, function(){
			$("#upload_pic").click();
		});
		
	});
	$('#upload_pic').unbind("change").on('change',function(){
			var formData = new FormData(document.getElementById("Form"));
		    $.myajax({
		    	 type: "POST",   
		         url: "fileAction/uploadFile", 
		         cache: false,
		         dataType : "JSON",
		         data: formData,
		         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
		         processData: false, // 告诉jQuery不要去处理发送的数据
		         success: function (result) {
		        	var item = result.result.data;
		        	exportInterestStudentList(item[0].file_url);
		        }
		    });
		});
}

//添加学生(兴趣班)
function exportInterestStudentList(url){
		$.myajax({
			url:'excelAction/importInterestStudent',
			data:{url:url,contact_id:contact_id},
			datatype:'json',
			success:function(data){
				layer.msg(data.result.data, {icon: 1});
				search_text="";
				groupPerson();
				//清空input，避免同名文件提交不执行
				$('#upload_pic').replaceWith('<input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>');
				$('#upload_pic').unbind("change").on('change',function(){ 
					var formData = new FormData(document.getElementById("Form"));
				    $.myajax({
				    	 type: "POST",   
				         url: "fileAction/uploadFile", 
				         cache: false,
				         dataType : "JSON",
				         data: formData,
				         contentType: false, // 告诉jQuery不要去设置Content-Type请求头
				         processData: false, // 告诉jQuery不要去处理发送的数据
				         success: function (result) {
				        	var item = result.result.data;
				        	exportInterestStudentList(item[0].file_url);
				        }
				    });
				});
			}
	});
}