var currentPage=1;
var limit=10;
var enroll_count;
var ratio;
//显示招生简章
function showContent(){
    var school_id=localStorage.getItem('school_id');
    $.myajax({
        url:"enrollAction/getAllRecruitList",
        datatype:"json",
        data:{school_id:school_id},
        success:function(data){
            var result=data.result.data;
            for (var i in result){
                var li;
                //正在报名状态
                if ('044001'==result[i].status)
                    li='<li class="zjq-applyList-detail"><h3>'+result[i].title+'</h3>' +
                        '<p class="zjq-apply-time">报名开始日期：'+result[i].apply_start_date+'</p>' +
                        '<p class="zjq-apply-time">报名截止日期：'+result[i].apply_end_date+'</p>' +
                        '<p class="zjq-applied-num">' +
                        '<span class="glyphicon glyphicon-user zjq-userIcon" aria-hidden="true"></span> 已报名：'+
                        result[i].count+'人</p>' +
                        '<a class="zjq-apply-status zjq-apply-detailBtn" href="recruitStudent/recruitDetail.jsp?recruit_id='+result[i].recruit_id+'&title='+result[i].title+'">查看招生简章</a>' +
                        '<a class="zjq-apply-status" href="recruitStudent/enrollStatus.jsp?recruit_id='+result[i].recruit_id+'&title='+result[i].title+'">查看报名情况</a></li>';
                else
                    li='<li class="zjq-applyList-detail" style="background: rgba(28,187,158,.2);"><h3>'+
                        result[i].title+'</h3><p class="zjq-apply-time">录取完成时间：'+result[i].completion_date
                        +'</p><p class="zjq-applied-num">' +
                        '<span class="glyphicon glyphicon-user zjq-userIcon" aria-hidden="true"></span> 报名：'+
                        result[i].count+'人 &nbsp;&nbsp;实际录取：'+result[i].enroll_count+'人</p>' +
                        '<a class="zjq-apply-status zjq-apply-detailBtn" href="recruitStudent/recruitDetail.jsp?recruit_id='+result[i].recruit_id+'&title='+result[i].title+'">' +
                        '查看招生简章</a><a class="zjq-apply-status" href="recruitStudent/admissionList.jsp?recruit_id='+result[i].recruit_id+'&title='+result[i].title+'">' +
                        '查看录取情况</a></li>'
                $('#enrollment').append(li);
            }
        }
    });
}

//显示录取率，报名人数，录取人数，男女比例
function showEnrollStatus(){
    $.myajax({
        url:'enrollAction/getEnrollStatus',
        datatype:'json',
        data:{recruit_id:recruit_id,enroll_status:'043002'},
        success:function(data){
            var result=data.result.data;
            $('#status').text(result.acceptance_rate+'%');
            $('#count').text('报名总人数：'+result.count);
            $('#enrollCount').text('录取人数：'+result.enroll_count);
            $('#ratio').text('男女比例：'+result.ratio);
            enroll_count=result.enroll_count;
            ratio=result.ratio;
        }
    });
}

