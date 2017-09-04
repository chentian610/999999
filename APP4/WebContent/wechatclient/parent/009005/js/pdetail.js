var uTap1 = document.getElementById("tap_center1"),
	uTap2 = document.getElementById("tap_center2"),
	uTap3 = document.getElementById("tap_center3"),
	uList = document.getElementById("list");
var oPdata = {};
oPitem = {};
child = {};

mui.init();
mui.ready(function() {
	cache.showDom(uTap3);
	child = cache.getChild();
	oPdata.id = getParamValue("id");
	BindEvent();
	InitScroll();
	InitPage();
});

function BindEvent() {
	uTap1.addEventListener('tap', function() {
		InitPage();
	});
};

function InitPage() {
	cache.myajax("scoreAction/getScoreListByID", {
		data: {
			user_type: "003015",
			score_id: oPdata.id
		},
		success: function(data) {
			var aRe = data.result.data;
			if(!aRe.length && aRe.length <= 0) {
				cache.showDom(uTap2);
				cache.hideDom(uTap3);
				return;
			}
			var attend_item = '';
			console.log("11"+JSON.stringify(aRe));
			var student_id = getParamValue("student_id");
			if(student_id == null){
				student_id = child.student_id;
			}
			for(var i = 0; i < aRe.length; i++) {
				if(aRe[i].student_id != student_id) {
					continue;
				}
				attend_item = aRe[i].attend_item;
				if(aRe[i].score_type == '012015') {
					uList.appendChild(createElement1(aRe[i]));
				} else {
					uList.appendChild(createElement(aRe[i]));
				}
			}
			cache.getAttendCodeByName({
				code: attend_item,
				success: function(name){
					document.title = cache.theValue(child.student_name) + name+ '考勤';
				},
				error: function(err){
					document.title = cache.theValue(child.student_name) + '考勤';
				}
			})
			cache.hideDom(uTap3);
		},
		error: function(err) {
			cache.showDom(uTap1);
			cache.hideDom(uTap3);
		}
	});
};

function InitScroll() {
	var deceleration = mui.os.ios ? 0.003 : 0.0009;
	document.querySelector('.mui-slider').style.height = (window.innerHeight - 50) + "px";
	mui('.mui-scroll-wrapper').each(function(i, e) {
		e.style.height = (window.innerHeight - 90) + "px";
	});
	mui('.mui-scroll-wrapper').scroll({
		bounce: false,
		indicators: true, //是否显示滚动条
		deceleration: deceleration
	});
}

function createElement(e) {
	var li = document.createElement("li");
	li.className = 'hxx-list-view-cell';
	li.setAttribute('data-id', e.list_id);
	var ele_txt = '<div class="hxx-cell-score">' + cache.isScore(e.score) + '</div>\
	        		<div class="hxx-cell-reason">' + cache.theValue(e.content) + '</div>\
	        		<div class="hxx-cell-bottom">\
	        		  	<p class="hxx-score-time">' + cache.theValue(e.score_date) + '</p>\
	        		  	<em class="hxx-score-user">检查老师：' + cache.theValue(e.user_name) + '</em>\
	        		</div>';
	li.innerHTML = ele_txt;
	return li;
};

function createElement1(e) {
	var li = document.createElement("li");
	li.className = 'hxx-list-view-cell';
	li.setAttribute('data-id', e.list_id);
	var ele_txt = '<div class="hxx-cell-atten">' + cache.theValue(e.content) + '</div>\
	        		<div class="hxx-cell-bottom">\
	        		  	<p class="hxx-score-time">' + cache.theValue(e.score_date) + '</p>\
	        		  	<em class="hxx-score-user">检查老师：' + cache.theValue(e.user_name) + '</em>\
	        		</div>';
	li.innerHTML = ele_txt;
	return li;
};