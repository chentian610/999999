document.addEventListener( "plusready",  function()
{
	if (plus.runtime.appid != 'HBuilder' && plus.runtime.appid != 'HelloH5') {
		var _BARCODE = 'NJShell',
		B = window.plus.bridge;
		var NJShell =
		{
			NJShellMethod_Data_ParamArry : function (Argus)
			{
	          	try{
	          		return B.execSync(_BARCODE, "NJShellMethod_Data_ParamArry", [Argus]);
	          		
	          	}catch(e){
	          		window.plus.NJShell = undefined;
	          	}
	        }
	  	};
    	window.plus.NJShell = NJShell;
    }
}, true );


function closeH5Page(){
	plus.NJShell.NJShellMethod_Data_ParamArry(['closeH5Page']);
	
}


/*
 * // 获取公共参数
function NJShellMethod_2()
{
	
	  @"school_id": school_id,
                          @""  : user_id,
                          @"" : app_type,
                          @"": user_type,
                          @"": user_name,
                          } mutableCopy];
    if ([GlobalKit CTEnumFromCode:user_type] == CTUserType_Parents) {
        CTParentChildInfoRFModel *defaultInfo = [[CTUserManager shareInstance] getDefaultStudentInfo];
        NSString *stuId = defaultInfo==nil? @"":defaultInfo.student_id;
        [dic setObject:stuId forKey:@"student_id"];
        
        
    var Argus = plus.NJShell.NJShellMethod_Data_ParamArry(["getCommonParans"]);
	var user_id = Argus.user_id;
	var app_type = Argus.app_type;
	var user_type = Argus.user_type;
	var user_name = Argus.user_name;
	var student_id = Argus.student_id;
	var school_id = Argus.school_id;
}
// 调用：
<div class="button" onclick="NJShellMethod_2()">NJShellMethod_2()</div>
 */