//显示报名的学生
function showEnrollPeople(){
    $.myajax({
        url:'enrollAction/getStudentList',
        datatype:'json',
        data:{recruit_id:recruit_id,enroll_status:enroll_status,start_id:(currentPage-1)*limit,limit:limit,page:currentPage},
        success:function(data){
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
            showEnrollPeople();
        }
    };
    $("#page_pagintor").bootstrapPaginator(options);
    $("#page_pagintor").show();
}

function addSToWeb(data) {
    $('#studentList').empty();
    var result=data.result.data;
    if(''==enroll_status) {//报名情况页
        for (var i in result) {
            var li;
            //已录取
            if ('043002' == result[i].enroll_status)
                li = '<dd><img src="' + result[i].head_url + '" alt="" />' + result[i].student_name +
                    '<div class="zjq-detailBtns"><a value="'+result[i].id+'" class="zjq-greenBtn zjq-checkDetail">查看详情</a>' +
                    '<div style="display: inline;">' +
                    '<a class="zjq-greenBtn zjq-applyselBtn zjq-admittedBtn" style="display: inline-block;">已录取</a></div></div></dd>';
            else
                li = '<dd><img src="' + result[i].head_url + '" alt="" />' + result[i].student_name +
                    '<div class="zjq-detailBtns"><a value="'+result[i].id+'" class="zjq-greenBtn zjq-checkDetail">查看详情</a>' +
                    '<div style="display: inline;">' +
                    '<a value="'+result[i].id+'" class="zjq-greenBtn zjq-applyselBtn zjq-admiting" style="display: inline-block;">录取</a></div></div></dd>';
            $('#studentList').append(li);
        }
        admission();
        studentDetail();
    } else {//录取情况页
        for (var i in result) {
            var li='<dd><img src="'+result[i].head_url+'" alt="" /><span>'+result[i].student_name+
                '</span>'+result[i].phone+'<div class="zjq-detailBtns">' +
                '<a class="zjq-greenBtn zjq-checkDetail" value="'+result[i].id+'">查看详情</a></div></dd>';
            $('#studentList').append(li);
        }
        studentDetail();
    }
}

//学生个人详情
function studentDetail(){
    $('.zjq-checkDetail').on('click',function(){
        var id=$(this).attr('value');
        $.myajax({
            url:'enrollAction/getStudentByID',
            datatype:'json',
            data:{id:id},
            success:function(data){
                var result=data.result.data;
                $('#student_name').text(result.student_name);
                if (result.sex==0) $('#sex').text('男');
                else if (result.sex==1) $('#sex').text('女');
                $('#id_number').html(result.id_number+'<img src="'+result.head_url+'" class="zjq-child-pic"/>');
                $('#middle').text(result.middle_school);
                $('#register').text(result.register_school);
                if (result.color_blindness==0) $('#color').text('否');
                else if (result.color_blindness==1) $('#color').text('是');
                $('#specialty').text(result.person_specialty);
                $('#award').text(result.award_situation);
                $('#parent').text(result.parent_name);
                $('#relation').text(result.relationship);
                $('#company').text(result.parent_company);
                $('#phone').text(result.phone);
                if (result.is_accommodate==0) $('#accommodate').text('否');
                else if (result.is_accommodate==1) $('#accommodate').text('是');
                if (result.enroll_status=='043002')
                    $('#admission').remove();
                $('#admission').val(result.id);
                $(".zjq-select-modal").show();
                admissionStudent();
            }
        });
    });
}

//录取学生
function admissionStudent() {
    $('#admission').on('click', function(){
        var id=$(this).val();
        $.myajax({
            url:'enrollAction/admission',
            datatype:'json',
            data:{id:id,enroll_status:'043002'},
            success:function(){
                $(".zjq-select-modal").hide();
                window.location.reload();
            }
        });
    });
}

//录取学生
function admission(){
    $('.zjq-admiting').on('click',function(){
        var id=$(this).attr('value');
        var obj=$(this);
        $.myajax({
            url:'enrollAction/admission',
            datatype:'json',
            data:{id:id,enroll_status:'043002'},
            success:function(){
                obj.replaceWith('<a class="zjq-greenBtn zjq-applyselBtn zjq-admittedBtn" style="display: inline-block;">已录取</a>');
                showEnrollStatus();
            }
        });
    });
}

//显示录取学生列表
function showEnrollingPeople(){
    $.myajax({
        url:'enrollAction/getStudentList',
        datatype:'json',
        data:{recruit_id:recruit_id,enroll_status:enroll_status,start_id:(currentPage-1)*limit,limit:limit,page:currentPage},
        success:function(data){
            page(data);
        }
    });
}

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
            showEnrollingPeople();
        }
    };
    $("#page_pagintor").bootstrapPaginator(options);
    $("#page_pagintor").show();
}

function addToWeb(data) {
    $('#studentList').empty();
    var result=data.result.data;
    for (var i in result){
        var li='<dd><img src="'+result[i].head_url+'" alt="" />'+result[i].student_name+
            '<div class="zjq-detailBtns"><a class="zjq-greenBtn zjq-checkDetail" value="'+result[i].id+'">查看详情</a>' +
            '<a class="zjq-greenBtn zjq-admittedBtn cancelAdmission" value="'+result[i].id+'">取消录取</a></div></dd>';
        $('#studentList').append(li);
    }
    studentDetail();
    cancelAdmission();
}

