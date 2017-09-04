var year;
var currentPage=1;
var limit=20;

//年份选择
function showRegisterYear() {
    $.myajax({
        url:'studentRegisterAction/getRegisterDetail',
        success:function(data){
            $('#year').empty();
            var result=data.result.data;
            for (var i in result){
                var option='<option value="'+result[i].enrollment_year+'">'+result[i].enrollment_year+'年度新生报到总人数：'+result[i].count+'人</option>';
                $('#year').append(option);
            }
            changeYear();
            $('#year option:first').change();
        }
    });
}

function changeYear(){
    $('#year').on('change',function(){
        year=$(this).val();
        currentPage=1;
        showRegisterList();
    });
}

//新生报到人员
function showRegisterList() {
    $.myajax({
        url:'studentRegisterAction/getRegisterList',
        datatype:'json',
        data:{enrollment_year:year,start_id:(currentPage-1)*limit,limit:limit,page:currentPage},
        success:function (data) {
            spage(data);
        }
    });
}

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
            showRegisterList();
        }
    };
    $("#page_pagintor").bootstrapPaginator(options);
    $("#page_pagintor").show();
}

function addSToWeb(data){
    $('#studentList').empty();
    var result=data.result.data;
    for (var i in result) {
        var li='<tr><td style="word-wrap:break-word;"><span>'+result[i].student_name+'</span></td><td style="word-wrap:break-word;"><span>'+
            (result[i].sex==0?'男':'女')+'</span></td><td style="word-wrap:break-word;"><span>'+result[i].id_number+'</span></td><td style="word-wrap:break-word;"><span>'+
            result[i].middle_school+'</span></td><td style="word-wrap:break-word;"><span>'+
            (result[i].is_accommodate==0?'否':'是')+'</span></td></tr>';
        $('#studentList').append(li);
    }
}