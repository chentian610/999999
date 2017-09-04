document.write('<script src="../resource/config.js" type="text/javascript" charset="utf-8"></script>');
document.write('<link rel="stylesheet" type="text/css" href="../css/reset.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="../css/mui.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="../css/hxx.font.css"/>');
var uHtml = document.querySelector('html');
uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';
var oPitem = {};
var oPdata = {};
window.onload = function(e){
	uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';
};
window.onresize = function(e){
	uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';
};
window.onerror = function(e){
	var obj = {};
	if (mui.os.plus) {
		obj.url = plus.webview.currentWebview().getURL();
		obj.os = mui.os;
	}
	obj.errorMsg = e;
	cache.addSuggestFromNet(JSON.stringify(obj));
};

function getParamValue(name) {   
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");   
    var r = window.location.search.substr(1).match(reg);   
    if (r != null) return decodeURI(r[2]); return null;   
} 

