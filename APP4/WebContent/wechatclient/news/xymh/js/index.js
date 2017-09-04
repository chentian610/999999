var uSchoolHome = document.getElementById("SchoolHome"),
	uSchoolNew = document.getElementById("SchoolNew"),
	uAlumniWindow = document.getElementById("AlumniWindow"),
	uSchoolAssociations = document.getElementById("SchoolAssociations"),
	uSchoolService = document.getElementById("SchoolService"),
	uSchoolInfo = document.getElementById("SchoolInfo"),
	uEnrollStudent = document.getElementById("EnrollStudent"),
	uJoinUs = document.getElementById("JoinUs"),
	uNavTab = document.getElementById("NavTab");

mui.ready(function() {
	initSchool();
	oPdata = {};
	oPdata.dict_group = getParamValue("dictId");
	oPdata.dict_name = config.school_name;
	var uTabItemList = mui('.mui-tab-item');
	if(oPdata){
		document.title = oPdata.dict_name;
		uTabItemList[0].classList.remove('mui-active');
		if(oPdata.dict_group == '022005'){
			uTabItemList[0].classList.add('mui-active');
			uTabItemList[0].isFirstTap = true;
			uSchoolHome.classList.add('mui-active');
			InitPage('022005');
		}else if(oPdata.dict_group == '022010'){
			uTabItemList[2].classList.add('mui-active');
			uTabItemList[2].isFirstTap = true;
			uSchoolAssociations.classList.add('mui-active');
			InitPage('022010');
		}else if(oPdata.dict_group == '022015'){
			uTabItemList[1].classList.add('mui-active');
			uTabItemList[1].isFirstTap = true;
			uSchoolNew.classList.add('mui-active');
			InitPage('022015');
		}else if(oPdata.dict_group == '022020'){
			uTabItemList[3].classList.add('mui-active');
			uTabItemList[3].isFirstTap = true;
			uAlumniWindow.classList.add('mui-active');
			InitPage('022020');
		}else if(oPdata.dict_group == '022025'){
			uTabItemList[4].classList.add('mui-active');
			uTabItemList[4].isFirstTap = true;
			uSchoolService.classList.add('mui-active');
			InitPage('022025');
		}
	}else {
		InitPage('022005');
		uTabItemList[0].isFirstTap = true;
		uTitle.innerText = config.school_name;
	}
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
	mui('.mui-bar-tab').on('tap','a',function(){
		var self = this;
		var attr = self.getAttribute('dataTab');
		if(attr == "d1"){
			mui('span')[0].className = "mui-icon hxx-icon icon-xiaoyuanfengcaishixin";
			mui('span')[2].className = "mui-icon hxx-icon icon-xiaoyuanxinwen";
			mui('span')[4].className = "mui-icon hxx-icon icon-dangzhenggongtuan";
			mui('span')[6].className = "mui-icon hxx-icon icon-xiaoyouzhichuang";
			mui('span')[8].className = "mui-icon hxx-icon icon-zhaoshengzhaopin";
		}else if(attr == "d2"){
			mui('span')[0].className = "mui-icon hxx-icon icon-xuexiaofengcai";
			mui('span')[2].className = "mui-icon hxx-icon icon-xiaoyuanxinwenshixin";
			mui('span')[4].className = "mui-icon hxx-icon icon-dangzhenggongtuan";
			mui('span')[6].className = "mui-icon hxx-icon icon-xiaoyouzhichuang";
			mui('span')[8].className = "mui-icon hxx-icon icon-zhaoshengzhaopin";
		}else if(attr == "d3"){
			mui('span')[0].className = "mui-icon hxx-icon icon-xuexiaofengcai";
			mui('span')[2].className = "mui-icon hxx-icon icon-xiaoyuanxinwen";
			mui('span')[4].className = "mui-icon hxx-icon icon-dangzhenggongtuanshixin";
			mui('span')[6].className = "mui-icon hxx-icon icon-xiaoyouzhichuang";
			mui('span')[8].className = "mui-icon hxx-icon icon-zhaoshengzhaopin";
		}else if(attr == "d4"){
			mui('span')[0].className = "mui-icon hxx-icon icon-xuexiaofengcai";
			mui('span')[2].className = "mui-icon hxx-icon icon-xiaoyuanxinwen";
			mui('span')[4].className = "mui-icon hxx-icon icon-dangzhenggongtuan";
			mui('span')[6].className = "mui-icon hxx-icon icon-xiaoyouzhichuangshixin";
			mui('span')[8].className = "mui-icon hxx-icon icon-zhaoshengzhaopin";
		}else if(attr == "d5"){
			mui('span')[0].className = "mui-icon hxx-icon icon-xuexiaofengcai";
			mui('span')[2].className = "mui-icon hxx-icon icon-xiaoyuanxinwen";
			mui('span')[4].className = "mui-icon hxx-icon icon-dangzhenggongtuan";
			mui('span')[6].className = "mui-icon hxx-icon icon-xiaoyouzhichuang";
			mui('span')[8].className = "mui-icon hxx-icon icon-zhaoshengzhaopinshixin";
		}
	});
	
	mui('.mui-bar-tab').on('tap','.mui-tab-item',function(e){
		var self = this;
		var newsGroup = self.getAttribute('news-group');
		if (!self.isFirstTap) {
			InitPage(newsGroup);
		}
		self.isFirstTap = true;
	});
}

function InitPage(news_group){
	var url = "newsAction/getNewsListOfLogin";
	if(oPdata){
		url = "newsAction/getNewsListForAPP";
	}
	cache.myajax(url,{
		data:{
			news_group: news_group
		},
		success:function(data){
			var aRe = data.result.data;
			for (var i=0; i<aRe.length; i++) {
				CheckStyle(aRe[i]);
			}
		}
	});
}

function CheckStyle(e){
	var obj = GetNewsName(e);
	switch (e.css_value){
		case 'join-us':
			obj.appendChild(CreateEle1(e));
			break;
		case 'headmaster-speak':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle2(e));
			break;
		case 'sch-honer':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle3(e));
			break;
		case 'excellent-tea':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle4(e));
			break;
		case 'excellent-stu':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle5(e));
			break;
		case 'sch-new':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle6(e));
			break;
		case 'party-bulid':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle7(e));
			break;
		case 'excellent-firend':
			obj.appendChild(CreateLine());
			obj.appendChild(CreateEle8(e));
			break;
		default:
			break;
	}
}

