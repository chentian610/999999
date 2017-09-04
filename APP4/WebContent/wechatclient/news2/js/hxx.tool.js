var tool = {
	closeSplashscreen: function(){
		plus.navigator.closeSplashscreen();
	},
	setScrollWrapper:function(e){
		if(!e){
			e = 0;
		}
		var deceleration = mui.os.ios ? 0.003 : 0.0009;
		document.querySelector('.mui-scroll-wrapper').style.height = (window.innerHeight - e) + "px";
		mui('.mui-scroll-wrapper').scroll({
			bounce: false,
			indicators: true, //是否显示滚动条
			deceleration: deceleration
		});
	},
	openWindow:function(opt){
		if (!opt) {return;}
		if (!opt.id) {return;}
		if (!opt.url) {return;}
		if (plus&&plus.webview) {
			var vwTo = plus.webview.getWebviewById(opt.id);
			if (vwTo) {
				if (opt.goto) {
					vwTo.loadURL(opt.url);
				}
				vwTo.show('slide-in-right');
			}else{
				vwTo = plus.webview.create(opt.url,opt.id);
				vwTo.show('slide-in-right');
			}
		}
	},
	sGetuiIndex: 0,
	getui:function(opt){
		this.sGetuiIndex++;
		var index = this.sGetuiIndex;
		if (!opt) {return;}
		if (!opt.url) {return;}
		opt.extras = opt.extras || {};
		if (plus&&plus.webview) {
			if (index != 1) {
				return;
			}
			var wvGetui = plus.webview.create(opt.url,'geTui',{},opt.extras);
			wvGetui.show('slide-in-right');
			setTimeout(function(){
				var wvAll = plus.webview.all();
				for (var i = 0; i < wvAll.length; i++) {
					if (wvAll[i].__uuid__ != wvGetui.__uuid__ && wvAll[i].id == wvGetui.id) {
						wvAll[i].close();
					}
				}
				tool.sGetuiIndex = 0;
			},500);
		}
	}
};
