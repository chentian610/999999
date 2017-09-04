var uList = document.getElementById("list");
var oExtras = {};
mui.ready(function() {
	//plus.nativeUI.showWaiting('数据加载中');
	oExtras.user_id = getParamValue("user_id");
	oExtras.phone = getParamValue("phone");
	InitPage();
});

function InitPage() {
	cache.ajax("contactAction/getTeacherListByUserID", {
		data: {
			user_type: "003015",
			user_id: oExtras.user_id,
			phone: oExtras.phone
		},
		success: function(aRe) {
			if(cache.isArray(aRe) && aRe.length > 0) {
				AppendLi(aRe);
			}
			plus.nativeUI.closeWaiting();
		},
		error: function() {
			plus.nativeUI.closeWaiting();
		}
	});
}

function AppendLi(e) {
	uList.innerHTML = '<ul class="mui-table-view" >\
				<li class="mui-table-view-cell">\
					<a class="hxx-list-detail">\
						<img  src="' + e[0].head_url + '"/>\
						<div>\
							<h4 >' + e[0].teacher_name + '</h4>\
							<ul id="duty">\
							</ul>\
						</div>\
					</a>\
				</li>\
			</ul>\
			<ul class="mui-table-view">\
				<li class="mui-table-view-cell mui-media">\
					<div class="mui-media-body">手机<p class="mui-ellipsis">' + e[0].phone + '</p>\
					</div>\
					<a class=" tel" id="BtnPhoneIcon"><b class="icon-phone-box hxx-icon icon-phone"></b></a>\
					<a class="tel" href="sms:' + e[0].phone + '"><b class="icon-sms-box hxx-icon icon-xiaoxi"></b></a>\
				</li>\
			</ul>\
			<div id="BoxMask" class="box-mask">\
				<div id="BtnTel" class="button-telephone-box">\
					<p class="tip-box">拨打电话给联系人</p>\
					<a id="ButtonCancel" class="button-cancel">取消</a>\
					<a id="ButtonSure" href="tel:' + e[0].phone + '" class="button-sure">确定</a>\
				</div>\
			</div>';
	uList.querySelector('.hxx-list-detail img').onerror = function() {
		uList.querySelector('.hxx-list-detail img').src = "../images/default_img_user.png";
	};
	var uBtnPhoneIcon = document.querySelector("#BtnPhoneIcon"),
		uBoxMask = document.querySelector("#BoxMask"),
		uButtonCancel = document.querySelector("#ButtonCancel"),
		uButtonSure = document.querySelector("#ButtonSure");
	uBtnPhoneIcon.addEventListener('click', function() {
		cache.showDom(uBoxMask);
	});
	uBoxMask.addEventListener('click', function() {
		cache.hideDom(this);
	});
	uButtonCancel.addEventListener('tap', function(e) {
		e.stopPropagation();
		cache.hideDom(uBoxMask);
	});
	uButtonSure.addEventListener('tap', function(e) {
		e.stopPropagation();
		setTimeout(function() {
			cache.hideDom(uBoxMask);
		}, 1200);
	});
	var duty = document.getElementById("duty");
	for(var i = 0; i < e.length; i++) {
		duty.appendChild(CreateEle(e[i]));
	}
};

function CreateEle(e) {
	var li = document.createElement('li');
	if(!cache.isNull(e.duty_name)) {
		li.innerHTML = e.duty_name;
	}
	return li;
};

function Course(e) {
	var oCourse = cache.getDict("015");
	var bFlag = true;
	for(var i = 0; i < oCourse.length; i++) {
		if(oCourse[i].value == e) {
			return " " + oCourse[i].text + "老师";
			bFlag = false;
		}
	}
	if(bFlag) {
		return "";
	}
}