var currentPage=1;
var limit=4;
var contact_id;
var contact_name;
var search_text;

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
}

function appendToWeb(teacherVO){
	var tr='<tr><td><input type="checkbox" name="teacher" id="phone'+teacherVO.phone+'" code="1"></td><td>'+teacherVO.teacher_name+'</td><td>'+teacherVO.phone+
	'</td><td>'+teacherVO.duty_name+'</td><td><a class="btn btn-normal"><i class="fa fa-bars">'+
	'</i></a></td></tr>';
	$('#teacherList').append(tr);
}

//显示全体教师
function showAllTeacher(){
	if(search_text==undefined) search_text="";
	$.myajax({
		url:"classAction/getTeacherListOfManagerGroup",
		data:{start_id:(currentPage-1)*limit,limit:limit,page:currentPage,teacher_name:search_text,contact_id:contact_id},
		datatype:'json',
		type:'post',
		success:function(data){
			pages(data);//分页
            }
	});
}

//分页
function pages(data){
	var result = data.result;
	if(result==null) return;
    var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
    addAllToWeb(data);
    if (pageCount<2) {
    	$("#page_pagintor1").hide();
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
       	showAllTeacher();
        }
    };
    $("#page_pagintor1").bootstrapPaginator(options);
    $("#page_pagintor1").show();
}

function addAllToWeb(data){
	$('#allTeacher').empty();
	var result = data.result.data;
	for(var i in result) {
		var teacherVO = result[i];
		appendAllToWeb(teacherVO);
	}
}

function appendAllToWeb(teacherVO){
	var tr='<tr><td><input type="checkbox" name="aTeacher" id="phone_'+teacherVO.phone+'" code="'+
	teacherVO.user_id+'_'+teacherVO.teacher_name+'"></td><td>'+teacherVO.teacher_name+'</td><td>'+
	teacherVO.phone+'</td><td>'+teacherVO.duty_name+'</td><td><a class="btn btn-normal">'+
	'<i class="fa fa-bars"></i></a></td></tr>';
	$('#allTeacher').append(tr);
}

//搜索
function search(){
	$('#search').on('click',function(){
		currentPage=1;
		search_text=$('#teachername').val();
		showAllTeacher();
	});
}

//全选
function allSelect(){
	$('#teacherListSelect').on('click',function(){
		$('#teacherList input').prop({"checked":true});
	});
	$('#allTeacherSelect').on('click',function(){
		$('#allTeacher input').prop({"checked":true});
	});
}

//全选删除
function allDelete(){
	$('#delete').on('click',function(){
		if ($(":checkbox[name=teacher]:checked").size() == 0 && $(":checkbox[name=aTeacher]:checked").size() == 0) {
			alert("请至少选择一条记录进行删除！"); return false;
		}
		for(var i=0;i<$('[name=teacher]').length;i++){//遍历所有name为teacher的元素
			 if($('[name=teacher]')[i].checked==true) {
				 var phone=$('[name=teacher]')[i].id.substring(5);
				 oneDelete(phone);//为了多次执行success中的语句
			 }
		}
		$(":checkbox[name=aTeacher]:checked").each(function(){
			var phone =$(this).attr("id");
			$('#'+phone).parent().parent().remove();//8888888888888888
		});
	});
}

//为了多次执行success中的语句
function oneDelete(phone){
		$.myajax({
			url:"contactAction/deleteContactByGorup",
			datatype:"json",
			data:{contact_id:contact_id,phone:phone},
			success:function(data){
				$('#phone'+phone).parent().parent().remove();
			}
		});
}

//添加到左侧
function addLeft(){
	$('#addLeft').on('click',function(){
		if ($(":checkbox[name=aTeacher]:checked").size() == 0) {
			alert("您还没有选择教师哦！"); return false;
		}
		$(":checkbox[name=aTeacher]:checked").each(function(){
			var phone =$(this).attr("id").substring(6);
			//var a=$('#teacherList input#phone_'+phone).attr('code');//用于判断该组是否已经有该教师
		//	var b=$('#teacherList input#phone'+phone).attr('code');
		//	if(a==undefined && b==undefined){
			 var tr='<tr>'+$('#phone_'+phone).parent().parent().html()+'</tr>';
			 $('#teacherList').append(tr);
			 $(this).parent().parent().remove();
		});
		$('#allTeacher input').prop({"checked":false});
	});
}

//保存添加结果
function save(){
	$('#save').on('click',function(){
		if($('#teacherList :checkbox[name=aTeacher]').size()==0){
			window.location.href='lezhi/teacherGroup.jsp';
		}else{
			$('#teacherList :checkbox[name=aTeacher]').each(function(){
				var phone =$(this).attr("id").substring(6);
				var arr=$(this).attr('code').split('_');
				var user_id=arr[0];
				var user_name=arr[1];
				var jsondata=[{'user_id':user_id,'user_type':'003005','user_name':user_name,'phone':phone}];
				$.myajax({
					url:"contactAction/addContactListByGroup",
					datatype:"json",
					type:'post',
					data:{contact_id:contact_id,item_list:JSON.stringify(jsondata)},
					success:function(data){
					}
				});
			});
			window.location.href='lezhi/teacherGroup.jsp';
		}
	});
}