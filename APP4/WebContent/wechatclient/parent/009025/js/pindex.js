var uTap1 = document.getElementById("Tap1"),
	uTap2 = document.getElementById("Tap2"),
	uTap3 = document.getElementById("Tap3"),
	uList1 = document.getElementById("List1"),
	uList2 = document.getElementById("List2"),
	uList3 = document.getElementById("List3");
var wvSelf = null;
	oPitem = {};
	oPdata = {};
oPdata.arr_slider = Array();
oPdata.slideNumber = 0;
oPdata.child = {};
mui.init();
mui.ready(function(){
	oPdata.arr_slider[0] = 3;
	oPdata.arr_slider[1] = 3;
	oPdata.arr_slider[2] = 3;
	cache.showDom(uTap3);
	
	oPdata.child = cache.getChild();
	Update();
	BindEvent();
	InitScroll();
	DownloadScoreList1();
	DownloadScoreList2();
	DownloadScoreList3();
});

function Update(){
	//wvSelf = plus.webview.currentWebview();
};
function BindEvent(){
	document.getElementById('slider').addEventListener('slide', function(e) {
		oPdata.slideNumber = e.detail.slideNumber;
		cache.hideDom(uTap1);
		cache.hideDom(uTap2);
		cache.hideDom(uTap3);
		switch (oPdata.arr_slider[oPdata.slideNumber]){
			case 1:
			cache.showDom(uTap1);
				break;
			case 2:
			cache.showDom(uTap2);
				break;
			case 3:
			cache.showDom(uTap3);
				break;
			default:
				break;
		}
	});
	//
	uTap1.addEventListener('tap',function(){
		DownloadScoreList1();
		DownloadScoreList2();
		DownloadScoreList3();
	});
};
function InitScroll(){
	var deceleration = mui.os.ios?0.003:0.0009;
	document.querySelector('.mui-slider').style.height = (window.innerHeight-50) + "px";
	mui('.mui-scroll-wrapper').each(function(i,e){
		e.style.height = (window.innerHeight-90) + "px";
	});
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration:deceleration
	});
}
function DownloadScoreList1(){
	
	cache.myajax("scoreAction/getScoreList", {
		data: {
			user_type :"003015",
			team_type: '011005',
			score_type: '012020',
			student_id: oPdata.child.student_id,
			direction: '0',
			start_id: 0
		},
		success: function(data) {
			var r = data.result.data;
			if (!r.length&&r.length<=0) {
				oPdata.arr_slider[0] = 2;
				cache.hideDom(uTap3);
				cache.showDom(uTap2);
				return;
			}
			for (var i = 0; i < r.length; i++) {
				uList1.appendChild(CreateElement(r[i]));
			}
			oPdata.arr_slider[0] = 0;
			cache.hideDom(uTap3);
		},
		error: function(err) {
			cache.hideDom(uTap3);
			cache.showDom(uTap1);
			oPdata.arr_slider[0] = 1;
		}
	});
};
function DownloadScoreList2(){
	
	cache.myajax("scoreAction/getScoreList", {
		data: {
			user_type :"003015",
			team_type: '011005',
			score_type: '012005',
			student_id: oPdata.child.student_id,
			direction: 0
		},
		success: function(data) {
			var r = data.result.data;
			if (!r.length&&r.length<=0) {
				oPdata.arr_slider[1] = 2;
				return;
			}
			for (var i = 0; i < r.length; i++) {
				uList2.appendChild(CreateElement(r[i]));
			}
			oPdata.arr_slider[1] = 0;
		},
		error: function(err) {
			oPdata.arr_slider[1] = 1;
		}
	});
};
function DownloadScoreList3(){
	cache.myajax("scoreAction/getScoreList", {
		data: {
			user_type :"003015",
			team_type: '011010',
			score_type: '012005',
			student_id: oPdata.child.student_id,
			direction: 0
		},
		success: function(data) {
			var r = data.result.data;
			if (!r.length&&r.length<=0) {
				oPdata.arr_slider[2] = 2;
				return;
			}
			for (var i = 0; i < r.length; i++) {
				uList3.appendChild(CreateElement(r[i]));
			}
			oPdata.arr_slider[2] = 0;
		},
		error: function(err) {
			oPdata.arr_slider[2] = 1;
		}
	});
};

function CreateElement(e){
	var li = document.createElement("li");
	li.className = 'mui-table-view-cell';
	li.setAttribute('data-id',e.score_id);
	var ele_txt = '<div class="hxx-cell-head">\
					  	<span class="'+(e.score>=0?'hxx-score-add':'hxx-score-min')+'">'+cache.isScore(e.score)+'</span>\
					</div>\
					<div class="hxx-cell-item">\
					  	<h4>'+cache.theValue(e.content)+'</h4>\
					  	<h5>'+cache.theValue(e.score_date)+'</h5>\
					</div>\
					<div class="hxx-cell-end">\
					  	<h4>'+cache.theValue(e.user_name)+'</h4>\
					</div>';
	li.innerHTML = ele_txt;
	return li;
};
