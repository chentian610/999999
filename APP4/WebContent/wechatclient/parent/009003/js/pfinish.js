var uUploadBox = document.getElementById("UploadBox");
var uHint1 = document.getElementById("Hint1");
var uHint2 = document.getElementById("Hint2");
var uBoxAudioMask = document.getElementById("BoxAudioMask");
var uBoxAudio = document.getElementById("BoxAudio");
var uBtnAutio = document.getElementById("BtnAutio");
var uTxtTitle = document.getElementById("TxtTitle");
var uTxtCustom1 = document.getElementById("TxtCustom1");
var uTxtHomeworkContent = document.getElementById("TxtHomeworkContent");
var uTxtCustom2 = document.getElementById("TxtCustom2");
var uTxtCustom2Content = document.getElementById("TxtCustom2Content");
var uBoxSubmitAgin = document.getElementById("BoxSubmitAgin");
var uTxtSubmitTime = document.getElementById("TxtSubmitTime");
var uBtnSubmit = document.getElementById("BtnSubmit");
var uCustom1ListAffix = document.getElementById("Custom1ListAffix");
var uCustom2ListAffix = document.getElementById("Custom2ListAffix");
var uAudio = document.getElementById("audio");

mui.init();
mui.ready(function(){
	wxUtil.init(cache.getSchoolId(), window.location.href);
	oPdata.homework_id = getParamValue("homework_id");
	oPdata.index = getParamValue("index");
	oPdata.item_id = getParamValue("item_id");
	oPitem.child = cache.getChild();
	downloadHomeWorkItem(oPdata.homework_id, oPdata.index - 1);
	cache.hideDom(uUploadBox);
	cache.hideDom(uBoxAudioMask);
	InitPicker();
	BindEvent();
	mui.previewImage();
});

function downloadHomeWorkItem(homework_id, index){
	cache.ajax("homeworkAction/getHomeworkList", {
		data: {
			homework_id: homework_id,
			student_id : cache.getChild().student_id,
			user_type: "003015",
		},
		async:false,
		success: function(aRe) {
			var oItemList = JSON.parse(aRe[0].item_list);
			oPdata.is_done = oItemList[index].is_done;
			oPdata.file_list = oItemList[index].file_list;
			oPdata.student_id = oItemList[index].student_id;
			oPdata.content = aRe[0].content;
			oPdata.end_date = aRe[0].end_date;
			oPdata.create_date = aRe[0].create_date;
			//判断用户是否已提交过作业，1为已提交，2为未提交
			if(oPdata.is_done == 1){
				cache.showDom(uBoxSubmitAgin);
				cache.showDom(uTxtCustom2);
				InitPageRevise();
			}else{
				cache.showDom(uBtnSubmit);
				cache.showDom(uTxtCustom1);
				InitPage();
			}
		}
	});
}


function InitPage() {
	InitHomeworkTitle();
};

function InitPicker() {
	//创建遮罩
	oMask = mui.createMask(function() {
		document.getElementById('abox').style.display = "none";
	});
	oPicker1 = new mui.PopPicker({
		layer: 1
	});
	oPicker1.setData([{value:'picture',text:'照片'},{value:'video',text:'语音'}]);
};

function InitPageRevise(){
	InitHomeworkTitle();
	GetDoneList();
}

