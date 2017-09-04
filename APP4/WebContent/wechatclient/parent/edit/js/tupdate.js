var oPicker0, oPicker1, oPicker2, oPicker3, oPicker4, oPicker5;
//var pkd0 = Array(),pkd1 = Array(),pkd2 = Array(),pkd3 = Array(),pkd4 = Array();
var uP0 = document.getElementById('p0'),
	uP1 = document.getElementById('p1'),
	uP2 = document.getElementById('p2'),
	uP3 = document.getElementById('p3'),
	uP4 = document.getElementById('p4'),
	uP5 = document.getElementById('p5'),
	uT0 = document.getElementById('t0'),
	uT1 = document.getElementById('t1'),
	uT2 = document.getElementById('t2'),
	uT3 = document.getElementById('t3'),
	uT4 = document.getElementById('t4'),
	uT5 = document.getElementById('t5');
	
mui.plusReady(function(){
	oPdata.oTeacher = oExtras;
	InitPicker();
	BindEvent();
	if (!cache.isNull(oPdata.oTeacher.duty)) {
		InitPage();
	}
});

//初始化页面上的内容
function InitPage(){
	uT0.innerHTML = cache.dictCode2Value(oPdata.oTeacher.duty) + '<span class="mui-icon mui-icon-arrowdown"></span>';
	uT1.innerHTML = oPdata.oTeacher.class_name[0] + '年级<span class="mui-icon mui-icon-arrowdown"></span>';
	uT2.innerHTML = oPdata.oTeacher.class_name + '<span class="mui-icon mui-icon-arrowdown"></span>';
	uT3.innerHTML = cache.dictCode2Value(oPdata.oTeacher.course) + '<span class="mui-icon mui-icon-arrowdown"></span>';
	uT4.innerHTML = (oPdata.oTeacher.is_charge == 1 ? '是' : '否') + '<span class="mui-icon mui-icon-arrowdown"></span>';
	uT5.innerHTML = cache.theValue(oPdata.oTeacher.class_name) + '<span class="mui-icon mui-icon-arrowdown"></span>';
	UpdatePickerDom();
};
//绑定事件
function BindEvent(){
	//提交更新的身份信息
	document.getElementById('submit').addEventListener('tap', function() {
		if (CheckDuty()) {
			cache.myajax("userAction/updateTeacherDuty", {
				data: {
					user_type :"003005",
					teacher_id: oPdata.oTeacher.teacher_id,
					grade_id: oPdata.oTeacher.grade_id,
					class_id: oPdata.oTeacher.class_id,
					class_name: oPdata.oTeacher.class_name,
					course: oPdata.oTeacher.course,
					duty: oPdata.oTeacher.duty,
					is_charge: oPdata.oTeacher.is_charge,
					contact_id: oPdata.oTeacher.contact_id
				},
				success: function(data) {
					var re = data.result.data;
					cache.setTeams(re);
					mui.fire(wvParent,'updatePage');
					mui.back();
				},
				error: function(err) {
					mui.toast('身份更新失败');
				}
			});
		}
	});
	
	//删除该职务
	document.getElementById('deleteID').addEventListener('tap', function() {
		var aTeams = cache.getTeams();
		if (aTeams.length === 1 && aTeams[0].teacher_id === oPdata.oTeacher.teacher_id) {
			plus.nativeUI.confirm('确认要删除最后一个职务吗？',function(e){
				if (e.index === 0) {
					cache.myajax("userAction/deleteTeacherDuty", {
						data: {
							user_type :"003005",
							teacher_id: oPdata.oTeacher.teacher_id
						},
						success: function(data) {
							var re = data.result.data;
							cache.setTeams(re);
							mui.fire(wvParent,'updatePage');
							mui.back();
						},
						error: function(err) {
							mui.toast('删除职务失败，请检查网络');
						}
					});
				}
			},'请注意',['确认','取消']);
		}else{
			DeleteTeacherDuty();
		}
		
		
	});
	
	//为picker添加点击事件
	uP0.addEventListener('tap', function() {
		oPicker0.show(function(item) {
			uT0.innerHTML = item[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
			oPdata.oTeacher.duty = item[0].value;
			UpdatePickerDom();
			oPdata.oTeacher.grade_id = '0';
			oPdata.oTeacher.class_id = '0';
			oPdata.oTeacher.class_name = '';
			oPdata.oTeacher.course = '015';
			oPdata.oTeacher.contact_id = '0';
		});
	});
	uP1.addEventListener('tap', function() {
		oPicker1.show(function(item) {
			oPdata.oTeacher.grade_id = item[0].value;
			oPdata.oTeacher.class_id = item[0].children[0].value;
			oPdata.oTeacher.class_name = item[0].children[0].text;
			uT1.innerHTML = item[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
			uT2.innerHTML = item[0].children[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
			oPicker2.setData(item[0].children);
		});
	});
	uP2.addEventListener('tap', function() {
		oPicker2.show(function(item) {
			oPdata.oTeacher.class_id = item[0].value;
			oPdata.oTeacher.class_name = item[0].text;
			uT2.innerHTML = item[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
		});
	});
	uP3.addEventListener('tap', function() {
		oPicker3.show(function(item) {
			oPdata.oTeacher.course = item[0].value;
			uT3.innerHTML = item[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
		});
	});
	uP4.addEventListener('tap', function() {
		oPicker4.show(function(item) {
			oPdata.oTeacher.is_charge = item[0].value;
			uT4.innerHTML = item[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
		});
	});
	uP5.addEventListener('tap', function() {
		oPicker5.show(function(item) {
			oPdata.oTeacher.contact_id = item[0].value;
			oPdata.oTeacher.class_name = item[0].text;
			oPdata.oTeacher.course = item[0].course;
			uT5.innerHTML = item[0].text + '<span class="mui-icon mui-icon-arrowdown"></span>';
		});
	});
	//bindEvent end
}
//初始化picker
function InitPicker(){
	var aGradeAndClass = cache.getClassRoom();
	//职务选择 器
	oPicker0 = new mui.PopPicker({
		layer: 1
	});
	oPicker0.setData(cache.getDict('016'));
	//年级选择器
	oPicker1 = new mui.PopPicker({
		layer: 1
	});
	oPicker1.setData(aGradeAndClass);
	//班级选择器
	oPicker2 = new mui.PopPicker({
		layer: 1
	});
	oPicker2.setData(aGradeAndClass[0].children);
	//学科选择器
	oPicker3 = new mui.PopPicker({
		layer: 1
	});
	oPicker3.setData(cache.getDict('015'));
	//班主任选择器
	oPicker4 = new mui.PopPicker({
		layer: 1
	});
	oPicker4.setData([{
		"text": "是",
		"value": "1"
	}, {
		"text": "否",
		"value": "0"
	}]);
	//兴趣班选中器
	oPicker5 = new mui.PopPicker({
		layer: 1
	});
	oPicker5.setData(cache.getPlaygroup());
	//initPicker end
};

function UpdatePickerDom(){
	switch (oPdata.oTeacher.duty) {
		case '016005':
			uP0.className = '';
			uP1.className = '';
			uP2.className = '';
			uP3.className = '';
			uP4.className = '';
			uP5.className = 'mui-disabled';
			break;
		case '016010':
			uP0.className = '';
			uP1.className = '';
			uP2.className = 'mui-disabled';
			uP3.className = 'mui-disabled';
			uP4.className = 'mui-disabled';
			uP5.className = 'mui-disabled';
			break;
		case '016015':
			uP0.className = '';
			uP1.className = 'mui-disabled';
			uP2.className = 'mui-disabled';
			uP3.className = 'mui-disabled';
			uP4.className = 'mui-disabled';
			uP5.className = 'mui-disabled';
			break;
		case '016020':
			uP0.className = '';
			uP1.className = 'mui-disabled';
			uP2.className = 'mui-disabled';
			uP3.className = 'mui-disabled';
			uP4.className = 'mui-disabled';
			uP5.className = 'mui-disabled';
			break;
		case '016025':
			uP0.className = '';
			uP1.className = 'mui-disabled';
			uP2.className = 'mui-disabled';
			uP3.className = 'mui-disabled';
			uP4.className = 'mui-disabled';
			uP5.className = '';
			break;
		default:
			mui.toast('未知的职务');
			break;
	}
};

function CheckDuty() {
	if (oPdata.oTeacher.duty == 0) {
		submitNum = 0;
		mui.toast('请选择职务');
		return false;
	}
	switch (oPdata.oTeacher.duty) {
		case '016005':
			if (oPdata.oTeacher.grade_id  === '0') {
				submitNum = 0
				mui.toast('请选择年级');
				return false;
			}
			if (oPdata.oTeacher.class_id  === '0') {
				submitNum = 0
				mui.toast('请选择班级');
				return false;
			}
			if (oPdata.oTeacher.course  === '015') {
				submitNum = 0
				mui.toast('请选择科目');
				return false;
			}
			oPdata.oTeacher.contact_id = '0'
			break;
		case '016010':
			if (oPdata.oTeacher.grade_id  === '0') {
				submitNum = 0
				mui.toast('请选择年级');
				return false;
			}
			oPdata.oTeacher.class_id = '0';
			oPdata.oTeacher.class_name = '年级主任';
			oPdata.oTeacher.course = '015';
			oPdata.oTeacher.contact_id = '0'
			break;
		case '016015':
			oPdata.oTeacher.course  === '015';
			oPdata.oTeacher.grade_id = '0';
			oPdata.oTeacher.class_id = '0';
			oPdata.oTeacher.contact_id == '0'
			oPdata.oTeacher.class_name = '行政管理组';
			break;
		case '016020':
			oPdata.oTeacher.contact_id = '0'
			oPdata.oTeacher.grade_id = '0';
			oPdata.oTeacher.class_id = '0';
			oPdata.oTeacher.class_name = '校领导';
			oPdata.oTeacher.course = '015'; 
			break;
		case '016025':
			if (oPdata.oTeacher.contact_id == '0') {
				submitNum = 0
				mui.toast('请选择兴趣班类型');
				return false;
			}
			oPdata.oTeacher.grade_id = '0';
			oPdata.oTeacher.class_id = '0';
			break;
	}
	return true;
};

function DeleteTeacherDuty(e){
	cache.myajax("userAction/deleteTeacherDuty", {
		data: {
			user_type :"003005",
			teacher_id: oPdata.oTeacher.teacher_id
		},
		success: function(data) {
			cache.deleTeam(oPdata.oTeacher.teacher_id);
			mui.fire(wvParent,'updatePage');
			mui.back();
		},
		error: function(err) {
			mui.toast('删除职务失败，请检查网络');
		}
	});
};
