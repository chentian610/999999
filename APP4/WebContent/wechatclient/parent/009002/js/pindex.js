var uList = document.getElementById('list'),
	uBtnClass = document.getElementById("btnClass"),
	uBtnDate = document.getElementById("btnDate"),
	uploadPhoto = document.getElementById("uploadPhoto"),
	ubtnDel = document.getElementById("btnDel"),
	uTip = document.getElementById("tip");

oPdata.end_id = 0;
oPdata.begin_id = 0;
oPdata.class_id = 0;
oPdata.add_date = "";
oPdata.page_size = 20;
oPitem.aPhotos = [];

mui.init({
	gestureConfig: {
		longtap: true //启动长按功能
	}
});

mui.ready(function() {
	InitScroll();
	BindEvent();
	InitPage();
	mui.previewImage();
	oPdata.class_id = cache.getChild().class_id;
});

function InitPage() {
	uList.innerHTML = '';
	cache.ajax("photoAction/getPhotoList", {
		data: {
			student_id: cache.getChild().student_id,
			class_id: cache.getChild().class_id,
			user_type: "003015",
			photo_type: "006010",
			add_date: oPdata.add_date,
			direction: "1",
			start_id: oPdata.end_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			if(!cache.isArray(aRe)) {
				return;
			}
			if(aRe.length > 0) {
				if(oPdata.end_id == 0) {
					if(aRe.length < oPdata.page_size) {
						pull.endPullUpToRefresh(true);
					}
				}
				oPdata.end_id = aRe[0].photo_id;
				ApppendDocumentFragment(aRe);
			} else {
				if(oPdata.end_id == 0) {
					pull.endPullUpToRefresh(true, true);
				} else {
					mui.toast('没有新的数据');
				}
			}
		},
		error: function(err) {
			mui.toast('获取数据失败，请重新获取');
		}
	});
}

function InitScroll() {
	var deceleration = mui.os.ios ? 0.003 : 0.0009;
	document.querySelector('.mui-slider').style.height = (window.innerHeight - 87) + "px";
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration: deceleration
	});
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
				var self = this;
				setTimeout(function() {
					PullupRefresh();
				}, 1000);
			}
		}
	});
};

function RefreshPhotoList(){
	cache.ajax("photoAction/getPhotoList", {
		data: {
			student_id: 0,
			class_id: oPdata.class_id,
			team_type: oPdata.team_type,
			user_type: "003005",
			photo_type: "006010",
			add_date: oPdata.add_date,
			direction: "1",
			start_id: oPdata.end_id,
			limit: oPdata.page_size
		},
		success: function(aRe) {
			oPdata.end_id = aRe[aRe.length-1].photo_id;
			InsertDocumentFragment(aRe);
		},
		error: function(err) {
			mui.toast('获取数据失败，请重新获取');
		}
	});
};

function BindEvent() {
	window.addEventListener('addPhotos', function(e) {
		RefreshPhotoList();
	});

	//点击下载
	mui('body').on('longtap', ".mui-zoom", function(e) {
		var path = this.src;
		//弹出系统选择框
		var actionbuttons = [{
			title: "下载"
		}];
		var actionstyle = {
			cancel: "取消",
			buttons: actionbuttons
		};
		plus.nativeUI.actionSheet(actionstyle, function(e) {
			switch(e.index) {
				case 1:
					createDownload(path);
					break;
			}
		});
	});

	uploadPhoto.addEventListener('tap', function() {
		mui.openWindow({
			url: "paddPhoto.html?class_id=" + oPdata.class_id,
			id: "paddPhoto",
			createNew: true,
			show: {
				duration: 300
			}
		});
	});

	mui('body').on('tap', "#btnDel", function(e) {
		if(ubtnDel.innerHTML == "删除") {
			ubtnDel.innerHTML = "完成";
			uList.classList.add("hxx-delete");
		} else if(ubtnDel.innerHTML == "完成") {
			ubtnDel.innerHTML = "删除";
			uList.classList.remove("hxx-delete");
		}
	});

	mui('body').on('tap', ".hxx-delete .hxx-photo-cell", function(e) {
		var self = this;
		var tempitem = self.affix;
		if(window.confirm('确定要删除该照片吗？')){
			for(var i = 0; i < self.parentNode.childNodes.length; i++) {
				var node = self.parentNode.childNodes[i];
				if(node.nodeType == 3 && !/\S/.test(node.nodeValue)) {
					node.parentNode.removeChild(node);
				}
			}
			if(self.parentNode.childNodes.length == 1) {
				uList.removeChild(self.parentNode.parentNode);
			} else {
				self.parentNode.removeChild(self);
			}
			delImgFromDatabase(tempitem);
		}
	});
}