function GetDoneList(){
	cache.ajax("homeworkAction/getDoneList", {
		data: {
			user_type: "003005",
			homework_id: oPdata.homework_id,
			student_id: cache.getChild().student_id,
			item_id: oPdata.item_id
		},
		success: function(aRe) {
			var oRe = aRe[0];
			try{
				aRe = JSON.parse(oRe.item_list);
				oRe = aRe[0];
				oPitem.content = oRe.content;
				oPitem.done_date = cache.timeStamp2str(oRe.done_date.time);
				oPitem.file_list = JSON.parse(oRe.file_list);
			}catch(e){
				mui.toast("数据格式错误,请联系管理员");
				return;
			}
			uTxtHomeworkContent.value = oPitem.content;
			uTxtCustom2Content.innerText = oPitem.content;
			uTxtSubmitTime.innerText = oPitem.done_date+'已完成';
			for (var i = 0; i < oPitem.file_list.length; i++) {
				if(oPitem.file_list[i].file_type == "020005"){
					uCustom2ListAffix.appendChild(CreateElementAffixImg(oPitem.file_list[i]));
					uCustom1ListAffix.appendChild(CreateElementAffixImgLocad(oPitem.file_list[i]));
				}else{
					uCustom2ListAffix.appendChild(CreateElementAffixAudio(oPitem.file_list[i]));
					uCustom1ListAffix.appendChild(CreateElementAffixAudioLocal2(oPitem.file_list[i]));
				}
			}
		},
		error: function(e) {
		}
	});
};

function BindEvent(){
	//再次编辑
	document.getElementById("BtnSubmitAgin").addEventListener('tap',function(){
		cache.hideDom(uBoxSubmitAgin);
		cache.hideDom(uTxtCustom2);
		cache.showDom(uBtnSubmit);
		cache.showDom(uTxtCustom1);
	});
	//添加附件
	document.getElementById("BtnAddAffix").addEventListener('tap', function() {
		oPicker1.show(function(item) {
			if(item[0].value == 'picture'){
				wxUtil.chooseImage({
					success:function(imgs){
						AppendDocumentFragment(imgs);
					}
				});
			}else{
				cache.showDom(uBoxAudioMask);
			}
		});
	});
	
	//录音相关
	uBoxAudioMask.addEventListener('tap', function(e) {
		e.preventDefault();
		e.stopPropagation();
		mui.later(function() {
			cache.hideDom(uBoxAudioMask);
		}, 350);
	});
	//防止点击穿透
	uBoxAudio.addEventListener('tap', function(e) {
		e.preventDefault();
		e.stopPropagation();
	});
	//开始录音
	uBtnAutio.addEventListener('touchstart', function(e) {
		var self = this;
		e.preventDefault();
		e.stopPropagation();
		self.classList.add('active');
		wxUtil.startRecord();
	});
	//结束录音
	uBtnAutio.addEventListener('touchend', function(e) {
		var self = this;
		e.preventDefault();
		e.stopPropagation();
		self.classList.remove('active');
		cache.hideDom(uBoxAudioMask);
		mui.later(function() {
			wxUtil.stopRecord({
				success:function(videoId, time){
					uCustom1ListAffix.appendChild(CreateElementAffixAudioLocal(videoId, time));
				}
			});
		}, 100);
	});

	
	//点击提交作业按钮
	document.getElementById("BtnSubmit").addEventListener('tap',function(){
		if (cache.isNull(uTxtHomeworkContent.value)) {
			mui.toast('留言不可为空');
			return;
		}else{
			oPdata.content = uTxtHomeworkContent.value;
		}
		UploadAffixItem(function(e){
			EditHomework({
				item_id: oPdata.item_id,
				homework_id: oPdata.homework_id,
				student_id: oPdata.student_id,
				content: oPdata.content,
				file_list: JSON.stringify(e)
			});
		},function(e){
			mui.toast(e);
		});
	});

};

