var tempJson;
function initData(){ 
	//加载模板
	$.myajax({
		url:"dictAction/getDictSchoolList?dict_group="+dict_group,
		datatype:"json",
		success:function(data){
			var result = data.result.data;
			for(var i in result) {
				appendToWeb( result[i]);
			}
			bindBtnAddClick();
		} 
	});
} 

function appendToWeb(VO){
	if (VO.is_active==1) {
		var li = '<li class="clear dd-item" data-id="' + VO.dict_code + '" data-dict_code="'+VO.dict_code+'" id="li_' + VO.dict_code + '" data-dict_value="'
			+ VO.dict_value + '"><div class="dd-handle clear"><span>' + VO.dict_value + '</span><button  data-dict_code="' +
			VO.dict_code + '" data-dict_value="' + VO.dict_value + '" class="btn btn-primary btn-sm btn-del pull-right" type="button"   onclick=deleteAttend("' +
			VO.dict_code + '","'+VO.dict_value+'")><i class="fa">禁用</i></button><button data-dict_code="' + VO.dict_code + '" data-dict_value="' +
			VO.dict_value + '" class="btn btn-primary btn-sm btn-rename pull-right" type="button"   ><i class="fa">重命名</i></button>'
			+ '</div></li>';
		$('#mainol').append(li);
	} else {
		var li = '<li class="clear dd-item" data-id="' + VO.dict_code + '" data-dict_code="'+VO.dict_code+'" id="li_' + VO.dict_code + '" data-dict_value="'
			+ VO.dict_value + '"><div class="dd-handle clear"><span>' + VO.dict_value + '</span><button  data-dict_code="' +
			VO.dict_code + '" data-dict_value="' + VO.dict_value + '" class="btn btn-primary btn-sm btn-del pull-right" type="button"   onclick=activeAttend("' +
			VO.dict_code + '","'+VO.dict_value+'") style="background:#d9534f;border:#d9534f; "><i class="fa">启用</i></button><button data-dict_code="' + VO.dict_code + '" data-dict_value="' +
			VO.dict_value + '" class="btn btn-primary btn-sm btn-rename pull-right" type="button"   ><i class="fa">重命名</i></button>'
			+ '</div></li>';
		$('#mainol').append(li);
	}
}

//显示添加栏目框
function newxiangmu(){
	$('#new-add-btn').on('click',function(){
		var li='<li id="iptLiEetpy"><input id="iptDictName" type="text" placeholder="请输入项目名称" class="form-control" style="width:70%;float:left;">'
			+'<button id="btnsave" type="button" class="btn btn-primary" style="width: 13%;left:800px;height: 34px;" >确定</button>'
			+'<button onclick="bindEmptyLi()" type="button" class="btn btn-primary" style="width: 13%;left:950px;height: 34px;" >取消</button></li>';
		$('#newObject').append(li);
		bindBtnSaveClick();
	});
}

function bindEmptyLi(){
	$('#iptLiEetpy').remove();
}

//添加栏目
function bindBtnSaveClick(){  
	$("#btnsave").on('click', function() {
		$.myajax({
			url:"dictAction/addDictSchool",
			datatype:"json",
			data: {dict_value:$("#iptDictName").val(),dict_group:dict_group},
			success:function(data){
				$('#iptLiEetpy').remove();
				var VO = data.result.data;
				appendToWeb(VO);
				bindBtnAddClick();	
			}
		});
	});
} 

function bindBtnAddClick(){
	$(".pull-right").on("mousedown",function(e) {
		e.stopPropagation();//阻止冒泡 
		attend_code=$(this).attr('data-dict_code');
		attend_name=$(this).attr('data-dict_value');
	});
}

function bindInputClick(){
	$(".input-text").on("mousedown",function(e) {
		e.stopPropagation();//阻止冒泡 
	});
}

