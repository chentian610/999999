var uList = document.getElementById('list'),
	uTap1 = document.getElementById("tap_center1"),
	uTap2 = document.getElementById("tap_center2"),
	uTap3 = document.getElementById("tap_center3");

var uMaxUl = null,
	uMinUl = null;

oPdata.page_size = 20;
oPdata.end_id = '';
oPdata.start_id = '';
oPitem.maxDate = null;
oPitem.minDate = null;
oPitem.child = {};
parent.ChangeHightLineTab("phome");
mui.init({
	pullRefresh: {
		container: '#pullrefresh',
		down: {
			callback: function(){
				setTimeout(PulldownRefresh,300);
			}
		},
		up: {
			auto: true,
			contentrefresh: '正在加载...',
			callback: function(){
				setTimeout(PullupRefresh,300);
			}
		}
	}
});

mui.ready(function() {
	uList.innerHTML = '';
	BindEvent();
});

function BindEvent(){
	window.addEventListener('reSelectChild',function(e){
		var e = e.detail;
		uList.innerHTML = '';
		oPdata.end_id = '';
		oPdata.start_id = '';
		mui('#pullrefresh').pullRefresh().pullupLoading();
	});
};

//下拉刷新
function PulldownRefresh() {
	cache.myajax('infoAction/getInformation', {
		data: {
			user_type: "003015",
			start_key: oPdata.end_id,
			direction: 1,
			student_id: cache.getChild().student_id,
			limit: oPdata.page_size
		},
		success: function(data) {
			var aRe = data.result.data;
			if(!cache.isArray(aRe) || aRe.length <= 0) {
				mui('#pullrefresh').pullRefresh().endPulldownToRefresh();
				return;
			}
			oPdata.end_id = aRe[0].key;
			InsertDocumentFragment(aRe);
			mui('#pullrefresh').pullRefresh().endPulldownToRefresh();
		},
		error: function(err) {
			mui('#pullrefresh').pullRefresh().endPulldownToRefresh();
		}
	});

};
//上拉加载
function PullupRefresh() {
	cache.myajax('infoAction/getInformation', {
		data: {
			user_type: "003015",
			start_key: oPdata.start_id,
			direction: 0,
			student_id: cache.getChild().student_id,
			limit: oPdata.page_size
		},
		success: function(data) {
			mui('#pullrefresh').pullRefresh().endPullupToRefresh();
			var aRe = data.result.data;
			if(!cache.isArray(aRe) || aRe.length <= 0) {
				mui('#pullrefresh').pullRefresh().endPullupToRefresh(true);
				return;
			}
			oPdata.start_id = aRe[aRe.length - 1].key;
			if (cache.isNull(oPdata.end_id)) {
				oPdata.end_id = aRe[0].key;
			}
			AppendDocumentFragment(aRe);
		},
		error: function() {
			mui('#pullrefresh').pullRefresh().endPullupToRefresh();
		}
	});
};

function AppendDocumentFragment(e) {
	var uDocumentFragment = document.createDocumentFragment();
	for(var i = 0; i < e.length; i++) {
		if (isBad(e[i])) {
			continue;
		}
		if(oPitem.minDate == cache.dateStamp2str(e[i].info_date)) {
			uDocumentFragment.appendChild(CreateElement(e[i]));
		} else {
			oPitem.minDate = cache.dateStamp2str(e[i].info_date);
			uDocumentFragment.appendChild(CreateElementDate(e[i]));
			uDocumentFragment.appendChild(CreateElement(e[i]));
		}
	}
	uList.appendChild(uDocumentFragment);
};

function InsertDocumentFragment(e) {
	e.reverse();
	oPitem.maxDate = uList.firstChild.affixData;
	var firstDom = null;
	for(var i = 0; i < e.length; i++) {
		if (isBad(e[i])) {
			continue;
		}
		if(oPitem.maxDate == cache.dateStamp2str(e[i].info_date)) {
			firstDom = uList.querySelector('.hxx-info-cell');
			uList.insertBefore(CreateElement(e[i]), firstDom);
		} else {
			oPitem.maxDate = cache.dateStamp2str(e[i].info_date);
			uList.insertBefore(CreateElement(e[i]),uList.firstChild);
			uList.insertBefore(CreateElementDate(e[i]),uList.firstChild);
		}
	}
};

