var currentPage=1;
var limit=20;
var gradeID;
var classID;
var dictcode;
var charge;
var classname;
var dictname;
var duty;
var teacherID=0;
var search_text;
var teachersex;
var phone;
var userid;
var dutyname1='016005';
var MODEL_DOWNLOAD_URL=localStorage.getItem('teacher_model_download_url');
var newName;
var newPhone;
var newSex;
var dutyname2;//身份中文

function appendToWeb(teacherVO){
	if(teacherVO.sex==0) teachersex='男'; else if(teacherVO.sex==1) teachersex='女'; else teachersex='未设置';
	var tr='<tr id="phone'+teacherVO.phone+'"><td><span>'+teacherVO.teacher_name+'</span></td><td><span>'+
		teacherVO.phone+'</span></td><td><span>'+teachersex+'</span></td><td class="tjob" title="'+teacherVO.duty_name+
		'"><span>'+teacherVO.duty_name+'</span></td><td><a class="btn btn-sm btn-req delete" onclick="_delItem(this)">删除</a>'+
		'<a href="javascript:;" class="btn btn-sm btn-req" onclick="_editItem(this)" value="'+teacherVO.phone+
		'" code="'+teacherVO.user_id+'">编辑</a></td></tr>';
	$('#teacherList').append(tr);
}

//搜索
function search(){
	$('#search').on('click',function(){
		currentPage=1;
		search_text=$('#teachername').val();
		loadContent();
	});
}

function loadContent(){
	if(search_text==undefined) search_text="";
	$.myajax({
		url:"classAction/getTeacherListOfManager",
		data:{start_id:(currentPage-1)*limit,limit:limit,page:currentPage,teacher_name:search_text},
		datatype:'json',
		type:'post',
		success:function(data){
			$('#teacherCount').text("共"+data.result.total+"人");
			page(data);//分页
		}
	});
}

//加载列表
function addToWeb(data){
	$('#teacherList').empty();
	var result = data.result.data;
	for(var i in result) {
		var teacherVO = result[i];
		appendToWeb(teacherVO);
	}
}

//分页
function page(data){
	var result = data.result;
	if(result==null) return;
	var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
	addToWeb(data);
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
			loadContent();
		}
	};
	$("#page_pagintor").bootstrapPaginator(options);
	$("#page_pagintor").show();
}

//删除教师
function _delItem(obj){
	var phone=$(obj).parent().parent().attr('id').substring(5);
	layer.confirm('确认删除？', {
		btn: ['确定','取消'] //按钮
	}, function(){
		$(obj).parents("tr").fadeOut();
		$.myajax({
			url:"userAction/deleteTeacherOfManager",
			datatype:"json",
			type:'post',
			data:{phone:phone},
			success:function(data){
				$('#phone'+phone).remove();
				layer.msg('删除成功', {icon: 1});
			}
		});
	});
}

//身份弹框
function _setJob(obj){
	dutyname();
	showDutyInfo();
	showGradeInfo();
	showSubject();
	//showInterest();
	layer.open({
		type: 1,
		area: '820px',
		title: false,
		closeBtn: 0,
		shadeClose: true,
		content: $("#dialogSf"),
		success: function(res,index){
			layerIndex = index;
			$.myajax({//弹框上显示已有的各个身份
				url:"userAction/getTeacherList",
				datatype:"json",
				data:{phone:phone},
				success:function(data){
					$('#duty').empty();
					var result=data.result.data;
					for(var i in result){
						if(result[i].duty_name==null) continue;
						if (result[i].team_type=='011005' && result[i].class_ids==undefined){//单个班级
							$('#duty').append('<a href="javascript:;" class="btn btn-sm one" value="'+
								result[i].grade_id+'-'+result[i].class_id+'-'+result[i].class_name+'-'+
								result[i].course+'-'+result[i].is_charge+'-'+result[i].duty+'-'+result[i].teacher_id
								+'">'+result[i].duty_name+'</a>');
						} else if (result[i].team_type=='011005' && result[i].class_ids!=undefined){//多个班级
							$('#duty').append('<a href="javascript:;" class="btn btn-sm one" value="'+
								result[i].grade_id+'-'+result[i].class_ids+'-'+result[i].class_name+'-'+
								result[i].course+'-'+result[i].is_charge+'-'+result[i].duty+'-'+result[i].teacher_ids
								+'">'+result[i].duty_name+'</a>');
						} else {//兴趣班职务
							$('#duty').append('<a href="javascript:;" class="btn btn-sm one" value="0-'+
								result[i].contact_id+'-'+result[i].class_name+'-'+result[i].course+'-'+
								result[i].is_charge+'-'+result[i].duty+'-'+result[i].teacher_id+'">'+
								result[i].duty_name+'</a>');
						}
					}
					$('#dialogSf input').attr("disabled",true);
					quit();
					if ($('#duty a').length==0) {
						$('.add').click();
					}
				}
			});
		}
	});
}

