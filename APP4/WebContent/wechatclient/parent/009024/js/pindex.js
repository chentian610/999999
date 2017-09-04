var tList = document.getElementById("teacherList"),
	sList = document.getElementById("studentList");
	
mui.ready(function() {
	//plus.nativeUI.showWaiting('数据加载中');
	InitPage();
});

function InitPage() {
	cache.ajax("contactAction/getTeacherContactList", {
		data: {
			user_type :"003015",
			class_id: cache.getChild().class_id
		},
		success: function(aRe) {
			if(cache.isArray(aRe) && aRe.length > 0) {
				AppendTeacher(aRe);
			} else {
				tList.innerHTML='<li class="mui-table-view-cell"><a>该学生暂未绑定老师</a></li>';
			}
			//plus.nativeUI.closeWaiting();
		},
		error: function() {
			plus.nativeUI.closeWaiting();
		}
	});
	cache.ajax("contactAction/getStudentContactList", {
		data: {
			user_type :"003015",
			class_id: cache.getChild().class_id
		},
		success: function(aRe) {
			if(cache.isArray(aRe) && aRe.length > 0) {
				appendStudent(aRe);
			}
			//plus.nativeUI.closeWaiting();
		},
		error: function() {
			//plus.nativeUI.closeWaiting();
		}
	});
}


function AppendTeacher(e) {
	for(var i = 0; i < e.length; i++) {
		var li = document.createElement('li');
		li.affixData = e[i];
		li.className = 'mui-table-view-cell';
		if(!cache.isNull( e[i].teacher_name)){
			li.innerHTML = '<a class="mui-navigate-right" >' + cache.theValue(e[i].teacher_name) + '</a>';
		}else{
			continue;
		}
		li.addEventListener('tap', function() {
			var self = this ;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function() {
				self.disable = false;
				var d = self.affixData;
				mui.openWindow({
					url: "pdetail.html?user_id="+d.user_id+"&phone="+d.phone,
					id: 'pdetail',
					extras: {
						item: self.affixData
					}
				})
			}, 50);
		});
		tList.appendChild(li);
	}
}

function appendStudent(e) {
	for(var i = 0; i < e.length; i++) {
		var li = document.createElement('li');
		li.className = 'mui-table-view-cell';
		if(!cache.isNull( e[i].student_name)){
			li.innerHTML = '<span class="hxx-list-num">' + cache.theValue(e[i].student_code) + '</span>' + cache.theValue(e[i].student_name) + '<span class="hxx-list-sex">' + Sex(e[i].sex) + '</span>';
		}else{
			continue;
		}
		sList.appendChild(li);
	}
}

function Sex(e) {
	if(e == 0) {
		return '男';
	} else {
		return '女';
	}
}