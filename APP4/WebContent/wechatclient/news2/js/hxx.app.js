/**
 * 缓存
 * varstion 1.0.0
 * by 黄晓旭
 * 
 */
var chnNumChar = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '久', '十'];
(function($, cache) {
	cache.setSchoolId = function(v) {
		localStorage.setItem("school_id",v);
	};
	
	cache.getSchoolId = function() {
		return localStorage.getItem("school_id");
	};

	cache.ajax = function(URI, opt, bool) {
		var self = this;
		var URL = config.url + URI;
		var oRe = {};
		if(URI.indexOf("http://") === 0 || URI.indexOf("https://") === 0) {
			URL = URI;
		}
		opt.crossDomain = true;
		if(!opt.timeout) opt.timeout = 30000;
		if(!opt.dataType) opt.dataType = 'json';
		if(!opt.type) opt.type = 'post';
		if(!opt.data) opt.data = {};
		if(!opt.data.school_id) opt.data.school_id = self.getSchoolId();
		if(!opt.data.app_type) opt.data.app_type = self.getAppType();
		if(!opt.data.user_id) opt.data.user_id = self.getUserId();
		if(!opt.data.user_type) opt.data.user_type = self.getUserType();
		if(!opt.data.client_id) opt.data.client_id = self.getClientID();
		if(!opt.data.user_name) opt.data.user_name = self.getUserName();
		var fn = {
			error: function(XMLHttpRequest, textStatus, errorThrown) {},
			success: function(data, textStatus) {}
		};
		if(opt.error) fn.error = opt.error;
		if(opt.success) fn.success = opt.success;
		//扩展增强处理
		opt.error = function(xhr, type, errorThrown) {
			oRe.get = URL + '?' + opt.data;
			oRe.type = type;
			oRe.status = xhr.status;
			oRe.statusText = xhr.statusText;
			cache.bugSubmit(oRe.type, oRe.status, oRe.statusText, oRe.get);
			console.log(URI);
			console.log(oRe.get);
			try{
				console.log('xhr:'+ JSON.stringify(xhr));
			}catch(e){
				console.log(e);
			}
			console.log('type:'+JSON.stringify(type));
			console.log('errorThrown:' + JSON.stringify(errorThrown));
			fn.error(xhr);
			return;
		};
		opt.success = function(data, textStatus, xhr) {
			oRe.get = URL + '?' + opt.data;
//			console.log(oRe.get);
//			console.log(JSON.stringify(data));
			if(cache.isObject(data)) {
				switch(data.code) {
					case 1:
						if(self.isObject(data.result)) {
							if(cache.isObject(data.result.data)){
								fn.success(self.theObject(data.result.data));
							}else{
								fn.success(data.result.data);
							}
						} else {
							fn.success(data);
						}
						break;
					case 100:
						fn.error(data); 
						break;
					case 0:
						mui.toast(data.msg);
						fn.error(data);
						break;
					default:
						oRe.msg = '未知错误';
						fn.error(data);
						break;
				}
			} else {
				data.code = -1;
				data.msg = '后台返回的数据不是json格式的对象';
				fn.error(data);
			}
		};
		mui.ajax(URL, opt);
	};
	cache.getResourcesFromNet = function(opt) {
		var self = this;
		if(!opt.timeout) opt.timeout = 10000;
		if(!opt.dataType) opt.dataType = 'json';
		if(!opt.type) opt.type = 'post';
		var fn = {
			error: function(XMLHttpRequest, textStatus, errorThrown) {},
			success: function(data, textStatus) {}
		};
		if(opt.error) fn.error = opt.error;
		if(opt.success) fn.success = opt.success;
		opt.error = function(xhr, type, errorThrown) {
			switch(type) {
				case 'abort':
					mui.toast('网络异常');
					break;
				case 'timeout':
					mui.toast('请求超时');
					break;
				default:
					break;
			}
			fn.error();
		};
		opt.success = function(data, textStatus, xhr) {
			fn.success(data);
		};
		var dTime = new Date();
		opt.JsonInfo.StreamingNo = md5(dTime.getTime());
		opt.JsonInfo.TimeStamp = cache.time2str(dTime);
		opt.JsonInfo.AppId = '4Ga3K8yT6X';
		opt.JsonInfo.Source = 'ShouJiDuan';
		opt.JsonInfo.Sign = md5('4Ga3K8yT6XShouJiDuan2JFsjysw7JSXkdMjscKdnhmP8ATjkrtK' + cache.date2str(dTime) + ' 03:30:00');
		opt.data = {};
		opt.data.JsonInfo = JSON.stringify(opt.JsonInfo);
		mui.ajax('http://118.180.8.123:8081/ecp/resources_share', opt);
	};
	//获取教学资源内容
	cache.getResources = function(jsoninfo, success, erro) {
		var self = this;
		try {
			var k = JSON.stringify(jsoninfo);
			var oCache = self._get(k);
			if(self.isNull(oCache) || !self.isArray(oCache)) {
				self.getResourcesFromNet({
					JsonInfo: jsoninfo,
					success: function(data) {
						self._set(k, data);
						success(data);
					},
					error: function(data) {
						erro({
							code: 98,
							msg: JSON.stringify(data)
						});
					}
				});
				//if end
			} else {
				success(oCache);
				//else end
			}
		} catch(e) {
			erro({
				code: 99,
				msg: '教学云资源获取失败，' + e
			});
		}
	};
	//上传文件
	cache.uploadFile = function(url, opt, bool) {
		var self = this;
		if(url) {
			url = config.fileURL + url;
		} else {
			return;
		}

		bool = bool || false;
		if(!opt.timeout) opt.timeout = 20000;
		if(!opt.dataType) opt.dataType = 'json';
		if(!opt.type) opt.type = 'post';

		var data = opt.data;
		if(!data.module_code) data.module_code = '009';
		url += '?module_code=';
		url += data.module_code;
		if(!data.school_id) data.school_id = self.getSchoolId();
		if(!data.app_type) data.app_type = self.getAppType();
		if(!data.user_id) data.user_id = self.getUserId();
		if(!data.user_type) data.user_type = self.getUserType();
		if(!data.user_name) data.user_name = self.getUserName();
		if(!data.filekey) data.filekey = 'file';
		if(!data.filePath) {
			mui.toast('上传图片路径为空');
			return;
		}
		if(!opt.error) {
			opt.error = function() {};
		};
		if(!opt.success) {
			opt.success = function() {};
		};
		if(!opt.upload) {
			opt.upload = function() {};
		};
		var task = plus.uploader.createUpload(url, {
				method: opt.type
			},
			function(t, status) {
				if(status == 200) {
					var ds = JSON.parse(t.responseText);
					if(ds.code == 1) {
						opt.success(ds.result.data);
						return;
					} else {
						opt.error(t);
						return;
					}
				} else {
					opt.error(t);
					return;
				}
			}
		);
		if(mui.isArray(data.filePath)) {
			for(var x in data.filePath) {
				task.addFile(data.filePath[x], {
					key: x
				});
			}
		} else {
			opt.error('图片路径必须为数据');
			return;
		}
		for(var k in data) {
			task.addData(k, data[k]);
		}
		task.addEventListener('statechanged', function(u, s) {
			opt.upload(u);
		});
		task.start();
	};
	//下载文件
	cache.downloadFile = function(url, opt) {
		var self = this;
		if(!url) {
			if(opt.error) {
				opt.error();
			}
			console.log("erro:downloadFile- 下载地址为空");
			return;
		}
		var fileName = md5(url);
		var sCacheFilePath = self.GetFilePathByURL(url);
		if(sCacheFilePath) {
			opt.success(sCacheFilePath);
			return;
		}
		var theFile = '';
		if(opt.file) {
			theFile = opt.file
		} else {
			theFile = fileName + '.' + self.fileType(url);
		}
		var fname = '_downloads/images/' + theFile;
		if(!opt.timeout) opt.timeout = 10000;
		if(!opt.type) opt.type = 'post';
		if(!opt.error) {
			opt.error = function() {};
		};
		if(!opt.success) {
			opt.success = function() {};
		};
		if(!opt.download) {
			opt.download = function() {};
		};
		var dtask = plus.downloader.createDownload(url, {
			filename: fname
		}, function(d, status) {
			if(status == 200) {
				var filepath = plus.io.convertLocalFileSystemURL(d.filename);
				self.SetFilePathByURL(url, filepath);
				opt.success(filepath);
			} else {
				opt.error(d);
			}
		});
		dtask.addEventListener('statechanged', function(u, s) {
			opt.download(u);
		});
		dtask.start();
	};

	//压缩图片
	cache.compressImage = function(url, success) {
		var dstURL = md5(url) + '.' + this.fileType(url);
		plus.zip.compressImage({
			src: url,
			dst: dstURL,
			overwrite: true,
			width: '750px'
		}, function(e) {
			success(e.target);
		}, function(e) {
			success(url);
		});
	};

	//封装从相册获取图片
	cache.getImageFromGallery = function(success, error) {
		if(plus) {
			plus.gallery.pick(function(e) {
				var files = e.files;
				for(var i = 0; i < files.length; i++) {
					cache.compressImage(files[i], function(e) {
						success(e,files.length);
					});
				}
			}, function(e) {
				console.log(e.message);
				error();
			}, {
				filter: "image",
				multiple: true
			});
		} else {
			console.log('plus 组建未初始化');
			error();
		}
	};
	//封装从相机获取图片
	cache.getImageFromCamera = function(success, error) {
		var self = this;
		if(plus) {
			plus.camera.getCamera().captureImage(function(e) {
				cache.compressImage(e, function(e) {
					success(e);
				});
			}, function(e) {
				console.log(e.message);
				error();
			});
		} else {
			console.log('plus 组建未初始化');
			error();
		}
	};
	//初始化录音对象，每个页面都要重新执行
	cache.initAudioRecorder = function() {
		var self = this;
		if(self.AudioRecorder) {
			return true;
		} else {
			if(plus) {
				self.AudioRecorder = plus.audio.getRecorder();
				return true;
			} else {
				document.addEventListener('plusready', function() {
					self.AudioRecorder = plus.audio.getRecorder();
				});
				return false;
			}
		}
	};
	//开始录音
	cache.startRecord = function(success, error) {
		var self = this;
		if(!self.AudioRecorder) {
			return;
		}
		self.AudioTime = self.currentTimeStamp();
		self.AudioRecorder.record({
			filename: '_doc/audio/',
			format: 'amr'
		}, function(e) {
			self.AudioTime = self.currentTimeStamp() - self.AudioTime;
			self.AudioTime = Math.floor(self.AudioTime / 1000);
			if(self.AudioTime <= 0) {
				if(error) {
					error('录音时间过短');
				}
			} else {
				if(success) {
					success({
						sPath: e,
						iTime: self.AudioTime
					});
				}
			}
		}, function(e) {
			console.log(JSON.stringify(e));
			if(error) {
				error(e.message);
			}
		});
	};
	//结束录音
	cache.stopRecord = function() {
		var self = this;
		if(!self.AudioRecorder) {
			console.log('录音控件未初始化');
			return;
		} else {
			self.AudioRecorder.stop();
		}
	};
	//播放录音
	cache.startPlayRecord = function(e, u, end) {
		var self = this;
		if(self.AudioPlayer) {
			self.uAudioPlayer.classList.remove('active');
			self.AudioPlayer.stop();
		}
		if(e) {
			if(e.indexOf('_') != 0) {
				e = plus.io.convertAbsoluteFileSystem(e);
				self.AudioPlayer = plus.audio.createPlayer(e);
				self.uAudioPlayer = u;
				self.uAudioPlayer.classList.add('active');
				self.AudioPlayer.play(function(e) {
					end();
				}, function(e) {
					end();
					console.log(JSON.stringify(e));
				});
			} else {
				self.AudioPlayer = plus.audio.createPlayer(e);
				self.uAudioPlayer = u;
				self.uAudioPlayer.classList.add('active');
				self.AudioPlayer.play(function(e) {
					end();
				}, function(e) {
					end();
					console.log(JSON.stringify(e));
				});
			}
		}
	};
	//停止播放录音
	cache.stopPlayRecord = function() {
		var self = this;
		if(self.AudioPlayer && self.uAudioPlayer) {
			self.uAudioPlayer.disable = false;
			self.uAudioPlayer.classList.remove('active');
			self.AudioPlayer.stop();
		}
	};
	//下载的文件缓存到本地
	cache.SetFilePathByURL = function(k, v) {
		try {
			var self = this;
			self._set('image_path_' + md5(k), v);
		} catch(e) {
			console.log('erro: SetImagePathByURL' + e);
		}
	};
	//下载的文件缓存到本地
	cache.GetFilePathByURL = function(k) {
		try {
			var self = this;
			var sImagePath = self._get('image_path_' + md5(k));
			return sImagePath;
		} catch(e) {
			console.log('erro: GetImagePathByURL' + e);
		}
	};

	/*与缓存打交道 start*/
	cache.setStartTime = function() {
		return this._set('app_start_time', cache.timeStamp());
	};
	cache.getStartTime = function() {
		return this._get('app_start_time');
	};
	cache.setUserType = function(v) {
		this._set('user_type', v);
	};
	cache.getUserType = function() {
		return this._get('user_type');
	};
	cache.getAppType = function() {
		if(mui.os.android) {
			return '005010';
		} else if(mui.os.ios) {
			return '005005';
		} else {
			return '005015';
		}
	};
	cache.setHeadPath = function(v) {
		this._set('head_path', v);
	};
	cache.getHeadPath = function() {
		var sHeadPath = cache._get('head_path');
		if (sHeadPath.indexOf('_') == 0) {
			sHeadPath = plus.io.convertLocalFileSystemURL(sHeadPath);
		}
		if (sHeadPath.indexOf('file://') == 0) {
			return sHeadPath;
		}
		if (sHeadPath.indexOf('/') == 0) {
			return 'file://' + sHeadPath;
		}
		return '';
	};
	cache.getClientID = function() {
		//return plus.push.getClientInfo().clientid;
		return 0;
	};
	cache.getChildIndex = function() {
		return this._get('child_index');
	};
	cache.setChildIndex = function(v) {
		return this._set('child_index', v);
	};
	cache.getChild = function() {
		return this._get('child');
	};
	cache.setChild = function(v) {
		this._set('child', v);
	};
	cache.getAutoLogin = function() {
		return this._get('auto_login');
	};
	cache.setAutoLogin = function(v) {
		this._set('auto_login', v);
	};
	cache.getValidateCode = function() {
		return this._get('validate_code');
	};
	cache.setValidateCode = function(v) {
		this._set('validate_code', v);
	};
	cache.setAttendList = function(v) {
		return this._set('attend_list', v);
	};
	cache.getAttendList = function() {
		return this._get('attend_list');
	};

	cache.setNewsList = function(v) {
		return this._set('page_018_index_news_list', v);
	};
	cache.getNewsList = function() {
		return this._get('page_018_index_news_list');
	};

	cache._get = function(k) {
		var self = this;
		try {
			if(self.isString(k)) {
				k += self.getSchoolId();
				var v = localStorage.getItem(k);
				if(cache.isNull(v)) {
					v = plus.storage.getItem(k);
				}
				if(self.isNull(v)) {
					return '';
				} else {
					var t = v.substring(v.length - 1, v.length);
					if(t === 'a') {
						v = v.substring(0, v.length - 1);
						return v;
					} else if(t === 'b') {
						v = v.substring(0, v.length - 1);
						v = JSON.parse(v);
						return v;
					} else {
						return '';
					}
				}
			} else {
				return '';
			}
		} catch(e) {
			console.log('erro:cache.get:' + e);
			return '';
		}
	};
	cache._set = function(k, v) {
		var self = this;
		try {
			if(self.isNull(k)) {
				return false;
			}
			k += self.getSchoolId();
			if(self.isString(v)) {
				v += 'a';
				localStorage.setItem(k, v);
				plus.storage.setItem(k, v);
			} else {
				v = JSON.stringify(v);
				v += 'b';
				localStorage.setItem(k, v);
				plus.storage.setItem(k, v);
			}
			return true;
		} catch(e) {
			console.log('erro:cache.set:  ' + e);
			return false;
		}
	};
	cache.savePage = function(e) {
		cache._set(window.location.href, e);
	};
	cache.getPage = function() {
		return cache._get(window.location.href);
	};
	cache.GetIconURLbyFileType = function(e) {
		switch(e) {
			case 'PPT':
				return '../images/ppt.png';
			case 'DOCX':
				return '../images/doc.png';
			case 'DOC':
				return '../images/doc.png';
			case 'SWF':
				return '../images/flash.png';
			case 'JPG':
				return '../images/jpg.png';
			case 'MP3':
				return '../images/mp3.png';
			case 'PNG':
				return '../images/png.png';
			case 'MP4':
				return '../images/mp4.png';
			case 'XLS':
				return '../images/xls.png';
			case 'TXT':
				return '../images/txt.png';
			case 'PPTX':
				return '../images/ppt.png';
			default:
				return '../images/default.png';
		}
	}
	//TODO 错误搜集
	cache.bugSubmit = function(a, b, c, d, e) {
		try {
			if(plus.runtime.appid != 'HBuilder' && plus.runtime.appid != 'HelloH5') {
				cache.ajax('http://106.14.33.168/api/error', {
					data: {
						app_id: cache.getClientID(),
						error_discribe: a,
						error_line: c,
						error_column: d,
						error_obj: JSON.stringify(e),
						happen_time: cache.time2str(),
						page_url: window.location.href,
						screen_shot: ''
					}
				});
			}

		} catch(e) {}
	};
	cache.getAttendCodeList = function(opt) {
		var self = this;
		var oRe = self.getAttendList();
		if(oRe.isOverdue) {
			opt.success(oRe.aRe);
			return;
		}
		self.ajax("attendAction/getAttendCodeList", {
			data: opt.data,
			success: function(aRe) {
				oRe = {};
				oRe.aRe = aRe;
				oRe.cache_date = self.date2str();
				oRe.isOverdue = true;
				cache.setAttendList(oRe);
				opt.success(aRe);
			},
			error: function(err) {
				opt.error(err);
			}
		});
	};
	cache.getChildByBedroomID = function(bedroomID, OnSuccess, OnErro) {
		var self = this;
		var data = {};
		data.school_id = self.getSchoolId();
		data.app_type = self.getAppType();
		data.user_id = self.getUserId();
		data.user_type = self.getUserType();
		data.user_name = self.getUserName();
		data.bedroom_id = bedroomID;
		var k = md5(JSON.stringify(data));
		var v = self._get(k);
		if(v != null) {
			OnSuccess(v);
		}
		self.ajax("userAction/getStudentPosistionOfBed", {
			data: {
				bedroom_id: bedroomID
			},
			success: function(re) {
				if(re.length <= 0){
					mui.toast("该寝室暂无学生");
					return ;
				}
				var aChilds = [];
				for(var i = 0; i < re.length; i++) {
					var ochild = {};
					ochild.id = re[i].id;
					ochild.school_id = re[i].school_id;
					ochild.bedroom_id = re[i].bedroom_id;
					ochild.bedroom_name = re[i].bedroom_name;
					ochild.bed_code = re[i].bed_code;
					ochild.student_id = re[i].student_id;
					ochild.student_code = re[i].student_code;
					ochild.student_name = re[i].student_name;
					ochild.class_name = re[i].class_name;
					ochild.sex = re[i].sex;
					aChilds.push(ochild);
				}
				self._set(k, aChilds);
				OnSuccess(aChilds);
				return;
			},
			error: function(e) {
				OnErro(e);
			}
		});
	};
	
	cache.UpdateUserInfo = function(OnSuccess, OnErro) {
		cache.ajax('userAction/updateUserInfo', {
			data: {
				user_name: cache.getUserName(),
				head_url: cache.getHeadUrl(),
				sex: cache.getUserSex(true)
			},
			success: OnSuccess,
			error: OnErro
		});
	};
	/*与服务器打交道 end*/
	/*数据缓存*/
	cache.getDictFromNet = function(opt) {
		var aDict = [];
		var index = 1;
		aDict = cache.getDict();
		if(!cache.arrIsNoUse(aDict)) {
			if(opt && opt.success) {
				opt.success(index);
				index++;
			}
		} else {
			aDict = [];
		}
		cache.ajax('dictAction/getDictSchoolList', {
			success: function(aRe) {
				aDict = [];
				for(var i = 0; i < aRe.length; i++) {
					if(aRe[i].dict_code && aRe[i].dict_value && aRe[i].dict_group) {
						var item = {};
						item.dict_group = aRe[i].dict_group;
						item.value = aRe[i].dict_code;
						item.text = aRe[i].dict_value;
						item.sort = aRe[i].sort;
						item.is_active = aRe[i].is_active;
						aDict.push(item);
					}
				}
				cache._setDict(aDict);
				if(opt && opt.success) {
					opt.success(index);
					index++;
				}
			},
			error: function(data) {
				if(cache.arrIsNoUse(aDict)) {
					if(opt && opt.error) {
						opt.error();
					}
				} else {
					if(opt && opt.success) {
						opt.success(index);
						index++;
					}
				}
			}
		});
	};
	cache.getDict = function(v, b) {
		var self = this;
		var aDict = [];
		var cache_dicts = self._getDict();
		if(!cache.arrIsNoUse(cache_dicts)) {
			if(v == true || b == true) {
				if(v != true) {
					for(var k in cache_dicts) {
						if(cache_dicts[k].dict_group == v) {
							aDict.push(cache_dicts[k]);
						}
					}
					return aDict;
				} else {
					return cache_dicts;
				}
			} else {
				if(v) {
					for(var k in cache_dicts) {
						if(cache_dicts[k].dict_group == v && cache_dicts[k].is_active == 1) {
							aDict.push(cache_dicts[k]);
						}
					}
					return aDict;
				} else {
					for(var k in cache_dicts) {
						if(cache_dicts[k].is_active == 1) {
							aDict.push(cache_dicts[k]);
						}
					}
					return aDict;
				}
			}
		} else {
			return [];
		}
	};
	cache._setDict = function(v) {
		return this._set('dict', v);
	};
	cache._getDict = function() {
		return this._get('dict');
	};
	cache.dictCode2Value = function(id) {
		if(id == "015001") {
			return '兴趣';
		}
		var dicts = this.getDict();
		for(var k in dicts) {
			if(dicts[k].value == id) {
				return dicts[k].text;
			}
		}
		return ' ';
	};
	cache.getModuleFromNet = function(opt) {
		var aModule = [];
		var index = 1;
		aModule = cache.getModules();
		if(!cache.arrIsNoUse(aModule)) {
			if(opt && opt.success) {
				opt.success(index);
				index++;
			}
		} else {
			aModule = [];
		}
		cache.ajax('moduleAction/getModuleList', {
			data: {
				user_type: '003'
			},
			success: function(aRe) {
				cache._setModules(aRe);
				if(opt && opt.success) {
					opt.success(index);
					index++;
				}
			},
			error: function(err) {
				if(cache.arrIsNoUse(aModule)) {
					if(opt && opt.error) {
						opt.error();
					}
				} else {
					if(opt && opt.success) {
						opt.success(index);
						index++;
					}
				}
			}
		});
	};
	cache._setModules = function(v) {
		return this._set('modules', v);
	};
	cache._getModules = function() {
		return cache._get('modules');
	};
	cache.getModules = function(u, v) {
		var self = this;
		var aModule = [];
		var cache_modules = [];
		if(typeof(v) == "string") {
			if(!cache.isArray(u)) {
				return false;
			}
		} else {
			if(typeof(u) == "string") {
				v = u;
				u = self._getModules();
			} else {
				return self._getModules();
			}
		}
		for(var i = 0; i < u.length; i++) {
			if(v.indexOf(u[i].user_type) == 0) {
				aModule.push(u[i]);
			}
		}
		return aModule;
	};
	cache.moduleCode2Name = function(id) {
		var aModules = this.getModules();
		for(var f in aModules) {
			if(id == aModules[f].module_code) {
				return aModules[f].module_name;
			}
		}
		return ' ';
	};
	cache.moduleCode2Obj = function(id) {
		var aModules = this.getModules();
		for(var f in aModules) {
			if(id == aModules[f].module_code) {
				return aModules[f];
			}
		}
		return {};
	};
	cache.moduleCode2Url = function(id) {
		var aModules = this.getModules();
		for(var f in aModules) {
			if(id == aModules[f].module_code) {
				return aModules[f].icon_url;
			}
		}
		return '../images/default-img.png';
	};
	cache.getClassRoomFromNet = function(opt) {
		var aClassRoom = [];
		var index = 1;
		aClassRoom = cache.getClassRoom();
		if(!cache.arrIsNoUse(aClassRoom)) {
			if(opt && opt.success) {
				opt.success(index);
				index++;
			}
		} else {
			aClassRoom = [];
		}
		cache.ajax('gradeAction/getGradeAndClass', {
			success: function(aRe) {
				aClassRoom = [];
				for(var i = 0; i < aRe.length; i++) {
					var oGrade = aRe[i];
					if(oGrade.grade_name && oGrade.grade_id && oGrade.class_list) {
						oGrade.value = oGrade.grade_id;
						oGrade.text = oGrade.grade_name;
						oGrade.children = [];
						try {
							var aClass = JSON.parse(oGrade.class_list);
						} catch(e) {
							continue;
						}
						oGrade.class_list = '';
						for(var j = 0; j < aClass.length; j++) {
							var oClass = aClass[j];
							oClass.text = oClass.class_name;
							oClass.value = oClass.class_id;
							oClass.grade_id = oGrade.grade_id;
							oClass.grade_name = oGrade.grade_name;
							oClass.teamType = "011005";
							oGrade.children.push(oClass);
						}
						aClassRoom.push(oGrade);
					}
				}
				cache._setClassRooms(aClassRoom);
				if(opt && opt.success) {
					opt.success(index);
					index++;
				}
			},
			error: function(err) {
				if(cache.arrIsNoUse(aClassRoom)) {
					if(opt && opt.error) {
						opt.error();
					}
				} else {
					if(opt && opt.success) {
						opt.success(index);
						index++;
					}
				}
			}
		});
	};
	cache._setClassRooms = function(v) {
		return this._set('classrooms', v);
	};
	cache._getClassRooms = function() {
		return this._get('classrooms');
	};
	cache.getClassRoom = function() {
		var self = this;
		var cache_classroom = self._getClassRooms();
		if(cache.arrIsNoUse(cache_classroom)) {
			return [];
		}
		return cache_classroom;
	};
	cache.classRoomCode2Value = function(id) {
		var classrooms = this.getClassRoom();
		for(var i = 0; i < classrooms.length; i++) {
			if(classrooms[i].value == id) {
				return classrooms[i].text;
			}
			for(var a = 0; a < classrooms[i].children.length; a++) {
				if(classrooms[i].children[a].value == id) {
					return classrooms[i].children[a].text;
				}
			}
		}
		return ' ';
	};
	cache.getClassByTeams = function(e, d) {
		var self = this;
		var aTeams = self.getTeams();
		var classRoom = self.getClassRoom();
		var classList = [];
		var bExist = false;
		for(var i = 0; i < aTeams.length; i++) {
			var item = aTeams[i];
			bExist = false;
			if(item.duty == "016005") { //任课老师
				for(var j = 0; j < classList.length; j++) {
					if(classList[j].value == item.grade_id) {
						for(var x = 0; x < classList[j].children.length; x++) {
							if(classList[j].children[x].value == item.class_id) {
								bExist = true;
								break;
							}
						}
						if(bExist) {
							break;
						}
						var oClassItem = {};
						oClassItem.value = item.class_id;
						oClassItem.text = item.class_name;
						oClassItem.teamType = "011005";
						classList[j].children.push(oClassItem);
						bExist = true;
						break;
					}
				}
				if(bExist) {
					continue;
				}
				var oGradeItem = {};
				oGradeItem.value = item.grade_id;
				oGradeItem.text = cache.classRoomCode2Value(item.grade_id);
				oGradeItem.children = [];
				var oClassItem = {};
				oClassItem.value = item.class_id;
				oClassItem.text = item.class_name;
				oClassItem.teamType = "011005";
				oGradeItem.children.push(oClassItem);
				classList.push(oGradeItem);
			}
			if(item.duty == "016010") { //年级主任
				for(var k = 0; k < classList.length; k++) {
					if(classList[k]) {
						if(classList[k].value === item.grade_id) {
							classList.splice(k, 1);
						}
					}

				}
				for(var j = 0; j < classRoom.length; j++) {
					if(item.grade_id == classRoom[j].value) {
						var aGradeItem = classRoom[j];
						classList.push(aGradeItem);

					}
				}
			}

			if(e) {
				if(item.duty == "016020" || item.duty == "016015") { //校领导和行政管理组
					if(d) {
						return classRoom;
					} else {
						var oGradeItem = {};
						oGradeItem.text = "全校";
						oGradeItem.value = 0;
						oGradeItem.children = [];
						oGradeItem.children.push({
							text: "全校",
							value: 0,
							teamType: "011005"
						});
						classList.push(oGradeItem);
					}
				}
			} else {
				if(item.duty == "016020") { //校领导
					if(d) {
						return classRoom;
					} else {
						var oGradeItem = {};
						oGradeItem.text = "全校";
						oGradeItem.value = 0;
						oGradeItem.children = [];
						oGradeItem.children.push({
							text: "全校",
							value: 0,
							teamType: "011005"
						});
						classList.push(oGradeItem);
					}
				}
				if(item.duty == "016025") { //兴趣班
					for(var j = 0; j < classList.length; j++) {
						if(classList[j].value == '0') {
							item.text = item.class_name;
							item.value = item.contact_id;
							item.teamType = "011015";
							classList[j].children.push(item);
							bExist = true;
							break;
						}
					}
					if(bExist) {
						continue;
					}
					var oGradeItem = {};
					oGradeItem.value = '0';
					oGradeItem.text = '兴趣班';
					oGradeItem.children = [];
					item.value = item.contact_id;
					item.text = item.class_name;
					item.teamType = "011015";
					oGradeItem.children.push(item);
					classList.push(oGradeItem);
				}
			}

		}
		return classList;
	};
	cache._setBedRooms = function(v) {
		return this._set('bedrooms', v);
	};
	cache._getBedRooms = function() {
		return this._get('bedrooms');
	};
	cache.getBedRoom = function() {
		var aBedRoom = cache._getBedRooms();
		return aBedRoom;
	};
	cache.getBedRoomFromNet = function(opt) {
		var aBedRoom = [];
		var index = 1;
		aBedRoom = cache.getBedRoom();
		if(!cache.arrIsNoUse(aBedRoom)) {
			if(opt && opt.success) {
				opt.success(index);
				index++;
			}
		} else {
			aBedRoom = [];
		}
		cache.ajax('bedroomAction/getBedroomList', {
			success: function(aRe) {
				aBedRoom = [];
				for(var i = 0; i < aRe.length; i++) {
					var oBedRoom = aRe[i];
					oBedRoom.value = oBedRoom.bedroom_id;
					oBedRoom.text = oBedRoom.bedroom_name;
					aBedRoom.push(oBedRoom);
				}
				cache._setBedRooms(aBedRoom);
				if(opt && opt.success) {
					opt.success(index);
					index++;
				}
			},
			error: function(err) {
				if(cache.arrIsNoUse(aBedRoom)) {
					if(opt && opt.error) {
						opt.error();
					}
				} else {
					if(opt && opt.success) {
						opt.success(index);
						index++;
					}
				}
			}
		});
	};
	cache.bedRoomCode2Name = function(id) {
		var bedrooms = this.getBedRoom();
		for(var i = 0; i < bedrooms.length; i++) {
			if(bedrooms[i].value == id) {
				return bedrooms[i].text;
			}
		}
		return '';
	};
	cache._setPlaygroup = function(v) {
		return this._set('playgroups', v);
	};
	cache._getPlaygroup = function(v) {
		return this._get('playgroups');
	};
	cache.getPlaygroup = function() {
		return cache._getPlaygroup();
	};
	cache.getPlayGroupFromNet = function(opt) {
		var aPlaygroup = [];
		var index = 1;
		aPlaygroup = cache.getPlaygroup();
		if(!cache.arrIsNoUse(aPlaygroup)) {
			if(opt && opt.success) {
				opt.success(index);
				index++;
			}
		} else {
			aPlaygroup = [];
		}
		cache.ajax('contactAction/getContactGroupListOfManager', {
			data: {
				user_type: '003010'
			},
			success: function(aRe) {
				aPlaygroup = [];
				for(var i = 0; i < aRe.length; i++) {
					var oPlaygroup = aRe[i];
					oPlaygroup.text = oPlaygroup.contact_name;
					oPlaygroup.value = oPlaygroup.contact_id;
					aPlaygroup.push(oPlaygroup);
				}
				cache._setPlaygroup(aPlaygroup);
				if(opt && opt.success) {
					opt.success(index);
					index++;
				}
			},
			error: function(err) {
				if(cache.arrIsNoUse(aPlaygroup)) {
					if(opt && opt.error) {
						opt.error();
					}
				} else {
					if(opt && opt.success) {
						opt.success(index);
						index++;
					}
				}
			}
		});
	};
	cache.playgroupID2Name = function(id) {
		var dicts = this.getPlaygroup();
		for(var k in dicts) {
			if(dicts[k].contact_id == id) {
				return dicts[k].contact_name;
			}
		}
		return 'null';
	};
	cache.playgroupCourse2Name = function(id) {
		var dicts = this.getPlaygroup();
		for(var k in dicts) {
			if(dicts[k].course == id) {
				return dicts[k].course_name;
			}
		}
		return cache.dictCode2Value(id);
	};
	cache.getUserFromNet = function(opt) {
		cache.ajax('userAction/getUserInfo', {
			success: function(oRe) {
				cache.setUser(oRe);
				if(opt && opt.success) {
					opt.success();
				}
			},
			error: function() {
				if(opt && opt.success) {
					opt.success();
				}
			}
		});
	};
	cache._setUserHeadUrl = function(v) {
		return this._set('user_head_url', v)
	};
	cache._getUserHeadUrl = function() {
		return this._get('user_head_url');
	};
	cache._setUserName = function(v) {
		return this._set('user_name', v)
	};
	cache._getUserName = function() {
		return this._get('user_name');
	};
	cache._setUserSex = function(v) {
		return this._set('user_sex', v)
	};
	cache._getUserSex = function() {
		return this._get('user_sex');
	};
	cache._setUserID = function(v) {
		return this._set('user_id', v)
	};
	cache._getUserID = function() {
		return this._get('user_id');
	};
	cache._setUserPhone = function(v) {
		return this._set('user_phone', v)
	};
	cache._getUserPhone = function() {
		return this._get('user_phone');
	};
	cache._setUserPass = function(v) {
		return this._set('user_pass', v)
	};
	cache._getUserPass = function() {
		return this._get('user_pass');
	};
	cache._setUserTeacherList = function(v) {
		return this._set('user_teacher_list', v)
	};
	cache._getUserTeacherList = function() {
		return this._get('user_teacher_list');
	};
	cache._setUserParentList = function(v) {
		return this._set('user_parent_list', v)
	};
	cache._getUserParentList = function() {
		return this._get('user_parent_list');
	};
	cache.setUser = function(v) {
		var self = this;
		cache._setUserHeadUrl(v.head_url);
		cache._setUserName(v.user_name);
		cache._setUserSex(v.sex);
		cache._setUserPhone(v.phone);
		cache._setUserPass(v.pass_word);
		cache._setUserID(v.user_id);
		try {
			var teacher_list = JSON.parse(v.teacher_list);
			cache._setUserTeacherList(teacher_list);
		} catch(e) {
			console.log(e);
		}
		try {
			var parent_list = JSON.parse(v.parent_list);
			for(var i = 0; i < parent_list.length; i++) {
				var team_list = JSON.parse(parent_list[i].team_list);;
				parent_list[i].team_list = team_list;
			}
			cache._setUserParentList(parent_list);
		} catch(e) {
			console.log(e);
		}
	};
	cache.getUser = function() {};
	cache.setPhone = function(v) {
		return cache._setUserPhone(v);
	};
	cache.getPhone = function() {
		return cache._getUserPhone();
	};
	cache.setUserSex = function(v) {
		return cache._setUserSex(v);
	};
	cache.getUserSex = function(v) {
		if(v) {
			return cache._getUserSex();
		}
		switch(cache._getUserSex()) {
			case 0:
				return '男';
				break;
			case 1:
				return '女';
				break;
			default:
				return '';
				break;
		}
	};
	cache.setUserPass = function(v) {
		return cache._setUserPass(v);
	};
	cache.getUserPass = function() {
		return cache._getUserPass();
	};
	cache.setHeadUrl = function(v) {
		return cache._setUserHeadUrl(v);
	};
	cache.getHeadUrl = function() {
		var head_url = cache._getUserHeadUrl();
		if(head_url.indexOf('http') == 0) {
			return head_url;
		}
		return '../images/default-img.png';
	};
	cache.setUserName = function(v) {
		return cache._setUserName(v);
	};
	cache.getUserName = function() {
		return cache._getUserName();
	};
	cache.setUserId = function(v) {
		return cache._setUserID(v);
	};
	cache.getUserId = function() {
		return cache._getUserID();
	};
	cache.deleTeam = function(id) {
		var teams = cache.getTeams();
		for(var i = 0; i < teams.length; i++) {
			if(teams[i].teacher_id == id) {
				teams.splice(i, 1);
				cache.setTeams(teams);
				if(teams.length <= 0) {
					cache.insertTeam({});
				}
				return;
			}
		}
	};
	cache.getTeams = function() {
		var aTeams = cache._getUserTeacherList();
		if(this.isArray(aTeams)) {
			return aTeams;
		}
		return [];
	};
	cache.setTeams = function(v) {
		if(cache.isArray(v)) {
			cache._setUserTeacherList(v);
		}
	};
	cache.hasTeams = function() {
		var aTeams = cache.getTeams();
		if(aTeams.length <= 0) {
			return 0;
		}
		if(this.isNull(this.getUserPass())) {
			return 1;
		}
		for(var i = 0; i < aTeams.length; i++) {
			if(cache.isNull(cache.dictCode2Value(aTeams[i].duty))) {
				return 1;
			}
			if(aTeams[i].is_confirm != 1) {
				return 1;
			}
		}
		return 2;
	};
	cache.insertTeam = function(v) {
		var teams = cache.getTeams();
		for(var i = 0; i < teams.length; i++) {
			if(teams[i].teacher_id == v.teacher_id) {
				teams[i] = team;
				cache.setTeams(teams);
				return;
			}
		}
		teams.unshift(v);
		cache.setTeams(teams);
	};
	cache.getChilds = function() {
		var aChilds = cache._getUserParentList();
		if(this.isArray(aChilds)) {
			return aChilds;
		}
		return [];
	};
	cache.setChilds = function(v) {
		if(cache.isArray(v)) {
			return cache._setUserParentList(v);
		}
	};
	cache.hasChilds = function() {
		var aChilds = cache.getChilds();
		if(aChilds.length <= 0) {
			return 1;
		}
		if(this.isNull(this.getUserPass())) {
			return 1;
		}
		return 2;
	};
	cache.deleChild = function(id) {
		var aChilds = cache.getChilds();
		for(var i = 0; i < aChilds.length; i++) {
			if(aChilds[i].parent_id == id) {
				aChilds.splice(i, 1);
				cache.setChilds(aChilds);
				return;
			}
		}
	};
	cache.insertChild = function(v) {
		var aChilds = cache.getChilds();
		v.info = v.student_name + ' - ' + v.class_name + ' - ' + v.student_code;
		for(var i = 0; i < aChilds.length; i++) {
			if(aChilds[i].parent_id == v.parent_id) {
				aChilds[i] = v;
				cache.setChilds(aChilds);
				return;
			}
		}
		aChilds.unshift(v);
		cache.setChilds(aChilds);
	};
	cache.CENetworkBroken = function() {
		var uDiv = document.createElement("div");
		uDiv.style.position = 'fixed';
		uDiv.style.top = '0';
		uDiv.style.marginTop = '44px';
		uDiv.style.backgroundColor = '#fff793';
		uDiv.style.width = '100%';
		uDiv.style.height = '30px';
		uDiv.style.zIndex = '9999';
		uDiv.innerHTML = '<span style="margin-left: 15px;line-height: 30px;font-size:15px;color:#fb5a5a;text-align:center;">当前无网络，请检查网络连接。</span><img style="position: relative;float: right;top: 5px;right: 16px;" src="../images/uncon_cancel@1x.png" />';
		uDiv.querySelector('img').addEventListener('tap', function() {
			this.parentNode.parentNode.removeChild(this.parentNode);
		});
		document.body.appendChild(uDiv);
	};
	cache.destoryTip = function() {
		try {
			if(self.uDiv) {
				self.uDiv.parentNode.removeChild(self.uDiv);
				self.uDiv = null;
			}
		} catch(e) {
			this.uDiv = null;
		}
	};
	cache.CEAjaxFail = function(uList, fun) {
		var self = this;
		cache.destoryTip();
		uDiv = document.createElement("div");
		uDiv.style.textAlign = 'center';
		uDiv.style.padding = '40px 20px';
		uDiv.innerHTML = '<img style="width: 60%;margin: 10px;" src="../images/tip_unconnection.png"/><div style="margin: 0 auto;text-align: center;font-size:18px;color:#d6d6d6;letter-spacing:0px;text-align:center;">请求失败，请检查网络后点击刷新</div>';
		if(fun && typeof(fun) == "function") {
			uDiv.addEventListener('tap', fun);
		}
		self.uDiv = uDiv;
		uList.appendChild(uDiv);
	};
	cache.CELoading = function(fun) {
		var self = this;
		cache.destoryTip();
		uDiv = document.createElement("div");
		uDiv.style.position = 'fixed';
		uDiv.style.textAlign = 'center';
		uDiv.style.backgroundColor = '#fff';
		uDiv.style.height = 'calc(100% - 45px)';
		uDiv.style.width = '100%';
		uDiv.style.zIndex = '99999999999999';
		uDiv.innerHTML = '<img style="width: 60%;margin: 80px 10px;" src="../images/tip_load.gif"/>';
		if(fun && typeof(fun) == "function") {
			uDiv.addEventListener('tap', fun);
		}
		self.uDiv = uDiv;
		document.body.appendChild(uDiv);
	};
	cache.CEEmpty = function(uList, fun) {
		var self = this;
		cache.destoryTip();
		uDiv = document.createElement("div");
		uDiv.style.textAlign = 'center';
		uDiv.style.backgroundColor = '#fff';
		uDiv.style.width = '100%';
		uDiv.style.margin = '15px 0';
		uDiv.innerHTML = '<img style="width: 60%;margin: 60px;" src="../images/tip_empty.png"/>';
		if(fun && typeof(fun) == "function") {
			uDiv.addEventListener('tap', fun);
		}
		self.uDiv = uDiv;
		uList.appendChild(uDiv);
	};
	/*数据处理函数 start*/

	cache.getAttendCodeByName = function(opt) {
		cache.getAttendCodeList({
			data: {},
			success: function(aRe) {
				for(var i = 0; i < aRe.length; i++) {
					if(aRe[i].attend_code == opt.code) {
						opt.success(aRe[i].attend_name);
						return;
					}
				}
				opt.error('未知');
			},
			error: function(err) {
				opt.error(err);
			}
		})
	};

	cache.getDutyByTeams = function(e) {
		var aTeams = cache.getTeams();
		var aDuty = ["016005", "016010", "016015", "016020"];
		var iIndex = 0;
		var aTeam = aTeams[0];
		for(var i = 0; i < aTeams.length; i++) {
			for(var j = 0; j < aDuty.length; j++) {
				if(aTeams[i].duty == aDuty[j]) {
					if(iIndex < j) {
						iIndex = j;
						aTeam = aTeams[i];
					}

				}
			}
		}
		if(e) {
			return aTeam;
		} else {
			return aDuty[iIndex];
		}
	};
	/*数据处理函数 end*/

	/*常用函数封装 start*/
	//获取URL中文件的类型或者文件的类型
	cache.fileType = function(f) {
		var fs = f.split('/');
		var f = fs[fs.length - 1];
		var ts = f.split('.');
		return ts[ts.length - 1];
	};

	cache.theObject = function(d) {
		if(d === null || d === undefined || d === 'null' || d === 'undefined' || !this.isObject(d)) {
			return {};
		}
		return d;
	};
	cache.theValue = function(d) {
		if(d === null || d === undefined || d === 'null' || d === 'undefined') {
			return ' ';
		}
		return d;
	};

	cache.valueOf = function(d) {
		if(d === null || d === undefined || d === 'null' || d === 'undefined') {
			return ' ';
		}
		return d;
	};

	cache.checkVersion = function(a, b) {
		var arr = a.split('.');
		var brr = b.split('.');
		for(var i = 0; i < arr.length; i++) {
			try {
				if(cache.isNull(brr[i])) {
					return true;
				}
				if(parseInt(arr[i]) > parseInt(brr[i])) {
					return true;
				} else if(parseInt(arr[i]) < parseInt(brr[i])) {
					return false;
				}
			} catch(e) {
				return false;
			}
		}
		return false;
	};

	cache.checkPhoneNum = function(p) {
		var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
		if(reg.test(p)) {
			return true;
		} else {
			return false;
		}
	};

	cache.QuitOnDoubleClick = function() {
		var backButtonPress = 0;
		mui.back = function(event) {
			backButtonPress++;
			if(backButtonPress > 1) {
				plus.runtime.quit();
			} else {
				mui.toast('再按一次退出应用');
			}
			setTimeout(function() {
				backButtonPress = 0;
			}, 1000);
			return false;
		};
	};
	cache.showDom = function(e) {
		if(e && e.style) {
			if(e.style.display != 'block') {
				e.style.display = 'block';
			}
			return true;
		}
		return false;
	};
	cache.hideDom = function(e) {
		if(e && e.style) {
			if(e.style.display != 'none') {
				e.style.display = 'none';
			}
			return true;
		}
		return false;
	}

	//判断v是否为字符串
	cache.isString = function(v) {
		if(typeof(v) === "string") {
			return true;
		} else {
			return false;
		}
	};

	//判断v是否为数字
	cache.isNumber = function(v) {
		if(typeof(v) === "number") {
			return true;
		} else {
			return false;
		}
	};

	cache.isOverdue = function(cache_date) {
		if(cache.isNull(cache_date) || cache.isNull(cache_date.time) || cache.getStartTime() > cache_date.time) {
			return true;
		}
		return false;
	};
	//
	cache.isArray = function(v) {
		return Object.prototype.toString.call(v) === '[object Array]';
	};

	cache.isUsed = function(v) {
		if(this.isArray(v)) {
			if(v.length > 0) {
				return true;
			}
		}
		return false;
	};
	cache.isNoUse = function(v) {
		if(this.isArray(v)) {
			if(v.length > 0) {
				return false;
			}
		}
		return true;
	};
	cache.arrIsNoUse = function(v) {
		if(this.isArray(v)) {
			if(v.length > 0) {
				return false;
			}
		}
		return true;
	};
	//
	cache.isObject = function(v) {
		if(typeof(v) === "object") {
			return true;
		} else {
			return false;
		}
	};

	//判断字符串是否为URL格式
	cache.isUrl = function(u) {
		var self = this;
		if(!self.isString(u)) {
			return false;
		}
		var Expression = /http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
		var objExp = new RegExp(Expression);
		return objExp.test(r);
	};

	cache.isNumber = function(d) {
		var reg = /^[0-9]*$/;
		return reg.test(d);
	};

	cache.isTimestamp = function(d) {
		var reg = /^[0-9]{13}$/;
		return reg.test(d);
	};

	cache.isDate = function(d) {
		var reg = /^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$/;
		return reg.test(d);
	};

	cache.isNull = function(d) {
		if(d == "" || d == null || d == undefined || d == 'null' || d == 'undefined') {
			return true;
		}
		return false;
	};

	cache.isScore = function(d) {
		if(d > 0) {
			return '+' + d;
		} else if(d <= 0) {
			return d
		}
	};

	cache.installPackage = function(path) {
		plus.runtime.install(path, {
			force: true
		}, function() {
			mui.toast('更新安装成功');
			if(mui.os.android) {
				plus.runtime.quit();
			} else if(mui.os.ios) {
				plus.runtime.restart();
			}
		}, function(e) {
			mui.alert('更新安装失败，失败原因：' + JSON.stringify(e));
		});
	};

	cache.UpdateCheck = function(opt) {
		plus.runtime.getProperty(plus.runtime.appid, function(wgtinfo) {
			var app_url = '';
			cache.ajax("systemAction/getLastVersion", {
				data: {},
				success: function(oRe) {
					if(oRe.all_version && cache.checkVersion(oRe.all_version, plus.runtime.version)) {
						if(opt && opt.success) {
							opt.success();
						}
						if(mui.os.android) {
							DownlaodUpdate(oRe.all_url);
						} else if(mui.os.ios) {
							plus.runtime.openURL(oRe.all_url);
						}
						return;
					} else if(oRe.min_version && cache.checkVersion(oRe.min_version, wgtinfo.version)) {
						if(opt && opt.success) {
							opt.success();
						}
						DownlaodUpdate(oRe.min_url);
						return;
					}
					if(opt && opt.error) {
						opt.error();
					}
				},
				error: function(data) {
					if(opt && opt.error) {
						opt.error();
					}
				}
			});
		});
	};

	cache.InstallUpdate = function(wgtPath, opt) {
		opt = opt || {};
		if(wgtPath.indexOf('_') != 0) {
			wgtPath = plus.io.convertAbsoluteFileSystem(wgtPath);
		}
		plus.runtime.install(wgtPath, {
			force: true
		}, function() {
			if(opt.success) {
				opt.success();
			}
		}, function(e) {
			console.log(JSON.stringify(e));
			if(opt.error) {
				opt.error();
			}
		});
	};

	/*常用函数封装 end*/

	cache.btn = function(e, fun) {
		var uLi = event.currentTarget;
		if(uLi) {
			if(uLi.affix_tap_count) {
				return;
			}
			uLi.affix_tap_count = true;
			uLi.classList.add('hxx-active');
			setTimeout(function() {
				uLi.classList.remove('hxx-active');
			}, 50);
			setTimeout(function() {
				uLi.affix_tap_count = false;
			}, 350);
		}
		setTimeout(fun, 100);
	};

	/*时间转换函数汇总 start*/
	cache.GetDateDiff = function(sTimestr) {
		var sTime = new Date(sTimestr);
		var eTime = new Date();
		var divNum = divNum = 1000 * 3600 * 24;
		var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
		if(diff == 0) {
			divNum = 1000 * 3600;
			var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
			if(diff == 0) {
				divNum = 1000 * 60;
				var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
				if(diff == 0) {
					divNum = 1000 * 60;
					var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
					if(diff == 0) {
						return '刚刚';
					} else {
						return diff + '秒前';
					}
				} else {
					return diff + '分钟前';
				}
			} else {
				return diff + '小时前';
			}
		} else {
			return diff + '天前';
		}
	};

	cache.dateDescribe = function(a) {
		var c = cache.timeStamp();
		var h = 0,
			m = 0,
			y = 0,
			ms = 0;
		if(typeof(a) === "string") {
			try {
				ms = cache.dateStamp2int(a);
			} catch(e) {
				return '错误的时间格式';
			}
		} else if(!cache.isTimestamp(a)) {
			return '错误的时间格式';
		} else {
			ms = a;
		}

		c = parseInt((c - ms) / 1000 / 60);
		if(c < 0) {
			return '您提供的时间早于当前时间';
		}
		h = parseInt(c / 60);
		d = parseInt(h / 24);
		m = parseInt(d / 30);
		y = parseInt(d / 365);
		c = c % 60;
		h = h % 24;
		d = d % 365;
		if(y > 0) {
			return cache.dateStamp2string(ms);
		} else if(m > 0) {
			return cache.dateStamp2stringNoYear(ms);
		} else if(d > 0) {
			return d + ' 天 ' + h + ' 小时前';
		} else if(h > 0) {
			return h + ' 小时前';
		} else if(c > 5) {
			return c + '分钟前';
		}
		return '刚刚';
	};

	cache.GetDateDescribe = function(sTimestr) {
		var self = this;
		var sTime = new Date(sTimestr);
		var eTime = new Date();
		if(sTime.getDate() == eTime.getDate()) {
			return '今天' + self.hour2str(sTime);
		}
		var iTime = self.str2timeStamp(self.date2str()) - sTime.getTime();
		iTime = parseInt(iTime / 1000 / 3600 / 24) + 1;
		if(iTime <= 0) {
			return -iTime + '天后' + self.hour2str(sTime);
		}
		if(iTime == 1) {
			return '昨天' + self.hour2str(sTime);
		}
		return iTime + '天前' + self.hour2str(sTime);
	};

	cache.getDateToEnd = function(sTimeStr) {
		sTimeStr = sTimeStr + ':00';
		var sTime = new Date(sTimeStr.replace(/-/g, '/'));
		var eTime = new Date();
		var diff = parseInt(sTime.getTime()) - parseInt(eTime.getTime());
		if(diff <= 0) {
			return '已到期';
		} else {
			var divNum = divNum = 1000 * 3600 * 24;
			var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
			if(diff == 0) {
				divNum = 1000 * 3600;
				var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
				if(diff == 0) {
					divNum = 1000 * 60;
					var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
					if(diff == 0) {
						divNum = 1000 * 60;
						var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
						if(diff == 0) {
							return '已到期';
						} else {
							return diff + '秒';
						}
					} else {
						return diff + '分钟';
					}
				} else {
					return diff + '小时';
				}
			} else {
				return diff + '天';
			}
		}
	};
	cache.date = function(d) {
		if(this.isTimestamp(d)) {
			d = parseInt(d);
			return new Date(d);
		}
		return this.str2date(d);
	};

	cache.timeStamp = function(d) {
		var oTime = new Date();
		return oTime.valueOf();
	};

	cache.initYearPicker = function() {
		var years = new Array();
		for(var i = 2016; i < 2020; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			years.push(year);
		}
		return years;
	};

	//初始化日的数据
	cache.initDayPicker = function() {
		var years = new Array();
		for(var i = 2016; i < 2020; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			year.children = new Array();
			for(var a = 1; a < 13; a++) {
				var month = {};
				month.text = a;
				month.value = a;
				month.children = new Array();
				var s_date, c;
				for(var b = 1; b < 29; b++) {
					var day = {};
					day.text = b;
					day.value = b;
					month.children.push(day);
					s_date = i + '-' + a + '-' + b;
					c = b;
				}
				var d_date = this.str2date(s_date);
				c++;
				d_date = this.nextDay(d_date);
				if(d_date.getMonth() + 1 == a) {
					var day = {};
					day.text = c;
					day.value = c;
					month.children.push(day);
				}
				c++;
				d_date = this.nextDay(d_date);
				if(d_date.getMonth() + 1 == a) {
					var day = {};
					day.text = c;
					day.value = c;
					month.children.push(day);
				}
				c++;
				d_date = this.nextDay(d_date);
				if(d_date.getMonth() + 1 == a) {
					var day = {};
					day.text = c;
					day.value = c;
					month.children.push(day);
				}
				year.children.push(month);
			}
			years.push(year);
		}
		return years;
	};

	//初始化月的数据
	cache.initMonthPicker = function() {
		var years = new Array();
		for(var i = 2016; i < 2020; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			year.children = new Array();
			for(var a = 1; a < 13; a++) {
				var month = {};
				month.text = a;
				month.value = a;
				year.children.push(month);
			}
			years.push(year);
		}
		return years;
	};

	//获取初始化周的数据
	cache.initWeekPicker = function() {
		var years = new Array();
		var iCurrentYear = cache.date().getFullYear();
		years = cache._get('initWeekPicker_' + iCurrentYear);
		if(!cache.isNull(years)) {
			return years;
		}
		years = [];
		for(var i = iCurrentYear - 2; i <= iCurrentYear + 2; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			year.children = new Array();
			var d = this.str2date(i + "-01-01");
			while(d.getDay() != 1) {
				d = cache.nextDay(d);
			}
			for(var x = 1; d.getFullYear() <= i; x++) {
				var week = {};
				var day = {};
				week.value = this.date2str(d);
				week.text = '第' + x + '周   ' + this.week2date(d);
				week.children = new Array();
				day.text = this.week2date(d);
				day.value = this.date2str(d);
				week.children.push(day);
				year.children.push(week);
				d = this.nextWeek(d);
			}
			years.push(year);
		}
		cache._set('initWeekPicker_' + iCurrentYear, years);
		return years;
	};

	cache.nextWeekFirstDay = function(d, w) {
		var s = (8 - d.getDay()) % 7;
		return new Date(d.valueOf() + s * 24 * 60 * 60 * 1000 * (w ? w : 1));
	};

	cache.lastWeekFirstDay = function(d, w) {
		if(!d) {
			d = new Date();
		}
		var s = (6 + d.getDay()) % 7;
		return new Date(d.valueOf() - s * 24 * 60 * 60 * 1000 * (w ? w : 1));
	};

	cache.endWeekFirstDay = function(d, w) {
		return new Date(d.valueOf() + 6 * 24 * 60 * 60 * 1000 * (w ? w : 1));
	};

	cache.nextWeek = function(d, w) {
		return new Date(d.valueOf() + 7 * 24 * 60 * 60 * 1000 * (w ? w : 1));
	};

	cache.nextDay = function(d, r) {
		return new Date(d.valueOf() + 24 * 60 * 60 * 1000 * (r ? r : 1));
	};

	cache.week2date = function(d) {
		var d1 = this.endWeekFirstDay(d);
		return this.date2str1(d) + '-' + this.date2str1(d1);
	};

	cache.date2str1 = function(d) {
		var m = d.getMonth() + 1;
		var r = d.getDate();
		var s = m + '.' + r;
		return s;
	};

	cache.str2date = function(s) {
		if(s) {
			s = s.replace(/-/g, "/");
			return new Date(s);
		} else {
			return new Date();
		}

	};

	cache.str2timeStamp = function(s) {
		s = s.replace(/-/g, "/");
		return new Date(s).valueOf();
	};

	cache.int2date = function(m) {
		return(m < 10 ? '0' + m : m);
	};

	cache.time2str = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var M = d.getMonth() + 1;
		var r = d.getDate();
		var h = d.getHours();
		var m = d.getMinutes();
		var s = d.getSeconds();
		var str = y + '-' + (M < 10 ? '0' + M : M) + '-' + (r < 10 ? '0' + r : r) + ' ' + (h < 10 ? '0' + h : h) + ':' + (m < 10 ? '0' + m : m) + ':' + (s < 10 ? '0' + s : s);
		return str;
	};
	cache.hour2str = function(d) {
		if(!d) {
			d = new Date();
		}
		var h = d.getHours();
		var m = d.getMinutes();
		var str = (h < 10 ? '0' + h : h) + ':' + (m < 10 ? '0' + m : m);
		return str;
	};

	cache.time2str1 = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var M = d.getMonth() + 1;
		var r = d.getDate();
		var h = d.getHours();
		var m = d.getMinutes();
		var s = d.getSeconds();
		var str = y + '-' + (M < 10 ? '0' + M : M) + '-' + (r < 10 ? '0' + r : r) + (h < 10 ? '0' + h : h) + ':' + (m < 10 ? '0' + m : m) + ':' + (s < 10 ? '0' + s : s);
		return str;
	};

	cache.date2str = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var r = d.getDate();
		var s = y + '-' + (m < 10 ? '0' + m : m) + '-' + (r < 10 ? '0' + r : r);
		return s;
	};

	cache.getDate = function(d) {
		var currentYear = (new Date()).getFullYear();
		if(!d) {
			d = new Date();
		} else {
			d = cache.date(d);
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var r = d.getDate();
		if(currentYear == y) {
			var s = (m < 10 ? '0' + m : m) + '-' + (r < 10 ? '0' + r : r);
		} else {
			var s = y + '-' + (m < 10 ? '0' + m : m) + '-' + (r < 10 ? '0' + r : r);
		}
		return s;
	}

	cache.getWeek = function(d) {
		switch(cache.date(d).getDay()) {
			case 0:
				return '星期日';
				break;
			case 1:
				return '星期一';
				break;
			case 2:
				return '星期二';
				break;
			case 3:
				return '星期三';
				break;
			case 4:
				return '星期四';
				break;
			case 5:
				return '星期五';
				break;
			case 6:
				return '星期六';
				break;
			default:
				return '';
				break;
		}
	};

	cache.date2string = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var r = d.getDate();
		return y + '年' + m + '月' + r + '日';
	};

	cache.date2StrNoYear = function(d) {
		if(!d) {
			d = new Date();
		}
		var m = d.getMonth() + 1;
		var r = d.getDate();
		var s = (m < 10 ? '0' + m : m) + '-' + (r < 10 ? '0' + r : r);
		return s;
	};
	cache.date2StringNoYear = function(d) {
		if(!d) {
			d = new Date();
		}
		var m = d.getMonth() + 1;
		var r = d.getDate();
		var s = m + '月' + r + '日';
		return s;
	};

	cache.month2str = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var s = y + '-' + (m < 10 ? '0' + m : m) + '-01';
		return s;
	};

	cache.month2string = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var s = y + '年' + (m < 10 ? '0' + m : m) + '月';
		return s;
	};

	cache.year2str = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var s = y + '-01-01';
		return s;
	};

	cache.year2string = function(d) {
		if(!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var s = y + '年';
		return s;
	};

	cache.week2string = function(s) {
		var tToday = this.str2date(s);
		var sYear = tToday.getFullYear();
		var tYearFirstDay = this.str2date(sYear + '-01-01');
		var tday = this.nextWeekFirstDay(tYearFirstDay);
		for(var i = 0; tday.getFullYear() == sYear; i++) {
			if(s == this.date2str(tday)) {
				return sYear + '年 第' + i + '周   ' + this.week2date(tday)
			}
			tday = this.nextWeek(tday);
		}
		return s;
	};

	cache.timeStamp2str = function(d) {
		return this.time2str(this.date(d));
	};

	cache.hourStamp2str = function(d) {
		return this.hour2str(this.date(d));
	}

	cache.dateStamp2str = function(d) {
		return this.date2str(this.date(d));
	};
	cache.dateStamp2string = function(d) {
		return this.date2string(this.date(d));
	};

	cache.dateStamp2strNoYear = function(d) {
		return this.date2StrNoYear(this.date(d));
	};
	cache.dateStamp2stringNoYear = function(d) {
		return this.date2StringNoYear(this.date(d));
	};

	cache.dateStamp2int = function(d) {
		return this.str2timeStamp(d);
	};

	cache.currentTimeStamp = function() {
		return(new Date()).valueOf();
	}

	cache.str2int = function(d) {
		return Date.parse(d);
	};

	cache.copy = function(e) {
		var obj = new Object();
		for(var k in e) {
			obj[k] = e[k];
		}
		return obj;
	};

	cache.getShareSerivces = function() {
		var shares = {};
		plus.share.getServices(function(s) {
			for(var i in s) {
				var t = s[i];
				shares[t.id] = t;
			}
		}, function(e) {
			console.log("获取分享服务列表失败：" + e.message);
		});
		return shares;
	};

	//分享到微信
	cache.shareShow = function(shares, content, flag) {
		var shareBts = [];
		// 更新分享列表
		var ss = shares['weixin'];
		ss && ss.nativeClient && (shareBts.push({
				title: '微信朋友圈',
				s: ss,
				x: 'WXSceneTimeline'
			}),
			shareBts.push({
				title: '微信好友',
				s: ss,
				x: 'WXSceneSession'
			}));
		// 弹出分享列表
		shareBts.length > 0 ? plus.nativeUI.actionSheet({
			title: '分享',
			cancel: '取消',
			buttons: shareBts
		}, function(e) {
			(e.index > 0) && cache.shareAction(shareBts[e.index - 1], content, flag);
		}) : plus.nativeUI.alert('当前环境无法支持分享操作!');
	};

	//授权
	cache.shareAction = function(sb, as, bh) {
		if(!sb || !sb.s) {
			console.log("无效的分享服务！");
			return;
		}
		var msg = {
			content: as.content,
			extra: {
				scene: sb.x
			}
		};
		if(bh) {
			if(as.address != "") {
				msg.href = as.address;
			}
			if(as.title != "") {
				msg.title = as.title;
			}
			msg.thumbs = ["_www/resource/logo.png"];
		} else {
			if(as.realUrl) {
				msg.pictures = [as.realUrl];
			}
		}
		// 发送分享
		if(sb.s.authenticated) {
			console.log("---已授权---");
			cache.shareMessage(msg, sb.s);
		} else {
			console.log("---未授权---");
			sb.s.authorize(function() {
				cache.shareMessage(msg, sb.s);
			}, function(e) {
				console.log("认证授权失败：" + e.code + " - " + e.message);
			});
		}
	};

	//分享发送
	cache.shareMessage = function(msg, s) {
		s.send(msg, function() {
			console.log("分享到\"" + s.description + "\"成功！");
		}, function(e) {
			console.log("分享到\"" + s.description + "\"失败: " + JSON.stringify(e));
		});
	};

	//分享的链接地址
	cache.shareUrl = function(e) {
		var code_name = encodeURIComponent(e.code_name);
		var surl = "wechatclient/news/009018/detail.html";
		var url = config.url + surl + "?schoolId=" + config.schoolid + "&id=" +
			e.id + "&news_code=" + e.news_code + "&news_group=" +
			e.news_group + "&code_name=" + code_name;
		return url;
	};

	/*时间转换函数汇总 end*/
})(mui, window.cache = {});

function downloadVedio(item) {
	if(item.file_type == '020010') {
		cache.downloadFile(item.file_url, {
			success: function(path) {
				vm.$set(item, 'size', 100);
				vm.$set(item, 'file_path', path);
			},
			download: function(e) {
				var d = Math.floor(e.downloadedSize / e.totalSize * 100);
				if(e.downloadedSize == 0 || d > 99) {

				} else {
					vm.$set(item, 'size', '已下载：' + d + '%');

				}
			},
			error: function(err) {
				vm.$set(item, 'size', '下载失败');
			}
		});
	}
};