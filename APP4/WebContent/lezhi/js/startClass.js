var is_active;
var grade_id;
var course;
var file_upload_action = localStorage.getItem('file_upload_action');
var contact_name;
var phone;
var start_date;
var end_date;
var remark;
var apply_start_date;
var apply_end_date;
var is_grab;
var team_count;
var apply_count;
var schedule_url;
var jsondata;
//兴趣班待选课程
function showCourse(){
    $.myajax({
        url:"dictAction/getDictSchoolList",
        datatype:"json",
        async:false,
        data:{dict_group:"015045",is_active:1},
        success:function(data){
            var result=data.result.data;
            for(var i in result){
                var li='<option value="'+result[i].dict_code+'">'+result[i].dict_value+'</option>';
                $('#course').append(li);
            }
            $('#radio3').click();
        }
    });
}

//保存新兴趣班
function save(){
    $('#save').on('click',function(){
        grade_id=$('#grade').val();
        course=$('#course').val();
        contact_name=$('#iName').val();
        phone=$('#teacher').val();
        start_date=$('#start_date').val();
        end_date=$('#end_date').val();
        remark=$('#remarkMsg').val();
        apply_start_date=$('#apply_start_date').val();
        apply_end_date=$('#apply_end_date').val();
        is_grab=$('input:radio[name="radio"]:checked').val();
        team_count=$('#teamCount').val();
        apply_count=$('#applyCount').val();
        schedule_url=$('#pic').val();
        if(contact_name=='' || course=='' ||  phone=='' || grade_id=='' || start_date=='' || end_date=='' ||
            apply_start_date=='' || apply_end_date=='' || team_count=='' || apply_count==''){
            layer.msg("请将信息填写完整！",{icon:0});
        } else if ($('input:radio[name="radio2"]:checked').val()==1 &&
            (schedule_url==undefined || schedule_url=='')){
            layer.msg('请上传课程表图片！',{icon:0});
        } else if ($('input:radio[name="radio2"]:checked').val()==0 &&
            ($('#schedule').val()==undefined || $('#schedule').val()=='')){
            layer.msg('请定制课程表!',{icon:0});
        } else if (is_grab==1 && apply_count>team_count){
            layer.msg('抢报模式下，可报名数不应大于班级总人数！',{icon:0});
        } else{
            if ($('input:radio[name="radio2"]:checked').val()==1 ){
               jsondata='';
            } else if ($('input:radio[name="radio2"]:checked').val()==0){
               schedule_url='';
            }
            if (contact_id==null) {//新增
                var teacher_name=$('#teacher option:selected').text();
                $.myajax({
                    url: "contactAction/addContact",
                    datatype: "json",
                    data: {
                        contact_name: contact_name, user_type: '003010', course: course, phone: phone,
                        grade_id: grade_id, start_date: start_date, end_date: end_date, remark: remark,
                        apply_start_date: apply_start_date, apply_end_date: apply_end_date,
                        is_grab: is_grab, team_count: team_count, apply_count: apply_count, schedule_url: schedule_url,
                        schedule: jsondata,teacher_name:teacher_name
                    },
                    success: function (data) {
                        layer.msg('保存成功', {icon: 1});
                        window.location.href = 'lezhi/allInterestClass.jsp';
                    }
                });
            } else {//修改
                $.myajax({
                    url:'contactAction/updateInterestClass',
                    datatype:"json",
                    data:{contact_id:contact_id,contact_name:contact_name,course:course,phone:phone,grade_id:grade_id,
                    start_date:start_date,end_date:end_date,remark:remark,apply_start_date:apply_start_date,apply_end_date:apply_end_date,
                    is_grab:is_grab,team_count:team_count,apply_count:apply_count,schedule_url:schedule_url,schedule:jsondata},
                    success:function (data) {
                        layer.msg('修改成功！',{icon:1});
                        window.location.href='lezhi/allInterestClass.jsp';
                    }
                });
            }
        }
    });
}

//显示年级信息
function showGradeInfo(){
    $.myajax({
        url:"gradeAction/getGradeList",
        type:'post',
        async:false,
        success:function(data){
            var result = data.result.data;
            for(var i in result) {
                var gradeVO = result[i];
                var li='<option value="'+gradeVO.grade_id+'">'+gradeVO.grade_name+'</option>';
                $('#grade').append(li);
            }
        }
    });
}

