document.addEventListener( "plusready",  function()
{
	if (plus.runtime.appid != 'HBuilder' && plus.runtime.appid != 'HelloH5') {
		var _BARCODE = 'NJShell',
		B = window.plus.bridge;
		var NJShell =
		{
			NJShellMethod_Data_ParamArry : function (Argus)
			{
	          	try{
	          		return B.execSync(_BARCODE, "NJShellMethod_Data_ParamArry", [Argus]);
	          	}catch(e){
	          		window.plus.NJShell = undefined;
	          	}
	        }
	  	};
    	window.plus.NJShell = NJShell;
    }
}, true );


function closeH5Page(){
	plus.NJShell.NJShellMethod_Data_ParamArry(['closeH5Page']);
};

function updateFail(){
	cache.startUpdate();
};

function updateSuccess(){
	cache.btn(event,function(){
		if(mui.os.ios) {
			try{
				closeH5Page();
			}catch(e){
				plus.runtime.restart();
			}
		} else if(mui.os.android) {
			plus.runtime.quit();
		} 
	});
};

(function($) {
	//安装更新包
	$.InstallUpdate = function(wgtPath) {
		vm.update.size = 99;
		vm.update.title = '更新中....(99%)';
		if (wgtPath.indexOf('_')!=0) {
			wgtPath = plus.io.convertAbsoluteFileSystem(wgtPath);
		}
		plus.runtime.install(wgtPath, {
			force: true
		}, function() {
			vm.update.title = '更新成功';
			vm.update.size = 100;
			vm.update.btn.state = 1;
			vm.update.btn.text = '手动重启应用';
		}, function(e) {
			vm.update.title = '下载失败';
			vm.update.btn.state = 2;
			vm.update.btn.text = '重试';
			cache.bugSubmit(e);
		});
	};
	//下载局部更新包
	$.DownlaodUpdate = function(wgtURL) {
		plus.navigator.closeSplashscreen();
		vm.update.state = 1;
		vm.update.title = '更新中....';
		vm.update.btn.state = 0;
		vm.update.btn.text = '';
		vm.update.size = 0;
		cache.downloadFile(wgtURL, {
			success: function(wgtPath) {
				cache.InstallUpdate(wgtPath);
			},
			download: function(e) {
				var d = Math.floor(e.downloadedSize / e.totalSize * 100);
				if(e.downloadedSize == 0) {
					vm.update.size = 0;
				}else{
					if (d>=99) {
						vm.update.size = 99;
					}else{
						vm.update.size = d;
					}
				}
				vm.update.title = '更新中....('+vm.update.size+'%)';
			},
			error: function(err) {
				vm.update.title = '下载失败';
				vm.update.btn.state = 2;
				vm.update.btn.text = '重试';
			}
		});
		return;
	};
	$.checkVersion = function(a, b) {
		var arr = a.split('.');
		var brr = b.split('.');
		for(var i = 0; i < arr.length; i++) {
			try {
				if(cache.isNull(brr[i])) {
					return true;
				}
				if(parseInt(arr[i]) > parseInt(brr[i])) {
					return true;
				}else if (parseInt(arr[i]) < parseInt(brr[i])) {
					return false;
				}
			} catch(e) {
				return false;
			}
		}
		return false;
	};
	//检查是否有更新
	$.UpdateCheck = function(opt) {
		plus.runtime.getProperty(plus.runtime.appid, function(wgtinfo) {
			var app_url = '';
			cache.ajax("systemAction/getLastVersion", {
				data: {},
				success: function(oRe) {
					if(oRe.all_version && cache.checkVersion(oRe.all_version, plus.runtime.version)) {
						if (opt&&opt.success) {
							opt.success();
						}
						if(mui.os.android) {
							cache.DownlaodUpdate(oRe.all_url);
						} else if(mui.os.ios) {
							plus.runtime.openURL(oRe.all_url);
						}
						return;
					} else if(oRe.min_version && cache.checkVersion(oRe.min_version, wgtinfo.version)) {
						if (opt&&opt.success) {
							opt.success();
						}
						cache.DownlaodUpdate(oRe.min_url);
						return;
					}
					if (opt&&opt.error) {
						opt.error();
					}
				},
				error: function(data) {
					if (opt&&opt.error) {
						opt.error();
					}
				}
			});
		});
	};

	$.startUpdate = function() {
		if(window.plus) {
			cache.UpdateCheck();
		} else {
			mui.plusReady(function() {
				cache.UpdateCheck();
			});
		}
	};

})(cache);