//上传作业附件
function UploadAffixItem(success,error){
	cache.showDom(uUploadBox);
	mui("#demo1").progressbar().setProgress(0);
	var uAffixItemList = uCustom1ListAffix.querySelectorAll('.hxx-affix-item');
	if (uAffixItemList.length<=1) {
		uHint1.innerText = '没有附件';
		success([]);
		return;
	}

	//所有附件
	var uAffixList = [];
	//需要上传的附件
	var affixFileItemList = [];
	for (var i = 1; i < uAffixItemList.length; i++) {
		var oAffixItem = uAffixItemList[i].affixData;
		if (oAffixItem.file_path.indexOf('http') == 0) {//已经上传
			uAffixList.push(oAffixItem);
			oAffixItem.index = -1;
		}else{
			oAffixItem.index = affixFileItemList.length;
			affixFileItemList.push(oAffixItem);
			uAffixList.push(oAffixItem);
		}
	}
	if (affixFileItemList.length<=0) {
		if (success) {
			success(uAffixList);
		}
		return;
	}
	
	//上传附件
	wxUtil.uploadImageOrRecord("fileAction/uploadWechatFile", {
		data: {
			items:affixFileItemList,
			module_code: '009003'
		},
		success: function(aRe){
			for (var i = 0; i < uAffixList.length; i++) {
				for (var j = 0; j < aRe.length; j++) {
					if (uAffixList[i].index == aRe[j].file_index) {
						uAffixList[i].file_url = aRe[j].file_url;
						uAffixList[i].file_resize_url = aRe[j].file_resize_url;
					}
				}
			}
			if (success) {
				success(uAffixList);
			}
			mui.later(function(){
				uHint1.innerText = '文件全部上传成功。';
			},50);
		},
		upload: function(d) {
			if(d == 0) {
				mui("#demo1").progressbar().setProgress(0);
				uHint1.innerText = '已上传：'+00+'%';
			} else {
				mui("#demo1").progressbar().setProgress(d);
				uHint1.innerText = '已上传：'+d+'%';
			}
		},
		error: function(err){
			if(!err) err = '上传失败!';
			uHint1.innerText = err;
			mui.later(function(){
				cache.hideDom(uUploadBox);
			},2000);
			self.disable = false;
		}
	}, false);
	
};

//修改作业内容
function EditHomework(e){
	uHint2.innerText ='同步数据到服务器。';
	cache.myajax("homeworkAction/updateItemDone", {
		data: {
			item_id: e.item_id,
			homework_id: e.homework_id,
			is_done: 1,
			user_type :"003015",
			student_id: e.student_id,
			content: e.content,
			file_list: e.file_list
		},
		success: function(data) {
			uHint2.innerText ='作业提交成功';
			if (cache.getDateToEnd(oPdata.end_date) == "已到期") {
				cache.hideDom(uUploadBox);
				mui.alert('作业成功提交,该作业截止日期已过,下次请及时完成作业','作业完成', function() {
					window.history.back(-1); 
				});
			}else{
				window.history.back(-1); 
			}
			
		},
		error: function(){
			uHint2.innerText ='同步失败';
			mui.later(function(){
				cache.hideDom(uUploadBox);
			},1000);
		}
	});
};

//初始化作业题目内容
function InitHomeworkTitle(){
	uTxtTitle.innerHTML = '<div class="hxx-table-content">\
								<span class="hxx-title-firstname">'+ oPitem.child.student_name[0] +'</span>\
								<span class="hxx-title-name">'+ oPitem.child.student_name +'</span>\
							</div>\
							<div class="hxx-table-content">\
								<span class="hxx-title-number">第'+ oPdata.index +'段作业：</span>\
								<span class="hxx-title-create-time">'+ cache.date2str(cache.date(oPdata.create_date)) +'</span>\
							</div>\
							<div class="hxx-table-content">\
								<span class="hxx-title-content">'+ oPdata.content +'</span>\
							</div>\
							<div class="hxx-table-content">\
								<div id="ListAffix" class="mui-row">\
								</div>\
							</div>';
							
							
	var uListAffix = uTxtTitle.querySelector('#ListAffix');
	try{
		var uFile = JSON.parse(oPdata.file_list);
		for (var i = 0; i < uFile.length; i++) {
			if(uFile[i].file_type == "020005"){
				uListAffix.appendChild(CreateElementAffixImg(uFile[i]));
			}else{
				uListAffix.appendChild(CreateElementAffixAudio(uFile[i]));
			}
		}
	}catch(e){
		console.log(e);
	}
};

