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
};

function quitApp(){
		if (mui.os.android) {
			plus.runtime.quit();
		}else{
			closeH5Page();
		}
	};