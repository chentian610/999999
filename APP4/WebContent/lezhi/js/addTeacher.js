var dutyname1;
var dutyname2;//身份中文
//添加教师页面的添加框
function showInsert(){
	var tr='<tr><td><input type="text" class="form-control" placeholder="输入姓名"></td>'+
		'<td><input type="text" class="form-control" placeholder="输入手机号码"></td>'+
		'<td><select class="form-control" name=""><option >选择性别</option><option value="0">男</option>'+
		'<option value="1">女</option></select></td><td>'+
		'<a href="javascript:;" class="btn btn-sm btn-req duty" onclick="_setJob(this)">添加身份</a></td>'+
		'<td><a class="btn btn-sm btn-req insert">确定</a></td></tr>';
	$('#nextInsert').append(tr);
	nextInsert();
}

//添加教师页面的确定按钮
function nextInsert(){
	$('#nextInsert').unbind('click').on('click','.insert',function(){
		var now=$(this).parent().parent();
		var now1=$(this);
		var teachername=now.find('input:first').val();
		var phone=now.find('input:eq(1)').val();
		if(teachername=='' || phone==''){
			alert("请输入教师姓名和联系方式！"); return false;
		}else if(!phone.match(/^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/)){
			alert("请输入正确的手机号！"); return false;
		}
		$.myajax({
			url:"userAction/getTeacherListByPhone",
			data:{phone:phone},
			datatype:'json',
			type:'post',
			success:function(data){
				var b=data.result.data
				if(b==""){
					now1.hide();
					showInsert();
					now.find('input').attr('disabled','disabled');
					now.find('select').attr('disabled','disabled');
					now.find('a').removeAttr('onclick');
				}else{
					alert("已存在该教师！");
					now.remove();
					showInsert();
				}
			}
		});
	});
}

//添加教师（保存）
function save(){
	$('#save').on('click',function(){
		// if ($('#nextInsert tr').size()==1) {
		// 	alert('请按确定按钮添加教师后保存！');
		// 	return false;
		// }
		var jsondata;
		for (var i=0;i<$('#nextInsert tr').size();i++) {
			var teachername=$('#nextInsert tr').eq(i).find('input').eq(0).val();
			var phone=$('#nextInsert tr').eq(i).find('input').eq(1).val();
			var sex=$('#nextInsert tr').eq(i).find('option:selected').attr("value");
			var someduty=$('#nextInsert tr').eq(i).find('a.duty').attr('value');
			if (someduty==undefined || someduty=="") {//没有选择职务时
				if (sex==undefined) sex='';
				if (jsondata==undefined) {
					jsondata='[{"teacher_name":"'+teachername+'","phone":"'+phone+'","sex":"'+sex+'"}';
				} else {
					jsondata=jsondata+',{"teacher_name":"'+teachername+'","phone":"'+phone+'","sex":"'+sex+'"}';
				}
			} else {//有选择职务时
				if (sex==undefined) sex='';
				var arr=someduty.split(',');
				for (var j=0;j<arr.length;j++) {
					var arr1=arr[j].split('-');
					var classes=arr1[1].split('.');//多个班级的情况（数学（2个班级））
					var classnames=arr1[2].split('.');
					for (var k=0;k<classes.length;k++){
						if (jsondata==undefined) {
							jsondata='[{"grade_id":'+arr1[0]+',"class_id":'+classes[k]+',"course":"'+arr1[3]+'","duty":"'+
								arr1[5]+'","is_charge":'+arr1[4]+',"teacher_name":"'+teachername+'","phone":"'+phone+
								'","class_name":"'+classnames[k]+'","sex":"'+sex+'"}';
						} else {
							jsondata=jsondata+',{"grade_id":'+arr1[0]+',"class_id":'+classes[k]+',"course":"'+arr1[3]+'","duty":"'+
								arr1[5]+'","is_charge":'+arr1[4]+',"teacher_name":"'+teachername+'","phone":"'+phone+
								'","class_name":"'+classnames[k]+'","sex":"'+sex+'"}';
						}
					}
				}
			}
		}
		jsondata=jsondata+']';
		$.myajax({
			url:"userAction/addTeacherList",
			datatype:"json",
			data:{item_list:jsondata},
			success:function(data){
				window.location.href='lezhi/teacherRoster.jsp';
			}
		});
	});
}

