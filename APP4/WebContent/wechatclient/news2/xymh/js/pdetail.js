var vm = new Vue({
	el: '#app',
	data: {
		title: '详情页',
		state: 0,
		data: {}
	},
	methods: {
		InitPage: function() {
			vm.state = 0;
			cache.ajax('newsAction/getNews', {
				data: {
					news_id: oExtras.id
				},
				success: function(oRe) {
					if(oRe.main_pic_url.indexOf('http') != 0) {
						oRe.main_pic_url = '';
					}
					vm.data = oRe;
					vm.state = 2;
					clearAll();
				},
				error: function() {
					vm.state = 3;
				}
			});
		}
	}
});

mui.plusReady(function() {
	vm.InitPage();
});

function clearAll() {
	setTimeout(function() {
		var uContent = document.querySelector('.mui-content');
		var all = uContent.querySelectorAll('a');
		for(var i = 0; i < all.length; i++) {
			all[i].onclick = function() {
				return false;
			}
		}
		all = uContent.querySelectorAll('img');
		for(var i = 0; i < all.length; i++) {
			all[i].style.width = '100%';
			all[i].style.height = 'auto';
			all[i].style.margin = '0';
			all[i].style.padding = '0';
		}
	}, 50)
};