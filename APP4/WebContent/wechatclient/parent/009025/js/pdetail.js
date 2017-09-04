var uTap1 = document.getElementById("Tap1"),
	uTap2 = document.getElementById("Tap2"),
	uTap3 = document.getElementById("Tap3"),
	uList = document.getElementById("List"),
	uTitle = document.getElementById("Title");
var wvSelf = null;
var oPScore = {},
	oPitem = {};
mui.init();
mui.ready(function(){
	cache.showDom(uTap3);
	oPScore.id = getParamValue("id");
	oPScore.student_id = getParamValue("student_id");
	BindEvent();
	InitScroll();
	InitPage();
});

function BindEvent(){
	uTap1.addEventListener('tap',function(){
		InitPage();
	});
};
function InitPage(){
	cache.myajax("scoreAction/getScoreListByID", {
		data: {
			user_type :"003015",
			score_id:oPScore.id,
			student_id:oPScore.student_id
		},
		success: function(data) {
			var r = data.result.data;
			if (!cache.isArray(r)||r.length<=0) {
				cache.showDom(uTap2);
				cache.hideDom(uTap3);
				return;
			}
			for (var i = 0; i < r.length; i++) {
				if(r[i].student_id != oPScore.student_id) {
					continue;
				}
				if (r[i].score_type == '012015') {
					uList.appendChild(CreateElement1(r[i]));
				}else{
					uList.appendChild(CreateElement(r[i]));
				}
			}
			cache.hideDom(uTap3);
		},
		error: function(err) {
			cache.showDom(uTap1);
			cache.hideDom(uTap3);
		}
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
function CreateElement(e){
	document.title = e.student_name +cache.dictCode2Value(e.score_type);
	var li = document.createElement("li");
	li.className = 'hxx-list-view-cell';
	li.setAttribute('data-id',e.list_id);
	var ele_txt = '<div class="hxx-cell-score">'+cache.isScore(e.score)+'</div>\
	        		<div class="hxx-cell-reason">'+cache.theValue(e.content)+'</div>\
	        		<div class="hxx-cell-bottom">\
	        		  	<p class="hxx-score-time">'+cache.theValue(e.score_date)+'</p>\
	        		  	<em class="hxx-score-user">检查老师：'+cache.theValue(e.user_name)+'</em>\
	        		</div>';
	li.innerHTML = ele_txt;
	return li;
};
function CreateElement1(e){
	uTitle.innerText = e.student_name +cache.dictCode2Value(e.score_type);
	var li = document.createElement("li");
	li.className = 'hxx-list-view-cell';
	li.setAttribute('data-id',e.list_id);
	var ele_txt = '<div class="hxx-cell-atten">'+cache.theValue(e.content)+'</div>\
	        		<div class="hxx-cell-bottom">\
	        		  	<p class="hxx-score-time">'+cache.theValue(e.score_date)+'</p>\
	        		  	<em class="hxx-score-user">检查老师：'+cache.theValue(e.user_name)+'</em>\
	        		</div>';
	li.innerHTML = ele_txt;
	return li;
};
