var	uList = document.getElementById("list"),
	uVideos = document.getElementById("videos"),
	uScrollWrapper = document.querySelector('.mui-slider'),
	uTip1 = document.getElementById("tap_center1"),
	uTip2 = document.getElementById("tap_center2"),
	uTip3 = document.getElementById("tap_center3");
mui.ready(function(){
	cache.showDom(uTip3);
	InitScroll();
});

mui.init();

mui.ready(function() {
	BindEvent();
	InitPage();
});

function InitScroll(){
	var deceleration = mui.os.ios?0.003:0.0009;
	uScrollWrapper.style.height = (window.innerHeight-50) + "px";
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration:deceleration
	});
	//循环初始化所有下拉刷新，上拉加载。
	uPull = mui('.mui-scroll').pullToRefresh({
		down: {
			callback: function() {
				fDownRefresh();
			}
		},
		up: {
			callback: function() {
				fUpRefresh();
			}
		}
	});
};

function InitPage(){
	cache.ajax("teachCloudAction/getSourceList", {
		data: {
			student_id: cache.getChild().student_id
		},
		success: function(data) {
			var aRe = SortData(data);
			if (!cache.isArray(aRe)||aRe.length<=0) {
				cache.hideDom(uTip3);
				cache.showDom(uTip2);
				return;
			}
			ApppendDocumentFragment(aRe);
			cache.hideDom(uTip3);
		},
		error: function(err){
			cache.hideDom(uTip3);
			cache.showDom(uTip1);
		}
	});
};

function BindEvent(){
	/*document.getElementById("videos").addEventListener('tap',function(){
		mui.openWindow({
			url: "videos.html",
			id: "videos"
		});
	});*/
};


function fUpRefresh(){
	setTimeout(function(){
		uPull.endPullUpToRefresh();
	},600);
};

function fDownRefresh(){
	setTimeout(function(){
		uPull.endPullDownToRefresh(true);
	},600);
};

function ApppendDocumentFragment(aRe) {
	var uDomFragment = document.createDocumentFragment();
	for (var i = 0; i < aRe.length; i++) {
		var e = aRe[i];
		try{
			var oBj = JSON.parse(e.source_data); 
		}catch(e){
			console.log(JSON.stringify(e));
			return;
		}
		oBj.send_time = e.send_time;
		oBj.remark = e.remark;
		oBj.sender_name = e.sender_name;
		if (oBj.ExtensionName != 'SWF') {
			uDomFragment.appendChild(CreateElement(oBj));
		}
	}
	uList.appendChild(uDomFragment);
};

function InsertDocumentFragment(aRe) {
	var uDomFragment = document.createDocumentFragment();
	for (var i = 0; i < aRe.length; i++) {
		var e = aRe[i];
		try{
			var oBj = JSON.parse(e.source_data); 
		}catch(e){
			console.log(JSON.stringify(e));
			return;
		}
		oBj.send_time = e.send_time;
		oBj.remark = e.remark;
		oBj.sender_name = e.sender_name;
		if (oBj.ExtensionName != 'SWF') {
			uDomFragment.appendChild(CreateElement(oBj));
		}
	}
	uList.insertBefore(uDomFragment,uList.firstChild);
};

function CreateElement(e){
	var uLi = document.createElement("li");
	uLi.affixData = e;
	uLi.className = 'mui-table-view-cell';
	uLi.innerHTML = '<div class="hxx-cell-head">\
		           		<img class="hxx-head-page" src="'+e.ResourceIcon+'"/>\
		           	</div>\
		           	<div class="hxx-cell-item">\
		           		<h4 class="hxx-item-page-name">'+e.ResourceName+'</h4>\
		           		<div class="hxx-item-receive-time">收到时间：'+cache.timeStamp2str(e.send_time)+'</div>\
		           		<div class="hxx-item-send">'+e.sender_name+'：'+e.remark+'</div>\
		           	</div>';
	uLi.addEventListener('tap',function(){
		var item = this.affixData;
		mui.openWindow({
			url: "pdetail.html?ResourceUrl=" + item.ResourceUrl + "&ResourceName=" + item.ResourceName + "&ExtensionName=" + item.ExtensionName,
			id: "pdetail",
			extras: {
				item: this.affixData
			}
		});
	});
	return uLi;
};

function SortData(e) {
	var temp = "";
	for(var i = 0; i < e.length; i++) {
		for(var j = 0; j < i; j++) {
			if(parseInt(e[j].send_time) <= parseInt(e[i].send_time)) {
				temp = e[j];
				e[j] = e[i];
				e[i] = temp;
			}
		}
	}
	return e;
}

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