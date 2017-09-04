(function($, document, undefined) {
	$.PopBox = $.Class.extend({
		init: function(element, options) {
			var self = this;
			self.element = null;
			self.title = {};
			self.title.element = null;
			self.items = [];
			self.selectItems = [];
			self.element = element;
			self.options = options;
			self.initMask();
			self.initPopBox();
			self.bindEvent();
		},
		bindEvent: function(){
			var self = this;
			window.document.querySelector(self.options.confim.element).addEventListener('tap',function(){
				if (self.host) {
					self.options.confim.fun(self.getSelected(),self.host);
				}else{
					self.options.confim.fun(self.getSelected());
				}
				self.hide();
			});
			window.document.querySelector(self.options.cancel.element).addEventListener('tap',function(){
				self.options.cancel.fun();
				self.hide();
			});
		},
		getSelected: function(){
			var self  = this;
			var oItems = [];
			var uItems = self.element.querySelectorAll('.active');
			for (var i = 0; i < uItems.length; i++) {
				oItems.push(uItems[i].affix);
			}
			if (self.host) {
				self.host.affix = oItems;
			}
			return oItems;
		},
		initPopBox: function(){
			var self = this;
			if (self.options.title) {
				if (self.options.title.element) {
					self.title.element = window.document.querySelector(self.options.title.element);
					if (self.options.title.text) {
						self.title.element.innerText = self.options.title.text;
					}
				}	
			}
			var datas = self.options.data;
			for (var i = 0; i < datas.length; i++) {
				var items = datas[i].items;
				var eleme = self.element.querySelector(datas[i].element);
				eleme.innerHTML = '';
				for (var j = 0; j < items.length; j++) {
					var uDiv = window.document.createElement('div');
					uDiv.className = 'hxx-popbox-cell';
					uDiv.innerHTML = '<div class="hxx-popbox-num">'+items[j].value+'</div><div class="hxx-popbox-txt">'+items[j].text+'</div>';
					var uBtn = uDiv.querySelector('.hxx-popbox-num');
					items[j].index = self.items.length;
					uBtn.affix = items[j];
					self.items.push(uBtn);
					uBtn.addEventListener('tap',function(){
						if (this.classList.contains('active')) {
							this.classList.remove('active');
						}else{
							this.classList.add('active')
						}
					});
					eleme.appendChild(uDiv);
				}
			}
		},
		initMask: function(){
			var self = this;
			self.uMask = mui.createMask(function(){
				self.element.style.display = 'none';
			});
		},
		clear: function(){
			var self = this;
			for (var i = 0; i < self.items.length; i++) {
				self.items[i].className = 'hxx-popbox-num';
			}
		},
		initItems: function(e){
			var self = this;
			self.clear();
			if (self.host&&self.host.affix) {
				for (var i = 0; i < self.host.affix.length; i++) {
					self.items[self.host.affix[i].index].classList.add('active');
				}
			}
		},
		show: function(e){
			var self = this;
			self.uMask.show();
			this.element.style.display = 'block';
			if (e) {
				self.host = e;
				self.initItems();
			}else{
				self.host = null;
			}
		},
		hide: function(){
			var self = this;
			self.uMask.close();
		},
		destroy: function(){
			$.popboxApi = null;
		},
		refresh: function(){
			var self = this;
			if (self.host) {
				self.host.affix = [];
			}
			self.clear();
		},
		setTitle: function(e){
			if (self.title.element) {
				self.title.element.innerText = e;
			}
		}
	});
	$.fn.popbox = function(options) {
		options = options || {};
		this.each(function() {
			var self = this;
			var popboxApi = $.popboxApi = new $.PopBox(self, options);
		});
	};
})(mui, window, document);