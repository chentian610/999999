document.write('<script src="../resource/config.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="../lib/mui/mui.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="../lib/vue/vue.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="../lib/hxx/hxx.md5.min.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="../js/hxx.app.js" type="text/javascript" charset="utf-8"></script>');
document.write('<link rel="stylesheet" type="text/css" href="../css/reset.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="../css/mui.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="../css/hxx.font.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="../css/mui.extend.css"/>');
window.uHtml = document.documentElement;
uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';
window.oPitem = {};
window.oPdata = {};
window.oExtras = {};
window.wvSelf = null;
window.wvParent = null;
window.onload = function(e){
	uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';
};
window.onresize = function(e){
	uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';
};
//默认图片
function imgLoadError() {
	var img = event.srcElement;
	img.src = '../images/default-img.png';
	img.onerror = null;
};
function bugSubmit(a,b,c,d,e) {
	try {
		if (plus.runtime.appid != 'HBuilder' && plus.runtime.appid != 'HelloH5') {
			cache.ajax('http://106.14.33.168/api/error', {
				data: {
					app_id: cache.getClientID(),
					error_discribe: a,
					error_line: c,
					error_column: d,
					error_obj: JSON.stringify(e),
					happen_time: cache.time2str(),
					page_url: b,
					screen_shot: ''
				}
			});
		}
	} catch(e) {
	}
};
window.onerror = bugSubmit;
window.AudioRecorder = null;
window.AudioPlayer = null;

function getParamValue(name) {   
	try{
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");   
    	var r = window.location.search.substr(1).match(reg);   
   		if (r != null) return decodeURI(r[2]); return null;   
	}catch(err){
		alert(err);
	}
   	return null;
} 

//初始化学校信息
function initSchool(){
	var schoolId = getParamValue("schoolId");
	if(schoolId == null){
		schoolId = cache.getSchoolId();	
	}
	cache.ajax('schoolAction/getSchoolById',{
		data:{school_id: schoolId},
		async: false,
		success:function(data){
			config.schoolid = data.school_id;
			config.school_name = data.school_name;
			cache.setSchoolId(data.school_id);
		}
	});
	
}