function AppendElementLi(e) {

};

function InsertElementLi(e) {

}

function CreateElementDate(e) {
	e.send_time_str = cache.dateStamp2str(e.info_date);
	var uDate = document.createElement("div");
	uDate.affixData = e.send_time_str;
	uDate.className = 'hxx-info-date';
	uDate.innerText = uDate.affixData;
	return uDate;
};

function CreateElementLi(e) {
	var obj = cache.moduleCode2Obj(e.module_code);
	var uDiv = document.createElement("div");
	uDiv.className = 'hxx-info-cell';
	uDiv.innerHTML = '<img class="hxx-info-logo" src="' + obj.icon_url + '">\
					<span class="hxx-info-name">' + obj.module_name + '</span>\
					<span class="hxx-info-time">' + cache.hourStamp2str(e.info_date) + '</span>\
					<h4 class="hxx-info-title">' + e.info_title + '</h4>\
					<p class="hxx-info-content">' + e.info_content + '</p>';
	return uDiv;
};

function isBad(e){
	if (cache.isNull(e.module_code)) {
		console.log('module_code'+JSON.stringify(e));
		return true;
	}
	if (cache.isNull(e.info_date)) {
		console.log('info_date'+JSON.stringify(e));
		return true;
	}
	if (cache.isNull(e.info_title)) {
		console.log('info_title'+JSON.stringify(e));
		return true;
	}
	return false;
}

function CreateElement009002(e) {
	var obj = cache.moduleCode2Obj(e.module_code);
	var uDiv = document.createElement("div");
	uDiv.className = 'hxx-info-cell';
	uDiv.innerHTML = '<img class="hxx-info-logo" src="' + obj.icon_url + '">\
					<span class="hxx-info-name">' + obj.module_name + '</span>\
					<span class="hxx-info-time">' + cache.hourStamp2str(e.info_date) + '</span>\
					<h4 class="hxx-info-title">' + e.info_title + '</h4>';
	var uImgs = document.createElement("div");
	uImgs.className = 'hxx-info-images';
	try {
		var photo_url_list = JSON.parse(e.photo_url_list);
		var photo_list_txt = '';
		for(var i = 0; i < photo_url_list.length; i++) {
			if(i < 5) {
				photo_list_txt += '<img class="hxx-info-image" src="' + photo_url_list[i].file_resize_url + '"/>';
			} else {
				break;
			}
		}
		uImgs.innerHTML = photo_list_txt;
	} catch(e) {
		console.log(e);
	}
	uDiv.appendChild(uImgs);
	return uDiv;
};

function CreateElement(e) {
	var uLi = null;
	if(e.module_code == '009002') {
		uLi = CreateElement009002(e);
	} else {
		uLi = CreateElementLi(e);
	}
	uLi.dataAffix = e;
	uLi.addEventListener('tap', function() {
		var self = this;
		if(self.disable) {
			return;
		}
		self.disable = true;
		setTimeout(function() {
			self.disable = false;
			self.style.backgroundColor = '#ffffff';
			var pItem = self.dataAffix;
			pItem.id = pItem.module_pkid;
			var url = '../' + pItem.module_code + '/p' + pItem.info_url + "?id=" + pItem.id;
			var dict_group = pItem.dict_group;
			if(typeof(dict_group) != "undefined"){
				url += "&news_code=" + pItem.news_code + "&dict_group=" + dict_group;
			}
			var student_id = pItem.student_id;
			if(typeof(student_id) != "undefined" && student_id > 0){
				url += "&student_id=" + pItem.student_id;
			}
			if(pItem.module_code == "009012"){
				cache.myajax('teachCloudAction/getSourceByID', {
					data: {
						id: pItem.id
					},
					async: false,
					success: function(data) {
						var d = JSON.parse(data.result.data.source_data);
						url += "&ResourceUrl=" + d.ResourceUrl + "&ResourceName=" + d.ResourceName + "&ExtensionName=" + d.ExtensionName;
					}
				});
			}
			mui.openWindow({
				url: url,
				id: "detail",
				extras: {
					item: pItem
				},
				createNew: true,
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		}, 50);
		self.style.backgroundColor = '#efeff4';

	});
	return uLi;
};