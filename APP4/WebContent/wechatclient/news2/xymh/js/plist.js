oPdata.page_size = 20;
oPdata.start_id = 0;
var default_img_index = 0;
var vm = new Vue({
	el: '#app',
	data: {
		state: 0,
		title: '列表页',
		news_list: []
	},
	methods: {
		gotoDetailPage: function(e) {
			var data = cache.copy(e);
			data.id = data.news_id;
			cache.btn(event, function() {
				mui.openWindow({
					url: 'pdetail.html',
					id: 'pdetail',
					extras: {
						item: data
					}
				});
			});
		},
		InitPage: function() {
			vm.state = 0;
			cache.ajax('newsAction/getNewsList', {
				data: {
					start_id: 0,
					direction: 0,
					user_type: "003005",
					news_code: oExtras.dict_code,
					dict_group: oExtras.dict_group,
					is_text: '1',
					is_resize: '0',
					limit: oPdata.page_size
				},
				success: function(aRe) {
					InitPullRefresh();
					try {
						for(var i = 0; i < aRe.length; i++) {
							if(typeof(aRe[i].main_pic_url) != "string" || aRe[i].main_pic_url.indexOf('http') != 0) {
								aRe[i].main_pic_url = 'img/a' + default_img_index % 50 + '.jpg';
								default_img_index++;
							}
						}
						vm.news_list = aRe;
						if(aRe.length < oPdata.page_size) {
							pull.endPullUpToRefresh(true);
						}
						oPdata.start_id = aRe[aRe.length - 1].news_id;
					} catch(e) {
						vm.state = 2;
						vm.news_list = [];
						return;
					}
					vm.state = 1;
				},
				error: function(e) {
					vm.state = 3;
				}
			});
		}
	}
});
mui.plusReady(function() {
	if(!cache.isNull(oExtras.dict_value)) {
		vm.title = oExtras.dict_value;
	}
	vm.InitPage();
});

function InitPullRefresh() {
	//循环初始化所有下拉刷新，上拉加载。
	pull = mui('.mui-scroll').pullToRefresh({
		down: {
			callback: function() {
				setTimeout(function() {
					PulldownRefresh();
				}, 500);
			}
		},
		up: {
			callback: function() {
				PullupRefresh();
			}
		}
	});
};

function PulldownRefresh() {
	cache.ajax('newsAction/getNewsList', {
		data: {
			start_id: 0,
			direction: 0,
			user_type: "003005",
			news_code: oExtras.dict_code,
			dict_group: oExtras.dict_group,
			is_text: '1',
			is_resize: '0',
			limit: oPdata.page_size
		},
		success: function(aRe) {
			try {
				default_img_index = 0;
				for(var i = 0; i < aRe.length; i++) {
					if(typeof(aRe[i].main_pic_url) != "string" || aRe[i].main_pic_url.indexOf('http') != 0) {
						aRe[i].main_pic_url = 'img/a' + default_img_index % 50 + '.jpg';
						default_img_index++;
					}
				}
				vm.news_list = aRe;
				if(vm.news_list.length < oPdata.page_size) {
					pull.endPullUpToRefresh(true);
				}
				oPdata.start_id = aRe[aRe.length - 1].news_id;
			} catch(e) {}
			pull.endPullDownToRefresh();
		},
		error: function(e) {
			pull.endPullDownToRefresh();
		}
	});
};

function PullupRefresh() {
	cache.ajax('newsAction/getNewsList', {
		data: {
			start_id: oPdata.start_id,
			direction: 0,
			user_type: "003005",
			news_code: oExtras.dict_code,
			dict_group: oExtras.dict_group,
			is_text: '1',
			is_resize: '0',
			limit: oPdata.page_size
		},
		success: function(aRe) {
			InitPullRefresh();
			try {

				for(var i = 0; i < aRe.length; i++) {
					if(typeof(aRe[i].main_pic_url) != "string" || aRe[i].main_pic_url.indexOf('http') != 0) {
						aRe[i].main_pic_url = 'img/a' + default_img_index % 50 + '.jpg';
						default_img_index++;
					}
					vm.news_list.push(aRe[i]);
				}
				oPdata.start_id = aRe[aRe.length - 1].news_id;
				if(vm.news_list.length < oPdata.page_size) {
					pull.endPullUpToRefresh(true);
				}
			} catch(e) {
				vm.state = 2;
				vm.news_list = [];
				return;
			}
			vm.state = 1;
		},
		error: function(e) {
			vm.state = 3;
		}
	});
};