//生成附件元素
//<div class="mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnImg">
//	<img src="../resource/bk_home_footer.jpg" data-preview-src="../resource/bk_home_footer.jpg" data-preview-group="1"/>
//</div>
function CreateElementAffixImg(e) {
	var uAffixItem = document.createElement("div");
	var uImg = document.createElement("img");
	uAffixItem.className = 'mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnImg';
	uImg.src = e.file_resize_url;
	uImg.setAttribute('data-preview-src',e.file_url);
	uImg.setAttribute('data-preview-group','1');
	uAffixItem.appendChild(uImg);
	return uAffixItem;
};
//<div class="mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnImg">
//	<img src="../resource/bk_home_footer.jpg"/>
//	<span>&#xe673;</span>
//</div>
function CreateElementAffixImgLocad(e) {
	var uAffixItem = document.createElement("div");
	uAffixItem.className = 'mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnImg';
	var uImg = document.createElement("img");
	var uSpan = document.createElement("span");
	if (typeof(e) == "object") {
		var arr = e.file_url.split('/');
		e.file_name = arr[arr.length-1];
		e.play_time = '';
		e.file_path = e.file_url;
		uAffixItem.affixData = e;
		uImg.src = e.file_resize_url;
		uImg.setAttribute('data-preview-src',e.file_url);
		uImg.setAttribute('data-preview-group','1');
		uSpan.innerHTML = '&#xe673;';
		uSpan.addEventListener('tap', function() {
			this.parentNode.parentNode.removeChild(this.parentNode);
		});
		uSpan.innerHTML = '&#xe673;';
		uAffixItem.appendChild(uImg);
		uAffixItem.appendChild(uSpan);
	}else{
		uAffixItem.affixData = {
			file_type: '020005',
			file_name: '',
			file_url: e,
			file_resize_url: '',
			play_time: '',
			file_path: e
		};
		uImg.src = '../images/default-img.png';
		uImg.setAttribute('data-preview-src',e);
		uImg.setAttribute('data-preview-group','1');
		
		uImg.src = e;
		uAffixItem.affixData.file_url = e;
		uImg.setAttribute('data-preview-src',e);
		
		uSpan.innerHTML = '&#xe673;';
		uSpan.addEventListener('tap', function() {
			this.parentNode.parentNode.removeChild(this.parentNode);
		});
		uSpan.innerHTML = '&#xe673;';
		uAffixItem.appendChild(uImg);
		uAffixItem.appendChild(uSpan);
	}
	return uAffixItem;
};
//<div class="mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnAudioPaly">
//	<h1 class="hxx-affix-downlad"></h1>
//	<h2>&#xe663;</h2>
//	<h4>40“</h4>
//</div>
function CreateElementAffixAudio(e) {
	var uAffixItem = document.createElement("div");
	var uH1 = document.createElement("h1");
	var uH2 = document.createElement("h2");
	var uH4 = document.createElement("h4");
	uAffixItem.className = 'mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnAudioPaly';
	uAffixItem.affixData = e;
	uH1.className = 'hxx-affix-downlad';
	uH1.innerText = '下载音频';
	uH2.innerHTML = '&#xe663;';
	
	wxUtil.downloadAudioFile("fileAction/amrToMp3",{
		file_path: uAffixItem.affixData.file_url, 
		success: function(e) {
			uAffixItem.affixData.file_path = e;
			cache.hideDom(uH1);
		},
		download: function(d) {
			if(d == 0) {
				uH1.innerText = '00%';
			} else {
				uH1.innerText = d + '%';
			}
		},
		error: function(e) {
			uH1.innerText = '下载失败';
		}
	});
	uH2.addEventListener('tap',function(){
		var self = this;
		$(".BtnAudioPaly").find('h2').removeClass('active');
		if (self.disable) {
			self.disable = false;
			stopPlayH5Audio();
			return;
		}
		self.disable = true;
		self.classList.add('active');
		startPlayH5Audio(self.parentNode.affixData.file_path, function(){
			self.disable = false;
			self.classList.remove('active');
		});
	});
	uH4.innerText = e.play_time + '“';
	uAffixItem.appendChild(uH1);
	uAffixItem.appendChild(uH2);
	uAffixItem.appendChild(uH4);
	return uAffixItem;
};
//<div class="mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnAudioPaly">
//	<h2>&#xe663;</h2>
//	<h4>40“</h4>
//	<span>&#xe673;</span>
//</div>
function CreateElementAffixAudioLocal(localId, timeLength) {
	var uAffixItem = document.createElement("div");
	var uH2 = document.createElement("h2");
	var uH4 = document.createElement("h4");
	var uSpan = document.createElement("span");
	uAffixItem.className = 'mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnAudioPaly';
	uH2.innerHTML = '&#xe663;';
	uH4.innerText = timeLength + '“';
	uSpan.innerHTML = '&#xe673;';
	
	uAffixItem.affixData = {
		file_type: '020010',
		file_path: localId,
		file_name: md5(localId)+'.amr',
		file_resize_url: '',
		file_url: localId,
		play_time: timeLength
	};
	
	uH2.addEventListener('tap',function(){
		var self = this;
		$(".BtnAudioPaly").find('h2').removeClass('active');
		if (self.disable) {
			self.disable = false;
			wxUtil.stopVoice(localId); 
			return;
		}
		self.disable = true;
		self.classList.add('active');
		wxUtil.playVoice(localId, function(){
			self.disable = false;
			self.classList.remove('active');
		});
	});
	uSpan.addEventListener('tap', function() {
		this.parentNode.parentNode.removeChild(this.parentNode);
	});
	
	uAffixItem.appendChild(uH2);
	uAffixItem.appendChild(uH4);
	uAffixItem.appendChild(uSpan);
	return uAffixItem;
};

