var uContent = document.getElementById("Content"),
	uTap1 = document.getElementById("tap1"),
	uTap2 = document.getElementById("tap2"),
	uTap3 = document.getElementById("tap3");
var oPdata = {};
mui.ready(function(){
	initSchool();
	cache.showDom(uTap3);
	oPdata.code_name = getParamValue("code_name");
	oPdata.news_code = getParamValue("news_code");
	oPdata.news_group = getParamValue("news_group");
	oPdata.css_value = getParamValue("css_value");
	document.title = oPdata.code_name;
	InitPage();
	BindEvent();
});

//初始化学校信息
function initSchool(){
	var schoolId = getParamValue("schoolId");
	cache.myajax('schoolAction/getSchoolById',{
		data:{school_id: schoolId},
		async: false,
		success:function(data){
			var school = data.result.data;
			config.schoolid = school.school_id;
			config.school_name = school.school_name;
		}
	});
}

function BindEvent(){
	uTap1.addEventListener('tap',function(){
		InitPage();
	});
}

function InitPage(){
	cache.myajax('newsAction/getNewsListForWeb',{
		data:{
			user_type : "003005",
			news_code : oPdata.news_code,
			dict_group : oPdata.news_group,
			is_text : '1',
			is_resize : '0'
		},
		success:function(data){
			var r = data.result.data;
			console.log(JSON.stringify(r));
			for (var i=0;i<r.length;i++) {
				uContent.appendChild(CreateEle6(r[i]));
			}
			cache.hideDom(uTap3);
		},
	});
}

function CheckStyle(e){
	var obj = uContent;
	switch (oPdata.css_value){
		case 'join-us':
			obj.appendChild(CreateEle1(e));
			break;
		case 'headmaster-speak':
			obj.appendChild(CreateEle2(e));
			break;
		case 'sch-honer':
			obj.appendChild(CreateEle3(e));
			break;
		case 'excellent-tea':
			obj.appendChild(CreateEle4(e));
			break;
		case 'excellent-stu':
			obj.appendChild(CreateEle5(e));
			break;
		case 'sch-new':
			obj.appendChild(CreateEle6(e));
			break;
		case 'party-bulid':
			obj.appendChild(CreateEle7(e));
			break;
		case 'excellent-firend':
			obj.appendChild(CreateEle8(e));
			break;
		default:
			break;
	}
}

//学校简介 join-us
function CreateEle1(e){
	try{
		var div = document.createElement("div");
		div.className = "school-header";
		var div1 = document.createElement("div");
		div1.className = "school-box-name";
		div.appendChild(div1);
		var span = document.createElement("span");
		span.className = "school-title";
		span.innerText = e.code_name;
		div1.appendChild(span);
		var img = document.createElement("img");
		img.className = "image-id";
		img.src = "../images/school_intro.png";
		div.appendChild(img);
		div.dataAffix = e;
		div.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	}catch(e){
		console.log(e);
	}
	return div;
}

//校长致辞
function CreateEle2(e){
	try{
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell master-content-box';
		li.innerHTML = '<img class="img-school-master" src="'+cache.theValue(e.main_pic_url)+'" onloadsrc="">\
		    	            <div class="master-contet-text">'+cache.theValue(e.content_text)+'</div>';
		li.dataAffix = e;
		li.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	}catch(e){
		console.log(e);
	}
	return li;
}

////校史荣誉
function CreateEle3(e){
	try{
		var img = document.createElement("img");
		img.className = 'img-school-honor';
		img.src = cache.theValue(e.main_pic_url);
		img.dataAffix = e;
		img.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
		
	}catch(e){
		console.log(e);
	}
	return img;
}

//卓越教师
function CreateEle4(e){
	try{
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell teacher-content-box';
		li.innerHTML = '<img class="img-teacher-master" src="'+cache.theValue(e.main_pic_url)+'"  onloadsrc="">\
		    	            <div class="contet-text-teacher">'+cache.theValue(e.content_text)+'</div>';
		li.dataAffix = e;
		li.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	}catch(e){
		console.log(e);
	}
	return li;
}

//卓越学子
function CreateEle5(e){
	try{
		var img = document.createElement("img");
		img.className = 'img-student';
		img.src = cache.theValue(e.main_pic_url);
		img.dataAffix = e;
		img.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	}catch(e){
		console.log(e);
	}
	return img;
}


//默认样式 类似新闻
function CreateEle6(e){
	try {
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell new-content-box';
		li.innerHTML = '<img class="img-school-new" src="'+cache.theValue(e.main_pic_url)+'">\
	    	            <div class="new-title">'+cache.theValue(e.title)+'</div>\
	    	            <div class="new-contet-text">'+cache.theValue(e.content_text)+'</div>';
		li.dataAffix = e;
		li.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = '022005';
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	} catch(e) {
		console.log(e);
	}
	return li;
}
//党政工团
function CreateEle7(e){
	try{
		var div = document.createElement("div");
		div.className = 'image-Associations-title';
		div.innerHTML = '<span class="title-description">'+cache.theValue(e.title)+'</span>\
			    	    		 <span class="mask-box"></span>\
			    	    		 <img class="Associations-image" src="'+cache.theValue(e.main_pic_url)+'"/>';
		div.dataAffix = e;
		div.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	}catch(e){
		console.log(e);
	}
	return div;
}

//校友名录
function CreateEle8(e){
	try{
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell remover-padding';
		li.innerHTML = '<span class="window-line"></span>\
			    	    		<span class="window-student">研究生</span>\
			    	    		<span class="window-student-time">'+cache.theValue(e.deploy_date)+'</span>\
		    	            <img class="school-window-new" src="'+cache.theValue(e.main_pic_url)+'">';
		li.dataAffix = e;
		li.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var pItem = {}; 
			pItem.id = self.dataAffix.news_id;
			pItem.news_code = self.dataAffix.news_code;
			pItem.news_group = e.news_group;
			pItem.code_name = self.dataAffix.code_name;
			mui.openWindow({
				url: 'detail.html?schoolId=' + config.schoolid + '&id=' + pItem.id + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&code_name=" + pItem.code_name,
				id: 'detail',
				createNew: true,
				extras:{
			        item: pItem
			    },
				show: {
					aniShow: "pop-in",
					duration: 300
				},
				waiting: {
					autoShow: false
				}
			});
		});
	}catch(e){
		console.log(e);
	}
	return li;
}