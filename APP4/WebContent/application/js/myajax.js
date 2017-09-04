(function($){  
    //备份jquery的ajax方法  
    var _ajax=$.ajax;  
      
    //重写jquery的ajax方法  
    $.myajax=function(opt){  
        //备份opt中error和success方法  
        var fn = {  
            error:function(XMLHttpRequest, textStatus, errorThrown){},  
            success:function(data, textStatus){} 
        };
        
        if(opt.error){  
            fn.error=opt.error;  
        };  
        
        if(opt.success){  
            fn.success=opt.success;  
        };  
          
        //扩展增强处理  
        var _opt = $.extend(opt,{  

        	error:function(response, textStatus, errorThrown){  
                //错误方法增强处理  
    			var errorData = $.parseJSON(response.responseText);
    			if (errorData){ 
    				swal({
        				title : errorData.msg,
        				type : "warning",
        				confirmButtonColor : "#DD6B55",});
    			}
    				//jConfirm(errorData.msg, '警告');
                fn.error(response, textStatus, errorThrown);  
            },  
            success:function(data, textStatus){ 
    			if (data.code!=1)
            	{
    				swal({
        				title : data.msg,
        				type : "warning",
        				confirmButtonColor : "#DD6B55",});
    				//jConfirm(data.msg, '警告');
            		return;
            	}
            	if (this.url.indexOf('showAlert=true')>-1)
            		{
	            		swal({
	        				title : data.msg,
	        				type : "warning",
	        				confirmButtonColor : "#DD6B55",});
            		//jAlert(data.msg, '提示');
            		}
                fn.success(data, textStatus);  
            }  
        });  
        _ajax(_opt);  
    };  
})(jQuery);