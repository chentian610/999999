document.write('<script src="../js/mui.min.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="../js/hxx.md5.min.js" type="text/javascript" charset="utf-8"></script>');
document.write('<script src="../js/hxx.app.js" type="text/javascript" charset="utf-8"></script>');
uHtml.style.fontSize = (uHtml.clientWidth/30) + 'px';

function getParamValue(name) {   
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");   
    var r = window.location.search.substr(1).match(reg);   
    if (r != null) return decodeURI(r[2]); return null;   
} 

//初始化学校信息
function initSchool(){
	var schoolId = getParamValue("schoolId");
	if(schoolId == null || schoolId == ''){
		//参数中未带学校id，尝试从缓存中获取
		schoolId = cache.getSchoolId();
		if(schoolId == null || schoolId == ''){
			mui.toast('学校信息未找到');
		}else{
			config.schoolid = schoolId;
			config.school_name = cache.getSchoolName();
		}
		return;
	}
	cache.myajax('schoolAction/getSchoolById',{
		data:{school_id: schoolId},
		async: false,
		success:function(data){
			var school = data.result.data;
			config.schoolid = school.school_id;
			config.school_name = school.school_name;
			cache.setSchoolId(school.school_id);
			cache.setSchoolName(school.school_name);
		}
	});
}