function CreateElementAffixAudioLocal2(e) {
	var uAffixItem = document.createElement("div");
	var uH1 = document.createElement("h1");
	var uH2 = document.createElement("h2");
	var uH4 = document.createElement("h4");
	var uSpan = document.createElement("span");
	uAffixItem.className = 'mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnAudioPaly';
	uAffixItem.affixData = e;
	uH1.className = 'hxx-affix-downlad';
	uH1.innerText = '下载音频';

	wxUtil.downloadAudioFile("fileAction/amrToMp3",{
		file_path: uAffixItem.affixData.file_url, 
		success: function(e) {
			uAffixItem.affixData.file_path = e;
			cache.hideDom(uH1);
		},
		download: function(d) {
			if(d == 0) {
				uH1.innerText = '00%';
			} else {
				uH1.innerText = d + '%';
			}
		},
		error: function(e) {
			uH1.innerText = '下载失败';
		}
	});
	uH2.innerHTML = '&#xe663;';
	uH2.addEventListener('tap', function() {
		var self = this;
		$(".BtnAudioPaly").find('h2').removeClass('active');
		if (self.disable) {
			self.disable = false;
			stopPlayH5Audio();
			return;
		}
		self.disable = true;
		self.classList.add('active');
		startPlayH5Audio(self.parentNode.affixData.file_path, function() {
			self.disable = false;
			self.classList.remove('active');
		});
	});
	uH4.innerText = e.play_time + '“';
	uSpan.innerHTML = '&#xe673;';
	uSpan.addEventListener('tap', function() {
		this.parentNode.parentNode.removeChild(this.parentNode);
	});
	uAffixItem.appendChild(uH2);
	uAffixItem.appendChild(uH4);
	uAffixItem.appendChild(uSpan);
	return uAffixItem;
};

function AppendDocumentFragment(e){
	var uDocumentFragment = document.createDocumentFragment();
	for (var i = 0; i < e.length; i++) {
		uDocumentFragment.appendChild(CreateElementAffixImgLocad(e[i]));
	}
	uCustom1ListAffix.appendChild(uDocumentFragment);
};

function startPlayH5Audio(src, ended){
	uAudio.src = src;
	uAudio.play();
	uAudio.addEventListener("ended", function() {
        ended();
    }, false);
}

function stopPlayH5Audio(){
	uAudio.pause();
	uAudio.currentTime = 0.0; 
}