//身份弹框
function _setJob(obj){
	dutyname();
	showDutyInfo();//显示该校职务
	showGradeInfo();
	showSubject();
	//showInterest();
	clear();
	layer.open({
		type: 1,
		area: '820px',
		title: false,
		closeBtn: 0,
		shadeClose: true,
		content: $("#dialogSf"),
		success: function(res,index){
			layerIndex = index;
		}
	});
	if ($(obj).attr('value')!=undefined){//为按确定按钮的情况下，再次点击进入修改身份
		$('#duty a').remove();
		var duty_name=$(obj).parent().attr('title');
		var arr=duty_name.split(',');
		var someduty=$(obj).attr('value');
		var arr1=someduty.split(',');
		for (var i=0;i<arr.length;i++){
			$('#duty').append('<a href="javascript:;" class="btn btn-sm one" value="'+arr1[i]+'">'+arr[i]+'</a>');
		}
	}
}

//显示职务
function showDutyInfo(){
	$.myajax({
		url:"dictAction/getDictSchoolList",
		datatype:"json",
		type:'post',
		data:{dict_group:'016'},
		success:function(data){
			$('#dutyname').empty();
			var result = data.result.data;
			for(var i in result) {
				var dutyVO = result[i];
				if (dutyVO.dict_code=='016025') continue;
				var li='<label value="'+dutyVO.dict_code+'"><input type="radio" name="a" id="d-'+dutyVO.dict_code
					+'">'+dutyVO.dict_value+'</label>';
				$('#dutyname').append(li);
			}
			$('#dutyname label:first').click();
			$('#dutyname label input').attr('checked',false);
		}
	});
}

//显示年级信息
function showGradeInfo(){
	$.myajax({
		url:"gradeAction/getGradeList",
		type:'post',
		success:function(data){
			$('#grade').empty();
			var result = data.result.data;
			for(var i in result) {
				var gradeVO = result[i];
				var li='<label id="grade'+gradeVO.grade_id+'" code="'+gradeVO.grade_name+
					'"><input type="radio" name="b">'+gradeVO.grade_name+'</label>';
				$('#grade').append(li);
			}
			gradeID=result[0].grade_id;
			showClassInfo();//显示班级信息
			$('#grade label:first').click();
			$('#grade label input').attr('checked',false);
		}
	});
}

//显示班级信息
function showClassInfo(){
	$.myajax({
		url:"classAction/getClassList",
		datatype:"json",
		type:'post',
		data:{grade_id:gradeID},
		success:function(data){
			$('#classname').empty();
			var result=data.result.data;
			for(var i in result){
				var classVO=result[i];
				var li='<label code="'+classVO.class_name+'" id="class'+classVO.class_id+
					'"><input type="checkbox" name="c">'+classVO.class_name+'</label>';
				$('#classname').append(li);
			}
		}
	});
}

//不同年级显示不同班级选项
function changeGrade(){
	$('#grade').unbind('click').on("click",'label',function(){
		gradeID=$(this).attr('id').substring(5);
		$('#classname').empty();
		showClassInfo();
	});
}

//显示学校科目
function showSubject(){
	$.myajax({
		url:"systemAction/getDictionary?dict_group=015",
		datatype:"json",
		success:function(data){
			$('#subject').empty();
			var result = data.result.data;
			for(var i in result) {
				var dictVO = result[i];
				var li='<label id="subject'+dictVO.dict_code+'" code="'+dictVO.dict_value+
					'"><input type="radio" name="d">'+dictVO.dict_value+'</label>';
				$('#subject').append(li);
			}
		}
	});
}

