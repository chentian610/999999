var currentPage=1;
var limit=4;
var contact_id;
var contact_name;

//教师分组(初次进入，加两个分组)
function insertGroup(){
	$.myajax({
		url:"contactAction/getContactGroupListOfManager",
		datatype:"json",
		data:{user_type:"003005"},
		success:function(data){
			var result=data.result.data;
			if(result.length==0){
					$.myajax({
		            	url:"contactAction/addContact",
		            	datatype:"json",
		            	data:{contact_name:'学校管理层',user_type:'003005'},
		            	success:function(data){
		            	}
		            });
					$.myajax({
		            	url:"contactAction/addContact",
		            	datatype:"json",
		            	data:{contact_name:'行政管理组',user_type:'003005'},
		            	success:function(data){
		            		var contactid=data.result.data.contact_id;
		            		$.myajax({
		            			url:"classAction/getTeacherListOfManager",
		            			data:{teacher_name:''},
		            			datatype:'json',
		            			type:'post',
		            			success:function(data){
		            				var result=data.result.data;
		            				for(var i=0;i<result.length;i++){
		            					var jsondata=[{'user_id':result[i].user_id,'user_type':'003005',
		            						'user_name':result[i].teacher_name,'phone':result[i].phone}];
		            					$.myajax({
		        							url:"contactAction/addContactListByGroup",
		        							datatype:"json",
		        							type:'post',
		        							data:{contact_id:contactid,item_list:JSON.stringify(jsondata)},
		        							success:function(data){
		        							}
		        						});
		            				}
		            				showGroup();
		            	            }
		            		});
		            	}
		            });
			}else{
				showGroup();
			}
		}
	});
}

//显示分组
function showGroup(){
	$.myajax({
		url:"contactAction/getContactGroupListOfManager",
		datatype:"json",
		data:{user_type:"003005"},
		success:function(data){
			var result=data.result.data;
			for(var i in result){
				var li='<li id="c'+result[i].contact_id+'" value="'+result[i].contact_name+'"><a>'+result[i].contact_name+'</a></li>';
				$('#groupmenu').append(li);
			}
			showGroupPerson();
			$('#groupmenu li:first').click();
		}
	});
}

//显示分组人员
function showGroupPerson(){
	$('#groupmenu').on('click','li',function(){
		$('#groupmenu li a').removeClass('active');
		$(this).find('a').addClass('active');
		contact_id=$(this).attr('id').substring(1);
		contact_name=$(this).attr('value');
		groupPerson();
	});
}

//显示分组人员
function groupPerson(){
	$.myajax({
		url:"classAction/getTeacherListOfGroup",
		datatype:'json',
		type:'post',
		data:{contact_id:contact_id,user_type:'003005',start_id:(currentPage-1)*limit,limit:limit,page:currentPage},
		success:function(data){
			page(data);//分页
		}
	});
}

//分页
function page(data){
	var result = data.result;
	if(result==null) return;
    var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
    addToWeb(data);
    if (pageCount<2) {
    	$("#page_pagintor").hide();
    	return;
    }
    var options = {
        bootstrapMajorVersion: 3, //版本
        currentPage: currentPage, //当前页数
        totalPages: pageCount, //总页数
        alignment:"center",
        itemTexts: function (type, page, current) {
            switch (type) {
                case "first":
                    return "首页";
                case "prev":
                    return "上一页";
                case "next":
                    return "下一页";
                case "last":
                    return "末页";
                case "page":
                    return page;
            }
        },//点击事件，用于通过Ajax来刷新整个list列表
        onPageClicked: function (event, originalEvent, type, page) {
       	currentPage=page;
       	groupPerson();
        }
    };
    $("#page_pagintor").bootstrapPaginator(options);
    $("#page_pagintor").show();
}

//加载列表
function addToWeb(data){
	$('#teacherList').empty();
	var result = data.result.data;
	for(var i in result) {
		var teacherVO = result[i];
		appendToWeb(teacherVO);
	}
	deleteTeacher();
}

function appendToWeb(teacherVO){
	if(teacherVO.sex==0) teachersex='男'; else teachersex='女';
	var tr='<tr id="phone'+teacherVO.phone+'"><td>'+teacherVO.teacher_name+'</td><td>'+teacherVO.phone+'</td><td>'
	+teachersex+'</td><td>'+teacherVO.duty_name+'</td><td><a class="btn btn-sm btn-req delete" id="p'+
	teacherVO.phone+'">删除</a></td></tr>';	
	$('#teacherList').append(tr);
}

//增加分组成员（跳转页面）
function addGroupTeacher(){
	$('#addGroupTeacher').on('click',function(){
		window.location.href='lezhi/addGroupTeacher.jsp?contact_name='+contact_name+'&contact_id='+contact_id;
	});
}

//删除组中成员
function deleteTeacher(){
	$('.delete').on('click',function(){
		var phone=$(this).attr('id').substring(1);
		$.myajax({
			url:"contactAction/deleteContactByGorup",
			datatype:"json",
			data:{contact_id:contact_id,phone:phone},
			success:function(data){
				$('#phone'+phone).remove();
			}
		});
	});
}