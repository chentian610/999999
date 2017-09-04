var gradeID;
var scoretype;
var counttype;
var sumtype;
var group_id;
var team_id;
var attend_item;
var start_date;
var end_date;
var team_type;

//显示年级信息
function showGradeInfo(){
    $.myajax({
        url:"gradeAction/getGradeList",
        type:'post',
        success:function(data){
            $('#grade').empty();
            var result = data.result.data;
            for(var i in result) {
                var gradeVO = result[i];
                var div='<div class="radio radio-info radio-inline" id="grade'+gradeVO.grade_id+'">' +
                    '<input type="radio" id="g'+gradeVO.grade_id+'" value="'+gradeVO.grade_id+'" name="grade">' +
                    '<label for="g'+gradeVO.grade_id+'">'+gradeVO.grade_name+'</label></div>';
                $('#grade').append(div);
            }
             gradeID=result[0].grade_id;
             showClassInfo();//显示班级信息
            changeGrade();
        }
    });
}

//显示班级信息
function showClassInfo(){
    $.myajax({
        url:"classAction/getClassList",
        datatype:"json",
        type:'post',
        data:{grade_id:gradeID},
        success:function(data){
            $('#classname').empty();
            var result=data.result.data;
            for(var i in result){
                var classVO=result[i];
                var div='<div class="radio radio-info radio-inline">'+
                    '<input type="radio" id="c'+classVO.class_id+'" value="'+classVO.class_id+'" name="class">' +
                    '<label for="c'+classVO.class_id+'">'+classVO.class_name+'</label></div>';
                $('#classname').append(div);
            }
        }
    });
}

function changeGrade(){
    $('#grade').unbind('click').on("click",'div',function(){
        gradeID=$(this).attr('id').substring(5);
        $('#classname').empty();
        showClassInfo();
    });
}

//统计类型
function showType(){
    //加载模板
    $.myajax({
        url:"dictAction/getDictSchoolList?dict_group="+dict_group,
        datatype:"json",
        success:function(data){
            var result = data.result.data;
            for(var i in result) {
                var dictVO=result[i];
                if (dict_group=='012') {
                    if (module_code!=dictVO.other_field) continue;
                }
                var div='<div class="radio radio-info radio-inline box-active">' +
                    '<input type="radio" id="d'+dictVO.dict_code+'" value="'+dictVO.dict_code+'" name="range">' +
                    '<label for="d'+dictVO.dict_code+'">'+dictVO.dict_value+'</label></div>';
                $('#type').append(div);
            }
            if (module_code=='009021')//寝室
                $('#type').append('<div class="radio radio-info radio-inline box-active">' +
                    '<input type="radio" id="d012015" value="012015" name="range">' +
                    '<label for="d012015">考勤</label></div>');
        }
    });
}

//显示该校寝室
function showBedroom(){
    $.myajax({
        url:"bedroomAction/showBedroom",
        success:function(data){
            var result=data.result.data;
            for(var i in result){
                var bed=result[i];
                var div='<div class="radio radio-info radio-inline" id="b'+bed.bedroom_id+'">' +
                    '<input type="radio" id="bed'+bed.bedroom_id+'" value="'+bed.bedroom_id+'" name="grade">' +
                    '<label for="bed'+bed.bedroom_id+'">'+bed.bedroom_name+'</label></div>';
                $('#bed').append(div);
            }
        }
    });
}

//统计
function exporttongji(){
    $('#exporttongji').on('click',function(){
        attend_item=$("input[name='range']:checked").val();
        if (attend_item==undefined && module_code!='009025' && module_code!='009008' && module_code!='009027') {layer.msg('请选择统计类别！',{icon:0}); return false;}
        if (module_code=='009005') scoretype='012015';
        else if (module_code=='009025') {
            scoretype='012020';
            attend_item='';
        }
        else {
            scoretype=attend_item;
            attend_item='';
        }
        counttype=$("input[name='count']:checked").val();
        sumtype=$("input[name='time']:checked").val();
        if (sumtype==undefined) {layer.msg('请选择统计时间！',{icon:0}); return false;}
        if (sumtype=='027') {//自定义
            sumtype='';
            start_date=$('#start_date').val();
            end_date=$('#end_date').val();
            if (start_date=='' || end_date=='') {layer.msg('请选择统计时间！',{icon:0}); return false;}
        }
        if (counttype==undefined && module_code!='009027') {layer.msg('请选择统计范围！',{icon:0}); return false;}
        if (counttype=='028005' && module_code!='009021'){
            group_id=$("input[name='grade']:checked").val();
            team_id=$("input[name='class']:checked").val();
            if (group_id==undefined || team_id==undefined){layer.msg('请选择统计班级！',{icon:0}); return false;}
            if (module_code=='009023' && scoretype=='012010') {layer.msg('卫生不能统计到个人！',{icon:0}); return false;}
        } else if ($("input[name='count']:checked").attr('id')=='inlineRadio2'){
            group_id=$("input[name='grade']:checked").val();
            team_id=0;
            if (group_id==undefined) {layer.msg('请选择统计年级！',{icon:0}); return false;}
        } else if ($("input[name='count']:checked").attr('id')=='inlineRadio5'){
            group_id=0;
            team_id=$("input[name='grade']:checked").val();
            if (team_id==undefined) {layer.msg('请选择统计寝室！',{icon:0}); return false;}
        } else {
            group_id=0;
            team_id=0;
        }
        if (module_code=='009021') team_type='011010';
        else team_type='011005';
        $.myajax({
            url:'excelAction/exportTongJi',
            dataType:'json',
            data:{'team_type':team_type,'score_type':scoretype,'count_type':counttype,'module_code':module_code,
                'sum_type':sumtype,'team_id':team_id,'group_id':group_id,'attend_item':attend_item,
                'start_date':start_date,'end_date':end_date},
            success:function(data){
                var file_name=data.result.data.path;
                var title=data.result.data.title;
                $('#download').attr('href','excelAction/download?path='+file_name+'&title='+title);
                $('#dl').click();
            }
        });
    });
}