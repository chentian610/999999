var	uList = document.getElementById("list"),
	uHistory = document.getElementById("history"),
	uSearch = document.getElementById("search"),
	uTip1 = document.getElementById("tap_center1"),
	uTip2 = document.getElementById("tap_center2"),
	uSearchValue = document.getElementById("searchValue"),
	uHistoryList = document.getElementById("historyList"),
	uResetHisList = document.getElementById("resetHisList"),
	uFooterBtn = document.getElementById("footerBtn"),
	uClassList = document.getElementById("classList"),
	uScrollWrapper = document.querySelector('.mui-scroll-wrapper'),
	uGradeBtn = document.getElementById("gradeBtn"),
	uCourseBtn = document.getElementById("courseBtn"),
	uVersionBtn = document.getElementById("versionBtn"),
	uFiltrate = document.getElementById("filtrate"),
	uClassListTitle = document.getElementById("classListTitle"),
	uTip3 = document.getElementById("tap_center3");

var oPCache = null;
	
oPitem.showMenu = false;
oPitem.showSearch = false;
oPitem.isSearch = false;
oPitem.showSelect = false;
oPitem.aCheckedList = [];
	
oPdata.VersionId = 'V16';
oPdata.GradeId = 'G04';
oPdata.CoursesId = 'K01';
oPdata.Page_num = '1';
oPdata.ResourceName = '';
oPdata.Limit = '20';
oPdata.IsCount_size = '1';
oPdata.ExtensionName = 'MP4';
	
mui.ready(function(){
	cache.showDom(uTip3);
	//筛选开始
	if (!mui.os.android) {
		var spans = document.querySelectorAll('.android-only');
		for (var i = 0, len = spans.length; i < len; i++) {
			cache.hideDom(spans[i]);
		}
	}
	InitScroll();
});

mui.init({
	swipeBack: false,
	beforeback: back
});

mui.plusReady(function() {
	mask = mui.createMask(closeMenu);
	Preload();
	BindEvent();
	InitPage();
	InitSearch();
});

function Preload(){
	setTimeout(function() {
		menu = mui.preload({
			id: 'menu',
			url: 'menu.html',
			styles: {
				left: "30%",
				width: '70%',
				zindex: 9997
			}
		});
	}, 100);
};

function InitSearch(){
	var aHistoryList = cache._get('HistoryList');
	if (cache.isNull(aHistoryList)) {
		cache._set('HistoryList',[]);
		return;
	}else{
		for (var i = 0; i < aHistoryList.length; i++) {
			var uLi = document.createElement("li");
			uLi.className = 'mui-table-view-cell';
			uLi.innerText = aHistoryList[i];
			uLi.addEventListener('tap',function(){
				uSearchValue.value = this.innerText;
			});
			uHistoryList.appendChild(uLi);
		}
	}
};

function InitScroll(){
	var deceleration = mui.os.ios?0.003:0.0009;
	uScrollWrapper.style.height = (window.innerHeight-130) + "px";
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration:deceleration
	});
	//循环初始化所有下拉刷新，上拉加载。
	uPull = mui('.mui-scroll').pullToRefresh({
		up: {
			callback: function() {
				fUpRefresh();
			}
		}
	});
};

function InitPage(){
	cache.showDom(uTip3);
	cache.hideDom(uTip1);
	cache.hideDom(uTip2);
	uPull.finished = true;
	mui('.mui-scroll-wrapper').scroll().scrollTo(0,0,100);
	if (oPitem.aCheckedList.length>0) {
		for (var i = 0; i < oPitem.aCheckedList.length; i++) {
			oPitem.aCheckedList[i].checkBox.checked = false;
		}
		uSelectList.innerHTML = '';
		oPitem.aCheckedList = [];
		mask.close();
		cache.hideDom(uFooterBtn);
		if (oPitem.isSearch) {
			uScrollWrapper.style.height = (window.innerHeight-81) + "px";
		}else{
			uScrollWrapper.style.height = (window.innerHeight-130) + "px";
		}
	}
	uList.innerHTML = '';
	cache.getResources({
		VersionId: oPdata.VersionId,
		Limit: oPdata.Limit,
		Page_num: oPdata.Page_num.toString(),
		IsCount_size: oPdata.IsCount_size,
		GradeId: oPdata.GradeId,
		CoursesId: oPdata.CoursesId,
		ResourceName: oPdata.ResourceName,
		ExtensionName: oPdata.ExtensionName
	},
	function(e){
		if (!cache.isArray(e)||e.length<=0) {
			cache.hideDom(uTip3);
			cache.showDom(uTip2);
			return;
		}
		ApppendDocumentFragment(e);
		cache.hideDom(uTip3);
		setTimeout(function(){
			uPull.finished = false;
		},1000);
	},
	function(e){
		cache.hideDom(uTip3);
		setTimeout(function(){
			uPull.finished = false;
		},1000);
	});
};

