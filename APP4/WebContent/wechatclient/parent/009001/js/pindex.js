var uList = document.getElementById('list'),
	uAddNotice = document.getElementById('uploadNotice');
	
oPdata.end_id = 0;
oPdata.begin_id = 0;
oPdata.page_size = 20;

mui.ready(function() {
	//plus.nativeUI.showWaiting('数据加载中');
	InitScroll();
	InitPage();
});


function InitPage() {
	uList.innerHTML = '';
	cache.ajax("noticeAction/getNoticeList", {
		data: {
			direction: "1",
			user_type :"003015",
			start_id: oPdata.end_id,
			module_code: "009001",
			student_id: cache.getChild().student_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if (!cache.isArray(aRe) || aRe.length <= 0) {
				//plus.nativeUI.closeWaiting();
				return;
			}
			if (aRe.length > 0) {
				if (oPdata.end_id == 0) {
					if (aRe.length < oPdata.page_size) {
						pull.endPullUpToRefresh(true);
					}
				}
				oPdata.end_id = aRe[0].notice_id;
				oPdata.begin_id = aRe[aRe.length-1].notice_id;
				ApppendDocumentFragment(aRe);
			} else {
				if (oPdata.end_id == 0) {
					pull.endPullUpToRefresh(true, true);
					
				} else {
					mui.toast('没有新的数据');
				}
			}
			InitPullRefresh();
			//plus.nativeUI.closeWaiting();
		},
		error: function(err) {
			//plus.nativeUI.closeWaiting();
			mui.toast('获取数据失败，请重新获取');
		}
	});
}

function InitScroll() {
	var deceleration = mui.os.ios ? 0.003 : 0.0009;
	//document.querySelector('.mui-slider').style.height = (window.innerHeight - 44) + "px";
	document.querySelector('.mui-slider').style.height = window.innerHeight + "px";
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration: deceleration
	});
};
function InitPullRefresh(){
	//循环初始化所有下拉刷新，上拉加载。
	pull = mui('.mui-scroll').pullToRefresh({
		down: {
			callback: function() {
				var self = this;
				setTimeout(function() {
					PulldownRefresh();
				}, 1000);
			}
		},
		up: {
			callback: function() {
				PullupRefresh();
			}
		}
	});
};