function GetNewsName(e){
	if(e.news_group == '022005'){
		return uSchoolHome;
	}else if(e.news_group == '022010'){
		return uSchoolAssociations;
	}else if(e.news_group == '022015'){
		return uSchoolNew;
	}else if(e.news_group == '022020'){
		return uAlumniWindow;
	}else if(e.news_group == '022025'){
		return uSchoolService;
	}
}

//10个像素的灰色条
function CreateLine(){
	var line = document.createElement("div");
	line.className = 'line-box';
	return line;
}

//标题
function CreateEleTitle(e){
	var li = document.createElement("li");
	li.className = 'mui-table-view-cell';
	li.innerHTML = '<span class="mui-navigate-right schoolmaster-speak">'+cache.theValue(e.code_name)+'</span>';
	li.addEventListener('tap',function(){
		var self = this;
		if (self.disable) {
			return;
		}
		self.disable = true;
		setTimeout(function(){
			self.disable = false;
		},1000);
		var p = e.news_group;
		var pItem = {};
		pItem = e;
		mui.openWindow({
			url: 'list.html?schoolId=' + config.schoolid + '&code_name=' + pItem.code_name + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&css_value=" + pItem.css_value,
			id: 'list',
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
	return li;
}

//学校简介 join-us
function CreateEle1(e){
	try{
		var list = JSON.parse(e.news_list);
		var div = document.createElement("div");
		div.className = "school-header";
		var div1 = document.createElement("div");
		div1.className = "school-box-name";
		div.appendChild(div1);
		var span = document.createElement("span");
		span.className = "school-title";
		if(e.news_code == "025001"){
			span.innerText = cache.theValue(e.code_name);
		}else{
			span.innerText = cache.theValue(list[0].title);
		}
		div1.appendChild(span);
		var maskDiv=document.createElement("div");
		maskDiv.className="mark-box";
		div.appendChild(maskDiv);
		var img = document.createElement("img");
		img.className = "image-id";
		img.src = cache.theValue(list[0].main_pic_url);
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
			if (e.news_code == '025001'){
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
			}else{
				var p = e.news_group;
				var pItem = {};
				pItem = e;
				mui.openWindow({
					url: 'list.html?schoolId=' + config.schoolid + '&code_name=' + pItem.code_name + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&css_value=" + pItem.css_value,
					id: 'list',
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
			}
		});
	}catch(e){
		console.log(e);
	}
	return div;
}

//校长致辞 headmaster-speak
function CreateEle2(e){
	try{
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell master-content-box';
		ul.appendChild(li);
		li.innerHTML = '<img class="img-school-master" src="'+cache.theValue(list[0].main_pic_url)+'" onloadsrc="">\
		    	            <div class="master-contet-text">'+cache.theValue(list[0].content_text)+'</div>';
		li.dataAffix = list[0];
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
	return ul;
}

//校史荣誉 sch-honer
function CreateEle3(e){
	try{
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell master-content-box remover-padding mui-disabled';
		ul.appendChild(li);
		for (var i=0;i<3;i++) {
			var img = document.createElement("img");
			img.className = 'img-school-honor';
			img.src = cache.theValue(list[i].main_pic_url);
			img.dataAffix = list[i];
			li.appendChild(img);
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
		}
		var moreDiv = document.createElement("div");
		moreDiv.className = 'img-school-honor';
		moreDiv.innerHTML = '<img class="hxx-icon icon-position" src="../images/more-right.png">\
		    	      		    <span class="text-position">更多'+cache.theValue(e.code_name)+'</span>\
		    	      		    <img class="image-school-back" src="../images/school_history.png" onloadsrc="" alt="">';
		li.appendChild(moreDiv);
		moreDiv.addEventListener('tap',function(){
			var self = this;
			if (self.disable) {
				return;
			}
			self.disable = true;
			setTimeout(function(){
				self.disable = false;
			},1000);
			var p = e.news_group;
			var pItem = {};
			pItem = e;
			mui.openWindow({
				url: 'list.html?schoolId=' + config.schoolid + '&code_name=' + pItem.code_name + "&news_code=" + pItem.news_code + "&news_group=" + pItem.news_group + "&css_value=" + pItem.css_value,
				id: 'list',
				extras:{
			        item: pItem
			   }
			});
		});
	}catch(e){
		console.log(e);
	}
	return ul;
}

//卓越教师 excellent-tea
function CreateEle4(e){
	try{
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after teacher-box';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		for (var i=0;i<2;i++) {
			var li = document.createElement("li");
			li.className = 'mui-table-view-cell teacher-content-box';
			ul.appendChild(li);
			li.innerHTML = '<img class="img-teacher-master" src="'+cache.theValue(list[i].main_pic_url)+'"  onloadsrc="">\
			    	            <div class="contet-text-teacher">'+cache.theValue(list[i].content_text)+'</div>';
			li.dataAffix = list[i];
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
					extras:{
				        item: pItem
				   }
				});
			});
		}
	}catch(e){
		console.log(e);
	}
	return ul;
}

