var currentPage=1;
var limit=5;
var sex;
var relation;
var student_name;
var student_code;
var parent_id;
var changeTel;

//获取列表数据
function loadSContent(){
	if(search_text==undefined) search_text="";
	$.myajax({
		url:"userAction/getParentOfMng",
		data:{start_id:(currentPage-1)*limit,limit:limit,page:currentPage,student_name:search_text},
		datatype:'json',
		success:function(data){
			spage(data);//分页
            }
	});
}

//分页
function spage(data){
	var result = data.result;
	if(result==null) return;
    var pageCount = Math.ceil(result.total/limit); //取到pageCount的值(把返回数据转成object类型)
    addSToWeb(data);
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
       	loadSContent();
        }
    };
    $("#page_pagintor").bootstrapPaginator(options);
    $("#page_pagintor").show();
}

function addSToWeb(data){
	$('#teacherList').empty();
	var result = data.result.data;
	$('#counttotal').text('共'+data.result.total+'人');
	for(var i in result) {
		var studentVO = result[i];
		appendSToWeb(studentVO);
	}
	//deleteParent();
}

function appendSToWeb(studentVO){
	if(studentVO.sex==0) sex='男';
	else if(studentVO.sex==1) sex='女';
	else sex='未设置';
	var p=studentVO.parent_list;
	var parentlist=eval(p);//转成集合对象
	var tr='';
	if(p==null){
		tr='<tr><td>'+studentVO.class_name+' '+studentVO.student_code+'</td><td>'+studentVO.student_name+'</td><td>'+sex+'</td><td>'+
		'<a href="javascript:;" onclick="_addItem(this)" class="btn btn-sm btn-req" value="'+
		studentVO.student_code+'" code="'+studentVO.student_name+'">添加家长</a></td></tr>';
	}else{
		tr='<tr><td>'+studentVO.class_name+' '+studentVO.student_code+'</td><td>'+studentVO.student_name+'</td><td>'+sex+'</td>'
		+'<td><a href="javascript:;" onclick="_addItem(this)" class="btn btn-sm btn-req" value="'+
		studentVO.student_code+'" code="'+studentVO.student_name+'">添加家长</a></td>'+
		'</tr><tr class="tr_do"><td>&nbsp;</td><td colspan="3">';
		for(var i in parentlist){
			dictvalue(parentlist[i]);
			tr=tr+'<div class="item"><div class="col-sm-2">'+relation+'</div><div class="col-sm-3">'+
			parentlist[i].parent_name+'</div><div class="col-sm-3">'+parentlist[i].phone+'</div>'+
			'<div class="col-sm-3"><a value="'+parentlist[i].parent_id+
			'" href="javascript:;" onclick="_editItem(this)" class="btn btn-sm btn-req" code="'+
			parentlist[i].user_id+'">编辑</a>'+
			'<a href="javascript:;" onclick="_delItem(this)" class="btn btn-sm btn-req" value="'+
			parentlist[i].parent_id+'">删除</a></div></div>';
		}
		tr=tr+'</td></tr>';
	}
	$('#teacherList').append(tr);
}

function dictvalue(parent){
	if(parent.relation=='003015')
		relation="家长";
	else if(parent.relation=='003015005')
		relation="父亲";
	else if(parent.relation=='003015010')
		relation='母亲';
	else if(parent.relation=='003015015')
		relation='爷爷';
	else if(parent.relation=='003015020')
		relation='奶奶';
	else if(parent.relation=='003015025')
		relation='外公';
	else if(parent.relation=='003015030')
		relation='外婆';
	else if(parent.relation=='003015035')
		relation='其他';
}

//搜索
function searchParent(){
	$('#search').on('click',function(){
		currentPage=1;
		search_text=$('#search_text').val();
		loadSContent();
	});
}

//添加家长或编辑家长的保存键
function _submitItem(obj){
	var $item = $(obj).parents("div.item");
	if($(">div",$item).size()<8){
		$item.prepend('<div class="col-sm-2" style="display:none"></div><div class="col-sm-3" style="display:none"></div><div class="col-sm-3" style="display:none"></div><div class="col-sm-3" style="display:none"><a href="javascript:;" onclick="_editItem(this)" class="btn btn-sm btn-req">编辑</a> <a href="javascript:;" onclick="_delItem(this)" class="btn btn-sm btn-req">删除</a></div>');
	}
	var rel = $("select>option:selected",$item).text(),
		name = $(">div",$item).eq(5).children('input').val().trim(),
		tel = $(">div",$item).eq(6).children('input').val().trim();
	var rela=$("select>option:selected",$item).val();
	if($("select",$item).val() == 0 || name == "" || tel == ""){
		layer.msg('请填写完整',{icon: 0});
		return false;
	}
	if(!tel.match(/^(13[0-9]|15[012356789]|17[0-9]|18[0-9]|14[57])[0-9]{8}$/)) {
		layer.msg('请填写正确的手机号码！',{icon: 0});
		return false;
	}
	$(">div",$item).eq(0).text(rel);
	$(">div",$item).eq(1).text(name.replace(/\s/g,''));//去除姓名中的空格
	$(">div",$item).eq(2).text(tel);
	$(">div",$item).each(function(index, el) {
		if(index<4){
			$(el).show();
		}else{
			$(el).remove();
		}
	});
	if(parent_id==0){//添加家长
	$.myajax({
		url:"userAction/addParent",
		datatype:"json",
		data:{relation:rela,student_code:student_code,student_name:student_name,parent_name:name,phone:tel},
		success:function(data){
			loadSContent();
		}
	});
	}else{//修改家长信息
		if (changeTel==tel) tel='';//手机号未改变
		 $.myajax({
				url:"userAction/updateParent",
				datatype:"json",
				data:{parent_id:parent_id,parent_name:name,phone:tel,relation:rela},
				success:function(data){
				}
			});
	}
}

//删除家长
function _delItem(obj){
	var parentid=$(obj).attr('value');
	layer.confirm('确认删除？', {
		btn: ['确定','取消'] //按钮
	}, function(){
		$(obj).parents("div.item").fadeOut(function(){
			var $tr = $(obj).parents("tr");
			$(this).remove();

			if($("div.item",$tr).size() == 0){
				$tr.remove();
			} 
		});
		$.myajax({
			url:"userAction/deleteChild",
			datatype:"json",
			data:{parent_id:parentid},
			success:function(data){
				layer.msg('删除成功', {icon: 1});
			}
		});
	});
}