//下拉刷新
function PulldownRefresh() {
	if(oPdata.end_id == 0) {
		uList.innerHTML = '';
	}
	cache.ajax("noticeAction/getNoticeList", {
		data: {
			direction: "1",
			user_type: "003015",
			start_id: oPdata.end_id,
			module_code: "009001",
			student_id: cache.getChild().student_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if(aRe.length > 0) {
				if(oPdata.end_id == 0) {
					if(aRe.length < oPdata.page_size) {
						pull.endPullUpToRefresh(true);
					}
				}
				oPdata.end_id = aRe[0].notice_id;
				InsertDocumentFragment(aRe);
			} else {
				if(oPdata.end_id == 0) {
					pull.endPullUpToRefresh(true, true);
					cache.showDom(u_tap_center2);
				} else {
					mui.toast('没有新的数据');
				}
			}
			pull.endPullDownToRefresh();

		},
		error: function(err) {
			mui.toast('获取数据失败，请重新获取');
			pull.endPullDownToRefresh();
		}
	});
};
//上拉加载获取数据
function PullupRefresh() {

	cache.ajax("noticeAction/getNoticeList", {
		data: {
			direction: "0",
			start_id: oPdata.begin_id,
			user_type: "003015",
			module_code: "009001",
			student_id: cache.getChild().student_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if(aRe.length > 0) {
				oPdata.begin_id = aRe[aRe.length-1].notice_id;
				ApppendDocumentFragment(aRe);
				pull.endPullUpToRefresh(false);
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
	if(uList.lastChild) {
		var sLastDate = uList.lastChild.send_time;
	} else {
		var sLastDate = null;
	}
	var iFisrtElement = true;
	for(var i = 0; i < e.length; i++) {
		var sDate = cache.date2str(cache.date(e[i].send_time));
		if(!cache.isNull(sLastDate) && sLastDate === sDate) {
			if(iFisrtElement) {
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
	if(uList.firstChild) {
		var sLastDate = uList.firstChild.send_time;
	} else {
		var sLastDate = null;
	}
	var ul = uList.firstChild.querySelector(".mui-table-view");
	var iFisrtElement = true;
	for(var i = e.length - 1; i >= 0; i--) {
		var sDate = cache.date2str(cache.date(e[i].send_time));
		if(!cache.isNull(sLastDate) && sLastDate === sDate) {
			if(iFisrtElement) {
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
	uLi.send_time = cache.theValue(cache.date2str(cache.date(e.send_time)));
	uLi.innerHTML = '<h4 class="hxx-notice-date">' + cache.theValue(cache.date2str(cache.date(e.send_time))) + '</h4>';
	var uNoticeGroup = document.createElement("ul");
	uNoticeGroup.className = 'mui-table-view';
	uNoticeGroup.appendChild(CreateCell(e));
	uLi.appendChild(uNoticeGroup);
	return uLi;
};

function CreateCell(e) {
	oPdata.begin_id = e.notice_id;
	var uLi = document.createElement("li");
	uLi.className = 'hxx-notice-cell mui-table-view-cell';
	var txt = '';
	txt += '<div class="hxx-notice-title">' + cache.theValue(e.notice_title) + '</div>\
    			<div class="hxx-notice-sender">\
					<span class="hxx-notice-sender-time">' + cache.theValue(GetDateDiff(e.send_time)) + '</span>\
					<span class="hxx-notice-sender-name">' + cache.theValue(e.sender_name) + '</span>\
				</div>\
				<div class="hxx-notice-content">' + cache.theValue(e.notice_content) + '</div>';
	if(!cache.isNull(e.count_list)) {
		txt += '<div class="hxx-notice-unread"><span>' + getCount1(e.count_list) + '</span>人已读</div><div class="hxx-notice-reply"><span>' + getCount2(e.count_list) + '</span>人回复</div>';
	}
	uLi.innerHTML = txt;
	e.id = e.notice_id;
	bindClickEvent(uLi, e);
	uLi.affix = e.notice_id;
	return uLi;
};

//距离当前的时间
function GetDateDiff(sTimestr) {
	var sTime = new Date(sTimestr);
	var eTime = new Date();
	var divNum = 1000 * 3600 * 24;
	var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
	if(diff == 0) {
		divNum = 1000 * 3600;
		var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
		if(diff == 0) {
			divNum = 1000 * 60;
			var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
			if(diff <= 0) {
				divNum = 1000 * 60;
				var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
				if(diff <= 0) {
					return '刚刚';
				} else {
					return diff + '秒前';
				}
			} else {
				return diff + '分钟前';
			}
		} else {
			return diff + '小时前';
		}
	} else {
		var diff = parseInt((eTime.getTime() - (sTime.getTime() - sTime.getHours() * 1000 * 3600)) / parseInt(divNum));
		return diff + '天前';
	}
}
//获取浏览数量
function getCount1(txt) {
	if(txt != null || txt != undefined) {

		var count_list = JSON.parse(txt);
		if(count_list.length == 2) {
			return count_list[1].count;
		} else if(count_list.length == 3) {
			return(count_list[1].count + count_list[2].count);
		} else {
			return 0;
		}
	} else {
		return 0;
	}
	if(txt == "[]") {
		return 0;
	}
}
//获取回复数量
function getCount2(txt) {
	if(txt != null || txt != undefined) {
		var count_list = JSON.parse(txt);
		if(count_list.length == 2) {
			if(count_list[1].notice_status == "008020") {
				return count_list[1].count;
			} else {
				return 0;
			}
		} else if(count_list.length == 3) {
			if(count_list[1].notice_status == "008020") {
				return count_list[1].count;
			} else if(count_list[2].notice_status == "008020") {
				return count_list[2].count;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	} else {
		return 0;
	}
	if(txt == "[]") {
		return 0;
	}
}

function bindClickEvent(li, item) {
	li.addEventListener('tap', function(event) {
		var self = this;
		if(self.disable) {
			return;
		}
		self.disable = true;
		setTimeout(function() {
			self.disable = false;
		}, 350);
		mui.openWindow({
			url: "pdetail.html?id=" + item.id,
			id: "pdetail",
			extras: {
				item: item
			}
		});
	});
}