//显示学校兴趣班
function showInterest(){
	$.myajax({
		url:"contactAction/getContactGroupListOfManager",
		datatype:"json",
		data:{user_type:"003010"},
		success:function(data){
			$('#interest').empty();
			var result=data.result.data;
			for(var i in result){
				var dictVO = result[i];
				var li='<label id="interest'+dictVO.contact_id+'" code="'+dictVO.contact_name+
					'"><input type="radio" name="e">'+dictVO.contact_name+'</label>';
				$('#interest').append(li);
			}
		}
	});
}

//不同身份，不同显示
function dutyname(){//不同身份中，不需要显示的内容呈禁用状态
	$('#dutyname').on('click','label',function(){
		dutyname1=$(this).attr('value');
		dutyname2=$(this).text();
		var boolCheck=$(this).find('input').is(":disabled");
		if(boolCheck) return false;
		if(dutyname1=='016010'){
			$('#subject label input').attr('disabled','disabled');
			$('#classname label input').attr('disabled','disabled');
			$('#charge label input').attr('disabled','disabled');
			$('#interest label input').attr('disabled','disabled');
			$('#subject label').css("color","#ccc");
			$('#classname label').css("color","#ccc");
			$('#charge label').css("color","#ccc");
			$('#interest label').css("color","#ccc");
			$('#grade label input').removeAttr('disabled');
			$('#grade label').css("color","#666");
			$('#grade').unbind('click');
		}else if(dutyname1=='016015'){//行政管理
			$('#subject label input').attr('disabled','disabled');
			$('#classname label input').attr('disabled','disabled');
			$('#charge label input').attr('disabled','disabled');
			$('#grade label input').attr('disabled','disabled');
			$('#interest label input').attr('disabled','disabled');
			$('#subject label').css("color","#ccc");
			$('#classname label').css("color","#ccc");
			$('#charge label').css("color","#ccc");
			$('#grade label').css("color","#ccc");
			$('#interest label').css("color","#ccc");
		}else if(dutyname1=='016025'){
			$('#subject label input').attr('disabled','disabled');
			$('#classname label input').attr('disabled','disabled');
			$('#charge label input').attr('disabled','disabled');
			$('#grade label input').attr('disabled','disabled');
			$('#subject label').css("color","#ccc");
			$('#classname label').css("color","#ccc");
			$('#charge label').css("color","#ccc");
			$('#grade label').css("color","#ccc");
			$('#interest label input').removeAttr('disabled');
			$('#interest label').css("color","#666");
		}else if(dutyname1=='016005'){
			changeGrade();
			$('#subject label input').removeAttr('disabled');
			$('#classname label input').removeAttr('disabled');
			$('#charge label input').removeAttr('disabled');
			$('#grade label input').removeAttr('disabled');
			$('#subject label').css("color","#666");
			$('#classname label').css("color","#666");
			$('#charge label').css("color","#666");
			$('#grade label').css("color","#666");
			$('#interest label input').attr('disabled','disabled');
			$('#interest label').css("color","#ccc");
		}else {//校领导，自定义身份
			$('#subject label input').attr('disabled','disabled');
			$('#classname label input').attr('disabled','disabled');
			$('#charge label input').attr('disabled','disabled');
			$('#grade label input').attr('disabled','disabled');
			$('#interest label input').attr('disabled','disabled');
			$('#subject label').css("color","#ccc");
			$('#classname label').css("color","#ccc");
			$('#charge label').css("color","#ccc");
			$('#grade label').css("color","#ccc");
			$('#interest label').css("color","#ccc");
		}
	});
}

//清空
function clear(){
	$('#dialogSf').on("click","a.btn-clear",function(){
		$('#subject label input').attr('checked',false);
		$('#classname label input').attr('checked',false);
		$('#charge label input').attr('checked',false);
		$('#grade label input').attr('checked',false);
		$('#dutyname label input').attr('checked',false);
		$('#interest label input').attr('checked',false);
	});
}

