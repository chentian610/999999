/**
 * 获取JSON值：null转空字符串
 */
function getJsonValue(Obj,field){ 
	if (Obj[field]) return Obj[field];
	else return "";
};

/**
 * 获取JSON值:null转0
 */
function getJsonMathValue(Obj,field){ 
	if (Obj[field]) return Obj[field];
	else return 0;
};

/**
 * 按yyyy-MM-dd HH:mm:ss 转化日期
 * format == 'day' 表示 yyyy-MM-dd
 * format == 'minute' 表示 yyyy-MM-dd HH:mm
 * format == 'second' 表示 yyyy-MM-dd HH:mm:ss
 */
function getDateStr(date, format){
	  var myDate = new Date(date);   
      var year = myDate.getFullYear();  
      var month = ("0" + (myDate.getMonth() + 1)).slice(-2);  
      var day = ("0" + myDate.getDate()).slice(-2);  
      var h = ("0" + myDate.getHours()).slice(-2);  
      var m = ("0" + myDate.getMinutes()).slice(-2);  
      var s = ("0" + myDate.getSeconds()).slice(-2);   
      if(format=='day'){
    	  return year + "-" + month + "-" + day;
      }else if(format=='minute'){
    	  return year + "-" + month + "-" + day+ " " + h + ":" + m;
      }else if(format=='second'){
    	  return year + "-" + month + "-" + day+ " " + h + ":" + m + ":" + s;
      }
} 


/**
 * 截取字符串
 */
function getSubString(str){
	var subStr = str.substr(0, 60)+'......'; 
	return subStr;
}