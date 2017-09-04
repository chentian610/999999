var uTap1 = document.getElementById('tap_center1'),
	uTap2 = document.getElementById('tap_center2'),
	uTap3 = document.getElementById('tap_center3'),
	uTitle = document.getElementById("title"),
	uList = document.getElementById("list");

var wvSelf = null;
var child = {};
var oPdata = {},
	oPitem = {};

mui.ready(function(){
	cache.showDom(uTap3);
	oPdata.id = getParamValue("id");
	BindEvent();
	InitPage();
});

function BindEvent(){
	uTap1.addEventListener('tap',function(){
		initPage();
	});
};

function InitPage(){
	cache.myajax("commentAction/getCommentByID",
	{
		data:{
			user_type :"003015",
			comment_id: oPdata.id
		},
		success:function(data){
			var oRe = data.result.data;
			if (cache.isNull(oRe)) {
				cache.showDom(uTap2);
				cache.hideDom(uTap3);
				return;
			}
			uList.insertBefore(createEle(oRe),uList.firstChild);
			cache.hideDom(uTap3);
		},
		error:function(xhr,type,errorThrown){
			cache.showDom(uTap1);
			cache.hideDom(uTap3);
		}
	});
};

function createEle(e){
	var li = document.createElement("li");
	li.className = 'comment-row';
	li.innerHTML = '<div class="comment-con">'+value(e.comment)+'</div>\
	    			<div class="comment-bot">\
	    				<span>'+value(e.comment_date)+'</span>\
	    				<em>评价教师：'+value(e.teacher_name)+'</em>\
	    			</div>';
	li.addEventListener('tap',function(){
//		u_comment_cotent.value = this.innerText;
	});
	return li;
}

function value(e){
	if (cache.isNull(e)) {
		return '空';
	}
	return e;
}