//保存，出现一个新身份(添加教师)
function saveduty(){
	$('.btn-submit').on('click',function(){
		var classID;
		var classname;
		if(dutyname1=='016010'){//年级主任
			if($('#grade label input:checked').size()==0){
				alert('请选择年级！'); return false;
			}
			gradeID=$('#grade label input:checked').parent().attr('id').substring(5);
			classID=0;
			classname=$('#grade label input:checked').parent().attr('code');
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+classID+
				'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+classname+'主任</a>');
		}else if(dutyname1=='016015'){//行政管理
			gradeID=0;
			classID=0;
			classname='';
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+classID+'-'+
				classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">行政管理</a>');
		}else if(dutyname1=='016025'){//兴趣班教师
			gradeID=0;
			classID=$('#interest label input:checked').parent().attr('id').substring(8);
			classname=$('#interest label input:checked').parent().attr('code');
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+classID+'-'+
				classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+classname+'兴趣教师</a>');
		}else if(dutyname1=='016005'){//任课教师
			var shu=$('#classname label input:checked').size();
			if(shu==0){
				layer.msg('请选择班级！', {icon: 0});
				return false;
			}
			dictname=$('#subject label input:checked').parent().attr('code');
			if(dictname==undefined){
				alert('请选择科目！');
				return false;
			}
			dictcode=$('#subject label input:checked').parent().attr('id').substring(7);
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			if (shu==1){//只选择了一个班级
				classID=$('#classname label input:checked').parent().attr('id').substring(5);
				classname=$('#classname label input:checked').parent().attr('code');
				if($('#charge label input:checked').size()>0){
					charge=1;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+
						classID+'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+'老师   班主任('+
						classname+')</a>');
				}else{
					charge=0;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+
						classID+'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+'老师('+classname+')</a>');
				}
			}else {//多个班级
				$('#classname label input:checked').each(function(){
					if (classID==undefined) {
						classID=$(this).parent().attr('id').substring(5);
					} else {
						classID=classID+"."+$(this).parent().attr('id').substring(5);
					}
				});
				$('#classname label input:checked').each(function () {
					if (classname==undefined){
						classname=$(this).parent().attr('code');
					} else {
						classname=classname+"."+$(this).parent().attr('code');
					}
				});
				if($('#charge label input:checked').size()>0){
					charge=1;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+
						classID+'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+
						'老师   班主任('+shu+'个班级)</a>');
				}else{
					charge=0;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+
						classID+'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+'老师('+shu+'个班级)</a>');
				}
			}
		} else {//校领导，自定义身份
			gradeID=0;
			classID=0;
			classname='';
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" value="'+gradeID+'-'+classID+'-'+
				classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dutyname2+'</a>');
		}
	});
}

