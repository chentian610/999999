var	uContent = document.getElementById("Content"),
	uDetailTitle = document.getElementById("detailTitle"),
	uConTitle = document.getElementById("con-title"),
	uConContent = document.getElementById("con-content"),
	uConDate = document.getElementById("con-date"),
	uConWriter = document.getElementById("con-writer"),
	uNewsDetail = document.getElementById("NewsDetail"),
	uSchoolInfo = document.getElementById("SchoolInfo"),
	uHead = document.getElementById("head");
var oPitem = {};
	oPdata = {};

mui.ready(function(){
	initSchool();
	oPdata.id = getParamValue("id");
	oPdata.news_code = getParamValue("news_code");
	oPdata.news_group = getParamValue("news_group");
	oPdata.code_name = getParamValue("code_name");
	
	document.title = oPdata.code_name;
	
	if(oPdata.news_code == '025001'||oPdata.news_code == '025002'){
		InitPage2();
	}else{
		InitPage();
	}
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

function InitPage(){
	uHead.parentNode.removeChild(uHead);
	cache.hideDom(uSchoolInfo);
	cache.myajax("newsAction/getNews",{
		data:{
			news_id: oPdata.id
		},
		success:function(data){
			var aRe = data.result.data;
			uConTitle.innerHTML = aRe.title;
			uConDate.innerHTML = aRe.deploy_date;
			uConWriter.innerHTML = aRe.dept_name;
			uConContent.innerHTML = aRe.content;
			InitCss();
		},
		error:function(){
		}
	});
}

function InitCss(){
	var uSpan = uConContent.querySelectorAll('span');
	for (var i = 0; i < uSpan.length; i++) {
		uSpan[i].style.fontSize = '15px';
		uSpan[i].style.color = '#444';
		uSpan[i].style.lineHeight = '20px';
		uSpan[i].style.backgroundColor = '#fff';
	}
	var uFont = uConContent.querySelectorAll('font');
	for (var i = 0; i < uFont.length; i++) {
		uFont[i].style.fontSize = '15px';
		uFont[i].style.color = '#444';
		uFont[i].style.lineHeight = '20px';
		uFont[i].style.backgroundColor = '#fff';
		uFont[i].style.display = 'block';
	}
	var uP = uConContent.querySelectorAll('p');
	for (var i = 0; i < uP.length; i++) {
		uP[i].style.width = '100%';
		uP[i].style.fontSize = '15px';
		uP[i].style.color = '#000';
		uP[i].style.lineHeight = '20px';
		uP[i].style.padding = '0px';
		uP[i].style.margin = '5px 0';
		uP[i].style.textIndent = '0';
		uP[i].style.backgroundColor = '#fff';
	}
	var uDiv = uConContent.querySelectorAll('div');
	for (var i = 0; i < uDiv.length; i++) {
		uDiv[i].style.width = '100%';
		uDiv[i].style.fontSize = '15px';
		uDiv[i].style.color = '#000';
		uDiv[i].style.lineHeight = '20px';
		uDiv[i].style.padding = '0px';
		uDiv[i].style.margin = '0px';
		uDiv[i].style.backgroundColor = '#fff';
	}
	var uA = uConContent.querySelectorAll('a');
	for (var i = 0; i < uA.length; i++) {
		uA[i].setAttribute('href', '#');
		uA[i].style.fontSize = '15px';
		uA[i].style.color = '#0062CC';
		uA[i].style.lineHeight = '20px';
		uA[i].style.padding = '0px';
		uA[i].style.margin = '0px';
		uA[i].style.backgroundColor = '#fff';
	}
	var uImg = uConContent.querySelectorAll('img');
	for (var i = 0; i < uImg.length; i++) {
		uImg[i].width = '100%';
		uImg[i].height = '100%';
		uImg[i].style.padding = '0px';
		uImg[i].style.margin = '0px';
		uImg[i].style.width = '100%';
		uImg[i].style.height = '100%';
		uImg[i].onerror = function(){
			this.parentNode.remove(this);
		};
	}
};

function InitPage2(){
	cache.hideDom(uNewsDetail);
	cache.myajax('newsAction/getNewsListForWeb',{
		data:{
			user_type : "003005",
			news_code : oPdata.news_code,
			dict_group : oPdata.news_group,
			is_text : '1',
			is_resize : '0'
		},
		success:function(data){
			var aRe = data.result.data;
			if(oPdata.news_code == '025001'){
				uSchoolInfo.appendChild(CreateEle1(aRe[0]));
				uHead.parentNode.removeChild(uHead);
			}else{
				uContent.style.backgroundColor = '#eee';
				uSchoolInfo.appendChild(CreateEle2(aRe[0]));
			}
		},
		error:function(){
		}
	});
}

function CreateEle1(v){
    var div = document.createElement('div');
    try{
		div.innerHTML = '<img class="hxx-banner" src="'+ cache.theValue(v.main_pic_url) +'" onerror = "imgLoadError()"/>\
		<div class="hxx-btns-blue"></div>\
		<div class="hxx-school-content">	'+ cache.theValue(v.content_text) +'</div>';
    }catch(e){
    		console.log(e);
    }
	return div;
}

//headmaster-speak
function CreateEle2(v){
	var div = document.createElement('div');
	try{
		div.className = 'hxx-headmaster-page';
		div.innerHTML = '<div class="hxx-headmaster-head"><img src="'+ cache.theValue(v.main_pic_url) +'" onerror = "imgLoadError2()"/></div>\
						<h3 class="hxx-headmaster-title">校长致辞</h3>\
						<p class="hxx-headmaster-sender">亲爱的同学们：</p>\
						<p class="hxx-headmaster-content">'+ cache.theValue(v.content_text) +'</p>';
	}catch(e){
		console.log(e);
	}
	return div;
}