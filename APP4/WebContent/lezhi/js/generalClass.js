var eyear;

//显示班级
function showClass(){
	$.myajax({
		url:"classAction/getClassOfManager",
		datatype:'json',
		success:function(data){
			var result=data.result.data;
			var enrollment_year;
			for(var i in result){
				var item = result[i];
				if (enrollment_year!=item.enrollment_year) {
					enrollment_year = item.enrollment_year;
					var tr = null;
					if (item.is_graduate != 1){
						tr='<tr id="e'+enrollment_year+'" code="'+enrollment_year+
						'"><td><a href="javascript:;" onclick="_editItem(this)" class="btn btn-req btn-sm update">'+
						'修改</a></td><td><input class="btn btn-req btn-sm update" type="button" value="毕业" id="setIsGraduate'+item.grade_id+'"  onclick="setIsGraduate('+item.grade_id+')"/></td><td><span>'+enrollment_year+'年</span></td><td style="padding: 0">'
						+'<table class="table-inset"><tr id = "tr'+enrollment_year+'"><td>'+item.class_num+'班</td></tr></table></td></tr>';
					}
					$('#classmenu').prepend(tr);
				} else $('#tr'+enrollment_year).append('<td>'+item.class_num+'班</td>');
			  }
            }
	});
}

function setIsGraduate(grade_id){
	swal({
		title : "您确定执行毕业操作？",
		text : "请核实毕业信息后,执行毕业操作！",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#DD6B55",
		confirmButtonText : "毕业",
		closeOnConfirm : false
	}, function() {
		$.myajax({
			url:"classAction/setClassIsGraduateByGradeID",
			datatype:'json',
			data:{grade_id:grade_id},
			success:function(data){
				$('#setIsGraduate'+grade_id+'').attr('disabled','disabled');
				swal("毕业了！", "该年级毕业操作成功!", "success");
			}
		});
	});
}

//显示入学年份选择
function showEnrollment_year(){
	var date=new Date();
	var year=date.getFullYear();
	var month=date.getMonth()+1;
	if(month>7){
		for(var i=year-10;i<year+1;i++){
			$('#enrollment_year').append('<option>'+i+'</option>');
		}
	}else{
		for(var i=year-10;i<year;i++){
			$('#enrollment_year').append('<option>'+i+'</option>');
		}
	}
}

//添加班级
function save(){
	$('#save').on('click',function(){
		var e=$('#enrollment_year option:selected').val();
		var class_num=$('#classnum option:selected').val();
		if($('#enrollment_year option:selected').val()=='选择入学年份' || 
		$('#classnum option:selected').val()=='选择班级数量'){
		alert('请选择入学年份和班级数量！');
	}else{
		if($('#e'+e).attr('code')==undefined){
			$.myajax({
				url:'classAction/addSomeClass',
				datatype:'json',
				data:{enrollment_year:e,class_num:class_num},
				success:function(data){
					var tr='<tr id="e'+e+'" code="'+e+
					'"><td><a href="javascript:;" onclick="_editItem(this)" class="btn btn-req btn-sm update">'+
					'修改</a></td><td><input class="btn btn-req btn-sm update" type="button" value="毕业" id="setIsGraduate'+
						data.result.data[0].grade_id+'"  onclick="setIsGraduate('+data.result.data[0].grade_id+')"/></td>'+
						'<td><span>'+e+'年</span></td><td style="padding: 0">'+
					'<table class="table-inset"><tr>';
					var result=data.result.data;
					for(var i in result){
						tr=tr+'<td>'+result[i].class_num+'班</td>';
					}
					tr=tr+'</tr></table></td></tr>';
					$('#classmenu').prepend(tr);
					$("#nData").hide();
					$("#cBtn").show();
				}
			});
		}else{
			alert('该入学年份已经存在！');
		}
	}
	});
}

//修改班级信息
function _submitEdit(obj){
	var $item = $(obj).parents("tr");
	var $_editForm = $(">td:eq(4)",$item);
	var year = $("select:eq(0)",$_editForm).val(),
		classs = $("select:eq(1)",$_editForm).val();

	if(year == 0 || classs == 0){
		return false;
	}

	if(year!=eyear){
		if($('#e'+year).length>0){
			alert('该年级已经存在!');
			return false;
		}
	}
	$.myajax({
		url:'classAction/updateClassOfManager',
		datatype:'json',
		data:{enrollment_year:eyear,grade_num:year,class_num:classs},
		success:function(data){
		}
	});
	
	$(">td:eq(2)",$item).text(year + "年").show();
	$(">td:eq(3)",$item).find("tr").empty();
	for(var n=1;n<=classs;n++){
		$(">td:eq(3)",$item).find("tr").append("<td>"+n+"班</td>");
	}
	$(">td:eq(3)",$item).show();
	$(">td:first",$item).children('a').show();
	$(">td:eq(1)",$item).children('input').show();
	$(">td:eq(4)",$item).remove();
}