function BindEvent(){
	//筛选按钮点击事件绑定
	uFiltrate.addEventListener('tap',function(){
		openMenu();
	});
	
	window.addEventListener('initPage',function(e){
		mui.extend(true,oPitem,e.detail);
		uGradeBtn.innerText = oPitem.oGrade.text;
		uCourseBtn.innerText = oPitem.oCourses.text;
		uVersionBtn.innerText = oPitem.oVersion.text;
		mask.close();
		oPdata.VersionId = oPitem.oVersion.ID;
		oPdata.Page_num = '1';
		oPdata.GradeId = oPitem.oGrade.ID;
		oPdata.CoursesId = oPitem.oCourses.ID;
		oPdata.ResourceName = '';
		InitPage();
	});
	
	uSearchValue.addEventListener('focus',function(){
		cache.hideDom(uTip1);
		cache.hideDom(uTip2);
		cache.hideDom(uTip3);
		oPitem.showSearch = true;
		cache.showDom(uHistory);
	});
	
	uResetHisList.addEventListener('tap',function(){
		cache._set('HistoryList',[]);
		uHistoryList.innerHTML = '';
	});
	
	uSearch.addEventListener('tap',function(){
		cache.hideDom(uFiltrate);
		uScrollWrapper.style.height = (window.innerHeight - 81)+'px';
		uScrollWrapper.style.top = '80px';
		cache.hideDom(uHistory);
		oPitem.showSearch = false;
		oPitem.isSearch = true;
		oPdata.VersionId = '';
		oPdata.GradeId = '';
		oPdata.CoursesId = '';
		oPdata.ResourceName = uSearchValue.value;
		oPdata.Page_num = '1';
		InitPage();
		
		var aHistoryList = cache._get('HistoryList');
		for (var i = 0; i < aHistoryList.length; i++) {
			if (aHistoryList[i] == uSearchValue.value) {
				aHistoryList.splice(i,1);
				aHistoryList.unshift(uSearchValue.value);
				return;
			}
		}
		aHistoryList.unshift(uSearchValue.value);
		cache._set('HistoryList',aHistoryList);
		var uLi = document.createElement("li");
		uLi.className = 'mui-table-view-cell';
		uLi.innerText = uSearchValue.value;
		uLi.addEventListener('tap',function(){
			uSearchValue.value = this.innerText;
		});
		uHistoryList.insertBefore(uLi,uHistoryList.firstChild);
	});
};

function back() {
	if (oPitem.showMenu) {
		mask.close();
		return false;
	}else if(oPitem.showSearch){
		uSearchValue.blur();
		cache.hideDom(uHistory);
		oPitem.showSearch = false;
		return false;
	}else if(oPitem.isSearch){
		uGradeBtn.innerText = '';
		uCourseBtn.innerText = '';
		uVersionBtn.innerText = '';
		cache.showDom(uFiltrate);
		uScrollWrapper.style.height = (window.innerHeight - 130)+'px';
		uScrollWrapper.style.top = '130px';
		oPdata.VersionId = '';
		oPdata.VersionId = 'V16';
		oPdata.GradeId = 'G04';
		oPdata.CoursesId = 'K01';
		oPdata.Page_num = '1';
		InitPage();
		oPitem.isSearch = false;
		return false;
	}else{
		menu.close('none');
		return true;
	}
}

function openMenu() {
	if (!oPitem.showMenu) {
		if (mui.os.android && parseFloat(mui.os.version) < 4.4) {
			document.querySelector("header.mui-bar").style.position = "static";
			document.querySelector(".mui-bar-nav~.mui-content").style.paddingTop = "0px";
		}
		menu.show('none', 0, function() {
			menu.setStyle({
				left: '30%',
				transition: {
					duration: 150
				}
			});
		});
		mask.show();
		oPitem.showMenu = true;
	}
}

