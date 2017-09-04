/**
 * 缓存
 * varstion 1.0.0
 * by 黄晓旭
 * 
 */
(function($, cache) {

	cache.setSchoolId = function(v) {
		localStorage.setItem("school_id", v);
	};
	
	cache.getSchoolId = function() {
		return localStorage.getItem('school_id');
	};
	
	cache.setDefalutIframeSrc = function(v){
		this._set('defalut_iframe_src', v);
	};
	
	cache.getDefalutIframeSrc = function(v){
		return this._get('defalut_iframe_src');
	};
	
	cache.setSchoolName = function(v) {
		this._set('school_name', v);
	};
	
	cache.getSchoolName = function() {
		return this._get('school_name');
	};
	
	cache.myajax = function(URI, opt) {
		var self = this;
		var URL = config.url + URI;
		if (URI.indexOf("http://") === 0 || URI.indexOf("https://") === 0) {
			URL = URI;
		}
		if (!opt.timeout) opt.timeout = 10000;
		if (!opt.dataType) opt.dataType = 'json';
		if (!opt.type) opt.type = 'post';
		if (!opt.data.school_id) opt.data.school_id = self.getSchoolId();
		if (!opt.data.app_type) opt.data.app_type = self.getAppType();
		if (!opt.data.user_id) opt.data.user_id = self.getUserId();
		if (!opt.data.user_type) opt.data.user_type = self.getUserType();
		if (!opt.data.client_id) opt.data.client_id = self.getClientID();
		if (!opt.data.user_name) opt.data.user_name = self.getUserName();
		var k = md5(URL+JSON.stringify(opt.data));
		if (opt.cache) {
			opt.data.cache_version = self._get(k);
		}
		var fn = {
			error: function(XMLHttpRequest, textStatus, errorThrown) {},
			success: function(data, textStatus) {}
		};
		if (opt.error) fn.error = opt.error;
		if (opt.success) fn.success = opt.success;

		//扩展增强处理  
		opt.error = function(xhr, type, errorThrown) {
			console.log(URL+'?'+opt.data);
			switch (type){
				case 'abort':
					mui.toast('请求中断，请检查网络');
					break;
				case 'timeout':
					mui.toast('请求超时，请稍后再试');
					break;
				default:
					break;
			}
			fn.error();
			return;
		};

		opt.success = function(data, textStatus, xhr) {
//			console.log(URL+'?'+opt.data);
//			console.log(JSON.stringify(data));
			if (data) {
				if (data.code == 1) {
					cache._set(k, data.cache_version);
					fn.success(data);
					return;
				} else if (data.code == 0) {
					if (opt.tip) {
						fn.error(data);
						return;
					}else{
						if (data.msg) {
							mui.toast(data.msg);
							fn.error(data);
							return;
						} 
					}
				}else if (data.code == 100) {
					fn.error(data);
					return;
				}
				fn.error(data);
				return;
			} else {
				fn.error();
				return;
			}
		};
		mui.ajax(URL, opt);
	};
	
	//ajax升级
		cache.ajax = function(URI, opt) {
		var self = this;
		var URL = config.url + URI;
		if (URI.indexOf("http://") === 0 || URI.indexOf("https://") === 0) {
			URL = URI;
		}
		if (!opt.timeout) opt.timeout = 10000;
		if (!opt.dataType) opt.dataType = 'json';
		if (!opt.type) opt.type = 'post';
		if (!opt.data.school_id) opt.data.school_id = self.getSchoolId();
		if (!opt.data.app_type) opt.data.app_type = self.getAppType();
		if (!opt.data.user_id) opt.data.user_id = self.getUserId();
		if (!opt.data.user_type) opt.data.user_type = self.getUserType();
		if (!opt.data.client_id) opt.data.client_id = self.getClientID();
		if (!opt.data.user_name) opt.data.user_name = self.getUserName();
		var k = md5(URL+JSON.stringify(opt.data));
		if (opt.cache) {
			opt.data.cache_version = self._get(k);
		}
		var fn = {
			error: function(XMLHttpRequest, textStatus, errorThrown) {},
			success: function(data, textStatus) {}
		};
		if (opt.error) fn.error = opt.error;
		if (opt.success) fn.success = opt.success;

		//扩展增强处理  
		opt.error = function(xhr, type, errorThrown) {
			console.log(URL+'?'+opt.data);
			switch (type){
				case 'abort':
					mui.toast('请求中断，请检查网络');
					break;
				case 'timeout':
					mui.toast('请求超时，请稍后再试');
					break;
				default:
					break;
			}
			return;
		};

		opt.success = function(data, textStatus, xhr) {
			console.log(URL+'?'+opt.data);
			console.log(JSON.stringify(data));
			if (data) {
				if (data.code == 1) {
					cache._set(k, data.cache_version);
					if (data.result) {
						if (data.result.data) {
							fn.success(data.result.data);
							return;
						}
					}
					fn.error(data);
					return;
				} else if (data.code == 0) {
					if (opt.tip) {
						fn.error(data);
						return;
					}else{
						if (data.msg) {
							mui.toast(data.msg);
							fn.error(data);
							return;
						} 
					}
				}else if (data.code == 100) {
					fn.error(data);
					return;
				}
				fn.error(data);
				return;
			} else {
				fn.error();
				return;
			}
		};
		mui.ajax(URL, opt);
	};
	
	//获取分页的数据
	cache.getListFromNet = function(opt) {
		var self = this;
		//扩展增强处理  
		var fn = {
			error: function(data) {},
			success: function(err) {}
		};
		if (opt.error) fn.error = opt.error;
		if (opt.success) fn.success = opt.success;
		opt.error = function(err) {
			fn.error();
		};

		opt.success = function(data) {
			var aRe = data.result.data;
			if (opt.start_id != '' && opt.start_id != 0){
				var aCache = self._get(opt.key);
				if (opt.direction ==  1) {
					for (var i = aRe.length-1; i >= 0; i--) {
						aCache.unshift(aRe[i]);
					}
				}else{
					for (var i = 0; i < aRe.length; i++) {
						aCache.push(aRe[i]);
					}
				}
				self._set(opt.key,aCache);
			}else{
				self._set(opt.key,aRe);
			}
			fn.success(aRe);
		};
		self.myajax(opt.interface, opt);
	};
	
	//上传文件
	cache.uploadFile = function(url, opt, bool) {
		var self = this;
		if (url) {
			url = config.fileURL + url;
		}else{
			return;
		}
		
		bool = bool || false;
		if (!opt.timeout) opt.timeout = 20000;
		if (!opt.dataType) opt.dataType = 'json';
		if (!opt.type) opt.type = 'post';

		var data = opt.data;
		if (!data.module_code) data.module_code = '009001';
		url += '?module_code=';
		url += data.module_code;
		if (!data.school_id) data.school_id = self.getSchoolId();
		if (!data.app_type) data.app_type = self.getAppType();
		if (!data.user_id) data.user_id = self.getUserId();
		if (!data.user_type) data.user_type = self.getUserType();
		if (!data.user_name) data.user_name = self.getUserName();
		if (!data.filekey) data.filekey = 'file';
		if (!data.filePath) {
			mui.toast('上传图片路径为空');
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
		var task = plus.uploader.createUpload(url, {
				method: opt.type
			},
			function(t, status) {
				if (status == 200) {
					var ds = JSON.parse(t.responseText);
					if (ds.code == 1) {
						opt.success(ds.result.data);
					} else {
						opt.error(ds);
					}
				} else {
					opt.error(ds);
				}
			}
		);
		if (mui.isArray(data.filePath)) {
			for (var x in data.filePath) {
				task.addFile(data.filePath[x], {
					key: x
				});
			}
		}else{
			opt.error('图片路径必须为数据');
			return;
		}
		for (var k in data) {
			task.addData(k, data[k]);
		}
		task.addEventListener('statechanged', function(u, s) {
			opt.upload(u);
		});
		task.start();
	};
	
	cache.downloadFile = function(url, opt) {
		var self = this;
		if (!url) {
			if (opt.error) {
				opt.error();
			}
			console.log("erro:downloadFile- 下载地址为空");
			return;
		}
		var fileName = md5(url);
		var sCacheFilePath = self.GetFilePathByURL(url); 
		if (sCacheFilePath) {
			console.log('获取到缓存： ' + sCacheFilePath);
			opt.success(sCacheFilePath);
			return;
		}
		var theFile = '';
		if (opt.file) {
			theFile = opt.file
		}else{
			theFile = fileName + '.' + self.fileType(url);
		}
		var fname = '_downloads/images/' + theFile;
		if (!opt.timeout) opt.timeout = 10000;
		if (!opt.type) opt.type = 'post';
		if (!opt.error) {
			opt.error = function() {};
		};
		if (!opt.success) {
			opt.success = function() {};
		};
		if (!opt.download) {
			opt.download = function() {};
		};
		var dtask = plus.downloader.createDownload(url, {
			filename: fname
		}, function(d, status) {
			if (status == 200) {
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
	cache.compressImage = function(url,success){
		var dstURL = md5(url)+'.'+this.fileType(url);
		plus.zip.compressImage({
			src: url,
			dst: dstURL,
			overwrite: true,
			width: '1080px'
		},function(e){
			success(e.target);
		},function(e){
			success(url);
		});
	};
	
	//封装从相册获取图片
	cache.getImageFromGallery = function(success,error){
		if (plus) {
			plus.gallery.pick(function(e) {
				success(e.files);
			}, function(e) {
				console.log(e.message);
				error();
			}, {
				filter: "image",
				multiple: true
			});
		}else{
			console.log('plus 组建未初始化');
			error();
		}
		
	};
	//封装从相机获取图片
	cache.getImageFromCamera = function(success,error){
		var self = this;
		if (plus) {
			plus.camera.getCamera().captureImage(function(e) {
				success(e);
			},function(e){
				console.log(e.message);
				error();
			});
		}else{
			console.log('plus 组建未初始化');
			error();
		}
	};
	//初始化录音对象，每个页面都要重新执行
	cache.initAudioRecorder = function(){
		var self = this;
		if (self.AudioRecorder) {
			return true;
		}else{
			if (plus) {
				self.AudioRecorder = plus.audio.getRecorder();
				return true;
			}else{
				document.addEventListener('plusready',function(){
					self.AudioRecorder = plus.audio.getRecorder();
				});
				return false;
			}
		}
	};
	//开始录音
	cache.startRecord = function(success,error){
		var self = this;
		if (!self.AudioRecorder) {
			return;
		}
		self.AudioTime = self.currentTimeStamp();
		self.AudioRecorder.record({
			filename: '_doc/audio/',
			format: 'amr'
		},function(e){
			self.AudioTime = self.currentTimeStamp() - self.AudioTime;
			self.AudioTime = Math.floor(self.AudioTime/1000);
			if (self.AudioTime<= 0) {
				if (error) {
					error('录音时间过短');
				}
			}else{
				if (success) {
					success({
						sPath: e,
						iTime: self.AudioTime
					});
				}
			}
		},function(e){
			console.log(JSON.stringify(e));
			if (error) {
				error(e.message);
			}
		});
	};
	//结束录音
	cache.stopRecord = function(){
		var self = this;
		if (!self.AudioRecorder) {
			console.log('录音控件未初始化');
			return;
		}else{
			self.AudioRecorder.stop();
		}
	};
	//播放录音
	cache.startPlayRecord = function(e,u,end){
		var self = this;
		if (self.AudioPlayer) {
			self.uAudioPlayer.classList.remove('active');
			self.AudioPlayer.stop();
		}
		if (e) {
			if (e.indexOf('_') != 0) {
				e= plus.io.convertAbsoluteFileSystem(e);
				self.AudioPlayer = plus.audio.createPlayer(e);
				self.uAudioPlayer = u;
				self.uAudioPlayer.classList.add('active');
				self.AudioPlayer.play(function(e){
					end();
				},function(e){
					end();
					console.log(JSON.stringify(e));
				});
			}else{
				self.AudioPlayer = plus.audio.createPlayer(e);
				self.uAudioPlayer = u;
				self.uAudioPlayer.classList.add('active');
				self.AudioPlayer.play(function(e){
					end();
				},function(e){
					end();
					console.log(JSON.stringify(e));
				});
			}
		}
	};
	//停止播放录音
	cache.stopPlayRecord = function(){
		var self = this;
		if (self.AudioPlayer&&self.uAudioPlayer) {
			self.uAudioPlayer.disable = false;
			self.uAudioPlayer.classList.remove('active');
			self.AudioPlayer.stop();
		}
	};
	
	cache.SetFilePathByURL = function(k, v) {
		try {
			var self = this;
			self._set('image_path_' + md5(k), v);
		} catch (e) {
			console.log('erro: SetImagePathByURL' + e);
		}
	};
	
	cache.GetFilePathByURL = function(k) {
		try {
			var self = this;
			var sImagePath = self._get('image_path_' + md5(k));
			return sImagePath;
		} catch (e) {
			console.log('erro: GetImagePathByURL' + e);
		}
	};
	
	/*与缓存打交道 start*/
	cache.setCacheFile = function(k,v){
		this._set(md5('filecache'+k), v);
	};
	
	cache.getCacheFile = function(k){
		return this._get(md5('filecache'+k));
	};
	
	cache.setAppType = function(v) {
		this._set('app_type', v);
	};
	
	cache.getAppType = function() {
		return this._get('app_type');
	};

	cache.setUserSex = function(v) {
		this._set('user_sex', v);
	};
	
	cache.getUserSex = function() {
		return this._get('user_sex') == 0 ? '男' : '女';
	};
	
	//用户密码
	cache.setUserPass = function(v) {
		this._set('user_pass_word', v);
	};
	
	//获取用户密码
	cache.getUserPass = function() {
		return this._get('user_pass_word');
	};
	
	//用户性别
	cache.getUserSexCode = function() {
		return this._get('user_sex');
	};
	
	//用户头像
	cache.setHeadUrl = function(v) {
		this._set('head_url', v);
	};
	
	//获取用户头像
	cache.getHeadUrl = function() {
		return this._get('head_url');
	};
	
	//用户本地头像
	cache.setHeadPath = function(v) {
		this._set('head_path', v);
	};
	
	//获取用户本地头像
	cache.getHeadPath = function() {
		return this._get('head_path');
	};
	
	//用户手机号
	cache.setPhone = function(v) {
		this._set('user_phone', v);
	};
	
	//获取用户手机号
	cache.getPhone = function() {
		return this._get('user_phone');
	};
	
	//用户类型
	cache.setUserType = function(v) {
		this._set('user_type', v);
	};
	
	//设置用户类型
	cache.getUserType = function() {
		return this._get('user_type');
	};
	
	//用户名字
	cache.setUserName = function(v) {
		this._set('user_name', v);
	};
	
	//设置用户名字
	cache.getUserName = function() {
		return this._get('user_name');
	};
	
	//app唯一识别码
	cache.getClientID = function() {
		return this._get('client_id');
	};
	
	cache.setClientID = function(v) {
		var clientInfo = plus.push.getClientInfo();
		this._set('client_id', clientInfo.clientid);
	};
	//用户id
	cache.setUserId = function(v) {
		this._set('user_id', v);
	};
	
	cache.getUserId = function() {
		return this._get('user_id');
	};
	//用户职务
	cache.getTeam = function() {
		return this._get('teams')[0];
	};
	
	cache.deleTeam = function(id) {
		var teams = this._get('teams');
		for (var i = 0; i < teams.length; i++) {
			if (teams[i].teacher_id == id) {
				teams.splice(i, 1);
				this._set('teams', teams);
				if (teams.length<=0) {
					this.insertTeam({});
				}
				return;
			}
		}
	};
	
	cache.getTeams = function() {
		var aTeams = this._get('teams');
		if (this.isArray(aTeams)) {
			return aTeams;
		}
		return [];
	};
	
	cache.hasTeams = function() {
		var aTeams = this.getTeams();
		if (aTeams.length<=0) {
			return 0;
		}
		if (this.isNull(this.getUserPass())) {
			return 1;
		}
		for (var i = 0; i < aTeams.length; i++) {
			if (cache.isNull(cache.dictCode2Value(aTeams[i].duty))) {
				return 1;
			}
			if (aTeams[i].is_confirm != 1) {
				return 1;
			}
		}
		return 2;
	};
	
	cache.insertTeam = function(v) {
		var teams = this._get('teams');
		var team = {};
		team.teacher_id = self.valueOf(v[i].teacher_id);
		team.class_id = self.valueOf(v[i].class_id);
		team.class_name = self.valueOf(v[i].class_name);
		team.course = self.valueOf(v[i].course);
		team.duty = self.valueOf(v[i].duty);
		if (self.isNull(v[i].duty_name)) {
			team.duty_name = self.dictCode2Value(v[i].duty);
		}else{
			team.duty_name = self.valueOf(v[i].duty_name);
		}
		team.grade_id = self.valueOf(v[i].grade_id);
		team.grade_name = self.valueOf(v[i].grade_name);
		team.is_charge = self.valueOf(v[i].is_charge);
		team.is_confirm = self.valueOf(v[i].is_confirm);
		team.sex = self.valueOf(v[i].sex);
		team.version = self.valueOf(v[i].version);
		team.contact_id = self.valueOf(v[i].contact_id);
		team.team_type = self.valueOf(v[i].team_type);
		for (var i = 0; i < teams.length; i++) {
			if (teams[i].teacher_id == team.teacher_id) {
				teams[i] = team;
				this._set('teams', teams);
				return;
			}
		}
		teams.unshift(team);
		this._set('teams', teams);
	};
	
	cache.setTeams = function(v) {
		var self = this;
		try {
			var teams = [];
			if (self.isArray(v) && v.length > 0) {
				for (var i = 0; i < v.length; i++) {
					var team = {};
					team.teacher_id = self.valueOf(v[i].teacher_id);
					team.class_id = self.valueOf(v[i].class_id);
					team.class_name = self.valueOf(v[i].class_name);
					team.course = self.valueOf(v[i].course);
					team.duty = self.valueOf(v[i].duty);
					if (self.isNull(v[i].duty_name)) {
						team.duty_name = self.dictCode2Value(v[i].duty);
					}else{
						team.duty_name = self.valueOf(v[i].duty_name);
					}
					team.grade_id = self.valueOf(v[i].grade_id);
					team.grade_name = self.valueOf(v[i].grade_name);
					team.is_charge = self.valueOf(v[i].is_charge);
					team.is_confirm = self.valueOf(v[i].is_confirm);
					team.sex = self.valueOf(v[i].sex);
					team.version = self.valueOf(v[i].version);
					team.contact_id = self.valueOf(v[i].contact_id);
					team.team_type = self.valueOf(v[i].team_type);
					teams.push(team);
				}
			} else {
				console.log('erro: setTeams-保存数据为空');
			}
			this._set('teams', teams);
		} catch (e) {
			console.log('erro: setTeams-' + e);
		}
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
	
	cache.getChilds = function() {
		var aChilds = this._get('childs');
		if (this.isArray(aChilds)) {
			return aChilds;
		}
		return [];
	};
	
	cache.hasChilds = function() {
		var aChilds = this._get('childs');
		if (aChilds.length<=0) {
			return 1;
		}
		if (this.isNull(this.getUserPass())) {
			return 1;
		}
		return 2;
	};
	
	cache.deleChild = function(id) {
		var childs = this._get('childs');
		for (var i = 0; i < childs.length; i++) {
			if (childs[i].parent_id == id) {
				childs.splice(i, 1);
				this._set('childs', childs);
				return;
			}
		}
	};
	
	cache.insertChild = function(v) {
		var childs = this._get('childs');
		var child = {};
		child.parent_id = this.valueOf(v.parent_id);
		child.class_id = this.valueOf(v.class_id);
		child.class_name = this.valueOf(v.class_name);
		child.grade_id = this.valueOf(v.grade_id);
		child.grade_name = this.valueOf(v.grade_name);
		child.head_url = this.valueOf(v.head_url);
		child.relation = this.valueOf(v.relation);
		child.student_id = this.valueOf(v.student_id);
		child.student_code = this.valueOf(v.student_code);
		child.student_name = this.valueOf(v.student_name);
		child.student_sex = this.valueOf(v.student_sex);
		child.version = this.valueOf(v.version);
		child.info = child.student_name+' - '+child.class_name+' - '+child.student_code;
		for (var i = 0; i < childs.length; i++) {
			if (childs[i].parent_id == child.parent_id) {
				childs[i] = child;
				this._set('childs', childs);
				return;
			}
		}
		childs.unshift(child);
		this._set('childs', childs);
	};
	
	cache.setChilds = function(v) {
		try {
			var childs = [];
			if (this.isArray(v) || v.length > 0) {
				for (var i = 0; i < v.length; i++) {
					var child = {};
					child.parent_id = this.valueOf(v[i].parent_id);
					child.class_id = this.valueOf(v[i].class_id);
					child.class_name = this.valueOf(v[i].class_name);
					child.grade_id = this.valueOf(v[i].grade_id);
					child.grade_name = this.valueOf(v[i].grade_name);
					child.head_url = this.valueOf(v[i].head_url);
					child.relation = this.valueOf(v[i].relation);
					child.student_id = this.valueOf(v[i].student_id);
					child.student_code = this.valueOf(v[i].student_code);
					child.student_name = this.valueOf(v[i].student_name);
					child.student_sex = this.valueOf(v[i].student_sex);
					child.version = this.valueOf(v[i].version);
					childs.push(child);
				}
			} else {
				var child = {};
				child.parent_id = this.valueOf(v[i].parent_id);
				child.class_id = this.valueOf(v[i].class_id);
				child.class_name = this.valueOf(v[i].class_name);
				child.grade_id = this.valueOf(v[i].grade_id);
				child.grade_name = this.valueOf(v[i].grade_name);
				child.head_url = this.valueOf(v[i].head_url);
				child.relation = this.valueOf(v[i].relation);
				child.student_id = this.valueOf(v[i].student_id);
				child.student_code = this.valueOf(v[i].student_code);
				child.student_name = this.valueOf(v[i].student_name);
				child.student_sex = this.valueOf(v[i].student_sex);
				child.version = this.valueOf(v[i].version);
				childs.push(child);
			}
			this._set('childs', childs);
		} catch (e) {
			console.log('erro: setChilds-' + e);
		}
	};
	
	cache.getModule = function(e){
		try{
			var self = this;
			var aModules = self._get('modules');
			if (e) {
				var aXModules = [];
				for (var i = 0; i < aModules.length; i++) {
					if (aModules[i].user_type == e||aModules[i].user_type == '003') {
						aXModules.push(aModules[i]);
					}
				}
				return aXModules;
			}else{
				return aModules;
			}
			return null;
		}catch(e){
		}
	};
	
	//保存模块信息
	cache._setModules = function(v) {
		try {
			var self = this;
			return self._set('modules', v);
		} catch (e) {
			console.log('erro: Cache._setModules:' + e);
			return false;
		}
		return true;
	};
	
	//保存教师职务到数据库
	cache._setDutys = function(v) {
		try {
			var self = this;
			return self._set('dutys', v);
		} catch (e) {
			console.log('erro: Cache._setDutys:' + e);
			return false;
		}
		return true;
	};
	
	//保存用户信息到数据库
	cache.setUser = function(v) {
		var self = this;
		self.setUserId(v.user_id);
		self.setHeadUrl(v.head_url);
		self.setUserName(v.user_name);
		self.setPhone(v.phone);
		self.setHeadPath(v.head_url);
		self.setUserSex(v.sex);
		self.setUserPass(v.pass_word);
		try{
			var teacher_list = JSON.parse(v.teacher_list);
			cache.setTeams(teacher_list);
		}catch(e){
			console.log(e);
		}
		try{
			var parent_list = JSON.parse(v.parent_list);
			cache.setChilds(parent_list);
		}catch(e){
			console.log(e);
		}
	};
	
	//保存寝室汇总到数据库
	cache._setBedRoom = function(v) {
		try {
			var self = this;
			return self._set('bed_room', v);
		} catch (e) {
			console.log('erro: Cache._setDict:' + e);
			return false;
		}
		return true;
	};
	
	//保存教室汇总到数据库
	cache._setClassRoom = function(v) {
		try {
			var self = this;
			return self._set('class_room', v);
		} catch (e) {
			console.log('erro: Cache._setDict:' + e);
			return false;
		}
		return true;
	};
	
	//保存字典到数据库
	cache._setDict = function(v) {
		try {
			var self = this;
			return self._set('dict', v);
		} catch (e) {
			console.log('erro: Cache._setDict:' + e);
			return false;
		}
		return true;
	};
	
	//数据缓存-用户信息
	cache.getUser = function() {

	};
	
	//数据缓存-教室职务
	cache.getDutys = function() {
		var self = this;
		return self._get('dutys');;
	};
	
	
	
	//数据缓存-寝室汇总
	cache.getBedRoom = function() {
		var self = this;
		return self._get('bed_room');
	};
	
	//获取动态接口
	cache.GetInformation = function(opt){
		var self = this;
		//生成key
		opt.key = opt.interface+opt.data.user_type;
		if (opt.data.user_type == '003015') {
			opt.key += cache.getChild().student_id;
		}
		opt.start_id = opt.data.start_key.toString();
		opt.direction = opt.data.direction;
		//从缓存中查找数据
		if (opt.reset) {
			self._set(opt.key,[]);
		}
		var aCache = self._get(opt.key);
		if (cache.isArray(aCache)&&aCache.length>0){
			if (opt.start_id == ''||opt.start_id == '0') {
				if (aCache.length>100) {
					aCache.splice(99,aCache.length-100);
					self._set(opt.key,aCache);
				}
				opt.success(aCache);
				return;
				//页面初始化，在缓存中获取到了数据，马上返回
			}else{
				for (var i = 0; i < aCache.length; i++) {
					if (aCache[i].key == opt.start_id) {
						var aRe = [];
						if (opt.data.direction ==  1) {
							if (i == 0) {
								break;
							}
							for (var j = aCache.length-1; j > i; j--) {
							    aRe.push(aCache[j]);
							}
						}else{
							if (i+1 == aCache.length) {
								break;
							}
							for (var j = i; j <= 0; j--) {
							    aRe.push(aCache[j]);
							}
						}
						opt.success(aRe);
						return;
						//下拉刷新或者上拉加载，在缓存中发现数据，缓存中的数据全部返回（不判断是否够page_num）
					}
				}
			}
		}
		self.getListFromNet(opt);
		//function end
	};
	
	cache.GetModuleList = function(moduleCode,callBack){
		var aRe = cache.getModule(moduleCode);
		if (!cache.isArray(aRe)||aRe.length<=0) {
			cache.myajax("moduleAction/getModuleList", {
				data: {
					user_type : moduleCode
				},
				success: function(data) {
					var aRe = data.result.data;
					callBack(aRe);
				},
				error: function(){
					callBack();
				}
			});
		}else{
			callBack(aRe);
		}
		
	};
	
	cache.getResources = function(jsoninfo,success,erro){
		var self = this;
		try{
			var k = JSON.stringify(jsoninfo);
			var oCache = self._get(k);
			if (self.isNull(oCache)) {
				self.getResourcesFromNet({
					JsonInfo: jsoninfo,
					success: function(data) {
						self.setResourcesFromCache(k,data);
						success(data);
					},
					error: function(data){
						erro({
							code: 98,
							msg: JSON.stringify(data)
						});
					}
				});
				//if end
			}else{
				success(oCache);
				//else end
			}
		}catch(e){
			erro({
				code: 99,
				msg: '教学云资源获取失败，'+e
			});
		}
	};
	
	cache.setResourcesFromCache = function(k,v) {
		var self = this;
		self._set(k,v);
	};
	
	//数据缓存-作业数据缓存
	cache.setHomework = function(v) {
		var self = this;
		var k = 'Homework'+cache.getUserType();
		if (cache.getUserType() === '003015') {
			k+=self.getChild().student_id;
		}
		return self._set(k,v);
	};
	
	cache.getHomework = function() {
		var self = this;
		var k = 'Homework'+self.getUserType();
		if (cache.getUserType() === '003015') {
			k+=self.getChild().student_id;
		}
		return self._get(k);
	};
	
	//数据缓存-单个作业数据缓存
	cache.setOneHomework = function(v) {
		var self = this;
		var k = 'OneHomework'+cache.getUserType()+v.homework_id+v.receive_id;
		if (cache.getUserType() === '003015') {
			k+=self.getChild().student_id;
		}
		return self._set(k,v);
	};
	
	cache.getOneHomework = function(v) {
		var self = this;
		var k = 'OneHomework'+self.getUserType()+v.homework_id+v.receive_id;
		if (cache.getUserType() === '003015') {
			k+=self.getChild().student_id;
		}
		return self._get(k);
	};
	
	//数据缓存-寝室汇总
	cache.getClassRoom = function() {
		var self = this;
		return self._get('class_room');
	};
	
	//数据缓存-字典信息
	cache.getDict = function(v) {
		if (v) {
			var dicts = this._get('dict');
			var dict = new Array();
			for (var k in dicts) {
				if (dicts[k].dict_group == v) {
					dict.push(dicts[k]);
				}
			}
			return dict;
		} else {
			return this._get('dict');
		}
	};
	
	//getAutoLogin
	cache.getAutoLogin = function() {
		return this._get('auto_login');
	};
	
	cache.setAutoLogin = function(v) {
		this._set('auto_login', v);
	};
	
	//setValidateCode
	cache.getValidateCode = function() {
		return this._get('validate_code');
	};
	

	cache.setValidateCode = function(v) {
		this._set('validate_code', v);
	};
	

	
	//兴趣班内容缓存操作
	cache._setPlaygroup = function(v) {
		var self = this;
		var k = 'play_group_list';
		self._set(k,v);
	};
	
	cache.getPlaygroup = function() {
		var self = this;
		var k = 'play_group_list';
		return self._get(k);
	};
	
	cache.setAttendList = function(v){
		return this._set('attend_list',v);
	};
	
	cache.getAttendList = function(){
		return this._get('attend_list');
	};

	//从本地缓存中获取数据
	cache._get = function(k) {
		var self = this;
		try {
			if (self.isString(k)) {
				k += self.getSchoolId();
				var v = localStorage.getItem(k);
				if (self.isNull(v)) {
					//console.log('erro:Cache.get-' + k + ':数据为空');
					return '';
				} else {
					var t = v.substring(v.length - 1, v.length);
					if (t === 'a') {
						v = v.substring(0, v.length - 1);
						return v;
					} else if (t === 'b') {
						v = v.substring(0, v.length - 1);
						v = JSON.parse(v);
						return v;
					} else {
						//console.log('erro:Cache.get-' + k + ':数据被已被污染');
						return '';
					}
				}
			} else {
				//console.log('erro:Cache.get:key必须为字符串');
				return '';
			}
		} catch (e) {
			console.log('erro:cache.get:' + e);
			return '';
		}
	};
	
	//保存数据到本地缓存
	cache._set = function(k, v) {
		var self = this;
		try {
			if (self.isNull(k)) {
				console.log('erro:cache.set: 数据格式不可为null key: '+ k);
				return false;
			}
			k += self.getSchoolId();
			if (self.isString(v)) {
				v += 'a';
				localStorage.setItem(k, v);
			} else {
				v = JSON.stringify(v);
				v += 'b';
				localStorage.setItem(k, v);
			}
			return true;
		} catch (e) {
			console.log('erro:cache.set:  ' + e);
			return false;
		}
	};
	
	/*与缓存打交道 end*/
	
	/*与服务器打交道 start*/
	//bug反馈
	cache.addSuggestFromNet = function(t) {
		var self = this;
		var old_msg = self._get('addSuggest');
		if (old_msg == t) {
			return;
		}
		self._set('addSuggest',t);
		self.myajax('appAction/addSuggest', {
			data: {
				content: t
			}
		});
	};
	
	//数据缓存-字典信息
	cache.getResourcesFromNet = function(opt) {
		var self = this;
		if (!opt.timeout) opt.timeout = 10000;
		if (!opt.dataType) opt.dataType = 'json';
		if (!opt.type) opt.type = 'post';
		var fn = {
			error: function(XMLHttpRequest, textStatus, errorThrown) {},
			success: function(data, textStatus) {}
		};
		if (opt.error) fn.error = opt.error;
		if (opt.success) fn.success = opt.success;
		opt.error = function(xhr,type,errorThrown) {
//			console.log(JSON.stringify(opt.data));
			switch (type){
				case 'abort':
					mui.toast('网络异常');
					break;
				case 'timeout':
					mui.toast('请求超时');
					break;
				default:
					break;
			}
//			console.log(xhr.response);
			fn.error();
		};
		opt.success = function(data, textStatus, xhr) {
//			console.log(JSON.stringify(opt.data));
//			console.log(JSON.stringify(data));
			fn.success(data);
		};
		
		var dTime = new Date();		
		opt.JsonInfo.StreamingNo = md5(dTime.getTime());
		opt.JsonInfo.TimeStamp = cache.time2str(dTime);
		opt.JsonInfo.AppId = '4Ga3K8yT6X';
		opt.JsonInfo.Source = 'ShouJiDuan';
		opt.JsonInfo.Sign = md5('4Ga3K8yT6XShouJiDuan2JFsjysw7JSXkdMjscKdnhmP8ATjkrtK'+cache.date2str(dTime)+' 03:30:00');
		opt.data = {};
		opt.data.JsonInfo = JSON.stringify(opt.JsonInfo);
		mui.ajax('http://118.180.8.123:8081/ecp/resources_share', opt);
	};
	
	//获取缓存
	cache.getAllCache = function(callback){
		var self = this;
		self.IndexCache = 0;
		self.CountCache = 0;
		self.CountCache++;
		self.myajax('systemAction/getDictionary', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
				var r = data.result.data;
				var dicts = [];
				for (var i = 0; i < r.length; i++) {
					if (r[i].dict_code && r[i].dict_value && r[i].dict_group) {
						var item = {};
						item.value = r[i].dict_code;
						item.text = r[i].dict_value;
						item.dict_group = r[i].dict_group;
						item.other_field = r[i].other_field;
						dicts.push(item);
					}
				}
				self._setDict(dicts);
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			},
			error: function(err) {
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			}
		});
		self.CountCache++;
		self.myajax('moduleAction/getModuleList', {
			cache: true,
			timeout: 0,
			data: {
				user_type: '003'
			},
			success: function(data) {
				var r = data.result.data;
				var aModules = [];
				for (var i = 0; i < r.length; i++) {
					var oModuleItem = {};
					oModuleItem.is_news = r[i].is_news;
					oModuleItem.icon_url = r[i].icon_url;
					oModuleItem.module_name = r[i].module_name;
					oModuleItem.module_code = r[i].module_code;
					oModuleItem.user_type = r[i].user_type;
					oModuleItem.initdata = r[i].initdata;
					oModuleItem.parent_code = r[i].parent_code;
					oModuleItem.module_url = r[i].module_url;
					aModules.push(oModuleItem);
				}
				cache._setModules(aModules);
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			},
			error: function(err) {
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			}
		});
		self.CountCache++;
		self.myajax('gradeAction/getGradeAndClass', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
				var classrooms = [];
				var r = data.result.data;
				for (var a = 0; a < r.length; a++) {
					if (r[a].grade_name && r[a].grade_id && r[a].class_list) {
						var classes = JSON.parse(r[a].class_list);
						if (!cache.isArray(classes) || classes<=0) {
							continue;
						}
						var item = {};
						item.value = r[a].grade_id;
						item.text = r[a].grade_name;
						item.children = new Array();
						for (var i = 0; i < classes.length; i++) {
							var clas = {};
							clas.text = classes[i].class_name;
							clas.value = classes[i].class_id;
							clas.teamType = "011005";
							item.children.push(clas);
						}
						classrooms.push(item);
					}
				}
				self._setClassRoom(classrooms);
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			},
			error: function(err) {
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			}
		});
		self.CountCache++;
		self.myajax('bedroomAction/getBedroomList', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
				var bedrooms = [];
				var r = data.result.data;
				for (var i = 0; i < r.length; i++) {
					if (r[i].bedroom_id && r[i].bedroom_name && r[i].sex) {
						var item = {};
						item.value = r[i].bedroom_id;
						item.text = r[i].bedroom_name;
						item.sex = r[i].sex;
						bedrooms.push(item);
					}
				}
				self._setBedRoom(bedrooms);
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			},
			error: function(err) {
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			}
		});
		self.CountCache++;
		self.myajax('contactAction/getContactGroupListOfManager', {
			cache: true,
			timeout: 0,
			tip: true,
			data: {
				user_type: "003010"
			},
			success: function(data) {
				var aRe = data.result.data;
				var aPlaygroup = [];
				for (var i = 0; i < aRe.length; i++) {
					var obj = {};
					obj.value = aRe[i].contact_id;
					obj.text = aRe[i].contact_name;
					aPlaygroup.push(obj);
				}
				self._setPlaygroup(aPlaygroup);
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			},
			error: function(err) {
				self.IndexCache++;
				if (self.IndexCache == self.CountCache) {
					callback();
				}
			}
		});
		if (cache.getAutoLogin() == true) {
			self.CountCache++;
			self.myajax('userAction/getUserInfo', {
				cache: false,
				tip: true,
				timeout: 0,
				data: {},
				success: function(data) {
					var aRe = data.result.data;
					cache.setUser(aRe);
					self.IndexCache++;
					if (self.IndexCache == self.CountCache) {
						callback();
					}
				},
				error: function(err) {
					self.IndexCache++;
					if (self.IndexCache == self.CountCache) {
						callback();
					}
				}
			});
		}
	};
	
	//数据缓存-字典信息
	cache.getUserInfoFromNet = function() {
		var self = this;
		self.myajax('userAction/getUserInfo', {
			cache: false,
			tip: true,
			timeout: 0,
			data: {
			},
			success: function(data) {
				var re = data.result.data;
				cache.setUser(re);
				mui.trigger(document, 'getUserInfoFromNet');
			},
			error: function() {
				mui.trigger(document, 'getUserInfoFromNet');
			}
		});
	};
	cache.isOverdue = function(cache_date){
		var self = this;
		if (self.isNull(cache_date)) {
			return true;
		}
		return false;
	};
	
	cache.getAttendCodeList = function(opt){
		var self = this;
		var oRe = self.getAttendList();
		if(!oRe.isOverdue){
			if (!self.isOverdue(oRe.cache_date)) {
				opt.success(oRe.aRe);
				return;
			}
		}
		
		self.myajax("attendAction/getAttendCodeList", {
			data: opt.data,
			success: function(data) {
				var aRe = data.result.data;
				oRe = {};
				oRe.aRe = aRe;
				oRe.cache_date = self.date2str();
				oRe.isOverdue = false;
				cache.setAttendList(oRe);
				opt.success(aRe);
			},
			error: function(err) {
				opt.error(err);
			}
		});
	};
	
	//数据缓存-字典信息
	cache.getDutyFromNet = function() {
		var self = this;
		self.myajax('classAction/getClassListOfTeacher', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
				self._data.dutys = [];
				var r = data.result.data;
				for (var i = 0; i < r.length; i++) {
					var item = {};
					item.teacher_id = r[i].teacher_id;
					item.grade_id = r[i].grade_id;
					item.grade_name = r[i].class_name[0] + '年级';
					item.class_id = r[i].class_id;
					item.is_confirm = r[i].is_confirm;
					item.class_name = r[i].class_name;
					item.course = r[i].course;
					item.duty = r[i].duty;
					item.is_charge = r[i].is_charge;
					self._data.dutys.push(item);
				}
				self._setDutys(self._data.dutys);
				mui.trigger(document, 'getDutyFromNet');
			},
			error: function(data) {
				mui.trigger(document, 'getDutyFromNet');
			}
		});
	};
	
	//数据缓存-字典信息
	cache.getModuleFromNet = function() {
		var self = this;
		var aModules = [];
		self.myajax('moduleAction/getModuleList', {
			cache: true,
			timeout: 0,
			data: {
				user_type: '003'
			},
			success: function(data) {
				var r = data.result.data;
				for (var i = 0; i < r.length; i++) {
					var oModuleItem = {};
					oModuleItem.is_news = r[i].is_news;
					oModuleItem.icon_url = r[i].icon_url;
					oModuleItem.module_name = r[i].module_name;
					oModuleItem.module_code = r[i].module_code;
					oModuleItem.user_type = r[i].user_type;
					oModuleItem.initdata = r[i].initdata;
					oModuleItem.parent_code = r[i].parent_code;
					oModuleItem.module_url = r[i].module_url;
					aModules.push(oModuleItem);
				}
				cache._setModules(aModules);
				mui.trigger(document, 'getModuleFromNet');
			},
			error: function(e) {
				mui.trigger(document, 'getModuleFromNet');
			}
		});
	};
	
	//数据缓存-字典信息
	cache.getDictFromNet = function() {
		var self = this;
		var dicts = new Array();
		self.myajax('systemAction/getDictionary', {
			cache: true,
			timeout: 0,
			data: {
			},
			success: function(data) {
				var r = data.result.data;
				for (var i = 0; i < r.length; i++) {
					if (r[i].dict_code && r[i].dict_value && r[i].dict_group) {
						var item = {};
						item.value = r[i].dict_code;
						item.text = r[i].dict_value;
						item.dict_group = r[i].dict_group;
						item.other_field = r[i].other_field;
						dicts.push(item);
					}
				}
				self._setDict(dicts);
				mui.trigger(document, 'getDictFromNet');
			},
			error: function(data) {
				mui.trigger(document, 'getDictFromNet');
			}
		});
	};
	
	//数据缓存-兴趣班信息
	cache.getPlayGroupFromNet = function(opt) {
		var self = this;
		opt = opt || {};
		opt.data = opt.data || {};
		opt.data.user_type = opt.data.user_type || '003010';
		self.myajax('contactAction/getContactGroupListOfManager', {
			cache: true,
			timeout: 0,
			tip: true,
			data: opt.data,
			success: function(data) {
				var aRe = data.result.data;
				var aPlaygroup = [];
				for (var i = 0; i < aRe.length; i++) {
					var obj = {};
					obj.value = aRe[i].contact_id;
					obj.text = aRe[i].contact_name;
					aPlaygroup.push(obj);
				}
				self._setPlaygroup(aPlaygroup);
				if (opt.success) {
					opt.success(data);
				}
			},
			error: function(err) {
				if (opt.error) {
					opt.error(err);
				}
			}
		});
	};
	
	//数据缓存-寝室信息
	cache.getBedRoomFromNet = function() {
		var self = this;
		self.myajax('bedroomAction/getBedroomList', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
				var bedrooms = [];
				var r = data.result.data;
				for (var i = 0; i < r.length; i++) {
					if (r[i].bedroom_id && r[i].bedroom_name && r[i].sex) {
						var item = {};
						item.value = r[i].bedroom_id;
						item.text = r[i].bedroom_name;
						item.sex = r[i].sex;
						bedrooms.push(item);
					}
				}
				self._setBedRoom(bedrooms);
				mui.trigger(document, 'getBedRoomFromNet');
			},
			error: function(err) {
				mui.trigger(document, 'getBedRoomFromNet');
			}
		});
	};
	
	cache.getChildByBedroomID = function(bedroomID,OnSuccess,OnErro) {
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
		if (v!=null) {
			OnSuccess(v);
		}
		self.myajax("userAction/getStudentPosistionOfBed", {
			cache:  true,
			data: {
				bedroom_id: bedroomID
			},
			success: function(data) {
				var re = data.result.data;
				var aChilds = [];
				for (var i = 0; i < re.length; i++) {
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
				self._set(k,aChilds);
				OnSuccess(aChilds);
				return;
			},
			error: function(e){
				OnErro(e);
			}
		});
	};
	
	cache.getModuleListFromNet = function() {
		var self = this;
		self.myajax('gradeAction/getGradeAndClass', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
			},
			error: function(err) {
			}
		});
	};
	
	//数据缓存-教室信息
	cache.getClassRoomFromNet = function() {
		var self = this;
		self.myajax('gradeAction/getGradeAndClass', {
			cache: true,
			timeout: 0,
			data: {},
			success: function(data) {
				var classrooms = [];
				var r = data.result.data;
				for (var a = 0; a < r.length; a++) {
					if (r[a].grade_name && r[a].grade_id && r[a].class_list) {
						var classes = JSON.parse(r[a].class_list);
						var item = {};
						item.value = r[a].grade_id;
						item.text = r[a].grade_name;
						item.children = new Array();
						for (var i = 0; i < classes.length; i++) {
							var clas = {};
							clas.text = classes[i].class_name;
							clas.value = classes[i].class_id;
							clas.teamType = "011005";
							item.children.push(clas);
						}
						classrooms.push(item);
					}
				}
				self._setClassRoom(classrooms);
				mui.trigger(document, 'getClassRoomFromNet');
			},
			error: function(err) {
				mui.trigger(document, 'getClassRoomFromNet');
			}
		});
	};
	
	cache.getTableHead = function(d) {
		this.myajax("scoreTableAction/getTableHead", {
			data: {
				team_type: d.team_type,
				score_type: d.score_type,
				count_type: d.count_type
			},
			success: function(data) {
				theTableHead = data.result.data;
			},
			error: function(err) {}
		});
	};
	
	cache.getScoreCount = function(d) {
		this.myajax("scoreTableAction/getScoreCount", {
			data: {
				score_type: d.score_type,
				team_type: d.team_type,
				sum_type: d.sum_type,
				field_list: d.field_list,
				team_id: d.team_id,
				score_date: d.score_date,
				group_id: d.group_id,
				attend_item: d.attend_item,
				count_type: d.count_type
			},
			success: function(data) {
				theTableScoreCount = data.result.data;
			},
			error: function(err) {}
		});
	};
	
	//更新用户信息
	cache.UpdateUserInfo = function(OnSuccess, OnErro) {
		cache.myajax('userAction/updateUserInfo', {
			data: {
				user_name: cache.getUserName(),
				head_url: cache.getHeadUrl(),
				sex: cache.getUserSexCode()
			},
			success: OnSuccess,
			error: OnErro
		});
	};
	/*与服务器打交道 end*/
	
	
	/*数据处理函数 start*/
	cache.bedRoomCode2Name = function(id) {
		var bedrooms = this.getBedRoom();
		for (var i = 0; i < bedrooms.length; i++) {
			if (bedrooms[i].value == id) {
				return bedrooms[i].text;
			}
		}
		return '';
	};
	
	cache.moduleCode2Name = function(id) {
		var aModules = this.getModule();
		for (var f in aModules) {
			if (id == aModules[f].module_code) {
				return aModules[f].module_name;
			}
		}
		return '';
	};
	
	cache.moduleCode2Obj = function(id) {
		var aModules = this.getModule();
		for (var f in aModules) {
			if (id == aModules[f].module_code) {
				return aModules[f];
			}
		}
		return {};
	};
	
	cache.moduleCode2Url = function(id) {
		var aModules = this.getModule();
		for (var f in aModules) {
			if (id == aModules[f].module_code) {
				return aModules[f].icon_url;
			}
		}
		return 'null';
	};
	
	cache.classRoomCode2Value = function(id) {
		var classrooms = this.getClassRoom();
		for (var i = 0; i < classrooms.length; i++) {
			if (classrooms[i].value == id) {
				return classrooms[i].text;
			}
			for (var a = 0; a < classrooms[i].children.length; a++) {
				if (classrooms[i].children[a].value == id) {
					return classrooms[i].children[a].text;
				}
			}
		}
		return 'null';
	};
	
	cache.dictCode2Value = function(id) {
		if (id == "015001") {
			return '兴趣';
		}
		var dicts = this.getDict();
		for (var k in dicts) {
			if (dicts[k].value == id) {
				return dicts[k].text;
			}
		}
		return 'null';
	};
	
	cache.playgroupID2Name = function(id) {
		var dicts = this.getPlaygroup();
		for (var k in dicts) {
			if (dicts[k].contact_id == id) {
				return dicts[k].contact_name;
			}
		}
		return 'null';
	};
	
	cache.playgroupCourse2Name = function(id) {
		var dicts = this.getPlaygroup();
		for (var k in dicts) {
			if (dicts[k].course == id) {
				return dicts[k].course_name;
			}
		}
		return cache.dictCode2Value(id);
	};
	
	cache.getAttendCodeByName = function(opt){
		cache.getAttendCodeList({
			data: {},
			success: function(aRe){
				for (var i = 0; i < aRe.length; i++) {
					if (aRe[i].attend_code == opt.code) {
						opt.success(aRe[i].attend_name);
						return;
					}
				}
				opt.error('未知');
			},
			error: function(err){
				opt.error(err);
			}
		})
	};

	
	cache.getClassByTeams = function(e,d) {
		var self = this;
		var aTeams = self.getTeams();
		var classRoom = self.getClassRoom();
		var classList = [];
		var bExist = false;
		for (var i = 0; i < aTeams.length; i++) {
			var item = aTeams[i];
			bExist = false;
			if (item.duty == "016005") { //任课老师
				for (var j = 0; j < classList.length; j++) {
					if (classList[j].value == item.grade_id) {
						for (var x = 0; x < classList[j].children.length; x++) {
							if (classList[j].children[x].value == item.class_id) {
								bExist = true;
								break;
							}
						}
						if (bExist) {
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
				if (bExist) {
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
			if (item.duty == "016010") { //年级主任
				for (var k = 0; k < classList.length; k++) {
					if (classList[k]) {
						if (classList[k].value === item.grade_id) {
							classList.splice(k, 1);
						}
					}

				}
				for (var j = 0; j < classRoom.length; j++) {
					if (item.grade_id == classRoom[j].value) {
						var aGradeItem = classRoom[j];
						classList.push(aGradeItem);

					}
				}
			}
			
			if (e) {
				if (item.duty == "016020" || item.duty == "016015" ) { //校领导和行政管理组
					if(d){
						return classRoom;
					}else{
						var oGradeItem = {};
						oGradeItem.text = "全校";
				    	oGradeItem.value = 0;
				    	oGradeItem.children = [];
				    	oGradeItem.children.push({
				    		text : "全校",
				    		value : 0,
				    		teamType :"011005"
				    	});
				    	classList.push(oGradeItem);
					}
				}
			} else {
				if (item.duty == "016020") { //校领导
					if(d){
						return classRoom;
					}else{
						var oGradeItem = {};
						oGradeItem.text = "全校";
				    	oGradeItem.value = 0;
				    	oGradeItem.children = [];
				    	oGradeItem.children.push({
				    		text : "全校",
				    		value : 0,
				    		teamType :"011005"
				    	});
				    	classList.push(oGradeItem);
					}
				}
				if (item.duty == "016025") { //兴趣班
					for (var j = 0; j < classList.length; j++) {
					    if (classList[j].value == '0') {
					    	item.text = item.class_name;
					    	item.value = item.contact_id;
					    	item.teamType = "011015";
					    	classList[j].children.push(item);
					    	bExist = true;
							break;
					    }
					}
					if (bExist) {
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

	cache.getDutyByTeams = function(e) {
		var aTeams = cache.getTeams();
		var aDuty = ["016005", "016010", "016015", "016020"];
		var iIndex = 0;
		var aTeam = aTeams[0];
		for (var i = 0; i < aTeams.length; i++) {
			for (var j = 0; j < aDuty.length; j++) {
				if (aTeams[i].duty == aDuty[j]) {
					if (iIndex < j) {
						iIndex = j;
						aTeam = aTeams[i];
					}

				}
			}
		}
		if (e) {
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
	
	cache.showDom = function(e) {
		if (e && e.style) {
			if (e.style.display != 'block') {
				e.style.display = 'block';
			}
			return true;
		}
		return false;
	};
	
	cache.theValue = function(d) {
		if (d === null || d === undefined || d === 'null' || d === 'undefined') {
			return '&nbsp;';
		}
		return d;
	};
	
	cache.valueOf = function(d) {
		if (d === null || d === undefined || d === 'null' || d === 'undefined') {
			return '';
		}
		return d;
	};
	
	cache.checkVersion = function(a, b) {
		var arr = a.split('.');
		var brr = b.split('.');
		for (var x in arr) {
			try {
				if (parseInt(arr[x]) < parseInt(brr[x])) {
					return false;
				} else if (parseInt(arr[x]) > parseInt(brr[x])) {
					return true;
				}
			} catch (e) {
				return false;
			}
		}
		return false;
	};
	
	cache.checkPhoneNum = function(p) {
		var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
		if (reg.test(p)) {
			return true;
		} else {
			return false;
		}
	};
	
	
	cache.QuitOnDoubleClick = function() {
		var backButtonPress = 0;
		mui.back = function(event) {
			backButtonPress++;
			if (backButtonPress > 1) {
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
	
	cache.GetDateDiff = function(sTimestr) {
		var sTime = new Date(sTimestr);
		var eTime = new Date();
		var divNum = divNum = 1000 * 3600 * 24;
		var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
		if (diff == 0) {
			divNum = 1000 * 3600;
			var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
			if (diff == 0) {
				divNum = 1000 * 60;
				var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
				if (diff == 0) {
					divNum = 1000 * 60;
					var diff = parseInt((eTime.getTime() - sTime.getTime()) / parseInt(divNum));
					if (diff == 0) {
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
	
	cache.GetDateDescribe = function(sTimestr) {
		var self = this;
		var sTime = new Date(sTimestr);
		var eTime = new Date();
		if (sTime.getDate() == eTime.getDate()) {
			return '今天' + self.hour2str(sTime);
		}
		var iTime = self.str2timeStamp(self.date2str()) - sTime.getTime();
		iTime = parseInt(iTime/1000/3600/24)+1;
		if (iTime <= 0) {
			return -iTime + '天后' + self.hour2str(sTime);
		}
		if (iTime == 1) {
			return '昨天' + self.hour2str(sTime);
		}
		return iTime + '天前' + self.hour2str(sTime);
	};
	
	cache.getDateToEnd = function(sTimeStr) {
		sTimeStr = sTimeStr + ':00';
		var sTime = new Date(sTimeStr.replace(/-/g, '/'));
		var eTime = new Date();
		var diff = parseInt(sTime.getTime()) - parseInt(eTime.getTime());
		if (diff <= 0) {
			return '已到期';
		} else {
			var divNum = divNum = 1000 * 3600 * 24;
			var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
			if (diff == 0) {
				divNum = 1000 * 3600;
				var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
				if (diff == 0) {
					divNum = 1000 * 60;
					var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
					if (diff == 0) {
						divNum = 1000 * 60;
						var diff = parseInt((sTime.getTime() - eTime.getTime()) / parseInt(divNum));
						if (diff == 0) {
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
	
	cache.hideDom = function(e) {
		if (e && e.style) {
			if (e.style.display != 'none') {
				e.style.display = 'none';
			}
			return true;
		}
		return false;
	}
	
	//判断v是否为字符串
	cache.isString = function(v) {
		if (typeof(v) === "string") {
			return true;
		} else {
			return false;
		}
	};
	
	//判断v是否为数字
	cache.isNumber = function(v) {
		if (typeof(v) === "number") {
			return true;
		} else {
			return false;
		}
	};
	
	//
	cache.isArray = function(v) {
		return Object.prototype.toString.call(v) === '[object Array]';
	};
	
	//
	cache.isObject = function(v) {
		if (typeof(v) === "object") {
			return true;
		} else {
			return false;
		}
	};
	
	//判断字符串是否为URL格式
	cache.isUrl = function(u) {
		var self = this;
		if (!self.isString(u)) {
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
		var reg = /^(^(\d{4}|\d{2})(\-|\/|\.)\d{1,2}\3\d{1,2}$)|(^\d{4}年\d{1,2}月\d{1,2}日$)$/;
		return reg.test(d);
	};
	
	cache.isNull = function(d) {
		if (d == "" || d == null || d == undefined || d == 'null' || d == 'undefined') {
			return true;
		}
		return false;
	};
	
	cache.isScore = function(d) {
		if (d > 0) {
			return '+' + d;
		} else if (d <= 0) {
			return d
		}
	};
	
	cache.installPackage = function(path) {
		u_tip.innerText = '安装更新中...';
		plus.runtime.install(path, {
			force: true
		}, function() {
			mui.toast('更新安装成功');
			if (mui.os.android) {
				plus.runtime.quit();
			} else if (mui.os.ios) {
				plus.runtime.restart();
			}
		}, function(e) {
			mui.alert('更新安装失败，失败原因：' + JSON.stringify(e));
		});
	};
	
	cache.isInstalled = function(id) {
		if(id === 'qihoo' && mui.os.plus) {
			return true;
		}
		if(mui.os.android) {
			var main = plus.android.runtimeMainActivity();
			var packageManager = main.getPackageManager();
			var PackageManager = plus.android.importClass(packageManager)
			var packageName = {
				"qq": "com.tencent.mobileqq",
				"weixin": "com.tencent.mm",
				"sinaweibo": "com.sina.weibo"
			}
			try {
				return packageManager.getPackageInfo(packageName[id], PackageManager.GET_ACTIVITIES);
			} catch(e) {}
		} else {
			switch(id) {
				case "qq":
					var TencentOAuth = plus.ios.import("TencentOAuth");
					return TencentOAuth.iphoneQQInstalled();
				case "weixin":
					var WXApi = plus.ios.import("WXApi");
					return WXApi.isWXAppInstalled()
				case "sinaweibo":
					var SinaAPI = plus.ios.import("WeiboSDK");
					return SinaAPI.isWeiboAppInstalled()
				default:
					break;
			}
		}
	}
	/*常用函数封装 end*/
	
	/*时间转换函数汇总 start*/
	cache.date = function(d) {
		if (this.isTimestamp(d)) {
			d = parseInt(d);
			return new Date(d);
		}
		return this.str2date(d);
	};
	
	cache.initYearPicker = function() {
		var years = new Array();
		for (var i = 2016; i < 2020; i++) {
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
		for (var i = 2016; i < 2020; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			year.children = new Array();
			for (var a = 1; a < 13; a++) {
				var month = {};
				month.text = a;
				month.value = a;
				month.children = new Array();
				var s_date, c;
				for (var b = 1; b < 29; b++) {
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
				if (d_date.getMonth() + 1 == a) {
					var day = {};
					day.text = c;
					day.value = c;
					month.children.push(day);
				}
				c++;
				d_date = this.nextDay(d_date);
				if (d_date.getMonth() + 1 == a) {
					var day = {};
					day.text = c;
					day.value = c;
					month.children.push(day);
				}
				c++;
				d_date = this.nextDay(d_date);
				if (d_date.getMonth() + 1 == a) {
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
		for (var i = 2016; i < 2020; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			year.children = new Array();
			for (var a = 1; a < 13; a++) {
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
		var startDay = '2015-01-01';
		var d = this.str2date(startDay);
		for (var i = 2016; i < 2020; i++) {
			var year = {};
			year.text = i;
			year.value = i;
			year.children = new Array();
			d = this.nextWeekFirstDay(d);
			for (var x = 1; d.getFullYear() <= i; x++) {
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
		return years;
	};

	cache.nextWeekFirstDay = function(d, w) {
		var s = (8 - d.getDay()) % 7;
		return new Date(d.valueOf() + s * 24 * 60 * 60 * 1000 * (w ? w : 1));
	};
	
	cache.lastWeekFirstDay = function(d, w) {
		if (!d) {
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
		if (s) {
			 s = s.replace(/-/g,"/");
			return new Date(s);
		}else{
			return new Date();
		}
	   
	};
	
	cache.str2timeStamp = function(s){
		 s = s.replace(/-/g,"/");
		return new Date(s).valueOf();
	};
	
	cache.int2date = function(m) {
		return (m < 10 ? '0' + m : m);
	};
	
	cache.time2str = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var M = d.getMonth() + 1;
		var r = d.getDate();
		var h = d.getHours();
		var m = d.getMinutes();
		var s = d.getSeconds();
		var str = y + '-' + (M < 10 ? '0' + M : M) + '-' + (r < 10 ? '0' + r : r)+ ' ' + (h < 10 ? '0' + h : h)+ ':' + (m < 10 ? '0' + m : m)+ ':' + (s < 10 ? '0' + s : s);
		return str;
	};
	cache.hour2str = function(d){
		if (!d) {
			d = new Date();
		}
		var h = d.getHours();
		var m = d.getMinutes();
		var str = (h < 10 ? '0' + h : h)+ ':' + (m < 10 ? '0' + m : m);
		return str;
	};
	
	cache.time2str1 = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var M = d.getMonth() + 1;
		var r = d.getDate();
		var h = d.getHours();
		var m = d.getMinutes();
		var s = d.getSeconds();
		var str = y + '-' + (M < 10 ? '0' + M : M) + '-' + (r < 10 ? '0' + r : r) + (h < 10 ? '0' + h : h)+ ':' + (m < 10 ? '0' + m : m)+ ':' + (s < 10 ? '0' + s : s);
		return str;
	};
	
	cache.date2str = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var r = d.getDate();
		var s = y + '-' + (m < 10 ? '0' + m : m) + '-' + (r < 10 ? '0' + r : r);
		return s;
	};
	
	cache.date2string = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var r = d.getDate();
		var s = y + '年' + (m < 10 ? '0' + m : m) + '月' + (r < 10 ? '0' + r : r) +'日';
		return s;
	};
	
	cache.month2str = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var s = y + '-' + (m < 10 ? '0' + m : m) + '-01';
		return s;
	};
	
	cache.month2string = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var m = d.getMonth() + 1;
		var s = y + '年' + (m < 10 ? '0' + m : m) + '月';
		return s;
	};
	
	cache.year2str = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var s = y + '-01-01';
		return s;
	};
	
	cache.year2string = function(d) {
		if (!d) {
			d = new Date();
		}
		var y = d.getFullYear();
		var s = y + '年';
		return s;
	};
	
	cache.week2string = function(s) {
		var tToday = this.str2date(s);
		var sYear = tToday.getFullYear();
		var tYearFirstDay = this.str2date(sYear+'-01-01');
		var tday = this.nextWeekFirstDay(tYearFirstDay);
		for (var i = 0; tday.getFullYear() == sYear; i++) {
			if (s == this.date2str(tday)) {
				return sYear +'年 第' + i + '周   ' + this.week2date(tday)
			}
			tday = this.nextWeek(tday);
		}
		return s;
	};
	
	cache.timeStamp2str = function(d) {
		return this.time2str(this.date(d));
	};
	
	cache.hourStamp2str = function(d){
		return this.hour2str(this.date(d));
	}
	
	cache.dateStamp2str = function(d) {
		return this.date2str(this.date(d));
	};
	
	cache.dateStamp2int = function(d) {
		return this.str2int(this.date2str(this.date(d)));
	};
	
	cache.currentTimeStamp = function(){
		return (new Date()).valueOf();
	}
	
	cache.str2int = function(d) {
		return Date.parse(this.str2date(d));
	};
	
	/*时间转换函数汇总 end*/
})(mui, window.cache = {});

var chnNumChar = ['零','一','二','三','四','五','六','七','八','久','十'];