//卓越学子 excellent-stu
function CreateEle5(e){
	try{
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		var li2 = document.createElement("li");
		li2.className = 'mui-table-view-cell master-content-box remover-padding mui-disabled';
		ul.appendChild(li2);
		for (var i=0;i < 3;i++) {
			var img = document.createElement("img");
			img.className = 'img-student';
			img.src = cache.theValue(list[i].main_pic_url);
			img.dataAffix = list[i];
			li2.appendChild(img);
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
					extras:{
				        item: pItem
				   }
				});
			});
		}
	}catch(e){
		console.log(e);
	}
	return ul;
}

//类似新闻 sch-new
function CreateEle6(e){
	try {
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		for (var i=0;i<2;i++) {
			var li = document.createElement("li");
			li.className = 'mui-table-view-cell new-content-box';
			ul.appendChild(li);
			li.innerHTML = '<img class="img-school-new" src="'+cache.theValue(list[i].main_pic_url)+'">\
		    	            <div class="new-title">'+cache.theValue(list[i].title)+'</div>\
		    	            <div class="new-contet-text">'+cache.theValue(list[i].content_text)+'</div>';
			li.dataAffix = list[i];
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
		}
	} catch(e) {
		console.log(e);
	}
	return ul;
}

//党政工团 party-bulid
function CreateEle7(e){
	try{
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		var li2 = document.createElement("li");
		li2.className = 'mui-table-view-cell remover-padding mui-disabled';
		ul.appendChild(li2);
		for (var i=0;i < 2;i++) {
			var div = document.createElement("div");
			div.className = 'image-Associations-title';
			div.innerHTML = '<span class="title-description">'+cache.theValue(list[i].title)+'</span>\
				    	    		 <span class="mask-box"></span>\
				    	    		 <img class="Associations-image" src="'+cache.theValue(list[i].main_pic_url)+'"/>';
			li2.appendChild(div);
			div.dataAffix = list[i];
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
		}
	}catch(e){
		console.log(e);
	}
	return ul;
}

//校友名录 excellent-firend
function CreateEle8(e){
	try{
		var list = JSON.parse(e.news_list);
		var ul = document.createElement("ul");
		ul.className = 'mui-table-view line-after';
		if(cache.isArray(list)&&list.length>0){
			ul.appendChild(CreateEleTitle(e));
		}
		var li = document.createElement("li");
		li.className = 'mui-table-view-cell remover-padding';
		ul.appendChild(li);
		li.innerHTML = '<span class="window-line"></span>\
				    		<span class="window-student">'+cache.theValue(list[0].title)+'</span>\
				    		<span class="window-student-time">'+cache.theValue(list[0].deploy_date)+'</span>\
				    		<div class="window-student-mask"></div>\
				    	<img class="school-window-new" src="'+cache.theValue(list[0].main_pic_url)+'">';
		li.dataAffix = list[0];
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
	return ul;
}
