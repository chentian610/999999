var uTxtTitle = document.getElementById("TxtTitle");
var uTxtSendTime = document.getElementById("TxtSendTime");
var uTxtSenderName = document.getElementById("TxtSenderName");
var uTxtNoticeContent = document.getElementById("TxtNoticeContent");
var uBoxSend = document.getElementById("BoxSend");
var uListAffix = document.getElementById("ListAffix");
var uTxtSend = document.getElementById("TxtSend");
var uListMessage = document.getElementById("ListMessage");

var oExtras = {};
mui.ready(function() {
	oExtras.id = getParamValue("id");
	InitPage();
	BindEvent();
	mui.previewImage();
});

function BindEvent(){
	document.getElementById("BtnSend").addEventListener('tap',function(){
		var self = this;
		if (self.disable) {
			return;
		}
		if (cache.isNull(uTxtSend.value)) {
			mui.toast('回复内容为空');
			return;
		}else{
			oPdata.reply_content = uTxtSend.value;
			uTxtSend.value = '';
		}
		self.disable = true;
		ReplyNotice(function(){
			self.disable = false;
		});
	});
};

function InitPage() {
	cache.ajax("noticeAction/getNoticeById", {
		data: {
			notice_id: oExtras.id,
			user_type :"003015",
			student_id: cache.getChild().student_id
		},
		success: function(oRe) {
			InitHeader(oRe);
			plus.nativeUI.closeWaiting();
			InitNoticeRecv();
			GetNoticeReplyList();
		},
		error: function(err){
			console.log("初始化数据失败");
		}
	});
};


function InitNoticeRecv(){
	cache.showDom(uBoxSend);
};

function GetNoticeReplyList() {
	cache.ajax("noticeAction/getNoticeReplyList", {
		data: {
			notice_id: oExtras.id,
			user_type :"003015",
			receive_id: cache.getChild().student_id,
			receive_type :"003010"
		},
		success: function(aRe) {
			for (var i = 0; i < aRe.length; i++) {
				if (aRe[i].student_id == cache.getChild().student_id && aRe[i].user_type == cache.getUserType()) {
					uListMessage.appendChild(CreateElementMessageSelf(aRe[i]));
				}else{
					uListMessage.appendChild(CreateElementMessageOther(aRe[i]));
				}
			}
		},
		error: function(err) {
		}
	});
};

//发送评论
function ReplyNotice(callBack) {
	cache.ajax("noticeAction/replyNotice", {
		data: {
			receive_id: cache.getChild().student_id,
			student_id: cache.getChild().student_id,
			notice_id: oExtras.id,
			user_type :"003015",
			reply_content: oPdata.reply_content,
			head_url: cache.getChild().head_url,
			receive_type :"003010",
			receive_name: cache.getChild().student_name
		},
		success: function(oRe) {
			uListMessage.appendChild(CreateElementMessageSelf(oRe));
			callBack();
		},
		error: function(err) {
			console.log(JSON.stringify(err));
			callBack();
		}
	});
};

function InitHeader(e){
	uTxtTitle.innerText = e.notice_title;
	uTxtSendTime.innerText = cache.dateStamp2str(e.send_time);
	uTxtSenderName.innerText = e.sender_name;
	uTxtNoticeContent.innerText = e.notice_content;
	try{
		var aFileList = JSON.parse(e.file_list);
		if (!cache.isArray(aFileList)) {
			return;
		}
		for (var i = 0; i < aFileList.length; i++) {
			if (aFileList[i].file_type == '020010') {
				uListAffix.appendChild(CreateElementAffixAudio(aFileList[i]));
			}else{
				uListAffix.appendChild(CreateElementAffixImg(aFileList[i]));
			}
		}
	}catch(e){
		//TODO handle the exception
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
//<div class="mui-col-sm-4 mui-col-xs-4 hxx-affix-item BtnAudioPaly">
//	<h1 class="hxx-affix-downlad">点击下载音频</h1>
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
	cache.downloadFile(uAffixItem.affixData.file_url, {
		success: function(e) {
			uAffixItem.affixData.file_path = e;
			cache.hideDom(uH1);
		},
		download: function(e) {
			var d = Math.floor(e.downloadedSize / e.totalSize * 100);
			if(e.downloadedSize == 0) {
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
	uH2.addEventListener('tap',function(){
		var self = this;
		if (self.disable) {
			self.disable = false;
			cache.stopPlayRecord();
			return;
		}
		self.disable = true;
		self.classList.add('active');
		cache.startPlayRecord(self.parentNode.affixData.file_path,self,function(e){
			self.disable = false;
			self.classList.remove('active');
		});
	});
	if (e.play_time) {
		uH4.innerText = e.play_time + '“';
	}
	uAffixItem.appendChild(uH1);
	uAffixItem.appendChild(uH2);
	uAffixItem.appendChild(uH4);
	return uAffixItem;
};
//<div class="hxx-chat-cell hxx-pull-right">
//	<h4 class="hxx-cell-xing">黄</h4>
//	<p class="hxx-cell-content">内容</p>
//</div>
function CreateElementMessageSelf(e){
	var uMes = document.createElement("div");
	uMes.affixData = e;
	uMes.className = 'hxx-chat-cell hxx-pull-right';
	uMes.innerHTML = '<h4 class="hxx-cell-xing">'+e.receive_name[0]+'</h4><p class="hxx-cell-content">'+e.reply_content+'</p>';
	return uMes;
};
//<div class="hxx-chat-cell hxx-pull-left">
//	<h4 class="hxx-cell-xing">黄</h4>
//	<p class="hxx-cell-content">内容</p>
//</div>
function CreateElementMessageOther(e){
	var uMes = document.createElement("div");
	uMes.affixData = e;
	uMes.className = 'hxx-chat-cell hxx-pull-left';
	uMes.innerHTML = '<h4 class="hxx-cell-xing">'+e.reply_name[0]+'</h4><p class="hxx-cell-content">'+e.reply_content+'</p>';
	return uMes;
};