//取消录取
function cancelAdmission(){
    $('.cancelAdmission').on('click',function(){
        var id=$(this).attr('value');
        var obj=$(this).parent().parent();
        $.myajax({
            url:'enrollAction/admission',
            datatype:'json',
            data:{id:id,enroll_status:'043001'},
            success:function(){
                obj.remove();
                showEnrollStatus();
            }
        });
    });
}

//生成正式录取名单
function createAdmission(){
    $(".zjq-createBtn .btn").click(function(){
        $.myajax({
            url:'enrollAction/completeEnroll',
            datatype:'json',
            data:{recruit_id:recruit_id,status:'044002'},
            success:function (data){
                window.location.href="recruitStudent/admissionList.jsp?recruit_id="+recruit_id+"&title="+title;
            }
        });
    });
}

//招生简章详情
function showDetail() {
    $.myajax({
        url:'enrollAction/getRecruitByID',
        datatype:'json',
        data:{recruit_id:recruit_id},
        success:function(data){
            var result=data.result.data;
            $('#recruitContent').html(result.content);
            $('#textarea1').html(result.content);
            var date=getDateStr(new Date(),'day');
            var start_date=getDateStr(result.apply_start_date,'day');
            //未开始报名才能修改简章
            if (date > start_date)
                $('#edit').remove();
            if ('044001'==result.status){
                $('#status').append('<a class="zjq-selBtn" href="recruitStudent/enrollStatus.jsp?recruit_id='+result.recruit_id+'&title='+result.title+'">查看报名情况</a>');
            } else {
                $('#status').append('<a class="zjq-selBtn" href="recruitStudent/admissionList.jsp?recruit_id='+result.recruit_id+'&title='+result.title+'">查看录取情况</a>');
            }
            // var textarea1=$("#textarea1");
            // var editor = new wangEditor('textarea1');
            // editor.create();//富文本编辑器
        }
    });
}

//更新招生简章
function updateDetail() {
    $.myajax({
        url:'enrollAction/getRecruitByID',
        datatype:'json',
        data:{recruit_id:recruit_id},
        success:function(data){
            var result=data.result.data;
            $('#textarea1').html(result.content);
            var textarea1=$("#textarea1").html();
            var editor = new wangEditor('textarea1');
            editor.config.uploadImgUrl = localStorage.getItem('file_upload_action');
            editor.config.menus = [
                'bold',
                'underline',
                'italic',
                'strikethrough',
                'eraser',
                'forecolor',
                'bgcolor',
                'quote',
                'fontfamily',
                'fontsize',
                'head',
                'unorderlist',
                'orderlist',
                'alignleft',
                'aligncenter',
                'alignright',
                'link',
                'unlink',
                'table',
                'emotion',
                'img',
                'undo',
                'redo',
                'fullscreen'
            ];
            editor.config.withCredentials = false;
            editor.create();
            $('#input4').val(result.title);
            $('#start_date').val(result.apply_start_date);
            $('#end_date').val(result.apply_end_date);
        }
    });
}

//新建招生简章
function checkRelease(){
    $('#checkRelease').on('click',function(){
        var title=$('#input4').val();
        var content=$('.wangEditor-txt').html();
        var apply_start_date=$('#start_date').val();
        var apply_end_date=$('#end_date').val();
        $.myajax({
            url:'enrollAction/addRecruit',
            type: 'POST',
            datatype:'json',
            data:{title:title,content:content,apply_start_date:apply_start_date,apply_end_date:apply_end_date,status:'044001'},
            success:function (data){
                window.location.href="recruitStudent/enrollmentList.jsp";
            }
        });
    });
}

//保存招生简章
function saveRecruit() {
    $('#update').on('click',function () {
        var title=$('#input4').val();
        var content=$('.wangEditor-txt').html();
        var apply_start_date=$('#start_date').val();
        var apply_end_date=$('#end_date').val();
        $.myajax({
            url:'enrollAction/updateRecruit',
            type: 'POST',
            datatype:'json',
            data:{title:title,content:content,apply_start_date:apply_start_date,apply_end_date:apply_end_date,
                recruit_id:recruit_id},
            success:function (data) {
            }
        });
    });
}