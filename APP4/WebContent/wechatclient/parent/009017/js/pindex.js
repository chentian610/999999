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
	BindEvent();
});

mui.ready(function(){
	InitPage();
});

function BindEvent(){
	uTap1.addEventListener('tap',function(){
		initPage();
	});
};

function InitPage(){
	cache.myajax("commentAction/getCommentList",
	{
		data:{
			user_type :"003015",
			class_id: cache.getChild().class_id,
			student_id: cache.getChild().student_id
		},
		success:function(data){
			var aRe = data.result.data;
			if (!cache.isArray(aRe)||aRe.length<=0) {
				cache.showDom(uTap2);
				cache.hideDom(uTap3);
				return;
			}
			for (var i = 0; i < aRe.length; i++) {
				uList.insertBefore(createEle(aRe[i]),uList.firstChild);
			}
			cache.hideDom(uTap3);
		},
		error:function(err){
			console.log(err);
			cache.showDom(uTap1);
			cache.hideDom(uTap3);
		}
	});
};

function createEle(e){
	var li = document.createElement("li");
	try{
		li.className = 'comment-row';
		li.innerHTML = '<div class="comment-con">'+value(e.comment)+'</div>\
		    			<div class="comment-bot">\
		    				<span>'+value(e.comment_date)+'</span>\
		    				<em>评价教师：'+value(e.teacher_name)+'</em>\
		    			</div>';
	}catch(e){
		console.log(e);
	}
	return li;
}

function value(e){
	if (cache.isNull(e)) {
		return '未知';
	}
	return e;
}