//点击身份框中的某个已存在的职务,选项自动变化
function oneduty(){
	$('#duty').unbind('click').on('click','a',function(){
		$('.dialog-btn1 a').removeClass('btn_s');
		$(this).addClass('btn_s');
		var indent =  $('.dialog-btn1 a').index(this);
		if(indent <=2){
			$(".dialog-btn1").animate({left:0});
		}else{
			if($(this).offset().left >"450"){
				var  oLeft = '-'+ (indent-2) * 150 +"px";
				$(".dialog-btn1").animate({left:oLeft});
			}else{
				var  oLeft = '-'+ (indent-2) * 150  +"px";
				$(".dialog-btn1").animate({left:oLeft});
			}
		}

		$('#dialogSf input').attr("disabled",false);
		if($(this).hasClass('new')){//填写身份键
			$('#subject label input').attr('checked',false);
			$('#classname label input').attr('checked',false);
			$('#charge label input').attr('checked',false);
			$('#grade label input').attr('checked',false);
			$('#dutyname label input').attr('checked',false);
			$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
			return false;
		}
		var someduty=$(this).attr('value');
		var arr=someduty.split('-');
		$('#d-'+arr[5]).click();
		if(arr[5]=='016005'){//任课教师
			$('#grade'+arr[0]).click();
			$('#subject'+arr[3]).click();
			if(arr[4]==1){
				$('#charge input').prop('checked',true);
			}else{
				$('#charge input').prop('checked',false);
			}
			//因为异步，所以只能再获取一遍保证效果
			gradeID=arr[0];
			$.myajax({
				url:"classAction/getClassList",
				datatype:"json",
				data:{grade_id:gradeID},
				success:function(data){
					$('#classname').empty();
					var result=data.result.data;
					for(var i in result){
						var classVO=result[i];
						var li='<label code="'+classVO.class_name+'" id="class'+classVO.class_id+
							'"><input type="checkbox" name="c">'+classVO.class_name+'</label>';
						$('#classname').append(li);
					}
					var classes=arr[1].split('.');
					for (var i=0;i<classes.length;i++){//多个班级
						$('#class'+classes[i]).click();
					}
					$('#dialogSf input').attr("disabled",true);
				}
			});
		}else if(arr[5]=='016010'){//年级主任
			$('#grade'+arr[0]).click();
			$('#subject input').prop('checked',false);
			$('#charge input').prop('checked',false);
			gradeID=arr[0];
			$.myajax({
				url:"classAction/getClassList",
				datatype:"json",
				data:{grade_id:gradeID},
				success:function(data){
					$('#classname').empty();
					var result=data.result.data;
					for(var i in result){
						var classVO=result[i];
						var li='<label code="'+classVO.class_name+'" id="class'+classVO.class_id+
							'"><input type="checkbox" name="c">'+classVO.class_name+'</label>';
						$('#classname').append(li);
					}
					$('#classname label input').attr('disabled','disabled');
					$('#classname label').css("color","#ccc");
				}
			});
		}else if(arr[5]=='016015'){//行政管理
			$('#class input').prop('checked',false);
			$('#charge input').prop('checked',false);
			$('#grade input').prop('checked',false);
			$('#subject input').prop('checked',false);
		}else if(arr[5]=='016025'){//兴趣班教师
			$('#class input').prop('checked',false);
			$('#charge input').prop('checked',false);
			$('#grade input').prop('checked',false);
			$('#subject input').prop('checked',false);
			$('#interest'+arr[1]).click();
		}else{//校领导
			$('#class input').prop('checked',false);
			$('#charge input').prop('checked',false);
			$('#grade input').prop('checked',false);
			$('#subject input').prop('checked',false);
		}
		$('#dialogSf input').attr("disabled",true);
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear btn_red btn_del">删除</a><a href="javascript:;" class="btn btn-req  btn-reivse">修改</a>');
	});
}

//点击叉号退出
function quit(){
	$('#dialogSf').on("click","a.close",function(){
		var msg='';//拼接显示文字
		var msgvalue='';//拼接需要的添加参数
		for(var i=0;i<$('#duty a').size();i++){
			if($('#duty a:eq('+i+')').text()=='填写身份资料') continue;
			if(msg==''){
				msg=$('#duty a:eq('+i+')').text();
			}else
				msg=msg+' , '+$('#duty a:eq('+i+')').text();
			if(msgvalue==''){
				msgvalue=$('#duty a:eq('+i+')').attr('value');
			}else
				msgvalue=msgvalue+','+$('#duty a:eq('+i+')').attr('value');
		}
		$('#nextInsert tr:last a.duty').parent().addClass('tjob');//设置title属性
		$('#nextInsert tr:last a.duty').parent().attr('title',msg);
		$('#nextInsert tr:last a.duty').text(msg);
		$('#nextInsert tr:last a.duty').attr('value',msgvalue);
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
		$('#duty').empty();
		$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s new">填写身份资料</a>');
		saveduty();
		layer.close(layerIndex);
	});
}

//删除一个身份
function deleteDuty(){
	$('#dialogSf').on("click","a.btn_del",function(){
		var num = $('.dialog-btn1 a').length;
		if(num>1){
			$('.btn_s').remove();
		}
	});
}