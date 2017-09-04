var uTitleBarName = document.getElementById("TitleBarName"),
	uTitleBar = document.getElementById("TitleBar"),
    uIframe = document.getElementById("ContentIframe"),
    uContentDiv = document.getElementById("contentDiv");

var wvMask = null;
//mui初始化
var oPitem = {},
	oPdata = {};

oPdata.sub_arr = [];

mui.ready(function() {
	InitPage();
	InitPicker();
	BindEvent();
});

function InitPicker() {
	//创建遮罩
	oMask = mui.createMask(function() {
		document.getElementById('abox').style.display = "none";
	});
	oPicker1 = new mui.PopPicker({
		layer: 1
	});
	var childs = cache.getChilds();
	var opts = [];
	for (var i = 0; i < childs.length; i++) {
		childs[i].array_index = i;
		var opt = {
			value:childs[i],	
			text: childs[i].student_name
		};
		opts.push(opt);
	}
	oPicker1.setData(opts);
};

function InitPage() {
	var child = cache.getChild();
	if(cache.isNull(child)) {
		child = cache.getChilds()[0];
		cache.setChildIndex(0);
		cache.setChild(child);
	}
	uTitleBarName.innerText = child.student_name;
	var defalut_iframe_src = cache.getDefalutIframeSrc();
	if(typeof(defalut_iframe_src) == "undefined" || defalut_iframe_src == ''){
		defalut_iframe_src = "phome";
	}
	ChangeTab(defalut_iframe_src);
};

function ChangeTab(v){
	if(v == "phome"){
		uIframe.src = "phome.html";
		ChangeHightLineTab("phome");
		cache.setDefalutIframeSrc("phome");
	}else if(v == "pfind"){
		uIframe.src = "pfind.html";
		ChangeHightLineTab("pfind");
		cache.setDefalutIframeSrc("pfind");
	}else if(v == "pset"){
		uIframe.src = "pset.html";
		ChangeHightLineTab("pset");
		cache.setDefalutIframeSrc("pset");
	}
};

function ChangeHightLineTab(v){
	$(".mui-bar-tab").find("a").removeClass("mui-active");
	$(".mui-bar-tab").find("a[href='" + v + "']").addClass("mui-active");
	if(v == "phome"){
		mui('span')[0].className = "mui-icon hxx-icon icon-dongtaishixin";
		mui('span')[2].className = "mui-icon hxx-icon icon-faxian";
		mui('span')[4].className = "mui-icon hxx-icon icon-wode";
		uTitleBar.style.display="block";
		uContentDiv.style.top = '45px';
		cache.setDefalutIframeSrc("phome");
	}else if(v == "pfind"){
		mui('span')[0].className = "mui-icon hxx-icon icon-dongtai";
		mui('span')[2].className = "mui-icon hxx-icon icon-faxianshixin";
		mui('span')[4].className = "mui-icon hxx-icon icon-wode";
		uTitleBar.style.display="block";
		uContentDiv.style.top = '45px';
		cache.setDefalutIframeSrc("pfind");
	}else if(v == "pset"){
		mui('span')[0].className = "mui-icon hxx-icon icon-dongtai";
		mui('span')[2].className = "mui-icon hxx-icon icon-faxian";
		mui('span')[4].className = "mui-icon hxx-icon icon-wodeshixin";
		uTitleBar.style.display="none";
		uContentDiv.style.top = '0px';
		cache.setDefalutIframeSrc("pset");
	}
};

function BindEvent() {
	uTitleBar.addEventListener('tap', function() {
		oPicker1.show(function(item) {
			var child = item[0].value;
			cache.setChildIndex(child.array_index);
			cache.setChild(child);
			uIframe.src = uIframe.src;
			uTitleBarName.innerText = item[0].text;
		});
	});
		
	mui('.mui-bar-tab').on('tap', 'a', function(e) {
		var targetTab = this.getAttribute('href');
		ChangeTab(targetTab);
	});
	//
	cache.QuitOnDoubleClick();
	//接收推送消息
	/*plus.push.addEventListener("receive", function(msg) {
		if(cache.getAutoLogin() != true) {
			return;
		}
		if(mui.os.android) {
			var oPayload = JSON.parse(msg.payload);
			oPayload.info_content = oPayload.info_content || '';
			plus.push.createMessage(oPayload.info_content, msg.payload, {
				"title": oPayload.info_title
			});
		}
	}, false);*/
	//绑定点击推送
	/*plus.push.addEventListener("click", function(msg) {
		if(cache.getAutoLogin() != true) {
			return;
		}
		var oPayload = {};
		if(mui.os.android) {
			oPayload = JSON.parse(msg.payload);
		} else {
			oPayload = JSON.parse(msg.payload.init_data);
		}
		var p = oPayload.module_code || '.';
		if(oPayload.user_type == '003010' || oPayload.user_type == '003015') {
			oPayload.info_url = "p"+ oPayload.info_url;
			var aStu = cache.getChilds();
			if (cache.isNull(oPayload.student_id) || oPayload.student_id == 0) {
				if (cache.isNull(oPayload.item_id) || oPayload.item_id == 0) {
					if (cache.isNull(oPayload.group_id) || oPayload.group_id == 0) {
						return;
					}else{
						for (var i = 0; i < aStu.length; i++) {
							if (aStu[i].grade_id == oPayload.group_id) {
								cache.setChild(aStu[i]);
								break;
							}
						}
					}
				}else{
					for (var i = 0; i < aStu.length; i++) {
						if (aStu[i].class_id == oPayload.item_id) {
							cache.setChild(aStu[i]);
							break;
						}
					}
				}
			}else{
				for (var i = 0; i < aStu.length; i++) {
					if (aStu[i].student_id == oPayload.student_id) {
						cache.setChild(aStu[i]);
						break;
					}
				}
			}
		}
		
		var wvs = plus.webview.all();
		var wvids = [];
		for (var i = 0; i < wvs.length; i++) {
			wvids.push(wvs[i].id);
		}
		var obj = {};
		if (mui.os.plus) {
			obj.url = plus.webview.currentWebview().getURL();
			obj.os = mui.os
		}
		obj.getui = wvids.join(',');
		cache.addSuggestFromNet(JSON.stringify(obj));
		
		oPayload.id = oPayload.module_pkid;
		var _url = "../" + oPayload.module_code +"/"+ oPayload.info_url;
		mui.openWindow({
			url: _url,
			id: p,
			createNew: true,
			extras: {
				item: oPayload
			},
			show: {
				aniShow: "fade-in",
				duration: 300
			},
			waiting: {
				autoShow: false
			}
		});
	}, false);*/
	//BindEvent end 
};