//显示全校教师
function showAllTeacher(){
    $.myajax({
        url:"userAction/getAllTeacher",
        datatype:"json",
        data:{is_filtered:0},
        success:function(data){
            var result=data.result.data;
            for (var i in result){
                var option='<option value="'+result[i].phone+'">'+result[i].teacher_name+'</option>'
                $('#teacher').append(option);
            }
            showContent();
        }
    });
}

//课程表图片
function schedulePicture(){
    $('#radio3').on('click',function () {
        $('#schedule').hide();
        $('#pic').show();
    });
}

//选择自定义课程表
function schedule(){
    $('#radio4').on('click',function(){
        $('#schedule').show();
        $('#pic').hide();
    });
}

//上传图片
function iploadPicture() {
    $("#pic").on("click", function() {
        $("#upload_pic").click();
    });
    $('#upload_pic').unbind("change").on('change',function(){
        var formData = new FormData(document.getElementById("Form"));
        formData.append("school_id", localStorage.getItem("school_id"));
        $.myajax({
            type: "POST",
            url: file_upload_action,
            cache: false,
            dataType : "JSON",
            data: formData,
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            processData: false, // 告诉jQuery不要去处理发送的数据
            success: function (result) {
                var item = result.result.data;
                cleanInput();
                $('#pic').attr('value',item[0].file_url);
                layer.msg('图片上传成功！',{icon:1});
            }
        });
    });
}

//清空input，避免同名文件提交不执行
function cleanInput(){
    $('#upload_pic').replaceWith('<input id="upload_pic" name="image" type="file" style="opacity:0;position:absolute;left:0px;top:0px;width:0px;height:0px"/>');
    $('#upload_pic').unbind("change").on('change',function(){
        var formData = new FormData(document.getElementById("Form"));
        formData.append("school_id", localStorage.getItem("school_id"));
        $.myajax({
            type: "POST",
            url: file_upload_action,
            cache: false,
            dataType : "JSON",
            data: formData,
            contentType: false, // 告诉jQuery不要去设置Content-Type请求头
            processData: false, // 告诉jQuery不要去处理发送的数据
            success: function (result) {
                var item = result.result.data;
                cleanInput();
                $('#pic').attr('value',item[0].file_url);
                layer.msg('图片上传成功！',{icon:1});
            }
        });
    });
}

//创建自定义课程表
function createSchedule(){
    $('#schedule').on('click',function(){
        grade_id=$('#grade').val();
        course=$('#course').val();
        contact_name=$('#iName').val();
        phone=$('#teacher').val();
        start_date=$('#start_date').val();
        end_date=$('#end_date').val();
        remark=$('#remarkMsg').val();
        apply_start_date=$('#apply_start_date').val();
        apply_end_date=$('#apply_end_date').val();
        is_grab=$('input:radio[name="radio"]:checked').val();
        team_count=$('#teamCount').val();
        apply_count=$('#applyCount').val();
        schedule_url=$('#pic').val();
        window.location.href='lezhi/createSchedule.jsp?course="'+course+'"&contact_name='+contact_name+'&phone="'+
            phone+'"&grade_id="'+grade_id+'"&start_date="'+start_date+'"&end_date="'+end_date+'"&remark="'+remark+
            '"&apply_start_date="'+apply_start_date+'"&apply_end_date="'+apply_end_date+'"&is_grab="'+is_grab+
            '"&team_count="'+team_count+'"&apply_count="'+apply_count+'"&jsondata="'+jsondata+'"&contact_id='+
            contact_id+'&schedule_url="'+schedule_url+'"';
    });
}

