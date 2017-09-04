var address;

//获取学校基本信息
function getInfo(){
    $.myajax({
        url:'schoolAction/getSchoolById',
        dataType:'json',
        async:false,//同步
        data:{'school_id':localStorage.getItem("school_id")},
        success:function (data) {
            var result=data.result.data;
            $('#suggestId').val(result.address);//公司地址
            $('#range').attr('value',result.attend_range);
            $('#pos').text('坐标：'+result.longitude+'，'+result.latitude);
            longitude=result.longitude;
            latitude=result.latitude;
            address=result.address;
        }
    });
}

//保存定位信息
function save(){
    $('#save').on('click',function(){
        var location=$('#suggestId').val();
        var range=$('#range').val();
        if(location=='' || range==''){
            layer.msg('请填写定位地址和范围！',{icon:0});
            return false;
        }
        $.myajax({
            url:'schoolAction/addLocationInfo',
            dataType:'json',
            data:{'address':location,'longitude':longitude,'latitude':latitude,'attend_range':range},
            success:function (data) {
                layer.msg('保存成功！',{icon:1});
            }
        });
    });
}