var uList = document.getElementById("list");
var uActiveBtn = null;

oPdata.end_id = 0;
oPdata.begin_id = 0;
oPdata.page_size = 20;

mui.ready(function() {
	//plus.nativeUI.showWaiting('数据加载中');
	InitScroll();
	InitPage();
	BindEvent();
});


function InitScroll() {
	var deceleration = mui.os.ios ? 0.003 : 0.0009;
	document.querySelector('.mui-slider').style.height = (window.innerHeight - 43) + "px";
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration: deceleration
	});
};

function BindEvent(e){
	window.addEventListener("updateState",function(e){
		uActiveBtn.querySelector('.p-time-finish').innerText = '已完成';
	});
};

function InitPage() {
	cache.ajax("homeworkAction/getHomeworkList", {
		data: {
			user_type :"003015",
			direction: "1",
			student_id : cache.getChild().student_id,
			start_id: oPdata.end_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if (aRe.length > 0) {
				if (oPdata.end_id == 0) {
					if (aRe.length < oPdata.page_size) {
						pull.endPullUpToRefresh(true);
					}
				}
				oPdata.end_id = aRe[0].homework_id;
				oPdata.begin_id = aRe[aRe.length-1].homework_id;
				ApppendDocumentFragment(aRe);
			} else {
				if (oPdata.end_id == 0) {
					pull.endPullUpToRefresh(true, true);
					return;
				} else {
					mui.toast('没有新的数据');
				}
			}
			InitPullRefresh();
			//plus.nativeUI.closeWaiting();
		},
		error: function(err) {
			//plus.nativeUI.closeWaiting();
		}
	});
};

function InitPullRefresh(){
	//循环初始化所有下拉刷新，上拉加载。
	pull = mui('.mui-scroll').pullToRefresh({
		down: {
			callback: function() {
				var self = this;
				setTimeout(function() {
					pulldownRefresh();
				}, 1000);
			}
		},
		up: {
			callback: function() {
				var self = this;
				setTimeout(function() {
					pullupRefresh();
				}, 1000);
			}
		}
	});
};

//下拉刷新
function pulldownRefresh() {
	if(oPdata.end_id == 0) {
		uList.innerHTML = '';
	}
	cache.ajax("homeworkAction/getHomeworkList", {
		data: {
			user_type :"003015",
			direction: "1",
			student_id : cache.getChild().student_id,
			start_id: oPdata.end_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if (aRe.length > 0) {
				if (oPdata.end_id == 0) {
					if (aRe.length < oPdata.page_size) {
						pull.endPullUpToRefresh(true);
					}
				}
				oPdata.end_id = aRe[0].homework_id;
				InsertDocumentFragment(aRe);
			} else {
				if (oPdata.end_id == 0) {
					pull.endPullUpToRefresh(true, true);
					return;
				} else {
					mui.toast('没有新的数据');
				}
			}
			pull.endPullDownToRefresh();
			pull.refresh(true);
			pull.endPullUpToRefresh(false);
		},
		error: function(err) {
			mui.toast('获取数据失败，请重新获取');
			pull.endPullDownToRefresh();
			pull.refresh(true);
		}
	});
};

//上拉加载获取数据
function pullupRefresh() {
	cache.ajax("homeworkAction/getHomeworkList", {
		data: {
			user_type :"003015",
			direction: "0",
			student_id : cache.getChild().student_id,
			start_id: oPdata.begin_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if (aRe.length > 0) {
				oPdata.begin_id = aRe[aRe.length-1].homework_id;
				ApppendDocumentFragment(aRe);
				pull.endPullUpToRefresh();
			} else {
				pull.endPullUpToRefresh(true);
			}
		},
		error: function(err) {
			mui.toast('获取数据失败，请重新获取');
			pull.endPullUpToRefresh(false);
		}
	});
};

function ApppendDocumentFragment(e) {
	var uDomFragment = document.createDocumentFragment();
	if (uList.lastChild) {
		var sLastDate = uList.lastChild.send_time;
	} else {
		var sLastDate = null;
	}
	var iFisrtElement = true;
	for (var i = 0; i < e.length; i++) {
		var sDate = cache.dateStamp2str(e[i].create_date);
		if (!cache.isNull(sLastDate) && sLastDate === sDate) {
			if (iFisrtElement) {
				uList.lastChild.querySelector('.mui-table-view').appendChild(CreateCell(e[i]));
				sLastDate = sDate;
				continue;
			} else {
				uDomFragment.lastChild.querySelector('.mui-table-view').appendChild(CreateCell(e[i]));
				sLastDate = sDate;
				continue;
			}
		} else {
			iFisrtElement = false;
			uDomFragment.appendChild(CreateLi(e[i]));
			sLastDate = sDate;
		}
	}
	uList.appendChild(uDomFragment);
};