function changeName(name){
	newName=name;
}

function changeTel(tel) {
	newPhone=tel;
}

function changeSex(){
	newSex=$('#changeSex').children('option:selected').val();
}

//编辑教师的确定
function _submitEdit(obj){
	var $item = $(obj).parents('tr');
	var $name = $(">td:first",$item), $tel = $(">td:eq(1)",$item), $sex = $(">td:eq(2)",$item), $job = $(">td:eq(3)",$item), $btns = $(obj).parent();
	var name = $("input",$name).val(),tel = $("input",$tel).val(),sex = $("option:selected",$sex).text(),job = $("a",$job).text();
	var tsex=$("option:selected",$sex).val();
	var someduty=$('#p_'+phone).attr('value');
	if (tsex==-1) tsex=null;
	if($.trim(name)== '' ||$.trim(tel)==''){
		alert('请填写教师姓名和手机号！'); return false;
	}else{
		if (newName!=undefined || newPhone!=undefined || newSex!=undefined) {
			if(userid=='null'){//更新还未注册的老师
				$.myajax({
					url:"userAction/updateTeacherNamePhone",
					datatype:"json",
					type:'post',
					data:{teacher_name:name,new_phone:tel,phone:phone,sex:tsex},
					success:function(data){
					}
				});
			}else{//编辑已注册的老师
				$.myajax({
					url:"userAction/updateTeacherNamePhone",
					datatype:"json",
					type:'post',
					data:{teacher_name:name,phone:tel,user_id:userid,sex:tsex},
					success:function(data){
					}
				});
			}
		}
	}
	//删除身份
	var delduty=$('#p_'+phone).attr('code');
	if(delduty!=undefined){
		var delarr=delduty.split(',');
		for(var i=0;i<delarr.length;i++){
			var del=delarr[i].split('-');
			$.myajax({
				url:"userAction/deleteTeacherDuty",
				datatype:"json",
				async:false,//同步
				data:{teacher_id:del[6]},
				success:function(){
				}
			});
		}
	}
	//修改或添加身份
	if(someduty!=undefined && someduty!=""){//有修改身份信息时
		var arr=someduty.split(',');
		for(var i=0;i<arr.length;i++){
			if (arr[i]=="undefined") continue;
			var arr1=arr[i].split('-');
			if(arr1[6]=='0'){//新增身份时
				var classids=arr1[1].split('.');//多个班级
				var classnames=arr1[2].split('.');
				for (var j=0;j<classids.length;j++){
					$.myajax({
						url:"userAction/addTeacher",
						datatype:"json",
						type:'post',
						data:{grade_id:arr1[0],class_id:classids[j],course:arr1[3],duty:arr1[5],is_charge:arr1[4],
							teacher_name:name,phone:tel,class_name:classnames[j],sex:tsex},
						success:function(data){
						}
					});
				}
			}else{//修改已有身份时
				$.myajax({
					url:"userAction/updateTeacherDutyOfManager",
					datatype:"json",
					type:'post',
					data:{teacher_id:arr1[6],grade_id:arr1[0],class_id:arr1[1],course:arr1[3],duty:arr1[5],
						is_charge:arr1[4],class_name:arr1[2],phone:tel},
					success:function(data){
					}
				});
			}
		}
	}
	$("span",$name).text(name.replace(/\s/g,'')).show();//去掉所有空格
	$("span",$tel).text(tel).show();
	$("span",$sex).text(sex).show();
	if (job!='填写身份资料') {
		$("span", $job).text(job).show();
	}
	$("input",$name).remove();
	$("input",$tel).remove();
	$("select",$sex).remove();
	$("a",$job).remove();
	$('a:eq(0),a:eq(1)',$btns).show();
	$('a:eq(2),a:eq(3)',$btns).remove();
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
		}else if(dutyname1=='016015'){
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

//保存，新增一个职务或修改一个职务
function editSomeDuty(){
	$('.btn-submit').on('click',function(){
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
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+classID+
				'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-'+teacherID+'">'+classname+'主任</a>');
		}else if(dutyname1=='016015'){//行政管理
			gradeID=0;
			classID=0;
			classname='';
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+classID+'-'+
				classname+'-'+dictcode+'-'+charge+'-'+duty+'-'+teacherID+'">行政管理</a>');
		}else if(dutyname1=='016025'){//兴趣班教师
			gradeID=0;
			classID=$('#interest label input:checked').parent().attr('id').substring(8);
			classname=$('#interest label input:checked').parent().attr('code');
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+classID+'-'+
				classname+'-'+dictcode+'-'+charge+'-'+duty+'-'+teacherID+'">'+classname+'兴趣教师</a>');
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
			if (shu==1){//单个班级
				classname=$('#classname label input:checked').parent().attr('code');
				classID=$('#classname label input:checked').parent().attr('id').substring(5);
				if($('#charge label input:checked').size()>0){
					charge=1;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+
						classID+'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+
						'老师   班主任('+classname+')</a>');
				}else{
					charge=0;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+
						classID+'-'+classname+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+'老师('+classname+')</a>');
				}
			} else {//多个班级
				var classids;
				var classnames;
				$('#classname label input:checked').each(function(){
					if (classids==undefined) {
						classids=$(this).parent().attr('id').substring(5);
					} else {
						classids=classids+"."+$(this).parent().attr('id').substring(5);
					}
				});
				$('#classname label input:checked').each(function () {
					if (classnames==undefined){
						classnames=$(this).parent().attr('code');
					} else {
						classnames=classnames+"."+$(this).parent().attr('code');
					}
				});
				if($('#charge label input:checked').size()>0){
					charge=1;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+
						classids+'-'+classnames+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+
						'老师   班主任('+shu+'个班级)</a>');
				}else{
					charge=0;
					$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+
						classids+'-'+classnames+'-'+dictcode+'-'+charge+'-'+duty+'-0">'+dictname+'老师('+shu+'个班级)</a>');
				}
			}
			//多个合并班级的修改：先删除旧的，再重新加一遍新的
            if (teacherID>0) {
                var teacherids = teacherID.split('.');
                for (var i = 0; i < teacherids.length; i++) {
                    var delduty = $('#p_' + phone).attr('code');
                    if (delduty == undefined) {
                        $('#p_' + phone).attr('code', '0-0-0-0-0-0-' + teacherids[i]);
                    } else {
                        $('#p_' + phone).attr('code', delduty + ',' + '0-0-0-0-0-0-' + teacherids[i]);
                    }
                }
            }
		}else {//校领导，自定义身份
			gradeID=0;
			classID=0;
			classname='';
			dictcode='015';
			charge=0;
			duty=dutyname1;
			$('#duty a.btn_s').remove();
			$('#duty').append('<a href="javascript:;" class="btn btn-sm btn_s one" code="1" value="'+gradeID+'-'+classID+'-'+
				classname+'-'+dictcode+'-'+charge+'-'+duty+'-'+teacherID+'">'+dutyname2+'</a>');
		}
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear btn_red btn_del">删除</a><a href="javascript:;" class="btn btn-req  btn-reivse">修改</a>');
		$('#dialogSf input').attr("disabled",true);
	});
}

