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
//	    bridge.registerHandler('invokePhoto', function(data,responseCallback) {
//		    var responseData = { 'Javascript Says':'Right back atcha!' };
//		    uploadPictureFromIOS(data);
//		 	responseCallback(responseData);
//		 });
//		 // Send message to ObjC
//		 var photoBtn = $('#photoBtn');
//		 if (photoBtn) {
//			 button.unbind("click").on('click',function(e) {
//				 e.preventDefault();
//				 var data = 'invokePhoto';
//				 bridge.send(data);
//			 });
//		 }
		 
	    bridge.registerHandler('invokeComomParams', function(data,responseCallback) {
		    var responseData = { 'Javascript Says':'Right back atcha!' };
		    var jsonObj = $.parseJSON(data);
		    newOpt.data.user_id=jsonObj.user_id;
		    newOpt.data.user_type=jsonObj.user_type;
		    newOpt.data.school_id=jsonObj.school_id;
		    newOpt.data.user_name=jsonObj.user_name;
		    newOpt.data.app_type=jsonObj.app_type;
		    newAjax(newOpt);
		 	responseCallback(responseData);
		 });
		 // Send message to ObjC
		 var localBtn = $('#localBtn');
		 if (localBtn) {
			 localBtn.unbind("click").on('click',function(e) {
				 e.preventDefault();
				 var data = 'invokeComomParams';
				 bridge.send(data);
			 });
		 }
		 
	    bridge.registerHandler('invokeDismissVC', function(data,responseCallback) {
		    var responseData = { 'Javascript Says':'Right back atcha!' };
		 	responseCallback(responseData);
		 });
		 // Send message to ObjC
		 var backBtn = $('#backBtn');
		 if (backBtn) {
			 backBtn.unbind("click").on('click',function(e) {
				 e.preventDefault();
				 var data = 'invokeDismissVC';
				 bridge.send(data);
			 });
		 }
		 
		 
	    bridge.registerHandler('invokeLogin', function(data,responseCallback) {
		    var responseData = { 'Javascript Says':'invokeLogin!' };
		 	responseCallback(responseData);
		 });
		 // Send message to ObjC
		 var loginBtn = $('#loginBtn');
		 if (loginBtn) {
			 loginBtn.unbind("click").on('click',function(e) {
				 alert(22222200);
				 e.preventDefault();
				 var data = 'invokeLogin';
				 bridge.send(data);
			 });
		 }
});
