var uReset = document.getElementById("reset"),
	uConfirm = document.getElementById("confirm");
var oPitem = {},
	oPdata = {},
	wvParent = null,
	aBtnGroups = [];

mui.init({
	keyEventBind: {
		backbutton: false,
		menubutton: false
	}
});

mui.ready(function(){
	var aGroup = document.querySelectorAll('.hxx-box-cell');
	for (var i = 0; i < aGroup.length; i++) {
		var aBtnDoms = aGroup[i].querySelectorAll('.hxx-box-item');
		for (var j = 0; j < aBtnDoms.length; j++) {
			aBtnDoms[j].groupIndex = i;
			aBtnDoms[j].btnIndex = j;
			aBtnDoms[j].dataAffix = aBtnDoms[j].getAttribute('data-id');
			aBtnDoms[j].addEventListener('tap',function(){
				var self = this;
				var selectIndex = aBtnGroups[self.groupIndex].selectIndex;
				if(selectIndex == self.btnIndex) {
					return;
				}else{
					aBtnGroups[self.groupIndex].aBtnDoms[selectIndex].classList.remove('active');
					self.classList.add('active');
					aBtnGroups[self.groupIndex].selectIndex = self.btnIndex;
				}
			});
		}
		var oBtnGroup = {};
		oBtnGroup.selectIndex = 0;
		oBtnGroup.aBtnDoms = aBtnDoms;
		aBtnGroups.push(oBtnGroup);
	}
});

mui.plusReady(function(){
	wvParent = plus.webview.getWebviewById('videos');
	BindEvent();
});

function BindEvent(){
	uReset.addEventListener('tap',function(){
		var self = this;
		var selectIndex = 0;
		if (self.disable) {
			return;
		}
		self.disable = true;
		for (var i = 0; i < aBtnGroups.length; i++) {
			selectIndex = aBtnGroups[i].selectIndex;
			aBtnGroups[i].aBtnDoms[selectIndex].classList.remove('active');
			aBtnGroups[i].aBtnDoms[0].classList.add('active');
			aBtnGroups[i].selectIndex = 0;
		}
		self.disable = false;
	});
	
	uConfirm.addEventListener('tap',function(){
		var self = this;
		var selectIndex = 0;
		if (self.disable) {
			return;
		}
		self.disable = true;
		//获取选中的年级
		oPdata.oGrade = {};
		selectIndex = aBtnGroups[0].selectIndex;
		oPdata.oGrade.ID = aBtnGroups[0].aBtnDoms[selectIndex].dataAffix;
		oPdata.oGrade.text = aBtnGroups[0].aBtnDoms[selectIndex].innerText;
		//获取选中的班级
		oPdata.oCourses = {};
		selectIndex = aBtnGroups[1].selectIndex;
		oPdata.oCourses.ID = aBtnGroups[1].aBtnDoms[selectIndex].dataAffix;
		oPdata.oCourses.text = aBtnGroups[1].aBtnDoms[selectIndex].innerText;
		//获取选中的出版社
		oPdata.oVersion = {};
		selectIndex = aBtnGroups[2].selectIndex;
		oPdata.oVersion.ID = aBtnGroups[2].aBtnDoms[selectIndex].dataAffix;
		oPdata.oVersion.text = aBtnGroups[2].aBtnDoms[selectIndex].innerText;
		
		mui.fire(wvParent,'initPage',oPdata);
		
		if (mui.os.android && parseFloat(mui.os.version) < 4.4) {
			document.querySelector("header.mui-bar").style.position = "fixed";
			document.querySelector(".mui-bar-nav~.mui-content").style.paddingTop = "44px";
		}
		
		self.disable = false;
	});
	//BindEvent end
};