//点击身份框中的某个已存在的职务,选项自动变化
function oneduty(){
	$('#duty').unbind('click').on('click','a',function(){
        if ($('.update').length>0 ){
            layer.msg('请先保存当前身份！', {icon: 0});
            return false;
        }
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
		$('#subject label input').attr('checked',false);
		$('#classname label input').attr('checked',false);
		$('#charge label input').attr('checked',false);
		$('#grade label input').attr('checked',false);
		$('#dutyname label input').attr('checked',false);
		if($(this).hasClass('new')){//填写身份键
			$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
			return false;
		}
		var someduty=$(this).attr('value');
		var arr=someduty.split('-');
		$('#d-'+arr[5]).click();
		if(arr[5]=='016005'){
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
		}else if(arr[5]=='016010'){
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
			layer.msg('兴趣班教师身份不能删除和修改哦！',{icon:0});
			// $('#class input').prop('checked',false);
			// $('#charge input').prop('checked',false);
			// $('#grade input').prop('checked',false);
			// $('#subject input').prop('checked',false);
			// $('#interest'+arr[1]).click();
		}else{
			$('#class input').prop('checked',false);
			$('#charge input').prop('checked',false);
			$('#grade input').prop('checked',false);
			$('#subject input').prop('checked',false);
		}
		$('#dialogSf input').attr("disabled",true);
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear btn_red btn_del">删除</a><a href="javascript:;" class="btn btn-req  btn-reivse">修改</a>');
		if (arr[5]=='016025') $('.dialog-btn').html('');
		updateDuty();
		deleteDuty();
	});
}