function closeMenu() {
	if (oPitem.showMenu) {
		if (mui.os.android && parseFloat(mui.os.version) < 4.4) {
			document.querySelector("header.mui-bar").style.position = "fixed";
			document.querySelector(".mui-bar-nav~.mui-content").style.paddingTop = "44px";
		}
		menu.setStyle({
			left: '100%',
			transition: {
				duration: 150
			}
		});
		setTimeout(function() {
			menu.hide();
		}, 300);
		oPitem.showMenu = false;
	}
	if (oPitem.showSelect) {
		cache.hideDom(uClassList);
	}
};

function fUpRefresh(){
	oPdata.Page_num++;
	cache.getResources({
		VersionId: oPdata.VersionId,
		Limit: oPdata.Limit,
		Page_num: oPdata.Page_num.toString(),
		IsCount_size: oPdata.IsCount_size,
		GradeId: oPdata.GradeId,
		CoursesId: oPdata.CoursesId,
		ResourceName: oPdata.ResourceName,
		ExtensionName: oPdata.ExtensionName
	},
	function(e){
		if (!cache.isArray(e)||e.length<=0) {
			uPull.endPullUpToRefresh(true);
			return;
		}
		ApppendDocumentFragment(e);
		if (e.length<20) {
			uPull.endPullUpToRefresh(true);
		}else{
			uPull.endPullUpToRefresh();
		}
	},
	function(e){
		uPull.endPullUpToRefresh(true);
	});
};


function ApppendDocumentFragment(e) {
	var uDomFragment = document.createDocumentFragment();
	for (var i = 0; i < e.length; i++) {
		if (e[i].ExtensionName != 'SWF') {
			uDomFragment.appendChild(CreateElement(e[i]));
		}
	}
	uList.appendChild(uDomFragment);
};

function InsertDocumentFragment(e) {
	var uDomFragment = document.createDocumentFragment();
	for (var i = 0; i < e.length; i++) {
		if (e[i].ExtensionName != 'SWF') {
			uDomFragment.appendChild(CreateElement(e[i]));
		}
	}
	uList.insertBefore(uDomFragment,uList.firstChild);
};

function CreateElement(e){
	var uLi = document.createElement("li");
	uLi.affixData = e;
	uLi.className = 'mui-table-view-cell';
	uLi.innerHTML = '<div class="hxx-cell-head">\
		           		<img class="hxx-head-page" src="'+GetIconURLbyFileType(e.ExtensionName)+'"/>\
		           	</div>\
		           	<div class="hxx-cell-item">\
		           		<h4 class="hxx-item-page-name">'+e.ResourceName+'</h4>\
		           		<div class="hxx-item-star">\
		           			<span>\
		           				<i class="hxx-hxx-icon hxx-icon-star">&#xe622;</i>\
		           				<i class="hxx-hxx-icon hxx-icon-star">&#xe622;</i>\
		           				<i class="hxx-hxx-icon hxx-icon-star">&#xe622;</i>\
		           				<i class="hxx-hxx-icon hxx-icon-star">&#xe622;</i>\
								<i class="hxx-hxx-icon hxx-icon-star">&#xe622;</i>\
							</span>\
		           		</div>\
		           		<h5 class="hxx-item-page-read">阅读量：'+e.Browse_count+'</h5>\
		           	</div>';
	uLi.querySelector('.hxx-cell-item').addEventListener('tap',function(){
		mui.openWindow({
			url: "pdetail.html",
			id: "pdetail",
			extras: {
				item: this.parentNode.affixData
			}
		});
	});
	return uLi;
};

function GetIconURLbyFileType(e){
	switch (e){
		case 'PPT':
			return '../images/ppt.png';
		case 'DOCX':
			return '../images/doc.png';
		case 'DOC':
			return '../images/doc.png';
		case 'SWF':
			return '../images/flash.png';
		case 'JPG':
			return '../images/jpg.png';
		case 'MP3':
			return '../images/mp4.png';
		case 'PNG':
			return '../images/jpg.png';
		case 'MP4':
			return '../images/mp4.png';
		case 'XLS':
			return '../images/xls.png';
		case 'TXT':
			return '../images/txt.png';
		case 'PPTX':
			return '../images/ppt.png';	
		default:
			return '../images/photo.png';
	}
}
//end