function InsertDocumentFragment(e) {
	var uDomFragment = document.createDocumentFragment();
	if (uList.firstChild) {
		var sLastDate = uList.firstChild.send_time;
		var ul = uList.firstChild.querySelector(".mui-table-view");
	} else {
		var sLastDate = null;
	}
	var iFisrtElement = true;
	for (var i = e.length - 1; i >= 0; i--) {
		var sDate = cache.dateStamp2str(e[i].create_date);
		if (!cache.isNull(sLastDate) && sLastDate === sDate) {
			if (iFisrtElement) {
				ul.insertBefore(CreateCell(e[i]), ul.firstChild);
				sLastDate = sDate;
				continue;
			} else {
				uDomFragment.firstChild.querySelector('.mui-table-view').insertBefore(CreateCell(e[i]), uDomFragment.firstChild.querySelector('.mui-table-view').firstChild);
				sLastDate = sDate;
				continue;
			}
		} else {
			iFisrtElement = false;
			uDomFragment.insertBefore(CreateLi(e[i]), uDomFragment.firstChild);
			sLastDate = sDate;
		}
	}
	uList.insertBefore(uDomFragment, uList.firstChild);
};

function CreateLi(e) {
	var uLi = document.createElement("li");
	uLi.send_time = cache.dateStamp2str(e.create_date);
	uLi.innerHTML = '<div class="addhw-time">' + cache.theValue(cache.date2str(cache.date(e.create_date))) + '</div>';
	var uNoticeGroup = document.createElement("ul");
	uNoticeGroup.className = 'mui-table-view  peop-finish';
	uNoticeGroup.appendChild(CreateCell(e));
	uLi.appendChild(uNoticeGroup);
	return uLi;
}
function CreateCell(e) {
	if (cache.isNull(e.send_time)||e.send_time == 0) {
		e.send_time = e.create_date;
	}
	var uLi = document.createElement("li");
	if(e.is_submit == 0){
		var uSubmit = "未完成";
	}else{
		 uSubmit = "已完成";
	}
	if(cache.getDateToEnd(e.end_date) == "已到期"){
		uLi.className = 'mui-table-view-cell conter-all conter-all-finish';
		uLi.innerHTML=' <div class="icon-HeadPortraits course-finish">' + cache.playgroupCourse2Name(e.course) + '</div>\
	                <div class="Pname">'+e.title+'</div>\
	            	<div class="SubmitTime">' + cache.GetDateDescribe(e.send_time) +'&nbsp;'+e.sender_name+'</div>\
	                <div class="t-content-finish">'+ e.content +'</div>\
	                <div class="p-time-finish">'+ uSubmit +'</div> \
	                <div class="t-time-finish">已到期</div>';
	}else{
		uLi.className = 'mui-table-view-cell conter-all';
		uLi.innerHTML=' <div class="icon-HeadPortraits course-finish">' + cache.playgroupCourse2Name(e.course) + '</div>\
	                <div class="Pname">'+e.title+'</div>\
	            	<div class="SubmitTime">' + cache.GetDateDescribe(e.send_time) +'&nbsp;'+e.sender_name+'</div>\
	                <div class="t-content-finish">'+ e.content +'</div>\
	                <div class="p-time-finish">'+ uSubmit +'</div> \
	                <div class="t-time-finish">距离截至还剩' + cache.getDateToEnd(e.end_date) + '</div>';
	}
	e.id = e.homework_id;
	uLi.affixData = e;
	uLi.addEventListener('tap', function() {
		var self = this;
		if(self.disable) {
			return;
		}
		self.disable = true;
		self.affixData.id = self.affixData.homework_id;
		setTimeout(function() {
			self.disable = false;
			mui.openWindow({
				url: "pdetail.html?id=" + self.affixData.id,
				id: 'pdetail',
				extras: {
					item: self.affixData
				}
			});
		}, 350);
		uActiveBtn = self;
	});
	return uLi;
}