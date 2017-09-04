var uBt1 = document.getElementById('bt1'),
	uBt2 = document.getElementById('bt2'),
	uBt4 = document.getElementById('bt4'),
	uBt5 = document.getElementById('bt5'),
	uBox = document.getElementById('abox'),
	uAbtn1 = document.getElementById('abtn1'),
	uAbtn2 = document.getElementById('abtn2');
var oMask;
var oPitem = {};
var wvSelf = null;

mui.ready(function(){
	//wvSelf = plus.webview.currentWebview();
	BindEvent();
});

function BindEvent() {
	//意见反馈取消
	uAbtn1.addEventListener('tap', function() {
		uBox.style.display = 'none';
		oMask.close();
	});
	//意见反馈确定
	uAbtn2.addEventListener('tap', function() {});
	//新消息通知
	uBt1.addEventListener('toggle', function(e) {
		if(e.detail.isActive) {
			setNewAlert('true');
		} else {
			setNewAlert('false');
		}
	});
	//仅WIFI下使用
	uBt2.addEventListener('toggle', function(e) {
		if(e.detail.isActive) {
			setOnlyWifi('true');
		} else {
			setOnlyWifi('false');
		}
	});
	//提点建议
	uBt4.addEventListener('tap', function() {
		oMask = mui.createMask(function() {
			uBox.style.display = 'none';
			this.close();
		});
		uBox.style.display = 'block';
		oMask.show();

	});
	//退出当前帐号
	uBt5.addEventListener('tap', function() {
		if(window.confirm("确定要退出吗？")) {
				cache.myajax("loginAction/logout", {
					data: {
						user_type: "003015",
					},
					success: function(data) {
						var sLaunchWebview = plus.webview.getLaunchWebview().id;
						var wvs = plus.webview.all();
						for (var i = 0; i < wvs.length; i++) {
							if (wvs[i].id != sLaunchWebview && wvs[i].id != wvSelf.id) {
								wvs[i].close();
							}
						}
						cache.setAutoLogin(false);
						mui.openWindow({
							url: "wall.html",
							id: "wall",
							createNew: true,
							show: {
								aniShow: "pop-in",
								duration: 300
							},
							waiting: {
								autoShow: false
							}
						});
					},
					error: function(err) {
						var sLaunchWebview = plus.webview.getLaunchWebview().id;
						var wvs = plus.webview.all();
						for (var i = 0; i < wvs.length; i++) {
							if (wvs[i].id != sLaunchWebview && wvs[i].id != wvSelf.id) {
								wvs[i].close();
							}
						}
						cache.setAutoLogin(false);
						mui.openWindow({
							url: "wall.html",
							id: "wall",
							createNew: true,
							show: {
								aniShow: "pop-in",
								duration: 300
							},
							waiting: {
								autoShow: false
							}
						});
					}
				});
			}
	});
};