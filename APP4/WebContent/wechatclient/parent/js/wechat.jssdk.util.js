/**
 * 微信jssdk开发工具
 * varstion 1.0.0
 * by 竺嵩
 * 
 */

var wxUtil = {};
var wxTimer;//计时器
//微信jssdk初始化
wxUtil.init = function(schoolId, URL){
	cache.myajax("wechatAuthAction/jsapiTicket", {
		data: {
			schoolId: schoolId,
			url: URL
		},
		cache : false,
		success: function(data) {
			var config = data.result.data;
			wx.config({
			  //开发模式把下面注释去掉
		      debug: false,
		      appId: config.appId,
		      timestamp: config.timestamp,
		      nonceStr: config.nonceStr,
		      signature: config.signature,
		      jsApiList: [
		        'chooseImage',
		        'uploadImage',
		        'startRecord',
			    'stopRecord',
			    'playVoice',
			    'uploadVoice'
		      ]
		  });
		}
	});
	cache.myajax("wechatAuthAction/accessToken", {
		data: {
			schoolId: schoolId
		},
		cache : false,
		success: function(data) {
			wxUtil.accessToken = data.result.data;
		}
	});
};

//拍照或从手机相册中选图
wxUtil.chooseImage = function(opt){
	
	if (!opt.success) {
		opt.success = function() {};
	}
	
	wx.chooseImage({
	    count: 9, // 默认9
	    sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
	    success: function (res) {
	    	if(res.errMsg == 'chooseImage:ok'){
	    		opt.success(res.localIds);
	    	}
	    }
	});
};

//上传图片
wxUtil.uploadImage = function(url, opt, showWxProgress){
	if (url) {
		url = config.fileURL + url;
	}else{
		return;
	}
	if (!opt.error) {
		opt.error = function() {};
	};
	if (!opt.success) {
		opt.success = function() {};
	};
	if (!opt.upload) {
		opt.upload = function() {};
	};
	var isShowProgressTips = 0;
	if(showWxProgress){
		isShowProgressTips = 1;
	}
	var data = opt.data;
	var localIds = data.localIds;
	if(localIds.length == 0){
		opt.error('没有选择图片');
		return;
	}
	var i = 0, length = localIds.length;  
	var serverIds = new Array();

	opt.upload(5);
	//上传到微信服务器
	function upload2WxServer() {  
        wx.uploadImage({  
        	localId: localIds[i], // 需要上传的图片的本地ID，由chooseImage接口获得
            isShowProgressTips: isShowProgressTips, // 默认为1，显示进度提示
            success: function(res) {  
            	i++;
            	serverIds.push(res.serverId);
            	var d = Math.floor(i / length * 50);
            	opt.upload(d);
            	if (i < length) {  
            		upload2WxServer();  
                }else{
                	upload2fileServer(serverIds);
                }  
            },    
            fail: function(res) {  
            	opt.error('上传失败'); 
            }  
        });  
    }  
	//上传到文件服务器
	function upload2fileServer(serverIds){
		
		for(var i = 0; i < serverIds.length; i++){
			if(i == 0){
				url += '?mediaid_list=' + serverIds[i];
			}else{
				url += '&mediaid_list=' + serverIds[i];
			}
		}
		
		$.ajax({
			url: url,
			type: "POST",
			data: {
				module_code: data.module_code,
				access_token: wxUtil.accessToken
			},
			dataType: "json",
			success: function(data) {
				if(data.success){
					var d = Math.floor(100);
	            	opt.upload(d);
					opt.success(data.result.data);
				}else{
					opt.error('上传失败'); 
				}
			},
			error:function(data){
				opt.error('上传失败');
			}
		});
	}
	upload2WxServer();
};