//下拉刷新
function PulldownRefresh() {
	if(oPdata.end_id == 0) {
		uList.innerHTML = '';
	}
	cache.myajax("photoAction/getPhotoList", {
		data: {
			student_id: cache.getChild().student_id,
			class_id: oPdata.class_id,
			user_type: "003015",
			photo_type: "006010",
			add_date: oPdata.add_date,
			direction: "1",
			start_id: oPdata.end_id,
			limit: oPdata.page_size
		},
		success: function(data) {
			if(data.result.data.length > 0) {
				if(oPdata.end_id == 0) {
					if(data.result.data.length < oPdata.page_size) {
						pull.endPullUpToRefresh(true);
					}
				}
				oPdata.end_id = data.result.data[0].photo_id;
				InsertDocumentFragment(data.result.data);
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
		error: function(data) {
			mui.toast('获取数据失败，请重新获取');
			pull.endPullDownToRefresh();
		}
	});
};
//上拉加载获取数据
function PullupRefresh() {
	cache.myajax("photoAction/getPhotoList", {
		data: {
			student_id: cache.getChild().student_id,
			class_id: oPdata.class_id,
			user_type: "003015",
			photo_type: "006010",
			add_date: oPdata.add_date,
			direction: "0",
			start_id: oPdata.begin_id,
			limit: oPdata.page_size
		},
		success: function(data) {
			if(data.result.data.length > 0) {
				ApppendDocumentFragment(data.result.data);
				pull.endPullUpToRefresh(false);
			} else {
				pull.endPullUpToRefresh(true);
			}
			pull.endPullUpToRefresh();
		},
		error: function(data) {
			mui.toast('获取数据失败，请重新获取');
			pull.endPullUpToRefresh();
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
				uList.lastChild.querySelector('.hxx-photo-group').appendChild(CreateImg(e[i]));
				sLastDate = sDate;
				continue;
			} else {
				uDomFragment.lastChild.querySelector('.hxx-photo-group').appendChild(CreateImg(e[i]));
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
	var uFigure = uList.firstChild.querySelector(".hxx-photo-group");
	var iFisrtElement = true;
	for(var i = e.length - 1; i >= 0; i--) {
		var sDate = cache.date2str(cache.date(e[i].send_time));
		if(!cache.isNull(sLastDate) && sLastDate === sDate) {
			if(iFisrtElement) {

				uFigure.insertBefore(CreateImg(e[i]), uFigure.firstChild);
				sLastDate = sDate;
				continue;
			} else {
				uDomFragment.firstChild.querySelector('.hxx-photo-group').insertBefore(CreateImg(e[i]), uDomFragment.firstChild.querySelector('.hxx-photo-group').firstChild);
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
	uLi.innerHTML = '<figcaption class="hxx-photo-date">' + cache.theValue(cache.date2str(cache.date(e.send_time))) + '</figcaption>';
	var uPhotoGroup = document.createElement("figure");
	uPhotoGroup.className = 'hxx-photo-group';
	uPhotoGroup.appendChild(CreateImg(e));
	uLi.appendChild(uPhotoGroup);
	return uLi;
}

function CreateImg(e) {
	var userId = cache.getUserId();
	oPdata.begin_id = e.photo_id;
	var uImg = document.createElement("div");

	if(e.sender_id == userId) {
		uImg.className = 'hxx-photo-cell';
	} else {
		uImg.className = 'hxx-photo-cell-other';
	}

	uImg.innerHTML = '<img class="hxx-photo-cell-img" src="' + cache.theValue(e.photo_resize_url) + '" data-preview-src="' + cache.theValue(e.photo_url) + '" data-preview-group="1"/>';
	uImg.querySelector('.hxx-photo-cell-img').onerror = function() {
		this.src = '../images/default-img.png';
	};
	uImg.affix = e.photo_id;
	return uImg;
}

//下载照片
function createDownload(path) {
	uTip.innerText = '照片下传中';
	cache.showDom(uTip);
	cache.downloadFile(path, {
		success: function(data) {
			cache.hideDom(uTip);
			plus.gallery.save(data, function() {});
			mui.toast("下载完成！");
		},
		download: function(e) {
			var d = Math.floor(e.downloadedSize / e.totalSize * 100);
			if(e.downloadedSize == 0) {
				uTip.innerText = '已完成: 00%';

			} else {
				uTip.innerText = '已完成: ' + d + '%';
			}
		},
		error: function(data) {
			console.log(" download fail");
		}
	})
}


function delImgFromDatabase(tempitem) {
	cache.myajax("photoAction/deletePhoto", {
		data: {
			user_type: "003015",
			photo_ids: tempitem
		},
		success: function(data) {
			mui.toast("删除成功!");
			oPdata.end_id = 0;
			oPdata.begin_id = 0;
		},
		error: function(data) {}
	});
}