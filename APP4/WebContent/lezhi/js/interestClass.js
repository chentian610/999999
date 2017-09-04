var is_active;
var grade_id=0;
var course;
var currentPage=1;
var limit=5;
//学生兴趣班
function showGroup(){
	$.myajax({
		url:"contactAction/getInterestClassList",
		datatype:"json",
		data:{grade_id:grade_id,is_active:is_active,course:course,is_my:isMy,start_id:(currentPage-1)*limit,limit:limit,page:currentPage,},
		success:function(data){
			spage(data);
		}
	});
}

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
			showGroup();
		}
	};
	$("#page_pagintor1").bootstrapPaginator(options);
	$("#page_pagintor1").show();
}

function addSToWeb(data) {
	$('#groupmenu').empty();
	var result=data.result.data;
	var date=new Date();
	for(var i in result){
		var li;
		var start_date=getDateStr(result[i].start_date,'day');
		var end_date=getDateStr(result[i].end_date,'day');
		var apply_start_date=getDateStr(result[i].apply_start_date,'second');
		var apply_end_date=getDateStr(result[i].apply_end_date,'second');
		if (result[i].end_date<date){
			li='<div class="qcontent_item"><span>'+result[i].contact_name+'</span>' +
				'<table class="table table-bordered"><tr><td class="item_left">科目</td><td>'+
				result[i].course_name+'</td></tr><tr><td class="item_left">老师</td><td>'+
				(result[i].teacher_name==null?'':result[i].teacher_name)+'</td></tr><tr><td class="item_left">起止时间</td>' +
				'<td>'+start_date+'--'+end_date+'</td></tr><tr><td class="item_left">备注</td><td>'+
				result[i].remark+'</td></tr><tr><td class="item_left">班级人数</td><td>'+
				result[i].exist_count+'人</td></tr></table>' +
				'<button class="btn btn-default btn-disable" disabled="disabled">已过期</button><button code="'+result[i].contact_id+'-'+result[i].is_grab+
				'-'+result[i].contact_name+'-'+result[i].grade_id+'" class="btn btn-default detail" value="'+start_date+'--'+end_date+
				'" style="margin-left:10px;">查看详情</button></div>';
		} else if (result[i].apply_end_date<date){//报名结束
			li='<div class="qcontent_item"><span>'+result[i].contact_name+'</span>' +
				'<table class="table table-bordered"><tr><td class="item_left">科目</td><td>'+
				result[i].course_name+'</td></tr><tr><td class="item_left">老师</td><td>'+
				(result[i].teacher_name==null?'':result[i].teacher_name)+'</td></tr><tr><td class="item_left">起止时间</td>' +
				'<td>'+start_date+'--'+end_date+'</td></tr><tr><td class="item_left">备注</td><td>'+
				result[i].remark+'</td></tr><tr><td class="item_left">班级人数</td><td>'+
				result[i].exist_count+'人</td></tr></table>' +
				'<button class="btn btn-default manage" code="'+result[i].contact_id+'-'+
				result[i].contact_name+'-'+result[i].grade_id+'">学生管理</button>' +
				'<button code="'+result[i].contact_id+'-'+result[i].is_grab+'-'+result[i].contact_name+'-'+result[i].grade_id+
				'" class="btn btn-default detail" value="'+start_date+'--'+end_date+
				'" style="margin-left:10px;">查看详情</button></div>';
		}else if (result[i].apply_start_date>date){//未开始报名
			li='<div class="qcontent_item"><span>'+result[i].contact_name+'</span>' +
				'<table class="table table-bordered"><tr><td class="item_left">科目</td><td>' +
				result[i].course_name+'</td></tr><tr><td class="item_left">老师</td><td>' +
				(result[i].teacher_name==null?'':result[i].teacher_name)+'</td></tr><tr><td class="item_left">起止时间</td>' +
				'<td>'+start_date+'--'+end_date+'</td></tr><tr><td class="item_left">备注</td><td>'+
				result[i].remark+'</td></tr><tr><td class="item_left">班级人数</td><td>'+
				result[i].exist_count+'人</td></tr><tr><td class="item_left">报名开始时间</td><td>'+
				apply_start_date+'</td></tr></table>' +
				'<button class="btn btn-default btn-disable" disabled="disabled">等待开始报名</button>';
			if (localStorage.getItem('user_id')==result[i].create_by) {
				li=li+'<button code="'+result[i].contact_id+'-'+result[i].is_grab+'-'+result[i].contact_name+'-'+result[i].grade_id+
					'-'+result[i].course+'-'+result[i].phone+'-'+result[i].remark+'-'+result[i].team_count+'-'+
					result[i].apply_count+'" value="'+start_date+'--'+end_date+'--'+apply_start_date+'--'+
					apply_end_date+'--'+result[i].schedule_url+'--'+
					(result[i].schedule==null?'':result[i].schedule.replace(/\"/g,"'"))+'" class="btn btn-default update" style="margin-left:10px;">修改</button>' +
					'</div>';
			} else {
				li=li+'<button code="'+result[i].contact_id+'-'+result[i].is_grab+'-'+result[i].contact_name+'-'+result[i].grade_id+
					'" value="'+start_date+'--'+end_date+
					'" class="btn btn-default detail" style="margin-left:10px;">查看详情</button></div>';
			}
		}else if (result[i].apply_start_date<date && result[i].apply_end_date>date &&
			result[i].count>0){//正在报名
			li='<div class="qcontent_item"><span>'+result[i].contact_name+'</span>' +
				'<table class="table table-bordered"><tr><td class="item_left">科目</td><td>'+
				result[i].course_name+'</td></tr><tr><td class="item_left">老师</td><td>'+
				(result[i].teacher_name==null?'':result[i].teacher_name)+'</td></tr><tr><td class="item_left">起止时间</td>' +
				'<td>'+start_date+'--'+end_date+'</td></tr><tr><td class="item_left">备注</td><td>'+
				result[i].remark+'</td></tr><tr><td class="item_left">班级人数</td><td>'+
				result[i].exist_count+'人</td></tr><tr><td class="item_left">已报名</td><td>'+
				(result[i].apply_count-result[i].count)+'人</td></tr></table>' +
				'<button class="btn btn-default btn-disable" disabled="disabled">剩余名额：'+
				result[i].count+'</button><button code="'+result[i].contact_id+'-'+result[i].is_grab+'-'+result[i].contact_name+'-'+result[i].grade_id+
				'" value="'+start_date+'--'+end_date+
				'" class="btn btn-default detail" style="margin-left:10px;">查看详情</button></div>';
		}else if (result[i].apply_start_date<date && result[i].apply_end_date>date &&
			result[i].count<=0){//正在报名
			li='<div class="qcontent_item"><span>'+result[i].contact_name+'</span>' +
				'<table class="table table-bordered"><tr><td class="item_left">科目</td><td>'+
				result[i].course_name+'</td></tr><tr><td class="item_left">老师</td><td>'+
				(result[i].teacher_name==null?'':result[i].teacher_name)+'</td></tr><tr><td class="item_left">起止时间</td>' +
				'<td>'+start_date+'--'+end_date+'</td></tr><tr><td class="item_left">备注</td><td>'+
				result[i].remark+'</td></tr><tr><td class="item_left">班级人数</td><td>'+
				result[i].exist_count+'人</td></tr><tr><td class="item_left">已报名</td><td>'+
				(result[i].apply_count-result[i].count)+'人</td></tr></table>' +
				'<button class="btn btn-default btn-disable" disabled="disabled">名额已满</button>' +
				'<button code="'+result[i].contact_id+'-'+result[i].is_grab+'-'+result[i].contact_name+'-'+result[i].grade_id+'" value="'+
				start_date+'--'+end_date+'" class="btn btn-default detail" style="margin-left:10px;">查看详情</button></div>';
		}
		$('#groupmenu').append(li);
	}
	toDetailPage();
	studentManage();
	updateInterestClass();
}

//兴趣班待选课程
function showCourse(){
	$.myajax({
		url:"dictAction/getDictSchoolList",
		datatype:"json",
		data:{dict_group:"015045",is_active:1},
		success:function(data){
			var result=data.result.data;
			for(var i in result){
				var li='<option value="'+result[i].dict_code+'">'+result[i].dict_value+'</option>';
				$('#course').append(li);
			}
		}
	});
}

//显示年级信息
function showGradeInfo(){
	$.myajax({
		url:"gradeAction/getGradeList",
		type:'post',
		success:function(data){
			var result = data.result.data;
			for(var i in result) {
				var gradeVO = result[i];
				var li='<option value="'+gradeVO.grade_id+'">'+gradeVO.grade_name+'</option>';
				$('#grade').append(li);
			}
		}
	});
}

//未过期，已结束状态
function changeStatus(){
	$('#status').on('change',function () {
		is_active=$(this).val();
		showGroup();
	});
}

//科目改变
function changeCourse(){
    $('#course').on('change',function () {
        course=$(this).val();
        showGroup();
    });
}

//年级改变
function changeGrade(){
    $('#grade').on('change',function () {
        grade_id=$(this).val();
        showGroup();
    });
}

//查看详情
function toDetailPage(){
	$('.detail').on('click',function () {
		var conarr=$(this).attr('code').split('-');
		var arr=$(this).val().split('--');
		var date=getDateStr(new Date(),'day');
		if (arr[0]>date && conarr[1]==1)//开课前抢报模式
			window.location.href='lezhi/grabDetail.jsp?contact_id='+conarr[0]+'&contact_name='+conarr[2]+'&grade_id='+conarr[3];
		if (arr[0]>date && conarr[1]==0)//开课前非抢暴模式
			window.location.href='lezhi/noGrabDetail.jsp?contact_id='+conarr[0]+'&contact_name='+conarr[2]+'&grade_id='+conarr[3];
		if (arr[0]<=date && arr[1]>=date)
			window.location.href='lezhi/alreadyStartDetail.jsp?contact_id='+conarr[0]+'&contact_name='+conarr[2]+'&grade_id='+conarr[3];
		if (arr[1]<date)
			window.location.href='lezhi/outDateDetail.jsp?contact_id='+conarr[0];
	});
}

//查看详情
function showDetail(){
	$.myajax({
		url:"contactAction/getInterestClassList",
		datatype:"json",
		data:{is_my:isMy,contact_id:contact_id},
		success:function(data){
			var result=data.result.data;
			$('.title').prepend(result[0].contact_name);
			var start_date=getDateStr(result[0].start_date,'day');
			var end_date=getDateStr(result[0].end_date,'day');
			var apply_start_date=getDateStr(result[0].apply_start_date,'minute');
			var apply_end_date=getDateStr(result[0].apply_end_date,'minute');
			var dd='<dd>兴趣班名称：'+result[0].contact_name+'</dd><dd>科目：'+result[0].course_name+'</dd><dd>教师：'+
				result[0].teacher_name+'</dd><dd>开课时间：'+start_date+' - '+end_date+'</dd><dd>报名时间：'+
				apply_start_date+' - '+apply_end_date+'</dd><dd>备注：'+result[0].remark+'</dd><dd>学生人数上限：'+
				result[0].team_count+'人</dd><dd>已报名人数：'+(result[0].apply_count-result[0].count)+'人</dd>';
			if (result[0].is_grab==1) dd=dd+'<dd>模式：抢报模式</dd>';
			else dd=dd+'<dd>模式：非抢报模式</dd>';
			$('#detail').append(dd);
			if (result[0].is_grab==1) showStuInfo(result[0].team_count);
			else {
				showStuInfoToNoGrab(result[0].team_count);
				showApplyStuInfo(result[0].apply_count-result[0].count);
			}
		}
	});
}

function updateDetail(){
	$.myajax({
		url:"contactAction/getInterestClassList",
		datatype:"json",
		data:{is_my:isMy,contact_id:contact_id},
		success:function(data){
			var result=data.result.data;
			if (result[0].is_grab==1) showStuInfo(result[0].team_count);
			else {
				showStuInfoToNoGrab(result[0].team_count);
				showApplyStuInfo(result[0].apply_count-result[0].count);
			}
			addGroupStudent();
		}
	});
}

//显示兴趣班中的学生
function showStuInfo(team_count){
	$.myajax({
		url:"userAction/getStudentListGroup",
		datatype:'json',
		data:{contact_id:contact_id,user_type:'003010',user_name:""},
		success:function(data){
			$('#stuInfo').empty();
			var result=data.result.data;
			if (result.length>team_count){
				for(var i=0;i<result.length-team_count;i++){
					var sex='男';
					if (result[i].sex==1) sex='女';
					var dd='<dd id="sid'+result[i].student_id+'"><img src="'+result[i].head_url+'" alt="" /><span>'+result[i].student_name+'</span>' +
						'<input code="'+contact_id+'-'+result[i].student_id+'" class="btn btn-default del-btn del" type="button" value="删除">' +
						'<div class="zjq-studentIntro"><img src="'+result[i].head_url+'" alt="" /><p>'+result[i].student_name
						+' | '+sex+'  </p><p>'+result[i].class_name+'</p><p>学号：'+result[i].student_code+
						'</p></div></dd>';
					$('#stuInfo').prepend(dd);
				}
			}
			var k=result.length-team_count;
			if (k<0) k=0;
			for (var j=k;j<result.length;j++){
				var sex1='男';
				if (result[j].sex==1) sex1='女';
				var dd1='<dd class="active"><img src="'+result[j].head_url+'" alt="" /><span>'+result[j].student_name+'</span>' +
					'<input class="btn btn-default del-btn" type="button" value="删除">' +
					'<div class="zjq-studentIntro"><img src="'+result[j].head_url+'" alt="" /><p>'+result[j].student_name
					+' | '+sex1+'  </p><p>'+result[j].class_name+'</p><p>学号：'+result[j].student_code+
					'</p></div></dd>';
				$('#stuInfo').prepend(dd1);
			}
			deleteStudent();
			$('#ratio').empty();
			$('#ratio').prepend('已添加人数：'+result.length+'/'+team_count+'<input id="addGroupStudent" class="btn btn-default add-btn" type="button" value="添加">');
			addGroupStudent();
		}
	});
}

//显示兴趣班中的学生(非抢报)
function showStuInfoToNoGrab(team_count){
	$.myajax({
		url:"userAction/getStudentListGroup",
		datatype:'json',
		data:{contact_id:contact_id,user_type:'003010',user_name:""},
		success:function(data){
			$('#stuInfo').empty();
			var result=data.result.data;
			for (var j=0;j<result.length;j++){
				var dd1='<dd id="sid'+result[j].student_id+'"><img src="'+result[j].head_url+'" alt="" /><span>'+result[j].student_name+'</span>' +
					'<input code="'+contact_id+'-'+result[j].student_id+'" class="btn btn-default del-btn del" type="button" value="删除"></dd>';
				$('#stuInfo').prepend(dd1);
			}
			deleteStudent();
			$('#ratio').empty();
			$('#ratio').prepend('已添加人数：'+result.length+'/'+team_count+'<input id="addGroupStudent" class="btn btn-default add-btn" type="button" value="添加">');
			addGroupStudent();
		}
	});
}

//非抢报模式-已报名.但还没在班级
function showApplyStuInfo(count){
	$.myajax({
		url:'contactAction/getUnContactStuList',
		datatype:'json',
		data:{contact_id:contact_id},
		success:function (data){
			$('#stuInfo1').empty();
			var result=data.result.data;
			for (var i=0;i<result.length;i++){
				var dd='<dd><img src="'+result[i].head_url+'" alt="" /><span>'+result[i].student_name
					+'</span><input code="'+result[i].student_id+'-'+result[i].student_name+
					'" class="btn btn-default pass-btn pass" type="button" value="通过"></dd>';
				$('#stuInfo1').append(dd);
			}
			$('#ratio1').text('已报名人数：'+result.length+'/'+count+'（点击通过按钮添加学生）');
			passApply();
		}
	});
}

//删除兴趣班学生
function deleteStudent(){
	$('.del').on('click',function (){
		var arr=$(this).attr('code').split('-');
		$.myajax({
			url:"contactAction/deleteContactByGorup",
			datatype:"json",
			data:{contact_id:arr[0],student_id:arr[1]},
			success:function(data){
				$('#ratio').empty();
				updateDetail();
			}
		});
	});
}

//通过学生的报名申请
function passApply(){
	$('.pass').on('click',function(){
			var arr=$(this).attr('code').split('-');
			var jsondata='[{"user_type":"003010","user_name":"'+arr[1]+'","student_id":'+arr[0]+
				',"user_id":0}]';
			$.myajax({
				url:"contactAction/addContactListByGroup",
				datatype:"json",
				data:{contact_id:contact_id,item_list:jsondata},
				success:function(data){
					updateDetail();
				}
			});
	});
}

//查看已开课详情
function showAlreadyDetail(){
	$.myajax({
		url:"contactAction/getInterestClassList",
		datatype:"json",
		data:{is_my:isMy,contact_id:contact_id},
		success:function(data){
			var result=data.result.data;
			$('.title').prepend(result[0].contact_name);
			var start_date=getDateStr(result[0].start_date,'day');
			var end_date=getDateStr(result[0].end_date,'day');
			var apply_start_date=getDateStr(result[0].apply_start_date,'minute');
			var apply_end_date=getDateStr(result[0].apply_end_date,'minute');
			var dd='<dd>兴趣班名称：'+result[0].contact_name+'</dd><dd>科目：'+result[0].course_name+'</dd><dd>教师：'+
				(result[0].teacher_name==null?'':result[0].teacher_name)+'</dd><dd>开课时间：'+start_date+' - '+end_date+'</dd><dd>备注：'+
				result[0].remark+'</dd>';
			$('#detail').append(dd);
			showAlreadyStuInfo();
		}
	});
}

//显示兴趣班中的学生
function showAlreadyStuInfo(){
	$.myajax({
		url:"userAction/getStudentListGroup",
		datatype:'json',
		data:{contact_id:contact_id,user_type:'003010',user_name:""},
		success:function(data){
			$('#stuInfo').empty();
			var result=data.result.data;
			for (var j=0;j<result.length;j++){
				var dd1='<li><img src="'+result[j].head_url+'" alt="" /><span>'+result[j].student_name+'</span></li>';
				$('#stuInfo').append(dd1);
			}
		}
	});
}

//增加兴趣班学生（跳转页面）
function addGroupStudent(){
	$('#addGroupStudent').on('click',function(){
		window.location.href='lezhi/addGroupStudent.jsp?contact_name='+contact_name+'&contact_id='+contact_id+
			'&grade_id='+grade_id;
	});
}

//学生管理
function studentManage(){
	$('.manage').on('click',function (){
		var arr=$(this).attr('code').split('-');
		window.location.href='lezhi/interestStudent.jsp?contact_name='+arr[1]+'&contact_id='+arr[0]+
			'&grade_id='+arr[2];
	});
}

//修改兴趣班
function updateInterestClass(){
	$('.update').on('click',function(){
		var arr=$(this).attr('code').split('-');
		var val=$(this).attr('value').split('--');
		window.location.href='lezhi/startClass.jsp?contact_name="'+arr[2]+'"&course="'+arr[4]+'"&phone="'+arr[5]+
			'"&grade_id='+arr[3]+'&start_date="'+val[0]+'"&end_date="'+val[1]+'"&remark="'+arr[6]+
			'"&apply_start_date="'+val[2]+'"&apply_end_date="'+val[3]+'"&is_grab='+arr[1]+'&team_count='+arr[7]
			+'&apply_count='+arr[8]+'&schedule_url="'+val[4]+'"&jsondata="'+val[5]+'"&contact_id='+arr[0];
	});
}