//删除考勤项目
function deleteAttend(code,value){
	layer.confirm('确认禁用？', {
		btn: ['确定','取消'] //按钮
	}, function(){
		if (dict_group=='015045') {
			$.myajax({
				url: "dictAction/forbiddenDictSchool",
				datatype: "json",
				data: {dict_code: code},
				success: function (data) {
					$('#li_' + code).replaceWith('<li class="clear dd-item" data-id="' + code + '" data-dict_code="'+code+'" id="li_' + code + '" data-dict_value="'
						+ value + '"><div class="dd-handle clear"><span>' + value + '</span><button  data-dict_code="' +
						code + '" data-dict_value="' + value + '" class="btn btn-primary btn-sm btn-del pull-right" type="button"   onclick=activeAttend("' +
						code + '","' + value + '") style="background:#d9534f;border:#d9534f; "><i class="fa">启用</i></button><button data-dict_code="' + code + '" data-dict_value="' +
						value + '" class="btn btn-primary btn-sm btn-rename pull-right" type="button"   ><i class="fa">重命名</i></button>'
						+ '</div></li>');
					layer.msg('禁用成功!', {icon: 1});
					bindBtnAddClick();
				}
			});
		} else {
			$.myajax({
				url: "dictAction/forbiddenDictSchool",
				datatype: "json",
				data: {dict_code: code},
				success: function (data) {
					$('#li_' + code).replaceWith('<li class="clear dd-item" data-id="' + code + '" data-dict_code="'+code+'" id="li_' + code + '" data-dict_value="'
						+ value + '"><div class="dd-handle clear"><span>' + value + '</span><button  data-dict_code="' +
						code + '" data-dict_value="' + value + '" class="btn btn-primary btn-sm btn-del pull-right" type="button"   onclick=activeAttend("' +
						code + '","' + value + '") style="background:#d9534f;border:#d9534f; "><i class="fa">启用</i></button><button data-dict_code="' + code + '" data-dict_value="' +
						value + '" class="btn btn-primary btn-sm btn-rename pull-right" type="button"   ><i class="fa">重命名</i></button>'
						+ '</div></li>');
					layer.msg('禁用成功!', {icon: 1});
					bindBtnAddClick();
				}
			});
		}
	});
}

//启用
function activeAttend(code,value){
	$.myajax({
		url:"dictAction/updateDictSchool",
		datatype:"json",
		data:{dict_code:code,is_active:1},
		success:function(data){
			$('#li_'+code).replaceWith('<li class="clear dd-item" data-id="' + code + '" data-dict_code="'+code+'" id="li_' + code + '" data-dict_value="'
				+ value + '"><div class="dd-handle clear"><span>' + value + '</span><button  data-dict_code="' +
				code + '" data-dict_value="' + value + '" class="btn btn-primary btn-sm btn-del pull-right" type="button"   onclick=deleteAttend("' +
				code + '","'+value+'")><i class="fa">禁用</i></button><button data-dict_code="' + code + '" data-dict_value="' +
				value + '" class="btn btn-primary btn-sm btn-rename pull-right" type="button"   ><i class="fa">重命名</i></button>'
				+ '</div></li>');
			layer.msg('启用成功!', {icon: 1});
			bindBtnAddClick();
		}
	});
}

//重命名
function updateAttendName(){
	$(".btn-qd").unbind('click').on("click",function(){
		if($('.input-text').val() == ''){
			layer.msg('请输入类型名称',{icon:0});
			return;
		}
		var dictcode=$(this).attr('data-dict_code');
		var dictvalue=$('.input-text').val();
		var i=$(this);
		$.myajax({
			url:"dictAction/updateDictSchool",
			datatype:"json",
			data:{dict_code:dictcode,dict_value:dictvalue},
			success:function(data){
				$('.input-text').remove().val('');
		        i.parent().parent().find('span').text(dictvalue);
		        $(".btn-qd").unbind('click');//避免后续执行错乱
		        i.text('重命名').removeClass('btn-qd pull-right').addClass('btn-rename pull-right');
		        $('.btn-qx').remove();
			}
		});
	});
}

//排序
function updateAttendSort(jsonStr){
	$.myajax({
		url:"dictAction/updateDictSchoolSort",
		datatype:"json",
		data: {json_array:jsonStr,dict_group:dict_group},
		success:function(data){
		}
	});
}