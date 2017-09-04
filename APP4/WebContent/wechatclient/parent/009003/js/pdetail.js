var uList2 = document.getElementById("list2");
var	uList = document.getElementById("list");

var uActiveBtn = null;
var aBtns = [];

mui.ready(function(){
	oPdata.id = getParamValue("id");
	InitPage();
	BindEvent();
});

function InitPage(){
	cache.ajax("homeworkAction/getHomeworkList", {
		data: {
			homework_id: oPdata.id,
			student_id : cache.getChild().student_id,
			user_type: "003015",
		},
		success: function(aRe) {
			uList.appendChild(CreateElementHead(aRe[0]));
			try{
				var oItemList = JSON.parse(aRe[0].item_list);
			}catch(e){
				oItemList = [];
				console.log(e);
			}
			for (var i = 0; i < oItemList.length; i++) {
				oItemList[i].index = i+1;
				oItemList[i].end_date = aRe[0].end_date;
				oItemList[i].create_date = aRe[0].create_date;
		    	uList2.appendChild(CreateElementItem(oItemList[i]));
			}
		},
		error: function(){
		}
	});
};

function BindEvent(){
	window.addEventListener("updateState",function(e){
		uActiveBtn.querySelector(".project-bar").innerText = cache.date2str() +"已完成";
		uActiveBtn.affixData.is_done = 1;
		uActiveBtn.is_done = 0;
		if (!cache.isArray(aBtns) || aBtns.length<=0) {
			return;
		}
		for (var i = 0; i < aBtns.length; i++) {
			if (aBtns[i].is_done == 1) {
				return;
			}
		}
		mui.fire(wvParent,'updateState',uActiveBtn.affixData);
	});
};


function CreateElementHead(e){
	var uLi = document.createElement("li");
	uLi.className ='ui-table-view-cell conter-all';
	if(cache.getDateToEnd(e.end_date) == "已到期"){
		uLi.innerHTML='<div class="icon-HeadPortraits course-finish">'+ cache.playgroupCourse2Name(e.course) +'</div>\
                <div class="Pname">'+ e.title +'</div>\
            	<div class="SubmitTime">'+ cache.dateStamp2str(e.create_date) +'</div>\
                <div class="titleNum remain-time-finish">已到期</div>';
	}else{
		uLi.innerHTML='<div class="icon-HeadPortraits course-finish">'+ cache.playgroupCourse2Name(e.course) +'</div>\
                <div class="Pname">'+ e.title +'</div>\
            	<div class="SubmitTime">'+ cache.dateStamp2str(e.create_date) +'</div>\
                <div class="titleNum remain-time-finish">距离截至还剩' + cache.getDateToEnd(e.end_date) + '</div>';
	}
	return uLi;
};

function CreateElementItem(e){
	var uDiv = document.createElement("div");
	uDiv.affixData = e;
	uDiv.className="project-content";
	var txt ='<div class="project-list">'+ e.index +'</div>\
		<div id="project-text">'+ e.content +'</div>';
	if(e.is_done == 0){
		txt += '<div class="project-bar">未完成</div>';
		uDiv.is_done = 1;
	}else{
		txt += '<div class="project-bar">'+ e.done_time.substr(0,10) +'已完成</div>';
		uDiv.is_done = 0;
	}
	uDiv.innerHTML = txt;
	uDiv.txt = e.content;
	uDiv.addEventListener('tap',function(){
		var self = this;
		if(self.disable){
			return;
		}
		self.style.backgroundColor = 'rgba(87, 206, 191, 0.17)';
		self.disable = true;
		setTimeout(function(){
			self.style.backgroundColor = 'rgba(87, 206, 191, 0.10)';
			self.disable = false;
			var d = self.affixData;
			mui.openWindow({
				url: "pfinish.html?homework_id="+d.homework_id + "&index="+d.index + "&item_id=" + d.item_id,
				id: "pfinish",
				extras: {
					item: self.affixData
				}
			});
		},350);
		uActiveBtn = self;
	});
	aBtns.push(uDiv);
	return uDiv;
};