//上传图片或录音
wxUtil.uploadImageOrRecord = function(url, opt, showWxProgress){
	if (url) {
		url = config.fileURL + url;
	}else{
		return;
	}
	var data = opt.data;
	var items = data.items;
	if(items.length == 0){
		opt.error('没有选择图片或音频');
		return;
	}
	if (!opt.error) {
		opt.error = function() {};
	};
	if (!opt.success) {
		opt.success = function() {};
	};
	if (!opt.upload) {
		opt.upload = function() {};
	};
	var isShowProgressTips = 0;
	if(showWxProgress){
		isShowProgressTips = 1;
	}
	
	var i = 0, length = items.length;  
	var serverItems = new Array();
	opt.upload(5);
	//上传到微信服务器
	function upload2WxServer() {  
		if(i >= length){
			return;
		}
		var item = items[i];
		var type = item.file_type;
		var localId = item.file_path;
		if(type == '020005'){//图片
			wx.uploadImage({  
		    	localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
		        isShowProgressTips: isShowProgressTips, // 默认为1，显示进度提示
		        success: function(res) {  
		        	i++;
		        	var serverItem = {};
		        	serverItem.serverId = res.serverId;
		        	serverItem.localItem = item;
		        	serverItems.push(serverItem);
		        	var d = Math.floor(i / length * 50);
		        	opt.upload(d);
		        	if (i < length) {  
		        		upload2WxServer();  
		            }else{
		            	upload2fileServer(serverItems);
		            }  
		        },    
		        fail: function(res) {  
		        	opt.error('上传失败'); 
		        }  
		    });  
		}else if(type == '020010'){//音频
			wx.uploadVoice({
			    localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
			    isShowProgressTips: isShowProgressTips, // 默认为1，显示进度提示
			    success: function (res) {
			    	i++;
			    	var serverItem = {};
		        	serverItem.serverId = res.serverId;
		        	serverItem.localItem = item;
		        	serverItems.push(serverItem);
		        	var d = Math.floor(i / length * 50);
		        	opt.upload(d);
		        	if (i < length) {  
		        		upload2WxServer();  
		            }else{
		            	upload2fileServer(serverItems);
		            }  
			    },    
		        fail: function(res) {  
		        	opt.error('上传失败'); 
		        } 
			});
		}
	}
	
	//上传到文件服务器
	function upload2fileServer(serverItems){
		for(var i = 0; i < serverItems.length; i++){
			if(i == 0){
				url += '?mediaid_list=' + serverItems[i].serverId;
			}else{
				url += '&mediaid_list=' + serverItems[i].serverId;
			}
		}
		$.ajax({
			url: url,
			type: "POST",
			data: {
				module_code: data.module_code,
				access_token: wxUtil.accessToken
			},
			dataType: "json",
			success: function(data) {
				opt.upload(90);
				if(data.success){
					var files = data.result.data;
					var aRe = [];
					for(var i = 0; i < files.length; i++){
						for(var j = 0; j < serverItems.length; j++){
							if(files[i].file_index == serverItems[j].serverId){
								serverItems[j].localItem.file_url = files[i].file_url; 
								serverItems[j].localItem.file_resize_url = files[i].file_resize_url; 
								aRe.push(serverItems[j].localItem);
							}
						}
					}
					opt.success(aRe);
	            	opt.upload(100);
				}else{
					opt.error('上传失败'); 
				}
			},
			error:function(data){
				opt.error('上传失败');
			}
		});
	}
	upload2WxServer();
};

//下载MP3格式的语音
wxUtil.downloadAudioFile = function(url, opt){
	if (url) {
		url = config.fileURL + url;
	}else{
		return;
	}
	
	if (!opt.error) {
		opt.error = function() {};
	};
	if (!opt.success) {
		opt.success = function() {};
	};
	if (!opt.download) {
		opt.download = function() {};
	};
	opt.download(10);
	$.ajax({
		url: url,
		type: "POST",
		data: {
			amrUrl: opt.file_path
		},
		dataType: "json",
		success: function(data) {
			if(data.success){
				var d = Math.floor(100);
            	opt.download(d);
				opt.success(data.result.data);
			}else{
				opt.error('上传失败'); 
			}
		},
		error:function(data){
			opt.error('上传失败');
		}
	});
};


//开始录音
wxUtil.startRecord = function(){
	wx.startRecord();
	startWxTimer();
};

//停止录音
wxUtil.stopRecord = function(opt){
	if (!opt.success) {
		opt.success = function() {};
	};
	wx.stopRecord({
	    success: function (res) {
	    	var time = stopWxTimer();
	    	var localId = res.localId;
	        opt.success(localId, time);
	    }
	});
};

//播放语音
wxUtil.playVoice = function(localId, ended){
	wx.playVoice({
	    localId: localId // 需要播放的音频的本地ID，由stopRecord接口获得
	});
	wx.onVoicePlayEnd({
	    success: function (res) {
	        if(localId == res.localId){
	        	ended();
	        }
	    }
	});
};

//暂停播放语音
wxUtil.pauseVoice = function(localId){
	wx.pauseVoice({
	    localId: localId // 需要暂停的音频的本地ID，由stopRecord接口获得
	});
};

//停止播放语音
wxUtil.stopVoice = function(localId){
	wx.stopVoice({
	    localId: localId // 需要停止的音频的本地ID，由stopRecord接口获得
	});
};

var mainWxTimer;

function startWxTimer(){
	wxTimer = 0;
	mainWxTimer = window.setInterval(function(){wxTimer++;},1000); 
}

function stopWxTimer(){
	window.clearInterval(mainWxTimer);
	return wxTimer;
}