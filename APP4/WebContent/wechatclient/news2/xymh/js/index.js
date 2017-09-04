var new_list = [];
var default_img_index = 0;

var vm = new Vue({
	el: '#app',
	data: {
		school_name: config.school_name,
		page: 0,
		state: 1,
		page1: {
			new_list: []
		}
	},
	methods: {
		GetDictSchoolList:function(){
			GetDictSchoolList();
		},
		gotoLogin: function(e) {
			cache.setUserType(e);
			cache.btn(event, function() {
				mui.openWindow({
					url: '../main/login.html',
					id: 'login'
				});
				vm.page = 0;
			});
		},
		gotoListPage: function(e) {
			var data = cache.copy(e);
			data.news_list = '';
			cache.btn(event, function() {
				mui.openWindow({
					url: 'list.html?dict_code=' + data.dict_code + '&dict_group=' + data.dict_group + '&dict_value=' + data.dict_value,
					id: 'list',
					extras: {
						item: data
					}
				});
			});
		},
		gotoDetailPage: function(e) {
			var data = cache.copy(e);
			data.id = data.news_id;
			cache.btn(event, function() {
				mui.openWindow({
					url: 'detail.html?id='+data.id,
					id: 'detail',
					extras: {
						item: data
					}
				});
			});
		}
	}
});
function HasInfo(e){
	for (var i = 0; i < e.length; i++) {
		if (!cache.arrIsNoUse(e[i].item_list)) {
			return true;
		}
	}
	return false;
};
function InitPage() {
	cache.ajax("newsAction/getNewsListForAPP", {
		data: {},
		success: function(aRe) {
			if (cache.arrIsNoUse(aRe)) {
				if (cache.arrIsNoUse(vm.page1.new_list) || !HasInfo(vm.page1.new_list)) {
					vm.state = 3;
					return;
				}else{
					return;
				}
			}
			for(var i = 0; i < new_list.length; i++) {
				for(var j = 0; j < aRe.length; j++) {
					if(aRe[j].dict_group == new_list[i].value) {
						try {
							var news_list = JSON.parse(aRe[j].news_list);
							aRe[j].news_list = news_list;
						} catch(e) {
							console.log(e);
						}
						new_list[i].item_list.push(aRe[j]);
					}
				}
			}
			for(var i = 0; i < new_list.length; i++) {
				new_list[i].item_list = sort(new_list[i].item_list);
			}
			if (cache.arrIsNoUse(new_list)) {
				if (cache.arrIsNoUse(vm.page1.new_list)) {
					vm.state = 3;
					return;
				}else{
					return;
				}
			}
			vm.page1.new_list = new_list;
			vm.$nextTick(function(){
				loadAll(function(){
					cache.setNewsList(new_list);
				});
			});
			InitSlider();
			vm.state = 1;
		},
		error: function(err) {
			if (cache.arrIsNoUse(vm.page1.new_list)) {
				vm.state = 3;
			}
		}
	});
};

function sort(arr) {
	for(var i = 0; i < arr.length; i++) {
		for(var j = i; j < arr.length; j++) {
			if(arr[i].sort > arr[j].sort) {
				var oRe = arr[i];
				arr[i] = arr[j];
				arr[j] = oRe;
			}
		}
	}
	return arr;
};

function GetDictSchoolList() {
	vm.state = 0;
	var old_new_list = cache.getNewsList();
	if (cache.arrIsNoUse(old_new_list)) {
		//plus.navigator.closeSplashscreen();
		vm.state = 0;
	}else{
		//plus.navigator.closeSplashscreen();
		vm.page1.new_list = old_new_list;
		vm.state = 1;
		InitSlider();
	}
	new_list = cache.getDict('022');
	if(cache.arrIsNoUse(new_list)) {
		cache.getDictFromNet({
			success: function(e) {
				new_list = cache.getDict('022');
				for(var i = 0; i < new_list.length; i++) {
					new_list[i].item_list = [];
				}
				InitPage();
				InitSlider();
			},
			error: function(e) {
				vm.state = 3;
			}
		});
	} else {
		for(var i = 0; i < new_list.length; i++) {
			new_list[i].item_list = [];
		}
		InitPage();
		InitSlider();
	}
};

function InitSlider() {
	setTimeout(function() {
		var deceleration = mui.os.ios ? 0.003 : 0.0009;
		mui('.mui-scroll-wrapper').scroll({
			bounce: false,
			indicators: true,
			deceleration: deceleration
		});
		try{
			mui('.mui-slider').slider().refresh();
			mui('.mui-scroll-wrapper.mui-slider-indicator.mui-segmented-control').scroll().refresh();
		}catch(e){
		}
	}, 10);
};

mui.ready(function() {	
	initSchool();
	GetDictSchoolList();
	document.getElementById("schoolTitle").innerHTML = config.school_name;
});