//初始化
function initdata(){
    if(jsondata!='null'&& jsondata!=''){
        $('#scheduleTable').empty();
        var dataset = $.parseJSON(jsondata.replace(/'/g, '"'));
        for (var i in dataset){
            var li='<div class="qcontent_item">' +
                '<input value="'+dataset[i].class_date+'" class="form-control" placeholder="选择日期" ' +
                'onclick="laydate({istime: true, format: \'YYYY-MM-DD hh:mm\',min: laydate.now()})"> ' +
                '<input type="text" value="'+dataset[i].place+'" placeholder="输入地点"/></div>';
            $('#scheduleTable').append(li);
        }
        var li1='<div class="qcontent_item">' +
            '<input class="form-control" placeholder="选择日期" onclick="laydate({istime: true, format: \'YYYY-MM-DD hh:mm\',min: laydate.now()})"> <input type="text" value="" placeholder="输入地点"/> <button class="btn btn-default btn-add">添加</button> </div>';
        $('#scheduleTable').append(li1);
    }
}

//创建自定义课程表
function addSchedule(){
    $('.btn-add').on('click',function(){
        var date=$(this).parent().find('input:first').val();
        var place=$(this).parent().find('input:last').val();
        if (date=='' || place==''){
            layer.msg('请填写完整！',{icon:0});
            return false;
        }
        $(this).hide();
        var li='<div class="qcontent_item">' +
            '<input class="form-control" placeholder="选择日期" onclick="laydate({istime: true, format: \'YYYY-MM-DD hh:mm\',min: laydate.now()})"> <input type="text" value="" placeholder="输入地点"/> <button class="btn btn-default btn-add">添加</button> </div>';
        $('#scheduleTable').append(li);
        addSchedule();
    });
}

//保存自定义课程表
function saveSchedule(){
    $('.btn-save').on('click',function(){
        var date=$('#scheduleTable div:last input:first').val();
        var place=$('#scheduleTable div:last input:last').val();
        if (date!='' && place!='') {
            $('#scheduleTable div:last .btn-add').click();
        }
        jsondata='null';
        for (var i=0;i<$('#scheduleTable div').size()-1;i++){
            var class_date=$('#scheduleTable div').eq(i).find('input:first').val();
            var place=$('#scheduleTable div').eq(i).find('input:last').val();
            if (jsondata=='null'){
                jsondata='[{\'class_date\':\''+class_date+'\',\'place\':\''+place+'\'}';
            } else {
                jsondata=jsondata+',{\'class_date\':\''+class_date+'\',\'place\':\''+place+'\'}';
            }
        }
        jsondata=jsondata+']';
        window.location.href='lezhi/startClass.jsp?course="'+course+'"&contact_name="'+contact_name+'"&phone="'+
            phone+'"&grade_id="'+grade_id+'"&start_date="'+start_date+'"&end_date="'+end_date+'"&remark="'+remark+
            '"&apply_start_date="'+apply_start_date+'"&apply_end_date="'+apply_end_date+'"&is_grab="'+is_grab+
            '"&team_count="'+team_count+'"&apply_count="'+apply_count+'"&jsondata="'+jsondata+'"&contact_id='+
            contact_id+'&schedule_url="'+schedule_url+'"';
    });
}

function showContent(){
    if (jsondata!=null || schedule_url!=null) {
        $('#course').val(course);
        $('#iName').val(contact_name);
        $('#teacher').val(phone);
        $('#grade').val(grade_id);
        $('#start_date').val(start_date);
        $('#end_date').val(end_date);
        $('#remarkMsg').val(remark);
        $('#apply_start_date').val(apply_start_date);
        $('#apply_end_date').val(apply_end_date);
        $('#grab input[value=' + is_grab + ']').attr("checked", true);
        $('#teamCount').val(team_count);
        $('#applyCount').val(apply_count);
    }
    if (jsondata!=null && jsondata!='') {
        $('#schedule').val(jsondata);
        $('#radio4').click();
        $('#pic').val(schedule_url);
    } else if (schedule_url!=null){
        $('#pic').val(schedule_url);
        $('#radio3').click();
    }
}

//blur可报名人数input失去焦点后判断人数
function applyCount(){
    $('#applyCount').on('blur',function(){
        team_count=$('#teamCount').val();
        apply_count=$('#applyCount').val();
        is_grab=$('input:radio[name="radio"]:checked').val();
        if (is_grab==1 && apply_count>team_count) layer.msg('抢报模式下，可报名数不应大于班级总人数！',{icon:0});
    });
}

//判断班级名字是否重复
function checkContactName(){
    $('#iName').on('blur',function(){
        contact_name=$('#iName').val();
        if (contact_name!='') {
            $.myajax({
                url: 'contactAction/getInterestByName',
                datatype: 'json',
                data: {contact_name:contact_name},
                success:function(data){
                    if (data.result.data.length>0)
                        layer.msg('该兴趣班名已被使用过！',{icon:0});
                }
            });
        }
    });
}