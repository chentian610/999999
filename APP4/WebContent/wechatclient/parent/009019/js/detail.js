var uTap1 = document.getElementById("tap1");
	uTap2 = document.getElementById("tap2");
	uTap3 = document.getElementById("tap3");
	uDetailTitle = document.getElementById("detailTitle");
	uConTitle = document.getElementById("con-title");
	uConContent = document.getElementById("con-content");
	uConDate = document.getElementById("con-date");
	uConWriter = document.getElementById("con-writer");
var oPitem = {};
	oPdata = {};

mui.ready(function(){
	cache.showDom(uTap3);
	oPdata.id = getParamValue("id");
	oPdata.news_code = getParamValue("news_code");
	oPdata.dict_group = getParamValue("dict_group");
	InitTitle();
	InitPage();
});

function InitTitle(){
	cache.myajax('dictAction/getNewsDictionary',{
		data:{
			news_group:oPdata.dict_group
		},
		success:function(data){
			var result = data.result.data
			for (var i=0;i<result.length;i++) {
				if(oPdata.news_code == result[i].news_code){
					document.title = result[i].code_name;
				}
			}
		}
	});
}

function InitPage(){
	cache.myajax("newsAction/getNews",{
		data:{
			news_id: oPdata.id
		},
		success:function(data){
			var aRe = data.result.data;
			uConDate.innerHTML = aRe.deploy_date;
			uConWriter.innerHTML = aRe.dept_name;
			uConTitle.innerHTML = aRe.title;
			uConContent.innerHTML = aRe.content;
			cache.hideDom(uTap3);
		},
		error:function(){
			cache.hideDom(uTap3);
		}
	});
}