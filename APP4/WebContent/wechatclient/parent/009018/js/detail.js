var	uDetailTitle = document.getElementById("detailTitle"),
	uConTitle = document.getElementById("con-title"),
	uConContent = document.getElementById("con-content"),
	uConDate = document.getElementById("con-date"),
	uConWriter = document.getElementById("con-writer");


mui.ready(function(){
	oPdata.id = getParamValue("id");
	oPdata.news_code = getParamValue("news_code");
	oPdata.dict_group = getParamValue("dict_group");
	InitTitle();
	InitPage();
});

function InitTitle(){
	cache.ajax('dictAction/getNewsDictionary',{
		data:{
			news_group:oPdata.dict_group
		},
		success:function(aRe){
			for (var i=0;i<aRe.length;i++) {
				if(oPdata.news_code == aRe[i].news_code){
					document.title = aRe[i].code_name;
				}
			}
		}
	});
}

function InitPage(){
	cache.ajax("newsAction/getNews",{
		data:{
			news_id: oPdata.id
		},
		success:function(aRe){
			uConDate.innerHTML = aRe.deploy_date;
			uConWriter.innerHTML = aRe.dept_name;
			uConTitle.innerHTML = aRe.title;
			uConContent.innerHTML = aRe.content;
			InitCss();
			//plus.nativeUI.closeWaiting();
		},
		error:function(){
			//plus.nativeUI.closeWaiting();
		}
	});
};

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