var uList = document.getElementById("List");
var uAddBtn = document.getElementById("AddBtn");
var uUploadBox = document.getElementById("UploadBox");
var uHint1 = document.getElementById("Hint1");
var uHint2 = document.getElementById("Hint2");

var oPicker = null;
var oMask = null;

mui.init({
	gestureConfig: {
		longtap: true //启动长按功能
	}
});

mui.ready(function(){
	wxUtil.init(cache.getSchoolId(), window.location.href);
	cache.hideDom(uUploadBox);
	oPdata.class_id = getParamValue("class_id");
	oPdata.path_list = [];
	mui.previewImage();
	BindEvent();
	InitPicker();
	InitPage();
});

function InitPage() {
	//创建遮罩
	oMask = mui.createMask(function() {
		document.getElementById('abox').style.display = "none";
	});
};

function InitPicker() {
	oPicker1 = new mui.PopPicker({
		layer: 1
	});
	oPicker1.setData([{value:'delete',text:'删除'}]);
};

function BindEvent() {
	uAddBtn.addEventListener('tap', function() {
		wxUtil.chooseImage({
			success:function(imgs){
				AppendDocumentFragment(imgs);
			}
		});
	});
	
	mui('#List').on('longtap', ".hxx-photo", function(e) {
		var _this = this;
		oPicker1.show(function(item) {
			if(item[0].value == 'delete'){
				_this.parentNode.removeChild(_this);
			}
		});
	});
	
	document.getElementById("Upload").addEventListener('tap', function() {
		var self = this;
		if(self.disable) {
			return;
		}
		self.disable = true;
		var uPhotoList = document.querySelectorAll(".hxx-photo");
		oPdata.path_list = [];
		for (var i = 0; i < uPhotoList.length; i++) {
			oPdata.path_list.push(uPhotoList[i].affixData);
		}

		if(oPdata.path_list.length<=0) {
			self.disable = false;
			mui.toast("没有图片");
			return;
		} else {
			cache.showDom(uUploadBox);
			wxUtil.uploadImage("fileAction/uploadWechatFile", {
				data: {
					localIds:oPdata.path_list,
					module_code: '009002'
				},
				success: function(aRe){
					var afileURL = [];
					var afileResizeURL = [];
					var afileName = [];
					for (var i = 0; i < aRe.length; i++) {
						var oRe = aRe[i];
						afileURL.push(oRe.file_url);
						afileResizeURL.push(oRe.file_resize_url);
						afileName.push(oRe.file_name);
					}
					AddPhoto(afileURL.join(','), afileResizeURL.join(','),afileName.join(','));
					mui.later(function(){
						uHint1.innerText = '文件全部上传成功。';
					},50);
					self.disable = false;
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
		}
	});
};

//插入一个图片
function AppendDocumentFragment(e){
	for(var i = 0; i < e.length; i++) {
		uList.insertBefore(CreateElement(e[i]), uAddBtn);
	}
};

//创建当个照片浏览
function CreateElement(e){
	var uDiv = document.createElement("div");
	uDiv.className = 'mui-col-sm-3 mui-col-xs-3 hxx-photo';
	uDiv.affixData = e;
	var img = document.createElement("img");
	img.src = e;
	img.className = "pic_imgs";
	img.setAttribute('data-preview-src', "");
	img.setAttribute('data-preview-group', "1");
	img.onload = function() {
		this.style.height = "100%";
		this.style.width = "98%";
	};
	uDiv.appendChild(img);
	return uDiv;
};

function AddPhoto(fileURL, fileResizeURL,fileName) {
	cache.myajax("photoAction/addPhoto", {
		data: {
			class_id: oPdata.class_id,
			student_id: 0,
			photo_type: "006010",
			user_type: "003015",
			fileURL: fileURL,
			fileResizeURL: fileResizeURL,
			fileResizeName: fileName
		},
		success: function(data) {
			uHint2.innerText = '数据同步完成';
			mui.later(function(){
				mui.openWindow({
					url: "pindex.html",
					id: "pindex",
					createNew: true,
					show: {
						duration: 300
					}
				});
			},1000);
		},
		error: function(err) {
			uHint2.innerText = '数据同步失败';
			cache.hideDom(uUploadBox);
		}
	});
}



