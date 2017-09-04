	window.onerror = function(err) {
		log('window.onerror: ' + err);
	};

	function connectWebViewJavascriptBridge(callback) {
		if (window.WebViewJavascriptBridge) {
			callback(WebViewJavascriptBridge);
		} else {
			document.addEventListener('WebViewJavascriptBridgeReady', function() {
				callback(WebViewJavascriptBridge);
			}, false);
		}
	}

	connectWebViewJavascriptBridge(function(bridge) {
		// Deal OC send Message to JS (send message按钮)
		bridge.init(function(message, responseCallback) {
			var data = {
				'Javascript Responds' : 'Wee!'
			};
			responseCallback(data);
		});
		 
		 
	    bridge.registerHandler('invokeLogin', function(data,responseCallback) {
		    var responseData = { 'Javascript Says':'invokeLogin!' };
		 	responseCallback(responseData);
		 });
		 // Send message to ObjC
		 var loginBtn = $('#loginBtn');
		 if (loginBtn) {
			 loginBtn.unbind("click").on('click',function(e) {
				 e.preventDefault();
				 var data = 'invokeLogin';
				 bridge.send(data);
			 });
		 }
});