//删除一个身份
function deleteDuty(){
	$('#dialogSf').on("click","a.btn_del",function(){
		var num = $('.dialog-btn1 a').length;
		if(num>1){
			if ($('.btn_s').attr('value')==undefined) return;
			var del=$('.btn_s').attr('value').split('-');
			var teacherids=del[6].split('.');//多个班级
			for (var i=0;i<teacherids.length;i++) {
				var delduty=$('#p_'+phone).attr('code');
				if(delduty==undefined){
					$('#p_'+phone).attr('code','0-0-0-0-0-0-'+teacherids[i]);
				}else{
					$('#p_'+phone).attr('code',delduty+','+'0-0-0-0-0-0-'+teacherids[i]);
				}
			}
			$('.btn_s').remove();
			$('#subject label input').attr('checked',false);
			$('#classname label input').attr('checked',false);
			$('#charge label input').attr('checked',false);
			$('#grade label input').attr('checked',false);
			$('#dutyname label input').attr('checked',false);
		}
	});
}

//添加新职务
function addOneDuty(){
	$('.add').on("click",function(){
        if ($('.update').length>0 || $('.new').length>0){
            layer.msg('请先保存当前身份！', {icon: 0});
            return false;
        }
		$('#dialogSf input').attr("checked",false);
		var num = $('.dialog-btn1 a').length;
		if(num <=2){
			$(".dialog-btn1").animate({left:0});
		}else{
			if($(this).offset().left >"450"){
				var  oLeft = '-'+ (num-2) * 150 +"px";
				$(".dialog-btn1").animate({left:oLeft});
			}else{
				var  oLeft = '-'+ (num-2) * 150  +"px";
				$(".dialog-btn1").animate({left:oLeft});
			}
		}
		teacherID=0;
		var html1 = '<a href="javascript:;" class="btn btn-sm new">填写身份资料</a>';
		$('.dialog-btn1').append(html1);
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
		$('#dialogSf input').attr("disabled",false);
		$('.dialog-btn1 a').removeClass('btn_s');
		$('.dialog-btn1 a:last-child').addClass('btn_s');
		editSomeDuty();
	});
}

//修改
function updateDuty(){
	$('#dialogSf').on("click","a.btn-reivse",function(){
        $('.btn_s').addClass('update');
		var someduty=$('.btn_s').attr('value');
		var arr=someduty.split('-');
		teacherID=arr[6];
		$('.dialog-btn').html('<a href="javascript:;" class="btn btn-req btn-clear">清空</a><a href="javascript:;" class="btn btn-req btn-submit">保存</a>');
		$('#dialogSf input').attr("disabled",false);
		editSomeDuty();
	});
}

//点击叉号退出
function quit(){
	$('#dialogSf').on("click","a.close",function(){
        if ($('.update').length>0 ){
            layer.msg('请先保存当前身份！', {icon: 0});
            return false;
        }
		var msg='';
		var msgvalue='';
		for(var i=0;i<$('#duty a').size();i++){
			if(msg==''){
				msg=$('#duty a:eq('+i+')').text();
			}else
				msg=msg+' , '+$('#duty a:eq('+i+')').text();
			if ($('#duty a:eq('+i+')').attr('code')==1) {
				if (msgvalue == '') {
					msgvalue = $('#duty a:eq(' + i + ')').attr('value');
				} else
					msgvalue = msgvalue + ',' + $('#duty a:eq(' + i + ')').attr('value');
			}
		}
		$('#p_'+phone).attr('title',msg);
		$('#p_'+phone).text(msg);
		$('#p_'+phone).attr('value',msgvalue);
		layer.close(layerIndex);
		$('#p_'+phone).parents().find('td:last a.save').click();
	});
}

function bindSBtnClickEvent() {
	$("#importbtn").on("click", function() {
		$("#upload_pic").click();
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
				exportTeacherList(item[0].file_url);
			}
		});
	});
}

//添加教师
function exportTeacherList(url){
	$.myajax({
		url:'excelAction/importTeacher',
		data:{url:url},
		datatype:'json',
		success:function(data){
			layer.msg(data.result.data, {icon: 1});
			search_text="";
			loadContent();
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
						exportTeacherList(item[0].file_url);
					}
				});
			});
		}
	});
}

//下载花名册模版
function exportTExcel(){
	$("#exportbtn").on("click",function(){
		window.open(MODEL_DOWNLOAD_URL);
	});
}